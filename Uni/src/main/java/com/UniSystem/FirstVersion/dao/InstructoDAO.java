package com.UniSystem.FirstVersion.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.UniSystem.FirstVersion.model.Instructor;

//this DAO class 

public class InstructoDAO {

	private static final String INSERT_INSTRUCTOR_SQL = "INSERT INTO instructor" + " (name, email) VALUES "
			+ " (?, ?);";

	private static final String SELECT_INSTRUCTOR_BY_ID = "select id,name,email from instructor where id =?";
	private static final String SELECT_ALL_INSTRUCTOR = "SELECT * FROM instructor;";
	private static final String DELETE_INSTRUCTOR_SQL = "delete from instructor where id = ?;";
	private static final String UPDATE_INSTRUCTOR_SQL = "UPDATE instructor SET name = ?, email = ? WHERE (ID = ?);";
	// UPDATE instructor SET name = ?, email = ? WHERE (ID = 13);

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // load a database driver class dynamically

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "0776846438");
			System.out.println("connection success");
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return connection;
	}

	public void insertInstructor(Instructor instructor) throws SQLException {
		// System.out.println(INSERT_INSTRUCTOR_SQL);
		System.out.println("insert");

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("INSERT INTO instructor (name, email) VALUES (?,?);")) {
			preparedStatement.setString(1, instructor.getName());
			preparedStatement.setString(2, instructor.getEmail());

			System.out.println(preparedStatement);
			preparedStatement.executeUpdate(); // returns an integer value indicating the number of rows affected by the
												// statement.
			// used to execute an SQL INSERT, UPDATE, or DELETE statement on a prepared
			// statement object
			System.out.println(instructor.getName());

		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public Instructor selectInstructor(int id) {
		Instructor instructor = null;

		try (Connection connection = getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_INSTRUCTOR_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println("Dao statement" + preparedStatement);

			ResultSet rs = preparedStatement.executeQuery(); // The method returns a ResultSet object that contains the
																// results of the query.
			// used to execute an SQL SELECT statement on a prepared statement object.

			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				instructor = new Instructor(id, name, email);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return instructor;
	}

	public List<Instructor> selectAllInstructor() {

		List<Instructor> instructors = new ArrayList<>();

		try (Connection connection = getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_INSTRUCTOR);) {
			System.out.println(preparedStatement);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				System.out.println("showing record");
				instructors.add(new Instructor(id, name, email));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return instructors;
	}

	public boolean deleteInstructor(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_INSTRUCTOR_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateInstructor(Instructor instructor) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();

				PreparedStatement statement = connection.prepareStatement(UPDATE_INSTRUCTOR_SQL);) {

			statement.setString(1, instructor.getName());
			statement.setString(2, instructor.getEmail());
			statement.setInt(3, instructor.getId());
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
