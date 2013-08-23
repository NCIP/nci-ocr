<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ attribute name="buttonId" required="true"%>
<%@ attribute name="form" required="true" type="gov.nih.nci.firebird.data.AbstractRegistrationForm"%>
<%@ attribute name="useName" type="java.lang.Boolean" description="Use the forms name instead of description in the button text" %>

<s:set var="buttonText" value="%{#attr.useName ? #attr.form.formType.name : #attr.form.formType.description}"/>

<s:if test="registration.annualRegistration">
    <s:url action="downloadForm" namespace="/investigator/annual/registration/ajax" var="pdfUrl">
        <s:param name="registration.id" value="%{registration.id}"/>
        <s:param name="registrationForm.id" value="%{form.id}"/>
    </s:url>
</s:if><s:else>
    <s:url action="downloadForm" namespace="/investigator/registration/ajax" var="pdfUrl">
        <s:param name="registration.id" value="%{registration.id}"/>
        <s:param name="registrationForm.id" value="%{form.id}"/>
    </s:url>
</s:else>

<s:a id="%{#attr.buttonId}" href="%{#pdfUrl}" cssClass="button float" disabled="true">
    <fmt:message key="button.view.generated.form.pdf">
        <fmt:param value="${buttonText}"/>
    </fmt:message>
</s:a>
