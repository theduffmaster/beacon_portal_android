package com.bernard.beaconportal.activities;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;

public class FragmentsHomeworkDue extends Fragment {

	private String actionbar_colors, background_colors;

	private Context context;

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.viewpager_main, container, false);

		System.out.println("create");

		setHasOptionsMenu(true);

		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();

		actionBar.setElevation(0);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.pagerTabStrip);

		SharedPreferences sharedprefer = getActivity().getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		if (!sharedprefer.contains("actionbar_color")) {

			actionbar_colors = "#4285f4";

			tabs.setBackgroundColor(Color.parseColor(actionbar_colors));

		} else {

			actionbar_colors = sharedprefer.getString("actionbar_color", null);

			tabs.setBackgroundColor(Color.parseColor(actionbar_colors));

		}

		SharedPreferences sharedpreference = getActivity()
				.getSharedPreferences("background_color", Context.MODE_PRIVATE);

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

		ScrollLock pager = (ScrollLock) view.findViewById(R.id.viewPager);

		Calendar calendar = Calendar.getInstance();

		int i = calendar.get(Calendar.DAY_OF_WEEK);

		if (i == 6 || i == 7 || i == 1) {

			pager.setAdapter(new ViewPagerAdapterHomeworkWeekend(
					getChildFragmentManager()));

		} else {

			String currHour = new SimpleDateFormat("kk").format(new Date());

			if (Integer.parseInt(currHour) > 14
					&& Integer.parseInt(currHour) < 24) {

				pager.setAdapter(new ViewPagerAdapterHomeworkAfterThree(
						getChildFragmentManager()));
				
				tabs.setIndicatorColor(Color.parseColor(actionbar_colors));

			} else {

				pager.setAdapter(new ViewPagerAdapterHomework(
						getChildFragmentManager()));
				
			}

		}

		tabs.setViewPager(pager);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		System.out.println("resume");

		ScrollLock pager = (ScrollLock) view.findViewById(R.id.viewPager);

		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.homeworkdue_container);

		layout.setBackgroundColor(Color.parseColor(actionbar_colors));

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.pagerTabStrip);

		tabs.setDividerColor(Color.parseColor(actionbar_colors));

		tabs.setBackgroundColor(Color.parseColor(actionbar_colors));

		Calendar calendar = Calendar.getInstance();

		int i = calendar.get(Calendar.DAY_OF_WEEK);

		if (i == 6 || i == 7 || i == 1) {

			pager.setAdapter(new ViewPagerAdapterHomeworkWeekend(
					getChildFragmentManager()));

		} else {

			String currHour = new SimpleDateFormat("kk").format(new Date());

			if (Integer.parseInt(currHour) > 14
					&& Integer.parseInt(currHour) < 24) {

				pager.setAdapter(new ViewPagerAdapterHomeworkAfterThree(
						getChildFragmentManager()));

			} else {

				pager.setAdapter(new ViewPagerAdapterHomework(
						getChildFragmentManager()));

			}

		}

		SharedPreferences sharedprefer = getActivity().getSharedPreferences(
				"first_run_starts", Context.MODE_PRIVATE);

		if (!sharedprefer.contains("help_check_homeworkdue")) {

			alert_help();

		}

	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	private void alert_help() {

		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(
					"Swipe down to refresh, and keep track of the homework you've done by swiping it away. You can turn off swipeable homework items in options. The number in the navigation drawer is the amount of homework you have due tommorow.")
					.setTitle("About");

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {

							SharedPreferences.Editor localEditors = getActivity()
									.getSharedPreferences("first_run_starts",
											Context.MODE_PRIVATE).edit();

							localEditors.putString("help_check_homeworkdue",
									"checked");

							localEditors.commit();

						}
					});

			AlertDialog alertDialog = builder.create();

			alertDialog.show();

		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		inflater.inflate(R.menu.android_help, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		case R.id.help:

			alert_help();

			return true;

		}
		return true;

	}

}
