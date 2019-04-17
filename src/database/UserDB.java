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
		if (this.conn != null) {
			String query = "SELECT * FROM `faceSOS`.`users` WHERE user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			System.out.println("HASTA LUEGO LUCAS");
			return rs;
		}
		return null;
	}

	public ResultSet createUser(UserResource user) throws SQLException {
		// TODO: unique username value. If already exists, then error
		if (this.conn != null) {
			String query = "INSERT INTO `faceSOS`.`users`(name,username,email,biography) VALUE (?,?,?,?);";
			PreparedStatement ps = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getName());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getBiography());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();

			return rs;
		}
		return null;
	}

	public int editUser(UserResource user) throws SQLException {
		if (this.conn != null) {
			String query = "UPDATE `faceSOS`.`users` SET name = ? WHERE user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setString(1, user.getName());
			ps.setInt(2, user.getId());
			return ps.executeUpdate();
		}
		return -1;
	}

	public int removeUser(UserResource user) throws SQLException {
		if (this.conn != null) {
			String query = "DELETE FROM `faceSOS`.`users` WHERE user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setInt(1, user.getId());
			return ps.executeUpdate();
		}
		return -1;
	}

	public ResultSet getUsers(String name, int limitTo, int page) throws SQLException {
		if (this.conn != null) {
			String query = "SELECT * FROM `faceSOS`.`users`";
			query += !(name.equals("")) ? " WHERE name LIKE ?" : "";
			query += limitTo != -1 ? " LIMIT ?,?" : "";
			PreparedStatement ps = this.conn.prepareStatement(query);
			int i = 1;

			if (!name.equals("")) {
				ps.setString(i++, "%" + name + "%");
			}

			if (limitTo != -1) {
				ps.setInt(i++, page * limitTo);
				ps.setInt(i, page * limitTo + limitTo);
			}

			return ps.executeQuery();
		}
		return null;
	}

	public ResultSet getFriends(String name, int count, String userId, int page) throws SQLException {
		if (this.conn != null) {
			// TODO: Change column name UserID1 and UserID2
			// TODO, maybe use *?
			String query = "SELECT faceSOS.friends.UserID2, faceSOS.users.name, faceSOS.users.username FROM faceSOS.users "
					+ "JOIN faceSOS.friends ON (faceSOS.users.user_id=faceSOS.friends.UserID2) where faceSOS.friends.UserID1= ? ";
			query += !name.equals("") ? " AND (faceSOS.users.name like ? or faceSOS.users.username like ?)" : "";
			query += count != 0 ? "LIMIT ?,?;" : "";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setString(1, userId);
			int i = 2;

			if (!name.equals("")) {
				// I think we have to remove username check
				ps.setString(i++, "%" + name + "%");
				ps.setString(i++, "%" + name + "%");
			}

			if (count != -1) {
				ps.setInt(i++, page * count);
				ps.setInt(i, page * count + count);
			}

			return ps.executeQuery();
		}
		return null;
	}

}
