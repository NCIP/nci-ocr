<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<div>
    <s:a id="exportOrganizationRolesButton" action="exportRoles" namespace="/sponsor/representative/export/ajax" cssClass="button"><fmt:message key="button.export.csv"/></s:a>
</div>
<br/>
<br/>
<table id="organizationRolesToBeCuratedTable" class="ui-jqgrid-htable ui-jqgrid-btable"
       summary="This table presents a list of Organization Roles which have been added via the OCR system but have not yet been curated.
                This table displays the NES ID of the role, the type of the role, the affected organization's NES ID, and the name of the organization">
    <thead>
        <tr>
            <th scope="col" width="170px"><div><fmt:message key="label.nes.id"/></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.structural.role.type"/></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.organization.player.nes.id"/></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.organization.name"/></div></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${rolesToBeCuratedJson};
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#organizationRolesToBeCuratedTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange" : isLongData,
            "bPaginate" : isLongData,
            "bFilter" : false,
            "aoColumns" : [
                           {mDataProp: "roleNesId"},
                           {mDataProp: "roleType"},
                           {mDataProp: "organizationNesId"},
                           {mDataProp: "name"}
                           ],
            "fnRowCallback" : function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                $(nRow).attr("id", aData.id);
                return nRow;
                }
        } );
    });
</script>