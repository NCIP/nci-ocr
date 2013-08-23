<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">


            <firebird:dialogHeader><s:property value="title" /></firebird:dialogHeader>

            <fmt:message key="registration.coordinator.request.message">
                <fmt:param value="${managedInvestigator.user.person.displayName}" />
            </fmt:message>

            <s:form id = "acceptRequestForm" namespace="/investigator/settings/ajax" action="acceptCoordinatorRequest">
                <s:hidden name="managedInvestigatorId" value="%{managedInvestigator.id}"/>
            </s:form>

            <s:form id = "rejectRequestForm" namespace="/investigator/settings/ajax" action="rejectCoordinatorRequest">
                <s:hidden name="managedInvestigatorId" value="%{managedInvestigator.id}"/>
            </s:form>

            <div class="btn_bar">
                <sj:a id="acceptBtn" formIds="acceptRequestForm" cssClass="button" cssStyle="width: 100px"><fmt:message key="button.accept"/></sj:a>
                <sj:a id="rejectBtn" formIds="rejectRequestForm" cssClass="button" cssStyle="width: 100px"><fmt:message key="button.reject"/></sj:a>
                <s:a id="cancelButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
            </div>

        </div>
    </div>
</div>


<script>

$(document).ready(function() {
    $.publish('injectCsrfTokens');
});

</script>