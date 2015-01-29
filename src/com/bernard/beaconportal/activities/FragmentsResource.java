package com.bernard.beaconportal.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentsResource extends ListFragment {

	private View view;

	private ListView listView;

	private View footer;

	String[] title;

	int[] IconID;

	private Resources listAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.resources_list, container, false);

		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();

		actionBar.setElevation(2);

		return view;

	}

	@Override
	public void onStart() {
		super.onStart();

		footer = getActivity().getLayoutInflater().inflate(
				R.layout.resources_footer, null);

		getListView().addFooterView(footer);

	}

	@Override
	public void onResume() {
		super.onResume();

		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();

		actionBar.setElevation(2);

		listView = getListView();

		title = new String[] { "Today's NYT Front Page",
				"Yesterday's NYT Front Page", "Engrade", "TeacherEase" };

		IconID = new int[] { R.drawable.nyt, R.drawable.nyt, R.drawable.engrade, R.drawable.teacher_ease };

		listAdapter = new Resources(getActivity(), title, IconID);

		getListView().setAdapter(listAdapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position) {

				case 0:

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

					Calendar c = Calendar.getInstance();

					String today = sdf.format(c.getTime());

					String todayLink = "http://www.nytimes.com/images/" + today
							+ "/nytfrontpage/scan.pdf";

					Intent todayIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(todayLink));
					startActivity(todayIntent);

					break;

				case 1:

					SimpleDateFormat sdatef = new SimpleDateFormat("yyyy/MM/dd");

					Calendar cal = Calendar.getInstance();

					cal.add(Calendar.DATE, -1);

					String yesterday = sdatef.format(cal.getTime());

					String yesterdayLink = "http://www.nytimes.com/images/"
							+ yesterday + "/nytfrontpage/scan.pdf";

					Intent yesterdayIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(yesterdayLink));
					startActivity(yesterdayIntent);

					break;
					
				case 2:

					String engrade = "https://www.engrade.com/user/login.php";

					Intent engradeIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(engrade));
					startActivity(engradeIntent);
					
					break;
					
				case 3:

					String teacherEase = "https://www.teacherease.com/common/login.aspx";

					Intent teacherEaseIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(teacherEase));
					startActivity(teacherEaseIntent);
					
					break;

				}
			};

		});

	}

	@Override
	public void onPause() {
		super.onPause();

		getListView().removeFooterView(footer);

	}
}
