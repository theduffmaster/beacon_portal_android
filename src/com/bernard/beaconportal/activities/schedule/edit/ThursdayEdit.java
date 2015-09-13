package com.bernard.beaconportal.activities.schedule.edit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bernard.beaconportal.activities.R;

public class ThursdayEdit extends Fragment {

	private String GBand, CBand, FBand, DBand, EBand, HBand, BBand;

	private EditText editCBand;
	private EditText editFBand;
	private EditText editGBand;
	private EditText editEBand;
	private EditText editDBand;
	private EditText editHBand;
	private EditText editBBand;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"thursday", Context.MODE_PRIVATE);
		GBand = sharedpref.getString("g_Band", null);
		EBand = sharedpref.getString("e_Band", null);
		DBand = sharedpref.getString("d_Band", null);
		CBand = sharedpref.getString("c_Band", null);
		FBand = sharedpref.getString("f_Band", null);
		HBand = sharedpref.getString("h_Band", null);
		BBand = sharedpref.getString("b_Band", null);

		View view = inflater.inflate(R.layout.thursday, container, false);

		TextView HValue = (TextView) view.findViewById(R.id.bandText11111);
		HValue.setText("H Band");

		editHBand = ((EditText) view.findViewById(R.id.editText11111));
		editHBand.setText(HBand, TextView.BufferType.EDITABLE);

		TextView FValue = (TextView) view.findViewById(R.id.bandText22222);
		FValue.setText("F Band");

		editFBand = ((EditText) view.findViewById(R.id.editText22222));
		editFBand.setText(FBand, TextView.BufferType.EDITABLE);

		TextView CValue = (TextView) view.findViewById(R.id.bandText33333);
		CValue.setText("C Band");

		editCBand = ((EditText) view.findViewById(R.id.editText33333));
		editCBand.setText(CBand, TextView.BufferType.EDITABLE);

		TextView DValue = (TextView) view.findViewById(R.id.bandText44444);
		DValue.setText("D Band");

		editDBand = ((EditText) view.findViewById(R.id.editText44444));
		editDBand.setText(DBand, TextView.BufferType.EDITABLE);

		TextView EValue = (TextView) view.findViewById(R.id.bandText55555);
		EValue.setText("E Band");

		editEBand = ((EditText) view.findViewById(R.id.editText55555));
		editEBand.setText(EBand, TextView.BufferType.EDITABLE);

		TextView BValue = (TextView) view.findViewById(R.id.bandText66666);
		BValue.setText("B Band");

		editBBand = ((EditText) view.findViewById(R.id.editText66666));
		editBBand.setText(BBand, TextView.BufferType.EDITABLE);

		TextView GValue = (TextView) view.findViewById(R.id.bandText77777);
		GValue.setText("G Band");

		editGBand = ((EditText) view.findViewById(R.id.editText77777));
		editGBand.setText(GBand, TextView.BufferType.EDITABLE);

		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				this.mClickedReceiver, new IntentFilter("clicked!"));

		return view;

	}

	private BroadcastReceiver mClickedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramAnonymousContext,
				Intent paramAnonymousIntent) {

			SharedPreferences.Editor localEditor = getActivity()
					.getSharedPreferences("thursday", Context.MODE_PRIVATE)
					.edit();

			localEditor.putString("b_Band", editBBand.getText().toString());
			localEditor.putString("c_Band", editCBand.getText().toString());
			localEditor.putString("d_Band", editDBand.getText().toString());
			localEditor.putString("e_Band", editEBand.getText().toString());
			localEditor.putString("f_Band", editFBand.getText().toString());
			localEditor.putString("g_Band", editGBand.getText().toString());
			localEditor.putString("h_Band", editHBand.getText().toString());

			localEditor.apply();

		}
	};

	@Override
	public void onDestroy()

	{

		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				this.mClickedReceiver);

		super.onDestroy();

	}

}