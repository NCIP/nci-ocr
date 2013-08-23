<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<firebird:dialogHeader>
    <fmt:message key="label.work.history" />
</firebird:dialogHeader>

<s:textfield name="workHistory.position" id="position" label="%{getText('label.position')}" requiredLabel="true" cssClass="twocol" maxlength="%{@gov.nih.nci.firebird.data.WorkHistory@POSITION_COLUMN_LENGTH}"/>

<div class="clear"></div>

<div class="formcol">
    <firebird:dateSelect fieldName="effectiveDate" label="label.start.date" required="true"/>
</div>
<div class="formcol">
    <firebird:dateSelect fieldName="expirationDate" label="label.end.date" forwardRangeInYears="5" />
</div>

<s:hidden id="workHistoryId" name="id" />

<div class="clear"></div>