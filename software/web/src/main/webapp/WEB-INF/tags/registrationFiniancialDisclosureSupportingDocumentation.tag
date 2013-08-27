<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<s:url var="downloadLink" action="downloadFile" namespace="/util/ajax" />
<s:url var="downloadIconUrl" value='/images/ico_document.png' />

<!--Content-->
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader>
                <fmt:message key="registration.financial.disclosure.supporting.documents.title"/>
            </firebird:dialogHeader>
            <firebird:messages/>
            <table id="supportingDocumentationGrid"
                   class="ui-jqgrid-htable ui-jqgrid-btable registrationFormsTable"
                   summary="This table shows the supporting documents that have been uploaded as part of the financial disclosure.
                        It displays the document name, the upload date, and a link to download the file.">
                <thead>
                <tr>
                    <th scope="col" width="250px">
                        <div>
                            <fmt:message
                                    key="registration.financial.disclosure.supporting.documents.name.column.header"/>
                        </div>
                    </th>
                    <th scope="col" width="150px">
                        <div>
                            <fmt:message
                                    key="registration.financial.disclosure.supporting.documents.upload.date.column.header"/>
                        </div>
                    </th>
                    <th scope="col" width="150px">
                        <div>
                            <fmt:message
                                    key="registration.financial.disclosure.supporting.documents.download.column.header"/>
                        </div>
                    </th>
                </tr>
                </thead>
            </table>
            <br/>

            <div class="formcol_xthin">
                <br/>
                <s:a id="closeBtn" href="#" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.close"/></s:a>
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>
<script>
    function buildDownloadLink(document) {
            var cellvalue = document.id;
            var options = { id :  document.id,
                    action : 'downloadFile',
                    url:'${downloadLink}',
                    imageUrl : '${downloadIconUrl}',
                    imageTitle : 'Download Document',
                    paramName:'file.id',
                    paramValue: 'id'};
            return linkedImageFormatter(cellvalue, options, document);
    }

    $(document).ready(function() {
        var dataRows = ${additionalDocumentsJson};
        $('#supportingDocumentationGrid').dataTable( {
            aaData : dataRows,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aoColumns: [
                {mDataProp: "name"},
                {mDataProp: "uploadDate", fnRender : function(obj) {
                    return formatDate(obj.aData.uploadDate, true);
                }},
                {mDataProp : null, fnRender : function(obj) {
                    return buildDownloadLink(obj.aData);
                }}
            ],
            fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                $(nRow).attr("id", aData.id);
                return nRow;
            }
        });
    });
</script>
