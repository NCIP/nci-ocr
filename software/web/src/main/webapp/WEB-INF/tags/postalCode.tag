<%@ tag body-content="empty" %>
<%@ attribute name="beanPrefix" required="false" %>
<%@ attribute name="varPrefix" required="false" %>

<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<s:set var="elementId" value="%{#attr.beanPrefix + '.postalCode'}" />
<s:set var="prefix" value="%{#attr.varPrefix.replaceAll('\\\\.','_')}"/>

<s:textfield
        id="%{#elementId}"
        name="%{#elementId}"
        maxlength="20" size="20" cssStyle="width: 19em;" requiredLabel="true"
        label="%{getText('textfield.zipCode')}" labelposition="left" />

<script>
var ${prefix}_addressForm = (${prefix}_addressForm) ? ${prefix}_addressForm : {};

${prefix}_addressForm.postalCodeField = {
	addressForm : ${prefix}_addressForm,
	postalCodeFieldId : escapeForJquery('#<s:property value="%{#elementId}"/>'),
	postalCodeLabelSelector : 'label[for="<s:property value="%{#elementId}"/>"]',
	updateMask : function() {
        if (this.addressForm.isUSSelected()) {
            $(this.postalCodeFieldId).mask('99999?-999999');
            $(this.postalCodeLabelSelector).html('<span class="required">*</span> <fmt:message key="textfield.zipCode" />')
        } else {
            $(this.postalCodeFieldId).unmask();
            $(this.postalCodeLabelSelector).html('<span class="required">*</span> <fmt:message key="textfield.postalCode" />')
        }
    }
};
</script>