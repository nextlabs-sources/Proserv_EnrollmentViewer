/*
 * Created on Mar 13, 2013
 * 
 * All sources, binaries and HTML pages (C) copyright 2013 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 * 
 */
package com.nextlabs.utc.conf;

import com.bluejungle.destiny.server.shared.registration.ServerComponentType;

/**
 * Constants which are shared amongst the classes within the On Demand Rights Management console.
 * Please add constants only if it truly makes sense for them to be shared
 * amongst the console
 * 
 * @author dwashburn
 * @version $Id: //depot/ProfessionalServices/Enrollment Viewer/EnrollmentManagerApp/src/com/nextlabs/utc/conf/EnrollmentConfConstants.java#1 $
 */
public final class EnrollmentConfConstants {
	
    public static final String ENROLLMENT_VIEWER_BUNDLE_NAME = "EnrollmentViewerMessages";
    
    public static final String COMPONENT_TYPE_NAME = "reporter";
    
    public static final ServerComponentType COMPONENT_TYPE = 
            ServerComponentType.fromString(EnrollmentConfConstants.COMPONENT_TYPE_NAME);
        

    
}