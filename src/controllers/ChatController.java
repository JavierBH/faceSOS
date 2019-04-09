package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import resources.ChatResource;
import database.ChatDB;

public class ChatController extends Controller {
	
	public ChatController(UriInfo uriInfo) {
		super(uriInfo);
	}
	
	public Response sendChat(ChatResource chat) throws SQLException {
		
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();
		if(chat.getContent() == null 
				|| chat.getReceiverUserId() == 0 
				|| chat.getSenderUserId() == 0) {
			this.getBadRequestResponse(res, 
					"Cannot send message without one of the following parameters: "
					+ "senderUserId, receiverUserId or content in the body");
		}
			
		if(chat.getSenderUserId() == chat.getReceiverUserId()) {
			this.getBadRequestResponse(res, "User cannot send a chat to itself");
		}
		
		// Set data in DB
		ChatDB db = new ChatDB();
		ResultSet rs = db.sendChat(chat);

		if(rs != null && rs.next()) {
			// Prepare data to send back to client
			chat.setChatId(rs.getString(1));
			String location = this.getPath() + "/user/chat/" + chat.getChatId();
			return this.getCreatedResponse(res, chat, location, "Chat created succesfully");
		}
		
		// Error
		return this.getInternalServerErrorResponse(res, "Unable to process request");
	}

}
