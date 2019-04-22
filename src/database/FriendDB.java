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
          String query = "SELECT faceSOS.friends.*, faceSOS.users.* FROM faceSOS.users " + 
              "JOIN faceSOS.friends ON (faceSOS.users.user_id = faceSOS.friends.user2_id) WHERE faceSOS.friends.user1_id = ?";
          query += nameIsSet ? " AND (faceSOS.users.name like ? or faceSOS.users.username LIKE ?)" : "";
          query += " LIMIT ?,?;";
          PreparedStatement ps = this.conn.prepareStatement(query);
          ps.setString(1, userId);
          int i = 2;

          if (nameIsSet) {
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

	public int addFriend(int idUser, int idFriend) throws SQLException {
	    if(this.conn == null)
	        return -1;
	    
	    String query = "INSERT IGNORE INTO faceSOS.friends (user1_id,user2_id) VALUES (?,?);";
	    PreparedStatement ps = this.conn.prepareStatement(query);
		ps.setInt(1, idUser);
		ps.setInt(2, idFriend);
		ps.executeUpdate();
		ps.setInt(1, idFriend);
		ps.setInt(2, idUser);
		return ps.executeUpdate();
	}

	public int removeFriend(UserResource friend1, UserResource friend2) throws SQLException {
	    if(this.conn == null)
            return -1;
		String query = "DELETE FROM faceSOS.friends WHERE (user1_id = ? AND user2_id = ?);";
		PreparedStatement ps = this.conn.prepareStatement(query);
		ps.setInt(1, friend1.getId());
		ps.setInt(2, friend2.getId());
		ps.executeUpdate();
		query = "DELETE FROM faceSOS.friends WHERE (user1_id = ? AND user2_id = ?);";
		ps.setInt(1, friend2.getId());
		ps.setInt(2, friend1.getId());
		return ps.executeUpdate();
	}
}
