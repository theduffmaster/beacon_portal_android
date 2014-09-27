/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bernard.beaconportal.activities;

import com.bernard.beaconportal.activities.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CopyOfColorPickerDialog1 extends Dialog implements
		ColorPickerView.OnColorChangedListener, View.OnClickListener {

	private ColorPickerView mColorPicker;

	private ColorPickerPanelView mOldColor;
	private ColorPickerPanelView mNewColor;

	private ColorPickerPanelView mWhite;
	private ColorPickerPanelView mBlack;
	private ColorPickerPanelView mCyan;
	private ColorPickerPanelView mRed;
	private ColorPickerPanelView mGreen;
	private ColorPickerPanelView mYellow;

	private EditText mHex;
	private ImageButton mSetButton;

	private OnColorChangedListener mListener;

	public interface OnColorChangedListener {
		public void onColorChanged(int color);
	}

	public CopyOfColorPickerDialog1(Context context, int initialColor) {
		super(context);

		init(initialColor);
	}

	private void init(int color) {
		// To fight color branding.
		getWindow().setFormat(PixelFormat.RGBA_8888);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setUp(color);

	}

	private void setUp(int color) {

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.dialog_color_picker, null);

		setContentView(layout);

		setTitle(R.string.dialog_color_picker);

		mColorPicker = (ColorPickerView) layout
				.findViewById(R.id.color_picker_view);
		mOldColor = (ColorPickerPanelView) layout
				.findViewById(R.id.old_color_panel);
		mNewColor = (ColorPickerPanelView) layout
				.findViewById(R.id.new_color_panel);

		mWhite = (ColorPickerPanelView) layout.findViewById(R.id.white_panel);
		mBlack = (ColorPickerPanelView) layout.findViewById(R.id.black_panel);
		mCyan = (ColorPickerPanelView) layout.findViewById(R.id.cyan_panel);
		mRed = (ColorPickerPanelView) layout.findViewById(R.id.red_panel);
		mGreen = (ColorPickerPanelView) layout.findViewById(R.id.green_panel);
		mYellow = (ColorPickerPanelView) layout.findViewById(R.id.yellow_panel);

		mHex = (EditText) layout.findViewById(R.id.hex);
		mSetButton = (ImageButton) layout.findViewById(R.id.enter);

		((LinearLayout) mOldColor.getParent()).setPadding(
				Math.round(mColorPicker.getDrawingOffset()), 0,
				Math.round(mColorPicker.getDrawingOffset()), 0);

		mOldColor.setOnClickListener(this);
		mNewColor.setOnClickListener(this);
		mColorPicker.setOnColorChangedListener(this);
		mOldColor.setColor(color);
		mColorPicker.setColor(color, true);

		setColorAndClickAction(mWhite, 0xff8e44ad);
		setColorAndClickAction(mBlack, 0xfff39c12);
		setColorAndClickAction(mCyan, 0xff3498db);
		setColorAndClickAction(mRed, 0xffe74c3c);
		setColorAndClickAction(mGreen, 0xff2ecc71);
		setColorAndClickAction(mYellow, 0xfff1c40f);

		SharedPreferences sharedpref = getContext().getSharedPreferences(
				"actionbar_color", Context.MODE_PRIVATE);

		int color1 = sharedpref.getInt("Old_Color", 0);

		if (color1 == 0) {

			mOldColor.setColor(-16602636);
			mColorPicker.setColor(-16602636);

		} else {

			System.out.println(color1);

			mOldColor.setColor(color1);
			mColorPicker.setColor(color1);

		}

		if (mHex != null) {
			mHex.setText(ColorPickerPreference.convertToARGB(color));
		}
		if (mSetButton != null) {
			mSetButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String text = mHex.getText().toString();
					try {
						int newColor = ColorPickerPreference
								.convertToColorInt(text);
						mColorPicker.setColor(newColor, true);

					} catch (Exception e) {
					}
				}
			});
		}

	}

	@Override
	public void onColorChanged(int color) {

		mNewColor.setColor(color);
		try {
			if (mHex != null) {
				mHex.setText(ColorPickerPreference.convertToARGB(color));

			}
		} catch (Exception e) {

		}
		/*
		 * if (mListener != null) { mListener.onColorChanged(color); }
		 */

	}

	public void setAlphaSliderVisible(boolean visible) {
		mColorPicker.setAlphaSliderVisible(visible);
	}

	public void setColorAndClickAction(ColorPickerPanelView previewRect,
			final int color) {
		if (previewRect != null) {
			previewRect.setColor(color);
			previewRect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						mColorPicker.setColor(color, true);

						Log.d("ColorPicker1", "Color Picked");

					} catch (Exception e) {
					}
				}
			});
		}
	}

	/**
	 * Set a OnColorChangedListener to get notified when the color selected by
	 * the user has changed.
	 * 
	 * @param listener
	 */
	public void setOnColorChangedListener(OnColorChangedListener listener) {
		mListener = listener;
	}

	public int getColor() {
		return mColorPicker.getColor();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.new_color_panel) {
			if (mListener != null) {
				mListener.onColorChanged(mNewColor.getColor());

			}
		}

		int color = mColorPicker.getColor();

		System.out.println(color);

		String mNewColor = ColorPickerPreference.convertToARGB(color)
				.toString();

		SharedPreferences.Editor localEditor = getContext()
				.getSharedPreferences("actionbar_color", Context.MODE_PRIVATE)
				.edit();

		localEditor.putString("actionbar_color", mNewColor);
		localEditor.putInt("Old_Color", color);

		localEditor.commit();

		SharedPreferences.Editor localEditor1 = getContext()
				.getSharedPreferences("return_to_main", Context.MODE_PRIVATE)
				.edit();

		// localEditor1.putString("fragment_to_start", "3");
		localEditor1.putString("fragment_to_start", "2");

		localEditor1.commit();

		Intent i = new Intent(getContext(), MainActivity.class);
		i.putExtra("toOpen", "fragment 1");
		getContext().startActivity(i);

		dismiss();

	}

}