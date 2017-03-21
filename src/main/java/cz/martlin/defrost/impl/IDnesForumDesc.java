package cz.martlin.defrost.impl;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import cz.martlin.defrost.base.CommonForumDescriptor;

public class IDnesForumDesc extends CommonForumDescriptor {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy hh:mm");

	public IDnesForumDesc() {
		super(//
				"http://" + CATEGORY_ID_NEEDLE + ".idnes.cz/archiv.aspx?strana=" + PAGE_NUMBER_NEEDLE, //
				"http://" + CATEGORY_ID_NEEDLE + ".idnes.cz/archiv.aspx?c=" + POST_ID_NEEDLE, //
				DATE_FORMAT);//

	}

	@Override
	public String postUrlToPostId(URL url) {
		// TODO okej, easy
		return null;
	}

	@Override
	public String postUrlToCategoryId(URL url) {
		// TODO okey, easy
		return null;
	}

	@Override
	public String selectorOfPostsItemsInCategorySite() {
		// TODO ok
		return null;
	}

	@Override
	public String selectorOfPostLinkInPostItem() {
		// TODO ok
		return null;
	}

	@Override
	public String selectorOfTitleInPostSite() {
		// TODO ok, easy
		return null;
	}

	@Override
	public String selectorOfDiscussElementInPostSite() {
		// TODO ok
		return null;
	}

	@Override
	public String selectorOfCommentsElementInDiscuss() {
		// TODO okay
		return null;
	}

	@Override
	public String selectorOfIdInComment() {
		// TODO ok?
		return null;
	}

	@Override
	public String selectorOfNameInComment() {
		// TODO ok?
		return null;
	}

	@Override
	public String selectorOfCommentDateInComment() {
		// TODO okay
		return null;
	}

	@Override
	public String selectorOfCommentContentInComment() {
		// TODO okay, easy
		return null;
	}

}
