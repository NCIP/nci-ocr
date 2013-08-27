<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<firebird:messageDialog titleKey="registration.protocol.updated.submission.warning.title"
    messageKey="registration.protocol.updated.submission.warning" />

<script>
  $('#registrationDialog').unbind("dialogclose");
  $('#registrationDialog').bind("dialogclose", function(event, ui) {
    refreshPage(0);
  });
</script>