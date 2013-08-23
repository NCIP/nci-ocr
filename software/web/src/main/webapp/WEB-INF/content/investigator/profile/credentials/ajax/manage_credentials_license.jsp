<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
  <body id="sec">
    <!--Content-->
    <div id="tabwrapper">
      <div class="ui-tabs">
                <div class="ui-tabs-panel">
                    <firebird:messages/>
                  <s:form namespace="/investigator/profile/credentials/ajax" action="saveLicense" id="credentialsLicenseForm">


            <firebird:dialogHeader>
                <fmt:message key="credentials.license.title" />
            </firebird:dialogHeader>

                        <s:select
                            id="licenseType"
              label="%{getText('dropdown.licenseType')}"
              requiredLabel="true"
              name="licenseTypeId"
              list="licensesWithRankedOnTop"
                            listKey="id"
                            listValue="name"
              headerKey=""
              headerValue="--- Select a License Type ---" />
                            <br/>
                        <s:textfield
                            id = "licenseId"
                            label="%{getText('textfield.license.id')}"
                            requiredLabel="true"
                            name="license.licenseId"
                        />
                        <br/>
                        <div id="usStateDiv">
                            <s:select
                                id="licenseState"
                                label="%{getText('dropdown.state')}"
                                requiredLabel="true"
                                headerKey=""
                                headerValue="--- Select a State ---"
                                list="states"
                                listKey="code"
                                listValue="name"
                                name="license.state"/>
                            <br/>
                        </div>
                        <s:select
                            id="licenseCountry"
                            label="%{getText('dropdown.country')}"
                            requiredLabel="true"
                            list="countries"
                            listKey="alpha3"
                            listValue="name"
                            name="license.country"
                            onchange="_licenseForm.handleStateFieldForCountry()" />
                        <br/>

                        <firebird:dateSelect fieldName="expirationDate" label="datePicker.expirationDate" isFuture="true" required="true"/>

                        <s:hidden name="id"/>
                        <s:hidden name="profile.id" value="%{profile.id}"/>

                        <firebird:profileButtonBar idPrefix="manageLicense" form="credentialsLicenseForm"/>

              </s:form>
        </div>
      </div>
    </div>
  </body>

    <script>

    var _licenseForm = (function() {

        var form = {};

        form.handleStateFieldForCountry = function() {
            if ($("#licenseCountry").val() == US_COUNTRY_CODE) {
                form.usStateDiv.show();
            } else {
                form.usStateDiv.hide();
                $('#licenseState').val('');
            }
        };

        form.initialize = function() {
            form.usStateDiv = $('#usStateDiv');
            form.handleStateFieldForCountry();
        };

        return form;

    })();

   $(document).ready(function() {
        _licenseForm.initialize();
        var index = <s:property value="rankedLicenses.size"/>;
        addSeperatorToSelectList('licenseType', index);
        $("input").keydown(testForEnter);
    });
    </script>
</html>