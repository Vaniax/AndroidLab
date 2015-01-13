package de.tubs.androidlab.instameet.ui.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.addfriend.AddFriendActivity;
import de.tubs.androidlab.instameet.ui.login.LoginActivity;
import de.tubs.androidlab.instameet.ui.settings.SettingsActivity;

/**
 * Main activity of the app which is shown on startup
 * @author Bjoern
 */
public class MainActivity extends Activity implements ActionBar.TabListener {

    private ViewPager viewPager = null;
    private final static String TAG = MainActivity.class.getSimpleName();
    private InstaMeetService service = null;
    
    private SharedPreferences pref = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Start Service if necessary
        // Omit this if you only want the service to be active during IPC
        startService(new Intent(this,InstaMeetService.class));

    	pref = PreferenceManager.getDefaultSharedPreferences(this);

    	if (!pref.contains("securityToken")) {
//    		finish();
//    		startLoginActivity();
//    		return;
    	}
    	
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        addTabs();

    }
    
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
    
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.e(TAG, "Connection lost");
            service = null;
        }
    };
    
    @SuppressWarnings("deprecation")
	private void addTabs() {
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	final FragmentPagerAdapter adapter =
    			new TabsPagerAdapter(getFragmentManager(), getResources());
        viewPager.setAdapter(adapter);
        
        // This is required to avoid a black flash when the map is loaded.  The flash is due
        // to the use of a SurfaceView as the underlying view of the map.
        viewPager.requestTransparentRegion(viewPager);

        // When swiping between different sections, select the corresponding tab. 
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < adapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Intent intent = new Intent(this, SettingsActivity.class);
        	startActivity(intent);
            return true;
        } else if (id == R.id.action_addFriend) {
        	Intent intent = new Intent(this, AddFriendActivity.class);
        	startActivity(intent);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
