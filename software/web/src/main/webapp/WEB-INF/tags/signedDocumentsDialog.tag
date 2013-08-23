<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript" src="<c:url value='/scripts/document_signing.js'/>"></script>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <h2 id="signedDocumentsTitle"><fmt:message key="registration.submitted.title" /></h2>
            <p>
                <jsp:doBody/>
            </p>
            <table id="documentsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                   summary="This table shows all of the forms being signed and submitted. It displays the filename
                                which can be clicked to download the file, if the file requires signing and if it has been
                                signed.">
                <thead>
                    <tr>
                        <th scope="col" width="0px"><%--parentForm?--%></th>
                        <th scope="col" width="70px"><div><fmt:message key="form.filename.header" /></div></th>
                        <th scope="col" width="30px"><div><fmt:message key="form.requires.digital.signature.header" /></div></th>
                        <th scope="col" width="30px"><div><fmt:message key="form.is.digitally.signed.header" /></div></th>
                    </tr>
                </thead>
            </table>
            <div class="btn_bar">
                <s:a id="closeButton" href="#" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.close"/></s:a>
            </div>
        </div>
    </div>
</div>
<s:url var="downloadFormUrl" action="downloadForm" >
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<s:url var="downloadDocumentUrl" namespace="/investigator/profile/files/ajax" action="download" >
    <s:param name="profile.id" value="%{profile.id}"/>
</s:url>
<script type="text/javascript">
    indicateLoading(false);
    var documentRows = ${documentsJson};
    var formDownloadUrl = '<s:property value="downloadFormUrl" />';
    var documentDownloadUrl = '<s:property value="downloadDocumentUrl" />';
    $(document).ready(renderDocumentsTable(documentRows, formDownloadUrl, documentDownloadUrl, "documentsTable"));
</script>
