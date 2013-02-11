<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean"%>

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
        <h1><html:errors /></h1>
    </body>
</html:html>