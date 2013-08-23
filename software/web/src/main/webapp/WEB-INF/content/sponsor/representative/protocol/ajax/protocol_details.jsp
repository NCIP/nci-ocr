<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<s:set var="protocol" value="protocolImportJob.details[selectedIndexForImport[0]].protocol"/>

<firebird:messageDialog titleKey="import.protocol.details.title" reloadOnClose="false">
    <firebird:protocolInformation protocol="${protocol}" />
</firebird:messageDialog>