package com.bernard.beaconportal.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import de.timroes.android.listview.EnhancedListView;

public class ScrollLock extends ViewPager {

	private Context pager_context;

	public ScrollLock(Context context) {
		super(context);

		pager_context = context;

	}

	public ScrollLock(Context context, AttributeSet attrs) {
		super(context, attrs);

		pager_context = context;

	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ViewPager) {
			if (getChildAt(getCurrentItem()) != null) {
				// get the ListView of current Fragment
				SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

				EnhancedListView enhancedListView = (EnhancedListView) getChildAt(
						getCurrentItem()).findViewById(R.id.listView1);
				// If the user is in first page and tries to swipe left, enable
				// the ListView swipe
				if (getCurrentItem() == 0 && dx > 0) {
					enhancedListView.enableSwipeToDismiss();
					swipeLayout.setEnabled(false);

					SharedPreferences preferer = pager_context
							.getSharedPreferences("CheckBox_swipe",
									Context.MODE_PRIVATE);

					String checkbox_swipe = preferer.getString("checked", null);

					if (checkbox_swipe != null) {

						if (checkbox_swipe.contains("false")) {

							enhancedListView.disableSwipeToDismiss();

						}

					}

					return true;
				}
				// If the user is in second page and tries to swipe right,
				// enable the ListView swipe
				else if (getCurrentItem() == 1 && dx < 0) {
					enhancedListView.enableSwipeToDismiss();
					swipeLayout.setEnabled(false);

					SharedPreferences preferer = pager_context
							.getSharedPreferences("CheckBox_swipe",
									Context.MODE_PRIVATE);

					String checkbox_swipe = preferer.getString("checked", null);

					if (checkbox_swipe != null) {

						if (checkbox_swipe.contains("false")) {

							enhancedListView.disableSwipeToDismiss();

						}

					}

					return true;
				}
				// Block the ListView swipe there by enabling the parent
				// ViewPager swiping
				else {
					enhancedListView.disableSwipeToDismiss();

					swipeLayout.setEnabled(true);

				}
			}
		}
		return super.canScroll(v, checkV, dx, x, y);
	}

}
