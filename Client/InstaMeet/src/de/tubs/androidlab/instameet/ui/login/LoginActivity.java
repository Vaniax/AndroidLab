package de.tubs.androidlab.instameet.ui.login;

import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LoginActivity extends Activity {

    private final static String TAG = LoginActivity.class.getSimpleName();

    private InstaMeetService service = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		LoginFragment loginFragment = new LoginFragment();
		
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		transaction.add(R.id.activity_login_fragment_container, loginFragment);
		transaction.commit();
	}

	@Override
	protected void onStart() {
		super.onStart();
    	Intent intent = new Intent(this, InstaMeetService.class);
    	if(!bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
    		Log.e(TAG, "Service not available");
    	}   	
	}

	@Override
	protected void onStop() {
		super.onStop();
    	if(service != null) {
    		unbindService(serviceConnection);
    		service = null;
    	}
	}
	
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.e(TAG, "Connection lost");
            service = null;
        }
    };

    public InstaMeetService getService() {
    	return this.service;
    }
}
