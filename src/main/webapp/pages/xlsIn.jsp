<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<html:form action="/loadXLS.do" method="post" enctype="multipart/form-data">
	<table>
		<tr>
			<td align="center" colspan="2">
				<span style="font-size: medium; ">Please Enter the	Following Details</span>
			</td>
		</tr>
		<tr>
			<td align="left" colspan="2">
				<span style="color: red; "><html:errors /></span>
			</td>
		</tr>
		<tr>
			<td align="right">File Name</td>
			<td align="left">
				<html:file property="file" accept="application/vnd.ms-excel"/>
			</td>
		</tr>
		<tr>
			<td align="center" colspan="2">
				<html:submit>Upload File</html:submit>
			</td>
		</tr>
	</table>
</html:form>