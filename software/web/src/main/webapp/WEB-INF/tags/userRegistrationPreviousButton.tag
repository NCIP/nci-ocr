<%@ tag body-content="empty" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<s:if test="#session[@gov.nih.nci.firebird.web.common.FirebirdUIConstants@REGISTRATION_FLOW_CONTROLLER].hasPreviousStep()">
    <s:a id="previousStepBtn" cssClass="button" href="javascript:void(0)"><fmt:message key="button.previous"/></s:a>

    <script>
    $(document).ready(function(){
        $('#previousStepBtn').click(function() {
            indicateLoading();
            var url = '<s:url action="previousStep"/>';
            var serializedForm = $('form').serialize();
            $.post(url, serializedForm, function(data) {
                $('#registrationContent').html(data);
                indicateLoading(false);
            });
        });
    });

    </script>
</s:if>
