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
            <s:iterator value="curationDataset.displayHeaderKeys" var="headerKey">
                <th scope="col"><div><fmt:message key="${headerKey}"/></div></th>
            </s:iterator>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${curationDataRowsJson};
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#organizationRolesToBeCuratedTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange" : isLongData,
            "bPaginate" : isLongData,
            "bFilter" : false
        } );
    });
</script>