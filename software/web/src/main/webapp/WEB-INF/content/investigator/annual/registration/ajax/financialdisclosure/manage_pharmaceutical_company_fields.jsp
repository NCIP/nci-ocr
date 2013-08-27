<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<!--Content-->
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader>
                <fmt:message key="annual.registration.financial.disclosure.add.pharmaceutical.company.title" />
            </firebird:dialogHeader>
            <firebird:messages/>
            <span style="float: right; padding-top: .5em;">
                <a id="searchAgainButton" href="javascript:void(0);" class="search">
                    <fmt:message key="button.searchAgain" />
                </a>
            </span>
            <s:form id="pharmaceuticalCompanyForm" action="savePharmaceuticalCompany">
                <s:if test="pharmaceuticalCompany.nesId in {null, ''}">
                    <div class="formcol">
                       <s:textfield id="pharmaceuticalCompany.name" name="pharmaceuticalCompany.name" maxlength="160" size="30" requiredLabel="true"
                           cssStyle="width: 19em;" label="%{getText('textfield.organization.name')}" />
                    </div>

                    <div class="dotted_line"></div>

                    <div class="formcol">
                       <s:textfield id="pharmaceuticalCompany.email" name="pharmaceuticalCompany.email" maxlength="50" size="30" cssStyle="width: 19em;"
                           label="%{getText('textfield.emailAddress')}" requiredLabel="true"/>
                    </div>

                    <div class="formcol">
                        <firebird:phoneNumber beanPrefix="pharmaceuticalCompany" required="true"/>
                    </div>

                    <div class="dotted_line"></div>

                    <firebird:addressFields beanPrefix="pharmaceuticalCompany.postalAddress" />
                </s:if>
                <s:else>
                    <firebird:organizationDisplay organization="${pharmaceuticalCompany}"/>
                </s:else>

                <s:hidden name="searchKey"/>

                <div class="btn_bar clear">
                    <a id="saveButton" href="javascript:void(0);" class="button"><fmt:message key="button.save"/></a>
                    <s:a id="cancelPharmaceuticalCompanyButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>
                <s:hidden name="profile.id" value="%{profile.id}" />
                <s:hidden name="registration.id" value="%{registration.id}" />
            </s:form>
        </div>
    </div>
</div>
<s:url var="searchPharmaceuticalCompanyUrl" action="enterAddPharmaceuticalCompany" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}" />
    <s:param name="registration.id" value="%{registration.id}" />
</s:url>
<script>
$(document).ready(function() {
    $('#searchAgainButton').click(function() {
        var url = '${searchPharmaceuticalCompanyUrl}';
        var target = getCurrentDialog();
        _fbUtil.performAjaxPost(url, target);
     });

    $('#saveButton').click(function() {
        var url = $('#pharmaceuticalCompanyForm').attr('action');
        var formData = $('#pharmaceuticalCompanyForm').serialize();
        var target = getCurrentDialog();
        _fbUtil.performAjaxPost(url, target, formData);
     });
    $("input").keydown(testForEnter);
});
</script>