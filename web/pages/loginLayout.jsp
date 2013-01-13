<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles" prefix="tiles"%>

<html:html>	
	<head>
		<title>
			<bean:message key="global.title"/>
			<bean:message key="global.version"/>
		</title>
		<html:base/>
		<link href="../static/coco.css" rel="stylesheet" type="text/css">
	</head>	
	<body>
		<div id="header">
			<table width="100%" cellpadding="0" cellspacing="0" class="rounded-upleft">
				<tr>
					<td width="75%">
						<h1><bean:message key="global.title" /></h1>
					</td>
					<td align="center" class="rounded-upright">
						<tiles:insert attribute="flags" />
					</td>
				</tr>
			</table>
		</div>

		<div id="content">
			<div id="columnA">
				<tiles:insert attribute="subscribe" />
			</div>
			<div id="columnB">
				<tiles:insert attribute="login" />
			</div>
			<div style="clear: both;"> </div>
		</div>
		
		<div id="footer">
			<p><bean:message key="login.footer" /></p>
		</div>		
	</body>
</html:html>