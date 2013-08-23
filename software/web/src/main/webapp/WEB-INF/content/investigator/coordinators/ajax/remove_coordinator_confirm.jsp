<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="removeCoordinatorUrl" namespace="/investigator/coordinators/ajax" action="removeCoordinator">
    <s:param name="managedInvestigatorId" value="%{managedInvestigatorId}" />
</s:url>

<firebird:confirmDialog titleKey="investigator.coordinators.remove.confirm.title" confirmUrl="${removeCoordinatorUrl}" dialogId="coordinatorDialog">
    <h3>
        <fmt:message key="investigator.coordinators.remove.confirm.message">
            <fmt:param>${managedInvestigator.user.person.displayName}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>