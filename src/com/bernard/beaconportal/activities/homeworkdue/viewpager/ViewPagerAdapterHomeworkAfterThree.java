package com.bernard.beaconportal.activities.homeworkdue.viewpager;

import com.bernard.beaconportal.activities.homeworkdue.DueTommorowFragment;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapterHomeworkAfterThree extends
		FragmentStatePagerAdapter {

	final int PAGE_COUNT = 1;
	private String titles[] = new String[] { "Homework Due Tommorow" };

	public ViewPagerAdapterHomeworkAfterThree(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

		case 0:
			DueTommorowFragment activity_main = new DueTommorowFragment();

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

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// do nothing here! no call to super.restoreState(arg0, arg1);
	}

}
