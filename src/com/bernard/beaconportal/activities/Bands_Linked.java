package com.bernard.beaconportal.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class Bands_Linked extends Fragment {

	private EditText editADV1Band;
	private EditText editADV2Band;
	private EditText editBBand;
	private EditText editCBand;
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

		View view = inflater.inflate(R.layout.linked, container, false);

		TextView AValue = (TextView) view.findViewById(R.id.bandText);
		AValue.setText("A Band");

		editABand = ((EditText) view.findViewById(R.id.editText1));

		TextView BValue = (TextView) view.findViewById(R.id.bandText2);
		BValue.setText("B Band");

		editBBand = ((EditText) view.findViewById(R.id.editText2));

		TextView CValue = (TextView) view.findViewById(R.id.bandText3);
		CValue.setText("C Band");

		editCBand = ((EditText) view.findViewById(R.id.editText3));

		TextView DValue = (TextView) view.findViewById(R.id.bandText4);
		DValue.setText("D Band");

		editDBand = ((EditText) view.findViewById(R.id.editText4));

		TextView EValue = (TextView) view.findViewById(R.id.bandText5);
		EValue.setText("E Band");

		editEBand = ((EditText) view.findViewById(R.id.editText5));

		TextView FValue = (TextView) view.findViewById(R.id.bandText6);
		FValue.setText("F Band");

		editFBand = ((EditText) view.findViewById(R.id.editText6));

		TextView GValue = (TextView) view.findViewById(R.id.bandText7);
		GValue.setText("G Band");

		editGBand = ((EditText) view.findViewById(R.id.editText7));

		TextView HValue = (TextView) view.findViewById(R.id.bandText8);
		HValue.setText("H Band");

		editHBand = ((EditText) view.findViewById(R.id.editText8));

		TextView ADV1Value = (TextView) view.findViewById(R.id.bandText9);
		ADV1Value.setText("Advisory 1");

		editADV1Band = ((EditText) view.findViewById(R.id.editText9));

		TextView ADV2Value = (TextView) view.findViewById(R.id.bandText10);
		ADV2Value.setText("Advisory 2");

		editADV2Band = ((EditText) view.findViewById(R.id.editText10));

		return view;

	}

	private boolean hasContent(EditText et) {

		boolean bHasContent = false;

		if (et.getText().toString().trim().length() > 0) {

			bHasContent = true;
		}
		return bHasContent;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.android_apply, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {

		case R.id.undo:

			Intent intent = new Intent(getActivity(), MainActivity.class);
			startActivity(intent);

			return true;
		default:
			return super.onOptionsItemSelected(item);

		case R.id.apply:

			SharedPreferences.Editor localEditor = getActivity()
					.getSharedPreferences("monday", Context.MODE_PRIVATE)
					.edit();

			if (hasContent(editABand)) {

				localEditor.putString("a_Band", editABand.getText().toString());

			} else {

			}

			if (hasContent(editBBand)) {

				localEditor.putString("b_Band", editBBand.getText().toString());

			} else {

			}

			if (hasContent(editCBand)) {

				localEditor.putString("c_Band", editCBand.getText().toString());

			} else {

			}

			if (hasContent(editDBand)) {

				localEditor.putString("d_Band", editDBand.getText().toString());

			} else {

			}

			if (hasContent(editEBand)) {

				localEditor.putString("e_Band", editEBand.getText().toString());

			} else {

			}

			if (hasContent(editFBand)) {

				localEditor.putString("f_Band", editFBand.getText().toString());

			} else {

			}

			if (hasContent(editHBand)) {

				localEditor.putString("h_Band", editHBand.getText().toString());

			} else {

			}

			localEditor.apply();

			SharedPreferences.Editor Editor = getActivity()
					.getSharedPreferences("friday", Context.MODE_PRIVATE)
					.edit();

			if (hasContent(editGBand)) {

				Editor.putString("g_Band", editGBand.getText().toString());

			} else {

			}

			if (hasContent(editBBand)) {

				Editor.putString("b_Band", editBBand.getText().toString());

			} else {

			}

			if (hasContent(editADV1Band)) {

				Editor.putString("adv1_Band", editADV1Band.getText().toString());

			} else {

			}

			if (hasContent(editADV2Band)) {

				Editor.putString("adv2_Band", editADV2Band.getText().toString());

			} else {

			}

			if (hasContent(editCBand)) {

				Editor.putString("c_Band", editCBand.getText().toString());

			} else {

			}

			if (hasContent(editFBand)) {

				Editor.putString("f_Band", editFBand.getText().toString());

			} else {

			}

			Editor.apply();

			SharedPreferences.Editor ditor = getActivity()
					.getSharedPreferences("thursday", Context.MODE_PRIVATE)
					.edit();

			if (hasContent(editABand)) {

				ditor.putString("a_Band", editABand.getText().toString());

			} else {

			}

			if (hasContent(editCBand)) {

				ditor.putString("c_Band", editCBand.getText().toString());

			} else {

			}

			if (hasContent(editDBand)) {

				ditor.putString("d_Band", editDBand.getText().toString());

			} else {

			}

			if (hasContent(editEBand)) {

				ditor.putString("e_Band", editEBand.getText().toString());

			} else {

			}

			if (hasContent(editFBand)) {

				ditor.putString("f_Band", editFBand.getText().toString());

			} else {

			}

			if (hasContent(editGBand)) {

				ditor.putString("g_Band", editGBand.getText().toString());

			} else {

			}

			if (hasContent(editHBand)) {

				ditor.putString("h_Band", editHBand.getText().toString());

			} else {

			}

			ditor.apply();

			SharedPreferences.Editor itor = getActivity().getSharedPreferences(
					"tuesday", Context.MODE_PRIVATE).edit();

			if (hasContent(editEBand)) {

				itor.putString("e_Band", editEBand.getText().toString());

			} else {

			}

			if (hasContent(editGBand)) {

				itor.putString("g_Band", editGBand.getText().toString());

			} else {

			}

			if (hasContent(editBBand)) {

				itor.putString("b_Band", editBBand.getText().toString());

			} else {

			}

			if (hasContent(editHBand)) {

				itor.putString("h_Band", editHBand.getText().toString());

			} else {

			}

			if (hasContent(editABand)) {

				itor.putString("a_Band", editABand.getText().toString());

			} else {

			}

			if (hasContent(editCBand)) {

				itor.putString("c_Band", editCBand.getText().toString());

			} else {

			}

			if (hasContent(editDBand)) {

				itor.putString("d_Band", editDBand.getText().toString());

			} else {

			}

			itor.apply();

			SharedPreferences.Editor tor = getActivity().getSharedPreferences(
					"wednesday", Context.MODE_PRIVATE).edit();

			if (hasContent(editABand)) {

				tor.putString("a_Band", editABand.getText().toString());

			} else {

			}

			if (hasContent(editBBand)) {

				tor.putString("b_Band", editBBand.getText().toString());

			} else {

			}

			if (hasContent(editGBand)) {

				tor.putString("g_Band", editGBand.getText().toString());

			} else {

			}

			if (hasContent(editDBand)) {

				tor.putString("d_Band", editDBand.getText().toString());

			} else {

			}

			if (hasContent(editEBand)) {

				tor.putString("e_Band", editEBand.getText().toString());

			} else {

			}

			if (hasContent(editFBand)) {

				tor.putString("f_Band", editFBand.getText().toString());

			} else {

			}

			if (hasContent(editHBand)) {

				tor.putString("h_Band", editHBand.getText().toString());

			} else {

			}

			tor.apply();

			SharedPreferences.Editor or = getActivity().getSharedPreferences(
					"batch", Context.MODE_PRIVATE).edit();

			if (hasContent(editABand)) {

				or.putString("a_Band", editABand.getText().toString());

			} else {

			}

			if (hasContent(editBBand)) {

				or.putString("b_Band", editBBand.getText().toString());

			} else {

			}

			if (hasContent(editCBand)) {

				or.putString("c_Band", editCBand.getText().toString());

			} else {

			}

			if (hasContent(editDBand)) {

				or.putString("d_Band", editDBand.getText().toString());

			} else {

			}

			if (hasContent(editEBand)) {

				or.putString("e_Band", editEBand.getText().toString());

			} else {

			}

			if (hasContent(editFBand)) {

				or.putString("f_Band", editFBand.getText().toString());

			} else {

			}

			if (hasContent(editGBand)) {

				or.putString("g_Band", editGBand.getText().toString());

			} else {

			}

			if (hasContent(editHBand)) {

				or.putString("h_Band", editHBand.getText().toString());

			} else {

			}

			if (hasContent(editADV1Band)) {

				or.putString("adv1_Band", editADV1Band.getText().toString());

			} else {

			}

			if (hasContent(editADV2Band)) {

				or.putString("adv2_Band", editADV2Band.getText().toString());

			} else {

			}

			or.apply();

			Intent localIntent = new Intent(getActivity(), MainActivity.class);
			startActivity(localIntent);

			return true;

		}
	}

}
