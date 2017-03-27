package cz.martlin.defrost.input.load;

import java.util.ArrayList;
import java.util.List;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.PagedDataResult;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.misc.DefrostException;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;
import cz.martlin.defrost.tasks.ItemsLoadingTask;

public class PostsLoadingTask extends ItemsLoadingTask<List<PostInfo>> {
	private final CategoryParser parser;
	private final List<String> categories;

	public PostsLoadingTask(BaseForumDescriptor descriptor, BaseLoadingIndicator indicator, List<String> categories) {
		super("Loading posts", "Loading posts of", indicator);

		this.parser = new CategoryParser(descriptor);
		this.categories = categories;
	}

	@Override
	protected List<PostInfo> call() throws Exception {
		return loadPosts(categories);
	}

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
			updateValue(result);
		}

		loadingFinished();
		return result;
	}

	private List<PostInfo> loadPostsOfCategory(String category) {
		List<PostInfo> result = new ArrayList<>();

		for (int i = 0; true; i++) {
			if (isCancelled()) {
				break;
			}

			loadingItemUpdated("page " + i);
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
