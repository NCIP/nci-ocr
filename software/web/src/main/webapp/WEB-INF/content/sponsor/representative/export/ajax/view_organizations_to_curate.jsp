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
        $('#organizationsToBeCuratedTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange" : isLongData,
            "bPaginate" : isLongData,
            "bFilter" : false
        } );
    });
</script>