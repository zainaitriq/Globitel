package com.model;

public class Course {
	private int id;
	private String name;
	private int instructor_id;

	public Course(int id, String name, int instructor_id) {
		super();
		this.id = id;
		this.name = name;
		this.instructor_id = instructor_id;
	}

	public Course(String name, int instructor_id) {
		super();
		this.name = name;
		this.instructor_id = instructor_id;
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

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", instructor_id=" + instructor_id + "]";
	}

}
