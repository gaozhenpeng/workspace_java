package com.at.bigdata.zookeeper.curator.recipe;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaderLatchDemo {
    private static final String LATCH_PATH = "/latch_path";
    
    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        List<CuratorFramework> curatorFrameworks = Lists.newArrayList();
        List<LeaderLatch> leaderLatches = Lists.newArrayList();
        TestingServer server=new TestingServer();
        try {
            for (int i = 0; i < 10; i++) {
                // client
                CuratorFramework client
                        = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(20000, 3));
                curatorFrameworks.add(client);
                
                // leader latch
                LeaderLatch latch = new LeaderLatch(client, LATCH_PATH, "Client #" + i);
                latch.addListener(new LeaderLatchListener() {
                    @Override
                    public void isLeader() {
                        log.info("I am Leader");
                    }
                    @Override
                    public void notLeader() {
                        log.info("I am not Leader");
                    }
                });
                leaderLatches.add(latch);
                
                // start client
                client.start();
                // start latch
                latch.start();
            }
            // let the election happen
            Thread.currentThread().sleep(10000);
            
            // search for the leader
            LeaderLatch currentLeader = null;
            for (LeaderLatch latch : leaderLatches) {
                if (latch.hasLeadership()) {
                    currentLeader = latch;
                }
            }
            log.info("current leader: '{}'.", currentLeader.getId());
            
            // close the current leader
            log.info("releasing the leader: '{}'.", currentLeader.getId());
            leaderLatches.remove(currentLeader);
            currentLeader.close();

            // let the election happen again
            Thread.currentThread().sleep(3000);

            // search for the leader again
            for (LeaderLatch latch : leaderLatches) {
                if (latch.hasLeadership()) {
                    currentLeader = latch;
                }
            }
            log.info("current leader: '{}'.", currentLeader.getId());
            
            
        } finally {
            for (LeaderLatch latch : leaderLatches) {
                if (latch.getState() != null) {
                    log.info("Closing latch '{}'", latch.getId());
                    CloseableUtils.closeQuietly(latch);
                }
            }
            for (CuratorFramework client : curatorFrameworks) {
                log.info("Closing client");
                CloseableUtils.closeQuietly(client);
            }
            
            CloseableUtils.closeQuietly(server);
        }
    }
}
