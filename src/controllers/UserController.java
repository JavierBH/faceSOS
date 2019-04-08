package controllers;

import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import database.UserDB;
import resources.UserResource;

import java.util.HashMap;

public class UserController   {
	private UriInfo uriInfo;
	
	public UserController(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	
	/*
	 * Get basic data from userId
	 * */
	public Response getUser(String userId) throws SQLException {
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		// Get data from DB
		UserResource user = new UserResource(userId);
		UserDB db = new UserDB();
		ResultSet rs = db.getUser(user);
		
		if (!rs.next()) {
			res.put("message", "User not found");
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(res)
					.build();
		}
		
		if(rs != null) {
			
			// Prepare data to send back to client
			user.setName(rs.getString("name"));
			user.setUsername(rs.getString("username"));
			res.put("message", "Data loaded succesfully");
			res.put("data", user);
			String location = this.uriInfo.getAbsolutePath() + "/user/" + userId;
			
			return Response
					.status(Response.Status.CREATED)
					.entity(res)
					.header("Location", location)
					.header("Content-Location", location)
					.build();
		}

		// Error
		return this.setMessage(res, "There was a problem. Unable to get user information");
	}
	
	
	
	/*
	 * Create new user from information in user variable
	 * */
	public Response createUser(UserResource user) throws SQLException {
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		// Check body data
		if (user.getName() == null || user.getUsername() == null) {
			res.put("message", "Unable to create user. "
					+ "One or more of the following fields are empty: "
					+ "user or/and username");
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(res)
					.build();
		}
		
		// Set data in DB
		UserDB db = new UserDB();
		ResultSet rs = db.createUser(user);
		
		if(rs != null && rs.next()) {
			
			// Prepare data to send back to client
			user.setId(rs.getString(1));
			res.put("message", "User created succesfully");
			res.put("data", user);
			String location = this.uriInfo.getAbsolutePath() + "/user/" + user.getId();
			
			return Response
					.status(Response.Status.CREATED)
					.entity(res)
					.header("Location", location)
					.header("Content-Location", location)
					.build();
		}
		
		// Error
		return this.setMessage(res, "There was an error. Unable to create user");
	}
	
	
	/*
	 * Edit user information using userId. If userId doesn't exists, then it create a new one
	 * */
	public Response editUser(String userId, UserResource user) throws SQLException {
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		// Edit data in DB
		user.setId(userId);
		UserDB db = new UserDB();
		int status = db.editUser(user);
		
		if (status > 0) {
			
			// Prepare data to send back to client
			user.setUsername(null);
			res.put("data", user);
			res.put("message", "Data updated succesfully");
			String location = this.uriInfo.getAbsolutePath() + "/user/" + user.getId();
			
			return Response
					.status(Response.Status.OK)
					.entity(res)
					.header("Location", location)
					.header("Content-Location", location)
					.build();
			
		} else if(status == 0) {
			
			// User doesn't exist so we create it
			return this.createUser(user);
			
		}
		
		// Error
		return this.setMessage(res, "There was an error. Unable to update user");
	}
	
	
	/*
	 * Remove a user using userId
	 * */
	public Response removeUser(String userId) throws SQLException {
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		// Delete data in DB
		UserResource user = new UserResource(userId);
		UserDB db = new UserDB();
		int status = db.removeUser(user);
		if(status > 0) {
			
			// Prepare data to send back to client
			res.put("data", user);
			res.put("message", "User removed succesfully");
			System.out.println(user.getId());
			return Response
					.status(Response.Status.OK)
					.entity(user)
					.build();
			
		} else if (status == 0) {
			
			// User doesn't exist so we return bad request
			res.put("message", "User does not exist");
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(res)
					.build();
		}
		
		// Error
		return this.setMessage(res, "There was an error. Unable to remove user");
	}
	
	
	/*
	 * Get basic data from userId
	 * */
	public Response getUsers(String name) throws SQLException {
		
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		// Get data from DB
		UserDB db = new UserDB();
		ResultSet rs = db.getUsers(name);
		
		if(rs != null) {
			// Array with users
			ArrayList<UserResource> users = new ArrayList<UserResource>();
			while (rs.next()) {
	            UserResource user = new UserResource(
	            		rs.getInt("user_id"),
	            		rs.getString("name"),
	            		rs.getString("username"));
	            user.setLocation(this.uriInfo.getAbsolutePath() + "/" + user.getId());
	            users.add(user);
	        }
			
			// Array with users
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("users", users);
			data.put("n", users.size());

			// Prepare data to send back to client
			res.put("data",	data);
			res.put("message", "Data loaded succesfully");
			return Response
					.status(Response.Status.CREATED)
					.entity(res)
					.build();
		}

		// Error
		return this.setMessage(res, "There was a problem. Unable to get user information");
	}
	
	
	/*
	 * Add error message to response
	 * */
	private Response setMessage(HashMap<String, Object> res, String err) {
		res.put("message", err);
		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(res)
				.build();
	}
}
