package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import database.PostDB;
import database.UserDB;
import resources.PostResource;
import resources.UserResource;

public class UserController extends Controller {

	public UserController(UriInfo uriInfo) {
		super(uriInfo);
	}

	/*
	 * Get basic data from userId
	 */
	public Response getUser(String userId) throws SQLException {
		// Get data from DB
		UserResource user = new UserResource(userId);
		return this.getUserInformationReponse(user);
	}

	/*
	 * Create new user from information in user variable
	 */
	public Response createUser(UserResource user) throws SQLException {
		// Check body data
		if (user.getName() == null || user.getUsername() == null || user.getEmail() == null) {
			return this.getResponse(Response.Status.BAD_REQUEST, "Unable to create user. "
					+ "One or more of the following fields are empty: user, username or/and email");
		}

		// Set data in DB
		UserDB db = new UserDB();
		ResultSet rs = db.createUser(user);

		if(!rs.next()) {
			// Error
			return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "There was an error. Unable to create user");
		}

		user.setUserId(rs.getString(1));
		String location = this.getPath() + "/user/" + user.getId();
		return this.getResponse(Response.Status.CREATED, "User created succesfully", user, location);
	}

	/*
	 * Edit user information using userId. If userId doesn't exists, then it create
	 * a new one
	 */
	public Response editUser(String userId, UserResource user) throws SQLException {
		user.setUsername(null);
		user.setUserId(userId);

		// Check that user exists
		Response userInformation = this.getUserInformationReponse(user);
		if (userInformation.getStatus() != 200) {
			return userInformation;
		}

		// Edit data in DB
		UserDB db = new UserDB();
		int rowsAffected = db.editUser(user);

		if (rowsAffected < 0) {
			return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "There was an error. Unable to update user");
		} else if (rowsAffected == 0) {
			// User doesn't exist so we create it
			return this.createUser(user);
		}

		// Prepare data to send back to client
		UserResource fullUserInfo = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
		user.join(fullUserInfo);
		String location = this.getPath() + "/user/" + user.getId();
		return this.getResponse(Response.Status.OK, "Data updated succesfully", user, location);
	}

	/*
	 * Remove a user using userId
	 */
	public Response removeUser(String userId) throws SQLException {
		// Delete data in DB
		UserResource user = new UserResource(userId);
		UserDB db = new UserDB();
		int rowsAffected = db.removeUser(user);

		if (rowsAffected < 0) {// Error
			return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "There was an error. Unable to remove user");
		} else if (rowsAffected == 0) {
			// User doesn't exist so we return bad request
			return this.getResponse(Response.Status.BAD_REQUEST, "User does not exist");
		}

		// Prepare data to send back to client
		return this.getResponse(Response.Status.OK, "User removed succesfully", user);
	}

	/*
	 * Get basic data from userId
	 */
	public Response getUsers(String name, int limitTo, int page) throws SQLException {
		// Get data from DB
		UserDB db = new UserDB();
		ResultSet rs = db.getUsers(name, limitTo , page - 1);

		if (rs == null) {
			return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "There was a problem. Unable to get user information");
		}

		// Array with users
		ArrayList<UserResource> users = new ArrayList<UserResource>();
		while (rs.next())
			users.add(new UserResource(rs.getInt("user_id"), rs, this.getBaseUri()));

		// Array with users
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("users", users);
		data.put("nUsers", users.size());
		data.put("pagination", this.getPagination(name,"name", page, users.size() > limitTo));

		// Prepare data to send back to client
		return this.getResponse(Response.Status.OK, "Data loaded succesfully", data);
	}

	public Response getUserApp(String userId) throws SQLException {
		UserDB userDB = new UserDB();
		UserResource user = new UserResource(userId);
		ResultSet rs = userDB.getUser(user);
		if (rs == null) {
			return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "There was a problem. Unable to get user information");
		} else if (!rs.next()) {
			return this.getResponse(Response.Status.BAD_REQUEST, "User with id " + user.getId() + " not found.");
		} else {
			// Prepare data to send back to client
			user.setName(rs.getString("name"));
			user.setUsername(rs.getString("username")); 
			user.setEmail(rs.getString("email"));
			user.setUpdatedAt(rs.getTimestamp("updated_at"));
			user.setBiography(rs.getString("biography"));
			PostDB postDB = new PostDB();
			rs = postDB.getPostFriends(user, null, 10, 0);
			ArrayList<PostResource> posts = new ArrayList<PostResource>();
			while (rs.next())
				posts.add(new PostResource(rs.getInt("post_id"), rs, this.getBaseUri()));
			rs = postDB.getPosts(user, 1,0, null); 
			PostResource post = null;
			if (rs.next())
				 post = new PostResource(rs, this.getBaseUri());
			
			// Array with users
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("user", user);
			data.put("nFriends", this.getUserNumberFriends(user));
			data.put("posts", posts);
			data.put("myPost", post);
			
			return this.getResponse(Response.Status.OK, "Data loaded succesfully", data);
		}
	}
}