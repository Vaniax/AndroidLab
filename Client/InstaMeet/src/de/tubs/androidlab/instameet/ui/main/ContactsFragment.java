package de.tubs.androidlab.instameet.ui.main;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import simpleEntities.SimpleUser;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.client.listener.AbstractInboundMessageListener;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.ContactsListAdapter;
import de.tubs.androidlab.instameet.ui.SwipeDetector.Action;
import de.tubs.androidlab.instameet.ui.chat.ChatActivity;
import de.tubs.androidlab.instameet.ui.SwipeDetector;

/**
 * First tab of main activity 
 * @author Bjoern
 */
public class ContactsFragment extends ListFragment {
	private final static String TAG = ContactsFragment.class.getSimpleName();
	
	public ContactsListAdapter adapter;
    private InstaMeetService service = null;
    private ServiceListener listener = new ServiceListener();
    private Map<Integer,Integer> positionUserID = new HashMap<Integer,Integer>();
    private boolean newFriendRequests = false;
    private MenuItem itemFriendRequest;
    private ContactsListAdapter requestListAdapter;
    private Dialog dialog;
    private SwipeDetector swipeDetector;
    private Animation anim;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ContactsListAdapter((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE),this.getActivity());
		setListAdapter(adapter);
		setHasOptionsMenu(true);
		swipeDetector = new SwipeDetector();
	
		anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_anim);
	}
	
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.contacts, menu);
       MenuItem item = menu.findItem(R.id.action_newFriendRequest);
       itemFriendRequest = item;
       item.setVisible(newFriendRequests);
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_contacts, container, false);

    	return view;

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
			List<SimpleUser> list = service.getFriends();
			adapter.setContacts(list);
			ListIterator it = list.listIterator();
			while (it.hasNext()) {
				positionUserID.put(((SimpleUser) it.next()).getId(),it.nextIndex()-1);
			}
		}
		@Override
		public void chatMessage() {
			super.chatMessage();
			Set<Integer> set = service.getNewMessages().keySet();
			for (Integer i : set) {
				adapter.setMessageIconVisibility(positionUserID.get(i));
			}
			adapter.notifyChanges();
		}
		@Override
		public void friendRequest() {
			super.friendRequest();
    		itemFriendRequest.setVisible(true);
		}
		
	}
	
	
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.action_newFriendRequest:
    		dialog = new Dialog(getActivity());
    		dialog.setContentView(R.layout.list);
    		
    		ListView view = (ListView) dialog.findViewById(R.id.result_list);
    		requestListAdapter = new ContactsListAdapter((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), this.getActivity());
    		List<SimpleUser> users;
    		if (service.areNewFriendRequests()) {
        		users = service.getNewFriendRequests();
        		requestListAdapter.setContacts(users);
    		}
    		view.setAdapter(requestListAdapter);
    		view.setOnTouchListener(swipeDetector);
    		view.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, final View view,
						final int position, long id) {
					if (swipeDetector.swipeDetected()) {
						if (swipeDetector.getAction() == Action.RL) {
							Log.i("Contacts","Swipe");
							anim.setAnimationListener(new Animation.AnimationListener() {
								
								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onAnimationRepeat(Animation animation) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onAnimationEnd(Animation animation) {
									SimpleUser user = requestListAdapter.getItem(position); 
									requestListAdapter.removeItem(user);
									service.removeFriendRequest(user);
									service.addFriendReply(false, user.getId());
									if (requestListAdapter.getContacts().isEmpty()) {
										dialog.dismiss();
										getActivity().invalidateOptionsMenu();
									}
								}
							});
							view.startAnimation(anim);
						}
					} else { // Normal click
						SimpleUser user = requestListAdapter.getItem(position);
						service.getNewFriendRequests().remove(user);
						service.addFriend(user);
						adapter.addContact(user);
						requestListAdapter.removeItem(user);
						service.addFriendReply(true, user.getId());
					}
					
					if (requestListAdapter.getContacts().isEmpty()) {
						dialog.dismiss();
					}
				}
    		});
    		dialog.setTitle("Friendship requests");
    		
    		dialog.show();
    		
    	break;
    	}
		return super.onOptionsItemSelected(item);
	}



	/** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
        	service.processor.listener.addListener(listener);
        	service.fetchOwnData();
        	itemFriendRequest.setVisible(service.areNewFriendRequests());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.e(TAG, "Connection lost");
            service = null;
        }
    };
}
