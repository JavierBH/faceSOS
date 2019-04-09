package controllers;

import java.net.URI;
import java.util.HashMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class Controller {
	/*****************************/
	// TODO: Check codes. Right now, we have:
	// PUT and POST return CREATED => 201
	// GET and DELETE return OK => 200
	/*****************************/
	
	private UriInfo uriInfo;
	
	public Controller(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	public URI getPath() {
		return this.uriInfo.getAbsolutePath();
	}
	
	/*
	 * Add error message to response
	 * */
	public Response getInternalServerErrorResponse(HashMap<String, Object> res, String message) {
		res.put("message", message);
		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(res)
				.build();
	}
	
	public Response getBadRequestResponse(HashMap<String, Object> res, String message) {
		res.put("message", message);
		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(res)
				.build();
	}
	
	public Response getOkResponse(HashMap<String, Object> res, Object data, String message) {
		res.put("message", message);
		res.put("data", data);
		return Response
				.status(Response.Status.OK)
				.entity(res)
				.build();
	}
	
	public Response getCreatedResponse(HashMap<String, Object> res, Object data, String location, String message) {
		res.put("message", message);
		res.put("data", data);
		return Response
				.status(Response.Status.OK)
				.entity(res)
				.header("Location", location)
				.header("Content-Location", location)
				.build();
	}
}
