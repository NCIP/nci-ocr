<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:manageCredentialFields organizationPrefix="fellowship" saveAction="saveFellowship" searchAgainAction="manageFellowshipCredentials">
    <firebird:medicalTrainingCommonFields titleKey="label.fellowships" getSpecialtiesAction="getFellowshipSpecialties" specialtyLabelKey="label.sub.specialty" />
</firebird:manageCredentialFields>