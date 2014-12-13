package de.tubs.androidlab.instameet.service;

import java.util.ArrayList;
import java.util.List;

import simpleEntities.SimpleUser;
import android.os.Binder;

/**
 * This binder is used to communicate with the InstaMeetService.
 * You can get an instance of this binder by calling {@link android.content.ContextWrapper.bindService()}
 * To communicate with the InstaMeetService get its instance by calling {@link #getService()}.
 * @author Bjoern
 */
public class InstaMeetServiceBinder extends Binder {

	private InstaMeetService service;
	
	InstaMeetServiceBinder(InstaMeetService service) {
		this.service = service;
	}
	
	public InstaMeetService getService() {
       	// Return this instance of LocalService so clients can call public methods
       	return service;
	}
	
	public List<SimpleUser> getFriends() {
		if(service.getOwnData() == null)
			return null;
		
		List<SimpleUser> friends = new ArrayList<SimpleUser>();
		for(int i : service.getOwnData().getFriends()) {
			if(!service.getUsers().containsKey(i)) {
				//Add new getUser Request and maybe placeholder in application
			}
			friends.add(service.getUsers().get(i));
		}
		return friends;
		
		
	}

}
