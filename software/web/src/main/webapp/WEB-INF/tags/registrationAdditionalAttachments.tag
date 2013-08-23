<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<s:url var="downloadLink" action="downloadFile" namespace="/util/ajax" />
<s:url var="downloadIconUrl" value='/images/ico_document.png' />

<firebird:additionalDocumentationDialog titleKey="registration.attachments.title">
        <table id="additionalAttachmentsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                summary="This table displays the list of additional documents that were attached by the investigator.
                        It displays the file name, the description, the upload date, and a link to download the file.">
            <thead>
                <tr>
                    <th scope="col" width="275px"><div><fmt:message key="label.file.name"/></div></th>
                    <th scope="col" width="125px"><div><fmt:message key="label.description"/></div></th>
                    <th scope="col" width="125px"><div><fmt:message key="label.upload.date"/></div></th>
                    <th scope="col" width="35px"><div><fmt:message key="label.download"/></div></th>
                </tr>
            </thead>
        </table>
</firebird:additionalDocumentationDialog>
<script>
    function buildDownloadLink(file) {
        var cellvalue = file.id;
        var options = { id: file.id,
            action: 'downloadFile',
            url: '${downloadLink}',
            imageUrl: '${downloadIconUrl}',
            imageTitle: 'Download Attachment',
            paramName: 'file.id',
            paramValue: 'id'};
        return linkedImageFormatter(cellvalue, options, file);
    }

    $(document).ready(function () {
        var dataRows = ${additionalDocumentsJson};
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#additionalAttachmentsTable').dataTable({
            aaData: dataRows,
            aaSorting: [
                [1, 'desc']
            ],
            bInfo: isLongData,
            bLengthChange: isLongData,
            bPaginate: isLongData,
            bFilter: isLongData,
            aoColumns: [
                {mDataProp: "name"},
                {mDataProp: "description"},
                {mDataProp: "uploadDate", stype: "date", fnRender: function (obj) {
                    return formatDate(obj.aData.uploadDate, true);
                }},
                {mDataProp: null, fnRender: function (obj) {
                    return buildDownloadLink(obj.aData);
                }}
            ],
            fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", aData.id);
                return nRow;
            }
        });
    });
</script>
