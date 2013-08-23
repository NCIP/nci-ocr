<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ attribute name="messageKey" required="true" %>
<%@ attribute name="messageParam"%>
<%@ attribute name="id"%>
<%@ attribute name="hasIndicator" type="java.lang.Boolean" %>

<s:set var="footer" value="%{#attr.hasIndicator == null || #attr.hasIndicator ? 'instructions_wrapper_footer' : ''}"/>


<div class="instructions_wrapper clear ${footer}" id="${id}">
    <div class="instructions">
        <p>
            <fmt:message key="${messageKey}" >
                <s:if test="%{#attr.messageParam not in #{null,''}}">
                    <fmt:param value="${messageParam}"/>
                </s:if>
            </fmt:message>
        </p>
    </div>
</div>
