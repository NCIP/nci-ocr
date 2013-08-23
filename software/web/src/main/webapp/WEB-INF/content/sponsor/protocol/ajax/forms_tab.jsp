<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h2>
    <fmt:message key="sponsor.protocol.forms.title"/>

    <s:if test="%{sponsorRepresentative}">
        <s:url namespace="/sponsor/representative/protocol/ajax" action="editForms" var="editFormsUrl">
            <s:param name="protocol.id">${protocol.id}</s:param>
        </s:url>
        <sj:a openDialog="popUpDialog" href="%{editFormsUrl}" cssClass="edit" id="editForms"> <fmt:message key="button.edit" /> </sj:a>
    </s:if>
</h2>

    <firebird:messages/>

    <div class="clear"><br></div>

    <s:url var="downloadLink" action="download" namespace="/sponsor/protocol/ajax" >
        <s:param name="protocol.id" value="%{protocol.id}"/>
    </s:url>

    <s:url var="documentIconUrl" value='/images/ico_document.png' />

    <table id="protocolFormsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
            summary="This table displays the list of forms that have been selected to be part of this protocol 
                    registration. It shows the name, the optionality for investigators, the optionality for 
                    subinvestigators, and a link to download a sample form (where applicable).">
        <thead>
            <tr>
                <th scope="col" width="200px"><div><fmt:message key="label.name"/></div></th>
                <th scope="col" width="200px"><div><fmt:message key="label.investigators"/></div></th>
                <th scope="col" width="200px"><div><fmt:message key="label.subinvestigators"/></div></th>
                <th scope="col" width="75px"><div><fmt:message key="label.sample"/></div></th>
            </tr>
        </thead>
    </table>

<s:url namespace="/sponsor/protocol/ajax" action="forms" var="formsUrl">
    <s:param name="protocol.id">${protocol.id}</s:param>
</s:url>
<script type="text/javascript">
    $(document).ready(function () {
        var dataRows = ${jsonFormsTable};
        $('#protocolFormsTable').dataTable({
            aaData: dataRows,
            bInfo: false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aoColumns: [
                {mDataProp: "description"},
                {mDataProp: "invOptionality.display"},
                {mDataProp: "subinvOptionality.display"},
                {mDataProp: null, fnRender: function (obj) {
                    if (obj.aData.sampleFileId !== null) {
                        var cellvalue = obj.aData.id;
                        var options = {
                            id: obj.aData.id,
                            action: 'download',
                            url: '${downloadLink}',
                            imageUrl: '${documentIconUrl}',
                            imageTitle: 'Download File',
                            paramName: 'file.id',
                            paramValue: 'sampleFileId'
                        };

                        return linkedImageFormatter(cellvalue, options, obj.aData);
                    }
                    return "";
                }}
            ],
            fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", aData.id);
                return nRow;
            }
        });
    });

</script>
