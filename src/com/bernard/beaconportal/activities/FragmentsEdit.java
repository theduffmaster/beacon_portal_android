package com.bernard.beaconportal.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.bernard.beaconportal.activities.R;

public class FragmentsEdit extends FragmentActivity {

	private ViewPager pager;
	private ViewPagerAdapterSchedule adapter;
	private PagerSlidingTabStrip tabs;
	String uriString;

	private String background_colors, actionbar_colors;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewpager_schedule_edit);

		SharedPreferences sharedprefer = getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.pagerTabStrip1); 
		
		if (!sharedprefer.contains("actionbar_color")) {

			actionbar_colors = "#1976D2";

			RelativeLayout layout = (RelativeLayout) 
					findViewById(R.id.view_container);

			layout.setBackgroundColor(Color.parseColor(actionbar_colors));
			
			tabs.setDividerColor(Color.parseColor(actionbar_colors));

		} else {

			actionbar_colors = sharedprefer
					.getString("actionbar_color", null);
			
			tabs.setDividerColor(Color.parseColor(actionbar_colors));

		}
		
		SharedPreferences sharedpreference = getSharedPreferences(
				"background_color", Context.MODE_PRIVATE);
		
		if (!sharedpreference.contains("background_color")) {

			background_colors = "#ffffff";

			tabs.setTextColor(Color.parseColor(background_colors));
			
			tabs.setIndicatorColor(Color.parseColor(background_colors));

		} else {

			background_colors = sharedpreference
					.getString("background_color", "#ffffff");
			
			tabs.setTextColor(Color.parseColor(background_colors));
			
			tabs.setIndicatorColor(Color.parseColor(background_colors));

		}

		ViewPager pager = (ViewPager) findViewById(R.id.viewPager1);

		RelativeLayout layout = (RelativeLayout) 
				findViewById(R.id.view_container);

		layout.setBackgroundColor(Color.parseColor(actionbar_colors));

		pager.setAdapter(new ViewPagerAdapterScheduleView(
				getSupportFragmentManager()));
		
		tabs.setViewPager(pager);

		String weekDay;
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

		Calendar calendar = Calendar.getInstance();
		weekDay = dayFormat.format(calendar.getTime());

		System.out.println(weekDay);

		if (weekDay.contains("Monday")) {
			pager.setCurrentItem(0);

		}
		if (weekDay.contains("Tuesday")) {
			pager.setCurrentItem(1);

		}

		if (weekDay.contains("Wednesday")) {
			pager.setCurrentItem(2);

		}
		if (weekDay.contains("Thursday")) {
			pager.setCurrentItem(3);

		}
		if (weekDay.contains("Friday")) {
			pager.setCurrentItem(4);

		}

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#1976D2")));

		} else {

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			getActionBar().setBackgroundDrawable(

new ColorDrawable(Color.parseColor(actionbar_colors)));


final int splitBarId = getResources().getIdentifier("split_action_bar", "id", "android");

    final View splitActionBar = findViewById(splitBarId);

    if (splitActionBar != null) {

       

    splitActionBar.setBackgroundDrawable(

new ColorDrawable(Color.parseColor(actionbar_colors)));


    }

		}

		android.app.ActionBar bar = getActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));
		bar.setTitle("Edit Schedule");

		pager = (ViewPager) findViewById(R.id.viewPager1);

		layout.setBackgroundColor(Color.parseColor(actionbar_colors));
		adapter = new ViewPagerAdapterSchedule(getSupportFragmentManager());

		tabs = (PagerSlidingTabStrip) findViewById(R.id.pagerTabStrip1);

		pager.setAdapter(adapter);

		tabs.setViewPager(pager);

		pager.setOffscreenPageLimit(4);

	}

	public class ViewPagerAdapterSchedule extends FragmentPagerAdapter {

		// Declare the number of ViewPager pages
		final int PAGE_COUNT = 5;
		private String titles[] = new String[] { "Monday", "Tuesday",
				"Wednesday", "Thursday", "Friday" };

		public ViewPagerAdapterSchedule(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {

			// Open FragmentTab1.java
			case 0:
				Monday schedule_list_view = new Monday();
				return schedule_list_view;

				// Open FragmentTab2.java
			case 1:
				Tuesday schedule_list_view1 = new Tuesday();
				return schedule_list_view1;

			case 2:
				Wednesday schedule_list_view2 = new Wednesday();
				return schedule_list_view2;

			case 3:
				Thursday schedule_list_view3 = new Thursday();
				return schedule_list_view3;

			case 4:
				Friday schedule_list_view4 = new Friday();
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

	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu)

	{
		getMenuInflater().inflate(R.menu.android_apply, paramMenu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case R.id.apply:
			Log.d("sender", "clicked!");
			Intent localIntent = new Intent("clicked!");
			LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
		case R.id.undo:

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);

			return true;
		default:
			return super.onOptionsItemSelected(paramMenuItem);

		}

	}

}
