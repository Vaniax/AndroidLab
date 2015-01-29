package de.tubs.androidlab.instameet.ui.appointment;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import de.tubs.androidlab.instameet.R;
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
	
	public static final String EXTRA_APPOINTMENT_ID = "de.tubs.androidlab.instameet.APPOINTMENT_ID";
	
	private static final int REQUEST_SELECT_LOCATION = 1;
	
	private ContactsListAdapter adapter;
	private Geocoder geocoder;
	private boolean isNewAppointment;
	private SimpleAppointment appointment;
	
	private boolean isModified = false;
	private EditText editTitle;
	private EditText editDescription;
	private Button buttonDate;
	private Button buttonTime;
	private Button buttonSelectLocation;
	
	private AlertDialog dialogSaveAppointment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// construct UI
		final ListView participantsList = new ListView(this);
		setContentView(participantsList);
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
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.containsKey(EXTRA_APPOINTMENT_ID)) {
			isNewAppointment = false;
			//TODO: fetch appointment from service, like
			//service.getAppointment(extras.getInt(EXTRA_APPOINTMENT_ID));
			appointment = new SimpleAppointment(); //TODO: remove this and get it from service via its id
			appointment.setStartingTime(new Timestamp(1422379242)); //TODO remove this
			editTitle.setText(appointment.getTitle());
			editDescription.setText(appointment.getDescription());
		} else {
			isNewAppointment = true;
			setTitle("New Appointment");
			appointment = new SimpleAppointment();
			appointment.setStartingTime(new Timestamp(System.currentTimeMillis()));
		}
		refreshDateButton();
		refreshTimeButton();
		adapter = new ContactsListAdapter(layoutInflater,this);
		participantsList.setAdapter(adapter);
		//TODO: fetch these from the appointment
			List<SimpleUser> l = new ArrayList<SimpleUser>();
			SimpleUser s = new SimpleUser();
			s.setUsername("Peter");
			l.add(s); l.add(s); 
			adapter.setContacts(l);

		editTitle.addTextChangedListener(this);
		editDescription.addTextChangedListener(this);
		geocoder = new Geocoder(this, Locale.getDefault());
		createDialog();
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
			//TODO: Send createAppointment via Service
			//service.createAppointment(ownUserId, appointment);

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
		java.text.DateFormat formatter = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
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
				List<Address> addresses;
				String text;
				try {
					addresses = geocoder.getFromLocation(latitude, longitude, 1);
					text = addresses.get(0).getAddressLine(0);
				} catch (IOException e) {
					text = "(" + latitude + ", " + longitude + ")";
				}
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
			int hour = appointment.getStartingTime().getHours();
			int minute = appointment.getStartingTime().getMinutes();

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			appointment.getStartingTime().setHours(hourOfDay);
			appointment.getStartingTime().setMinutes(minute);
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
	        int year = appointment.getStartingTime().getYear();
	        int month = appointment.getStartingTime().getMonth();
	        int day = appointment.getStartingTime().getDate();
	        // Create a new instance of DatePickerDialog and return it
	        return new DatePickerDialog(getActivity(), this, year, month, day);
	    }

	    public void onDateSet(DatePicker view, int year, int month, int day) {
	        appointment.getStartingTime().setYear(year);
	        appointment.getStartingTime().setMonth(month);
	        appointment.getStartingTime().setDate(day);
	        isModified = true;
	        refreshDateButton();
	    }
	}
	
}
