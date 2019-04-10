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

public class UserDB extends Conexion {
	public UserDB() {
		super();
	}
	
	public ResultSet getUser(UserResource user) throws SQLException {
		if(this.conn != null) {
			String query = "SELECT * FROM `faceSOS`.`users` WHERE user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			return rs;
		}
		return null;
	}
	
	public ResultSet createUser(UserResource user) throws SQLException {
		// TODO: unique username value. If already exists, then error
		if(this.conn != null) {
			String query = "INSERT INTO `faceSOS`.`users`(name,username) VALUE (?,?);";
			PreparedStatement ps = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getName());
			ps.setString(2, user.getUsername());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();

			return rs;
		}
		return null;
	}
	
	public int editUser(UserResource user) throws SQLException {
		if(this.conn != null) {
			String query = "UPDATE `faceSOS`.`users` SET name = ? WHERE user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setString(1, user.getName());
			ps.setInt(2, user.getId());
			return ps.executeUpdate();
		}
		return -1;
	}
	
	public int removeUser(UserResource user) throws SQLException {
		if(this.conn != null) {
			String query = "DELETE FROM `faceSOS`.`users` WHERE user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setInt(1, user.getId());
			return ps.executeUpdate();
		}
		return -1;
	}
	
	public ResultSet getUsers(String name) throws SQLException {
		if(this.conn != null) {
			PreparedStatement ps;
			if(name.equals("")) {
				String query = "SELECT * FROM `faceSOS`.`users`";
				ps = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			} else {
				String query = "SELECT * FROM `faceSOS`.`users` WHERE name LIKE ?;";
				ps = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, "%" + name + "%");
			}
			ResultSet rs = ps.executeQuery();
			return rs;
		}
		return null;
	}
	
	public ResultSet getFriends(UserResource user, int count, String userId) throws SQLException {
		if(this.conn != null) {
			
			String query = "select faceSOS.friends.friend_id,faceSOS.users.name,faceSOS.users.username from faceSOS.users join faceSOS.friends " + 
					"on (faceSOS.users.user_id=faceSOS.friends.friend_id) where faceSOS.friends.user_id= ? "
					+ "union select faceSOS.friends.user_id,faceSOS.users.name,faceSOS.users.username from faceSOS.users join faceSOS.friends " + 
					"on (faceSOS.users.user_id=faceSOS.friends.user_id) where faceSOS.friends.friend_id= ? ;";
			
			if (!user.getName().equals(""))
				query = "select faceSOS.friends.friend_id,faceSOS.users.name,faceSOS.users.username from faceSOS.users join faceSOS.friends " + 
						"on (faceSOS.users.user_id=faceSOS.friends.friend_id) where faceSOS.friends.user_id= ? and faceSOS.users.name like ?"
						+ "union select faceSOS.friends.user_id,faceSOS.users.name,faceSOS.users.username from faceSOS.users join faceSOS.friends " + 
						"on (faceSOS.users.user_id=faceSOS.friends.user_id) where faceSOS.friends.friend_id= ? and faceSOS.users.name like ?;";
			if (count != 0)	
				query += "limit ?;";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setString(1, userId);
			int i = 2;
			String name = user.getName();
			if (!user.getName().equals("") ) {
				name += "%";
				ps.setString(i, name);
				i++;
			}
			ps.setString(i, userId);
			i++;
			if (!name.equals("")) {
				name+="%";
				ps.setString(i, name);
				i++;
			}
			if (count != 0)
				ps.setInt(i, count);
				
			ResultSet rs =ps.executeQuery();
			return rs;
		}
		return null;
	}
	
	
}
