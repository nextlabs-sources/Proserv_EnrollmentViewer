package com.nextlabs.utc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bluejungle.destiny.appframework.appsecurity.loginmgr.ILoggedInUser;
import com.bluejungle.destiny.container.shared.applicationusers.core.IApplicationUserManager;
import com.bluejungle.destiny.webui.framework.context.AppContext;
import com.bluejungle.dictionary.Dictionary;
import com.bluejungle.dictionary.DictionaryException;
import com.bluejungle.framework.comp.ComponentManagerFactory;
import com.bluejungle.framework.comp.IComponentManager;
import com.bluejungle.framework.datastore.hibernate.HibernateUtils;
import com.bluejungle.framework.datastore.hibernate.IHibernateRepository;
import com.google.gson.Gson;
import com.nextlabs.enrollment.webapp.hibernate.EnrollmentSession;
import com.nextlabs.utc.conf.EnrollmentConfComponentImpl;
import com.nextlabs.utc.conf.bean.EnrollmentInfo;
import com.nextlabs.utc.conf.bean.TotalUsers;
import com.nextlabs.utc.conf.helper.DictHelper;

import net.sf.hibernate.Session;

/**
 * Servlet implementation class CCLCountryServlet
 */

public class DictServlet extends HttpServlet {
	public final static String APP_AUTHS_ATTR = "app_auths";
	private static final long serialVersionUID = 1L;
	public static final String VIEW_ADMINISTRATOR_ACTION = "VIEW_ADMINISTRATOR";
	private static final String ROLE_SYSTEM_ADMINISTRATOR = "System Administrator";
	public final static String UN_AUTH_ACCESS_REDIRECT = "unAuthAccessRedirectUrl";
	private String unAuthAccessRedirectUrl = "./logout";

	/**
	 * Default constructor.
	 */
	public DictServlet() {
		EnrollmentConfComponentImpl.log.info("-----------Dictionary Servlet Loaded---------");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 * response
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	private AppContext populateAuths(AppContext ctx, String username, HttpServletRequest httpReq, Dictionary dict) {
		ILoggedInUser loggedInUser = getAuthenticatedUser(username, ctx,dict);
		if (loggedInUser != null) {
			EnrollmentConfComponentImpl.log.info("loggedInUser principal name = " + loggedInUser.getPrincipalName()
					+ " and username = " + loggedInUser.getUsername());
			ctx.setRemoteUser(loggedInUser);
			if (loggedInUser.getUsername().equalsIgnoreCase(IApplicationUserManager.SUPER_USER_USERNAME)) {
				Set<String> auths = new TreeSet<String>();
				auths.add(VIEW_ADMINISTRATOR_ACTION);
				auths.add(ROLE_SYSTEM_ADMINISTRATOR);
				ctx.setAttribute(APP_AUTHS_ATTR, auths);
			}
		}
		return ctx;
	}

	private ILoggedInUser getAuthenticatedUser(String username, AppContext ctx, Dictionary dict) {
		Session session = null;
		String query = "";
		ILoggedInUser loggedInUser = null;
		EnrollmentConfComponentImpl.log.info("loggedInUser principal name = " + username);
		try {
			if (username.equalsIgnoreCase(IApplicationUserManager.SUPER_USER_USERNAME)) {
				query = "SELECT u.ID, u.USERNAME, u.FIRST_NAME, u.LAST_NAME, u.DOMAIN_ID, d.NAME  as DOMAIN, 'internal' as USER_TYPE, 'ADMIN' as USER_CATEGORY"
						+ " FROM SUPER_APPLICATION_USER u LEFT JOIN APPLICATION_USER_DOMAIN d ON ( d.ID = u.DOMAIN_ID)"
						+ "  WHERE LOWER(u.USERNAME) = ? ";
			} else {
				query = "SELECT u.ID, u.USERNAME, u.FIRST_NAME, u.LAST_NAME, u.DOMAIN_ID, d.NAME as DOMAIN, u.USER_TYPE, u.USER_CATEGORY"
						+ " FROM APPLICATION_USER u LEFT JOIN APPLICATION_USER_DOMAIN d ON ( d.ID = u.DOMAIN_ID)"
						+ " WHERE LOWER(u.USERNAME) = ? ";
			}
			EnrollmentSession es=new EnrollmentSession();
			es.setManager(dict.getManager());
			es.init();
			
			//IHibernateRepository dataSource = EnrollmentConfComponentImpl.getActivityDataSource();

			session = es.getSession();
			Connection conn = session.connection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username.toLowerCase());

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				final Long userId = rs.getLong("ID");
				final String principalName = rs.getString("USERNAME").trim();
				final String firstName = rs.getString("FIRST_NAME");
				final String lastName = rs.getString("LAST_NAME");
				final String name = firstName + ((lastName != null) ? " " + lastName : "");
				final String domainName = rs.getString("DOMAIN");
				final String userType = rs.getString("USER_TYPE");
				ctx.setUserCategory(rs.getString("USER_CATEGORY"));

				loggedInUser = new ILoggedInUser() {

					@Override
					public Long getPrincipalId() {
						return userId;
					}

					@Override
					public String getUsername() {
						return name;
					}

					@Override
					public String getPrincipalName() {
						return (username.equalsIgnoreCase(IApplicationUserManager.SUPER_USER_USERNAME)) ? principalName
								: principalName + "@" + domainName;
					}

					@Override
					public boolean isPasswordModifiable() {
						if ("internal".equals(userType))
							return true;
						else
							return false;
					}

					@Override
					public String toString() {
						return "LoggedInUser [ User Id:" + this.getPrincipalId() + ", Principal name: "
								+ this.getPrincipalName() + "]";
					}
				};
			}

			rs.close();
			stmt.close();
		} catch (Exception e) {
			EnrollmentConfComponentImpl.log.error("Error occurred during fetch logged in user details", e);
		} finally {
			HibernateUtils.closeSession(session, EnrollmentConfComponentImpl.log);
			;
		}
		return loggedInUser;
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String tableAction = request.getParameter("tableaction");
		IComponentManager componentManager = ComponentManagerFactory.getComponentManager();
		EnrollmentConfComponentImpl.log
				.info("-----------Component Manager initialized " + componentManager + "---------");
		Dictionary dict = componentManager.getComponent(Dictionary.COMP_INFO);
		EnrollmentConfComponentImpl.log.info("-----------Dictionary Manager initialized" + dict + " ---------");
		AppContext myContext = AppContext.getContext(request);

		EnrollmentConfComponentImpl.log.info("-----------myContext.isLoggedIn()---------" + myContext.isLoggedIn());
		EnrollmentConfComponentImpl.log.info("-----------request.getRemoteUser()---------" + request.getRemoteUser());

		if (request.getRemoteUser() != null && !myContext.isLoggedIn()) {
			Principal principal = request.getUserPrincipal();
			String username = principal.getName();
			EnrollmentConfComponentImpl.log.info("--------------------" + username);
			myContext = populateAuths(myContext, username, request,dict);
			if (!username.equalsIgnoreCase(IApplicationUserManager.SUPER_USER_USERNAME)) {
				EnrollmentConfComponentImpl.log.warn("---USER is not an Super Admin---");
				response.sendRedirect(this.unAuthAccessRedirectUrl);
				;
			}

		}

		/*
		 * if (myContext.getRemoteUser() != null) { EnrollmentConfComponentImpl.log
		 * .info("-----------myContext user name---------" +
		 * myContext.getRemoteUser().getUsername()); } else {
		 * getServletContext().getRequestDispatcher("/login/login.jsf")
		 * .forward(request, response); }
		 */
		if (tableAction != null) {
			EnrollmentConfComponentImpl.log.info("-----------tableaction:" + tableAction + "---------");
			PrintWriter pw;
			pw = response.getWriter();
			try {
				/*
				 * IComponentManager componentManager = (IComponentManager) request
				 * .getSession().getServletContext() .getAttribute("componentManager");
				 */
			

				if (tableAction.equalsIgnoreCase("load")) {
					ArrayList<EnrollmentInfo> eiList = new ArrayList<EnrollmentInfo>();
					EnrollmentConfComponentImpl.log.info("###########Inside LOAD ###########");
					eiList = DictHelper.getEnrollmentInfos(dict);
					Gson gson = new Gson();
					String toJson = gson.toJson(eiList);
					EnrollmentConfComponentImpl.log.info("###########Inside JSON ###########" + toJson);
					response.setContentType("text/json");
					pw.println(toJson);
					EnrollmentConfComponentImpl.log.info("###########Inside JSON ###########" + toJson);
				} else if (tableAction.equalsIgnoreCase("getProperty")) {
					String elementType = request.getParameter("type");
					String enrollmentId = request.getParameter("id");
					response.setContentType("text/html");
					pw.println(DictHelper.getProperty(elementType, enrollmentId));
				} else if (tableAction.equalsIgnoreCase("getEnrollment")) {
					response.setContentType("text/html");
					pw.println(DictHelper.getEnrollmentForDropDown());
				} else if (tableAction.equalsIgnoreCase("getFilteredResult")) {
					String elementType = request.getParameter("type");
					String enrollmentId = request.getParameter("id");
					String filterVal = request.getParameter("filter");
					int interval = 20;
					try {
						interval = Integer.parseInt(request.getParameter("interval"));
					} catch (Exception e) {
						EnrollmentConfComponentImpl.log
								.warn("Exception is parsing interval for pagination. Interval is set to default 20");
					}
					String projections[] = request.getParameterValues("projections[]");
					TotalUsers totalUser = DictHelper.getFilteredData(elementType, enrollmentId, filterVal, projections,
							interval);
					Gson gson = new Gson();
					String toJson = gson.toJson(totalUser);
					response.setContentType("text/json");
					pw.println(toJson);
				} else if (tableAction.equalsIgnoreCase("getUserData")) {
					String id = request.getParameter("value");
					response.setContentType("text/html");
					pw.println(DictHelper.getDataSummary(id));
				} else if (tableAction.equalsIgnoreCase("getEnrollmentProperty")) {
					String id = request.getParameter("value");
					response.setContentType("text/html");
					pw.println(DictHelper.getEnrollmentProperties(id));
				} else if (tableAction.equalsIgnoreCase("paginate")) {
					int start = Integer.parseInt(request.getParameter("startVal"));
					int interval = Integer.parseInt(request.getParameter("intervalVal"));
					response.setContentType("text/json");
					TotalUsers totalUser = DictHelper.getPaginationData(start, interval);
					Gson gson = new Gson();
					String toJson = gson.toJson(totalUser);
					pw.println(toJson);
				} else if (tableAction.equalsIgnoreCase("listGroups")) {
					String id = request.getParameter("id");
					String filterVal = request.getParameter("val");

					TotalUsers totalUser = DictHelper.getGroups(id, filterVal);
					Gson gson = new Gson();
					String toJson = gson.toJson(totalUser);
					response.setContentType("text/json");
					pw.println(toJson);

				} else if (tableAction.equalsIgnoreCase("getgfilter")) {
					String id = request.getParameter("id");
					response.setContentType("text/html");
					pw.println(DictHelper.getOptionalFilterForGroups(id));
				} else if (tableAction.equalsIgnoreCase("getGroupMembers")) {
					String groupid = request.getParameter("group");
					String elementType = request.getParameter("et");
					String enrollmentId = request.getParameter("enrollmentId");
					int interval = 10;
					try {
						interval = Integer.parseInt(request.getParameter("interval"));
					} catch (Exception e) {
						EnrollmentConfComponentImpl.log
								.warn("Exception is parsing interval for pagination. Interval is set to default 10");
					}

					EnrollmentConfComponentImpl.log.info("Inteval value in getgroup members:" + interval);
					TotalUsers totalUser = DictHelper.getGroupMembers(elementType, groupid, interval, enrollmentId);
					Gson gson = new Gson();
					String toJson = gson.toJson(totalUser);
					EnrollmentConfComponentImpl.log.info("EV: RESULT json result:" + toJson);
					response.setContentType("text/json");
					pw.println(toJson);

				} else if (tableAction.equalsIgnoreCase("validateEnrollment")) {
					String id = request.getParameter("value");
					String etype = request.getParameter("etype");
					response.setContentType("text/html");
					pw.println(DictHelper.validateEnrollment(id, etype));
				} else if (tableAction.equalsIgnoreCase("removeInActiveUsers")) {
					String enrollment_ids = request.getParameter("enrollmentIds");

					String[] enrollmentIds = enrollment_ids.split(",");

					String result = "Successfully removed inactive users of ";
					try {
						for (String enrollmentId : enrollmentIds) {
							if (result.endsWith("of ")) {
								result += DictHelper.removeInactiveUsers(enrollmentId);

							} else {
								result += ", ";
								result += DictHelper.removeInactiveUsers(enrollmentId);
							}

						}
						result += " are removed.";
					} catch (Exception e) {
						result = "Failure in removing the inactive users";
					}
					EnrollmentConfComponentImpl.log.info("###########result###########" + result);

					response.setContentType("text/html");
					pw.println(result);
					EnrollmentConfComponentImpl.log.info("###########Enrollment Deleted Sucessfully###########");

				} else if (tableAction.equalsIgnoreCase("deleteEnrollments")) {

					String enrollment_ids = request.getParameter("enrollmentIds");

					String[] enrollmentIds = enrollment_ids.split(",");

					String result = "Successfully Enrollment of ";
					try {
						for (String enrollmentId : enrollmentIds) {
							if (result.endsWith("of ")) {
								result += DictHelper.removeEnrollment(enrollmentId);

							} else {
								result += ", ";
								result += DictHelper.removeEnrollment(enrollmentId);
							}

						}
						result += " are deleted.";
					} catch (Exception e) {
						result = "Failure in removing the enrollments";
					}
					EnrollmentConfComponentImpl.log.info("###########result###########" + result);

					response.setContentType("text/html");
					pw.println(result);
					EnrollmentConfComponentImpl.log.info("###########Enrollment Deleted Sucessfully###########");

				}

			} catch (DictionaryException e) {
				EnrollmentConfComponentImpl.log
						.error("-----------Dictionary Component instantiatation failure---------", e);

			} catch (Exception e) {
				EnrollmentConfComponentImpl.log.info("-----------Exception Occured---------" + e.toString());
			}

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

}
