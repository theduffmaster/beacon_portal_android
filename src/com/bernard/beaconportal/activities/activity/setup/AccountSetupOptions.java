package com.bernard.beaconportal.activities.activity.setup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.MAIL;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.MAILActivityMaterial;
import com.bernard.beaconportal.activities.mail.Store;

public class AccountSetupOptions extends MAILActivityMaterial implements
		OnClickListener {
	private static final String EXTRA_ACCOUNT = "account";

	private static final String EXTRA_MAKE_DEFAULT = "makeDefault";

	private Spinner mCheckFrequencyView;

	private Spinner mDisplayCountView;

	private CheckBox mNotifyView;
	private CheckBox mNotifySyncView;
	private CheckBox mPushEnable;

	private Account mAccount;

	public static void actionOptions(Context context, Account account,
			boolean makeDefault) {
		Intent i = new Intent(context, AccountSetupOptions.class);
		i.putExtra(EXTRA_ACCOUNT, account.getUuid());
		i.putExtra(EXTRA_MAKE_DEFAULT, makeDefault);
		context.startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_setup_options);

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

		mCheckFrequencyView = (Spinner) findViewById(R.id.account_check_frequency);
		mDisplayCountView = (Spinner) findViewById(R.id.account_display_count);
		mNotifyView = (CheckBox) findViewById(R.id.account_notify);
		mNotifySyncView = (CheckBox) findViewById(R.id.account_notify_sync);
		mPushEnable = (CheckBox) findViewById(R.id.account_enable_push);

		findViewById(R.id.next).setOnClickListener(this);

		SpinnerOption checkFrequencies[] = {
				new SpinnerOption(
						-1,
						getString(R.string.account_setup_options_mail_check_frequency_never)),
				new SpinnerOption(
						1,
						getString(R.string.account_setup_options_mail_check_frequency_1min)),
				new SpinnerOption(
						5,
						getString(R.string.account_setup_options_mail_check_frequency_5min)),
				new SpinnerOption(
						10,
						getString(R.string.account_setup_options_mail_check_frequency_10min)),
				new SpinnerOption(
						15,
						getString(R.string.account_setup_options_mail_check_frequency_15min)),
				new SpinnerOption(
						30,
						getString(R.string.account_setup_options_mail_check_frequency_30min)),
				new SpinnerOption(
						60,
						getString(R.string.account_setup_options_mail_check_frequency_1hour)),
				new SpinnerOption(
						120,
						getString(R.string.account_setup_options_mail_check_frequency_2hour)),
				new SpinnerOption(
						180,
						getString(R.string.account_setup_options_mail_check_frequency_3hour)),
				new SpinnerOption(
						360,
						getString(R.string.account_setup_options_mail_check_frequency_6hour)),
				new SpinnerOption(
						720,
						getString(R.string.account_setup_options_mail_check_frequency_12hour)),
				new SpinnerOption(
						1440,
						getString(R.string.account_setup_options_mail_check_frequency_24hour)),

		};

		ArrayAdapter<SpinnerOption> checkFrequenciesAdapter = new ArrayAdapter<SpinnerOption>(
				this, android.R.layout.simple_spinner_item, checkFrequencies);
		checkFrequenciesAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCheckFrequencyView.setAdapter(checkFrequenciesAdapter);

		SpinnerOption displayCounts[] = {
				new SpinnerOption(
						10,
						getString(R.string.account_setup_options_mail_display_count_10)),
				new SpinnerOption(
						25,
						getString(R.string.account_setup_options_mail_display_count_25)),
				new SpinnerOption(
						50,
						getString(R.string.account_setup_options_mail_display_count_50)),
				new SpinnerOption(
						100,
						getString(R.string.account_setup_options_mail_display_count_100)),
				new SpinnerOption(
						250,
						getString(R.string.account_setup_options_mail_display_count_250)),
				new SpinnerOption(
						500,
						getString(R.string.account_setup_options_mail_display_count_500)),
				new SpinnerOption(
						1000,
						getString(R.string.account_setup_options_mail_display_count_1000)), };

		ArrayAdapter<SpinnerOption> displayCountsAdapter = new ArrayAdapter<SpinnerOption>(
				this, android.R.layout.simple_spinner_item, displayCounts);
		displayCountsAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDisplayCountView.setAdapter(displayCountsAdapter);

		String accountUuid = getIntent().getStringExtra(EXTRA_ACCOUNT);
		mAccount = Preferences.getPreferences(this).getAccount(accountUuid);

		mNotifyView.setChecked(mAccount.isNotifyNewMail());
		mNotifySyncView.setChecked(mAccount.isShowOngoing());
		SpinnerOption.setSpinnerOptionValue(mCheckFrequencyView,
				mAccount.getAutomaticCheckIntervalMinutes());
		SpinnerOption.setSpinnerOptionValue(mDisplayCountView,
				mAccount.getDisplayCount());

		boolean isPushCapable = false;
		try {
			Store store = mAccount.getRemoteStore();
			isPushCapable = store.isPushCapable();
		} catch (Exception e) {
			Log.e(MAIL.LOG_TAG, "Could not get remote store", e);
		}

		if (!isPushCapable) {
			mPushEnable.setVisibility(View.GONE);
		} else {
			mPushEnable.setChecked(true);
		}

	}

	private void onDone() {
		mAccount.setDescription(mAccount.getEmail());
		mAccount.setNotifyNewMail(mNotifyView.isChecked());
		mAccount.setShowOngoing(mNotifySyncView.isChecked());
		mAccount.setAutomaticCheckIntervalMinutes((Integer) ((SpinnerOption) mCheckFrequencyView
				.getSelectedItem()).value);
		mAccount.setDisplayCount((Integer) ((SpinnerOption) mDisplayCountView
				.getSelectedItem()).value);

		if (mPushEnable.isChecked()) {
			mAccount.setFolderPushMode(Account.FolderMode.FIRST_CLASS);
		} else {
			mAccount.setFolderPushMode(Account.FolderMode.NONE);
		}

		mAccount.save(Preferences.getPreferences(this));
		if (mAccount.equals(Preferences.getPreferences(this)
				.getDefaultAccount())
				|| getIntent().getBooleanExtra(EXTRA_MAKE_DEFAULT, false)) {
			Preferences.getPreferences(this).setDefaultAccount(mAccount);
		}
		MAIL.setServicesEnabled(this);
		AccountSetupNames.actionSetNames(this, mAccount);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			onDone();
			break;
		}
	}
}
