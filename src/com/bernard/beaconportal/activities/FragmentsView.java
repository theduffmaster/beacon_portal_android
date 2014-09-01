package com.bernard.beaconportal.activities;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.astuetz.PagerSlidingTabStrip;
import com.bernard.beaconportal.activities.R;

public class FragmentsView extends SherlockFragment {

	private String background_colors;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		View view = inflater.inflate(R.layout.viewpager_schedule, container,
				false);

		SharedPreferences sharedprefer = getActivity().getSharedPreferences(
				"background_color", Context.MODE_PRIVATE);

		if (!sharedprefer.contains("background_color")) {

			background_colors = "#ffffff";

			RelativeLayout layout = (RelativeLayout) view
					.findViewById(R.id.view_container);

			layout.setBackgroundColor(Color.parseColor(background_colors));

		} else {

			background_colors = sharedprefer
					.getString("background_color", null);

		}

		ViewPager pager = (ViewPager) view.findViewById(R.id.viewPager1);

		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.view_container);

		layout.setBackgroundColor(Color.parseColor(background_colors));

		pager.setAdapter(new ViewPagerAdapterScheduleView(
				getChildFragmentManager()));
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.pagerTabStrip1);

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

		return view;
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.android_edit, menu);
		inflater.inflate(R.menu.android_help, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {

		case R.id.help:

			alert_help();

			return true;

		case R.id.edit:
			alert_dialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void alert_help() {

		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.help).setTitle("About");

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {

						}
					});

			AlertDialog alertDialog = builder.create();

			alertDialog.show();

		}

	}

	private void alert_dialog() {
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setItems(R.array.edit_mode,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg) {
							// TODO Auto-generated method stub
							switch (arg) {
							case 0:
								Intent myIntent = new Intent(((Dialog) dialog)
										.getContext(), FragmentsLinked.class);
								startActivityForResult(myIntent, 0);
								break;
							case 1:
								Intent anIntent = new Intent(((Dialog) dialog)
										.getContext(), FragmentsEdit.class);
								startActivityForResult(anIntent, 0);
								break;
							default:
								break;
							}
						}

					});
			AlertDialog alertDialog = builder.create();

			alertDialog.show();

		}
	}

}
