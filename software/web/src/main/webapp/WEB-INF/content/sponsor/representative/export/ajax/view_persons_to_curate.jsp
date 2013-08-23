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
        $('#personsToBeCuratedTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange" : isLongData,
            "bPaginate" : isLongData,
            "bFilter" : false
        } );
    });
</script>