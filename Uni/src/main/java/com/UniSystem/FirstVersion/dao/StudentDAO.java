package com.UniSystem.FirstVersion.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.UniSystem.FirstVersion.model.Instructor;
import com.UniSystem.FirstVersion.model.Student;

public class StudentDAO {


	private static final String SELECT_STUDENT_BY_ID = "select ID,name,email from student where ID =?";
	private static final String SELECT_ALL_STUDENT = "SELECT * FROM student;";
	private static final String DELETE_STUDENT_SQL = "delete from student where ID = ?;";
	private static final String UPDATE_STUDENT_SQL = "UPDATE student SET name = ?, email = ? WHERE (ID = ?);";

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); //
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

	public void insertStudent(Student student) throws SQLException {
	
		System.out.println("insert");

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("INSERT INTO student (name, email) VALUES (?,?);")) {
			System.out.println("connected to database successfully");
			preparedStatement.setString(1, student.getName());
			preparedStatement.setString(2, student.getEmail());

			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
			System.out.println(student.getName());

		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public Student selectStudent(int id) {
		Student student = null;
		try (Connection connection = getConnection();
				
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENT_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println("Dao statement" + preparedStatement);
	
			ResultSet rs = preparedStatement.executeQuery();

		
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				student = new Student(id, name, email);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return student;
	}

	public List<Student> selectAllStudent() {
	
		List<Student> students = new ArrayList<>();
		
		try (Connection connection = getConnection();

			
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_STUDENT);) {
			System.out.println(preparedStatement);
		
			ResultSet rs = preparedStatement.executeQuery();

		
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				System.out.println("showing record");
				students.add(new Student(id, name, email));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return students;
	}

	public boolean deleteStudent(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateStudent(Student student) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
			
				PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT_SQL);) {

			statement.setString(1, student.getName());
			statement.setString(2, student.getEmail());
			statement.setInt(3, student.getId());
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
