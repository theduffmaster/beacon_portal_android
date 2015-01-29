package com.bernard.beaconportal.activities.activity.setup;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.FragmentSettings;
import com.bernard.beaconportal.activities.FragmentsHomeworkDue;
import com.bernard.beaconportal.activities.FragmentsSchedule;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.Accounts;
import com.bernard.beaconportal.activities.activity.MAILActivity;
import com.bernard.beaconportal.activities.helper.Utility;

public class AccountSetupNames extends MAILActivity implements OnClickListener {
	private static final String EXTRA_ACCOUNT = "account";

	public static final String PREF_NAME = "pref_name";

	static final Uri mailAccountsUri = Uri
			.parse("content://com.bernard.beaconportal.activities.messageprovider/accounts/");
	static final String mailUnreadUri = "content://com.bernard.beaconportal.activities.messageprovider/account_unread/";
	static final String mailMessageProvider = "content://com.bernard.beaconportal.activities.messageprovider/";

	ContentObserver contentObserver = null;
	BroadcastReceiver receiver = null;
	IntentFilter filter = null;

	DrawerLayout mDrawerLayout;
	LinearLayout mDrawerLinear;
	TextView mWelcomePerson;
	TextView mWelcome;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	String actionbar_colors, actionbar_colorsString;
	String[] title;
	String[] count;
	int[] icon;
	Fragment fragment1 = new FragmentsSchedule();
	Fragment fragment2 = new FragmentsHomeworkDue();
	Fragment fragment3 = new FragmentSettings();
	ProgressDialog LoginDialog;

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

		InputMethodManager im = (InputMethodManager) this
				.getApplicationContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		if (Utility.requiredFieldValid(mDescription)) {
			mAccount.setDescription(mDescription.getText().toString());
		}
		mAccount.setName(mName.getText().toString());
		mAccount.save(Preferences.getPreferences(this));
		Accounts.listAccounts(this);

		String name = mName.getText().toString();

		SharedPreferences.Editor localEditor = getSharedPreferences(
				"Login_info", Context.MODE_PRIVATE).edit();

		localEditor.putString("name", name);

		localEditor.commit();

		Intent intent = new Intent(AccountSetupNames.this, LoadingLayout.class);

		startActivity(intent);
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
