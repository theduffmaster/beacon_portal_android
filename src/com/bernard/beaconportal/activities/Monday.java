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

public class Monday extends Fragment {

	private String BBand, CBand, FBand, DBand, EBand, HBand, ABand;

	private EditText editBBand;
	private EditText editCBand;
	private EditText editFBand;
	private EditText editEBand;
	private EditText editDBand;
	private EditText editHBand;
	private EditText editABand;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"monday", Context.MODE_PRIVATE);
		ABand = sharedpref.getString("a_Band", null);
		BBand = sharedpref.getString("b_Band", null);
		CBand = sharedpref.getString("c_Band", null);
		DBand = sharedpref.getString("d_Band", null);
		EBand = sharedpref.getString("e_Band", null);
		FBand = sharedpref.getString("f_Band", null);
		HBand = sharedpref.getString("h_Band", null);

		View view = inflater.inflate(R.layout.monday, container, false);

		TextView AValue = (TextView) view.findViewById(R.id.bandText11);
		AValue.setText("A Band");

		editABand = ((EditText) view.findViewById(R.id.editText11));
		editABand.setText(ABand, TextView.BufferType.EDITABLE);

		TextView BValue = (TextView) view.findViewById(R.id.bandText22);
		BValue.setText("B Band");

		editBBand = ((EditText) view.findViewById(R.id.editText22));
		editBBand.setText(BBand, TextView.BufferType.EDITABLE);

		TextView CValue = (TextView) view.findViewById(R.id.bandText33);
		CValue.setText("C Band");

		editCBand = ((EditText) view.findViewById(R.id.editText33));
		editCBand.setText(CBand, TextView.BufferType.EDITABLE);

		TextView DValue = (TextView) view.findViewById(R.id.bandText44);
		DValue.setText("D Band");

		editDBand = ((EditText) view.findViewById(R.id.editText44));
		editDBand.setText(DBand, TextView.BufferType.EDITABLE);

		TextView EValue = (TextView) view.findViewById(R.id.bandText55);
		EValue.setText("E Band");

		editEBand = ((EditText) view.findViewById(R.id.editText55));
		editEBand.setText(EBand, TextView.BufferType.EDITABLE);

		TextView FValue = (TextView) view.findViewById(R.id.bandText66);
		FValue.setText("F Band");

		editFBand = ((EditText) view.findViewById(R.id.editText66));
		editFBand.setText(FBand, TextView.BufferType.EDITABLE);

		TextView HValue = (TextView) view.findViewById(R.id.bandText77);
		HValue.setText("H Band");

		editHBand = ((EditText) view.findViewById(R.id.editText77));
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
					.getSharedPreferences("monday", Context.MODE_PRIVATE)
					.edit();

			localEditor.putString("a_Band", editABand.getText().toString());
			localEditor.putString("b_Band", editBBand.getText().toString());
			localEditor.putString("c_Band", editCBand.getText().toString());
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