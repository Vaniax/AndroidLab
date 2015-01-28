package de.tubs.androidlab.instameet.ui.viewuser;

import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.R.id;
import de.tubs.androidlab.instameet.R.layout;
import de.tubs.androidlab.instameet.R.menu;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This activity displays information about a specific user
 * You must specify the user's ID when starting this activity
 * in its intent's extras as follows:</br>
 * <code>
 * intent.putExtra(ViewUserActivity.EXTRA_USER_ID, id);
 * </code>
 * @author Bjoern
 */
public class ViewUserActivity extends Activity {
	
	public static final String EXTRA_USER_ID = "de.tubs.androidlab.instameet.USER_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_addFriend) {
			//TODO: add friend to user's contacts
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
