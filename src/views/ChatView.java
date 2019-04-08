package views;

import java.sql.SQLException;
import javax.ws.rs.Consumes;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import controllers.ChatController;
import resources.ChatResource;

@Path("/chat/fromUser/{senderUserId}/toUser/{receiverUserId}")
public class ChatView {
	
	@Context
	private UriInfo uriInfo;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(
			@PathParam("senderUserId") String senderUserId,
			@PathParam("receiverUserId") String receiverUserId,
			ChatResource chat
			) throws SQLException {
		return new ChatController(this.uriInfo).sendChat(senderUserId, receiverUserId, chat);
	}
}
