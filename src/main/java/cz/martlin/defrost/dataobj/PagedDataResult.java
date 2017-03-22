package cz.martlin.defrost.dataobj;

public class PagedDataResult<T> {
	private final T data;
	private final int page;
	private final boolean hasNextPage;

	public PagedDataResult(T data, int page, boolean hasNextPage) {
		super();
		this.data = data;
		this.page = page;
		this.hasNextPage = hasNextPage;
	}

	public T getData() {
		return data;
	}

	public int getPage() {
		return page;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + (hasNextPage ? 1231 : 1237);
		result = prime * result + page;
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
		PagedDataResult<?> other = (PagedDataResult<?>) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (hasNextPage != other.hasNextPage)
			return false;
		if (page != other.page)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PagedDataResult [data=" + data + ", page=" + page + ", hasNextPage=" + hasNextPage + "]";
	}

}
