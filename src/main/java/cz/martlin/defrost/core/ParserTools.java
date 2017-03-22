package cz.martlin.defrost.core;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

public class ParserTools {

	public ParserTools() {
	}

	@Deprecated
	public Node findChildByGlobalId(Node node, String id) throws DefrostException {
		NodeFilter filter = new HasAttributeFilter("id", id);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter, true);

		return getFirst(filtered, "with global id " + id);
	}

	@Deprecated
	public Node findChildById(Node node, String id) throws DefrostException {
		NodeFilter filter = new HasAttributeFilter("id", id);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter);

		return getFirst(filtered, "with id " + id);
	}

	@Deprecated
	public Node findChildByGlobalTagName(Node node, String tag) throws DefrostException {
		NodeFilter filter = new TagNameFilter(tag);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter, true);
		return getFirst(filtered, "with global tag " + tag);
	}

	@Deprecated
	public Node findChildByTagName(Node node, String tag) throws DefrostException {
		NodeFilter filter = new TagNameFilter(tag);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter);
		return getFirst(filtered, "with tag " + tag);
	}

	@Deprecated
	public Node findChildByClassName(Node node, String clazz) throws DefrostException {
		NodeFilter filter = new HasAttributeFilter("class", clazz);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter);
		return getFirst(filtered, "with class " + clazz);
	}

	@Deprecated
	public NodeList findChildrenByClassName(Node node, String clazz) throws DefrostException {
		NodeFilter filter = new HasAttributeFilter("class", clazz);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter);
		return filtered;
	}

	@Deprecated
	public boolean isClass(Node node, String clazz) {
		TagNode elem = (TagNode) node;
		String clazzReal = elem.getAttribute("class");

		return clazz.equals(clazzReal);
	}

	///////////////////////////////////////////////////////////////////////////

	public Node inferHead(Html document) throws DefrostException {
		return findChildByTagName(document, "head");
	}

	public Node inferBody(Html document) throws DefrostException {
		return findChildByTagName(document, "body");
	}

	public String inferTextInside(Node node) {
		String text = node.toPlainTextString();
		String trimmed = text.trim();
		return trimmed;
	}

	// TODO FIXME change back to private
	public Node getFirst(NodeList nodes, String desc) throws DefrostException {

		if (nodes.size() < 1) {
			throw new DefrostException("No such tag (" + desc + ") in \"" + nodes.toHtml() + "\"");
		}

		return nodes.elementAt(0);
	}

	public Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar;
	}
	///////////////////////////////////////////////////////////////////////////

	public NodeList applyFilter(Node node, NodeFilter filter) {
		NodeList children = node.getChildren();
		NodeList filtered = children.extractAllNodesThatMatch(filter, true);

		return filtered;
	}

	public Node applyFilterGetFirst(Node node, NodeFilter filter) throws DefrostException {
		NodeList filtered = applyFilter(node, filter);
		return getFirst(filtered, "with filter " + filter);
	}

	public NodeList applySelector(Node node, String selector) {
		NodeFilter filter = new CssSelectorNodeFilter(selector);
		return applyFilter(node, filter);
	}

	public Node applySelectorGetFirst(Node node, String selector) throws DefrostException {
		NodeFilter filter = new CssSelectorNodeFilter(selector);
		return applyFilterGetFirst(node, filter);
	}

	public Map<String, String> getUrlQueryParams(URL url) {
		// http://stackoverflow.com/questions/13592236/parse-a-uri-string-into-name-value-collection
		Map<String, String> query_pairs = new HashMap<String, String>();

		String query = url.getQuery();
		String[] pairs = query.split("&");

		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			try {
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO handle exception
				return null;
			}
		}
		return query_pairs;
	}
}
