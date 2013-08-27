<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<h1><fmt:message key="user.registration.primary.organization.fields.title"/></h1>
<!--Content-->
<s:form action="nextStep" id="primaryOrgForm">
    <div id="tabwrapper">
        <div class="ui-tabs">
            <div class="ui-tabs-panel">
                <s:if test="%{accountConfigurationData.primaryOrganization.organization.nesId == null
                    || accountConfigurationData.primaryOrganization.organization.nesId == ''}">

                    <div class="formcol clear">
                        <s:select id="accountConfigurationData.primaryOrganization.type"
                                  name="accountConfigurationData.primaryOrganization.type"
                                  list="%{@gov.nih.nci.firebird.data.PrimaryOrganizationType@values()}"
                                  listValue="display"
                                  headerValue="%{getText('select.type.default.option')}"
                                  headerKey=""
                                  label="%{getText('label.type')}"
                                  requiredLabel="true"/>

                    </div>
                    <div class="clear"><br></div>

                    <div class="formcol">
                       <s:textfield id="accountConfigurationData.primaryOrganization.organization.name" name="accountConfigurationData.primaryOrganization.organization.name" maxlength="160" size="30" requiredLabel="true"
                           cssStyle="width: 19em;" label="%{getText('textfield.organization.name')}"
                          />
                    </div>

                    <div class="formcol">
                       <s:textfield id="accountConfigurationData.primaryOrganization.organization.email" name="accountConfigurationData.primaryOrganization.organization.email" maxlength="50" size="30" cssStyle="width: 19em;"
                           label="%{getText('textfield.emailAddress')}" requiredLabel="true"/>
                    </div>

                    <div class="formcol">
                        <firebird:phoneNumber beanPrefix="accountConfigurationData.primaryOrganization.organization"/>
                    </div>

                    <div class="dotted_line"><br></div>

                    <firebird:addressFields beanPrefix="accountConfigurationData.primaryOrganization.organization.postalAddress" />

                </s:if>
                <s:else>
                    <div class="formcol">
                        <firebird:label messageKey="label.name" bold="true"/>
                        <div id="orgName">${accountConfigurationData.primaryOrganization.organization.name}</div>

                        <firebird:label messageKey="label.address" bold="true"/>
                        <div id="orgAddress">
                            <firebird:addressDisplay address="${accountConfigurationData.primaryOrganization.organization.postalAddress}"/>
                        </div>
                    </div>

                    <div class="formcol">
                        <firebird:label messageKey="label.email" bold="true"/>
                        <div id="orgEmail">${accountConfigurationData.primaryOrganization.organization.email}</div>

                        <firebird:label messageKey="label.phone" bold="true"/>
                        <div id="orgPhone">${accountConfigurationData.primaryOrganization.organization.phoneNumber}</div>

                        <firebird:label messageKey="label.ctep.id" bold="true"/>
                        <div id="orgCtepId">${accountConfigurationData.primaryOrganization.organization.ctepId}</div>
                    </div>
                </s:else>

                <div class="clear"><br></div>
                <div class="btn_bar">
                    <a id="searchDifferentOrganization" class="search btn" href="#"><fmt:message key="button.searchAgain" /></a>
                    <firebird:userRegistrationPreviousButton/>
                    <firebird:userRegistrationNextButton/>
                </div>
            </div>
        </div>
    </div>
</s:form>

<script>

$(document).ready(function(){
    $('#searchDifferentOrganization').click(function() { clickUserRegistrationSelectButton('<s:url action="currentStep"/>', {navigationOption: 'search'}) });
});

</script>