<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="unsuspendCoordinatorFromRegistrationsUrl" action="unsuspendCoordinatorFromRegistrations"
    namespace="/investigator/coordinators/ajax">
    <s:param name="managedInvestigatorId" value="%{managedInvestigatorId}" />
</s:url>

<firebird:confirmDialog titleKey="investigator.coordinators.unsuspend.confirm.title"
    confirmUrl="${unsuspendCoordinatorFromRegistrationsUrl}" dialogId="coordinatorDialog">
    <h3>
        <fmt:message key="investigator.coordinators.unsuspend.registrations.confirm.message">
            <fmt:param>${managedInvestigator.user.person.displayName}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>