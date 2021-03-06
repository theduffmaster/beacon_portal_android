package com.bernard.beaconportal.activities.activity.setup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.MAILActivityMaterial;

public class AccountSetupComposition extends MAILActivityMaterial {

	private static final String EXTRA_ACCOUNT = "account";

	private Account mAccount;

	private EditText mAccountSignature;
	private EditText mAccountEmail;
	private EditText mAccountAlwaysBcc;
	private EditText mAccountName;
	private CheckBox mAccountSignatureUse;
	private RadioButton mAccountSignatureBeforeLocation;
	private RadioButton mAccountSignatureAfterLocation;
	private LinearLayout mAccountSignatureLayout;

	public static void actionEditCompositionSettings(Activity context,
			Account account) {
		Intent i = new Intent(context, AccountSetupComposition.class);
		i.setAction(Intent.ACTION_EDIT);
		i.putExtra(EXTRA_ACCOUNT, account.getUuid());
		context.startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String accountUuid = getIntent().getStringExtra(EXTRA_ACCOUNT);
		mAccount = Preferences.getPreferences(this).getAccount(accountUuid);

		setContentView(R.layout.account_setup_composition);

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

		/*
		 * If we're being reloaded we override the original account with the one
		 * we saved
		 */
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(EXTRA_ACCOUNT)) {
			accountUuid = savedInstanceState.getString(EXTRA_ACCOUNT);
			mAccount = Preferences.getPreferences(this).getAccount(accountUuid);
		}

		mAccountName = (EditText) findViewById(R.id.account_name);
		mAccountName.setText(mAccount.getName());

		mAccountEmail = (EditText) findViewById(R.id.account_email);
		mAccountEmail.setText(mAccount.getEmail());

		mAccountAlwaysBcc = (EditText) findViewById(R.id.account_always_bcc);
		mAccountAlwaysBcc.setText(mAccount.getAlwaysBcc());

		mAccountSignatureLayout = (LinearLayout) findViewById(R.id.account_signature_layout);

		mAccountSignatureUse = (CheckBox) findViewById(R.id.account_signature_use);
		boolean useSignature = mAccount.getSignatureUse();
		mAccountSignatureUse.setChecked(useSignature);
		mAccountSignatureUse
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mAccountSignatureLayout.setVisibility(View.VISIBLE);
							mAccountSignature.setText(mAccount.getSignature());
							boolean isSignatureBeforeQuotedText = mAccount
									.isSignatureBeforeQuotedText();
							mAccountSignatureBeforeLocation
									.setChecked(isSignatureBeforeQuotedText);
							mAccountSignatureAfterLocation
									.setChecked(!isSignatureBeforeQuotedText);
						} else {
							mAccountSignatureLayout.setVisibility(View.GONE);
						}
					}
				});

		mAccountSignature = (EditText) findViewById(R.id.account_signature);

		mAccountSignatureBeforeLocation = (RadioButton) findViewById(R.id.account_signature_location_before_quoted_text);
		mAccountSignatureAfterLocation = (RadioButton) findViewById(R.id.account_signature_location_after_quoted_text);

		if (useSignature) {
			mAccountSignature.setText(mAccount.getSignature());

			boolean isSignatureBeforeQuotedText = mAccount
					.isSignatureBeforeQuotedText();
			mAccountSignatureBeforeLocation
					.setChecked(isSignatureBeforeQuotedText);
			mAccountSignatureAfterLocation
					.setChecked(!isSignatureBeforeQuotedText);
		} else {
			mAccountSignatureLayout.setVisibility(View.GONE);
		}
	}

	private void saveSettings() {
		mAccount.setEmail(mAccountEmail.getText().toString());
		mAccount.setAlwaysBcc(mAccountAlwaysBcc.getText().toString());
		mAccount.setName(mAccountName.getText().toString());
		mAccount.setSignatureUse(mAccountSignatureUse.isChecked());
		if (mAccountSignatureUse.isChecked()) {
			mAccount.setSignature(mAccountSignature.getText().toString());
			boolean isSignatureBeforeQuotedText = mAccountSignatureBeforeLocation
					.isChecked();
			mAccount.setSignatureBeforeQuotedText(isSignatureBeforeQuotedText);
		}

		mAccount.save(Preferences.getPreferences(this));
	}

	@Override
	public void onBackPressed() {
		saveSettings();
		super.onBackPressed();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(EXTRA_ACCOUNT, mAccount.getUuid());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mAccount.save(Preferences.getPreferences(this));
		finish();
	}
}
