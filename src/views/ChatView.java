package views;

import java.sql.SQLException;
import javax.ws.rs.Consumes;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import controllers.ChatController;
import resources.ChatResource;

@Path("/user/chat")
public class ChatView {

	@Context
	private UriInfo uriInfo;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sendChat(ChatResource chat, @QueryParam("from") int senderUserId,
			@QueryParam("to") int receiverUserId) throws SQLException {
		return new ChatController(this.uriInfo).sendChat(chat, senderUserId, receiverUserId);
	}
}
