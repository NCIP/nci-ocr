<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<s:url action="manageWorkHistoryCredentials" var="manageWorkHistoryUrl">
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>

<sj:a openDialog="profileDialog" href="%{manageWorkHistoryUrl}" cssClass="addCredential" id="createNewPosition"><fmt:message key="button.add.position"/></sj:a>

<table id="workHistoryTable" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table contains all of the work history that has been entered for the profile. It displays the position,
                where it was achieved, when it started, when it ended (if necessary), a link to edit the details, and a link to remove it.">
    <thead>
        <tr>
            <th scope="col" width="246px"><div><fmt:message key="label.position" /></div></th>
            <th scope="col" width="246px"><div><fmt:message key="label.where" /></div></th>
            <th scope="col" width="246px"><div><fmt:message key="label.start.date" /></div></th>
            <th scope="col" width="246px"><div><fmt:message key="label.end.date" /></div></th>
            <th scope="col" width="33px"><%-- Edit Credential Link Column --%></th>
            <th scope="col" width="33px"><%-- Delete Credential Link Column --%></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${credentialJsonString};
        var isLongData = dataRows.length > ${minPaginationResults};
        var baseUrl = '<s:property value="manageWorkHistoryUrl"/>';
        var editPage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_FIELDS_PAGE}"/>';
        var deletePage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_DELETE_CONFIRM_PAGE}"/>';
        $('#workHistoryTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange": isLongData,
            "bPaginate": isLongData,
            "bFilter": isLongData,
            "aoColumns": [{mDataProp: "position"},
                          {mDataProp: "issuer", fnRender : function(obj) {
                              return organizationFormatter(obj.aData.issuer);
                          }},
                          {mDataProp: "effectiveDate", "stype" : "date",
                              fnRender : function(obj) {
                              return formatDate(obj.aData.effectiveDate);
                          }},
                          {mDataProp: "expirationDate", "stype" : "date",
                              fnRender : function(obj) {
                              return formatDate(obj.aData.expirationDate);
                          }},
                          {mDataProp : null, fnRender : function(obj) {
                              return credentialEditLink(obj.aData, baseUrl,
                                      editPage);
                          }},
                          {mDataProp : null, fnRender : function(obj) {
                              return credentialDeleteLink(obj.aData, baseUrl,
                                      deletePage);
                          }}
                      ],
           "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
               $(nRow).attr("id", aData.id);
               return nRow;
           }
        } );
    });
</script>