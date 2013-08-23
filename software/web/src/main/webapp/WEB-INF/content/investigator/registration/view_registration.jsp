<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div>
    <firebird:InvestigatorRegistrationPageBackButton/>
    <h1 class="registrationHeader inline-block">
        <s:property value="%{headerTitle}" />
    </h1>
</div>
<s:url namespace="/investigator/registration/ajax" action="viewOverview" var="overviewUrl" escapeAmp="false" >
    <s:param name="registration.id" value="registration.id"/>
    <s:param name="profile.id" value="profile.id"/>
</s:url>
<s:url namespace="/investigator/registration/ajax" action="viewProtocolInformation" var="protocolInformationUrl" escapeAmp="false" >
    <s:param name="registration.id" value="registration.id"/>
    <s:param name="profile.id" value="profile.id"/>
</s:url>
<s:url var="subinvestigatorsUrl" action="list" namespace="/investigator/registration/ajax/subinvestigator" escapeAmp="false" >
    <s:param name="registration.id" value="registration.id"/>
    <s:param name="profile.id" value="profile.id"/>
</s:url>

<sj:dialog id="registrationDialog" autoOpen="false" modal="true" width="950" position="top" onCloseTopics="dialogClosed" resizable="false" onOpenTopics="dialogOpened" />

<sj:tabbedpanel id="tabwrapper" selectedTab="1">
    <sj:tab id="protocolInformationTab" href="%{protocolInformationUrl}" label="Protocol Info" />
    <sj:tab id="overviewTab" href="%{overviewUrl}" label="%{getText('label.overview')}" />
    <s:if test="registration.investigatorRegistration">
        <sj:tab id="subinvestigatorsTab" href="%{subinvestigatorsUrl}" label="Subinvestigators" />
    </s:if>
    <s:iterator value="%{forms}" var="formType">
        <s:url namespace="/investigator/registration/ajax" action="viewForm" var="tabURL" escapeAmp="false">
            <s:param name="registration.id" value="registration.id"/>
            <s:param name="formType.id" value="%{#formType.id}"/>
            <s:param name="profile.id" value="profile.id"/>
        </s:url>

        <sj:tab id="form_%{#formType.name.toLowerCase().replaceAll(' ','_')}_tab" label="%{#formType.name}" href="%{tabURL}" />
    </s:iterator>

</sj:tabbedpanel>

<s:url var="protocolLastUpdatedUrl" action="lastProtocolUpdateAction" namespace="/investigator/registration" escapeAmp="false" />
<script>

$(function() {
    initiateBackButton();
});

var _viewReg = (function() {
    var page = {};
    var alertTimer = undefined;

    page.alertCountDown = <fmt:message key="registration.protocol.updated.alert.countdown"/>;
    page.pollLastUpdateTimer = undefined;
    page.pollForProtocolUpdates = function() {
        if ($('#registrationDialog').length > 0) {
            var url = '<s:property value="#protocolLastUpdatedUrl"/>';
            $.post(url,
                {
                    'registration.id' : '${registration.id}',
                    'profile.id' : '${profile.id}',
                    'originalUpdateDate' : '${registration.protocol.lastUpdate.time}',
                    'originalIsLockedForInvestigator' : '${registration.lockedForInvestigator}'
                },
                function(data) {
                    var isUpdated = data;
                    if (isUpdated) {
                        _viewReg.alertRefresh();
                        clearInterval(page.pollLastUpdateTimer);
                    }
            });
        } else {
            clearInterval(page.pollLastUpdateTimer);
        }
    };

    page.alertRefresh = function() {
        var timeout = page.alertCountDown;

        alertTimer = setInterval(function() {
            if (timeout > 0) {
                setPageErrorMessages("<fmt:message key='registration.protocol.updated.page.alert.warning'/> " + timeout-- + " <fmt:message key='label.seconds' />.");
            } else {
                refreshPage(0);
            }
        }, 1000)

    };

    page.clearTimers = function() {
        clearInterval(page.pollLastUpdateTimer);
        clearInterval(alertTimer);
    }

    return page;
})();

_viewReg.pollLastUpdateTimer = setInterval(_viewReg.pollForProtocolUpdates, <fmt:message key="registration.protocol.updated.poll.period.milliseconds"/>);

</script>
