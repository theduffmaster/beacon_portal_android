package com.bernard.beaconportal.activities;

import java.util.ArrayList;
import java.util.List;

import com.bernard.beaconportal.activities.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Linked_Bands extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.schedule_list_view, container, false);
	}

	private List<schedule_linked> myschedule;

	static class ViewHolder {

		public TextView HomeworkDueText;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myschedule = new ArrayList<schedule_linked>();
		populatescheduleList();
		populateListView();

	}

	private void populatescheduleList() {
		myschedule.add(new schedule_linked("A Band"));
		myschedule.add(new schedule_linked("B Band"));
		myschedule.add(new schedule_linked("C Band"));
		myschedule.add(new schedule_linked("D Band"));
		myschedule.add(new schedule_linked("E Band"));
		myschedule.add(new schedule_linked("F Band"));
		myschedule.add(new schedule_linked("G Band"));
		myschedule.add(new schedule_linked("H Band"));
		myschedule.add(new schedule_linked("Advisory 1"));
		myschedule.add(new schedule_linked("Advisory 2"));
	}

	private void populateListView() {
		ArrayAdapter<schedule_linked> adapter = new MyListAdapter();
		ListView list = (ListView) getView().findViewById(R.id.listView2);
		list.setAdapter(adapter);

	}

	public class MyListAdapter extends ArrayAdapter<schedule_linked> {
		public MyListAdapter() {
			super(getActivity(), R.layout.item_view, myschedule);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.schedule_list_item, parent, false);
				holder = new ViewHolder();

				holder.HomeworkDueText = (TextView) convertView
						.findViewById(R.id.bandText);

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}
			schedule_linked currenthomeworkdue = myschedule.get(position);
			holder.HomeworkDueText.setText(currenthomeworkdue.Band());
			return convertView;

		}

	}

}