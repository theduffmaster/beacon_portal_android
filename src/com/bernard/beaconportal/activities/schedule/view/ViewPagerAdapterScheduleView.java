package com.bernard.beaconportal.activities.schedule.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapterScheduleView extends FragmentPagerAdapter {

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
			MondayView schedule_list_view = new MondayView();
			return schedule_list_view;

		case 1:
			TuesdayView schedule_list_view1 = new TuesdayView();
			return schedule_list_view1;

		case 2:
			WednesdayView schedule_list_view2 = new WednesdayView();
			return schedule_list_view2;

		case 3:
			ThursdayView schedule_list_view3 = new ThursdayView();
			return schedule_list_view3;

		case 4:
			FridayView schedule_list_view4 = new FridayView();
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
