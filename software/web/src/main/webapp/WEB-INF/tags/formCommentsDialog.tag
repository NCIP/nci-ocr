<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form action="rejectForm" id="commentsForm">
                <s:hidden name="registration.id"/>
                <s:hidden name="registrationForm.id"/>
                <firebird:dialogHeader>
                  <fmt:message key="sponsor.protocol.forms.reject.comment.header">
                    <fmt:param value="${registrationForm.formType.description}" />
                  </fmt:message>
                </firebird:dialogHeader>

        <div class="clear"></div>
                <s:if test="registration.reviewable">
                    <firebird:label forId="commentsEditor" messageKey="label.comments"/>
                    <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.web.action.investigator.registration.common.ReviewRegistrationFormActionProcessor@MAX_CHAR_COUNT" />
                    <firebird:richTextArea
                        id="commentsEditor"
                        name="registrationForm.comments"
                        value="${cleanFormComments}"
                        maxCharacters="${maxCommentCharacters}" />

                    <div class="clear"></div>

                    <div class="btn_bar">
                        <s:url var="saveUrl" action="rejectForm"/>
                        <sj:a id="saveCommentsBtn" value="Save" formIds="commentsForm"
                              targets="registrationDialog" cssClass="button" href="%{saveUrl}"
                              onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.save"/></sj:a>
                        <s:a href="#" id="cancelCommentsBtn" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.cancel"/></s:a>
                    </div>

                </s:if>
                <s:else>
                    <div id="comments" class="richText">
                        <s:if test="%{'' != cleanFormComments}">
                            <s:property value="%{cleanFormComments}" escape="false"/>
                        </s:if>
                        <s:else>
                            <fmt:message key="registration.form.review.comments.empty"/>
                        </s:else>
                    </div>
                    <div class="clear"></div>

                    <div class="btn_bar">
                        <s:a href="#" id="closeCommentsBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.close"/></s:a>
                    </div>
                </s:else>
            </s:form>
        </div>
    </div>
</div>
