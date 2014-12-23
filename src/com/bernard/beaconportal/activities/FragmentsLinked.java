package com.bernard.beaconportal.activities;

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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class FragmentsLinked extends ActionBarActivity {

	private ViewPager pager;
	private ViewPagerAdapterScheduleView adapter;
	private View underline;
	private String background_colors, actionbar_colors;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar bar = getSupportActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));
		bar.setTitle("Batch Edit");

		bar.setElevation(0);

		setContentView(R.layout.viewpager_schedule_linked);

		pager = (ViewPager) findViewById(R.id.viewPager2);

		adapter = new ViewPagerAdapterScheduleView(getSupportFragmentManager());

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			getSupportActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#4285f4")));

		} else {

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			getSupportActionBar().setBackgroundDrawable(

			new ColorDrawable(Color.parseColor(actionbar_colors)));

			final int splitBarId = getResources().getIdentifier(
					"split_action_bar", "id", "android");

			final View splitActionBar = findViewById(splitBarId);

			if (splitActionBar != null) {

				splitActionBar.setBackgroundDrawable(

				new ColorDrawable(Color.parseColor(actionbar_colors)));

			}

		}

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
