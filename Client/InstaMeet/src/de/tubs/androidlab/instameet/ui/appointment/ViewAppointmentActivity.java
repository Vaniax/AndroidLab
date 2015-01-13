package de.tubs.androidlab.instameet.ui.appointment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import de.tubs.androidlab.instameet.R;

public class ViewAppointmentActivity extends Activity {
	
	public final static String EXTRA_APPOINTMENT_NAME = "de.tubs.anroidlab.instameet.NAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_appointment);
		getActionBar().setTitle(getIntent().getStringExtra(EXTRA_APPOINTMENT_NAME));
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
}
