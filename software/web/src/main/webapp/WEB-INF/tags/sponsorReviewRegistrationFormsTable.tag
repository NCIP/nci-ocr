<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>

<%@ attribute name="ctepRegistration" required="true"%>

<s:set var="namespace">${ctepRegistration ? "annual/registration" : "protocol"}</s:set>
<s:set var="protectedNamespace">${ctepRegistration ? "annual/ctep/registration" : "protocol"}</s:set>
<s:set var="isTab">${ctepRegistration ? "false" : "true"}</s:set>

<div id="reviewRegistrationFormsTable">
    <!-- Without setting autocomplete="off", FF would cache the radio buttons state -->
    <form autocomplete="off">
        <s:iterator value="registration.formsForSponsorReview" var="form">
            <s:url namespace="/sponsor/%{namespace}/review/ajax" action="downloadForm" var="downloadFormUrl">
                <s:param name="registration.id">${registration.id}</s:param>
                <s:param name="registrationForm.id">${form.id}</s:param>
            </s:url>
            <s:url namespace="/sponsor/%{namespace}/review/ajax" action="additionalDocumentsEnter" var="additionalDocumentsEnterUrl">
                <s:param name="registration.id">${registration.id}</s:param>
                <s:param name="registrationForm.id">${form.id}</s:param>
            </s:url>
            <s:url namespace="/sponsor/%{namespace}/review/ajax" action="additionalDocumentsEnter" var="additionalDocumentsEnter" escapeAmp="false">
                <s:param name="registration.id">${registration.id}</s:param>
                <s:param name="registrationForm.id">${form.id}</s:param>
            </s:url>
            <s:url namespace="/sponsor/%{protectedNamespace}/review/ajax" action="commentsEnter" var="commentsUrl" escapeAmp="false">
                <s:param name="registration.id">${registration.id}</s:param>
                <s:param name="registrationForm.id">${form.id}</s:param>
            </s:url>
            <s:div cssClass="form formStatus_%{#form.formStatus}" id="%{#form.id}">

                <!-- Left Section -->
                <div class="float borderRight_gray leftSection">
                    <div class="downloadButtonContainer">
                        <s:if test="#form.collectionOfDocuments">
                            <sj:a href="%{additionalDocumentsEnter}" cssClass="button downloadButton" targets="registrationDialog" onclick='$("#registrationDialog").dialog("open");'>
                                <fmt:message key="button.download"/>
                            </sj:a>
                        </s:if><s:elseif test="#form.formType.formTypeEnum != @gov.nih.nci.firebird.data.FormTypeEnum@ADDITIONAL_ATTACHMENTS">
                            <s:a href="%{downloadFormUrl}" cssClass="button downloadButton" onclick='reloadReviewTab();'><fmt:message key="button.download"/></s:a>
                        </s:elseif>
                    </div>

                    <s:if test="#form.reviewRequired">
                        <div class="radioButtons">
                            <sj:radio
                                id="review_%{#form.id}"
                                list='#{"ACCEPTED":"Accept","REJECTED":"Reject"}'
                                name="review_%{#form.id}"
                                onChangeTopics="submitForm1"
                                disabled="%{!(registration.reviewable && (#form.currentlyReviewable || #form.reviewed))}"
                                onclick="submitFormReview(this, %{#form.id}, '%{#form.formStatus}');"
                                value='%{#form.formStatus}'/>
                        </div>
                    </s:if>
                </div>
                <!-- End Left Section -->

                <!-- Middle Section -->
                <div id="formDescriptionAndStatus">
                    <div id="formDescription" class="boldHeader marginTop_5 "><s:property value='%{#form.formType.description}' /></div>
                    <s:hidden id="formId" value="%{#form.id}" />
                    <div class="formStatus">
                        <span id="formStatus"><s:property value='%{#form.formStatus.display}' /></span>
                        <span id="formStatusDate"><s:date name="formStatusDate" format="MM/dd/yyyy" /></span>
                    </div>
                </div>
                <!-- End Middle Section -->

                <!-- Buttons -->
                <s:if test="#form.commentsLinkToInvestigatorToBeDisplayed">
                    <sj:a href="%{commentsUrl}" cssClass="viewComments button float_right marginRight_5" targets="registrationDialog" onclick='$("#registrationDialog").dialog("open");'>
                        <fmt:message key="button.view.comments"/>
                    </sj:a>
                </s:if>
                <s:if test="#form.numberOfAdditionalDocuments > 0">
                    <sj:a href="%{additionalDocumentsEnter}" cssClass="viewAttachments button float_right marginRight_5" targets="registrationDialog" onclick='$("#registrationDialog").dialog("open");'>
                        <fmt:message key="button.view.attachments"/> (<s:property value="numberOfAdditionalDocuments"/>)
                    </sj:a>
                </s:if>
                <!-- End Buttons -->
            </s:div>
        </s:iterator>
    </form>
</div>

<script>

var ACCEPTED = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@ACCEPTED" />';
var REJECTED = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@REJECTED" />';
var NONE = "none";

function submitFormReview(radio, formId, fromStatus) {
    indicateLoading(true);
    var clickedStatus = radio.value;
    var newStatus = getNewStatus(clickedStatus, fromStatus);
    var onSuccess = getSuccessFunction(newStatus);
    var url = getFormSubmissionUrl(newStatus);
    $.post(url, {'registration.id': ${registration.id}, 'registrationForm.id' : formId}, onSuccess);
}

function getNewStatus(clickedStatus, fromStatus) {
    return clickedStatus == fromStatus ? NONE : clickedStatus;
}

function getSuccessFunction(radioState) {
    if(radioState == ACCEPTED || radioState == NONE) {
      return function() {refreshPage(0, ${isTab}); };
    } else {
      return function(data) {
          indicateLoading(false);
          $('#registrationDialog').html(data).dialog("open");
      };
    }
}

function getFormSubmissionUrl(radioState) {
    if (radioState == NONE) {
        return '<s:url namespace="/sponsor/%{protectedNamespace}/review/ajax" action="clearForm"/>';
    } else if(radioState == ACCEPTED) {
        return '<s:url namespace="/sponsor/%{protectedNamespace}/review/ajax" action="acceptForm"/>';
    } else {
        return '<s:url namespace="/sponsor/%{protectedNamespace}/review/ajax" action="commentsEnter"/>';
    }
}

function reloadReviewTab() {
  refreshPage(1200, ${isTab});
}

</script>