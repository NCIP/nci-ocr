<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div class="blank_space"></div>
<div class="blank_space"></div>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <fmt:message key="privacy.act.disclaimer"/>
            <div class="btn_bar clear">
                <s:a id="acceptDisclaimer" namespace="/disclaimer" action="acceptDisclaimer" cssClass="button" onclick="indicateLoading();"><fmt:message key="button.accept"/></s:a>
                <s:a id="rejectDisclaimer" namespace="/login" action="logout" cssClass="button" onclick="indicateLoading();"><fmt:message key="button.reject"/></s:a>
            </div>
        </div>
    </div>
</div>
