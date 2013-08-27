<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:messages/>
<h2>
    <fmt:message key="investigator.profile.contactInformation" />
    <s:url var="personEditUrl" namespace="/investigator/profile/contact/ajax" action="managePersonAjaxEnter">
        <s:param name="profile.id" value="%{profile.id}" />
    </s:url>

    <sj:a id="editPerson" openDialog="profileDialog" href="%{personEditUrl}" cssClass="edit">
        <fmt:message key="button.edit" />
    </sj:a>
</h2>
<s:if test="profile.person.updatePending">
    <div id="pendingUpdateMessage" class="pageAlert">
        <fmt:message key="profile.pending.person.updates.message">
            <fmt:param value="${profile.person.updateRequested}"/>
        </fmt:message>
    </div>
</s:if>
<firebird:viewPerson person="${profile.person}" showProviderNumber="true" showCtepId="true" ctepIdLabelKey="label.nci.investigator.number"/>

<firebird:label messageKey="label.nci.investigator.status" bold="true" />
<div id="investigatorStatus">${profile.user.investigatorRole.status.display}</div>

<div class="clear">
    <br>
</div>

<h2>
    <fmt:message key="investigator.profile.contactInformation.organization" />

    <s:url var="orgSearchUrl" namespace="/investigator/profile/contact/ajax" action="organizationSearchAgain">
        <s:param name="profile.id" value="%{profile.id}" />
    </s:url>
    <sj:a id="queryPrimaryOrg" openDialog="profileDialog" href="%{orgSearchUrl}" cssClass="search">
        <fmt:message key="button.searchAgain" />
    </sj:a>
</h2>

<firebird:organizationDisplay organization="${profile.primaryOrganization.organization}"/>

<div class="clear">
    <br>
</div>
