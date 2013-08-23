<%@ tag body-content="empty" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>


<firebird:dialogHeader>
	<fmt:message key="credentials.degree.title" />
</firebird:dialogHeader>
 
<div class="formcol_degree_select"> 
     <s:select list="degreesWithRankedOnTop" 
         name="degreeTypeId" 
         required="true"
         label="%{getText('dropdown.degreeType')}"
         listKey="id" 
         listValue="name" 
         headerKey=""
         headerValue="-- Select --"
         id="credentialsDegreeType"/>
</div>

<firebird:dateSelect fieldName="effectiveDate" label="textfield.effectiveDate.degree" required="true"/>

<s:hidden id="degreeId" name="id" />

<div class="clear"></div>

<script>
    $(document).ready(function() {
	    var index = <s:property value="rankedDegrees.size"/>;
	    addSeperatorToSelectList('credentialsDegreeType', index);
    });
</script>
