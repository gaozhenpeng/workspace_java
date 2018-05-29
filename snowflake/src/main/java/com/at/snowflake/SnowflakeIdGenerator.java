package com.at.snowflake;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * 0 - 12345678 12345678 12345678 12345678 12345678 1 - 12345678 12 - 12345678 1234
 *   * 0
 *      unused
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
    
    public SnowflakeIdGenerator(long dataCenterId, long workerId) {
        super();
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * start miliseconds (2015-01-01)
     */
    private final long START_TIME_IN_MS = 1420041600000L;

    private final long MACHINE_ID_BITS = 5L;
    private final long DATACENTER_ID_BITS = 5L;
    private final long SEQUENCE_BITS = 12L;

    private final long MACHINE_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;
    private final long DATACENTER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS + MACHINE_ID_BITS;
    private final long TIMESTAMP_LEFT_SHIFT_BITS = SEQUENCE_BITS + MACHINE_ID_BITS + DATACENTER_ID_BITS;

    /** 0b1111 1111 1111 = 0xFFF */
    private final long SEQUENCE_MASK = 0xFFF;
    
    
    private long dataCenterId;
    private long workerId;
    
    private long lastSequenceNum = 0L;
    private long lastTimestamp = -1L;
    
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public long nextId() throws InterruptedException {
        long thisTimestamp = timeGen();
        long thisSequenceNum = lastSequenceNum;
        
        reentrantLock.tryLock(1, TimeUnit.SECONDS);
        thisSequenceNum = (thisSequenceNum + 1) & SEQUENCE_MASK;
        if (thisSequenceNum == 0 && thisTimestamp == lastTimestamp) {
            thisTimestamp = tilNextMillis();
        }
        lastSequenceNum = thisSequenceNum;
        lastTimestamp = thisTimestamp;
        reentrantLock.unlock();

        return ((thisTimestamp - START_TIME_IN_MS) << TIMESTAMP_LEFT_SHIFT_BITS) | (dataCenterId << DATACENTER_ID_LEFT_SHIFT_BITS) | (workerId << MACHINE_ID_LEFT_SHIFT_BITS) | thisSequenceNum;
    }

    protected long tilNextMillis() {
        long timestamp = -1;
        do{
            timestamp = timeGen();
        }while(timestamp <= lastTimestamp);
        
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }
    
    public static void main(String[] args) throws InterruptedException {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(123, 456);
        for(int i = 0 ; i < 100 ; i++) {
            log.info("id: '{}'", snowflakeIdGenerator.nextId());
        }
    }
}
