<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<s:url var="downloadLink" action="downloadFile" namespace="/util/ajax" />
<s:url var="downloadIconUrl" value='/images/ico_document.png' />

<firebird:additionalDocumentationDialog titleKey="clinical.laboratory.certificates.title">
    <table id="labCertificatesGrid" class="ui-jqgrid-htable ui-jqgrid-btable registrationFormsTable"
           summary="Displays all of the clinical laboratory certificates that have been added to the laboratory.
                    It displays the organization, the certificate type, the effective date, the expiration date,
                    and a link to download the certificate.">
        <thead>
        <tr>
            <th scope="col" width="250px">
                <div>
                    <fmt:message key="clinical.laboratory.certificates.organization.column.header"/>
                </div>
            </th>
            <th scope="col" width="150px">
                <div>
                    <fmt:message key="clinical.laboratory.certificates.certificate.column.header"/>
                </div>
            </th>
            <th scope="col" width="150px">
                <div>
                    <fmt:message key="clinical.laboratory.certificates.effective.date.column.header"/>
                </div>
            </th>
            <th scope="col" width="75px">
                <div>
                    <fmt:message key="clinical.laboratory.certificates.expiration.date.column.header"/>
                </div>
            </th>
            <th scope="col" width="75px">
                <div>
                    <fmt:message key="clinical.laboratory.certificates.download.column.header"/>
                </div>
            </th>
        </tr>
        </thead>
    </table>
</firebird:additionalDocumentationDialog>
<script>
    function buildDownloadLink(certificate) {
        if (certificate.fileAttached) {
            var cellvalue = certificate.id;
            var options = { id :  certificate.id,
                    action : 'downloadFile',
                    url:'${downloadLink}',
                    imageUrl : '${downloadIconUrl}',
                    imageTitle : 'Download Document',
                    paramName:'file.id',
                    paramValue: 'certificateFile.id'};
            return linkedImageFormatter(cellvalue, options, certificate);
        }
        return "";
    }

    $(document).ready(function() {
        var dataRows = ${additionalDocumentsJson};
        $('#labCertificatesGrid').dataTable( {
            aaData : dataRows,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aoColumns: [
                {mDataProp: "clinicalLaboratory.organization.name"},
                {mDataProp: "type"},
                {mDataProp: "effectiveDate", fnRender : function(obj) {
                    return formatDate(obj.aData.effectiveDate, true);
                }},
                {mDataProp: "expirationDate", fnRender : function(obj) {
                    return formatDate(obj.aData.expirationDate, true);
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
