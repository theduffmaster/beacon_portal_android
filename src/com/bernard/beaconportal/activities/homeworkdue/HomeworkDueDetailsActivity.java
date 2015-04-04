package com.bernard.beaconportal.activities.homeworkdue;

import static com.bernard.beaconportal.activities.homeworkdue.DueTodayFragment.KEY_BAND;
import static com.bernard.beaconportal.activities.homeworkdue.DueTodayFragment.KEY_DATE;
import static com.bernard.beaconportal.activities.homeworkdue.DueTodayFragment.KEY_DESC;
import static com.bernard.beaconportal.activities.homeworkdue.DueTodayFragment.KEY_HOMEWORK;
import static com.bernard.beaconportal.activities.homeworkdue.DueTodayFragment.KEY_TYPE;

import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.R.id;
import com.bernard.beaconportal.activities.R.layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

public class HomeworkDueDetailsActivity extends ActionBarActivity {

	private String actionbar_colors;

	private TextView headlineTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		//
		// getWindow().getDecorView().setSystemUiVisibility(
		// View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		// | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		// | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		// | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		// | View.SYSTEM_UI_FLAG_FULLSCREEN
		// | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
		// | View.INVISIBLE);
		//
		// } else {
		//
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//
		// }

		setContentView(R.layout.activity_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		String homework = "";
		String desc = "";
		String date = "";
		String type = "";
		String band = "";

		Intent intent = getIntent();
		if (null != intent) {
			homework = intent.getStringExtra(KEY_HOMEWORK);
			desc = intent.getStringExtra(KEY_DESC);
			date = intent.getStringExtra(KEY_DATE);
			type = intent.getStringExtra(KEY_TYPE);
			band = intent.getStringExtra(KEY_BAND);

		}

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			getSupportActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#4285f4")));

			if (Build.VERSION.SDK_INT >= 21) {
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(Color.parseColor("#3367d6"));
			}

		} else {

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			getSupportActionBar().setBackgroundDrawable(

			new ColorDrawable(Color.parseColor(actionbar_colors)));

			if (Build.VERSION.SDK_INT >= 21) {
				
				//darken color for status bar
				float[] hsv = new float[3];
				int darkerColor = Color.parseColor(actionbar_colors);
				Color.colorToHSV(darkerColor, hsv);
				hsv[2] *= 0.85f; // value component
				darkerColor = Color.HSVToColor(hsv);
				
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(darkerColor);
//				decode(Color.parseColor(actionbar_colors))
			}


		}

		ActionBar bar = getSupportActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));
		bar.setTitle(type);

		bar.setElevation(0);

		String[] separated = date.split("-");
		String year = separated[0].trim();
		String day = separated[1].trim();
		String month = separated[2].trim();

		headlineTxt = (TextView) findViewById(R.id.texthomeworkdue);

		headlineTxt.setText(homework);

		if (band.substring(0, Math.min(band.length(), 2)).equals("UU")) {

			System.out.println(band);

			headlineTxt.setBackgroundColor(Color.parseColor("#bdc3c7"));

		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("UN")) {

			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#95a5a6"));

		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("UG")) {

			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#6c7a89"));

		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("TZ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#c0392b"));

		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("TQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#ef4836"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SR")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#663399"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#674172"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SP")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#aea8d3"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SK")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#9b59b6"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SF")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#bf55ec"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SC")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#be90d4"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SB")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#913d88"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("PQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f5d76e"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("PP")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f7ca18"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("PH")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f4d03f"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MS")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f5ab35"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MR")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f4b350"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f9bf3b"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MP")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#e87e04"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MG")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f2784b"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("ME")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f27935"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MC")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#eb9532"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("HU")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#3a539b"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("HG")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#6bb9f0"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("HF")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#59abe3"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("DM")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#00b16a"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("DW")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#e26a6a"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("EE")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#00b16a"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("DQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f62459"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("DJ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#db0a5b"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("CR")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#bfbfbf"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("CQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#95a5a6"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("CJ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#6c7a89"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("AQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#ef4836"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("AJ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f22613"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("AN")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f0211b"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("AC")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#d24d57"));
		}

		if (band.substring(0, Math.min(band.length(), 2)).equals("FS")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#e74c3c"));
		}

		if (band.substring(0, Math.min(band.length(), 2)).equals("FF")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#3a539b"));
		}

		String mimeType = "text/html";
		String encoding = "utf-8";

		WebView pubdescTxt = (WebView) findViewById(R.id.textdescription);
		pubdescTxt.loadData(desc, mimeType, encoding);

		TextView pubdateTxt = (TextView) findViewById(R.id.textdate);
		pubdateTxt.setText("Due " + day + "/" + month + "/" + year);

	}

	@Override
	protected void onResume() {
		super.onResume();

		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		//
		// getWindow().getDecorView().setSystemUiVisibility(
		// View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		// | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		// | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		// | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		// | View.SYSTEM_UI_FLAG_FULLSCREEN
		// | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
		// | View.INVISIBLE);
		//
		// } else {
		//
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//
		// }

		setContentView(R.layout.activity_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		String homework = "";
		String desc = "";
		String date = "";
		String type = "";
		String band = "";

		System.out.println(band);

		Intent intent = getIntent();
		if (null != intent) {
			homework = intent.getStringExtra(KEY_HOMEWORK);
			desc = intent.getStringExtra(KEY_DESC);
			date = intent.getStringExtra(KEY_DATE);
			type = intent.getStringExtra(KEY_TYPE);
			band = intent.getStringExtra(KEY_BAND);

		}

		System.out.println(band);

		getSharedPreferences("actionbar_color", Context.MODE_PRIVATE);

		ActionBar bar = getSupportActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));
		bar.setTitle(type);

		bar.setElevation(0);

		String[] separated = date.split("-");
		String year = separated[0].trim();
		String day = separated[1].trim();
		String month = separated[2].trim();

		headlineTxt = (TextView) findViewById(R.id.texthomeworkdue);

		headlineTxt.setText(homework);

		if (band.substring(0, Math.min(band.length(), 2)).equals("UU")) {

			System.out.println(band);

			headlineTxt.setBackgroundColor(Color.parseColor("#bdc3c7"));

		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("UN")) {

			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#95a5a6"));

		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("UG")) {

			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#6c7a89"));

		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("TZ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#c0392b"));

		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("TQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#ef4836"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SR")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#663399"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#674172"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SP")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#aea8d3"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SK")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#9b59b6"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SF")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#bf55ec"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SC")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#be90d4"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("SB")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#913d88"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("PQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f5d76e"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("PP")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f7ca18"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("PH")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f4d03f"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MS")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f5ab35"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MR")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f4b350"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f9bf3b"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MP")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#e87e04"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MG")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f2784b"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("ME")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f27935"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("MC")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#eb9532"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("HU")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#3a539b"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("HG")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#6bb9f0"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("HF")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#59abe3"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("DM")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#00b16a"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("DW")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#e26a6a"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("EE")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#00b16a"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("DQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f62459"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("DJ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#db0a5b"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("CR")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#bfbfbf"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("CQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#95a5a6"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("CJ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#6c7a89"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("AQ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#ef4836"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("AJ")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f22613"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("AN")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#f0211b"));
		}
		if (band.substring(0, Math.min(band.length(), 2)).equals("AC")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#d24d57"));
		}

		if (band.substring(0, Math.min(band.length(), 2)).equals("FS")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#e74c3c"));
		}

		if (band.substring(0, Math.min(band.length(), 2)).equals("FF")) {
			System.out.println(band);
			headlineTxt.setBackgroundColor(Color.parseColor("#3a539b"));
		}

		String mimeType = "text/html";
		String encoding = "utf-8";

		WebView pubdescTxt = (WebView) findViewById(R.id.textdescription);
		pubdescTxt.loadData(desc, mimeType, encoding);

		TextView pubdateTxt = (TextView) findViewById(R.id.textdate);
		pubdateTxt.setText("Due " + day + "/" + month + "/" + year);

	}

	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			Back();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void Back() {

		super.onBackPressed();

	}

}
