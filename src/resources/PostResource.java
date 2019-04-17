package resources;

public class PostResource {
	private int postId;
	private int userId;
	private String content;
	private String location;
	private String date;
	private String userLocation;

	public PostResource() {
	}

	public PostResource(String postId, String userId) {
		this.postId = Integer.parseInt(postId);
		this.userId = Integer.parseInt(userId);
	}

	public PostResource(String postId, String userId, String content, String date, String location,
			String userLocation) {
		this.postId = Integer.parseInt(postId);
		this.userId = Integer.parseInt(userId);
		this.content = content;
		this.date = date;
		this.location = location;
		this.userLocation = userLocation;
	}

	public PostResource(String content) {
		this.content = content;
	}

	public int getPostId() {
		return this.postId;
	}

	public int getUserId() {
		return this.userId;
	}

	public String getContent() {
		return this.content;
	}

	public void setPostId(String postId) {
		this.postId = Integer.parseInt(postId);
	}

	public void setUserId(String userId) {
		this.userId = Integer.parseInt(userId);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setlocation(String location) {
		this.location = location;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
