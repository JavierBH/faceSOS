package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import resources.UserResource;

public class FriendDB extends Conexion {

	public FriendDB() {
		super();
	}
	
	public ResultSet getFriends(String name, int count, String userId, int page) throws SQLException {
      if (this.conn != null) {
          boolean nameIsSet = name != null;
          String query = "SELECT friends.*, users.* FROM users " + 
              "JOIN friends ON (users.user_id = friends.user2_id) WHERE friends.user1_id = ?";
          query += nameIsSet ? " AND (users.name like ? or users.username LIKE ?)" : "";
          query += " LIMIT ?,?;";
          PreparedStatement ps = this.conn.prepareStatement(query);
          ps.setString(1, userId);
          int i = 2;

          if (nameIsSet) {
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

	// TODO: Add check if conn is null in all operations
	public int addFriend(int idUser, int idFriend) throws SQLException {
	    if(this.conn == null)
	        return -1;
	    
	    String query = "INSERT IGNORE INTO friends (user1_id,user2_id) VALUES (?,?);";
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
	    if(this.conn == null)
            return -1;
		// this remove the two rows??
		String query = "DELETE FROM friends WHERE (user1_id = ? AND user2_id = ?) OR (user2_id = ? AND user1_id = ?);";
		PreparedStatement ps = this.conn.prepareStatement(query);
		ps.setInt(1, friend1.getId());
		ps.setInt(2, friend2.getId());
		ps.setInt(4, friend1.getId());
		ps.setInt(3, friend2.getId());
		return ps.executeUpdate();

	}
}
