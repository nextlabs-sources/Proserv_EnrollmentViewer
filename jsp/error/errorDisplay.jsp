<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles-1.1" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- Note that in order to function properly, this page cannot contain JSF tags --%>

<%-- Load Resource Bundles --%>
<fmt:setBundle basename="UTCConfMessages" var="bundle" />

<%-- Define Home Page Tiles Definition --%>
<tiles:definition id="errorDefinition" extends="UTCConfErrorDisplayDefinition">
  <tiles:put name="applicationTitle">
    <fmt:message key="utc_main_title" var="bundle" />
  </tiles:put>
</tiles:definition>

<%-- Insert Home Definition --%>
<tiles:insert beanName="errorDefinition" flush="false" />