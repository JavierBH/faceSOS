package resources;

public class PostResource {
	private int postId;
	private int userId;
	private String content;
	
	public PostResource() {}
	
	public PostResource(String postId, String userId) {
		this.postId = Integer.parseInt(postId);
		this.userId = Integer.parseInt(userId);
	}
	
	public PostResource(String content) {
		this.content=content;
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
}
