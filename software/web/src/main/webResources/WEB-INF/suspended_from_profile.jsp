<%@ include file="/WEB-INF/content/common/headIncludes.jsp"%>

<div id="errors">
    <p class="fielderrors">
        <fmt:message key="registration.coordinator.suspended.from.profile.message"></fmt:message>
    </p>
</div>
<script>
    function forwardHome(){
        window.location = '<s:url value="/"/>';
    }
    
    $(document).ready(function (){
        setTimeout(forwardHome, 5000);
    });
</script>
