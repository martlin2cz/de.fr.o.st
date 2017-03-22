package cz.martlin.defrost.core;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.PostInfo;

public class CategoryParser {
	private final Logger LOG = Logger.getLogger(getClass().getName());

	private final Networker networker;
	private final BaseForumDescriptor desc;

	public CategoryParser(BaseForumDescriptor desc) {
		super();
		this.networker = new Networker();
		this.desc = desc;
	}

	public List<PostInfo> listPosts(String category, int page) throws DefrostException {
		URL url;
		try {
			url = desc.urlOfCategory(category, page);
		} catch (Exception e) {
			throw new DefrostException("Cannot construct URL of category", e);
		}

		Html document;
		try {
			document = networker.query(url);
		} catch (Exception e) {
			throw new DefrostException("Cannot download category site", e);
		}

		List<PostInfo> posts;
		try {
			posts = parsePosts(document);
		} catch (Exception e) {
			throw new DefrostException("Cannot parse posts of category", e);
		}

		return posts;
	}

	private List<PostInfo> parsePosts(Html document) throws Exception {
		List<PostInfo> posts = new LinkedList<>();

		NodeList items = desc.findPostItems(document);

		for (int i = 0; i < items.size(); i++) {
			Node item = items.elementAt(i);

			PostInfo info;
			try {
				info = desc.postItemToPostInfo(item);
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Cannot find post info in post item", e);
				continue;
			}
			posts.add(info);
		}

		return posts;
	}

}
