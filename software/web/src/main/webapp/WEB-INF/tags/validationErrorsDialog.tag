<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader><span class="ui-icon ui-icon-alert"></span><fmt:message key="registration.submit.forms.incomplete"/></firebird:dialogHeader>
            <firebird:messages/>
            <div class="btn_bar">
                <s:a id="closeBtn" href="#" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.close"/></s:a>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

$(function() {
    indicateLoading(false);
});

</script>