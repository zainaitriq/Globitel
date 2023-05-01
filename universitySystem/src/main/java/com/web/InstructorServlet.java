package com.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;

import com.dao.InstructorDAO;
import com.google.gson.Gson;
import com.model.Instructor;

@WebServlet("/Instructor")
public class InstructorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InstructorDAO instructorDao;
	Gson gson = new Gson();
	private static final Logger LOGGER = Logger.getLogger(InstructorServlet.class.getName());

	public InstructorServlet() {
		super();

	}

	public void init(ServletConfig config) throws ServletException {

		instructorDao = new InstructorDAO();
		BasicConfigurator.configure();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		LOGGER.info("Servlet called with GET method.");
		System.out.println("Working directory: " + System.getProperty("user.dir"));
		System.out.println("Classpath: " + System.getProperty("java.class.path"));

		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();

		// TODO Auto-generated method stub
		try {

			listInstructor(request, response);
			responseData.put("status", status);
			responseData.put("message", "Success");

		} catch (SQLException ex) {
			throw new ServletException(ex);
			
			
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(responseData);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String json = request.getReader().lines().collect(Collectors.joining());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseData = new HashMap<>();

		if (json == null || json.trim().isEmpty()) {
			// Handle empty or null JSON data here
			return;
		}

		try {

			Instructor newInstructor = mapper.readValue(json, Instructor.class);
			String name = newInstructor.getName();

			String email = newInstructor.getEmail();
			String jsonResponse = "{ \"name\": \"" + name + ", \"email\": \"" + email + "\" }";
			int status = response.getStatus();
			instructorDao.insertInstructor(newInstructor);
			if (instructorDao.getStatus()==200) {
				responseData.put("status", "success");
				responseData.put("message", "Instructor added successfully");
				
			}
			else {
			  
				responseData.put("status", "error");
				responseData.put("message","unable to add instructor");
			}
			
			ObjectMapper mapp = new ObjectMapper();
			String jsonn = mapp.writeValueAsString(responseData);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonn);
			out.flush();

			listInstructor(request, response);

			// Process the object as needed
		} catch (JsonProcessingException e) {
			// Handle JSON processing exception here
			e.printStackTrace();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String json = request.getReader().lines().collect(Collectors.joining());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();


		try {
	
			Instructor book = mapper.readValue(json, Instructor.class);
			int id = book.getId();
			String name = book.getName();
			String email = book.getEmail();

			instructorDao.updateInstructor(book);
			
			response.setStatus(HttpServletResponse.SC_OK);
			if (instructorDao.getStatus()==200) {
				responseData.put("status", "success");
				responseData.put("message", "instructor updated successfully");
				
			}
			else {
			    response.setStatus(1062 );

				responseData.put("status", "error");
				responseData.put("message","unable to update instructor");
			}
			
			ObjectMapper mapp = new ObjectMapper();
			String jsonn = mapp.writeValueAsString(responseData);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonn);
			out.flush();
			listInstructor(request, response);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String json = request.getReader().lines().collect(Collectors.joining());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();
		try {

		
			Instructor book = mapper.readValue(json, Instructor.class);
			int id = book.getId();
			instructorDao.deleteInstructor(id);

			response.setStatus(HttpServletResponse.SC_OK);
			if (instructorDao.getStatus()==200) {
			 

				responseData.put("status", "success");
				responseData.put("message", "instructor deleted successfully");
				
			}
			else {
			    response.setStatus(1062 );

				responseData.put("status", "error");
				responseData.put("message","unable to update instructor");
			}
			
			ObjectMapper mapp = new ObjectMapper();
			String jsonn = mapp.writeValueAsString(responseData);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonn);
			out.flush();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void listInstructor(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Instructor> listInstructor = instructorDao.selectAllInstructor();
		String instructorJSON = gson.toJson(listInstructor);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.write(instructorJSON);
		out.close();

	}

}
