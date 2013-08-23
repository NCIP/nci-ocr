<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="completeUrl" action="submitRegistration">
    <s:param name="registration.id" value="registration.id" />
</s:url>
<s:url var="submitToInvestigatorUrl" action="confirmSubmitToInvestigatorEnter" >
    <s:param name="registration.id" value="registration.id" />
</s:url>
<s:url var="enterDeleteAnnualRegistrationUrl" action="enterDeleteAnnualRegistration">
    <s:param name="registration.id" value="registration.id" />
</s:url>
<html>
<body>

    <div id="ctepOverview"><!-- Marker that this is the CTEP Overview Tab --></div>

    <span class="bold"><fmt:message key="label.status"/>: </span><span id="registrationStatus" class="blueText bold" >${registration.status.display}</span>
    <div class="blank_space"></div>

    <s:if test="submittable">
        <div>
        <firebird:registrationProgressBar registration="${registration}"/>

        <s:if test="%{userAnApprovedCoordinatorForRegistrationsProfile || ctepSponsor || ctepSponsorDelegate}">
            <div>
                  <sj:a id="submitForReviewButton" cssClass="button blueButton float_right" href="%{#submitToInvestigatorUrl}"
                  openDialog="registrationDialog" ><fmt:message key="button.submit.to.investigator" /></sj:a>
            </div>
        </s:if>
        <s:else>
            <div>
                <sj:a id="submitForReviewButton" cssClass="button blueButton float_right" href="%{#completeUrl}"
                      openDialog="registrationDialog" ><fmt:message key="button.submit.for.review" /></sj:a>
            </div>
        </s:else>
        </div>
    </s:if>

    <s:if test="deleteRegistrationAllowed">
        <br/><br/>
        <sj:a id="deleteRegistration" cssClass="button blueButton float_right" href="%{#enterDeleteAnnualRegistrationUrl}"
                          openDialog="registrationDialog" ><fmt:message key="button.delete.registration"/></sj:a>
    </s:if>

    <firebird:registrationComments registration="${registration}" retainState="true"/>
    <br/><br/>

    <firebird:investigatorRegistrationFormTables registration="${registration}"/>

</body>
</html>