<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:signedDocumentsDialog>
    <fmt:message key="registration.submitted.message">
        <fmt:param value="${registration.profile.person.displayName}" />
        <fmt:param value="${registration.protocol.protocolNumber}" />
    </fmt:message>
</firebird:signedDocumentsDialog>