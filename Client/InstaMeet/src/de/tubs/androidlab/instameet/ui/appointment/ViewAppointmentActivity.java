package de.tubs.androidlab.instameet.ui.appointment;

import java.util.ArrayList;
import java.util.List;

import simpleEntities.SimpleUser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.ui.ContactsListAdapter;

/**
 * Activity which allows you to view an existing
 * appointment
 * @author Bjoern
 *
 */
public class ViewAppointmentActivity extends Activity {
	
	public final static String EXTRA_APPOINTMENT_NAME = "de.tubs.anroidlab.instameet.NAME";
	
	private ContactsListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		getActionBar().setTitle(getIntent().getStringExtra(EXTRA_APPOINTMENT_NAME));
		
		final ListView participantsList = new ListView(this);
		final View header = layoutInflater.inflate(R.layout.activity_view_appointment, null);
		participantsList.addHeaderView(header, null, false);
		setContentView(participantsList);
		
		adapter = new ContactsListAdapter(layoutInflater,this);
			List<SimpleUser> l = new ArrayList<SimpleUser>();
			SimpleUser s = new SimpleUser();
			s.setUsername("Peter");
			l.add(s); l.add(s); l.add(s);l.add(s); l.add(s); l.add(s);l.add(s); l.add(s); l.add(s);
			adapter.setContacts(l);
		participantsList.setAdapter(adapter);
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
