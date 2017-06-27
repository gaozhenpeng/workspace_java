package com.at.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLiteMain {
	private static final Logger logger = LoggerFactory.getLogger(SQLiteMain.class);

	public void featured() throws SQLException {
		Connection connTest = DriverManager.getConnection("jdbc:sqlite:./db/test.db");
		Statement stat = connTest.createStatement();
		// cleanup
		stat.executeUpdate("drop table if exists TableName;");

		// create table
		stat.executeUpdate("create table TableName (Name, ItemCount);");

		//
		PreparedStatement prep = connTest.prepareStatement("insert into TableName values (?, ?);");
		// 写入数据目录
		prep.setString(1, "Name1");
		prep.setString(2, "1");
		prep.addBatch();
		prep.setString(1, "Name2");
		prep.setString(2, "2");
		prep.addBatch();
		prep.setString(1, "Name3");
		prep.setString(2, "3");
		prep.addBatch();

		connTest.setAutoCommit(false);
		prep.executeBatch();
		connTest.setAutoCommit(true);

		ResultSet rs = stat.executeQuery("select * from TableName;");
		while (rs.next()) {
			logger.info("Name = " + rs.getString("Name"));
			logger.info("ItemCount = " + rs.getString("ItemCount"));
		}
		rs.close();
		connTest.close();
	}

	public void mysql() throws SQLException {
		Connection connTest = DriverManager.getConnection("jdbc:sqlite:./db/mysql.db");
		Statement stat = connTest.createStatement();
		// cleanup
		stat.executeUpdate("DROP TABLE IF EXISTS `vc3_cloudgateway_key`;");

		// create table
		stat.executeUpdate("" + " CREATE TABLE `vc3_cloudgateway_key` ( "
				+ "   `id` integer /* unsigned bigint */ NOT NULL PRIMARY KEY AUTOINCREMENT  "
				+ "   ,`k` varchar(512) DEFAULT NULL /* COMMENT 'Plain text key. e.g. AK for aliyun, subscriber_id for azure' */ "
				+ "   ,`v` text DEFAULT NULL /* COMMENT 'Plain text value. e.g. SK for aliyun, certificate encoded in base64' */ "
				+ "   ,`cloudtype` varchar(32) DEFAULT NULL /* COMMENT 'e.g. aliyun, azure, openstackj, easystacki' */ "
				+ "   ,`created_datetime` datetime DEFAULT null  " + "   ,`updated_datetime` datetime DEFAULT null  "
				+ " )");

		// create index
		stat.executeUpdate(
				"CREATE INDEX IF NOT EXISTS `vc3_cloudgateway_key_cloudtype_idx` on `vc3_cloudgateway_key` (`cloudtype`);");

		//
		PreparedStatement prep = connTest.prepareStatement(
				"insert into `vc3_cloudgateway_key` (`k`, `v`, `cloudtype`, `created_datetime`, `updated_datetime`) values (?, ?, ?, ?, ?) ");
		// 写入数据目录
		prep.setString(1, "ak1");
		prep.setString(2, "c2sx");
		prep.setString(3, "aliyun");
		prep.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
		prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime())); // (4,
																				// new
																				// Date(new
																				// java.util.Date().getTime()));
		prep.addBatch();
		prep.setString(1, "ak2");
		prep.setString(2, "c2sy");
		prep.setString(3, "aliyun");
		prep.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
		prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
		prep.addBatch();
		prep.setString(1, "ak3");
		prep.setString(2, "c2sz");
		prep.setString(3, "aliyun");
		prep.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
		prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
		prep.addBatch();

		connTest.setAutoCommit(false);
		prep.executeBatch();
		connTest.setAutoCommit(true);

		ResultSet rs = stat.executeQuery("select * from `vc3_cloudgateway_key`;");
		while (rs.next()) {
			logger.info("k = " + rs.getString("k"));
			logger.info("v = " + rs.getString("v"));
			logger.info("cloudtype = " + rs.getString("cloudtype"));
			logger.info("updated_datetime = " + rs.getTimestamp("updated_datetime"));
			logger.info("created_datetime = " + rs.getTimestamp("created_datetime"));
		}
		rs.close();
		connTest.close();
	}

	public void memory() throws SQLException {

		Connection connTest = DriverManager.getConnection("jdbc:sqlite::memory:");
		Statement stat = connTest.createStatement();
		// cleanup
		stat.executeUpdate("DROP TABLE IF EXISTS `vc3_cloudgateway_key`;");

		// create table
		stat.executeUpdate("" + " CREATE TABLE `vc3_cloudgateway_key` ( "
				+ "   `id` integer /* unsigned bigint */ NOT NULL PRIMARY KEY AUTOINCREMENT  "
				+ "   ,`k` varchar(512) DEFAULT NULL /* COMMENT 'Plain text key. e.g. AK for aliyun, subscriber_id for azure' */ "
				+ "   ,`v` text DEFAULT NULL /* COMMENT 'Plain text value. e.g. SK for aliyun, certificate encoded in base64' */ "
				+ "   ,`cloudtype` varchar(32) DEFAULT NULL /* COMMENT 'e.g. aliyun, azure, openstackj, easystacki' */ "
				+ "   ,`created_datetime` datetime DEFAULT null  " + "   ,`updated_datetime` datetime DEFAULT null  "
				+ " )");

		// create index
		stat.executeUpdate(
				"CREATE INDEX IF NOT EXISTS `vc3_cloudgateway_key_cloudtype_idx` on `vc3_cloudgateway_key` (`cloudtype`);");

		//
		PreparedStatement prep = connTest.prepareStatement(
				"insert into `vc3_cloudgateway_key` (`k`, `v`, `cloudtype`, `created_datetime`, `updated_datetime`) values (?, ?, ?, ?, ?) ");
		// 写入数据目录
		prep.setString(1, "ak1");
		prep.setString(2, "c2sx");
		prep.setString(3, "aliyun");
		prep.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
		prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime())); // (4,
																				// new
																				// Date(new
																				// java.util.Date().getTime()));
		prep.addBatch();
		prep.setString(1, "ak2");
		prep.setString(2, "c2sy");
		prep.setString(3, "aliyun");
		prep.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
		prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
		prep.addBatch();
		prep.setString(1, "ak3");
		prep.setString(2, "c2sz");
		prep.setString(3, "aliyun");
		prep.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
		prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
		prep.addBatch();

		connTest.setAutoCommit(false);
		prep.executeBatch();
		connTest.setAutoCommit(true);

		ResultSet rs = stat.executeQuery("select * from `vc3_cloudgateway_key`;");
		while (rs.next()) {
			logger.info("k = " + rs.getString("k"));
			logger.info("v = " + rs.getString("v"));
			logger.info("cloudtype = " + rs.getString("cloudtype"));
			logger.info("updated_datetime = " + rs.getTimestamp("updated_datetime"));
			logger.info("created_datetime = " + rs.getTimestamp("created_datetime"));
		}
		rs.close();
		connTest.close();
	}

	public static void main(String[] args) throws Exception {
		Class.forName("org.sqlite.JDBC");
		SQLiteMain sqliteMain = new SQLiteMain();
		sqliteMain.featured();
		sqliteMain.mysql();
		sqliteMain.memory();
	}
}
