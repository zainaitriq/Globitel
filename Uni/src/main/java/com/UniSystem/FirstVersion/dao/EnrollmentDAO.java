package com.UniSystem.FirstVersion.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.UniSystem.FirstVersion.model.Enrollment;
import com.UniSystem.FirstVersion.model.Instructor;

public class EnrollmentDAO {
	


	private static final String INSERT_ENROLLMENT_SQL = "INSERT INTO enroll" + " (student_id, course_id) VALUES "
			+ " (?, ?);";
	private static final String SELECT_ENROLLMENT_BY_ID = "select student_id, course_id from enroll where WHERE (course_id = ?) and (student_id = ?);";
	private static final String SELECT_ALL_ENROLLMENT = "SELECT * FROM enroll;";
	private static final String DELETE_ENROLLMENT_SQL = "delete from enroll where WHERE (course_id = ?) and (student_id = ?);";
	
	
	private static final String UPDATE_ENROLLMENT_SQL = "UPDATE enroll  SET student_id = ?,course_id = ?  WHERE (course_id = ?) and (student_id = ?);";

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
	
	public void registerStudent(Enrollment enrollment) throws SQLException {
		
		System.out.println("insert");

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("INSERT INTO enroll (student_id, course_id) VALUES (?,?);")) {
			preparedStatement.setInt(1, enrollment.getStudent_id());
			preparedStatement.setInt(2, enrollment.getCourse_id());

			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		

		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public Enrollment selectEnrollment(int student_id,int  instructor_id) {
		Enrollment enrollment = null;
	
		try (Connection connection = getConnection();
				
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ENROLLMENT_BY_ID);) {
			preparedStatement.setInt(1, student_id);
			preparedStatement.setInt(2, instructor_id);
			System.out.println("Dao statement" + preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();

	
			while (rs.next()) {
				int stu_id = Integer.parseInt(rs.getString("student_id"));
				int ins_id = Integer.parseInt(rs.getString("instructor_id"));
				enrollment = new Enrollment(stu_id, ins_id);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return enrollment;
	}

	public List<Enrollment> selectAllEnrollment() {

	
		List<Enrollment> enrollments = new ArrayList<>();
		
		try (Connection connection = getConnection();

			
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ENROLLMENT);) {
			System.out.println(preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int stu_id = Integer.parseInt(rs.getString("student_id"));
				int course_id = Integer.parseInt(rs.getString("course_id"));
				
				System.out.println("showing record");
				enrollments.add(new Enrollment(stu_id, course_id));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return enrollments;
	}

	public boolean deleteEnrollment(int student_id,int  course_id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ENROLLMENT_SQL);) {
			preparedStatement.setInt(1, student_id);
			preparedStatement.setInt(2, course_id);
			rowDeleted = preparedStatement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateEnrollment(Enrollment enrollment) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ENROLLMENT_SQL);) {

			preparedStatement.setInt(1, enrollment.getStudent_id());
			preparedStatement.setInt(2, enrollment.getCourse_id());
			System.out.println(preparedStatement);
			rowUpdated = preparedStatement.executeUpdate() > 0;
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


