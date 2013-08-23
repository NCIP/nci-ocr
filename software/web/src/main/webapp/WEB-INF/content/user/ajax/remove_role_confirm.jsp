<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/user/ajax" action="removeRole" var="removeRoleUrl">
    <s:param name="role" value="%{role}" />
</s:url>
<firebird:confirmDialog titleKey="user.role.remove.confirm.title" confirmUrl="${removeRoleUrl}" dialogId="roleRemovalDialog">
    <h3>
        <fmt:message key="user.role.remove.confirm.text">
            <fmt:param>${role.display}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>