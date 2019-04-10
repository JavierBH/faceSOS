package controllers;

import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import database.UserDB;
import resources.UserResource;

public class UserController extends Controller {
	
	public UserController(UriInfo uriInfo) {
		super(uriInfo);
	}
	
	/*
	 * Get basic data from userId
	 * */
	public Response getUser(String userId) throws SQLException {
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		// Get data from DB
		UserResource user = new UserResource(userId);
		return this.getUserInformationReponse(res, user);
	}
	
	
	
	/*
	 * Create new user from information in user variable
	 * */
	public Response createUser(UserResource user) throws SQLException {
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		// Check body data
		if (user.getName() == null || user.getUsername() == null) {
			return this.getBadRequestResponse(res, "Unable to create user. "
					+ "One or more of the following fields are empty: "
					+ "user or/and username");
		}
		
		// Set data in DB
		UserDB db = new UserDB();
		ResultSet rs = db.createUser(user);
		
		if(rs.next()) {
			// Prepare data to send back to client
			
			//TODO: I would like to change 1 to "user_id". But #doesntWork
			user.setUserId(rs.getString(1));
			String location = this.getPath() + "/user/" + user.getId();
			return this.getCreatedResponse(res, user, location, "User created succesfully");
		}
		
		// Error
		return this.getInternalServerErrorResponse(res, "There was an error. Unable to create user");
	}
	
	
	/*
	 * Edit user information using userId. If userId doesn't exists, then it create a new one
	 * */
	public Response editUser(String userId, UserResource user) throws SQLException {
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();
		
		// Edit data in DB
		user.setUserId(userId);
		UserDB db = new UserDB();
		int rowsAffected = db.editUser(user);
		
		if (rowsAffected > 0) {
			// Prepare data to send back to client
			user.setUsername(null);
			String location = this.getPath() + "/user/" + user.getId();
			return this.getCreatedResponse(res, user, location, "Data updated succesfully");
		} else if(rowsAffected == 0) {
			// User doesn't exist so we create it
			return this.createUser(user);
		}
		
		// Error
		return this.getInternalServerErrorResponse(res, "There was an error. Unable to update user");
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
		int rowsAffected = db.removeUser(user);
		
		if(rowsAffected > 0) {
			// Prepare data to send back to client
			return this.getOkResponse(res, user, "User removed succesfully");
		} else if (rowsAffected == 0) {
			// User doesn't exist so we return bad request
			return this.getBadRequestResponse(res, "User does not exist");
		}
		
		// Error
		return this.getInternalServerErrorResponse(res, "There was an error. Unable to remove user");
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
	            user.setLocation(this.getPath() + "/" + user.getId());
	            users.add(user);
	        }
			
			// Array with users
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("users", users);
			data.put("n", users.size());

			// Prepare data to send back to client
			return this.getOkResponse(res, data, "Data loaded succesfully");
		}

		// Error
		return this.getInternalServerErrorResponse(res, "There was a problem. Unable to get user information");
	}
	
	public Response getFriends(String userId, String name, int limitTo) throws SQLException {
		//UserResource user = new UserResource(userId);
		HashMap <String,Object> res = new HashMap <String,Object>();
		
		// Check that userId exists
		UserResource user = new UserResource(userId);
		Response userInformationResponse = this.getUserInformationReponse(res, user);
		if(userInformationResponse.getStatus() != 200) {
			return userInformationResponse;
		}
			
		// Get data from DB
		UserDB db = new UserDB();
		ResultSet rs = db.getFriends(user, limitTo, userId);
		
		if(rs != null) {
			ArrayList<UserResource> friends = new ArrayList<UserResource>();
			while(rs.next()) {
				friends.add(new UserResource(
						rs.getString("friend_id"),
						rs.getString("name"),
						rs.getString("username")));
			}
			
			// Array with users
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("friends", friends);
			data.put("n", friends.size());
			return this.getOkResponse(res, data, "Data loaded succesfully");
		}
		
		// Error
		return this.getInternalServerErrorResponse(res, "Unable to get friends information");
	}
}
