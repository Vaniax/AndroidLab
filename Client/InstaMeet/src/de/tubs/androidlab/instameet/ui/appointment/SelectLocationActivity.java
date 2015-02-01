package de.tubs.androidlab.instameet.ui.appointment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.tubs.androidlab.instameet.R;

public class SelectLocationActivity extends FragmentActivity implements OnMapClickListener, OnMarkerClickListener  {

	static public final String EXTRA_LATITUDE = "de.tubs.androidlab.instameet.LATITUDE";
	static public final String EXTRA_LONGITUDE = "de.tubs.androidlab.instameet.LONGITUDE";
	public static final String EXTRA_SELECT_LOCATION_RESULT = "de.tubs.androidlab.instameet.SEARCH_RESULT";

	
	private final static float ZOOM_STEP_SIZE = 2;
	private final static int ZOOM_DURATION_MILLIS = 20;
	private final static float ZOOM_LIMIT = 14;
	
    private GoogleMap mMap;
    private Marker marker;
    
    private ListView listView;
    private ListAdapter adapter;

    private MapFragment mMapFragment;
    private Map<Integer,LatLng> currSearchItems = new HashMap<Integer,LatLng>();

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        setTitle("Search Location");
       
        listView = (ListView) findViewById(R.id.select_location_list);
        listView.setBackgroundColor(Color.WHITE);
        listView.setAlpha(0.75f);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LatLng currPosition = currSearchItems.get(position);
				marker.setPosition(currPosition);
				float zoom = mMap.getCameraPosition().zoom;
				marker.setTitle((String)adapter.getItem(position));
				marker.showInfoWindow();
				mMap.animateCamera(
						CameraUpdateFactory.newLatLngZoom(currPosition, 13f),
						ZOOM_DURATION_MILLIS,
						null
						);
			}
		});
        
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
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_accept:
		{
			if (!marker.getPosition().equals(new LatLng(0, 0))) {
				Intent intent = new Intent();
				intent.putExtra(EXTRA_LATITUDE, marker.getPosition().latitude);
				intent.putExtra(EXTRA_LONGITUDE, marker.getPosition().longitude);
				intent.putExtra(EXTRA_SELECT_LOCATION_RESULT, marker.getTitle());
				setResult(RESULT_OK, intent);
				finish();
				return true;
			} else {
				Toast.makeText(this, "Please enter a valid location", Toast.LENGTH_LONG).show();;
			}
		} 
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}
		

    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
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
        		.title("Click to move me to the desired location")
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
		Geocoder geo = new Geocoder(getBaseContext());
		double latitude = marker.getPosition().latitude;
		double longitude = marker.getPosition().longitude;
		
		
		try {
			List<Address> address = geo.getFromLocation(latitude, longitude, 1);
			marker.setTitle(addressToString(address.get(0)));
		} catch (IOException e) {
			marker.setTitle("(" + latitude + ", " + longitude + ")");
			e.printStackTrace();
		}		
		marker.showInfoWindow();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return true;
	}
		
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doSearch(query);
		}
	}
	
	private void doSearch(String query) {
		new GeocoderTask().execute(query);
	}
	
	private String addressToString(Address address) {
		String addressLine = address.getAddressLine(0);
        String city = address.getLocality();
        String state = address.getAdminArea();
        String zip = address.getPostalCode();
        String country = address.getCountryName();
        StringBuilder result = new StringBuilder();
        String spacer = ", ";
        if(addressLine != null) {
        	result.append(addressLine);
        } if (zip != null && !zip.isEmpty()) {
        	result.append(spacer);
        	result.append(zip);
        } if (city != null && !city.equals(addressLine) && !city.isEmpty()) {
        	result.append(spacer);
        	result.append(city);

        }
        return result.toString();
	}

	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> 
	{
		@Override
		protected List<Address> doInBackground(String... locationName) {
			Geocoder geocoder = new Geocoder(getBaseContext());
			List<Address> addresses = null;
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {
			int position = 0;
			if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No location found", Toast.LENGTH_SHORT).show();
            } else {
                Address address = addresses.get(0);
                List<String> addressFragments = new ArrayList<String>();
                
                for(Address i : addresses) {
                    String result = addressToString(i);
                    
                    addressFragments.add(result);
                    currSearchItems.put(position, new LatLng(i.getLatitude(),i.getLongitude()));
                    position++;
                }

                
                adapter = new ArrayAdapter<String>(SelectLocationActivity.this, android.R.layout.simple_list_item_1,addressFragments);
                listView.setAdapter(adapter);
            }
		}
		
	}
}
