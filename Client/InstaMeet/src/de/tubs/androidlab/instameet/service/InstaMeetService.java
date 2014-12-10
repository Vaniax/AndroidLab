package de.tubs.androidlab.instameet.service;

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
		Log.i(TAG, "Service started");
		Thread.currentThread().setName(InstaMeetService.class.getSimpleName());
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "Service stopped. This should NOT happen!");
	}
	
	public synchronized void doWork() {
		Log.i(TAG, "do work!!!!");
	}

}
