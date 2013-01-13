<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>

<html:javascript formName="loginForm" />

<html:form action="/login" method="post">
	<h2><bean:message key="login.title" /></h2>
	<p>&nbsp;</p>
	<p><bean:message key="login.text" /></p>
	<p>&nbsp;</p>
	<table>
		<tr>
			<td><bean:message key="login.email"/>:</td>
		</tr>
		<tr>
			<td><html:text size="22" property="emailLogin" /></td>
		</tr>
        <html:messages id="err_email" property="emailLogin">
            <tr>
                <td class="msgError"><bean:write name="err_email"/></td>
            </tr>
        </html:messages>
		<tr>
			<td><bean:message key="login.password"/>:</td>
		</tr>
		<tr>
			<td><html:password size="22" property="passwordLogin" /></td>
		</tr>
        <html:messages id="err_password" property="passwordLogin">
            <tr>
                <td class="msgError"><bean:write name="err_password"/></td>
            </tr>
        </html:messages>
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