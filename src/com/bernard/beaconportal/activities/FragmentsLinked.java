package com.bernard.beaconportal.activities;

import com.bernard.beaconportal.activities.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class FragmentsLinked extends FragmentActivity {

	private ViewPager pager;
	private ViewPagerAdapterScheduleView adapter;
	private String background_colors, actionbar_colors;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#298ccd")));

		} else {

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor(actionbar_colors)));

		}

		android.app.ActionBar bar = getActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));
		bar.setTitle("Batch Edit");

		setContentView(R.layout.viewpager_schedule_linked);

		pager = (ViewPager) findViewById(R.id.viewPager2);

		adapter = new ViewPagerAdapterScheduleView(getSupportFragmentManager());

		// Bind the tabs to the ViewPager

		pager.setAdapter(adapter);

	}

	public class ViewPagerAdapterScheduleView extends FragmentPagerAdapter {

		// Declare the number of ViewPager pages
		final int PAGE_COUNT = 1;
		private String titles[] = new String[] { "Linked Bands" };

		public ViewPagerAdapterScheduleView(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {

			// Open FragmentTab1.java
			case 0:
				Bands_Linked schedule_list_view = new Bands_Linked();
				return schedule_list_view;

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

}
