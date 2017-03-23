package cz.martlin.defrost.misc;

import java.util.List;

import cz.martlin.defrost.dataobj.Post;
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

	void postLoaded(PostIdentifier identifier, Post post);

	///////////////////////////////////////////////////////////////////////////////
	void interrupted();

	void error(Exception e);

	///////////////////////////////////////////////////////////////////////////////

	void firstCategoryLoading(List<String> categories);

	void lastCategoryLoaded(List<PostInfo> infos);

	void firstPostLoading(List<PostInfo> infos);

	void lastPostLoaded(List<Post> posts);

	///////////////////////////////////////////////////////////////////////////////

	void loadingOfCategoriesInThreadStarted(List<String> categories);

	void loadingOfPostsInThreadStarted(List<PostInfo> posts);

	void loadingInThreadStopping();

	void loadingInThreadStopped();

}
