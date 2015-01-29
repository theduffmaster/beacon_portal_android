package com.bernard.beaconportal.activities.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.helper.ContactItem;

public class EmailAddressList extends MAILListActivity implements
		OnItemClickListener {
	public static final String EXTRA_CONTACT_ITEM = "contact";
	public static final String EXTRA_EMAIL_ADDRESS = "emailAddress";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.email_address_list);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");

		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(getResources().getColor((R.color.white)));

		Intent i = getIntent();
		ContactItem contact = (ContactItem) i
				.getSerializableExtra(EXTRA_CONTACT_ITEM);
		if (contact == null) {
			finish();
			return;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.email_address_list_item, contact.emailAddresses);

		ListView listView = getListView();
		listView.setOnItemClickListener(this);
		listView.setAdapter(adapter);
		setTitle(contact.displayName);

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

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String item = (String) parent.getItemAtPosition(position);

		Toast.makeText(EmailAddressList.this, item, Toast.LENGTH_LONG).show();

		Intent intent = new Intent();
		intent.putExtra(EXTRA_EMAIL_ADDRESS, item);
		setResult(RESULT_OK, intent);
		finish();
	}
}
