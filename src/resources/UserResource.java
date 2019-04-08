package resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class UserResource {
	private int userId;
	private String username;
	private String name;
	private String location;
	
	public UserResource() {}
	
	public UserResource(String name, String username) {
		this.name = name;
		this.username = username;
	}

	public UserResource(String userId) {
		this.userId = Integer.parseInt(userId);
	}
	
	public UserResource(String userId, String name, String username) {
		this.userId = Integer.parseInt(userId);
		this.name = name;
		this.username = username;
	}
	
	public UserResource(int userId, String name, String username) {
		this.userId = userId;
		this.name = name;
		this.username = username;
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
	
	public String getLocation() {
		return this.location;
	}
	
	public void setId(String userId) {
		this.userId = Integer.parseInt(userId);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
}
