<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested" prefix="nested"%>

<%@page import="com.coco.vo.ElementVO"%>

<nested:form action="/saveProblem.do?operation=save" method="post" enctype="multipart/form-data">
	<%! private final java.text.DecimalFormat df = new java.text.DecimalFormat("###.####"); %>

	<nested:size id="numAttributes" property="inCOCO.attributes"/>
	
	<table id="rounded-corner">
	<thead>
		<tr>
			<th class="rounded-upleft" colspan="<%= numAttributes + 1 %>">
				<bean:message key="coco.matrix.ranking" />
			</th>
			
			<th class="rounded-upright"></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><bean:message key="coco.elementAttributeTitle" /></td>
			
			<nested:iterate property="inCOCO.attributes" type="com.coco.vo.AttributeVO">
				<td><nested:write property="name" /></td>
			</nested:iterate>
			
			<td><bean:message key="coco.ranking.total" /></td>
		</tr>
		<!-- 2. Loop for elements ranking -->
		<nested:iterate property="inCOCO.elements" type="com.coco.vo.ElementVO">
			<tr>
				<td><nested:write property="name" /></td>
				
				<nested:iterate property="cells" type="com.coco.vo.CellVO">
					<td><nested:write property="ranking" /></td>
				</nested:iterate>
				
				<td><nested:write property="totalRanking" /></td>
			</tr>
		</nested:iterate>
	</tbody>
	<tfoot>
		<tr>
			<td class="rounded-foot-left" colspan="<%= numAttributes + 1 %>"></td>
			<td class="rounded-foot-right"></td>
		</tr>
	</tfoot>
	</table>
	
	<table id="rounded-corner">
	<thead>
		<tr>
			<th class="rounded-upleft" colspan="<%= numAttributes + 1 %>">
				<bean:message key="coco.matrix.idealvalue" />
			</th>
			
			<th class="rounded-upright"></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><bean:message key="coco.elementAttributeTitle" /></td>
			
			<nested:iterate property="inCOCO.attributes" type="com.coco.vo.AttributeVO">
				<td><nested:write property="name" /></td>
			</nested:iterate>
			
			<td class="rounded-upright" scope="col">
				<bean:message key="coco.idealvalue.total" />
			</td>
		</tr>
		<!-- 4. Loop for elements ideal values -->
		<nested:iterate property="inCOCO.elements" type="com.coco.vo.ElementVO" id="element">
			<tr>
				<td><nested:write property="name" /></td>
				
				<nested:iterate property="cells" type="com.coco.vo.CellVO" id="cell">
					<td><%= df.format(cell.getIdealValue()) %></td>
				</nested:iterate>
				
				<td><%= df.format(element.getTotalIdealValue()) %></td>
			</tr>
		</nested:iterate>
	</tbody>
	<tfoot>
		<tr>
			<td class="rounded-foot-left" colspan="<%= numAttributes + 1 %>"></td>
			<td class="rounded-foot-right"></td>
		</tr>
	</tfoot>
	</table>
	
	<table id="rounded-corner">
	<thead>
		<tr>
			<th class="rounded-upleft" colspan="<%= numAttributes + 1 %>"><bean:message key="coco.matrix.results" /></th>
		
			<th class="rounded-upright"></th>
		</tr>
	</thead>
	<tbody>
		<!-- 1. Attributes title -->
		<tr>
			<td></td>
			
			<nested:iterate property="inCOCO.attributes" type="com.coco.vo.AttributeVO">
				<td><nested:write property="name" /></td>
			</nested:iterate>
			
			<td></td>
		</tr>
	
		<!-- 5. Importance list -->
		<tr>
			<td><bean:message key="coco.importantlist.title" /></td>
			
			<nested:iterate property="outCOCO.importanceObjects" 
			id="importantObj" type="java.lang.Double">
				<td><%= df.format(importantObj) %></td>
			</nested:iterate>
			
			<td></td>
		</tr>
		<!-- 6. Sensitivity list -->
		<tr>
			<td><bean:message key="coco.sensitivitylist.title" /></td>
			
			<nested:iterate property="outCOCO.sensitivityObjects" 
			id="sensitivityObj" type="java.lang.Double">
				<td><%= df.format(sensitivityObj) %></td>
			</nested:iterate>
			
			<td></td>
		</tr>
		<!-- 7. Best objects -->
		<tr>
			<td><bean:message key="coco.bestobjectslist.title" /></td>
			
			<nested:define id="elements" 
				property="inCOCO.elements" type="java.util.List" />
			
			<nested:iterate property="outCOCO.bestObjects" 
				id="bestObj" type="java.lang.Integer">
				<td>
					<% 
						String elementName;
						if (elements.get(bestObj) != null) {
							elementName = 
								((ElementVO)elements.get(bestObj)).getName();
						} else {
							elementName = "NULL";
						}
					%>
					<%= elementName %>
				</td>
			</nested:iterate>
			
			<td></td>
		</tr>
	</tbody>
	<tfoot>
		<!-- 8. Solution -->
		<tr>
			<td colspan="<%= numAttributes + 2 %>"></td>
		</tr>
		<tr>
			<td class="rounded-foot-left"><bean:message key="coco.solution" /></td>
			
			<td><nested:write property="outCOCO.solution" /></td>
			
			<td class="rounded-foot-right" colspan="<%= numAttributes %>"></td>
		</tr>
	</tfoot>
	</table>
</nested:form>