package instameet.server.netty;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import de.tubs.androidlab.instameet.server.protobuf.Messages.GetUsersByName;
import de.tubs.androidlab.instameet.server.protobuf.Messages.Login;
import de.tubs.androidlab.instameet.server.protobuf.Messages.SecurityToken;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ChatMessage;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse.Type;
import service.ServiceInterface;
import simpleEntities.Location;
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
			channels.put(userID, ctx.channel());
			System.out.println("Add new channel for user id: " + userID);
			SecurityToken token = SecurityToken.newBuilder().setToken(data.getToken()).build();
			ctx.writeAndFlush(ClientResponse.newBuilder().setType(Type.SECURITY_TOKEN).setToken(token).build());
			break;
		
		// TODO: Change simple echo reply to correct one
		case CREATE_USER: {
			BoolReply bool = BoolReply.newBuilder().setIsTrue(true).build();
			CreateUser user = msg.getCreateUser();
			SimpleUser createdUser  = service.createUser(user.getName(), user.getPassword());
			ctx.writeAndFlush(ClientResponse.newBuilder().setType(Type.BOOL).setBoolReply(bool).build());
		} break;			
		case SEND_CHAT_MESSAGE: {
		   ChatMessage message = msg.getMessage();
		   
		   int userIDChannel = message.getUserID();
		   checkChannel(userIDChannel,ctx.channel());
		   
		   int id = message.getFriendID();
		   Channel ch = null;
		   if (channels.containsKey(id)) {
			   ch = channels.get(id);
			   ch.writeAndFlush(ClientResponse.newBuilder().setType(Type.CHAT_MESSAGE).setMessage(message).build());
		   }
		} break;
		case GET_OWN_DATA: {
			SimpleUser own = service.getOwnData(msg.getGetOwnData().getSecurityToken(), 1);
			Messages.SimpleUser ownUser = createMessageSimpleUser(own);
			
		   int userIDChannel = ownUser.getUserID();
		   checkChannel(userIDChannel,ctx.channel());
			   
			System.out.println(ownUser.toString());
			
			ClientResponse response = ClientResponse.newBuilder()
					.setType(Type.OWN_DATA)
					.setUserData(Messages.OwnData.newBuilder().setUserData(ownUser)).build();
			
			ctx.writeAndFlush(response);
		}break;
		case GET_FRIENDS: {
			List<SimpleUser> friends = service.GetFriends(msg.getGetFriendList().getSecurityToken(), msg.getGetFriendList().getUserID());
			
		   int userIDChannel = msg.getGetFriendList().getUserID();
		   checkChannel(userIDChannel,ctx.channel());
			   
			Messages.ListFriends.Builder getFriends =  Messages.ListFriends.newBuilder();
			
			for(SimpleUser friend : friends) {
				Messages.SimpleUser protoFriend = createMessageSimpleUser(friend);				
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
			
		   int userIDChannel = app.getHoster();
		   checkChannel(userIDChannel,ctx.channel());
			
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
			
			   int userIDChannel = msg.getVisitAppointment().getUserID();
			   checkChannel(userIDChannel,ctx.channel());
			
			BoolReply msgSucessful = BoolReply.newBuilder().setIsTrue(true).build();
			ClientResponse response = ClientResponse.newBuilder()
					.setType(Type.BOOL)
					.setBoolReply(msgSucessful)
					.build();
			ctx.writeAndFlush(response);			
		} break;
		case GET_NEAR_APPOINTMENTS: {
			msg.getGetNearAppointments().getSecurityToken();
			Location loc = new Location();
			loc.setLattitude(msg.getGetNearAppointments().getLocation().getLattitude());
			loc.setLongitude(msg.getGetNearAppointments().getLocation().getLongitude());
			
		   int userIDChannel = msg.getGetNearAppointments().getUserID();
		   checkChannel(userIDChannel,ctx.channel());
			   
			List<SimpleAppointment> nearApps = service.GetNearAppointments(
					msg.getGetNearAppointments().getSecurityToken(), 
					msg.getGetNearAppointments().getUserID(), 
					loc);
			
			Messages.ListNearestAppointments.Builder msgNearAppsBuilder = Messages.ListNearestAppointments.newBuilder();
			
			for(SimpleAppointment app : nearApps) {
				Messages.SimpleAppointment  msgApp= createAppMessageFromApp(app);
				msgNearAppsBuilder.addAppointments(msgApp);
			}
			
			ClientResponse response = ClientResponse.newBuilder()
					.setType(Type.LIST_NEAREST_APPOINTMENTS)
					.setListNearestAppointments(msgNearAppsBuilder.build())
					.build();
			ctx.writeAndFlush(response);			

			
		} break;
		case GET_MY_VISITING_APPOINTMENTS: {
			msg.getGetMyVisitingAppointments().getSecurityToken();
			
		   int userIDChannel = msg.getGetMyVisitingAppointments().getUserID();
		   checkChannel(userIDChannel,ctx.channel());
			//Visiting Appointments
			List<SimpleAppointment> myApps = service.GetMyVisitingAppointments(
					msg.getGetMyVisitingAppointments().getSecurityToken(), 
					msg.getGetMyVisitingAppointments().getUserID());	
			
			Messages.ListVisitingAppointments.Builder msgMyAppsBuilder = Messages.ListVisitingAppointments.newBuilder();
			
			for(SimpleAppointment app : myApps) {
				Messages.SimpleAppointment  msgApp= createAppMessageFromApp(app);
				msgMyAppsBuilder.addAppointments(msgApp);
			}
			
			System.out.println(msgMyAppsBuilder.build());
			
			//Additional hosted appointments for lesser messages
			List<SimpleAppointment> hApps = service.GetMyHostedAppointments(
					msg.getGetMyVisitingAppointments().getSecurityToken(), 
					msg.getGetMyVisitingAppointments().getUserID());	
			
			Messages.ListHostedAppointments.Builder msgHoAppsBuilder = Messages.ListHostedAppointments.newBuilder();
			
			for(SimpleAppointment app : hApps) {
				Messages.SimpleAppointment  msgApp= createAppMessageFromApp(app);
				msgHoAppsBuilder.addAppointments(msgApp);
			}
			
			System.out.println(msgHoAppsBuilder.build());			
			
			//Send message
			ClientResponse response = ClientResponse.newBuilder()
					.setType(Type.LIST_VISITING_APPOINTMENTS)
					.setListVisitingAppointment(msgMyAppsBuilder.build())
					.setHostedAppointments(msgHoAppsBuilder.build())
					.build();
			ctx.writeAndFlush(response);		
		
		} break;
		case GET_HOSTED_APPOINTMENTS: {
			msg.getGetHostedAppointments().getSecurityToken();
			
		   int userIDChannel = msg.getGetHostedAppointments().getUserID();
		   checkChannel(userIDChannel,ctx.channel());
			
			List<SimpleAppointment> hApps = service.GetMyHostedAppointments(
					msg.getGetMyVisitingAppointments().getSecurityToken(), 
					msg.getGetMyVisitingAppointments().getUserID());	
			
			Messages.ListHostedAppointments.Builder msgHoAppsBuilder = Messages.ListHostedAppointments.newBuilder();
			
			for(SimpleAppointment app : hApps) {
				Messages.SimpleAppointment  msgApp= createAppMessageFromApp(app);
				msgHoAppsBuilder.addAppointments(msgApp);
			}
			
			System.out.println(msgHoAppsBuilder.build());
			
			ClientResponse response = ClientResponse.newBuilder()
					.setType(Type.LIST_HOSTED_APPOINTMENTS)
					.setHostedAppointments(msgHoAppsBuilder.build())
					.build();
			ctx.writeAndFlush(response);		
		
		} break;		
		
		case ADD_FRIEND_REQUEST: {
			
			boolean successful = service.addFriendRequest(
					msg.getAddFriendRequest().getSecurityToken(),
					msg.getAddFriendRequest().getUserID(),
					msg.getAddFriendRequest().getFriendID());
			
		   int userIDChannel = msg.getAddFriendRequest().getUserID();
		   checkChannel(userIDChannel,ctx.channel());
			
		   if(successful) {
				//TODO: notify requested user to react
			   ClientResponse response = ClientResponse.newBuilder()
					   .setType(Type.ADD_FRIEND_REQUEST)
					   .setUser(createMessageSimpleUser(service.getUser(msg.getAddFriendRequest().getUserID())))
					   .build();
			   Channel channel = channels.get(msg.getAddFriendRequest().getFriendID());
			   if (channel != null) {
				   channel.writeAndFlush(response);
			   }
		   }
		} break;
		case ADD_FRIEND_REPLY: {
			
			boolean successful = service.addFriendReply(
					msg.getAddFriendReply().getSecurityToken(),
					msg.getAddFriendReply().getUserID(),
					msg.getAddFriendReply().getFriendID(),
					msg.getAddFriendReply().getAccepted());
		   int userIDChannel = msg.getAddFriendReply().getUserID();
		   checkChannel(userIDChannel,ctx.channel());
			//TODO: notify requesting user if friendship was accepted or not	
		   ClientResponse response = ClientResponse.newBuilder()
				   .setType(Type.ADD_FRIEND_REPLY)
				   .setBoolean(msg.getAddFriendReply().getAccepted())
				   .setUser(createMessageSimpleUser(service.getUser(msg.getAddFriendReply().getUserID())))
				   .build();
		   Channel channel = channels.get(msg.getAddFriendReply().getFriendID());
		   if (channel != null) {
			   channel.writeAndFlush(response);
		   }
		   
		} break;
		case GET_USERS_BY_NAME: {
			Messages.GetUsersByName message = msg.getGetUsersByName();
			List<SimpleUser> users = service.getUsersByName(message.getSecurityToken(), message.getUserID(), message.getSubName());

			Messages.ListUsers userList = Messages.ListUsers.newBuilder()
					.addAllUsers(createSimpleUserList(users))
					.build();
			
			Messages.ClientResponse response = Messages.ClientResponse.newBuilder()
					.setType(Type.LIST_USERS)
					.setListUsers(userList)
					.build();
					
			
		    int userIDChannel = message.getUserID();
		    checkChannel(userIDChannel,ctx.channel());
		    
		    ctx.writeAndFlush(response);
		} break;
		case UPDATE_LOCATION: {
			Location loc = new Location(
					msg.getUpdateLocation().getLocation().getLongitude(),
					msg.getUpdateLocation().getLocation().getLattitude());
			boolean sucessful = service.UpdateLocation(
					msg.getUpdateLocation().getSecurityToken(), 
					msg.getUpdateLocation().getUserID(), 
					loc);
		} break;
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
		app.setStartingTime(msgApp.getTime());
		app.setDescription(msgApp.getDescription());
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
		msgApp.setTime(app.getStartingTime());
		msgApp.setDescription(app.getDescription());
		for(int userId : app.getVisitingUsers()) {
			msgApp.addParticipants(userId);
			
		}
		return msgApp.build();
	}
	
	private Messages.SimpleUser createMessageSimpleUser(SimpleUser user) {
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
	
	private List<Messages.SimpleUser> createSimpleUserList(List<SimpleUser> users)
	{
		List<Messages.SimpleUser> result = new ArrayList<Messages.SimpleUser>();
		for(SimpleUser user : users) {
			result.add(createMessageSimpleUser(user));
		}
		return result;
	}
	
	private void checkChannel(int userID, Channel channel) {
		channels.put(userID, channel);
	}
}
