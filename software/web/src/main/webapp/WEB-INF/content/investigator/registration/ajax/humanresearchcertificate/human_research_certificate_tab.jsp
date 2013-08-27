<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div id="registrationCertificates">
    <h2 class="clear"><fmt:message key="registration.human.research.certificate.title" /></h2>
    <firebird:messages/>

    <s:url namespace="/investigator/registration/ajax" action="manageCertificateCredentials" var="manageCertificateUrl" escapeAmp="false" >
        <s:param name="certificate.certificateType" value="@gov.nih.nci.firebird.data.CertificateType@HUMAN_RESEARCH_CERTIFICATE"/>
        <s:param name="profile.id" value="profile.id"/>
    </s:url>
    <s:url namespace="/investigator/registration/ajax/humanresearchcertificate" action="selectCertificate" var="selectCertificateUrl" escapeAmp="false" >
        <s:param name="profile.id" value="%{profile.id}"/>
        <s:param name="registration.id" value="%{registration.id}"/>
    </s:url>
    <s:url namespace="/investigator/registration/ajax/humanresearchcertificate" action="deselectCertificate" var="deselectCertificateUrl" escapeAmp="false" >
        <s:param name="profile.id" value="%{profile.id}"/>
        <s:param name="registration.id" value="%{registration.id}"/>
    </s:url>
    <s:url var="downloadLink" action="download" namespace="/investigator/profile/files/ajax" />

    <s:if test="%{registration.humanResearchCertificateForm.comments != null && !registration.humanResearchCertificateForm.comments.empty}">
        <firebird:comments retainState="true">
            ${registration.humanResearchCertificateForm.comments}
        </firebird:comments>
    </s:if>
    <div class="line"></div>
    <s:if test="%{!registration.lockedForInvestigator}">
       <firebird:instructionBubble messageKey="registration.human.research.certificate.instructions" />
        <span id="noCertificatesAsterisk" class="validationErrorAsterisk" style="display:none; float:left">*</span>
        <sj:a openDialog="registrationDialog" href="%{manageCertificateUrl}" cssClass="button" id="addCertificateButton">
            <fmt:message key="button.add.training.certificate"/>
        </sj:a>
    </s:if>

    <table id="certificateCredentialsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
           summary="This table displays the list of Protection of Human Research Participants certificates which have
                    been added to the profile. It displays a checkbox to include the certificate in your registration,
                    the uploaded file name, the organization which bestowed it, the effective date, the expiration date
                    (if necessary), a link to edit the details, and a link to remove it.">
        <thead>
            <tr>
                <th scope="col" width="33px"><%--Checkbox for selecting certificate for inclusion--%></th>
                <th scope="col" width="275px"><div><fmt:message key="label.file.name"/></div></th>
                <th scope="col" width="275px"><div><fmt:message key="label.organization.name"/></div></th>
                <th scope="col" width="125px"><div><fmt:message key="textfield.effectiveDate"/></div></th>
                <th scope="col" width="125px"><div><fmt:message key="textfield.expirationDate"/></div></th>
                <th scope="col" width="33px"><%-- Edit Certificate link --%></th>
                <th scope="col" width="33px"><%-- Remove Certificate link --%></th>
            </tr>
        </thead>
    </table>

    <s:set var="selectedCertificateIds" value="%{form.certificates.{#this.id}}"/>

    <script type="text/javascript">
    var _hrcPage = (function(){
        var page = {}
        page.isLocked = <s:property value="registration.lockedForInvestigator"/>;
        page.selectedCertificateIds = ${selectedCertificateIds} ? ${selectedCertificateIds} : [];
        page.createCheckColumn = function(certificate) {
            var checkboxHtml = "";
            if(!this.isLocked) {
                checkboxHtml = "<input type='checkbox' "
                var checkboxId = "certificate_"+ certificate.id;
                checkboxHtml += "id='"+ checkboxId + "' ";
                checkboxHtml += "value='" + certificate.id + "' ";
                if (_.indexOf(this.selectedCertificateIds, certificate.id) >= 0) {
                    checkboxHtml += "checked ";
                }
                checkboxHtml += "onclick='_hrcPage.submitAndSwitch(this, " + certificate.id + ")'";
                checkboxHtml += "/>";
                return createHiddenCheckboxLabel(checkboxId) + checkboxHtml;
            }
            return checkboxHtml;
        }

        page.switchToLoading = function(item) {
            _fbUtil.switchObjects(item, _fbUtil.loadingIcon);
        }

        page.switchFromLoading = function(item) {
            _fbUtil.switchObjects(_fbUtil.loadingIcon, item);
        }

    page.submitAndSwitch = function(checkbox, certificateId) {
            var url = _hrcPage.getSubmitUrl(checkbox);
            var data = { 'selectedCertificate.id' : certificateId };
            _hrcPage.switchToLoading(checkbox);
            $.post(url, data, function(errorMessages) {
                       setPageErrorMessages(errorMessages);
                       _hrcPage.switchFromLoading(checkbox);
                   }).error(function() {
                       $(checkbox).prop("checked", false);
                   });
        }

        page.getSubmitUrl = function(checkbox) {
              if ($(checkbox).attr("checked")) {
                  return '${selectCertificateUrl}';
              } else {
                  return '${deselectCertificateUrl}';
              }
          }

        return page;
    })();

        $(document).ready(function() {
            var dataRows = ${certificatesAsJson};
            var isLongData = dataRows.length > ${minPaginationResults};
            var baseUrl = '<s:property value="manageCertificateUrl"/>'
            var editPage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_FIELDS_PAGE}"/>';
            var deletePage = '<s:property value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_DELETE_CONFIRM_PAGE}"/>';
            $('#certificateCredentialsTable').dataTable( {
                aaData : dataRows,
                aaSorting : [[2,'desc']],
                bInfo : isLongData,
                bLengthChange : isLongData,
                bPaginate : isLongData,
                bFilter : isLongData,
                aoColumns : [
                              {mDataProp: null, fnRender : function(obj) {
                                 return _hrcPage.createCheckColumn(obj.aData);
                              }},
                            {mDataProp: null, fnRender : function(obj) {
                                  return linkFormatter(obj.aData.file.name,{
                                      paramName:'file.id',
                                      paramValue: 'file.id',
                                      action : 'viewFile',
                                      url : '<s:property value="downloadLink"/>'
                                  }, obj.aData)
                              }},
                            {mDataProp:"issuerName"},
                            {mDataProp: "effectiveDate", stype : "date",
                                  fnRender : function(obj) {
                                  return formatDate(obj.aData.effectiveDate);
                              }},
                              {mDataProp: "expirationDate", stype : "date",
                                  fnRender : function(obj) {
                                  return formatDate(obj.aData.expirationDate);
                              }},
                              {mDataProp : null, fnRender : function(obj) {
                                  return (_hrcPage.isLocked) ? "" :
                                      credentialEditLink(obj.aData, baseUrl, editPage, 'registrationDialog');
                                  }},
                              {mDataProp : null, fnRender : function(obj) {
                                  return (_hrcPage.isLocked) ? "" :
                                      credentialDeleteLink(obj.aData, baseUrl, deletePage, 'registrationDialog');
                              }}
                          ],
               fnRowCallback : function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                   $(nRow).attr("id", aData.id);
                   return nRow;
               }
            });
            checkForNoCertificatesIfNecessary();
        });

        function checkForNoCertificatesIfNecessary() {
          if ("%{registration.status == @gov.nih.nci.firebird.data.RegistrationStatus@INCOMPLETE}") {
            $("#noCertificatesAsterisk").toggle(!isCertificateUploaded());
          }
        }

        function isCertificateUploaded() {
          return $("#certificateCredentialsTable").dataTable().fnGetData().length > 0;
        }
    </script>

</div>
