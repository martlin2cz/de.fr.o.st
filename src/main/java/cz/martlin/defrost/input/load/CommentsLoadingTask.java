package cz.martlin.defrost.input.load;

import java.util.ArrayList;
import java.util.List;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PagedDataResult;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.forums.base.BaseForumDescriptor;
import cz.martlin.defrost.tasks.BaseLoadingIndicator;
import cz.martlin.defrost.tasks.ItemsLoadingTask;
import cz.martlin.defrost.utils.DefrostException;

/**
 * Task implementing loading of posts' comments.
 * 
 * @author martin
 *
 */
public class CommentsLoadingTask extends ItemsLoadingTask<List<Comment>> {

	private final PostParser parser;
	private final List<PostInfo> posts;

	public CommentsLoadingTask(BaseForumDescriptor descriptor, BaseLoadingIndicator indicator, List<PostInfo> posts) {
		super(Messages.getString("Loading_comments"), Messages.getString("Loading_comments_of"), indicator); //$NON-NLS-1$ //$NON-NLS-2$

		this.posts = posts;
		this.parser = new PostParser(descriptor);
	}

	@Override
	protected List<Comment> call() throws Exception {
		return loadComments(posts);
	}

	/**
	 * Loads comments of given posts.
	 * 
	 * @param posts
	 * @return
	 */
	private List<Comment> loadComments(List<PostInfo> posts) {
		List<Comment> result = new ArrayList<>();

		loadingStarted(posts.size());

		for (PostInfo post : posts) {
			if (isCancelled()) {
				break;
			}

			loadingNextItem(post.getTitle());

			List<Comment> comments = loadCommentsOfPost(post);
			result.addAll(comments);

			updateValue(null);
			updateValue(result);
		}

		loadingFinished();

		return result;
	}

	/**
	 * Loads comments of given post.
	 * 
	 * @param post
	 * @return
	 */
	private List<Comment> loadCommentsOfPost(PostInfo post) {
		List<Comment> result = new ArrayList<>();

		for (int i = 0; true; i++) {
			if (isCancelled()) {
				break;
			}

			loadingItemUpdated(Messages.getString("page") + " " + i); //$NON-NLS-1$
			PagedDataResult<List<Comment>> comments;
			try {
				PostIdentifier identifier = post.getIdentifier();
				comments = parser.loadAndParse(identifier, i);
			} catch (DefrostException e) {
				error(e);
				continue;
			}

			result.addAll(comments.getData());

			if (!comments.isHasNextPage()) {
				break;
			}
		}

		return result;
	}

}
