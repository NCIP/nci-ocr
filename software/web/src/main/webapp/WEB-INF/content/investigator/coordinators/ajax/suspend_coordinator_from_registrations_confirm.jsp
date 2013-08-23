<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="suspendCoordinatorFromRegistrationsUrl" action="suspendCoordinatorFromRegistrations"
    namespace="/investigator/coordinators/ajax">
    <s:param name="managedInvestigatorId" value="%{managedInvestigatorId}" />
</s:url>

<firebird:confirmDialog titleKey="investigator.coordinators.suspend.confirm.title"
    confirmUrl="${suspendCoordinatorFromRegistrationsUrl}" dialogId="coordinatorDialog">
    <h3>
        <fmt:message key="investigator.coordinators.suspend.registrations.confirm.message">
            <fmt:param>${managedInvestigator.user.person.displayName}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>