package cz.martlin.defrost.misc;

import java.util.List;

import cz.martlin.defrost.dataobj.PostIdentifier;
import cz.martlin.defrost.dataobj.PostInfo;

public interface StatusReporter {
	///////////////////////////////////////////////////////////////////////////////
	void startingLoadCategory(String category);

	void loadingCategoryPage(String category, int page);

	void lastCategoryPage(String category, int page);

	void categoryLoaded(String category);
	///////////////////////////////////////////////////////////////////////////////

	void startingLoadPost(PostIdentifier post);

	void loadingPostPage(PostIdentifier post, int page);

	void lastPostPage(PostIdentifier post, int page);

	void postLoaded(PostIdentifier post);

	///////////////////////////////////////////////////////////////////////////////
	void interrupted();

	void error(Exception e);

	///////////////////////////////////////////////////////////////////////////////

	void loadingOfCategoriesInThreadStarted(List<String> categories);

	void loadingOfPostsInThreadStarted(List<PostInfo> posts);

	void loadingInThreadStopping();

	void loadingInThreadStopped();

}
