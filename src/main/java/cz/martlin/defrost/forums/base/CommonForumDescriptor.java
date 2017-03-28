package cz.martlin.defrost.forums.base;

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

import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.dataobj.User;
import cz.martlin.defrost.utils.ParserTools;

/**
 * Implements the common forum. Take look at abstract <code>select</code>*
 * methods, to see how this class works.
 * 
 * @author martin
 *
 */
public abstract class CommonForumDescriptor implements BaseForumDescriptor {

	public static final String CATEGORY_ID_NEEDLE = "${category_id}";
	public static final String POST_ID_NEEDLE = "${comment_id}";
	public static final String PAGE_NUMBER_NEEDLE = "${page_number}";

	protected final ParserTools tools;
	protected final String categorySiteURLPattern;
	protected final String commentSiteURLPattern;
	protected final DateFormat commentDateFormat;

	public CommonForumDescriptor(String categorySiteURLPattern, String commentSiteURLPattern,
			DateFormat commentDateFormat) {
		super();
		this.tools = new ParserTools();

		this.categorySiteURLPattern = categorySiteURLPattern;
		this.commentSiteURLPattern = commentSiteURLPattern;
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
		String path = renderPath(commentSiteURLPattern, identifier, page);
		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid url of post " + identifier, e);
		}
	}

	@Override
	public PostIdentifier identifierOfPost(URL url, String category) {
		String id = postUrlToPostId(url);

		return new PostIdentifier(category, id);
	}

	/**
	 * From given url of post infers id of post.
	 * 
	 * @param url
	 * @return
	 */
	public abstract String postUrlToPostId(URL url);

	@Override
	public boolean hasCategoryNextPage(Html document) throws Exception {
		Node node = selectCategoryNextPageButton(document);
		return node != null;
	}

	///////////////////////////////////////////////////////////////////////////

	@Override
	public NodeList findPostItems(Html document) throws Exception {
		return selectPostItemInCategorySite(document);
	}

	@Override
	public PostInfo postItemToPostInfo(Node postItem, String category) throws Exception {
		URL url = findPostURLInPostItem(postItem);
		PostIdentifier identifier = identifierOfPost(url, category);

		String title = findPostTitleInPostItem(postItem);
		return new PostInfo(title, identifier);
	}

	/**
	 * Finds link to post in post item.
	 * 
	 * @param postItem
	 * @return
	 * @throws Exception
	 */
	protected LinkTag findPostItemLinkElement(Node postItem) throws Exception {
		return selectPostLinkInPostItem(postItem);
	}

	/**
	 * In given post item finds URL of post.
	 * 
	 * @param postItem
	 * @return
	 * @throws Exception
	 */
	protected URL findPostURLInPostItem(Node postItem) throws Exception {
		LinkTag link = findPostItemLinkElement(postItem);
		String path = link.getLink();

		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid url of post" + path, e);
		}
	}

	/**
	 * In given post item find title of post.
	 * 
	 * @param postItem
	 * @return
	 * @throws Exception
	 */
	protected String findPostTitleInPostItem(Node postItem) throws Exception {
		LinkTag link = findPostItemLinkElement(postItem);
		return tools.inferTextInside(link);
	}

	@Override
	public boolean hasPostNextPage(Html document) throws Exception {
		Node node = selectPostNextPageButton(document);
		return node != null;
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

	/**
	 * In given post finds its title.
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
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
		Node node = selectCommentDateInComment(comment);
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

	/**
	 * In given comment finds comment's author's id.
	 * 
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	protected String findCommentAuthorId(Node comment) throws Exception {
		Node idNode = selectAuthorIdInComment(comment);
		String id = tools.inferTextInside(idNode);
		return id;
	}

	/**
	 * In given comment finds comment's author's name.
	 * 
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	protected String findCommentAuthorName(Node comment) throws Exception {
		Node nameNode = selectAuthorNameInComment(comment);
		String name = tools.inferTextInside(nameNode);
		return name;
	}

	///////////////////////////////////////////////////////////////////////////
	/**
	 * Returns list of post items in given category site.
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public abstract NodeList selectPostItemInCategorySite(Html document) throws Exception;

	/**
	 * Returns link to post in given post item.
	 * 
	 * @param postItem
	 * @return
	 * @throws Exception
	 */
	public abstract LinkTag selectPostLinkInPostItem(Node postItem) throws Exception;

	/**
	 * Returns the "next button" in category site or null if no such.
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public abstract Node selectCategoryNextPageButton(Html document) throws Exception;

	/**
	 * Returns element of the discuss.
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public abstract Node selectDiscussElementInPostSite(Html document) throws Exception;

	/**
	 * Returns the comments elements in the discuss.
	 * 
	 * @param discuss
	 * @return
	 * @throws Exception
	 */
	public abstract NodeList selectCommentsElementInDiscuss(Node discuss) throws Exception;

	/**
	 * Returns element with title of post.
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public abstract Node selectTitleInPostSite(Html document) throws Exception;

	/**
	 * Returns the "next button" of post or null if no such.
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public abstract Node selectPostNextPageButton(Html document) throws Exception;

	/**
	 * Returns element with the date of comment.
	 * 
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public abstract Node selectCommentDateInComment(Node comment) throws Exception;

	/**
	 * Returns element with the comment content.
	 * 
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public abstract Node selectCommentContentInComment(Node comment) throws Exception;

	/**
	 * Returns element with the author's id.
	 * 
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public abstract Node selectAuthorIdInComment(Node comment) throws Exception;

	/**
	 * Returns element with the author's name.
	 * 
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public abstract Node selectAuthorNameInComment(Node comment) throws Exception;

	///////////////////////////////////////////////////////////////////////////

	/**
	 * For given pattern (string possibly containing {@link #CATEGORY_ID_NEEDLE}
	 * , {@link #POST_ID_NEEDLE} or {@link #PAGE_NUMBER_NEEDLE}) replaces theese
	 * needles with given real values.
	 * 
	 * @param pattern
	 * @param identifier
	 * @param page
	 * @return
	 */
	protected static String renderPath(String pattern, PostIdentifier identifier, int page) {
		String result = pattern;

		result = result.replace(CATEGORY_ID_NEEDLE, identifier.getCategory());
		result = result.replace(POST_ID_NEEDLE, identifier.getId());
		result = result.replace(PAGE_NUMBER_NEEDLE, Integer.toString(page));

		return result;
	}

	/**
	 * Parses date and returns as {@link Calendar}.
	 * 
	 * @param text
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected Calendar parseDateFromDateNode(String text) throws IllegalArgumentException {

		try {
			Date date = commentDateFormat.parse(text);
			return ParserTools.dateToCalendar(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Cannot parse date " + text, e);
		}
	}

}
