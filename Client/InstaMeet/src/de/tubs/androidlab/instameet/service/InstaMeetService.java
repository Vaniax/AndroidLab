package de.tubs.androidlab.instameet.service;

import io.netty.handler.codec.protobuf.ProtobufDecoder;

import java.sql.Timestamp;
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
import de.tubs.androidlab.instameet.server.protobuf.Messages.AddFriend;
import de.tubs.androidlab.instameet.server.protobuf.Messages.BoolReply;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ChatMessage;
import de.tubs.androidlab.instameet.server.protobuf.Messages.CreateUser;
import de.tubs.androidlab.instameet.server.protobuf.Messages.GetFriends;
import de.tubs.androidlab.instameet.server.protobuf.Messages.GetOwnData;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ListChatMessages;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ListFriends;
import de.tubs.androidlab.instameet.server.protobuf.Messages.Login;
import de.tubs.androidlab.instameet.server.protobuf.Messages.OwnData;
import de.tubs.androidlab.instameet.server.protobuf.Messages.SecurityToken;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest.Type;
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
	// Contains all incoming chat messages
	private Map<Integer, List<String>> chatMessages = new HashMap<Integer, List<String>>(); 
	// Contains the history messages by users
	private Map<Integer, List<String>> chatMessageHistory = new HashMap<Integer, List<String>>();
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
		return super.onStartCommand(intent, flags, startId);
//		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "Service onDestroy. This should NOT happen!");
		Toast.makeText(getApplicationContext(), "Service destroyed :(", Toast.LENGTH_LONG).show();
		clientThread.interrupt();
	}
	
	public List<SimpleUser> getFriends() {
		if (friends == null) {
			fetchFriends();
		}
		return friends;
	}
	
	public void fetchFriends() {
//		if(ownData == null)
//			return null;
//		
//		List<SimpleUser> friends = new ArrayList<SimpleUser>(ownData.getFriends().size());
//		
//		for(int i : ownData.getFriends()) {
//			if(!users.containsKey(i)) {
//				
//			}
//			friends.add(users.get(i));
//		}
//		return friends;
		GetFriends.Builder friendRequest = GetFriends.newBuilder();
		for(int i : ownData.getFriends()) {
			if(!users.containsKey(i)) {
				friendRequest.addFriendIDs(i);
			}
		}
		String token = PreferenceManager.getDefaultSharedPreferences(this).getString("securityToken", "");
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

	public List<SimpleAppointment> getVisitingAppointments() {
		if(ownData == null)
			return null;
		
		List<SimpleAppointment> visitingAppointments = new ArrayList<SimpleAppointment>(ownData.getVisitingAppointments().size());
		for(int i : ownData.getVisitingAppointments()) {
			if(!appointments.containsKey(i)) {
				//Add new getApplication Request and maybe placeholder in application
			}
			visitingAppointments.add(appointments.get(i));
		}
		return visitingAppointments;
	}	
	
	// Only for some stupid dummy testing
	// Synchronization not necessary here
	public void sendDummyMessage(String msg) {
		Log.i(TAG,"call sendDummyMessage");
		
		ChatMessage t = ChatMessage.newBuilder().setMessage("testSend").setFriendID(2).setSecurityToken("test").build();
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
	public void sendMessage(String securityToken, int userId, String message) {
		ChatMessage chatMessage = ChatMessage
				.newBuilder()
				.setFriendID(userId)
				.setMessage(message)
				.setSecurityToken(securityToken).build();
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
		AddFriend addFriend = AddFriend.newBuilder()
				.setSecurityToken(securityToken)
				.setFriendName(friendName)
				.build();
		
		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(Type.ADD_FRIEND)
				.setAddFriend(addFriend)
				.build();
		this.client.insertToQueue(request);
		Log.d(TAG,"Insert send request to queue with content:\n" + addFriend.toString());
	}
	
	public class IncomingMessageProcessor implements ReceivedMessageCallbacks {

		private final static String TAG = "InstaMeetService MessageProcessor";
		public final InboundListener listener = new InboundListener();
		private InstaMeetService service = null;
		
		
		public IncomingMessageProcessor(InstaMeetService instaMeetService) {
			service = instaMeetService;
		}

		@Override
		public void chatMessage(ChatMessage msg) {
			Log.d(TAG,"Incoming Message:\n" + msg.toString());
			List<String> message = new ArrayList<String>();
			message.add(msg.getMessage());
			if (chatMessages.containsKey(msg.getFriendID())) {
				chatMessages.get(msg.getFriendID()).addAll(message);
			}
			
			synchronized (chatMessages) {
				chatMessages.put(msg.getFriendID(), message);
			}
			
		}

		@Override
		public void listFriends(ListFriends msg) {
			Log.d(TAG,"Incoming Message:\n" + msg.toString());

			for (Messages.SimpleUser it : msg.getFriendsList()) {
				SimpleUser user = new SimpleUser();
				user.setId(it.getUserID());
				user.setLatestLocationUpdate(Timestamp.valueOf(it.getLatestLocationUpdate().getTime()));
				user.setLattitude(it.getLocation().getLattitude());
				user.setLongitude(it.getLocation().getLongitude());
				user.setUsername(it.getUserName());
				user.getFriends().addAll(it.getFriendIDsList());
				user.getHostedAppointments().addAll(it.getHostedAppointmentIDsList());
				user.getVisitingAppointments().addAll(it.getHostedAppointmentIDsList());
				
				synchronized (users) {
					users.put(it.getUserID(), user);
				}
			}
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
						chatMessageHistory.get(id).addAll(message);
					} else {
						chatMessageHistory.put(id, message);
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
			
			Timestamp timestamp = Timestamp.valueOf(data.getLatestLocationUpdate().getTime());
			user.setLatestLocationUpdate(timestamp);
			
			Set<Integer> friends = new HashSet<Integer>(data.getFriendIDsList());
			user.setFriends(friends);
			
			listener.notifyOwnData();
		}
		
	}

}
