package com.at.log4j2.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionFactory {
    private static final DataSource DATASOURCE;
    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(
                "jdbc:mysql://127.0.0.1:3306/log4j2?useSSL=false&serverTimezone=UTC&nullCatalogMeansCurrent=true");
        hikariConfig.setUsername("log4j2");
        hikariConfig.setPassword("log4j2");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        DATASOURCE = new HikariDataSource(hikariConfig);
    }

    public static Connection getDatabaseConnection() throws SQLException {
        return DATASOURCE.getConnection();
    }
}
