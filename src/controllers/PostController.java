package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import database.PostDB;
import resources.PostResource;
import resources.Posts;
import resources.UserResource;

public class PostController extends Controller {

	public PostController(UriInfo uriInfo) {
		super(uriInfo);
	}

	public Response createPost(PostResource post, String userId) throws SQLException {
		// Check body data
		if (post.getContent() == null) {
			this.getResponse(Response.Status.BAD_REQUEST,
					"Unable to post message: Content is not defined in the body of the post is undefined.");
		} else if (post.getContent().equals("")) {
			this.getResponse(Response.Status.BAD_REQUEST, "Unable to post message: Content is of the post is empty.");
		}
		
		
		UserResource user = new UserResource(userId);
        Response userInformation = this.getUserInformationReponse(user);
        if(userInformation.getStatus() != 200) {
          return userInformation;
        }
        
        user = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
		post.setUser(user);

		// Set data in DB
		PostDB db = new PostDB();
		ResultSet rs = db.createPost(post);

		if (!rs.next()) {
	        return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "There was an error. Unable to create a post");
		}
		
		// Prepare data to send back to client
		// TODO change 1 to "post_id"
		post.setPostId(rs.getString(1));
		String location = this.getPath() + "/post/" + post.getPostId();
		return this.getResponse(Response.Status.CREATED, "Post created succesfully", post, location);		
	}

	public Response deletePost(String userId, String postId) throws SQLException {
		PostResource post = new PostResource();
		
		UserResource user = new UserResource(userId);
        Response userInformation = this.getUserInformationReponse(user);
        if(userInformation.getStatus() != 200) {
          return userInformation;
        }
        user = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
        // Set data in DB
        post.setUser(user);
        post.setPostId(postId);
        PostDB db = new PostDB();
		int affectedRows = db.deletePost(post);

		if (affectedRows <= 0) {
		    return this.getResponse(Response.Status.BAD_REQUEST, "There was an error. Unable to delete message");
		}
        return this.getResponse(Response.Status.OK, "Post deleted succesfully", post);
	}

	public Response editPost(PostResource post, String postId, String userId) throws SQLException {
		post.setPostId(postId);
		System.out.println(userId);
		UserResource user = new UserResource(userId);
        Response userInformation = this.getUserInformationReponse(user);
        if(userInformation.getStatus() != 200) {
          return userInformation;
        }
        user = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
        
        post.setUser(user);

		// Set data in DB
		PostDB db = new PostDB();
		int affectedRows = db.editPost(post);
		
		// TODO: Check if <= is the correct way to handle the error
		if (affectedRows <= 0) {
	        return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "There was an error. Unable to edit message");
		}
		
		// Prepare data to send back to client
        String location = this.getPath() + "/post/" + postId;
        // TODO check if 200 status for this operation is the best option, maybe other 20X?
        return this.getResponse(Response.Status.OK, "Post updated succesfully", post, location);
	}

	public Response getPosts(String userId, int limitTo, int page, String date) throws SQLException {
		Date d = null;
		UserResource user = new UserResource(userId);
        Response userInformation = this.getUserInformationReponse(user);
        if(userInformation.getStatus() != 200) {
          return userInformation;
        }
        if(!date.equals("")) {
        	DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        	try {
				d = df.parse(date);
				
			} catch (ParseException e) {
				System.err.println("Date formate is not correct");
				System.err.println("Should be dd-MM-yyyy");
			}
        }
        user = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
        
        // Set data in DB
		PostDB db = new PostDB();
		ResultSet rs = db.getPosts(user, limitTo, page - 1, d);
		
		if (rs == null) {
		    // Error
	        return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "There was an error. Unable to get post");
		}
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		Posts p = new Posts();
		ArrayList<PostResource> posts = p.getPosts();

		while(rs.next())
		    posts.add(new PostResource(rs, this.getBaseUri()));
		
		data.put("posts", posts);
		data.put("user", user);
		data.put("pagination", this.getPagination(null,null, page, posts.size() > limitTo));
		return this.getResponse(Response.Status.OK, "Post loaded succesfully", data);
	}

	public Response getFriendsPosts(String userId, String content, int limitTo, int page) throws SQLException {		
		// Check that user exists
		UserResource user = new UserResource(userId);
        Response userInformation = this.getUserInformationReponse(user);
        if (userInformation.getStatus() != 200) {
            return userInformation;
        }
		
        
        PostDB db = new PostDB();
		ResultSet rs = db.getPostFriends(user, content, limitTo, page);
		
		if (rs == null) {
		  return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "Unable to get friends information");
		}
		
		HashMap<String, Object> data = new HashMap<String, Object>();
        // Array with posts
        ArrayList<PostResource> posts = new ArrayList<PostResource>();
        while (rs.next())
            posts.add(new PostResource(rs, this.getBaseUri()));
        user = (UserResource) ((HashMap<String, Object>) userInformation.getEntity()).get("data");
        data.put("user", user);
        data.put("posts", posts);
        data.put("nposts", posts.size());
        data.put("pagination", this.getPagination(content,"content", page, posts.size() == limitTo));
        return this.getResponse(Response.Status.OK, "Data loaded succesfully", posts);
	}
}
