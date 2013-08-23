<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/annual/registration/ajax" action="enterViewOverview" var="overviewUrl" escapeAmp="false" >
    <s:param name="registration.id" value="registration.id"/>
    <s:param name="profile.id" value="profile.id"/>
</s:url>
<div>
    <firebird:InvestigatorRegistrationPageBackButton/>
    <h1 class="inline-block">
        <fmt:message key="annual.registration.view.overview.title">
            <fmt:param>${registration.profile.person.displayName}</fmt:param>
        </fmt:message>
    </h1>
</div>

<sj:dialog id="registrationDialog" autoOpen="false" modal="true" width="950" position="top" onCloseTopics="dialogClosed" resizable="false" onOpenTopics="dialogOpened" />

<sj:tabbedpanel id="tabwrapper" >
    <sj:tab id="overviewTab" cssClass="tab" href="%{overviewUrl}" label="%{getText('label.overview')}" />
    <s:iterator value="%{registration.forms}" var="form">
        <s:url namespace="/investigator/annual/registration/ajax" action="viewForm" var="tabURL" escapeAmp="false">
            <s:param name="registration.id" value="registration.id"/>
            <s:param name="formType.id" value="%{#form.formType.id}"/>
            <s:param name="profile.id" value="profile.id"/>
        </s:url>
        <sj:tab id="form_%{#form.formType.name.toLowerCase().replaceAll(' ','_')}_tab" cssClass="tab" label="%{#form.formType.name}" href="%{tabURL}" />
    </s:iterator>
</sj:tabbedpanel>

<script>

$(function() {
  initiateBackButton();
});

</script>