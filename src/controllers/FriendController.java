package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import database.FriendDB;
import database.UserDB;
import resources.UserResource;

public class FriendController extends Controller {

	public FriendController() {
		super();
	}

	public Response addFriend(String friend1Id, String friend2Id) throws SQLException {
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();

		// Check that friend1Id and friend2Id exist
		UserResource friend1 = new UserResource(friend1Id);
		Response friend1InformationResponse = this.getUserInformationReponse(res, friend1);
		if (friend1InformationResponse.getStatus() != 200) {
			return friend1InformationResponse;
		}

		UserResource friend2 = new UserResource(friend2Id);
		Response friend2InformationResponse = this.getUserInformationReponse(res, friend2);
		if (friend2InformationResponse.getStatus() != 200) {
			return friend2InformationResponse;
		}

		if (friend1.getId() == friend2.getId()) {
			return this.getBadRequestResponse(res, "You can only be friend of other user");
		}
		FriendDB db = new FriendDB();
		int affected_rows = db.addFriend(friend1.getId(), friend2.getId());
		if (affected_rows > 0) {
			String location = "http://localhost:8080/SOS/api/user/" + friend2.getId();
			friend2.setLocation(location);
			return this.getCreatedResponse(res, friend2, location, "Friend relation added succesfully");
		}
		// Error
		return this.getInternalServerErrorResponse(res, "Unable to create friend relation");

	}

	public Response removeFriend(String friend1Id, String friend2Id) throws SQLException {
		// Res stores body response
		HashMap<String, Object> res = new HashMap<String, Object>();

		UserResource friend1 = new UserResource(friend1Id);
		UserResource friend2 = new UserResource(friend2Id);

		if (friend1.getId() == friend2.getId()) {
			return this.getBadRequestResponse(res, "You can only be friend of other user");
		}

		FriendDB db = new FriendDB();
		if (db.removeFriend(friend1, friend2) > 0) {
			// TODO getOkResponse doesn't allow location. I think is useless. Should ask
			// teacher.
			// String location = "http://localhost:8080/SOS/api/user/" + idFriend;
			// TODO Other comment: second argument should data, what about returning an
			// array with 2 elements with location as well
			return this.getOkResponse(res, null, "Friend relation removed succesfully");
		}

		// Error message should be an object
		return this.getInternalServerErrorResponse(res, "Unable to remove friend");
	}
}
