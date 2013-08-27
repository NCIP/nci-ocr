<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
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

                <sj:a id="approvePacketBtn" value="Approve"
                      targets="registrationDialog" cssClass="button" href="%{approveUrl}"
                      onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.approve.registration"/></sj:a>
                <s:a href="#" id="returnBtn" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.return.to.overview"/></s:a>
            </div>
        </div>
    </div>
</div>
