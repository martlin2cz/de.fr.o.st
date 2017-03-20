package cz.martlin.defrost.impl;

import java.text.SimpleDateFormat;

import org.htmlparser.Node;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Html;

import cz.martlin.defrost.tools.NetworkingException;

public class IDnesForumDesc extends CommonPostDescriptor  {

	public IDnesForumDesc() {
		super(new SimpleDateFormat("d.M.y h:mm"));
	}

	@Override
	public Node inferPostContentElem(Html document) throws NetworkingException {
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
	public Node inferTitleElemFromPC(Node content) throws NetworkingException {
		Node node0 = content;
		Node node1 = tools.findChildByClassName(node0, "moot-art");
		Node node2 = tools.findChildByTagName(node1, "h3");
		Node node3 = tools.findChildByTagName(node2, "a");
		return node3;
	}

	@Override
	public Node inferDiscussElemFromPC(Node content) throws NetworkingException {
		Node node0 = content;
		Node node1 = tools.findChildById(node0, "disc-list");
		return node1;
	}

	@Override
	public boolean isCommentElem(Node node) {
		return node instanceof Div;	//TODO and class = contribution
	}
	
	private Node inferComentBodyElement(Node comment) throws NetworkingException {
		Node node0 = comment;
		Node node1 = tools.findChildByTagName(node0, "table");
		//Node node2 = tools.findChildByTagName(node1, "tbody");
		Node node3 = tools.findChildByTagName(node1, "tr");
		//Node node4 = tools.findChildByTagName(node3, "td");
		Node node5 = tools.findChildByClassName(node3, "cell");
		return node5;
	}
	
	
	@Override
	public Node inferCommentAuthorElemFromC(Node comment) throws NetworkingException {
		Node node0 = inferComentBodyElement(comment);
		Node node1 = tools.findChildByTagName(node0, "h4");
		return node1;
	}



	@Override
	public String inferNameFromCA(Node author) throws NetworkingException {
		Node nodeA = tools.findChildByTagName(author, "a");
		return tools.inferTextChild(nodeA);
	}

	@Override
	public String inferIdFromCA(Node author) throws NetworkingException {
		Node nodeSup = tools.findChildByTagName(author, "sup");
		return tools.inferTextChild(nodeSup);
	}
	
	@Override
	public Node inferDateElemFromC(Node comment) throws NetworkingException {
		Node node0 = inferComentBodyElement(comment);
		Node node1 = tools.findChildByClassName(node0, "properties");
		Node node2 = tools.findChildByClassName(node1, "date hover");

		return node2;
	}

	@Override
	public String inferCommentContent(Node comment) throws NetworkingException {
		Node node0 = inferComentBodyElement(comment);
		Node node1 = tools.findChildByClassName(node0, "user-text");
		return node1.toHtml();
	}



}
