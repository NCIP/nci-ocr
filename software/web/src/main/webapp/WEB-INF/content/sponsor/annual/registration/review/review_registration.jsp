<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<script src="<c:url value='/scripts/jquery-ui.toggleSwitch.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/styles/toggle-switch.css'/>" type="text/css" />

<s:url action="enterToggleReviewOnHoldComments" namespace="ajax" var="enterToggleReviewOnHoldCommentsUrl">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<s:url action="enterDisqualifyInvestigator" namespace="ajax" var="enterDisqualifyInvestigatorUrl">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>

<sj:dialog id="registrationDialog" autoOpen="false" modal="true" width="950" position="top" onCloseTopics="dialogClosed" resizable="false" onOpenTopics="dialogOpened" />

<span class="reviewAnnualRegistrationBackButton">
    <s:a cssClass="button" href="javascript:void(0)" onclick="history.go(-1);"><fmt:message key="button.back"/></s:a>
</span>
<h1 id="reviewAnnualRegistrationTitle">
    <fmt:message key="review.annual.registration.header">
        <fmt:param value="${registration.profile.person.displayName}" />
    </fmt:message>
</h1>
<div class="boldHeader">
    <span>
        <s:text name="label.status"/>: <span class="statusBullet statusBullet_sponsor_${registration.status}">&bull;</span>
        <span id="registrationStatus">${registration.status.display}</span>
    </span>
    <s:if test="%{registration.reviewable || registration.reviewOnHold}">
        <span class="toggleSwitch" >
            <select id="onHoldToggle">
                <option value="off"><fmt:message key="label.in.review" /></option>
                 <s:if test="%{registration.reviewOnHold}">
                    <option value="on" selected="selected"><fmt:message key="label.on.hold" /></option>
                </s:if>
                <s:else>
                    <option value="on"><fmt:message key="label.on.hold" /></option>
                </s:else>
            </select>
        </span>
        <div class="clear"></div>
    </s:if>
</div>

<div class="ui-tabs">
    <div class="ui-tabs-panel">
        <firebird:messages />

        <s:if test="%{registration.reviewable || registration.reviewOnHold}">
            <sj:a id="disqualifyInvestigatorButton" cssClass="button float_right" href="%{enterDisqualifyInvestigatorUrl}" targets="registrationDialog" onclick='$("#registrationDialog").dialog("open");'>
                <fmt:message key="button.disqualify.investigator"/>
            </sj:a>
            <div class="clear"></div>
        </s:if>

        <firebird:registrationComments registration="${registration}" retainState="true"/>
        <div class="dotted_line"></div>
        <div id="reviewRegistrationFormsTable">
            <!-- Without setting autocomplete="off", FF would cache the radio buttons state -->
            <form autocomplete="off">
                <s:iterator value="registration.formsForSponsorReview" var="form">
                    <s:url namespace="/sponsor/annual/registration/review/ajax" action="downloadForm" var="downloadFormUrl">
                        <s:param name="registration.id">${registration.id}</s:param>
                        <s:param name="registrationForm.id">${form.id}</s:param>
                    </s:url>
                    <s:url namespace="/sponsor/annual/registration/review/ajax" action="additionalDocumentsEnter" var="additionalDocumentsEnter" escapeAmp="false">
                        <s:param name="registration.id">${registration.id}</s:param>
                        <s:param name="registrationForm.id">${form.id}</s:param>
                    </s:url>
                    <s:url namespace="/sponsor/annual/registration/review/ajax" action="commentsEnter" var="commentsUrl" escapeAmp="false">
                        <s:param name="registration.id">${registration.id}</s:param>
                        <s:param name="registrationForm.id">${form.id}</s:param>
                    </s:url>
                    <s:div cssClass="form formStatus_%{#form.formStatus}" id="%{#form.id}">

                        <!-- Left Section -->
                        <div class="float borderRight_gray leftSection">
                            <div class="downloadButtonContainer">
                                <s:if test="#form.fileToReview != null">
                                    <s:a href="%{downloadFormUrl}" cssClass="button downloadButton" onclick='refreshPage(1000)'><fmt:message key="button.download"/></s:a>
                                </s:if>
                            </div>

                            <s:if test="#form.reviewRequired">
                                <div class="radioButtons">
                                    <sj:radio
                                        id="review_%{#form.id}"
                                        list='#{"ACCEPTED":"Accept","REJECTED":"Reject"}'
                                        name="review_%{#form.id}"
                                        onChangeTopics="submitForm1"
                                        disabled="%{#form.currentlyReviewable == false}"
                                        onclick="submitFormReview(this, %{#form.id}, '%{#form.formStatus}');"
                                        value='%{#form.formStatus}'/>
                                </div>
                            </s:if>
                        </div>
                        <!-- End Left Section -->

                        <!-- Middle Section -->
                        <div class="float marginLeft_5">
                            <div id="formDescription" class="boldHeader marginTop_5"><s:property value='%{#form.formType.description}' /></div> <br />
                                <span id="formStatus"><s:property value='%{#form.formStatus.display}' /></span>
                                <span id="formStatusDate"><s:date name="formStatusDate" format="MM/dd/yyyy" /></span>
                        </div>
                        <!-- End Middle Section -->

                        <!-- Buttons -->
                        <s:if test="#form.plainTextComments.empty == false && #form.formStatus != @gov.nih.nci.firebird.data.FormStatus@ACCEPTED && #form.formStatus != @gov.nih.nci.firebird.data.FormStatus@IN_REVIEW">
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

        <s:if test="registration.completeable">
            <sj:a id="completeReviewButton" href="javascript:void(0)" cssClass="button blueButton completeReviewButton" targets="registrationDialog" onclick="submitReview();" >
                <fmt:message key="button.complete.review" />
            </sj:a>
        </s:if>
        <s:elseif test="%{registration.approvable}">
            <sj:a id="approveRegistrationButton" href="javascript:void(0)"
                  cssClass="button blueButton completeReviewButton" targets="registrationDialog"
                  onclick="approveRegistration();">
                <fmt:message key="button.approve.registration"/>
            </sj:a>
        </s:elseif>
        <s:else>
            <firebird:instructionBubble messageKey="complete.review.button.instructions.${registration.status}" hasIndicator="false" />
        </s:else>
        <div class="clear"></div>

    </div>
</div>

<script>

var ACCEPTED = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@ACCEPTED" />';
var REJECTED = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@REJECTED" />';
var NONE = "none";
var initialized = false;
$(document).ready(function() {
  $("#onHoldToggle").toggleSwitch({
      change: function(e) {
            toggleReviewOnHold();
      }
    });
    $('#registrationDialog').bind( "dialogclose", function(event, ui) {
        refreshPage(0, false);
    });
});

function submitFormReview(radio, formId, fromStatus) {
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
      return function() {refreshPage(0, false); };
    } else {
      return function(data) {
          indicateLoading(false);
          $('#registrationDialog').html(data).dialog("open");
      };
    }
}

function getFormSubmissionUrl(radioState) {
    if (radioState == NONE) {
        return '<s:url namespace="/sponsor/annual/registration/review/ajax" action="clearForm"/>';
    } else if(radioState == ACCEPTED) {
        return '<s:url namespace="/sponsor/annual/registration/review/ajax" action="acceptForm"/>';
    } else {
        return '<s:url namespace="/sponsor/annual/registration/review/ajax" action="commentsEnter"/>';
    }
}

function submitReview() {
    var isAccepted = areAllFormsAccepted()
    handleRegistrationCompletion(isAccepted);
    indicateLoading(false);
}

function areAllFormsAccepted() {
    var rejectedCount = $("#reviewRegistrationFormsTable input:checked").filter(function () {
      return $(this).val() == REJECTED;
    }).length;
    return rejectedCount === 0;
}

function handleRegistrationCompletion(isAccepted) {
    if (isAccepted) {
        acceptRegistration();
    } else {
        rejectRegistration();
    }
}

function approveRegistration() {
    doAction("enterApproveRegistration");
}

function acceptRegistration() {
    doAction("acceptRegistration");
}

function doAction(actionName) {
    var url = '<s:url namespace="/sponsor/annual/registration/review/ajax" action="' + actionName + '"/>';
       $.unsubscribe('ajaxError');
       $.post(url, {'registration.id': ${registration.id}}, function(data) {
           if (isBlank(data)) {
               refreshPage();
           } else {
               $('#registrationDialog').html(data).dialog("open");
           }
       }, 'html');
}
function rejectRegistration(url) {
    var url = '<s:url namespace="/sponsor/annual/registration/review/ajax" action="reviewCommentsEnter"/>';
    $('#registrationDialog').load(url,{"registration.id" : ${registration.id}}).dialog("open");
}

function toggleReviewOnHold() {
  _fbUtil.performAjaxPost("${enterToggleReviewOnHoldCommentsUrl}", "#registrationDialog");
  $("#registrationDialog").dialog('open');
}

</script>

