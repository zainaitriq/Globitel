package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.model.Course;
import com.model.Enrollment;

public class EnrollmentDAO {

	private static final String INSERT_ENROLLMENT_SQL = "INSERT INTO enroll (student_id, course_id) VALUES (?,?);";
	private static final String SELECT_ENROLLMENT_BY_ID = "select student_id, course_id from enroll where WHERE (course_id = ?) and (student_id = ?);";
	private static final String SELECT_ALL_ENROLLMENT = "SELECT * FROM enroll;";
	private static final String DELETE_ENROLLMENT_SQL = "delete from enroll where course_id = ? and student_id = ?;";
	private static final String UPDATE_ENROLLMENT_SQL = "UPDATE enroll  SET student_id = ?,course_id = ?  WHERE (course_id = ?) and (student_id = ?);";
	private static final String SELECT_COUNT_SQL = "SELECT COUNT(student_id) as count from enroll where course_id=?";
	private static final String SELECT_COURSEBY_STUID = "SELECT course_id FROM enroll where student_id=?";

	private int status = 200;
	private String message;
	private int count;
	static Logger logger = Logger.getLogger(EnrollmentDAO.class);
	DataBaseConnection con = new DataBaseConnection();
	CourseDAO courseDao = new CourseDAO();

	public EnrollmentDAO() {
		super();
		BasicConfigurator.configure();

	}

	public void registerStudent(Enrollment enrollment) throws SQLException {
		logger.debug("Enrollment DAO insert method");
		status = 200;
		if (checkTime(enrollment.getStudent_id(), enrollment.getCourse_id())) {
			// status = 200;
			logger.debug("true time checked");
			Course book = courseDao.selectCourse(enrollment.getCourse_id());
			int capacity = book.getCapacity();

			if (count_capacity(book.getId()) < capacity) {
				try (Connection connection = con.getConnection();
						PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ENROLLMENT_SQL)) {

					preparedStatement.setInt(1, enrollment.getStudent_id());
					preparedStatement.setInt(2, enrollment.getCourse_id());
					logger.debug(preparedStatement);
					preparedStatement.executeUpdate();
					logger.info("Student registered successfully");
					connection.close();
					message = "Student registered successfully";

				} catch (SQLException e) {
					printSQLException(e);
					logger.error(e);
					status = 0;

				}
			} else {
				logger.info("capacity is full the student can't register to this course");
				message = "capacity is full the student can't register to this course";
				status = 0;
			}

		} else {
			logger.info("there is mismatched course");
			status = 0;
			message = "there is mismatched course";

		}

	}

	public Enrollment selectEnrollment(int student_id, int course_id) {
		Enrollment enrollment = null;

		try (Connection connection = con.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ENROLLMENT_BY_ID);) {
			preparedStatement.setInt(1, student_id);
			preparedStatement.setInt(2, course_id);
			logger.debug(preparedStatement);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int stu_id = Integer.parseInt(rs.getString("student_id"));
				int coue_id = Integer.parseInt(rs.getString("course_id"));
				enrollment = new Enrollment(stu_id, coue_id);
			}
			logger.info("Enrollment returned by id successfully");
			connection.close();

		} catch (SQLException e) {
			printSQLException(e);
			logger.error(e);
			status = 0;

		}
		return enrollment;
	}

	public List<Enrollment> selectAllEnrollment() {

		List<Enrollment> enrollments = new ArrayList<>();

		try (Connection connection = con.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ENROLLMENT);) {
			logger.debug(preparedStatement);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int stu_id = Integer.parseInt(rs.getString("student_id"));
				int course_id = Integer.parseInt(rs.getString("course_id"));

				enrollments.add(new Enrollment(stu_id, course_id));
			}
			logger.info("Enrollment listed successfully");

		} catch (SQLException e) {
			printSQLException(e);
			logger.error(e);
			status = 0;

		}
		return enrollments;
	}

	public boolean deleteEnrollment(int student_id, int course_id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = con.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ENROLLMENT_SQL);) {
			preparedStatement.setInt(1, student_id);
			preparedStatement.setInt(2, course_id);
			System.out.println(preparedStatement);
			rowDeleted = preparedStatement.executeUpdate() > 0;
			logger.info("Enrolment deleted successfully");

		}
		if (!rowDeleted) {
			logger.error("there is no row to delete");
			status = 0;

		}
		return rowDeleted;
	}

	public boolean updateEnrollment(Enrollment enrollment) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = con.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ENROLLMENT_SQL);) {

			preparedStatement.setInt(1, enrollment.getStudent_id());
			preparedStatement.setInt(2, enrollment.getCourse_id());
			System.out.println(preparedStatement);
			rowUpdated = preparedStatement.executeUpdate() > 0;
		}
		if (!rowUpdated) {
			logger.error("no update happened");
			status = 0;

		} else {
			logger.info("Enrollment updated successfully");

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private int count_capacity(int course_id) {
		int count = 0;
		try (Connection connection = con.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_SQL);) {
			preparedStatement.setInt(1, course_id);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Count: " + count);

		return count;
	}

	boolean checkTime(int stu_id, int course_id) throws SQLException {
		List<Integer> courses_id = new ArrayList<>();
		CourseDAO coursedao = new CourseDAO();
		Course course;
		Course book_course = coursedao.selectCourse(course_id);
		boolean register_status = false;

		try (Connection connection = con.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COURSEBY_STUID);) {
			preparedStatement.setInt(1, stu_id);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next()) {
				logger.info("there no courses");
				register_status = true;
			} else {
				while (rs.next()) {
					logger.info("array list for loop");
					int Course_id = Integer.parseInt(rs.getString("course_id"));
					courses_id.add(Course_id);
				}

				for (int i = 0; i < courses_id.size(); i++) {
					course = coursedao.selectCourse(courses_id.get(i));
					// selectEnrollment(stu_id, courses_id.get(i));
					if (course.getEnd_time().before(book_course.getStart_time())
							|| course.getEnd_time().equals(book_course.getStart_time())) {
						register_status = true;

					} else if (book_course.getEnd_time().before(course.getStart_time())
							|| book_course.getEnd_time().equals(course.getStart_time())) {
						register_status = true;

					} else {
						register_status = false;
					}

				}

			}

		}
		return register_status;

	}


}
