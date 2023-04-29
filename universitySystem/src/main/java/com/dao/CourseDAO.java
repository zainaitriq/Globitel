package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.model.Course;

public class CourseDAO {

	private static final String INSERT_COURSE_SQL = "INSERT INTO course (name, instructor_id) VALUES (?, ?);";
	private static final String SELECT_COURSE_BY_ID = "select id,name,instructor_id from course where id =?";
	private static final String SELECT_ALL_COURSE = "SELECT * FROM course;";
	private static final String DELETE_COURSE_SQL = "delete from course where id = ?;";
	private static final String UPDATE_COURSE_SQL = "UPDATE course SET name = ?, instructor_id = ? WHERE (id = ?);";
	private int status = 200;

	static Logger logger = Logger.getLogger(CourseDAO.class);
	DataBaseConnection con = new DataBaseConnection();

	public CourseDAO() {
		super();
		BasicConfigurator.configure();

	}

	public void insertCourse(Course course) throws SQLException {

		logger.debug("course insert method");

		try (Connection connection = con.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COURSE_SQL)) {
			preparedStatement.setString(1, course.getName());
			preparedStatement.setInt(2, course.getInstructor_id());
			logger.debug(preparedStatement);

			preparedStatement.executeUpdate();
			logger.info("course added successfully");

		} catch (SQLException e) {
			printSQLException(e);
			logger.error(e);
			status = 0;

		}
	}

	public Course selectCourse(int id) {
		Course course = null;

		try (Connection connection = con.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COURSE_BY_ID);) {
			logger.debug(preparedStatement);
			preparedStatement.setInt(1, id);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");

				int instructor_id = rs.getInt("instructor_id");
				course = new Course(id, name, instructor_id);
			}
			logger.info("Course returned by id successfully");

		} catch (SQLException e) {
			printSQLException(e);
			logger.error(e);
			status = 0;

		}
		return course;
	}

	public List<Course> selectAllCourse() {

		List<Course> course = new ArrayList<>();

		try (Connection connection = con.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COURSE);) {
			logger.debug(preparedStatement);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				// String email = rs.getString("email");
				int instructor_id = rs.getInt("instructor_id");
				course.add(new Course(id, name, instructor_id));
			}
			logger.info("Courses listed successfully");

			rs.close();
		} catch (SQLException e) {
			logger.error(e);
			printSQLException(e);
			status = 0;

		}
		return course;
	}

	public boolean deleteCourse(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = con.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_COURSE_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		if (!rowDeleted) {
			logger.error("there is no row to delete");
			status = 0;

		}
		return rowDeleted;
	}

	public boolean updateCourse(Course course) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = con.getConnection();

				PreparedStatement statement = connection.prepareStatement(UPDATE_COURSE_SQL);) {

			statement.setString(1, course.getName());
			statement.setInt(2, course.getInstructor_id());
			statement.setInt(3, course.getId());

			logger.debug(statement);

			rowUpdated = statement.executeUpdate() > 0;
		}
		if (!rowUpdated) {
			logger.error("no update happened");
			status = 0;

		} else {
			logger.info("course updated successfully");

		}

		return rowUpdated;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
