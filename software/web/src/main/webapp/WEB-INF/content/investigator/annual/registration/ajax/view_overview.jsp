<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="completeUrl" action="submitRegistration">
    <s:param name="registration.id" value="registration.id" />
</s:url>
<s:url var="submitToInvestigatorUrl" action="confirmSubmitToInvestigatorEnter" >
    <s:param name="registration.id" value="registration.id" />
</s:url>
<html>
<body>
    <span class="bold"><fmt:message key="label.status"/>: </span><span id="registrationStatus" class="blueText bold" ></span>
    <div class="blank_space"></div>
    <s:if test="%{registration.submittable}">
        <div>
        <div class="progressBar" id="registrationProgressBar" ></div>

        <s:if test="%{userAnApprovedCoordinatorForRegistrationsProfile || ctepSponsor || ctepSponsorDelegate}">
            <s:if test="%{!registration.lockedForInvestigator}">
                <div>
                      <sj:a id="submitForReviewButton" cssClass="button blueButton float_right" href="%{#submitToInvestigatorUrl}"
                      openDialog="registrationDialog" ><fmt:message key="button.submit.to.investigator" /></sj:a>
                </div>
            </s:if>
        </s:if>
        <s:else>
            <div>
                <sj:a id="submitForReviewButton" cssClass="button blueButton float_right" href="%{#completeUrl}"
                      openDialog="registrationDialog" ><fmt:message key="button.submit.for.review" /></sj:a>
            </div>
        </s:else>
        </div>
    </s:if>

    <firebird:registrationComments registration="${registration}" retainState="true"/>
    <br/><br/>
    <div class="boldHeader"><fmt:message key="label.required.forms" /></div>
    <div id="requiredFormsTable" class="annualRegistrationFormsTable">
        <s:iterator value="registration.forms.{?#this.formOptionality == @gov.nih.nci.firebird.data.FormOptionality@REQUIRED}" var="form">
            <firebird:formListing form="${#form}" />
        </s:iterator>
    </div>
    <div class="clear"></div>
    <s:if test="registration.forms.{?#this.formOptionality == @gov.nih.nci.firebird.data.FormOptionality@OPTIONAL}.size > 0">
        <br/>
        <div class="boldHeader"><fmt:message key="label.optional.forms" /></div>
        <div id="optionalFormsTable" class="annualRegistrationFormsTable">
            <s:iterator value="registration.forms.{?#this.formOptionality == @gov.nih.nci.firebird.data.FormOptionality@OPTIONAL}" var="form">
                <firebird:formListing form="${#form}" />
            </s:iterator>
        </div>
        <div class="clear"></div>
    </s:if>
    <s:if test="registration.forms.{?#this.formOptionality == @gov.nih.nci.firebird.data.FormOptionality@SUPPLEMENTARY}.size > 0">
        <br/>
        <div class="boldHeader"><fmt:message key="label.supplemental.forms" /></div>
        <div id="supplementalFormsTable" class="annualRegistrationFormsTable">
            <s:iterator value="registration.forms.{?#this.formOptionality == @gov.nih.nci.firebird.data.FormOptionality@SUPPLEMENTARY}" var="form">
                <firebird:formListing form="${#form}" />
            </s:iterator>
        </div>
        <div class="clear"></div>
    </s:if>
    <script type="text/javascript">
          var required = '<s:property value="@gov.nih.nci.firebird.data.FormOptionality@REQUIRED.display"/>';
          var completed = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@COMPLETED.display"/>';
          var viewOverviewTab = {};

          viewOverviewTab.setRegistrationStatusAndReturnCompletionPercentage = function() {
            var registrationStatus = "${registration.status.display}";
            if (!${registration.submittable}) {
              $("#registrationStatus").text(registrationStatus);
              return 100;
            }

            var formListings = ${formListings};
            var requiredForms = 0;
            var completedRequiredForms = 0;

            _.each(formListings, function(form) {
              if (form.optionality.display == required) {
                requiredForms++;
                if (form.status.display == completed) {
                  completedRequiredForms++;
                }
              }
            });
            var completionStatusSuffix = '<fmt:message key="label.required.forms.progress.bar.completion.status.suffix"/>';
            var completionStatus = registrationStatus + " (" + completedRequiredForms + "/" + requiredForms + " " + completionStatusSuffix;
            $("#registrationStatus").text(completionStatus);

            if (requiredForms === 0) {
              return 100;
            } else {
              return completedRequiredForms / requiredForms * 100;
            }
          }

          viewOverviewTab.createProgressBar = function(completionPercentage) {
            $("#registrationProgressBar").progressbar({
              value : completionPercentage
            });
          }

          $(function() {
            var completionPercentage = viewOverviewTab.setRegistrationStatusAndReturnCompletionPercentage();
            viewOverviewTab.createProgressBar(completionPercentage);
          });

        </script>
</body>
</html>