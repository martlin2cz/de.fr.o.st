package cz.martlin.defrost.input.load;

import java.util.ArrayList;
import java.util.List;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.PagedDataResult;
import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.misc.DefrostException;
import cz.martlin.defrost.misc.Interruptable;
import cz.martlin.defrost.misc.StatusReporter;

public class Loader implements Interruptable {

	// private final BaseForumDescriptor desc;
	private final StatusReporter reporter;
	private final CategoryParser categoryParser;
	private final PostParser postParser;

	private boolean interrupted;

	public Loader(BaseForumDescriptor desc, StatusReporter reporter) {
		// this.desc = desc;
		this.reporter = reporter;
		this.categoryParser = new CategoryParser(desc);
		this.postParser = new PostParser(desc);
	}

	///////////////////////////////////////////////////////////////////////////////

	public List<PostInfo> loadCategory(String category) {
		restart();
		List<PostInfo> posts = new ArrayList<>();

		reporter.startingLoadCategory(category);
		for (int i = 1;; i++) {
			if (isInterrupted()) {
				reporter.interrupted();
				break;
			}

			reporter.loadingCategoryPage(category, i);

			PagedDataResult<List<PostInfo>> result;
			try {
				result = categoryParser.listPosts(category, i);
			} catch (DefrostException e) {
				reporter.error(e);
				continue;
			}
			posts.addAll(result.getData());

			if (!result.isHasNextPage()) {
				reporter.lastCategoryPage(category, i);
				break;
			}
		}
		reporter.categoryLoaded(category, posts);

		return posts;
	}

	public List<Comment> loadComments(PostIdentifier identifier) {
		restart();
		List<Comment> comments = new ArrayList<>();

		reporter.startingLoadPost(identifier);
		for (int i = 1;; i++) {
			if (isInterrupted()) {
				reporter.interrupted();
				break;
			}

			reporter.loadingPostPage(identifier, i);

			PagedDataResult<List<Comment>> result;
			try {
				result = postParser.loadAndParse(identifier, i);
			} catch (DefrostException e) {
				reporter.error(e);
				continue;
			}

			comments.addAll(result.getData());

			if (!result.isHasNextPage()) {
				reporter.lastPostPage(identifier, i);
				break;
			}
		}
		reporter.postLoaded(identifier, comments);

		return comments;
	}
	///////////////////////////////////////////////////////////////////////////////

	public List<PostInfo> loadCategories(List<String> categories) {
		List<PostInfo> result = new ArrayList<>();

		reporter.firstCategoryLoading(categories);

		for (String category : categories) {
			if (isInterrupted()) {
				break;
			}

			List<PostInfo> posts = loadCategory(category);
			result.addAll(posts);
		}

		reporter.lastCategoryLoaded(result);

		return result;
	}

	public List<Comment> loadComments(List<PostInfo> infos) {
		List<Comment> result = new ArrayList<>();

		reporter.firstPostLoading(infos);

		for (PostInfo info : infos) {
			if (isInterrupted()) {
				break;
			}

			List<Comment> comments = loadComments(info.getIdentifier());
			result.addAll(comments);
		}

		reporter.lastPostLoaded(result);

		return result;
	}

	///////////////////////////////////////////////////////////////////////////////

	@Override
	public void restart() {
		interrupted = false;
	}

	@Override
	public void interrupt() {
		interrupted = true;

	}

	@Override
	public boolean isInterrupted() {
		return interrupted;
	}

}
