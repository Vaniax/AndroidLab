package de.tubs.androidlab.instameet.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import simpleEntities.SimpleUser;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.tubs.androidlab.instameet.R;

/**
 * An adapter connects data with a list.
 * @author Bjoern
 */
public class ContactsListAdapter extends BaseAdapter
{
	private List<SimpleUser> contacts;
	private Activity activity = null;
	private ViewHolder holder = null;
	private Map<Integer,Boolean> isVisible = new HashMap<Integer,Boolean>();

	/**
	 * Additional data assigned to each entry which holds
	 * references to all widgets (to avoid findViewById()-calls)
	 * @author Bjoern
	 */
	class ViewHolder {
		public TextView name;
		public ImageView picture;
		public ImageView messageIcon;
	}
	
	private LayoutInflater inflater;
	
	public ContactsListAdapter(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater; 
		this.activity = activity;
		holder = new ViewHolder();
	}

	@Override
	public int getCount() {
		if(contacts == null) {
			return 0;
		} else {
			return contacts.size();
		}
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
		if(convertView != null) {
			rowView = convertView;
			holder = (ViewHolder) rowView.getTag();
		} else {
			rowView = inflater.inflate(R.layout.contact_list_entry, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) rowView.findViewById(R.id.name);
			holder.picture = (ImageView) rowView.findViewById(R.id.picture);
			holder.messageIcon = (ImageView) rowView.findViewById(R.id.contacts_message_icon);

			rowView.setTag(holder);
		}
		if (isVisible.containsKey(position)) {
			if (isVisible.remove(position)) {
				holder.messageIcon.setVisibility(View.VISIBLE);
			}
		} else {
			holder.messageIcon.setVisibility(View.GONE);
		}
		holder.name.setText(contacts.get(position).getUsername());
		return rowView;
	}
	
	/**
	 * Changes the adapter's data set of contacts
	 * @param contacts
	 */
	public void setContacts(List<SimpleUser> contacts) {
		this.contacts = contacts;
		notifyChanges();
	}
	
	public List<SimpleUser> getContacts() {
		return contacts;
	}
	
	public void addContact(SimpleUser user) {
		contacts.add(user);
		notifyChanges();
	}
	
	public void setMessageIconVisibility(int position) {
		isVisible.put(position, true);
	}
	public void notifyChanges() {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				notifyDataSetChanged();
			}	
		});
	}
	public void removeItem(SimpleUser user) {
		contacts.remove(user);
		notifyChanges();
	}
}
