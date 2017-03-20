package cz.martlin.defrost.tools;

import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

public class Networker {
	private static final String HTML_TAG_NAME = "html";
	
	public Networker() {
	}
	
	public Html query(URL url) throws NetworkingException {
		try {
			String adress = url.toExternalForm(); 
			
			Parser parser = new Parser(adress);
			NodeFilter filter = new TagNameFilter(HTML_TAG_NAME);
			
			NodeList nodes = parser.parse(filter);
			
			Node node = nodes.elementAt(0);
			Html html = (Html) node;
			return html;
		} catch (Exception e) {
			throw new NetworkingException("Cannnot parse HTML", e);
		}
	}
	

}
