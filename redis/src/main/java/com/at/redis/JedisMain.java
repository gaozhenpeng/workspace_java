package com.at.redis;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisMain {
	private static final Logger logger = LoggerFactory.getLogger(JedisMain.class);
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
		

		logger.info("Keys in repository: ");
		Set<String> keys = jedis.keys("*");
		keys.forEach((key) -> logger.info("\t" + key));
		

		logger.info("Setting 'testkey' to 'testvalue' in the repository.");
		jedis.set("testkey", "testvalue");
		logger.info("set if not exist: 'testkey' to 'testvalue2' -> failed ");
		jedis.setnx("testkey", "testvalue2");
		assert "testvalue".equals(jedis.get("testkey")) : "the value of 'testkey' is expected to be 'testvalue'";
		
		logger.info("set if not exist: 'testkey2' to 'testvalue2' -> success ");
		jedis.setnx("testkey2", "testvalue2");
		assert "testvalue2".equals(jedis.get("testkey2")) : "the value of 'testkey2' is expected to be 'testvalue2'";
		
		logger.info("expire testkey2 within 1 sec");
		jedis.expire("testkey2", 1);
		
		String v = jedis.get("testkey");
		logger.info("Value of 'testkey': '" + v + "'");
		
		Long del_res = jedis.del("testkey");
		logger.info("res of deleting: '" + del_res + "'");

		String v_after_deletion = jedis.get("testkey");
		logger.info("after deletion: '" + v_after_deletion + "'");

		
		jedis.close();
		jedispool.close();
		jedispool.destroy();
	}
}
