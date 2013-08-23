<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/sponsor/protocol" action="enterProtocol" var="protocolUrl">
    <s:param name="protocol.id">${registration.protocol.id}</s:param>
</s:url>
<s:url namespace="/sponsor/protocol/review/ajax" action="downloadForm" var="viewFormUrl">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<s:url namespace="/sponsor/protocol/review/ajax" action="commentsEnter" var="commentsUrl">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<s:url namespace="/sponsor/protocol/review/ajax" action="additionalDocumentsEnter" var="additionalDocumentsEnter">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>

<h2 id="reviewProtocolRegistrationTitle">${headerTitle}</h2>
<h3>
    <span>
        <s:text name="label.status"/>: <span class="statusBullet statusBullet_sponsor_${registration.status}">&bull;</span>
        <span id="registrationStatus">${registration.status.display}</span>
    </span>
</h3>

<firebird:messages />
<firebird:registrationComments registration="${registration}" retainState="true"/>
<div class="dotted_line"></div>

<firebird:sponsorReviewRegistrationFormsTable ctepRegistration="false"/>

<s:if test="registration.completeable">
    <s:a id="completeReviewButton" href="#" cssClass="button blueButton completeReviewButton" onclick="indicateLoading();_reviewPage.submitReview();">
        <fmt:message key="button.complete.review"/>
    </s:a>
</s:if>
<s:else>
    <firebird:instructionBubble messageKey="complete.review.button.instructions.${registration.status}" hasIndicator="false"/>
</s:else>
<div class="clear"></div>

<script type="text/javascript">

    var ACCEPTED = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@ACCEPTED" />';
    var _reviewPage = (function() {
        var reviewPage = {};

        reviewPage.submitReview = function() {
            if(!checkAllFormsCompleted()) {
              alert("why");
                return false;
            }

            var isAccepted = checkAllFormsAccepted()
            handleRegistrationCompletion(isAccepted);
            indicateLoading(false);
        }

        function checkAllFormsCompleted() {
            return $("#reviewRegistrationFormsTable:visible input:checked").length === $("#reviewRegistrationFormsTable:visible input[type='radio']:not(:checked)").length;
        }

        function checkAllFormsAccepted() {
            var isAccepted = true;

            $("#reviewRegistrationFormsTable:visible input:checked").each(function () {
                isAccepted = isAccepted && ($(this).val() == ACCEPTED);
            });
            return isAccepted;
        }

        function handleRegistrationCompletion(isAccepted) {
            if (isAccepted) {
                acceptRegistration();
            } else {
                rejectRegistration();
            }
        }

        function acceptRegistration() {
            var url = '<s:url namespace="/sponsor/protocol/review/ajax" action="acceptRegistration"/>';
            $.post(url, {'registration.id': ${registration.id}}, function(data) {
                if (isBlank(data)) {
                    reloadCurrentTab();
                    setTimeout('setPageSuccessMessage(\'<fmt:message key="registration.review.complete.success"/>\');', 800);
                } else {
                    $('#registrationDialog').html(data).dialog("open");
                }
            }, 'html');
        }

        function rejectRegistration(url) {
            var url = '<s:url namespace="/sponsor/protocol/review/ajax" action="reviewCommentsEnter"/>';
            $('#registrationDialog').load(url,{"registration.id" : ${registration.id}}).dialog("open");
        }

        return reviewPage;
    })();

</script>
