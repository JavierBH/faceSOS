package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
			return this.getBadRequestResponse("Unable to create user. "
					+ "One or more of the following fields are empty: user, username or/and email");
		}

		// Set data in DB
		UserDB db = new UserDB();
		ResultSet rs = db.createUser(user);

		if (rs.next()) {
			// Prepare data to send back to client

			// TODO: I would like to change 1 to "user_id". But #doesntWork
			user.setUserId(rs.getString(1));
			String location = this.getPath() + "/user/" + user.getId();
			return this.getCreatedResponse(user, location, "User created succesfully");
		}

		// Error
		return this.getInternalServerErrorResponse("There was an error. Unable to create user");
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

		if (rowsAffected > 0) {
			// Prepare data to send back to client
			UserResource fullUserInfo = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
			user.join(fullUserInfo);
			String location = this.getPath() + "/user/" + user.getId();
			return this.getCreatedResponse(user, location, "Data updated succesfully");
		} else if (rowsAffected == 0) {
			// User doesn't exist so we create it
			return this.createUser(user);
		}

		// Error
		return this.getInternalServerErrorResponse("There was an error. Unable to update user");
	}

	/*
	 * Remove a user using userId
	 */
	public Response removeUser(String userId) throws SQLException {
		// Delete data in DB
		UserResource user = new UserResource(userId);
		UserDB db = new UserDB();
		int rowsAffected = db.removeUser(user);

		if (rowsAffected > 0) {
			// Prepare data to send back to client
			return this.getOkResponse(user, "User removed succesfully");
		} else if (rowsAffected == 0) {
			// User doesn't exist so we return bad request
			return this.getBadRequestResponse("User does not exist");
		}

		// Error
		return this.getInternalServerErrorResponse("There was an error. Unable to remove user");
	}

	/*
	 * Get basic data from userId
	 */
	public Response getUsers(String name, int limitTo, int page) throws SQLException {
		// Get data from DB
		UserDB db = new UserDB();
		ResultSet rs = db.getUsers(name, this.getElementsPage(limitTo), page - 1);

		if (rs != null) {
			// Array with users
			ArrayList<UserResource> users = new ArrayList<UserResource>();
			while (rs.next())
			    users.add(new UserResource(rs.getInt("user_id"), rs, this.getBaseUri()));
			
			// Array with users
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("users", users);
			data.put("nUsers", users.size());
			data.put("pagination", this.getPagination(name, page, users.size() == limitTo));

			// Prepare data to send back to client
			return this.getOkResponse(data, "Data loaded succesfully");
		}

		// Error
		return this.getInternalServerErrorResponse("There was a problem. Unable to get user information");
	}
}
