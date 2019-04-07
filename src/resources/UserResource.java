package resources;
import resources.PostResource;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class UserResource {
	private String userId;
	private String username;
	private String name;
	
	public UserResource(String name, String username) {
		this.name = name;
		this.username = username;
	}
	
	public UserResource() {}

	public UserResource(String userId) {
		this.userId = userId;
	}
	
	public UserResource(String userId, String name, String username) {
		this.userId = userId;
		this.name = name;
		this.username = username;
	}

	@XmlAttribute(required=false)
	public String getId() {
		return this.userId;
	}
	
	public void setId(String id) {
		this.userId = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	public String getName() {
		return this.name;
	}
}
