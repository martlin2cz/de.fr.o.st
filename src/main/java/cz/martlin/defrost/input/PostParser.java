package cz.martlin.defrost.input;

import java.net.URL;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PagedDataResult;
import cz.martlin.defrost.dataobj.Post;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.dataobj.User;
import cz.martlin.defrost.misc.DefrostException;

public class PostParser {
	private final Logger LOG = Logger.getLogger(getClass().getName());

	private final BaseForumDescriptor desc;
	private final Networker networker;

	public PostParser(BaseForumDescriptor desc) {
		super();

		this.desc = desc;
		this.networker = new Networker();
	}

	/**
	 * Loads and parses the post on given url.
	 * 
	 * 
	 * @return
	 * @throws DefrostException
	 */
	public PagedDataResult<Post> loadAndParse(PostIdentifier identifier, int page) throws DefrostException {
		URL url;
		try {
			url = desc.urlOfPost(identifier, page);
		} catch (Exception e) {
			throw new DefrostException("Cannot create url of post", e);
		}

		Html html;
		try {
			html = networker.query(url);
		} catch (Exception e) {
			throw new DefrostException("Cannot download post", e);
		}

		PostInfo info;
		try {
			info = desc.findPostInfoInPost(identifier, html);
		} catch (Exception e) {
			throw new DefrostException("Cannot infer post title", e);
		}

		NodeList cmts;
		try {
			cmts = desc.findPostDiscussElements(html);
		} catch (Exception e) {
			throw new DefrostException("Cannot find comments", e);
		}

		List<Comment> comments;
		try {
			comments = inferComments(cmts);
		} catch (DefrostException e) {
			throw new DefrostException("Cannot parse comments", e);
		}

		boolean hasNextPage;
		try {
			hasNextPage = desc.hasPostNextPage(html);
		} catch (Exception e) {
			throw new DefrostException("Cannot find if post has next page", e);
		}

		Post post = new Post(info, comments);
		return new PagedDataResult<Post>(post, page, hasNextPage);
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
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Cannot infer comment", e);
				continue;
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
	 * @throws Exception
	 */
	private Comment inferComment(Node comment) throws Exception {
		User author = desc.findCommentAuthor(comment);
		Calendar date = desc.findCommentDate(comment);
		String content = desc.findCommentContent(comment);

		return new Comment(author, date, content);
	}

}
