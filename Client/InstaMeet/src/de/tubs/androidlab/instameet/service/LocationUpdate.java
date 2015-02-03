package de.tubs.androidlab.instameet.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationUpdate extends Thread {
	private final static String TAG = "LocationUpdate";

	private InstaMeetService service;
	private LocationManager locationManager;
	private MyLocationListener listener;
	private Location previousBestLocation = null;
	
	private static final int TIME_LIMIT = 30000;
	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;

	
	public LocationUpdate(InstaMeetService service) {
		this.service = service;
		
		locationManager = (LocationManager) service.getSystemService(Context.LOCATION_SERVICE);
	    listener = new MyLocationListener();        
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_LIMIT, 0, listener);
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_LIMIT, 0, listener);
	    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    if (isGPSEnabled) {
		    service.updateLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
	    } else if (isNetworkEnabled) {
	    	service.updateLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
	    }
	}
	
//	public Thread performOnBackgroundThread() {
//	    final Thread t = new Thread() {
//	        @Override
//	        public void run() {
//	            try {
//	                this.run();
//	            } finally {
//
//	            }
//	        }
//	    };
//	    return t;
//	}
	
	public Location getLatestLocation() {
		return previousBestLocation;
	}
	
	boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TIME_LIMIT;
	    boolean isSignificantlyOlder = timeDelta < -TIME_LIMIT;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than xxx minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}
	
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	private class MyLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(Location location) {
			Log.i(TAG,"Location changed");
			if(isBetterLocation(location, previousBestLocation) && (isGPSEnabled||isNetworkEnabled)) {
				previousBestLocation = location;
				service.updateLocation(location);
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			if (provider.equals(LocationManager.GPS_PROVIDER)) {
				isGPSEnabled = true;
			} else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
				isGPSEnabled = true;
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			if (provider.equals(LocationManager.GPS_PROVIDER)) {
				isGPSEnabled = false;
			} else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
				isGPSEnabled = false;
			}
		}
		
	}
}
