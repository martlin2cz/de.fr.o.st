package cz.martlin.defrost.dataobj;

import java.io.Serializable;

public class PostIdentifier implements Serializable, Comparable<PostIdentifier> {

	private static final long serialVersionUID = 7908894990189622622L;

	private final String category;
	private final String id;

	public PostIdentifier(String category, String id) {
		super();
		this.category = category;
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		PostIdentifier other = (PostIdentifier) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PostIdentifier [category=" + category + ", id=" + id + "]";
	}

	@Override
	public int compareTo(PostIdentifier o) {
		int cmp;

		cmp = category.compareTo(o.category);
		if (cmp != 0) {
			return cmp;
		}

		cmp = id.compareTo(o.id);
		if (cmp != 0) {
			return cmp;
		}

		return 0;
	}

}