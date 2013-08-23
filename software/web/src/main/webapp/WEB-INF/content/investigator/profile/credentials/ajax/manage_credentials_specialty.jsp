<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
  <body id="sec">
    <!--Content-->
    <div id="tabwrapper">
      <div class="ui-tabs">
                <div class="ui-tabs-panel">
                    <firebird:messages/>
                  <s:form namespace="/investigator/profile/credentials/ajax" action="saveSpecialty" id="credentialsSpecialtyForm">

            <firebird:dialogHeader>
                            <fmt:message key="credentials.specialty.title" />
            </firebird:dialogHeader>

            <div class="formcol_wide">
                            <s:select id="credentialsSpecialtyBoard"
                name="specialtyBoardId"
                label="%{getText('dropdown.boardName')}"
                requiredLabel="true"
                list="%{specialtyBoards}"
                                listKey="id"
                                listValue="name"
                headerKey=""
                headerValue="--- Select a Board ---"
                onchange="$.publish('reloadsecondlist','',this)"/>
            </div>

            <div class="formcol">
                            <s:url var="specialtyTypesUrl" namespace="/investigator/profile/credentials/ajax" action="specialtyTypes"/>
                            <sj:select
                                requiredLabel="true"
                                label="%{getText('dropdown.specialtyType')}"
                                href="%{specialtyTypesUrl}"
                                id="credentialsSpecialtyType"
                                formIds="credentialsSpecialtyForm"
                                reloadTopics="reloadsecondlist"
                                name="specialtyTypeId"
                                list="specialties"
                                listKey="id"
                                listValue="display"
                                headerKey="-1"
                                headerValue="-- Select A Specialty / Sub-Specialty --"/>
                        </div>

                        <div class="formcol_wide clear">
                            <s:select id="credentialsSpecialtyBoardEligible"
                                      label="%{getText('dropdown.specialtyStatus')}"
                                      requiredLabel="true"
                                      name="specialty.status"
                                      list="@gov.nih.nci.firebird.data.CertificationStatus@values()"
                                      listValue="display"
                                      headerKey=""
                                      headerValue="--- Select a Status ---" />
                        </div>
                        <div class="clear" ></div>

                        <div class="formcol">
                            <firebird:dateSelect fieldName="effectiveDate" label="datePicker.effectiveDate" required="true"/>
                        </div>
                        <div class="formcol">
                            <firebird:dateSelect fieldName="expirationDate" label="datePicker.expirationDate" required="true" isFuture="true"/>
                        </div>
                        <s:hidden name="id"/>
                        <s:hidden name="profile.id" value="%{profile.id}"/>

                        <div class="clear"></div>

                        <firebird:profileButtonBar idPrefix="manageSpecialty" form="credentialsSpecialtyForm"/>

              </s:form>
        </div>
      </div>
    </div>
  </body>
    <script type="text/javascript">
    $(document).ready(function() {
      checkSpecialtySelectForValidationError();
    });

    function checkSpecialtySelectForValidationError() {
      var specialtySelect = $('#credentialsSpecialtyType');
        var validationError = $(specialtySelect).parent().attr('class').indexOf("fieldError") != -1;
        if (validationError) {
          $(specialtySelect).wrap('<span class="selectFieldError" />');
         }
    }
    </script>
</html>