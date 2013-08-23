<%@ include file="/WEB-INF/content/common/headIncludes.jsp"%>
<div id="errors">
    <p class="fielderrors">
        <fmt:message key="403.message"></fmt:message>
    </p>
</div>
<script>
    function forwardHome(){
        window.location = '<s:url value="/"/>';
    }
    
    $(document).ready(function (){
        setTimeout('forwardHome()', 5000);
    });
</script>
