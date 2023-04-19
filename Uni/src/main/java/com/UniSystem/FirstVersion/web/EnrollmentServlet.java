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

import com.UniSystem.FirstVersion.dao.EnrollmentDAO;
import com.UniSystem.FirstVersion.dao.InstructoDAO;
import com.UniSystem.FirstVersion.model.Enrollment;
import com.UniSystem.FirstVersion.model.Instructor;

/**
 * Servlet implementation class EnrollmentServlet
 */
@WebServlet("/Enrollment")
public class EnrollmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
private EnrollmentDAO enrollmentDao;
	
	public void init() {
		enrollmentDao = new EnrollmentDAO();
	}
   
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			
			listEnrollment(request, response);
					
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			insertEnrollment(request, response);
			
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	
	}
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			deleteEnrollment(request, response);
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}
	
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
	          throws ServletException, IOException {
		
		
		try {
		
			updateEnrollment(req, resp);
		
		
	} catch (SQLException ex) {
		throw new ServletException(ex);
	}
	  }
	private void listEnrollment(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Enrollment> listEnrollment = enrollmentDao.selectAllEnrollment();
		for (int i =0 ; i< listEnrollment.size() ; i++) {
			PrintWriter out = response.getWriter();
			out.print(i + " " +listEnrollment.get(i) + "\n");
		}
	}

	

	private void insertEnrollment(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		int student_id = Integer.parseInt(request.getParameter("student_id"));
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		
		PrintWriter out = response.getWriter();
		out.print(student_id);
		out.print(course_id);
		Enrollment newEnroll = new Enrollment(student_id, course_id);
	    enrollmentDao.registerStudent(newEnroll);
		listEnrollment(request, response);	

		}

	private void updateEnrollment(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		int student_id = Integer.parseInt(request.getParameter("student_id"));
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		

		Enrollment book = new Enrollment(student_id, course_id);
		enrollmentDao.updateEnrollment(book);
		listEnrollment(request, response);	

	}

	private void deleteEnrollment(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		
		int student_id = Integer.parseInt(request.getParameter("student_id"));
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		enrollmentDao.deleteEnrollment(student_id, course_id);
		listEnrollment(request, response);	

	}

}
