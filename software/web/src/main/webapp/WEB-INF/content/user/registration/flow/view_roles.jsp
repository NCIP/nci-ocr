<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h1><fmt:message key="user.registration.view.roles.title"/></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:instructionBubble messageKey="user.registration.view.roles.text"/>
            <s:form id="viewRolesForm" action="nextStep">
                <ul>
                    <s:iterator value="roleNames" var="roleName">
                        <li><div class="boldHeader"><s:property value="roleName"/></div></li>
                    </s:iterator>
                </ul>
            </s:form>
            <div class="btn_bar">
                <firebird:userRegistrationNextButton displayButtonAlways="true"/>
            </div>
        </div>
    </div>
</div>
