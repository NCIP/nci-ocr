<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages />
            <s:form action="showConfirmation" id="commentsForm">
                <s:hidden name="registration.id" />
                <firebird:dialogHeader>
                    <jsp:doBody />
                </firebird:dialogHeader>

                <div class="clear"></div>

                <firebird:instructionBubble messageKey="sponsor.protocol.forms.complete.comment.instructions" />

                <firebird:comments>${registration.formComments}</firebird:comments>

                <br/>

                <firebird:label forId="commentsEditor" messageKey="label.comments"/>
                <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.web.action.investigator.registration.common.ReviewRegistrationFormActionProcessor@MAX_CHAR_COUNT" />
                <firebird:richTextArea
                    id="commentsEditor"
                    name="registration.sponsorComments"
                    value="${cleanSponsorComments}"
                    maxCharacters="${maxCommentCharacters}" />

                <div class="clear"></div>

                <div class="btn_bar">
                    <s:url var="saveUrl" action="showConfirmation" />
                    <sj:a id="saveCommentsBtn" value="Save" formIds="commentsForm" targets="registrationDialog"
                        cssClass="button" href="%{saveUrl}" onSuccessTopics="enable" onClickTopics="submit"
                        onErrorTopics="ajaxError"><fmt:message key="button.save"/></sj:a>
                    <s:a href="#" id="cancelCommentsBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>

            </s:form>
        </div>
    </div>
</div>