package cz.martlin.defrost.gui;

import java.util.List;

import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.misc.StatusReporter;

public class GuiReporter implements StatusReporter {

	private final MainController ctl;

	public GuiReporter(MainController ctl) {
		super();
		this.ctl = ctl;
	}

	@Override
	public void startingLoadCategory(String category) {
		ctl.setStatus("Loading category " + category);
	}

	@Override
	public void loadingCategoryPage(String category, int page) {
		ctl.setStatus("Loading category " + category + ", page " + page);
	}

	@Override
	public void lastCategoryPage(String category, int page) {
		ctl.loadingStopped();
	}

	@Override
	public void categoryLoaded(String category) {
		ctl.setStatus("Category " + category + " loaded");
		ctl.updateTotals();
	}

	@Override
	public void startingLoadPost(PostIdentifier post) {
		ctl.setStatus("Loading post " + post.getId());
	}

	@Override
	public void loadingPostPage(PostIdentifier post, int page) {
		ctl.setStatus("Loading post " + post.getId() + ", page " + page);
	}

	@Override
	public void lastPostPage(PostIdentifier post, int page) {
		ctl.loadingStopped();
	}

	@Override
	public void postLoaded(PostIdentifier post) {
		ctl.setStatus("Post " + post.getId() + " loaded");
		ctl.updateTotals();
	}

	@Override
	public void interrupted() {
		// nothing
	}

	@Override
	public void error(Exception e) {
		ctl.error(e.getMessage(), true);
	}

	@Override
	public void loadingOfCategoriesInThreadStarted(List<String> categories) {
		ctl.setStatus("Started");
	}

	@Override
	public void loadingOfPostsInThreadStarted(List<PostInfo> posts) {
		ctl.setStatus("Started");

	}

	@Override
	public void loadingInThreadStopping() {
		ctl.setStatus("Stopping...");
	}

	@Override
	public void loadingInThreadStopped() {
		ctl.setStatus("Stopped");
	}

}
