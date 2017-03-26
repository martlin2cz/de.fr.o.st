package cz.martlin.defrost.input.load;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.PagedDataResult;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.input.tools.Networker;
import cz.martlin.defrost.misc.DefrostException;

public class CategoryParser {
	private final Logger LOG = Logger.getLogger(getClass().getName());

	private final Networker networker;
	private final BaseForumDescriptor desc;

	public CategoryParser(BaseForumDescriptor desc) {
		super();
		this.networker = new Networker();
		this.desc = desc;
	}

	public PagedDataResult<List<PostInfo>> listPosts(String category, int page) throws DefrostException {
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
			posts = parsePosts(document, category);
		} catch (Exception e) {
			throw new DefrostException("Cannot parse posts of category", e);
		}

		boolean hasNextPage;
		try {
			hasNextPage = desc.hasCategoryNextPage(document);
		} catch (Exception e) {
			throw new DefrostException("Cannot find if has next page", e);
		}

		return new PagedDataResult<List<PostInfo>>(posts, page, hasNextPage);
	}

	private List<PostInfo> parsePosts(Html document, String category) throws Exception {
		List<PostInfo> posts = new LinkedList<>();

		NodeList items = desc.findPostItems(document);

		for (int i = 0; i < items.size(); i++) {
			Node item = items.elementAt(i);

			PostInfo post;
			try {
				post = desc.postItemToPostInfo(item, category);
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Cannot find post info in post item", e);
				continue;
			}
			posts.add(post);
		}

		return posts;
	}

}
