package cz.martlin.defrost.misc;

import cz.martlin.defrost.dataobj.PostIdentifier;

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

	void error(DefrostException e);

	///////////////////////////////////////////////////////////////////////////////

}
