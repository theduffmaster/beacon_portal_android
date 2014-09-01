package com.bernard.beaconportal.activities;

public class Due_Today_List {

	String Band;
	String Number;
	String Class;
	String Teacher;
	String Title;
	String Date;
	String Type;
	String Description;

	public Due_Today_List(String Band, String Number, String Class,
			String Teacher, String Title, String Date, String Type,
			String Description) {

		super();

		this.Band = Band;
		this.Number = Number;
		this.Class = Class;
		this.Teacher = Teacher;
		this.Title = Title;
		this.Date = Date;
		this.Type = Type;
		this.Description = Description;
	}

	public String getBand() {
		return Band;
	}

	public String getNumber() {
		return Number;
	}

	public String getClasses() {
		return Class;
	}

	public String getTeacher() {
		return Teacher;
	}

	public String getTitle() {
		return Title;
	}

	public String getDate() {
		return Date;
	}

	public String getType() {
		return Type;
	}

	public String getDescription() {
		return Description;

	}

}
