<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:manageCredentialSearch organizationSearchLabel="label.where.was.it.completed" saveAction="manageResidencyCredentials">
    <firebird:medicalTrainingCommonFields titleKey="label.residencies" getSpecialtiesAction="getResidencySpecialties" specialtyLabelKey="label.specialty" />
</firebird:manageCredentialSearch>