<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles" prefix="tiles"%>

<html:html>
	<head>
		<title>
			<bean:message key="global.title" /> 
			<bean:message key="global.version" />
		</title>
		<html:base />
		<link href="../../images/coco.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<div id="header">
			<tiles:insert attribute="header" />
		</div>
		<div id="menu">
				<tiles:insert attribute="menu" />
		</div>
		<div id="content">
			<div id="columnA">
				<tiles:insert attribute="body" />
			</div>
			<div id="columnB">
				<tiles:insert attribute="sidebar" />
			</div>
			<div style="clear: both;"> </div>
		</div>
		<div id="footer">
			<tiles:insert attribute="footer" />
		</div>
	</body>
</html:html>