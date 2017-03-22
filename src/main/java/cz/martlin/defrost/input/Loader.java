package cz.martlin.defrost.input;

import java.util.ArrayList;
import java.util.List;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.PagedDataResult;
import cz.martlin.defrost.dataobj.Post;
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
			if (isInterrupted()) {
				reporter.interrupted();
				break;
			}
		}
		reporter.categoryLoaded(category);

		return posts;
	}

	public Post loadPost(PostIdentifier identifier) {
		restart();
		Post post = null;

		reporter.startingLoadPost(identifier);
		for (int i = 1;; i++) {
			reporter.loadingPostPage(identifier, i);

			PagedDataResult<Post> result;
			try {
				result = postParser.loadAndParse(identifier, i);
			} catch (DefrostException e) {
				reporter.error(e);
				continue;
			}
			if (post == null) {
				post = result.getData();
			} else {
				post.getComments().addAll(result.getData().getComments());
			}

			if (!result.isHasNextPage()) {
				reporter.lastPostPage(identifier, i);
				break;
			}
			if (isInterrupted()) {
				reporter.interrupted();
				break;
			}
		}
		reporter.postLoaded(identifier);

		return post;
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

	///////////////////////////////////////////////////////////////////////////////

}
