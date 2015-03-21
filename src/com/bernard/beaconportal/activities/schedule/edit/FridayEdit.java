package com.bernard.beaconportal.activities.schedule.edit;

import com.bernard.beaconportal.activities.MainActivity;
import com.bernard.beaconportal.activities.R;
import com.bernard.beaconportal.activities.R.id;
import com.bernard.beaconportal.activities.R.layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FridayEdit extends Fragment {

	private String GBand, BBand, ADV1Band, ADV2Band, CBand, FBand;

	private EditText editADV1Band;
	private EditText editADV2Band;
	private EditText editBBand;
	private EditText editCBand;
	private EditText editFBand;
	private EditText editGBand;

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle)

	{

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"friday", Context.MODE_PRIVATE);
		GBand = sharedpref.getString("g_Band", null);
		BBand = sharedpref.getString("b_Band", null);
		ADV1Band = sharedpref.getString("adv1_Band", null);
		ADV2Band = sharedpref.getString("adv2_Band", null);
		CBand = sharedpref.getString("c_Band", null);
		FBand = sharedpref.getString("f_Band", null);

		View localView = paramLayoutInflater.inflate(R.layout.friday,
				paramViewGroup, false);

		((TextView) localView.findViewById(R.id.bandText111111))
				.setText("G Band");

		editGBand = ((EditText) localView.findViewById(R.id.editText111111));
		editGBand.setText(GBand, TextView.BufferType.EDITABLE);

		((TextView) localView.findViewById(R.id.bandText222222))
				.setText("B Band");

		editBBand = ((EditText) localView.findViewById(R.id.editText222222));
		editBBand.setText(BBand, TextView.BufferType.EDITABLE);

		((TextView) localView.findViewById(R.id.bandText333333))
				.setText("Advisory 1");

		editADV1Band = ((EditText) localView.findViewById(R.id.editText333333));
		editADV1Band.setText(ADV1Band, TextView.BufferType.EDITABLE);

		((TextView) localView.findViewById(R.id.bandText444444))
				.setText("Advisory 2");

		editADV2Band = ((EditText) localView.findViewById(R.id.editText444444));
		editADV2Band.setText(ADV2Band, TextView.BufferType.EDITABLE);

		((TextView) localView.findViewById(R.id.bandText555555))
				.setText("C Band");

		editCBand = ((EditText) localView.findViewById(R.id.editText555555));
		editCBand.setText(CBand, TextView.BufferType.EDITABLE);

		((TextView) localView.findViewById(R.id.bandText666666))
				.setText("F Band");

		editFBand = ((EditText) localView.findViewById(R.id.editText666666));
		editFBand.setText(FBand, TextView.BufferType.EDITABLE);

		// check to see if broadcast launched here

		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				this.mClickedReceiver, new IntentFilter("clicked!"));

		return localView;
	}

	// broadcast receiver with shared preferences is here

	private BroadcastReceiver mClickedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramAnonymousContext,
				Intent paramAnonymousIntent) {

			// all the shared preferences stuff
			SharedPreferences.Editor localEditor = FridayEdit.this
					.getActivity()
					.getSharedPreferences("friday", Context.MODE_PRIVATE)
					.edit();

			localEditor.putString("g_Band", FridayEdit.this.editGBand.getText()
					.toString());
			localEditor.putString("b_Band", FridayEdit.this.editBBand.getText()
					.toString());
			localEditor.putString("adv1_Band", FridayEdit.this.editADV1Band
					.getText().toString());
			localEditor.putString("adv2_Band", FridayEdit.this.editADV2Band
					.getText().toString());
			localEditor.putString("c_Band", FridayEdit.this.editCBand.getText()
					.toString());
			localEditor.putString("f_Band", FridayEdit.this.editFBand.getText()
					.toString());

			localEditor.apply();

			Log.d("receiver", "Got message");

			Intent localIntent = new Intent(FridayEdit.this.getActivity(),
					MainActivity.class);
			FridayEdit.this.startActivity(localIntent);
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