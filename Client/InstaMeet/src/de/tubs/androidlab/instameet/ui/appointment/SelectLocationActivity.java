package de.tubs.androidlab.instameet.ui.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.tubs.androidlab.instameet.R;

public class SelectLocationActivity extends FragmentActivity implements OnMapClickListener, OnMarkerClickListener  {

	static public final String EXTRA_LATITUDE = "de.tubs.androidlab.instameet.LATITUDE";
	static public final String EXTRA_LONGITUDE = "de.tubs.androidlab.instameet.LONGITUDE";
	
	private final static float ZOOM_STEP_SIZE = 2;
	private final static int ZOOM_DURATION_MILLIS = 20;
	private final static float ZOOM_LIMIT = 14;
	
    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.select_location, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_accept:
		{
			Intent intent = new Intent();
			intent.putExtra(EXTRA_LATITUDE, marker.getPosition().latitude);
			intent.putExtra(EXTRA_LONGITUDE, marker.getPosition().longitude);
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
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
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.select_location_map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        marker = mMap.addMarker(
        		new MarkerOptions()
        		.position(new LatLng(0, 0))
        		.title("Click to move me to the desired Location")
        		.draggable(true)
        	);
        marker.showInfoWindow();
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

	@Override
	public void onMapClick(LatLng position) {
		marker.setPosition(position);
		float zoom = mMap.getCameraPosition().zoom;
		if(zoom < ZOOM_LIMIT) {
			mMap.animateCamera(
					CameraUpdateFactory.newLatLngZoom(position, zoom+ZOOM_STEP_SIZE),
					ZOOM_DURATION_MILLIS,
					null
					);
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return true;
	}

}
