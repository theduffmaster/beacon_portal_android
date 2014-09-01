package com.bernard.beaconportal.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapterScheduleView extends FragmentPagerAdapter {

	private int days;

	private int day;

	final int PAGE_COUNT = 5;
	private String titles[] = new String[] { "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday" };

	public ViewPagerAdapterScheduleView(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

		case 0:
			Monday_view schedule_list_view = new Monday_view();
			return schedule_list_view;

		case 1:
			Tuesday_view schedule_list_view1 = new Tuesday_view();
			return schedule_list_view1;

		case 2:
			Wednesday_view schedule_list_view2 = new Wednesday_view();
			return schedule_list_view2;

		case 3:
			Thursday_view schedule_list_view3 = new Thursday_view();
			return schedule_list_view3;

		case 4:
			Friday_view schedule_list_view4 = new Friday_view();
			return schedule_list_view4;
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
