<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<s:url action="exportPersons" namespace="/sponsor/representative/export/ajax" var="exportPersonsUrl" />
<div>
    <s:a id="exportPersonsButton" href="%{#exportPersonsUrl}" cssClass="button"><fmt:message key="button.export.csv"/></s:a>
</div>
<br/>
<br/>
<table id="personsToBeCuratedTable" class="ui-jqgrid-htable ui-jqgrid-btable"
       summary="This table presents a list of People who have been added via the OCR system but have not yet been curated.
                This table displays the NES ID and the name of the Person.">
    <thead>
        <tr>
            <th scope="col" width="170px"><div><fmt:message key="label.nes.id"/></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.name"/></div></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${personsToBeCuratedJson};
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#personsToBeCuratedTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange" : isLongData,
            "bPaginate" : isLongData,
            "bFilter" : false,
            "aoColumns" : [
                           {mDataProp: "nesId"},
                           {mDataProp: "sortableName", bUseRendered: false, fnRender: function (obj) {
                               return obj.aData.displayNameForList;
                           }}
                           ],
            "fnRowCallback" : function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                $(nRow).attr("id", aData.id);
                return nRow;
                }
        } );
    });
</script>