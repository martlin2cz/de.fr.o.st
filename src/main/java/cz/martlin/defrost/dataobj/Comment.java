package cz.martlin.defrost.dataobj;

import java.util.Calendar;

public class Comment {
	private final PostInfo post;
	private final User author;
	private final Calendar date;
	private final String content;

	public Comment(PostInfo post, User author, Calendar date, String content) {
		this.post = post;
		this.author = author;
		this.date = date;
		this.content = content;
	}

	public PostInfo getPost() {
		return post;
	}

	public User getAuthor() {
		return author;
	}

	public Calendar getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((date == null) ? 0 : (int) date.getTimeInMillis());
		result = prime * result + ((post == null) ? 0 : post.hashCode());
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
		Comment other = (Comment) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (date.getTimeInMillis() != other.date.getTimeInMillis())
			return false;
		if (post == null) {
			if (other.post != null)
				return false;
		} else if (!post.equals(other.post))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comment [post=" + post + ", author=" + author + ", date=" + date + ", content=" + content + "]";
	}

}
