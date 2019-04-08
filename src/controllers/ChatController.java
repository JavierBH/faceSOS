package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import resources.ChatResource;
import database.ChatDB;

public class ChatController {
	
	private UriInfo uriInfo;
	
	public ChatController(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	public Response sendChat(
			String senderUserId, 
			String receiverUserId, 
			ChatResource chat) throws SQLException {
		
		if(senderUserId.equals(receiverUserId)) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("User cannot send a chat to itself")
					.build();
		}
		
		chat.setSenderUserId(senderUserId);
		chat.setReceiverUserId(receiverUserId);
		
		ChatDB db = new ChatDB();
		ResultSet rs = db.sendChat(chat);

		if(rs != null && rs.next()) {
			return Response
					.status(Response.Status.CREATED)
					.entity(chat)
					// No header in location response?? In that case, uriInfo useless
					//.header("Location", location)
					//.header("Content-Location", location)
					.build();
		}
		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity("Unable to process request")
				.build();
	}

}
