<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<firebird:messages/>
<h2 class="clear">
    <fmt:message key="registration.financial.disclosure.title"/>
</h2>

<s:if test="registration.lockedForInvestigator">
    <div>
        <firebird:viewGeneratedFormButton buttonId="viewFinancialDisclosurePdfButtonTop" form="${form}"/>
    </div>
</s:if>
<s:else>
    <%--TODO Perhaps add in functionality to show that saves have occurred properly. i.e. a Checkmark appears next to a successful save. --%>
    <div class="formcol_row">
        <firebird:viewGeneratedFormButton buttonId="viewFinancialDisclosurePdfButtonTop" form="${form}"/>
        <s:if test="%{registration.financialDisclosure.comments != null && !registration.financialDisclosure.comments.empty}">
            <firebird:comments retainState="true">
                ${registration.financialDisclosure.comments}
            </firebird:comments>
        </s:if>
    </div>
    <div class="line"></div>
    <div class="formcol_row">
        <fmt:message key="registration.financial.disclosure.intro" />
    </div>
    <br/>
    <s:form id="financialDisclosureQuestionsForm" action="save" namespace="/investigator/registration/ajax/financialdisclosure">
        <s:hidden name="registration.id" />
        <firebird:instructionBubble messageKey="registration.financial.disclosure.instructions" />
        <div class="line"></div>
        <div id="financialArrangementsDiv">
            <div id="question1">
                <div class="formcol_row">
                    <div class="formcol_xthin">
                        <span id="monetaryGainAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
                        <s:radio id="registration_financialDisclosure_monetaryGain"
                            name="registration.financialDisclosure.monetaryGain" list="#{true:'Yes',false:'No'}" onclick="updateObligations(this);"/>
                    </div>
                    <fmt:message key="registration.financial.disclosure.instructions.q1" />
                </div>
                <div class="dotted_line"></div>
            </div>
            <div id="question2">
                <div class="formcol_row">
                    <div class="formcol_xthin">
                        <span id="otherSponsorPaymentsAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
                        <s:radio id="registration_financialDisclosure_otherSponsorPayments"
                            name="registration.financialDisclosure.otherSponsorPayments" list="#{true:'Yes',false:'No'}" onclick="updateObligations(this);" />
                    </div>
                    <fmt:message key="registration.financial.disclosure.instructions.q2" />
                </div>
                <div class="dotted_line"></div>
            </div>
            <div id="question3">
                <div class="formcol_row">
                    <div class="formcol_xthin">
                        <span id="financialInterestAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
                        <s:radio id="registration_financialDisclosure_financialInterest"
                            name="registration.financialDisclosure.financialInterest" list="#{true:'Yes',false:'No'}" onclick="updateObligations(this);" />
                    </div>
                    <fmt:message key="registration.financial.disclosure.instructions.q3" />
                </div>
                <div class="dotted_line"></div>
            </div>
            <div id="question4">
                <div class="formcol_row">
                    <div class="formcol_xthin">
                        <span id="equityInSponsorAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
                        <s:radio id="registration_financialDisclosure_equityInSponsor"
                            name="registration.financialDisclosure.equityInSponsor" list="#{true:'Yes',false:'No'}" onclick="updateObligations(this);" />
                    </div>
                    <fmt:message key="registration.financial.disclosure.instructions.q4" />
                </div>
                <div class="clear"></div>
            </div>
        </div>
        <div class="line"></div>
        <div id="noFinancialArrangementsDiv">
            <div class="formcol_row">
                <h3>OR</h3>
                <div class="formcol_xthin">
                    <span id="noFinancialInterestAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
                    <label for='registration_financialDisclosure_noFinancialInterest' class='hide'>Select</label>
                    <s:checkbox id="registration_financialDisclosure_noFinancialInterest"
                        name="registration.financialDisclosure.noFinancialInterest" fieldValue="true" onclick="updateNoObligations()"/>
                </div>
                <fmt:message key="registration.financial.disclosure.instructions.q5" />
            </div>
        </div>
    </s:form>

    <div class="line"></div>
        <div class="formcol_row">
            <fmt:message key="registration.financial.disclosure.instructions.supporting.documents" />
        </div>
        <div class="clear"></div>
        <div class="line"></div>

   <firebird:supportingDocuments />

    <div>
        <firebird:viewGeneratedFormButton buttonId="viewFinancialDisclosurePdfButtonBottom" form="${form}"/>
    </div>
    <div class="clear"></div>

    <script type='text/javascript'>

        $(document).ready(function() {
            addValidationErrorAsterisksIfNesessary();
            $.publish('injectCsrfTokens');
        });

        function updateNoObligations() {
            if($('#registration_financialDisclosure_noFinancialInterest').is(':checked')) {
                $('#registration_financialDisclosure_monetaryGainfalse').prop("checked","true");
                $('#registration_financialDisclosure_otherSponsorPaymentsfalse').prop("checked","true");
                $('#registration_financialDisclosure_financialInterestfalse').prop("checked","true");
                $('#registration_financialDisclosure_equityInSponsorfalse').prop("checked","true");
            }
            submitQuestionsForm();
        }

        function submitQuestionsForm() {
            indicateLoading(true);
            $.post($('#financialDisclosureQuestionsForm').attr("action"),$('#financialDisclosureQuestionsForm').serialize(), function(errorMessages) {
              setPageErrorMessages(errorMessages);
              addValidationErrorAsterisksIfNesessary();
              indicateLoading(false);
            });
        }

        function addValidationErrorAsterisksIfNesessary() {
          var incomplete = '<s:property value="@gov.nih.nci.firebird.data.RegistrationStatus@INCOMPLETE"/>';
          if ("${registration.status}" == incomplete) {
            checkForMissingRequiredDocumentation();
            checkForUnansweredQuestions();
          }
        }

        function checkForMissingRequiredDocumentation() {
          var showAsterisk = yesAnsweredForAnyQuestion()  && !isDocumentUploaded();
          $("#requiredDocumentationAsterisk").toggle(showAsterisk);
        }

        function yesAnsweredForAnyQuestion() {
          return $(":radio[value='true']:checked").length > 0;
        }

        function isDocumentUploaded() {
          return $("#supportingDocumentsTable").dataTable().fnGetData().length > 0;
        }

        function checkForUnansweredQuestions() {
          var monetaryGainNotAnswered = $('input:radio[name=registration\\.financialDisclosure\\.monetaryGain]:checked').val() === undefined;
          var otherSponsorPaymentsNotAnswered = $('input:radio[name=registration\\.financialDisclosure\\.otherSponsorPayments]:checked').val() === undefined;
          var financialInterestNotAnswered = $('input:radio[name=registration\\.financialDisclosure\\.financialInterest]:checked').val() === undefined;
          var equityInSponsorNotAnswered = $('input:radio[name=registration\\.financialDisclosure\\.equityInSponsor]:checked').val() === undefined;
          var noFinancialInterestChecked = $('#registration_financialDisclosure_noFinancialInterest').is(':checked');
          $("#monetaryGainAsterisk").toggle(monetaryGainNotAnswered);
          $("#otherSponsorPaymentsAsterisk").toggle(otherSponsorPaymentsNotAnswered);
          $("#financialInterestAsterisk").toggle(financialInterestNotAnswered);
          $("#equityInSponsorAsterisk").toggle(equityInSponsorNotAnswered);
          var showNoFinancialInterestAsterisk = noAnsweredForAllQuestions() && !noFinancialInterestChecked;
          $("#noFinancialInterestAsterisk").toggle(showNoFinancialInterestAsterisk);

        }

        var $noFinancialInterestCheckbox = $('#registration_financialDisclosure_noFinancialInterest');
        function updateObligations(obj) {
            if (noAnsweredForAllQuestions()){
              $noFinancialInterestCheckbox.prop("checked", true);
            } else {
              $noFinancialInterestCheckbox.prop("checked", false);
            }
            submitQuestionsForm();
        }

        var $totalQuestions = $(":radio[value='false']").length;
        function noAnsweredForAllQuestions() {
          return $(":radio[value='false']:checked").length === $totalQuestions;
        }
    </script>
</s:else>