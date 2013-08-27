<%@ tag body-content="empty" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<s:if test="%{sponsorList.size() > 1}">
    <s:select id="protocolSponsorField" name="sponsor.id"
        list="sponsorList" listKey="id" listValue="name" headerKey=""
        headerValue="%{getText('select.sponsor.default.option')}"
        label="%{getText('dropdown.sponsor')}" requiredLabel="true"
        labelposition="left" />
</s:if>
<s:else>
        <s:hidden id="protocolSponsorField" name="sponsor.id" value="%{sponsorList.get(0).id}" />
        <firebird:label messageKey="dropdown.sponsor" required="true" />
        <span id="sponsorName"><s:property value="%{sponsorList.get(0).name}"/></span>
</s:else>
