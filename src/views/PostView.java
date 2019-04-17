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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import controllers.PostController;
import resources.PostResource;

@Path("/user/{user_id}/post")
public class PostView {

	@Context
	private UriInfo uriInfo;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postMessage(@PathParam("user_id") String userId, PostResource post) throws SQLException {
		return new PostController(this.uriInfo).postMessage(post, userId);
	}

	@Path("/{postId}")
	@DELETE
	public Response deleteMessage(@PathParam("user_id") String userId, @PathParam("post_id") String postId)
			throws SQLException {
		return new PostController(this.uriInfo).deleteMessage(userId, postId);
	}

	@Path("/{postId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editMessage(@PathParam("user_id") String userId, @PathParam("postId") String postId,
			PostResource post) throws SQLException {
		return new PostController(this.uriInfo).editMessage(post, postId, userId);
	}

	@GET
	public Response getMessage(@QueryParam("limitTo") @DefaultValue("") String limitTo,
			@PathParam("user_id") String userId) throws SQLException {
		return new PostController(this.uriInfo).getMessage(userId, limitTo);
	}

	@Path("/friends")
	@GET
	public Response getMessageFriends(@QueryParam("content") @DefaultValue("") String content,
			@PathParam("user_id") String userId) throws SQLException {
		return new PostController(this.uriInfo).getMessageFriends(userId, content);
	}

}
