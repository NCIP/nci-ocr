<%@ tag body-content="empty" %>
<%@ attribute name="organization" type="gov.nih.nci.firebird.data.Organization" rtexprvalue="true" required="true" %>
<%@ attribute name="displayLine" type="java.lang.Boolean" rtexprvalue="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div class="sponsorSection">
    <div class="formcol">
        <firebird:label messageKey="profile.organization.name" bold="true"/>
        <div id="orgName" class="_orgName">${organization.name}</div>

        <firebird:label messageKey="profile.postalAddress" bold="true"/>
        <div id="orgAddress" class="_orgAddress">
            <firebird:addressDisplay address="${organization.postalAddress}" />
        </div>
    </div>

    <div class="formcol">
        <firebird:label messageKey="profile.email" bold="true"/>
        <div id="orgEmail" class="_orgEmail">${organization.email}</div>

        <firebird:label messageKey="profile.phoneNumber" bold="true"/>
        <div id="orgPhone" class="_orgPhone">${organization.phoneNumber}</div>
    </div>
    <s:if test="%{#attr.displayLine}">
        <div class="line"></div>
    </s:if>
    <s:else>
        <div class="clear"></div>
        <br />
    </s:else>
</div>