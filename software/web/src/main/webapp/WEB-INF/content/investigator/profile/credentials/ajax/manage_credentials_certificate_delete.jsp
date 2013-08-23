<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:confirmDeleteCredentialDialog titleKey="credentials.certificate.title" >
    <s:form id="deleteCertificateForm" action="deleteCertificate">
        <div class="formcol">
            <firebird:label messageKey="dropdown.certificateType" bold="true" />
            <div id="certificateType"><s:property value="%{getText(certificate.certificateType.nameProperty)}"/></div>

            <firebird:label messageKey="textfield.effectiveDate" bold="true" />
            <s:date name="certificate.effectiveDate" format="MM/yyyy" var="effDate"/>
            <div id="effectiveDate">${certificate.effectiveDate == null ? 'N/A' : effDate}</div>

            <firebird:label messageKey="textfield.expirationDate" bold="true" />
            <s:date name="certificate.expirationDate" format="MM/yyyy" var="expDate"/>
            <div id="expirationDate">${certificate.expirationDate == null ? 'N/A' : expDate}</div>
        </div>
        <div class="formcol_wide clear">
            <firebird:label messageKey="label.issuing.organizatiion" bold="true" />
            <div id="issuingOrganization">${certificate.issuer.name}</div>
        </div>

        <s:hidden name="id"/>
        <s:hidden name="profile.id" value="%{profile.id}"/>

        <firebird:profileButtonBar isDelete="true" idPrefix="deleteCertificate" form="deleteCertificateForm"/>

    </s:form>
</firebird:confirmDeleteCredentialDialog>