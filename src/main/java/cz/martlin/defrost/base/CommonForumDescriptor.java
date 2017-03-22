package cz.martlin.defrost.base;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.core.ParserTools;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.dataobj.User;

public abstract class CommonForumDescriptor implements BaseForumDescriptor {

	public static final String CATEGORY_ID_NEEDLE = "${category_id}";
	public static final String POST_ID_NEEDLE = "${post_id}";
	public static final String PAGE_NUMBER_NEEDLE = "${page_number}";

	protected final ParserTools tools;
	protected final String categorySiteURLPattern;
	protected final String postSiteURLPattern;
	protected final DateFormat commentDateFormat;

	public CommonForumDescriptor(String categorySiteURLPattern, String postSiteURLPattern,
			DateFormat commentDateFormat) {
		super();
		this.tools = new ParserTools();

		this.categorySiteURLPattern = categorySiteURLPattern;
		this.postSiteURLPattern = postSiteURLPattern;
		this.commentDateFormat = commentDateFormat;
	}

	///////////////////////////////////////////////////////////////////////////

	@Override
	public URL urlOfCategory(String categoryID, int page) throws IllegalArgumentException {
		PostIdentifier fakePost = new PostIdentifier(categoryID, "");
		String path = renderPath(categorySiteURLPattern, fakePost, page);
		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid url of category " + categoryID, e);
		}
	}

	@Override
	public URL urlOfPost(PostIdentifier identifier, int page) throws IllegalArgumentException {
		String path = renderPath(postSiteURLPattern, identifier, page);
		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid url of post " + identifier, e);
		}
	}

	@Override
	public PostIdentifier identifierOfPost(URL url) {
		String id = postUrlToPostId(url);
		String category = postUrlToCategoryId(url);

		return new PostIdentifier(category, id);
	}

	public abstract String postUrlToPostId(URL url);

	public abstract String postUrlToCategoryId(URL url);

	///////////////////////////////////////////////////////////////////////////

	@Override
	public NodeList findPostItems(Html document) throws Exception {
		return selectPostItemInCategorySite(document);
	}

	@Override
	public PostInfo postItemToPostInfo(Node postItem) throws Exception {
		URL url = findPostURLInPostItem(postItem);
		PostIdentifier identifier = identifierOfPost(url);

		String title = findPostTitleInPostItem(postItem);
		return new PostInfo(title, identifier);
	}

	protected LinkTag findPostItemElement(Node postItem) throws Exception {
		return selectPostLinkInPostItem(postItem);
	}

	protected URL findPostURLInPostItem(Node postItem) throws Exception {
		LinkTag link = findPostItemElement(postItem);
		String path = link.getLink();

		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid url of post" + path, e);
		}
	}

	protected String findPostTitleInPostItem(Node postItem) throws Exception {
		LinkTag link = findPostItemElement(postItem);
		return tools.inferTextInside(link);
	}

	///////////////////////////////////////////////////////////////////////////

	@Override
	public PostInfo findPostInfoInPost(PostIdentifier identifier, Html document) throws Exception {
		String title = findPostTitleInPost(document);
		return new PostInfo(title, identifier);
	}

	@Override
	public NodeList findPostDiscussElements(Html document) throws Exception {
		Node discuss = selectDiscussElementInPostSite(document);
		NodeList comments = selectCommentsElementInDiscuss(discuss);

		return comments;
	}

	protected String findPostTitleInPost(Html document) throws Exception {
		Node node = selectTitleInPostSite(document);
		return tools.inferTextInside(node);

	}
	///////////////////////////////////////////////////////////////////////////

	@Override
	public User findCommentAuthor(Node comment) throws Exception {
		String name = findCommentAuthorName(comment);
		String id = findCommentAuthorId(comment);

		return new User(id, name);
	}

	@Override
	public Calendar findCommentDate(Node comment) throws Exception {
		Node node = selectorCommentDateInComment(comment);
		String text = tools.inferTextInside(node);
		return parseDateFromDateNode(text);
	}

	@Override
	public String findCommentContent(Node comment) throws Exception {
		Node node = selectCommentContentInComment(comment);
		NodeList children = node.getChildren();
		String html = children.toHtml();
		return html.trim();
	}

	///////////////////////////////////////////////////////////////////////////

	protected String findCommentAuthorId(Node comment) throws Exception {
		Node idNode = selectAuthorIdInComment(comment);
		String id = tools.inferTextInside(idNode);
		return id;
	}

	protected String findCommentAuthorName(Node comment) throws Exception {
		Node nameNode = selectAuthorNameInComment(comment);
		String name = tools.inferTextInside(nameNode);
		return name;
	}

	///////////////////////////////////////////////////////////////////////////
	public abstract NodeList selectPostItemInCategorySite(Html document) throws Exception;

	public abstract LinkTag selectPostLinkInPostItem(Node postItem) throws Exception;

	public abstract Node selectDiscussElementInPostSite(Html document) throws Exception;

	public abstract NodeList selectCommentsElementInDiscuss(Node discuss) throws Exception;

	public abstract Node selectTitleInPostSite(Html document) throws Exception;

	public abstract Node selectorCommentDateInComment(Node comment) throws Exception;

	public abstract Node selectCommentContentInComment(Node comment) throws Exception;

	public abstract Node selectAuthorIdInComment(Node comment) throws Exception;

	public abstract Node selectAuthorNameInComment(Node comment) throws Exception;

	///////////////////////////////////////////////////////////////////////////

	protected static String renderPath(String pattern, PostIdentifier identifier, int page) {
		String result = pattern;

		result = result.replace(CATEGORY_ID_NEEDLE, identifier.getCategory());
		result = result.replace(POST_ID_NEEDLE, identifier.getId());
		result = result.replace(PAGE_NUMBER_NEEDLE, Integer.toString(page));

		return result;
	}

	protected Calendar parseDateFromDateNode(String text) {

		try {
			Date date = commentDateFormat.parse(text);
			return tools.dateToCalendar(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Cannot parse date " + text, e);
		}
	}

}
