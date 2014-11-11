package de.tubs.androidlab.instameet.ui.main;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactsFragment extends ListFragment {
	private ContactsListAdapter adapter;
	
    String[] values = new String[] { "Hans Wurst", 
                                    "Peter Schmidt",
                                    "Angela Merkel",
                                    "Ann Droid", 
                                    "42", 
                                    "Ende", 
                                  };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	adapter = new ContactsListAdapter(getActivity());
    	setListAdapter(adapter);
    	return super.onCreateView(inflater, container, savedInstanceState);
    }

	private class ContactsListAdapter extends BaseAdapter
	{
		
		private Context context;
		
		public ContactsListAdapter(Context c) {
			context = c;
		}
		
		@Override
		public int getCount() {
			return values.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = new TextView(context);
			view.setText(values[position]);
			return view;
		}

	}
}
