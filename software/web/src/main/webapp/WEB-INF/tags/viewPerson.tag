<%@ tag body-content="empty" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<%@ attribute name="person" type="gov.nih.nci.firebird.data.Person" rtexprvalue="true" required="true" %>
<%@ attribute name="tagVariableName" description="Variable name for this organization display tag and enclosing div ID" %>
<%@ attribute name="showCtepId" type="java.lang.Boolean" %>
<%@ attribute name="showProviderNumber" type="java.lang.Boolean" description="Optionally display the Provider Number field" %>
<%@ attribute name="ctepIdLabelKey" description="Label key for the CTEP ID field. Defaults to label.ctep.id" %>

<s:set var="ctepIdLabelKey" value="%{#attr.ctepIdLabelKey in #{null,''} ? 'label.ctep.id' : #attr.ctepIdLabelKey}"/>

<s:if test="%{#attr.tagVariableName in #{null,''}}">
    <c:set var="tagVariableName" value="personDisplayTag" />
</s:if>

<div id="${tagVariableName}">
  <div class="formcol">
      <firebird:label messageKey="profile.person.displayName" bold="true" />
      <div id="personDisplayName">${person.displayName}</div>

      <firebird:label messageKey="profile.postalAddress" bold="true" />
      <div id="personDisplayAddress"><firebird:addressDisplay address="${person.postalAddress}" tagVariableName="${tagVariableName}Address"/></div>
  </div>

  <div class="formcol">
      <firebird:label messageKey="profile.email" bold="true" />
      <div id="personDisplayEmail">${person.email}</div>

      <firebird:label messageKey="profile.phoneNumber" bold="true" />
      <div id="personDisplayPhone"></div>

      <s:if test="%{#attr.showProviderNumber}">
        <firebird:label messageKey="profile.providerNumber" bold="true" />
        <div id="personProviderNumber">${person.providerNumber}</div>
      </s:if>
  </div>
  <s:if test="%{#attr.showCtepId}">
      <firebird:label messageKey="${ctepIdLabelKey}" bold="true" />
      <div id="personCtepId">${person.ctepId}</div>
  </s:if>
</div>

<script>
var ${tagVariableName} = {
    updatePhoneFormat : function(shouldFormat, personPhoneNumber) {
        if (shouldFormat) {
            personPhoneNumber = personPhoneNumber.replace("(", "").replace(")", "");
        }
        $("#${tagVariableName} #personDisplayPhone").text(personPhoneNumber);
    },
    setPerson: function(person) {
        $("#${tagVariableName} #personDisplayName").text(person.displayName);
        ${tagVariableName}Address.setAddress(person.postalAddress);
        $("#${tagVariableName} #personDisplayEmail").text(person.email);
        $("#${tagVariableName} #personDisplayPhone").text(person.phoneNumber);
        $("#${tagVariableName} #personCtepId").text(person.ctepId);
        this.updatePhoneFormat(person.postalAddress.usAddress, person.phoneNumber);
    }
};

$(document).ready(function() {
  ${tagVariableName}.updatePhoneFormat("${person.postalAddress.usAddress}", "${person.phoneNumber}");
});
</script>