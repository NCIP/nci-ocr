<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<script src="<c:url value='/scripts/jquery-ui.toggleSwitch.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/styles/toggle-switch.css'/>" type="text/css" />

<s:url action="enterToggleReviewOnHoldComments" namespace="/sponsor/annual/ctep/registration/review/ajax" var="enterToggleReviewOnHoldCommentsUrl">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<s:url action="enterDisqualifyInvestigator" namespace="/sponsor/annual/ctep/registration/review/ajax" var="enterDisqualifyInvestigatorUrl">
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
    <s:if test="controlsEnabled">
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

        <s:if test="controlsEnabled">
            <sj:a id="disqualifyInvestigatorButton" cssClass="button float_right" href="%{enterDisqualifyInvestigatorUrl}" targets="registrationDialog" onclick='$("#registrationDialog").dialog("open");'>
                <fmt:message key="button.disqualify.investigator"/>
            </sj:a>
            <div class="clear"></div>
        </s:if>

        <firebird:registrationComments registration="${registration}" retainState="true"/>
        <div class="dotted_line"></div>
        <firebird:sponsorReviewRegistrationFormsTable ctepRegistration="true"/>

        <s:if test="completable">
            <sj:a id="completeReviewButton" href="javascript:void(0)" cssClass="button blueButton completeReviewButton" targets="registrationDialog" onclick="submitReview();" >
                <fmt:message key="button.complete.review" />
            </sj:a>
        </s:if>
        <s:elseif test="approvable">
            <sj:a id="approveRegistrationButton" href="javascript:void(0)"
                  cssClass="button blueButton completeReviewButton" targets="registrationDialog"
                  onclick="approveRegistration();">
                <fmt:message key="button.approve.registration"/>
            </sj:a>
        </s:elseif>
        <s:elseif test="ctepReviewer">
            <firebird:instructionBubble messageKey="complete.review.button.instructions.${registration.status}" hasIndicator="false" />
        </s:elseif>
        <div class="clear"></div>

    </div>
</div>

<script>

var REJECTED = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@REJECTED" />';
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
    var url = '<s:url namespace="/sponsor/annual/ctep/registration/review/ajax" action="' + actionName + '"/>';
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
    var url = '<s:url namespace="/sponsor/annual/ctep/registration/review/ajax" action="reviewCommentsEnter"/>';
    $('#registrationDialog').load(url,{"registration.id" : ${registration.id}}).dialog("open");
}

function toggleReviewOnHold() {
  _fbUtil.performAjaxPost("${enterToggleReviewOnHoldCommentsUrl}", "#registrationDialog");
  $("#registrationDialog").dialog('open');
}

</script>

