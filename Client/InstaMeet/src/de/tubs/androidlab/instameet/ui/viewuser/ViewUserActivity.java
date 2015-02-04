package de.tubs.androidlab.instameet.ui.viewuser;

import simpleEntities.SimpleUser;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;

/**
 * This activity displays information about a specific user
 * You must specify the user's ID when starting this activity
 * in its intent's extras as follows:</br>
 * <code>
 * intent.putExtra(ViewUserActivity.EXTRA_USER_ID, id);
 * </code>
 * @author Bjoern
 */
public class ViewUserActivity extends Activity {
	
	public static final String EXTRA_USER_ID = "de.tubs.androidlab.instameet.USER_ID";
	private final static String TAG = ViewUserActivity.class.getSimpleName();

	private int friendID;
    private InstaMeetService service;
    private TextView textUsername;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_user);
		Intent intent = getIntent();
		friendID = intent.getIntExtra(EXTRA_USER_ID, 0);
		textUsername = (TextView) findViewById(R.id.user_name);
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
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
        	SimpleUser friend = service.getUser(friendID);
        	textUsername.setText(friend.getUsername());
        	getActionBar().setTitle(friend.getUsername());
        }

		@Override
		public void onServiceDisconnected(ComponentName name) {
        	Log.e(TAG, "Connection lost");
            service = null;			
		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_addFriend) {
			service.addFriendRequest(friendID);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
