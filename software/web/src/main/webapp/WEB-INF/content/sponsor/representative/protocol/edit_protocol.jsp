<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<h1><fmt:message key="sponsor.protocol.edit" /></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form id="protocolForm" action="updateProtocol" namespace="/sponsor/representative/protocol">
                <s:hidden name="protocol.id"/>
                <div id="protocolFields">
                <%@include file="../protocol/protocol_fields.jspf" %>
                </div>
                <div class="clear"></div>
                <br/>
                <firebird:instructionBubble messageKey="sponsor.protocol.edit.instructions" hasIndicator="false"/>

                <firebird:label forId="commentsEditor" messageKey="label.comments"/>
                <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.data.ProtocolRevision@MAX_COMMENT_LENGTH" />
                <firebird:richTextArea id="commentEditor" name="comment" maxCharacters="${maxCommentCharacters}" />

            </s:form>
            <div class="clear">
            </div>
            <div class="btn_bar">
                <sj:a id="saveButton" cssClass="button" formIds="protocolForm" href="#"><fmt:message key="button.save"/></sj:a>
                <s:a id="cancelButton" action="enterProtocol" namespace="/sponsor/protocol" cssClass="button">
                    <fmt:message key="button.cancel"/>
                    <s:param name="protocol.id" value="%{protocol.id}"/>
                </s:a>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

var editProtocolPage = {};

editProtocolPage.form = $("#protocolForm").get(0);

editProtocolPage.form.getSerializedState = function () {
    return _.reject($(editProtocolPage.form).find("input, select").serializeArray(), function(item) {
        return _.contains(["OWASP_CSRFTOKEN", 'null_widget'], item.name);
    });
};

editProtocolPage.form.messages = {
    "sponsor.protocol.edit.close.warning": "<fmt:message key='sponsor.protocol.edit.close.warning' />"
};


editProtocolPage.toggleSaveButtonIfNeccessary = function () {
    var show = editProtocolPage.areValidationErrorsPresent() || editProtocolPage.form.hasChanges();
    $("#saveButton").toggle(show);
};

editProtocolPage.areValidationErrorsPresent = function () {
    return $(".errorMessage").length > 0;
};

editProtocolPage.addHandlersToFormElements = function () {
    $("#cancelButton").click(editProtocolPage.form.checkForChangesAndBlock);
    $(editProtocolPage.form).change(editProtocolPage.toggleSaveButtonIfNeccessary);
    $(editProtocolPage.form).keyup(editProtocolPage.toggleSaveButtonIfNeccessary);
    $(editProtocolPage.form).delegate("a", "click", editProtocolPage.toggleSaveButtonIfNeccessary);
};

$(editProtocolPage.form).subscribe("checkUpdates", function() {
   editProtocolPage.toggleSaveButtonIfNeccessary();
});

$(document).ready(function () {
    $("input").keydown(testForEnter);

    setUpProtocolForm(editProtocolPage.form);
    editProtocolPage.toggleSaveButtonIfNeccessary();
    editProtocolPage.addHandlersToFormElements();
});

</script>
