package cz.martlin.defrost.dataobj;

public class PostIdentifier {
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
	
}