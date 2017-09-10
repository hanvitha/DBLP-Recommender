package parser;

import java.util.ArrayList;

public class XMLElements {

	public static final int OTHER = 0;
	public static final int INPROCEEDING = 1;
	public static final int PROCEEDING = 2;
	public static final int CONFNAME = 3;
	
	public static final int AUTHOR = 4;
	public static final int TITLE = 5;
	public static final int CONFDETAIL = 5;
	public static final int YEAR = 6;

	public int paper_key;
	public String paper_title;
	public int year;
	public int paper_conf;
	public int conf_name;
	public String conf_detail;
	public String conf_key;
	public ArrayList<Integer> authors;
	
	public XMLElements() {
		authors = new ArrayList<Integer>();
		paper_title = "";
		conf_detail = "";
		conf_key = "";
		paper_conf = -1;
		paper_key = -1;
		conf_name = -1;
		year = 0;
	}
	
	public void resetPaperDetails(){
		authors = new ArrayList<Integer>();
		paper_title = "";
		conf_detail = "";
		conf_key = "";
		paper_key = -1;
		conf_name = -1;
		year = 0;
	}
	
	public String toString(){
		return "title: " + paper_title + " authors: " + authors.toString() +
				" conference: " + conf_name + " year: " + year + " key: " + paper_key;
	}
	
	public static int getElement(String rawName) {
		if (rawName.equals("inproceedings")) {
			return INPROCEEDING;
		} else if (rawName.equals("proceedings")) {
			return PROCEEDING;
		} else if (rawName.equals("booktitle")) {
			return CONFNAME;
		} else if (rawName.equals("author")) {
			return AUTHOR;
		}  else if (rawName.equals("title") || rawName.equals("sub") || rawName.equals("sup") || rawName.equals("i")
				|| rawName.equals("tt")) {
			return TITLE;
		} else if (rawName.equals("year")) {
			return YEAR;
		} else {
			return OTHER;
		}
	}
	
	
}
