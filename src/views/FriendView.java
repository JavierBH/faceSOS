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

@Path("uaser/{idUser}")
public class FriendView {
	
	@Context
	private UriInfo uriInfo;

	
	//Add friend
	@PUT
	@Path("/friend/add/{idFriend}")
	//@Produces(MediaType.APPLICATION_JSON)
	public Response addFriend(
			@PathParam("userId")String userId,
			@PathParam("friendId") String friendId) 
					throws SQLException {
		return new FriendController(uriInfo).addFriend(userId,friendId);
	}

}
