<%@ tag body-content="scriptless" %>
<%@ attribute name="location" required="false"%>

<div class="${location == 'full' ? 'formcol_row' : 'section_wrapper ' } ${location == 'right' ? 'section_wrapper_right' : ''}">
    <jsp:doBody/>
</div>