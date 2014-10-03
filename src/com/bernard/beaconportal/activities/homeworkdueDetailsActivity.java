package com.bernard.beaconportal.activities;

import static com.bernard.beaconportal.activities.Due_Today_Fragment.KEY_DATE;
import static com.bernard.beaconportal.activities.Due_Today_Fragment.KEY_DESC;
import static com.bernard.beaconportal.activities.Due_Today_Fragment.KEY_HOMEWORK;
import static com.bernard.beaconportal.activities.Due_Today_Fragment.KEY_TYPE;
import static com.bernard.beaconportal.activities.Due_Today_Fragment.KEY_BAND;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.bernard.beaconportal.activities.R;

public class homeworkdueDetailsActivity extends SherlockActivity {

	private String background_colors, actionbar_colors;

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

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#298ccd")));

		} else {

			actionbar_colors = sharedpref.getString("actionbar_color", null);

			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor(actionbar_colors)));

		}

		android.app.ActionBar bar = getActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));
		bar.setTitle(type);

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

		SharedPreferences sharedpref = getSharedPreferences("actionbar_color",
				Context.MODE_PRIVATE);

		// if (band.substring(0, Math.min(band.length(), 2)).equals("UU")) {
		//
		// System.out.println(band);
		//
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#bdc3c7")));
		//
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("UN")) {
		//
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#95a5a6")));
		//
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("UG")) {
		//
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#6c7a89")));
		//
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("TZ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#c0392b")));
		//
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("TQ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#ef4836")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("SR")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#663399")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("SQ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#674172")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("SP")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#aea8d3")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("SK")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#9b59b6")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("SF")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#bf55ec")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("SC")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#be90d4")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("SB")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#913d88")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("PQ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f5d76e")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("PP")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f7ca18")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("PH")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f4d03f")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("MS")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f5ab35")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("MR")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f4b350")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("MQ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f9bf3b")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("MP")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#e87e04")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("MG")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f2784b")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("ME")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f27935")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("MC")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#eb9532")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("HU")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#3a539b")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("HG")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#6bb9f0")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("HF")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#59abe3")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("DM")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#00b16a")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("DW")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#e26a6a")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("EE")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#00b16a")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("DQ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f62459")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("DJ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#db0a5b")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("CR")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#bfbfbf")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("CQ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#95a5a6")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("CJ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#6c7a89")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("AQ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#ef4836")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("AJ")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f22613")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("AN")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#f0211b")));
		// }
		// if (band.substring(0, Math.min(band.length(), 2)).equals("AC")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#d24d57")));
		// }
		//
		// if (band.substring(0, Math.min(band.length(), 2)).equals("FS")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#e74c3c")));
		// }
		//
		// if (band.substring(0, Math.min(band.length(), 2)).equals("FF")) {
		// System.out.println(band);
		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#3a539b")));
		// }

		android.app.ActionBar bar = getActionBar();

		bar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));
		bar.setTitle(type);

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
	public boolean onOptionsItemSelected(MenuItem item) {
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
