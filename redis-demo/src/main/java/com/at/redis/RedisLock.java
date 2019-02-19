package com.at.redis;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

public class RedisLock {
	private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

	public static String acquireLock(Jedis jedis, String lockNamePrefix, long acquireTimeoutInMillisecond) {
		String identifier = null;
//		identifier = UUID.randomUUID().toString(); // SecureRandom, slow
		Random random = new Random();
		identifier = new UUID(random.nextLong(), random.nextLong()).toString();
		
		
		String lockName = lockNamePrefix + "_lock";
		long end = System.currentTimeMillis() + acquireTimeoutInMillisecond;
		while (System.currentTimeMillis() < end) {
			if (1L == jedis.setnx(lockName, identifier).longValue()) {
				logger.info("Lock acquired, [lockName:{}].", lockName);
				jedis.expire(lockName, 5);
				return identifier;
			} else if (jedis.ttl(lockName) < 0) {
				jedis.expire(lockName, 5);
			}
		}
		logger.info("Acquiring lock failed, [lockName:{}].", lockName);
		return null;
	}

	public static boolean releaseLock(Jedis jedis, String lockNamePrefix, String identifier) {
		if (null == jedis || null == lockNamePrefix || "".equals(lockNamePrefix) || null == identifier
				|| "".equals(identifier)) {
			return false;
		}
		String lockName = lockNamePrefix + "_lock";
		while (true) {
			try {
				jedis.watch(lockName);
				if (identifier.equals(jedis.get(lockName))) {
					try (Transaction transaction = jedis.multi()) {
						transaction.del(lockName);
						transaction.exec();
						logger.info("Lock released, [lockName:{}].", lockName);
						return true;
					}
				}
				jedis.unwatch();
				break;
			} catch (Exception e) {
				logger.warn("Releasing lock failed, [lockName:{}].", lockName, e);
			}
		}
		return false;
	}
	
	

	private static final String redis_hostname = "localhost";
	private static final Integer redis_port = 6379;
	private static final Integer redis_conn_timeout_in_ms = 2000;
	private static final Integer redis_pool_minidle = 1;
	private static final Integer redis_pool_maxidle = 10;
	private static final Integer redis_pool_total = 128;
	private static final String redis_password = "masterpassword";
	
	public static void main(String[] args){
		logger.info("Connecting to "+redis_hostname+":"+redis_port+"");
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMinIdle(redis_pool_minidle);
		jedisPoolConfig.setMaxIdle(redis_pool_maxidle);
		jedisPoolConfig.setMaxTotal(redis_pool_total);
		JedisPool jedispool = new JedisPool(jedisPoolConfig, redis_hostname, redis_port, redis_conn_timeout_in_ms);

		Jedis jedis = null;
//		jedis = new Jedis(redis_hostname, redis_port, redis_conn_timeout_in_ms); // direct connection setting
		jedis = jedispool.getResource();
		
		String authResult = jedis.auth(redis_password);
		logger.info("auth result: '" + authResult + "'");
		
		String myResourceLockPrefix = "myresource";
		String lockId = null;
		long acquireTimeoutInMillisecond = 5000;
		try{
			logger.info("acquiring lock.");
			lockId = RedisLock.acquireLock(jedis, myResourceLockPrefix, acquireTimeoutInMillisecond);
			//do something with "myresource";
			logger.info("do something with 'myresource' ........................");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			logger.warn("Sleepping was interuptted", e);
		}finally{
			logger.info("releasing lock.");
			RedisLock.releaseLock(jedis, myResourceLockPrefix, lockId);
		}
		
		jedis.close();
		jedispool.close();
		jedispool.destroy();
	}
}