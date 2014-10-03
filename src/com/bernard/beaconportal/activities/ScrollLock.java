package com.bernard.beaconportal.activities;

import de.timroes.android.listview.EnhancedListView;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import de.timroes.android.listview.EnhancedListView;

public class ScrollLock extends ViewPager {

	public ScrollLock(Context context) {
		super(context);
	}

	public ScrollLock(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ViewPager) {
			if (getChildAt(getCurrentItem()) != null) {
				// get the ListView of current Fragment
				EnhancedListView enhancedListView = (EnhancedListView) getChildAt(
						getCurrentItem()).findViewById(R.id.listView1);
				// If the user is in first page and tries to swipe left, enable
				// the ListView swipe
				if (getCurrentItem() == 0 && dx > 0) {
					enhancedListView.enableSwipeToDismiss();
				}
				// If the user is in second page and tries to swipe right,
				// enable the ListView swipe
				else if (getCurrentItem() == 1 && dx < 0) {
					enhancedListView.enableSwipeToDismiss();
				}
				// Block the ListView swipe there by enabling the parent
				// ViewPager swiping
				else {
					enhancedListView.disableSwipeToDismiss();
				}
			}
		}
		return super.canScroll(v, checkV, dx, x, y);
	}

}
