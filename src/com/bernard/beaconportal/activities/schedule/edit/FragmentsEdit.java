package com.bernard.beaconportal.activities.schedule.edit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.bernard.beaconportal.activities.MainActivity;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.R.id;
import com.bernard.beaconportal.activities.R.layout;
import com.bernard.beaconportal.activities.R.menu;
import com.bernard.beaconportal.activities.schedule.view.ViewPagerAdapterScheduleView;

public class FragmentsEdit extends ActionBarActivity {

	private ViewPagerAdapterSchedule adapter;
	String uriString;

	private String background_colors, actionbar_colors;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewpager_schedule_edit);

		SharedPreferences sharedprefer = getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		ActionBar actionBar = getSupportActionBar();

		actionBar.setElevation(0);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.pagerTabStrip1);

		if (!sharedprefer.contains("actionbar_color")) {

			actionbar_colors = "#4285f4";

			RelativeLayout layout = (RelativeLayout) findViewById(R.id.view_container);

			layout.setBackgroundColor(Color.parseColor(actionbar_colors));

			tabs.setDividerColor(Color.parseColor(actionbar_colors));

			if (Build.VERSION.SDK_INT >= 21) {
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(Color.parseColor("#3367d6"));
			}

		} else {

			actionbar_colors = sharedprefer.getString("actionbar_color", null);

			tabs.setDividerColor(Color.parseColor(actionbar_colors));

			if (Build.VERSION.SDK_INT >= 21) {
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(Color.parseColor(actionbar_colors));
			}

		}

		SharedPreferences sharedpreference = getSharedPreferences(
				"background_color", Context.MODE_PRIVATE);

		if (!sharedpreference.contains("background_color")) {

			background_colors = "#ffffff";

			tabs.setTextColor(Color.parseColor(background_colors));

			tabs.setIndicatorColor(Color.parseColor(background_colors));

		} else {

			background_colors = sharedpreference.getString("background_color",
					"#ffffff");

			tabs.setTextColor(Color.parseColor(background_colors));

			tabs.setIndicatorColor(Color.parseColor(background_colors));

		}

		ViewPager pager = (ViewPager) findViewById(R.id.viewPager1);

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.view_container);

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

		ActionBar bar = getSupportActionBar();

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

		int margin_dpi = convertDip2Pixels(FragmentsEdit.this, 32);

		pager.setPageMargin(margin_dpi);

		ColorDrawable inbetween = new ColorDrawable(0xFFeeeeee);

		pager.setPageMarginDrawable(inbetween);

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
				MondayEdit schedule_list_view = new MondayEdit();
				return schedule_list_view;

				// Open FragmentTab2.java
			case 1:
				TuesdayEdit schedule_list_view1 = new TuesdayEdit();
				return schedule_list_view1;

			case 2:
				WednesdayEdit schedule_list_view2 = new WednesdayEdit();
				return schedule_list_view2;

			case 3:
				ThursdayEdit schedule_list_view3 = new ThursdayEdit();
				return schedule_list_view3;

			case 4:
				FridayEdit schedule_list_view4 = new FridayEdit();
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

	public static int convertDip2Pixels(Context context, int dip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, context.getResources().getDisplayMetrics());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu paramMenu)

	{
		getMenuInflater().inflate(R.menu.android_apply_all, paramMenu);

		return true;
	}

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
