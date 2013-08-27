<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<!--Content-->
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form id="removeOrgAssociationForm" namespace="/investigator/profile/associations/org/ajax" action="removeOrganizationAssociation">

                <firebird:dialogHeader>
                    <fmt:message key="organization.association.title.remove.association">
                        <fmt:param value="${associationType.display}"/>
                    </fmt:message>
                </firebird:dialogHeader>

                <firebird:instructionBubble messageKey="organization.association.remove.association.instructions" messageParam="${associationType.display}"/>
                <firebird:organizationDisplay organization="${associatedOrganization}"/>

                <s:hidden name="searchKey" value="%{searchKey}"/>
                <s:hidden name="associationType" value="%{associationType}"/>
                <s:hidden name="profile.id" value="%{profile.id}"/>

                <div class="clear"><br></div>
                <firebird:profileButtonBar isDelete="true" idPrefix="removeOrgAssociation" form="removeOrgAssociationForm"/>

            </s:form>
        </div>
    </div>
</div>
