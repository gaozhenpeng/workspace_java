package com.at.bigdata.zookeeper.curator.recipe;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/**
 * cache (watcher) demo
 */
@Slf4j
public class CacheDemo {
    private static String ZOOKEEPER_ADDRESS = "127.0.0.1:2181";
    private static String CURATOR_FRAMEWORK_NAMESPACE = "curator-cache";
    private static String CACHE_PATH = "/cache";
    private static String DATA_PATH = CACHE_PATH + "/data/node";
    
    private CuratorFramework curatorFramework = null;
    
    public void start() {
        log.info("starting client: '{}'", System.currentTimeMillis());
        // initialize curatorFramework
        curatorFramework = 
                    CuratorFrameworkFactory.builder()
                        .connectString(ZOOKEEPER_ADDRESS)
                        // session timeout, default 60000, must equal to or greater than connection timeout
                        .sessionTimeoutMs(5000)
                        // connection timeout, default 60000, must equal to or less than session timeout
                        .connectionTimeoutMs(3000)
                        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                        .namespace(CURATOR_FRAMEWORK_NAMESPACE)
                        .build();
        curatorFramework.start();
    }
    
    public void close() {
        log.info("closing client: '{}'", System.currentTimeMillis());
        CloseableUtils.closeQuietly(curatorFramework);
    }
    
    public void pathCacheNodeCache() throws Exception {
        log.info("pathCacheNodeCache: '{}'", System.currentTimeMillis());
        
        // PathChildrenCache
        @Cleanup PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, CACHE_PATH, true);
        pathChildrenCache.start();
        
        pathChildrenCache.getListenable().addListener((client, event) -> {
            // childEvent
            log.info("PathChildrenCache Event time: '{}'", System.currentTimeMillis());
            PathChildrenCacheEvent.Type eventType = event.getType();
            log.info("Event type: '{}'",  eventType);
            
            ChildData childData = event.getData();
            if(childData != null) {
                String path = childData.getPath();
                byte[] data = childData.getData();
                log.info("path: '{}', data: '{}'", path, new String(data, "UTF-8"));
            }
        });
        
        // NodeCache
        @Cleanup NodeCache nodeCache = new NodeCache(curatorFramework, DATA_PATH);
        nodeCache.start();
        nodeCache.getListenable().addListener(() -> {
            // nodeChanged
            log.info("NodeCache Event time: '{}'", System.currentTimeMillis());
            ChildData childData = nodeCache.getCurrentData();
            if(childData != null) {
                String path = childData.getPath();
                byte[] data = childData.getData();
                log.info("path: '{}', data: '{}'", path, new String(data, "UTF-8"));
            }
        });
        
        
        
        // operations
        
        log.info("Operating time: '{}'", System.currentTimeMillis());
        // PathChildrenCache: CHILD_ADDED
        // NodeCache: set data
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(DATA_PATH, "{\"name\":\"dataname\"}".getBytes("UTF-8"));
        Thread.sleep(3000);
        // PathChildrenCache: no event
        // NodeCache: update data
        curatorFramework.setData().forPath(DATA_PATH, "{\"name\":\"updatedName\"}".getBytes("UTF-8"));
        Thread.sleep(3000);
        // PathChildrenCache: no event
        // NodeCache: event without data
        curatorFramework.delete().deletingChildrenIfNeeded().forPath(DATA_PATH);
        Thread.sleep(3000);
        
    }
    
    public void treeCache() throws Exception {
        log.info("treeCache: '{}'", System.currentTimeMillis());
        
        
        // TreeNode
        @Cleanup TreeCache treeCache = new TreeCache(curatorFramework, DATA_PATH);
        treeCache.start();
        
        treeCache.getListenable().addListener((client, event) -> {

            log.info("TreeCache Event time: '{}'", System.currentTimeMillis());
            TreeCacheEvent.Type eventType = event.getType();
            log.info("Event type: '{}'",  eventType);
            
            ChildData childData = event.getData();
            if(childData != null) {
                String path = childData.getPath();
                byte[] data = childData.getData();
                log.info("path: '{}', data: '{}'", path, new String(data, "UTF-8"));
            }
        });
        
        
        
        
        // operations
        
        log.info("Operating time: '{}'", System.currentTimeMillis());
        // TreeCache: INITIALIZED, no data
        // TreeCache: NODE_ADDED, data
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(DATA_PATH, "{\"name\":\"dataname\"}".getBytes("UTF-8"));
        Thread.sleep(3000);
        // TreeCache: NODE_UPDATED, data
        curatorFramework.setData().forPath(DATA_PATH, "{\"name\":\"updatedName\"}".getBytes("UTF-8"));
        Thread.sleep(3000);
        // TreeCache: NODE_REMOVED, data
        curatorFramework.delete().deletingChildrenIfNeeded().forPath(DATA_PATH);
        Thread.sleep(3000);
    }
    
    public static void main(String[] args) throws Exception {
        CacheDemo cacheDemo = new CacheDemo();
        cacheDemo.start();
        cacheDemo.pathCacheNodeCache();
        cacheDemo.treeCache();
        cacheDemo.close();
    }
}
