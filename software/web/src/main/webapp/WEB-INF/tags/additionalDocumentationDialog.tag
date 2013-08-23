<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<%@ attribute name="titleKey" required="true"%>

<firebird:dialogLayout>
    <firebird:dialogHeader>
        <fmt:message key="${titleKey}"/>
    </firebird:dialogHeader>
    <firebird:messages/>
    <jsp:doBody/>
    <br/>

    <div class="formcol_xthin">
        <br/>
        <s:a id="closeBtn" href="#" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.close"/></s:a>
    </div>
    <div class="clear"></div>
</firebird:dialogLayout>