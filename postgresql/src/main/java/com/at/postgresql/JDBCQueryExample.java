package com.at.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCQueryExample {
	private static final Logger logger = LoggerFactory.getLogger(JDBCQueryExample.class);

	public static void main(String[] args) throws Exception {
		String url = "jdbc:postgresql://localhost/test?user=root";
		Connection conn = DriverManager.getConnection(url);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM vc3_cloudgateway_key");
		for (int i = 1; rs.next(); i++) {
			logger.info("ln " + i + ": '" + rs.getString("k") + "'");
		}
		rs.close();
		st.close();
		conn.close();
	}
}
