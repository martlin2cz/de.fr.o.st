package cz.martlin.defrost.core;

import java.net.URL;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.base.ForumDescriptorBase;
import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.Post;
import cz.martlin.defrost.dataobj.User;

/**
 * Does the parsing of the one post, based on {@link ForumDescriptorBase}
 * instance.
 * 
 * @author martin
 *
 */
public class PostParser {

	private final ForumDescriptorBase desc;
	private final Networker networker;

	public PostParser(ForumDescriptorBase desc) {
		super();

		this.desc = desc;
		this.networker = new Networker();
	}

	/**
	 * Loads and parses the post on given url.
	 * 
	 * @param url
	 * @return
	 * @throws DefrostException
	 */
	public Post loadAndParse(URL url) throws DefrostException {
		try {
			Html html = networker.query(url);
			String title = desc.inferPostTitle(html);
			NodeList cmts = desc.inferCommentsElements(html);
			List<Comment> comments = inferComments(cmts);

			return new Post(title, url, comments);
		} catch (DefrostException e) {
			throw new DefrostException("Cannot analyze post " + url.toExternalForm(), e);
		}
	}

	/**
	 * Infers comments from given list of comments elements.
	 * 
	 * @param cmts
	 * @return
	 * @throws DefrostException
	 */
	private List<Comment> inferComments(NodeList cmts) throws DefrostException {
		List<Comment> comments = new LinkedList<>();

		for (int i = 0; i < cmts.size(); i++) {
			Node node = cmts.elementAt(i);

			Comment comment;
			try {
				comment = inferComment(node);
			} catch (DefrostException e) {
				throw new DefrostException("Cannot infer comment from: " + node.toHtml(), e);
			}
			comments.add(comment);
		}

		return comments;
	}

	/**
	 * From given comment element parses and creates comment object.
	 * 
	 * @param comment
	 * @return
	 * @throws DefrostException
	 */
	private Comment inferComment(Node comment) throws DefrostException {
		User author = desc.inferCommentAuthor(comment);
		Calendar date = desc.inferCommentDate(comment);
		String content = desc.inferCommentContent(comment);

		return new Comment(author, date, content);
	}

}
