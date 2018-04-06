package com.at.bigdata.zookeeper.curator.recipe;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaderSelectorDemo {
    private static final String SELECTOR_PATH = "/selector_path";
    
    public static class LeaderSelectorAdapter extends LeaderSelectorListenerAdapter implements Closeable {
        private final String name;
        private final LeaderSelector leaderSelector;
        private final AtomicInteger leaderCount = new AtomicInteger();

        public LeaderSelectorAdapter(CuratorFramework client, String path, String name) {
            this.name = name;
            leaderSelector = new LeaderSelector(client, path, this);
            leaderSelector.autoRequeue();
        }

        public void start() throws IOException {
            leaderSelector.start();
        }

        @Override
        public void close() throws IOException {
            leaderSelector.close();
        }

        @Override
        public void takeLeadership(CuratorFramework client) throws Exception {
            final int waitSeconds = (int) (5 * Math.random()) + 1;
            log.info("'{}' is now the leader. Waiting {} seconds...", name, waitSeconds);
            log.info("'{}' has been a leader {} time(s) before.", name, leaderCount.getAndIncrement());
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
            } catch (InterruptedException e) {
                log.error("{} was interrupted.", name, e);
                Thread.currentThread().interrupt();
            } finally {
                log.info("{} relinquishing leadership.", name);
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        List<CuratorFramework> clients = Lists.newArrayList();
        List<LeaderSelectorAdapter> examples = Lists.newArrayList();
        TestingServer server = new TestingServer();
        try {
            for (int i = 0; i < 10; i++) {
                // client
                CuratorFramework client
                        = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(20000, 3));
                clients.add(client);
                
                LeaderSelectorAdapter selectorAdapter = new LeaderSelectorAdapter(client, SELECTOR_PATH, "Client #" + i);
                examples.add(selectorAdapter);
                
                // start client
                client.start();
                // start adapter
                selectorAdapter.start();
            }
            log.info("Press enter/return to quit");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } finally {
            log.info("Shutting down...");
            for (LeaderSelectorAdapter exampleClient : examples) {
                CloseableUtils.closeQuietly(exampleClient);
            }
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }
            CloseableUtils.closeQuietly(server);
        }
    }

}
