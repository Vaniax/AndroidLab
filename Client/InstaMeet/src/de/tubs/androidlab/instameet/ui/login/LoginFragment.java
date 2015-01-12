package de.tubs.androidlab.instameet.ui.login;

import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.client.listener.AbstractInboundMessageListener;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.main.MainActivity;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {

    private final static String TAG = LoginFragment.class.getSimpleName();
	private Button registerButton = null;
	private Button loginButton = null;
	private EditText password = null;
	private EditText userName = null;
	private InstaMeetService service = null;
	private final TokenListener tokenListener = new TokenListener();

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		
		registerButton = (Button) view.findViewById(R.id.login_button_register);
		
		registerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment createUserFragment = new CreateUserFragment();
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.activity_login_fragment_container, createUserFragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
		
		userName = (EditText) view.findViewById(R.id.login_firstname);
		password = (EditText) view.findViewById(R.id.login_password);
		loginButton = (Button) view.findViewById(R.id.login_button_login);
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				service.login(userName.getText().toString(), password.getText().toString());
				service.processor.listener.addListener(tokenListener);
			}
		});
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		service = ((LoginActivity) activity).getService();
    	Intent intent = new Intent(activity, InstaMeetService.class);
    	if(!getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
    		Log.e(TAG, "Service not available");
    	}
	}
	
    private void toMainActivity() {
        Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

	private class TokenListener extends AbstractInboundMessageListener {
		@Override
		public void securityToken(String token) {
			super.securityToken(token);
			Log.d("TokenListener","Token listener activated\nContent: " + token);
			Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
			edit.putString("securityToken", token);
			edit.commit();
			toMainActivity();
		}
	}
	
    @Override
	public void onDestroyView() {
		super.onDestroyView();
		service.processor.listener.removeListener(tokenListener);
    	if(service != null) {
    		getActivity().unbindService(serviceConnection);
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
}
