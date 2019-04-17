package database;

import resources.PostResource;
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

public class PostDB extends Conexion {

	public PostDB() {
		super();
	}

	public ResultSet createPost(PostResource post) throws SQLException {
		if (this.conn != null) {
			String query = "INSERT INTO `faceSOS`.`posts`(user_id,content) VALUE (?,?);";
			PreparedStatement ps = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, post.getUserId());
			ps.setString(2, post.getContent());
			ps.executeUpdate();
			return ps.getGeneratedKeys();
		}
		return null;
	}

	public int deletePost(PostResource post) throws SQLException {
		if (this.conn != null) {
			String query = "DELETE FROM `faceSOS`.`posts` WHERE  post_id = ? AND user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setInt(1, post.getUserId());
			ps.setInt(2, post.getPostId());
			return ps.executeUpdate();
		}
		return -1;
	}

	public int editPost(PostResource post) throws SQLException {
		if (this.conn != null) {
			String query = "UPDATE `faceSOS`.`posts` SET content = ?  WHERE post_id = ? AND user_id = ?;";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setString(1, post.getContent());
			ps.setInt(2, post.getPostId());
			ps.setInt(3, post.getUserId());
			return ps.executeUpdate();
		}
		return -1;
	}

	public ResultSet getPost(String userId, String limitTo) throws SQLException {
		if (this.conn != null) {
			String query = "SELECT * FROM faceSOS.posts WHERE user_id = ? ORDER BY created_at DESC ";
			query += !limitTo.equals("") ? "LIMIT ?;" : "";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setString(1, userId);
			if (!limitTo.equals(""))
				ps.setString(2, limitTo);
			return ps.executeQuery();
		}
		return null;
	}

	public ResultSet getPostFriends(String userId, String content) throws SQLException {
		if (this.conn != null) {
			String query = "SELECT faceSOS.posts.post_id,faceSOS.posts.user_id, faceSOS.posts.content, faceSOS.posts.created_at FROM faceSOS.posts JOIN"
					+ " faceSOS.friends ON (faceSOS.posts.user_id=faceSOS.friends.UserID2) WHERE faceSOS.friends.UserID1= ?";
			query += !content.equals("") ? " AND faceSOS.posts.content LIKE ?" : "";
			PreparedStatement ps = this.conn.prepareStatement(query);
			ps.setString(1, userId);
			if (!content.equals("")) {
				ps.setString(2, "%" + content + "%");
			}
			return ps.executeQuery();
		}
		return null;
	}
}
