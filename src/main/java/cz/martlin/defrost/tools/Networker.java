package cz.martlin.defrost.tools;

import java.net.URL;

import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class Networker {
	
	public NodeList query(URL url) throws NetworkingException {
	
		try {
			String adress = url.toExternalForm(); 
			Parser parser = new Parser(adress);

			NodeList nodes = parser.parse(null);
			return nodes;
		} catch (ParserException e) {
			throw new NetworkingException("Cannnot parse HTML", e);
		}
	}

}
