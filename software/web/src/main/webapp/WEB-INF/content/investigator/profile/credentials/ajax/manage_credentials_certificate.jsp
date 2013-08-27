<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <s:form action="saveCertificate" id="credentialsCertificateForm" enctype="multipart/form-data" method="POST">
                <firebird:messages/>
                <firebird:dialogHeader>
                    <fmt:message key="credentials.certificate.title" />
                </firebird:dialogHeader>
                <firebird:instructionBubble messageKey="credentials.certificate.instructions" />

                <div class="formcol">
                    <s:select
                        label="%{getText('dropdown.certificateType')}"
                        id="certificateType"
                        name="certificate.certificateType"
                        requiredLabel="true"
                        list="@gov.nih.nci.firebird.data.CertificateType@values()"
                        listValue="getText(nameProperty)"
                        headerKey=""
                        headerValue="-- Select a Certificate Type --"
                        disabled="%{locked}"
                        onchange="certificateDialog.toggleEffectiveDateRequired();"
                        />
                    <s:if test="%{locked}">
                        <s:hidden name="certificate.certificateType"/>
                    </s:if>
                </div>
                <div class="formcol clear">
                    <s:if test="%{certificate.id != null}">
                        <br/>
                        <firebird:instructionBubble messageKey="credentials.certification.modify.file" />
                    </s:if>

                    <s:file id="certificateFile" name="certificateFile.data" size="30" requiredLabel="true" label="%{getText('label.file')}" />
                </div>
                <br/>
                <div class="formcol clear">
                    <firebird:dateSelect fieldName="effectiveDate" label="datePicker.effectiveDate" required="true"/>
                </div>
                <div class="formcol clear">
                    <firebird:dateSelect fieldName="expirationDate" label="datePicker.expirationDate" isFuture="true"/>
                </div>
                <div class="formcol_wide clear">
                    <s:checkbox id="nihOerCertificate"  name="nihOerIssued" label="%{getText('credentials.certification.nih.oer')}" labelposition="right" onchange="certificateDialog.toggleIssuerDisplay()" />
                </div>
                <div class="clear">

                <s:hidden name="id"/>
                <s:hidden name="profile.id" value="%{profile.id}"/>

                <div id="issuerSearch">
                        <firebird:organizationSearch labelKey="credentials.certificate.issuer.search.label" required="true" >
                            <s:a id="createIssuerButton" cssClass="button" href="javascript:void(0)" onclick="certificateDialog.showCreateNewIssuerDiv()"><fmt:message key="button.create.new"/></s:a>
                        </firebird:organizationSearch>
                    </div>
                </div>

                <s:hidden id="issuerSearchKey" name="issuerSearchKey"/>

                <div id="issuerDisplay">
                    <firebird:organizationDisplay organization="${certificate.issuer}" tagVariableName="issuerDisplay"/>
                    <s:a href="javascript:void(0)" id="searchAgain" onclick="certificateDialog.enableIssuerSearch()">
                        <fmt:message key="button.searchAgain" />
                    </s:a>
                </div>
                <div id="nihOerIssuerDisplay">
                    <firebird:organizationDisplay organization="${nihOerIssuer}" />
                    <s:a href="javascript:void(0)" id="searchAgain" onclick="certificateDialog.enableIssuerSearch()">
                        <fmt:message key="button.searchAgain" />
                    </s:a>
                </div>

                <div id="createNewIssuerFieldsWrapper">
                    <div id="createNewIssuerFields">
                        <span style="float: right; padding-top: .5em;">
                           <s:a href="javascript:void(0)" cssClass="search" id="searchAgain" onclick="certificateDialog.hideCreateNewIssuerDiv()">
                           <fmt:message key="button.searchAgain" /></s:a>
                       </span>
                        <div class="formcol">
                           <s:textfield id="certificate.issuer.name" name="certificate.issuer.name" maxlength="160" size="30" required="true"
                               cssStyle="width: 19em;" label="%{getText('textfield.organization.name')}"
                              />
                        </div>

                        <div class="dotted_line"><br></div>

                        <div class="formcol">
                           <s:textfield id="certificate.issuer.email" name="certificate.issuer.email" maxlength="50" size="30" cssStyle="width: 19em;"
                               label="%{getText('textfield.emailAddress')}" requiredLabel="true"/>
                        </div>

                        <div class="formcol">
                            <firebird:phoneNumber beanPrefix="certificate.issuer"/>
                        </div>

                        <div class="dotted_line"><br></div>

                        <firebird:addressFields beanPrefix="certificate.issuer.postalAddress" />
                    </div>
                </div>

                <firebird:profileButtonBar idPrefix="manageCertificate" form="credentialsCertificateForm" dialogId="${dialogId}"/>
            </s:form>
        </div>
    </div>
</div>

<s:url var="retrieveCtepIdUrl" action="retrieveCtepId" />

<script>

var certificateDialog = {
    toggleSpeed: 300,
    searchedOrganization: ${nihOerIssued} || "${certificate.issuer}".length === 0 ? null : "${certificate.issuer}",
    createNewIssuerDiv: null, //instantiated in onReady
    effectiveDateLabel: null, //instantiated in onReady
    effectiveDateRequiredAsterisk: null, //instantiated in onReady
    humanResearchCertificateType: '<s:property value="%{@gov.nih.nci.firebird.data.CertificateType@HUMAN_RESEARCH_CERTIFICATE.name()}"/>',

    toggleIssuerDisplay: function () {
        $("#createNewIssuerFields").hide(certificateDialog.toggleSpeed, function(){ certificateDialog.createNewIssuerDiv = $(this).detach(); });
        certificateDialog.toggleEffectiveDateRequired();
        var nihOerIssued = $("#nihOerCertificate").is(':checked');
        if (nihOerIssued) {
            $("#nihOerIssuerDisplay").show(certificateDialog.toggleSpeed);
            $("#issuerDisplay").hide(certificateDialog.toggleSpeed);
            $("#issuerSearch").hide(certificateDialog.toggleSpeed);
        } else {
            $("#nihOerIssuerDisplay").hide(certificateDialog.toggleSpeed);
            if (certificateDialog.searchedOrganization != null) {
                $("#issuerDisplay").show(certificateDialog.toggleSpeed);
            } else {
                $("#issuerSearch").show(certificateDialog.toggleSpeed);
            }
        }
    },

    toggleEffectiveDateRequired: function () {
      var required = certificateDialog.isHumanResearchCertificateSelected();
      certificateDialog.effectiveDateLabel.toggleClass("requiredLabel", required);
      certificateDialog.effectiveDateRequiredAsterisk.toggle(required);
    },

    isHumanResearchCertificateSelected: function () {
      return $('#certificateType').val() === certificateDialog.humanResearchCertificateType;
    },

    enableIssuerSearch: function () {
        $('#issuerSearchKey').val("");
        certificateDialog.searchedOrganization = null;
        $('#nihOerCertificate').attr('checked', false);
        $("#nihOerIssuerDisplay").hide(certificateDialog.toggleSpeed);
        $("#issuerDisplay").hide(certificateDialog.toggleSpeed);
        $("#issuerSearch").show(certificateDialog.toggleSpeed);
        $("#createNewIssuerFields").hide(certificateDialog.toggleSpeed, function(){ certificateDialog.createNewIssuerDiv = $(this).detach(); });
    },

    showCreateNewIssuerDiv: function () {
        $("#createNewIssuerFieldsWrapper").append(certificateDialog.createNewIssuerDiv);
        $("#createNewIssuerFields").show(certificateDialog.toggleSpeed);
        $("#nihOerIssuerDisplay").hide(certificateDialog.toggleSpeed);
        $("#issuerDisplay").hide(certificateDialog.toggleSpeed);
        $("#issuerSearch").hide(certificateDialog.toggleSpeed);
    },

    hideCreateNewIssuerDiv: function () {
        certificateDialog.toggleIssuerDisplay();
    }
}

$(function () {
    certificateDialog.effectiveDateLabel = $("label[for=effectiveDateMonthSelect]");
    certificateDialog.effectiveDateRequiredAsterisk = certificateDialog.effectiveDateLabel.find(".required");
    var showCreateNew = $("#createNewIssuerFields .errorMessage").length > 0;
    $("#nihOerIssuerDisplay").toggle(${nihOerIssued} && !showCreateNew);
    $("#issuerSearch").toggle(!${nihOerIssued} && certificateDialog.searchedOrganization == null && !showCreateNew);
    $("#issuerDisplay").toggle(!${nihOerIssued} && certificateDialog.searchedOrganization != null && !showCreateNew);
    if (!showCreateNew) {
      certificateDialog.createNewIssuerDiv = $("#createNewIssuerFields").detach();
    }
    certificateDialog.toggleEffectiveDateRequired();

    organizationSearch.clickSelectButton = function (searchKey, rowData) {
        indicateLoading(true);
        $.post("${retrieveCtepIdUrl}", { issuerSearchKey: searchKey } , function(ctepId) {
            rowData.organization.ctepId = ctepId;
            certificateDialog.searchedOrganization = rowData.organization;
            issuerDisplay.setOrganization(rowData.organization);
            $("#issuerSearch").hide(certificateDialog.toggleSpeed);
            $("#issuerDisplay").show(certificateDialog.toggleSpeed);
            $('#issuerSearchKey').val(searchKey);
            indicateLoading(false);
        });
    };

    $.publish('injectCsrfTokens');
});
</script>
