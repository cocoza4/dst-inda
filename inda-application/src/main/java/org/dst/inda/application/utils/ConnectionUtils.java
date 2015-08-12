package org.dst.inda.application.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ConnectionUtils {
	
	private static Connection conn;
	
	static {
		try {
			conn = DriverManager.getConnection("jdbc:phoenix:localhost");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return conn;
	}

}
