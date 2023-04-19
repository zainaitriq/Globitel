package com.UniSystem.FirstVersion.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.UniSystem.FirstVersion.dao.CourseDAO;
import com.UniSystem.FirstVersion.dao.InstructoDAO;
import com.UniSystem.FirstVersion.model.Course;
import com.UniSystem.FirstVersion.model.Instructor;


@WebServlet("/Course")
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private CourseDAO courseDao;  
    
    public void init() {
		courseDao = new CourseDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			listCourses(request, response);
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			insertCourse(request, response);
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}
	
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
	          throws ServletException, IOException {
		
		
		try {
		
			updateCourse(req, resp);
		
		
	} catch (SQLException ex) {
		throw new ServletException(ex);
	}
	  }
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			deleteCourse(request, response);
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}
	
	
	private void listCourses(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Course> listCourse = courseDao.selectAllCourse();
		for (int i =0 ; i< listCourse.size() ; i++) {
			PrintWriter out = response.getWriter();
			out.print(i + " " +listCourse.get(i) + "\n");
		}
	
	}

	private void insertCourse(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		String name = request.getParameter("name");
		//String email = request.getParameter("email");
		int instructor_id = Integer.parseInt(request.getParameter("instructor_id"));
		PrintWriter out = response.getWriter();
		out.print(name);
		out.print(instructor_id);
		Course newCourse = new Course(name, instructor_id);
		courseDao.insertCourse(newCourse);
		listCourses(request, response);	

		}

	private void updateCourse(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		//String email = request.getParameter("email");
		int instructor_id = Integer.parseInt(request.getParameter("instructor_id"));

		Course book = new Course(id, name, instructor_id);
		courseDao.updateCourse(book);
		listCourses(request, response);	

	}

	private void deleteCourse(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		courseDao.deleteCourse(id);
	
		listCourses(request, response);	

	}

}
