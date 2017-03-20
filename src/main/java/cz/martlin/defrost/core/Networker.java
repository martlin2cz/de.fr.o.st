package cz.martlin.defrost.core;

import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

/**
 * Implements networking stuff.
 * 
 * @author martin
 *
 */
public class Networker {
	private static final String HTML_TAG_NAME = "html";

	public Networker() {
	}

	/**
	 * Connects to specified URL, downloads HTML, parses and returns the &lt;html&gt element.
	 * @param url
	 * @return
	 * @throws DefrostException
	 */
	public Html query(URL url) throws DefrostException {
		try {
			String adress = url.toExternalForm();

			Parser parser = new Parser(adress);
			NodeFilter filter = new TagNameFilter(HTML_TAG_NAME);

			NodeList nodes = parser.parse(filter);

			Node node = nodes.elementAt(0);
			Html html = (Html) node;
			return html;
		} catch (Exception e) {
			throw new DefrostException("Cannnot parse HTML", e);
		}
	}

}
