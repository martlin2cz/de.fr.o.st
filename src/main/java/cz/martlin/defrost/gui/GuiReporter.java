package cz.martlin.defrost.gui;

import java.util.List;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.misc.StatusReporter;

public class GuiReporter implements StatusReporter {

	private final MainController ctl;

	@Deprecated
	private int Comments;
	@Deprecated
	private int comments;

	private int itemsToLoad;
	private int currentItemIndex;

	public GuiReporter(MainController ctl) {
		super();
		this.ctl = ctl;
	}

	///////////////////////////////////////////////////////////////////////////////

	public void firstCategoryLoading(List<String> categories) {
		itemsLoadingStarting("categories", categories.size());
	}

	@Override
	public void startingLoadCategory(String category) {
		itemsLoadingProgress("category " + category, null);
	}

	@Override
	public void loadingCategoryPage(String category, int page) {
		itemsLoadingProgress("category " + category, page);
	}

	@Override
	public void lastCategoryPage(String category, int page) {
		// nothing
	}

	@Override
	public void categoryLoaded(String category, List<PostInfo> infos) {
		// nothing
	}

	@Override
	public void lastCategoryLoaded(List<PostInfo> infos) {
		itemsLoadingComplete("categories");
	}

	///////////////////////////////////////////////////////////////////////////////

	public void firstPostLoading(List<PostInfo> infos) {
		itemsLoadingStarting("Post", infos.size());
	}

	@Override
	public void startingLoadPost(PostIdentifier post) {
		itemsLoadingProgress("Post " + post.getId(), null);
	}

	@Override
	public void loadingPostPage(PostIdentifier post, int page) {
		itemsLoadingProgress("Post " + post.getId(), page);
	}

	@Override
	public void lastPostPage(PostIdentifier post, int page) {
		// nothing
	}

	@Override
	public void postLoaded(PostIdentifier identifier, List<Comment> comments) {
		// nothing
	}

	@Override
	public void lastPostLoaded(List<Comment> Comments) {
		itemsLoadingComplete("Comments");
	}

	///////////////////////////////////////////////////////////////////////////////

	@Override
	public void interrupted() {
		// nothing
	}

	@Override
	public void error(Exception e) {
		e.printStackTrace(); // TODO FIXME LOGGING
		ctl.error(e.getMessage(), true);
	}

	///////////////////////////////////////////////////////////////////////////////

	@Override
	public void loadingOfPostsInThreadStarting(List<String> categories) {
		ctl.setStatus("Starting");
	}

	@Override
	public void loadingOfCommentsInThreadStarting(List<PostInfo> Comments) {
		ctl.setStatus("Starting");
	}

	@Override
	public void loadingOfPostsInThreadStarted(List<String> categories) {
		ctl.setStatus("Started");
	}

	@Override
	public void loadingOfCommentsInThreadStarted(List<PostInfo> comments) {
		ctl.setStatus("Started");
	}

	@Override
	public void loadingOfPostsInThreadFinished(List<String> categories) {
		ctl.setStatus("Finished");
		ctl.loadingFinished();
	}

	@Override
	public void loadingOfCommentsInThreadFinished(List<PostInfo> comments) {
		ctl.setStatus("Finished");
		ctl.loadingFinished();
	}

	@Override
	public void loadingInThreadStopping() {
		ctl.setStatus("Stopping...");
	}

	@Override
	public void loadingInThreadStopped() {
		ctl.setStatus("Stopped");
	}

	///////////////////////////////////////////////////////////////////////////////

	/**
	 * Reports that started loading of what with given total count of items to
	 * be loaded.
	 * 
	 * @param what
	 * @param totalCount
	 */
	private void itemsLoadingStarting(String what, int totalCount) {
		itemsToLoad = totalCount;
		currentItemIndex = 0;

		String message = "(" + currentItemIndex + "/" + itemsToLoad + ") Loading " + what;
		ctl.setStatus(message);
		ctl.setLoadingStarted(message);
	}

	/**
	 * Reports loaded one next item. If page is specified, reports as a new
	 * item, else reports as only a page of item loaded.
	 * 
	 * @param what
	 * @param page
	 */
	private void itemsLoadingProgress(String what, Integer page) {
		if (page == null) {
			currentItemIndex++;

			double progress = ((double) currentItemIndex) / (itemsToLoad + 1);
			String message = "(" + currentItemIndex + "/" + itemsToLoad + ") Loading " + what;

			ctl.setLoadingProgress(message, progress);
		} else {
			double progress = ((double) currentItemIndex) / (itemsToLoad + 1);
			String message = "(" + currentItemIndex + "/" + itemsToLoad + ") Loading " + what + ", page " + page;
			ctl.setLoadingProgress(message, progress);
		}
	}

	/**
	 * Reports that loading have been completed.
	 * 
	 * @param what
	 */
	private void itemsLoadingComplete(String what) {
		String message = what + " loaded";
		ctl.setLoadingStopped(message);
		ctl.updateTotals();
	}

	///////////////////////////////////////////////////////////////////////////////

	@Deprecated
	private void updateTotals(String category, PostInfo info) {

	}

	///////////////////////////////////////////////////////////////////////////////

}
