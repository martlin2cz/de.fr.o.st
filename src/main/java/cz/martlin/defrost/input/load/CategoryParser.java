package cz.martlin.defrost.input.load;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

import cz.martlin.defrost.dataobj.PagedDataResult;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.forums.base.BaseForumDescriptor;
import cz.martlin.defrost.utils.DefrostException;
import cz.martlin.defrost.utils.Networker;

/**
 * Implements parsing of category site.
 * @author martin
 *
 */
public class CategoryParser {

	private final Networker networker;
	private final BaseForumDescriptor desc;

	public CategoryParser(BaseForumDescriptor desc) {
		super();
		this.networker = new Networker();
		this.desc = desc;
	}

	/**
	 * Lists posts of given category and given page.
	 * @param category
	 * @param page
	 * @return
	 * @throws DefrostException
	 */
	public PagedDataResult<List<PostInfo>> listPosts(String category, int page) throws DefrostException {
		URL url;
		try {
			url = desc.urlOfCategory(category, page);
		} catch (Exception e) {
			throw new DefrostException(Messages.getString("Cannot_construct_URL_of_category"), e); //$NON-NLS-1$
		}

		Html document;
		try {
			document = networker.query(url);
		} catch (Exception e) {
			throw new DefrostException(Messages.getString("Cannot_download_category_site"), e); //$NON-NLS-1$
		}

		List<PostInfo> posts;
		try {
			posts = parsePosts(document, category);
		} catch (Exception e) {
			throw new DefrostException(Messages.getString("Cannot_parse_posts_of_category"), e); //$NON-NLS-1$
		}

		boolean hasNextPage;
		try {
			hasNextPage = desc.hasCategoryNextPage(document);
		} catch (Exception e) {
			throw new DefrostException(Messages.getString("Cannot_find_if_has_next_page"), e); //$NON-NLS-1$
		}

		return new PagedDataResult<List<PostInfo>>(posts, page, hasNextPage);
	}

	/**
	 * From given site's document parses posts.
	 * @param document
	 * @param category
	 * @return
	 * @throws Exception
	 */
	private List<PostInfo> parsePosts(Html document, String category) throws Exception {
		List<PostInfo> posts = new LinkedList<>();

		NodeList items = desc.findPostItems(document);

		for (int i = 0; i < items.size(); i++) {
			Node item = items.elementAt(i);

			PostInfo post;
			try {
				post = desc.postItemToPostInfo(item, category);
			} catch (Exception e) {
				throw new DefrostException(Messages.getString("Cannot_find_post_info_in_post_item"), e); //$NON-NLS-1$
			}
			posts.add(post);
		}

		return posts;
	}

}
