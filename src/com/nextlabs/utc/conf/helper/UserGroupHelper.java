package com.nextlabs.utc.conf.helper;

import com.bluejungle.destiny.container.dcc.BaseDCCComponentImpl;

public class UserGroupHelper /**extends BaseDCCComponentImpl*/ {
	/*
	 * 
	 * private static final String USER_SERVICE_LOCATION_SERVLET_PATH =
	 * "/services/UserRoleServiceIFPort";
	 * 
	 * private UserRoleServiceIF userService; private static final String
	 * USER_GROUP_SERVICE_LOCATION_SERVLET_PATH = "/services/UserGroupService";
	 * 
	 * private UserGroupServiceIF userGroupService;
	 * 
	 * public ArrayList<String> getGroupForUser(ILoggedInUser user) {
	 * ArrayList<String> grouplist = new ArrayList<String>(); try {
	 * EnrollmentConfComponentImpl.log.info("login user id" +
	 * user.getPrincipalId()); if (user != null && user.getPrincipalId() != null) {
	 * UserDTO userdto = getUserService().getUser(
	 * BigInteger.valueOf(user.getPrincipalId().longValue()));
	 * EnrollmentConfComponentImpl.log.info("userdto:" + userdto.getFirstName()); if
	 * (userdto != null) { UserGroupReducedList groupReducedList =
	 * getUserGroupServiceIF() .getUserGroupsForUser(userdto); if (groupReducedList
	 * != null) { UserGroupReduced[] ut = groupReducedList .getUserGroupReduced();
	 * for (UserGroupReduced ugr : ut) {
	 * EnrollmentConfComponentImpl.log.info("Title:" + ugr.getTitle());
	 * EnrollmentConfComponentImpl.log.info("Domain:" + ugr.getDomain());
	 * EnrollmentConfComponentImpl.log.info("ID:" + ugr.getId());
	 * grouplist.add(ugr.getTitle()); } } } }
	 * 
	 * } catch (Exception e) { log.warn("Error in getgroup for user", e); }
	 * 
	 * return grouplist; }
	 * 
	 * private UserRoleServiceIF getUserService() throws ServiceException { if
	 * (this.userService == null) { IComponentManager compMgr =
	 * ComponentManagerFactory .getComponentManager(); IConfiguration mainCompConfig
	 * = (IConfiguration) compMgr
	 * .getComponent(IDCCContainer.MAIN_COMPONENT_CONFIG_COMP_NAME); String location
	 * = (String) mainCompConfig .get(IDCCContainer.DMS_LOCATION_CONFIG_PARAM);
	 * location += USER_SERVICE_LOCATION_SERVLET_PATH; UserRoleServiceLocator
	 * locator = new UserRoleServiceLocator();
	 * locator.setUserRoleServiceIFPortEndpointAddress(location); this.userService =
	 * locator.getUserRoleServiceIFPort(); }
	 * 
	 * return this.userService;
	 * 
	 * }
	 * 
	 *//**
		 * Retrieve the User Group Service. (protected to allow unit testing of this
		 * class)
		 */
	/*
	 * protected UserGroupServiceIF getUserGroupServiceIF() throws ServiceException
	 * { if (this.userGroupService == null) { IComponentManager componentManager =
	 * ComponentManagerFactory .getComponentManager(); IConfiguration mainCompConfig
	 * = (IConfiguration) componentManager
	 * .getComponent(IDCCContainer.MAIN_COMPONENT_CONFIG_COMP_NAME); String location
	 * = (String) mainCompConfig .get(IDCCContainer.DMS_LOCATION_CONFIG_PARAM);
	 * location += USER_GROUP_SERVICE_LOCATION_SERVLET_PATH; UserGroupServiceLocator
	 * locator = new UserGroupServiceLocator();
	 * locator.setUserGroupServiceEndpointAddress(location); this.userGroupService =
	 * locator.getUserGroupService(); }
	 * 
	 * return this.userGroupService; }
	 */}