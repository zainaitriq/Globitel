package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import com.model.Course;
import org.apache.log4j.Logger;

public class scheduleDAO {

	private static final String INNER_JOIN = "SELECT id ,name , instructor_id, capacity,start_time, end_time FROM course INNER JOIN enroll ON id = course_id where student_id= ?;";
	static Logger logger = Logger.getLogger(EnrollmentDAO.class);
	DataBaseConnection con = new DataBaseConnection();
	CourseDAO courseDao = new CourseDAO();
	private int status = 200;

	public  List<Course> showSchedule(int stu_id) {
		List<Course> course = new ArrayList<>();
		try (Connection connection = con.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INNER_JOIN);) {
			preparedStatement.setInt(1, stu_id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				int id = rs.getInt("id");
				int instructor_id = rs.getInt("instructor_id");
				int capacity = rs.getInt("capacity");
				Time start_time = rs.getTime("start_time");
				Time end_time = rs.getTime("end_time");

				course.add(new Course(id, name, instructor_id, capacity, start_time, end_time));
			}

			System.out.println("Courses listed successfully");
			rs.close();
			for (int i = 0; i < course.size(); i++) {
				System.out.println(course.get(i));

			}

		} catch (SQLException e) {
			logger.error(e);
			printSQLException(e);
			status = 0;

		}
		return course;
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
