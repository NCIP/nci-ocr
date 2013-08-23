<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/registration/ajax" action="enterComments" var="commentsUrl">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<s:url var="initiateRegistrationRevisionUrl" action="initiateRegistrationRevision" escapeAmp="false">
    <s:param name="registration.id" value="registration.id" />
</s:url>
<s:url var="cancelRegistrationRevisionUrl" action="cancelRegistrationRevision" escapeAmp="false">
    <s:param name="registration.id" value="registration.id" />
</s:url>

<div id="dcpOverview"><!-- Marker that this is the DCP Overview Tab --></div>

<div>
<firebird:messages/>
<span class="bold"><fmt:message key="label.status"/>: </span><span id="registrationStatus" class="blueText bold" >${registration.status.display}</span>
    <div class="blank_space"></div>
    <s:if test="%{registration.submittable}">
        <div>
            <firebird:registrationProgressBar registration="${registration}"/>

            <div>
                <s:if test="%{userAnApprovedCoordinatorForRegistrationsProfile}">
                    <s:if test="%{!readOnly}">
                        <s:url var="submitToInvestigatorUrl" action="confirmSubmitToInvestigatorEnter" escapeAmp="false">
                            <s:param name="registration.id" value="registration.id" />
                            <s:param name="originalUpdateDate" value="registration.protocol.lastUpdate.time" />
                        </s:url>
                            <sj:a id="processRegistrationBtn" href="%{#submitToInvestigatorUrl}" cssClass="button blueButton float_right"
                                  openDialog="registrationDialog"><fmt:message key="button.submit.to.investigator"/></sj:a>
                    </s:if>
                </s:if>
                <s:else>
                    <s:url var="completeUrl" action="submitRegistration" escapeAmp="false">
                        <s:param name="registration.id" value="registration.id" />
                        <s:param name="originalUpdateDate" value="registration.protocol.lastUpdate.time" />
                    </s:url>
                        <sj:a id="processRegistrationBtn" openDialog="registrationDialog" href="%{#completeUrl}"
                            cssClass="button blueButton float_right"><fmt:message key="button.submit.for.review"/></sj:a>
                </s:else>
            </div>
        </div>
        </s:if><s:elseif test="%{registration.revisable}">
            <s:a id="initiateRegistrationRevisionButton" href="javascript:void(0)" onclick="initiateRegistrationRevision();" cssClass="button blueButton clear float_right">
                <fmt:message key="button.revise"/>
            </s:a>
        </s:elseif>
        <s:if test="%{registration.cancelable}">
            <s:a id="cancelRegistrationRevisionButton" href="javascript:void(0)" onclick="cancelRegistrationRevision();" cssClass="button blueButton float_right clear float_right">
                <fmt:message key="button.cancel.revision"/>
            </s:a>
        </s:if>

    <firebird:registrationComments registration="${registration}" retainState="true"/>
    <br/><br/>

    <firebird:investigatorRegistrationFormTables registration="${registration}"/>

    <script type="text/javascript">

        $(document).ready(function() {
          $('#registrationDialog').bind("dialogclose", function(event, ui) {
            _viewReg.clearTimers(); //defined on view_registration.jsp
          });
        });

        function initiateRegistrationRevision() {
          var url = "${initiateRegistrationRevisionUrl}";
          $.post(url, null, function(data) {
             window.location.reload();
          });
        }

        function cancelRegistrationRevision() {
          var url = "${cancelRegistrationRevisionUrl}";
          $.post(url, null, function(data) {
             window.location.reload();
          });
        }

    </script>
</div>