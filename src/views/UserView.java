package views;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.FormParam;
import org.apache.naming.NamingContext;
import controllers.*;
import resources.*;

@Path("user")
public class UserView {
	@Context
	private UriInfo uriInfo;
	
	@GET
	@Path("/{userId}")
	public Response getUser(
			@PathParam("userId") String userId) throws SQLException {
		return new UserController(this.uriInfo).getUser(userId);
	}
	
	//Crear nuevo usuario
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(UserResource user) throws SQLException {
		return new UserController(this.uriInfo).createUser(user);
	}
	
	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editUser(
			@PathParam("userId") String userId,
			UserResource user) throws SQLException {
		return new UserController(this.uriInfo).editUser(userId, user);
	}
	
	@DELETE
	@Path("/{userId}")
	public Response removeUser(
			@PathParam("userId") String userId) throws SQLException {
		return new UserController(this.uriInfo).removeUser(userId);
	}
}
