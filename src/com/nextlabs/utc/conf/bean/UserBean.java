package com.nextlabs.utc.conf.bean;

public class UserBean {
String principalName,displayName,firstName,SecondName,summary;
public String getPrincipalName() {
	return principalName;
}

public void setPrincipalName(String principalName) {
	this.principalName = principalName;
}

public String getDisplayName() {
	return displayName;
}

public void setDisplayName(String displayName) {
	this.displayName = displayName;
}

public String getFirstName() {
	return firstName;
}

public void setFirstName(String firstName) {
	this.firstName = firstName;
}

public String getSecondName() {
	return SecondName;
}

public void setSecondName(String secondName) {
	SecondName = secondName;
}

public String getSummary() {
	return summary;
}

public void setSummary(String summary) {
	this.summary = summary;
}
}
