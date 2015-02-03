package de.tubs.androidlab.instameet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.android.gms.drive.internal.ap;

import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;
import de.tubs.androidlab.instameet.client.InstaMeetClient;
import de.tubs.androidlab.instameet.client.listener.InboundListener;
import de.tubs.androidlab.instameet.server.protobuf.Messages;
import de.tubs.androidlab.instameet.server.protobuf.Messages.AddFriendReply;
import de.tubs.androidlab.instameet.server.protobuf.Messages.AddFriendRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.BoolReply;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ChatMessage;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse;
import de.tubs.androidlab.instameet.server.protobuf.Messages.CreateAppointment;
import de.tubs.androidlab.instameet.server.protobuf.Messages.CreateUser;
import de.tubs.androidlab.instameet.server.protobuf.Messages.GetFriends;
import de.tubs.androidlab.instameet.server.protobuf.Messages.GetMyVisitingAppointments;
import de.tubs.androidlab.instameet.server.protobuf.Messages.GetNearAppointments;
import de.tubs.androidlab.instameet.server.protobuf.Messages.GetOwnData;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ListChatMessages;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ListFriends;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ListNearestAppointments;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ListUsers;
import de.tubs.androidlab.instameet.server.protobuf.Messages.Location;
import de.tubs.androidlab.instameet.server.protobuf.Messages.Login;
import de.tubs.androidlab.instameet.server.protobuf.Messages.OwnData;
import de.tubs.androidlab.instameet.server.protobuf.Messages.SecurityToken;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest.Type;
import de.tubs.androidlab.instameet.ui.chat.ChatMessageProxy;
import de.tubs.androidlab.instameet.ui.chat.ChatMessageProxy.DIRECTION;
import de.tubs.androidlab.instameet.ui.main.MainActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Service which handles all communication with the server.
 * It runs in background and receives data from server / sends data to the server.
 * @author Bjoern
 */
public class InstaMeetService extends Service implements OutgoingMessages {
	
	private final static String TAG = "InstaMeetService";
	private final InstaMeetServiceBinder binder = new InstaMeetServiceBinder(this);
	private Thread clientThread = null;
	private InstaMeetClient client = null;
	final public IncomingMessageProcessor processor = new IncomingMessageProcessor(this);
	
	//Blackboard Data
	//Contains all data over the logged in user
	private SimpleUser ownData = new SimpleUser(); 
	//Contains all fetched users
	private Map<Integer, SimpleUser> users = new HashMap<Integer,SimpleUser>();		
	//Contains all fetched appointments
	private Map<Integer, SimpleAppointment> appointments = new HashMap<Integer, SimpleAppointment>();	
	private List<Integer> nearAppList = new ArrayList<Integer>();
	// Contains all incoming chat messages
	private Map<Integer, List<ChatMessageProxy>> chatMessagesNew = 
			new HashMap<Integer, List<ChatMessageProxy>>(); 
	// Contains the history messages by users
	private Map<Integer, List<ChatMessageProxy>> chatMessageHistory = 
			new HashMap<Integer, 
			List<ChatMessageProxy>>();
	// Contains all friends of the user
	List<SimpleUser> friends = new ArrayList<SimpleUser>();
	// Contains all friend requests
	List<SimpleUser> friendRequests = new ArrayList<SimpleUser>();
	// SecurityToken
//	String securityToken = new String();
	
	// Location update
	LocationUpdate locationUpdate = null;
	
	
	@Override
	public void onCreate() {
		Log.i(TAG, "Service onCreate");
		client = new InstaMeetClient(processor);
		clientThread = new Thread(client);
		clientThread.start();
		locationUpdate = new LocationUpdate(this);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "Service started by 'bindService()'");
		return binder;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Service started by 'startService()'");
//		return super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "Service onDestroy. This should NOT happen!");
		Toast.makeText(getApplicationContext(), "Service destroyed :(", Toast.LENGTH_LONG).show();
		clientThread.interrupt();
	}
	
	public SimpleUser getOwnData() {
		return ownData;
	}
	
	public List<SimpleUser> getFriends() {
		List<SimpleUser> friends = new ArrayList<SimpleUser>();
		if(ownData == null) {
			fetchOwnData();
			return friends;
		}
		if(ownData.getFriends() == null) {
			fetchFriends();
			return friends;
		}
		for(int friendId : ownData.getFriends()) {
			if(users.containsKey(friendId)) {
				friends.add(users.get(friendId));
			} else {
				//TODO: request frienddata here
			}
			
		}
		return friends;
	}
	
	public void addFriend(SimpleUser user) {
		friends.add(user);
	}
	
	public List<SimpleAppointment> getNearAppointments() {
		List<SimpleAppointment> apps = new ArrayList<SimpleAppointment>();
		for(int appId : nearAppList) {
			if(appointments.containsKey(appId)) {
				apps.add(appointments.get(appId));
			} else {
				//TODO: request appointmentdata here				
			}
		}
		return apps;
	}
	
	public List<SimpleAppointment> getVisitingAppointments() {
		if(ownData == null)
			return null;
		
		List<SimpleAppointment> visitingAppointments = new ArrayList<SimpleAppointment>(ownData.getVisitingAppointments().size());
		for(int i : ownData.getVisitingAppointments()) {
			if(!appointments.containsKey(i)) {
				//TODO: add new getApplication Request and maybe placeholder in application
			} else {
				visitingAppointments.add(appointments.get(i));
			}
		}
		return visitingAppointments;
	}	

	public List<SimpleAppointment> getMyHostedAppointments() {
		if(ownData == null)
			return null;
		
		List<SimpleAppointment> hostedAppointments = new ArrayList<SimpleAppointment>(ownData.getHostedAppointments().size());
		for(int i : ownData.getHostedAppointments()) {
			if(!appointments.containsKey(i)) {
				//TODO: add new getApplication Request and maybe placeholder in application
			} else {
				hostedAppointments.add(appointments.get(i));
			}
		}
		return hostedAppointments;
	}		
	
	public List<SimpleUser> getNewFriendRequests() {
		return friendRequests;
	}
	
	public boolean areNewFriendRequests() {
		return !friendRequests.isEmpty();
	}
	
	public void removeFriendRequest(SimpleUser user) {
		friendRequests.remove(user);
	}

	// Only for some stupid dummy testing
	// Synchronization not necessary here
	public void sendDummyMessage(String msg) {
		Log.i(TAG,"call sendDummyMessage");
		
		ChatMessage t = ChatMessage.newBuilder().setMessage("testSend").setFriendID(2).setSecurityToken("test").setUserID(ownData.getId()).build();
		client.insertToQueue(ServerRequest.newBuilder().setType(Type.SEND_CHAT_MESSAGE).setMessage(t).build());

		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(ServerRequest.Type.SEND_CHAT_MESSAGE)
				.setMessage(t)
				.build();
		client.insertToQueue(request);
	}
	
	@Override
	public void login(String name, String password) {
		Login login = Login.newBuilder().setName(name).setPassword(password).build();
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.LOGIN)
				.setLogin(login)
				.build();
		this.client.insertToQueue(request);
		Log.d(TAG,"Insert send request to queue with content:\n" + login.toString());
	}

	@Override
	public void createUser(String name, String password) {
		CreateUser create = CreateUser.newBuilder().setName(name).setPassword(password).build();
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.CREATE_USER)
				.setCreateUser(create)
				.build();
		this.client.insertToQueue(request);
		Log.d(TAG,"Insert send request to queue with content:\n" + create.toString());
	}

	@Override
	public void sendMessage(String message, int friendID) {
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");
		
		ChatMessage chatMessage = ChatMessage
				.newBuilder()
				.setSecurityToken(token)
				.setFriendID(friendID)
				.setMessage(message)
				.setUserID(ownData.getId())
				.setSecurityToken(token).build();
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.SEND_CHAT_MESSAGE)
				.setMessage(chatMessage)
				.build();
		this.client.insertToQueue(request);
		Log.d(TAG,"Insert send request to queue with content:\n" + chatMessage.toString());
	}
	
	@Override
	public void addFriendRequest(int friendID) {
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");

		AddFriendRequest addFriend = AddFriendRequest.newBuilder()
				.setSecurityToken(token)
				.setFriendID(friendID)
				.setUserID(ownData.getId())
				.build();
		
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.ADD_FRIEND_REQUEST)
				.setAddFriendRequest(addFriend)
				.build();
		this.client.insertToQueue(request);
		Log.d(TAG,"Insert send request to queue with content:\n" + addFriend.toString());
	}
	
	@Override
	public void addFriendReply(boolean accepted, int friendID) {
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");
		AddFriendReply reply = AddFriendReply.newBuilder()
				.setSecurityToken(token)
				.setFriendID(friendID)
				.setUserID(ownData.getId())
				.setAccepted(accepted)
				.build();
				
		ServerRequest response = ServerRequest
				.newBuilder()
				.setType(Type.ADD_FRIEND_REPLY)
				.setAddFriendReply(reply)
				.build();
		this.client.insertToQueue(response);
		Log.d(TAG,"Insert send request to queue with content:\n" + reply.toString());
	}
	
	@Override	
	public void fetchFriends() {
		if(ownData == null || ownData.getFriends() == null) {
			fetchOwnData();
			return;
		}

		GetFriends.Builder friendRequest = GetFriends.newBuilder();
		for(int i : ownData.getFriends()) {
			if(!users.containsKey(i)) {
				friendRequest.addFriendIDs(i);
			}
		}
		
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");
		friendRequest.setUserID(ownData.getId());
		friendRequest.setSecurityToken(token);
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.GET_FRIENDS)
				.setGetFriendList(friendRequest.build())
				.build();
		Log.d(TAG,token);
		client.insertToQueue(request);
	}
	
	@Override
	public void fetchOwnData() {
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");
		GetOwnData t = GetOwnData.newBuilder().setSecurityToken(token).build();
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.GET_OWN_DATA)
				.setGetOwnData(t)
				.build();
		client.insertToQueue(request);
	}
	
	@Override
	public void fetchNearAppointments() {
		//TODO: replace dummy location
		double lat = 5.3;
		double lon = 3.3;
		Location loc = Location.newBuilder().setLattitude(lat).setLongitude(lon).build();
		
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");
		GetNearAppointments t = GetNearAppointments.newBuilder()
				.setSecurityToken(token)
				.setUserID(ownData.getId())
				.setLocation(loc)
				.build();
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.GET_NEAR_APPOINTMENTS)
				.setGetNearAppointments(t)
				.build();
		client.insertToQueue(request);		
	}

	@Override
	public void fetchVisitingAppointments() {
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");
		GetMyVisitingAppointments t = GetMyVisitingAppointments.newBuilder()
				.setSecurityToken(token)
				.setUserID(ownData.getId())
				.build();
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.GET_MY_VISITING_APPOINTMENTS)
				.setGetMyVisitingAppointments(t)
				.build();
		client.insertToQueue(request);		
	}	
	
	@Override
	public void fetchUsersByName(String subString) {
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");
		Messages.GetUsersByName userByName = Messages.GetUsersByName.newBuilder()
				.setUserID(ownData.getId())
				.setSecurityToken(token)
				.setSubName(subString)
				.build();
		Messages.ServerRequest request = Messages.ServerRequest.newBuilder()
				.setType(Type.GET_USERS_BY_NAME)
				.setGetUsersByName(userByName)
				.build();
		client.insertToQueue(request);
	}
	
	@Override
	public void createAppointment(SimpleAppointment app) {
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");
		CreateAppointment createApp = CreateAppointment.newBuilder()
				.setAppointment(createMessageAppFromApp(app))
				.setSecurityToken(token)
				.setUserID(ownData.getId())
				.build();
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.CREATE_APPOINTMENT)
				.setCreateAppointment(createApp)
				.build();
		this.client.insertToQueue(request);
	}
	
	@Override
	public void updateLocation(android.location.Location location)
	{
		if(ownData != null && location != null) {
			String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");

			Messages.Location msgLoc = Messages.Location.newBuilder()
					.setLattitude(location.getLatitude())
					.setLongitude(location.getLongitude())
					.build();
			Messages.UpdateLocation update = Messages.UpdateLocation.newBuilder()
					.setUserID(ownData.getId())
					.setSecurityToken(token)
					.setLocation(msgLoc) 
					.build();
			Messages.ServerRequest request = Messages.ServerRequest.newBuilder()
					.setType(Type.UPDATE_LOCATION)
					.setUpdateLocation(update)
					.build();
			client.insertToQueue(request);
		}

	}
	
	
	private Messages.SimpleAppointment createMessageAppFromApp(SimpleAppointment app) {
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
	
	public Map<Integer, SimpleUser> getUsers() {
		return users;
	}
	
	public SimpleUser getUser(int userID) {
		if(users.containsKey(userID))
			return users.get(userID);
		return null;
	}
	
	public SimpleAppointment getAppointment(int appId) {
		if(appointments.containsKey(appId))
			return appointments.get(appId);
		return null;
	}
	
	public List<ChatMessageProxy> getNewMessagesAndRemove(int friendID) {
		return chatMessagesNew.remove(friendID);
	}	
	public Map<Integer, List<ChatMessageProxy>> getNewMessages() {
		return chatMessagesNew;
	}
	
	public List<ChatMessageProxy> getHistoryMessages(int friendID) {
		if (!chatMessageHistory.containsKey(friendID)) {
			List<ChatMessageProxy> newList = new ArrayList<ChatMessageProxy>();
			chatMessageHistory.put(friendID, newList);
		}
		return chatMessageHistory.get(friendID);
	}
	
	public class IncomingMessageProcessor implements ReceivedMessageCallbacks {

		private final static String TAG = "InstaMeetService MessageProcessor";
		public final InboundListener listener = new InboundListener();
		private InstaMeetService service = null;
		
		
		public IncomingMessageProcessor(InstaMeetService instaMeetService) {
			service = instaMeetService;
		}

		@Override
		public void chatMessage(Messages.ChatMessage msg) {
			Log.d(TAG,"Incoming Message:\n" + msg.toString());
			
			ChatMessageProxy message = 
					 new ChatMessageProxy(msg.getMessage().toString(),DIRECTION.INCOMING);
			
			// To history
			if (!chatMessageHistory.containsKey(msg.getUserID())) {
				List<ChatMessageProxy> list =
							new ArrayList<ChatMessageProxy>();
				list.add(message);
				synchronized (chatMessageHistory) {
					chatMessageHistory.put(msg.getUserID(),list);
				}
			} else {
				synchronized (chatMessageHistory) {
					chatMessageHistory.get(msg.getUserID()).add(message);
				}
			}

			// To new message
			List<ChatMessageProxy> list =
							new ArrayList<ChatMessageProxy>();
			list.add(message);
			synchronized (chatMessagesNew) {
				chatMessagesNew.put(msg.getUserID(),list);
			}

			listener.notifyChatMessage();	
			
			Intent appIntent = new Intent(getApplicationContext(), MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 
					0,
					appIntent,
					0);
		    NotificationManager mNotificationManager = 
				     (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
   
		    Notification.Builder notificationBuilder = new Notification.Builder(
		    		getApplicationContext())
		    .setSmallIcon(android.R.drawable.stat_notify_chat)
		    .setAutoCancel(true)
		    .setContentTitle("InstaMeet")
		    .setContentText("Neue Nachricht von (" + users.get(msg.getUserID()).getUsername() + ")")
		    .setContentIntent(contentIntent);

		    mNotificationManager.notify(1, notificationBuilder.build()); 
		}

		@Override
		public void listFriends(ListFriends msg) {
			Log.d(TAG,"Incoming Message:\n" + msg.toString());

			for (Messages.SimpleUser it : msg.getFriendsList()) {
				SimpleUser user = new SimpleUser();
				user.setId(it.getUserID());
//				user.setLatestLocationUpdate(Timestamp.valueOf(it.getLatestLocationUpdate().getTime()));
				user.setLattitude(it.getLocation().getLattitude());
				user.setLongitude(it.getLocation().getLongitude());
				user.setUsername(it.getUserName());
				
				Set<Integer> hostedAppointments = new HashSet<Integer>(it.getHostedAppointmentIDsList());
				user.setHostedAppointments(hostedAppointments);
				
				Set<Integer> visitingAppointments = new HashSet<Integer>(it.getVisitingAppointmentIDsList());
				user.setVisitingAppointments(visitingAppointments);
				
				Set<Integer> friends = new HashSet<Integer>(it.getFriendIDsList());
				user.setFriends(friends);
				
				synchronized (users) {
					users.put(it.getUserID(), user);
				}
			}
			listener.notifyListFriends();
		}

		@Override
		public void listChatMessages(ListChatMessages msg) {
			Log.d(TAG,"Incoming Message:\n" + msg.toString());

			for (ChatMessage it : msg.getMessagesList()) {
				int id = it.getFriendID();
				List<String> message = new ArrayList<String>();
				message.add(it.getMessage());
				
				synchronized (chatMessageHistory) {
					if(chatMessageHistory.containsKey(id)) {
//						chatMessageHistory.get(id).addAll(message);
					} else {
//						chatMessageHistory.put(id, message);
					}
				}
			}
		}

		@Override
		public void securityToken(SecurityToken token) {
			Log.d(TAG,"Incoming security token");
			listener.notifyToken(token.getToken());
		}

		@Override
		public void bool(BoolReply bool) {
			Log.d(TAG,"Incoming bool");
			listener.notifyBool(bool.getIsTrue());
		}

		@Override
		public void ownData(OwnData ownData) {
			Log.d(TAG,"Incoming ownData");
			Messages.SimpleUser data = ownData.getUserData();
			SimpleUser user = new SimpleUser();
			user.setId(data.getUserID());
			user.setUsername(data.getUserName());
			user.setLattitude(data.getLocation().getLattitude());
			user.setLongitude(data.getLocation().getLongitude());
			
			Set<Integer> hostedAppointments = new HashSet<Integer>(data.getHostedAppointmentIDsList());
			user.setHostedAppointments(hostedAppointments);
			
			Set<Integer> visitingAppointments = new HashSet<Integer>(data.getVisitingAppointmentIDsList());
			user.setVisitingAppointments(visitingAppointments);
			
//			Timestamp timestamp = Timestamp.valueOf(data.getLatestLocationUpdate().getTime());
//			user.setLatestLocationUpdate(timestamp);
			
			Set<Integer> friends = new HashSet<Integer>(data.getFriendIDsList());
			user.setFriends(friends);
			
			service.ownData = user;
			service.users.put(user.getId(), user);
			listener.notifyOwnData();
			
			
	
			//Trigger next requests
			fetchVisitingAppointments();
			fetchNearAppointments();
			fetchFriends();
		}

		@Override
		public void simpleAppointment(Messages.SimpleAppointment msg) {
			SimpleAppointment app = createAppFromAppMessage(msg);
			if (app.getHoster().intValue() == (ownData.getId())) {
				ownData.getHostedAppointments().add(app.getId());
			}
			appointments.put(msg.getId(), app);
			listener.notifyAppointment();
		}
		
		@Override
		public void listNearAppointments(ListNearestAppointments msg) {
			// TODO Auto-generated method stub
			List<Messages.SimpleAppointment> msgApps = msg.getAppointmentsList();
			for(Messages.SimpleAppointment a : msgApps) {
				SimpleAppointment app = createAppFromAppMessage(a);
				appointments.put(app.getId(), app);
				nearAppList.add(app.getId());
			}		
			listener.notifyListNearApps();
		}

		@Override
		public void listVisitingAppointments(ClientResponse msg) {
			List<Messages.SimpleAppointment> msgApps = msg.getListVisitingAppointment().getAppointmentsList();
			for(Messages.SimpleAppointment a : msgApps) {
				SimpleAppointment app = createAppFromAppMessage(a);
				appointments.put(app.getId(), app);
			}			
			if(msg.hasHostedAppointments()) {
				msgApps = msg.getHostedAppointments().getAppointmentsList();
				for(Messages.SimpleAppointment a : msgApps) {
					SimpleAppointment app = createAppFromAppMessage(a);
					appointments.put(app.getId(), app);
				}	
			}
			listener.notifyListVisitingApps();
		}
		
		@Override
		public void listUsers(ListUsers listUsers) {
			List<SimpleUser> users = createListUser(listUsers.getUsersList());
			
			listener.notifyListUsers(users);
		}
		
		@Override
		public void addFriendRequest(Messages.SimpleUser user) {
			friendRequests.add(createUserFromUserMessage(user));
			listener.notifyFriendRequest();
		}

		@Override
		public void addFriendReply(boolean bool,Messages.SimpleUser user) {
			if(bool == true) {
				SimpleUser friend = createUserFromUserMessage(user);
				ownData.getFriends().add(friend.getId());
				users.put(friend.getId(), friend);
			}		
			listener.notifyFriendReply(bool);
		}
		
		/** Converter functions **/
		private SimpleAppointment createAppFromAppMessage(Messages.SimpleAppointment msgApp) {
			SimpleAppointment app = new SimpleAppointment();
			app.setId(msgApp.getId());
			app.setTitle(msgApp.getTitle());
			app.setHoster(msgApp.getHoster());
			app.setLattitude(msgApp.getLocation().getLattitude());
			app.setLongitude(msgApp.getLocation().getLongitude());
			app.setStartingTime(msgApp.getTime());
			app.setDescription(msgApp.getDescription());
			if(msgApp.getParticipantsList() != null) {
				for(int msgUser : msgApp.getParticipantsList()) {
					app.getVisitingUsers().add(msgUser);
				}
			}
			return app;
		}
		
		private SimpleUser createUserFromUserMessage(Messages.SimpleUser user) 
		{
			SimpleUser simpleUser = new SimpleUser();
			
			simpleUser.setId(user.getUserID());
			simpleUser.setUsername(user.getUserName());
			simpleUser.setLattitude(user.getLocation().getLattitude());
			simpleUser.setLongitude(user.getLocation().getLongitude());
			
			Set<Integer> hostedAppointments = new HashSet<Integer>(user.getHostedAppointmentIDsList());
			simpleUser.setHostedAppointments(hostedAppointments);
			
			Set<Integer> visitingAppointments = new HashSet<Integer>(user.getVisitingAppointmentIDsList());
			simpleUser.setVisitingAppointments(visitingAppointments);
			
//			Timestamp timestamp = Timestamp.valueOf(data.getLatestLocationUpdate().getTime());
//			user.setLatestLocationUpdate(timestamp);
			
			Set<Integer> friends = new HashSet<Integer>(user.getFriendIDsList());
			simpleUser.setFriends(friends);
			return simpleUser;
		}
		
		private List<SimpleUser> createListUser(List<Messages.SimpleUser> users) {
			List<SimpleUser> result = new ArrayList<SimpleUser>();
			for (Messages.SimpleUser u : users) {
				result.add(createUserFromUserMessage(u));
			}
			return result;
		}
	}

}
