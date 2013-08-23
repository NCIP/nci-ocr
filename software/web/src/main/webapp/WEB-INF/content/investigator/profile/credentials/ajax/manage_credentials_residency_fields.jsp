<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:manageCredentialFields organizationPrefix="residency" saveAction="saveResidency" searchAgainAction="manageResidencyCredentials">
    <firebird:medicalTrainingCommonFields titleKey="label.residencies" getSpecialtiesAction="getResidencySpecialties" specialtyLabelKey="label.specialty" />
</firebird:manageCredentialFields>