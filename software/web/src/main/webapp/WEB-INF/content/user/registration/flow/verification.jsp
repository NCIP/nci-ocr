<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<sj:dialog id="functionalityWarningDialog"
    autoOpen="false"
    modal="true"
    width="920"
    position="top"
    resizable="false"
    onCloseTopics="dialogClosed"
    onOpenTopics="dialogOpened"/>

<h1><fmt:message key="user.registration.verification.title"/></h1>
<!--Content-->
<s:form action="nextStep" id="verificationForm">
    <div id="tabwrapper">
        <div class="ui-tabs">
            <div class="ui-tabs-panel">
                <s:set var="flowSteps" value="#session.flowController.flowSteps"/>
                <firebird:messages/>

                <s:if test="%{@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@PERSON_SELECTION in #flowSteps}">
                    <s:url var="editPersonUrl" namespace="/user/registration/nav" action="enterFlowStep">
                        <s:param name="flowStepToGoTo" value="@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@PERSON_SELECTION"/>
                    </s:url>
                    <h2>
                        <fmt:message key="user.registration.verification.person.label"/>
                        <sj:a id="editPerson" href="%{#editPersonUrl}" cssClass="edit" targets="registrationContent">Edit</sj:a>
                    </h2>
                    <firebird:viewPerson person="${accountConfigurationData.person}" showProviderNumber="true" showCtepId="true"/>
                    <div class="clear"><br></div>
                </s:if>
                <h2>
                    <fmt:message key="user.registration.verification.selected.roles.label"/>
                    <s:if test="%{@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@ROLE_SELECTION in #flowSteps}">
                        <s:url var="editRolesUrl" namespace="/user/registration/nav" action="enterFlowStep">
                            <s:param name="flowStepToGoTo" value="@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@ROLE_SELECTION"/>
                        </s:url>
                        <sj:a id="editRoles" href="%{#editRolesUrl}" cssClass="edit" targets="registrationContent">Edit</sj:a>
                    </s:if>
                </h2>
                <ul id="selectedRoles">
                    <s:iterator value="accountConfigurationData.roles" var="role">
                       <li><span><s:property value="#role.display"/></span></li>
                    </s:iterator>
                </ul>
                <div class="clear"><br></div>

                <s:if test="%{@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@PRIMARY_ORGANIZATION_SELECTION in #flowSteps}">

                    <s:url var="editPrimaryOrgUrl" namespace="/user/registration/nav" action="enterFlowStep">
                        <s:param name="flowStepToGoTo" value="@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@PRIMARY_ORGANIZATION_SELECTION"/>
                    </s:url>
                    <h2>
                        <fmt:message key="user.registration.verification.primary.organization.label"/>
                        <sj:a id="editPrimaryOrganization" href="%{#editPrimaryOrgUrl}" cssClass="edit" targets="registrationContent">Edit</sj:a>
                    </h2>
                    <firebird:organizationDisplay organization="${accountConfigurationData.primaryOrganization.organization}"/>
                    <div class="clear"><br></div>
                </s:if>

                <s:if test="%{@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@INVESTIGATOR_SELECTION in #flowSteps}">

                    <s:url var="editSelectedInvestigatorsUrl" namespace="/user/registration/nav" action="enterFlowStep">
                        <s:param name="flowStepToGoTo" value="@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@INVESTIGATOR_SELECTION"/>
                    </s:url>
                    <h2>
                        <fmt:message key="user.registration.verification.selected.investigators.label"/>
                        <sj:a id="editSelectedInvestigators" href="%{#editSelectedInvestigatorsUrl}" cssClass="edit" targets="registrationContent">Edit</sj:a>
                    </h2>
                    <ul id="selectedInvestigators">
                        <s:iterator value="accountConfigurationData.selectedInvestigators" var="role">
                           <li><s:property value="#role.person.displayNameForList"/></li>
                        </s:iterator>
                    </ul>
                    <div class="clear"><br></div>
                </s:if>
                <s:if test="!accountConfigurationData.sponsorOrganizations.empty">
                    <div id="sponsorRepresentativesSection">
                        <h2>
                            <fmt:message key="user.registration.verification.sponsor.organizations.label"/>
                            <s:if test="%{@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@SPONSOR_ORGANIZATIONS in #flowSteps}">
                                <s:url var="editSponsorOrgsUrl" namespace="/user/registration/nav" action="enterFlowStep">
                                    <s:param name="flowStepToGoTo" value="@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@SPONSOR_ORGANIZATIONS"/>
                                </s:url>
                                <sj:a id="editSponsorOrgs" href="%{#editSponsorOrgsUrl}" cssClass="edit" targets="registrationContent">Edit</sj:a>
                            </s:if>
                        </h2>
                        <s:iterator value="accountConfigurationData.sponsorOrganizations" var="org" status="orgStatus">
                            <firebird:sponsorSectionDisplay organization="${org}" displayLine="${!orgStatus.last}"/>
                        </s:iterator>
                    </div>
                </s:if>
                <s:if test="!accountConfigurationData.delegateOrganizations.empty">
                    <div id="sponsorDelegatesSection">
                        <h2>
                            <fmt:message key="user.registration.verification.sponsor.delegates.label"/>
                            <s:if test="%{@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@SPONSOR_DELEGATES in #flowSteps}">
                                <s:url var="editSponsorDelegatesUrl" namespace="/user/registration/nav" action="enterFlowStep">
                                    <s:param name="flowStepToGoTo" value="@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@SPONSOR_DELEGATES"/>
                                </s:url>
                                <sj:a id="editSponsorDelegateOrgs" href="%{#editSponsorDelegatesUrl}" cssClass="edit" targets="registrationContent">Edit</sj:a>
                            </s:if>
                        </h2>
                        <s:iterator value="accountConfigurationData.delegateOrganizations" var="org" status="orgStatus">
                            <firebird:sponsorSectionDisplay organization="${org}" displayLine="${!orgStatus.last}"/>
                        </s:iterator>
                    </div>
                </s:if>
                <div class="clear"><br></div>
                <div class="btn_bar">
                    <firebird:userRegistrationPreviousButton/>
                    <a id="completeRegistrationBtn" class="button" href="#"><fmt:message key="button.save"/></a>
                </div>
            </div>
        </div>
    </div>
</s:form>
<s:url var="acknowledgeUrl" namespace="/user/registration/flow/ajax" action="acknowledgeFunctionalityWarning">
    <s:param name="newUser" value="%{newUser}" />
</s:url>

<script>
$(document).ready(function() {
   $('#completeRegistrationBtn').click(function() {
       var showLimitedFunctionalityWarning = <s:property value="verificationRequiredForRoles"/>;
       indicateLoading();
       var url = $('#verificationForm').attr("action");
       if (showLimitedFunctionalityWarning) {
           $('#functionalityWarningDialog').load(url, $('#verificationForm').serialize(), function() {
               indicateLoading(false);
               $(this).dialog("open");
               $(this).bind( "dialogclose", function(event, ui) {
                   window.location = '<s:property value="#acknowledgeUrl"/>';
               });
           });
       } else {
           $('#verificationForm').submit();
       }
   });

   $.publish('injectCsrfTokens');
});
</script>