<%@ tag body-content="empty" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<%@ attribute name="titleKey" required="true"%>
<%@ attribute name="getSpecialtiesAction" required="true"%>
<%@ attribute name="specialtyLabelKey" required="true" rtexprvalue="true"%>

<firebird:dialogHeader>
    <fmt:message key="${titleKey}" />
</firebird:dialogHeader>


<div class="formcol_xwide">
    <firebird:label forId="certifyingBoard" messageKey="label.certifying.board" required="true" />
     <s:select list="certifyingBoards"
         id="certifyingBoard"
         name="certifyingBoardId"
         requiredLabel="true"
         listKey="id"
         listValue="name"
         headerKey=""
         headerValue="-- Select --"
         onchange="$.publish('reloadSpecialtiesSelectBox','',this)"/>
</div>

<div class="formcol">
    <s:url var="specialtyTypesUrl" action="%{#attr.getSpecialtiesAction}"/>
    <firebird:label forId="specialty" messageKey="${specialtyLabelKey}" required="true" />
    <sj:select
      id="specialty"
        name="specialtyId"
        requiredLabel="true"
        href="%{specialtyTypesUrl}"
        formIds="credentialForm"
        reloadTopics="reloadSpecialtiesSelectBox"
        list="specialties"
        listKey="id"
        listValue="name"
        headerKey="-1"
        headerValue="-- Select A Specialty --"/>
</div>

<div class="clear" ></div>

<div class="formcol">
    <firebird:dateSelect fieldName="effectiveDate" label="label.start.date" required="true"/>
</div>
<div class="formcol">
    <firebird:dateSelect fieldName="expirationDate" label="label.end.date" forwardRangeInYears="5"/>
</div>

<s:hidden id="specialtyId" name="id" />

<div class="clear"></div>
