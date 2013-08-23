<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
<head>
    <title><fmt:message key="login.title" /></title>
</head>
<body onload="setFocusToFirstControl();">
<div class="blank_space"></div>
<div class="blank_space"></div>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>

            <form action="j_security_check" method="post" id="loginForm" onsubmit="indicateLoading(); return true;">
                <c:if test="${not empty param.failedLogin}">
                    <div class="messages fielderror">
                        ${failedLoginMessage}
                    </div>
                </c:if>

                <firebird:instructionBubble messageKey="login.instructions" hasIndicator="false"/>

                <label for="username"><fmt:message key="login.username"/></label>
                <input type="text" name="j_username" id="username" size="15" maxlength="100" style="width:175px" />

                <label for="password"><fmt:message key="login.password"/></label>
                <input type="password" name="j_password" id="password" size="15" maxlength="100" style="width:175px" />

                <label for="idpUrl"><fmt:message key="login.idp"/></label>
                <s:select id="idpUrl" name="idpUrl" list="%{idps}" listValue="displayName" listKey="authenticationServiceURL" />

                <br />
                <fmt:message key="login.submit" var="submitLabel"/>
                <input type="submit" value="${submitLabel}" name="submitLogin" id="submitLogin" class="btn" />
            </form>
            <div class="clear"></div>
            <br/>
            <s:a namespace="/user" action="requestAccountEnter" id="requestAccount"><fmt:message key="request.account" /></s:a>
        </div>
    </div>
</div>
</body>
</html>