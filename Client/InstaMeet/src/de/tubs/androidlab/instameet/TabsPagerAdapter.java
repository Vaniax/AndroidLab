package de.tubs.androidlab.instameet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * This adapter stores the fragments assigned to each tab
 * of the main activity 
 * @author Bjoern
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	Resources resources;

	public TabsPagerAdapter(FragmentManager fm, Resources r) {
		super(fm);
		resources = r;
	}

	@Override
	public Fragment getItem(int index) {
		switch(index) {
		case 0: return new ContactsFragment();
		case 1: return new MapFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}
	
	public String getPageTitle(int index) {
		switch(index) {
		case 0: return resources.getString(R.string.main_tab_contacts);
		case 1: return resources.getString(R.string.main_tab_map);
		}
		return null;
	}

}
