<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form namespace="/sponsor/representative/protocol/ajax" action="deactivateRegistrationPacket" id="deactivateForm">
                <s:hidden name="registration.id"/>
                <firebird:dialogHeader>
                    <fmt:message key="sponsor.protocol.deactivate.investigator"/>
                </firebird:dialogHeader>
                <h3>Investigator, <s:property value="registration.profile.person.displayName"/>, and all SubInvestigators will be deactivated. Would you like to continue?</h3>

                <div class="clear"></div>

                <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.web.action.sponsor.protocol.RegistrationPacketAccessAction@MAX_CHAR_COUNT" />
                <firebird:richTextArea
                    id="deactivateCommentsEditor"
                    name="comments"
                    label="Comments"
                    value="${cleanText}"
                    required="true"
                    maxCharacters="${maxCommentCharacters}" />

                <div class="clear"></div>

                <div class="btn_bar">
                    <s:url var="saveUrl" namespace="/sponsor/representative/protocol/ajax" action="deactivateRegistrationPacket"/>
                    <sj:a id="saveDeactivationBtn" value="Deactivate" formIds="deactivateForm"
                          targets="popUpDialog" cssClass="button" href="%{saveUrl}" onSuccessTopics="enable"
                          onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.save"/></sj:a>
                    <s:a href="#" id="cancelDeactivationBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>

            </s:form>
        </div>
    </div>
</div>
