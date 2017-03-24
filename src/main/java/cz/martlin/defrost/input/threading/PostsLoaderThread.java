package cz.martlin.defrost.input.threading;

import java.util.List;

import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.input.load.Loader;
import cz.martlin.defrost.misc.StatusReporter;

public class PostsLoaderThread extends Thread {

	private final Loader loader;
	private final StatusReporter reporter;
	private final List<String> categories;
	private List<PostInfo> infos;

	public PostsLoaderThread(Loader loader, StatusReporter reporter, List<String> categories) {
		super("CategoriesLoaderThread");
		this.loader = loader;
		this.reporter = reporter;
		this.categories = categories;

	}

	@Override
	public void run() {
		reporter.loadingOfPostsInThreadStarted(categories);

		infos = loader.loadCategories(categories);

		reporter.loadingOfPostsInThreadFinished(categories);
	}

	public List<PostInfo> getLoadedInfos() {
		return infos;
	}
}
