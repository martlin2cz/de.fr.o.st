package cz.martlin.defrost.input.threading;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.dataobj.Post;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.input.load.Loader;
import cz.martlin.defrost.misc.StatusReporter;

public class LoaderInThread {

	private final Loader loader;
	private final StatusReporter reporter;

	private CategoriesLoaderThread currentCategoriesThread;
	private PostsLoaderThread currentPostsThread;
	
	private final Set<PostInfo> loadedInfos;
	private final Set<Post> loadedPosts;

	public LoaderInThread(BaseForumDescriptor desc, StatusReporter reporter) {
		this.loader = new Loader(desc, reporter);
		this.reporter = reporter;

		this.loadedInfos = new LinkedHashSet<>();
		this.loadedPosts = new LinkedHashSet<>();
	}

	public synchronized List<PostInfo> getLoadedInfos() {
		return new ArrayList<>(loadedInfos);
	}
	
	public synchronized int getLoadedInfosCount() {
		return loadedInfos.size();
	}

	public synchronized List<Post> getLoadedPosts() {
		return new ArrayList<>(loadedPosts);
	}
	
	public synchronized int getLoadedCount() {
		return loadedInfos.size();
	}

	///////////////////////////////////////////////////////////////////////////////

	public synchronized void startLoadingCategories(List<String> categories) {
		currentCategoriesThread = new CategoriesLoaderThread(loader, categories);
		loader.restart();
		currentCategoriesThread.start();

		reporter.loadingOfCategoriesInThreadStarted(categories);
	}

	public synchronized void startLoadingPosts(List<PostInfo> posts) {
		currentPostsThread = new PostsLoaderThread(loader, posts);
		loader.restart();
		currentPostsThread.start();

		reporter.loadingOfPostsInThreadStarted(posts);
	}

	public synchronized void stopLoading() {
		reporter.loadingInThreadStopping();
		loader.interrupt();

		try {
			if (currentCategoriesThread != null) {
				currentCategoriesThread.join();

				List<PostInfo> infos = currentCategoriesThread.getLoadedInfos();
				loadedInfos.addAll(infos);
			}

			if (currentPostsThread != null) {
				currentPostsThread.join();

				List<Post> posts = currentPostsThread.getLoadedPosts();
				loadedPosts.addAll(posts);
			}
		} catch (InterruptedException e) {
			reporter.error(e);
		}

		reporter.loadingInThreadStopped();
	}

}
