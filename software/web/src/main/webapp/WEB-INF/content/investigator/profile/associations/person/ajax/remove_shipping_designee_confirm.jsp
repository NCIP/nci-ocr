<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="removeShippingDesigneeUrl" namespace="/investigator/profile/associations/person/ajax" action="removeShippingDesignee" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}" />
    <s:param name="personAssociationId" value="%{personAssociationId}" />
</s:url>

<firebird:confirmDialog titleKey="remove.shipping.designee.confirm.title" confirmUrl="${removeShippingDesigneeUrl}" dialogId="profileDialog">
    <h3>
        <fmt:message key="remove.shipping.designee.confirm.message">
            <fmt:param>${personAssociation.person.displayName}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>
