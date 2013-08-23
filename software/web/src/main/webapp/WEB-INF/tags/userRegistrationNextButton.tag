<%@ tag body-content="empty" %>
<%@ attribute name="displayButtonAlways" required="false" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<s:if test="%{#attr.displayButtonAlways || #session[@gov.nih.nci.firebird.web.common.FirebirdUIConstants@REGISTRATION_FLOW_CONTROLLER].hasNextStep()}">
    <s:a id="nextStepBtn" cssClass="button" href="javascript:void(0)"><fmt:message key="button.next"/></s:a>

    <script>
    $(document).ready(function(){
        $('#nextStepBtn').click(function() {
            indicateLoading();
            var url = $('form').first().attr('action');
            var serializedForm = $('form').serialize();
            $.post(url, serializedForm, function(data) {
                $('#registrationContent').html(data);
                indicateLoading(false);
            });
        });
    });

    </script>
</s:if>
