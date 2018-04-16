package com.at.bigdata.zookeeper.curator.recipe;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

import lombok.extern.slf4j.Slf4j;

/** 
 * <p>Shared Reentrant Read Write Lock: InterProcessReadWriteLock, InterProcessMutex</p>
 * <p>The usage of <strong>org.apache.curator.framework.recipes.locks</strong> is just like the same class in <strong>java.util.concurrent.locks</strong></p>
 * <ul>
 *  <li>Shared: it can be seen globally.</li>
 *  <li>Reentrant: if a client obtained a lock, it can acquire the same lock again without blocking.</li>
 * </ul>
 */
@Slf4j
public class SharedReentrantReadWriteLock {
    private CuratorFramework curatorFramework;
    private String hostPort = "127.0.0.1:2181";
    private String namespace = "curator-locks";
    private InterProcessReadWriteLock readWriteLock = null;
    private InterProcessMutex readLock = null;
    private InterProcessMutex writeLock = null;
    
    public SharedReentrantReadWriteLock(){
        log.info("Initializing '{}'", SharedReentrantReadWriteLock.class.getSimpleName());
        curatorFramework = 
                    CuratorFrameworkFactory.builder()
                        .connectString(hostPort)
                        // session timeout, default 60000, must equal to or greater than connection timeout
                        .sessionTimeoutMs(5000)
                        // connection timeout, default 60000, must equal to or less than session timeout
                        .connectionTimeoutMs(3000)
                        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                        .namespace(namespace)
                        .build();
        curatorFramework.start();
        
        readWriteLock = new InterProcessReadWriteLock(curatorFramework, "/iprwl");
        
        // who held a read lock is not able to obtain a write lock unless itself held a write lock.
        readLock = readWriteLock.readLock();
        // who held a read lock is able to obtain a read lock
        writeLock = readWriteLock.writeLock();
    }
    
    
    public InterProcessMutex getReadLock() {
        return readLock;
    }
    
    public InterProcessMutex getWriteLock() {
        return writeLock;
    }
    
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        
        
        SharedReentrantReadWriteLock srrwl = new SharedReentrantReadWriteLock();
        
        InterProcessMutex readLock = srrwl.getReadLock();
        InterProcessMutex writeLock = srrwl.getWriteLock();
        
        executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                if(readLock != null) {
                    try {
                        if(readLock.acquire(2, TimeUnit.SECONDS)) {
                            log.info("do some read work here....");
                            Thread.sleep(1000);
                            log.info("done read work!");
                        }
                    }finally {
                        readLock.release();
                    }
                }
                return 100L;
            }
            
        });
        executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                if(writeLock != null) {
                    try {
                        if(writeLock.acquire(2, TimeUnit.SECONDS)) {
                            log.info("do some write work here....");
                            Thread.sleep(1000);
                            log.info("done write work!");
                        }
                    }finally {
                        writeLock.release();
                    }
                }
                return 100L;
            }
            
        });
        if(executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            log.info("terminated gracefully.");
        }
        log.info("shuting down executorService...");
        executorService.shutdown();
        if(!executorService.isShutdown()) {
            log.info("shuting down executorService now!");
            executorService.shutdownNow();
        }
    }
}
