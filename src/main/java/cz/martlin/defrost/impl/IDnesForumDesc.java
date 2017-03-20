package cz.martlin.defrost.impl;

import java.util.Calendar;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.base.ForumDescriptorBase;
import cz.martlin.defrost.tools.NetworkingException;
import cz.martlin.defrost.tools.ParserTools;

public class IDnesForumDesc implements ForumDescriptorBase {

	private final ParserTools tools;
	
	public IDnesForumDesc() {
		this.tools = new ParserTools();
	}

	@Override
	public String inferPostTitle(Html document) throws NetworkingException {
		Node head = tools.inferHead(document);
		Node title = tools.findChildByTagName(head, "title");
		Node text = title.getFirstChild();
		return text.getText();
	}

	@Override
	public NodeList inferComments(Html document) throws NetworkingException {
		//TODO
		return document.getChildren().elementAt(1).getChildren();
	}

	@Override
	public String inferCommentAuthor(Node coment) throws NetworkingException {
		// TODO
		return "kdoo";
	}

	@Override
	public Calendar inferCommentDate(Node coment) throws NetworkingException {
		// TODO 
		return Calendar.getInstance();
	}

	@Override
	public String inferCommentContent(Node coment) throws NetworkingException {
		// TODO 
		return "cooo";
	}

}
