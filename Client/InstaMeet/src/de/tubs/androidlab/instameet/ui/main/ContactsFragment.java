package de.tubs.androidlab.instameet.ui.main;

import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import de.tubs.androidlab.instameet.ui.chat.ChatActivity;

/**
 * First tab of main activity 
 * @author Bjoern
 */
public class ContactsFragment extends ListFragment {
	private ContactsListAdapter adapter;
	
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
    	return inflater.inflate(R.layout.fragment_contacts, container, false);
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(getActivity(), ChatActivity.class);
    	intent.putExtra(ChatActivity.EXTRA_NAME, (String) adapter.getItem(position));
    	startActivity(intent);
    }

    /**
     * An adapter connects data with a list.
     * @author Bjoern
     */
	private class ContactsListAdapter extends BaseAdapter
	{
		ArrayList<String> contacts = new ArrayList<String>();
	    String[] values = new String[] { "Hans Wurst", 
                "Peter Schmidt",
                "Angela Merkel",
                "Ann Droid", 
                "42",
                "Edmund Stoiber",
                "Helge Schneider",
                "Peter Pan",
                "Ende",
                "Peter Schmidt",
                "Angela Merkel",
                "Ann Droid", 
                "42",
                "Edmund Stoiber",
                "Helge Schneider",
                "Peter Pan",
                "Ende"
              };
		
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
			return values.length;
		}

		@Override
		public Object getItem(int position) {
			return values[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
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
			holder.name.setText(values[position]);
			return rowView;
		}

	}
}
