package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import resources.ChatResource;

public class ChatDB extends Conexion{
	
	public ChatDB () {
		super();
	}
	
	public ResultSet sendChat(ChatResource chat) throws SQLException {
		if(this.conn != null) {
			String query = "INSERT INTO `faceSOS`.`chats` "
					+ "(sender_user_id, receiver_user_id, content) "
					+ "VALUE (?,?,?)";
			PreparedStatement ps = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, chat.getSenderUserId());
			ps.setInt(2, chat.getReceiverUserId());
			ps.setString(3, chat.getContent());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			return rs;
		}
		return null;
	}
}
