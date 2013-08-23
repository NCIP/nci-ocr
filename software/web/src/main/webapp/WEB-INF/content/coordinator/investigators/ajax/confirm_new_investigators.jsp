<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="searchAgainUrl" namespace="/coordinator/investigators/ajax" action="enterAddInvestigators"/>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages />
            <firebird:dialogHeader>
                <fmt:message key="registration.coordinator.confirm.investigators.title" />
            </firebird:dialogHeader>
            <h3><fmt:message key="registration.coordinator.confirm.investigators.message" /><fmt:message key="label.separator" /></h3>
            <ul id="investigatorList">
                <s:iterator value="selectedInvestigatorProfiles" var="investigatorProfile">
                    <li id="profile_<s:property value='%{#investigatorProfile.id}'/>">
                        <s:property value="%{#investigatorProfile.person.displayNameForList}"/>
                    </li>
                </s:iterator>
            </ul>

            <s:form id="selectedInvestigatorForm" namespace="/coordinator/investigators/ajax" action="confirmSelection">
                <s:iterator value="selectedInvestigatorProfiles" var="investigatorProfile">
                    <s:hidden name="selectedInvestigatorsProfileIds" value="%{#investigatorProfile.id}"/>
                </s:iterator>
            </s:form>

            <div class="btn_bar clear">
                <sj:a id="searchAgainLink" cssClass="search btn" href="%{searchAgainUrl}"
                    formIds="selectedInvestigatorForm" targets="investigatorsDialog">
                    <fmt:message key="button.searchAgain"/>
                </sj:a>
                <sj:a id="saveSelectedInvestigatorsBtn" value="Save" formIds="selectedInvestigatorForm"
                    targets="investigatorsDialog" cssClass="button" href="#"
                    onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.save"/></sj:a>
                <s:a id="%{#cancelButton}" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
            </div>
        </div>
    </div>
</div>