<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages />
            <firebird:dialogHeader>
                <fmt:message key="registration.coordinator.registration.complete.confirm.title" />
            </firebird:dialogHeader>
            <div>
                <h3 id="confirmationMessageHeader"><fmt:message key="registration.coordinator.registration.complete.confirm.message" /></h3>
            </div>
            <s:form id="commentsForm" action="submitRegistration">
                <jsp:doBody />
                <s:hidden name="registration.id"/>
            </s:form>

            <div class="btn_bar clear">
                <sj:a id="confirmSubmissionBtn" value="Confirm" targets="registrationDialog" cssClass="button"
                    onSuccessTopics="enable" onClickTopics="submit" formIds="commentsForm"
                    onErrorTopics="ajaxError" href="#"><fmt:message key="button.confirm"/></sj:a>
                <s:a id="cancelSubmissionBtn" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
            </div>
        </div>
    </div>
</div>
