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

import com.UniSystem.FirstVersion.dao.InstructoDAO;
import com.UniSystem.FirstVersion.dao.StudentDAO;
import com.UniSystem.FirstVersion.model.Instructor;
import com.UniSystem.FirstVersion.model.Student;

/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/Student")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private StudentDAO studentDao;
    
    public void init() {
    	studentDao = new StudentDAO();
	}

   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getServletPath();
		
		try {
	
			listStudent(request, response);	

			
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getServletPath();
		try {

			insertStudent(request, response);
			
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}
	
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
	          throws ServletException, IOException {
		
		
		try {
		
			updateStudent(req, resp);
		
		
	} catch (SQLException ex) {
		throw new ServletException(ex);
	}
	  }
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			deleteStudent(request, response);
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}
	
	private void listStudent(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Student> listStudent = studentDao.selectAllStudent();
		for (int i =0 ; i< listStudent.size() ; i++) {
			PrintWriter out = response.getWriter();
			out.print(i + " " +listStudent.get(i) + "\n");
		}

	}
	
	
	private void insertStudent(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		PrintWriter out = response.getWriter();
		out.print(name);
		out.print(email);
		Student newStudent = new Student(name, email);
		studentDao.insertStudent(newStudent);
		listStudent(request, response);	

	}
	
	private void updateStudent(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		

		Student book = new Student(id, name, email);
		studentDao.updateStudent(book);
		listStudent(request, response);	
	}
	
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		studentDao.deleteStudent(id);

		listStudent(request, response);	

	}

}
