<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="deleteAnnualRegistrationUrl" action="deleteAnnualRegistration">
    <s:param name="registration.id" value="registration.id" />
</s:url>
<firebird:confirmDialog titleKey="investigator.delete.annual.registration.title" confirmUrl="${deleteAnnualRegistrationUrl}" dialogId="registrationDialog">
    <p><fmt:message key="investigator.delete.annual.registration.message" /></p>
</firebird:confirmDialog>
