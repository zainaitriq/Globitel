package com.model;

import java.sql.Time;

public class Course {
	private int id;
	private String name;
	private int instructor_id;
	private int capacity;
	private Time start_time;
	private Time end_time;

	public Course() {
		super();
	}

	public Course(int id, String name, int instructor_id, int capacity, Time start_time, Time end_time) {
		super();
		this.id = id;
		this.name = name;
		this.instructor_id = instructor_id;
		this.capacity = capacity;
		this.start_time = start_time;
		this.end_time = end_time;
	}

	public Course(String name, int instructor_id, int capacity, Time start_time, Time end_time) {
		super();
		this.name = name;
		this.instructor_id = instructor_id;
		this.capacity = capacity;
		this.start_time = start_time;
		this.end_time = end_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInstructor_id() {
		return instructor_id;
	}

	public void setInstructor_id(int instructor_id) {
		this.instructor_id = instructor_id;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Time getStart_time() {
		return start_time;
	}

	public void setStart_time(Time start_time) {
		this.start_time = start_time;
	}

	public Time getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Time end_time) {
		this.end_time = end_time;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", instructor_id=" + instructor_id + ", capacity=" + capacity
				+ ", start_time=" + start_time + ", end_time=" + end_time + "]";
	}

}
