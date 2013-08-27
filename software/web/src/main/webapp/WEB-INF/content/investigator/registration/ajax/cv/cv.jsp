<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<h2 class="clear"><fmt:message key="registration.cv.title"/></h2>
<firebird:messages/>

<s:if test="registration.lockedForInvestigator">
    <div>
        <firebird:viewGeneratedFormButton buttonId="viewCvPdfButtonTop" form="${form}"/>
    </div>
</s:if>
<s:else>
    <s:if test="%{profileContainsExpiredCredentials()}">
        <div id="expiredCredentialMessageDiv" class="errorMessage formcol_row blank_space">
            <fmt:message key="registration.cv.expired.credentials.warning"/>
        </div>
    </s:if>
    <firebird:viewGeneratedFormButton buttonId="viewCvPdfButtonTop" form="${form}"/>
    <s:if test="%{registration.curriculumVitaeForm.comments != null && !registration.curriculumVitaeForm.comments.empty}">
        <div class="formcol_row">
            <firebird:comments retainState="true">
                ${registration.curriculumVitaeForm.comments}
            </firebird:comments>
        </div>
    </s:if>
    <div class="line"></div>
    <div class="formcol_row">
        <fmt:message key="registration.cv.intro" />
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
        <firebird:sectionDisplay id="nesIdSection" sectionTitleKey="label.person.nes.id"  sectionNum="${fieldNum}">
            <p>${registration.profile.person.nesId}</p>
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
                            where it was achieved, when it started, and when it ended (if necessary)">
            <thead>
                <tr>
                    <th scope="col" class="nofill"><div><fmt:message key="label.position"/></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.where"/></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.start.date"/></div></th>
                    <th scope="col" class="nofill"><div><fmt:message key="label.end.date"/></div></th>
                </tr>
            </thead>
            <tbody>
                <s:set var="list" value="%{getCvCredentials(@gov.nih.nci.firebird.data.CredentialType@WORK_HISTORY)}"/>
                <s:if test="%{#list.empty}">
                    <tr>
                        <td colspan="4"><fmt:message key="label.none"/></td>
                    </tr>
                </s:if>
                <s:iterator value="%{#list}" var="cred">
                    <tr id="${cred.id}">
                        <td>${cred.position}</td>
                        <td>${cred.issuer.name}</td>
                        <td><s:date name="%{#cred.effectiveDate}" format="MM/yyyy"/></td>
                        <td><s:date name="%{#cred.expirationDate}" format="MM/yyyy"/></td>
                    </tr>
                </s:iterator>
            </tbody>
        </table>
        </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="licensesSection" sectionTitleKey="label.professional.licenses" sectionNum="${fieldNum}">
          <table id="licensesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                   summary="This table contains all of the licenses that have been entered for the profile. It displays the
                            license name, the Identification number, where it was acquired, andthe expiration date.">
              <thead>
                  <tr>
                       <th scope="col" class="nofill"><div><fmt:message key="label.license.type"/></div></th>
                       <th scope="col" class="nofill"><div><fmt:message key="label.license.number"/></div></th>
                       <th scope="col" class="nofill"><div><fmt:message key="label.where"/></div></th>
                       <th scope="col" class="nofill"><div><fmt:message key="label.license.expiration.date"/></div></th>
                  </tr>
              </thead>
              <tbody>
                  <s:set var="list" value="%{getCvCredentials(@gov.nih.nci.firebird.data.CredentialType@MEDICAL_LICENSE)}"/>
                  <s:if test="%{#list.empty}">
                      <tr>
                          <td colspan="4"><fmt:message key="label.none"/></td>
                      </tr>
                  </s:if>
                  <s:iterator value="%{#list}" var="cred">
                      <tr id="${cred.id}" class="${cred.expired ? 'expiredCredential' : ''}">
                          <td>${cred.licenseType.name}</td>
                          <td>${cred.licenseId}</td>
                          <td>${cred.stateAndCountryString}</td>
                          <td><s:date name="%{#cred.expirationDate}" format="MM/yyyy"/></td>
                      </tr>
                  </s:iterator>
              </tbody>
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
                     <th scope="col" class="nofill"><div><fmt:message key="label.degree"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="registration.cv.degree.date.column"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.institution"/></div></th>
                </tr>
            </thead>
            <tbody>
                <s:set var="list" value="%{getCvCredentials(@gov.nih.nci.firebird.data.CredentialType@DEGREE)}"/>
                <s:if test="%{#list.empty}">
                    <tr>
                        <td colspan="3"><fmt:message key="label.none"/></td>
                    </tr>
                </s:if>
                <s:iterator value="%{#list}" var="cred">
                    <tr id="${cred.id}">
                        <td>${cred.degreeType.name}</td>
                        <td><s:date name="%{#cred.effectiveDate}" format="yyyy"/></td>
                        <td>${cred.issuer.name}</td>
                    </tr>
                </s:iterator>
            </tbody>
        </table>
        </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="internshipsSection" sectionTitleKey="label.internships" sectionNum="${fieldNum}">
        <table id="internshipsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                   summary="This table contains all of the internships that have been entered for the profile. It displays the
                            certifying board, the specialty, where the internship took place, the start date, and when it
                            ended (if necessary).">
            <thead>
                <tr>
                     <th scope="col" class="nofill"><div><fmt:message key="label.certifying.board"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.specialty"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.where"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.start.date"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.end.date"/></div></th>
                </tr>
            </thead>
            <tbody>
                <s:set var="list" value="%{getCvCredentials(@gov.nih.nci.firebird.data.CredentialType@INTERNSHIP)}"/>
                <s:if test="%{#list.empty}">
                    <tr>
                        <td colspan="5"><fmt:message key="label.none"/></td>
                    </tr>
                </s:if>
                <s:iterator value="%{#list}" var="cred">
                    <tr id="${cred.id}">
                        <td>${cred.specialty.certifyingBoard.name}</td>
                        <td>${cred.specialty.name}</td>
                        <td>${cred.issuer.name}</td>
                        <td><s:date name="%{#cred.effectiveDate}" format="MM/yyyy"/></td>
                        <td><s:date name="%{#cred.expirationDate}" format="MM/yyyy"/></td>
                    </tr>
                </s:iterator>
            </tbody>
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
                     <th scope="col" class="nofill"><div><fmt:message key="label.certifying.board"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.specialty"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.where"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.start.date"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.end.date"/></div></th>
                </tr>
            </thead>
            <tbody>
                <s:set var="list" value="%{getCvCredentials(@gov.nih.nci.firebird.data.CredentialType@RESIDENCY)}"/>
                <s:if test="%{#list.empty}">
                    <tr>
                        <td colspan="5"><fmt:message key="label.none"/></td>
                    </tr>
                </s:if>
                <s:iterator value="%{#list}" var="cred">
                    <tr id="${cred.id}">
                        <td>${cred.specialty.certifyingBoard.name}</td>
                        <td>${cred.specialty.name}</td>
                        <td>${cred.issuer.name}</td>
                        <td><s:date name="%{#cred.effectiveDate}" format="MM/yyyy"/></td>
                        <td><s:date name="%{#cred.expirationDate}" format="MM/yyyy"/></td>
                    </tr>
                </s:iterator>
            </tbody>
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
                     <th scope="col" class="nofill"><div><fmt:message key="label.certifying.board"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.sub.specialty"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.where"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.start.date"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.end.date"/></div></th>
                </tr>
            </thead>
            <tbody>
                <s:set var="list" value="%{getCvCredentials(@gov.nih.nci.firebird.data.CredentialType@FELLOWSHIP)}"/>
                <s:if test="%{#list.empty}">
                    <tr>
                        <td colspan="5"><fmt:message key="label.none"/></td>
                    </tr>
                </s:if>
                <s:iterator value="%{#list}" var="cred">
                    <tr id="${cred.id}">
                        <td>${cred.specialty.certifyingBoard.name}</td>
                        <td>${cred.specialty.name}</td>
                        <td>${cred.issuer.name}</td>
                        <td><s:date name="%{#cred.effectiveDate}" format="MM/yyyy"/></td>
                        <td><s:date name="%{#cred.expirationDate}" format="MM/yyyy"/></td>
                    </tr>
                </s:iterator>
            </tbody>
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
                       <th scope="col" class="nofill"><div><fmt:message key="label.certification.board"/></div></th>
                       <th scope="col" class="nofill"><div><fmt:message key="label.specialty"/></div></th>
                       <th scope="col" class="nofill"><div><fmt:message key="label.specialty.status.eligible.or.certified"/></div></th>
                       <th scope="col" class="nofill"><div><fmt:message key="label.effective.date"/></div></th>
                       <th scope="col" class="nofill"><div><fmt:message key="label.expiration.date"/></div></th>
                  </tr>
              </thead>
              <tbody>
                  <s:set var="list" value="%{getCvCredentials(@gov.nih.nci.firebird.data.CredentialType@SPECIALTY)}"/>
                  <s:if test="%{#list.empty}">
                      <tr>
                          <td colspan="4"><fmt:message key="label.none"/></td>
                      </tr>
                  </s:if>
                  <s:iterator value="%{#list}" var="cred">
                      <tr id="${cred.id}" class="${cred.expired ? 'expiredCredential' : ''}">
                          <td>${cred.specialtyType.board.name}</td>
                          <td>${cred.specialtyType.display}</td>
                          <td>${cred.status.display}</td>
                          <td><s:date name="%{#cred.effectiveDate}" format="MM/yyyy"/></td>
                          <td><s:date name="%{#cred.expirationDate}" format="MM/yyyy"/></td>
                      </tr>
                  </s:iterator>
              </tbody>
          </table>
      </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="certificationsSection" sectionTitleKey="label.professional.certifications" sectionNum="${fieldNum}">
        <table id="certificationsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                   summary="This table displays the list of Certifications which have been added to the profile.
                            It displays the name of the certification, the effective date, and when the certification expires
                            (if necessary).">
            <thead>
                <tr>
                     <th scope="col" class="nofill"><div><fmt:message key="label.certification"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="registration.cv.certification.effective.date.column"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="registration.cv.certification.expiration.date.column"/></div></th>
                </tr>
            </thead>
            <tbody>
                <s:set var="list" value="%{getCvCredentials(@gov.nih.nci.firebird.data.CredentialType@CERTIFICATION)}"/>
                <s:if test="%{#list.empty}">
                    <tr>
                        <td colspan="3"><fmt:message key="label.none"/></td>
                    </tr>
                </s:if>
                <s:iterator value="%{#list}" var="cred">
                    <tr id="${cred.id}" class="${cred.expired ? 'expiredCredential' : ''}">
                        <td>${cred.certificationType.name}</td>
                        <td><s:date name="%{#cred.effectiveDate}" format="MM/yyyy"/></td>
                        <td><s:date name="%{#cred.expirationDate}" format="MM/yyyy"/></td>
                    </tr>
                </s:iterator>
            </tbody>
        </table>
      </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="trainingCertificatesSection" sectionTitleKey="label.training.certificates" sectionNum="${fieldNum}">
        <table id="certificatesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                   summary="This table displays the list of training certificates which have been added to the profile. It displays
                            the certificate type, the organization which bestowed it, the effective date, and the
                            expiration date (if necessary).">
            <thead>
                <tr>
                     <th scope="col" class="nofill"><div><fmt:message key="label.training.certificate"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.organization.name"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.effective.date"/></div></th>
                     <th scope="col" class="nofill"><div><fmt:message key="label.expiration.date"/></div></th>
                </tr>
            </thead>
            <tbody>
                <s:set var="list" value="%{getCvCredentials(@gov.nih.nci.firebird.data.CredentialType@CERTIFICATE)}"/>
                <s:if test="%{#list.empty}">
                    <tr>
                        <td colspan="2"><fmt:message key="label.none"/></td>
                    </tr>
                </s:if>
                <s:iterator value="%{#list}" var="cred">
                    <tr id="${cred.id}" class="${cred.expired ? 'expiredCredential' : ''}">
                        <td><s:property value="%{getText(#cred.certificateType.nameProperty)}"/></td>
                        <td>${cred.issuer.name}</td>
                        <td><s:date name="%{#cred.effectiveDate}" format="MM/yyyy"/></td>
                        <td><s:date name="%{#cred.expirationDate}" format="MM/yyyy"/></td>
                    </tr>
                </s:iterator>
            </tbody>
        </table>
      </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <s:set var="fieldNum" value="#fieldNum + 1"/>
    <firebird:sectionColumn location="full">
        <firebird:sectionDisplay id="experienceSection" sectionTitleKey="label.clinical.research.experience" sectionNum="${fieldNum}">
        <div class="richText">
              <s:if test="registration.profile.clinicalResearchExperience">
                  <s:property value="registration.profile.clinicalResearchExperience.text" escape="false"/>
              </s:if>
              <s:else>
                  <fmt:message key="registration.cv.experience.summary.empty"/>
              </s:else>
      </div>
      </firebird:sectionDisplay>
    </firebird:sectionColumn>

    <div class="blank_space"></div>
    <div>
        <firebird:viewGeneratedFormButton buttonId="viewCvPdfButtonBottom" form="${form}"/>
    </div>
    <div class="clear"></div>

</s:else>
