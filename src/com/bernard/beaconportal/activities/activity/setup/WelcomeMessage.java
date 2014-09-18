package com.bernard.beaconportal.activities.activity.setup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.Accounts;
import com.bernard.beaconportal.activities.activity.K9Activity;
import com.bernard.beaconportal.activities.helper.HtmlConverter;

/**
 * Displays a welcome message when no accounts have been created yet.
 */
public class WelcomeMessage extends K9Activity implements OnClickListener {

	public static void showWelcomeMessage(Context context) {
		Intent intent = new Intent(context, WelcomeMessage.class);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.welcome_message);

		Intent Intent = new Intent(WelcomeMessage.this,
				AccountSetupBasics.class);

		WelcomeMessage.this.startActivity(Intent);

		TextView welcome = (TextView) findViewById(R.id.welcome_message);
		welcome.setText(HtmlConverter
				.htmlToSpanned(getString(R.string.accounts_welcome)));
		welcome.setMovementMethod(LinkMovementMethod.getInstance());

		((Button) findViewById(R.id.next)).setOnClickListener(this);
		((Button) findViewById(R.id.import_settings)).setOnClickListener(this);

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#03a9f4")));

		} else {

			String actionbar_colors = sharedpref.getString("actionbar_color",
					null);

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor(actionbar_colors)));

		}

		android.app.ActionBar bar = getActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.next: {
			AccountSetupBasics.actionNewAccount(this);
			finish();
			break;
		}
		case R.id.import_settings: {
			Accounts.importSettings(this);
			finish();
			break;
		}
		}
	}
}
