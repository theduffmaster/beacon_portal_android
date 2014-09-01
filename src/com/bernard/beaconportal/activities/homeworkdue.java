package com.bernard.beaconportal.activities;

public class homeworkdue {
	private String homework;
	private String desc;
	private int IconID;
	private String teacher;

	public homeworkdue(String homework, String desc, int IconID, String teacher) {

		super();
		this.homework = homework;
		this.desc = desc;
		this.IconID = IconID;
		this.teacher = teacher;
	}

	public String getHomeworkdue() {
		return homework;
	}

	public String getDesciption() {
		return desc;
	}

	public int getIconID() {
		return IconID;
	}

	public String getTeacher() {
		return teacher;
	}

}
