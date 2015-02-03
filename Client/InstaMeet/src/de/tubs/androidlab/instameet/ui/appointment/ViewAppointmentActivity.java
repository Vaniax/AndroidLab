package de.tubs.androidlab.instameet.ui.appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.ContactsListAdapter;

/**
 * Activity which allows you to view an existing
 * appointment
 * @author Bjoern
 *
 */
public class ViewAppointmentActivity extends Activity {
	
	public final static String EXTRA_APPOINTMENT_ID = "de.tubs.anroidlab.instameet.ID";
	private final static String TAG = EditAppointmentActivity.class.getSimpleName();

	private InstaMeetService service = null;

	private ContactsListAdapter adapter;
	List<SimpleUser> adapterList;
	private int appId;
	
	private TextView description;
	private TextView hoster;
	private TextView date;
	private TextView time;

	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		appId = getIntent().getIntExtra(EXTRA_APPOINTMENT_ID, 0);
		getActionBar().setTitle(Integer.toString(appId));
		
		final ListView participantsList = new ListView(this);
		final View header = layoutInflater.inflate(R.layout.activity_view_appointment, null);
		participantsList.addHeaderView(header, null, false);
		setContentView(participantsList);
		
		description = (TextView) findViewById(R.id.description);
		hoster = (TextView) findViewById(R.id.hoster);
		date = (TextView) findViewById(R.id.date);
		time = (TextView) findViewById(R.id.time);
		
		adapter = new ContactsListAdapter(layoutInflater,this);
		adapterList = new ArrayList<SimpleUser>();
			SimpleUser s = new SimpleUser();
			s.setUsername("Peter");
			adapterList.add(s); adapterList.add(s); adapterList.add(s);
			adapter.setContacts(adapterList);
		participantsList.setAdapter(adapter);
		
		initAppointment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_appointment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_calender:
			Intent calIntent = new Intent(Intent.ACTION_INSERT); 
			calIntent.setType("vnd.android.cursor.item/event");    
			calIntent.putExtra(Events.TITLE, "My House Party"); 
			calIntent.putExtra(Events.EVENT_LOCATION, "My Beach House"); 
			calIntent.putExtra(Events.DESCRIPTION, "A Pig Roast on the Beach"); 
			GregorianCalendar calDate = new GregorianCalendar(2015, 2, 2);
			calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true); 
			calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, 
			     calDate.getTimeInMillis()); 
			calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, 
			     calDate.getTimeInMillis()); 

			startActivity(calIntent);			
			return true;
		case R.id.action_edit:
			Intent intent = new Intent(this, EditAppointmentActivity.class);
			//TODO: this must be the correct appointment id
			intent.putExtra(EditAppointmentActivity.EXTRA_APPOINTMENT_ID, 123);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
        	initAppointment();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.e(TAG, "Connection lost");
            service = null;
        }
    };
    
    private void initAppointment() {
    	SimpleAppointment app;
    	String hosterName;
    	if(service != null) {
    		app = service.getAppointment(appId);
        	if(service.getUser(app.getHoster()) != null) {
        		hosterName = service.getUser(app.getHoster()).getUsername();    		
        	} else {
        		hosterName ="ID #" +app.getHoster();
        	}
    	} else {
    		app = new SimpleAppointment();
    		app.setTitle("DefaultTitle");
    		app.setDescription("Default description");
    		app.setLattitude(1.0);
    		app.setLattitude(2.0);
    		app.setStartingTime(System.currentTimeMillis());
    		hosterName = "DefaultName";
    	}
    	getActionBar().setTitle(app.getTitle());
    	description.setText(app.getDescription());
    	hoster.setText(hosterName);

    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	String dateString = dateFormat.format(new Date(app.getStartingTime()));
    	date.setText(dateString);

    	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
    	String timeString = timeFormat.format(new Date(app.getStartingTime()));
    	time.setText(timeString);

    	
    }
	
}
