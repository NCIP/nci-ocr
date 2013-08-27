<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="createPharmaceuticalCompanyUrl" namespace="/investigator/annual/registration/ajax/financialdisclosure" action="selectPharmaceuticalCompany" escapeAmp="false" >
    <s:param name="profile.id" value="%{profile.id}"/>
    <s:param name="registration.id" value="%{registration.id}"/>
</s:url>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader>
                <s:if test="%{pharmaceuticalCompany == null || pharmaceuticalCompany.id == null}">
                    <fmt:message key="annual.registration.financial.disclosure.add.pharmaceutical.company.title" />
                </s:if>
                <s:else>
                    <fmt:message key="annual.registration.financial.disclosure.edit.pharmaceutical.company.title" />
                </s:else>
            </firebird:dialogHeader>
            <firebird:messages/>
            <s:form id="selectPharmaceuticalCompanyForm" action="selectPharmaceuticalCompany" namespace="/investigator/annual/registration/ajax/financialdisclosure" onsubmit="return false">
                <s:hidden id="pharmaceuticalCompanyId" name="pharmaceuticalCompanyId" />
                <s:hidden name="profile.id" value="%{profile.id}" />
                <s:hidden name="registration.id" value="%{registration.id}" />
            </s:form>
            <firebird:organizationSearch labelKey="credentials.degree.search.label" required="true" searchAction="searchPharmaceuticalCompanies" setFocus="true">
                <sj:a id="createPharmaceuticalCompanyButton" cssClass="button" href="%{createPharmaceuticalCompanyUrl}" targets="registrationDialog"><fmt:message key="button.create.new"/></sj:a>
                <s:a id="cancelPharmaceuticalCompanyButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
            </firebird:organizationSearch>
        </div>
    </div>
</div>

<script type="text/javascript">
       $(document).ready(function() {
           setFocusToFirstControl();

           organizationSearch.clickSelectButton = function(pharmaceuticalCompanyId) {
               $('#pharmaceuticalCompanyId').val(pharmaceuticalCompanyId);
               var url = $('#selectPharmaceuticalCompanyForm').attr('action');
               var serializedForm = $('#selectPharmaceuticalCompanyForm').serialize();
               var target = getCurrentDialog();
               _fbUtil.performAjaxPost(url, target, serializedForm);
           };
       });
</script>