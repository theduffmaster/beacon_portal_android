package com.bernard.beaconportal.activities.helper;

import android.content.Context;
import android.util.TypedValue;

import com.bernard.beaconportal.activities.MAIL;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.misc.ContactPictureLoader;

public class ContactPicture {

	public static ContactPictureLoader getContactPictureLoader(Context context) {
		final int defaultBgColor;
		if (!MAIL.isColorizeMissingContactPictures()) {
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
