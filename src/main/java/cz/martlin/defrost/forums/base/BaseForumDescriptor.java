package cz.martlin.defrost.forums.base;

import java.net.URL;
import java.util.Calendar;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.dataobj.User;

/**
 * Represents forum, how to find particular informations on the parsed sites.
 * 
 * @author martin
 *
 */
public interface BaseForumDescriptor {

	/**
	 * Returns the description of forum.
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Lists all avaible cateogories this forum supports. Assuming it is static.
	 * 
	 * @return
	 */
	public String[] listAvaibleCategories();

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Returns URL of html document containing given page of given category's
	 * posts.
	 * 
	 * @param categoryID
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public URL urlOfCategory(String categoryID, int page) throws Exception;

	/**
	 * Returns list of nodes of posts items.
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public NodeList findPostItems(Html document) throws Exception;

	/**
	 * From given post item (and category) infers post info.
	 * 
	 * @param postItem
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public PostInfo postItemToPostInfo(Node postItem, String category) throws Exception;

	/**
	 * Returns true if this site has next one.
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public boolean hasCategoryNextPage(Html document) throws Exception;

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Returns URL of given page of discuss specified by given post.
	 * 
	 * @param identifier
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public URL urlOfPost(PostIdentifier identifier, int page) throws Exception;

	/**
	 * Returns post identifier of given post and category.
	 * 
	 * @param url
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public PostIdentifier identifierOfPost(URL url, String category) throws Exception;

	/**
	 * Returns true if given post site has next site.
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public boolean hasPostNextPage(Html document) throws Exception;

	///////////////////////////////////////////////////////////////////////////

	/**
	 * In given site finds post info.
	 * 
	 * @param identifier
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public PostInfo findPostInfoInPost(PostIdentifier identifier, Html document) throws Exception;

	/**
	 * Returns list of elements representing the comments.
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public NodeList findPostDiscussElements(Html document) throws Exception;

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Infers the user of comment.
	 * 
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public User findCommentAuthor(Node comment) throws Exception;

	/**
	 * Infers the date of comment.
	 * 
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public Calendar findCommentDate(Node comment) throws Exception;

	/**
	 * Infers the text content of comment.
	 * 
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public String findCommentContent(Node comment) throws Exception;

	///////////////////////////////////////////////////////////////////////////

}
