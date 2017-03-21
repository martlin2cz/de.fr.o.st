package cz.martlin.defrost.impl;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.core.DefrostException;
@Deprecated
public class XXX_EmiminoForumDesc extends XXX_CommonPostDescriptor {

	public XXX_EmiminoForumDesc() {
		super(new EmiminoDateFormat());
	}

	@Override
	public Node inferPostContentElem(Html document) throws DefrostException {
		Node node0 = tools.inferBody(document);
		Node node1 = tools.findChildById(node0, "main");
		Node node2 = tools.findChildByClassName(node1, "m-bg-1");
		Node node3 = tools.findChildByClassName(node2, "m-bg-2");
		Node node4 = tools.findChildByClassName(node3, "m-bg-3");
		Node node5 = tools.findChildByClassName(node4, "m-bg-4");
		Node node6 = tools.findChildByClassName(node5, "col-a");
		return node6;
	}

	@Override
	public Node inferTitleElemFromPC(Node content) throws DefrostException {
		Node node0 = content;
		// Node node1 = tools.findChildById(node0, "diary");
		Node theOwner = content.getParent().getParent().getParent();
		Node node2 = tools.findChildByGlobalTagName(theOwner, "h1");
		return node2;
	}

	@Override
	public Node inferDiscussElemFromPC(Node content) throws DefrostException {
		// Node node0 = content;
		// Node node1 = tools.findChildById(node0, "diary_comments");
		// Node node2 = tools.findChildById(node1, "discussionPostList");
		Node node = content.getParent().getParent().getParent().getParent();

		Node theOwner = content.getParent().getParent().getParent();
		Node theDiscuss = tools.findChildByGlobalId(theOwner, "discussionPostList");

		// System.out.println(theDiscuss.toHtml().substring(0, 5000));
		return theDiscuss;
	}

	@Override
	public boolean isCommentElem(Node node) {
		if (!(node instanceof Div)) {
			return false;
		}

		if (!tools.isClass(node, "discussion_post  vycisti ")) {
			return false;
		}

		return true;
	}

	@Override
	public Node inferCommentAuthorElemFromC(Node comment) throws DefrostException {
		Node node0 = comment;
		Node node1 = tools.findChildByClassName(node0, "user");
		Node node2 = tools.findChildByClassName(node1, "user_in");
		return node2;
	}

	@Override
	public String inferNameFromCA(Node author) throws DefrostException {
		Node node0 = author;
		// Node node1 = tools.findChildByTagName(node0, "b");
		Node node2 = tools.findChildByTagName(node0, "a");
		return tools.inferTextChild(node2);
	}

	@Override
	public String inferIdFromCA(Node author) throws DefrostException {
		return inferNameFromCA(author); // TODO parse from ?profileid= link?
	}

	@Override
	public Node inferDateElemFromC(Node comment) throws DefrostException {
		Node node0 = inferCommentAuthorElemFromC(comment);
		// NodeList nodes1 = tools.findChildrenByClassName(node0, "date");
		Node node2 = node0.getLastChild().getPreviousSibling();
		Node node3 = tools.findChildByTagName(node2, "span");
		return node3;
	}

	@Override
	public Node inferCommentElementFromC(Node comment) throws DefrostException {
		Node node0 = comment;
		Node node1 = tools.findChildByClassName(node0, "post");
		Node node2 = tools.findChildByClassName(node1, "post_in");
		//Node node3 = tools.findChildByTagName(node2, "p");
		return node2;

	}

}
