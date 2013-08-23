<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:dialogLayout>
    <div id="validationErrorMessages">
        <firebird:dialogHeader><span class="ui-icon ui-icon-alert"></span><fmt:message key="registration.protocol.updated.submission.warning.title"/></firebird:dialogHeader>
        <ul class="fielderror">
            <li><fmt:message key="registration.protocol.updated.submission.warning" /></li>
        </ul>
        <div class="btn_bar">
            <s:a id="closeBtn" href="#" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.close"/></s:a>
        </div>
    </div>
</firebird:dialogLayout>

<script>
  $('#registrationDialog').unbind("dialogclose");
  $('#registrationDialog').bind("dialogclose", function(event, ui) {
    refreshPage(0);
  });
</script>