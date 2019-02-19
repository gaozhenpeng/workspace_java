package com.at.spring_data_redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

	@Bean
	public JedisConnectionFactory jedisConnectionFactory(){
		logger.debug("jedisConnectionFactory() begin");
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMinIdle(3); // 3
		jedisPoolConfig.setMaxTotal(10); // 10
		jedisPoolConfig.setMaxWaitMillis(1000); // 1s
		
		JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling().poolConfig(jedisPoolConfig).build();
		
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName("127.0.0.1");
		redisStandaloneConfiguration.setPort(6379); // default: 6379
		redisStandaloneConfiguration.setPassword(RedisPassword.of("masterpassword"));
		redisStandaloneConfiguration.setDatabase(0); // 0 should be used by default.
		
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
		return jedisConnectionFactory;
	}
	
	@Bean
	public RedisTemplate redisTemplate(){
		logger.debug("redisTemplate() begin");
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}
	
}
