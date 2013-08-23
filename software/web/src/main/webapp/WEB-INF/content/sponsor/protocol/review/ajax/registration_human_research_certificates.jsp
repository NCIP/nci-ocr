<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="downloadLink" action="downloadFile" namespace="/util/ajax" />
<s:url var="downloadIconUrl" value='/images/ico_document.png' />

<firebird:additionalDocumentationDialog titleKey="registration.human.research.certificate.title">
    <table id="humanResearchCertificatesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
           summary="This table displays the Protection of Human Research Certificates included as part of this
                    registration. It displays the Name of uploaded file, the effective date of the certificate, the
                    expiration date of the certificate, and a link to download the certificate file.">
        <thead>
            <tr>
                <th scope="col" width="275px"><div><fmt:message key="label.file.name"/></div></th>
                <th scope="col" width="125px"><div><fmt:message key="textfield.effectiveDate"/></div></th>
                <th scope="col" width="125px"><div><fmt:message key="textfield.expirationDate"/></div></th>
                <th scope="col" width="35px"><%--Download Link--%></th>
            </tr>
        </thead>
    </table>
</firebird:additionalDocumentationDialog>
<script>
    function buildDownloadLink(certificate) {
            var cellvalue = certificate.file.id;
            var options = { id :  document.id,
                    action : 'downloadFile',
                    url:'${downloadLink}',
                    imageUrl : '${downloadIconUrl}',
                    imageTitle : 'Download Certificate',
                    paramName:'file.id',
                    paramValue: 'file.id'};
            return linkedImageFormatter(cellvalue, options, certificate);
    }

    $(document).ready(function() {
        var dataRows = ${additionalDocumentsJson};
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#humanResearchCertificatesTable').dataTable({
            aaData : dataRows,
            aaSorting : [[1,'desc']],
            bInfo : isLongData,
            bLengthChange : isLongData,
            bPaginate : isLongData,
            bFilter : isLongData,
            aoColumns : [
                          {mDataProp: "file.name"},
                          {mDataProp: "effectiveDate", stype : "date", fnRender : function(obj) {
                              return formatDate(obj.aData.effectiveDate);
                          }},
                          {mDataProp: "expirationDate", stype : "date", fnRender : function(obj) {
                              return formatDate(obj.aData.expirationDate);
                          }},
                          {mDataProp: null, fnRender : function(obj) {
                              return buildDownloadLink(obj.aData);
                          }}
                      ],
           fnRowCallback : function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
               $(nRow).attr("id", aData.id);
               return nRow;
           }
        });
    });
</script>
