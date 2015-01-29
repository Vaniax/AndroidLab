package de.tubs.androidlab.instameet.ui.addfriend;

import java.util.ArrayList;
import java.util.List;

import simpleEntities.SimpleUser;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.client.listener.AbstractInboundMessageListener;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.ContactsListAdapter;
import de.tubs.androidlab.instameet.ui.main.MainActivity;
import de.tubs.androidlab.instameet.ui.viewuser.ViewUserActivity;

/**
 * This activity allows the user to add a new friend to his/her contacts
 */
public class AddFriendActivity extends Activity implements OnItemClickListener  {

	private final static String TAG = MainActivity.class.getSimpleName();
	
	private SharedPreferences pref = null;
    private ContactsListAdapter adapter;
    
	private ImageButton searchButton = null;
	private EditText friendName = null;
    private InstaMeetService service = null;
    private ListView searchResults = null;
    private MessageListener listener = new MessageListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_addfriend);
		
    	pref = PreferenceManager.getDefaultSharedPreferences(this);
    	adapter = new ContactsListAdapter((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE), this);
		
		searchButton = (ImageButton) findViewById(R.id.button_search);
		friendName = (EditText) findViewById(R.id.addfriend_username);
		searchResults = (ListView) findViewById(R.id.list_search_results);
		
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (friendName.getText().toString().isEmpty()) {
					Toast.makeText(AddFriendActivity.this, "Please enter a username", Toast.LENGTH_LONG).show();
				} else if (pref.contains("securityToken")) {
					searchButton.setEnabled(false);
					service.fetchUsersByName(friendName.getText().toString());
					//TODO: list all search results...
					//service.addFriendRequest(token, friendName.getText().toString());
//						List<SimpleUser> l = new ArrayList<SimpleUser>();
//						SimpleUser s = new SimpleUser();
//						s.setUsername("Peter");
//						l.add(s); l.add(s); 
//						adapter.setContacts(l);
				} else {
					Toast.makeText(AddFriendActivity.this, "Token not available", Toast.LENGTH_LONG).show();
				}
			}
		});
		searchResults.setAdapter(adapter);
		searchResults.setOnItemClickListener(this);
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
        	service.processor.listener.removeListener(listener);

    		unbindService(serviceConnection);
    		service = null;
    	}		
    }
    
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
    		service.processor.listener.addListener(listener);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.e(TAG, "Connection lost");
        	service.processor.listener.removeListener(listener);
            service = null;
        }
    };

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SimpleUser user = adapter.getItem(position);
		Intent intent = new Intent(this, ViewUserActivity.class);
		intent.putExtra(ViewUserActivity.EXTRA_USER_ID, user.getId());
		startActivity(intent);
	}
	
	private class MessageListener extends AbstractInboundMessageListener {

		@Override
		public void listUsers(List<SimpleUser> users) {
			super.listUsers(users);
			if (adapter != null) {
				adapter.setContacts(users);
				adapter.notifyChanges();
			}
		}
	}
}
