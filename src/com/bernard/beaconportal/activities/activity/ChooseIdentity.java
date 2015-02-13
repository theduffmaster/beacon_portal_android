package com.bernard.beaconportal.activities.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bernard.beaconportal.activities.Account;
import com.bernard.beaconportal.activities.Identity;
import com.bernard.beaconportal.activities.Preferences;
import com.bernard.beaconportal.activities.R;

public class ChooseIdentity extends MAILListActivity {
	Account mAccount;
	ArrayAdapter<String> adapter;

	public static final String EXTRA_ACCOUNT = "com.bernard.beaconportal.activities.ChooseIdentity_account";
	public static final String EXTRA_IDENTITY = "com.bernard.beaconportal.activities.ChooseIdentity_identity";

	protected List<Identity> identities = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.list_content_simple);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");

		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(getResources().getColor((R.color.white)));

		getListView().setTextFilterEnabled(true);
		getListView().setItemsCanFocus(false);
		getListView().setChoiceMode(AbsListView.CHOICE_MODE_NONE);
		Intent intent = getIntent();
		String accountUuid = intent.getStringExtra(EXTRA_ACCOUNT);
		mAccount = Preferences.getPreferences(this).getAccount(accountUuid);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		setListAdapter(adapter);
		setupClickListeners();

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

	@Override
	public void onResume() {
		super.onResume();
		refreshView();
	}

	protected void refreshView() {
		adapter.setNotifyOnChange(false);
		adapter.clear();

		identities = mAccount.getIdentities();
		for (Identity identity : identities) {
			String description = identity.getDescription();
			if (description == null || description.trim().isEmpty()) {
				description = getString(R.string.message_view_from_format,
						identity.getName(), identity.getEmail());
			}
			adapter.add(description);
		}

		adapter.notifyDataSetChanged();
	}

	protected void setupClickListeners() {
		this.getListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Identity identity = mAccount.getIdentity(position);
						String email = identity.getEmail();
						if (email != null && !email.trim().equals("")) {
							Intent intent = new Intent();

							intent.putExtra(EXTRA_IDENTITY,
									mAccount.getIdentity(position));
							setResult(RESULT_OK, intent);
							finish();
						} else {
							Toast.makeText(ChooseIdentity.this,
									getString(R.string.identity_has_no_email),
									Toast.LENGTH_LONG).show();
						}
					}
				});

	}
}
