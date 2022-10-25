/*
 * Created on Mar 13, 2013
 * 
 * All sources, binaries and HTML pages (C) copyright 2013 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 * 
 */
package com.nextlabs.utc.conf.shared;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.bluejungle.destiny.webui.framework.faces.ActionListenerBase;
import com.bluejungle.destiny.webui.framework.message.MessageManager;
import com.nextlabs.utc.conf.EnrollmentConfConstants;

/**
 * @author dwashburn
 * @version $Id: //depot/ProfessionalServices/Enrollment Viewer/EnrollmentManagerApp/src/com/nextlabs/utc/conf/shared/UTCConfActionListenerBase.java#1 $:
 */

public abstract class UTCConfActionListenerBase extends ActionListenerBase {

    /**
     * Add a non-parameterized single line error message with the specified
     * bundle key 
     * 
     * @param messageKey
     */
    protected void addErrorMessage(String messageKey) {
        FacesMessage facesMessage = new FacesMessage();
        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        
        String messageDetail = getOdrmConsoleBundle().getString(messageKey);
        facesMessage.setDetail(messageDetail);
        MessageManager.getInstance().addMessage(facesMessage);
    }

    /**
     * Add a non-parameterized single line success message
     */
    protected void addSuccessMessage(String messageKey) {
        ResourceBundle odrmBundle = getOdrmConsoleBundle();
    
        FacesMessage facesMessage = new FacesMessage();
        facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
        String messageDetail = odrmBundle.getString(messageKey);
        facesMessage.setDetail(messageDetail);
        MessageManager.getInstance().addMessage(facesMessage);
    }

    /**
     * Retrieve the On Demand Rights Management Console Bundle
     * 
     * @param currentLocale
     * @return
     */
    protected ResourceBundle getOdrmConsoleBundle() {
        Locale currentLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return ResourceBundle.getBundle(EnrollmentConfConstants.ENROLLMENT_VIEWER_BUNDLE_NAME, currentLocale);
    }

}
