package de.tubs.androidlab.instameet.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.ui.login.LoginActivity;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
	private static final String KEY_ENABLE_LOCATION_TRACKING = "enable_location_tracking";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // this may be used by every activity to obtain settings
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
     
        Preference button = (Preference) getPreferenceManager().findPreference("logout");      
        if (button != null) {
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    sharedPref.edit().remove("securityToken").commit(); 
                    startLoginActivity();
                    return true;
                }
            });     
        }
    }
	
	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
	
    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
		if(key.equals(KEY_ENABLE_LOCATION_TRACKING)) {
			//TODO: enable / disable location tracking
		}
	}

}