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
	
	public Response sendChat(ChatResource chat, int senderUserId, int receiverUserId) throws SQLException {
		chat.setSenderUser(senderUserId);
		chat.setReceiverUser(receiverUserId);
		
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();
		if(chat.getContent() == null 
				|| chat.getSenderUser().getId() == 0 
				|| chat.getReceiverUser().getId() == 0) {
			this.getBadRequestResponse(res, 
					"Cannot send message without one of the following parameters: "
					+ "senderUserId, receiverUserId or content in the body");
		}
			
		if(chat.getSenderUser().getId() == chat.getReceiverUser().getId()) {
			this.getBadRequestResponse(res, "User cannot send a chat to itself");
		}
		
		// Check that both users exist
		Response senderUserInformation = this.getUserInformationReponse(res, chat.getSenderUser());
		if(senderUserInformation.getStatus() != 200) {
			return senderUserInformation;
		}
		Response receiverUserInformation = this.getUserInformationReponse(res, chat.getReceiverUser());
		if(receiverUserInformation.getStatus() != 200) {
			return receiverUserInformation;
		}
		
		// Set data in DB
		ChatDB db = new ChatDB();
		ResultSet rs = db.sendChat(chat);

		if(rs != null && rs.next()) {
			// Prepare data to send back to client
			chat.setChatId(rs.getString(1));

			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("chat", chat);
			data.put("from", senderUserInformation.getEntity());
			data.put("to", receiverUserInformation.getEntity());
			String location = this.getPath() + "/user/chat/" + chat.getChatId();
			return this.getCreatedResponse(res, chat, location, "Chat created succesfully");
		}
		
		// Error
		return this.getInternalServerErrorResponse(res, "Unable to process request");
	}

}
