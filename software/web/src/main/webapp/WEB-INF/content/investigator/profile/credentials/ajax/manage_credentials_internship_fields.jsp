<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:manageCredentialFields organizationPrefix="internship" saveAction="saveInternship" searchAgainAction="manageInternshipCredentials">
    <firebird:medicalTrainingCommonFields titleKey="label.internships" getSpecialtiesAction="getInternshipSpecialties" specialtyLabelKey="label.specialty" />
</firebird:manageCredentialFields>