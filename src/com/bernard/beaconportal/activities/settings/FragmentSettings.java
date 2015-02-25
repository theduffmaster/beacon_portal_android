package com.bernard.beaconportal.activities.settings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.bernard.beaconportal.activities.MainActivity;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.R.array;
import com.bernard.beaconportal.activities.R.id;
import com.bernard.beaconportal.activities.R.layout;
import com.bernard.beaconportal.activities.homeworkdue.alarms.DailyHomeworkDownload;
import com.bernard.beaconportal.activities.settings.colorpicker.ColorPickerDialogSecondary;
import com.bernard.beaconportal.activities.settings.colorpicker.ColorPickerDialogTheme;
import com.commonsware.cwac.merge.MergeAdapter;

public class FragmentSettings extends Fragment {

	private List<Settings> settings;

	private ArrayList<String> mSelectedItems;

	private String actionbar_colors, background_colors;

	private MergeAdapter merge;

	static class ViewHolder {

		public TextView TitleText;
		public TextView ColorText;
		public View View_Color;
	}

	private CheckBox refresh_swipe;

	private CheckBox refresh_edit;

	final static int RQS_1 = 1;

	private int hour;
	private int minute;
	private String time;
	private View view;

	private View relative_views;

	private RelativeLayout relativelayout;

	private RelativeLayout relativerefresh;

	public static final String KEY_HOMEWORK = "homework";
	public static final String KEY_DESC = "desc";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.settings, container, false);

		relative_views = View.inflate(getActivity(), R.layout.settings_layouts,
				null);

		ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();

		actionBar.setElevation(2);

		merge = new MergeAdapter();

		relativerefresh = (RelativeLayout) relative_views
				.findViewById(R.id.relative_Layout_Settings_Refresh);

		relativelayout = (RelativeLayout) relative_views
				.findViewById(R.id.Relative_Layout_Settings);

		relativerefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				showDatePicker();

			}

		});

		SharedPreferences refresh_time = getActivity().getSharedPreferences(
				"Alarm", Context.MODE_PRIVATE);

		time = refresh_time.getString("Time", "3:00pm");

		TextView dateSubText = (TextView) relative_views
				.findViewById(R.id.textViewSubTitle_Refresh);

		dateSubText.setText("A notification is set to be issued at " + time);
		
		relativelayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				mSelectedItems = new ArrayList();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				builder.setTitle("View to Show at Start")

						.setSingleChoiceItems(R.array.Start_View, 0, null)

						.setPositiveButton("set",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();

										int selectedPosition = ((AlertDialog) dialog)
												.getListView()
												.getCheckedItemPosition();

										Log.i("button selected", Integer
												.toString(selectedPosition));

										if (selectedPosition == 0) {

											SharedPreferences.Editor localEditor = getActivity()
													.getSharedPreferences(
															"show_view",
															Context.MODE_PRIVATE)
													.edit();

											String Show_View = "Schedule";

											localEditor.putString("show_view",
													Show_View);

											localEditor.commit();

										} else {

											SharedPreferences.Editor localEditor = getActivity()
													.getSharedPreferences(
															"show_view",
															Context.MODE_PRIVATE)
													.edit();

											String Show_View = "Homework Due";

											localEditor.putString("show_view",
													Show_View);

											localEditor.commit();

										}

										Intent intent = getActivity()
												.getIntent();
										getActivity()
												.overridePendingTransition(0, 0);
										intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
										getActivity().finish();

										getActivity()
												.overridePendingTransition(0, 0);
										startActivity(intent);

									}
								})
						.setNegativeButton("cancel",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {

										dialog.dismiss();

									}
								});

				AlertDialog alertDialog = builder.create();

				// show it
				alertDialog.show();

			}

		});

		refresh_edit = (CheckBox) relative_views
				.findViewById(R.id.checkBoxEdit);

		SharedPreferences prefer = getActivity().getSharedPreferences(
				"CheckBox_edit", Context.MODE_PRIVATE);

		String checkbox_edit = prefer.getString("checked", null);

		String subTitleEdit = prefer.getString("subTitle", null);

		if (checkbox_edit != null) {

			if (checkbox_edit.contains("true")) {
				refresh_edit.setChecked(true);

				if (subTitleEdit != null) {
					TextView SubTitle = (TextView) relative_views
							.findViewById(R.id.textViewSubTitle_Edit);
					SubTitle.setText(subTitleEdit);
					
					SubTitle.setTextColor(Color.parseColor(actionbar_colors));
				}
			}

		}

		refresh_swipe = (CheckBox) relative_views
				.findViewById(R.id.checkBoxSwipe);

		SharedPreferences preferer = getActivity().getSharedPreferences(
				"CheckBox_swipe", Context.MODE_PRIVATE);

		String checkbox_swipe = preferer.getString("checked", null);

		String subTitleSwipe = preferer.getString("subTitle", null);

		if (checkbox_swipe != null) {

			if (checkbox_swipe.contains("false")) {
				refresh_swipe.setChecked(false);

				TextView SubTitle = (TextView) relative_views
						.findViewById(R.id.textViewSubTitle_Swipe);
				SubTitle.setText(subTitleSwipe);
				
				SubTitle.setTextColor(Color.parseColor(actionbar_colors));

			} else {

				refresh_swipe.setChecked(true);

				if (subTitleSwipe != null) {
					TextView SubTitle = (TextView) relative_views
							.findViewById(R.id.textViewSubTitle_Swipe);
					SubTitle.setText(subTitleSwipe);
					
					SubTitle.setTextColor(Color.parseColor(actionbar_colors));
				}
			}

		} else {

			refresh_swipe.setChecked(true);

		}

		return view;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		addListenerOnChkButton();

		addListenerOnChkButtonSwipe();

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			actionbar_colors = "#4285f4";

		} else {

			actionbar_colors = sharedpref.getString("actionbar_color", null);

		}

		SharedPreferences sharedprefer = getActivity().getSharedPreferences(
				"background_color", Context.MODE_PRIVATE);

		if (!sharedprefer.contains("background_color")) {

			background_colors = "#ffffff";

		} else {

			background_colors = sharedprefer
					.getString("background_color", null);

		}

		settings = new ArrayList<Settings>();
		populatehomeworkdueList();
		populateListView();

		registerClickCallback();

		refresh_edit = (CheckBox) relative_views
				.findViewById(R.id.checkBoxEdit);

		SharedPreferences prefer = getActivity().getSharedPreferences(
				"CheckBox_edit", Context.MODE_PRIVATE);

		String checkbox_edit = prefer.getString("checked", null);

		String subTitleEdit = prefer.getString("subTitle", null);

		if (checkbox_edit != null) {

			if (checkbox_edit.contains("true")) {
				refresh_edit.setChecked(true);

				if (subTitleEdit != null) {
					TextView SubTitle = (TextView) relative_views
							.findViewById(R.id.textViewSubTitle_Edit);
					SubTitle.setText(subTitleEdit);
					
					SubTitle.setTextColor(Color.parseColor(actionbar_colors));
				}

			}

		}

		refresh_swipe = (CheckBox) relative_views
				.findViewById(R.id.checkBoxSwipe);

		SharedPreferences preferer = getActivity().getSharedPreferences(
				"CheckBox_swipe", Context.MODE_PRIVATE);

		String checkbox_swipe = preferer.getString("checked", null);

		String subTitleSwipe = preferer.getString("subTitle", null);

		if (checkbox_swipe != null) {

			if (checkbox_swipe.contains("false")) {
				refresh_swipe.setChecked(false);

				TextView SubTitle = (TextView) relative_views
						.findViewById(R.id.textViewSubTitle_Swipe);
				SubTitle.setText(subTitleSwipe);
				
				SubTitle.setTextColor(Color.parseColor(actionbar_colors));

			} else {

				refresh_swipe.setChecked(true);

				if (subTitleSwipe != null) {
					TextView SubTitle = (TextView) relative_views
							.findViewById(R.id.textViewSubTitle_Swipe);
					SubTitle.setText(subTitleSwipe);
					
				}
			}

		} else {

			refresh_swipe.setChecked(true);

		}
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = listView.getPaddingTop()
				+ listView.getPaddingBottom();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup)
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public void refreshColors() {

		Fragment frg = new FragmentSettings();

		final FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.detach(frg);
		ft.attach(frg);
		ft.detach(frg);
		ft.commit();
	}

	private void registerClickCallback() {
		ListView list = (ListView) getView()
				.findViewById(R.id.listViewSettings);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {

				Settings clickedhomeworkdue = settings.get(position);

				if (clickedhomeworkdue.getTitle().equals("Set Secondary Color")) {

					getActivity().getSupportFragmentManager();
					ColorPickerDialogSecondary colorpickerDialog = new ColorPickerDialogSecondary(
							getActivity(), position);
					colorpickerDialog.show();
				}

				if (clickedhomeworkdue.getTitle().equals("Set Theme Color")) {
					getActivity().getSupportFragmentManager();
					ColorPickerDialogTheme colorpickerDialog = new ColorPickerDialogTheme(
							getActivity(), position);
					colorpickerDialog.show();

				}

			}
		});
	}

	private void populatehomeworkdueList() {

		settings.add(new Settings("Set Theme Color",
				"Pick which color is used throughout the app ",
				actionbar_colors));

		settings.add(new Settings("Set Secondary Color",
				"Pick a color to compliment the theme color",
				background_colors));

	}

	private void populateListView() {
		ArrayAdapter<Settings> adapter = new MyListAdapter();
		ListView list = (ListView) getView()
				.findViewById(R.id.listViewSettings);
		// list.setAdapter(adapter);

		merge.addAdapter(adapter);

		merge.addView(relative_views);

		list.setAdapter(merge);

	}

	public class MyListAdapter extends ArrayAdapter<Settings> {
		public MyListAdapter() {
			super(getActivity(), R.layout.setting_color_picker, settings);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.setting_color_picker, parent, false);
				holder = new ViewHolder();

				holder.TitleText = (TextView) convertView
						.findViewById(R.id.textViewTitle);

				holder.ColorText = (TextView) convertView
						.findViewById(R.id.textViewColor);

				holder.View_Color = convertView.findViewById(R.id.viewColor);

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			Settings currentsettings = settings.get(position);

			holder.TitleText.setText(currentsettings.getTitle());

			holder.ColorText.setText(currentsettings.getColor());
			
			if(currentsettings.getTitle().equals("Set Theme Color")){
			
			holder.ColorText.setTextColor(Color.parseColor(actionbar_colors));

			}
			else if(currentsettings.getTitle().equals("Set Secondary Color") && !background_colors.equals("#ffffff")){
				
				holder.ColorText.setTextColor(Color.parseColor(background_colors));
				
			}
			
			holder.View_Color.setBackgroundColor(Color
					.parseColor(currentsettings.getView_Color()));

			return convertView;

		}

	}

	public void addListenerOnChkButton() {

		CheckBox floating = (CheckBox) relative_views
				.findViewById(R.id.checkBoxEdit);

		floating.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				SharedPreferences.Editor editor = getActivity()
						.getSharedPreferences("CheckBox_edit",
								Context.MODE_PRIVATE).edit();

				if ((buttonView.isChecked())) {

					String subTitle = "Edit Button moved to ActionBar";

					editor.putString("checked", "true");
					editor.putString("subTitle", subTitle);
					editor.commit();

					TextView SubTitle = (TextView) relative_views
							.findViewById(R.id.textViewSubTitle_Edit);
					SubTitle.setText(subTitle);
					
					SubTitle.setTextColor(Color.parseColor(actionbar_colors));

				} else {

					String subTitle = "Edit Button not in ActionBar";

					editor.putString("checked", "false");
					editor.putString("subTitle", subTitle);
					editor.commit();

					TextView SubTitle = (TextView) relative_views
							.findViewById(R.id.textViewSubTitle_Edit);
					SubTitle.setText(subTitle);
					
					SubTitle.setTextColor(Color.parseColor(actionbar_colors));

				}

			}

		});
	}

	public void addListenerOnChkButtonSwipe() {

		CheckBox floating = (CheckBox) relative_views
				.findViewById(R.id.checkBoxSwipe);

		floating.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				SharedPreferences.Editor editor = getActivity()
						.getSharedPreferences("CheckBox_swipe",
								Context.MODE_PRIVATE).edit();

				if ((buttonView.isChecked())) {

					String subTitle = "Homework Items are Swipeable";

					editor.putString("checked", "true");
					editor.putString("subTitle", subTitle);
					editor.commit();

					TextView SubTitle = (TextView) relative_views
							.findViewById(R.id.textViewSubTitle_Swipe);
					SubTitle.setText(subTitle);
					
					SubTitle.setTextColor(Color.parseColor(actionbar_colors));

				} else {

					String subTitle = "Homework Items are Not Swipeable";

					editor.putString("checked", "false");
					editor.putString("subTitle", subTitle);
					editor.commit();

					TextView SubTitle = (TextView) relative_views
							.findViewById(R.id.textViewSubTitle_Swipe);
					SubTitle.setText(subTitle);
					
					SubTitle.setTextColor(Color.parseColor(actionbar_colors));

				}

			}

		});

	}

	public void showDatePicker() {
		// Initializiation
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
				getActivity());
		View customView = inflater
				.inflate(R.layout.download_alarm_dialog, null);
		dialogBuilder.setView(customView);
		// View settings
		dialogBuilder.setTitle("Set Notification Creation Time");

		final TimePicker timePicker = (TimePicker) customView
				.findViewById(R.id.timePicker1);

		Calendar choosenDate = Calendar.getInstance();
		hour = choosenDate.get(Calendar.HOUR_OF_DAY);
		minute = choosenDate.get(Calendar.MINUTE);

		SharedPreferences refresh_time = getActivity().getSharedPreferences(
				"Alarm", Context.MODE_PRIVATE);

		try {

			hour = refresh_time.getInt("Hour", 15);

			minute = refresh_time.getInt("Minute", 0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Buttons
		dialogBuilder.setNegativeButton("Stop",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						cancelAlarm();

						Toast.makeText(getActivity(),
								"Homework will no longer update automatically",
								Toast.LENGTH_LONG).show();

						SharedPreferences.Editor localEditor = getActivity()
								.getSharedPreferences("Alarm",
										Context.MODE_PRIVATE).edit();

						localEditor.putString("Time",
								"Homework will not be refreshed automatically");

						localEditor.apply();

						TextView dateSubText = (TextView) relative_views
								.findViewById(R.id.textViewSubTitle_Refresh);

						dateSubText
								.setText("Homework will not be refreshed automatically");

					}
				});
		dialogBuilder.setNeutralButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		dialogBuilder.setPositiveButton("Set Time",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						SharedPreferences.Editor localEditor = getActivity()
								.getSharedPreferences("Alarm",
										Context.MODE_PRIVATE).edit();

						hour = timePicker.getCurrentHour();

						minute = timePicker.getCurrentMinute();

						localEditor.putInt("Hour", hour);

						localEditor.putInt("Minute", minute);

						if (hour > 11) {
							if (minute < 10) {

								time = Integer.toString(hour - 12) + ":" + "0"
										+ Integer.toString(minute) + "pm";

							} else {

								time = Integer.toString(hour - 12) + ":"
										+ Integer.toString(minute) + "pm";

							}

						}

						if (hour <= 11) {

							if (minute < 10) {

								time = Integer.toString(hour) + ":" + "0"
										+ Integer.toString(minute) + "am";

							} else {

								time = Integer.toString(hour) + ":"
										+ Integer.toString(minute) + "am";

							}

						}

						TextView dateSubText = (TextView) relative_views
								.findViewById(R.id.textViewSubTitle_Refresh);

						dateSubText.setText("Homework will refresh at " + time);

						localEditor.putString("Time", time);

						localEditor.apply();

						Toast.makeText(getActivity(),
								"Homework set to refresh at " + time,
								Toast.LENGTH_LONG).show();

						Calendar calNow = Calendar.getInstance();
						Calendar calSet = (Calendar) calNow.clone();

						calSet.set(Calendar.HOUR_OF_DAY,
								timePicker.getCurrentHour());
						calSet.set(Calendar.MINUTE,
								timePicker.getCurrentMinute());
						calSet.set(Calendar.SECOND, 0);
						calSet.set(Calendar.MILLISECOND, 0);

						setAlarm(calSet);

					}
				});
		final AlertDialog dialog = dialogBuilder.create();
		// Initialize datepicker in dialog atepicker
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay,
					int minuteNow) {

				hour = hourOfDay;
				minute = minuteNow;

			}

		};

		dialog.show();
	}

	private void setAlarm(Calendar targetCal) {

		Intent intent = new Intent(getActivity().getBaseContext(),
				DailyHomeworkDownload.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity()
				.getBaseContext(), RQS_1, intent, 0);

		AlarmManager alarmManager = (AlarmManager) getActivity()
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
				pendingIntent);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				targetCal.getTimeInMillis(), TimeUnit.HOURS.toMillis(24),
				pendingIntent);

		System.out.println(targetCal);

	}

	private void cancelAlarm() {

		Intent intent = new Intent(getActivity().getBaseContext(),
				DailyHomeworkDownload.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity()
				.getBaseContext(), RQS_1, intent, 0);

		AlarmManager alarmManager = (AlarmManager) getActivity()
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

	}

}
