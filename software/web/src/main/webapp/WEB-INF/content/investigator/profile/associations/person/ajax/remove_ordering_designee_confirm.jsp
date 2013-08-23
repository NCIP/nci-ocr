<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="removeOrderingDesigneeUrl" namespace="/investigator/profile/associations/person/ajax" action="removeOrderingDesignee" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}" />
    <s:param name="personAssociationId" value="%{personAssociationId}" />
</s:url>

<firebird:confirmDialog titleKey="remove.ordering.designee.confirm.title" confirmUrl="${removeOrderingDesigneeUrl}" dialogId="profileDialog">
    <h3>
        <fmt:message key="remove.ordering.designee.confirm.message">
            <fmt:param>${personAssociation.person.displayName}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>
