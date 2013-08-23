<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:manageCredentialSearch organizationSearchLabel="label.where.was.it.completed" saveAction="manageInternshipCredentials">
    <firebird:medicalTrainingCommonFields titleKey="label.internships" getSpecialtiesAction="getInternshipSpecialties" specialtyLabelKey="label.specialty" />
</firebird:manageCredentialSearch>