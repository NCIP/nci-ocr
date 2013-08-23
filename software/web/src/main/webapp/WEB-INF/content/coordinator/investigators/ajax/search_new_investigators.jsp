<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader>
                <fmt:message key="user.registration.investigator.selection.title"/>
            </firebird:dialogHeader>
            <firebird:messages/>
            <s:form id="selectedInvestigatorForm" namespace="/coordinator/investigators/ajax" action="continueSelection" />
            <firebird:investigatorSearchSelect />
            <div class="btn_bar clear">
                <s:a id="nextStepBtn" cssClass="button" href="javascript:void(0)"><fmt:message key="button.next"/></s:a>
                <s:a id="cancelSelection" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    _investigatorSelectionPage.saveAddition = function(profileId) {
        var input = createNamedElement("input type='hidden'", "selectedInvestigatorsProfileIds").attr("id", this.getElementId(profileId)).val(profileId);
        $('#selectedInvestigatorForm').append(input);
        return true;
    };

    _investigatorSelectionPage.getElementId = function (profileId) {
        return "selected_" + profileId;
    };

    _investigatorSelectionPage.saveRemoval = function(profileId) {
        $('#' + this.getElementId(profileId)).remove();
        return true;
    };

    <s:iterator value="currentUser.registrationCoordinatorRole.managedProfiles" var="investigatorProfile">
        _investigatorSelectionPage.addToAlreadySelectedList('<s:property value="#investigatorProfile.id"/>');
    </s:iterator>
    <s:iterator value="selectedInvestigatorProfiles" var="investigatorProfile">
        _investigatorSelectionPage.addInvestigatorToList('<s:property value="#investigatorProfile.id"/>', '<s:property value="#investigatorProfile.person.displayNameForList"/>');
        _investigatorSelectionPage.saveAddition('<s:property value="#investigatorProfile.id"/>');
    </s:iterator>

    $(document).ready(function(){
        $('#nextStepBtn').click(function() {
            indicateLoading();
            var url = $('#selectedInvestigatorForm').attr('action');
            var dialog = "#investigatorsDialog";
            var serializedForm = $('#selectedInvestigatorForm').serialize();
            _fbUtil.performAjaxPost(url, dialog, serializedForm);
        });
    });
</script>
