package com.bernard.beaconportal.activities;

public class Settings {
	private String title;
	private String color;

	private String view_color;

	public Settings(String title, String color, String view_color) {

		super();
		this.title = title;
		this.color = color;

		this.view_color = view_color;
	}

	public String getTitle() {
		return title;
	}

	public String getColor() {
		return color;
	}

	public String getView_Color() {
		return view_color;
	}

}
