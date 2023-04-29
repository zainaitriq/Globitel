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

import com.dao.CourseDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.model.Course;
import com.model.Instructor;
import com.model.Student;

@WebServlet("/Course")
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CourseDAO courseDao;
	Gson gson = new Gson();
	private static final Logger LOGGER = Logger.getLogger(CourseServlet.class.getName());

	public CourseServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		courseDao = new CourseDAO();
		BasicConfigurator.configure();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.info("Servlet called with GET method.");
		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();
		try {

			listCourses(request, response);

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
		// TODO Auto-generated method stub
		String json = request.getReader().lines().collect(Collectors.joining());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseData = new HashMap<>();

		if (json == null || json.trim().isEmpty()) {
			// Handle empty or null JSON data here
			return;
		}

		try {

			Course newCourse = mapper.readValue(json, Course.class);
			String name = newCourse.getName();
			int instructor_id = newCourse.getInstructor_id();

			String jsonResponse = "{ \"name\": \"" + name + ", \"instructor id\": \"" + instructor_id + "\" }";
			int status = response.getStatus();
			courseDao.insertCourse(newCourse);
			if (courseDao.getStatus() == 200) {
				responseData.put("status", "success");
				responseData.put("message", "Course added successfully");

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

			listCourses(request, response);

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
		// TODO Auto-generated method stub
		String json = request.getReader().lines().collect(Collectors.joining());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();

		try {

			Course book = mapper.readValue(json, Course.class);
			int id = book.getId();
			String name = book.getName();
			int instructor_id = book.getInstructor_id();

			courseDao.updateCourse(book);

			response.setStatus(HttpServletResponse.SC_OK);
			if (courseDao.getStatus() == 200) {
				responseData.put("status", "success");
				responseData.put("message", "Course updated successfully");
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
			listCourses(request, response);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String json = request.getReader().lines().collect(Collectors.joining());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseData = new HashMap<>();
		int status = response.getStatus();
		try {

			Course book = mapper.readValue(json, Course.class);
			int id = book.getId();
			courseDao.deleteCourse(id);

			response.setStatus(HttpServletResponse.SC_OK);
			if (courseDao.getStatus() == 200) {
				responseData.put("status", "success");
				responseData.put("message", "Course deleted successfully");

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

	private void listCourses(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Course> listCourse = courseDao.selectAllCourse();

		String courseJSON = gson.toJson(listCourse);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.write(courseJSON);
		out.close();

	}

}
