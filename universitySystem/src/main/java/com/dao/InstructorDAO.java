package com.dao;

import com.model.Instructor;

import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class InstructorDAO {

//	public ArrayList<Instructor> instructors = new ArrayList<Instructor>();
	static Logger logger = Logger.getLogger(InstructorDAO.class);
	DataBaseConnection con = new DataBaseConnection();
	private static final String INSERT_INSTRUCTOR_SQL = "INSERT INTO instructor (name, email) VALUES (?,?);";

	private static final String SELECT_INSTRUCTOR_BY_ID = "select id,name,email from instructor where id =?";
	private static final String SELECT_ALL_INSTRUCTOR = "SELECT * FROM instructor;";
	private static final String DELETE_INSTRUCTOR_SQL = "delete from instructor where id = ?;";
	private static final String UPDATE_INSTRUCTOR_SQL = "UPDATE instructor SET name = ?, email = ? WHERE (ID = ?);";
	private int status = 200;

	public InstructorDAO() {
		super();
		BasicConfigurator.configure();
	}

	public void insertInstructor(Instructor instructor) {

		try (Connection connection = con.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INSTRUCTOR_SQL)) {
			logger.debug(preparedStatement);

			preparedStatement.setString(1, instructor.getName());
			preparedStatement.setString(2, instructor.getEmail());
			preparedStatement.executeUpdate();
			logger.info("instructor added successfully");
			connection.close();

		} catch (SQLException e) {
			logger.error(e);
			printSQLException(e);
			status = 0;

		}

	}

	public List<Instructor> selectAllInstructor() {
		List<Instructor> instructors = new ArrayList<>();
		try (Connection connection = con.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_INSTRUCTOR)) {

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				instructors.add(new Instructor(id, name, email));
				logger.info("instructors listed successfully");

			}
			logger.info("instructors listed successfully");
			rs.close();

		} catch (SQLException e) {
			logger.error(e);
			status = 0;

			printSQLException(e);
		}

		return instructors;

	}

	public boolean deleteInstructor(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = con.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_INSTRUCTOR_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
			logger.info("instructor deleted successfully");
			connection.close();
			if (statement.executeUpdate() == 0) {
				status=0;

			}

		}

		return rowDeleted;
	}

	public boolean updateInstructor(Instructor instructor) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = con.getConnection();

				PreparedStatement statement = connection.prepareStatement(UPDATE_INSTRUCTOR_SQL);) {

			statement.setString(1, instructor.getName());
			statement.setString(2, instructor.getEmail());
			statement.setInt(3, instructor.getId());
			rowUpdated = statement.executeUpdate() > 0;
			logger.info("instructor updated successfully");
			if (statement.executeUpdate() == 0) {
				status=0;

			}
			connection.close();
		

		}

		return rowUpdated;
	}

	public Instructor selectInstructor(int id) {
		Instructor instructor = null;

		try (Connection connection = con.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_INSTRUCTOR_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println("Dao statement" + preparedStatement);

			ResultSet rs = preparedStatement.executeQuery(); // The method returns a ResultSet object that contains the
																// results of the query.
			// used to execute an SQL SELECT statement on a prepared statement object.
			if (preparedStatement.executeUpdate() == 0) {
				status=0;

			}

			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				instructor = new Instructor(id, name, email);
			}
			logger.info("instructor by id returned successfully");
			rs.close();

		} catch (SQLException e) {
			logger.error(e);

			printSQLException(e);

		}
		return instructor;
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