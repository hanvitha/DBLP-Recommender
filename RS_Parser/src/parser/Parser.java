package parser;

import recommender.*;

import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;




public class Parser {

	public void parseXML(XMLHandler handler) {		
		SAXParserFactory saxpf = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = saxpf.newSAXParser();
			parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
			parser.parse(new File(Main.filePath), handler);
			Main.stmts.ExcecuteStatements();
			if(handler.errors >0)
				System.out.println("No of Errors : " +handler.errors);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
}
