<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<logic:present name="UserContainer">
    <logic:redirect action="initPage" />
</logic:present>

<logic:redirect action="loginPage" />