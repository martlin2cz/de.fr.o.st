package cz.martlin.defrost.gui;

import java.util.List;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.misc.StatusReporter;
import javafx.concurrent.Task;

@Deprecated
public class TasksToGuiReporter implements StatusReporter {

	public TasksToGuiReporter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startingLoadCategory(String category) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadingCategoryPage(String category, int page) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lastCategoryPage(String category, int page) {
		// TODO Auto-generated method stub

	}

	@Override
	public void categoryLoaded(String category, List<PostInfo> infos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startingLoadPost(PostIdentifier identifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadingPostPage(PostIdentifier identifier, int page) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lastPostPage(PostIdentifier identifier, int page) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postLoaded(PostIdentifier identifier, List<Comment> comments) {
		// TODO Auto-generated method stub

	}

	@Override
	public void interrupted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(Exception e) {
		// TODO Auto-generated method stub

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
	public void lastPostLoaded(List<Comment> comments) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadingOfPostsInThreadStarting(List<String> categories) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadingOfCommentsInThreadStarting(List<PostInfo> comments) {
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
	public void loadingInThreadStopping() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadingInThreadStopped() {
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

}
