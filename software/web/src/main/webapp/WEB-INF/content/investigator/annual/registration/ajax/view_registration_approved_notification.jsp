<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader><fmt:message key="label.registration.approved" /></firebird:dialogHeader>

            <pre class="notificationText" >${notification}</pre>

            <div class="btn_bar">
                <s:a id="goToRegistrationButton" cssClass="button float_right marginLeft_5" namespace="/investigator/annual/registration" action="enterViewRegistration">
                    <s:param name="registration.id" value="registration.id"/>
                    <fmt:message key="button.go.to.registration"/>
                </s:a>
                <s:a id="closeButton" href="#" cssClass="button float_right" onclick="closeDialogAndReload();">
                    <fmt:message key="button.close"/>
                </s:a>
            </div>
        </div>
    </div>
</div>