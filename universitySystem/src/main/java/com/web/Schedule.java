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
import org.json.JSONObject;
import org.json.JSONException;

import com.dao.EnrollmentDAO;
import com.dao.scheduleDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.model.Course;
import com.model.Enrollment;
import com.model.Student;

/**
 * Servlet implementation class Schedule
 */
@WebServlet("/Schedule")
public class Schedule extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EnrollmentDAO enrollmentDao;
	private scheduleDAO schedule;
	Gson gson = new Gson();
	private static final Logger LOGGER = Logger.getLogger(InstructorServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Schedule() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		enrollmentDao = new EnrollmentDAO();
		schedule = new scheduleDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		String json = request.getReader().lines().collect(Collectors.joining());
		Student book = mapper.readValue(json, Student.class);
		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();
		int id = book.getId();
		try {
			listSchedule(request, response, id);
			responseData.put("status", status);
			responseData.put("message", "Success");
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}

		ObjectMapper mapperr = new ObjectMapper();
		String jsonn = mapperr.writeValueAsString(responseData);
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

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private void listSchedule(HttpServletRequest request, HttpServletResponse response, int id)
			throws SQLException, IOException, ServletException {

		List<Course> listCourseSchedule = schedule.showSchedule(id);
		String courseJSON = gson.toJson(listCourseSchedule);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.write(courseJSON);
		out.close();
	}
}
