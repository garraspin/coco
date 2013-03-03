<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<ul>
	<li>
		<h2><bean:message key="output.menuTitle"/></h2>
	</li>	
	
	<jsp:include flush="true" page="/pages/cocoListOut.jsp" />
</ul>