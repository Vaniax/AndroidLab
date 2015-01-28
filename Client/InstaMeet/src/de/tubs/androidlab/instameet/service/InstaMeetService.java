package de.tubs.androidlab.instameet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;
import de.tubs.androidlab.instameet.client.InstaMeetClient;
import de.tubs.androidlab.instameet.client.listener.InboundListener;
import de.tubs.androidlab.instameet.server.protobuf.Messages;
import de.tubs.androidlab.instameet.server.protobuf.Messages.AddFriendRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.BoolReply;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ChatMessage;
import de.tubs.androidlab.instameet.server.protobuf.Messages.CreateAppointment;
import de.tubs.androidlab.instameet.server.protobuf.Messages.CreateUser;
import de.tubs.androidlab.instameet.server.protobuf.Messages.GetFriends;
import de.tubs.androidlab.instameet.server.protobuf.Messages.GetOwnData;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ListChatMessages;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ListFriends;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ListNearestAppointments;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ListVisitingAppointments;
import de.tubs.androidlab.instameet.server.protobuf.Messages.Login;
import de.tubs.androidlab.instameet.server.protobuf.Messages.OwnData;
import de.tubs.androidlab.instameet.server.protobuf.Messages.SecurityToken;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest.Type;
import de.tubs.androidlab.instameet.ui.chat.ChatMessageProxy;
import de.tubs.androidlab.instameet.ui.chat.ChatMessageProxy.DIRECTION;
import android.app.Service;
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
	// SecurityToken
//	String securityToken = new String();
	
	@Override
	public void onCreate() {
		Log.i(TAG, "Service onCreate");
		client = new InstaMeetClient(processor);
		clientThread = new Thread(client);
		clientThread.start();
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
	public void addFriendRequest(String securityToken, String friendName) {
		AddFriendRequest addFriend = AddFriendRequest.newBuilder()
				.setSecurityToken(securityToken)
				.setFriendName(friendName)
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
	
	public void createAppointment(String securityToken, SimpleAppointment app) {
		CreateAppointment createApp = CreateAppointment.newBuilder()
				.setAppointment(createMessageAppFromApp(app))
				.build();
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.CREATE_APPOINTMENT)
				.setCreateAppointment(createApp)
				.build();
		this.client.insertToQueue(request);

	}
	
	private de.tubs.androidlab.instameet.server.protobuf.Messages.SimpleAppointment createMessageAppFromApp(SimpleAppointment app) {
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
	
	public Map<Integer, SimpleUser> getUsers() {
		return users;
	}
	
	public SimpleUser getUser(int userID) {
		return users.get(userID);
	}
	
	public List<ChatMessageProxy> getNewMessages(int friendID) {
		return chatMessagesNew.remove(friendID);
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
			
			listener.notifyOwnData();
		}

		@Override
		public void listNearAppointments(ListNearestAppointments msg) {
			// TODO Auto-generated method stub
			List<de.tubs.androidlab.instameet.server.protobuf.Messages.SimpleAppointment> msgApps = msg.getAppointmentsList();
			for(de.tubs.androidlab.instameet.server.protobuf.Messages.SimpleAppointment a : msgApps) {
				SimpleAppointment app = createAppFromAppMessage(a);
				appointments.put(app.getId(), app);
				nearAppList.add(app.getId());
			}		
		}

		@Override
		public void listVisitingAppointments(ListVisitingAppointments msg) {
			List<de.tubs.androidlab.instameet.server.protobuf.Messages.SimpleAppointment> msgApps = msg.getAppointmentsList();
			for(de.tubs.androidlab.instameet.server.protobuf.Messages.SimpleAppointment a : msgApps) {
				SimpleAppointment app = createAppFromAppMessage(a);
				appointments.put(app.getId(), app);
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
	}

}
