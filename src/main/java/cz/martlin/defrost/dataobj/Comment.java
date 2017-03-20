package cz.martlin.defrost.dataobj;

import java.util.Calendar;

public class Comment {
	private final User author;
	private final Calendar date;
	private final String content;

	public Comment(User author, Calendar date, String content) {
		super();
		this.author = author;
		this.date = date;
		this.content = content;
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
		result = prime * result + ((date == null) ? 0 : date.hashCode());
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
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comment [author=" + author + ", date=" + (date != null ? date.getTime() : null) + ", content=" + content
				+ "]";
	}

}
