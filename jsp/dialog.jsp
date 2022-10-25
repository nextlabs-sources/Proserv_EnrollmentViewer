<%@page import="com.nextlabs.utc.conf.EnrollmentConfComponentImpl"%>
<%@page import="com.nextlabs.utc.conf.helper.CommonConstants"%>
<%@page import="java.util.ArrayList"%>
<div id="ElementTypeCount" title="Enrollment Properties"
	style="display: none; text-align: center">
	<h2 id="dialog-table-heading"></h2>
	<div id="epropcontainer" class="jtable-main-container">
		<table class="jtable" id="enrollprops">
			<thead>
				<th class="thdialog">Property</th>
				<th class="thdialog">Value</th>


			</thead>
			<tbody id='ElementTypeCount-tBody'>

			</tbody>
		</table>
	</div>
	<br />
	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="$('#ElementTypeCount').dialog('close');">
		<span class="ui-button-text" id='dialogbutton'>Close</span>
	</button>
</div>

<div id="DataSummary" title="User Details"
	style="display: none; text-align: center">
	<h2 id="dialog-table-heading"></h2>
	<div id="udetailcontainer" class="jtable-main-container">
		<table class="jtable" id='userDetail'>
			<thead>
				<th class="thdialog">Field</th>
				<th class="thdialog">Value</th>
			</thead>
			<tbody id='DataSummary-tBody'>

			</tbody>
		</table>
	</div>
	<br/>
	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="$('#DataSummary').dialog('close');">
		<span class="ui-button-text" id='dialogbutton'>Close</span>
	</button>
</div>


<div id="popup" title="Enrollment Viewer Message"
	style="display: none; text-align: center">
	<h2 id="dialog-table-heading"></h2>
	<div id="popupcontainer" class="jtable-main-container">
<!-- 		<table class="jtable" id='userDetail'>
			<thead>
				<th class="thdialog">Field</th>
				<th class="thdialog">Value</th>
			</thead>
			<tbody id='DataSummary-tBody'>

			</tbody>
		</table> -->
	</div>
	<br/>
	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="$('#popup').dialog('close');">
		<span class="ui-button-text" id='dialogbutton'>OK</span>
	</button>
</div>
