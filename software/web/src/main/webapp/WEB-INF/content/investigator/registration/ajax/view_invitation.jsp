<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
    <body id="registrationInvitation">

        <div id="tabwrapper">
            <div class="ui-tabs">
                <div class="ui-tabs-panel">

                    <firebird:dialogHeader><s:property value="title" /></firebird:dialogHeader>

                    <s:if test="registration.investigatorRegistration">
                        <fmt:message key="investigator.registration.invitation.message.greeting">
                            <fmt:param value="${registration.profile.person.displayName}" />
                            <fmt:param value="${registration.protocol.sponsor.name}" />
                            <fmt:param value="${registration.protocol.protocolNumber}" />
                        </fmt:message>
                    </s:if>
                    <s:else>
                        <fmt:message key="subinvestigator.registration.invitation.message.greeting">
                            <fmt:param value="${registration.profile.person.displayName}" />
                            <fmt:param value="${registration.primaryRegistration.profile.person.displayName}" />
                            <fmt:param value="${registration.protocol.protocolNumber}" />
                        </fmt:message>
                    </s:else>

                    <fmt:message key="registration.invitation.message.body">
                        <fmt:param value="${registration.protocol.protocolNumber}" />
                        <fmt:param value="${registration.protocol.protocolTitle}" />
                        <fmt:param value="${leadOrganizationsForDisplay}" />
                        <fmt:param value="${registration.protocol.phase.display}" />
                        <fmt:param value="${registration.protocol.agentListForDisplay}" />
                        <fmt:param value="${registration.protocol.sponsor.name}" />
                        <fmt:param value="${sponsorEmailAddress}" />
                    </fmt:message>

                    <div class="clear"><br></div>

                    <div class="btn_bar">

                        <s:a id="beginRegistrationButton" namespace="/investigator/registration" action="enterRegistrations"
                            cssClass="button"><fmt:message key="button.begin.registration"/>
                            <s:param name="id" value="registration.id"/>
                        </s:a>
                    </div>

                </div>
            </div>
        </div>
        <script type="text/javascript">
            function chooseFirebirdRegistration() {
                closeDialog();
            }
        </script>

    </body>
</html>