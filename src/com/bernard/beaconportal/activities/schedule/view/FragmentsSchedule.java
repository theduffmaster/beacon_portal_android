package com.bernard.beaconportal.activities.schedule.view;

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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStripMargins;
import com.bernard.beaconportal.activities.MainActivity;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.R.array;
import com.bernard.beaconportal.activities.R.drawable;
import com.bernard.beaconportal.activities.R.id;
import com.bernard.beaconportal.activities.R.layout;
import com.bernard.beaconportal.activities.R.menu;
import com.bernard.beaconportal.activities.R.string;
import com.bernard.beaconportal.activities.schedule.edit.FragmentsEdit;
import com.bernard.beaconportal.activities.schedule.linked.FragmentsLinked;
import com.faizmalkani.floatingactionbutton.Fab;

public class FragmentsSchedule extends Fragment {

	private String actionbar_colors, background_colors;

	private Fab mFab;

	private String checkbox_edit;

	private ViewPager pager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		final View view = inflater.inflate(R.layout.viewpager_schedule,
				container, false);

		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();

		actionBar.setElevation(0);

		SharedPreferences sharedprefer = getActivity().getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		PagerSlidingTabStripMargins tabs = (PagerSlidingTabStripMargins) view
				.findViewById(R.id.pagerTabStrip1);

		if (!sharedprefer.contains("actionbar_color")) {

			actionbar_colors = "#4285f4";

			tabs.setDividerColor(Color.parseColor(actionbar_colors));

			tabs.setBackgroundColor(Color.parseColor(actionbar_colors));

		} else {

			actionbar_colors = sharedprefer.getString("actionbar_color", null);

			tabs.setDividerColor(Color.parseColor(actionbar_colors));

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

		pager = (ViewPager) view.findViewById(R.id.viewPager1);

		pager.setAdapter(new ViewPagerAdapterScheduleView(
				getChildFragmentManager()));

		tabs.setViewPager(pager);

		int margin_dpi = convertDip2Pixels(getActivity(), 24);

		pager.setPageMargin(margin_dpi);

		ColorDrawable inbetween = new ColorDrawable(0xFFeeeeee);

		pager.setPageMarginDrawable(inbetween);

		mFab = (Fab) view.findViewById(R.id.fabbutton);

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			mFab.setFabColor(Color.parseColor("#4285f4"));

		} else {

			String actionbar_colors = sharedpref.getString("actionbar_color",
					null);

			mFab.setFabColor(Color.parseColor(actionbar_colors));

		}

		mFab.setFabDrawable(getResources().getDrawable(
				R.drawable.ic_action_edit));

		mFab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				alert_dialog();

			}

		});

		SharedPreferences prefer = getActivity().getSharedPreferences(
				"CheckBox_edit", Context.MODE_PRIVATE);

		checkbox_edit = prefer.getString("checked", null);

		if (checkbox_edit != null) {
			if (checkbox_edit.contains("true")) {

				mFab.setVisibility(View.GONE);
			}
		}

		return view;
	}

	public void onResume() {
		super.onResume();

		String weekDay;
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

		Calendar calendar = Calendar.getInstance();
		weekDay = dayFormat.format(calendar.getTime());

		System.out.println(weekDay);

		if (weekDay.contains("Monday")) {
			pager.setCurrentItem(0, true);

		}
		if (weekDay.contains("Tuesday")) {
			pager.setCurrentItem(1, true);

		}

		if (weekDay.contains("Wednesday")) {
			pager.setCurrentItem(2, true);

		}
		if (weekDay.contains("Thursday")) {
			pager.setCurrentItem(3, true);

		}
		if (weekDay.contains("Friday")) {
			pager.setCurrentItem(4, true);

		}

	}

	public static int convertDip2Pixels(Context context, int dip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, context.getResources().getDisplayMetrics());
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

		if (checkbox_edit != null) {
			if (checkbox_edit.contains("true")) {

				inflater.inflate(R.menu.android_edit, menu);
			}
		}

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

			if (checkbox_edit != null) {
				if (checkbox_edit.contains("true")) {

					alert_dialog();

				}
			}

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

			builder.setTitle("Edit Schedule Items Using...");

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
