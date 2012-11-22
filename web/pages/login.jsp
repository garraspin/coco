<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>

<html:form action="/login" method="post">
	<h2><bean:message key="login.title" /></h2>
	<p>&nbsp;</p>
	<p><bean:message key="login.text" /></p>
	<p>&nbsp;</p>
	<table width="50%" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td><bean:message key="login.email"/>&nbsp;:</td>			
			
		</tr>
		<tr>
			<td><html:text size="22" property="emailLogin" value="" /></td>
		</tr>
		<tr>
			<td><bean:message key="login.password"/>&nbsp;:</td>
		</tr>
		<tr>
			<td><html:password size="22" property="passwordLogin" value="" /></td>
		</tr>
		<tr>
			<td colspan="2" align="right">
				<html:submit property="submitLogin">
					<bean:message key="button.login" />
				</html:submit>
			</td>			
		</tr>
	</table>	
	<p>&nbsp;</p>
</html:form>