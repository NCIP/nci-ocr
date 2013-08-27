<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/annual/registration/ajax/financialdisclosure" action="enterAddPharmaceuticalCompany" var="addPharmaceuticalCompanyUrl" escapeAmp="false">
    <s:param name="profile.id" value="profile.id"/>
    <s:param name="registration.id" value="%{registration.id}" />
</s:url>
<s:url var="removePharmaceuticalCompanyUrl" action="enterRemovePharmaceuticalCompanyConfirm" escapeAmp="false" >
    <s:param name="profile.id" value="%{profile.id}" />
    <s:param name="registration.id" value="%{registration.id}" />
</s:url>
<firebird:messages/>
<h2 class="clear">
    <fmt:message key="registration.financial.disclosure.title"/>
</h2>

<s:if test="registration.lockedForInvestigator">
    <div id="lockedViewFinancialDisclosurePdfButtonDiv">
        <firebird:viewGeneratedFormButton buttonId="viewFinancialDisclosurePdfButtonTop" form="${form}"/>
    </div>
</s:if>
<s:else>
    <div id="unlockedViewFinancialDisclosurePdfButtonDiv" class="formcol_row">
        <firebird:viewGeneratedFormButton buttonId="viewFinancialDisclosurePdfButtonTop" form="${form}"/>
        <s:if test="%{registration.financialDisclosure.comments != null && !registration.financialDisclosure.comments.empty}">
            <firebird:comments retainState="true">
                ${registration.financialDisclosure.comments}
            </firebird:comments>
        </s:if>
    </div>
    <div class="line"></div>
    <div class="formcol_row">
        <fmt:message key="annual.registration.financial.disclosure.intro" />
    </div>
    <br/>
    <s:form id="financialDisclosureQuestionsForm" action="save" namespace="/investigator/annual/registration/ajax/financialdisclosure">
        <s:hidden name="registration.id" />
        <firebird:instructionBubble messageKey="annual.registration.financial.disclosure.instructions" />
        <div class="line"></div>
        <div id="financialArrangementsDiv">
            <div id="question1">
                <div class="formcol_row">
                    <div class="formcol_xthin">
                        <span id="monetaryGainAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
                        <s:radio id="registration_financialDisclosure_monetaryGain"
                            name="registration.financialDisclosure.monetaryGain" list="#{true:'Yes',false:'No'}" onclick="submitQuestionsForm();"/>
                    </div>
                    <fmt:message key="annual.registration.financial.disclosure.instructions.q1" />
                </div>
                <div class="dotted_line"></div>
            </div>
            <div id="question2">
                <div class="formcol_row">
                    <div class="formcol_xthin">
                        <span id="otherSponsorPaymentsAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
                        <s:radio id="registration_financialDisclosure_otherSponsorPayments"
                            name="registration.financialDisclosure.otherSponsorPayments" list="#{true:'Yes',false:'No'}" onclick="submitQuestionsForm();" />
                    </div>
                    <fmt:message key="annual.registration.financial.disclosure.instructions.q2" />
                </div>
                <div class="dotted_line"></div>
            </div>
            <div id="question3">
                <div class="formcol_row">
                    <div class="formcol_xthin">
                        <span id="financialInterestAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
                        <s:radio id="registration_financialDisclosure_financialInterest"
                            name="registration.financialDisclosure.financialInterest" list="#{true:'Yes',false:'No'}" onclick="submitQuestionsForm();" />
                    </div>
                    <fmt:message key="annual.registration.financial.disclosure.instructions.q3" />
                </div>
                <div class="dotted_line"></div>
            </div>
            <div id="question4">
                <div class="formcol_row">
                    <div class="formcol_xthin">
                        <span id="equityInSponsorAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
                        <s:radio id="registration_financialDisclosure_equityInSponsor"
                            name="registration.financialDisclosure.equityInSponsor" list="#{true:'Yes',false:'No'}" onclick="submitQuestionsForm();" />
                    </div>
                    <fmt:message key="annual.registration.financial.disclosure.instructions.q4" />
                </div>
                <div class="clear"></div>
            </div>
        </div>
    </s:form>

    <div class="line"></div>
        <firebird:instructionBubble messageKey="annual.registration.financial.disclosure.pharmaceutical.companies" />
        <div class="formcol_row">
            <h2>
                <span id="requiredPharmaceuticalCompanyAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
                <fmt:message key="label.pharmaceutical.companies" />
                <sj:a openDialog="registrationDialog" href="%{addPharmaceuticalCompanyUrl}" cssClass="button edit" id="addPharmaceuticalCompany">
                    <fmt:message key="button.add.pharmaceutical.company"/>
                </sj:a>
            </h2>

            <table id="pharmaceuticalCompaniesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                    summary="This table displays the pharmaceutical companies that have been added to this form. It displays
                            the company's name, CTEP ID, email address, phone number, mailing address, and link to remove
                            the company from the list.">
                <thead>
                    <tr>
                        <th scope="col" width="200px"><div><fmt:message key="label.name"/></div></th>
                        <th scope="col" width="150px"><div><fmt:message key="label.ctep.id"/></div></th>
                        <th scope="col" width="200px"><div><fmt:message key="label.email"/></div></th>
                        <th scope="col" width="200px"><div><fmt:message key="label.phone"/></div></th>
                        <th scope="col" width="200px"><div><fmt:message key="label.address"/></div></th>
                        <th scope="col" width="33px"><%--Delete Link--%></th>
                    </tr>
                </thead>
            </table>
        </div>
        <div class="clear"></div>
        <div class="line"></div>

    <firebird:supportingDocuments />

    <h5><fmt:message key="annual.registration.financial.disclosure.dislaimer" /></h5>

    <firebird:viewGeneratedFormButton buttonId="viewFinancialDisclosurePdfButtonBottom" form="${form}"/>
    <firebird:nextTabButton form="${form}" />

    <script type='text/javascript'>

        $(document).ready(function() {
            buildPharmaceuticalCompaniesTable();
            addValidationErrorAsterisksIfNesessary();
            $.publish('injectCsrfTokens');
        });

        function buildPharmaceuticalCompaniesTable() {
            var pharmaceuticalCompanies = ${pharmaceuticalCompaniesJson};
            var isLongData = pharmaceuticalCompanies.length > ${minPaginationResults};
            $('#pharmaceuticalCompaniesTable').dataTable( {
                "aaData" : pharmaceuticalCompanies,
                "bInfo" : pharmaceuticalCompanies,
                "bLengthChange": isLongData,
                "bPaginate": isLongData,
                "bFilter": isLongData,
                "aoColumns": [{mDataProp: "name"},
                              {mDataProp: "ctepId"},
                              {mDataProp: "email"},
                              {mDataProp: "phoneNumber"},
                              {mDataProp: "postalAddress", fnRender : function(obj) {
                                  return __addressFormatter(obj.aData.postalAddress);
                              }},
                              {"mDataProp" : null, "fnRender" : function(obj) {
                                  return createDeleteLink(obj.aData);
                              }}
                          ],
               fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                   $(nRow).attr("id", aData.id);
                   return nRow;
               },
               fnInitComplete: function() {
                   indicateLoading(false);
               }
            });
        }

        function createDeleteLink(pharmaceuticalCompany) {
          var url = '<s:property value="%{#removePharmaceuticalCompanyUrl}"/>';
          return ajaxLinkFormatter(createImage('ico_delete.gif', 'Delete'),
                  {
                      'url': url,
                      'paramName':'pharmaceuticalCompanyId',
                      'paramValue': 'id',
                      'action': 'delete',
                      'target': 'registrationDialog'
                  },
                  pharmaceuticalCompany);
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
            checkForMissingRequiredPharmaceuticalCompanies();
            checkForUnansweredQuestions();
          }
        }

        function checkForMissingRequiredPharmaceuticalCompanies() {
          var showAsterisk = yesAnsweredForAnyQuestion()  && !isPharmaceuticalCompanySelected();
          $("#requiredPharmaceuticalCompanyAsterisk").toggle(showAsterisk);
        }

        function yesAnsweredForAnyQuestion() {
          return $(":radio[value='true']:checked").length > 0;
        }

        function isPharmaceuticalCompanySelected() {
          return $("#pharmaceuticalCompaniesTable").dataTable().fnGetData().length > 0;
        }

        function checkForUnansweredQuestions() {
          var monetartyGainNotAnswered = $('input:radio[name=registration\\.financialDisclosure\\.monetaryGain]:checked').val() === undefined;
          var otherSponsorPaymentsNotAnswered = $('input:radio[name=registration\\.financialDisclosure\\.otherSponsorPayments]:checked').val() === undefined;
          var financialInterestNotAnswered = $('input:radio[name=registration\\.financialDisclosure\\.financialInterest]:checked').val() === undefined;
          var equityInSponsorNotAnswered = $('input:radio[name=registration\\.financialDisclosure\\.equityInSponsor]:checked').val() === undefined;
          $("#monetaryGainAsterisk").toggle(monetartyGainNotAnswered);
          $("#otherSponsorPaymentsAsterisk").toggle(otherSponsorPaymentsNotAnswered);
          $("#financialInterestAsterisk").toggle(financialInterestNotAnswered);
          $("#equityInSponsorAsterisk").toggle(equityInSponsorNotAnswered);
        }
    </script>
</s:else>