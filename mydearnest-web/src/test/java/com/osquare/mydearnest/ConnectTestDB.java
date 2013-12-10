package com.osquare.mydearnest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectTestDB {
	public static void checkDB() {
		
		Connection conn;
		Statement stmt;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (ClassNotFoundException e) {
			System.err.print("ClassNotFoundException");
		}
		
		try {
			String jdbcUrl = "jdbc:mysql://localhost:3306/dbtest";
			String userid = "root";
			String userPass = "testskullacy";
			
			conn = DriverManager.getConnection(jdbcUrl, userid, userPass);
			stmt = conn.createStatement();
			
			System.out.println("제대로 연결");
			
			stmt.close();
			conn.close();
			
					
		} catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		
		
	}
}
