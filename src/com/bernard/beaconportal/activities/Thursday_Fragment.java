package com.bernard.beaconportal.activities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Thursday_Fragment extends Fragment {
	private List<Due_Today_List> due_schedule_list;

	private View swipe;

	private String date;

	private String finalDay;

	private java.util.Date dt1;

	public static ListView lView;

	private ProgressBar progress;

	private ArrayAdapter<Due_Today_List> adapter;

	private String Band, Number, Class, Teacher, Title, Date, Type,
			Description, due_schedule_shared;

	static class ViewHolder {
		public ImageView imageView;
		public TextView HomeworkDueText;
		public TextView DescriptionText;
		public TextView TeacherText;
		public TextView TypeText;
	}

	HttpResponse response;

	public static final String KEY_HOMEWORK = "homework";
	public static final String KEY_DESC = "desc";
	public static final String KEY_DATE = "date";
	public static final String KEY_TYPE = "type";
	public static final String KEY_BAND = "band";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		swipe = inflater.inflate(R.layout.day_homework_fragment, container,
				false);

		lView = (ListView) swipe.findViewById(R.id.listView1);

		progress = (ProgressBar) swipe.findViewById(R.id.progress);

		lView.setVisibility(View.GONE);

		return swipe;

	}

	@Override
	public void onResume() {

		super.onResume();

		due_schedule_list = new ArrayList<Due_Today_List>();

		parse_due_schedule_content();

		populateListView();

		registerClickCallback();

		progress.setVisibility(View.GONE);

		lView.setVisibility(View.VISIBLE);

	}

	public void parse_due_schedule_content() {

		date = "Thursday";

		SharedPreferences Today_Homework_Counter = getActivity()
				.getSharedPreferences("due_schedule_counter",
						Context.MODE_PRIVATE);

		int counterssss = Today_Homework_Counter.getInt(
				"last shared preference", 0);

		int countersssss = counterssss + 1;

		for (int i = 0; i < countersssss; i++) {

			due_schedule_shared = "due_schedule" + Integer.toString(i);

			SharedPreferences Todays_Homework = getActivity()
					.getSharedPreferences(due_schedule_shared,
							Context.MODE_PRIVATE);

			String Band1 = Todays_Homework.getString("due_schedule0", null);

			String Number1 = Todays_Homework.getString("due_schedule1", null);

			String Class1 = Todays_Homework.getString("due_schedule2", null);

			String Teacher1 = Todays_Homework.getString("due_schedule3", null);

			String Title1 = Todays_Homework.getString("due_schedule4", null);

			String Date1 = Todays_Homework.getString("due_schedule5", null);

			String Type1 = Todays_Homework.getString("due_schedule6", null);

			String Description1 = Todays_Homework.getString("due_schedule7",
					null);

			if (Band1 != null) {

				Band = Band1.trim();

			}

			if (Number1 != null) {

				Number = Number1.trim();

			}

			if (Class1 != null) {

				Class = Class1.trim();

			}

			if (Teacher1 != null) {

				Teacher = Teacher1.trim();

			}

			if (Title1 != null) {

				Title = Title1.trim();

			}

			if (Date1 != null) {

				Date = Date1.trim();

				String input_date = Date;

				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

				try {
					dt1 = format1.parse(input_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DateFormat format2 = new SimpleDateFormat("EEEE");

				finalDay = format2.format(dt1);

				Log.d("Date" + i, Date);

			}

			if (Type1 != null) {

				Type = Type1.trim();

			}

			if (Description1 != null) {

				Description = Description1.trim();

			}

			SharedPreferences description_check = getActivity()
					.getSharedPreferences("descriptioncheck",
							Context.MODE_PRIVATE);

			String descriptionCheck = description_check.getString(
					"description", "");

			if (Type != null && Description != null
					&& !Description.equals(descriptionCheck)
					&& date.contentEquals(finalDay)) {

				SharedPreferences.Editor checkeditor = getActivity()

				.getSharedPreferences("descriptioncheck", Context.MODE_PRIVATE)
						.edit();

				checkeditor.putString("description", Description);

				checkeditor.commit();

				if (!"Type".equals(Type)) {

					due_schedule_list.add(new Due_Today_List(Band, Number,

					Class, Teacher, Title, Date, Type, Description));
				}

			}

		}

	}

	private void registerClickCallback() {
		ListView list = (ListView) swipe.findViewById(R.id.listView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {

				Due_Today_List clickedhomeworkdue = due_schedule_list
						.get(position);

				// Description =
				// clickedhomeworkdue.getDescription().substring(5);

				Intent intent = new Intent(getActivity(),
						homeworkdueDetailsActivity.class);
				intent.putExtra(KEY_HOMEWORK, clickedhomeworkdue.getTitle());
				intent.putExtra(KEY_DESC, clickedhomeworkdue.getDescription());
				intent.putExtra(KEY_DATE, clickedhomeworkdue.getDate());
				intent.putExtra(KEY_TYPE, clickedhomeworkdue.getType());
				intent.putExtra(KEY_BAND, clickedhomeworkdue.getBand());

				startActivity(intent);
			}
		});
	}

	private void populateListView() {

		adapter = new due_scheduleAdapter();
		ListView list = (ListView) swipe.findViewById(R.id.listView1);
		list.setAdapter(adapter);

	}

	public class due_scheduleAdapter extends ArrayAdapter<Due_Today_List> {
		public due_scheduleAdapter() {
			super(getActivity(), R.layout.homeworkday_item_view,
					due_schedule_list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.homeworkday_item_view, parent, false);
				holder = new ViewHolder();

				holder.imageView = (ImageView) convertView
						.findViewById(R.id.item_iconclass);

				holder.HomeworkDueText = (TextView) convertView
						.findViewById(R.id.item_texthomeworkdue);

				holder.DescriptionText = (TextView) convertView
						.findViewById(R.id.item_textdescription);

				holder.TeacherText = (TextView) convertView
						.findViewById(R.id.item_textteacher);

				holder.TypeText = (TextView) convertView
						.findViewById(R.id.item_texttype);

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			Due_Today_List currenthomeworkdue = due_schedule_list.get(position);

			Teacher = currenthomeworkdue.getTeacher().substring(0, 1)
					.toUpperCase()
					+ currenthomeworkdue.getTeacher().substring(1)
							.toLowerCase();

			// Description = currenthomeworkdue.getDescription().substring(5);

			Description = currenthomeworkdue.getDescription().toString();

			Description = Description.trim();

			Description = Description.replaceAll("[\\n\\t]", "");

			Description = Html.fromHtml(Description).toString();

			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UU")) {

				holder.imageView.setImageResource(R.drawable.uu);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UN")) {

				holder.imageView.setImageResource(R.drawable.un);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("UG")) {

				holder.imageView.setImageResource(R.drawable.ug);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("TZ")) {

				holder.imageView.setImageResource(R.drawable.tz);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("TQ")) {

				holder.imageView.setImageResource(R.drawable.tq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SR")) {

				holder.imageView.setImageResource(R.drawable.sr);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SQ")) {

				holder.imageView.setImageResource(R.drawable.sq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SP")) {

				holder.imageView.setImageResource(R.drawable.sp);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SK")) {

				holder.imageView.setImageResource(R.drawable.sk);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SF")) {

				holder.imageView.setImageResource(R.drawable.sf);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SC")) {

				holder.imageView.setImageResource(R.drawable.sc);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("SB")) {

				holder.imageView.setImageResource(R.drawable.sb);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PQ")) {

				holder.imageView.setImageResource(R.drawable.pq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PP")) {

				holder.imageView.setImageResource(R.drawable.pp);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("PH")) {

				holder.imageView.setImageResource(R.drawable.ph);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MS")) {

				holder.imageView.setImageResource(R.drawable.ms);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MR")) {

				holder.imageView.setImageResource(R.drawable.mr);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MQ")) {

				holder.imageView.setImageResource(R.drawable.mq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MP")) {

				holder.imageView.setImageResource(R.drawable.mp);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MG")) {

				holder.imageView.setImageResource(R.drawable.mg);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("ME")) {

				holder.imageView.setImageResource(R.drawable.me);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("MC")) {

				holder.imageView.setImageResource(R.drawable.mc);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HU")) {

				holder.imageView.setImageResource(R.drawable.hu);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HG")) {

				holder.imageView.setImageResource(R.drawable.hg);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("HF")) {

				holder.imageView.setImageResource(R.drawable.hf);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DM")) {

				holder.imageView.setImageResource(R.drawable.dm);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DW")) {

				holder.imageView.setImageResource(R.drawable.dw);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("EE")) {

				holder.imageView.setImageResource(R.drawable.ee);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DQ")) {

				holder.imageView.setImageResource(R.drawable.dq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("DJ")) {

				holder.imageView.setImageResource(R.drawable.dj);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CR")) {

				holder.imageView.setImageResource(R.drawable.cr);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CQ")) {

				holder.imageView.setImageResource(R.drawable.cq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("CJ")) {

				holder.imageView.setImageResource(R.drawable.cj);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AQ")) {

				holder.imageView.setImageResource(R.drawable.aq);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AJ")) {

				holder.imageView.setImageResource(R.drawable.aj);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AN")) {

				holder.imageView.setImageResource(R.drawable.an);

			}
			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("AC")) {

				holder.imageView.setImageResource(R.drawable.ac);

			}

			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("FS")) {

				holder.imageView.setImageResource(R.drawable.spanish);

			}

			if (currenthomeworkdue.Band.substring(0,
					Math.min(currenthomeworkdue.Band.length(), 2)).equals("FF")) {

				holder.imageView.setImageResource(R.drawable.french);

			}

			holder.HomeworkDueText
					.setText(currenthomeworkdue.getTitle().trim());

			holder.DescriptionText.setText(Description);

			holder.DescriptionText.setEllipsize(TruncateAt.END);

			holder.TeacherText.setText(Teacher.trim());

			holder.TypeText.setText(currenthomeworkdue.getType().trim());

			return convertView;

		}

	}

}