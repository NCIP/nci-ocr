<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<firebird:confirmDeleteCredentialDialog titleKey="label.internships" >
    <s:form id="internshipFieldsForm" action="deleteInternship">

        <div class="formcol">
            <firebird:label messageKey="label.certifying.board" bold="true" />
            <div id="certifyingBoard"><s:property value="internship.specialty.certifyingBoard.name"/></div>
        </div>

        <div class="formcol">
            <firebird:label messageKey="label.specialty" bold="true" />
            <div id="specialty"><s:property value="internship.specialty.name"/></div>
        </div>

        <div class="clear"></div>

        <div class="formcol">
            <firebird:label messageKey="label.effective.date" bold="true" />
            <div id="effectiveDate"><s:date name="internship.effectiveDate" format="MM/yyyy"/></div>
        </div>
        <div class="formcol">
            <firebird:label messageKey="label.expiration.date" bold="true" />
            <div id="expirationDate"><s:date name="internship.expirationDate" format="MM/yyyy"/></div>
        </div>
       <div class="clear"><br></div>

       <firebird:label messageKey="label.issuing.organizatiion" bold="true" />
       <div id="issuerDisplay">
          <firebird:organizationDisplay organization="${internship.issuer}"/>
        </div>

        <s:hidden name="id"/>
        <s:hidden name="profile.id" value="%{profile.id}"/>

        <div class="clear"><br></div>
        <firebird:profileButtonBar isDelete="true" idPrefix="deleteCredential" form="internshipFieldsForm"/>

    </s:form>
</firebird:confirmDeleteCredentialDialog>
