package de.tubs.androidlab.instameet.ui.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.tubs.androidlab.instameet.R;
import de.tubs.androidlab.instameet.ui.appointment.ViewAppointmentActivity;

public class OverviewMapFragment extends Fragment implements OnInfoWindowClickListener {

    private GoogleMap mMap;

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
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(52.273821, 10.531404)).title("Feuerzangenbowle"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(52.266993, 10.553677)).title("Öffentl. Grillen"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(52.263499, 10.527799)).title("Demo gegen alles")).showInfoWindow();
        
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(
        		CameraUpdateFactory.newLatLngZoom(new LatLng(52.262948, 10.521834), 13f)
        	);
        mMap.setOnInfoWindowClickListener(this);
    }

	@Override
	public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getActivity(), ViewAppointmentActivity.class);
        intent.putExtra(ViewAppointmentActivity.EXTRA_APPOINTMENT_NAME, marker.getTitle());
        startActivity(intent);
	}
}
