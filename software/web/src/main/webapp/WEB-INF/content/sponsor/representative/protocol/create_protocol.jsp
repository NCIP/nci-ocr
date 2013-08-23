<%@ include file="/WEB-INF/content/common/taglibs.jsp" %>
<h1><fmt:message key="sponsor.protocol.create" /></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form id="protocolForm" action="save" namespace="/sponsor/representative/protocol">
                <%@include file="protocol_fields.jspf" %>
            </s:form>
            <br>
            <div class="btn_bar">
                <sj:a href="#" cssClass="button" onclick="indicateLoading();" formIds="protocolForm" id="saveButton"><fmt:message key="button.save"/></sj:a>
                <s:url namespace="/sponsor/protocol" action="enterBrowseProtocols" var="protocolsUrl" />
                <s:a id="cancelButton" href="%{protocolsUrl}" cssClass="button"><fmt:message key="button.cancel"/></s:a>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $("input").keydown(testForEnter);
    });
</script>
