package com.at.h2database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2Main {
    private static final Logger logger = LoggerFactory.getLogger(H2Main.class);

    public void memory() throws Exception {

        Connection conn = DriverManager.getConnection("jdbc:h2:mem:vc3_gateway;DB_CLOSE_DELAY=-1;MVCC=TRUE;AUTO_RECONNECT=TRUE", "sa", "");
        Statement stat = conn.createStatement();
        // cleanup
        stat.executeUpdate("DROP TABLE IF EXISTS `vc3_cloudgateway_key`;");

        // create table
        stat.executeUpdate(""
                + " CREATE TABLE `vc3_cloudgateway_key` ( "
                + "   `id` bigint NOT NULL PRIMARY KEY AUTO_INCREMENT  "
                + "   ,`k` varchar(512) DEFAULT NULL COMMENT 'Plain text key. e.g. AK for aliyun, subscriber_id for azure' "
                + "   ,`v` text DEFAULT NULL COMMENT 'Plain text value. e.g. SK for aliyun, certificate encoded in base64' "
                + "   ,`cloudtype` varchar(32) DEFAULT NULL COMMENT 'e.g. aliyun, azure, openstackj, easystacki' "
                + "   ,`created_datetime` datetime DEFAULT null  "
                + "   ,`updated_datetime` datetime DEFAULT null  "
                + " )"
                );

        // create index
        stat.executeUpdate("CREATE INDEX IF NOT EXISTS `vc3_cloudgateway_key_cloudtype_idx` on `vc3_cloudgateway_key` (`cloudtype`);");

        //
        PreparedStatement prep = conn.prepareStatement("insert into `vc3_cloudgateway_key` (`k`, `v`, `cloudtype`, `created_datetime`, `updated_datetime`) values (?, ?, ?, ?, ?) ");
        // 写入数据目录
        prep.setString(1, "ak1");
        prep.setString(2, "c2sx");
        prep.setString(3, "aliyun");
        prep.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
        prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime())); //(4, new Date(new java.util.Date().getTime()));
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

        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);

        ResultSet rs = stat.executeQuery("select * from `vc3_cloudgateway_key`;");
        while (rs.next()) {
            logger.info("k = " + rs.getString("k"));
            logger.info("v = " + rs.getString("v"));
            logger.info("cloudtype = " + rs.getString("cloudtype"));
            logger.info("updated_datetime = " + rs.getTimestamp("updated_datetime"));
            logger.info("created_datetime = " + rs.getTimestamp("created_datetime"));
        }
        rs.close();
        conn.close();
    }

    public void mysql() throws Exception {

        // 1. AUTO_SERVER=TRUE is required in the url of all local connections;
        //         e.g. ./h2.cmd -url "jdbc:h2:file:./vc3_gateway;DB_CLOSE_DELAY=10;MVCC=TRUE;AUTO_SERVER=TRUE;" -user sa
        // 2. It will use all avaiable tcp ports, except you explicitly speicify it by AUTO_SERVER_PORT=9090.
        // 3.
        //    DB_CLOSE_DELAY=-1, never close the connection ;
        //    DB_CLOSE_DELAY=0, close the connection immediately while calling close() ;
        //    DB_CLOSE_DELAY=10, idle for 10s, then close the connection ;
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./vc3_gateway;DB_CLOSE_DELAY=0;MVCC=TRUE;AUTO_SERVER=TRUE;AUTO_RECONNECT=TRUE", "sa", "");
        Statement stat = conn.createStatement();
        // cleanup
        stat.executeUpdate("DROP TABLE IF EXISTS `vc3_cloudgateway_key`;");

        // create table
        stat.executeUpdate(""
                + " CREATE TABLE `vc3_cloudgateway_key` ( "
                + "   `id` bigint NOT NULL PRIMARY KEY AUTO_INCREMENT  "
                + "   ,`k` varchar(512) DEFAULT NULL COMMENT 'Plain text key. e.g. AK for aliyun, subscriber_id for azure' "
                + "   ,`v` text DEFAULT NULL COMMENT 'Plain text value. e.g. SK for aliyun, certificate encoded in base64' "
                + "   ,`cloudtype` varchar(32) DEFAULT NULL COMMENT 'e.g. aliyun, azure, openstackj, easystacki' "
                + "   ,`created_datetime` datetime DEFAULT null  "
                + "   ,`updated_datetime` datetime DEFAULT null  "
                + " )"
                );

        // create index
        stat.executeUpdate("CREATE INDEX IF NOT EXISTS `vc3_cloudgateway_key_cloudtype_idx` on `vc3_cloudgateway_key` (`cloudtype`);");

        //
        PreparedStatement prep = conn.prepareStatement("insert into `vc3_cloudgateway_key` (`k`, `v`, `cloudtype`, `created_datetime`, `updated_datetime`) values (?, ?, ?, ?, ?) ");
        // 写入数据目录
        prep.setString(1, "ak1");
        prep.setString(2, "c2sx");
        prep.setString(3, "aliyun");
        prep.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
        prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime())); //(4, new Date(new java.util.Date().getTime()));
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

        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);

        ResultSet rs = stat.executeQuery("select * from `vc3_cloudgateway_key`;");
        while (rs.next()) {
            logger.info("k = " + rs.getString("k"));
            logger.info("v = " + rs.getString("v"));
            logger.info("cloudtype = " + rs.getString("cloudtype"));
            logger.info("updated_datetime = " + rs.getTimestamp("updated_datetime"));
            logger.info("created_datetime = " + rs.getTimestamp("created_datetime"));
        }
        rs.close();
        conn.close(); // if the value of parameter 'DB_CLOSE_DELAY' is greater than 0, conn.close() will NOT close the connection actually
    }

    public void tcp() throws Exception {

        // 1. java -cp h2-1.4.191.jar org.h2.tools.Server -tcp -tcpPort 6666 -tcpAllowOthers -tcpPassword 8anMDZ0/zm3CHMDS
        //       -tcpDaemon, cause the server running as daemon, if it's specified in command line, it will exit immediately
        //                   if it's NOT specified in program, it will NOT exit after main ending.
        //       -tcpSSL    ssl connection,
        //                  System.setProperty("javax.net.ssl.trustStore", "<path/to/truststore.jks>");
        //                  System.setProperty("javax.net.ssl.trustStorePassword", "<truststorepassword>");
        //                  System.setProperty("javax.net.ssl.keyStore", "<path/to/keystore.jks>");
        //                  System.setProperty("javax.net.ssl.keyStorePassword", "<keystorepassword>");
        String[] args = new String[]{"-tcp", "-tcpPort", "6666", "-tcpAllowOthers", "-tcpPassword", "8anMDZ0/zm3CHMDS"};
        Server h2Server = Server.createTcpServer(args).start();

        // 2. -tcpSSL => ssl://
        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:6666/e:/workspace_java/h2database/vc3_gateway;DB_CLOSE_DELAY=0;MVCC=TRUE;AUTO_RECONNECT=TRUE", "sa", "");
        Statement stat = conn.createStatement();
        // cleanup
        stat.executeUpdate("DROP TABLE IF EXISTS `vc3_cloudgateway_key`;");

        // create table
        stat.executeUpdate(""
                + " CREATE TABLE `vc3_cloudgateway_key` ( "
                + "   `id` bigint NOT NULL PRIMARY KEY AUTO_INCREMENT  "
                + "   ,`k` varchar(512) DEFAULT NULL COMMENT 'Plain text key. e.g. AK for aliyun, subscriber_id for azure' "
                + "   ,`v` text DEFAULT NULL COMMENT 'Plain text value. e.g. SK for aliyun, certificate encoded in base64' "
                + "   ,`cloudtype` varchar(32) DEFAULT NULL COMMENT 'e.g. aliyun, azure, openstackj, easystacki' "
                + "   ,`created_datetime` datetime DEFAULT null  "
                + "   ,`updated_datetime` datetime DEFAULT null  "
                + " )"
                );

        // create index
        stat.executeUpdate("CREATE INDEX IF NOT EXISTS `vc3_cloudgateway_key_cloudtype_idx` on `vc3_cloudgateway_key` (`cloudtype`);");

        //
        PreparedStatement prep = conn.prepareStatement("insert into `vc3_cloudgateway_key` (`k`, `v`, `cloudtype`, `created_datetime`, `updated_datetime`) values (?, ?, ?, ?, ?) ");
        // 写入数据目录
        prep.setString(1, "ak1");
        prep.setString(2, "c2sx");
        prep.setString(3, "aliyun");
        prep.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
        prep.setTimestamp(5, new Timestamp(new java.util.Date().getTime())); //(4, new Date(new java.util.Date().getTime()));
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

        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);

        ResultSet rs = stat.executeQuery("select * from `vc3_cloudgateway_key`;");
        while (rs.next()) {
            logger.info("k = " + rs.getString("k"));
            logger.info("v = " + rs.getString("v"));
            logger.info("cloudtype = " + rs.getString("cloudtype"));
            logger.info("updated_datetime = " + rs.getTimestamp("updated_datetime"));
            logger.info("created_datetime = " + rs.getTimestamp("created_datetime"));
        }
        rs.close();

        Thread.sleep(100000); // sleep

        conn.close();
        h2Server.stop(); // if NO -tcpDaemon, stop() is required.
    }



    public static void main(String[] args) throws Exception {

        Class.forName("org.h2.Driver");
        H2Main h2Main = new H2Main();
        logger.info("-----------------------------h2 memory-------------------------------------");
        h2Main.memory();
        logger.info("-----------------------------h2 mysql statement-------------------------------------");
        h2Main.mysql();
        logger.info("-----------------------------h2 tcp server-------------------------------------");
        h2Main.tcp();
    }
}
