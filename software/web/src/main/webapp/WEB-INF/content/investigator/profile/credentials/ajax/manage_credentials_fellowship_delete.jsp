<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<firebird:confirmDeleteCredentialDialog titleKey="label.fellowships" >
    <s:form id="fellowshipFieldsForm" action="deleteFellowship">

        <div class="formcol">
            <firebird:label messageKey="label.certifying.board" bold="true" />
            <div id="certifyingBoard"><s:property value="fellowship.specialty.certifyingBoard.name"/></div>
        </div>

        <div class="formcol">
            <firebird:label messageKey="label.specialty" bold="true" />
            <div id="specialty"><s:property value="fellowship.specialty.name"/></div>
        </div>

        <div class="clear"></div>

        <div class="formcol">
            <firebird:label messageKey="label.effective.date" bold="true" />
            <div id="effectiveDate"><s:date name="fellowship.effectiveDate" format="MM/yyyy"/></div>
        </div>
        <div class="formcol">
            <firebird:label messageKey="label.expiration.date" bold="true" />
            <div id="expirationDate"><s:date name="fellowship.expirationDate" format="MM/yyyy"/></div>
        </div>
       <div class="clear"><br></div>

       <firebird:label messageKey="label.issuing.organizatiion" bold="true" />
       <div id="issuerDisplay">
          <firebird:organizationDisplay organization="${fellowship.issuer}"/>
        </div>

        <s:hidden name="id"/>
        <s:hidden name="profile.id" value="%{profile.id}"/>

        <div class="clear"><br></div>
        <firebird:profileButtonBar isDelete="true" idPrefix="deleteCredential" form="fellowshipFieldsForm"/>

    </s:form>
</firebird:confirmDeleteCredentialDialog>
