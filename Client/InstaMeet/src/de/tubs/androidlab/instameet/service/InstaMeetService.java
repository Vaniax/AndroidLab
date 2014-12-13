package de.tubs.androidlab.instameet.service;

import java.util.Map;

import simpleEntities.SimpleUser;
import de.tubs.androidlab.instameet.client.InstaMeetClient;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ChatMessage;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest.Type;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Service which handles all communication with the server.
 * It runs in background and receives data from server / sends data to the server.
 * @author Bjoern
 */
public class InstaMeetService extends Service {
	
	private final static String TAG = "Service";
	private final InstaMeetServiceBinder binder = new InstaMeetServiceBinder(this);
	private Thread clientThread = null;
	private InstaMeetClient client = null;
	
	//Blackboard Data
	private SimpleUser ownData; //Conatins all data over the logged in user
	private Map<Integer, SimpleUser> users;		//Contains all fetched users
	private Map<Integer, SimpleUser> appointments;	//Contains all fetched appointments
	
	
	@Override
	public void onCreate() {
		Log.i(TAG, "Service onCreate");
		client = new InstaMeetClient();
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
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "Service onDestroy. This should NOT happen!");
		Toast.makeText(getApplicationContext(), "Service destroyed :(", Toast.LENGTH_LONG).show();
		clientThread.interrupt();
	}
	
	// TODO: Only for some stupid dummy testing
	public synchronized void sendDummyMessage(String msg) {
		Log.i(TAG,"call");
		ChatMessage t = ChatMessage.newBuilder().setMessage(msg).setFriendID(2).setSecurityToken("test").build();
		client.queue.add(ServerRequest.newBuilder().setType(Type.SEND_CHAT_MESSAGE).setMessage(t).build());
		Log.i(TAG,"queue");
	}

	public SimpleUser getOwnData() {
		return ownData;
	}

	public Map<Integer, SimpleUser> getUsers() {
		return users;
	}

	public Map<Integer, SimpleUser> getAppointments() {
		return appointments;
	}

}
