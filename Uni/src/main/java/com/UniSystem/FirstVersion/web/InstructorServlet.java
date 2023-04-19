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
import com.UniSystem.FirstVersion.model.Instructor;
/**
 * Servlet implementation class InstructorServlet
 */
@WebServlet("/Instructor")
public class InstructorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InstructoDAO instructorDao;
	
	public void init() {
		instructorDao = new InstructoDAO();
	}

  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//String action = request.getServletPath();
		try {
			insertInstructor(request, response);
			
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			deleteInstructor(request, response);
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//String action = request.getServletPath();
		
			try {
						
				listInstructor(request, response);
						
			} catch (SQLException ex) {
				throw new ServletException(ex);
			}
	}	
	
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
	          throws ServletException, IOException {
		
		
		try {
		
			updateInstructor(req, resp);
		
		
	} catch (SQLException ex) {
		throw new ServletException(ex);
	}
	  }
	
	
	private void listInstructor(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Instructor> listInstructor = instructorDao.selectAllInstructor();
		for (int i =0 ; i< listInstructor.size() ; i++) {
			PrintWriter out = response.getWriter();
			out.print(i + " " +listInstructor.get(i) + "\n");
		}
		
		
	}

	

	private void insertInstructor(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		PrintWriter out = response.getWriter();
		out.print(name);
		out.print(email);
		Instructor newInstructor = new Instructor(name, email);
		instructorDao.insertInstructor(newInstructor);
		listInstructor(request, response);	

		}

	private void updateInstructor(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		

		Instructor book = new Instructor(id, name, email);
		instructorDao.updateInstructor(book);
		listInstructor(request, response);	

	}

	private void deleteInstructor(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		instructorDao.deleteInstructor(id);
	
		listInstructor(request, response);	

	}
}
