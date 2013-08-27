<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
TESTING
<firebird:messageDialog titleKey="import.protocol.messages.title" messageKey="import.protocol.messages.message" reloadOnClose="false" >
    <div class="messages">
        <div class="errors">
            <ul class="fielderror" >
                <s:iterator var="failure" value="%{protocolImportJob.details[selectedIndexForImport[0]].validationResult.failures}">
                   <li class="pageErrorMessage"><s:property value="#failure.message"/></li>
                </s:iterator>
            </ul>
        </div>
    </div>
</firebird:messageDialog>