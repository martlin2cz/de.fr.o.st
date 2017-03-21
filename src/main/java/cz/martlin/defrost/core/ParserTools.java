package cz.martlin.defrost.core;

import java.util.Calendar;
import java.util.Date;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.base.ForumDescriptorBase;

/**
 * Tools to be used in {@link ForumDescriptorBase}'s implementations.
 * 
 * @author martin
 *
 */
public class ParserTools {

	public ParserTools() {
	}

	public Node findChildByGlobalId(Node node, String id) throws DefrostException {
		NodeFilter filter = new HasAttributeFilter("id", id);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter, true);

		return getFirst(filtered, "with global id " + id);
	}

	public Node findChildById(Node node, String id) throws DefrostException {
		NodeFilter filter = new HasAttributeFilter("id", id);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter);

		return getFirst(filtered, "with id " + id);
	}


	public Node findChildByGlobalTagName(Node node, String tag) throws DefrostException {
		NodeFilter filter = new TagNameFilter(tag);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter, true);
		return getFirst(filtered, "with global tag " + tag);
	}

	
	public Node findChildByTagName(Node node, String tag) throws DefrostException {
		NodeFilter filter = new TagNameFilter(tag);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter);
		return getFirst(filtered, "with tag " + tag);
	}

	public Node findChildByClassName(Node node, String clazz) throws DefrostException {
		NodeFilter filter = new HasAttributeFilter("class", clazz);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter);
		return getFirst(filtered, "with class " + clazz);
	}

	public NodeList findChildrenByClassName(Node node, String clazz) throws DefrostException {
		NodeFilter filter = new HasAttributeFilter("class", clazz);
		NodeList children = node.getChildren();

		NodeList filtered = children.extractAllNodesThatMatch(filter);
		return filtered;
	}

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

	public String inferTextChild(Node node) {
		Node child = node.getFirstChild();
		return child.getText();
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

}
