package parser;

import java.sql.SQLException;
import java.util.ArrayList;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import database.*;

public class XMLHandler extends DefaultHandler {

	public static ArrayList<String> Conferences;
	public static ArrayList<String> Authors;
	public static ArrayList<String> Papers;

	private XMLElements xml;
	
	private int curElement = -1;
	private int ancestor = -1;
	private int id = 0;
	private int line = 0;
	private int authorid = -1;
	public int errors = 0;

	public XMLHandler() {
		Conferences = new ArrayList<String>();
		Authors = new ArrayList<String>();
		Papers = new ArrayList<String>();
		xml = new XMLElements();
	}

	public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs)
			throws SAXException {

		if (rawName.equals("inproceedings")) {
			ancestor = XMLElements.INPROCEEDING;
			id = SetAndGetID(attrs.getValue("key"), Papers);
			xml = new XMLElements();
			xml.paper_key = id;
			curElement = XMLElements.INPROCEEDING;
		} else if (rawName.equals("proceedings")) {
			ancestor = XMLElements.PROCEEDING;
			curElement = XMLElements.PROCEEDING;
			xml = new XMLElements();
			xml.conf_key = attrs.getValue("key");
		}

		if (ancestor == XMLElements.INPROCEEDING) {
			curElement = XMLElements.getElement(rawName);
		} else if (ancestor == XMLElements.PROCEEDING) {
			curElement = XMLElements.getElement(rawName);
		} else if (ancestor == -1) {
			curElement = XMLElements.OTHER;
			ancestor = XMLElements.OTHER;
		} else {
			curElement = XMLElements.OTHER;
		}
		line++;
	}

	public void characters(char[] ch, int start, int length) {
		String xmlStr = new String(ch, start, length).trim();
		if (ancestor == XMLElements.INPROCEEDING) {
			if (curElement == XMLElements.AUTHOR) {
				id = SetAndGetID(xmlStr, Authors);
				authorid = id;
			} else if (curElement == XMLElements.CONFNAME) {
				id = SetAndGetID(xmlStr, Conferences);
				xml.conf_name = id;
				xml.paper_conf = id;
			} else if (curElement == XMLElements.TITLE) {
				xml.paper_title = xmlStr;
			} else if (curElement == XMLElements.YEAR) {
				xml.year = Integer.parseInt(xmlStr);
			}
		} else if (ancestor == XMLElements.PROCEEDING) {
			if (curElement == XMLElements.CONFNAME) {
				id = SetAndGetID(xmlStr, Conferences);
				xml.conf_name = id;
			} else if (curElement == XMLElements.CONFDETAIL) {
				xml.conf_detail = xmlStr;
			}
		}
	}

	public void endElement(String namespaceURI, String localName, String rawName) {
		if (rawName.equals("author") && ancestor == XMLElements.INPROCEEDING) {
			xml.authors.add(authorid);
		}

		if (XMLElements.getElement(rawName) == XMLElements.INPROCEEDING) {
			ancestor = -1;
			try {
				if (xml.paper_title.equals("") || xml.paper_conf == -1 || xml.year == 0) {
					errors++;
					System.out.println("Error in parsing " + xml.paper_title);
					return;
				}
				PrepStmts.SetInproceedings(xml);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Line : " + line);
			}

		} else if (XMLElements.getElement(rawName) == XMLElements.PROCEEDING) {
			ancestor = -1;
			try {
				if (xml.conf_name == -1) {
					id = SetAndGetID(xml.conf_detail, Conferences);
					xml.conf_name = id;
				}
				if (xml.conf_key.equals("") || xml.conf_name == -1 || xml.conf_detail.equals("")) {
					System.out.println("Line:" + line);
					System.exit(0);
				}
				PrepStmts.SetProceedings(xml);
			} catch (SQLException e) {
				System.out.println("line:" + line);
				e.printStackTrace();
				System.exit(0);
			}
		}

		if (line % 10000 == 0) {
			try {
				PrepStmts.statement_inproc.executeBatch();
				PrepStmts.statement_conf.executeBatch();
				PrepStmts.statement_author.executeBatch();
				System.out.print(". ");
				
				if (line > 20000000) {
					PrepStmts.ExecuteStatementsofLists();
					System.exit(0);
				}
				PrepStmts.conn.commit();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	public int SetAndGetID(String str, ArrayList<String> list) {
		if (!list.contains(str)) {
			list.add(str);
		}
		return list.indexOf(str);
	}
}
