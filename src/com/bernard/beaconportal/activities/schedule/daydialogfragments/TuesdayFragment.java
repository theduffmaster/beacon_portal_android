package com.bernard.beaconportal.activities.schedule.daydialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.homeworkdue.DueTodayList;
import com.bernard.beaconportal.activities.homeworkdue.HomeworkDueDetailsActivity;
import com.bumptech.glide.Glide;

import org.apache.http.HttpResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TuesdayFragment extends DialogFragment {
	private List<DueTodayList> due_schedule_list;
	private View swipe;

	private String date;

	private String finalDay;

	private java.util.Date dt1;

	public static ListView lView;

	private ProgressBar progress;

	private ArrayAdapter<DueTodayList> adapter;

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


    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        swipe = inflater.inflate(R.layout.day_homework_fragment, null);

        lView = (ListView) swipe.findViewById(R.id.listView1);
        progress = (ProgressBar) swipe.findViewById(R.id.progress);
        lView.setVisibility(View.GONE);



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setView(swipe)
                .setTitle("Homework Due Tuesday")
                .setNegativeButton("Dismiss",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        return alertDialogBuilder.create();
    }

	@Override
	public void onResume() {
		super.onResume();

		TextView emptyView = (TextView) swipe.findViewById(R.id.emptyView);

		emptyView.setText("As of now no homework due Tuesday");

		lView.setEmptyView(emptyView);

		due_schedule_list = new ArrayList<DueTodayList>();

		parse_due_schedule_content();

		populateListView();

		registerClickCallback();

		progress.setVisibility(View.GONE);

		lView.setVisibility(View.VISIBLE);

	}

	public void parse_due_schedule_content() {

		date = "Tuesday";

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

					due_schedule_list.add(new DueTodayList(Band, Number,

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

				DueTodayList clickedhomeworkdue = due_schedule_list
						.get(position);

				// Description =
				// clickedhomeworkdue.getDescription().substring(5);

				Intent intent = new Intent(getActivity(),
						HomeworkDueDetailsActivity.class);
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

	public class due_scheduleAdapter extends ArrayAdapter<DueTodayList> {
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

			DueTodayList currenthomeworkdue = due_schedule_list.get(position);

			Teacher = currenthomeworkdue.getTeacher().substring(0, 1)
					.toUpperCase()
					+ currenthomeworkdue.getTeacher().substring(1)
							.toLowerCase();

			// Description = currenthomeworkdue.getDescription().substring(5);

			Description = currenthomeworkdue.getDescription().toString();

			Description = Description.trim();

			Description = Description.replaceAll("[\\n\\t]", "");

			Description = Html.fromHtml(Description).toString();

            String name = currenthomeworkdue.Band.substring(0,
                    Math.min(currenthomeworkdue.Band.length(), 2));

            Log.d("Drawable name= ", name);

            StringBuilder sb = new StringBuilder(name);
            for (int index = 0; index < sb.length(); index++) {
                char c = sb.charAt(index);
                if (Character.isLowerCase(c)) {
                    sb.setCharAt(index, Character.toUpperCase(c));
                } else {
                    sb.setCharAt(index, Character.toLowerCase(c));
                }
            }

            Log.d("Drawable name lowercase= ", sb.toString());

            int resId = getResources().getIdentifier(sb.toString(), "drawable", getActivity().getPackageName());

            Drawable d;

            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    d = getActivity().getResources().getDrawable(resId, getActivity().getTheme());
                } else {
                    d = getActivity().getResources().getDrawable(resId);
                }

                System.out.println("Drawable id = " + d);

                Glide.with(getActivity()).load(resId).into(holder.imageView);

            }catch (Resources.NotFoundException e){

                System.out.println("Uknown Class Identifier");

                Glide.with(getActivity()).load(R.drawable.z).into(holder.imageView);

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