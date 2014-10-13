package com.bernard.beaconportal.activities;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.astuetz.PagerSlidingTabStrip;
import com.bernard.beaconportal.activities.R;

import de.timroes.android.listview.EnhancedListView;

public class FragmentsHomeworkDue extends SherlockFragment {

	private String background_colors;

	private Context context;
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.viewpager_main, container, false);

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

		ScrollLock pager = (ScrollLock) view
				.findViewById(R.id.viewPager);

		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.homeworkdue_container);

		layout.setBackgroundColor(Color.parseColor(background_colors));

		Calendar calendar = Calendar.getInstance();
	     
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		
		if(i == 6 || i== 7 || i==1){

			String currHour = new SimpleDateFormat("kk").format(new Date());
			
			if(Integer.parseInt(currHour) > 14){
				
				pager.setAdapter(new ViewPagerAdapterHomeworkAfterThreeWeekend(getChildFragmentManager()));
				
			}else{
			
				pager.setAdapter(new ViewPagerAdapterHomeworkWeekend(getChildFragmentManager()));

			}		
			
		}else{

			String currHour = new SimpleDateFormat("kk").format(new Date());
			
			if(Integer.parseInt(currHour) > 14){
				
				pager.setAdapter(new ViewPagerAdapterHomeworkAfterThree(getChildFragmentManager()));
				
			}else{
			
				pager.setAdapter(new ViewPagerAdapterHomework(getChildFragmentManager()));

			}

		}
		
		
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.pagerTabStrip);
		tabs.setViewPager(pager);
		
		return view;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		ScrollLock pager = (ScrollLock) view
				.findViewById(R.id.viewPager);

		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.homeworkdue_container);

		layout.setBackgroundColor(Color.parseColor(background_colors));

		Calendar calendar = Calendar.getInstance();
	     
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		
		if(i == 6 || i== 7 || i==1){

			String currHour = new SimpleDateFormat("kk").format(new Date());
			
			if(Integer.parseInt(currHour) > 14){
				
				pager.setAdapter(new ViewPagerAdapterHomeworkAfterThreeWeekend(getChildFragmentManager()));
				
			}else{
			
				pager.setAdapter(new ViewPagerAdapterHomeworkWeekend(getChildFragmentManager()));

			}		
			
		}else{

			String currHour = new SimpleDateFormat("kk").format(new Date());
			
			if(Integer.parseInt(currHour) > 14){
				
				pager.setAdapter(new ViewPagerAdapterHomeworkAfterThree(getChildFragmentManager()));
				
			}else{
			
				pager.setAdapter(new ViewPagerAdapterHomework(getChildFragmentManager()));

			}

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

}