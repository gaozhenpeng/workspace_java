package com.at.log4j2.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ConnectionFactory {
	private static interface Singleton {
		final ConnectionFactory INSTANCE = new ConnectionFactory();
	}

	private final DataSource dataSource;

	private ConnectionFactory() {
		Properties properties = new Properties();
		properties.setProperty("user", "log4j2");
		properties.setProperty("password", "log4j2"); // or get properties from some configuration file

		DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				"jdbc:mysql://127.0.0.1:3306/log4j2?useSSL=false", properties);
		GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
		genericObjectPoolConfig.setMaxIdle(10);
		genericObjectPoolConfig.setMinIdle(3);
		genericObjectPoolConfig.setMaxWaitMillis(120000); // 120s
		
		GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>(
				new PoolableConnectionFactory(connectionFactory, null)
				,genericObjectPoolConfig
				);

		this.dataSource = new PoolingDataSource<PoolableConnection>(pool);
	}

	public static Connection getDatabaseConnection() throws SQLException {
		return Singleton.INSTANCE.dataSource.getConnection();
	}
}
