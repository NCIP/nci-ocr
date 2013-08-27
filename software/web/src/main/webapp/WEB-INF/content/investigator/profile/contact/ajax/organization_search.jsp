<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/profile/contact/ajax" action="enterCreatePrimaryOrganization" var="orgCreateUrl" >
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader><fmt:message key="investigator.profile.contactInformation.organization" /></firebird:dialogHeader>
            <s:form id="selectForm" action="selectOrganizationAndClose" namespace="/investigator/profile/contact/ajax" onsubmit="return false">
                <s:hidden id="searchKey" name="searchKey" />
                <s:hidden name = "profile.id" value="%{profile.id}"/>
            </s:form>

            <firebird:organizationSearch labelKey="organization.search.label" required="true" searchAction="searchForPrimaryOrganizations" setFocus="true">
                <sj:a href="%{orgCreateUrl}" cssClass="button" id="orgCreateButton" targets="profileDialog"><fmt:message key="button.create.new"/></sj:a>
                <s:a id="cancelOrgButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
            </firebird:organizationSearch>
            <script type="text/javascript">
                $(document).ready(function() {
                    organizationSearch.clickSelectButton = function(organizationId) {
                        $('#searchKey').val(organizationId);
                        var url = $('#selectForm').attr('action');
                        var target = "#profileDialog";
                        var serializedForm = $('#selectForm').serialize();
                        _fbUtil.performAjaxPost(url, target, serializedForm);
                    };
                });
            </script>
        </div>
    </div>
</div>