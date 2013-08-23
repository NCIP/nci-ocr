<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<link rel="stylesheet" href="<c:url value='/styles/accordion.css'/>" type="text/css" />

<s:url var="workHistorySectionUrl" action="enterType" escapeAmp="false">
    <s:param name="credentialType" value="%{@gov.nih.nci.firebird.data.CredentialType@WORK_HISTORY.name()}" />
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="licensesSectionUrl" action="enterType" escapeAmp="false">
    <s:param name="credentialType" value="%{@gov.nih.nci.firebird.data.CredentialType@MEDICAL_LICENSE.name()}" />
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="degreesSectionUrl" action="enterType" escapeAmp="false">
    <s:param name="credentialType" value="%{@gov.nih.nci.firebird.data.CredentialType@DEGREE.name()}" />
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="internshipsSectionUrl" action="enterType" escapeAmp="false">
    <s:param name="credentialType" value="%{@gov.nih.nci.firebird.data.CredentialType@INTERNSHIP.name()}" />
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="residenciesSectionUrl" action="enterType" escapeAmp="false">
    <s:param name="credentialType" value="%{@gov.nih.nci.firebird.data.CredentialType@RESIDENCY.name()}" />
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="fellowshipsSectionUrl" action="enterType" escapeAmp="false">
    <s:param name="credentialType" value="%{@gov.nih.nci.firebird.data.CredentialType@FELLOWSHIP.name()}" />
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="specialtiesSectionUrl" action="enterType" escapeAmp="false">
    <s:param name="credentialType" value="%{@gov.nih.nci.firebird.data.CredentialType@SPECIALTY.name()}" />
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="certificationsSectionUrl" action="enterType" escapeAmp="false">
    <s:param name="credentialType" value="%{@gov.nih.nci.firebird.data.CredentialType@CERTIFICATION.name()}" />
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="certificatesSectionUrl" action="enterType" escapeAmp="false">
    <s:param name="credentialType" value="%{@gov.nih.nci.firebird.data.CredentialType@CERTIFICATE.name()}" />
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="experienceSectionUrl" action="viewExperience" >
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>

<sj:accordion id="accordion" heightStyle="content" >

    <sj:accordionItem id="workHistorySection" title="%{getText('credentials.work.history.title')}" onclick="credentialsTab.reloadTopic = 'reloadWorkHistory';" >
        <sj:div id="workHistoryDiv" href="%{workHistorySectionUrl}" cssClass="credentialSection" reloadTopics="reloadWorkHistory" />
    </sj:accordionItem>

    <sj:accordionItem id="licensesSection" title="%{getText('credentials.license.title')}" onclick="credentialsTab.reloadTopic = 'reloadLicenses';" >
        <sj:div id="licenseDiv" href="%{licensesSectionUrl}" cssClass="credentialSection" reloadTopics="reloadLicenses" />
    </sj:accordionItem>

    <sj:accordionItem id="degreesSection" title="%{getText('credentials.degree.title')}" onclick="credentialsTab.reloadTopic = 'reloadDegrees';" >
        <sj:div id="degreeDiv" href="%{degreesSectionUrl}" cssClass="credentialSection" reloadTopics="reloadDegrees" />
    </sj:accordionItem>

    <sj:accordionItem id="internshipsSection" title="%{getText('credentials.internships.title')}" onclick="credentialsTab.reloadTopic = 'reloadInternships';" >
        <sj:div id="internshipsDiv" href="%{internshipsSectionUrl}" cssClass="credentialSection" reloadTopics="reloadInternships" />
    </sj:accordionItem>

    <sj:accordionItem id="residenciesSection" title="%{getText('credentials.residencies.title')}" onclick="credentialsTab.reloadTopic = 'reloadResidencies';" >
        <sj:div id="residenciesDiv" href="%{residenciesSectionUrl}" cssClass="credentialSection" reloadTopics="reloadResidencies" />
    </sj:accordionItem>

    <sj:accordionItem id="fellowshipsSection" title="%{getText('credentials.fellowships.title')}" onclick="credentialsTab.reloadTopic = 'reloadFellowships';" >
        <sj:div id="fellowshipsDiv" href="%{fellowshipsSectionUrl}" cssClass="credentialSection" reloadTopics="reloadFellowships" />
    </sj:accordionItem>

    <sj:accordionItem id="specialtiesSection" title="%{getText('credentials.specialty.title')}" onclick="credentialsTab.reloadTopic = 'reloadSpecialties';" >
        <sj:div id="specialtiesDiv" href="%{specialtiesSectionUrl}" cssClass="credentialSection" reloadTopics="reloadSpecialties" />
    </sj:accordionItem>

    <sj:accordionItem id="certificationsSection" title="%{getText('credentials.certification.title')}" onclick="credentialsTab.reloadTopic = 'reloadCertifications';" >
        <sj:div id="certificationsDiv" href="%{certificationsSectionUrl}" cssClass="credentialSection" reloadTopics="reloadCertifications" />
    </sj:accordionItem>

    <sj:accordionItem id="certificatesSection" title="%{getText('credentials.certificate.title')}" onclick="credentialsTab.reloadTopic = 'reloadCertificates';" >
        <sj:div id="certificatesDiv" href="%{certificatesSectionUrl}" cssClass="credentialSection" reloadTopics="reloadCertificates" />
    </sj:accordionItem>

    <sj:accordionItem id="experienceSection" title="%{getText('credentials.experience.title')}" onclick="credentialsTab.reloadTopic = 'reloadExperience';" >
        <sj:div id="experienceDiv" href="%{experienceSectionUrl}" cssClass="credentialSection" reloadTopics="reloadExperience" />
    </sj:accordionItem>

</sj:accordion>

<script>

var credentialsTab = {
  reloadTopic: "reloadWorkHistory",

  isCredentialsTab: function(current_index) {
      return current_index == 1;
  }
}

function reloadCurrentTab(tabset) {
  indicateLoading(true);
  if (!(tabset) || isBlank(tabset)) {
      tabset = "tabwrapper";
  }
  var current_index = getCurrentSelectedTabIndex(tabset);

  if (credentialsTab.isCredentialsTab(current_index)) {
      $.publish(credentialsTab.reloadTopic);
  } else {
      $("#" + tabset).tabs('load', current_index);
  }
  indicateLoading(false);
}

</script>