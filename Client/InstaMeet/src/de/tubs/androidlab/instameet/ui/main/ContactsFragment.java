package de.tubs.androidlab.instameet.ui.main;

import java.util.ArrayList;
import java.util.List;

import simpleEntities.SimpleUser;
import android.app.Activity;
import android.app.ListFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.client.listener.AbstractInboundMessageListener;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.chat.ChatActivity;

/**
 * First tab of main activity 
 * @author Bjoern
 */
public class ContactsFragment extends ListFragment {
	private final static String TAG = ContactsFragment.class.getSimpleName();
	public ContactsListAdapter adapter;
    private InstaMeetService service = null;
    private ServiceListener listener = new ServiceListener();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ContactsListAdapter(getActivity());
		setListAdapter(adapter);
		setHasOptionsMenu(true);
	}
	
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contacts, menu);
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_addFriend) {
        	//Intent intent = new Intent(this, AddFriendActivity.class);
        	//startActivity(intent);
        	return true;
        }
        return false;
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	container.invalidate();
    	return inflater.inflate(R.layout.fragment_contacts, container, false);
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(getActivity(), ChatActivity.class);
    	intent.putExtra(ChatActivity.EXTRA_NAME, adapter.getItem(position).getId());
    	startActivity(intent);
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
    	Intent intent = new Intent(activity, InstaMeetService.class);
    	if(!getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
    		Log.e(TAG, "Service not available");
    	}   	
	}

	@Override
	public void onDetach() {
		super.onDetach();
    	if(service != null) {
    		getActivity().unbindService(serviceConnection);
        	service.processor.listener.removeListener(listener);
    		service = null;
    	}
	}

	private class ServiceListener extends AbstractInboundMessageListener {
		@Override
		public void ownData() {
			super.ownData();
			service.fetchFriends();
		}
		@Override
		public void listFriends() {
			super.listFriends();			
			adapter.setContacts(service.getFriends());
		}
	}
	
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
        	service.processor.listener.addListener(listener);
        	service.fetchOwnData();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.e(TAG, "Connection lost");
            service = null;
        }
    };
    

    /**
     * An adapter connects data with a list.
     * @author Bjoern
     */
	private class ContactsListAdapter extends BaseAdapter
	{
		private ArrayList<SimpleUser> contacts = new ArrayList<SimpleUser>();

		/**
		 * Additional data assigned to each entry which holds
		 * references to all widgets (to avoid findViewById()-calls)
		 * @author Bjoern
		 */
		class ViewHolder {
			public TextView name;
			public ImageView picture;
		}
		
		private Context context;
		private LayoutInflater inflater;
		
		public ContactsListAdapter(Context c) {
			context = c;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return contacts.size();
		}

		@Override
		public SimpleUser getItem(int position) {
			return contacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return contacts.get(position).getId();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView;
			ViewHolder holder;
			if(convertView != null) {
				rowView = convertView;
				holder = (ViewHolder) rowView.getTag();
			} else {
				rowView = inflater.inflate(R.layout.contact_list_entry, parent, false);
				holder = new ViewHolder();
				holder.name = (TextView) rowView.findViewById(R.id.name);
				holder.picture = (ImageView) rowView.findViewById(R.id.picture);
				rowView.setTag(holder);
			}
			holder.name.setText(contacts.get(position).getUsername());
			return rowView;
		}

		public void setContacts(List<SimpleUser> newContacts) {
			contacts.clear();
			contacts.addAll(newContacts);
			if(adapter != null) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			}
			
		}
	}
}
