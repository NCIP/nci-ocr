<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<s:url var="deleteUrl" action="delete" namespace="/investigator/registration/ajax/subinvestigator" escapeAmp="false">
    <s:param name="registration.id" value="%{registration.id}" />
    <s:param name="subinvestigatorRegistration.id" value="%{subinvestigatorRegistration.id}" />
</s:url>
<firebird:confirmDialog titleKey="registration.subinvestigator.remove.title" confirmUrl="${deleteUrl}" dialogId="registrationDialog">
    <h3>
        <fmt:message key="registration.subinvestigator.remove.message">
            <fmt:param>${subinvestigatorRegistration.profile.person.displayName}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>
