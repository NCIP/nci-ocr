<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/user" action="enterMyAccount" var="myAccountUrl" />
<script type="text/javascript">
    var userMenu = {
        divclass : 'anylinkmenu',
        inlinestyle : '',
        linktarget : ''
    };
    userMenu.items = [ [ "<fmt:message key='menu.item.user.my.account'/>",
            "${myAccountUrl}" ] ]
</script>
<!--User Area-->
<div id="user_area">

    <c:choose>
        <c:when test="${empty pageContext.request.remoteUser}">
            <s:a namespace="/" action="index" id="signinButton" cssClass="button btn_signin" title="Sign In"><fmt:message key="button.sign.in"/></s:a>
        </c:when>
        <c:otherwise>
            <s:a namespace="/login" action="logout" id="signoutButton" cssClass="button btn_signout" title="Sign Out"><fmt:message key="button.sign.out"/></s:a>
            <s:if test="%{currentUser.person != null}">
                    <span class="user_icon"><fmt:message key="user.welcome.message" />
                    <s:a href="%{myAccountUrl}" cssClass="menuanchorclass" id="myAccountLink" rel="userMenu">
                        <span class="arrow_down"><strong>${currentUser.person.displayName}</strong></span>
                    </s:a>
                </span>
            </s:if>
        </c:otherwise>
    </c:choose>
</div>
<!--/User Area-->
