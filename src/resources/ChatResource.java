package resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChatResource {
	private int senderUserId;
	private int receiverUserId;
	private String content;
	
	public ChatResource() {}
	
	public int getSenderUserId() {
		return this.senderUserId;
	}
	
	public int getReceiverUserId() {
		return this.receiverUserId;
	}
	
	@XmlAttribute(required=true)
	public String getContent() {
		return this.content;
	}
	
	public void setSenderUserId(String senderUserId) {
		this.senderUserId = Integer.parseInt(senderUserId);
	}
	
	public void setReceiverUserId(String receiverUserId) {
		this.receiverUserId = Integer.parseInt(receiverUserId);
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
