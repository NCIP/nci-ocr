<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader>Deny Investigator Withdrawal Request</firebird:dialogHeader>

            <s:form id="denialForm" action="denyRequest" >
                <firebird:label forId="denialExplanation" messageKey="registration.withdrawal.request.denial.explanation" required="true"/>
                <firebird:richTextArea maxCharacters="500" name="denialExplanation" id="denialExplanation" />
                <s:hidden name="user.id" value="%{user.id}"/>
            </s:form>

            <sj:a id="submitButton" targets="taskDialog" formIds="denialForm" cssClass="button float_right" href="#"><fmt:message key="button.submit"/></sj:a>
            <s:a id="cancelButton"  href="javascript:void(0);" onclick="closeDialog();" cssClass="button float_right marginRight_5"><fmt:message key="button.cancel"/></s:a>
        </div>
    </div>
</div>