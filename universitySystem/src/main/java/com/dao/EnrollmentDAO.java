package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.model.Enrollment;

public class EnrollmentDAO {

	private static final String INSERT_ENROLLMENT_SQL = "INSERT INTO enroll (student_id, course_id) VALUES (?,?);";
	private static final String SELECT_ENROLLMENT_BY_ID = "select student_id, course_id from enroll where WHERE (course_id = ?) and (student_id = ?);";
	private static final String SELECT_ALL_ENROLLMENT = "SELECT * FROM enroll;";
	private static final String DELETE_ENROLLMENT_SQL = "delete from enroll where WHERE (course_id = ?) and (student_id = ?);";
	private static final String UPDATE_ENROLLMENT_SQL = "UPDATE enroll  SET student_id = ?,course_id = ?  WHERE (course_id = ?) and (student_id = ?);";
	private int status = 200;

	static Logger logger = Logger.getLogger(EnrollmentDAO.class);
	DataBaseConnection con = new DataBaseConnection();

	public EnrollmentDAO() {
		super();
		BasicConfigurator.configure();

	}

	public void registerStudent(Enrollment enrollment) throws SQLException {
		logger.debug("Enrollment DAO insert method");

		try (Connection connection = con.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ENROLLMENT_SQL)) {
			preparedStatement.setInt(1, enrollment.getStudent_id());
			preparedStatement.setInt(2, enrollment.getCourse_id());
			logger.debug(preparedStatement);

			preparedStatement.executeUpdate();

			logger.info("Student registered successfully");
			connection.close();

		} catch (SQLException e) {
			printSQLException(e);
			logger.error(e);
			status = 0;


		}
	}

	public Enrollment selectEnrollment(int student_id, int instructor_id) {
		Enrollment enrollment = null;

		try (Connection connection = con.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ENROLLMENT_BY_ID);) {
			preparedStatement.setInt(1, student_id);
			preparedStatement.setInt(2, instructor_id);
			logger.debug(preparedStatement);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int stu_id = Integer.parseInt(rs.getString("student_id"));
				int ins_id = Integer.parseInt(rs.getString("instructor_id"));
				enrollment = new Enrollment(stu_id, ins_id);
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
	
	

}
