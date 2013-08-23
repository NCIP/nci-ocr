<%@ tag body-content="empty" %>
<%-- Either beanPrefix or id must be specified --%>
<%@ attribute name="beanPrefix" required="false" %>
<%@ attribute name="varPrefix" required="false" %>
<%@ attribute name="fieldsDisabled" required="false" rtexprvalue="true"%>
<%@ attribute name="required" required="false" rtexprvalue="true"%>

<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<s:set var="elementId" value="%{#attr.beanPrefix + '.phoneNumber'}" />
<s:set var="prefix" value="%{#attr.varPrefix.replaceAll('\\\\.','_')}"/>

<s:textfield id="%{#elementId}" name="%{#elementId}" maxlength="25" size="25" cssStyle="width: 19em;"
       label="%{getText('textfield.phoneNumber')}" requiredLabel="%{#attr.required}" disabled="%{#attr.fieldsDisabled}"/>
<script>
var ${prefix}_addressForm = ${prefix}_addressForm || {};

${prefix}_addressForm.phoneNumberField = {
    phoneNumberFieldId : escapeForJquery('#<s:property value="%{#elementId}"/>'),
    addressForm : ${prefix}_addressForm,
    updateMask : function() {
        if (this.addressForm.isUSSelected() || this.addressForm.isCANSelected()) {
            $(this.phoneNumberFieldId).mask('999-999-9999?x99999');
        } else {
            $(this.phoneNumberFieldId).unmask();
        }
    }
};
</script>