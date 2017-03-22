package cz.martlin.defrost.impl;

import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.base.SelectorsUsingForumDescriptor;

public class EmiminoForumDesc extends SelectorsUsingForumDescriptor {

	public EmiminoForumDesc() {
		super(//
				"http://www.emimino.cz/diskuse/" + CATEGORY_ID_NEEDLE + "/strankovani/" + PAGE_NUMBER_NEEDLE, //
				"http://www.emimino.cz/diskuse/" + POST_ID_NEEDLE + "/strankovani/" + PAGE_NUMBER_NEEDLE, //
				new EmiminoDateFormat()); //
	}

	///////////////////////////////////////////////////////////////////////////

	@Override
	public String postUrlToPostId(URL url) {
		final String prefix = "/diskuse/";
		String path = url.getPath();

		int start = path.indexOf(prefix);
		int begin = start + prefix.length();
		int end = path.indexOf("/", begin + 1);

		String id = path.substring(begin, end);
		return id;
	}

	@Override
	public String postUrlToCategoryId(URL url) {
		return "???"; // TODO FIXME ...
	}
	///////////////////////////////////////////////////////////////////////////

	@Override
	public String selectorOfPostsItemsInCategorySite() {
		return "span.topic";
	}

	@Override
	public String selectorOfPostLinkInPostItem() {
		return "a";
	}

	@Override
	public String selectorOfTitleInPostSite() {
		return "h1";
	}

	@Override
	public String selectorOfDiscussElementInPostSite() {
		return "div#discussionPostList";
	}

	@Override
	public String selectorOfCommentsElementInDiscuss() {
		throw new UnsupportedOperationException(
				"multiple CSS classes, needed to rewrite #selectCommentsElementInDiscuss");
		// return "div.discussion_post";
	}

	@Override
	public String selectorOfIdInComment() {
		return "div.user div.user_in a";
	}

	@Override
	public String selectorOfNameInComment() {
		return "div.user div.user_in a";
	}

	@Override
	public String selectorOfCommentDateInComment() {
		//warning: there are two span.date elements siblings, but the (second) one with the date contains inner span
		return "div.user div.user_in span.date span";	 
	}

	@Override
	public String selectorOfCommentContentInComment() {
		return "div.post div.post_in";
	}

	///////////////////////////////////////////////////////////////////////////

	@Override
	public NodeList selectCommentsElementInDiscuss(Node discuss) {
		NodeFilter firstPostFilter = new HasAttributeFilter("class", "discussion_post first vycisti");
		NodeFilter othersPostsFilter = new HasAttributeFilter("class", "discussion_post  vycisti ");
		NodeFilter filter = new OrFilter(firstPostFilter, othersPostsFilter);

		return tools.applyFilter(discuss, filter);
		// return super.selectCommentsElementInDiscuss(discuss);
	}
/*
	@Override
	public Node selectAuthorIdInComment(Node comment) throws DefrostException {
	NodeFilter userFilter = new CssSelectorNodeFilter("div.user div.user_in");
	Node userNode = tools.applyFilterGetFirst(comment, userFilter);
	
		return userNode;	// .filter("b");
	}

	@Override
	public Node selectAuthorNameInComment(Node comment) throws DefrostException {
		NodeFilter userFilter = new CssSelectorNodeFilter("div.user div.user_in a");
		Node linkNode = tools.applyFilterGetFirst(comment, userFilter);
		
		//NodeFilter linkFilter = new CssSelectorNodeFilter("a");
		//Node userNode = tools.applyFilterGetFirst(comment, userFilter);
		
			return linkNode;	// .filter("b");
	}
*/
	/*
	@Override
	public Node selectorCommentDateInComment(Node comment) throws DefrostException {
		NodeFilter dateFilter = new CssSelectorNodeFilter("");
		
		// TODO Auto-generated method stub
		return super.selectorCommentDateInComment(comment);
	}
*/
}
