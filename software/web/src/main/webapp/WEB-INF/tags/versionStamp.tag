<%@ tag language="java" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<s:set var="ocrBuildDate" value="%{getText('ocr.build.timestamp')}"/>
<s:set var="ocrBuildVersion" value="%{getText('ocr.build.version')}"/>
<!-- Project Build Number ${ocrBuildVersion} -->
<!-- Project Build Time ${ocrBuildDate} -->
<div id="versionnumber">
    Version: <c:out value="${fn:substring(ocrBuildVersion, 0, 3)}"/>
</div>