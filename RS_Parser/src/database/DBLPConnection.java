package database;

import java.sql.*;

public class DBLPConnection {	
	public static Connection getConn(String db_url, String user_name, String pwd) {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			return DriverManager.getConnection(db_url, user_name, pwd);
		} catch (Exception e) {
			System.out.println("Error while opening a conneciton to database server: "+ e.getMessage());
			return null;
		}
	}
}
