<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="disqualifyUrl" action="disqualifyInvestigator"/>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form id="disqualifyInvestigatorForm" action="disqualifyInvestigator">
                <s:hidden name="registration.id"/>
                <firebird:dialogHeader>
                    <fmt:message key="sponsor.disqualify.investigator.title" />
                </firebird:dialogHeader>
                <h3>
                    <fmt:message key="sponsor.disqualify.investigator.instructions" >
                        <fmt:param value="${registration.profile.person.displayName}"/>
                    </fmt:message>
                </h3>
                <div class="clear"></div>

                <firebird:label forId="commentsEditor" messageKey="sponsor.disqualify.investigator.comment" required="true"/>
                <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.web.action.sponsor.annual.registration.ajax.DisqualifyInvestigatorAction@MAX_CHARACTER_COUNT" />
                <firebird:richTextArea
                    id="commentsEditor"
                    name="disqualificationComments"
                    maxCharacters="${maxCommentCharacters}" />

                <div class="clear"></div>

                <div class="btn_bar">
                    <sj:a id="saveBtn" value="Save" formIds="disqualifyInvestigatorForm"
                          cssClass="button blueButton" targets="registrationDialog" href="#"
                          onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.save"/></sj:a>
                    <s:a href="#" id="cancelBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>

            </s:form>
        </div>
    </div>
</div>