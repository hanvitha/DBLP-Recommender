package recommender;

import database.*;
import parser.*;
import java.sql.*;

public class Main {
	public static String filePath;
	public static PrepStmts stmts;

	public static void main(String[] args) {
		filePath = args[0];
		try {
			Connection conn = DBLPConnection.getConn("jdbc:mysql://127.0.0.1:3306/dblp3", "root", "root");
			stmts = new PrepStmts(conn);
			XMLHandler handler = new XMLHandler();
			Parser parser = new Parser();
			conn.setAutoCommit(false);
			parser.parseXML(handler);
			System.out.println("Done saving processed data to DataBase!");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
