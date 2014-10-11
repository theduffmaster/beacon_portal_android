package com.bernard.beaconportal.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapterHomeworkAfterThree extends FragmentPagerAdapter {
	
	final int PAGE_COUNT = 1;
	private String titles[] = new String[] { "                             Homework Due Tommorow                             " };

	public ViewPagerAdapterHomeworkAfterThree(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

		case 0:
			Due_Tommorow_Fragment activity_main = new Due_Tommorow_Fragment();

			return activity_main;
		}
		return null;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}
}
	
