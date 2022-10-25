package com.nextlabs.utc.conf.bean;

import java.util.HashMap;

import com.google.gson.Gson;

public class KeyValueBean {
String key,value;

public String getKey() {
	return key;
}

public void setKey(String key) {
	this.key = key;
}

public String getValue() {
	return value;
}

public void setValue(String value) {
	this.value = value;
}
public static void main(String args[])
{
	EnrollmentInfo ei=new EnrollmentInfo();
	ei.setEnrollmentDomainName("Sample");
	ei.setEnrollmentType("LDIF");
	ei.setActive("Y");
	ei.setRecurring("Y");
	ei.setEMCount("23");
	ei.setEMGCount("27");
	ei.setSGCount("29");
	
	HashMap<String, Integer> map=new HashMap<String, Integer>();
	map.put("USER",5);
	map.put("HOST",5);
	map.put("SITE",5);
	map.put("APPLICATION",5);
	map.put("CONTACT",5);
	map.put("CLIENT_INFO",5);
	ei.setEMmap(map);
	HashMap<String, Integer> smap=new HashMap<String, Integer>();
	smap.put("USER",5);
	smap.put("HOST",3);
	smap.put("SITE",5);
	smap.put("APPLICATION",5);
	smap.put("CONTACT",1);
	smap.put("CLIENT_INFO",5);
	ei.setSGmap(smap);
	HashMap<String, Integer> sgmap=new HashMap<String, Integer>();
	sgmap.put("USER",5);
	sgmap.put("HOST",3);
	sgmap.put("SITE",12);
	sgmap.put("APPLICATION",5);
	sgmap.put("CONTACT",1);
	sgmap.put("CLIENT_INFO",5);
	ei.setEMGmap(sgmap);
	Gson gson = new Gson();
	String toJson = gson.toJson(ei);
	System.out.println(toJson);
	
}
}
