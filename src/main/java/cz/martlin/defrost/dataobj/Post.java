package cz.martlin.defrost.dataobj;

import java.net.URL;
import java.util.List;

public class Post {

	private final String title;
	private final URL url;
	private final List<Comment> comments;

	public Post(String title, URL url, List<Comment> comments) {
		super();
		this.title = title;
		this.url = url;
		this.comments = comments;
	}

	public String getTitle() {
		return title;
	}

	public URL getUrl() {
		return url;
	}

	public List<Comment> getComments() {
		return comments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Post [title=" + title + ", url=" + url + ", comments=" + comments + "]";
	}

}