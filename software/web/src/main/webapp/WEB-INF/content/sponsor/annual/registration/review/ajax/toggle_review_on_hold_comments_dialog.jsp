<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<s:if test="%{registration.reviewOnHold}">
    <s:set var="titleKey">sponsor.resume.on.hold.registration.title</s:set>
    <s:set var="instructionsKey">sponsor.resume.on.hold.registration.instructions</s:set>
</s:if> <s:else>
    <s:set var="titleKey">sponsor.set.registration.on.hold.title</s:set>
    <s:set var="instructionsKey">sponsor.set.registration.on.hold.instructions</s:set>
</s:else>

<!--Content-->
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form action="saveToggleReviewOnHold" id="toggleReviewOnHoldForm">
                <s:hidden name="registration.id"/>
                <firebird:dialogHeader>
                    <fmt:message key="${titleKey}" />
                </firebird:dialogHeader>
                <h3>
                    <fmt:message key="${instructionsKey}" >
                        <fmt:param value="${registration.profile.person.displayName}"/>
                    </fmt:message>
                </h3>
                <div class="clear"></div>

                <firebird:label forId="toggleReviewOnHoldCommentsEditor" messageKey="label.comments"/>
                <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.web.action.sponsor.annual.registration.ajax.ToggleReviewOnHoldAction@MAX_CHARACTER_COUNT" />
                <firebird:richTextArea
                    id="toggleReviewOnHoldCommentsEditor"
                    name="registration.sponsorComments"
                    maxCharacters="${maxCommentCharacters}" />

                <div class="clear"></div>

                <div class="btn_bar">
                    <sj:a id="saveBtn" value="Save" formIds="toggleReviewOnHoldForm"
                          cssClass="button blueButton" targets="registrationDialog" href="#"
                          onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.save"/></sj:a>
                    <s:a href="#" id="cancelBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>

            </s:form>
        </div>
    </div>
</div>
