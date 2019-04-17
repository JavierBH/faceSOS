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

public class FriendDB extends Conexion {

	public FriendDB() {
		super();
	}

	// TODO: Add check if conn is null in all operations

	public boolean checkFriends(int idUser, int idFriend) throws SQLException {
		String query = "SELECT * FROM faceSOS.friends WHERE (user_id= ? and friend_id=?) OR (user_id= ? and friend_id=?);";
		PreparedStatement ps = this.conn.prepareStatement(query);
		ps.setInt(1, idUser);
		ps.setInt(2, idFriend);
		ps.setInt(4, idUser);
		ps.setInt(3, idFriend);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}

	public int addFriend(int idUser, int idFriend) throws SQLException {
		String query = "INSERT IGNORE INTO `faceSOS`.`friends` (UserID1,UserID2) VALUES (?,?);";
		PreparedStatement ps = this.conn.prepareStatement(query);
		// Max likes this
		ps.setInt(1, idUser);
		ps.setInt(2, idFriend);
		ps.executeUpdate();
		ps.setInt(1, idFriend);
		ps.setInt(2, idUser);
		// END: Max likes this
		return ps.executeUpdate();
	}

	public int removeFriend(UserResource friend1, UserResource friend2) throws SQLException {
		// this remove the two rows??
		String query = "DELETE FROM `faceSOS`.`friends` WHERE (UserID1= ? AND UserID2=?) OR (UserID1= ? and UserID2=?);";
		PreparedStatement ps = this.conn.prepareStatement(query);
		ps.setInt(1, friend1.getId());
		ps.setInt(2, friend2.getId());
		ps.setInt(4, friend1.getId());
		ps.setInt(3, friend2.getId());
		return ps.executeUpdate();

	}
}
