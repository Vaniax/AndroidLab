package de.tubs.androidlab.instameet.ui.appointment;

import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.R.id;
import de.tubs.androidlab.instameet.R.layout;
import de.tubs.androidlab.instameet.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_appointment, menu);
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
