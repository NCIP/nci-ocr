<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/profile/credentials/ajax" action="manageLicenseCredentials" var="manageLicenseUrl" >
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>

<sj:a openDialog="profileDialog" href="%{manageLicenseUrl}"
    cssClass="addCredential" id="createNewLicense"><fmt:message key="button.add.license" /></sj:a>

<table id="licenseCredentialsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
       summary="This table contains all of the licenses that have been entered for the profile. It displays the 
                license name, the Identification number, where it was acquired, the expiration date, 
                a link to edit the details, and a link to remove it.">
    <thead>
        <tr>
            <th scope="col" width="225px"><div><fmt:message key="label.license.type"/></div></th>
            <th scope="col" width="150px"><div><fmt:message key="label.license.number"/></div></th>
            <th scope="col" width="150px"><div><fmt:message key="label.where"/></div></th>
            <th scope="col" width="150px"><div><fmt:message key="label.expiration.date"/></div></th>
            <th scope="col" width="33px"><%-- Edit Credential Link Column --%></th>
            <th scope="col" width="33px"><%-- Delete Credential Link Column --%></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${credentialJsonString};
        var isLongData = dataRows.length > ${minPaginationResults};
        var baseUrl = '<s:property value="manageLicenseUrl"/>';
        var editPage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_FIELDS_PAGE}"/>';
        var deletePage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_DELETE_CONFIRM_PAGE}"/>';
        $('#licenseCredentialsTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange": isLongData,
            "bPaginate": isLongData,
            "bFilter": isLongData,
            "aoColumns": [{mDataProp: "licenseType.name"},
                          {mDataProp: "licenseId"},
                          {mDataProp: "state", fnRender : function(obj) {
                              return locationFormatter(obj.aData);
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
    function locationFormatter(rowObject) {
        if(!isBlank(rowObject['state']) && !isBlank(rowObject['country'])) {
            return rowObject['state'] + ', ' + rowObject['country'];
        } else if (!isBlank(rowObject['state'])) {
            return rowObject['state'];
        } else {
            return rowObject['country'];
        }
    }
</script>