package com.bernard.beaconportal.activities;

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

public class Wednesday extends Fragment {

	private String GBand, BBand, FBand, DBand, EBand, HBand, ABand;

	private EditText editBBand;
	private EditText editFBand;
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
				"wednesday", Context.MODE_PRIVATE);
		ABand = sharedpref.getString("a_Band", null);
		BBand = sharedpref.getString("b_Band", null);
		GBand = sharedpref.getString("g_Band", null);
		DBand = sharedpref.getString("d_Band", null);
		EBand = sharedpref.getString("e_Band", null);
		FBand = sharedpref.getString("f_Band", null);
		HBand = sharedpref.getString("h_Band", null);

		View view = inflater.inflate(R.layout.wednesday, container, false);

		TextView AValue = (TextView) view.findViewById(R.id.bandText1111);
		AValue.setText("A Band");

		editABand = ((EditText) view.findViewById(R.id.editText1111));
		editABand.setText(ABand, TextView.BufferType.EDITABLE);

		TextView BValue = (TextView) view.findViewById(R.id.bandText2222);
		BValue.setText("B Band");

		editBBand = ((EditText) view.findViewById(R.id.editText2222));
		editBBand.setText(BBand, TextView.BufferType.EDITABLE);

		TextView GValue = (TextView) view.findViewById(R.id.bandText3333);
		GValue.setText("G Band");

		editGBand = ((EditText) view.findViewById(R.id.editText3333));
		editGBand.setText(GBand, TextView.BufferType.EDITABLE);

		TextView DValue = (TextView) view.findViewById(R.id.bandText4444);
		DValue.setText("D Band");

		editDBand = ((EditText) view.findViewById(R.id.editText4444));
		editDBand.setText(DBand, TextView.BufferType.EDITABLE);

		TextView EValue = (TextView) view.findViewById(R.id.bandText5555);
		EValue.setText("E Band");

		editEBand = ((EditText) view.findViewById(R.id.editText5555));
		editEBand.setText(EBand, TextView.BufferType.EDITABLE);

		TextView FValue = (TextView) view.findViewById(R.id.bandText6666);
		FValue.setText("F Band");

		editFBand = ((EditText) view.findViewById(R.id.editText6666));
		editFBand.setText(FBand, TextView.BufferType.EDITABLE);

		TextView HValue = (TextView) view.findViewById(R.id.bandText7777);
		HValue.setText("H Band");

		editHBand = ((EditText) view.findViewById(R.id.editText7777));
		editHBand.setText(HBand, TextView.BufferType.EDITABLE);

		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				this.mClickedReceiver, new IntentFilter("clicked!"));

		return view;

	}

	private BroadcastReceiver mClickedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramAnonymousContext,
				Intent paramAnonymousIntent) {

			SharedPreferences.Editor localEditor = getActivity()
					.getSharedPreferences("wednesday", Context.MODE_PRIVATE)
					.edit();

			localEditor.putString("a_Band", editABand.getText().toString());
			localEditor.putString("b_Band", editBBand.getText().toString());
			localEditor.putString("g_Band", editGBand.getText().toString());
			localEditor.putString("d_Band", editDBand.getText().toString());
			localEditor.putString("e_Band", editEBand.getText().toString());
			localEditor.putString("f_Band", editFBand.getText().toString());
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
