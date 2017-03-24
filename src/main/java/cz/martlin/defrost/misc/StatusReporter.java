package cz.martlin.defrost.misc;

import java.util.List;

import cz.martlin.defrost.dataobj.Comment;
import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;

public interface StatusReporter {
	///////////////////////////////////////////////////////////////////////////////
	void startingLoadCategory(String category);

	void loadingCategoryPage(String category, int page);

	void lastCategoryPage(String category, int page);

	void categoryLoaded(String category, List<PostInfo> infos);
	///////////////////////////////////////////////////////////////////////////////

	void startingLoadPost(PostIdentifier identifier);

	void loadingPostPage(PostIdentifier identifier, int page);

	void lastPostPage(PostIdentifier identifier, int page);

	void postLoaded(PostIdentifier identifier, List<Comment> comments);

	///////////////////////////////////////////////////////////////////////////////
	void interrupted();

	void error(Exception e);

	///////////////////////////////////////////////////////////////////////////////

	void firstCategoryLoading(List<String> categories);

	void lastCategoryLoaded(List<PostInfo> infos);

	void firstPostLoading(List<PostInfo> infos);

	void lastPostLoaded(List<Comment> comments);

	///////////////////////////////////////////////////////////////////////////////

	void loadingOfPostsInThreadStarting(List<String> categories);

	void loadingOfCommentsInThreadStarting(List<PostInfo> comments);

	void loadingOfPostsInThreadStarted(List<String> categories);

	void loadingOfPostsInThreadFinished(List<String> categories);

	void loadingOfCommentsInThreadStarted(List<PostInfo> comments);

	void loadingOfCommentsInThreadFinished(List<PostInfo> comments);

	void loadingInThreadStopping();

	void loadingInThreadStopped();

}
