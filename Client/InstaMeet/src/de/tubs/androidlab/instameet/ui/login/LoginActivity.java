package de.tubs.androidlab.instameet.ui.login;

import de.tubs.androidlab.instameet.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class LoginActivity extends Activity {

    private final static String TAG = LoginActivity.class.getSimpleName();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		if(savedInstanceState == null) {
			LoginFragment loginFragment = new LoginFragment();
			
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			
			transaction.add(R.id.activity_login_fragment_container, loginFragment);
			transaction.commit();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
