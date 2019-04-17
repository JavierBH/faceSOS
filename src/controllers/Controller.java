package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import database.UserDB;
import resources.UserResource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class Controller {
	/*****************************/
	// TODO: Check codes. Right now, we have:
	// PUT and POST return CREATED => 201
	// GET and DELETE return OK => 200
	/*****************************/

	private UriInfo uriInfo;

	public Controller(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	public Controller() {
	}

	public String getPath() {
		return this.uriInfo.getAbsolutePath().toString();
	}

	public Response getUserInformationReponse(HashMap<String, Object> res, UserResource user) throws SQLException {
		UserDB db = new UserDB();
		ResultSet rs = db.getUser(user);
		if (rs == null) {
			return this.getInternalServerErrorResponse(res, "There was a problem. Unable to get user information");
		} else if (!rs.next()) {
			return this.getBadRequestResponse(res, "User " + user.getId() + " not found.");
		} else {
			// Prepare data to send back to client
			user.setName(rs.getString("name"));
			user.setUsername(rs.getString("username"));
			user.setEmail(rs.getString("email"));
			user.setCreatedAt(rs.getTimestamp("created_at"));
			user.setBiography(rs.getString("biography"));
			return this.getOkResponse(res, user, "Data loaded succesfully");
		}
	}

	/*
	 * Add error message to response
	 */
	public Response getInternalServerErrorResponse(HashMap<String, Object> res, String message) {
		res.put("message", message);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
	}

	public Response getBadRequestResponse(HashMap<String, Object> res, String message) {
		res.put("message", message);
		return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
	}

	public Response getOkResponse(HashMap<String, Object> res, Object data, String message) {
		res.put("message", message);
		res.put("data", data);
		return Response.status(Response.Status.OK).entity(res).build();
	}

	public Response getCreatedResponse(HashMap<String, Object> res, Object data, String location, String message) {
		res.put("message", message);
		res.put("data", data);
		return Response.status(Response.Status.OK).entity(res).header("Location", location)
				.header("Content-Location", location).build();
	}
}
