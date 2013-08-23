<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/profile/credentials/ajax" action="manageCertificationCredentials" var="manageCertificationUrl" >
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>

<sj:a openDialog="profileDialog" href="%{manageCertificationUrl}" cssClass="addCredential" id="createNewCertification"><fmt:message key="button.add.certification" /></sj:a>

<table id="certificationCredentialsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
       summary="This table displays the list of Certifications which have been added to the profile. It displays 
                the name of the certification, the effective date, when the certification expires (if necessary), a link to edit 
                the details, and a link to remove it.">
    <thead>
        <tr>
            <th scope="col" width="300px"><div><fmt:message key="label.certification"/></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.effective.date"/></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.expiration.date"/></div></th>
            <th scope="col" width="33px"><%-- Edit Credential Link Column --%></th>
            <th scope="col" width="33px"><%-- Delete Credential Link Column --%></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${credentialJsonString};
        var isLongData = dataRows.length > ${minPaginationResults};
        var baseUrl = '<s:property value="manageCertificationUrl"/>'
            var editPage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_FIELDS_PAGE}"/>';
            var deletePage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_DELETE_CONFIRM_PAGE}"/>';
            $('#certificationCredentialsTable').dataTable( {
                "aaData" : dataRows,
                "bInfo" : isLongData,
                "bLengthChange": isLongData,
                "bPaginate": isLongData,
                "bFilter": isLongData,
                "aoColumns": [{mDataProp: "certificationType.name"},
                              {mDataProp: "effectiveDate", "stype" : "date",
                               fnRender : function(obj) {
                                 return formatDate(obj.aData.effectiveDate);
                               }},
                               {mDataProp: "expirationDate", "stype" : "date",
                                fnRender : function(obj) {
                                       return formatDate(obj.aData.expirationDate);
                               }},
                               {mDataProp : null, fnRender : function(obj) {
                                       return credentialEditLink(obj.aData, baseUrl, editPage);
                               }},
                               {mDataProp : null, fnRender : function(obj) {
                                       return credentialDeleteLink(obj.aData, baseUrl, deletePage);
                               }}],
                 "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                     $(nRow).attr("id", aData.id);
                     return nRow;
                 }
            } );
    });
</script>