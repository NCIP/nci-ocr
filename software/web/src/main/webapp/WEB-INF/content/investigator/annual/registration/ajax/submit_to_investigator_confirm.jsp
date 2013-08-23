<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:submitRegistrationToInvestigatorConfirmDialog>
    <firebird:label forId="commentsEditor" messageKey="label.comments"/>
    <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.web.action.investigator.registration.common.CompletionActionProcessor@MAX_CHAR_COUNT" />
    <firebird:richTextArea
        id="commentsEditor"
        name="comments"
        value="${cleanCoordinatorComments}"
        maxCharacters="${maxCommentCharacters}" />
</firebird:submitRegistrationToInvestigatorConfirmDialog>
