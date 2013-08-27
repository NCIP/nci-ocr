<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/annual/registration/ajax" action="enterViewOverview" var="overviewUrl" escapeAmp="false" >
    <s:param name="registration.id" value="registration.id"/>
    <s:param name="profile.id" value="profile.id"/>
</s:url>
<div>
<span class="vertical-align-super">
    <s:a id="backButton" cssClass="button" href="javascript:void(0)" onclick="navigateBack();"><fmt:message key="button.back"/></s:a>
</span>
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

var tabStack = new Array();
var backClicked = false;

$(function() {
    selectPassedInFormsTab();
    $( "#tabwrapper" ).tabs({
         select: function(event, ui) {
           addTabToNavigationStack();
         }
      });
});

function selectPassedInFormsTab() {
    var form = "<s:property value='%{#parameters[\"form\"]}' />";
    var selectedTab = getSelectedTabIndex(form);
    $("#tabwrapper").tabs("option", "selected" , selectedTab);
}

function addTabToNavigationStack() {
  if (!backClicked) {
    tabStack.push(getCurrentSelectedTabIndex());
  } else {
    backClicked = false;
  }
}

function getSelectedTabIndex(form) {
    var selectedTabIndex = 0;
    $(".tab").each(function(index, tab) {
        if ($(tab).attr("id") == "form_" + form + "_tab") {
          selectedTabIndex = index;
        }
    });
    return selectedTabIndex;
}

function navigateBack() {
    backClicked = true;
    var tabIndex = tabStack.pop();
    if (tabIndex != undefined) {
      $("#tabwrapper").tabs("option", "selected", tabIndex);
    } else {
      history.go(-1);
    }
}
</script>