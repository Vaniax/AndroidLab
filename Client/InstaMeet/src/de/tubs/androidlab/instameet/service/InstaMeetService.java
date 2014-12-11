package de.tubs.androidlab.instameet.service;

import de.tubs.androidlab.instameet.client.InstaMeetClient;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ChatMessage;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest.Type;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Service which handles all communication with the server.
 * It runs in background and receives data from server / sends data to the server.
 * @author Bjoern
 */
public class InstaMeetService extends Service {
	
	private static final String TAG = "Service";
	private final InstaMeetServiceBinder binder = new InstaMeetServiceBinder(this);
	private InstaMeetService service = null;
	boolean serviceStarted = false;
	Thread clientThread = null;
	InstaMeetClient client = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "Service onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Service onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "Service onCreate");
		Thread.currentThread().setName(InstaMeetService.class.getSimpleName());
		client = new InstaMeetClient();
		clientThread = new Thread(client);
		clientThread.start();
		serviceStarted = true;
		service = this;
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "Service onDestroy. This should NOT happen!");
	}
	
	public synchronized void doWork() {
		Log.i(TAG, "do work!!!!");
	}
	
	// TODO: Only for some stupid dummy testing
	public void sendDummyMessage(String msg) {
		Log.i("ConnectandSend","call");
		ChatMessage t = ChatMessage.newBuilder().setMessage(msg).setFriendID(2).setSecurityToken("test").build();
		client.queue.add(ServerRequest.newBuilder().setType(Type.SEND_CHAT_MESSAGE).setMessage(t).build());
		Log.i("ConnectandSend","queue");
	}

}
