<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<s:url var="deleteIconUrl" value='/images/ico_delete.gif' />
<s:url var="deleteUrl" action="delete" >
    <s:param name="registration.id" value="%{registration.id}"/>
</s:url>
<s:url var="downloadLink" action="download" namespace="/investigator/profile/files/ajax" />
<s:url var="documentIconUrl" value='/images/ico_document.png' />

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
            <sj:submit targets="FDF" value="Upload" id="uploadSupportingDocument" onclick="indicateLoading();" />
        </div>
    </s:form>
    <div class="clear blank_space"></div>
    <table id="supportingDocumentsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
           summary="This table shows the supporting documents that have been uploaded as part of the financial disclosure.
                    It displays the document name, the upload date, a link to download the file, and a link to delete the documents.">
        <thead>
            <tr>
                <th scope="col" width="150px"><div><fmt:message key="label.name"/></div></th>
                <th scope="col" width="150px"><div><fmt:message key="label.upload.date"/></div></th>
                <th scope="col" width="30px"><div><fmt:message key="label.download"/></div></th>
                <th scope="col" width="10px">&nbsp;<%--Delete column--%></th>
            </tr>
        </thead>
    </table>
    </div>
</div>

<script type='text/javascript'>
    $(document).ready(function () {
        buildSupportingDocumentsTable();
    });

    function buildSupportingDocumentsTable() {
        var filesAsJson = ${filesAsJson};
        var isLongData = filesAsJson.length > ${minPaginationResults};
        $('#supportingDocumentsTable').dataTable({
            "aaData": filesAsJson,
            "bInfo": isLongData,
            "bLengthChange": isLongData,
            "bPaginate": isLongData,
            "bFilter": isLongData,
            "aoColumns": [
                {mDataProp: "name"},
                {mDataProp: "uploadDate", stype: "date",
                    fnRender: function (obj) {
                        return formatDate(obj.aData.uploadDate);
                    }},
                {mDataProp: null, fnRender: function (obj) {
                    return createDownloadColumn(obj.aData);
                }},
                {mDataProp: null, fnRender: function (obj) {
                    return createDeleteColumn(obj.aData);
                }}
            ],
            fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", aData.id);
                return nRow;
            },
            fnInitComplete: function () {
                indicateLoading(false);
            }
        });
    }

    function createDownloadColumn(file) {
        var cellvalue = file.id;
        var options = { id: file.id,
            url: '${downloadLink}',
            action: 'download',
            imageUrl: '${documentIconUrl}',
            imageTitlePrefix: 'Download File',
            paramName: 'file.id',
            paramValue: 'id'};
        return linkedImageFormatter(cellvalue, options, file);
    }

    function createDeleteColumn(file) {
        var cellvalue = file.id;
        var options = { id: file.id,
            url: '${deleteUrl}',
            imageUrl: '${deleteIconUrl}',
            imageTitle: 'Delete',
            paramName: 'fileId',
            paramValue: 'id',
            action: 'delete',
            target: 'FDF'};
        return ajaxImageFormatter(cellvalue, options, file);
    }
</script>