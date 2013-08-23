
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <div>
                <firebird:dialogHeader><fmt:message key="investigator.withdraw.request.title" /></firebird:dialogHeader>
                <p><fmt:message key="investigator.withdraw.request.warning.message" /></p>
            </div>
            <div>
                <s:form action="submitWithdraw" id="submitWithdrawForm">
                    <firebird:label forId="commentsEditor" messageKey="investigator.withdraw.request.comment" required="true"/>
                    <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.web.action.investigator.registration.common.CompletionActionProcessor@MAX_CHAR_COUNT" />
                    <firebird:richTextArea id="commentsEditor" name="comments" maxCharacters="${maxCommentCharacters}" />

                    <div class="clear"></div>


                    <firebird:messages/>
                    <firebird:instructionBubble messageKey="investigator.withdraw.request.authentication.message" />
                    <s:textfield label="%{getText('login.username')}" id="username" name="username" requiredLabel="true" size="15" maxlength="100" style="width:175px"/>
                    <s:password label="%{getText('login.password')}" name="password" id="password" requiredLabel="true" size="15" maxlength="100" style="width:175px" />
                    <input class="hide" disabled/>
                    <br/>

                    <div class="btn_bar">
                        <sj:a id="submit" value="Submit" targets="withdrawDialog" onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"
                              cssClass="button" formIds="submitWithdrawForm" href="#"><fmt:message key="button.confirm"/></sj:a>
                        <s:a id="cancel" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.close"/></s:a>
                    </div>
                </s:form>

            </div>
        </div>
    </div>
</div>
