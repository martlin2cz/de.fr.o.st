package cz.martlin.defrost.input.threading;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.input.load.Loader;
import cz.martlin.defrost.misc.StatusReporter;

public class LoaderInThread {

	private final Loader loader;
	private final StatusReporter reporter;

	private PostsLoaderThread currentPostsThread;
	private CommentsLoaderThread currentCommentsThread;

	private final Set<PostInfo> loadedPosts;
	private final Set<Comment> loadedComments;

	public LoaderInThread(BaseForumDescriptor desc, StatusReporter reporter) {
		this.loader = new Loader(desc, reporter);
		this.reporter = reporter;

		this.loadedPosts = new LinkedHashSet<>();
		this.loadedComments = new LinkedHashSet<>();
	}

	public synchronized List<PostInfo> getLoadedPosts() {
		return new ArrayList<>(loadedPosts);
	}

	public synchronized int getLoadedPostsCount() {
		return loadedPosts.size();
	}

	public void setLoadedPosts(List<PostInfo> posts) {
		this.loadedPosts.clear();
		this.loadedPosts.addAll(posts);
	}

	public synchronized List<Comment> getLoadedComments() {
		return new ArrayList<>(loadedComments);
	}

	public synchronized int getLoadedCommentsCount() {
		return loadedComments.size();
	}

	public void setLoadedComments(List<Comment> comments) {
		this.loadedComments.clear();
		this.loadedComments.addAll(comments);
	}

	///////////////////////////////////////////////////////////////////////////////

	public synchronized void startLoadingPosts(List<String> categories) {
		reporter.loadingOfPostsInThreadStarting(categories);
		
		currentPostsThread = new PostsLoaderThread(loader, reporter, categories);
		loader.restart();
		currentPostsThread.start();

	}

	public synchronized void startLoadingComments(List<PostInfo> comments) {

		reporter.loadingOfCommentsInThreadStarting(comments);
		
		currentCommentsThread = new CommentsLoaderThread(loader, reporter, comments);
		loader.restart();
		currentCommentsThread.start();

	}

	public synchronized void stopLoading() {
		reporter.loadingInThreadStopping();
		loader.interrupt();

		try {
			if (currentPostsThread != null) {
				currentPostsThread.join();

				List<PostInfo> infos = currentPostsThread.getLoadedInfos();
				loadedPosts.addAll(infos);
			}

			if (currentCommentsThread != null) {
				currentCommentsThread.join();

				List<Comment> pomments = currentCommentsThread.getLoadedComments();
				loadedComments.addAll(pomments);
			}
		} catch (InterruptedException e) {
			reporter.error(e);
		}

		reporter.loadingInThreadStopped();
	}

}
