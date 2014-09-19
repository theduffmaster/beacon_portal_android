package com.bernard.beaconportal.activities.helper;

import android.content.Context;
import android.util.TypedValue;

import com.bernard.beaconportal.activities.K9;
import com.bernard.beaconportal.activities.activity.misc.ContactPictureLoader;
import com.bernard.beaconportal.activities.R;

public class ContactPicture {

	public static ContactPictureLoader getContactPictureLoader(Context context) {
		final int defaultBgColor;
		if (!K9.isColorizeMissingContactPictures()) {
			TypedValue outValue = new TypedValue();
			context.getTheme().resolveAttribute(
					R.attr.contactPictureFallbackDefaultBackgroundColor,
					outValue, true);
			defaultBgColor = outValue.data;
		} else {
			defaultBgColor = 0;
		}

		return new ContactPictureLoader(context, defaultBgColor);
	}
}
