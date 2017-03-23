package cz.martlin.defrost.input.threading;

import java.util.List;

import cz.martlin.defrost.dataobj.Post;
import cz.martlin.defrost.dataobj.PostInfo;
import cz.martlin.defrost.input.load.Loader;

public class PostsLoaderThread extends Thread {

	private final Loader loader;
	private final List<PostInfo> infos;
	private List<Post> posts;

	public PostsLoaderThread(Loader loader, List<PostInfo> infos) {
		super("PostsLoaderThread");
		this.loader = loader;
		this.infos = infos;
	}

	@Override
	public void run() {
		posts = loader.loadPosts(infos);
	}
	
	public List<Post> getLoadedPosts() {
		return posts;
	}
}
