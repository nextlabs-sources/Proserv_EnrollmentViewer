package com.nextlabs.utc.conf.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.bluejungle.dictionary.Dictionary;
import com.bluejungle.dictionary.DictionaryException;
import com.bluejungle.dictionary.IDictionaryIterator;
import com.bluejungle.dictionary.IEnrollment;
import com.bluejungle.dictionary.IMGroup;
import com.bluejungle.framework.comp.IComponentManager;
import com.bluejungle.framework.crypt.IDecryptor;
import com.bluejungle.framework.crypt.ReversibleEncryptor;
import com.nextlabs.enrollment.webapp.hibernate.EnrollmentSession;
import com.nextlabs.utc.conf.EnrollmentConfComponentImpl;
import com.nextlabs.utc.conf.bean.EnrollmentInfo;
import com.nextlabs.utc.conf.bean.GroupBean;
import com.nextlabs.utc.conf.bean.LDAPAccessBean;
import com.nextlabs.utc.conf.bean.TotalUsers;

public class DictHelper {
	private static HashMap<String, String> elementTypes = null;
	private static HashMap<String, EnrollmentInfo> enrollmentIDMapping = null;
	private static HashMap<String, HashMap<String, TreeMap<String, String>>> enrollmentFieldMapping = null;
	private static String builder = "<option value=\"{0}\">{1}</option>";
	private static String selectedBuilder = "<option value=\"{0}\" selected='selected'>{1}</option>";
	private static String checkboxBuilder = "	<input type=\"checkbox\" value=\"{1}\" name=\"{2}property\" id=\"{0}\" onChange=\"checkCount(this.id)\"/><label for=\"{0}\">{1}</label>";
	private static String checkedboxBuilder = "	<input type=\"checkbox\" checked=\"true\" value=\"{1}\" name=\"{2}property\" id=\"{0}\" onChange=\"checkCount(this.id)\"/><label for=\"{0}\">{1}</label>";
	private static HashMap<String, ArrayList<String>> elementTypeFieldMapping = getElementTypeFieldMapping();
	private static EnrollmentSession es;
	private static HashSet<String> propertiesSet = getPropertiesSet();
	private static ArrayList<String> dataKey;
	private static HashMap<String, HashMap<String, String>> dataMapping;
	private static HashMap<String, ArrayList<String>> eTypedefCheckMapping = geteTypedefCheckMapping();
	private static HashMap<String, LDAPAccessBean> eidLdapAccessBeanMapping;
	private static ArrayList<String> projections;
	private static HashMap<String, String> elementTypeLDAPKeyMapping = getElementTypeLDAPKeyMapping();
	private static IDecryptor decryptor = new ReversibleEncryptor();

	private static HashMap<String, String> getElementTypes(EnrollmentSession es) {
		HashMap<String, String> elementTypes = new HashMap<String, String>();
		String QUERY = "SELECT ID,NAME FROM DICT_ELEMENT_TYPES";
		ResultSet elementTypesSet = es.getResultSet(QUERY);
		if (null != elementTypesSet && getSize(elementTypesSet) > 0) {
			try {
				while (elementTypesSet.next()) {
					elementTypes.put(elementTypesSet.getString("ID"),
							elementTypesSet.getString("NAME"));
				}
			} catch (SQLException e) {
				EnrollmentConfComponentImpl.log
						.error("----DictHelper.getElementTypes# SQLException-----\n");
				EnrollmentConfComponentImpl.log.error(e);
			} finally {
				try {
					elementTypesSet.close();
				} catch (SQLException e) {
					EnrollmentConfComponentImpl.log
							.error("----DictHelper.getElementTypes# SQLException-----\n");
					EnrollmentConfComponentImpl.log.error(e);
				}
			}
		} else {
			elementTypes.put("1", "USER");
			elementTypes.put("2", "CONTACT");
			elementTypes.put("3", "HOST");
			elementTypes.put("4", "APPLICATION");
			elementTypes.put("5", "SITE");
			elementTypes.put("6", "CLIENT_INFO");
		}

		EnrollmentConfComponentImpl.log
				.info("----DictHelper.getElementTypes# Result:elementTypesSet-----\n"
						+ elementTypes.toString());
		return elementTypes;
	}

	private static int getSize(ResultSet rs) {
		int size = 0;
		try {
			if (rs.last()) {
				size = rs.getRow();
				rs.beforeFirst();
			}
		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log
					.error("----DictHelper.getSize()# SQLException-----\n");
			EnrollmentConfComponentImpl.log.error(e);

		}

		return size;
	}

	private static HashMap<String, String> getElementTypeLDAPKeyMapping() {
		HashMap<String, String> elementTypes = new HashMap<String, String>();
		elementTypes.put("1", "User Principal Name");
		elementTypes.put("2", "Contact Principal Name");
		elementTypes.put("3", "DNS Host Name");
		elementTypes.put("4", "Display Name");
		elementTypes.put("5", "IP Address");
		elementTypes.put("6", "Identifier");
		return elementTypes;
	}

	private static HashMap<String, ArrayList<String>> geteTypedefCheckMapping() {
		HashMap<String, ArrayList<String>> elementTypes = new HashMap<String, ArrayList<String>>();
		ArrayList<String> coloumns = new ArrayList<String>();
		coloumns.add("Full Name");
		coloumns.add("First Name");
		coloumns.add("Last Name");

		elementTypes.put("1", coloumns);

		coloumns = new ArrayList<String>();
		coloumns.add("Full Name");
		coloumns.add("Contact Principal Name");
		elementTypes.put("2", coloumns);

		coloumns = new ArrayList<String>();
		coloumns.add("DNS Host Name");
		coloumns.add("UNIX Computer ID");
		elementTypes.put("3", coloumns);

		coloumns = new ArrayList<String>();
		coloumns.add("Display Name");
		coloumns.add("Unique Name");
		elementTypes.put("4", coloumns);

		coloumns = new ArrayList<String>();
		coloumns.add("Name");
		coloumns.add("IP Address");

		elementTypes.put("5", coloumns);

		coloumns = new ArrayList<String>();
		coloumns.add("LongName");
		coloumns.add("Identifier");
		elementTypes.put("6", coloumns);

		return elementTypes;
	}

	private static HashSet<String> getPropertiesSet() {
		HashSet<String> set = new HashSet<String>();
		set.add("port");
		set.add("enroll.users");
		set.add("ispagingenabled");
		set.add("entry.attributefor.staticid");
		set.add("enableaddirchgreplication");
		set.add("enroll.computers");
		set.add("other.requirements");
		set.add("scheduledsyncinterv");
		set.add("login");
		set.add("enroll.applications");
		set.add("structure.requirements");
		set.add("roots");
		set.add("enroll.contacts");
		set.add("server");
		set.add("filter");
		set.add("enroll.groups");
		return set;
	}

	private static HashMap<String, ArrayList<String>> getElementTypeFieldMapping() {
		HashMap<String, ArrayList<String>> elementTypes = new HashMap<String, ArrayList<String>>();
		ArrayList<String> coloumns = new ArrayList<String>();
		coloumns.add("User Principal Name");
		coloumns.add("UNIX User ID");
		coloumns.add("Windows User SID");
		coloumns.add("Full Name");
		elementTypes.put("1", coloumns);

		coloumns = new ArrayList<String>();
		coloumns.add("Full Name");
		coloumns.add("Contact Principal Name");
		elementTypes.put("2", coloumns);

		coloumns = new ArrayList<String>();
		coloumns.add("DNS Host Name");
		coloumns.add("UNIX Computer ID");
		elementTypes.put("3", coloumns);

		coloumns = new ArrayList<String>();
		coloumns.add("Display Name");
		coloumns.add("Unique Name");
		elementTypes.put("4", coloumns);

		coloumns = new ArrayList<String>();
		coloumns.add("Name");
		coloumns.add("IP Address");

		elementTypes.put("5", coloumns);

		coloumns = new ArrayList<String>();
		coloumns.add("LongName");
		coloumns.add("Identifier");
		elementTypes.put("6", coloumns);

		return elementTypes;
	}

	public static String getEnrollmentProperties(String id) {
		StringBuilder builder = new StringBuilder();
		String query = "SELECT name,property_value FROM DICT_ENROLLMENT_PROPERTIES WHERE enrollment_id='"
				+ id + "'";
		ResultSet rs = es.getResultSet(query);
		try {
			while (rs.next()) {
				if (propertiesSet.contains(rs.getString(1))) {
					builder.append("<tr><td>");
					if (rs.getString(1) != null)
						builder.append(rs.getString(1).toUpperCase());
					else
						builder.append(rs.getString(1));
					builder.append("</td><td>");
					builder.append(rs.getString(2));
					builder.append("</td></tr>");

				}
			}
		} catch (Exception e) {
			EnrollmentConfComponentImpl.log.error(e.toString(), e);
			builder = new StringBuilder(
					"<tr><td colspan=\"2\">Failed to fetch properties</td></tr>");
		}
		return builder.toString();
	}

	public static ArrayList<EnrollmentInfo> getEnrollmentInfos(Dictionary dict)
			throws DictionaryException {
		ArrayList<EnrollmentInfo> eiList = new ArrayList<EnrollmentInfo>();
		IComponentManager manager = dict.getManager();
		EnrollmentConfComponentImpl.log
				.info("###########Inside getEnrollmentInfos ###########");
		es = new EnrollmentSession();
		es.setManager(manager);
		es.init();
		populateEnrolmentFieldMapping();
		elementTypes = getElementTypes(es);

		enrollmentIDMapping = new HashMap<String, EnrollmentInfo>();
		String query = "SELECT id,domain_name,isRecurring,isActive,enrollment_type FROM dict_enrollments";
		ResultSet rs = es.getResultSet(query);
		ResultSet rs1 = null;
		IDictionaryIterator<IMGroup> strutGroups = null, enumGroups = null;
		EnrollmentInfo ei = null;
		if (rs != null) {
			try {
				eidLdapAccessBeanMapping = new HashMap<String, LDAPAccessBean>();
				while (rs.next()) {
					/*
					 * if (rs.getString(4).equals("0"))// To avoid non active //
					 * enrollments continue;
					 */
					HashMap<String, Integer> EMmap = getDefaultValues();
					HashMap<String, Integer> EMGmap = new HashMap<String, Integer>();
					HashMap<String, Integer> SGmap = new HashMap<String, Integer>();
					ei = new EnrollmentInfo();
					String id = rs.getString(1);

					ei.setEnrollmentDomainName(rs.getString(2));
					ei.setActive(rs.getString(4));
					ei.setRecurring(rs.getString(3));
					ei.setEnrollmentID(id);
					if (null != rs.getString(5)
							&& rs.getString(5).contains(
									"ActiveDirectoryEnroller")) {
						ei.setEnrollmentType("AD");
						// eidLdapAccessBeanMapping.put(id,
						// getLdapAccessBean(id));
					} else {
						ei.setEnrollmentType("LDIF");
						// eidLdapAccessBeanMapping.put(id,
						// getLdifAccessBean(id));
					}
					query = "SELECT type_id,COUNT(*) FROM dict_leaf_elements WHERE element_ID IN (SELECT ID FROM dict_elements WHERE enrollment_id='"
							+ id
							+ "' AND ACTIVE_TO > "
							+ System.currentTimeMillis() + ") GROUP BY type_id";
					rs1 = es.getResultSet(query);
					int total = 0;
					if (rs1 != null) {
						while (rs1.next()) {
							if (null != rs1.getString(1)
									&& null != rs1.getObject(2)) {
								EMmap.put(elementTypes.get(rs1.getString(1)),
										Integer.parseInt(rs1.getString(2)));
								total += Integer.parseInt(rs1.getString(2));
							}
						}
					}
					EnrollmentConfComponentImpl.log.info("EM MAP:" + EMmap);
					rs1.close();
					ei.setEMCount(Integer.toString(total));
					EnrollmentConfComponentImpl.log.info("domain name:"
							+ ei.getEnrollmentDomainName());
					IEnrollment en = dict.getEnrollment(ei
							.getEnrollmentDomainName());

					if (null != en && en.getStatus() != null
							&& en.getStatus().getEndTime() != null)
						ei.setLastSyncTime(en.getStatus().getEndTime()
								.toString());
					else
						ei.setLastSyncTime("Not Able to Get the Last Sync time");

					EnrollmentConfComponentImpl.log.info("ei last sync time:"
							+ ei.getLastSyncTime());

					query = "SELECT count(element_id) FROM DICT_ENUM_GROUPS  where ELEMENT_ID in(SELECT id FROM DICT_ELEMENTS where ACTIVE_TO>"
							+ System.currentTimeMillis()
							+ "and enrollment_id='" + id + "')";
					EnrollmentConfComponentImpl.log.info("#####query:" + query);
					ResultSet resultSet = es.getResultSet(query);
					int enumgroupcount = 0;
					while (resultSet.next()) {
						if (resultSet != null && resultSet.getObject(1) != null)
							enumgroupcount = resultSet.getInt(1);
					}
					resultSet.close();
					EMGmap.put("1", enumgroupcount);
					EnrollmentConfComponentImpl.log.info("#####enumgroupcount:"
							+ enumgroupcount);
					ei.setEMGCount(Integer.toString(enumgroupcount));

					int stgroupcount = 0;
					query = "SELECT count(element_id) FROM DICT_STRUCT_GROUPS  where ELEMENT_ID in(SELECT id FROM DICT_ELEMENTS where ACTIVE_TO>"
							+ System.currentTimeMillis()
							+ "and enrollment_id='" + id + "')";
					EnrollmentConfComponentImpl.log.info("#####query:" + query);
					ResultSet resultSet2 = es.getResultSet(query);
					while (resultSet2.next()) {
						if (resultSet2 != null
								&& resultSet2.getObject(1) != null)
							stgroupcount = resultSet2.getInt(1);
					}
					ei.setSGCount(Integer.toString(stgroupcount));
					resultSet2.close();
					SGmap.put("1", stgroupcount);
					EnrollmentConfComponentImpl.log.info("#####stgroupcount:"
							+ stgroupcount);
					/*
					 * int usercount = EMmap.get("USER"); usercount = usercount
					 * - stgroupcount - enumgroupcount; EMmap.put("USER",
					 * usercount);
					 */
					ei.setEMmap(EMmap);
					EnrollmentConfComponentImpl.log.info("EMmap:" + EMmap);
					ei.setEMGmap(EMGmap);
					EnrollmentConfComponentImpl.log.info("EMGmap:" + EMGmap);
					ei.setSGmap(SGmap);
					EnrollmentConfComponentImpl.log.info("SGmap:" + SGmap);
					enrollmentIDMapping.put(id, ei);
					EnrollmentConfComponentImpl.log.info("enrollmentIDMapping:"
							+ enrollmentIDMapping);
					ei.setInactiveUser(getInActiveActiveUsersCount(id));
					eiList.add(ei);

					EnrollmentConfComponentImpl.log.info("eiList:"
							+ eiList.toString());
				}
			} catch (SQLException e) {
				EnrollmentConfComponentImpl.log.error(e.toString(), e);

			} catch (Exception e) {
				EnrollmentConfComponentImpl.log.error(e.toString(), e);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (rs1 != null) {
						rs1.close();
					}
					if (enumGroups != null)
						enumGroups.close();
					if (strutGroups != null)
						strutGroups.close();
				} catch (SQLException e) {
					EnrollmentConfComponentImpl.log.error(e.toString(), e);
				}
			}
		}

		return eiList;
	}

	@SuppressWarnings("unused")
	private static LDAPAccessBean getLdifAccessBean(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unused")
	private static LDAPAccessBean getLdapAccessBean(String id) {
		LDAPAccessBean ldapbean = null;
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> attributemap = new HashMap<String, String>();
		String query = "SELECT name,property_value FROM DICT_ENROLLMENT_PROPERTIES WHERE enrollment_id='"
				+ id + "'";
		ResultSet rs = es.getResultSet(query);
		try {
			if (rs != null) {
				while (rs.next()) {
					map.put(rs.getString(1), rs.getString(2));
				}
				ldapbean = new LDAPAccessBean();
				ldapbean.setEnrollmentid(id);
				ldapbean.setHostname(map.get("server"));
				if (map.get("roots") != null) {
					ldapbean.setOuroot(map.get("roots").substring(1,
							map.get("roots").length() - 1));
				}
				HashMap<String, String> subTreeFilterMap = new HashMap<String, String>();
				if (map.get("password") != null)
					ldapbean.setPassword(decryptor.decrypt(map.get("password")));
				else
					ldapbean.setPassword("");
				ldapbean.setPortno(map.get("port"));
				ldapbean.setAuthentication("simple");
				ldapbean.setUsername(map.get("login"));
				if ((map.get("enroll.users") != null)
						&& map.get("enroll.users").equals("true"))
					subTreeFilterMap.put("1", map.get("user.requirements"));
				if ((map.get("enroll.applications") != null)
						&& map.get("enroll.applications").equals("true"))
					subTreeFilterMap.put("6",
							map.get("application.requirements"));
				if ((map.get("enroll.contacts") != null)
						&& map.get("enroll.contacts").equals("true"))
					subTreeFilterMap.put("2", map.get("contact.requirements"));
				/*
				 * if ((map.get("enroll.groups") != null) &&
				 * map.get("enroll.groups").equals("true"))
				 * subTreeFilterMap.put("7",map.get("group.requirements"));
				 */

				ldapbean.setSubtreefiltermap(subTreeFilterMap);
				query = "Select  dfm.external_name,dtf.label from dict_type_fields dtf, dict_field_mappings dfm where dtf.id=dfm.field and dfm.enrollment_id='"
						+ id + "'";
				rs = es.getResultSet(query);
				while (rs.next()) {
					attributemap.put(rs.getString(2), rs.getString(1));
				}
				ldapbean.setAttributelabelmapping(attributemap);
			}
		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log.error(e.toString(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				EnrollmentConfComponentImpl.log.error(e.toString(), e);
			}
		}
		EnrollmentConfComponentImpl.log.info("--ldapbean.getHostname:"
				+ ldapbean.getHostname() + "--");
		EnrollmentConfComponentImpl.log.info("--ldapbean.getOuroot:"
				+ ldapbean.getOuroot() + "--");
		EnrollmentConfComponentImpl.log.info("--ldapbean.getPassword:"
				+ ldapbean.getPassword() + "--");
		EnrollmentConfComponentImpl.log.info("--ldapbean.getProtNo:"
				+ ldapbean.getPortno() + "--");
		EnrollmentConfComponentImpl.log.info("--ldapbean.getSubtreefilter:"
				+ ldapbean.getSubtreefiltermap().toString() + "--");
		EnrollmentConfComponentImpl.log.info("--ldapbean.getUsername:"
				+ ldapbean.getUsername() + "--");
		EnrollmentConfComponentImpl.log.info("--ldapbean.getAuthentication:"
				+ ldapbean.getAuthentication() + "--");
		EnrollmentConfComponentImpl.log
				.info("--ldapbean.getAttributelabelmapping:"
						+ ldapbean.getAttributelabelmapping().toString() + "--");
		return ldapbean;
	}

	private static void populateEnrolmentFieldMapping() {

		enrollmentFieldMapping = new HashMap<String, HashMap<String, TreeMap<String, String>>>();
		String query = "Select dfm.enrollment_id,dfm.field_type,dtf.label,dtf.mapping From DICT_FIELD_MAPPINGS dfm,DICT_TYPE_FIELDS dtf where dfm.field=dtf.id";
		ResultSet rs = es.getResultSet(query);
		try {
			while (rs.next()) {
				if (enrollmentFieldMapping.get(rs.getString(1)) == null) {
					HashMap<String, TreeMap<String, String>> fieldMapping = new HashMap<String, TreeMap<String, String>>();
					TreeMap<String, String> enMapping = new TreeMap<String, String>();
					enMapping.put(rs.getString(3), rs.getString(4));
					fieldMapping.put(rs.getString(2), enMapping);
					enrollmentFieldMapping.put(rs.getString(1), fieldMapping);
				} else {
					HashMap<String, TreeMap<String, String>> fieldMapping = enrollmentFieldMapping
							.get(rs.getString(1));
					if (fieldMapping.get(rs.getString(2)) == null) {
						TreeMap<String, String> enMapping = new TreeMap<String, String>();
						enMapping.put(rs.getString(3), rs.getString(4));
						fieldMapping.put(rs.getString(2), enMapping);
					} else {
						fieldMapping.get(rs.getString(2)).put(rs.getString(3),
								rs.getString(4));
					}
				}

			}
		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log.error(e.toString(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				EnrollmentConfComponentImpl.log.error(e.toString(), e);
			}
		}

	}

	private static HashMap<String, Integer> getDefaultValues() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("USER", 0);
		map.put("CONTACT", 0);
		map.put("HOST", 0);
		map.put("APPLICATION", 0);
		map.put("SITE", 0);
		map.put("CLIENT_INFO", 0);
		return map;
	}

	public static HashMap<String, EnrollmentInfo> getEnrollmentIDMapping() {
		return enrollmentIDMapping;
	}

	public static void setEnrollmentIDMapping(
			HashMap<String, EnrollmentInfo> enrollmentIDMapping) {
		DictHelper.enrollmentIDMapping = enrollmentIDMapping;
	}

	public static HashMap<String, HashMap<String, TreeMap<String, String>>> getEnrollmentFieldMapping() {
		return enrollmentFieldMapping;
	}

	public static void setEnrollmentFieldMapping(
			HashMap<String, HashMap<String, TreeMap<String, String>>> enrollmentFieldMapping) {
		DictHelper.enrollmentFieldMapping = enrollmentFieldMapping;
	}

	public static void setElementTypes(HashMap<String, String> elementTypes) {
		DictHelper.elementTypes = elementTypes;
	}

	public static String getEnrollmentForDropDown() {
		StringBuilder sb = new StringBuilder();
		sb.append(MessageFormat.format(selectedBuilder, "-1",
				"Select Enrollment"));
		if (enrollmentIDMapping != null) {
			for (String key : enrollmentIDMapping.keySet()) {

				sb.append(MessageFormat.format(builder, key,
						enrollmentIDMapping.get(key).getEnrollmentDomainName()));

			}
		}
		return sb.toString();
	}

	public static String getProperty(String elementType, String enrollmentId) {

		StringBuilder sb = new StringBuilder();
		StringBuilder cb = new StringBuilder();
		int count = 0;

		populateEnrolmentFieldMapping();
		if (enrollmentId != null && elementType != null
				&& enrollmentFieldMapping != null) {

			if (enrollmentId.trim().length() <= 0) {
				for (String enrollmentKey : enrollmentFieldMapping.keySet()) {
					HashMap<String, TreeMap<String, String>> fieldTypeMap = enrollmentFieldMapping
							.get(enrollmentKey);
					TreeMap<String, String> map = fieldTypeMap.get(elementType);

					if (map != null && map.size() > 0) {
						ArrayList<String> cbdef = eTypedefCheckMapping
								.get(elementType);
						cb.append("<label><span>Property Selector &nbsp : &nbsp</span><br /><table id='"
								+ elementType
								+ "checkboxtable' width=\"1100px\"  border=\"0\">");
						cb.append(MessageFormat
								.format("<tr style=\"display:none;\"><td ><input type=\"checkbox\" value=\"{1}\" name=\"{2}property\" id=\"{0}\" onChange=\"checkCount(this.id)\"/><label for=\"{0}\">{1}</label></td></tr>",
										"dummy", "dummy", "elementType"));
						for (String key : map.keySet()) {

							if (count % 4 == 0)
								cb.append("<tr>");
							count++;
							if (cbdef != null && cbdef.contains(key)) {
								cb.append("<td class=\"whitebg\" >"
										+ MessageFormat.format(
												checkedboxBuilder, elementType
														+ "prop" + count, key,
												elementType) + "</td>");
							} else {
								cb.append("<td class=\"whitebg\" >"
										+ MessageFormat.format(checkboxBuilder,
												elementType + "prop" + count,
												key, elementType) + "</td>");
							}
							if (count % 4 == 0)
								cb.append("</tr>");
							sb.append(MessageFormat.format(builder,
									map.get(key), key));

						}
					}

				}

			} else {
				EnrollmentConfComponentImpl.log.info("enrollmentFieldMapping:"
						+ enrollmentFieldMapping.toString());
				HashMap<String, TreeMap<String, String>> fieldMap = enrollmentFieldMapping
						.get(enrollmentId);
				if (fieldMap != null) {
					TreeMap<String, String> map = fieldMap.get(elementType);
					if (map != null && map.size() > 0) {
						ArrayList<String> cbdef = eTypedefCheckMapping
								.get(elementType);
						cb.append("<label><span>Properties for Display: &nbsp;&nbsp;</span><br /><table id='"
								+ elementType
								+ "checkboxtable' width=\"1100px\" border=\"0\">");
						cb.append(MessageFormat
								.format("<tr style=\"display:none;\"><td ><input type=\"checkbox\" value=\"{1}\" name=\"{2}property\" id=\"{0}\" onChange=\"checkCount(this.id)\"/><label for=\"{0}\">{1}</label></td></tr>",
										"dummy", "dummy", "elementType"));
						for (String key : map.keySet()) {
							if (count % 4 == 0)
								cb.append("<tr>");
							count++;
							if (cbdef != null && cbdef.contains(key)) {
								cb.append("<td class=\"whitebg\" >"
										+ MessageFormat.format(
												checkedboxBuilder, elementType
														+ "prop" + count, key,
												elementType) + "</td>");
							} else {
								cb.append("<td>"
										+ MessageFormat.format(checkboxBuilder,
												elementType + "prop" + count,
												key, elementType) + "</td>");
							}
							if (count % 4 == 0)
								cb.append("</tr>");
							sb.append(MessageFormat.format(builder,
									map.get(key), key));
						}
					}
				}
			}
		}

		if (sb.toString().trim().length() <= 0) {
			sb.append(MessageFormat.format(builder, "empty", "None"));
		}
		cb.append("</tr></table>");
		EnrollmentConfComponentImpl.log.info("sb:" + sb.toString());
		return sb.toString() + "##$$" + cb.toString();
	}

	public static TotalUsers getFilteredData(String elementType,
			String enrollmentId, String filterVal, String[] projection,
			int interval) {
		StringBuilder sb = new StringBuilder();
		HashMap<String, HashMap<String, String>> fieldMappingValue = getFieldMappingValue(filterVal);
		EnrollmentConfComponentImpl.log.info("EnrollmentFieldMapping:"
				+ enrollmentFieldMapping);
		EnrollmentConfComponentImpl.log.info("fieldMappingValue:"
				+ fieldMappingValue);
		sb.append(getHeading(projection));
		EnrollmentConfComponentImpl.log.info("After getHeading");
		String projectionString = getProjectionString(elementType,
				enrollmentId, projection);
		EnrollmentConfComponentImpl.log.info("After getProjectionString");
		String predicateString = getPredicateString(elementType, enrollmentId,
				fieldMappingValue);
		EnrollmentConfComponentImpl.log.info("After getPredicates");
		String query = "SELECT {0} FROM dict_elements de, dict_leaf_elements dle WHERE {1}";

		query = MessageFormat.format(query, projectionString, predicateString);
		EnrollmentConfComponentImpl.log.info("Query" + query);
		TotalUsers result = getTableData(query, elementType, interval);
		if (!result.getTableData().contains("No Results Found")) {
			sb.append(result.getTableData());
			result.setTableData(sb.toString());
		}
		return result;
	}

	public static TotalUsers getGroupMembers(String etype, String groupid,
			int interval, String enrollmentId) {
		EnrollmentConfComponentImpl.log.info("EV: DictHelper getGroupMembers");
		populateEnrolmentFieldMapping();
		EnrollmentConfComponentImpl.log.info("EV: enrollmentFieldMapping is "
				+ enrollmentFieldMapping);
		EnrollmentConfComponentImpl.log.info("EV: enrollmentId is "
				+ enrollmentId);
		HashMap<String, TreeMap<String, String>> map = enrollmentFieldMapping
				.get(enrollmentId);
		EnrollmentConfComponentImpl.log.info("EV: map is " + map);
		EnrollmentConfComponentImpl.log.info("EV: etype " + etype);
		StringBuilder sb = new StringBuilder();
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT de.id ID");
		projections = new ArrayList<String>();
		projections.add("ID");

		sb.append("<thead>");
		TreeMap<String, String> fieldMap = map.get(etype);
		if (fieldMap != null) {

			EnrollmentConfComponentImpl.log
					.info("EV: Before User Principal Name");
			if (fieldMap.get("User Principal Name") != null) {
				sb.append("<th class=\"cc\" title=\"UserPrincipalName\">User Principal Name</th>");
				queryBuilder.append(",dle.");
				queryBuilder.append(fieldMap.get("User Principal Name"));
				queryBuilder.append(" UserPrincipalName");
				projections.add("UserPrincipalName");
			}
			EnrollmentConfComponentImpl.log
					.info("EV: Before  Windows User SID");
			if (fieldMap.get("Windows User SID") != null) {
				sb.append("<th class=\"cc\" title=\"Windowssid\">Windows sid </th>");
				queryBuilder.append(",dle.");
				queryBuilder.append(fieldMap.get("Windows User SID"));
				queryBuilder.append(" WindowsUserSID");
				projections.add("WindowsUserSID");

			}
			EnrollmentConfComponentImpl.log.info("EV: Before  UNIX User ID");
			if (fieldMap.get("UNIX User ID") != null) {
				sb.append("<th class=\"cc\" title=\"UnixId\">Unix Id</th>");
				projections.add("UNIXUserID");
				queryBuilder.append(",dle.");
				queryBuilder.append(fieldMap.get("UNIX User ID"));
				queryBuilder.append(" UNIXUserID");

			}
		}
		sb.append("<th class=\"cc\" title=\"Display Name\">Display Name</th>");
		sb.append("</thead>");
		projections.add("DISPLAYNAME");
		queryBuilder
				.append(" , de.displayName DISPLAYNAME FROM dict_elements de, dict_leaf_elements dle WHERE de.id=dle.element_id AND de.original_id in (select member_id from dict_enum_members where group_id=''{0}'') AND dle.type_id=''{1}''  and de.active_to>"
						+ System.currentTimeMillis());

		String query = MessageFormat.format(queryBuilder.toString(), groupid,
				etype, enrollmentId);
		EnrollmentConfComponentImpl.log.info("EV: query" + query);
		TotalUsers tu = getTableData(query, groupid, interval);
		sb.append(tu.getTableData());
		sb.append("</table>");
		EnrollmentConfComponentImpl.log.info("EV: TableData: " + sb.toString());
		tu.setTableData(sb.toString());
		return tu;
	}

	private static TotalUsers getTableData(String query, String elString,
			int interval) {
		EnrollmentConfComponentImpl.log.info("Inside getTableData");
		EnrollmentConfComponentImpl.log.info("Inside getTableData projections:"
				+ projections);
		EnrollmentConfComponentImpl.log
				.info("Inside getTableData enrollmentIDMapping:"
						+ enrollmentIDMapping);
		dataKey = new ArrayList<String>();
		dataMapping = new HashMap<String, HashMap<String, String>>();

		ResultSet rs = es.getResultSet(query);
		int projectionSize = projections.size();
		TotalUsers totalUsers = new TotalUsers();
		StringBuilder sb = new StringBuilder("<tbody id='" + elString
				+ "tbody'>");
		try {
			while (rs.next()) {
				String key = rs.getString(projections.get(0));
				HashMap<String, String> map = new HashMap<String, String>();
				if (key != null) {
					dataKey.add(key);
					map.put(projections.get(0), key);
					for (int i = 1; i < projectionSize; i++) {
						if (projections.get(i).equals("ENROLLMENTID")) {
							map.put(projections.get(i), enrollmentIDMapping
									.get(rs.getString(projections.get(i)))
									.getEnrollmentDomainName());
						} else {
							map.put(projections.get(i),
									rs.getString(projections.get(i)));
						}
					}
				}
				dataMapping.put(key, map);
			}
			EnrollmentConfComponentImpl.log.info("getTableData dataKey:"
					+ dataKey.size());

			EnrollmentConfComponentImpl.log.info("getTableData dataMapping:"
					+ dataMapping.size());
			totalUsers = getPaginationData(0, interval);
			sb.append(totalUsers.getTempString());
			totalUsers.setTotal(Integer.toString(dataKey.size()));

		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log.error(e.toString(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					EnrollmentConfComponentImpl.log.error(e.toString(), e);
				}
			}
		}
		if (!sb.toString().contains("<tr")) {
			sb.append("<tr><td align=\"center\" colspan=\""
					+ (projections.size() - 1)
					+ "\">No Results Found</td></tr>");
		}
		EnrollmentConfComponentImpl.log.info("Projections" + projections);
		sb.append("</tbody>");

		totalUsers.setTableData(sb.toString());
		return totalUsers;
	}

	public static TotalUsers getPaginationData(int start, int interval) {
		TotalUsers totalUsers = new TotalUsers();
		StringBuffer sb = new StringBuffer();

		if (dataKey != null && dataMapping != null) {
			EnrollmentConfComponentImpl.log.info("getPaginationData dataKey:"
					+ dataKey.size());
			EnrollmentConfComponentImpl.log
					.info("getPaginationData dataMapping:" + dataMapping.size());

			int projectionSize = projections.size();
			int totalpages;
			EnrollmentConfComponentImpl.log
					.info("getPaginationData projectionSize:" + projectionSize);
			int loopend = start + interval;
			loopend = loopend < dataKey.size() ? loopend : dataKey.size();
			EnrollmentConfComponentImpl.log.info("getPaginationData loopend:"
					+ loopend);
			EnrollmentConfComponentImpl.log
					.info("getPaginationData dataKey.size() :" + dataKey.size());
			ArrayList<String> elementids = new ArrayList<String>();
			for (int i = start; i < loopend; i++) {
				String key = dataKey.get(i);

				HashMap<String, String> dataMap = dataMapping.get(key);
				String elementid = dataMap.get(projections.get(0));
				EnrollmentConfComponentImpl.log
						.info("dataMap.get(projections.get(0)) :" + elementid);
				elementids.add(elementid);
				sb.append("<tr id='tr" + elementid + "'>");
				/*
				 * sb.append("<td class=\"cc\">");
				 * sb.append(dataMap.get(projections.get(1)));
				 * sb.append("</td>");
				 */
				for (int j = 1; j < projectionSize; j++) {
					sb.append("<td class=\"cc\">");
					sb.append(dataMap.get(projections.get(j)));
					sb.append("</td>");
				}
				sb.append("</tr>");
			}
			int pageno = 1;
			totalpages = dataKey.size() / interval;
			if (dataKey.size() % interval != 0)
				totalpages = totalpages + 1;
			if (start != 0) {
				pageno = (start / interval) + 1;
			}
			EnrollmentConfComponentImpl.log.info("getPaginationData loopend:"
					+ loopend);
			EnrollmentConfComponentImpl.log
					.info("getPaginationData dataKey.size() :" + dataKey.size());
			EnrollmentConfComponentImpl.log.info("getPaginationData start:"
					+ start);
			int colspanredsize = projectionSize - 1;
			if (dataKey.size() > 0) {
				sb.append("<tr id='nextprevious' style='background-color: #FFFFFF;'>");
				if (start != 0 && loopend - interval >= 0) {
					sb.append(" <td align='left' style='background-color: #FFFFFF;'><img src='images/first.jpg' id='firstbutton'><img src='images/prev.jpg' id='prevButton' ></td>");
					colspanredsize--;
				}

				if (loopend < dataKey.size()
						|| (start != 0 && loopend - interval >= 0)) {
					sb.append(" <td align='right' colspan='");
					sb.append(colspanredsize);
					sb.append("' style='background-color: #FFFFFF;'> <span style='margin-right: 360px;'>Page ");
					sb.append(pageno);
					sb.append(" of ");
					sb.append(totalpages);
					sb.append(" </span>");
					if (loopend < dataKey.size()) {
						sb.append("<img src='images/next.jpg' id='nextButton'>");
						sb.append("<img src='images/last.jpg' id='lastbutton'>");
					}
					sb.append("</td>");
				}
			}
			sb.append("</tr>");
			totalUsers.setElementIds(elementids);
			totalUsers.setStart(start);
			totalUsers.setInterval(interval);
			totalUsers.setTotalPages(totalpages);
			totalUsers.setTempString(sb.toString());
		}

		return totalUsers;
	}

	private static String getPredicateString(String elementType,
			String enrollmentId,
			HashMap<String, HashMap<String, String>> fieldMappingValue) {
		StringBuilder sb = new StringBuilder(
				"de.id=dle.element_id AND de.ACTIVE_TO >"
						+ System.currentTimeMillis() + " AND ");
		if (enrollmentId != null)
			sb.append("de.enrollment_id='" + enrollmentId
					+ "' AND dle.type_id='" + elementType + "' ");
		else
			sb.append("dle.type_id='" + elementType + "' ");
		EnrollmentConfComponentImpl.log.info("The fieldMappingValue:"
				+ fieldMappingValue);
		if (fieldMappingValue != null) {
			Iterator<String> fieldIterator = fieldMappingValue.keySet()
					.iterator();
			if (fieldIterator.hasNext())
				sb.append(" AND ");
			while (fieldIterator.hasNext()) {
				HashMap<String, String> mappingValue = fieldMappingValue
						.get(fieldIterator.next());
				if (mappingValue != null) {
					Iterator<String> mappingIterator = mappingValue.keySet()
							.iterator();
					String mapping = mappingIterator.next();
					if (mapping != null && !mapping.equals("1e")
							&& mappingValue.get(mapping) != null) {
						sb.append("LOWER(dle." + mapping + ") like LOWER('"
								+ validate(mappingValue.get(mapping)) + "%')");
						if (fieldIterator.hasNext()) {
							sb.append(" AND ");
						}
					}

				}
			}
		}
		return sb.toString();
	}

	private static String validate(String paramString) {
		String str = paramString;
		if ((paramString.contains("="))
				|| (paramString.toLowerCase().contains(" and "))
				|| (paramString.toLowerCase().contains(" or "))
				|| (paramString.toLowerCase().contains(" where "))
				|| (paramString.toLowerCase().contains(" from "))
				|| (paramString.toLowerCase().contains(" like "))
				|| (paramString.contains(">"))
				|| (paramString.toLowerCase().contains(" not "))
				|| (paramString.contains("<"))) {
			str = "-99999";
		}
		return str;
	}

	private static String getProjectionString(String elementType,
			String enrollmentId, String[] projection) {
		projections = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(
				"de.id id,de.ENROLLMENT_ID EnrollmentId");
		projections.add("id");
		// projections.add("EnrollmentId");

		HashMap<String, TreeMap<String, String>> etypemap = enrollmentFieldMapping
				.get(enrollmentId);
		if (etypemap != null) {
			TreeMap<String, String> map = etypemap.get(elementType);
			if (projection != null) {
				for (String field : projection) {
					sb.append(", dle." + map.get(field) + " "
							+ validField(field));
					projections.add(validField(field));
				}
			}
		}
		return sb.toString();
	}

	private static String validField(String paramString) {
		String str1 = Normalizer.normalize(paramString, Normalizer.Form.NFD);
		String str2 = str1.replaceAll("[^A-Za-z0-9]", "");
		return str2;
	}

	private static HashMap<String, HashMap<String, String>> getFieldMappingValue(
			String filterVal) {
		HashMap<String, HashMap<String, String>> result = null;
		if (filterVal != null && filterVal.trim().length() > 0) {
			result = new HashMap<String, HashMap<String, String>>();
			StringTokenizer st = new StringTokenizer(filterVal, "@#$");
			int tokencount = 0;
			while (st.hasMoreTokens()) {
				StringTokenizer temp = new StringTokenizer(st.nextToken(), "=");

				String field = temp.nextToken();
				result.put(field, new HashMap<String, String>());
				result.get(field).put(temp.nextToken(), temp.nextToken());
				tokencount++;
				if (tokencount == 3)
					break;
			}
		}
		return result;
	}

	private static String getHeading(String[] projection) {

		StringBuilder sb = new StringBuilder("<thead>");
		if (projection != null) {
			for (String field : projection)
				sb.append("<th class=\"cc\" >" + field + "</th>");
		}
		sb.append("</thead>");
		return sb.toString();
	}

	public static String getDataSummary(String id) {
		StringBuilder sb = new StringBuilder();
		String query = "SELECT * FROM dict_elements de, dict_leaf_elements dle WHERE de.id=dle.element_id AND de.id='"
				+ id + "'";
		EnrollmentConfComponentImpl.log.info("query:" + query);
		ResultSet rs = es.getResultSet(query);
		try {
			while (rs.next()) {

				String enrollmentId = rs.getString("ENROLLMENT_ID");
				String typeId = rs.getString("TYPE_ID");
				EnrollmentConfComponentImpl.log.info("enrollmentId:"
						+ enrollmentId);
				EnrollmentConfComponentImpl.log.info("typeId:" + typeId);
				if (enrollmentFieldMapping.get(enrollmentId) != null
						&& enrollmentFieldMapping.get(enrollmentId).get(typeId) != null) {
					TreeMap<String, String> fieldMapping = enrollmentFieldMapping
							.get(enrollmentId).get(typeId);
					for (String field : fieldMapping.keySet()) {
						if (!field.equalsIgnoreCase("windowsSid")) {
							sb.append("<tr>");
							sb.append("<td>");
							sb.append(field);
							sb.append("</td>");
							sb.append("<td>");
							if (fieldMapping.get(field) != null)
								sb.append(rs.getString(fieldMapping.get(field)));
							else
								sb.append("");
							sb.append("</td>");
							sb.append("</tr>");
						}
					}
				}
			}

			query = "SELECT   de.displayName from DICT_ELEMENTs de, DICT_ENUM_MEMBERS dem where de.id=dem.group_id AND dem.member_id='"
					+ id + "' AND de.active_to>" + System.currentTimeMillis();
			rs = es.getResultSet(query);
			String groups = "";
			while (rs.next()) {
				groups += rs.getString(1) + ",";
			}
			sb.append("<tr>");
			sb.append("<td>");
			sb.append("User Groups");
			sb.append("</td>");
			sb.append("<td>");
			if (groups.trim().length() > 0) {
				groups = groups.substring(0, groups.length() - 2);
				sb.append(groups);
			} else {
				sb.append("N/A");
			}

			sb.append("</td>");
			sb.append("</tr>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		EnrollmentConfComponentImpl.log.info("getData:" + sb.toString());
		return sb.toString();
	}

	public static TotalUsers getGroups(String id, String filterVal) {
		EnrollmentConfComponentImpl.log
				.info("----DictHelper.getElementTypes# Result:elementTypesSet-----\n"
						+ elementTypes.toString());
		TotalUsers tu = new TotalUsers();
		String field = null;
		String mapping = null;
		String value = null;
		EnrollmentConfComponentImpl.log.info("filterVal:" + filterVal);
		if (filterVal != null) {
			StringTokenizer st = new StringTokenizer(filterVal, "#$#");
			EnrollmentConfComponentImpl.log.info("st.hasMoreTokens():"
					+ st.hasMoreTokens());
			if (st.hasMoreTokens())
				mapping = st.nextToken();
			if (st.hasMoreTokens())
				field = st.nextToken();
			if (st.hasMoreTokens())
				value = st.nextToken();
		}
		EnrollmentConfComponentImpl.log.info("Field:" + field);
		EnrollmentConfComponentImpl.log.info("mapping:" + mapping);
		EnrollmentConfComponentImpl.log.info("value:" + value);
		StringBuffer sb = new StringBuffer(
				"<thead><th class=\"juris\" title=\"Group Name\">Group Name</th>");
		sb.append("<th class=\"cc\" title=\"User Member Count\">User </th>");
		sb.append("<th class=\"cc\" title=\"Host Member Count\">Host </th>");
		sb.append("<th class=\"cc\" title=\"Site Member Count\">Site</th>");
		sb.append("<th class=\"cc\" title=\"Application Member Count\">Application</th>");
		sb.append("</thead>");
		HashMap<String, HashMap<String, String>> outputMap = new HashMap<String, HashMap<String, String>>();
		boolean flag = true;
		ResultSet rs = null;
		ArrayList<GroupBean> gb = new ArrayList<GroupBean>();
		if (id != null) {
			try {
				if (mapping == null || field == null || value == null
						|| mapping.equals("1")
						|| field.equals("Select a Filter")
						|| value.trim().length() <= 0) {
					String query = "SELECT dem.GROUP_ID GROUP_ID,de.displayname DisplayName ,dem.element_type_id element_type,count(dem.enrollment_id) memcount FROM DICT_ENUM_MEMBERS dem,Dict_Elements de where de.original_id=dem.group_id and dem.enrollment_id=''{0}'' and  de.active_to >"
							+ System.currentTimeMillis()
							+ " and dem.active_to >"
							+ System.currentTimeMillis()
							+ " group by dem.GROUP_ID,de.displayname,dem.element_type_id";
					query = MessageFormat.format(query, id);
					rs = es.getResultSet(query);

					if (rs != null) {
						while (rs.next()) {

							String groupId = rs.getString("GROUP_ID");
							String dispName = rs.getString("DisplayName");
							String elementType = rs.getString("element_type");
							String count = rs.getString("memcount");
							if (outputMap.get(groupId) != null) {
								outputMap.get(groupId).put(elementType, count);

							} else {
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("DisplayName", dispName);
								map.put(elementType, count);
								outputMap.put(groupId, map);
							}
							if (flag)
								flag = false;
						}

					}
				} else {
					String query = "SELECT de.id id FROM dict_elements de, dict_leaf_elements dle WHERE de.id=dle.element_id AND de.enrollment_id=''{0}'' AND LOWER(dle.{1}) like LOWER(''%{2}%'') and de.active_to>"
							+ System.currentTimeMillis();
					query = MessageFormat.format(query, id, mapping,
							validate(value));
					rs = es.getResultSet(query);
					StringBuilder members = new StringBuilder();
					while (rs.next()) {
						members.append("'");
						members.append(rs.getString(1));
						members.append("',");
					}
					rs.close();
					EnrollmentConfComponentImpl.log.info("members:"
							+ members.toString());
					String member = null;
					if (members.toString().trim().length() > 0)
						member = members.toString().substring(0,
								members.toString().length() - 1);
					if (member != null) {
						query = MessageFormat
								.format("SELECT dem.GROUP_ID GROUP_ID,de.displayname DisplayName, dem.element_type_id element_type,count(dem.enrollment_id) memcount FROM DICT_ENUM_MEMBERS dem,Dict_Elements de where de.original_id=dem.group_id and de.active_to>"
										+ System.currentTimeMillis()
										+ " and dem.active_to>"
										+ System.currentTimeMillis()
										+ " and dem.enrollment_id=''{1}'' and dem.group_id in (select group_id from dict_enum_members where member_id in ({0})) group by dem.GROUP_ID,de.displayname,dem.element_type_id",
										member, id);
						rs = es.getResultSet(query);
						EnrollmentConfComponentImpl.log.info("---EV: Query= "
								+ query);
						if (rs != null) {
							while (rs.next()) {
								EnrollmentConfComponentImpl.log
										.info("Inside record loop");
								String groupId = rs.getString("GROUP_ID");
								String dispName = rs.getString("DisplayName");
								String elementType = rs
										.getString("element_type");
								String count = rs.getString("memcount");
								if (outputMap.get(groupId) != null) {
									outputMap.get(groupId).put(elementType,
											count);

								} else {
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("DisplayName", dispName);
									map.put(elementType, count);
									outputMap.put(groupId, map);
								}
								if (flag)
									flag = false;

							}

						}
					}
				}
				EnrollmentConfComponentImpl.log.info("OutputMap:" + outputMap);
				Iterator<String> iterator = outputMap.keySet().iterator();
				HashMap<String, String> revmap = getReverseElementTypes(elementTypes);
				String user = revmap.get("USER");
				String host = revmap.get("HOST");
				String site = revmap.get("SITE");
				String application = revmap.get("APPLICATION");
				while (iterator.hasNext()) {
					String key = iterator.next();
					HashMap<String, String> map = outputMap.get(key);
					sb.append("<tr>");
					sb.append("<td class=\"juris\">");
					sb.append(map.get("DisplayName"));
					sb.append("</td>");
					if (map.get(user) != null) {
						sb.append("<td class=\"cc\" ><a href=\"#\" id='au");
						sb.append(key);
						sb.append("'><u>");
						sb.append(map.get(user));
						sb.append("</u></a></td>");
						GroupBean gbean = new GroupBean();
						gbean.setAid("au" + key);
						gbean.setElementId(key);
						gbean.setElementType("u");
						gbean.setGroupName(map.get("DisplayName"));
						gbean.setEnrollmentName(enrollmentIDMapping.get(id)
								.getEnrollmentDomainName());
						EnrollmentConfComponentImpl.log
								.info("verify enrollment id:" + id);
						gbean.setEnrollmentId(id);
						gb.add(gbean);

					} else {
						sb.append("<td class=\"cc\">0</td>");
					}
					if (map.get(host) != null) {
						sb.append("<td class=\"cc\" >");
						sb.append(map.get(host));
						sb.append("</td>");
					} else {
						sb.append("<td class=\"cc\">0</td>");
					}
					if (map.get(site) != null) {
						sb.append("<td class=\"cc\">");
						sb.append(map.get(site));
						sb.append("</td>");
					} else {
						sb.append("<td class=\"cc\">0</td>");
					}
					if (map.get(application) != null) {
						sb.append("<td class=\"cc\">");
						sb.append(map.get(application));
						sb.append("</td>");
					} else {
						sb.append("<td class=\"cc\">0</td>");
					}
					sb.append("</tr>");

				}
			} catch (Exception e) {
				EnrollmentConfComponentImpl.log.error(e.toString(), e);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					EnrollmentConfComponentImpl.log.error(e.toString(), e);
				}
			}
		}
		if (flag) {

			sb = new StringBuffer(
					"<tr><td colspan='5' style='text-align:center;'> No Groups enrolled </td></tr>");

		}
		tu.setGroups(gb);
		tu.setTableData(sb.toString());
		return tu;

	}

	private static HashMap<String, String> getReverseElementTypes(
			HashMap<String, String> elementTypes2) {
		EnrollmentConfComponentImpl.log
				.info("----EV: Element Types for reverse:" + elementTypes2);
		HashMap<String, String> revelementTypes2 = new HashMap<String, String>();
		Iterator<String> iterator = elementTypes2.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			revelementTypes2.put(elementTypes2.get(key), key);
		}
		EnrollmentConfComponentImpl.log
				.info("----EV: Element Types for revelementTypes:"
						+ revelementTypes2);
		return revelementTypes2;
	}

	/*
	 * private static String getMembers(String groupId) { StringBuilder sb = new
	 * StringBuilder(); String query =
	 * "SELECT member_id member_id FROM DICT_ENUM_MEMBERS WHERE group_id='" +
	 * groupId + "'"; ResultSet rs = null; rs = es.getResultSet(query);
	 * ArrayList<String> members = new ArrayList<String>(); int loopbreaker = 0;
	 * try { if (rs != null) { while (rs.next()) { members.add(rs.getString(1));
	 * loopbreaker++; if (loopbreaker == 5) break; }
	 * 
	 * if (members.size() > 0) { for (String member : members) { query =
	 * "SELECT id id,displayname displayname FROM DICT_ELEMENTS WHERE id = " +
	 * member; rs = es.getResultSet(query); while (rs.next()) {
	 * sb.append(" <a href=\"#\" onclick='getUserData(\"");
	 * sb.append(rs.getString(1)); sb.append("\")'><u>");
	 * sb.append(rs.getString(2)); sb.append("</u></a>"); sb.append("</br>"); }
	 * } sb.append("<a  href=\"#\" onclick='showAllMembers(\"");
	 * sb.append(groupId); sb.append("\")'> <u>View All Members</u></a>"); }
	 * else { sb.append("No members for the group"); } } } catch (Exception e) {
	 * EnrollmentConfComponentImpl.log.error(e.toString(), e); } finally { try {
	 * if (rs != null) { rs.close(); } } catch (SQLException e) {
	 * EnrollmentConfComponentImpl.log.error(e.toString(), e); } }
	 * 
	 * return sb.toString(); }
	 */

	public static String getOptionalFilterForGroups(String id) {
		populateEnrolmentFieldMapping();
		EnrollmentConfComponentImpl.log
				.info("Enrollment viewer enrollmentFieldMapping:"
						+ enrollmentFieldMapping);
		StringBuilder sb = new StringBuilder();

		if (id != null) {
			HashMap<String, TreeMap<String, String>> fieldMapping = enrollmentFieldMapping
					.get(id);
			if (fieldMapping != null) {
				Iterator<String> iterator = fieldMapping.keySet().iterator();
				sb.append(MessageFormat.format(builder, "1", "Select a Filter"));
				while (iterator.hasNext()) {
					String elementType = iterator.next();
					TreeMap<String, String> map = fieldMapping.get(elementType);
					ArrayList<String> filters = elementTypeFieldMapping
							.get(elementType);
					if (filters != null) {
						for (String filter : filters)
							if (map.get(filter) != null)
								sb.append(MessageFormat.format(builder,
										map.get(filter), filter));
					}
				}

			}
		}
		if (sb.toString().trim().length() <= 0)
			sb.append(MessageFormat.format(builder, "empty", "None"));
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String validateEnrollment(String id, String etype) {
		StringBuilder sb = new StringBuilder();
		LDAPAccessBean lab = eidLdapAccessBeanMapping.get(id);
		HashSet<String> ldapDataSet = new HashSet<String>();
		HashSet<String> dictDataSet = new HashSet<String>();
		TreeSet<String> diffDataSet = new TreeSet<String>();
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL,
				"ldap://" + lab.getHostname() + ":" + lab.getPortno());
		env.put(Context.SECURITY_AUTHENTICATION, lab.getAuthentication());
		env.put(Context.SECURITY_PRINCIPAL, lab.getUsername());
		env.put(Context.SECURITY_CREDENTIALS, lab.getPassword());
		NamingEnumeration<SearchResult> results = null;

		ResultSet rs = null;
		try {
			DirContext ctx = new InitialDirContext(env);
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			results = ctx.search(lab.getOuroot(), lab.getSubtreefiltermap()
					.get("1"), controls);
			String key = lab.getAttributelabelmapping().get(
					elementTypeLDAPKeyMapping.get(etype));
			EnrollmentConfComponentImpl.log.info("KEY:" + key);
			EnrollmentConfComponentImpl.log.info("etype:" + etype);
			EnrollmentConfComponentImpl.log.info("elementTypeLDAPKeyMapping:"
					+ elementTypeLDAPKeyMapping);
			EnrollmentConfComponentImpl.log
					.info("lab.getAttributelabelmapping():"
							+ lab.getAttributelabelmapping());
			while (results.hasMore()) {
				SearchResult searchResult = (SearchResult) results.next();
				Attributes attributes = searchResult.getAttributes();
				Attribute attr = attributes.get(key);
				if (attr.get() != null)
					ldapDataSet.add(((String) attr.get()).toLowerCase());
			}
			String query = "SELECT {0} primarykey FROM DICT_ELEMENTS de,DICT_LEAF_ELEMENTS dle WHERE de.id=dle.element_id AND de.ENROLLMENT_ID=''{1}'' AND dle.TYPE_ID=''{2}''";
			String dictPrimaryKey = null;

			if (enrollmentFieldMapping.get(id) != null
					&& enrollmentFieldMapping.get(id).get(etype) != null
					&& enrollmentFieldMapping.get(id).get(etype)
							.get(elementTypeLDAPKeyMapping.get(etype)) != null)
				dictPrimaryKey = enrollmentFieldMapping.get(id).get(etype)
						.get(elementTypeLDAPKeyMapping.get(etype));
			if (dictPrimaryKey != null) {
				query = MessageFormat.format(query, dictPrimaryKey, id, etype);
				rs = es.getResultSet(query);
				while (rs.next()) {
					dictDataSet.add(rs.getString(1).toLowerCase());
				}
				if (dictDataSet.size() == ldapDataSet.size()) {
					diffDataSet
							.add("All data from activedirectory are enrolled in the dictionary perfectly");
				} else {
					diffDataSet = (TreeSet<String>) SetUtils.difference(
							ldapDataSet, dictDataSet);
				}
			}

		} catch (Exception e) {
			EnrollmentConfComponentImpl.log.error(e.toString(), e);
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (NamingException e) {
					EnrollmentConfComponentImpl.log.error(e.toString(), e);
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					EnrollmentConfComponentImpl.log.error(e.toString(), e);
				}
			}
		}
		sb.append((diffDataSet.toString()));
		return sb.toString();
	}

	public static String getInActiveActiveUsersCount(String enrollment_id) {

		// HashMap<String, String> response = new HashMap<String, String>();

		EnrollmentInfo ei = new EnrollmentInfo();
		ei.setEnrollmentID(enrollment_id);
		ei.setEMCount("0");
		ei.setEMGCount("0");
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder userCountBuilder = new StringBuilder();
		userCountBuilder
				.append("SELECT COUNT(ELEMENT_ID) FROM DICT_ENUM_GROUPS  WHERE ELEMENT_ID IN(SELECT ID FROM DICT_ELEMENTS WHERE  ENROLLMENT_ID='");
		queryBuilder
				.append("SELECT COUNT(*) FROM DICT_ELEMENTS WHERE ENROLLMENT_ID='");
		userCountBuilder.append(enrollment_id);
		queryBuilder.append(enrollment_id);
		queryBuilder.append("'");
		userCountBuilder.append("'");

		queryBuilder.append(" AND ACTIVE_TO < ");
		queryBuilder.append(System.currentTimeMillis());
		userCountBuilder.append(" AND ACTIVE_TO < ");
		userCountBuilder.append(System.currentTimeMillis());

		// queryBuilder.append(") AND TYPE_ID='1'");
		userCountBuilder.append(")");
		EnrollmentConfComponentImpl.log
				.info("---DICTHELPER:getInActiveActiveUsersCount-- query:"
						+ queryBuilder.toString());
		ResultSet userCountSet = es.getResultSet(queryBuilder.toString());
		EnrollmentConfComponentImpl.log
				.info("---DICTHELPER:getInActiveActiveUsersCount-- userCountBuilder:"
						+ userCountBuilder.toString());
		/*
		 * ResultSet userGroupCountSet = es.getResultSet(userCountBuilder
		 * .toString());
		 */
		try {
			while (userCountSet.next()) {
				ei.setEMCount(userCountSet.getString(1));
				// response.put("userCount", userCountSet.getString(1));
			}
			/*
			 * while (userGroupCountSet.next()) {
			 * ei.setEMGCount(userGroupCountSet.getString(1)); //
			 * response.put("userGroupCount",userGroupCountSet.getString(1)); }
			 */
		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log
					.error("----DictHelper.getInActiveActiveUsersCount# SQLException-----\n");
			EnrollmentConfComponentImpl.log.error(e);
		} finally {
			try {
				userCountSet.close();
				/* userGroupCountSet.close(); */
			} catch (SQLException e) {
				EnrollmentConfComponentImpl.log
						.error("----DictHelper.getInActiveActiveUsersCount# SQLException-----\n");
				EnrollmentConfComponentImpl.log.error(e);
			}
		}

		return ei.getEMCount();
	}

	private static ArrayList<String> getEnrollmentIds() {
		String query = "SELECT ID FROM DICT_ENROLLMENTS";
		ArrayList<String> enrollment_ids = new ArrayList<String>();
		ResultSet enrollment_idset = es.getResultSet(query);
		try {
			while (enrollment_idset.next()) {
				enrollment_ids.add(enrollment_idset.getString(1));
				// response.put("userCount", enrollment_idset.getString(1));
			}

		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log
					.error("----DictHelper.getEnrollmentIds# SQLException-----\n");
			EnrollmentConfComponentImpl.log.error(e);
		} finally {
			try {
				enrollment_idset.close();
			} catch (SQLException e) {
				EnrollmentConfComponentImpl.log
						.error("----DictHelper.getEnrollmentIds# SQLException-----\n");
				EnrollmentConfComponentImpl.log.error(e);
			}
		}
		EnrollmentConfComponentImpl.log
				.info("----DictHelper.getEnrollmentIds# enrollment_ids:"
						+ enrollment_ids);
		return enrollment_ids;
	}

	public static String removeInactiveUsers(String enrollmentId) {
		String cachedUserClearanceQuery = "DELETE FROM CACHED_USER WHERE ORIGINAL_ID IN (SELECT DISTINCT(ORIGINAL_ID) FROM DICT_ELEMENTS WHERE ENROLLMENT_ID='"
				+ enrollmentId
				+ "'  AND ACTIVE_TO <"
				+ System.currentTimeMillis() + " )";

		String cachedUserGroupClearanceQuery = "DELETE FROM CACHED_USERGROUP WHERE ORIGINAL_ID IN (SELECT DISTINCT(ORIGINAL_ID) FROM DICT_ELEMENTS WHERE ENROLLMENT_ID='"
				+ enrollmentId
				+ "' AND ACTIVE_TO <"
				+ System.currentTimeMillis() + " )";

		String cachedUserGroupMemberClearanceQuery = "DELETE FROM CACHED_USERGROUP_MEMBER WHERE groupId IN (SELECT DISTINCT(DE.ORIGINAL_ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID='"
				+ enrollmentId
				+ "' AND ACTIVE_TO <"
				+ System.currentTimeMillis() + " )";

		String leafElementsClearanceQuery = "DELETE FROM DICT_LEAF_ELEMENTS WHERE ELEMENT_ID IN (Select DISTINCT(DE.ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID ='"
				+ enrollmentId
				+ "' AND ACTIVE_TO <"
				+ System.currentTimeMillis() + " )";

		String enumGroupClearanceQuery = "DELETE FROM  DICT_ENUM_GROUPS WHERE ELEMENT_ID in (Select DISTINCT(DE.ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID  ='"
				+ enrollmentId
				+ "' AND ACTIVE_TO <"
				+ System.currentTimeMillis() + " )";

		String enumGroupMembersClearanceQuery = "DELETE FROM DICT_ENUM_GROUP_MEMBERS where ENROLLMENT_ID='"
				+ enrollmentId
				+ "' AND ACTIVE_TO <"
				+ System.currentTimeMillis();

		String enumMembersClearanceQuery = "DELETE FROM DICT_ENUM_MEMBERS where ENROLLMENT_ID='"
				+ enrollmentId
				+ "' AND ACTIVE_TO <"
				+ System.currentTimeMillis();

		String structGroupClearanceQuery = "DELETE FROM DICT_STRUCT_GROUPS WHERE ELEMENT_ID in (Select DISTINCT(DE.ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID  ='"
				+ enrollmentId
				+ "' AND ACTIVE_TO <"
				+ System.currentTimeMillis() + " )";

		String elementsClearanceQuery = "DELETE FROM DICT_ELEMENTS  WHERE ENROLLMENT_ID ='"
				+ enrollmentId
				+ "' AND ACTIVE_TO <"
				+ System.currentTimeMillis();

		Statement st = es.getStatement();
		String enrollmentName = "";
		try {
			ResultSet rs = st
					.executeQuery("SELECT domain_name FROM DICT_ENROLLMENTS  where ID='"
							+ enrollmentId + "'");
			while (rs.next()) {
				enrollmentName = rs.getString("domain_name");
				EnrollmentConfComponentImpl.log
						.info("the Enrollment with domain name "
								+ enrollmentName + " is removed");
			}
			st.addBatch(cachedUserClearanceQuery);
			st.addBatch(cachedUserGroupMemberClearanceQuery);
			st.addBatch(cachedUserGroupClearanceQuery);
			st.addBatch(leafElementsClearanceQuery);
			st.addBatch(enumGroupClearanceQuery);
			st.addBatch(enumGroupMembersClearanceQuery);
			st.addBatch(enumMembersClearanceQuery);
			st.addBatch(structGroupClearanceQuery);
			st.addBatch(elementsClearanceQuery);

			st.executeBatch();

		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log
					.error("----DictHelper.removeEnrollment# SQLException-----\n");
			EnrollmentConfComponentImpl.log.error(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				EnrollmentConfComponentImpl.log
						.error("----DictHelper.removeEnrollment# SQLException-----\n");
				EnrollmentConfComponentImpl.log.error(e);
			}
		}
		return enrollmentName;
	}

	public static String removeEnrollment(String enrollmentId) {

		String cachedUserClearanceQuery = "DELETE FROM CACHED_USER WHERE ORIGINAL_ID IN (SELECT DISTINCT(ORIGINAL_ID) FROM DICT_ELEMENTS WHERE ENROLLMENT_ID='"
				+ enrollmentId + "')";

		String cachedUserGroupClearanceQuery = "DELETE FROM CACHED_USERGROUP WHERE ORIGINAL_ID IN (SELECT DISTINCT(ORIGINAL_ID) FROM DICT_ELEMENTS WHERE ENROLLMENT_ID='"
				+ enrollmentId + "')";

		String cachedUserGroupMemberClearanceQuery = "DELETE FROM CACHED_USERGROUP_MEMBER WHERE groupId IN (SELECT DISTINCT(DE.ORIGINAL_ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID='"
				+ enrollmentId + "')";

		String leafElementsClearanceQuery = "DELETE FROM DICT_LEAF_ELEMENTS WHERE ELEMENT_ID IN (Select DISTINCT(DE.ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID ='"
				+ enrollmentId + "')";

		String enumGroupClearanceQuery = "DELETE FROM  DICT_ENUM_GROUPS WHERE ELEMENT_ID in (Select DISTINCT(DE.ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID  ='"
				+ enrollmentId + "')";

		String enumGroupMembersClearanceQuery = "DELETE FROM DICT_ENUM_GROUP_MEMBERS where ENROLLMENT_ID='"
				+ enrollmentId + "'";

		String enumMembersClearanceQuery = "DELETE FROM DICT_ENUM_MEMBERS where ENROLLMENT_ID='"
				+ enrollmentId + "'";

		String enumRefMembersClearanceQuery = "DELETE FROM DICT_ENUM_REF_MEMBERS where ENROLLMENT_ID='"
				+ enrollmentId + "'";

		String structGroupClearanceQuery = "DELETE FROM DICT_STRUCT_GROUPS WHERE ELEMENT_ID in (Select DISTINCT(DE.ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID  ='"
				+ enrollmentId + "')";

		String elementsClearanceQuery = "DELETE FROM DICT_ELEMENTS  WHERE ENROLLMENT_ID ='"
				+ enrollmentId + "'";

		String dictUpdatesClearanceQuery = "DELETE FROM DICT_UPDATES  WHERE ENROLLMENT_ID ='"
				+ enrollmentId + "'";

		String dictFieldMappingClearanceQuery = "DELETE FROM DICT_FIELD_MAPPINGS  WHERE ENROLLMENT_ID ='"
				+ enrollmentId + "'";
		String dictEnrollmentPropertiesClearanceQuery = "DELETE FROM DICT_ENROLLMENT_PROPERTIES  WHERE ENROLLMENT_ID ='"
				+ enrollmentId + "'";

		String dictEnrollmentClearanceQuery = "DELETE FROM DICT_ENROLLMENTS  where ID='"
				+ enrollmentId + "'";

		Statement st = es.getStatement();
		String enrollmentName = "";

		try {
			ResultSet rs = st
					.executeQuery("SELECT domain_name FROM DICT_ENROLLMENTS  where ID='"
							+ enrollmentId + "'");
			while (rs.next()) {
				enrollmentName = rs.getString("domain_name");
				EnrollmentConfComponentImpl.log
						.info("the Enrollment with domain name "
								+ enrollmentName + " is removed");
			}

			st.addBatch(cachedUserClearanceQuery);
			st.addBatch(cachedUserGroupMemberClearanceQuery);
			st.addBatch(cachedUserGroupClearanceQuery);
			st.addBatch(leafElementsClearanceQuery);
			st.addBatch(enumGroupClearanceQuery);
			st.addBatch(enumGroupMembersClearanceQuery);
			st.addBatch(enumMembersClearanceQuery);
			st.addBatch(enumRefMembersClearanceQuery);
			st.addBatch(structGroupClearanceQuery);
			st.addBatch(elementsClearanceQuery);
			st.addBatch(dictUpdatesClearanceQuery);
			st.addBatch(dictFieldMappingClearanceQuery);
			st.addBatch(dictEnrollmentPropertiesClearanceQuery);
			st.addBatch(dictEnrollmentClearanceQuery);
			st.executeBatch();

		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log
					.error("----DictHelper.removeEnrollment# SQLException-----\n");
			EnrollmentConfComponentImpl.log.error(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				EnrollmentConfComponentImpl.log
						.error("----DictHelper.removeEnrollment# SQLException-----\n");
				EnrollmentConfComponentImpl.log.error(e);
			}
		}
		return enrollmentName;

	}
	public static String removeEnrollments(String enrollmentId, Statement st) {

		String cachedUserClearanceQuery = "DELETE FROM CACHED_USER WHERE ORIGINAL_ID IN (SELECT DISTINCT(ORIGINAL_ID) FROM DICT_ELEMENTS WHERE ENROLLMENT_ID='"
				+ enrollmentId + "')";

		String cachedUserGroupClearanceQuery = "DELETE FROM CACHED_USERGROUP WHERE ORIGINAL_ID IN (SELECT DISTINCT(ORIGINAL_ID) FROM DICT_ELEMENTS WHERE ENROLLMENT_ID='"
				+ enrollmentId + "')";

		String cachedUserGroupMemberClearanceQuery = "DELETE FROM CACHED_USERGROUP_MEMBER WHERE groupId IN (SELECT DISTINCT(DE.ORIGINAL_ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID='"
				+ enrollmentId + "')";

		String leafElementsClearanceQuery = "DELETE FROM DICT_LEAF_ELEMENTS WHERE ELEMENT_ID IN (Select DISTINCT(DE.ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID ='"
				+ enrollmentId + "')";

		String enumGroupClearanceQuery = "DELETE FROM  DICT_ENUM_GROUPS WHERE ELEMENT_ID in (Select DISTINCT(DE.ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID  ='"
				+ enrollmentId + "')";

		String enumGroupMembersClearanceQuery = "DELETE FROM DICT_ENUM_GROUP_MEMBERS where ENROLLMENT_ID='"
				+ enrollmentId + "'";

		String enumMembersClearanceQuery = "DELETE FROM DICT_ENUM_MEMBERS where ENROLLMENT_ID='"
				+ enrollmentId + "'";

		String enumRefMembersClearanceQuery = "DELETE FROM DICT_ENUM_REF_MEMBERS where ENROLLMENT_ID='"
				+ enrollmentId + "'";

		String structGroupClearanceQuery = "DELETE FROM DICT_STRUCT_GROUPS WHERE ELEMENT_ID in (Select DISTINCT(DE.ID) FROM DICT_ELEMENTS DE WHERE ENROLLMENT_ID  ='"
				+ enrollmentId + "')";

		String elementsClearanceQuery = "DELETE FROM DICT_ELEMENTS  WHERE ENROLLMENT_ID ='"
				+ enrollmentId + "'";

		String dictUpdatesClearanceQuery = "DELETE FROM DICT_UPDATES  WHERE ENROLLMENT_ID ='"
				+ enrollmentId + "'";

		String dictFieldMappingClearanceQuery = "DELETE FROM DICT_FIELD_MAPPINGS  WHERE ENROLLMENT_ID ='"
				+ enrollmentId + "'";
		String dictEnrollmentPropertiesClearanceQuery = "DELETE FROM DICT_ENROLLMENT_PROPERTIES  WHERE ENROLLMENT_ID ='"
				+ enrollmentId + "'";

		String dictEnrollmentClearanceQuery = "DELETE FROM DICT_ENROLLMENTS  where ID='"
				+ enrollmentId + "'";

		//Statement st = es.getStatement();
		String enrollmentName = "";

		try {
			ResultSet rs = st
					.executeQuery("SELECT domain_name FROM DICT_ENROLLMENTS  where ID='"
							+ enrollmentId + "'");
			while (rs.next()) {
				enrollmentName = rs.getString("domain_name");
		/*		EnrollmentConfComponentImpl.log
						.info("the Enrollment with domain name "
								+ enrollmentName + " is removed");*/
				System.out.println("the Enrollment with domain name "
						+ enrollmentName + " is removed");
			}

			st.addBatch(cachedUserClearanceQuery);
			st.addBatch(cachedUserGroupMemberClearanceQuery);
			st.addBatch(cachedUserGroupClearanceQuery);
			st.addBatch(leafElementsClearanceQuery);
			st.addBatch(enumGroupClearanceQuery);
			st.addBatch(enumGroupMembersClearanceQuery);
			st.addBatch(enumMembersClearanceQuery);
			st.addBatch(enumRefMembersClearanceQuery);
			st.addBatch(structGroupClearanceQuery);
			st.addBatch(elementsClearanceQuery);
			st.addBatch(dictUpdatesClearanceQuery);
			st.addBatch(dictFieldMappingClearanceQuery);
			st.addBatch(dictEnrollmentPropertiesClearanceQuery);
			st.addBatch(dictEnrollmentClearanceQuery);
			st.executeBatch();

		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log
					.error("----DictHelper.removeEnrollment# SQLException-----\n");
			EnrollmentConfComponentImpl.log.error(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				EnrollmentConfComponentImpl.log
						.error("----DictHelper.removeEnrollment# SQLException-----\n");
				EnrollmentConfComponentImpl.log.error(e);
			}
		}
		return enrollmentName;

	}
	public static void main(String args[])
	{
		  try {

	            Class.forName("oracle.jdbc.driver.OracleDriver");

	        } catch (ClassNotFoundException e) {

	            System.out.println("Where is your Oracle JDBC Driver?");
	            e.printStackTrace();
	            return;

	        }

	        System.out.println("Oracle JDBC Driver Registered!");

	        Connection connection = null;

	        try {

	            connection = DriverManager.getConnection(
	                    "jdbc:oracle:thin:@10.23.58.156:1521/genorcl03.qapf1.qalab01.nextlabs.com", "RCI_CC87","123next");
	            
	            System.out.println("Connected");
	            Statement st = connection.createStatement();
	     DictHelper.removeEnrollments("1345", st);
	     
/*	     ResultSet rs = st
					.executeQuery("SELECT * FROM DICT_ENROLLMENTS");
			while (rs.next()) {
		System.out.println(rs.getString("ID"));
		
			}*/
	     
	            

	        } catch (SQLException e) {

	            System.out.println("Connection Failed! Check output console");
	            e.printStackTrace();
	            return;

	        }

		
	}
	
	
}
