package com.bernard.beaconportal.activities;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.astuetz.PagerSlidingTabStrip;
import com.bernard.beaconportal.activities.R;

public class FragmentsHomeworkDue extends SherlockFragment {

	private String background_colors;

	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.viewpager_main, container, false);

		SharedPreferences sharedprefer = getActivity().getSharedPreferences(
				"background_color", Context.MODE_PRIVATE);

		if (!sharedprefer.contains("background_color")) {

			background_colors = "#ffffff";

			RelativeLayout layout = (RelativeLayout) view
					.findViewById(R.id.homeworkdue_container);

			layout.setBackgroundColor(Color.parseColor(background_colors));

		} else {

			background_colors = sharedprefer
					.getString("background_color", null);

		}

		final ViewPager pager = (ViewPager) view.findViewById(R.id.viewPager);

		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.homeworkdue_container);

		layout.setBackgroundColor(Color.parseColor(background_colors));

		pager.setAdapter(new ViewPagerAdapterHomework(getChildFragmentManager()));

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.pagerTabStrip);
		tabs.setViewPager(pager);

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

}
