<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/user/registration/nav" action="enterCurrentStep" var="currentStepUrl"/>
<div id="registrationContent"></div>
<div id="flowErrors" class="hide">
    <s:actionerror cssClass='fielderror'/>
</div>
<script>
$(document).ready(function() {
    $('#registrationContent').load('<s:property value="%{currentStepUrl}"/>', function() {
        $('div.errors').html($('#flowErrors').html());
        $('#flowErrors').remove();
    });
});
</script>