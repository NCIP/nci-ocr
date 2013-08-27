<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="createNewOrgAssocUrl" namespace="/investigator/profile/associations/org/ajax" action="selectAssociatedOrganization" escapeAmp="false" >
    <s:param name="associationType" value="%{associationType}"/>
    <s:param name="profile.id" value="%{profile.id}"/>
    <s:param name="ohrpRequired" value="%{ohrpRequired}"/>
</s:url>

<s:if test="%{associationType == @gov.nih.nci.firebird.data.OrganizationRoleType@CLINICAL_LABORATORY}" >
    <s:set var="searchUrl" value="'searchForClinicalLabs'"/>
</s:if>
<s:elseif test="%{associationType == @gov.nih.nci.firebird.data.OrganizationRoleType@PRACTICE_SITE}">
    <s:set var="searchUrl" value="'searchForPracticeSites'"/>
</s:elseif>
<s:else>
    <s:set var="searchUrl" value="'searchForIrbs'"/>
</s:else>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader>
                <s:if test="%{assocOrg == null || assocOrg.id == null}">
                    <fmt:message key="organization.association.title.${associationType}.add" />
                </s:if>
                <s:else>
                    <fmt:message key="organization.association.title.${associationType}.edit" />
                </s:else>
            </firebird:dialogHeader>
            <firebird:messages/>
            <s:form id="selectAssociatedOrgForm" action="selectAssociatedOrganization" namespace="/investigator/profile/associations/org/ajax" onsubmit="return false">
                <s:hidden id="searchKey" name="searchKey" />
                <s:hidden name="associationType" />
                <s:hidden name="profile.id" value="%{profile.id}" />
                <s:hidden name="ohrpRequired" value="%{ohrpRequired}" />
            </s:form>
            <firebird:organizationSearch labelKey="credentials.degree.search.label" required="true" searchAction="${searchUrl}" setFocus="true">
                <s:a id="createNewOrgAssocButton" cssClass="button" href="javascript:void(0);" onclick="createNewOrganizationAssociation();"><fmt:message key="button.create.new"/></s:a>
                <s:a id="cancelOrganizationAssociationBtn" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
            </firebird:organizationSearch>
        </div>
    </div>
</div>

<script type="text/javascript">
       $(document).ready(function() {
           setFocusToFirstControl();

           organizationSearch.clickSelectButton = function(organizationId) {
               $('#searchKey').val(organizationId);
               var url = $('#selectAssociatedOrgForm').attr('action');
               var serializedForm = $('#selectAssociatedOrgForm').serialize();
               var target = getCurrentDialog();
               _fbUtil.performAjaxPost(url, target, serializedForm);
           };
       });

       function createNewOrganizationAssociation() {
           var url = '${createNewOrgAssocUrl}';
           var target = getCurrentDialog();
           _fbUtil.performAjaxPost(url, target);
       }
</script>