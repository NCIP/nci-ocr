<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ attribute name="titleKey" required="true"%>
<%@ attribute name="messageKey" required="true"%>

<div id="errorPage">
    <div id="main">
        <h1><fmt:message key="${titleKey}"/></h1>
        <div class="instructions_wrapper clear">
            <div class="instructions">
                <p>
                    <fmt:message key="${messageKey}" />
                </p>
            </div>
        </div>
    </div>
</div>