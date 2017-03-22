package cz.martlin.defrost.misc;

import java.util.logging.Level;
import java.util.logging.Logger;

import cz.martlin.defrost.dataobj.PostIdentifier;

public class LoggingReporter implements StatusReporter {
	private final Logger LOG = Logger.getLogger(getClass().getName());

	public LoggingReporter() {
		// TODO Auto-generated constructor stub
	}

	///////////////////////////////////////////////////////////////////////////////

	@Override
	public void startingLoadCategory(String category) {
		LOG.info("Starting loading category " + category);
	}

	@Override
	public void loadingCategoryPage(String category, int page) {
		LOG.info("Loading category, page " + page);
	}

	@Override
	public void lastCategoryPage(String category, int page) {
		LOG.info("Loaded last category page, page " + page);
	}

	@Override
	public void categoryLoaded(String category) {
		LOG.info("Category " + category + " loaded");

	}

	///////////////////////////////////////////////////////////////////////////////

	@Override
	public void startingLoadPost(PostIdentifier post) {
		LOG.info("Starting loading post " + post);
	}

	@Override
	public void loadingPostPage(PostIdentifier post, int page) {
		LOG.info("Loading post, page " + page);
	}

	@Override
	public void lastPostPage(PostIdentifier post, int page) {
		LOG.info("Loading last post page, page " + page);

	}

	@Override
	public void postLoaded(PostIdentifier post) {
		LOG.info("Post " + post + " loaded");
	}

	///////////////////////////////////////////////////////////////////////////////

	@Override
	public void interrupted() {
		LOG.warning("Interupted");
	}

	@Override
	public void error(DefrostException e) {
		LOG.log(Level.SEVERE, "Error: " + e, e);
	}

	///////////////////////////////////////////////////////////////////////////////

}
