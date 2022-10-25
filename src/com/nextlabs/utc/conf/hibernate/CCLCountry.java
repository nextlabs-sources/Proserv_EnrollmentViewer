package com.nextlabs.utc.conf.hibernate;

public class CCLCountry {

	public int getCclid() {
		return cclid;
	}

	public void setCclid(int cclid) {
		this.cclid = cclid;
	}

	int cclid;

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getReasonForControl() {
		return reasonForControl;
	}

	public void setReasonForControl(String reasonForControl) {
		this.reasonForControl = reasonForControl;
	}

	String countryCode;

	String jurisdiction;
	String classification;
	String reasonForControl;
	int cCLFlag;

	public int getcCLFlag() {
		return cCLFlag;
	}

	public void setcCLFlag(int cCLFlag) {
		this.cCLFlag = cCLFlag;
	}
}