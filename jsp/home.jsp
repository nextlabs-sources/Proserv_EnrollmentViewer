<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.nextlabs.utc.conf.helper.CommonConstants"%>
<%@page import="com.nextlabs.utc.conf.EnrollmentConfComponentImpl"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.security.Principal" %>
<%@ page
	import="com.bluejungle.destiny.webui.framework.context.AppContext"%>
<html lang="en">

<head>
<meta charset="utf-8">
<meta name="COPYRIGHT" content="© NextLabs">

<title>Enrollment Viewer</title>
<link rel="stylesheet" href="css/jquery.ui.all.css">
<script src="javascript/jquery-1.9.1.js"></script>
<script src="javascript/jquery.ui.core.js"></script>
<script src="javascript/jquery.ui.widget.js"></script>
<script src="javascript/jquery.ui.tabs.js"></script>
<link href="css/table/jquery-ui-1.8.16.custom.css" rel="stylesheet"
	type="text/css" />
<link href="css/table/lightgray/jtable.css" rel="stylesheet"
	type="text/css" />
<script src="javascript/home.js" type="text/javascript"></script>
<script src="javascript/jquery-ui.js" type="text/javascript"></script>
<script src="javascript/jquery.jtable.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/demos.css">
<script>
	$(document).ready(function() {
		loadData(1);
		$("#tabs").tabs();
		$("#subtabs").tabs();
		//	$("#subtabs").tabs("option", "disabled", [1,2,3,4,5]);

	});
</script>
<%
	boolean isWriter = false;
	String searchName = "uisearchbutton1";
	String ipmSearchName = "uiipmsearchbutton1";
%>
</head>
<body>
<body class="Text TextMedium Link LayoutP">
	<div id="cbWrapper">
		<div id="cbHeader">
			<!-- <img class="box1" src="images/header.jpg">  -->
			<span id="login"><b style="font-size: 15px">Enrollment
					Viewer</b><br> <br>Logged in as: <%
 	AppContext myContext = AppContext.getContext(request);
					Principal principal = request.getUserPrincipal();
					
 
 	session.setAttribute("request", request);
 	if (principal != null) {
 		String username = principal.getName();
 		if(username!=null || username.length()>0)
 		out.print(username);
 		isWriter = true;
 	}/*  else if (myContext.getRemoteUser() != null) {
 		ArrayList<String> groupList = ugh.getGroupForUser(myContext
 				.getRemoteUser());
 		EnrollmentConfComponentImpl.log.info("groupList" + groupList);
 		EnrollmentConfComponentImpl.log
 				.info("before validation isWriter" + isWriter);
 		if (groupList != null
 				&& groupList.contains(CommonConstants.CONFIG_WRITE)) {
 			isWriter = true;

 		}
 		EnrollmentConfComponentImpl.log.info("isWriter" + isWriter);
 		out.print(myContext.getRemoteUser().getUsername());
 		} else {
 		getServletContext().getRequestDispatcher("/login/login.jsf")
 				.forward(request, response);
 	}*/
 	request.setAttribute("isWriter", isWriter);
 %> |<a href="LogOutServlet"> Logout</a> </span>
		</div>
		<div id="tabs">
			<ul>
				<li id="enroll-summary"><a href="#tabs-1" onclick='loadData(0)'>Enrollment
						Summary</a></li>
				<li id="enroll"
					onclick="getEnrollment('userenrollmentId','u','propertycheckbox','1')"><a
					href="#tabs-2">Search Enrolled Data</a></li>
			</ul>
			<div id="tabs-1">
				<div class="ccltable-container">
					<div align="left">
						<h2 class='summary-table'>Enrollment Summary</h2>
					</div>
					<div align="right">
						<!-- 	<button id='DeleteEnrollments'
							class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
							role="button" onclick="deleteEnrollment()">
							<span class="ui-button-text" id='dialogbutton'>Delete
								Enrollment</span>
						</button> -->
						<img class="icon" title="Click Here to Delete Enrollments" alt="Delete Enrollment"  src="images/delete.png" onclick="deleteEnrollment();"> Delete Enrollment
						<img class="icon" title="Click Here to Delete Inactive users"  src="images/Delete Inactive.png" onclick="removeInActiveUsers();"> Delete Inactive Users
				<!-- 		<label for="showInActive">Show Inactive Users</label> <input
							type="checkbox" id="cbShowInActive" name="showInActive"
							value="Show InActive Users" onchange="showInActiveUsers();" /> -->
					</div>
					<br>
					<div id="cclContainer" class="jtable-main-container">
						<table id='etable' class="jtable">
							<thead>
								<th class="juris" title="Enrollment Name"><input
									type="checkbox" value="check
									 " name="deleteItemsname"
									id='selectall' onchange='selectAll();'>Enrollment Name</th>
								<th class="classi"
									title="Enrollment Type (Either Active Directory or LDIF)">Type</th>
								<th class="cc" title="To check Enrollment is Active now">Active</th>
								<th class="roc" title="Displays the enrollment last sync time">Last
									Sync Time</th>
								<th class="count" title="Enrolled user elements count">Active User
								</th>
								<th class="count" title="Enrolled Inactive user elements count">Inactive User
								</th>
								<th class="count" title="Enrolled host elements count">Host
								</th>
								<!-- 	<th class="count" title="Enrolled site elements count">Site
								</th> 
								<th class="count" title="Enrolled Application elements count">Application
								</th>-->
								<th class="count" title="Groups Count">Group</th>
									<th class="count" title="Delete">Delete</th>
								<!-- <th class="count"
									title="Validate the enrollment">Validate 
									</th> -->

							</thead>
							<tbody id="cclbody">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="tabs-2">

				<div id="subtabs">
					<ul>
						<li><a href="#subtabs-1"
							onclick="getEnrollment('userenrollmentId','u','propertycheckbox','1')">User</a></li>
						<!--<li><a href="#subtabs-2" onclick="getEnrollment('cenrollmentId','c','cpropertycheckbox','2')">CONTACT</a></li>
<li><a href="#subtabs-3" onclick="getEnrollment('henrollmentId','h','hpropertycheckbox','3')">HOST</a></li>
<li><a href="#subtabs-4" onclick="getEnrollment('senrollmentId','s','spropertycheckbox','4')">SITE</a></li>
<li><a href="#subtabs-5" onclick="getEnrollment('ienrollmentId','i','ipropertycheckbox','5')">CLIENT_INFO</a></li>
<li><a href="#subtabs-6" onclick="getEnrollment('aenrollmentId','a','apropertycheckbox','6')">APPLICATION</a></li>-->
						<li><a href="#subtabs-2"
							onClick="getEnrollmentforgroups('genrollmentId')">Groups</a></li>
					</ul>

					<div id='subtabs-1'>
						<div>
							<div class="sidehead">
								<h2 class='summary-table'>Search for Users</h2>
							</div>
							<div id="searchbox" class='searchbox'>
								<label><span>Enrollment Name: &nbsp;&nbsp;</span><br>
									<div style="margin-top: 4px;">
										<select class='selectclass' id="userenrollmentId"
											onchange="populateProperties('1',$('#userenrollmentId').val(),'utbody','propertycheckbox','u');"></select>
									</div></label> <br /> <label><span>Filters for Search
										(Optional):&nbsp;&nbsp;</span></label>
								<div id='utbody' align=center style="margin-top: 4px;"></div>
							</div>
							<div id='upropbut' style="margin-top: 10px;" align=left>
								<div id="propertycheckbox" class="propertycheckbox" align="left">
								</div>
								<br />
								<!--  <input type="button" class='cssbutton' value="Search" id='usearchButton' onClick="searchData('userenrollmentId','utbody','1','uresult_table');"> -->


							</div>
							<br/>
							<button id='usearchButton'
								class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
								role="button"
								onclick="searchData('userenrollmentId','utbody','1','uresult_table');">
								<span class="ui-button-text" id='dialogbutton'>Search</span>
							</button>
							<br />
							<div>
								<div id='utitle' style="display: none;">
									<label><h2 class='summary-table'>
											Found <span id="totalUsers"></span> Users
										</h2> <span>Click on the row to view user details.</span></label>
								</div>

								<br />
							</div>
							<div id="resultContainer" class="jtable-main-container"
								align="center"
								style="width: 98%; margin-left: 7px; margin-top: 1px;">
								<span id='uspan' style="display: none; margin-left: 910px;margin-bottom:6px">Records
									per Page &nbsp;&nbsp;&nbsp;<select id="umaximuminterval"
									onchange="searchDataforIntervalChange('userenrollmentId','utbody','1','uresult_table');"><option
											value="10">10</option>
										<option value="20">20</option>
										<option value="50">50</option>
										<option value="100">100</option>
										<option value="250">250</option>
										<option value="500">500</option></select>
								</span>
								<table class="jtable" id='uresult_table'
									style="table-layout: fixed;">
								</table>
								<br />
							</div>
						</div>
					</div>
					<!-- 
<div id='subtabs-2'>
	<div>
	<div class="sidehead"><h2 class='summary-table' >Search the Enrolled Contact Data</h2></div>
		<div id="csearchbox" class='searchbox'  >
		<label><span>Enrollment &nbsp;&nbsp;</span>
		<span class="css3-selectbox"><select id="cenrollmentId" onchange="populateProperties('2',$('#cenrollmentId').val(),'ctbody','cpropertycheckbox','c');"></select></span></label><br/>
		<br/>
		<label><span>Enrollment Property Filters&nbsp : &nbsp</span></label>
			<div id='ctbody' align=center >
			</div>
		</div>
	<div id='cpropbut' style="vertical-align=middle;margin-top:10px" align="left">
		<div id="cpropertycheckbox"  class="propertycheckbox" align="left">
		</div>
			<br/>
 		 <input type="button" class='cssbutton' value="Search" id='csearchButton' onClick="searchData('cenrollmentId','ctbody','2','cresult_table');">
	</div>
		<br/>
	<div><div id='ctitle' style="display:none;"><label><h3 class='summary-table'>Filtered Result</h3><span>Click on the records to view entire contact details</span></label></div>
	<div class="sidehead" id='cgtitle' style="display:none;"><label><h3 class='summary-table'>Group Members</h3></span></label></div><br /></div>
		<div id="cresultContainer" class="jtable-main-container" align="center" style="width:900px;margin-left:12px;margin-top: 1px;">
			<table class="jtable" id='cresult_table' style="table-layout: fixed;">
			</table>
			<br/>	
		</div>
	</div>
</div>
<div id='subtabs-3'>
	<div>
	<div class="sidehead"><h2 class='summary-table' >Search the Enrolled Host Data</h2></div>
		<div id="hsearchbox" class='searchbox'  >
		<label><span>Enrollment &nbsp; &nbsp;</span>
		<span class="css3-selectbox"><select id="henrollmentId" onchange="populateProperties('3',$('#henrollmentId').val(),'htbody','hpropertycheckbox','h');"></select></span></label><br/>
		<br/>
		<label><span>Enrollment Property Filters&nbsp : &nbsp</span></label>
			<div id='htbody' align=center >
			</div>
		</div>
	<div id='hpropbut' style="vertical-align=middle;margin-top:10px" align="left">
		<div id="hpropertycheckbox"  class="propertycheckbox" align="left">
		</div>
			<br/>
 		 <input type="button" class='cssbutton' value="Search" id='hsearchButton' onClick="searchData('henrollmentId','htbody','3','hresult_table');">
	</div>
		<br/>
	<div><div id='htitle' style="display:none;"><label><h3 class='summary-table'>Filtered Result</h3><span>Click on the records to view entire Host details</span></label></div>
	<div class="sidehead" id='hgtitle' style="display:none;"><label><h3 class='summary-table'>Group Members</h3></span></label></div><br /></div>
		<div id="hresultContainer" class="jtable-main-container" align="center" style="width:900px;margin-left:20px;margin-top: 1px;">
			<table class="jtable" id='hresult_table' style="table-layout: fixed;">
			</table>
			<br/>	
		</div>
	</div>
</div>
<div id='subtabs-4'>
	<div>
	<div class="sidehead"><h2 class='summary-table'>Search the Enrolled Site Data</h2></div>
		<div id="ssearchbox" class='searchbox'  >
		<label><span>Enrollment &nbsp; &nbsp;</span>
		<span class="css3-selectbox"><select id="senrollmentId" onchange="populateProperties('4',$('#senrollmentId').val(),'stbody','spropertycheckbox','s');"></select></span></label><br/>
		<br/>
		<label><span>Enrollment Property Filters&nbsp : &nbsp</span></label>
			<div id='stbody' align=center >
			</div>
		</div>
	<div id='spropbut' style="vertical-align=middle;margin-top:10px" align="left">
		<div id="spropertycheckbox"  class="propertycheckbox" align="left">
		</div>
			<br/>
 		 <input type="button" class='cssbutton' value="Search" id='ssearchButton' onClick="searchData('senrollmentId','stbody','4','sresult_table');">
	</div>
		<br/>
	<div><div id='stitle' style="display:none;"><label><h3 class='summary-table'>Found Users</h3><span>Click on the records to view entire site details</span></label></div>
	<div class="sidehead" id='sgtitle' style="display:none;"><label><h3 class='summary-table'>Group Members</h3></span></label></div><br /></div>
		<div id="sresultContainer" class="jtable-main-container" align="center" style="width:900px;margin-left:20px;margin-top: 1px;">
			<table class="jtable" id='sresult_table' style="table-layout: fixed;">
			</table>
			<br/>	
		</div>
	</div>
</div>
<div id='subtabs-5'>
	<div>
	<div class="sidehead"><h2 class='summary-table' >Search the Enrolled client info Data</h2></div>
		<div id="isearchbox" class='searchbox'  >
		<label><span>Enrollment &nbsp;  &nbsp;</span>
		<span class="css3-selectbox"><select id="ienrollmentId" onchange="populateProperties('5',$('#ienrollmentId').val(),'itbody','ipropertycheckbox','i');"></select></span></label><br/>
		<br/>
		<label><span>Enrollment Property Filters&nbsp : &nbsp</span></label>
			<div id='itbody' align=center >
			</div>
		</div>
	<div id='ipropbut'  style="vertical-align=middle;margin-top:10px" align="left">
		<div id="ipropertycheckbox"  class="propertycheckbox" align="left">
		</div>
			<br/>
 		 <input type="button" class='cssbutton' value="Search" id='isearchButton' onClick="searchData('ienrollmentId','itbody','5','iresult_table');">
	</div>
		<br/>
	<div><div id='ititle' style="display:none;"><label><h3 class='summary-table'>Filtered Result</h3><span>Click on the records to view entire client info details</span></label></div>
	<div class="sidehead" id='igtitle' style="display:none;"><label><h3 class='summary-table'>Group Members</h3></span></label></div><br /></div>
		<div id="iresultContainer" class="jtable-main-container" align="center" style="width:900px;margin-left:20px;margin-top: 1px;">
			<table class="jtable" id='iresult_table' style="table-layout: fixed;">
			</table>
			<br/>	
		</div>
	</div>
</div>
<div id='subtabs-6'>
	<div>
<div class="sidehead">	<h2 class='summary-table' >Search the Enrolled Application Data</h2></div>
		<div id="asearchbox" class='searchbox'  >
		<label><span>Enrollment Name &nbsp; &nbsp;</span>
		<span class="css3-selectbox"><select id="aenrollmentId" onchange="populateProperties('6',$('#aenrollmentId').val(),'atbody','apropertycheckbox','a');"></select></span></label><br/>
		<br/>
		<label><span>&nbsp;Enrollment Property Filters&nbsp : &nbsp</span></label>
			<div id='atbody' align=center >
			</div>
		</div>
	<div id='apropbut' style="vertical-align=middle;margin-top:10px" align="left">
		<div id="apropertycheckbox"  class="propertycheckbox" align="left">
		</div>
			<br/>
 		 <input type="button" class='cssbutton' value="Search" id='asearchButton' onClick="searchData('aenrollmentId','atbody','6','aresult_table');">
	</div>
		<br/>
<div><div id='atitle' style="display:none;"><label><h3 class='summary-table'>Filtered Result</h3><span>Click on the records to view entire Application details</span></label></div>
<div class="sidehead" id='agtitle' style="display:none;"><label><h3 class='summary-table'>Group Members</h3></span></label></div><br /></div>
		<div id="aresultContainer" class="jtable-main-container" align="center" style="width:900px;margin-left:20px;margin-top: 1px;">
			<table class="jtable" id='aresult_table' style="table-layout: fixed;">
			</table>
			<br/>	
		</div>
	</div>
</div>
 -->
					<div id='subtabs-2'>
						<div>
							<div class="sidehead">
								<h2 class="summary-table">Search for Groups</h2>
							</div>
							<div id="gsearchbox" class="searchbox">
								<label><span>Enrollment Name:&nbsp;&nbsp; </span></label> <br>
								<div style="margin-top: 4px">
									<select class='selectclass' id="genrollmentId"
										onchange="populategfilter($('#genrollmentId').val());">
									</select>
								</div>
								<br> <label><span>Select Filters for Search
										(Optional): &nbsp; &nbsp;</span></label><br>
								<div style="margin-top: 4px">
									<select class='selectclass' id="gfilter" onchange="resetonchange('gtextbox')"></select> <input
										type="text" id="gtextbox" class="tb7" value="">
								</div>
								<br />
								<!-- <input type="button" class="cssbutton" value="Search"
									id="gsearchButton"
									onclick="listGroups('genrollmentId','gresult_table');"> -->
							</div>

							<button id='gsearchButton'
								class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
								role="button"
								onclick="listGroups('genrollmentId','gresult_table');">
								<span class="ui-button-text" id='dialogbutton'>Search</span>
							</button>
						</div>
						<div id='gtitle' style="display: none;">
							<label><h3 class='summary-table'>Found Groups</h3> <!--  <span style="margin-left:715px;">Records per Page &nbsp;&nbsp;&nbsp;<select id="gmaximuminterval"><option value="10">10</option><option value="20">20</option><option value="50">50</option><option value="100">100</option><option value="250">250</option><option value="500">500</option></select></span>--></label>
						</div>
						<div id="gresultContainer" class="jtable-main-container"
							align="center"
							style="width: 900px; margin-left: 12px; margin-top: 8px;">
							<table class="jtable" id='gresult_table'
								style="table-layout: fixed;">
							</table>
						</div>

					</div>
				</div>
				<jsp:include page="dialog.jsp" />
				<form name='enrollform' action='DictServlet' method='post'>
					<input type="hidden" name='tableaction'
						value='FindEnrollmentValues' /> <input type="hidden"
						id='enrollmentname' name='enrollmentname' value='' /> <input
						type="hidden" id='mapvalue' name='map' value='' /> <input
						type="hidden" id='udropdown' name='udropdown' value='' />
				</form>
				<input type="hidden" id='isChecked' value="0" />
</body>
</html>
