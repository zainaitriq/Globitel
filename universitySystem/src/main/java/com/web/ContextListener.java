package com.web;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.apache.log4j.PropertyConfigurator;

/**
 * Servlet implementation class ContextListener
 */
@WebServlet("/ContextListener")
public class ContextListener implements ServletContextListener {
	/**
	 * Initialize log4j when the application is being started
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		// initialize log4j here
		ServletContext context = event.getServletContext();
		String prefix = context.getRealPath("/");
		String file = System.getProperty("file.separator") + "WEB-INF" + System.getProperty("file.separator")
				+ "classes" + System.getProperty("file.separator") + "log4j.properties";

		if (file != null) {
			PropertyConfigurator.configure(prefix + file);
			System.out.println("Log4J Logging started for application: " + prefix + file);
		} else {
			System.out.println("Log4J Is not configured for application Application: " + prefix + file);
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// do nothing
	}
}
