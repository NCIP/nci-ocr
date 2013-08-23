<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:confirmDeleteCredentialDialog titleKey="credentials.specialty.title" >
    <s:form id="specialtyFieldsForm" namespace="/investigator/profile/credentials/ajax" action="deleteSpecialty">
        <div class="formcol_wide">
            <firebird:label messageKey="dropdown.boardName" bold="true" />
            <div id="specialtyBoard"><s:property value="specialty.specialtyType.board.name"/></div>

            <firebird:label messageKey="dropdown.specialtyStatus" bold="true" />
            <div id="specialtyStatus"><s:property value="specialty.status.display"/></div>
        </div>

        <div class="formcol">
            <firebird:label messageKey="dropdown.specialtyType" bold="true" />
            <div id="specialtyType"><s:property value="specialty.specialtyType.display"/></div>

            <firebird:label messageKey="textfield.effectiveDate" bold="true" />
             <div id="effectiveDate"><s:date name="specialty.effectiveDate" format="MM/yyyy"/></div>
        </div>

        <s:hidden name="id"/>
        <s:hidden name="profile.id" value="%{profile.id}"/>

        <div class="clear"> </div>

        <firebird:profileButtonBar isDelete="true" idPrefix="deleteSpecialty" form="specialtyFieldsForm"/>

    </s:form>
</firebird:confirmDeleteCredentialDialog>