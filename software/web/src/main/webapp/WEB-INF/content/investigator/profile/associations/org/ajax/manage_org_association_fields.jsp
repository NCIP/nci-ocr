<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<!--Content-->
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader>
                <fmt:message key="organization.association.title.${associationType}.add" />
            </firebird:dialogHeader>
            <firebird:messages/>
            <firebird:searchAgainLink id="orgAssociationSearchAgain"/>
            <s:form id="associatedOrgForm" action="save">
                <s:if test="associatedOrganization.externalId in {null, ''}">
                    <div class="formcol">
                        <s:textfield id="associatedOrganization.name"
                                     name="associatedOrganization.name"
                                     maxlength="160"
                                     size="30"
                                     requiredLabel="true"
                                     cssStyle="width: 19em;"
                                     label="%{getText('textfield.organization.name')}"/>
                    </div>

                </s:if>

                <s:if test="%{associationType == @gov.nih.nci.firebird.data.OrganizationRoleType@PRACTICE_SITE}" >
                    <div class="clear"><br></div>
                    <div id="typeData" class="formcol">
                        <s:textfield id="associatedOrganizationOhrp"
                                     name="associationOhrp"
                                     maxlength="%{@gov.nih.nci.firebird.data.PracticeSite@OHRP_ASSURANCE_NUMBER_LENGTH}"
                                     size="20"
                                     requiredLabel="%{ohrpRequired}"
                                     cssStyle="width: 19em;"
                                     label="%{getText('organization.association.type.data.' + associationType)}"/>
                    </div>
                    <div class="clear"><br></div>

                    <s:if test="associatedOrganization.externalId in {null, ''}">
                        <div class="formcol clear">
                            <s:select id="practiceSiteType"
                                      name="practiceSiteType"
                                      list="%{@gov.nih.nci.firebird.data.PracticeSiteType@values()}"
                                      listValue="display"
                                      headerValue="%{getText('select.type.default.option')}"
                                      headerKey=""
                                      label="%{getText('label.type')}"
                                      requiredLabel="true"/>

                        </div>
                        <div class="clear"><br></div>
                    </s:if>
                </s:if>

                <s:if test="associatedOrganization.externalId in {null, ''}">
                    <div class="dotted_line"></div>

                    <div class="formcol">
                       <s:textfield id="associatedOrganization.email" name="associatedOrganization.email" maxlength="50" size="30" cssStyle="width: 19em;"
                           label="%{getText('textfield.emailAddress')}" requiredLabel="true" />
                    </div>

                    <s:set value="associationType == @gov.nih.nci.firebird.data.OrganizationRoleType@PRACTICE_SITE && profile.user.ctepUser" var="phoneRequired"/>
                    <div class="formcol">
                        <firebird:phoneNumber beanPrefix="associatedOrganization"
                        required="${phoneRequired}" />
                    </div>

                    <div class="dotted_line"></div>

                    <firebird:addressFields beanPrefix="associatedOrganization.postalAddress" />
                </s:if>
                <s:else>
                    <firebird:organizationDisplay organization="${associatedOrganization}"/>
                </s:else>

                <s:hidden name="associationType" />
                <s:hidden name="organizationExternalId"/>

                <div class="btn_bar clear">
                    <a id="saveOrganizationAssociationBtn" href="javascript:void(0);" class="button"><fmt:message key="button.save"/></a>
                    <s:a id="cancelOrganizationAssociationBtn" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>
                <s:hidden name="profile.id" value="%{profile.id}" />
                <s:hidden name="ohrpRequired" value="%{ohrpRequired}"/>
            </s:form>
        </div>
    </div>
</div>
<s:url var="searchAssociatedOrgUrl" namespace="/investigator/profile/associations/org/ajax" action="manageAssociatedOrganizationAjaxEnter">
    <s:param name="associationType" value="associationType" />
</s:url>
<script>
$(document).ready(function() {
    $('#orgAssociationSearchAgain').click(function() {
        var url = '<s:property value="%{#searchAssociatedOrgUrl}"/>';
        var target = getCurrentDialog();
        _fbUtil.performAjaxPost(url, target);
     });

    $('#saveOrganizationAssociationBtn').click(function() {
        var url = $('#associatedOrgForm').attr('action');
        var formData = $('#associatedOrgForm').serialize();
        var target = getCurrentDialog();
        _fbUtil.performAjaxPost(url, target, formData);
     });
    $("input").keydown(testForEnter);
});
</script>