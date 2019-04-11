package database;
import resources.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FriendDB extends Conexion{
	
	public FriendDB() {
		super();
	}
	
	public boolean checkFriends(int idUser, int idFriend) throws SQLException {
		String query = "Select * from faceSOS.friends where (user_id= ? and friend_id=?) or (user_id= ? and friend_id=?);";
		PreparedStatement ps = this.conn.prepareStatement(query);
		ps.setInt(1, idUser);
		ps.setInt(2, idFriend);
		ps.setInt(4, idUser);
		ps.setInt(3, idFriend);
		ResultSet rs=ps.executeQuery();
		return rs.next();
	}
	
	public void addFriend(int idUser, int idFriend) throws SQLException {
		String query = "INSERT INTO `faceSOS`.`friends` (user_id,friend_id) VALUES (?,?);";
		PreparedStatement ps = this.conn.prepareStatement(query);
		ps.setInt(1, idUser);
		ps.setInt(2, idFriend);
		ps.executeUpdate();
	}
	
	public void removeFriend(UserResource friend1, UserResource friend2) throws SQLException {
		String query = "DELETE FROM `faceSOS`.`friends` WHERE (user_id= ? AND friend_id=?) OR (user_id= ? and friend_id=?);";
		PreparedStatement ps = this.conn.prepareStatement(query);
		ps.setInt(1, friend1.getId());
		ps.setInt(2, friend2.getId());
		ps.setInt(4, friend1.getId());
		ps.setInt(3, friend2.getId());
		ps.executeUpdate();
		
	}
}
