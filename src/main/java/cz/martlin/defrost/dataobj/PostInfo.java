package cz.martlin.defrost.dataobj;

public class PostInfo {
	private final String title;
	private final PostIdentifier identifier;

	public PostInfo(String title, PostIdentifier identifier) {
		super();
		this.title = title;
		this.identifier = identifier;
	}

	public String getTitle() {
		return title;
	}

	public PostIdentifier getIdentifier() {
		return identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		PostInfo other = (PostInfo) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PostInfo [title=" + title + ", identifier=" + identifier + "]";
	}

}
