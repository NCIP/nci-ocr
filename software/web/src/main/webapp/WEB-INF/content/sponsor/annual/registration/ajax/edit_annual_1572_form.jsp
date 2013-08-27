<%@ include file="/WEB-INF/content/common/taglibs.jsp" %>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form action="validateForm" id="edit1572Form"
                    namespace="/sponsor/annual/registration/ajax" method="POST">

                <firebird:dialogHeader>
                    <fmt:message key="sponsor.annual.registrations.edit.form"/>
                </firebird:dialogHeader>

                <div class="formcol clear">
                    <s:textarea label="%{getText('sponsor.annual.registrations.edit.subInvestigatorText')}"
                                id="subInvestigatorTextInput" name="subInvestigatorTextInput"
                                cols="80" rows="5"/>
                </div>
                <div class="clear"><br></div>
                <div class="formcol clear">
                    <s:textarea label="%{getText('sponsor.annual.registrations.edit.protocolText')}"
                                id="protocolTextInput" name="protocolTextInput"
                                cols="100" rows="5"/>
                </div>

                <div class="clear"><br></div>
                <div class="btn_bar clear">
                    <s:a id="edit1572FormSubmitButton" href="#" cssClass="button" onclick="updateValues();"><fmt:message key="button.save"/></s:a>
                    <s:a id="edit1572FormCancelButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>


                <script type="text/javascript">

                    function updateValues() {
                        indicateLoading(true);

                        $.post($('#edit1572Form').attr("action"), $('#edit1572Form').serialize(),
                                null, 'html').success(
                                function (errorMessages) {

                                    indicateLoading(false);

                                    if (_.isUndefined(errorMessages) ||
                                            _.isEmpty(errorMessages)) {
                                        $("#subInvestigatorText").val($("#subInvestigatorTextInput").val());
                                        $("#protocolText").val($("#protocolTextInput").val());
                                        closeDialog();
                                    }
                                    else {
                                        $("#editFormDialog").html(errorMessages).dialog();
                                    }
                                });
                    }

                    $(document).ready(function () {
                        //make sure its not fresh page load and has no validation errors
                        if ($("[class*=errorMessage]").size() < 1) {
                            $("#subInvestigatorTextInput").val($("#subInvestigatorText").val());
                            $("#protocolTextInput").val($("#protocolText").val());
                        }
                    });
                </script>


            </s:form>
        </div>
    </div>

</div>

