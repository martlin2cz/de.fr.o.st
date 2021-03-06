package cz.martlin.defrost.forums.impl;

import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.forums.base.SelectorsUsingForumDescriptor;
import cz.martlin.defrost.utils.DefrostException;

public class EmiminoForumDesc extends SelectorsUsingForumDescriptor {

	public EmiminoForumDesc() {
		super(//
				"http://www.emimino.cz/diskuse/" + CATEGORY_ID_NEEDLE + "/strankovani/" + PAGE_NUMBER_NEEDLE, //
				"http://www.emimino.cz/diskuse/" + POST_ID_NEEDLE + "/strankovani/" + PAGE_NUMBER_NEEDLE, //
				new EmiminoDateFormat()); //
	}

	@Override
	public String getDescription() {
		return "emimino.cz";
	}

	@Override
	public String[] listAvaibleCategories() {
		return new String[] { "o-bazaru", // 2 pages
				"jak-na-emimino", // 30 pages
				"hobby-zajmy", // 144 pages
				"hledame-maminky", // 178 pages
				"kocarky", // 196 pages
				"vybavicka-pro-miminko", // 236 pages
				"snazime-se-o-miminko", // 317 pages
				"finance", // 356 pages
				"vztahy", // 359 pages
				"od-batolete-do-puberty", // 421 pages
				"bydleni-domacnost", // 474 pages
				"tehotenstvi-porod", // 536 pages
				"novorozenec-kojenec", // 644 pages
				"co-trapi-maminky", // 660 pages
				"zdravi", // 684 pages
				// "xxxx-testing-error", //
		};
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

	///////////////////////////////////////////////////////////////////////////

	@Override
	public String selectorOfPostItemsInCategorySite() {
		return "span.topic";
	}

	@Override
	public String selectorOfPostLinkInPostItem() {
		return "a";
	}

	@Override
	public String selectorOfCategoryNextPageButton() {
		throw new UnsupportedOperationException("neded to rewrite");
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
	public String selectorOfPostNextPageButton() {
		throw new UnsupportedOperationException("neded to rewrite");
	}

	@Override
	public String selectorOfCommentsElementInDiscuss() {
		throw new UnsupportedOperationException("multiple CSS classes, needed to rewrite #selectPostsElementInDiscuss");
	}

	@Override
	public String selectorOfAuthorIdInComment() {
		return "div.user div.user_in a";
	}

	@Override
	public String selectorOfAuthorNameInComment() {
		return "div.user div.user_in a";
	}

	@Override
	public String selectorOfCommentDateInComment() {
		// warning: there are two span.date elements siblings, but the (second)
		// one with the date contains inner span
		return "div.user div.user_in span.date span";
	}

	@Override
	public String selectorOfCommentContentInComment() {
		return "div.post div.post_in";
	}

	///////////////////////////////////////////////////////////////////////////

	@Override
	public Node selectCategoryNextPageButton(Html document) throws Exception {
		return findPagerIfHasNexButton(document);
	}

	@Override
	public Node selectPostNextPageButton(Html document) throws Exception {
		return findPagerIfHasNexButton(document);
	}

	@Override
	public NodeList selectCommentsElementInDiscuss(Node discuss) {
		NodeFilter firstCommentFilter = new HasAttributeFilter("class", "discussion_post first vycisti");
		NodeFilter othersCommentsFilter = new HasAttributeFilter("class", "discussion_post  vycisti ");
		NodeFilter filter = new OrFilter(firstCommentFilter, othersCommentsFilter);

		return tools.applyFilter(discuss, filter);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the pager element (if exists).
	 * 
	 * @param document
	 * @return
	 * @throws DefrostException
	 */
	private Node findPagerIfHasNexButton(Html document) throws DefrostException {
		NodeFilter filter = new CssSelectorNodeFilter("div.pager");
		NodeList nodes = tools.applyFilter(document, filter);

		if (nodes.size() == 0) {
			return null;
		}

		Node node = nodes.elementAt(0);
		String text = node.toPlainTextString();

		if (text.contains("Další")) {
			return node;
		} else {
			return null;
		}
	}

}
