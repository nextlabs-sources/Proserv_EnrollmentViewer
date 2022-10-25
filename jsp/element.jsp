<!doctype html>
<%@page import="com.nextlabs.utc.conf.helper.CommonConstants"%>
<%@page import="com.nextlabs.utc.conf.EnrollmentConfComponentImpl"%>
<%@page import="java.util.ArrayList"%>
<%@ page
	import="com.bluejungle.destiny.webui.framework.context.AppContext"%>
<html lang="en">

<head>
<meta charset="utf-8">
<meta name="COPYRIGHT" content="© NextLabs">

<title>Configuration Manager</title>
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
		//	loadData();
		$("#subtabs").tabs();
		$(".jtable tr:nth-child(even)").addClass("even");
		$(".jtable tr:nth-child(odd)").addClass("odd");

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
			<img class="box1" src="images/header.jpg"> <span id="login">Logged
				in as: <%
 	AppContext myContext = AppContext.getContext(request);
 	
 	session.setAttribute("request", request);
 	if (myContext.getRemoteUser() != null
 			&& myContext.getRemoteUser().getPrincipalId() == 0) {
 		out.print(myContext.getRemoteUser().getUsername());
 		isWriter = true;
 	}  else {
 		 String unAuthAccessRedirectUrl = "./logout";
 		response.sendRedirect(unAuthAccessRedirectUrl);
 	}
 	request.setAttribute("isWriter", isWriter);
 %> |<a href="LogOutServlet"> Logout</a>
			</span>
		</div>
		<div id="subtabs">
			<ul id="ulsubtab-element">
				<li><a href="#tabs-1" onclick="loadData()">USER</a></li>
				<li><a href="#tabs-2" onclick="contact()">CONTACT</a></li>
				<li><a href="#tabs-3" onclick="loadData()">HOST</a></li>
				<li><a href="#tabs-4" onclick="loadIPMData()">SITE</a></li>
				<li><a href="#tabs-5" onclick="loadData()">CLIENT_INFO</a></li>
				<li><a href="#tabs-6" onclick="loadIPMData()">APPLICATION</a></li>
				<li><a href="#tabs-7" onclick="loadData()">STRUCTURAL
						GROUPS</a></li>
				<li><a href="#subtabs-8" onclick="loadIPMData()">ENUMERATED
						GROUPS</a></li>

			</ul>
			<div id="tabs-1">

				<div class="ccltable-container">
					<div class="<%=searchName%>">
						<input class="tb10" id="jurisdiction" name="jurisdiction"
							type="text"> <input class="tb10" id="classification"
							name="classification" type="text"> <input class="tb10"
							id="country-code" name="countrycode" type="text"> <input
							class="tb10" id="reason-for-control" name="reasonforcontrol"
							type="text">


						<button
							class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
							role="button" onclick="searchData()">
							<span class="ui-button-text">Search </span>
						</button>
					</div>
					<div id="cclContainer" class="jtable-main-container">
						<table class="jtable">
							<thead>
								<tr>
									<th class="juris">WSID</th>
									<th class="classi">USERID</th>
									<th class="cc">EMAIL</th>
									<th class="roc">Country of Birth</th>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
							</thead>
							<tbody id="cclbody">
								<TR>
									<td class="juris">S-000342-1244
									</th>
									<td class="classi">Ram.Narayanan
									</th>
									<td class="cc">ram.narayanan@nextlabs.com
									</th>
									<td class="roc">IN
									</th>
								</TR>
								<TR>
									<td class="juris">S-000342-1245
									</th>
									<td class="classi">John.Conduit
									</th>
									<td class="cc">john.conduit@nextlabs.com
									</th>
									<td class="roc">US
									</th>
								</TR>
								<TR>
									<td class="juris">S-000342-1246
									</th>
									<td class="classi">Don.Bradman
									</th>
									<td class="cc">don.bradman@nextlabs.com
									</th>
									<td class="roc">AU
									</th>
								</TR>
								<TR>
									<td class="juris">S-000342-1247
									</th>
									<td class="classi">chan.lee
									</th>
									<td class="cc">chan.lee@nextlabs.com
									</th>
									<td class="roc">CN
									</th>
								</TR>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="tabs-2">

				<div class="ccltable-container">
					<div id="cclContainer" class="jtable-main-container">
						<table class="jtable">
							<thead>
								<tr>
									<th class="juris">WSID</th>
									<th class="classi">USERID</th>
									<th class="cc">EMAIL</th>
									<th class="roc">Country of Birth</th>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
							</thead>
							<tbody id="cclbody">

							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="tabs-3">

				<div class="ccltable-container">
					<div id="cclContainer" class="jtable-main-container">
						<table class="jtable">
							<thead>
								<tr>
									<th class="juris">WSID</th>
									<th class="classi">USERID</th>
									<th class="cc">EMAIL</th>
									<th class="roc">Country of Birth</th>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
							</thead>
							<tbody id="cclbody">

							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="tabs-4">

				<div class="ccltable-container">
					<div id="cclContainer" class="jtable-main-container">
						<table class="jtable">
							<thead>
								<tr>
									<th class="juris">WSID</th>
									<th class="classi">USERID</th>
									<th class="cc">EMAIL</th>
									<th class="roc">Country of Birth</th>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
							</thead>
							<tbody id="cclbody">

							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="tabs-5">

				<div class="ccltable-container">
					<div id="cclContainer" class="jtable-main-container">
						<table class="jtable">
							<thead>
								<tr>
									<th class="juris">WSID</th>
									<th class="classi">USERID</th>
									<th class="cc">EMAIL</th>
									<th class="roc">Country of Birth</th>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
							</thead>
							<tbody id="cclbody">

							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="tabs-6">

				<div class="ccltable-container">
					<div id="cclContainer" class="jtable-main-container">
						<table class="jtable">
							<thead>
								<tr>
									<th class="juris">WSID</th>
									<th class="classi">USERID</th>
									<th class="cc">EMAIL</th>
									<th class="roc">Country of Birth</th>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
							</thead>
							<tbody id="cclbody">

							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="tabs-7">

				<div class="ccltable-container">
					<div id="cclContainer" class="jtable-main-container">
						<table class="jtable">
							<thead>
								<tr>
									<th class="juris">WSID</th>
									<th class="classi">USERID</th>
									<th class="cc">EMAIL</th>
									<th class="roc">Country of Birth</th>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
							</thead>
							<tbody id="cclbody">

							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="tabs-8">

				<div class="ccltable-container">
					<div id="cclContainer" class="jtable-main-container">
						<table class="jtable">
							<thead>
								<tr>
									<th class="juris">Group Name</th>
									<th class="classi">USERS</th>
									<th class="cc">CONTACT</th>
									<th class="classi">APPLICATION</th>
									<th class="cc">SITE</th>
									<th class="roc">HOST</th>
									<th class="roc">CLIENT_INFO</th>
									<th class="roc">TOTAL MEMBERS</th>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
							</thead>
							<tbody id="cclbody">
								<tr>
									<td class="juris">US_PLANT_1000</td>
									<td class="classi">200</td>
									<td class="cc">12</td>
									<td class="roc">34</td>
									<td class="classi">0</td>
									<td class="cc">0</td>
									<td class="roc">0</td>
									<td class="roc">248</td>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
								<tr>
									<td class="juris">US_PLANT_1001</td>
									<td class="classi">20</td>
									<td class="cc">1</td>
									<td class="roc">3</td>
									<td class="classi">0</td>
									<td class="cc">0</td>
									<td class="roc">0</td>
									<td class="roc">24</td>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
								<tr>
									<td class="juris">US_PLANT_1002</td>
									<td class="classi">60</td>
									<td class="cc">12</td>
									<td class="roc">0</td>
									<td class="classi">0</td>
									<td class="cc">10</td>
									<td class="roc">0</td>
									<td class="roc">82</td>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
								<tr>
									<td class="juris">US_PLANT_1003</td>
									<td class="classi">25</td>
									<td class="cc">0</td>
									<td class="roc">0</td>
									<td class="classi">0</td>
									<td class="roc">0</td>
									<td class="cc">41</td>
									<td class="roc">71</td>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="dialog.jsp" />
	<input type="hidden" id='isCross' value="0" />
</body>
</html>
