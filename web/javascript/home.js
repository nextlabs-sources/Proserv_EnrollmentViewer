var tabTemplate = "<li><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close' onClick='closeTab(this)' role='presentation'>Remove Tab</span></li>"
var tabCounter = 3;

function addTab(tabHeading, tabContent) {
	var tabs = $("#subtabs").tabs();
	var label = tabHeading || "Tab " + tabCounter;
	id = "subtabs-" + tabCounter;
	var li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(
			/#\{label\}/g, label));
	tabContent = tabContent || "Tab " + tabCounter + " content."
	tabs.find(".ui-tabs-nav").append(li);
	tabs.append("<div id='" + id + "'>" + tabContent + "</div>");
	tabs.tabs("refresh");
	tabCounter++;
}

function closeTab(obj) {
	var tabs = $("#subtabs").tabs();
	var panelId = $(obj).closest("li").remove().attr("aria-controls");
	$("#" + panelId).remove();
	tabs.tabs("refresh");
}

function loadData(num) {
	$.post("DictServlet", {
		tableaction : "load"
	}).done(function(data) {
		if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
			window.location.reload();
		}
		/*
		 * var note='<pre style="font-size:small">'+ '<Strong>Note:</Strong>'+ '<br/>Member
		 * Count: No of Elements enrolled.<br/>'+ 'SG Count: No of Structural
		 * groups enrolled.<br/>'+ 'EMG Count: No of Enumerated groups
		 * enrolled.<br/>'+ '<br/>Click on the counts to see the summary of
		 * counts with repect to element types.<br/>'+ 'Click on the
		 * Enrollments to see the elements enrolled in that enrollment.<br/>'+ '</pre>'
		 */
		constructccltable(data, num);
		/* $('#note').html(note); */
	});
}
function validateEnrollment(enrollmentid) {

	$.post("DictServlet", {
		tableaction : "validateEnrollment",
		value : enrollmentid,
		etype : "1"
	}).done(function(data) {
		alert(data);
	});
}
function submitEnrollment(itemid) {
	$.post("DictServlet", {
		tableaction : "getEnrollmentProperty",
		value : itemid
	}).done(function(data) {

		if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
			window.location.reload();
		}
		// $('#dialog-table-heading').html("Enrollment Properties");
		else {

			$('#ElementTypeCount-tBody').html(data);
			$('#validateid').val(itemid);
			$('#ElementTypeCount').dialog({
				width : 715

			});
		}
	});
}
function deleteEnrollment() {

	var favorite = [];
	$.each($("input[name='deleteItems']:checked"), function() {
		favorite.push($(this).val());
	});

	var enrollmentIds = favorite.join(", ");

	$.post("DictServlet", {
		tableaction : "deleteEnrollments",
		enrollmentIds : enrollmentIds
	}).done(function(data) {
		if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
			window.location.reload();
		} else {
			$('#popupcontainer').html(data);
			$('#popup').dialog({
				width : 698
			});

			loadData(0);
		}

	});

}
function removeInActiveUsers() {

	var favorite = [];
	$.each($("input[name='deleteItems']:checked"), function() {
		favorite.push($(this).val());
	});

	var enrollmentIds = favorite.join(", ");

	$.post("DictServlet", {
		tableaction : "removeInActiveUsers",
		enrollmentIds : enrollmentIds
	}).done(function(data) {
		if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
			window.location.reload();
		} else {
			$('#popupcontainer').html(data);
			$('#popup').dialog({
				width : 698
			});

			loadData(0);
			loadData(0);
		}

	});

}
function selectAll() {

	var checked = document.getElementById("selectall").checked;
	$.each($("input[name='deleteItems']"), function() {
		$(this).prop('checked', checked);
	});

}
function deleteEnrollments(enrollmentIds) {

	$.post("DictServlet", {
		tableaction : "deleteEnrollments",
		enrollmentIds : enrollmentIds
	}).done(function(data) {
		if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
			window.location.reload();
		} else {
			$('#popupcontainer').html(data);
			$('#popup').dialog({
				width : 698
			});

			loadData(0);
		}
	});

}
function getFirstPageForTable(elementType, enrollmentName) {
	if (typeof $('#subtabs-body-' + enrollmentName + '-' + elementType).html() == 'undefined') {
		$.post("DictServlet", {
			tableaction : "subtabsdata",
			type : elementType,
			name : enrollmentName,
			pageno : 1,
			noOfItems : 20
		}).done(function(data) {
			if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
				window.location.reload();
			} else {
				$('#subtabs-' + enrollmentName + '-1').html(data);
			}
		});
	}
}
function populateCountDialog(map, domainname, subname) {
	var table = "";
	var obj = map.split("&#$");
	for (i = 0; i < 6; i++) {
		table = table + "<tr>";
		var str = obj[i].split("=");
		table = table + "<td>" + str[0] + "</td>";
		table = table + "<td>" + str[1] + "</td>";
		table = table + "</tr>";
	}
	$('#dialog-table-heading').html(domainname.toUpperCase() + ' ' + subname);
	$('#ElementTypeCount-tBody').html(table);
	$('#ElementTypeCount').dialog({
		width : 564,
		height : 258
	});
}

function constructccltable(data, num) {
	var perhtml = '';
	$
			.each(
					data,
					function(i, item) {
						var temp = "";
						var html = '';
						var itemid = "ui-id-" + (i + 2);
						if (typeof item.EnrollmentDomainName != 'undefined') {
							html += '<td class="juris"> <input type="checkbox" value="'
									+ item.enrollmentID
									+ '" name="deleteItems"> &nbsp <a href="#" id=\'a'
									+ item.enrollmentID
									+ '\'><u>'
									+ item.EnrollmentDomainName
									+ '</u></a></td>';
							$(document).off("click", '#a' + item.enrollmentID);

							$(document).on('click', '#a' + item.enrollmentID,
									function() {
										submitEnrollment(item.enrollmentID);
									});

						} else {
							html += '<td class="juris">null</td>';
							temp += "null&$#";
						}
						if (typeof item.EnrollmentType != 'undefined') {
							html += '<td class="classi" >'
									+ item.EnrollmentType + '</td>';
							temp += item.EnrollmentType + "&$#";
						} else {
							html += '<td class="classi">null</td>';
							temp += "null&$#";
						}
						if (typeof item.Active != 'undefined') {
							if (item.Active == 1) {
								html += '<td class="cc">ACTIVE</td>';
								temp += "ACTIVE&$#";
							} else {
								html += '<td class="cc">INACTIVE</td>';
								temp += "INACTIVE&$#";
							}

						} else {
							html += '<td class="cc">null</td>';
							temp += "null&$#";
						}
						if (typeof item.lastSyncTime != 'undefined') {
							html += '<td class="roc">' + item.lastSyncTime
									+ '</td>';
							temp += item.lastSyncTime + "&$#";
						} else {
							html += '<td class="roc">null</td>';
							temp += "null&$#";
						}
						if (typeof item.EMmap['USER'] != 'undefined') {

							html += '<td class="count" ><a href="#" id=\'au'
									+ item.enrollmentID + '\'><u id="user'
									+ item.enrollmentID + '">'
									+ item.EMmap['USER'] + '</u></a></td>';
							$(document).off("click", '#au' + item.enrollmentID);
							$(document).on('click', '#au' + item.enrollmentID,
									function() {
										gotoUsersTab(item.enrollmentID);
									});
							temp += item.EMmap['USER'] + "&$#";
						} else {
							html += '<td class="count">null</td>';

						}
						if (typeof item.inactiveUser != 'undefined') {

							html += '<td class="count" >' + item.inactiveUser
									+ '</td>';
							temp += item.inactiveUser + "&$#";
						} else {
							html += '<td class="count">null</td>';

						}

						if (typeof item.EMmap['HOST'] != 'undefined') {
							/*
							 * html += '<td class="count" ><u><a href="#"
							 * onclick=\'gotoHostTab("' + item.enrollmentID +
							 * '");\'>' + item.EMmap['HOST'] + '</a></u></td>';
							 */
							html += '<td class="count" >' + item.EMmap['HOST']
									+ '</td>';

						} else {
							html += '<td class="count">null</td>';
							temp += "null&$#";
						}
						if (typeof item.EMGCount != 'undefined') {

							html += '<td class="count" ><a href="#" id=\'ag'
									+ item.enrollmentID + '\'><u id="userg'
									+ item.enrollmentID + '">' + item.EMGCount
									+ '</u></a></td>';
							$(document).off("click", '#ag' + item.enrollmentID);
							$(document).on('click', '#ag' + item.enrollmentID,
									function() {
										gotoGroupsTab(item.enrollmentID);
									});
							temp += item.EMGCount + "&$#";
						} else {
							html += '<td class="count">null</td>';
							temp += "null&$#";
						}
						/*
						 * if (typeof item.EMmap['SITE'] != 'undefined') {
						 * 
						 * html += '<td class="count" ><u><a href="#"
						 * onclick=\'gotoSiteTab("' + item.enrollmentID +
						 * '");\'>' + item.EMmap['SITE'] + '</a></u></td>';
						 * 
						 * html += '<td class="count" >' + item.EMmap['SITE'] + '</td>'; }
						 * else { html += '<td class="count">null</td>';
						 * temp += "null&$#"; }
						 * 
						 * if (typeof item.EMmap['APPLICATION'] != 'undefined') { /*
						 * html += '<td class="count" ><u><a href="#"
						 * onclick=\'gotoAppsTab("' + item.enrollmentID +
						 * '");\'>' + item.EMmap['APPLICATION'] + '</a></u></td>';
						 * 
						 * html += '<td class="count" >' +
						 * item.EMmap['APPLICATION'] + '</td>'; } else { html += '<td class="count">null</td>';
						 * temp += "null&$#"; }
						 */
						if (typeof item.enrollmentID != 'undefined') {
							html += '<td class="count" ><img class="icon"  alt="Delete Enrollment" title="Deletes the enrollment"  src="images/cross.png" id=\'ai'
									+ item.enrollmentID + '\'></td>';
							$(document).off("click", '#ai' + item.enrollmentID);
							$(document).on('click', '#ai' + item.enrollmentID,
									function() {
										deleteEnrollments(item.enrollmentID);
									});

						} else {
							html += '<td class="count">null</td>';
							temp += "null&$#";
						}

						/*
						 * html += '<td class="count"><input type="checkbox"
						 * id="cb' + item.enrollmentID + '"
						 * onchange=\'showInActiveUsers("' + item.enrollmentID +
						 * '");\'/></td>';
						 */
						/*
						 * html += '<td class="count" ><u><img class="icon"
						 * alt="Validate the enrollments"
						 * src="images/validate.png"
						 * onclick=\'validateEnrollment("' + item.enrollmentID +
						 * '");\'>' + '</u></td>';
						 */

						// html+='<td class="editdelete"><img class="icon"
						// src="images/edit.jpg"
						// alt="Edit Record"
						// onclick=editData("'+temp+'")>&nbsp;&nbsp;&nbsp;<img
						// class="icon" src="images/delete.jpg" alt="Delete
						// Record"
						// onclick=deleteData("'+temp+'")> </td>';
						perhtml += '<tr>' + html;
						perhtml += '</tr>';
					});
	if (perhtml == '') {
		perhtml = '<tr><td colspan="9">Enrollment list is empty.</td></tr>';
	}
	$('#cclbody').empty();
	$('#cclbody').append(perhtml);
}
function showInActiveUsers() {
	var checked = document.getElementById("cbShowInActive").checked;
	$.post("DictServlet", {
		tableaction : "showInactiveActiveUsers",
		checked : checked
	}).done(
			function(data) {
				$.each(data, function(i, item) {
					var usercount = document.getElementById("user"
							+ item.enrollmentID)
					var userGCount = document.getElementById("userg"
							+ item.enrollmentID)
					$(usercount).html(item.EMCount);
					$(userGCount).html(item.EMGCount);
				});
			});
}

function jsonToString(map) {

	var result = "";
	for ( var key in map) {
		var attrName = key;
		var attrValue = map[key];
		result = result + attrName + "=" + attrValue + "&#$";
	}

	return result
}
function getCount(map, value) {

	var result = "";
	for ( var key in map) {
		if (key.toUpperCase() == value.toUpperCase())
			result = map[key];

	}

	return result
}

function cross(enrollmentname, tabname) {
	submitRequest('ui-id-1');
	$("#tabs").tabs("select", "tabs-1")
	// $('#'+enrollmentname).css("display","none");
	// $('#'+tabname).css("display","none");
	// $('#tabs').tabs('remove', 1); // close first tab
	$('#isCross').val(1);

}

function contact(name, tabname) {
	if ($('#isCross').val() == 1) {
		$('#isCross').val(0);

	} else {
		if (typeof $('#subtabs-' + name).html() == 'undefined') {
			var tabstr = getSubTabs(name);
			$('#' + tabname).html(tabstr);
			$("#subtabs-" + name).tabs();
		}
	}
}
function createtab(name, tabname) {

	var ultext = '<li id=\'' + name
			+ '\' style="display:none"><a style="cursor:pointer;" href="#'
			+ tabname + '" onclick=\'contact("' + name + '","' + tabname
			+ '")\'>' + name
			+ '&nbsp;<img src="images/cross.gif" onclick=\'cross("' + name
			+ '","' + tabname + '")\'></a></li>';
	var divtext = '<div id="' + tabname + '"></div>';
	$('#tabs ul').html($('#tabs ul').html() + ultext);
	$('#tabs').html($('#tabs').html() + divtext);
	$("#tabs").tabs();
	$("#tabs").tabs("refresh");
	// $('#'+tabname).html(divtext);
}

function submitRequest(buttonId) {

	if (document.getElementById(buttonId) == null
			|| document.getElementById(buttonId) == undefined) {
		return;
	}
	if (document.getElementById(buttonId).dispatchEvent) {
		var e = document.createEvent("MouseEvents");
		e.initEvent("click", true, true);
		document.getElementById(buttonId).dispatchEvent(e);
	} else {
		document.getElementById(buttonId).click();
	}
}

function getHeadingOfTable(elementType, enrollmentName) {
	if (typeof $('#subtabs-body-' + enrollmentName + '-' + elementType).html() == 'undefined') {

		$.post("DictServlet", {
			tableaction : "heading",
			type : elementType,
			name : enrollmentName
		}).done(function(data) {
			if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
				window.location.reload();
			} else {
				$('#subtabs-' + enrollmentName + '-1').html(data);
			}
		});

	}

}
function getEnrollment(enrolldropdownid, type, checkboxproperty, etype) {
	var htmlval = $('#' + enrolldropdownid).html();
	htmlval = (htmlval.trim) ? htmlval.trim() : htmlval.replace(/^\s+/, '')
	if (htmlval == '') {
		$.post("DictServlet", {
			tableaction : "getEnrollment"
		}).done(function(data) {
			if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
				window.location.reload();
			} else {
				$('#' + enrolldropdownid).html(data);
			}
		});
		populateProperties(etype, '0', type + 'tbody', checkboxproperty, type);
	}
}
function populategfilter(val) {
	$.post("DictServlet", {
		tableaction : "getgfilter",
		id : val
	}).done(function(data) {
		if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
			window.location.reload();
		} else {
			$('#gfilter').html(data);

		}
	});
}
function getEnrollmentforgroups(enrolldropdownid) {
	var htmlval = $('#' + enrolldropdownid).html();
	htmlval = (htmlval.trim) ? htmlval.trim() : htmlval.replace(/^\s+/, '')
	if (htmlval == '') {
		$.post("DictServlet", {
			tableaction : "getEnrollment"
		}).done(function(data) {
			if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
				window.location.reload();
			} else {
				$('#' + enrolldropdownid).html(data);
			}
		});
		populategfilter(htmlval);
	}
}
function resetonchange(textid) {
	var obj = document.getElementById(textid);
	obj.value = '';
}
function populateProperties(etype, eid, tbodyid, checkboxproperty, type) {
	$("#" + type + "propbut").css("display", "block");
	$
			.post("DictServlet", {
				tableaction : "getProperty",
				id : eid,
				type : etype
			})
			.done(
					function(data) {
						if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
							window.location.reload();
						} else {
							var arr = data.split('##$$');
							options = "<option value=\"1e\" selected='selected'>Select a Filter</option>"
									+ arr[0];
							var resetString = "<div id='"
									+ type
									+ "td1' style='display:none' class='txtproperty'><img src='images/minus.jpeg' width='18px' height='18px' style='cursor:pointer' onclick=\"removerow($(this).parent().attr('id'))\">"
									+ "<select class='selectclass' id='"
									+ type
									+ "Select1' onChange=\"resetonchange('utproperty1')\">"
									+ options
									+ "</select> &nbsp;&nbsp; <input type='text' id='"
									+ type
									+ "tproperty1' class='tb7' value=''>"
									+ "</div>"
									+ "<div id='"
									+ type
									+ "td2' class='txtproperty'><img src='images/minus.jpeg' width='18px' height='18px' style='cursor:pointer' onclick=\"removerow($(this).parent().attr('id'))\">"
									+ "&nbsp;&nbsp;<select class='selectclass' id='"
									+ type
									+ "Select2' onChange=\"resetonchange('utproperty2')\">"
									+ options
									+ "</select> &nbsp;&nbsp; <input type='text' id='"
									+ type
									+ "tproperty2' class='tb7' value=''>"
									+ "</div>"
									+ "<div id='"
									+ type
									+ "td3' class='txtproperty'>"
									+ "<img src='images/plus.jpg' width='18px' height='18px' style='cursor:pointer' id='uplus' onclick=\"addNewSelect($(this).parent().attr('id'),'"
									+ type + "tbody','" + type
									+ "Select','u')\">" + "</div>"
							$('#' + tbodyid).html(resetString);
							$('#' + type + 'dropdown').val(options);
							$('#' + checkboxproperty).html(arr[1]);
						}
					});
	document.getElementById(type + "searchButton").disabled = false;

}

function addPropertyToFilter(propid, textboxid, filterid) {
	var fieldName = $('#' + propid + ' option:selected').text();
	var fieldMapping = $('#' + propid + ' option:selected').val();
	var searchText = $('#' + textboxid).val();
	var v = fieldName + "=" + fieldMapping + "=" + searchText;
	var obj = new Option(fieldName + "=" + searchText, v);
	$(obj).html(fieldName + "=" + searchText);
	if (!(fieldName.trim == "" || searchText == "" || fieldMapping == "empty"))
		$('#' + filterid).append(obj);

}
function getUserData(id) {

	$.post("DictServlet", {
		tableaction : "getUserData",
		value : id
	}).done(function(data) {
		if (data.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
			window.location.reload();
		} else {
			$('#DataSummary-tBody').html(data);
			$('#DataSummary').dialog({
				width : 698
			});
		}
	});
}

function checkCount(id) {
	if ($("input[name=property]:checked").length > 5) {
		$('#' + id).attr("checked", false);
		alert("Maximum of 5 attributes and minimum of 0 properties can be selected to view in the main table");
	}
	return;
}
function listGroups(enrollmentid, tableid) {
	var enrollmentId = $('#' + enrollmentid + ' option:selected').val();
	var filterval = $('#gfilter option:selected').val();
	filterval = filterval + "#$#" + $('#gfilter option:selected').text();
	filterval = filterval + '#$#' + $('#gtextbox').val();
	var type = enrollmentid.substr(0, 1);
	$('#' + type + 'title').css("margin-left", "12px");
	$.post("DictServlet", {
		tableaction : "listGroups",
		id : enrollmentId,
		val : filterval
	}).done(
			function(data) {
				var result = data.tableData;
				if (result.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
					window.location.reload();
				} else {

					if (result.indexOf("No Groups enrolled") != -1) {
						$('#' + type + 'title').css("display", "none");
					} else {
						$('#' + type + 'title').css("display", "block");
					}
					$('#' + tableid).html(result);

					$.each(data.groups, function(i, item) {
						$(document).off("click", '#' + item.aid);
						$(document).on(
								'click',
								'#' + item.aid,
								function() {
									listMembers(item.elementId,
											item.elementType, item.groupName,
											item.enrollmentName,
											item.enrollmentId);
								});

					});
				}

			});
}
function searchDataforIntervalChange(enrollmentid, tbodyid, etype, tableid) {
	if ($('#' + tableid).html().toLowerCase().indexOf("thead") != -1) {
		searchData(enrollmentid, tbodyid, etype, tableid)
	}
}
function searchData(enrollmentid, tbodyid, etype, tableid) {
	var enrollmentId = $('#' + enrollmentid + ' option:selected').val();
	var filterVal = "";
	var type;
	$('#' + tbodyid)
			.children()
			.each(
					function(i) {
						var id = this.id;
						if (typeof id != 'undefined') {
							type = id.substr(0, 1);
							var val = parseInt(id.substr(3))
							if (val != 1) {

								var selectname = type + 'Select' + val;
								var textboxname = type + 'tproperty' + val;
								if (!((typeof $(
										'#' + selectname + ' option:selected')
										.val() == 'undefined')
										|| (typeof $(
												'#' + selectname
														+ ' option:selected')
												.text() == 'undefined')
										|| (typeof $('#' + textboxname).val() == 'undefined') || ($(
										'#' + textboxname).val() == ""))) {
									var fieldName = $(
											'#' + selectname
													+ ' option:selected')
											.text();
									var fieldMapping = $(
											'#' + selectname
													+ ' option:selected').val();
									var searchText = $('#' + textboxname).val();
									if (!(fieldName.trim == ""
											|| searchText == "" || fieldMapping == "empty")) {
										var filterValue = fieldName + "="
												+ fieldMapping + "="
												+ searchText;
										filterVal += filterValue + "@#$";
									}
								}
							}
						}
					});

	/*
	 * $('#'+filterid+ ' option').each(function(i) { filterVal += this.value +
	 * "@#$"; });
	 */
	var projection = new Array();
	$('input:checkbox[name="' + etype + 'property"]:checked').each(function() {
		projection.push($(this).val());
	});
	$.post("DictServlet", {
		tableaction : "getFilteredResult",
		id : enrollmentId,
		type : etype,
		filter : filterVal,
		interval : $('#' + type + 'maximuminterval option:selected').val(),
		'projections[]' : projection
	}).done(
			function(data) {
				var filteredUsers = data.tableData;
				var filteredUsersCount = data.total;
				var start = data.start;
				var interval = data.interval;
				var totalPages = data.totalPages;
				$('#totalUsers').html(filteredUsersCount);
				if (filteredUsers.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
					window.location.reload();
				} else {
					if (filteredUsers.indexOf("No Results Found") == -1) {
						$('#' + type + 'title').css("display", "block");
						$('#' + type + 'span').css("display", "block");
						$('#' + type + 'title').css("margin-left", "12px");
					} else {
						$('#' + type + 'title').css("display", "none");
						$('#' + type + 'span').css("display", "none");
					}

					$('#' + tableid).html(filteredUsers);

					$.each(data.elementIds, function(i, item) {
						$(document).off("click", '#tr' + item);
						$(document).on('click', '#tr' + item, function() {
							getUserData(item);
						});

					});
					if (filteredUsers.indexOf('nextButton') > -1) {
						$(document).off("click", '#nextButton');
						$(document).on(
								'click',
								'#nextButton',
								function() {
									paginateData(start + interval, interval, $(
											this).parent().parent().parent()
											.attr('id'));
								});
					}
					if (filteredUsers.indexOf('lastbutton') > -1) {
						$(document).off("click", '#lastbutton');
						$(document).on(
								'click',
								'#lastbutton',
								function() {
									paginateData((totalPages - 1) * interval,
											interval, $(this).parent().parent()
													.parent().attr('id'));
								});
					}
					if (filteredUsers.indexOf('prevButton') > -1) {
						$(document).off("click", '#prevButton');
						$(document).on(
								'click',
								'#prevButton',
								function() {
									paginateData(start - interval, interval, $(
											this).parent().parent().parent()
											.attr('id'));
								});
					}
					if (filteredUsers.indexOf('firstbutton') > -1) {
						$(document).off("click", '#firstbutton');
						$(document).on(
								'click',
								'#firstbutton',
								function() {
									paginateData(0, interval, $(this).parent()
											.parent().parent().attr('id'));
								});
					}
				}
			});
}
function clearFilter(id) {
	$("#" + id).html("");
}

function addNewSelect(tdid, tbodyid, selectname, type) {
	var obj = parseInt(tdid.substr(3));
	var newselect = '<img src="images/minus.jpeg" width="18px" height="18px" style="cursor:pointer" onclick="removerow($(this).parent().attr(\'id\'))">'
			+ '&nbsp;&nbsp;<select class="selectclass" id="'
			+ selectname
			+ obj
			+ '" onChange=\"resetonchange(\''
			+ type
			+ 'tproperty'
			+ obj
			+ '\')\">'
			+ $('#' + type + 'dropdown').val()
			+ '</select> &nbsp;&nbsp;&nbsp;<input type="text" id="'
			+ type
			+ 'tproperty' + obj + '" class="tb7" value=""/>';
	$('#' + tdid).html(newselect);
	var add = "<div class=\"txtproperty\" id='"
			+ type
			+ "td"
			+ (obj + 1)
			+ "'><img src='images/plus.jpg' width='18px' height='18px' style='cursor:pointer' id='uplus' onclick='addNewSelect($(this).parent().attr(\"id\"),\""
			+ type + "tbody\",\"" + type + "Select\",\"" + type + "\")'></div>";
	$('#' + tbodyid).append(add);
}
function removerow(trid) {
	$('#' + trid).remove();
}
function paginateData(start, interval, tableid) {

	$.post("DictServlet", {
		tableaction : "paginate",
		startVal : start,
		intervalVal : interval
	}).done(
			function(data) {
				var filteredUsers = data.tempString;
				var totalPages = data.totalPages;
				var start = data.start;
				var interval = data.interval;
				if (filteredUsers.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
					window.location.reload();
				} else {

					$('#' + tableid).html(filteredUsers);
					$.each(data.elementIds, function(i, item) {
						$(document).off("click", '#tr' + item);
						$(document).on('click', '#tr' + item, function() {
							getUserData(item);
						});

					});
					if (filteredUsers.indexOf('nextButton') > -1) {
						$(document).off("click", '#nextButton');
						$(document).on(
								'click',
								'#nextButton',
								function() {
									paginateData(start + interval, interval, $(
											this).parent().parent().parent()
											.attr('id'));
								});
					}
					if (filteredUsers.indexOf('lastbutton') > -1) {
						$(document).off("click", '#lastbutton');
						$(document).on(
								'click',
								'#lastbutton',
								function() {
									paginateData((totalPages - 1) * interval,
											interval, $(this).parent().parent()
													.parent().attr('id'));
								});
					}
					if (filteredUsers.indexOf('prevButton') > -1) {
						$(document).off("click", '#prevButton');

						$(document).on(
								'click',
								'#prevButton',
								function() {
									paginateData(start - interval, interval, $(
											this).parent().parent().parent()
											.attr('id'));
								});
					}
					if (filteredUsers.indexOf('firstbutton') > -1) {
						$(document).off("click", '#firstbutton');
						$(document).on(
								'click',
								'#firstbutton',
								function() {
									paginateData(0, interval, $(this).parent()
											.parent().parent().attr('id'));
								});
					}
				}
			});
}
function listMembers(groupid, etype, groupname, enrollmentName, eId) {

	switch (etype) {
	case 'u':
		/*
		 * submitRequest('ui-id-3');
		 * $('#userenrollmentId').val($('#genrollmentId').val()); setTimeout(
		 * function() { listmembersfromdict(groupid, "1", "uresult_table",
		 * groupname, etype); }, 100);
		 */
		// var va = $("#" + groupname + "maximuminterval").val();
		if (typeof $('#' + groupname + 'maximuminterval').val() == 'undefined') {
			var tabContent = "<div id='"
					+ groupname
					+ "container'>"
					+ "<div class='sidehead'>	<h2 class='summary-table'>Group Members</h2></div> <br/>"
					+ "<div class='sidehead'>Enrollment Name :&nbsp;"
					+ enrollmentName
					+ "</div><br/>"
					+ "<div class='sidehead'>Group Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;"
					+ groupname
					+ "</div><div id='"
					+ groupname
					+ "resultContainer' class='jtable-main-container' align='center' style='width:900px;margin-left:12px;margin-top: 1px;'>"
					+ "<span style='margin-left:705px;'>Records per Page &nbsp;&nbsp;&nbsp;<select id='"
					+ groupname
					+ "maximuminterval'><option value='10'>10</option><option value='20'>20</option><option value='50'>50</option><option value='100'>100</option><option value='250'>250</option><option value='500'>500</option></select></span>"
					+ "<table class='jtable' id='" + groupname
					+ "result_table' style='table-layout: fixed;'>";

			$
					.post("DictServlet", {
						tableaction : "getGroupMembers",
						group : groupid,
						et : 1,
						interval : 10,
						enrollmentId : eId
					})
					.done(
							function(data) {

								var filteredUsers = data.tableData;
								var filteredUsersCount = data.total;
								var start = data.start;
								var interval = data.interval;
								var totalPages = data.totalPages;
								if (filteredUsers
										.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
									window.location.reload();
								} else {
									tabContent = tabContent + " "
											+ filteredUsers
											+ "</table></div></div></div>";
									addTab(groupname + " :: members",
											tabContent);
									var groupnamec = groupname + 'container';
									var node = document
											.getElementById(groupnamec);
									var parent = node.parentNode;
									var att = parent
											.getAttribute('aria-labelledby');
									submitRequest(att);
									var cell = document
											.getElementsByClassName("ui-icon ui-icon-close");
									for (var i = 0; i < cell.length; i++) {
										cell[i].addEventListener("click",
												function() {
													closeTab(this);
												});
									}
									var selectid = groupname
											+ 'maximuminterval';
									var maxinterval = document
											.getElementById(selectid);
									maxinterval
											.removeEventListener(
													"change",
													function() {
														listmembersfromdict(
																groupid,
																'1',
																groupname
																		+ 'result_table',
																groupname);
													});
									maxinterval
											.addEventListener(
													"change",
													function() {
														listmembersfromdict(
																groupid,
																'1',
																groupname
																		+ 'result_table',
																groupname);
													});
									/*
									 * document.addEventListener('change',
									 * function(event) {
									 * 
									 * if (event.target.id == selectid) {
									 * 
									 *  } });
									 */
									$.each(data.elementIds, function(i, item) {
										$(document).off("click", '#tr' + item);
										$(document).on('click', '#tr' + item,
												function() {
													getUserData(item);
												});

									});
									if (filteredUsers.indexOf('nextButton') > -1) {
										$(document).off("click", '#nextButton');
										$(document)
												.on(
														'click',
														'#nextButton',
														function() {
															paginateData(
																	start
																			+ interval,
																	interval,
																	$(this)
																			.parent()
																			.parent()
																			.parent()
																			.attr(
																					'id'));
														});
									}
									if (filteredUsers.indexOf('lastbutton') > -1) {
										$(document).off("click", '#lastbutton');
										$(document)
												.on(
														'click',
														'#lastbutton',
														function() {
															paginateData(
																	(totalPages - 1)
																			* interval,
																	interval,
																	$(this)
																			.parent()
																			.parent()
																			.parent()
																			.attr(
																					'id'));
														});
									}
									if (filteredUsers.indexOf('prevButton') > -1) {
										$(document).off("click", '#prevButton');
										$(document)
												.on(
														'click',
														'#prevButton',
														function() {
															paginateData(
																	start
																			- interval,
																	interval,
																	$(this)
																			.parent()
																			.parent()
																			.parent()
																			.attr(
																					'id'));
														});
									}
									if (filteredUsers.indexOf('firstbutton') > -1) {
										$(document)
												.off("click", '#firstbutton');
										$(document)
												.on(
														'click',
														'#firstbutton',
														function() {
															paginateData(
																	0,
																	interval,
																	$(this)
																			.parent()
																			.parent()
																			.parent()
																			.attr(
																					'id'));
														});
									}

								}
							});
		} else {
			var id = $('#' + groupname + 'container').parent().attr(
					'aria-labelledby');
			submitRequest(id);
		}
		break;
	case 'h':
		submitRequest('ui-id-5');
		$('#henrollmentId').val($('#genrollmentId').val());

		setTimeout(
				function() {
					listmembersfromdict(groupid, "3", "hresult_table",
							groupname, etype);
				}, 100);
		break;
	case 's':
		submitRequest('ui-id-6');
		$('#senrollmentId').val($('#genrollmentId').val());
		setTimeout(
				function() {
					listmembersfromdict(groupid, "4", "sresult_table",
							groupname, etype);
				}, 100);
		break;
	case 'a':
		submitRequest('ui-id-8');
		$('#aenrollmentId').val($('#genrollmentId').val());
		setTimeout(
				function() {
					listmembersfromdict(groupid, "6", "aresult_table",
							groupname, etype);
				}, 100);
		break;
	}
}
function listmembersfromdict(groupid, elementtype, tableid, groupname) {
	var selID = document.getElementById(groupname + "maximuminterval");
	var text = selID.options[selID.selectedIndex].text;
	$
			.post("DictServlet", {
				tableaction : "getGroupMembers",
				group : groupid,
				et : elementtype,
				interval : text,
				enrollmentId : $('#genrollmentId').val()
			})
			.done(
					function(data) {
						var filteredUsers = data.tableData;
						var filteredUsersCount = data.total;
						var start = data.start;
						var interval = data.interval;
						var totalPages = data.totalPages;
						if (data.tableData.indexOf("<!DOCTYPE HTML PUBLIC") != -1) {
							window.location.reload();
						} else {
							document.getElementById(tableid).innerHTML = data.tableData;
							$.each(data.elementIds, function(i, item) {
								$(document).off("click", '#tr' + item);
								$(document).on('click', '#tr' + item,
										function() {
											getUserData(item);
										});

							});
							if (filteredUsers.indexOf('nextButton') > -1) {
								$(document).off("click", '#nextButton');
								$(document).on(
										'click',
										'#nextButton',
										function() {
											paginateData(start + interval,
													interval, $(this).parent()
															.parent().parent()
															.attr('id'));
										});
							}
							if (filteredUsers.indexOf('lastbutton') > -1) {
								$(document).off("click", '#lastbutton');
								$(document).on(
										'click',
										'#lastbutton',
										function() {
											paginateData((totalPages - 1)
													* interval, interval, $(
													this).parent().parent()
													.parent().attr('id'));
										});
							}
							if (filteredUsers.indexOf('prevButton') > -1) {
								$(document).off("click", '#prevButton');
								$(document).on(
										'click',
										'#prevButton',
										function() {
											paginateData(start - interval,
													interval, $(this).parent()
															.parent().parent()
															.attr('id'));
										});
							}
							if (filteredUsers.indexOf('firstbutton') > -1) {
								$(document).off("click", '#firstbutton');
								$(document).on(
										'click',
										'#firstbutton',
										function() {
											paginateData(0, interval, $(this)
													.parent().parent().parent()
													.attr('id'));
										});
							}

						}
					});

}

function gotoUsersTab(enrollmentid) {
	submitRequest('ui-id-2');
	submitRequest('ui-id-3');
	var t = setTimeout(function() {
		/*
		 * $('#userenrollmentId [value="' + enrollmentid +
		 * '"]').attr('selected', true);
		 */
		$('#userenrollmentId').val(enrollmentid);
		populateProperties('1', $('#userenrollmentId').val(), 'utbody',
				'propertycheckbox', 'u');
	}, 300);
	t = setTimeout(function() {
		searchData('userenrollmentId', 'utbody', '1', 'uresult_table');
	}, 700);
}
function gotoAppsTab(enrollmentid) {
	submitRequest('ui-id-2');
	submitRequest('ui-id-8');
	var t = setTimeout(function() {
		$('#aenrollmentId [value="' + enrollmentid + '"]').attr('selected',
				true);
		populateProperties('6', $('#aenrollmentId').val(), 'atbody',
				'apropertycheckbox', 'a');
	}, 300);
	t = setTimeout(function() {
		searchData('aenrollmentId', 'atbody', '6', 'aresult_table');
	}, 700);
}
function gotoSiteTab(enrollmentid) {
	submitRequest('ui-id-2');
	submitRequest('ui-id-6');
	var t = setTimeout(function() {
		$('#senrollmentId [value="' + enrollmentid + '"]').attr('selected',
				true);
		populateProperties('4', $('#senrollmentId').val(), 'stbody',
				'spropertycheckbox', 's');
	}, 300);
	t = setTimeout(function() {
		searchData('senrollmentId', 'stbody', '4', 'sresult_table');
	}, 700);
}
function gotoHostTab(enrollmentid) {
	submitRequest('ui-id-2');
	submitRequest('ui-id-5');
	var t = setTimeout(function() {
		$('#henrollmentId [value="' + enrollmentid + '"]').attr('selected',
				true);
		populateProperties('3', $('#henrollmentId').val(), 'htbody',
				'hpropertycheckbox', 'h');
	}, 300);
	t = setTimeout(function() {
		searchData('henrollmentId', 'htbody', '3', 'hresult_table');
	}, 700);
}
function gotoGroupsTab(enrollmentid) {
	submitRequest('ui-id-2');
	submitRequest('ui-id-4');
	$('#gtextbox').val("");
	var t = setTimeout(function() {
		/*
		 * $('#genrollmentId [value="' + enrollmentid + '"]').attr('selected',
		 * true);
		 */
		$('#genrollmentId').val(enrollmentid);
		populategfilter(enrollmentid);

	}, 300)
	t = setTimeout(function() {
		listGroups('genrollmentId', 'gresult_table')
	}, 700);
}