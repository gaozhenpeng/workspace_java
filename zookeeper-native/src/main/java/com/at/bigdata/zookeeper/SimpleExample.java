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
            log.error("USAGE: {} <hostPort> <zkpath> <path4Setting>", SimpleExample.class.getSimpleName());
            System.exit(2);
        }
        String hostPort = args[0]; //   127.0.0.1:2181
        String zkPath = args[1]; //   /zookeeper
        String path4Setting = args.length >= 3 ? args[2] : null; //   /mytest

        ZooKeeper zk = new ZooKeeper(hostPort, 3000, new SimpleWatcher());
        setValues(zk, path4Setting);
        travelByPreorder(zk, zkPath);

//        Stat stat = zk.exists(args[1], false);
//        assert stat != null : "The root dir should have been already existed.";
//
//        List<String> children = zk.getChildren(zkPath, false);
//
//        for (String c : children) {
//            log.info("\t{}\n", c);
//        }
    }

    public static void setValues(ZooKeeper zk, String zkPath) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        log.info("set values, zk: '{}', zkPath: '{}'", zk, zkPath);
        Objects.requireNonNull(zk, "zk should not be null");
        Objects.requireNonNull(zkPath, "zkPath should not be null");
        
        if (zk.exists(zkPath, false) == null) {
            zk.create(zkPath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }
        byte[] setD = "{\"data\" : \"fox\"}".getBytes("UTF-8");
        log.info("setD: '{}'.", new String(setD, "UTF-8"));
        log.debug("setD: '{}'.", setD);
        zk.setData(zkPath, setD, zk.exists(zkPath, false).getVersion());

        Stat getDataStat = new Stat();
        int hashCode = getDataStat.hashCode();
        byte[] getD = zk.getData(zkPath, false, getDataStat);
        log.info("getD: '{}'.", new String(getD, "UTF-8"));
        log.debug("getD: '{}'.", getD);
        assert hashCode != getDataStat.hashCode() : "Stat is expected to be changed.";
        assert getD != null && new String(getD, "UTF-8")
                .equals(new String(setD, "UTF-8")) : "getD is null or does not equals to setD";

        //		zk.delete(zkPath, zk.exists(zkPath, false).getVersion());
    }

    public static void travelByPreorder(ZooKeeper zk, String zkPath) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        log.info("set values, zk: '{}', zkPath: '{}'", zk, zkPath);
        Objects.requireNonNull(zk, "zk should not be null");
        Objects.requireNonNull(zkPath, "zkPath should not be null");

        if (zk.exists(zkPath, false) == null) {
            log.warn("zkPath '{}' does not exist.", zkPath);
            return;
        }
        // preorder, deal with the path
        log.info(zkPath);
        byte[] data = zk.getData(zkPath, false, null);
        if (null != data) {
            log.info("\tdata: '{}'", new String(data, "UTF-8"));
            log.debug("\tdata: '{}'", data);
        }

        List<String> children = zk.getChildren(zkPath, false);

        for (String c : children) {
            String path = zkPath.replaceAll("/+$", "") + "/" + c;
            travelByPreorder(zk, path);
        }
    }
}
