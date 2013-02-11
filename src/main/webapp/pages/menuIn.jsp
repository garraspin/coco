<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>

<ul>
  <li>		
	<html:link action="/createProblem"><bean:message key="button.create"/></html:link>		
  </li>

  <li>
	<logic:notEqual name="UserContainer" property="listCOCO.actualCOCO" value="-1">
		<bean:define id="inProblem" name="UserContainer" property="listCOCO.actualCOCO.inCOCO" type="com.coco.vo.InputVO" />
	
		<html:link action="/showProblem.do?operation=showOutput" paramId="id" paramProperty="id" paramName="inProblem">
	  		<bean:message key="output.title"/>
	  	</html:link>
	</logic:notEqual>
	
	<logic:equal name="UserContainer" property="listCOCO.actualCOCO" value="-1">
		<html:link action="/initOutPage.do"><bean:message key="output.title"/></html:link>
  	</logic:equal>
  </li>
  
  <li>
	<html:link action="xlsIn"><bean:message key="xls.in"/></html:link>
  </li>
  
  <li style="float: right">			
	<html:link action="/logout"><bean:message key="button.logout"/></html:link>		
  </li>
</ul>