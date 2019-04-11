package resources;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class UserResource {
	private int userId;
	private String name;
	private String username;
	private String email;
	private String biography;
	private String location;
	private String friendsLocation;
	private Timestamp createdAt;
	
	public UserResource() {}
	
	public UserResource(String name, String username) {
		this.name = name;
		this.username = username;
	}

	public UserResource(String userId) {
		this.userId = Integer.parseInt(userId);
	}
	
	public UserResource(int userId) {
		this.userId = userId;
	}
	
	public UserResource(String userId, String name, String username) {
		this.userId = Integer.parseInt(userId);
		this.name = name;
		this.username = username;
	}
	
	public UserResource(int userId, String name, String username, String email, String biography, String location, Timestamp createdAt) {
		this.userId = userId;
		this.name = name;
		this.username = username;
		this.email = email;
		this.biography = biography;
		this.location = location + "/" + this.getId();
		this.friendsLocation = this.location + "/friends"; 
		this.createdAt = createdAt;
	}
	
	@XmlAttribute(required=false)
	public int getId() {
		return this.userId;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getBiography() {
		return this.biography;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public String getFriendsLocation() {
		return this.friendsLocation;
	}
	
	public Timestamp getCreatedAt() {
		return this.createdAt;
	}
	
	public void setUserId(String userId) {
		this.userId = Integer.parseInt(userId);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setBiography(String biography) {
		this.biography = biography;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	/*
	 * Join two different objects.
	 * */
	public void join(UserResource user) {
		if(user.getUsername() == null) { this.name = user.getName(); }
		if(user.getName() == null) { this.name = user.getName(); }
	}
}
