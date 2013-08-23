<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="removeDelegateUrl" namespace="/sponsor/representative/ajax" action="removeDelegate">
    <s:param name="sponsorDelegateRoleId" value="%{sponsorDelegateRoleId}" />
</s:url>

<firebird:confirmDialog titleKey="sponsor.remove.delegate.confirm.title" confirmUrl="${removeDelegateUrl}" dialogId="delegateDialog">
    <h3>
        <fmt:message key="sponsor.remove.delegate.confirm.message">
            <fmt:param>${sponsorDelegateRole.sponsor.name}</fmt:param>
            <fmt:param>${sponsorDelegateRole.user.person.displayName}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>