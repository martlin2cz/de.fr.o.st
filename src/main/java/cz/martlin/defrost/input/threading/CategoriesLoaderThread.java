package cz.martlin.defrost.input.threading;

import java.util.List;

import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.input.load.Loader;

public class CategoriesLoaderThread extends Thread {

	private final Loader loader;
	private final List<String> categories;
	
	private List<PostInfo> posts;
	
	public CategoriesLoaderThread(Loader loader, List<String> categories) {
		super("CategoriesLoaderThread");
		this.loader = loader;
		this.categories = categories;
	}
	
	@Override
	public void run() {
		posts = loader.loadCategories(categories);
	}
	
	public List<PostInfo> getLoadedInfos() {
		return posts;
	}

}
