<%@ tag body-content="empty" %>
<%@ attribute name="id"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<span class="search_again">
    <a id="${id}" href="javascript:void(0);" class="searchAgainLink" >
         <fmt:message key="button.searchAgain" />
    </a>
</span>