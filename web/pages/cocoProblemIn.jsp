<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested" prefix="nested"%>


<nested:form action="/saveProblem.do?operation=save" method="post" enctype="multipart/form-data">
    <%! private final java.text.DecimalFormat df = new java.text.DecimalFormat("###.####"); %>

    <html:errors />

	<nested:nest property="inCOCO">
		<nested:hidden property="id" />

		<table id="rounded-corner">
			<thead>
				<!-- 1. title -->
				<tr>
					<th class="rounded-upleft">
						<bean:message key="coco.title" />
					</th>
					<th class="rounded-upright" scope="col"></th>
				</tr>
			</thead>
			<tbody>
				<!-- 2. coco problem name and description -->
				<tr>
					<td><bean:message key="coco.name" /></td>
					<td><nested:text property="name" /></td>
				</tr>
				<tr>
					<td><bean:message key="coco.description" /></td>
					<td><nested:text property="description" /></td>
				</tr>
				<!-- 6. idFunction -->
				<tr>
					<td><bean:message key="coco.functionTitle" /></td>
					<td>
                        <nested:select property="idFunction">
						    <html:optionsCollection value="id" name="UserContainer" property="functionsList" label="name" />
					    </nested:select>
                    </td>
				</tr>
				<!-- 7. negativeAllowed -->
				<tr>
					<td><bean:message key="coco.negativeAllowed" /></td>
					<td><nested:checkbox property="negativeAllowed" /></td>
				</tr>
			</tbody>
			<tfoot>
				<!-- 8. equilibrium -->
				<tr>
					<td class="rounded-foot-left"><bean:message key="coco.equilibrium" /></td>
					<td class="rounded-foot-right"><nested:text property="equilibrium" /></td>
				</tr>
			</tfoot>
		</table>

		<table id="rounded-corner">
			<thead>
				<!-- 3. matrix title -->
				<tr>
					<nested:size id="numAttributes" property="attributes"/>
					<th class="rounded-upleft" colspan="<%= numAttributes + 2 %>">
						<bean:message key="coco.matrix.initial" />
					</th>
					<th class="rounded-upright"></th>
				</tr>
			</thead>
			<tbody>
				<!-- 4. elementsAttributeTitle , attribute names , YAttribute name -->
				<tr>
					<!-- first row for attribute names -->
					<td><bean:message key="coco.elementAttributeTitle" /></td>
					<nested:iterate property="attributes" type="com.coco.vo.AttributeVO">
						<td><nested:text size="10" property="name" /></td>
						<nested:hidden property="id" />
					</nested:iterate>
					<td>
						<html:link action="/saveProblem.do?operation=alterRowCol&amp;col=1&amp;row=0">
						<html:img align="left" src="../static/plus.gif" border="0" height="30" />
						</html:link>
						<html:link action="/saveProblem.do?operation=alterRowCol&amp;col=-1&amp;row=0">
						<html:img src="../static/minus.gif" border="0" height="30" />
						</html:link>
					</td>
					<td><nested:text size="10" property="attributeY" /></td>
				</tr>
				<!-- Element names , elementAttributesValues and YValues -->
				<nested:iterate property="elements" indexId="idElements" type="com.coco.vo.ElementVO" id="element">
					<tr>
						<td><nested:text size="10" property="name" /></td>
						
						<nested:hidden property="id" />
						
						<nested:iterate property="cells" type="com.coco.vo.CellVO" id="cell">
							<td><nested:text size="10" property="value" value="<%= df.format(cell.getValue()) %>" /></td>
						</nested:iterate>

                        <td>&nbsp;</td>

						<td><nested:text size="10" property="yvalue" value="<%= df.format(element.getYvalue()) %>" /></td>
					</tr>
				</nested:iterate>
				<tr>
					<td>
						<html:link action="/saveProblem.do?operation=alterRowCol&amp;col=0&amp;row=1">
						<html:img align="left" src="../static/plus.gif" border="0" height="30" />
						</html:link>
						<html:link action="/saveProblem.do?operation=alterRowCol&amp;col=0&amp;row=-1">
						<html:img src="../static/minus.gif" border="0" height="30" />
						</html:link>
					</td>
					<td colspan="<%= numAttributes + 2 %>">&nbsp;</td>
				</tr>

				<!-- 8. rankRules and Optimal values -->
				<tr>
					<td><bean:message key="coco.optimalTitle" /></td>
					<nested:iterate property="attributes" type="com.coco.vo.AttributeVO" id="attribute">
						<td><nested:text size="10" property="optima" value="<%= df.format(attribute.getOptima()) %>" /></td>
					</nested:iterate>
					<td colspan="2"></td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td class="rounded-foot-left"><bean:message key="coco.rankRuleTitle" /></td>
					
					<nested:iterate property="attributes">
					<td>
						<nested:select property="rankRule">
						<html:optionsCollection value="id" name="UserContainer"
							property="rankRulesList" label="name" />
						</nested:select>
					</td>
					</nested:iterate>
					
					<td colspan="2" class="rounded-foot-right"></td>
				</tr>
			</tfoot>
		</table>

		<html:submit property="submitCreate">
			<nested:equal property="id" value="-1">
				<bean:message key="coco.submitCreate" />
			</nested:equal>
			<nested:notEqual property="id" value="-1">
				<bean:message key="coco.submitModify" />
			</nested:notEqual>
		</html:submit>
	</nested:nest>
</nested:form>