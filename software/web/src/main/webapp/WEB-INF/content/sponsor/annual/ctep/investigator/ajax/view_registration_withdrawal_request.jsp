<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url action="acceptRequest" var="acceptUrl">
    <s:param name="user.id" value="user.id"/>
</s:url>
<s:url action="enterDenialComments" var="denyUrl">
    <s:param name="user.id" value="user.id"/>
</s:url>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader><fmt:message key="registration.withdrawal.request.title"/></firebird:dialogHeader>
            <fmt:message key="registration.withdrawal.request.message">
                <fmt:param value="${user.person.displayName}"/>
            </fmt:message>
            <firebird:comments titleKey="label.explanation">${user.investigatorRole.withdrawalRequest.explanation}</firebird:comments>
            <br>

            <sj:a id="acceptButton" href="%{acceptUrl}" targets="taskDialog" cssClass="button float_right"><fmt:message key="button.accept"/></sj:a>
            <sj:a id="denyButton" href="%{denyUrl}" targets="taskDialog" cssClass="button float_right marginRight_5"><fmt:message key="button.deny"/></sj:a>
        </div>
    </div>
</div>