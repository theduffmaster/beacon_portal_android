package com.bernard.beaconportal.activities.activity.setup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bernard.beaconportal.activities.FontSizes;
import com.bernard.beaconportal.activities.MAIL;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.MAILPreferenceActivity;

/**
 * Activity to configure the font size of the information displayed in the
 * account list, folder list, message list and in the message view.
 * 
 * @see FontSizes
 */
public class FontSizeSettings extends MAILPreferenceActivity {
	/*
	 * Keys of the preferences defined in res/xml/font_preferences.xml
	 */
	private static final String PREFERENCE_ACCOUNT_NAME_FONT = "account_name_font";
	private static final String PREFERENCE_ACCOUNT_DESCRIPTION_FONT = "account_description_font";
	private static final String PREFERENCE_FOLDER_NAME_FONT = "folder_name_font";
	private static final String PREFERENCE_FOLDER_STATUS_FONT = "folder_status_font";
	private static final String PREFERENCE_MESSAGE_LIST_SUBJECT_FONT = "message_list_subject_font";
	private static final String PREFERENCE_MESSAGE_LIST_SENDER_FONT = "message_list_sender_font";
	private static final String PREFERENCE_MESSAGE_LIST_DATE_FONT = "message_list_date_font";
	private static final String PREFERENCE_MESSAGE_LIST_PREVIEW_FONT = "message_list_preview_font";
	private static final String PREFERENCE_MESSAGE_VIEW_SENDER_FONT = "message_view_sender_font";
	private static final String PREFERENCE_MESSAGE_VIEW_TO_FONT = "message_view_to_font";
	private static final String PREFERENCE_MESSAGE_VIEW_CC_FONT = "message_view_cc_font";
	private static final String PREFERENCE_MESSAGE_VIEW_ADDITIONAL_HEADERS_FONT = "message_view_additional_headers_font";
	private static final String PREFERENCE_MESSAGE_VIEW_SUBJECT_FONT = "message_view_subject_font";
	private static final String PREFERENCE_MESSAGE_VIEW_DATE_FONT = "message_view_date_font";
	private static final String PREFERENCE_MESSAGE_VIEW_CONTENT_FONT_SLIDER = "message_view_content_font_slider";
	private static final String PREFERENCE_MESSAGE_COMPOSE_INPUT_FONT = "message_compose_input_font";

	private ListPreference mAccountName;
	private ListPreference mAccountDescription;
	private ListPreference mFolderName;
	private ListPreference mFolderStatus;
	private ListPreference mMessageListSubject;
	private ListPreference mMessageListSender;
	private ListPreference mMessageListDate;
	private ListPreference mMessageListPreview;
	private ListPreference mMessageViewSender;
	private ListPreference mMessageViewTo;
	private ListPreference mMessageViewCC;
	private ListPreference mMessageViewAdditionalHeaders;
	private ListPreference mMessageViewSubject;
	private ListPreference mMessageViewDate;
	private SliderPreference mMessageViewContentSlider;
	private ListPreference mMessageComposeInput;

	private static final int FONT_PERCENT_MIN = 40;
	private static final int FONT_PERCENT_MAX = 250;

	/**
	 * Start the FontSizeSettings activity.
	 * 
	 * @param context
	 *            The application context.
	 */
	public static void actionEditSettings(Context context) {
		Intent i = new Intent(context, FontSizeSettings.class);
		context.startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");

		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(getResources().getColor((R.color.white)));

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#4285f4")));
			
			if (Build.VERSION.SDK_INT >= 21) {
	            Window window = getWindow();
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	            window.setStatusBarColor(Color.parseColor("#3367d6"));
	}

		} else {

			String actionbar_colors = sharedpref.getString("actionbar_color",
					null);

			getActionBar().setBackgroundDrawable(

			new ColorDrawable(Color.parseColor(actionbar_colors)));

			final int splitBarId = getResources().getIdentifier(
					"split_action_bar", "id", "android");

			final View splitActionBar = findViewById(splitBarId);

			if (splitActionBar != null) {

				splitActionBar.setBackgroundDrawable(

				new ColorDrawable(Color.parseColor(actionbar_colors)));

			}

			if (Build.VERSION.SDK_INT >= 21) {
	            Window window = getWindow();
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	            window.setStatusBarColor(Color.parseColor(actionbar_colors));
	}
			
		}

		android.app.ActionBar bar = getActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

		FontSizes fontSizes = MAIL.getFontSizes();
		addPreferencesFromResource(R.xml.font_preferences);

		mAccountName = setupListPreference(PREFERENCE_ACCOUNT_NAME_FONT,
				Integer.toString(fontSizes.getAccountName()));
		mAccountDescription = setupListPreference(
				PREFERENCE_ACCOUNT_DESCRIPTION_FONT,
				Integer.toString(fontSizes.getAccountDescription()));

		mFolderName = setupListPreference(PREFERENCE_FOLDER_NAME_FONT,
				Integer.toString(fontSizes.getFolderName()));
		mFolderStatus = setupListPreference(PREFERENCE_FOLDER_STATUS_FONT,
				Integer.toString(fontSizes.getFolderStatus()));

		mMessageListSubject = setupListPreference(
				PREFERENCE_MESSAGE_LIST_SUBJECT_FONT,
				Integer.toString(fontSizes.getMessageListSubject()));
		mMessageListSender = setupListPreference(
				PREFERENCE_MESSAGE_LIST_SENDER_FONT,
				Integer.toString(fontSizes.getMessageListSender()));
		mMessageListDate = setupListPreference(
				PREFERENCE_MESSAGE_LIST_DATE_FONT,
				Integer.toString(fontSizes.getMessageListDate()));
		mMessageListPreview = setupListPreference(
				PREFERENCE_MESSAGE_LIST_PREVIEW_FONT,
				Integer.toString(fontSizes.getMessageListPreview()));

		mMessageViewSender = setupListPreference(
				PREFERENCE_MESSAGE_VIEW_SENDER_FONT,
				Integer.toString(fontSizes.getMessageViewSender()));
		mMessageViewTo = setupListPreference(PREFERENCE_MESSAGE_VIEW_TO_FONT,
				Integer.toString(fontSizes.getMessageViewTo()));
		mMessageViewCC = setupListPreference(PREFERENCE_MESSAGE_VIEW_CC_FONT,
				Integer.toString(fontSizes.getMessageViewCC()));
		mMessageViewAdditionalHeaders = setupListPreference(
				PREFERENCE_MESSAGE_VIEW_ADDITIONAL_HEADERS_FONT,
				Integer.toString(fontSizes.getMessageViewAdditionalHeaders()));
		mMessageViewSubject = setupListPreference(
				PREFERENCE_MESSAGE_VIEW_SUBJECT_FONT,
				Integer.toString(fontSizes.getMessageViewSubject()));
		mMessageViewDate = setupListPreference(
				PREFERENCE_MESSAGE_VIEW_DATE_FONT,
				Integer.toString(fontSizes.getMessageViewDate()));

		mMessageViewContentSlider = (SliderPreference) findPreference(PREFERENCE_MESSAGE_VIEW_CONTENT_FONT_SLIDER);

		final String summaryFormat = getString(R.string.font_size_message_view_content_summary);
		final String titleFormat = getString(R.string.font_size_message_view_content_dialog_title);
		mMessageViewContentSlider.setValue(scaleFromInt(fontSizes
				.getMessageViewContentAsPercent()));
		mMessageViewContentSlider
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					// Show the preference value in the preference summary
					// field.
					@Override
					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						final SliderPreference slider = (SliderPreference) preference;
						final Float value = (Float) newValue;
						slider.setSummary(String.format(summaryFormat,
								scaleToInt(value)));
						slider.setDialogTitle(String.format(titleFormat,
								slider.getTitle(), slider.getSummary()));
						if (slider.getDialog() != null) {
							slider.getDialog()
									.setTitle(slider.getDialogTitle());
						}
						return true;
					}
				});
		mMessageViewContentSlider.getOnPreferenceChangeListener()
				.onPreferenceChange(mMessageViewContentSlider,
						mMessageViewContentSlider.getValue());

		mMessageComposeInput = setupListPreference(
				PREFERENCE_MESSAGE_COMPOSE_INPUT_FONT,
				Integer.toString(fontSizes.getMessageComposeInput()));
	}

	/**
	 * Update the global FontSize object and permanently store the (possibly
	 * changed) font size settings.
	 */
	private void saveSettings() {
		FontSizes fontSizes = MAIL.getFontSizes();

		fontSizes.setAccountName(Integer.parseInt(mAccountName.getValue()));
		fontSizes.setAccountDescription(Integer.parseInt(mAccountDescription
				.getValue()));

		fontSizes.setFolderName(Integer.parseInt(mFolderName.getValue()));
		fontSizes.setFolderStatus(Integer.parseInt(mFolderStatus.getValue()));

		fontSizes.setMessageListSubject(Integer.parseInt(mMessageListSubject
				.getValue()));
		fontSizes.setMessageListSender(Integer.parseInt(mMessageListSender
				.getValue()));
		fontSizes.setMessageListDate(Integer.parseInt(mMessageListDate
				.getValue()));
		fontSizes.setMessageListPreview(Integer.parseInt(mMessageListPreview
				.getValue()));

		fontSizes.setMessageViewSender(Integer.parseInt(mMessageViewSender
				.getValue()));
		fontSizes.setMessageViewTo(Integer.parseInt(mMessageViewTo.getValue()));
		fontSizes.setMessageViewCC(Integer.parseInt(mMessageViewCC.getValue()));
		fontSizes.setMessageViewAdditionalHeaders(Integer
				.parseInt(mMessageViewAdditionalHeaders.getValue()));
		fontSizes.setMessageViewSubject(Integer.parseInt(mMessageViewSubject
				.getValue()));
		fontSizes.setMessageViewDate(Integer.parseInt(mMessageViewDate
				.getValue()));
		fontSizes
				.setMessageViewContentAsPercent(scaleToInt(mMessageViewContentSlider
						.getValue()));

		fontSizes.setMessageComposeInput(Integer.parseInt(mMessageComposeInput
				.getValue()));

		SharedPreferences preferences = Preferences.getPreferences(this)
				.getPreferences();
		Editor editor = preferences.edit();
		fontSizes.save(editor);
		editor.commit();
	}

	private int scaleToInt(float sliderValue) {
		return (int) (FONT_PERCENT_MIN + sliderValue
				* (FONT_PERCENT_MAX - FONT_PERCENT_MIN));
	}

	private float scaleFromInt(int value) {
		return (float) (value - FONT_PERCENT_MIN)
				/ (FONT_PERCENT_MAX - FONT_PERCENT_MIN);
	}

	@Override
	public void onBackPressed() {
		saveSettings();
		super.onBackPressed();
	}
}
