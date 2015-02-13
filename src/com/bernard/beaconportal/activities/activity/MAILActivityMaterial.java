package com.bernard.beaconportal.activities.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bernard.beaconportal.activities.activity.MAILActivityCommon.MAILActivityMagic;
import com.bernard.beaconportal.activities.activity.misc.SwipeGestureDetector.OnSwipeGestureListener;

public class MAILActivityMaterial extends ActionBarActivity implements
		MAILActivityMagic {

	private MAILActivityCommon mBase;
	
	private String actionbar_colors;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		mBase = MAILActivityCommon.newInstance(this);
		super.onCreate(savedInstanceState);
		
		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		final int splitBarId = getResources().getIdentifier("split_action_bar",
				"id", "android");
		final View splitActionBar = findViewById(splitBarId);

		if (!sharedpref.contains("actionbar_color")) {

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#4285f4")));

			if (splitActionBar != null) {

				splitActionBar.setBackgroundDrawable(new ColorDrawable(Color
						.parseColor("#4285f4")));

			}
			
			if (Build.VERSION.SDK_INT >= 21) {
	            Window window = getWindow();
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	            window.setStatusBarColor(Color.parseColor("#3367d6"));
	}

		} else {

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			getActionBar().setBackgroundDrawable(

			new ColorDrawable(Color.parseColor(actionbar_colors)));

			if (splitActionBar != null) {

				splitActionBar.setBackgroundDrawable(new ColorDrawable(Color
						.parseColor(actionbar_colors)));

			}

			if (Build.VERSION.SDK_INT >= 21) {
	            Window window = getWindow();
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	            window.setStatusBarColor(Color.parseColor("#3367d6"));
	}

		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		mBase.preDispatchTouchEvent(event);
		return super.dispatchTouchEvent(event);
	}

	@Override
	public void setupGestureDetector(OnSwipeGestureListener listener) {
		mBase.setupGestureDetector(listener);
	}
}
