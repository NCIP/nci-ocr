<%@ tag body-content="empty" %>
<%@ attribute name="breakCount" %>
<%@ attribute name="escape" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="messages">
    <div class="errors">
        <s:actionerror cssClass="fielderror"/>
    </div>
    <s:if test="%{!actionMessages.empty}">
        <div class="topconfirm">
            <s:iterator value="%{actionMessages}">
                <c:choose>
                    <c:when test="${escape == 'false'}">
                        <p><s:property value="%{toString()}" escape="false"/></p>
                    </c:when>
                    <c:otherwise>
                        <p><s:property value="%{toString()}" escape="true"/></p>
                    </c:otherwise>
                </c:choose>
            </s:iterator>
        </div>
    </s:if>
</div>
