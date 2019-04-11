package views;

import javax.ws.rs.Path;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import controllers.ChatController;
import controllers.FriendController;
import resources.ChatResource;

@Path("friend/{userId}")
public class FriendView {
	// I think is better to join UserView and FriendView. 
	// At the end, they are the same thing, an UserResource
	
	@Context
	private UriInfo uriInfo;

	
	//Add friend
	@PUT
	@Path("/add/{friendId}")
	//@Produces(MediaType.APPLICATION_JSON)
	public Response addFriend(
			@PathParam("userId")String userId,
			@PathParam("friendId") String friendId) 
					throws SQLException {
		return new FriendController(uriInfo).addFriend(userId,friendId);
	}
	
	//Remove friend
	@PUT
	@Path("/remove/{idFriend}")
	//@Produces(MediaType.APPLICATION_JSON)
	public Response removeFriend(@PathParam("idUser")String idUser, @PathParam("idFriend") String idFriend) throws SQLException {
		return new FriendController(uriInfo).removeFriend(idUser,idFriend);
	}

}
