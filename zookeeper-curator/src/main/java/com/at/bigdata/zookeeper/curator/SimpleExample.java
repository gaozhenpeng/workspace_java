package com.at.bigdata.zookeeper.curator;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleExample {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            log.error("USAGE: {} <hostPort> <namespace> <setValuePath>", SimpleExample.class.getSimpleName());
            System.exit(2);
        }
        String hostPort = args[0]; //   127.0.0.1:2181
        String namespace = args[1]; //   zookeeper_curator
        String setValuePath = args.length >= 3 ? args[2] : null; //   /testpath
        
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        
        CuratorFramework curatorFramework = 
                    CuratorFrameworkFactory.builder()
                        .connectString(hostPort)
                        // session timeout, default 60000, must equal to or greater than connection timeout
                        .sessionTimeoutMs(5000)
                        // connection timeout, default 60000, must equal to or less than session timeout
                        .connectionTimeoutMs(3000)
                        .retryPolicy(retryPolicy)
                        .namespace(namespace)
                        .build();
        curatorFramework.start();
        
        
        if (curatorFramework.checkExists().forPath(setValuePath) == null) {
            log.info("create path: '{}'", setValuePath);
//            // close the executor at the end of the program
//            // spring's executor pool is prefered
//            Executor executor = Executors.newFixedThreadPool(2);
            curatorFramework
                .create()
                .creatingParentsIfNeeded()
                // create mode, default: CreateMode.PERSISTENT
                .withMode(CreateMode.EPHEMERAL)
                // create acl setting, default: Ids.OPEN_ACL_UNSAFE
                .withACL(Ids.OPEN_ACL_UNSAFE)
                // asynchronous operation
//                .inBackground(
//                        (curaterFramework, curaterEvent) -> {
//                            log.info("eventType: '{}', resultCode: '{}'", curaterEvent.getType(), curaterEvent.getResultCode());
//                        }
////                        ,executor
//                )
                .forPath(setValuePath)
                ;
        }
        
        
        
        
        setValues(curatorFramework, setValuePath);
        
        // transaction
        curatorFramework.transaction().forOperations(
                curatorFramework.transactionOp().create().withMode(CreateMode.EPHEMERAL).forPath("/transactionpath1")
                ,curatorFramework.transactionOp().create().withMode(CreateMode.EPHEMERAL).forPath("/transactionpath2","data2".getBytes())
                ,curatorFramework.transactionOp().setData().forPath("/transactionpath1","data1".getBytes())
//                ,curatorFramework.transactionOp().delete().forPath("/transactionpathx")
                );
        
        
        travelByPreorder(curatorFramework, "/", 0);

        log.info("delete path: '{}'", setValuePath);
        curatorFramework
            .delete()
            .deletingChildrenIfNeeded()
//            .withVersion(curatorFramework.checkExists().forPath(zkPath).getVersion())
            .forPath(setValuePath)
            ;

        travelByPreorder(curatorFramework, "/", 0);
    }

    public static void setValues(CuratorFramework curatorFramework, String zkPath) throws Exception{
        log.info("set values, curatorFramework: '{}', zkPath: '{}'", curatorFramework, zkPath);
        Objects.requireNonNull(curatorFramework, "curatorFramework should not be null");
        Objects.requireNonNull(zkPath, "zkPath should not be null");
        
        byte[] setData = "{\"data\" : \"fox\"}".getBytes("UTF-8");
        log.info("setData string: '{}'.", new String(setData, "UTF-8"));
        log.debug("setData bytes: '{}'.", setData);
        
        curatorFramework
            .setData()
            .forPath(zkPath, setData)
//            .setVersion(curatorFramework.checkExists().forPath(zkPath).getVersion())
            ;

        Stat getDataStat = new Stat();
        int hashCode = getDataStat.hashCode();
        byte[] getData = 
                curatorFramework
                    .getData()
                    .storingStatIn(getDataStat)
                    .forPath(zkPath);
        
        log.info("getData string: '{}'.", new String(getData, "UTF-8"));
        log.debug("getData bytes: '{}'.", getData);
        
        assert hashCode != getDataStat.hashCode() : "Stat is expected to be changed.";
        assert getData != null 
                && new String(getData, "UTF-8").equals(new String(setData, "UTF-8"))
                : "getData is null or does not equals to setData";
                
    }

    public static void travelByPreorder(CuratorFramework curatorFramework, String zkPath, int level) throws Exception {
        log.info("set values, curatorFramework: '{}', zkPath: '{}'", curatorFramework, zkPath);
        Objects.requireNonNull(curatorFramework, "curatorFramework should not be null");

        if (zkPath == null || curatorFramework.checkExists().forPath(zkPath) == null) {
            log.warn("zkPath '{}' does not exist.", zkPath);
            return;
        }

        StringBuilder indent = new StringBuilder();
        for(int i = 0 ; i < level + 1 ; i++) {
            indent.append("\t");
        }
        
        // preorder, deal with the path
        log.info(zkPath);
        byte[] data = curatorFramework.getData().forPath(zkPath);
        if (null != data) {
            log.info("{}data string: '{}'", indent, new String(data, "UTF-8"));
            log.debug("{}data bytes: '{}'", indent, data);
        }

        List<String> children = curatorFramework.getChildren().forPath(zkPath);

        for (String c : children) {
            String path = zkPath.replaceAll("/+$", "") + "/" + c;
            travelByPreorder(curatorFramework, path, level + 1);
        }
    }
}
