package cz.martlin.defrost.impl;

import java.text.SimpleDateFormat;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.NotFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.core.DefrostException;

public class IDnesForumDesc extends CommonPostDescriptor {

	public IDnesForumDesc() {
		super(new SimpleDateFormat("d.M.yyyy h:mm"));
	}

	@Override
	public Node inferPostContentElem(Html document) throws DefrostException {
		Node node0 = tools.inferBody(document);
		Node node1 = tools.findChildById(node0, "main");
		Node node2 = tools.findChildByClassName(node1, "m-bg-1");
		Node node3 = tools.findChildByClassName(node2, "m-bg-2");
		Node node4 = tools.findChildByClassName(node3, "m-bg-3");
		Node node5 = tools.findChildByClassName(node4, "m-bg-4");
		Node node6 = tools.findChildByClassName(node5, "content");
		Node node7 = tools.findChildByClassName(node6, "col-a");
		return node7;
	}

	@Override
	public Node inferTitleElemFromPC(Node content) throws DefrostException {
		Node node0 = content;
		Node node1 = tools.findChildByClassName(node0, "moot-art");
		Node node2 = tools.findChildByTagName(node1, "h3");
		Node node3 = tools.findChildByTagName(node2, "a");
		return node3;
	}

	@Override
	public Node inferDiscussElemFromPC(Node content) throws DefrostException {
		Node node0 = content;
		Node node1 = tools.findChildById(node0, "disc-list");
		return node1;
	}

	@Override
	public boolean isCommentElem(Node node) {
		if (!(node instanceof Div)) {
			return false;
		}

		if (!tools.isClass(node, "contribution")) {
			return false;
		}

		return true;
	}

	private Node inferComentBodyElement(Node comment) throws DefrostException {
		Node node0 = comment;
		Node node1 = tools.findChildByTagName(node0, "table");
		Node node3 = tools.findChildByTagName(node1, "tr");
		Node node5 = tools.findChildByClassName(node3, "cell");
		return node5;
	}

	@Override
	public Node inferCommentAuthorElemFromC(Node comment) throws DefrostException {
		Node node0 = inferComentBodyElement(comment);
		Node node1 = tools.findChildByTagName(node0, "h4");
		return node1;
	}

	@Override
	public String inferNameFromCA(Node author) throws DefrostException {
		Node nodeA = tools.findChildByTagName(author, "a");

		NodeFilter filter = new NotFilter(new NodeClassFilter(TagNode.class));
		NodeList chars = nodeA.getChildren().extractAllNodesThatMatch(filter);
		return chars.toHtml();
	}

	@Override
	public String inferIdFromCA(Node author) throws DefrostException {
		return inferNameFromCA(author); // FIXME
		// Node nodeSup = tools.findChildByTagName(author, "sup");
		// return nodeSup.toHtml();// FIXME
		// // tools.inferTextChild(nodeSup);
	}

	@Override
	public Node inferDateElemFromC(Node comment) throws DefrostException {
		Node node0 = inferComentBodyElement(comment);
		Node node1 = tools.findChildByClassName(node0, "properties");
		Node node2 = tools.findChildByClassName(node1, "date hover");

		return node2;
	}

	@Override
	public Node inferCommentElementFromC(Node comment) throws DefrostException {
		Node node0 = inferComentBodyElement(comment);
		Node node1 = tools.findChildByClassName(node0, "user-text");

		return node1;
	}

}