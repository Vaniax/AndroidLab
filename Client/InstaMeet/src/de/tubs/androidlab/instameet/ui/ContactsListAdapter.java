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
	 * Keep in mind, that the contact data is NOT copied
	 * If you want to change it later, you should pass a copy
	 * here or use the addContac() / removeCOntact() methods.
	 * @param contacts new contact data
	 */
	public void setContacts(List<SimpleUser> contacts) {
		this.contacts = contacts;
		notifyChanges();
	}
	
	/**
	 * Gets the adapter's underlying set of contacts
	 * @return
	 */
	public List<SimpleUser> getContacts() {
		return contacts;
	}
	
	/**
	 * Adds a user to the contacts list
	 * @param user contact to add
	 */
	public void addContact(SimpleUser user) {
		contacts.add(user);
		notifyChanges();
	}
	
	/**
	 * Removes a user from the contacts list
	 * @param user contact to remove
	 */
	public void removeContact(SimpleUser user) {
		contacts.remove(user);
		notifyChanges();
	}
	
	/**
	 * Shows a message icon at a specified position
	 * @param position position to show a message icon at
	 */
	public void setMessageIconVisibility(int position) {
		//TODO: should pass a SimpleUser instead of position
		isVisible.put(position, true);
	}
	
	//TODO: make private!
	public void notifyChanges() {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				notifyDataSetChanged();
			}	
		});
	}

}
