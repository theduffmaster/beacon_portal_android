package com.bernard.beaconportal.activities.activity.setup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.K9;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.FolderInfoHolder;
import com.bernard.beaconportal.activities.activity.K9PreferenceActivity;
import com.bernard.beaconportal.activities.mail.Folder;
import com.bernard.beaconportal.activities.mail.Folder.FolderClass;
import com.bernard.beaconportal.activities.mail.MessagingException;
import com.bernard.beaconportal.activities.mail.Store;
import com.bernard.beaconportal.activities.mail.store.LocalStore;
import com.bernard.beaconportal.activities.mail.store.LocalStore.LocalFolder;
import com.bernard.beaconportal.activities.service.MailService;

public class FolderSettings extends K9PreferenceActivity {

	private static final String EXTRA_FOLDER_NAME = "com.bernard.beaconportal.activities.folderName";
	private static final String EXTRA_ACCOUNT = "com.bernard.beaconportal.activities.account";

	private static final String PREFERENCE_TOP_CATERGORY = "folder_settings";
	private static final String PREFERENCE_DISPLAY_CLASS = "folder_settings_folder_display_mode";
	private static final String PREFERENCE_SYNC_CLASS = "folder_settings_folder_sync_mode";
	private static final String PREFERENCE_PUSH_CLASS = "folder_settings_folder_push_mode";
	private static final String PREFERENCE_IN_TOP_GROUP = "folder_settings_in_top_group";
	private static final String PREFERENCE_INTEGRATE = "folder_settings_include_in_integrated_inbox";

	private LocalFolder mFolder;

	private CheckBoxPreference mInTopGroup;
	private CheckBoxPreference mIntegrate;
	private ListPreference mDisplayClass;
	private ListPreference mSyncClass;
	private ListPreference mPushClass;

	public static void actionSettings(Context context, Account account,
			String folderName) {
		Intent i = new Intent(context, FolderSettings.class);
		i.putExtra(EXTRA_FOLDER_NAME, folderName);
		i.putExtra(EXTRA_ACCOUNT, account.getUuid());
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
					new ColorDrawable(Color.parseColor("#1976D2")));

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

		}

		android.app.ActionBar bar = getActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

		String folderName = (String) getIntent().getSerializableExtra(
				EXTRA_FOLDER_NAME);
		String accountUuid = getIntent().getStringExtra(EXTRA_ACCOUNT);
		Account mAccount = Preferences.getPreferences(this).getAccount(
				accountUuid);

		try {
			LocalStore localStore = mAccount.getLocalStore();
			mFolder = localStore.getFolder(folderName);
			mFolder.open(Folder.OPEN_MODE_RW);
		} catch (MessagingException me) {
			Log.e(K9.LOG_TAG, "Unable to edit folder " + folderName
					+ " preferences", me);
			return;
		}

		boolean isPushCapable = false;
		Store store = null;
		try {
			store = mAccount.getRemoteStore();
			isPushCapable = store.isPushCapable();
		} catch (Exception e) {
			Log.e(K9.LOG_TAG, "Could not get remote store", e);
		}

		addPreferencesFromResource(R.xml.folder_settings_preferences);

		String displayName = FolderInfoHolder.getDisplayName(this, mAccount,
				mFolder.getName());
		Preference category = findPreference(PREFERENCE_TOP_CATERGORY);
		category.setTitle(displayName);

		mInTopGroup = (CheckBoxPreference) findPreference(PREFERENCE_IN_TOP_GROUP);
		mInTopGroup.setChecked(mFolder.isInTopGroup());
		mIntegrate = (CheckBoxPreference) findPreference(PREFERENCE_INTEGRATE);
		mIntegrate.setChecked(mFolder.isIntegrate());

		mDisplayClass = (ListPreference) findPreference(PREFERENCE_DISPLAY_CLASS);
		mDisplayClass.setValue(mFolder.getDisplayClass().name());
		mDisplayClass.setSummary(mDisplayClass.getEntry());
		mDisplayClass
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String summary = newValue.toString();
						int index = mDisplayClass.findIndexOfValue(summary);
						mDisplayClass.setSummary(mDisplayClass.getEntries()[index]);
						mDisplayClass.setValue(summary);
						return false;
					}
				});

		mSyncClass = (ListPreference) findPreference(PREFERENCE_SYNC_CLASS);
		mSyncClass.setValue(mFolder.getRawSyncClass().name());
		mSyncClass.setSummary(mSyncClass.getEntry());
		mSyncClass
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String summary = newValue.toString();
						int index = mSyncClass.findIndexOfValue(summary);
						mSyncClass.setSummary(mSyncClass.getEntries()[index]);
						mSyncClass.setValue(summary);
						return false;
					}
				});

		mPushClass = (ListPreference) findPreference(PREFERENCE_PUSH_CLASS);
		mPushClass.setEnabled(isPushCapable);
		mPushClass.setValue(mFolder.getRawPushClass().name());
		mPushClass.setSummary(mPushClass.getEntry());
		mPushClass
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						final String summary = newValue.toString();
						int index = mPushClass.findIndexOfValue(summary);
						mPushClass.setSummary(mPushClass.getEntries()[index]);
						mPushClass.setValue(summary);
						return false;
					}
				});
	}

	private void saveSettings() throws MessagingException {
		mFolder.setInTopGroup(mInTopGroup.isChecked());
		mFolder.setIntegrate(mIntegrate.isChecked());
		// We call getPushClass() because display class changes can affect push
		// class when push class is set to inherit
		FolderClass oldPushClass = mFolder.getPushClass();
		FolderClass oldDisplayClass = mFolder.getDisplayClass();
		mFolder.setDisplayClass(FolderClass.valueOf(mDisplayClass.getValue()));
		mFolder.setSyncClass(FolderClass.valueOf(mSyncClass.getValue()));
		mFolder.setPushClass(FolderClass.valueOf(mPushClass.getValue()));

		mFolder.save();

		FolderClass newPushClass = mFolder.getPushClass();
		FolderClass newDisplayClass = mFolder.getDisplayClass();

		if (oldPushClass != newPushClass
				|| (newPushClass != FolderClass.NO_CLASS && oldDisplayClass != newDisplayClass)) {
			MailService.actionRestartPushers(getApplication(), null);
		}
	}

	@Override
	public void onPause() {
		try {
			saveSettings();
		} catch (MessagingException e) {
			Log.e(K9.LOG_TAG, "Saving folder settings failed", e);
		}

		super.onPause();
	}
}
