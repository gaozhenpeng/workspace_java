package com.at.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SQLiteMain {

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
			log.info("Name = " + rs.getString("Name"));
			log.info("ItemCount = " + rs.getString("ItemCount"));
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
		prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
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
			log.info("k = " + rs.getString("k"));
			log.info("v = " + rs.getString("v"));
			log.info("cloudtype = " + rs.getString("cloudtype"));
			log.info("updated_datetime = " + rs.getTimestamp("updated_datetime"));
			log.info("created_datetime = " + rs.getTimestamp("created_datetime"));
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
		prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
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
			log.info("k = " + rs.getString("k"));
			log.info("v = " + rs.getString("v"));
			log.info("cloudtype = " + rs.getString("cloudtype"));
			log.info("updated_datetime = " + rs.getTimestamp("updated_datetime"));
			log.info("created_datetime = " + rs.getTimestamp("created_datetime"));
		}
		rs.close();
		connTest.close();
	}

    public void json_column() throws SQLException {

        Connection connTest = DriverManager.getConnection("jdbc:sqlite::memory:");
        Statement stat = connTest.createStatement();
        // cleanup
        stat.executeUpdate("drop table if exists `json_user`;");

        // create table
        stat.executeUpdate("" + " CREATE TABLE `json_user` ( "
                + "   `id` integer /* unsigned bigint */ NOT NULL PRIMARY KEY AUTOINCREMENT  "
                + "   ,`user_name` varchar(20) DEFAULT NULL /* COMMENT 'user name' */ "
                + "   ,`last_login_info` json DEFAULT NULL /* COMMENT 'the info of last login' */ "
                // illegal in sqlite
                //+ "   , PRIMARY KEY(`id`) "
                + " )");

        // create index
        //     Exception in thread "main" org.sqlite.SQLiteException: [SQLITE_ERROR] SQL error or missing database (near "GENERATED": syntax error)
//        stat.executeUpdate(
//                "ALTER TABLE json_user ADD last_login_result VARCHAR(15) GENERATED ALWAYS AS (JSON_EXTRACT(last_login_info, '$.result')) VIRTUAL ;");
//        stat.executeUpdate(
//                "ALTER TABLE json_user ADD index idx_json_user_result(last_login_result) ;");
        //
        PreparedStatement prep = connTest.prepareStatement(
                "insert into `json_user` (`user_name`, `last_login_info`) values (?, ?) ");
        prep.setString(1, "lucy");
        prep.setString(2, "{\"time\":\"2015-01-01 13:00:00\",\"ip\":\"192.168.1.1\",\"result\":\"fail\"}");
        prep.addBatch();
        prep.setString(1, "bob");
        prep.setString(2, "{\"time\":\"2015-01-07 06:44:00\",\"ip\":\"192.168.1.4\",\"result\":\"success\"}");
        prep.addBatch();
        prep.setString(1, "lucy");
        prep.setString(2, "{\"time\":\"2016-07-08 09:44:00\",\"ip\":\"192.168.1.44\",\"result\":\"success\"}");
        prep.addBatch();

        connTest.setAutoCommit(false);
        prep.executeBatch();
        connTest.setAutoCommit(true);

        ResultSet rs = stat.executeQuery("select * from `json_user` where JSON_EXTRACT(last_login_info, '$.result') = 'success' ;");
        while (rs.next()) {
            log.info("id = " + rs.getInt("id"));
            log.info("user_name = " + rs.getString("user_name"));
            log.info("last_login_info = " + rs.getString("last_login_info"));
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
		sqliteMain.json_column();
	}
}
