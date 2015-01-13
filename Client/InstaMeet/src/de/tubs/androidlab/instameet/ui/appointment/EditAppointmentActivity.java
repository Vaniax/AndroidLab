package de.tubs.androidlab.instameet.ui.appointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import de.tubs.androidlab.instameet.R;

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
	
	private boolean isNewAppointment;
	private int appointmentId;
	
	private boolean isModified = false;
	private EditText editTitle;
	private EditText editDescription;
	
	private AlertDialog dialogSaveAppointment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_appointment);
		editTitle = (EditText) findViewById(R.id.edit_title);
		editDescription = (EditText) findViewById(R.id.edit_description);
		editTitle.addTextChangedListener(this);
		editDescription.addTextChangedListener(this);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.containsKey(EXTRA_APPOINTMENT_ID)) {
			isNewAppointment = false;
			appointmentId = extras.getInt(EXTRA_APPOINTMENT_ID);
			//TODO: fill form widgets with information
			editTitle.setText("Known title");
			editDescription.setText("This appointment already exists.");
		} else {
			isNewAppointment = true;
		}
		
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
			//... appointmentId ...
			//...
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
}
