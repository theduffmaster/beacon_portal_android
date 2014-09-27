package com.bernard.beaconportal.activities;

import android.content.Context;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ListView;

public class ScrollLock extends ViewPager {
	public ScrollLock(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ListView) {
			return (false);
		}

		return (super.canScroll(v, checkV, dx, x, y));
	}
}