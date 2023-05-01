package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.model.Student;



public class StudentDAO {
	private static final String INSERT_STUDENT_SQL = "INSERT INTO student (name, email) VALUES (?,?);";
	private static final String SELECT_STUDENT_BY_ID = "select ID,name,email from student where ID =?";
	private static final String SELECT_ALL_STUDENT = "SELECT * FROM student;";
	private static final String DELETE_STUDENT_SQL = "delete from student where ID = ?;";
	private static final String UPDATE_STUDENT_SQL = "UPDATE student SET name = ?, email = ? WHERE (ID = ?);";
	private int status = 200;

	static Logger logger = Logger.getLogger(StudentDAO.class);
	DataBaseConnection con = new DataBaseConnection();

	public StudentDAO() {
		super();
		BasicConfigurator.configure();

	}

	public void insertStudent(Student student) throws SQLException {

		try (Connection connection = con.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STUDENT_SQL)) {

			preparedStatement.setString(1, student.getName());
			preparedStatement.setString(2, student.getEmail());

			preparedStatement.executeUpdate();
			logger.info("Student added successfully");
			connection.close();

		} catch (SQLException e) {
			printSQLException(e);
			logger.error(e);
			status = 0;


		}
	}

	public Student selectStudent(int id) {
		Student student = null;
		try (Connection connection = con.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENT_BY_ID);) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				student = new Student(id, name, email);
			}
			logger.info("Student returned by id successfully");
			rs.close();

		} catch (SQLException e) {
			printSQLException(e);
			logger.error(e);
			status = 0;


		}
		return student;
	}

	public List<Student> selectAllStudent() {

		List<Student> students = new ArrayList<>();

		try (Connection connection = con.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_STUDENT);) {
			logger.debug(preparedStatement);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				System.out.println("showing record");
				students.add(new Student(id, name, email));
			}
			logger.info("Students returned successfully");
			rs.close();

		} catch (SQLException e) {
			printSQLException(e);
			logger.error(e);
			status = 0;


		}
		return students;
	}

	public boolean deleteStudent(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = con.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
			logger.info("Students deleted successfully");

		}

		if (!rowDeleted) {
			logger.error("there is no row to delete");
			status = 0;

		}
		return rowDeleted;
	}

	public boolean updateStudent(Student student) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = con.getConnection();

				PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT_SQL);) {

			statement.setString(1, student.getName());
			statement.setString(2, student.getEmail());
			statement.setInt(3, student.getId());
			logger.debug(statement);

			rowUpdated = statement.executeUpdate() > 0;
			logger.info("Students updated successfully");

		}
		if (!rowUpdated) {
			logger.error("there is no row to update");
			status = 0;

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
