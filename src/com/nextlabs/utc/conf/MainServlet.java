package com.nextlabs.utc.conf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bluejungle.destiny.appframework.appsecurity.loginmgr.ILoggedInUser;
import com.bluejungle.destiny.webui.framework.context.AppContext;
import com.nextlabs.utc.conf.hibernate.IpcountryMapping;

import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.odmg.Transaction;
import net.sf.hibernate.Query;

public class MainServlet extends HttpServlet {
	
	SessionFactory sessionFactory;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		AppContext myContext = AppContext.getContext(request);
		
		PrintWriter out = response.getWriter();
		
		try {
			setUp(out);
		} catch (Exception e) {
			out.println(e.getMessage());
		}
		
		if (myContext == null) {

			EnrollmentConfComponentImpl.log
			.info("Remote user info could not be found - no context.");

			request.setAttribute("message",
					"Remote user info could not be found. - no context.");
			getServletContext().getRequestDispatcher("/login/login.jsp").forward(
					request, response);
			return;
		}

		ILoggedInUser remoteUser = myContext.getRemoteUser();
		
		if (remoteUser == null) {
			
			out.println("Remote user info could not be found.");
			request.setAttribute("message",
					"Remote user info could not be found.");
		
			return;
			
		}

		Long principalId = remoteUser.getPrincipalId();
		String principalName = remoteUser.getPrincipalName();
		String principalUserName = remoteUser.getUsername();

		EnrollmentConfComponentImpl.log
		.info("remote user info is: Id=" + principalId + " Name=" + principalName + 
				" UserName=" + principalUserName);
		
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	
	}
	
	protected void setUp(PrintWriter out) throws Exception {
		
	    // A SessionFactory is set up once for an application
	    sessionFactory = new Configuration()
	            .configure() // configures settings from hibernate.cfg.xml
	            .buildSessionFactory();
	    
	    Session session = sessionFactory.openSession();
	    net.sf.hibernate.Transaction trans =  session.beginTransaction();
	    
	
	    trans.commit();
	    
	    session.close();	    

	}


}
