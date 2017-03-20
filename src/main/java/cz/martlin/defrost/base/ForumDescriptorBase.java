package cz.martlin.defrost.base;

import java.util.Calendar;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.core.DefrostException;
import cz.martlin.defrost.dataobj.User;

/**
 * Describes the structure of post and comments. Specifies how to infer various
 * data from HTML DOM of post.
 * 
 * @author martin
 *
 */
public interface ForumDescriptorBase {

	/**
	 * From given post infers title of post.
	 * 
	 * @param document
	 * @return
	 * @throws DefrostException
	 */
	public String inferPostTitle(Html document) throws DefrostException;

	/**
	 * Lists elements of comments.
	 * 
	 * @param document
	 * @return
	 * @throws DefrostException
	 */
	public NodeList inferCommentsElements(Html document) throws DefrostException;

	/**
	 * From given comment node infers the author.
	 * 
	 * @param coment
	 * @return
	 * @throws DefrostException
	 */
	public User inferCommentAuthor(Node coment) throws DefrostException;

	/**
	 * From given comment node infers the date of comment.
	 * 
	 * @param coment
	 * @return
	 * @throws DefrostException
	 */
	public Calendar inferCommentDate(Node coment) throws DefrostException;

	/**
	 * From given comment node infers the (html) content of comment.
	 * 
	 * @param coment
	 * @return
	 * @throws DefrostException
	 */
	public String inferCommentContent(Node coment) throws DefrostException;

}
