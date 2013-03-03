<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>

<html:javascript formName="subscribeForm" />

<html:form action="/subscribe" method="post">
	<h2><bean:message key="subscribe.title" /></h2>
	<h4><bean:message key="subscribe.text" /></h4>

	<table>
		<tr>
			<td><bean:message key="login.name" />:</td>
			<td><html:text property="nameSubs" /></td>
            <html:messages id="err_name" property="nameSubs">
                <td class="msgError"><bean:write name="err_name" /></td>
            </html:messages>
		</tr>
		<tr>
			<td><bean:message key="login.surname" />:</td>
			<td><html:text property="surnameSubs" /></td>
            <html:messages id="err_surname" property="surnameSubs">
                <td class="msgError"><bean:write name="err_surname" /></td>
            </html:messages>
		</tr>
		<tr>
			<td><bean:message key="login.email" />:</td>
			<td><html:text property="emailSubs" /></td>
            <html:messages id="err_email" property="emailSubs">
                <td class="msgError"><bean:write name="err_email" /></td>
            </html:messages>
		</tr>
		<tr>
			<td><bean:message key="login.password" />:</td>
			<td><html:password property="passwordSubs" /></td>
            <html:messages id="err_password" property="passwordSubs">
                <td class="msgError"><bean:write name="err_password" /></td>
            </html:messages>
		</tr>
		<tr>
			<td><bean:message key="login.repassword" />:</td>
			<td><html:password property="repasswordSubs" /></td>
            <html:messages id="err_repassword" property="repasswordSubs">
                <td class="msgError"><bean:write name="err_repassword" /></td>
            </html:messages>
		</tr>
		<tr>
			<td></td>
			<td><html:submit property="submitSubs"><bean:message key="button.subscribe" /></html:submit>
                <html:reset />
            </td>
		</tr>
	</table>
	<p>&nbsp;</p>
</html:form>