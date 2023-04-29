package com.dao;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;



public class DataBaseConnection {

	String className = "com.mysql.cj.jdbc.Driver";
	String databaseURL = "jdbc:mysql://localhost:3306/university";
	String username = "root";
	String password = "0776846438";
	static Logger logger = Logger.getLogger(DataBaseConnection.class);

	/*public DataBaseConnection() {
		BasicConfigurator.configure();
	
		try (FileReader reader = new FileReader("config")) {
			Properties properties = new Properties();
			properties.load(reader);
			this.username = properties.getProperty("username");
			this.password = properties.getProperty("password");
			this.className = properties.getProperty("className");
			this.databaseURL = properties.getProperty("databaseURL");
		

		} catch (Exception e) {
			;
			e.printStackTrace();
		}

	
	}*/


	

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(className); // load a database driver class dynamically

			connection = (Connection) DriverManager.getConnection(databaseURL, username,
					password);
			if (connection != null) {
				logger.info("database connected succefully");
			}
		} catch (SQLException e) {

			
			logger.error(e.getStackTrace());
		} catch (ClassNotFoundException e) {

			logger.error(e.getStackTrace());
		}
		return connection;
	}

}
