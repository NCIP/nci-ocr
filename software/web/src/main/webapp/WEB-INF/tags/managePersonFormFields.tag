<%@ tag body-content="empty" %>
<%@ attribute name="beanPrefix" required="true" %>
<%@ attribute name="fieldsDisabled" required="false" rtexprvalue="true"%>
<%@ attribute name="requirePhoneNumber" required="false"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<%@ attribute name="showProviderNumber" type="java.lang.Boolean"
              description="Optionally display the Provider Number field" %>

<div id="personFields">

    <div class="formcol_xxthin">
        <s:select list="#{'' :'-','Mr.': 'Mr.','Mrs.': 'Mrs.','Ms.': 'Ms.','Dr.': 'Dr.'}" id="%{#attr.beanPrefix}Prefix"
        name="%{#attr.beanPrefix}.prefix" label="%{getText('dropdown.namePrefix')}"/>
        <s:if test="%{#attr.fieldsDisabled}">
            <s:hidden name="%{#attr.beanPrefix}.prefix"/>
        </s:if>
    </div>

    <div class="formcol">
        <s:textfield id="%{#attr.beanPrefix}FirstName" name="%{#attr.beanPrefix}.firstName" maxlength="50" size="30"
        cssStyle="width: 19em;" label="%{getText('textfield.firstName')}" requiredLabel="true"/>
    </div>

    <div class="formcol_xxthin">
      <s:textfield id="%{#attr.beanPrefix}MiddleName" name="%{#attr.beanPrefix}.middleName"
            maxlength="50" size="3"cssStyle="width: 3.5em;" label="%{getText('textfield.middleInitial')}"/>
    </div>

    <div class="formcol">
        <s:textfield id="%{#attr.beanPrefix}LastName" name="%{#attr.beanPrefix}.lastName"
            maxlength="50" size="30" cssStyle="width: 19em;" label="%{getText('textfield.lastName')}" requiredLabel="true"/>
    </div>

    <div class="formcol_xxthin">
        <s:textfield id="%{#attr.beanPrefix}Suffix" name="%{#attr.beanPrefix}.suffix"
            maxlength="10" size="3" cssStyle="width: 3.5em;" label="%{getText('textfield.nameSuffix')}"/>
    </div>

    <div class="dotted_line"><br></div>

    <div class="formcol">
        <s:textfield id="%{#attr.beanPrefix}Email" name="%{#attr.beanPrefix}.email"
            maxlength="50" size="30" cssStyle="width: 19em;" label="%{getText('textfield.emailAddress')}" requiredLabel="true"/>
    </div>

    <div class="formcol">
        <firebird:phoneNumber beanPrefix="${beanPrefix}" required="${requirePhoneNumber}" varPrefix="${beanPrefix}"/>
    </div>

    <s:if test="%{#attr.showProviderNumber}">
    <div class="formcol">
        <s:textfield id="%{#attr.beanPrefix}.providerNumber" name="%{#attr.beanPrefix}.providerNumber"
            maxlength="50" size="30" cssStyle="width: 19em;" label="%{getText('textfield.providerNumber')}" />
    </div>
    </s:if>

    <div class="dotted_line"><br>
    </div>

    <firebird:addressFields beanPrefix="${beanPrefix}.postalAddress" fieldsDisabled="${fieldsDisabled}" varPrefix="${beanPrefix}"/>

</div>

<s:hidden name="%{#attr.beanPrefix}.externalId"/>
<s:hidden name="%{#attr.beanPrefix}.id"/>
<s:hidden name="profile.id" value="%{profile.id}"/>

<script>

$(document).ready(function () {
   if ('${fieldsDisabled}' == "true") {
       $('#personFields select').attr("disabled", true);
       $('#personFields input').attr("readonly", true);
   }
   $("input").keydown(testForEnter);
});
</script>
