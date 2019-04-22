package resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChatResource {
	private int chatId;
	private UserResource senderUser;
	private UserResource receiverUser;
	private String content;

	public ChatResource() {
	}
	public ChatResource(String chatId) {
		this.chatId = Integer.parseInt(chatId);
	}

	public int getChatId() {
		return this.chatId;
	}

	public UserResource getSenderUser() {
		return this.senderUser;
	}

	public UserResource getReceiverUser() {
		return this.receiverUser;
	}

	@XmlAttribute(required = true)
	public String getContent() {
		return this.content;
	}

	public void setChatId(String chatId) {
		this.chatId = Integer.parseInt(chatId);
	}
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}
	public void setSenderUser(String senderUserId) {
		this.senderUser = new UserResource(senderUserId);
	}

	public void setSenderUser(int senderUserId) {
		this.senderUser = new UserResource(senderUserId);
	}

	public void setReceiverUser(String receiverUserId) {
		this.receiverUser = new UserResource(receiverUserId);
	}

	public void setReceiverUser(int receiverUserId) {
		this.receiverUser = new UserResource(receiverUserId);
	}

	public void setContent(String content) {
		this.content = content;
	}
}
