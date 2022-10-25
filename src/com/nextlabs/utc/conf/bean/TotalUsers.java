package com.nextlabs.utc.conf.bean;

import java.util.ArrayList;

public class TotalUsers {
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTableData() {
		return tableData;
	}

	public void setTableData(String tableData) {
		this.tableData = tableData;
	}

	public ArrayList<String> getElementIds() {
		return elementIds;
	}

	public void setElementIds(ArrayList<String> elementIds) {
		this.elementIds = elementIds;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public String getTempString() {
		return tempString;
	}

	public void setTempString(String tempString) {
		this.tempString = tempString;
	}
	ArrayList<GroupBean> groups; 
	public ArrayList<GroupBean> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<GroupBean> groups) {
		this.groups = groups;
	}
	String tempString;
	String total;
	String tableData;
	ArrayList<String> elementIds;
	int start, interval, totalPages;
}
