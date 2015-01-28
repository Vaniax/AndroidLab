package de.tubs.androidlab.instameet.ui.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simpleEntities.SimpleAppointment;
import simpleEntities.SimpleUser;
import android.app.Activity;
import android.app.Fragment;
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
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.client.listener.AbstractInboundMessageListener;
import de.tubs.androidlab.instameet.service.InstaMeetService;
import de.tubs.androidlab.instameet.service.InstaMeetServiceBinder;
import de.tubs.androidlab.instameet.ui.appointment.ViewAppointmentActivity;

public class OverviewMapFragment extends Fragment implements OnInfoWindowClickListener {
	private final static String TAG = OverviewMapFragment.class.getSimpleName();

    private GoogleMap mMap;
    private InstaMeetService service = null;   
    private ServiceListener listener = new ServiceListener();

    private Map<Marker, SimpleUser> userMarker = new HashMap<Marker, SimpleUser>();
    private Map<Marker, SimpleAppointment> nearAppMarker = new HashMap<Marker, SimpleAppointment>();
    private Map<Marker, SimpleAppointment> myAppMarker = new HashMap<Marker, SimpleAppointment>();
    private Map<Marker, SimpleAppointment> visitingAppMarker = new HashMap<Marker, SimpleAppointment>();
    
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }
	
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.overview_map, menu);
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
    
    @Override
	public void onResume() {
        super.onResume();
        setUpMapIfAvailable();
        setHasOptionsMenu(true);
        
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfAvailable() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the MapFragment.
        	mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.main_map) ).getMap(); 
        }
        if (mMap != null) {
            setUpMap();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    private void setUpMap() {

    	if(service != null){
    		Log.i(TAG, "Adding markers to map");
    			getActivity().runOnUiThread(new Runnable() {
    				@Override
					public void run() {
    			    	mMap.clear();
    		        	//Get and Display friends from Service
    		    		List<SimpleUser> friends = service.getFriends();
    		    		for(final SimpleUser f : friends) {
	    	    	        Marker mark = mMap.addMarker(new MarkerOptions().position(new LatLng(f.getLattitude(), f.getLongitude()))
	    	    	        		.title(f.getUsername())
	    	    	        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
	    	    	        userMarker.put(mark, f);
    		    		}
    		        	//TODO: Get and Display Own Appointments from Service
    		    		List<SimpleAppointment> hostedApps = service.getMyHostedAppointments();
    		    		for(final SimpleAppointment a : hostedApps) {
	    	    	        Marker mark = mMap.addMarker(new MarkerOptions().position(new LatLng(a.getLattitude(), a.getLongitude()))
	    	    	        		.title(a.getTitle())
	    	    	        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
	    	    	        myAppMarker.put(mark, a);    		    			
    		    		}
    		        	//TODO: Get and Display your visiting Appointments from Service
    		    		List<SimpleAppointment> visitingApps = service.getVisitingAppointments();
    		    		for(final SimpleAppointment a : visitingApps) {
	    	    	        Marker mark = mMap.addMarker(new MarkerOptions().position(new LatLng(a.getLattitude(), a.getLongitude()))
	    	    	        		.title(a.getTitle())
	    	    	        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	    	    	        visitingAppMarker.put(mark, a);    		    			
    		    		}   		    		
    		        	//TODO: Get and Display near appointments from Service   		    		
    		    		List<SimpleAppointment> nearApps = service.getNearAppointments();
    		    		for(final SimpleAppointment a : nearApps) {
	    	    	        Marker mark = mMap.addMarker(new MarkerOptions().position(new LatLng(a.getLattitude(), a.getLongitude()))
	    	    	        		.title(a.getTitle())
	    	    	        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
	    	    	        nearAppMarker.put(mark, a);    		    			
    		    		}   		    		
    				}
    			});

    	}
  
//        mMap.addMarker(new MarkerOptions().position(new LatLng(52.273821, 10.531404)).title("Feuerzangenbowle"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(52.266993, 10.553677)).title("Öffentl. Grillen"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(52.263499, 10.527799)).title("Demo gegen alles")).showInfoWindow();
        
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(
        		CameraUpdateFactory.newLatLngZoom(new LatLng(52.262948, 10.521834), 13f)
        	);
        mMap.setOnInfoWindowClickListener(this);
    }

	@Override
	public void onInfoWindowClick(Marker marker) {
		Intent intent;
		if(userMarker.containsKey(marker)) {
			//TODO: ViewUser Intent. 
	        intent = new Intent(getActivity(), ViewAppointmentActivity.class);
	        //intent.putExtra(ViewUserActivity.EXTRA_USER_ID, userMarker.get(marker).getUserId());
		} else {
	        intent = new Intent(getActivity(), ViewAppointmentActivity.class);
	        intent.putExtra(ViewAppointmentActivity.EXTRA_APPOINTMENT_NAME, marker.getTitle());

		}
        startActivity(intent);
	}


	private class ServiceListener extends AbstractInboundMessageListener {
		@Override
		public void listFriends() {
			super.listFriends();		
			setUpMapIfAvailable();
		}
		
		@Override
		public void listNearAppointments() {
			// TODO Auto-generated method stub
			super.listNearAppointments();
			setUpMapIfAvailable();
		}
		
		@Override
		public void listVisitingAppointments() {
			// TODO Auto-generated method stub
			super.listVisitingAppointments();
			setUpMapIfAvailable();
		}
		
	}	
	
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
        	service = ( (InstaMeetServiceBinder) binder).getService();
        	service.processor.listener.addListener(listener);
        	setUpMapIfAvailable();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.e(TAG, "Connection lost");
            service = null;
        }
    };
}
