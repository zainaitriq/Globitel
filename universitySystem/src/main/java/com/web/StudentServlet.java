package com.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;

import com.dao.StudentDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.model.Instructor;
import com.model.Student;

/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/Student")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentDAO studentDao;
	Gson gson = new Gson();
	private static final Logger LOGGER = Logger.getLogger(StudentServlet.class.getName());

	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		studentDao = new StudentDAO();
		BasicConfigurator.configure();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.info("Servlet called with GET method.");
		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();
		try {

			listStudent(request, response);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String json = request.getReader().lines().collect(Collectors.joining());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseData = new HashMap<>();

		if (json == null || json.trim().isEmpty()) {
			// Handle empty or null JSON data here
			return;
		}

		try {

			Student newStudent = mapper.readValue(json, Student.class);
			String name = newStudent.getName();

			String email = newStudent.getEmail();
			String jsonResponse = "{ \"name\": \"" + name + ", \"email\": \"" + email + "\" }";
			int status = response.getStatus();
			studentDao.insertStudent(newStudent);
			if (studentDao.getStatus() == 200) {

				responseData.put("status", "success");
				responseData.put("message", "Student added successfully");

			} else {
				response.setStatus(1062);

				responseData.put("status", "error");
				responseData.put("message", "unable to add instructor");
			}

			ObjectMapper mapp = new ObjectMapper();
			String jsonn = mapp.writeValueAsString(responseData);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonn);
			out.flush();

			listStudent(request, response);

			// Process the object as needed
		} catch (JsonProcessingException e) {
			// Handle JSON processing exception here
			e.printStackTrace();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String json = request.getReader().lines().collect(Collectors.joining());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();

		try {

			Student book = mapper.readValue(json, Student.class);
			int id = book.getId();
			String name = book.getName();
			String email = book.getEmail();

			studentDao.updateStudent(book);

			response.setStatus(HttpServletResponse.SC_OK);
			if (studentDao.getStatus() == 200) {
				responseData.put("status", "success");
				responseData.put("message", "Student updated successfully");
			} else {
				response.setStatus(1062);

				responseData.put("status", "error");
				responseData.put("message", "unable to update instructor");
			}

			ObjectMapper mapp = new ObjectMapper();
			String jsonn = mapp.writeValueAsString(responseData);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonn);
			out.flush();
			listStudent(request, response);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String json = request.getReader().lines().collect(Collectors.joining());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();
		try {

			Student book = mapper.readValue(json, Student.class);
			int id = book.getId();
			studentDao.deleteStudent(id);

			response.setStatus(HttpServletResponse.SC_OK);
			if (studentDao.getStatus() == 200) {
				responseData.put("status", "success");
				responseData.put("message", "Student deleted successfully");

			} else {
				response.setStatus(1062);
				responseData.put("status", "error");
				responseData.put("message", "unable to update instructor");
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

	private void listStudent(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {

		List<Student> listStudent = studentDao.selectAllStudent();
		String studentJSON = gson.toJson(listStudent);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.write(studentJSON);
		out.close();
	}

}
