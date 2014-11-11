package de.tubs.androidlab.instameet.serivce;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This broadcast receiver gets called when the system has been restared
 * It just restarts the InstaMeetService so that the service runs at startup
 * @author Bjoern
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent = new Intent(InstaMeetService.class.getName());
		context.startService(serviceIntent);
	}
	
}
