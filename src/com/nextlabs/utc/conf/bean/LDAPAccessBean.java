package com.nextlabs.utc.conf.bean;
import java.util.HashMap;


public class LDAPAccessBean {
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getPortno() {
		return portno;
	}
	public void setPortno(String portno) {
		this.portno = portno;
	}
	public String getAuthentication() {
		return authentication;
	}
	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOuroot() {
		return ouroot;
	}
	public void setOuroot(String ouroot) {
		this.ouroot = ouroot;
	}

	public String getEnrollmentid() {
		return enrollmentid;
	}
	public void setEnrollmentid(String enrollmentid) {
		this.enrollmentid = enrollmentid;
	}
	String enrollmentid;
	String hostname;
	String portno;
	String authentication;
	String username;
	String password;
	String ouroot;
	public HashMap<String, String> getSubtreefiltermap() {
		return subtreefiltermap;
	}
	public void setSubtreefiltermap(HashMap<String, String> subtreefiltermap) {
		this.subtreefiltermap = subtreefiltermap;
	}
	HashMap<String,String> subtreefiltermap;
	String primaryKey;
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	HashMap<String,String> attributelabelmapping;
	public HashMap<String, String> getAttributelabelmapping() {
		return attributelabelmapping;
	}
	public void setAttributelabelmapping(
			HashMap<String, String> attributelabelmapping) {
		this.attributelabelmapping = attributelabelmapping;
	}

}
