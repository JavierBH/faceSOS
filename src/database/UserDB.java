package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import resources.UserResource;

public class UserDB extends Conexion {
	public UserDB() {
		super();
	}

	public ResultSet getUser(UserResource user) throws SQLException {
		if (this.conn != null) {
			String query = "SELECT * FROM faceSOS.users WHERE user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			return rs;
		}
		return null;
	}

	public ResultSet createUser(UserResource user) throws SQLException {
		if (this.conn != null) {
			String query = "INSERT INTO faceSOS.users (name,username,email,biography) VALUE (?,?,?,?);";
			PreparedStatement ps = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getName());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getBiography());
			ps.executeUpdate();
			return ps.getGeneratedKeys();
		}
		return null;
	}

	public int editUser(UserResource user) throws SQLException {
		if (this.conn != null) {
			String query = "UPDATE faceSOS.users SET name = ? WHERE user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setString(1, user.getName());
			ps.setInt(2, user.getId());
			return ps.executeUpdate();
		}
		return -1;
	}

	public int removeUser(UserResource user) throws SQLException {
		if (this.conn != null) {
			String query = "DELETE FROM faceSOS.users WHERE user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setInt(1, user.getId());
			return ps.executeUpdate();
		}
		return -1;
	}

	public ResultSet getUsers(String name, int limitTo, int page) throws SQLException {
		if (this.conn != null) {
			boolean nameIsSet = name != null;
			String query = nameIsSet
					? "SELECT * FROM faceSOS.users WHERE name LIKE ? LIMIT ?,?" 
					: "SELECT * FROM faceSOS.users LIMIT ?,?";
			PreparedStatement ps = this.conn.prepareStatement(query);
			
			int i = 1;
			if (nameIsSet) 
				ps.setString(i++, "%" + name + "%");
				
			ps.setInt(i++, page * limitTo);
			ps.setInt(i, page * limitTo + limitTo);

			return ps.executeQuery();
		}
		return null;
	}

	public ResultSet getUserNumberFriends(UserResource user) throws SQLException {
		if (this.conn != null) {
			String query = "SELECT COUNT(user1_id) FROM faceSOS.friends WHERE user1_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setInt(1, user.getId());
			return ps.executeQuery();
		}
		return null;
	}
}
