<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h1><fmt:message key="user.registration.sponsor.delegate.selection.title"/></h1>
<s:set var="selectedSponsorIds" value="%{selectedSponsors.{#this.id}}"/>
<s:if test="currentUser.sponsorRepresentative">
    <s:set var="disabledSponsorIds" value="%{currentUser.sponsorRepresentativeOrganizations.{#this.id}}"/>
</s:if>
<s:else>
    <s:set var="disabledSponsorIds" value="%{accountConfigurationData.sponsorOrganizations.{#this.id}}"/>
</s:else>
<firebird:sponsorOrganizationSelection
    disabledSponsorIds="${disabledSponsorIds}"
    selectedSponsorIds="${selectedSponsorIds}"
    disabledSponsorKey="user.add.roles.sponsor.delegate.disabled.organizations.message"
/>
