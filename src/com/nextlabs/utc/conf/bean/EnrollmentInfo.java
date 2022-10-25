package com.nextlabs.utc.conf.bean;

import java.util.HashMap;

public class EnrollmentInfo {
	String EnrollmentDomainName;
	
	// String properties;
	public String getEnrollmentDomainName() {
		return EnrollmentDomainName;
	}

	public void setEnrollmentDomainName(String enrollmentDomainName) {
		EnrollmentDomainName = enrollmentDomainName;
	}

	public String getEnrollmentType() {
		return EnrollmentType;
	}

	public void setEnrollmentType(String enrollmentType) {
		EnrollmentType = enrollmentType;
	}

	public String getActive() {
		return Active;
	}

	public void setActive(String active) {
		Active = active;
	}

	public String getRecurring() {
		return Recurring;
	}

	public void setRecurring(String recurring) {
		Recurring = recurring;
	}

	public String getEMCount() {
		return EMCount;
	}

	public void setEMCount(String eMCount) {
		EMCount = eMCount;
	}

	public String getSGCount() {
		return SGCount;
	}

	public void setSGCount(String sGCount) {
		SGCount = sGCount;
	}

	public String getEMGCount() {
		return EMGCount;
	}

	public void setEMGCount(String eMGCount) {
		EMGCount = eMGCount;
	}

	public HashMap<String, Integer> getEMmap() {
		return EMmap;
	}

	public void setEMmap(HashMap<String, Integer> eMmap) {
		EMmap = eMmap;
	}

	public HashMap<String, Integer> getSGmap() {
		return SGmap;
	}

	public void setSGmap(HashMap<String, Integer> sGmap) {
		SGmap = sGmap;
	}

	public HashMap<String, Integer> getEMGmap() {
		return EMGmap;
	}

	public void setEMGmap(HashMap<String, Integer> eMGmap) {
		EMGmap = eMGmap;
	}

	String EnrollmentType;
	String Active;
	String Recurring;
	String inactiveUser;
	public String getInactiveUser() {
		return inactiveUser;
	}

	public void setInactiveUser(String inactiveUser) {
		this.inactiveUser = inactiveUser;
	}

	String EMCount;
	String SGCount;
	String EMGCount;
	String lastSyncTime;
	String enrollmentID;
	public String getEnrollmentID() {
		return enrollmentID;
	}

	public void setEnrollmentID(String enrollmentID) {
		this.enrollmentID = enrollmentID;
	}

	public String getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(String lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	HashMap<String, Integer> EMmap;
	HashMap<String, Integer> SGmap;
	HashMap<String, Integer> EMGmap;

}
