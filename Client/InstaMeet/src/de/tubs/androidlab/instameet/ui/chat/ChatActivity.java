package de.tubs.androidlab.instameet.ui.chat;

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
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.main.MainActivity;

public class ChatActivity extends Activity {
	
	public static final String EXTRA_NAME = "de.tubs.anroidlab.instameet.NAME";
	private final static String TAG = MainActivity.class.getSimpleName();
    private InstaMeetService service;

	private Button sendBtn = null;
	private EditText editText = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_chat);
		sendBtn = (Button) findViewById(R.id.send_message_chat);
		editText = (EditText) findViewById(R.id.message_field_chat);
		
		sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	service.sendDummyMessage(editText.getText().toString());
                editText.setText(null);
            }
        });
		
		Intent intent = getIntent();
		getActionBar().setTitle(intent.getStringExtra(EXTRA_NAME));
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	    
		Intent intent = new Intent(this, InstaMeetService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
        	service.doWork();
   
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.e(TAG, "Connection lost");
            service = null;
        }
    };
	
	@Override
	protected void onStop() {
		super.onStop();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
