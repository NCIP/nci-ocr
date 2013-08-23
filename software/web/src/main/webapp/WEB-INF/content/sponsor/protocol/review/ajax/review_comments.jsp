<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:registrationRejectionCommentsDialog>
    <fmt:message key="sponsor.protocol.forms.complete.comment.header">
        <fmt:param value="${registration.profile.person.displayName}" />
        <fmt:param value="${registration.protocol.protocolNumber}" />
    </fmt:message>
</firebird:registrationRejectionCommentsDialog>