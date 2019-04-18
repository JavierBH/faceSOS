package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.ws.rs.core.Response;
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
	private final int ELEMENTS_PAGE = 25;

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
			return this.getInternalServerErrorResponse("There was a problem. Unable to get user information");
		} else if (!rs.next()) {
			return this.getBadRequestResponse("User " + user.getId() + " not found.");
		} else {
			// Prepare data to send back to client
			user.setName(rs.getString("name"));
			user.setUsername(rs.getString("username"));
			user.setEmail(rs.getString("email"));
			user.setCreatedAt(rs.getTimestamp("created_at"));
			user.setBiography(rs.getString("biography"));
			return this.getOkResponse(user, "Data loaded succesfully");
		}
	}

	/*
	 * Add error message to response
	 */
	protected Response getInternalServerErrorResponse(String message) {
	    HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("message", message);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
	}

	protected Response getBadRequestResponse(String message) {
        HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("message", message);
		return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
	}

	protected Response getOkResponse(Object data, String message) {
        HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("message", message);
		res.put("data", data);
		return Response.status(Response.Status.OK).entity(res).build();
	}

	protected Response getCreatedResponse(Object data, String location, String message) {
        HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("message", message);
		res.put("data", data);
		return Response.status(Response.Status.OK).entity(res).header("Location", location)
				.header("Content-Location", location).build();
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
