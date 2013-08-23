<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url action="enterAddRolesFlow" namespace="/user/registration" var="addRoleUrl">
    <s:param name="preselectedRole" value="@gov.nih.nci.firebird.data.user.UserRoleType@INVESTIGATOR" />
</s:url>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages />
            <firebird:dialogHeader>
                <fmt:message key="user.missing.expected.investigator.role.title" />
            </firebird:dialogHeader>
            <span id="missingInvestigatorRoleText">
                <fmt:message key="user.missing.expected.investigator.role.text">
                    <fmt:param>${investigatorType}</fmt:param>
                </fmt:message>
            </span>
            <div class="btn_bar clear">
                <s:a id="continueBtn" value="continue" cssClass="button" href="%{addRoleUrl}"><fmt:message key="button.continue"/></s:a>
                <s:a id="cancelBtn" href="# " cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
            </div>
        </div>
    </div>
</div>