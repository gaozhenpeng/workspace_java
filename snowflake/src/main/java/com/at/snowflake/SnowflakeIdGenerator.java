package com.at.snowflake;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * 0 - 12345678 12345678 12345678 12345678 12345678 1 - 12345678 12 - 12345678 1234
 *   * 0
 *       unused
 *   * 12345678 12345678 12345678 12345678 12345678 1
 *       41 bits of timestamp in miliseconds
 *          System.currentTimeMillis: 64bits
 *       2^41 = 2199023255552, 69years
 *       It can set to be the eliminating time since a start point
 *   * 12345678 12
 *       10 bits of machine id
 *          5 bits for data center id, 5 bits for machine id
 *       2^10 = 1024
 *   * 12345678 1234
 *       12 bits of lastSequenceNum id
 *          2^12 = 4096
 */
public class SnowflakeIdGenerator {
    
    /**
     * start miliseconds (2018-01-01 00:00:00.000 = 1514736000000)
     */
    private final long START_TIME_IN_MS = 1514736000000L;

    private final long MACHINE_ID_BITS = 5L;
    private final long DATACENTER_ID_BITS = 5L;
    private final long SEQUENCE_BITS = 12L;

    private final long MACHINE_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;
    private final long DATACENTER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS + MACHINE_ID_BITS;
    private final long TIMESTAMP_LEFT_SHIFT_BITS = SEQUENCE_BITS + MACHINE_ID_BITS + DATACENTER_ID_BITS;

    /** 0b1111 1111 1111 = 0xFFF */
    private final long SEQUENCE_MASK = 0xFFF;
    /** 0b1 1111 = 0x1F */
    private final long WORKER_ID_MASK = 0x1F;
    /** 0b1 1111 = 0x1F */
    private final long DATA_CENTER_ID_MASK = 0x1F;
    /** 0b 1 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 = 0x1FFFFFFFFFF */
    private final long TIMESTAMP_MASK = 0x1FFFFFFFFFFL;
    
    
    /** lock for sequential */
    private static final ReentrantLock reentrantLock = new ReentrantLock();
    

    
    
    private long dataCenterId;
    private long workerId;
    
    private long lastSequenceNum = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator(long dataCenterId, long workerId) {
        super();
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    public long nextId() throws InterruptedException {
        reentrantLock.lock();
        long thisTimestamp = timeGen();
        long thisSequenceNum = lastSequenceNum;
        if (lastTimestamp == thisTimestamp) {
            thisSequenceNum = (thisSequenceNum + 1) & SEQUENCE_MASK;
            // overflow
            if (thisSequenceNum == 0) {
                thisTimestamp = tilNextMillis(thisTimestamp);
            }
        } else {
            thisSequenceNum = 0L;
        }
        lastSequenceNum = thisSequenceNum;
        lastTimestamp = thisTimestamp;
        reentrantLock.unlock();
        
        return (((thisTimestamp - START_TIME_IN_MS) & TIMESTAMP_MASK)      << TIMESTAMP_LEFT_SHIFT_BITS)
                | ((dataCenterId                    & DATA_CENTER_ID_MASK) << DATACENTER_ID_LEFT_SHIFT_BITS)
                | ((workerId                        & WORKER_ID_MASK)      << MACHINE_ID_LEFT_SHIFT_BITS)
                | (thisSequenceNum                  & SEQUENCE_MASK);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = -1;
        do{
            timestamp = timeGen();
        }while(timestamp <= lastTimestamp);
        
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
    
    public static void main(String[] args) throws InterruptedException {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(123, 456);
        
        List<Future<Long>> futureIds = new ArrayList<>(10000);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0 ; i < 1000000 ; i++) {
            futureIds.add(executorService.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return snowflakeIdGenerator.nextId();
                }
                
            }));
        }
        AtomicInteger counter = new AtomicInteger();
        ConcurrentMap<Long, Long> id2Count = new ConcurrentHashMap<>(10000);
        futureIds.stream().parallel()
            .forEach(f->{
                try {
                    long id = f.get();
                    id2Count.computeIfPresent(id, (k,v)->{
                        counter.incrementAndGet(); 
                        v++; 
                        return v;
                    });
                    id2Count.putIfAbsent(id, 1L);
                } catch (Exception e) {
                    log.error("err", e);
                }
                
            });
        log.error("Shit total: '{}'", counter.get());
        executorService.shutdown();
    }
}
