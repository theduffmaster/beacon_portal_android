package com.bernard.beaconportal.activities.activity.setup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bernard.beaconportal.activities.*;
import com.bernard.beaconportal.activities.MainActivity.Update;
import com.bernard.beaconportal.activities.activity.Accounts;
import com.bernard.beaconportal.activities.activity.K9Activity;
import com.bernard.beaconportal.activities.helper.Utility;
import com.bernard.beaconportal.activities.R;

public class AccountSetupNames extends K9Activity implements OnClickListener {
	private static final String EXTRA_ACCOUNT = "account";

	private EditText mDescription;

	private EditText mName;

	private Account mAccount;

	private Button mDoneButton;

	public static void actionSetNames(Context context, Account account) {
		Intent i = new Intent(context, AccountSetupNames.class);
		i.putExtra(EXTRA_ACCOUNT, account.getUuid());
		context.startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_setup_names);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");

		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(getResources().getColor((R.color.white)));

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#298ccd")));

		} else {

			String actionbar_colors = sharedpref.getString("actionbar_color",
					null);

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor(actionbar_colors)));

		}

		android.app.ActionBar bar = getActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

		mDescription = (EditText) findViewById(R.id.account_description);
		mName = (EditText) findViewById(R.id.account_name);
		mDoneButton = (Button) findViewById(R.id.done);
		mDoneButton.setOnClickListener(this);

		TextWatcher validationTextWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				validateFields();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		};
		mName.addTextChangedListener(validationTextWatcher);

		mName.setKeyListener(TextKeyListener.getInstance(false,
				Capitalize.WORDS));

		String accountUuid = getIntent().getStringExtra(EXTRA_ACCOUNT);
		mAccount = Preferences.getPreferences(this).getAccount(accountUuid);

		/*
		 * Since this field is considered optional, we don't set this here. If
		 * the user fills in a value we'll reset the current value, otherwise we
		 * just leave the saved value alone.
		 */
		// mDescription.setText(mAccount.getDescription());
		if (mAccount.getName() != null) {
			mName.setText(mAccount.getName());
		}
		if (!Utility.requiredFieldValid(mName)) {
			mDoneButton.setEnabled(false);
		}
	}

	private void validateFields() {
		mDoneButton.setEnabled(Utility.requiredFieldValid(mName));
		Utility.setCompoundDrawablesAlpha(mDoneButton,
				mDoneButton.isEnabled() ? 255 : 128);
	}

	protected void onNext() {
		
		InputMethodManager im = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		if (Utility.requiredFieldValid(mDescription)) {
			mAccount.setDescription(mDescription.getText().toString());
		}
		mAccount.setName(mName.getText().toString());
		mAccount.save(Preferences.getPreferences(this));
		Accounts.listAccounts(this);
		finish();

		Intent Intent = new Intent(AccountSetupNames.this, MainActivity.class);

		AccountSetupNames.this.startActivity(Intent);

		String name = mName.getText().toString();

		SharedPreferences.Editor localEditor = getSharedPreferences(
				"Login_info", Context.MODE_PRIVATE).edit();

		localEditor.putString("name", name);

		localEditor.commit();
		
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.done:
			onNext();
			
			break;
		}
	}
}
