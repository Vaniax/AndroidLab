package de.tubs.androidlab.instameet.ui.addfriend;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.main.MainActivity;

public class AddFriendActivity extends Activity  {
	private final static String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences pref = null;
    
    private static final String[] COUNTRIES = new String[] {
        "Belgium", "France", "Italy", "Germany", "Spain"
    };


	private Button addButton = null;
	private EditText friendName = null;
    private InstaMeetService service = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_addfriend);
		
    	pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		addButton = (Button) findViewById(R.id.addfriend_button_add);
	//	friendName = (EditText) findViewById(R.id.addfriend_username);
		
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (friendName.getText().toString().isEmpty()) {
					Toast.makeText(AddFriendActivity.this, "Please enter a username", Toast.LENGTH_LONG).show();
				} else if (pref.contains("securityToken")) {
					String token = pref.getString("securityToken", "");
					service.addFriendRequest(token, friendName.getText().toString());
					toMainActivity();
				} else {
					Toast.makeText(AddFriendActivity.this, "Token not available", Toast.LENGTH_LONG).show();
				}
			}
		});
		
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.addfriend_username);
        textView.setAdapter(adapter);

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
    
    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
    
}
