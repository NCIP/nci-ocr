<%@ tag body-content="scriptless" %>
<%@ attribute name="sectionTitleKey" required="true"%>
<%@ attribute name="sectionNum" required="true"%>
<%@ attribute name="id" required="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div id="${id}" class="section_wrapper_bg">
    <div class="section_title">
        <p>
	       ${sectionNum}.  <fmt:message key="${sectionTitleKey}"/>
        </p>
    </div>
    <div class="section_body">
	    <jsp:doBody/>
    </div>
</div>