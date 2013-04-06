<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<logic:lessEqual scope="session" name="UserContainer" property="listCOCO.size" value="0">
	<bean:message key="cocoList.null" />
</logic:lessEqual>

<logic:greaterThan scope="session" name="UserContainer" property="listCOCO.size" value="0">
	<logic:iterate id="problems" name="UserContainer" property="listCOCO.problems">
		<li>
			<table width="100%">
				<tr>
					<td width="75%">
						<html:link action="/showProblem.do?operation=showOutput" paramId="id" paramName="problems" paramProperty="id">
							<bean:write name="problems" property="name" />
						</html:link>
					</td>
					<td align="center">
						<html:link action="/deleteProblem.do" paramId="id" paramName="problems" paramProperty="id">
							<html:img src="../static/eliminar.gif" titleKey="cocoList.delete" />
						</html:link>
					</td>
				</tr>
			</table>
		</li>
	</logic:iterate>
</logic:greaterThan>