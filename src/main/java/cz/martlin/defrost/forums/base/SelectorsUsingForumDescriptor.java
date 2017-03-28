package cz.martlin.defrost.forums.base;

import java.text.DateFormat;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.utils.DefrostException;

/**
 * Extends the {@link CommonForumDescriptor} in way that all the
 * <code>select</code>* uses CSS selectors given by abstract methods
 * <code>selectorOf</code>* specified in this class. If selector is not enough,
 * particular <code>select</code>-method can be overriden.
 * 
 * @author martin
 *
 */
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
		String selector = selectorOfTitleInPostSite();
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
		String selector = selectorOfAuthorIdInComment();
		return tools.applySelectorGetFirst(comment, selector);
	}

	@Override
	public Node selectAuthorNameInComment(Node comment) throws DefrostException {
		String selector = selectorOfAuthorNameInComment();
		return tools.applySelectorGetFirst(comment, selector);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Returns selector of post items list in category site.
	 * @return
	 */
	public abstract String selectorOfPostItemsInCategorySite();

	/**
	 * Returns selector of post link in post item.
	 * @return
	 */
	public abstract String selectorOfPostLinkInPostItem();

	/**
	 * Returns selector of next page button in category site.
	 * @return
	 */
	public abstract String selectorOfCategoryNextPageButton();

	/**
	 * Returns selector of post in post site.
	 * @return
	 */
	public abstract String selectorOfTitleInPostSite();

	/**
	 * Returns selector of discuss element in post site.
	 * @return
	 */
	public abstract String selectorOfDiscussElementInPostSite();

	/**
	 * Returns selector of next page button in post site.
	 * @return
	 */
	public abstract String selectorOfPostNextPageButton();

	/**
	 * Returns selector of comments element in post discuss element.
	 * @return
	 */
	public abstract String selectorOfCommentsElementInDiscuss();

	/**
	 * Returns selector of author's id in comment element.
	 * @return
	 */
	public abstract String selectorOfAuthorIdInComment();

	/**
	 * Returns selector of author's name in comment element.
	 * @return
	 */
	public abstract String selectorOfAuthorNameInComment();

	/**
	 * Returns selector of comments's date in comment element.
	 * @return
	 */
	public abstract String selectorOfCommentDateInComment();

	/**
	 * Returns selector of comment content in comment element.
	 * @return
	 */
	public abstract String selectorOfCommentContentInComment();

	///////////////////////////////////////////////////////////////////////////

}
