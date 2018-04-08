package com.at.bigdata.zookeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleExample {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        if (args.length < 3) {
            log.error("USAGE: {} <hostPort> <travalPath> <setValuePath>", SimpleExample.class.getSimpleName());
            System.exit(2);
        }
        String hostPort = args[0]; //   127.0.0.1:2181
        String travalPath = args[1]; //   /zookeeper_native
        String setValuePath = args.length >= 3 ? args[2] : null; //   /zookeeper_native

        ZooKeeper zk = new ZooKeeper(hostPort, 3000, new SimpleWatcher());
        

        if (zk.exists(setValuePath, false) == null) {
            log.info("Create path: '{}'", setValuePath);
            zk.create(setValuePath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }
        
        setValues(zk, setValuePath);

        // transaction
        zk.transaction()
            .create("/transactionpath1", null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL)
            .create("/transactionpath2", "data2".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL)
            .setData("/transactionpath1", "data1".getBytes(), -1)
//            .delete("/transactionpathx", -1)
            .commit()
            ;
        
        
        travelByPreorder(zk, travalPath, 0);
        

        log.info("Delete path: '{}'", setValuePath);
        zk.delete(setValuePath
//                , zk.exists(setValuePath, false).getVersion()
                , -1
                );

        travelByPreorder(zk, travalPath, 0);
    }

    public static void setValues(ZooKeeper zk, String zkPath) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        log.info("set values, zk: '{}', zkPath: '{}'", zk, zkPath);
        Objects.requireNonNull(zk, "zk should not be null");
        Objects.requireNonNull(zkPath, "zkPath should not be null");
        
        byte[] setD = "{\"data\" : \"fox\"}".getBytes("UTF-8");
        log.info("setD string: '{}'.", new String(setD, "UTF-8"));
        log.debug("setD bytes: '{}'.", setD);
        zk.setData(zkPath, setD, zk.exists(zkPath, false).getVersion());

        Stat getDataStat = new Stat();
        int hashCode = getDataStat.hashCode();
        byte[] getD = zk.getData(zkPath, false, getDataStat);
        log.info("getD string: '{}'.", new String(getD, "UTF-8"));
        log.debug("getD bytes: '{}'.", getD);
        assert hashCode != getDataStat.hashCode() : "Stat is expected to be changed.";
        assert getD != null && new String(getD, "UTF-8")
                .equals(new String(setD, "UTF-8")) : "getD is null or does not equals to setD";

    }

    public static void travelByPreorder(ZooKeeper zk, String zkPath, int level) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        log.info("set values, zk: '{}', zkPath: '{}'", zk, zkPath);
        Objects.requireNonNull(zk, "zk should not be null");
        Objects.requireNonNull(zkPath, "zkPath should not be null");

        
        if (zk.exists(zkPath, false) == null) {
            log.warn("zkPath '{}' does not exist.", zkPath);
            return;
        }
        StringBuilder indent = new StringBuilder();
        for(int i = 0 ; i < level + 1 ; i++) {
            indent.append("\t");
        }
        // preorder, deal with the path
        log.info(zkPath);
        byte[] data = zk.getData(zkPath, false, null);
        if (null != data) {
            log.info("{}data string: '{}'", indent, new String(data, "UTF-8"));
            log.debug("{}data bytes: '{}'", indent, data);
        }

        List<String> children = zk.getChildren(zkPath, false);

        for (String c : children) {
            String path = zkPath.replaceAll("/+$", "") + "/" + c;
            travelByPreorder(zk, path, level + 1);
        }
    }
}
