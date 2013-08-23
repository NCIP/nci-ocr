<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/annual/registration/ajax/financialdisclosure" action="enterAddPharmaceuticalCompany"
    var="addPharmaceuticalCompanyUrl" escapeAmp="false">
    <s:param name="profile.id" value="profile.id" />
    <s:param name="registration.id" value="%{registration.id}" />
</s:url>
<s:url var="removePharmaceuticalCompanyUrl" action="enterRemovePharmaceuticalCompanyConfirm" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}" />
    <s:param name="registration.id" value="%{registration.id}" />
</s:url>
<firebird:messages />
<h2 class="clear">
    <fmt:message key="registration.supplemental.investigator.data.form.title" />
</h2>

<s:if test="readOnly">
    <div id="lockedViewIdfPdfButtonTopDiv">
        <firebird:viewGeneratedFormButton buttonId="viewIdfPdfButtonTop" form="${form}"/>
    </div>
</s:if>
<s:else>
    <div id="unlockedViewIdfPdfButtonTopDiv" class="formcol_row">
        <firebird:viewGeneratedFormButton buttonId="viewIdfPdfButtonTop" form="${form}"/>
        <div class="clear"></div>
        <s:if test="%{form.comments != null && !form.comments.empty}">
            <firebird:comments retainState="true">
                ${form.comments}
            </firebird:comments>
        </s:if>
    </div>

    <div class="line"></div>
    <div class="formcol_row">
        <fmt:message key="registration.supplemental.investigator.data.form.intro" />
    </div>
    <br/>
    <s:set var="fieldNum" value="1"/>

    <s:url namespace="/investigator/profile" action="home" var="profileUrl" escapeAmp="false">
        <s:param name="selectedTab" value="0"/>
        <s:param name="profile.id" value="profile.id"/>
    </s:url>
    <h2>
        <fmt:message key="label.professional.contact.information" />
        <s:a id="manageContactInfoButton" href="%{#profileUrl}" cssClass="edit"><fmt:message key="button.modify.contact.info" /></s:a>
    </h2>
    <firebird:sectionColumn>
        <firebird:sectionDisplay id="personNameSection" sectionTitleKey="label.investigator.name" sectionNum="${fieldNum}">
            <p>${registration.profile.person.displayName}</p>
        </firebird:sectionDisplay>
        <s:set var="fieldNum" value="#fieldNum + 1"/>
        <firebird:sectionDisplay id="primaryAddressSection" sectionTitleKey="label.primary.address"  sectionNum="${fieldNum}">
            <p><firebird:addressDisplay address="${registration.profile.person.postalAddress}"/></p>
        </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <firebird:sectionColumn location="right">
        <s:set var="fieldNum" value="#fieldNum + 1"/>
        <firebird:sectionDisplay id="emailSection" sectionTitleKey="label.email" sectionNum="${fieldNum}">
            <p>${registration.profile.person.email}</p>
        </firebird:sectionDisplay>
        <s:set var="fieldNum" value="#fieldNum + 1"/>
        <firebird:sectionDisplay id="phoneNumberSection" sectionTitleKey="label.phone" sectionNum="${fieldNum}">
            <p>${registration.profile.person.phoneNumber}</p>
        </firebird:sectionDisplay>
        <s:set var="fieldNum" value="#fieldNum + 1"/>
        <firebird:sectionDisplay id="providerNumberSection" sectionTitleKey="label.provider.number" sectionNum="${fieldNum}">
            <p>${registration.profile.person.providerNumber}</p>
        </firebird:sectionDisplay>
        <s:set var="fieldNum" value="#fieldNum + 1"/>
        <firebird:sectionDisplay id="nciInvestigatorNumberSection" sectionTitleKey="label.nci.investigator.number" sectionNum="${fieldNum}">
            <p>${registration.profile.person.ctepId}</p>
        </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <div class="line"></div>

    <h2>
        <fmt:message key="label.primary.organization" />
        <s:a id="manageContactInfoButton" href="%{#profileUrl}" cssClass="edit"><fmt:message key="button.edit" /></s:a>
    </h2>
  <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn>
        <firebird:sectionDisplay id="primaryOrgNameSection" sectionTitleKey="label.name" sectionNum="${fieldNum}">
            <p>${profile.primaryOrganization.organization.name}</p>
        </firebird:sectionDisplay>
        <s:set var="fieldNum" value="#fieldNum + 1"/>
        <firebird:sectionDisplay id="primaryOrgAddressSection" sectionTitleKey="label.address"  sectionNum="${fieldNum}">
            <p><firebird:addressDisplay address="${profile.primaryOrganization.organization.postalAddress}"/></p>
        </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <firebird:sectionColumn location="right">
        <s:set var="fieldNum" value="#fieldNum + 1"/>
        <firebird:sectionDisplay id="primaryOrgCtepIdSection" sectionTitleKey="label.ctep.id" sectionNum="${fieldNum}">
            <p>${profile.primaryOrganization.organization.ctepId}</p>
        </firebird:sectionDisplay>
        <firebird:sectionDisplay id="primaryOrgEmailSection" sectionTitleKey="label.email" sectionNum="${fieldNum}">
            <p>${profile.primaryOrganization.organization.email}</p>
        </firebird:sectionDisplay>
        <s:set var="fieldNum" value="#fieldNum + 1"/>
        <firebird:sectionDisplay id="primaryOrgPhoneNumberSection" sectionTitleKey="label.phone" sectionNum="${fieldNum}">
            <p>${profile.primaryOrganization.organization.phoneNumber}</p>
        </firebird:sectionDisplay>
        <s:set var="fieldNum" value="#fieldNum + 1"/>
    </firebird:sectionColumn>

    <div class="line"></div>

    <s:url namespace="/investigator/profile" action="home" var="credentialsUrl" escapeAmp="false" >
        <s:param name="selectedTab" value="1"/>
        <s:param name="profile.id" value="profile.id"/>
    </s:url>
    <h2>
        <fmt:message key="label.credentials" />
        <s:a id="manageCredentialsButton" href="%{#credentialsUrl}" cssClass="edit" ><fmt:message key="button.add.modify.credentials" /></s:a>
    </h2>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="workHistorySection" sectionTitleKey="label.work.history" sectionNum="${fieldNum}">
        <table id="workHistoryTable" class="ui-jqgrid-htable ui-jqgrid-btable"
               summary="This table contains all of the work history that has been entered for the profile. It displays the position,
                        where it was achieved, when it started, and when it ended (if necessary).">
            <thead>
            <tr>
                <th scope="col" class="nofill"><div><fmt:message key="label.position"/></div></th>
                <th scope="col" class="nofill"><div><fmt:message key="label.where"/></div></th>
                <th scope="col" class="nofill"><div><fmt:message key="label.start.date"/></div></th>
                <th scope="col" class="nofill"><div><fmt:message key="label.end.date"/></div></th>
            </tr>
            </thead>
        </table>
        </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="licensesSection" sectionTitleKey="label.professional.licenses" sectionNum="${fieldNum}">
        <table id="licensesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
               summary="This table contains all of the licenses that have been entered for the profile. It displays the
                license type, the Identification number, where it was acquired, and the expiration date.">
        <thead>
                <tr>
                    <th scope="col" class="nofill"><div><fmt:message key="label.license.type" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.license.number" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.license.location" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.license.expiration.date" /></div></th>
                </tr>
            </thead>
        </table>
      </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="degreesSection" sectionTitleKey="label.degrees" sectionNum="${fieldNum}">
      <table id="degreesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
             summary="This table displays the list of Degrees which have been added to the profile. It displays the name of the
                      degree, the institution which bestowed the degree, and the effective date.">
          <thead>
              <tr>
                  <th scope="col" class="nofill"><div><fmt:message key="label.degree" /></div></th>
                  <th scope="col" class="nofill"><div><fmt:message key="label.institution" /></div></th>
                  <th scope="col" class="nofill"><div><fmt:message key="label.date" /></div></th>
              </tr>
          </thead>
            </table>
        </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="internshipsSection" sectionTitleKey="label.internships" sectionNum="${fieldNum}">
        <table id="internshipsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
               summary="This table contains all of the internships that have been entered for the profile. It displays the
                certifying board, the specialty, where the internship took place, the start date, and when it ended (if necessary).">
            <thead>
                <tr>
                    <th scope="col" class="nofill"><div><fmt:message key="label.certifying.board" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.specialty" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.where" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.start.date" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.end.date" /></div></th>
                </tr>
            </thead>
        </table>
      </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="residenciesSection" sectionTitleKey="label.residencies" sectionNum="${fieldNum}">
        <table id="residenciesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
               summary="This table contains all of the residencies that have been entered for the profile. It displays the
                        certifying board, the specialty, where the residency took place, the start date, and when it
                        ended (if necessary).">
            <thead>
                <tr>
                    <th scope="col" class="nofill"><div><fmt:message key="label.specialty" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.where" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.start.date" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.end.date" /></div></th>
                </tr>
            </thead>
        </table>
        </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="fellowshipsSection" sectionTitleKey="label.fellowships" sectionNum="${fieldNum}">
        <table id="fellowshipsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
               summary="This table contains all of the fellowships that have been entered for the profile. It displays the
                        certifying board, the sub specialty name, where the fellowship took place, the start date, and
                        when it ended (if necessary).">
            <thead>
                <tr>
                    <th scope="col" class="nofill"><div><fmt:message key="label.sub.specialty" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.where" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.start.date" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.end.date" /></div></th>
                </tr>
            </thead>
        </table>
      </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="boardSpecialtiesSection" sectionTitleKey="label.board.specialties" sectionNum="${fieldNum}">
        <table id="specialtiesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
               summary="This table contains all of the board certified specialites that have been entered for the profile. It displays the
                        certifying board, the specialty name, the current board status, the effective date, when it has expired
                        (if necessary).">
        <thead>
                <tr>
                    <th scope="col" class="nofill"><div><fmt:message key="label.board" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.specialty" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.specialty.status.eligible.or.certified" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.effective.date" /></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.expiration.date" /></div></th>
                </tr>
            </thead>
        </table>
      </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="phrpSection" sectionTitleKey="label.phrp" sectionNum="${fieldNum}">
        <table id="certificatesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
               summary="This table displays the list of Protection of Human Research Participants training certificates
                        which have been added to the profile. It displays the organization which bestowed it, the effective
                        date, and the expiration date (if necessary).">
            <thead>
                <tr>
                    <th scope="col" class="nofill"><div><fmt:message key="label.organization.name"/></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="textfield.effectiveDate"/></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="textfield.expirationDate"/></div></th>
                </tr>
            </thead>
         </table>
       </firebird:sectionDisplay>
     </firebird:sectionColumn>

    <div>
        <firebird:viewGeneratedFormButton buttonId="viewIdfPdfButtonBottom" form="${form}"/>
    </div>
    <firebird:nextTabButton form="${form}" />

    <script>

    var expirationDateColumn = {mDataProp:"expirationDate", stype:"date",
        fnRender:function (obj) {
            return formatDateOrEmpty(obj.aData.expirationDate);
        }};
    var effectiveDateColumn = {mDataProp:"effectiveDate", stype:"date",
        fnRender:function (obj) {
            return formatDateOrEmpty(obj.aData.effectiveDate);
        }};

    var issuerNameColumn = {mDataProp:"issuer.name"};

    $(function() {
        createWorkHistoryTable();
        createDegreeTable();
        createInternshipTable();
        createResidencyTable();
        createFellowshipTable();
        createLicenseTable();
        createSpecialtyTable();
        createCertificatesTable();
      });


    function setupTable(data, columns) {
      return {
        aaData:data,
            bInfo:false,
            bLengthChange:false,
            bPaginate:false,
            bFilter:false,
            aoColumns: columns,
            fnRowCallback:function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", aData.id);
                return nRow;
            }
      };
    }

    function createWorkHistoryTable() {
        $("#workHistoryTable").dataTable(
            setupTable(${workHistoryJson}, [
                  {mDataProp:"position"},
                  issuerNameColumn,
                  effectiveDateColumn,
                  expirationDateColumn
        ]));
    }

    function createInternshipTable() {
      createMedicalSpecialtyTable("#internshipsTable", ${internshipJson}, true);
    }

    function createMedicalSpecialtyTable(tableId, data, isInternship) {
        var columns = [
            {mDataProp:"specialty.name"},
            issuerNameColumn,
            effectiveDateColumn,
            expirationDateColumn
        ];

        if (isInternship) {
            columns.unshift({mDataProp:"specialty.certifyingBoard.name"});
        }

      $(tableId).dataTable(
            setupTable(data, columns)
        );
    }

    function createResidencyTable() {
      createMedicalSpecialtyTable("#residenciesTable", ${residenciesJson});
    }

    function createFellowshipTable() {
      createMedicalSpecialtyTable("#fellowshipsTable", ${fellowshipJson});
    }

    function createDegreeTable() {
    $("#degreesTable").dataTable(
     setupTable(${degreesJson}, [
            {mDataProp: "degreeType.name"},
            {mDataProp:"effectiveDate", stype:"date",
                fnRender:function (obj) {
                    return getDateAsYear(obj.aData.effectiveDate);
              }},
            issuerNameColumn
    ]));
    }

    function createCertificatesTable() {
        $("#certificatesTable").dataTable(
            setupTable(${phrpCertificatesJson}, [
                issuerNameColumn,
                effectiveDateColumn,
                expirationDateColumn
            ]));
    }

    function createLicenseTable() {
        $("#licensesTable").dataTable(
          setupTable(${licensesJson}, [
              {mDataProp: "licenseType.name"},
                {mDataProp: "licenseId"},
                {mDataProp: "stateAndCountryString"},
                expirationDateColumn
          ]));
    }

    function createSpecialtyTable() {
        $("#specialtiesTable").dataTable(
          setupTable(${specialtiesJson}, [
              {mDataProp: "specialtyType.board.name"},
                {mDataProp: "specialtyType.display"},
                {mDataProp: "status.display"},
                effectiveDateColumn,
                expirationDateColumn
          ]));
    }

    </script>

</s:else>