<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<firebird:confirmDeleteCredentialDialog titleKey="label.residencies" >
    <s:form id="residencyFieldsForm" action="deleteResidency">

        <div class="formcol">
            <firebird:label messageKey="label.certifying.board" bold="true" />
            <div id="certifyingBoard"><s:property value="residency.specialty.certifyingBoard.name"/></div>
        </div>

        <div class="formcol">
            <firebird:label messageKey="label.specialty" bold="true" />
            <div id="specialty"><s:property value="residency.specialty.name"/></div>
        </div>

        <div class="clear"></div>

        <div class="formcol">
            <firebird:label messageKey="label.effective.date" bold="true" />
            <div id="effectiveDate"><s:date name="residency.effectiveDate" format="MM/yyyy"/></div>
        </div>
        <div class="formcol">
            <firebird:label messageKey="label.expiration.date" bold="true" />
            <div id="expirationDate"><s:date name="residency.expirationDate" format="MM/yyyy"/></div>
        </div>
       <div class="clear"><br></div>

       <firebird:label messageKey="label.issuing.organizatiion" bold="true" />
       <div id="issuerDisplay">
          <firebird:organizationDisplay organization="${residency.issuer}"/>
        </div>

        <s:hidden name="id"/>
        <s:hidden name="profile.id" value="%{profile.id}"/>

        <div class="clear"><br></div>
        <firebird:profileButtonBar isDelete="true" idPrefix="deleteCredential" form="residencyFieldsForm"/>

    </s:form>
</firebird:confirmDeleteCredentialDialog>
