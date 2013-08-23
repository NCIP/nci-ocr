<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<%@ attribute name="titleKey" required="true"%>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>

            <firebird:dialogHeader>
                <fmt:message key="${titleKey}" />
            </firebird:dialogHeader>

            <firebird:instructionBubble messageKey="credentials.delete.message" messageParam="${credential.type.display}"/>

            <jsp:doBody />
        </div>
    </div>
</div>
