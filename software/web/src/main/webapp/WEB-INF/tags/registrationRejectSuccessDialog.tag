<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages />
            <firebird:dialogHeader>
                <fmt:message key="sponsor.protocol.forms.complete.success.title" />
            </firebird:dialogHeader>
            <p><fmt:message key="sponsor.protocol.forms.complete.success.message" /></p>

            <div class="btn_bar clear">
                <s:a href="#" id="closeBtn" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.close"/></s:a>
            </div>
        </div>
    </div>
</div>
