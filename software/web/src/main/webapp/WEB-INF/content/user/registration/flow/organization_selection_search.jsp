<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h1><fmt:message key="user.registration.primary.organization.search.title"/></h1>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form id="selectOrganizationForm" action="nextStep" onsubmit="return false">
                <s:hidden id="selectedOrganizationExternalId" name="selectedOrganizationExternalId" />
            </s:form>
            <firebird:organizationSearch labelKey="organization.search.label" required="true" searchAction="searchForPrimaryOrganizations" setFocus="true"/>
            <div class="btn_bar clear">
                <firebird:userRegistrationPreviousButton/>
                <s:if test="%{#session[@gov.nih.nci.firebird.web.common.FirebirdUIConstants@ACCOUNT_CONFIGURATION_DATA].primaryOrganization != null}">
                    <s:a id="cancelBtn" cssClass="button" href="javascript:void(0)"><fmt:message key="button.cancel"/></s:a>
                </s:if>
                <s:a id="createPrimaryOrgNewBtn" cssClass="button" href="javascript:void(0)"><fmt:message key="button.create.new"/></s:a>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        organizationSearch.clickSelectButton = function(organization) {
            indicateLoading();
            $('#selectedOrganizationExternalId').val(organization.externalId);
            var url = $('form').first().attr('action');
            var serializedForm = $('form').serialize();
            $.post(url, serializedForm).success(function(data) {
                indicateLoading(false);
                $('#registrationContent').html(data);
            }).error(function (){
                enableDialog();
                setPageErrorMessages('<fmt:message key="error.problem.submitting.data"/>')
            });
        };

        $('#cancelBtn').click(function() { clickUserRegistrationSelectButton('<s:url action="currentStep"/>') });
        $('#createPrimaryOrgNewBtn').click(function() { clickUserRegistrationSelectButton('<s:url action="currentStep"/>', {navigationOption: 'NEW'}) });

    });
</script>
