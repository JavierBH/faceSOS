package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import database.UserDB;
import resources.UserResource;

public class Controller {
	/*****************************/
	// TODO: Check codes. Right now, we have:
	// PUT and POST return CREATED => 201
	// GET and DELETE return OK => 200
	/*****************************/

	private UriInfo uriInfo;
	private final int ELEMENTS_PAGE = 2;

	protected Controller(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	protected String getPath() {
		// TODO: Remove last char if it is sidebar ('/')
		return this.uriInfo.getAbsolutePath().toString();
	}
	
	protected String getBaseUri() {
	    return this.uriInfo.getBaseUri().toString();
	}
	
	protected int getElementsPage(int userChoice) {
	  return userChoice != 0 ? userChoice : this.ELEMENTS_PAGE;
	}

	protected Response getUserInformationReponse(UserResource user) throws SQLException {
		UserDB db = new UserDB();
		ResultSet rs = db.getUser(user);
		if (rs == null) {
			return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "There was a problem. Unable to get user information");
		} else if (!rs.next()) {
			return this.getResponse(Response.Status.BAD_REQUEST, "User with id " + user.getId() + " not found.");
		} else {
			// Prepare data to send back to client
			user.setName(rs.getString("name"));
			user.setUsername(rs.getString("username"));
			user.setEmail(rs.getString("email"));
			user.setCreatedAt(rs.getTimestamp("created_at"));
			user.setBiography(rs.getString("biography"));
			return this.getResponse(Response.Status.OK, "Data loaded succesfully", user);
		}
	}

	/*
	 * Add error message to response
	 */
	protected Response getResponse(Status status, String message) {
	  return this.getResponse(status, message, null, null);
	}
	protected Response getResponse(Status status, String message, Object data) {
      return this.getResponse(status, message, data, null);
    }
	protected Response getResponse(Status status, String message, Object data, String location) {
      HashMap<String, Object> res = new HashMap<String, Object>();
      res.put("message", message);
      if(data != null) { 
        res.put("data", data);
      }
      ResponseBuilder responseBuilder = Response.status(status).entity(res);
      if(location != null) {
        responseBuilder.header("Location", location).header("Content-Location", location);
      }
      return responseBuilder.build();
    }
	
	protected HashMap<String, Object> getPagination(String name, int page, boolean showNext) {
      // TODO: Think about returning next attribute when for example we have 4 rows in our DB and the 
      // limitTo is the same
      HashMap<String, Object> pagination = new HashMap<String, Object>();
      pagination.put("page", page);
      String base = this.getPath() + "?"
              + (name != null ? "name=" + name + "&" : "");
      if(showNext)
          pagination.put("next", base + "page=" + (page + 1));
      if(page != 1)
          pagination.put("previous", base + "page=" + (page - 1));
      return pagination;
  }
}
