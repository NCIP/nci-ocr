<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:dialogLayout>
    <firebird:spinner messageKey="label.validating"/>
    <div id="approved" class="hide">
        <h1 class="centeredText"><img src="<c:url value='/images/veri_check.png'/>" alt="Success" /><fmt:message key="label.approved"/></h1>
        <p class="centeredText"><fmt:message key="sponsor.annual.registration.approved"/></p>
        <div class="center" style="width:130px;">
            <a id="closeBtn" href="javascript:void(0)" class="button blueButton bigButton" onclick="closeDialog();"><fmt:message key="button.close"/></a>
        </div>

    </div>
</firebird:dialogLayout>

<s:url var="approveUrl" namespace="/sponsor/annual/ctep/registration/review/ajax" action="approveRegistration">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<script>
    $(document).ready(function () {
        $(".ui-dialog-titlebar").hide();
        var target = getCurrentDialog();
        target.dialog("option", "width", 400);
        $(".ui-tabs-panel", target).height(200);
        $("#registrationDialog #tabwrapper").width(370);
        target.dialog("option", "position", "center");

        var url = '${approveUrl}';
        _fbUtil.performAjaxPost(url, null, function() {
            setTimeout(function() {
              $("#spinner").hide();
              $("#approved").show();
            }, 500);
        });

        $("input").keydown(testForEnter);
    });
</script>
