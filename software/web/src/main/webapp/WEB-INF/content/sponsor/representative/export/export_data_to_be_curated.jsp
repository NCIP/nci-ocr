<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
<head>
<title><fmt:message key="sponsor.export.data.title" /></title>
</head>
<body>
    <h1>
        <fmt:message key="sponsor.export.data.title" />
    </h1>

    <s:url namespace="/sponsor/representative/export/ajax" action="viewPersonsToCurate" var="viewPersonsToCurateUrl" />
    <s:url namespace="/sponsor/representative/export/ajax" action="viewOrganizationsToCurate" var="viewOrganizationsToCurateUrl" />
    <s:url namespace="/sponsor/representative/export/ajax" action="viewOrganizationRolesToCurate" var="viewOrganizationRolesToCurateUrl" />

    <sj:tabbedpanel id="tabwrapper"
        useSelectedTabCookie="false"
        selectedTab="%{#parameters['selectedTab']}">
        <sj:tab id="personsTab" href="%{viewPersonsToCurateUrl}"
            label="%{getText('sponsor.export.persons.tab.title')}" />
        <sj:tab id="organizationsTab" href="%{viewOrganizationsToCurateUrl}"
            label="%{getText('sponsor.export.organizations.tab')}" />
        <sj:tab id="organizationRolesTab" href="%{viewOrganizationRolesToCurateUrl}"
            label="%{getText('sponsor.export.organization.roles.tab')}" />
    </sj:tabbedpanel>
</body>
</html>