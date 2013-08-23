<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/profile/credentials/ajax" action="manageSpecialtyCredentials" var="manageSpecialtyUrl" >
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>

<sj:a openDialog="profileDialog" href="%{manageSpecialtyUrl}" cssClass="addCredential" id="createNewSpecialty"><fmt:message key="button.add.specialty" /></sj:a>

<table id="specialtyCredentialsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
       summary="This table contains all of the board certified specialites that have been entered for the profile. It displays the 
                certifying board, the specialty name, the current board status, the effective date, when it has expired 
                (if necessary), a link to edit the details, and a link to remove it.">
    <thead>
        <tr>
            <th scope="col" width="225px"><div><fmt:message key="label.certification.board"/></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.specialty"/></div></th>
            <th scope="col" width="125px"><div><fmt:message key="label.board.status"/></div></th>
            <th scope="col" width="100px"><div><fmt:message key="label.effective.date"/></div></th>
            <th scope="col" width="110px"><div><fmt:message key="label.expiration.date"/></div></th>
            <th scope="col" width="33px"><%-- Edit Credential Link Column --%></th>
            <th scope="col" width="33px"><%-- Delete Credential Link Column --%></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function () {
        var dataRows = ${credentialJsonString};
        var isLongData = dataRows.length > ${minPaginationResults};
        var baseUrl = '<s:property value="manageSpecialtyUrl"/>';
        var editPage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_FIELDS_PAGE}"/>';
        var deletePage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_DELETE_CONFIRM_PAGE}"/>';
        $('#specialtyCredentialsTable').dataTable({
            "aaData": dataRows,
            "bInfo": isLongData,
            "bLengthChange": isLongData,
            "bPaginate": isLongData,
            "bFilter": isLongData,
            "aoColumns": [
                {mDataProp: "specialtyType.board.name"},
                {mDataProp: "specialtyType.display"},
                {mDataProp: "status.display"},
                {mDataProp: "effectiveDate", "stype": "date",
                    fnRender: function (obj) {
                        return formatDate(obj.aData.effectiveDate);
                    }},
                {mDataProp: "expirationDate", "stype": "date",
                    fnRender: function (obj) {
                        return formatDate(obj.aData.expirationDate);
                    }},
                {mDataProp: null, fnRender: function (obj) {
                    return credentialEditLink(obj.aData, baseUrl,
                            editPage);
                }},
                {mDataProp: null, fnRender: function (obj) {
                    return credentialDeleteLink(obj.aData, baseUrl,
                            deletePage);
                }}
            ],
            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", aData.id);
                return nRow;
            }
        });
    });
</script>