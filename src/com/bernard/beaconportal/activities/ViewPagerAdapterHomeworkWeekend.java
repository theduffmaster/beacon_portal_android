package com.bernard.beaconportal.activities;

import java.util.Calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapterHomeworkWeekend extends FragmentPagerAdapter {
	
	final int PAGE_COUNT = 2;
	private String titles[] = new String[] { "Homework Due Monday",
			"Homework Due Today" };

	public ViewPagerAdapterHomeworkWeekend(FragmentManager fm) {
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