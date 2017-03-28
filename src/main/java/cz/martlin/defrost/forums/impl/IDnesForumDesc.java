package cz.martlin.defrost.forums.impl;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.HasAttributeFilter;

import cz.martlin.defrost.forums.base.SelectorsUsingForumDescriptor;
import cz.martlin.defrost.utils.DefrostException;

public class IDnesForumDesc extends SelectorsUsingForumDescriptor {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy hh:mm");

	public IDnesForumDesc() {
		super(//
				"http://" + CATEGORY_ID_NEEDLE + ".idnes.cz/archiv.aspx?strana=" + PAGE_NUMBER_NEEDLE, //
				"http://" + CATEGORY_ID_NEEDLE + ".idnes.cz/diskuse.aspx?iddiskuse=" + POST_ID_NEEDLE, //
				DATE_FORMAT);//

		// article url:
		// "http://" + CATEGORY_ID_NEEDLE + ".idnes.cz/archiv.aspx?c="+
		// Comment_ID_NEEDLE

	}

	@Override
	public String getDescription() {
		return "idnes.cz";
	}

	@Override
	public String[] listAvaibleCategories() {
		return new String[] { "zpravy", "ona", "technet", "sport" };
	}

	///////////////////////////////////////////////////////////////////////////

	@Override
	public String postUrlToPostId(URL url) {
		Map<String, String> params = tools.getUrlQueryParams(url);

		return params.get("c");
	}

	///////////////////////////////////////////////////////////////////////////

	@Override
	public String selectorOfPostItemsInCategorySite() {
		return "div.art h3";
	}

	@Override
	public String selectorOfPostLinkInPostItem() {
		return "a";
	}

	@Override
	public String selectorOfTitleInPostSite() {
		return "h3 a";
	}

	@Override
	public String selectorOfCategoryNextPageButton() {
		return "td.tar a.ico-right span";
	}

	@Override
	public String selectorOfDiscussElementInPostSite() {
		return "div#disc-list";
	}

	@Override
	public String selectorOfCommentsElementInDiscuss() {
		return "div.contribution";
	}

	@Override
	public String selectorOfPostNextPageButton() {
		return "td.tar a.ico-right span";
	}

	@Override
	public String selectorOfAuthorIdInComment() {
		return "h4.name";
	}

	@Override
	public String selectorOfAuthorNameInComment() {
		return "h4.name a";
	}

	@Override
	public String selectorOfCommentDateInComment() {
		throw new UnsupportedOperationException("Not working, need to rewrite #findCommentDate");
	}

	@Override
	public String selectorOfCommentContentInComment() {
		return "table tr td.cell div";
	}

	///////////////////////////////////////////////////////////////////////////

	@Override
	public Node selectCommentDateInComment(Node comment) throws DefrostException {
		NodeFilter filter = new HasAttributeFilter("class", "date hover");
		return tools.applyFilterGetFirst(comment, filter);
	}

	///////////////////////////////////////////////////////////////////////////

	@Override
	protected String findCommentAuthorId(Node comment) throws Exception {
		String id = super.findCommentAuthorId(comment);
		return id.replaceAll("\\s+", "");
	}

	@Override
	protected String findCommentAuthorName(Node comment) throws Exception {
		String name = super.findCommentAuthorName(comment);
		return name.replaceAll("\\d+", "");
	}
}
