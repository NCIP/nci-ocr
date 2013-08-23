<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages />
            <firebird:dialogHeader>
                <fmt:message key="registration.coordinator.investigators.added.success.title" />
            </firebird:dialogHeader>

            <fmt:message key="registration.coordinator.investigators.added.success.message" />

            <div class="btn_bar clear">
                <s:a href="#" id="closeBtn" cssClass="btn" onclick="closeDialog({reload: true, reloadPage: true});" ><fmt:message key="button.close"/></s:a>
            </div>
        </div>
    </div>
</div>
