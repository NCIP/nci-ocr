<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
<head>
<title><fmt:message key="investigator.profile.home" /></title>
</head>
<body>
    <h1>
        <fmt:message key="investigator.profile.home" /> <fmt:message key="label.separator"/> <span class="gray">${profile.person.displayName}</span>
    </h1>

    <%-- Dialog is defined in the main page to prevent problems when the tab information is reloaded when the dialog is closed --%>
    <sj:dialog id="profileDialog" autoOpen="false" modal="true" width="1000"
        position="top" onCloseTopics="dialogClosed" resizable="false" onOpenTopics="dialogOpened" />

    <s:url namespace="/investigator/profile/contact/ajax" action="view"
        var="contactUrl" >
        <s:param name="profile.id" value="%{profile.id}"/>
    </s:url>
    <s:url namespace="/investigator/profile/files/ajax" action="list"
        var="filesUrl">
        <s:param name="profile.id" value="%{profile.id}"/>
    </s:url>
    <s:url namespace="/investigator/profile/associations/org/ajax"
        action="enter" var="organizationAssociationUrl">
        <s:param name="profile.id" value="%{profile.id}"/>
    </s:url>
    <s:url namespace="/investigator/profile/associations/subinvestigators/ajax"
        action="enter" var="subinvestigatorAssociationUrl">
        <s:param name="profile.id" value="%{profile.id}"/>
    </s:url>
    <s:url namespace="/investigator/profile/associations/designees/ajax"
        action="enter" var="designeeAssociationUrl">
        <s:param name="profile.id" value="%{profile.id}"/>
    </s:url>
    <s:url namespace="/investigator/profile/credentials/ajax"
        action="enter" var="credentialsUrl">
        <s:param name="profile.id" value="%{profile.id}"/>
    </s:url>

    <sj:tabbedpanel id="tabwrapper"
        useSelectedTabCookie="false"
        selectedTab="%{#parameters['selectedTab']}">
        <sj:tab id="contactTab" href="%{contactUrl}"
            label="%{getText('label.contact.info')}" />
        <sj:tab id="credentialsTab" href="%{credentialsUrl}"
            label="%{getText('label.credentials')}" />
        <sj:tab id="orgsTab" href="%{organizationAssociationUrl}"
            label="%{getText('label.organization.associations')}" />
        <sj:tab id="subInvestigatorAssociationTab" href="%{subinvestigatorAssociationUrl}"
            label="%{getText('label.subinvestigators')}" />
        <s:if test="%{ctepProfile}">
          <sj:tab id="designeeAssociationTab" href="%{designeeAssociationUrl}"
              label="%{getText('label.designees')}" />
        </s:if>
        <sj:tab id="filesTab" href="%{filesUrl}" label="%{getText('label.files')}" />
    </sj:tabbedpanel>
</body>
</html>