package controllers;
import database.*;
import resources.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class UserController   {
	private UriInfo uriInfo;
	
	public UserController(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	
	public Response getUser(String userId) throws SQLException {
		UserResource user = new UserResource(userId);
		UserDB db = new UserDB();
		ResultSet rs = db.getUser(user);
		if(rs != null && rs.next()) {
			System.out.println();
			user.setName(rs.getString("name"));
			user.setUsername(rs.getString("username"));
			String location = this.uriInfo.getAbsolutePath() + "/user/" + userId;
			return Response
					.status(Response.Status.CREATED)
					.entity(user)
					.header("Location", location)
					.header("Content-Location", location)
					.build();
		}
		// Error message should be an object
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to get user information").build();
	}

	public Response createUser(UserResource user) throws SQLException {
		UserDB db = new UserDB();
		ResultSet rs = db.createUser(user);
		if(rs != null && rs.next()) {
			user.setId(rs.getString(1));
			String location = this.uriInfo.getAbsolutePath() + "/user/" + user.getId();
			return Response
					.status(Response.Status.CREATED)
					.entity(user)
					.header("Location", location)
					.header("Content-Location", location)
					.build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to create user").build();
	}
	
	public Response editUser(String userId, UserResource user) throws SQLException {
		user.setId(userId);
		UserDB db = new UserDB();
		ResultSet rs = db.editUser(user);
		if(rs != null && rs.next()) {
			System.out.println("LÑO");
			String location = this.uriInfo.getAbsolutePath() + "/user/" + user.getId();
			return Response
					// TODO check Status.ACCEPTED ???
					.status(Response.Status.ACCEPTED)
					.entity(user)
					.header("Location", location)
					.header("Content-Location", location)
					.build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to create user").build();
	}
	
	public Response removeUser(String userId) throws SQLException {
		UserResource user = new UserResource(userId);
		UserDB db = new UserDB();
		int i = db.removeUser(user);
		if(i > 0) {
			return Response.status(Response.Status.CREATED).entity(user).
				header("Location", i).header("Content-Location", i).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to create user").build();
	}
}
