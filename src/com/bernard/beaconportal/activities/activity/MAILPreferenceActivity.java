package com.bernard.beaconportal.activities.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bernard.beaconportal.activities.MAIL;

public class MAILPreferenceActivity extends PreferenceActivity {

	private String actionbar_colors;

	@Override
	public void onCreate(Bundle icicle) {
		MAILActivityCommon.setLanguage(this, MAIL.getMAILLanguage());

		if (Build.VERSION.SDK_INT >= 6 && Build.VERSION.SDK_INT < 14) {
			// There's a display bug in all supported Android versions before
			// 4.0 (SDK 14) which
			// causes PreferenceScreens to have a black background.
			// http://code.google.com/p/android/issues/detail?id=4611
			setTheme(MAIL.getMAILThemeResourceId(MAIL.Theme.DARK));
		} else {
			setTheme(MAIL.getMAILThemeResourceId());
		}

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

		super.onCreate(icicle);
	}

	/**
	 * Set up the {@link ListPreference} instance identified by {@code key}.
	 * 
	 * @param key
	 *            The key of the {@link ListPreference} object.
	 * @param value
	 *            Initial value for the {@link ListPreference} object.
	 * 
	 * @return The {@link ListPreference} instance identified by {@code key}.
	 */
	protected ListPreference setupListPreference(final String key,
			final String value) {
		final ListPreference prefView = (ListPreference) findPreference(key);
		prefView.setValue(value);
		prefView.setSummary(prefView.getEntry());
		prefView.setOnPreferenceChangeListener(new PreferenceChangeListener(
				prefView));
		return prefView;
	}

	/**
	 * Initialize a given {@link ListPreference} instance.
	 * 
	 * @param prefView
	 *            The {@link ListPreference} instance to initialize.
	 * @param value
	 *            Initial value for the {@link ListPreference} object.
	 * @param entries
	 *            Sets the human-readable entries to be shown in the list.
	 * @param entryValues
	 *            The array to find the value to save for a preference when an
	 *            entry from entries is selected.
	 */
	protected void initListPreference(final ListPreference prefView,
			final String value, final CharSequence[] entries,
			final CharSequence[] entryValues) {
		prefView.setEntries(entries);
		prefView.setEntryValues(entryValues);
		prefView.setValue(value);
		prefView.setSummary(prefView.getEntry());
		prefView.setOnPreferenceChangeListener(new PreferenceChangeListener(
				prefView));
	}

	/**
	 * This class handles value changes of the {@link ListPreference} objects.
	 */
	private static class PreferenceChangeListener implements
			Preference.OnPreferenceChangeListener {
		private ListPreference mPrefView;

		private PreferenceChangeListener(final ListPreference prefView) {
			this.mPrefView = prefView;
		}

		/**
		 * Show the preference value in the preference summary field.
		 */
		@Override
		public boolean onPreferenceChange(final Preference preference,
				final Object newValue) {
			final String summary = newValue.toString();
			final int index = mPrefView.findIndexOfValue(summary);
			mPrefView.setSummary(mPrefView.getEntries()[index]);
			mPrefView.setValue(summary);
			return false;
		}
	}
}
