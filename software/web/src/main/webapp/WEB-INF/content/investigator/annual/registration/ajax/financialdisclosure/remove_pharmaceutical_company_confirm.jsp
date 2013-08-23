<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url action="removePharmaceuticalCompany" var="removePharmaceuticalCompanyUrl" escapeAmp="false">
    <s:param name="registration.id" value="%{registration.id}" />
    <s:param name="pharmaceuticalCompanyExternalId" value="%{pharmaceuticalCompanyExternalId}" />
</s:url>
<firebird:confirmDialog titleKey="remove.pharmaceutical.company.confirm.title" confirmUrl="${removePharmaceuticalCompanyUrl}" dialogId="registrationDialog">
    <h3>
        <fmt:message key="remove.pharmaceutical.company.confirm.text">
            <fmt:param>${pharmaceuticalCompany.name}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>