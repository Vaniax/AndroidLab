package de.tubs.androidlab.instameet.ui.chat;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.client.listener.AbstractInboundMessageListener;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.main.MainActivity;

public class ChatActivity extends Activity {
	
	public final static String EXTRA_NAME = "de.tubs.anroidlab.instameet.NAME";
	private final static String TAG = MainActivity.class.getSimpleName();
	private ChatListAdapter adapter;
    private InstaMeetService service;

	private Button sendBtn = null;
	private EditText editText = null;
	private ListView chatList = null;
	
	private SimpleUser user = null;
	int friendID = 0;
	
	MessageListener listener = new MessageListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_chat);
		
		sendBtn = (Button) findViewById(R.id.send_message_chat);
		editText = (EditText) findViewById(R.id.message_field_chat);
		chatList = (ListView) findViewById(R.id.chat_list);
		
		adapter = new ChatListAdapter(ChatActivity.this);
		chatList.setAdapter(adapter);
		
		sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            	service.sendDummyMessage(editText.getText().toString());
            	service.sendMessage(editText.getText().toString(), user.getId());
                editText.setText(null);
            }
        });
		Intent intent = getIntent();
		friendID = intent.getIntExtra(EXTRA_NAME,100);
	}

	@Override
	protected void onStart() {
		super.onStart();
    	Intent intent = new Intent(this, InstaMeetService.class);
    	if(!bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
    		Log.e(TAG, "Service not available");
    	}   
	}
	
	private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
        	user = service.getUser(friendID);
    		getActionBar().setTitle(user.getUsername());
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
	protected void onStop() {
		super.onStop();
    	if(service != null) {
    		unbindService(serviceConnection);
    		service = null;
    	}		
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
	
	
	private class MessageListener extends AbstractInboundMessageListener {
		@Override
		public void chatMessage(String message) {
			super.chatMessage(message);
			if(adapter != null) {
				adapter.addMessage(message);

				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			}
			
		}
	}
}
