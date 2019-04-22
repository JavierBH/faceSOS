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
	
	private UriInfo uriInfo;
	
	protected Controller(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	protected String getPath() {
		return this.uriInfo.getAbsolutePath().toString();
	}
	
	protected String getBaseUri() {
	    return this.uriInfo.getBaseUri().toString();
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
			user.setUpdatedAt(rs.getTimestamp("updated_at"));
			user.setBiography(rs.getString("biography"));
			return this.getResponse(Response.Status.OK, "Data loaded succesfully", user);
		}
	}
	
	/*
	 * Get number of friends of an user
	 */
	protected int getUserNumberFriends(UserResource user) throws SQLException {
		
		UserDB db = new UserDB();
		ResultSet rs = db.getUserNumberFriends(user);
		if(rs.next()) 
			return rs.getInt(1);
		
		return -1;
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
	
	protected HashMap<String, Object> getPagination(String name, String filter, int page, boolean showNext) {
      HashMap<String, Object> pagination = new HashMap<String, Object>();
      pagination.put("page", page);
      String base = this.getPath() + "?";
      if(name != null) {
      	switch(filter) {
      		case "name":
      			base += "name=" + name + "&";
      			break;
      		case "content":
      			base += "content=" + name + "&";
      			break;
      		default:
      			base += "date=" + name + "&";
      	}
      }
    	  
      if(showNext)
          pagination.put("next", base + "page=" + (page + 1));
      if(page != 1)
          pagination.put("previous", base + "page=" + (page - 1));
      return pagination;
  }
}
