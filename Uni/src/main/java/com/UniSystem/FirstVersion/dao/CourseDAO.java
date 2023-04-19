package com.UniSystem.FirstVersion.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.UniSystem.FirstVersion.model.Course;
import com.UniSystem.FirstVersion.model.Instructor;

public class CourseDAO {

	

	private static final String INSERT_COURSE_SQL = "INSERT INTO course (name, instructor_id) VALUES (?, ?);";
	private static final String SELECT_COURSE_BY_ID = "select id,name,instructor_id from course where id =?";
	private static final String SELECT_ALL_COURSE = "SELECT * FROM course;";
	private static final String DELETE_COURSE_SQL = "delete from course where id = ?;";
	private static final String UPDATE_COURSE_SQL = "UPDATE course SET name = ?, instructor_id = ? WHERE (id = ?);";

	
	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // com.mysql.cj.jdbc.Driver.class
		
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "0776846438");
			System.out.println("connection success");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	
	
	public void insertCourse(Course course) throws SQLException {
		
		System.out.println("insert");

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(INSERT_COURSE_SQL)) {
			preparedStatement.setString(1, course.getName());
			preparedStatement.setInt(2, course.getInstructor_id());

			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
			System.out.println(course.getName());

		} catch (SQLException e) {
			printSQLException(e);
		}
	}
	
	
	
	public Course selectCourse(int id) {
		Course course = null;
		
		try (Connection connection = getConnection();
				
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COURSE_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println("Dao statement" + preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();

			
			while (rs.next()) {
				String name = rs.getString("name");

				int instructor_id = rs.getInt("instructor_id");
				course = new Course(id, name, instructor_id);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return course;
	}
	public List<Course> selectAllCourse() {

		
		List<Course> course = new ArrayList<>();
	
		try (Connection connection = getConnection();
				
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COURSE);) {
			System.out.println(preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();

		
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				//String email = rs.getString("email");
				int instructor_id = rs.getInt("instructor_id");
				System.out.println("showing record");
				course.add(new Course(id, name, instructor_id));
			}
			
			rs.close();
		} catch (SQLException e) {
			printSQLException(e);
		}
		return course;
	}
	

	public boolean deleteCourse(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_COURSE_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
	public boolean updateCourse(Course course) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
			
				PreparedStatement statement = connection.prepareStatement(UPDATE_COURSE_SQL);) {

			statement.setString(1, course.getName());
			statement.setInt(2, course.getInstructor_id());

			;
			statement.setInt(3, course.getId());
		

			System.out.println(statement);
			rowUpdated = statement.executeUpdate() > 0;
		}

		System.out.println(rowUpdated);
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

	
}
