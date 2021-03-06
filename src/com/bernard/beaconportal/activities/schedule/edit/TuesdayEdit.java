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

public class TuesdayEdit extends Fragment {

	private String GBand, BBand, CBand, DBand, EBand, HBand, ABand;

	private EditText editBBand;
	private EditText editCBand;
	private EditText editGBand;
	private EditText editEBand;
	private EditText editDBand;
	private EditText editHBand;
	private EditText editABand;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"tuesday", Context.MODE_PRIVATE);
		EBand = sharedpref.getString("e_Band", null);
		GBand = sharedpref.getString("g_Band", null);
		BBand = sharedpref.getString("b_Band", null);
		HBand = sharedpref.getString("h_Band", null);
		ABand = sharedpref.getString("a_Band", null);
		CBand = sharedpref.getString("c_Band", null);
		DBand = sharedpref.getString("d_Band", null);

		View view = inflater.inflate(R.layout.tuesday, container, false);

		TextView HValue = (TextView) view.findViewById(R.id.bandText111);
		HValue.setText("H Band");

		editHBand = ((EditText) view.findViewById(R.id.editText111));
		editHBand.setText(HBand, TextView.BufferType.EDITABLE);

		TextView GValue = (TextView) view.findViewById(R.id.bandText222);
		GValue.setText("G Band");

		editGBand = ((EditText) view.findViewById(R.id.editText222));
		editGBand.setText(GBand, TextView.BufferType.EDITABLE);

		TextView DValue = (TextView) view.findViewById(R.id.bandText333);
		DValue.setText("D Band");

		editDBand = ((EditText) view.findViewById(R.id.editText333));
		editDBand.setText(DBand, TextView.BufferType.EDITABLE);

		TextView EValue = (TextView) view.findViewById(R.id.bandText444);
		EValue.setText("E Band");

		editEBand = ((EditText) view.findViewById(R.id.editText444));
		editEBand.setText(EBand, TextView.BufferType.EDITABLE);

		TextView CValue = (TextView) view.findViewById(R.id.bandText555);
		CValue.setText("C Band");

		editCBand = ((EditText) view.findViewById(R.id.editText555));
		editCBand.setText(CBand, TextView.BufferType.EDITABLE);

		TextView AValue = (TextView) view.findViewById(R.id.bandText666);
		AValue.setText("A Band");

		editABand = ((EditText) view.findViewById(R.id.editText666));
		editABand.setText(ABand, TextView.BufferType.EDITABLE);

		TextView BValue = (TextView) view.findViewById(R.id.bandText777);
		BValue.setText("B Band");

		editBBand = ((EditText) view.findViewById(R.id.editText777));
		editBBand.setText(BBand, TextView.BufferType.EDITABLE);

		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				this.mClickedReceiver, new IntentFilter("clicked!"));

		return view;

	}

	private BroadcastReceiver mClickedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramAnonymousContext,
				Intent paramAnonymousIntent) {

			SharedPreferences.Editor localEditor = getActivity()
					.getSharedPreferences("tuesday", Context.MODE_PRIVATE)
					.edit();

			localEditor.putString("e_Band", editEBand.getText().toString());
			localEditor.putString("g_Band", editGBand.getText().toString());
			localEditor.putString("b_Band", editBBand.getText().toString());
			localEditor.putString("h_Band", editHBand.getText().toString());
			localEditor.putString("a_Band", editABand.getText().toString());
			localEditor.putString("c_Band", editCBand.getText().toString());
			localEditor.putString("d_Band", editDBand.getText().toString());

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
