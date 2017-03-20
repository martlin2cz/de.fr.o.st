package cz.martlin.defrost.dataobj;

import java.util.Calendar;

public class Comment {
	private final String author;
	private final Calendar when;
	private final String content;

	public Comment(String author, Calendar when, String content) {
		super();
		this.author = author;
		this.when = when;
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public Calendar getWhen() {
		return when;
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
		result = prime * result + ((when == null) ? 0 : when.hashCode());
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
		if (when == null) {
			if (other.when != null)
				return false;
		} else if (!when.equals(other.when))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comment [author=" + author + ", when=" + when + ", content=" + content + "]";
	}

}
