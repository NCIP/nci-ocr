<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/profile/credentials/ajax" action="manageCertificateCredentials" var="manageCertificateUrl">
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="downloadLink" action="downloadFile" namespace="/util/ajax" />

<sj:a openDialog="profileDialog" href="%{manageCertificateUrl}" cssClass="addCredential" id="addCertificateButton"><fmt:message key="button.add.certificate" /></sj:a>

<table id="certificateCredentialsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table displays the list of training certificates which have been added to the profile. It displays
                the certificate type, the uploaded file name, the organization which bestowed it, the effective date, 
                the expiration date (if necessary), a link to edit the details, and a link to remove it.">
    <thead>
        <tr>
            <th scope="col" width="155px"><div><fmt:message key="label.certificate.type"/></div></th>
            <th scope="col" width="200px"><div><fmt:message key="label.file.name"/></div></th>
            <th scope="col" width="200px"><div><fmt:message key="label.organization.name"/></div></th>
            <th scope="col" width="100px"><div><fmt:message key="textfield.effectiveDate"/></div></th>
            <th scope="col" width="100px"><div><fmt:message key="textfield.expirationDate"/></div></th>
            <th scope="col" width="33px"><%-- Edit Credential Link --%></th>
            <th scope="col" width="33px"><%-- Remove Credenital Link --%></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${credentialJsonString};
        var isLongData = dataRows.length > ${minPaginationResults};
        var baseUrl = '<s:property value="manageCertificateUrl"/>';
        var editPage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_FIELDS_PAGE}"/>';
        var deletePage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_DELETE_CONFIRM_PAGE}"/>';
        $('#certificateCredentialsTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange": isLongData,
            "bPaginate": isLongData,
            "bFilter":isLongData,
            "aoColumns":[
                {mDataProp:"certificateType.nameProperty"},
                {mDataProp:null, fnRender:function (obj) {
                    return linkFormatter(obj.aData.file.name, {
                        paramName:'file.id',
                        paramValue:'file.id',
                        action:'viewFile',
                        url:'<s:property value="downloadLink"/>'
                    }, obj.aData)
                }},
                {mDataProp:null, fnRender:function (obj) {
                    return getIssuerName(obj.aData.issuer);
                }},
                {mDataProp:"effectiveDate", "stype":"date",
                    fnRender:function (obj) {
                        return formatDate(obj.aData.effectiveDate);
                    }},
                {mDataProp:"expirationDate", "stype":"date",
                    fnRender:function (obj) {
                        return formatDate(obj.aData.expirationDate);
                    }},
                {mDataProp:null, fnRender:function (obj) {
                    return credentialEditLink(obj.aData, baseUrl,
                            editPage);
                }},
                {mDataProp:null, fnRender:function (obj) {
                    return credentialDeleteLink(obj.aData, baseUrl,
                            deletePage);
                }}
            ],
            "fnRowCallback":function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", aData.id);
                return nRow;
            }
        });
    });
    
    function getIssuerName(issuer) {
    	if (issuer === null) {
    	    return "";
    	} else {
    	    return issuer.name;
    	}
    }
    
</script>