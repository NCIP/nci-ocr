<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div id="approveDiv">
    <firebird:dialogLayout>
        <h1>
            <fmt:message key="registration.review.finalize.dialog.header"/>
        </h1>
        <h3>
            <fmt:message key="registration.review.finalize.dialog.text"/>
        </h3>

        <div class="btn_bar">
            <s:url var="approveUrl" namespace="/sponsor/protocol/review/ajax" action="approvePacket">
                <s:param name="registration.id">${registration.id}</s:param>
            </s:url>

            <s:a href="#" id="approvePacketBtn" cssClass="button" onclick="approvePacket();"><fmt:message key="button.approve.registration"/></s:a>
            <s:a href="#" id="returnBtn" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.return.to.overview"/></s:a>
        </div>
    </firebird:dialogLayout>
</div>

<div id="validatingDiv" class="hide">
    <firebird:dialogLayout>
        <firebird:spinner messageKey="label.validating"/>
        <div id="approved" class="hide">
            <h1 class="centeredText"><img src="<c:url value='/images/veri_check.png'/>" alt="Success" /><fmt:message key="label.approved"/></h1>
            <p class="centeredText"><fmt:message key="sponsor.annual.registration.approved"/></p>
            <div class="center" style="width:130px;">
                <a id="closeBtn" href="javascript:void(0)" class="button blueButton bigButton" onclick="closeDialogAndReload();"><fmt:message key="button.close"/></a>
            </div>

        </div>
    </firebird:dialogLayout>
</div>

<script>

function approvePacket() {
    $("#approveDiv").hide();
    $("#validatingDiv").show();
    $(".ui-dialog-titlebar").hide();
    var target = getCurrentDialog();
    target.dialog("option", "width", 400);
    $(".ui-tabs-panel", target).height(200);
    $("#registrationDialog #tabwrapper").width(370);
    target.dialog("option", "position", "center");

    var url = '${approveUrl}';
    _fbUtil.performAjaxPost(url, null, function() {
        $("#spinner").hide();
        $("#approved").show();
    });
}

</script>
