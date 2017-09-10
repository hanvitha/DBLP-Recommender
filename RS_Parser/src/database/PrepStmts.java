package database;

import parser.*;
import java.sql.*;

public class PrepStmts {
	public static PreparedStatement statement_inproc, statement_conf, statement_author;
	public static PreparedStatement statement_author_id, statement_conf_id, statement_paper_id;
	public static Connection conn;

	public PrepStmts(Connection c) {
		conn = c;
		try {
			statement_inproc = conn
					.prepareStatement("insert into paper(title,year,conference,paper_key) values (?,?,?,?)");

			statement_author = conn.prepareStatement("insert into author(name,paper_key) values (?,?)");

			statement_conf = conn.prepareStatement("insert into conference(conf_key,name,detail) values (?,?,?)");

			statement_author_id = conn
					.prepareStatement("insert into author_id(author_name_id, author_name) values (?,?)");

			statement_conf_id = conn.prepareStatement("insert into conf_id(conf_name_id, conf_name) values (?,?)");

			statement_paper_id = conn.prepareStatement("insert into paper_id(paper_key_id, paper_key) values (?,?)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void prepareStatementsForIDS() {
		try {
			for (int i = 0; i < XMLHandler.Papers.size(); i++) {
				statement_paper_id.setInt(1, i);
				statement_paper_id.setString(2, XMLHandler.Papers.get(i));
				statement_paper_id.addBatch();
			}
			for (int i = 0; i < XMLHandler.Authors.size(); i++) {
				statement_author_id.setInt(1, i);
				statement_author_id.setString(2, XMLHandler.Authors.get(i));
				statement_author_id.addBatch();
			}
			for (int i = 0; i < XMLHandler.Conferences.size(); i++) {
				statement_conf_id.setInt(1, i);
				statement_conf_id.setString(2, XMLHandler.Conferences.get(i));
				statement_conf_id.addBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ExcecuteStatements() {
		try {
			statement_inproc.executeBatch();
			statement_conf.executeBatch();
			statement_author.executeBatch();

			prepareStatementsForIDS();
			statement_author_id.executeBatch();
			statement_conf_id.executeBatch();
			statement_paper_id.executeBatch();

			conn.commit();
			System.out.println("Exiting after saving data ");
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void ExecuteStatementsofLists() throws SQLException {
		System.out.println("Saving unique Authors, Conferences and Paper with ids");
		prepareStatementsForIDS();
		statement_author_id.executeBatch();
		statement_conf_id.executeBatch();
		statement_paper_id.executeBatch();
		conn.commit();
	}

	public static void SetInproceedings(XMLElements x) throws SQLException {
		statement_inproc.setString(1, x.paper_title);
		statement_inproc.setInt(2, x.year);
		statement_inproc.setInt(3, x.conf_name);
		statement_inproc.setInt(4, x.paper_key);
		statement_inproc.addBatch();

		for (int author1 : x.authors) {
			statement_author.setInt(1, author1);
			statement_author.setInt(2, x.paper_key);
			statement_author.addBatch();
		}
	}

	public static void SetProceedings(XMLElements xml) throws SQLException {
		statement_conf.setString(1, xml.conf_key);
		statement_conf.setInt(2, xml.conf_name);
		statement_conf.setString(3, xml.conf_detail);
		statement_conf.addBatch();
	}
}
