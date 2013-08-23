<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:confirmDeleteCredentialDialog titleKey="credentials.license.title" >
    <s:form id="deleteLicenseForm" namespace="/investigator/profile/credentials/ajax" action="deleteLicense">
        <div class="formcol_wide">
            <firebird:label messageKey="dropdown.licenseType" bold="true" />
            <div id="credentialsLicenseType"><s:property value="license.licenseType.name"/></div>

            <firebird:label messageKey="dropdown.state" bold="true" />
            <div id="licenseState"><s:property value="license.state"/></div>
        </div>

        <div class="formcol">
            <firebird:label messageKey="textfield.license.id" bold="true" />
            <div id="licenseId"><s:property value="license.licenseId"/></div>

            <firebird:label messageKey="textfield.expirationDate" bold="true" />
            <div id="expirationDate"><s:date name="license.expirationDate" format="MM/yyyy"/></div>
        </div>

        <s:hidden name="id"/>
        <s:hidden name="profile.id" value="%{profile.id}"/>

        <div class="clear"> </div>

        <firebird:profileButtonBar isDelete="true" idPrefix="deleteLicense" form="deleteLicenseForm"/>

    </s:form>
</firebird:confirmDeleteCredentialDialog>