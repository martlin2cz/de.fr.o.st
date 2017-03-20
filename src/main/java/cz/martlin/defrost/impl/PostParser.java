package cz.martlin.defrost.impl;

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
import cz.martlin.defrost.tools.Networker;
import cz.martlin.defrost.tools.NetworkingException;

public class PostParser {

	private final ForumDescriptorBase desc;
	private final Networker networker;

	public PostParser(ForumDescriptorBase desc) {
		super();
		
		this.desc = desc;
		this.networker = new Networker();
	}

	public Post loadAndParse(URL url) throws NetworkingException {
		try {
			Html html = networker.query(url);
			String title = desc.inferPostTitle(html);
			NodeList cmts = desc.inferCommentsElements(html);
			List<Comment> comments = inferComments(cmts);

			return new Post(title, url, comments);
		} catch (NetworkingException e) {
			throw new NetworkingException("Cannot analyze post " + url.toExternalForm(), e);
		}
	}

	private List<Comment> inferComments(NodeList cmts) throws NetworkingException {
		List<Comment> comments = new LinkedList<>();

		for (int i = 0; i < cmts.size(); i++) {
			Node node = cmts.elementAt(i);

			Comment comment;
			try {
				comment = inferComment(node);
			} catch (NetworkingException e) {
				throw new NetworkingException("Cannot infer comment from :" + node.toHtml(), e);
			}
			comments.add(comment);
		}

		return comments;
	}

	private Comment inferComment(Node comment) throws NetworkingException {
		User author = desc.inferCommentAuthor(comment);
		Calendar date = desc.inferCommentDate(comment);
		String content = desc.inferCommentContent(comment);
		
		return new Comment(author, date, content);
	}

}
