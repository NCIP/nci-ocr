<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form id="commentsForm" action="submitComments">
                <s:hidden name="registration.id"/>
                <firebird:dialogHeader>
                    <fmt:message key="registration.resubmit.comments.title" />
                </firebird:dialogHeader>

                <div class="clear"></div>

                <firebird:label forId="commentsEditor" messageKey="label.comments"/>
                <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.web.action.investigator.registration.common.CompletionActionProcessor@MAX_CHAR_COUNT" />
                <firebird:richTextArea
                    id="commentsEditor"
                    name="comments"
                    value="${cleanComments}"
                    maxCharacters="${maxCommentCharacters}"/>

                <div class="btn_bar clear">
                    <sj:a id="saveCommentsBtn" value="Save" formIds="commentsForm" onClickTopics="submit"
                          targets="registrationDialog" cssClass="button" href="#" onSuccessTopics="enable"
                          onErrorTopics="ajaxError"><fmt:message key="button.save"/></sj:a>
                    <s:a href="#" id="cancelCommentsBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>
            </s:form>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        indicateLoading(false);
    });
</script>

