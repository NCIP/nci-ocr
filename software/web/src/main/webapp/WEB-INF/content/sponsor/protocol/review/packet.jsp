<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:set var="packet" value="packet"/>
<s:set var="selectedTab" value="%{#parameters['selectedTab'] != null ? #parameters['selectedTab'] : #packet.indexOf(registration) + 1}"/>
<div>
    <p>
        <s:url namespace="/sponsor/protocol" action="enterProtocol" var="protocolUrl">
            <s:param name="protocol.id" value="registration.protocol.id"/>
        </s:url>
        <s:a href="%{protocolUrl}">
            <fmt:message key="registration.review.return.to.protocol.link">
                <fmt:param value="${registration.protocol.protocolNumber}" />
            </fmt:message>
        </s:a>
    </p>
    <sj:dialog id="registrationDialog" autoOpen="false" modal="true" width="950" position="top" onCloseTopics="dialogClosed" resizable="false" onOpenTopics="dialogOpened" />

    <sj:tabbedpanel id="tabwrapper" useSelectedTabCookie="false" selectedTab="%{#selectedTab}">
        <s:url namespace="/sponsor/protocol/review/ajax" action="overview" var="overviewURL" escapeAmp="false">
            <s:param name="registration.id" value="registration.primaryRegistration.id"/>
        </s:url>

        <sj:tab id="overviewTab" label="%{getText('label.overview')}" href="%{overviewURL}" />
        <s:iterator value="#packet" var="reg">
            <s:url namespace="/sponsor/protocol/review/ajax" action="enter" var="regURL" escapeAmp="false">
                <s:param name="registration.id" value="%{#reg.id}"/>
            </s:url>

            <sj:tab id="tab_%{#reg.id}" label="%{getTabName(#reg)}" href="%{regURL}" />
        </s:iterator>
    </sj:tabbedpanel>
</div>