package de.tubs.androidlab.instameet.ui.chat;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.ui.chat.ChatMessageProxy.DIRECTION;

/**
 * An adapter connects data with a list.
 * @author Bjoern
 */
class ChatListAdapter extends BaseAdapter
{
	ArrayList<ChatMessageProxy> messages = new ArrayList<ChatMessageProxy>();
	
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
	private Activity activity;
	private LayoutInflater inflater;
	
	public ChatListAdapter(Context c) {
		context = c;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
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
		ChatMessageProxy message = messages.get(position);
		holder.message.setText(message.getMessage());
		holder.timeStamp.setText("13:37");

		if (message.getDirection().equals(DIRECTION.OUTGOING)){
			rowView.setBackgroundColor(Color.LTGRAY);
		}
		
		return rowView;
	}
	
	public void addMessage(ChatMessageProxy message) {
		messages.add(message);
		
		((ChatActivity)context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
}
