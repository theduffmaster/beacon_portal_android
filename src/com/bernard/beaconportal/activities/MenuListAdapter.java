package com.bernard.beaconportal.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MenuListAdapter extends BaseAdapter {

	Context context;
	String[] mTitle;
	String[] mCount;
	int[] mIcon;
	LayoutInflater inflater;

	public MenuListAdapter(Context context, String[] title, int[] icon,
			String[] count) {
		this.context = context;
		this.mTitle = title;
		this.mIcon = icon;
		this.mCount = count;
	}

	@Override
	public int getCount() {
		return mTitle.length;
	}

	@Override
	public Object getItem(int position) {
		return mTitle[position];
	}

	public Object getImage(int position) {
		return mIcon[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TextView txtTitle;

		TextView txtCount;

        ImageView imgIcon;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.drawer_list_item1, parent,
				false);

		Log.d("Creating Drawer ListView", "Yes");

		txtTitle = (TextView) itemView.findViewById(R.id.title);

		txtCount = (TextView) itemView.findViewById(R.id.counter);

		imgIcon = (ImageView) itemView.findViewById(R.id.icon);
		
		txtTitle.setText(mTitle[position]);

		txtCount.setText(mCount[position]);

        Glide.with(context).load(mIcon[position]).into(imgIcon);

		// if(position == selectedItem + 1){
		//
		// ColorFilter filter = new LightingColorFilter( Color.BLACK,
		// Color.BLACK);
		//
		// imgIcon.setColorFilter(filter);
		//
		// txtTitle.setTextColor(Color.BLACK);
		//
		// Log.d("Creating Drawer ListView", "Selected item" +
		// Integer.toString(selectedItem + 1));
		//
		// Log.d("Creating Drawer ListView", "Position" +
		// Integer.toString(position));
		//
		// }

		return itemView;
	}
}