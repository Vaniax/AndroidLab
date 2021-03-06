package de.tubs.androidlab.instameet.ui.appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView.BufferType;
import android.widget.TimePicker;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.ContactsListAdapter;

/**
 * This activity displays a form to create a new appointment
 * or to edit an existing one.
 * If you want to edit an existing appointment, you have to pass
 * it's id as integer extra in your intent: </br>
 * <code>
 * intent.putExtra(EXTRA_APPOINTMENT_ID, id);
 * </code></br>
 * If you want to create a new activity, omnit any extras.
 * @author Bjoern
 *
 */
public class EditAppointmentActivity extends Activity implements TextWatcher {
	private final static String TAG = EditAppointmentActivity.class.getSimpleName();

	public static final String EXTRA_APPOINTMENT_ID = "de.tubs.androidlab.instameet.APPOINTMENT_ID";
	
	private static final int REQUEST_SELECT_LOCATION = 1;
	private InstaMeetService service = null;
	
	private ContactsListAdapter adapter;
	private boolean isNewAppointment;
	Bundle extras;
	private SimpleAppointment appointment;
	
	private boolean isModified = false;
	private EditText editTitle;
	private EditText editDescription;
	private Button buttonDate;
	private Button buttonTime;
	private Button buttonSelectLocation;
	
	private AlertDialog dialogSaveAppointment;
	private AlertDialog dialogAddParticipant;
	
	private String savedTitle = new String();
	private String savedDescription = new String();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// construct UI
		final ListView participantsList = new ListView(this);
		setContentView(participantsList);
		final ImageButton buttonAddParticipant = new ImageButton(this);
		buttonAddParticipant.setImageResource(R.drawable.ic_action_new);
		participantsList.addFooterView(buttonAddParticipant, null, false);
		final View header = layoutInflater.inflate(R.layout.activity_edit_appointment, null);
		participantsList.addHeaderView(header, null, false);
		setContentView(participantsList);
		
		// search widgets
		editTitle = (EditText) findViewById(R.id.edit_title);
		editDescription = (EditText) findViewById(R.id.edit_description);
		buttonDate = (Button) findViewById(R.id.button_date);
		buttonTime = (Button) findViewById(R.id.button_time);
		buttonSelectLocation = (Button) findViewById(R.id.button_select_location);
		
		// fill form widgets with information
		extras = getIntent().getExtras();
		if(extras != null && extras.containsKey(EXTRA_APPOINTMENT_ID)) {
			isNewAppointment = false;

		} else {
			isNewAppointment = true;
			setTitle("New Appointment");
			appointment = new SimpleAppointment();
			appointment.setStartingTime(System.currentTimeMillis());
		}
		if (appointment != null) {
			refreshDateButton();
			refreshTimeButton();
		}

		adapter = new ContactsListAdapter(layoutInflater,this);
		participantsList.setAdapter(adapter);
		//TODO: fetch these from the appointment
//				List<SimpleUser> l = new ArrayList<SimpleUser>();
//				SimpleUser s = new SimpleUser();
//				s.setUsername("Peter");
//				l.add(s); l.add(s); 
//				adapter.setContacts(l);

		//register listeners
		editTitle.addTextChangedListener(this);
		editDescription.addTextChangedListener(this);
		buttonAddParticipant.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogAddParticipant.show();
			}
		});
		participantsList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				SimpleUser user = adapter.getItem(position-1);
				adapter.removeContact(user);
				appointment.getVisitingUsers().remove(user.getId());
				return true;
			}
		});
	
		
//		createDialog();
	}

	@Override
	protected void onStart() {
    	super.onStart();
    	Intent intent = new Intent(this, InstaMeetService.class);
    	if(service == null && !bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
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
	protected void onPause() {
		super.onPause();
		savedTitle = editTitle.getText().toString();
		savedDescription = editDescription.getText().toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_appointment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_accept:
			saveInputs();
			return true;
		case R.id.action_cacnel:
			discardInputs();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		if(isModified) {
			dialogSaveAppointment.show();
		} else {
			discardInputs();
		}
	}
	
	private void saveInputs() {
		//TODO: check, if all fields are vaild!
		//TODO: save appointment
		if(isNewAppointment) {
			appointment.setTitle(editTitle.getText().toString());
			appointment.setDescription(editDescription.getText().toString());
			service.createAppointment(appointment);
		}
		finish();
	}
	
	private void discardInputs() {
		finish();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		isModified = true;
	}

	@Override
	public void afterTextChanged(Editable s) {
		;
	}
	
	private void refreshDateButton() {
		java.text.DateFormat formatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
		buttonDate.setText(
				formatter.format(appointment.getStartingTime())
					);
	}
	
	private void refreshTimeButton() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		buttonTime.setText(
				formatter.format(appointment.getStartingTime())
				);
	}
	
	private void createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.dialog_save_appointment);
		builder.setIcon(android.R.drawable.alert_dark_frame);
		builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               saveInputs();
	           }
	       });
		builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               discardInputs();
	           }
	       });
		dialogSaveAppointment = builder.create();
		
		final ContactsListAdapter adapter = new ContactsListAdapter((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
			
			//TODO: fetch these from the service
//			List<SimpleUser> l = new ArrayList<SimpleUser>();
//			SimpleUser s = new SimpleUser();
//			s.setUsername("Peter");
//			l.add(s); l.add(s);
			List<SimpleUser> l = service.getFriends();
			adapter.setContacts(l);
		builder = new AlertDialog.Builder(this);
		builder.setAdapter(adapter,  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int position) {
				SimpleUser user = adapter.getItem(position);
				EditAppointmentActivity.this.adapter.addContact(user);
				appointment.getVisitingUsers().add(user.getId());
				dialogAddParticipant.dismiss();
			}
		});
		dialogAddParticipant = builder.create();
	}
	
	public void showTimePickerClicked(View view) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void showDatePickerClicked(View view) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void selectLocationClicked(View view) {
		Intent intent = new Intent(this, SelectLocationActivity.class);
		startActivityForResult(intent, REQUEST_SELECT_LOCATION);
	}
	
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			switch(requestCode) {
			case REQUEST_SELECT_LOCATION:
				double longitude = data.getExtras().getDouble(SelectLocationActivity.EXTRA_LONGITUDE);
				double latitude  = data.getExtras().getDouble(SelectLocationActivity.EXTRA_LATITUDE);
				String text = data.getExtras().getString(SelectLocationActivity.EXTRA_SELECT_LOCATION_RESULT);

				buttonSelectLocation.setText(text);
				appointment.setLattitude(latitude);
				appointment.setLongitude(longitude);
				break;
			}
		}
  } 
	
	/**
	 * A dialog allowing the user to pick a specific time (hour and minute)
	 * After picking a specific time, it is stored in the appointment
	 * and the time button is refreshed accordingly.
	 * @author Bjoern
	 */
	private class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Calendar cal = Calendar.getInstance();

			cal.setTimeInMillis(appointment.getStartingTime());
			
			int hour = cal.get(cal.HOUR_OF_DAY);
			int minute = cal.get(cal.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute, true);
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
			cal.set(Calendar.MINUTE, minute);
			appointment.setStartingTime(cal.getTimeInMillis());

			isModified = true;
			refreshTimeButton();
		}
	}
	
	/**
	 * A dialog allowing the user to pick a specific date (year, month and day)
	 * After picking a specific date, it is stored in the appointment
	 * and the date button is refreshed accordingly.
	 * @author Bjoern
	 */
	private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(appointment.getStartingTime());
	    	
	        int year = cal.get(cal.YEAR);
	        int month = cal.get(cal.MONTH);
	        int day = cal.get(cal.DAY_OF_MONTH);
	        // Create a new instance of DatePickerDialog and return it
	        return new DatePickerDialog(getActivity(), this, year, month, day);
	    }

	    public void onDateSet(DatePicker view, int year, int month, int day) {
			Calendar cal = Calendar.getInstance();
			
			cal.set(cal.YEAR, year);
			cal.set(cal.MONTH, month);
			cal.set(cal.DAY_OF_MONTH, day);
	        appointment.setStartingTime(cal.getTimeInMillis());
	        isModified = true;
	        refreshDateButton();
	    }
	}
	
	/** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
			if(!isNewAppointment && service.getAppointment(extras.getInt(EXTRA_APPOINTMENT_ID)) != null) {
				appointment = service.getAppointment(extras.getInt(EXTRA_APPOINTMENT_ID));
				adapter.setContacts(service.getFriends());
			} else {
//	        	appointment = new SimpleAppointment();
				appointment.setHoster(service.getOwnData().getId());
			}
			editTitle.setText(appointment.getTitle());
			editDescription.setText(appointment.getDescription());
			createDialog();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.e(TAG, "Connection lost");
            service = null;
        }
    };
}
