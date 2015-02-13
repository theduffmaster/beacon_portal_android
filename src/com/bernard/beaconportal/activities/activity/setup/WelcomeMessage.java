package com.bernard.beaconportal.activities.activity.setup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.activity.Accounts;
import com.bernard.beaconportal.activities.activity.MAILActivity;
import com.bernard.beaconportal.activities.helper.HtmlConverter;

/**
 * Displays a welcome message when no accounts have been created yet.
 */
public class WelcomeMessage extends MAILActivity implements OnClickListener {

	public static void showWelcomeMessage(Context context) {
		Intent intent = new Intent(context, WelcomeMessage.class);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Intent intent = new Intent(this, AccountSetupBasics.class);
		this.startActivity(intent);

		setContentView(R.layout.welcome_message);

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

		TextView welcome = (TextView) findViewById(R.id.welcome_message);
		welcome.setText(HtmlConverter
				.htmlToSpanned(getString(R.string.accounts_welcome)));
		welcome.setMovementMethod(LinkMovementMethod.getInstance());

		((Button) findViewById(R.id.next)).setOnClickListener(this);
		((Button) findViewById(R.id.import_settings)).setOnClickListener(this);
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
