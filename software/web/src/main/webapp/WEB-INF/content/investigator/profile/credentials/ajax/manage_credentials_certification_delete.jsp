<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:confirmDeleteCredentialDialog titleKey="credentials.certification.title" >
    <s:form id="deleteCertificationForm" namespace="/investigator/profile/credentials/ajax" action="deleteCertification">
        <div class="formcol_wide">
            <firebird:label messageKey="dropdown.certificationType" bold="true" />
            <div id="certificationType"><s:property value="certification.certificationType.name"/></div>
        </div>
        <div class="clear" ></div>

        <div class="formcol">
             <firebird:label messageKey="textfield.effectiveDate" bold="true" />
             <div id="effectiveDate"><s:date name="certification.effectiveDate" format="MM/yyyy"/></div>
        </div>

        <div id="expirationColumn" class="formcol">
             <firebird:label messageKey="textfield.expirationDate" bold="true" />
             <div id="expirationDate"><s:date name="certification.expirationDate" format="MM/yyyy"/></div>
        </div>

        <s:hidden name="id"/>
        <s:hidden name="profile.id" value="%{profile.id}"/>

        <firebird:profileButtonBar isDelete="true" idPrefix="deleteCertification" form="deleteCertificationForm"/>

    </s:form>
</firebird:confirmDeleteCredentialDialog>