package de.tubs.androidlab.instameet.service;

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

}
