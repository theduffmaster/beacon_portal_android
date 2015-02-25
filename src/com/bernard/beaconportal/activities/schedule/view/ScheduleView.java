package com.bernard.beaconportal.activities.schedule.view;

public class ScheduleView {
	private String band;
	private String classes;
	private int note_number;

	public ScheduleView(String band, String classes, int note_number) {

		super();
		this.band = band;
		this.classes = classes;
		this.note_number = note_number;

	}

	public String Band() {
		return band;

	}

	public int Note_Number() {
		return note_number;

	}

	public String Classes() {
		return classes;

	}

}
