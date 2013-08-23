<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:confirmDeleteCredentialDialog titleKey="credentials.degree.title" >
<s:form id="degreeFieldsForm" namespace="/investigator/profile/credentials/ajax" action="deleteDegree">
    <div class="formcol">
        <firebird:label messageKey="dropdown.degreeType" bold="true" />
        <div id="degreeType"><s:property value="degree.degreeType.name"/></div>
    </div>

    <div class="formcol">
        <firebird:label messageKey="textfield.effectiveDate.degree" bold="true" />
        <div id="effectiveDate"><s:date name="degree.effectiveDate" format="MM/yyyy"/></div>
    </div>
   <div class="clear"><br></div>

   <firebird:label messageKey="label.institution" bold="true" />
   <div id="institutionDisplay">
        <div class="formcol">
            <firebird:label messageKey="profile.organization.name" bold="true" />
            <div id="orgName"><s:property value="degree.issuer.name"/></div>

            <firebird:label messageKey="profile.postalAddress" bold="true" />
            <div id="orgAddress"><firebird:addressDisplay address="${degree.issuer.postalAddress }"/></div>
        </div>

        <div class="formcol">
            <firebird:label messageKey="profile.email" bold="true" />
            <div id="orgEmail"><s:property value="degree.issuer.email"/></div>

            <firebird:label messageKey="profile.phoneNumber" bold="true" />
            <div id="orgPhone"><s:property value="degree.issuer.phoneNumber"/></div>
        </div>
    </div>

    <s:hidden name="id"/>
    <s:hidden name="profile.id" value="%{profile.id}"/>

    <div class="clear"><br></div>
    <firebird:profileButtonBar isDelete="true" idPrefix="deleteDegree" form="degreeFieldsForm"/>

</s:form>
</firebird:confirmDeleteCredentialDialog>
