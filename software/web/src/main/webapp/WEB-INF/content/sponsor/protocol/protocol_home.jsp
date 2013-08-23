<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
    <head>
        <title>
            <fmt:message key="sponsor.protocol.home"/>
        </title>
    </head>
    <body>
        <h1>
            <fmt:message key="sponsor.protocol.home.header">
                <fmt:param value="${protocol.protocolTitle}"/>
                <fmt:param value="${protocol.protocolNumber}"/>
            </fmt:message>
        </h1>

        <%-- Dialog is defined in the main page to prevent problems when the tab information is reloaded when the dialog is closed --%>
        <sj:dialog id="popUpDialog" autoOpen="false" modal="true" width="950" position="top" onCloseTopics="dialogClosed" resizable="false" onOpenTopics="dialogOpened" />

        <s:url namespace="/sponsor/protocol/ajax" action="enter" var="protocolUrl">
            <s:param name="protocol.id">${protocol.id}</s:param>
        </s:url>
        <s:url namespace="/sponsor/protocol/ajax" action="investigatorsTab" var="investigatorsUrl">
            <s:param name="protocol.id">${protocol.id}</s:param>
        </s:url>
        <s:url namespace="/sponsor/protocol/ajax" action="subinvestigatorsTab" var="subinvestigatorsUrl">
            <s:param name="protocol.id">${protocol.id}</s:param>
        </s:url>
        <s:url namespace="/sponsor/protocol/ajax" action="forms" var="formsUrl">
            <s:param name="protocol.id">${protocol.id}</s:param>
        </s:url>

        <sj:tabbedpanel id="tabwrapper" >
            <sj:tab id="protocolTab" href="%{protocolUrl}" label="Protocol" />
            <sj:tab id="investigatorsTab" href="%{investigatorsUrl}" label="Investigators" />
            <sj:tab id="subinvestigatorsTab" href="%{subinvestigatorsUrl}" label="Subinvestigators" />
            <sj:tab id="formsTab" href="%{formsUrl}" label="Registration Forms"/>
        </sj:tabbedpanel>
    </body>
</html>