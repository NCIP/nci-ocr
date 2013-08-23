<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<script type="text/javascript" src="<c:url value='/scripts/document_signing.js'/>"></script>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <div>
                <firebird:dialogHeader><fmt:message key="forms.complete.for.submission.title" /></firebird:dialogHeader>
                <p><fmt:message key="forms.complete.for.submission.message" /></p>
                <p><fmt:message key="forms.submission.includes.message" /></p>

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
            </div>
            <div>
                <s:form action="sign" id="signForm">
                <jsp:doBody />
                <firebird:messages/>
                <firebird:instructionBubble messageKey="forms.submission.authentication.message" />
                    <s:textfield label="%{getText('login.username')}" id="username" name="username" requiredLabel="true" size="15" maxlength="100" style="width:175px"/>
                    <s:password label="%{getText('login.password')}" name="password" id="password" requiredLabel="true" size="15" maxlength="100" style="width:175px" />
                    <s:hidden name="registration.id"/>
                    <br/>

                    <div class="btn_bar">
                        <sj:a id="sign" value="Sign" targets="registrationDialog" onclick="indicateLoading(true)"
                        cssClass="button" formIds="signForm" href="#" onClickTopics="submit" onSuccessTopics="enable">
                            <fmt:message key="button.sign"/></sj:a>
                        <s:a id="closeButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.close"/></s:a>
                    </div>
                </s:form>

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

$(function() {
    indicateLoading(false);
    $("#username").focus();
    var documentRows = ${documentsJson};
    var formDownloadUrl = '<s:property value="downloadFormUrl" />';
    var documentDownloadUrl = '<s:property value="downloadDocumentUrl" />';
    $(document).ready(function () {
        renderDocumentsTable(documentRows, formDownloadUrl, documentDownloadUrl, "documentsTable");
        $("input").keydown(testForEnter);
    });
});

</script>
