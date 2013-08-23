<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <div id="errorDiv">
                <firebird:messages/>
            </div>
            <s:form namespace="/investigator/profile/credentials/ajax" action="saveExperience" id="credentialsExperienceForm">

                <firebird:dialogHeader>
                    <fmt:message key="credentials.experience.title" />
                </firebird:dialogHeader>

                <div class="clear"></div>

                <firebird:label forId="experienceEditor" messageKey="label.clinical.research.experience"/>
                <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.web.action.investigator.profile.ManageExperienceAction@MAX_CHAR_COUNT" />
                <firebird:richTextArea
                    id="experienceEditor"
                    name="profile.clinicalResearchExperience.text"
                    value="${cleanText}"
                    maxCharacters="${maxCommentCharacters}" />

                <div class="clear"></div>

                <s:hidden name="profile.id" value="%{profile.id}"/>

                <div class="btn_bar">
                    <s:url var="saveUrl" namespace="/investigator/profile/credentials/ajax" action="saveExperience"/>
                    <sj:a id="saveExperienceBtn" value="Save" formIds="credentialsExperienceForm"
                          targets="profileDialog" cssClass="button" href="%{saveUrl}"
                          onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.save"/></sj:a>
                    <s:a href="#" id="cancelExperienceBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>
            </s:form>
        </div>
    </div>
</div>
