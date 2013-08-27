<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:dialogLayout>
    <div style="padding-top: 30px">
    <h1>
        <fmt:message key="annual.registration.review.finalize.dialog.header"/>
    </h1>
    <h3>
        <fmt:message key="annual.registration.review.finalize.dialog.text"/>
    </h3>

    <div class="btn_bar" style="margin-top: 40px;">
        <s:url var="approveUrl" namespace="/sponsor/annual/registration/review/ajax" action="enterApproveRegistration">
            <s:param name="registration.id">${registration.id}</s:param>
        </s:url>

        <sj:a id="approveRegistrationBtn" value="Approve"
              targets="registrationDialog" cssClass="button" href="javascript:void(0);"
              onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.approve.registration"/></sj:a>
        <s:a href="#" id="returnBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.return.to.overview"/></s:a>
    </div>
    </div>
</firebird:dialogLayout>

<script>
    $(document).ready(function () {
        $('#approveRegistrationBtn').click(function () {
            var url = '${approveUrl}';
            var target = getCurrentDialog();
            _fbUtil.performAjaxPost(url, target);
        });
        $("input").keydown(testForEnter);
    });
</script>
