<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<firebird:confirmDeleteCredentialDialog titleKey="label.work.history" >
    <s:form id="workHistoryFieldsForm" action="deleteWorkHistory">

        <div class="formcol">
            <firebird:label messageKey="label.position" bold="true" />
            <div id="position"><s:property value="workHistory.position"/></div>
        </div>

        <div class="clear"></div>

        <div class="formcol">
            <firebird:label messageKey="label.effective.date" bold="true" />
            <div id="effectiveDate"><s:date name="workHistory.effectiveDate" format="MM/yyyy"/></div>
        </div>
        <div class="formcol">
            <firebird:label messageKey="label.expiration.date" bold="true" />
            <div id="expirationDate"><s:date name="workHistory.expirationDate" format="MM/yyyy"/></div>
        </div>
       <div class="clear"><br></div>

       <firebird:label messageKey="label.issuing.organizatiion" bold="true" />
       <div id="organizatiionDisplay">
          <firebird:organizationDisplay organization="${workHistory.issuer}"/>
        </div>

        <s:hidden name="id"/>
        <s:hidden name="profile.id" value="%{profile.id}"/>

        <div class="clear"><br></div>
        <firebird:profileButtonBar isDelete="true" idPrefix="deleteWorkHistory" form="workHistoryFieldsForm"/>

    </s:form>
</firebird:confirmDeleteCredentialDialog>
