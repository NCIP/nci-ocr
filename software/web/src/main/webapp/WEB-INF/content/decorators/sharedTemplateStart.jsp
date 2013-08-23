<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/WEB-INF/content/common/taglibs.jsp" %>
<%
    request.setAttribute("decorated", Boolean.TRUE);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title><fmt:message key="firebird.title" /> - <decorator:title default="Welcome"/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=7" /> 
        <%@ include file="/WEB-INF/content/common/headIncludes.jsp" %>
        <decorator:head/>
    </head>
    <body>
        <%@ include file="/WEB-INF/content/common/nciHeader.jsp" %>
        <%@ include file="/WEB-INF/content/common/timeout.jsp" %>
        <!--NCI OCR--> 
        <div id="wrapper_outer"> 
            <div id="wrapper_inner">
