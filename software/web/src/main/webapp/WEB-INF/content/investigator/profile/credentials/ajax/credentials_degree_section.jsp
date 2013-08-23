<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/profile/credentials/ajax" action="manageDegreeCredentials" var="manageDegreeUrl">
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>

<sj:a openDialog="profileDialog" href="%{manageDegreeUrl}" cssClass="addCredential" id="createNewDegree"><fmt:message key="button.add.degree" /></sj:a>

<table id="degreeCredentialsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table displays the list of Degrees which have been added to the profile. It displays the name of the
                degree, the institution which bestowed the degree, the effective date, a link to edit the details, and 
                a link to remove it.">
    <thead>
        <tr>
            <th scope="col" width="246px"><div><fmt:message key="label.degree"/></div></th>
            <th scope="col" width="246px"><div><fmt:message key="label.institution"/></div></th>
            <th scope="col" width="246px"><div><fmt:message key="textfield.effectiveDate.degree"/></div></th>
            <th scope="col" width="33px"><%-- Edit Credential Link Column --%></th>
            <th scope="col" width="33px"><%-- Delete Credential Link Column --%></th>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${credentialJsonString};
        var isLongData = dataRows.length > ${minPaginationResults};
        var baseUrl = '<s:property value="manageDegreeUrl"/>';
        var editPage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_FIELDS_PAGE}"/>';
        var deletePage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_DELETE_CONFIRM_PAGE}"/>';
        $('#degreeCredentialsTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange": isLongData,
            "bPaginate": isLongData,
            "bFilter": isLongData,
            "aoColumns": [{mDataProp: "degreeType.name"},
                          {mDataProp: "issuer", fnRender : function(obj) {
                              return organizationFormatter(obj.aData.issuer);
                          }},
                          {mDataProp: "effectiveDate", "stype" : "date",
                              fnRender : function(obj) {
                              return formatDate(obj.aData.effectiveDate);
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
