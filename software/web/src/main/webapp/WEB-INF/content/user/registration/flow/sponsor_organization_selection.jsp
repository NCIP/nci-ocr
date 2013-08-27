<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h1><fmt:message key="user.registration.sponsor.organization.selection.title"/></h1>
<s:set var="selectedSponsorIds" value="%{selectedSponsors.{#this.id}}"/>
<s:if test="currentUser.sponsorDelegate">
    <s:set var="disabledSponsorIds" value="%{currentUser.sponsorDelegateOrganizations.{#this.id}}"/>
</s:if>
<s:else>
    <s:set var="disabledSponsorIds" value="%{accountConfigurationData.delegateOrganizations.{#this.id}}"/>
</s:else>
<firebird:sponsorOrganizationSelection
    disabledSponsorIds="${disabledSponsorIds}"
    selectedSponsorIds="${selectedSponsorIds}"
    disabledSponsorKey="user.add.roles.sponsor.organization.disabled.organizations.message"
/>