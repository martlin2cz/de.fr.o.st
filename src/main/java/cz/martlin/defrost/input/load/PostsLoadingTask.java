package cz.martlin.defrost.input.load;

import java.util.ArrayList;
import java.util.List;

import cz.martlin.defrost.dataobj.PagedDataResult;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.forums.base.BaseForumDescriptor;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;
import cz.martlin.defrost.tasks.ItemsLoadingTask;
import cz.martlin.defrost.utils.DefrostException;

/**
 * Task implementing loading of posts of categories.
 * 
 * @author martin
 *
 */
public class PostsLoadingTask extends ItemsLoadingTask<List<PostInfo>> {
	private final CategoryParser parser;
	private final List<String> categories;

	public PostsLoadingTask(BaseForumDescriptor descriptor, BaseLoadingIndicator indicator, List<String> categories) {
		super(Messages.getString("Loading_posts"), Messages.getString("Loading_posts_of"), indicator); //$NON-NLS-1$ //$NON-NLS-2$

		this.parser = new CategoryParser(descriptor);
		this.categories = categories;
	}

	@Override
	protected List<PostInfo> call() throws Exception {
		return loadPosts(categories);
	}

	/**
	 * Loads posts of given categories.
	 * 
	 * @param categories
	 * @return
	 */
	private List<PostInfo> loadPosts(List<String> categories) {
		List<PostInfo> result = new ArrayList<>();

		loadingStarted(categories.size());

		for (String category : categories) {
			if (isCancelled()) {
				break;
			}

			loadingNextItem(category);

			List<PostInfo> posts = loadPostsOfCategory(category);
			result.addAll(posts);

			updateValue(null);
			updateValue(result);
		}

		loadingFinished();
		updateValue(result);

		return result;
	}

	/**
	 * Loads posts of given category.
	 * 
	 * @param category
	 * @return
	 */
	private List<PostInfo> loadPostsOfCategory(String category) {
		List<PostInfo> result = new ArrayList<>();

		for (int i = 0; true; i++) {
			if (isCancelled()) {
				break;
			}

			loadingItemUpdated(Messages.getString("page") + " " + i); //$NON-NLS-1$
			PagedDataResult<List<PostInfo>> posts;
			try {
				posts = parser.listPosts(category, i);
			} catch (DefrostException e) {
				error(e);
				continue;
			}

			result.addAll(posts.getData());

			if (!posts.isHasNextPage()) {
				break;
			}
		}

		return result;
	}

}
