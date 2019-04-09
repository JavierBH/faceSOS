package views;
import java.sql.SQLException;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import controllers.UserController;
import resources.UserResource;

@Path("/user")
public class UserView {
	
	@Context
	private UriInfo uriInfo;
	
	@GET
	@Path("/{userId}")
	public Response getUser(
			@PathParam("userId") String userId) throws SQLException {
		return new UserController(this.uriInfo).getUser(userId);
	}
	
	@GET
	public Response getUsers(
			@QueryParam("name") @DefaultValue("") String name) throws SQLException {
		return new UserController(this.uriInfo).getUsers(name);
	}
	
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
	
	@GET
	@Path("{userId}/friends")
	public Response getFriends(@QueryParam("name") @DefaultValue("") String name,
			@QueryParam("count") @DefaultValue("0") String count,
			@PathParam("userId") String userId) throws SQLException {
		int cont=Integer.parseInt(count);
		return new UserController(this.uriInfo).getFriends(name,cont,userId);
	}
}
