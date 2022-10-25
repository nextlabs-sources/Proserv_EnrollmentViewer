package com.nextlabs.utc.servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bluejungle.destiny.webui.framework.context.AppContext;
import com.nextlabs.utc.conf.EnrollmentConfComponentImpl;

/**
 * Servlet implementation class LogOutServlet
 */

public class LogOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public final static String UN_AUTH_ACCESS_REDIRECT = "unAuthAccessRedirectUrl";
	private String unAuthAccessRedirectUrl = "./logout";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LogOutServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		AppContext myContext = AppContext.getContext(request);
		EnrollmentConfComponentImpl.log.info("myContext.getRemoteUser().getUsername(): "+myContext.getRemoteUser().getUsername());
		myContext=null;
		EnrollmentConfComponentImpl.log.warn("---USER is not an Super Admin---");
		response.sendRedirect(this.unAuthAccessRedirectUrl);;

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
