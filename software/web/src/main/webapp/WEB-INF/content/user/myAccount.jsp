<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:set var="isInvestigator" value="%{currentUser.investigator}" />
<s:set var="isRegistrationCoordinator" value="%{currentUser.registrationCoordinator}" />
<s:set var="isSponsorDelegate" value="%{currentUser.sponsorDelegate}" />
<s:set var="isSponsorRepresentative" value="%{currentUser.sponsorRepresentative}" />

<h1>
    <fmt:message key="user.my.account.title" />
</h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages />
            <sj:dialog id="roleRemovalDialog" autoOpen="false" modal="true" width="950" position="top" onCloseTopics="dialogClose" resizable="false" onOpenTopics="dialogOpened" />

            <h2>
                <fmt:message key="user.registration.verification.person.label" />
            </h2>
            <firebird:viewPerson person="${currentUser.person}" showProviderNumber="true" showCtepId="true" ctepIdLabelKey="label.nci.investigator.number"/>

            <s:if test="%{currentUser.investigator}">
                <firebird:label messageKey="label.nci.investigator.status" bold="true"/>
                <div id="investigatorStatus">${currentUser.investigatorRole.status.display}</div>
            </s:if>

            <div class="clear">
                <br />
            </div>
            <h2>
                <fmt:message key="user.registration.verification.selected.roles.label" />
                <s:if test="%{currentUser.roles.size() != @gov.nih.nci.firebird.data.user.UserRoleType@values().length}">
                    <s:a id="addNewRoles" action="enterAddRolesFlow" namespace="/user/registration" cssClass="edit">
                        <fmt:message key="button.addNew"/>
                    </s:a>
                </s:if>
            </h2>
            <ul id="selectedRoles">
                <s:iterator value="currentUser.roles" var="role">
                    <li>
                        <span style="float:left; width:12em"><s:property value="display"/></span>
                        <s:if test="removable">
                            <s:url namespace="/user/ajax" action="enterRemoveRole" var="removeRoleUrl">
                                <s:param name="role" value="%{role}" />
                            </s:url>
                            <span>
                                <sj:a openDialog="roleRemovalDialog" href="%{#removeRoleUrl}" id="removeRoleLink_%{role}"><img class="delete" alt="Delete" src="<c:url value="/images/ico_delete.gif"/>" /></sj:a>
                            </span>
                        </s:if>
                        <br/>
                    </li>
                </s:iterator>
            </ul>
            <div class="clear">
                <br />
            </div>

            <s:if test="#isInvestigator">
                <h2>
                    <fmt:message key="user.registration.verification.primary.organization.label" />
                </h2>
                <firebird:organizationDisplay organization="${currentUser.investigatorRole.profile.primaryOrganization.organization}" />
                <div class="clear">
                    <br />
                </div>
            </s:if>

            <s:if test="#isSponsorRepresentative">
                <div id="sponsorRepresentativesSection">
                    <h2>
                        <fmt:message key="user.registration.verification.sponsor.organizations.label" />
                    </h2>
                    <s:iterator value="currentUser.sponsorRepresentativeOrganizations" var="org" status="orgStatus">
                        <firebird:sponsorSectionDisplay organization="${org}" displayLine="${!orgStatus.last}"/>
                    </s:iterator>
                </div>
            </s:if>

            <s:if test="#isSponsorDelegate">
                <div id="sponsorDelegatesSection">
                    <h2>
                        <fmt:message key="user.registration.verification.sponsor.delegates.label" />
                    </h2>
                    <s:iterator value="currentUser.sponsorDelegateOrganizations" var="org" status="orgStatus">
                        <firebird:sponsorSectionDisplay organization="${org}" displayLine="${!orgStatus.last}"/>
                    </s:iterator>
                </div>
            </s:if>
            <div class="clear">
                <br />
            </div>
        </div>
    </div>

    <script type="text/javascript">
        function showDialog() {
            $('#roleRemovalDialog').show();
        }
    </script>
</div>