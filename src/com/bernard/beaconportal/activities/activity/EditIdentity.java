package com.bernard.beaconportal.activities.activity;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.Identity;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.R;

public class EditIdentity extends MAILActivity {

	public static final String EXTRA_IDENTITY = "com.bernard.beaconportal.activities.EditIdentity_identity";
	public static final String EXTRA_IDENTITY_INDEX = "com.bernard.beaconportal.activities.EditIdentity_identity_index";
	public static final String EXTRA_ACCOUNT = "com.bernard.beaconportal.activities.EditIdentity_account";

	private Account mAccount;
	private Identity mIdentity;
	private int mIdentityIndex;
	private EditText mDescriptionView;
	private CheckBox mSignatureUse;
	private EditText mSignatureView;
	private LinearLayout mSignatureLayout;
	private EditText mEmailView;
	// private EditText mAlwaysBccView;
	private EditText mNameView;
	private EditText mReplyTo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mIdentity = (Identity) getIntent().getSerializableExtra(EXTRA_IDENTITY);
		mIdentityIndex = getIntent().getIntExtra(EXTRA_IDENTITY_INDEX, -1);
		String accountUuid = getIntent().getStringExtra(EXTRA_ACCOUNT);
		mAccount = Preferences.getPreferences(this).getAccount(accountUuid);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");

		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(getResources().getColor((R.color.white)));

		if (mIdentityIndex == -1) {
			mIdentity = new Identity();
		}

		setContentView(R.layout.edit_identity);

		/*
		 * If we're being reloaded we override the original account with the one
		 * we saved
		 */
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(EXTRA_IDENTITY)) {
			mIdentity = (Identity) savedInstanceState
					.getSerializable(EXTRA_IDENTITY);
		}

		mDescriptionView = (EditText) findViewById(R.id.description);
		mDescriptionView.setText(mIdentity.getDescription());

		mNameView = (EditText) findViewById(R.id.name);
		mNameView.setText(mIdentity.getName());

		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mIdentity.getEmail());

		mReplyTo = (EditText) findViewById(R.id.reply_to);
		mReplyTo.setText(mIdentity.getReplyTo());

		// mAccountAlwaysBcc = (EditText)findViewById(R.id.bcc);
		// mAccountAlwaysBcc.setText(mIdentity.getAlwaysBcc());

		mSignatureLayout = (LinearLayout) findViewById(R.id.signature_layout);
		mSignatureUse = (CheckBox) findViewById(R.id.signature_use);
		mSignatureView = (EditText) findViewById(R.id.signature);
		mSignatureUse.setChecked(mIdentity.getSignatureUse());
		mSignatureUse
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mSignatureLayout.setVisibility(View.VISIBLE);
							mSignatureView.setText(mIdentity.getSignature());
						} else {
							mSignatureLayout.setVisibility(View.GONE);
						}
					}
				});

		if (mSignatureUse.isChecked()) {
			mSignatureView.setText(mIdentity.getSignature());
		} else {
			mSignatureLayout.setVisibility(View.GONE);

		}

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#4285f4")));

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

	}

	private void saveIdentity() {

		mIdentity.setDescription(mDescriptionView.getText().toString());
		mIdentity.setEmail(mEmailView.getText().toString());
		// mIdentity.setAlwaysBcc(mAccountAlwaysBcc.getText().toString());
		mIdentity.setName(mNameView.getText().toString());
		mIdentity.setSignatureUse(mSignatureUse.isChecked());
		mIdentity.setSignature(mSignatureView.getText().toString());

		if (mReplyTo.getText().length() == 0) {
			mIdentity.setReplyTo(null);
		} else {
			mIdentity.setReplyTo(mReplyTo.getText().toString());
		}

		List<Identity> identities = mAccount.getIdentities();
		if (mIdentityIndex == -1) {
			identities.add(mIdentity);
		} else {
			identities.remove(mIdentityIndex);
			identities.add(mIdentityIndex, mIdentity);
		}

		mAccount.save(Preferences.getPreferences(getApplication()
				.getApplicationContext()));

		finish();
	}

	@Override
	public void onBackPressed() {
		saveIdentity();
		super.onBackPressed();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(EXTRA_IDENTITY, mIdentity);
	}
}
