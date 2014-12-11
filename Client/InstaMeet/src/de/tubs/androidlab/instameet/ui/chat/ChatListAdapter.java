package de.tubs.androidlab.instameet.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.tubs.androidlab.instameet.R;

/**
 * An adapter connects data with a list.
 * @author Bjoern
 */
class ChatListAdapter extends BaseAdapter
{
	
    String[] values = new String[] { "H",
    		"a", "l", "l", "o", "W", "elt", "!",
            "Ende"
          };
	
	/**
	 * Additional data assigned to each entry which holds
	 * references to all widgets (to avoid findViewById()-calls)
	 * @author Bjoern
	 */
	class ViewHolder {
		public TextView message;
		public TextView timeStamp;
	}
	
	private Context context;
	private LayoutInflater inflater;
	
	public ChatListAdapter(Context c) {
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
			rowView = inflater.inflate(R.layout.chat_list_entry, parent, false);
			holder = new ViewHolder();
			holder.message = (TextView) rowView.findViewById(R.id.message);
			holder.timeStamp = (TextView) rowView.findViewById(R.id.time_stamp);
			rowView.setTag(holder);
		}
		holder.message.setText(values[position]);
		holder.timeStamp.setText("13:37");
		return rowView;
	}
}
