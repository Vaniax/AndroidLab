package instameet.server.netty;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import de.tubs.androidlab.instameet.server.protobuf.Messages;
import de.tubs.androidlab.instameet.server.protobuf.Messages.BoolReply;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse;
import de.tubs.androidlab.instameet.server.protobuf.Messages.CreateUser;
import de.tubs.androidlab.instameet.server.protobuf.Messages.Login;
import de.tubs.androidlab.instameet.server.protobuf.Messages.SecurityToken;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ChatMessage;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse.Type;
import service.ServiceInterface;
import simpleEntities.LoginData;
import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;

public class InstaMeetServerHandler extends SimpleChannelInboundHandler<ServerRequest> {

	static public ConcurrentMap<Integer, Channel> channels = new ConcurrentHashMap<>();
		
	static private ServiceInterface service = null;
	
	InstaMeetServerHandler(ServiceInterface service) {
		InstaMeetServerHandler.service = service;
	}
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ServerRequest msg)
			throws Exception {
		System.out.println("Message:\n" + msg.toString());
		switch (msg.getType()) {
		
		case LOGIN:
			// TODO: Call Service
			Login login = msg.getLogin();
			LoginData data = service.login(login.getName(), login.getPassword());
			if(data == null)
				return;
			int userID = data.getUserId();
			if (!channels.containsKey(userID)) { // TODO: also test for successful login
				channels.put(userID, ctx.channel());
				System.out.println("Add new channel for user id: " + userID);
			}
			SecurityToken token = SecurityToken.newBuilder().setToken(data.getToken()).build();
			ctx.writeAndFlush(ClientResponse.newBuilder().setType(Type.SECURITY_TOKEN).setToken(token).build());
			break;
		
		// TODO: Change simple echo reply to correct one
		case SEND_CHAT_MESSAGE:
			ChatMessage message = msg.getMessage();
			
			ctx.writeAndFlush(ClientResponse.newBuilder().setType(Type.CHAT_MESSAGE).setMessage(message).build());
		break;
		case CREATE_USER: {
			BoolReply bool = BoolReply.newBuilder().setIsTrue(true).build();
			CreateUser user = msg.getCreateUser();
			SimpleUser createdUser  = service.createUser(user.getName(), user.getPassword());
			ctx.writeAndFlush(ClientResponse.newBuilder().setType(Type.BOOL).setBoolReply(bool).build());
		} break;
		case GET_OWN_DATA: {
			SimpleUser own = service.getOwnData(msg.getGetOwnData().getSecurityToken(), 1);
			Messages.SimpleUser ownUser = createSimpleUserBuilder(own);
			
			System.out.println(ownUser.toString());
			
			ClientResponse response = ClientResponse.newBuilder()
					.setType(Type.OWN_DATA)
					.setUserData(Messages.OwnData.newBuilder().setUserData(ownUser)).build();
			
			ctx.writeAndFlush(response);
		}break;
		case GET_FRIENDS: {
			List<SimpleUser> friends = service.GetFriends(msg.getGetFriendList().getSecurityToken(), msg.getGetFriendList().getUserID());
			Messages.ListFriends.Builder getFriends =  Messages.ListFriends.newBuilder();
			
			for(SimpleUser friend : friends) {
				Messages.SimpleUser protoFriend = createSimpleUserBuilder(friend);				
				getFriends.addFriends(protoFriend);
			}
			ClientResponse response = ClientResponse.newBuilder()
					.setType(Type.LIST_FRIENDS)
					.setListFriends(getFriends.build()).build();
			
			
			System.out.println(getFriends.toString());

			ctx.writeAndFlush(response);

		}break;
		case CREATE_APPOINTMENT: {
			SimpleAppointment app = createAppFromAppMessage(msg.getCreateAppointment().getAppointment());
			app = service.createAppointment(msg.getCreateAppointment().getSecurityToken(), msg.getCreateAppointment().getUserID(), app);
			Messages.SimpleAppointment msgAppointment = createAppMessageFromApp(app);

			ClientResponse response = ClientResponse.newBuilder()
					.setType(Type.SIMPLE_APPOINTMENT)
					.setAppointment(msgAppointment)
					.build();
			ctx.writeAndFlush(response);
		}break;
		case VISIT_APPOINTMENT: {
			boolean sucessful = service.visitAppointment(
					msg.getVisitAppointment().getSecurityToken(), 
					msg.getVisitAppointment().getUserID(), 
					msg.getVisitAppointment().getAppointmentID());
			BoolReply msgSucessful = BoolReply.newBuilder().setIsTrue(true).build();
			ClientResponse response = ClientResponse.newBuilder()
					.setType(Type.BOOL)
					.setBoolReply(msgSucessful)
					.build();
			ctx.writeAndFlush(response);			
		}
		default:
			break;
		}
	}
	private SimpleAppointment createAppFromAppMessage(Messages.SimpleAppointment msgApp) {
		SimpleAppointment app = new SimpleAppointment();
		app.setId(msgApp.getId());
		app.setTitle(msgApp.getTitle());
		app.setHoster(msgApp.getHoster());
		app.setLattitude(msgApp.getLocation().getLattitude());
		app.setLongitude(msgApp.getLocation().getLongitude());
		//app.setStartingTime(msgApp.getTime());
		app.setDescription(null);
		for(int msgUser : msgApp.getParticipantsList()) {
			app.getVisitingUsers().add(msgUser);
		}
		return app;
	}
	
	private Messages.SimpleAppointment createAppMessageFromApp(SimpleAppointment app) {
		Messages.SimpleAppointment.Builder msgApp = Messages.SimpleAppointment.newBuilder();
		msgApp.setId(app.getId());
		msgApp.setTitle(app.getTitle());
		msgApp.setHoster(app.getHoster());
		Messages.Location loc = Messages.Location.newBuilder()
				.setLattitude(app.getLattitude())
				.setLongitude(app.getLongitude()).build();
		msgApp.setLocation(loc);
		//app.setStartingTime(msgApp.getTime());
		msgApp.setDescription(null);
		for(int userId : app.getVisitingUsers()) {
			msgApp.addParticipants(userId);
			
		}
		return msgApp.build();
	}
	
	private Messages.SimpleUser createSimpleUserBuilder(SimpleUser user) {
		Messages.SimpleUser.Builder userBuild = Messages.SimpleUser.newBuilder();
		userBuild.setUserID(user.getId());
		userBuild.setUserName(user.getUsername());
		userBuild.addAllFriendIDs(user.getFriends());
		userBuild.addAllVisitingAppointmentIDs(user.getVisitingAppointments());
		userBuild.addAllHostedAppointmentIDs(user.getHostedAppointments());
		Messages.Location location = Messages.Location.newBuilder()
				.setLattitude(user.getLattitude())
				.setLongitude(user.getLongitude())
				.build();
		userBuild.setLocation(location);		
		
		return userBuild.build();
		
	}
	
}
