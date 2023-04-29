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

import com.dao.EnrollmentDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.model.Enrollment;
import com.model.Instructor;

/**
 * Servlet implementation class EnrollmentServlet
 */
@WebServlet("/Enrollment")
public class EnrollmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EnrollmentDAO enrollmentDao;
	Gson gson = new Gson();
	private static final Logger LOGGER = Logger.getLogger(InstructorServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EnrollmentServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		enrollmentDao = new EnrollmentDAO();
		BasicConfigurator.configure();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		LOGGER.info("Servlet called with GET method.");
		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();

		// TODO Auto-generated method stub
		try {

			listEnrollment(request, response);
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

			Enrollment newEnroll = mapper.readValue(json, Enrollment.class);
			int student_id = newEnroll.getStudent_id();
			int course_id = newEnroll.getCourse_id();

			String jsonResponse = "{ \"student_id\": \"" + student_id + ", \"course_id\": \"" + course_id + "\" }";
			int status = response.getStatus();
			enrollmentDao.registerStudent(newEnroll);
			if (enrollmentDao.getStatus() == 200) {
				responseData.put("status", status);
				responseData.put("message", "Success");

			} else {
				response.setStatus(1062);

				responseData.put("status", "success");
				responseData.put("message", "Enrollment added successfully");

			}

			ObjectMapper mapp = new ObjectMapper();
			String jsonn = mapp.writeValueAsString(responseData);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonn);
			out.flush();

			listEnrollment(request, response);

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

			Enrollment book = mapper.readValue(json, Enrollment.class);
			int student_id = book.getStudent_id();
			int course_id = book.getCourse_id();

			enrollmentDao.updateEnrollment(book);

			response.setStatus(HttpServletResponse.SC_OK);
			if (enrollmentDao.getStatus() == 200) {
				responseData.put("status", "success");
				responseData.put("message", "Enrollment updated successfully");
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
			listEnrollment(request, response);

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

			Enrollment book = mapper.readValue(json, Enrollment.class);
			int student_id = book.getStudent_id();
			int course_id = book.getCourse_id();

			enrollmentDao.deleteEnrollment(student_id, course_id);

			response.setStatus(HttpServletResponse.SC_OK);
			if (enrollmentDao.getStatus() == 200) {
				responseData.put("status", "success");
				responseData.put("message", "Enrollment deleted successfully");
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

	private void listEnrollment(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Enrollment> listEnrollment = enrollmentDao.selectAllEnrollment();
		String enrollmentJSON = gson.toJson(listEnrollment);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.write(enrollmentJSON);
		out.close();

	}

}
