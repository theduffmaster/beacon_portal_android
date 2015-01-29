package com.bernard.beaconportal.activities.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.bernard.beaconportal.activities.activity.MAILActivityCommon.MAILActivityMagic;
import com.bernard.beaconportal.activities.activity.misc.SwipeGestureDetector.OnSwipeGestureListener;

public class MAILActivity extends Activity implements MAILActivityMagic {

	private MAILActivityCommon mBase;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		mBase = MAILActivityCommon.newInstance(this);
		super.onCreate(savedInstanceState);

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
