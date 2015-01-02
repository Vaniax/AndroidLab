package de.tubs.androidlab.instameet.ui.login;

import de.tubs.androidlab.instameet.R;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginFragment extends Fragment {

    private final static String TAG = LoginFragment.class.getSimpleName();
	private Button registerButton = null;
	
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
		
		return view;
	}



}
