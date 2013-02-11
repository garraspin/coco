<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<ul>
	<logic:notEqual name="UserContainer" property="listCOCO.actualCOCO.inCOCO.id" value="-1">
		<bean:define id="inProblem" name="UserContainer" 
			property="listCOCO.actualCOCO.inCOCO" type="com.coco.vo.InputVO" />
		<li>
			<html:link action="/showProblem.do?operation=showInput" paramId="id" 
				paramProperty="id" paramName="inProblem">
		  		<bean:message key="input.title"/>
		  	</html:link>
	  	</li>
	  	<li>
	  		<html:link action="downloadXLS"><bean:message key="xls.out"/></html:link>
  		</li>		
	</logic:notEqual>
	
	<logic:equal name="UserContainer" property="listCOCO.actualCOCO.inCOCO.id" value="-1">
		<li>
			<html:link action="/initPage.do"><bean:message key="input.title"/></html:link>
	  	</li>
	  	<li>
		  	<a><bean:message key="xls.out"/></a>
	  	</li>
  	</logic:equal>

	<li style="float: right">			
		<html:link action="/logout"><bean:message key="button.logout"/></html:link>		
	</li>
</ul>