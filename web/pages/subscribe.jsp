<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>

<html:form action="/subscribe" method="post">
	<h2><bean:message key="subscribe.title" /></h2>
	<p>&nbsp;</p>
	<p><bean:message key="subscribe.text" /></p>
	<p>&nbsp;</p>
	
	<table>
		<tr>
			<td><bean:message key="login.name"/>&nbsp;&nbsp;:</td>			
			<td><html:text size="22" property="nameSubs" value=""/></td>
		</tr>	
		<tr>
			<td><bean:message key="login.surname"/>&nbsp;&nbsp;:</td>			
			<td><html:text size="22" property="surnameSubs" value=""/></td>
		</tr>		
		<tr>
			<td><bean:message key="login.email"/>&nbsp;&nbsp;:</td>			
			<td><html:text size="22" property="emailSubs" value=""/></td>
		</tr>
		<tr>
			<td><bean:message key="login.password"/>&nbsp;:</td>
			<td><html:password size="22" property="passwordSubs"/></td>
		</tr>
		<tr>
			<td><bean:message key="login.repassword"/>&nbsp;&nbsp;:</td>			
			<td><html:password size="22" property="repasswordSubs"/></td>
		</tr>
		<tr>
			<td></td>
			<td><html:submit property="submitSubs"><bean:message key="button.subscribe" /></html:submit> <html:reset /></td>
		</tr>
	</table>	
	<p>&nbsp;</p>
</html:form>