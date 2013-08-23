<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:manageCredentialSearch organizationSearchLabel="label.where.was.it.completed" saveAction="manageFellowshipCredentials">
    <firebird:medicalTrainingCommonFields titleKey="label.fellowships" getSpecialtiesAction="getFellowshipSpecialties" specialtyLabelKey="label.sub.specialty" />
</firebird:manageCredentialSearch>