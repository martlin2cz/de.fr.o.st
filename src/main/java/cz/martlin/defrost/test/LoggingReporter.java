package cz.martlin.defrost.test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.misc.StatusReporter;

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
	public void categoryLoaded(String category, List<PostInfo> infos) {
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
	public void postLoaded(PostIdentifier identifier, List<Comment> comments) {
		LOG.info("Post " + identifier + " loaded");
	}

	///////////////////////////////////////////////////////////////////////////////

	@Override
	public void interrupted() {
		LOG.warning("Interupted");
	}

	@Override
	public void error(Exception e) {
		LOG.log(Level.SEVERE, "Error: " + e, e);
	}

	///////////////////////////////////////////////////////////////////////////////

	public void loadingOfPostsInThreadStarting(java.util.List<String> categories) {
		LOG.info("Loading Comments of " + categories.size() + " categories in thread started");
	}

	@Override
	public void loadingOfCommentsInThreadStarting(List<PostInfo> Comments) {
		LOG.info("Loading comments of " + Comments.size() + " Comments in thread started");

	}
//
//	@Override
//	public void lastCategoryLoaded() {
//		LOG.info("Categories loaded");
//
//	}
//
//	@Override
//	public void lastCommentLoaded() {
//		LOG.info("Comments loaded");
//	}

	public void loadingInThreadStopping() {
		LOG.info("Stopping of loading in thread");

	}

	@Override
	public void loadingInThreadStopped() {
		LOG.info("Loading in thread stopped");
	}

	@Override
	public void firstCategoryLoading(List<String> categories) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lastCategoryLoaded(List<PostInfo> infos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void firstPostLoading(List<PostInfo> infos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lastPostLoaded(List<Comment> Comments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadingOfPostsInThreadStarted(List<String> categories) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadingOfPostsInThreadFinished(List<String> categories) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadingOfCommentsInThreadStarted(List<PostInfo> comments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadingOfCommentsInThreadFinished(List<PostInfo> comments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportPostsStarted(List<PostInfo> posts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportingPost(PostInfo post) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportPostsDone(List<PostInfo> posts) {
		// TODO Auto-generated method stub
		
	}
	
	///////////////////////////////////////////////////////////////////////////////

}
