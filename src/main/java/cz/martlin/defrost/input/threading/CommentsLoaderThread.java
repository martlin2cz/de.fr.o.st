package cz.martlin.defrost.input.threading;

import java.util.List;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.input.load.Loader;
import cz.martlin.defrost.misc.StatusReporter;

public class CommentsLoaderThread extends Thread {

	private final Loader loader;
	private final StatusReporter reporter;
	private final List<PostInfo> posts;
	private List<Comment> comments;

	public CommentsLoaderThread(Loader loader, StatusReporter reporter, List<PostInfo> posts) {
		super("CommentsLoaderThread");
		this.loader = loader;
		this.reporter = reporter;
		this.posts = posts;
	}

	@Override
	public void run() {
		reporter.loadingOfCommentsInThreadStarted(posts);

		comments = loader.loadComments(posts);

		reporter.loadingOfCommentsInThreadFinished(posts);
	}

	public List<Comment> getLoadedComments() {
		return comments;
	}
}
