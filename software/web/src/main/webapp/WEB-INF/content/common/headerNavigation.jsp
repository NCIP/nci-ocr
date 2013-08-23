<%@ include file="/WEB-INF/content/common/taglibs.jsp" %>
<s:url namespace="/investigator/profile" action="home" var="profileUrl" />
<s:url namespace="/investigator/coordinators" action="browse" var="coordinatorsUrl" />
<s:url namespace="/investigator/annual/registration" action="enterBrowseAnnualRegistrations" var="annualRegistrationsUrl" />
<s:url namespace="/investigator/registration" action="enterRegistrations" var="registrationsUrl" />
<s:url namespace="/sponsor/representative/protocol" action="createProtocolEnter" var="createProtocolUrl" />
<s:url namespace="/sponsor/protocol" action="enterBrowseProtocols" var="browseProtocolEnterUrl" />
<s:url namespace="/sponsor/protocol/export" action="enterExportProtocols" var="exportProtocolsEnterUrl" />
<s:url namespace="/sponsor/representative/protocol" action="enterProtocolImport" var="importProtocolEnterUrl" />
<s:url namespace="/sponsor/representative" action="enterBrowseSponsorDelegates" var="browseSponsorDelegatesUrl" />
<s:url namespace="/sponsor/representative/export" action="enterDataToCurate" var="enterDataToCurateUrl" />
<s:url namespace="/sponsor/annual/ctep/representative" action="enterRequiredForms" var="requiredFormsUrl" />
<s:url namespace="/sponsor/annual/registration" action="enterBrowseAnnualRegistrations" var="enterBrowseAnnualRegistrationsUrl" />
<s:url namespace="/sponsor/annual/investigator" action="enterBrowseCtepInvestigators" var="enterBrowseCtepInvestigatorsUrl" />

<ul id="nav" class="sf-menu">
    <li><a id="homeBtn" href="<c:url value='/'/>"><fmt:message key="menu.heading.home"/></a></li>

    <!-- Investigator Menu -->
    <s:if test="%{investigator || ctepInvestigator}">
        <li>
            <s:a id="investigatorBtn" href="%{profileUrl}">
                <span class="arrow_down"><fmt:message key="menu.heading.investigator"/></span>
            </s:a>
            <ul>
                <li><a href="${profileUrl}"><fmt:message key='investigator.menu.item.profile'/></a></li>
                <li><a href="${registrationsUrl}"><fmt:message key='investigator.menu.item.registrations'/></a></li>
                <s:if test="%{ctepInvestigator}">
                    <li><a href="${annualRegistrationsUrl}"><fmt:message key='investigator.menu.item.annual.registrations'/></a></li>
                </s:if>
                <li><a href="${coordinatorsUrl}"><fmt:message key='investigator.menu.item.coordinators'/></a></li>
            </ul>
        </li>
    </s:if>

    <!-- Coordinator Menu -->
    <s:if test="%{registrationCoordinator || ctepRegistrationCoordinator}">
        <s:url var="investigatorsUrl" namespace="/coordinator/investigators" action="browse"/>
        <li class="investigator">
            <s:a id="investigatorsBtn" href="%{investigatorsUrl}">
                <fmt:message key="menu.heading.investigators"/>
            </s:a>
        </li>
    </s:if>

    <!-- Protocol Menu -->
    <s:if test="%{verifiedSponsor || verifiedSponsorDelegate}">
        <li>
            <s:a id="protocolBtn" href="%{browseProtocolEnterUrl}" >
                <span class="arrow_down"><fmt:message key="menu.heading.protocols"/></span>
            </s:a>
            <ul>
                <li><a href="${browseProtocolEnterUrl}"><fmt:message key='menu.item.protocols.browse'/></a></li>
                <s:if test="%{currentUser != null && currentUser.sponsorRepresentative}">
                    <li><a href="${createProtocolUrl}"><fmt:message key='menu.item.protocols.create'/></a></li>
                    <s:if test="%{#session[@gov.nih.nci.firebird.web.common.FirebirdUIConstants@PROTOCOL_IMPORT_JOB] == null}">
                        <li><a href="${importProtocolEnterUrl}"><fmt:message key='menu.item.protocols.import'/></a></li>
                    </s:if><s:else>
                        <li><a href="${importProtocolEnterUrl}"><fmt:message key='menu.item.protocols.import.in.progress'/></a></li>
                    </s:else>
                    <li><a href="${exportProtocolsEnterUrl}"><fmt:message key='menu.item.protocols.export'/></a></li>
                </s:if>
            </ul>
        </li>
    </s:if>

    <!-- Annual Registration Menu -->
    <s:if test="%{ctepSponsor || ctepSponsorDelegate || verifiedSponsor || verifiedSponsorDelegate}">
        <li>
            <s:a id="annualRegistrationsBtn" href="%{enterBrowseAnnualRegistrationsUrl}">
                <span class="arrow_down"><fmt:message key="menu.heading.annual.registrations"/></span>
            </s:a>

            <ul>
                <li><a href="${enterBrowseAnnualRegistrationsUrl}"><fmt:message key='label.submissions'/></a></li>
                <s:if test="%{ctepSponsor}">
                    <li><a href="${requiredFormsUrl}"><fmt:message key='label.required.forms'/></a></li>
                </s:if>
                <li><a href="${enterBrowseCtepInvestigatorsUrl}"><fmt:message key='menu.item.ctep.investigator.search'/></a></li>
            </ul>
        </li>
    </s:if>

    <!-- Sponsor Menu -->
    <s:if test="%{verifiedSponsor}">
        <li>
            <s:a id="sponsorBtn" href="%{browseSponsorDelegatesUrl}">
                <span class="arrow_down"><fmt:message key="menu.heading.sponsor"/></span>
            </s:a>
            <ul>
                <li><a href="${browseSponsorDelegatesUrl}"><fmt:message key='menu.heading.sponsor.delegates'/></a></li>
                <li><a href="${enterDataToCurateUrl}"><fmt:message key='menu.heading.export.data.to.curate'/></a></li>
            </ul>
        </li>
    </s:if>
</ul>

<!--User Area-->
<%@ include file="/WEB-INF/content/common/headerUserSection.jsp" %>
<!--/User Area-->
<script>
$(function() {
    $("UL.sf-menu").superfish({
        delay:       200,                             // delay on mouseout
        animation:   {opacity:'show',height:'show'},  // fade-in and slide-down animation
        speed:       'fast',                          // faster animation speed
        autoArrows:  false                            // disable generation of arrow mark-up
    });
});
</script>