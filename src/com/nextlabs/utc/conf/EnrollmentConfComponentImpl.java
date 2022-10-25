/*
 * Created on Mar 13, 2013
 * 
 * All sources, binaries and HTML pages (C) copyright 2013 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 * 
 */

package com.nextlabs.utc.conf;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.nextlabs.destiny.configclient.ConfigClient;
import com.bluejungle.destiny.appframework.appsecurity.loginmgr.ILoginMgr;
import com.bluejungle.destiny.container.dcc.BaseDCCComponentImpl;
import com.bluejungle.destiny.container.dcc.IDCCContainer;
import com.bluejungle.destiny.server.shared.registration.ServerComponentType;
import com.bluejungle.destiny.webui.framework.loginmgr.remote.WebAppRemoteLoginMgr;
import com.bluejungle.framework.comp.ComponentInfo;
import com.bluejungle.framework.comp.HashMapConfiguration;
import com.bluejungle.framework.comp.IConfiguration;
import com.bluejungle.framework.comp.LifestyleType;
import com.bluejungle.framework.configuration.DestinyRepository;
import com.nextlabs.destiny.configclient.ConfigClient;
import com.bluejungle.framework.datastore.hibernate.IHibernateRepository;

/**
 * This is the implementation class for the UTC Config console component.
 * 
 * @author dwashburn
 * @version $Id: //depot/ProfessionalServices/Enrollment
 *          Viewer/EnrollmentManagerApp/src/com/nextlabs/utc/conf/EnrollmentConfComponentImpl.java#1
 *          $
 */

public class EnrollmentConfComponentImpl extends BaseDCCComponentImpl {

	private static final String SECURE_SESSION_SERVICE_PATH_INFO = "services/SecureSessionService";

	public static Log log = null;
	private static IHibernateRepository activityDataSrc;

	/**
	 * @see com.bluejungle.destiny.server.shared.registration.IRegisteredDCCComponent#getComponentType()
	 */
	public ServerComponentType getComponentType() {
		return ServerComponentType.REPORTER;
	}

	public static IHibernateRepository getActivityDataSource() {
		return activityDataSrc;
	}

	/**
	 * Initializes the Management console component. The initialization sets up the
	 * authentication manager.
	 */
	public void init() {
		log = LogFactory.getLog(EnrollmentConfComponentImpl.class);
		try {
			ConfigClient.init("enrollment");
			super.init();
		} catch (Exception e) {
			log.error(e);
		}
		log.info("Enrollment Viewer setting login manager");

		activityDataSrc = (IHibernateRepository) getManager()
				.getComponent(DestinyRepository.ACTIVITY_REPOSITORY.getName());
		if (activityDataSrc == null) {
			throw new RuntimeException("Data source " + DestinyRepository.ACTIVITY_REPOSITORY
					+ " is not correctly setup for the DABS component.");
		}
		// Initializes the login manager
		IConfiguration componentConfiguration = (IConfiguration) getManager()
				.getComponent(IDCCContainer.MAIN_COMPONENT_CONFIG_COMP_NAME);
		final String dmsLocation = (String) componentConfiguration.get("DMSLocation");
		initLoginManager(dmsLocation);

	}

	/**
	 * This function sets up the remote login manager component
	 * 
	 * @param dmsLocation location of the DMS component
	 */
	private void initLoginManager(final String location) {
		if (location == null) {
			final String msg = "Error : no DMS location specified. Unable to intialize login manager";
			getLog().fatal(msg);
			throw new RuntimeException(msg);
		}
		HashMapConfiguration componentConfig = new HashMapConfiguration();
		String loginServiceLocation = location;
		if (!loginServiceLocation.endsWith("/")) {
			loginServiceLocation = loginServiceLocation.concat("/");
		}
		loginServiceLocation = loginServiceLocation.concat(SECURE_SESSION_SERVICE_PATH_INFO);
		componentConfig.setProperty(WebAppRemoteLoginMgr.SECURE_SESSION_SERVICE_ENDPOINT_PROP_NAME,
				loginServiceLocation);
		ComponentInfo componentInfo = new ComponentInfo(ILoginMgr.COMP_NAME, WebAppRemoteLoginMgr.class.getName(),
				ILoginMgr.class.getName(), LifestyleType.SINGLETON_TYPE, componentConfig);
		// Prime the login manager (make sure it comes up)
		ILoginMgr loginMgr = (ILoginMgr) getManager().getComponent(componentInfo);

	}

}
