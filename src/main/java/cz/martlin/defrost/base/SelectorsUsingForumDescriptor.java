package cz.martlin.defrost.base;

import java.text.DateFormat;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.misc.DefrostException;

public abstract class SelectorsUsingForumDescriptor extends CommonForumDescriptor {

	public SelectorsUsingForumDescriptor(String categorySiteURLPattern, String commentSiteURLPattern,
			DateFormat commentDateFormat) {
		super(categorySiteURLPattern, commentSiteURLPattern, commentDateFormat);
	}

	///////////////////////////////////////////////////////////////////////////
	@Override
	public NodeList selectPostItemInCategorySite(Html document) throws DefrostException {
		String selector = selectorOfPostItemsInCategorySite();
		return tools.applySelector(document, selector);
	}

	@Override
	public LinkTag selectPostLinkInPostItem(Node PostItem) throws DefrostException {
		String selector = selectorOfPostLinkInPostItem();
		return (LinkTag) tools.applySelectorGetFirst(PostItem, selector);
	}

	@Override
	public Node selectCategoryNextPageButton(Html document) throws Exception {
		String selector = selectorOfCategoryNextPageButton();
		return tools.applySelectorGetFirst(document, selector);
	}

	@Override
	public Node selectDiscussElementInPostSite(Html document) throws DefrostException {
		String selector = selectorOfDiscussElementInPostSite();
		return tools.applySelectorGetFirst(document, selector);
	}

	@Override
	public NodeList selectCommentsElementInDiscuss(Node discuss) {
		String selector = selectorOfCommentsElementInDiscuss();
		return tools.applySelector(discuss, selector);
	}

	@Override
	public Node selectTitleInPostSite(Html document) throws DefrostException {
		String selector = selectorOfTitleInCommentSite();
		return tools.applySelectorGetFirst(document, selector);
	}

	@Override
	public Node selectPostNextPageButton(Html document) throws Exception {
		String selector = selectorOfPostNextPageButton();
		NodeList nodes = tools.applySelector(document, selector);
		if (nodes.size() == 0) {
			return null;
		} else {
			return nodes.elementAt(0);
		}
	}

	@Override
	public Node selectCommentDateInComment(Node comment) throws DefrostException {
		String selector = selectorOfCommentDateInComment();
		return tools.applySelectorGetFirst(comment, selector);
	}

	@Override
	public Node selectCommentContentInComment(Node comment) throws DefrostException {
		String selector = selectorOfCommentContentInComment();
		return tools.applySelectorGetFirst(comment, selector);
	}

	@Override
	public Node selectAuthorIdInComment(Node comment) throws DefrostException {
		String selector = selectorOfIdInComment();
		return tools.applySelectorGetFirst(comment, selector);
	}

	@Override
	public Node selectAuthorNameInComment(Node comment) throws DefrostException {
		String selector = selectorOfNameInComment();
		return tools.applySelectorGetFirst(comment, selector);
	}

	///////////////////////////////////////////////////////////////////////////

	public abstract String selectorOfPostItemsInCategorySite();

	public abstract String selectorOfPostLinkInPostItem();

	public abstract String selectorOfCategoryNextPageButton();

	public abstract String selectorOfTitleInCommentSite();

	public abstract String selectorOfDiscussElementInPostSite();

	public abstract String selectorOfPostNextPageButton();

	public abstract String selectorOfCommentsElementInDiscuss();

	public abstract String selectorOfIdInComment();

	public abstract String selectorOfNameInComment();

	public abstract String selectorOfCommentDateInComment();

	public abstract String selectorOfCommentContentInComment();

	///////////////////////////////////////////////////////////////////////////

}
