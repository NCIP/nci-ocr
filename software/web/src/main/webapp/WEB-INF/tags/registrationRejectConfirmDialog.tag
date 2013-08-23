<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<s:url var="rejectRegistrationUrl" action="rejectRegistration"
    escapeAmp="false">
    <s:param name="registration.id" value="%{registration.id}" />
    <s:param name="registration.sponsorComments" value="%{registration.sponsorComments}" />
</s:url>
<s:url var="cancelRejectionUrl" action="reviewCommentsEnter" escapeAmp="false">
    <s:param name="registration.id" value="%{registration.id}" />
    <s:param name="registration.sponsorComments" value="%{registration.sponsorComments}" />
</s:url>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages />
            <firebird:dialogHeader>
                <fmt:message key="sponsor.protocol.forms.complete.confirm.title" />
            </firebird:dialogHeader>
            <p><fmt:message key="sponsor.protocol.forms.complete.confirm.message" /></p>

            <firebird:comments titleKey="registration.review.rejection.comments.title">
                ${registration.formComments}
                <b><fmt:message key="label.additional.comments"/></b>
                ${registration.sponsorComments}
            </firebird:comments>

            <div class="btn_bar clear">
                <sj:a id="confirmRejectionBtn" value="Confirm" targets="registrationDialog" cssClass="button"
                    href="%{rejectRegistrationUrl}" onSuccessTopics="enable" onClickTopics="submit"
                    onErrorTopics="ajaxError"><fmt:message key="button.confirm"/></sj:a>
                <sj:a id="cancelRejectionBtn" value="Cancel" targets="registrationDialog" cssClass="button"
                    href="%{cancelRejectionUrl}" onSuccessTopics="enable" onClickTopics="submit"
                    onErrorTopics="ajaxError"><fmt:message key="button.cancel"/></sj:a>
            </div>
        </div>
    </div>
</div>