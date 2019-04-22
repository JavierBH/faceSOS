package views;

import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import controllers.PostController;
import resources.PostResource;

@Path("/user/{userId}/post")
public class PostView {

	@Context
	private UriInfo uriInfo;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPost(@PathParam("userId") String userId, PostResource post) throws SQLException {
		return new PostController(this.uriInfo).createPost(post, userId);
	}

	@Path("/{postId}")
	@DELETE
	public Response deletePost(@PathParam("userId") String userId, @PathParam("postId") String postId)
			throws SQLException {
		return new PostController(this.uriInfo).deletePost(userId, postId);
	}

	@Path("/{postId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editPost(@PathParam("userId") String userId, @PathParam("postId") String postId,
			PostResource post) throws SQLException {
		return new PostController(this.uriInfo).editPost(post, postId, userId);
	}

	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response getPosts(@QueryParam("limit-to") @DefaultValue("2") int limitTo,
			@PathParam("userId") String userId, @QueryParam("page") @DefaultValue("1") int page,
			 @QueryParam("date") @DefaultValue("") String date) throws SQLException {
		return new PostController(this.uriInfo).getPosts(userId, limitTo, page, date);
	}

	@Path("/friends")
	@GET
	public Response getFriendsPosts(@QueryParam("content") String content,
			@PathParam("userId") String userId,
			@QueryParam("limit-to") @DefaultValue("2")  int limitTo,
			@QueryParam("page") @DefaultValue("1") int page) throws SQLException {
		return new PostController(this.uriInfo).getFriendsPosts(userId, content, limitTo, page);
	}

}
