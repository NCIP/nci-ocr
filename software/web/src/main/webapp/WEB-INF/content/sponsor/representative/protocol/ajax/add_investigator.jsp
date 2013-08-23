<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader><fmt:message key="sponsor.protocol.investigator.add.title"/></firebird:dialogHeader>

            <firebird:messages/>

            <s:form id="selectInvestigatorForm" action="selectInvestigator" onsubmit="return !isBlank(selectedPersonExternalId.value)">
                <firebird:personSearch labelKey="person.search.label" setFocus="true">
                    <s:url namespace="/sponsor/representative/protocol/ajax" action="enterCreateInvestigator" var="createNewInvestigatorUrl" >
                        <s:param name="protocol.id">${protocol.id}</s:param>
                    </s:url>
                    <sj:a targets="investigatorInfoContent" href="%{createNewInvestigatorUrl}" cssClass="button"
                          id="createNewInvestigatorButton" onclick="showContent();" onClickTopics="submit" onSuccessTopics="enable"
                          onErrorTopics="ajaxError"><fmt:message key="button.create.new"/></sj:a>
                    <s:a id="doneButton" href="#" cssClass="button"><fmt:message key="button.done"/></s:a>
                </firebird:personSearch>
                <s:hidden id="selectedPersonExternalId" name="selectedPersonExternalId" />
                <s:hidden id="protocolId" name="protocol.id" />
            </s:form>
            <div class="clear"></div>
             <div id="investigatorInfo" class="hide">
                <div class="dotted_line"></div>
                <div id="investigatorInfoContent"></div>
            </div>
        </div>
    </div>

    <s:url namespace="/sponsor/representative/protocol/ajax" action="selectInvestigator" var="selectInvestigatorUrl"/>

    <script type="text/javascript">

        function showContent() {
            $('#investigatorInfo').show();
        }

        $(document).ready(function() {

            $("#doneButton").click( function(){
                closeDialogAndReload();
            });

            personSearch.clickSelectButton = function(person) {
                $('#selectedPersonExternalId').val(person.externalId);
                var url = "${selectInvestigatorUrl}";
                var serializedForm = $('#selectInvestigatorForm').serialize();
                var target = "#investigatorInfoContent";
                _fbUtil.performAjaxPost(url, target, serializedForm);
                showContent();
                personSearch.clear();
            }
            $("input").keydown(testForEnter);
        });

    </script>
</div>