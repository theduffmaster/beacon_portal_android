package com.bernard.beaconportal.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapterHomework extends FragmentPagerAdapter {

	final int PAGE_COUNT = 2;
	private String titles[] = new String[] { "Homework Due Tommorow",
			"Homework Due Today" };

	public ViewPagerAdapterHomework(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

		case 0:
			Due_Tommorow_Fragment activity_main = new Due_Tommorow_Fragment();

			return activity_main;

		case 1:
			Due_Today_Fragment acitivty_main1 = new Due_Today_Fragment();

			return acitivty_main1;
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