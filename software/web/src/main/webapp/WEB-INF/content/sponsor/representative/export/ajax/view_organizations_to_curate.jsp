<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<s:url action="exportOrganizations" namespace="/sponsor/representative/export/ajax" var="exportOrganizationsUrl" />
<div>
    <s:a id="exportOrganizationsButton" href="%{#exportOrganizationsUrl}" cssClass="button"><fmt:message key="button.export.csv"/></s:a>
</div>
<br/>
<br/>
<table id="organizationsToBeCuratedTable" class="ui-jqgrid-htable ui-jqgrid-btable"
       summary="This table presents a list of Organization which have been added via the OCR system but have not yet been curated.
                This table displays the NES ID and the name of the Organization.">
    <thead>
        <tr>
            <th scope="col" width="170px"><div><fmt:message key="label.nes.id"/></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.name"/></div></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${organizationsToBeCuratedJson};
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#organizationsToBeCuratedTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange" : isLongData,
            "bPaginate" : isLongData,
            "bFilter" : false,
            "aoColumns" : [
                           {mDataProp: "nesId"},
                           {mDataProp: "name"}
                           ],
            "fnRowCallback" : function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                $(nRow).attr("id", aData.id);
                return nRow;
                }
        } );
    });
</script>