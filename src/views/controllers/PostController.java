package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import database.PostDB;
import resources.PostResource;
import resources.UserResource;

public class PostController extends Controller {

	public PostController(UriInfo uriInfo) {
		super(uriInfo);
	}

	public Response createPost(PostResource post, String userId) throws SQLException {
		// Check body data
		if (post.getContent() == null) {
			this.getBadRequestResponse(
					"Unable to post message: Content is not defined in the body of the post is undefined.");
		} else if (post.getContent().equals("")) {
			this.getBadRequestResponse("Unable to post message: Content is of the post is empty.");
		}
		
		
		UserResource user = new UserResource(userId);
        Response userInformation = this.getUserInformationReponse(user);
        if(userInformation == null) {
          return userInformation;
        }
        user = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
		
		post.setUser(user);

		// Set data in DB
		PostDB db = new PostDB();
		ResultSet rs = db.createPost(post);

		if (rs.next()) {
			// Prepare data to send back to client
			post.setPostId(rs.getString(1));
			String location = this.getPath() + "/post/" + post.getPostId();
			return this.getCreatedResponse(post, location, "Post created succesfully");
		}

		// Error
		return this.getInternalServerErrorResponse("There was an error. Unable to create a post");
	}

	public Response deletePost(String userId, String postId) throws SQLException {
		PostResource post = new PostResource(postId);
		
		UserResource user = new UserResource(userId);
        Response userInformation = this.getUserInformationReponse(user);
        if(userInformation == null) {
          return userInformation;
        }
        user = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
        
        post.setUser(user);
        
		// Set data in DB
		PostDB db = new PostDB();
		int affectedRows = db.deletePost(post);

		if (affectedRows > 0) {
			// Prepare data to send back to client
			return this.getOkResponse(post, "Post deleted succesfully");
		}

		// Error
		return this.getInternalServerErrorResponse("There was an error. Unable to delete message");
	}

	public Response editPost(PostResource post, String postId, String userId) throws SQLException {
		post.setPostId(postId);

		UserResource user = new UserResource(userId);
        Response userInformation = this.getUserInformationReponse(user);
        if(userInformation == null) {
          return userInformation;
        }
        user = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
        
        post.setUser(user);

		// Set data in DB
		PostDB db = new PostDB();
		int affectedRows = db.editPost(post);

		if (affectedRows > 0) {
			// Prepare data to send back to client
			String location = this.getPath() + "/post/" + postId;
			return this.getCreatedResponse(post, location, "Post updated succesfully");
		}

		// Error
		return this.getInternalServerErrorResponse("There was an error. Unable to edit message");
	}

	public Response getPost(String userId, int limitTo) throws SQLException {
		UserResource user = new UserResource(userId);
        Response userInformation = this.getUserInformationReponse(user);
        if(userInformation == null) {
          return userInformation;
        }
        user = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
        
		// Set data in DB
		PostDB db = new PostDB();
		ResultSet rs = db.getPost(user, this.getElementsPage(limitTo));
		
		if (rs.next()) {
			// Prepare data to send back to client
		    PostResource post = new PostResource(rs, this.getBaseUri());
			post.setUser(user);
			return this.getOkResponse(post, "Post loaded succesfully");
		}

		// Error
		return this.getInternalServerErrorResponse("There was an error. Unable to get message");
	}

	public Response getFriendsPosts(String userId, String content) throws SQLException {		
		// Check that user exists
		UserResource user = new UserResource(userId);
        Response userInformation = this.getUserInformationReponse(user);
        if (userInformation.getStatus() != 200) {
            return userInformation;
        }
		
        
        PostDB db = new PostDB();
		ResultSet rs = db.getPostFriends(user, content);
		if (rs != null) {
		    HashMap<String, Object> data = new HashMap<String, Object>();
			// Array with posts
			ArrayList<PostResource> posts = new ArrayList<PostResource>();
			while (rs.next())
				posts.add(new PostResource(rs, this.getBaseUri()));
            user = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
            data.put("user", user);
            data.put("posts", posts);
            data.put("nposts", posts.size());
			return this.getOkResponse(posts, "Data loaded succesfully");
		}

		// Error
		return this.getInternalServerErrorResponse("Unable to get friends information");
	}
}
