<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<div id="supportingDocumentationDiv">
    <firebird:instructionBubble messageKey="registration.financial.disclosure.instructions.supporting.documents.pii.warning"/>
    <div>
    <span id="requiredDocumentationAsterisk" class="validationErrorAsterisk" style="display:none; float:left">*</span>
    <s:form id="financialDisclosureDocumentsForm" action="addDocument" enctype="multipart/form-data" method="POST">
        <s:hidden name="registration.id" />
        <div class="float">
            <s:file name="document.data" id="document_data" size="30" label="File"/>
        </div>
        <div class="btn_upload">
            <sj:submit targets="financialDisclosureTab" value="Upload" id="uploadSupportingDocument" onclick="indicateLoading();" />
        </div>
    </s:form>

<firebird:supportingDocumentsDisplay />