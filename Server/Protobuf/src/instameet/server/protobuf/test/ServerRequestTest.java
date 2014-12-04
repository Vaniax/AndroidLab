package instameet.server.protobuf.test;

import instameet.server.protobuf.ServerRequests.Location;
import instameet.server.protobuf.ServerRequests.Login;
import instameet.server.protobuf.ServerRequests.ServerRequest;
import instameet.server.protobuf.ServerRequests.Time;


public class ServerRequestTest {

	public static void main(String[] args) {

		// Create an instance of a message. Messages are generated as classes.
		java.util.Date date = new java.util.Date();
		java.sql.Time sqlTime = new java.sql.Time(date.getTime());
		Time time = Time.newBuilder().setTime(sqlTime.toString()).build();
		System.out.println("Message:\n" + time.toString());
		
		Login login = Login.newBuilder().setName("Matthias").setPassword("123").build();
		System.out.println("Message:\n" + login.toString());
		
		Location location = Location.newBuilder().setLattitude(90.0f).setLongitude(90.0f).build();
		System.out.println("Message:\n" + location.toString());
		
		// Build a server request
		// This is the only message the server can process (multiple messages aren't possible!)
		
		// E.g. a login request:
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(ServerRequest.Type.LOGIN)
				.setLogin(login)
				.build();
		System.out.println("Message:\n" + request.toString());
		
		// Receiving a request through tcp channel
		// Checking for type is necessary to forward request to service implementation
		ServerRequest.Type type = request.getType();
		switch (type) {
		case CREATE_USER:
			System.out.println("Fail - Message cannot be of type CreateUser");
			break;
		case LOGIN:
			System.out.println("Request message is of type Login - correct -> Further processing");
		default:
			break;
		}
	}

}
