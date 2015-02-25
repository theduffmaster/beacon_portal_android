package com.bernard.beaconportal.activities.homeworkdue.viewpager;

import com.bernard.beaconportal.activities.homeworkdue.DueTodayFragment;
import com.bernard.beaconportal.activities.homeworkdue.DueTommorowFragment;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapterHomework extends FragmentStatePagerAdapter {

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
			DueTommorowFragment activity_main = new DueTommorowFragment();

			return activity_main;

		case 1:
			DueTodayFragment acitivty_main1 = new DueTodayFragment();

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

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// do nothing here! no call to super.restoreState(arg0, arg1);
	}

}