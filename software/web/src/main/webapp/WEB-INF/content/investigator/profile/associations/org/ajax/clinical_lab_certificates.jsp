<%@ include file="/WEB-INF/content/common/taglibs.jsp" %>
<s:url var="updateCertificateUrl" namespace="/investigator/profile/associations/org/ajax" action="clinicalLabCertificateEnter">
    <s:param name="associationId" value="%{associationId}"/>
</s:url>
<s:url var="deleteCertificateUrl" namespace="/investigator/profile/associations/org/ajax" action="deleteCertificate">
    <s:param name="associationId" value="%{associationId}"/>
</s:url>
<s:url var="downloadLink" action="download" namespace="/investigator/profile/files/ajax" />
<s:url var="documentIconUrl" value='/images/ico_document.png' />
<s:url var="editIconUrl" value='/images/ico_edit.gif' />
<s:url var="deleteIconUrl" value='/images/ico_delete.gif' />

<!--Content-->
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
      <firebird:dialogHeader><fmt:message key="clinical.laboratory.certificates.title" /></firebird:dialogHeader>
            <firebird:messages/>
      <s:if test="%{!certificateTypes.isEmpty() || certificate != null}">
                <s:form action="saveCertificate" namespace="/investigator/profile/associations/org/ajax" id="manageCertificates" enctype="multipart/form-data" method="POST">
                  <div class="formcol">
                        <s:if test="certificate == null || certificate.id == null">
                   <s:select
                                id="certificate.type"
                                name="certificate.type"
                                requiredLabel="true"
                                list="certificateTypes"
                                headerKey=""
                                headerValue="-- Select a Certificate Type --"
                                label="%{getText('dropdown.certificateType')}"/>
                        </s:if>
                        <s:else>
                            <h3><s:property value="certificate.type.name()"/></h3>
                            <s:hidden name="certificate.type" />
                            <s:hidden name="certificate.id" />
                        </s:else>
            </div>
                    <div class="clear"></div>
                  <div class="formcol">
                        <firebird:dateSelect fieldName="effectiveDate" label="datePicker.effectiveDate"/>
            </div>
            <div class="formcol">
                        <firebird:dateSelect fieldName="expirationDate" label="datePicker.expirationDate" isFuture="true" required="true"/>
            </div>
            <div class="clear"></div>
            <div class="formcol_double">
                <s:file requiredLabel="true" name="certificateFile.data" id="certificateFile.data" size="30" label="%{getText('label.file')}"/>
            </div>
                    <s:hidden name="associationId" />
                </s:form>
                <div class="clear"></div>
                <div class="formcol_xthin">
                    <br/>
                    <sj:a id="saveCertificate" value="Save" formIds="manageCertificates"
                        targets="profileDialog" cssClass="button" href="#"
                        onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.save"/></sj:a>
                </div>
                <s:if test="%{certificate.id != null}">
                    <div class="formcol_xthin">
                        <br/>
                        <sj:a id="cancelUpdate" value="Cancel"
                            targets="profileDialog" cssClass="button" href="%{updateCertificateUrl}"
                            onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.cancel"/></sj:a>
                    </div>
                </s:if>

            </s:if>

            <s:if test="%{!laboratory.organizationRole.certificates.isEmpty()
                && (!certificateTypes.isEmpty() || certificate != null)}">
                <div class="line"></div>
            </s:if>

            <s:if test="%{!laboratory.organizationRole.certificates.isEmpty()}">
                <table id="labCertificatesTable" class="ui-jqgrid-htable ui-jqgrid-btable registrationFormsTable"
                       summary="Displays all of the clinical laboratory certificates that have been added to the laboratory.
                                It displays the certificate type, the effective date, the expiration date,
                                a link to download the certificate, a link to edit the certificate, and a link to delete
                                the certificate.">
                    <thead>
                    <tr>
                        <th scope="col" width="150px">
                            <div>
                                <fmt:message key="clinical.laboratory.certificates.certificate.column.header"/>
                            </div>
                        </th>
                        <th scope="col" width="150px">
                            <div>
                                <fmt:message key="clinical.laboratory.certificates.effective.date.column.header"/>
                            </div>
                        </th>
                        <th scope="col" width="150px">
                            <div>
                                <fmt:message key="clinical.laboratory.certificates.expiration.date.column.header"/>
                            </div>
                        </th>
                        <th scope="col" width="75px">
                            <div>
                                <fmt:message key="clinical.laboratory.certificates.download.column.header"/>
                            </div>
                        </th>
                        <th scope="col" width="10px">
                            <div><%--Edit Certificate--%></div>
                        </th>
                        <th scope="col" width="10px">
                            <div><%--Remove Certificate--%></div>
                        </th>
                    </tr>
                    </thead>
                </table>

                <br/>
            </s:if>
            <div class="formcol_xthin">
                <br/>
                <s:a id="closeCertificates" href="#" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.close"/></s:a>
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        buildLabCertificatesTable();
        $.publish('injectCsrfTokens');
    });

    function buildLabCertificatesTable() {
        var dataRows = ${certificatesJson};
        $('#labCertificatesTable').dataTable( {
            aaData : dataRows,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aoColumns: [
                {mDataProp: "type"},
                {mDataProp: "effectiveDate", fnRender : function(obj) {
                    return formatDate(obj.aData.effectiveDate, false);
                }},
                {mDataProp: "expirationDate", fnRender : function(obj) {
                    return formatDate(obj.aData.expirationDate, false);
                }},
                {mDataProp : null, fnRender : function(obj) {
                    return createDownloadColumn(obj.aData);
                }},
                {mDataProp : null, fnRender : function(obj) {
                    return createEditColumn(obj.aData);
                }},
                {mDataProp : null, fnRender : function(obj) {
                    return createRemoveColumn(obj.aData);
                }}
            ],
            fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                $(nRow).attr("id", aData.id);
                return nRow;
            }
        });
    }

    function createDownloadColumn(certificate) {
        var cellvalue = certificate.id;
        var options = { id :  certificate.id,
            action : 'download',
            url:'${downloadLink}',
            imageUrl : '${documentIconUrl}',
            imageTitle : 'Download Certificate',
            paramName:'file.id',
            paramValue: 'id'};
        return linkedImageFormatter(cellvalue, options, certificate);
    }

    function createEditColumn(certificate) {
        var cellvalue = certificate.id;
        var options = { id :  certificate.id,
            url:'${updateCertificateUrl}',
            imageUrl : '${editIconUrl}',
            imageTitle : 'Update Certificate',
            paramName:'certificate.type',
            paramValue: 'type',
            action : 'update',
            target: 'profileDialog'};
        return ajaxImageFormatter(cellvalue, options, certificate);
    }

    function createRemoveColumn(certificate) {
        var cellvalue = certificate.id;
        var options = { id :  certificate.id,
            url:'${deleteCertificateUrl}',
            imageUrl : '${deleteIconUrl}',
            imageTitle : 'Delete Certificate',
            paramName:'certificate.type',
            paramValue: 'type',
            action: 'delete',
            target: 'profileDialog'};
        return ajaxImageFormatter(cellvalue, options, certificate);
    }

</script>
