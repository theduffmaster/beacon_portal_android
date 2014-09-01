package com.bernard.beaconportal.activities;

import java.util.ArrayList;
import java.util.List;

import com.bernard.beaconportal.activities.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentSettings extends Fragment {

	private List<Settings> settings;

	private ArrayList<String> mSelectedItems;

	private String actionbar_colors, background_colors, actionbar_colorcheck,
			background_colorcheck;

	static class ViewHolder {

		public TextView TitleText;
		public TextView ColorText;
		public View View_Color;
	}

	private CheckBox refresh;

	private View view;

	public static final String KEY_HOMEWORK = "homework";
	public static final String KEY_DESC = "desc";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.settings, container, false);

		addListenerOnChkWindows();

		RelativeLayout relativelayout = (RelativeLayout) view
				.findViewById(R.id.Relative_Layout_Settings);

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

										Intent i = new Intent(getActivity(),
												MainActivity.class);
										i.putExtra("toOpen", "fragment 1");
										getActivity().startActivity(i);

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

		SharedPreferences pref = getActivity().getSharedPreferences("CheckBox",
				Context.MODE_PRIVATE);

		String checkbox = pref.getString("checked", null);

		if (checkbox != null) {

			if (checkbox.contains("true")) {
				refresh.setChecked(true);

				TextView text1 = (TextView) view
						.findViewById(R.id.textViewSubTitle_Start);
				text1.setText("HomeworkDue will now refresh on app start");

			}

			if (checkbox.contains("false")) {
				refresh.setChecked(false);
				TextView text1 = (TextView) view
						.findViewById(R.id.textViewSubTitle_Start);
				text1.setText("HomeworkDue won't refresh on app start, this is the default");
			}

		}

		return view;

	}

	private String getString(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		addListenerOnChkWindows();

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		if (!sharedpref.contains("actionbar_color")) {

			actionbar_colors = "#03a9f4";

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

				if (clickedhomeworkdue.getTitle().equals("Set Tab Color")) {

					FragmentManager setcolor1 = getActivity()
							.getSupportFragmentManager();
					ColorPickerDialog colorpickerDialog = new ColorPickerDialog(
							getActivity(), position);
					colorpickerDialog.show();
				}

				if (clickedhomeworkdue.getTitle().equals("Set ActionBar Color")) {
					FragmentManager setcolor1 = getActivity()
							.getSupportFragmentManager();
					CopyOfColorPickerDialog1 colorpickerDialog = new CopyOfColorPickerDialog1(
							getActivity(), position);
					colorpickerDialog.show();

				}

			}
		});
	}

	private void populatehomeworkdueList() {

		settings.add(new Settings("Set Tab Color", "Set the Color of the Tabs",
				background_colors));

		settings.add(new Settings("Set ActionBar Color",
				"Set the ActionBar Background Color", actionbar_colors));

	}

	private void populateListView() {
		ArrayAdapter<Settings> adapter = new MyListAdapter();
		ListView list = (ListView) getView()
				.findViewById(R.id.listViewSettings);
		list.setAdapter(adapter);

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

			holder.View_Color.setBackgroundColor(Color
					.parseColor(currentsettings.getView_Color()));

			return convertView;

		}

	}

	public void addListenerOnChkWindows() {

		refresh = (CheckBox) view.findViewById(R.id.checkBox1);

		refresh.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				SharedPreferences.Editor editor = getActivity()
						.getSharedPreferences("CheckBox", Context.MODE_PRIVATE)
						.edit();

				if ((buttonView.isChecked())) {

					editor.putString("checked", "true");
					editor.commit();
				} else {

					editor.putString("checked", "false");
					editor.commit();

				}

			}

		});

	}

}
