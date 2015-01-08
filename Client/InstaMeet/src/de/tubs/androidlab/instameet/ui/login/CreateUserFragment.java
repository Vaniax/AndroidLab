package de.tubs.androidlab.instameet.ui.login;

import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.client.listener.AbstractInboundMessageListener;
import de.tubs.androidlab.instameet.client.listener.InboundListener;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUserFragment extends Fragment {
	
	private Button registerButton = null;
	private EditText password = null;
	private EditText passwordRep = null;
	private EditText userName = null;
	
	private InstaMeetService service = null;
	private final BoolListener boolListener = new BoolListener();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_login_create_user, container, false);
		
		registerButton = (Button) view.findViewById(R.id.login_register_button);
		userName = (EditText) view.findViewById(R.id.login_firstname);
		password = (EditText) view.findViewById(R.id.login_password);
		passwordRep = (EditText) view.findViewById(R.id.login_password_repeat);		
		registerButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				if (isUserNameAcceptable() && isPasswordAcceptable()) {
					service.createUser(password.getText().toString(), userName.getText().toString());; // TODO: Implementation in main branch necessary
					Toast.makeText(getActivity().getBaseContext(), "Creating new user", Toast.LENGTH_LONG).show();
					// TODO: Implementation of some callback mechanism to check when there is a valid security token
					service.processor.listener.addListener(boolListener);
//					toLoginScreen();
				}
			}
		});
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		service = ((LoginActivity) activity).getService();
	}
	
		
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		service.processor.listener.removeListener(boolListener);
	}

	private boolean isPasswordAcceptable() {
		String pass1 = password.getText().toString();
		String pass2 = passwordRep.getText().toString();
		int neededPasswordLength = 3; 
		
		boolean acceptable = true;
		if (pass1.isEmpty() || pass2.isEmpty() || (pass1.isEmpty() && pass2.isEmpty())) {
			Toast.makeText(getActivity().getBaseContext(), "Please fill in both password fields!", Toast.LENGTH_LONG).show();
			acceptable = false;
		} else if (!pass1.equals(pass2)) {
			Toast.makeText(getActivity().getBaseContext(), "Passwords don't match!", Toast.LENGTH_LONG).show();
			acceptable = false;
		} else if (pass1.length() <= neededPasswordLength) {
			Toast.makeText(
					getActivity().getBaseContext(), 
					"Password needs more than " + neededPasswordLength + " characters!", 
					Toast.LENGTH_LONG
			).show();
			acceptable = false;
		}
		return acceptable;
	}
	
	private boolean isUserNameAcceptable() {
		String name = userName.getText().toString();
		boolean acceptable = true;
		if (name.isEmpty()) {
			Toast.makeText(getActivity().getBaseContext(), "No user name choosen!", Toast.LENGTH_LONG).show();
			acceptable = false;
		}
		// TODO: Some form of checking if there is a duplicate user name
		return acceptable;
	}

	private void toLoginScreen() {
		Fragment createLoginFragment = new LoginFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.activity_login_fragment_container, createLoginFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	private class BoolListener extends AbstractInboundMessageListener {

		@Override
		public void bool(boolean isTrue) {
			super.bool(isTrue);
			if (isTrue) {
//				Toast.makeText(getActivity().getBaseContext(), "User created sucessfull", Toast.LENGTH_LONG).show(); // Throws exception because of wrong context
				toLoginScreen();
			} else {
//				Toast.makeText(getActivity().getBaseContext(), "User not created", Toast.LENGTH_LONG).show();
			}
		}

	}
	
}
