/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a organization or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the NCI OCR Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the NCI OCR Software; (ii) distribute and
 * have distributed to and by third parties the NCI OCR Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM and the National Cancer Institute. If You do not include
 * such end-user documentation, You shall include this acknowledgment in the
 * Software itself, wherever such third-party acknowledgments normally appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", or "5AM"
 * to endorse or promote products derived from this Software. This License does
 * not authorize You to use any trademarks, service marks, trade names, logos or
 * product names of either NCI or 5AM, except as required to comply with the
 * terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC. OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.firebird.web.action.investigator.profile;

import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.organization.InvalidatedOrganizationException;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.common.Struts2UploadedFileInfo;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action class to handle the back and forth of dealing with credential edits.
 */
@Namespace(ManageCertificateCredentialsAction.NAMESPACE)
@InterceptorRef(value = "profileManagementStack")
@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.TooManyMethods" })
//for validateFields()
public class ManageCertificateCredentialsAction extends AbstractManageCredentialsAction {

    private static final long serialVersionUID = 1L;
    private Struts2UploadedFileInfo certificateFile;

    @SuppressWarnings("ucd")
    // annotations access these
    static final String NAMESPACE = "/investigator/profile/credentials/ajax";
    @SuppressWarnings("ucd")
    // annotations access these
    static final String JSP_FIELDS = "/WEB-INF/content" + NAMESPACE + "/manage_credentials_certificate.jsp";
    @SuppressWarnings("ucd")
    // annotations access these
    static final String JSP_DELETE = "/WEB-INF/content" + NAMESPACE + "/manage_credentials_certificate_delete.jsp";
    private boolean nihOerIssued;
    private final Organization nihOerIssuer;

    /**
     * Constructor.
     *
     * @param profileService the profileService to set
     * @param dataService the GenericDataRetrievalService to set
     * @param stateLookup the stateLookupService to set
     * @param countryLookup country lookup service.
     * @param organizationService Organization Service
     * @param nihOerOrganizationExternalId NIH OER external Identifier
     */
    @Inject
    @SuppressWarnings("PMD.ExcessiveParameterList")
    // All services need to be injected
    public ManageCertificateCredentialsAction(InvestigatorProfileService profileService,
            GenericDataRetrievalService dataService, StateLookupService stateLookup,
            CountryLookupService countryLookup, OrganizationService organizationService,
            @Named("nih.oer.organization.nes.id")
            String nihOerOrganizationExternalId) {
        super(profileService, dataService, stateLookup, countryLookup);
        setCertificate(new TrainingCertificate());
        try {
            this.nihOerIssuer = organizationService.getByExternalId(nihOerOrganizationExternalId);
        } catch (InvalidatedOrganizationException e) {
            throw new IllegalStateException("Unexpected failure for organization external id: "
                    + nihOerOrganizationExternalId, e);
        }
    }

    @Override
    public void prepare() {
        super.prepare();
        if (isNihOerIssued()) {
            getCertificate().setIssuer(nihOerIssuer);
        } else {
            setNihOerIssued(nihOerIssuer.equals(getCertificate().getIssuer()));
        }
        resolveContentType(certificateFile);
    }

    /**
     * Routes the user to the correct jsp for the dialog, either the main fields dialog or the delete information
     * dialog.
     *
     * @return the struts forward.
     */
    @Action(value = "manageCertificateCredentials", results = {
            @Result(name = FirebirdUIConstants.RETURN_FIELDS_PAGE, location = JSP_FIELDS),
            @Result(name = FirebirdUIConstants.RETURN_DELETE_CONFIRM_PAGE, location = JSP_DELETE) })
    public String manageCredentialsAjaxEnter() {
        if (getPage() == null) {
            setPage(FirebirdUIConstants.RETURN_FIELDS_PAGE);
        }
        return getPage();
    }

    /**
     * Save the new certificate.
     *
     * @return the struts forward
     * @throws IOException if uploaded file persistence fails.
     */
    @Action(value = "saveCertificate", results = { @Result(name = INPUT, location = JSP_FIELDS) })
    @Validations(
            customValidators = { @CustomValidator(type = "hibernate", fieldName = "certificate.issuer", parameters = {
                    @ValidationParameter(name = "resourceKeyBase", value = "profile.organization"),
                    @ValidationParameter(name = "excludes", value = "externalId") }) },
            requiredFields = { @RequiredFieldValidator(fieldName = "certificate.certificateType",
            key = "error.credentials.certificate.type.required") },
                fieldExpressions = { @FieldExpressionValidator(
            fieldName = "certificateFile.data",
            expression = "certificate.id != null || (certificateFile != null && certificateFile.data != null)",
            key = "error.credentials.certificate.file.required"),
            @FieldExpressionValidator(fieldName = "nihOerIssued",
                expression = "nihOerIssued || issuingOrganizationExternalId != null || certificate.issuer != null",
                key = "error.credentials.organization.required"),
            @FieldExpressionValidator(
                    fieldName = "certificate.issuer.postalAddress.stateOrProvince",
                    expression = "certificate.issuer != null"
                            + " && certificate.issuer.postalAddress.stateOrProvinceValid",
                    key = "stateOrProvince.required")
            })
    public String saveCertificate() throws IOException {
        setFileDescription();
        setCertificateIssuer();
        boolean valid = validateFields();
        if (valid) {
            try {
                doSave();
            } catch (CredentialAlreadyExistsException ae) {
                handleCredentialAlreadyExists();
                valid = false;
            } catch (ValidationException e) {
                return handleValidationException(e);
            }
        }

        return valid ? closeDialog(getDialogId()) : INPUT;
    }

    private void setFileDescription() {
        if (getCertificateFile() != null) {
            if (getCertificateFile().getData() == null) {
                setCertificateFile(null);
            } else {
                getCertificateFile().setDescription(getText(getCertificate().getCertificateType().getNameProperty()));
            }
        }
    }

    private void setCertificateIssuer() {
        if (isNihOerIssued()) {
            getCertificate().setIssuer(nihOerIssuer);
        }
    }

    private boolean validateFields() {
        return isUniqueCertificate() && isEffectiveDateValid() && isExpirationDateValid()
                && isExpirationAfterEffectiveDates();
    }

    private boolean isUniqueCertificate() {
        List<TrainingCertificate> existingCertificates = getExistingCertificatesIssuedByOrganization(getCertificate()
                .getIssuer());
        if (!existingCertificates.isEmpty()
                && (existingCertificates.size() > 1 || !isEditingCertificate(existingCertificates.get(0)))) {
            addFieldError("nihOerIssued", getText("error.duplicate.certificate.for.issuer"));
            return false;
        }
        return true;
    }

    private List<TrainingCertificate> getExistingCertificatesIssuedByOrganization(final Organization organization) {
        List<TrainingCertificate> certificates = Lists.newArrayList();
        for (AbstractCredential<?> credential : getExistingCredentials()) {
            TrainingCertificate existingCertificate = (TrainingCertificate) credential;
            if (organization.equals(existingCertificate.getIssuer())
                    && existingCertificate.getCertificateType().equals(getCertificate().getCertificateType())) {
                certificates.add(existingCertificate);
            }
        }
        return certificates;
    }

    private boolean isEditingCertificate(TrainingCertificate existingCertificate) {
        return existingCertificate.getId().equals(getCertificate().getId());
    }

    private boolean isEffectiveDateValid() {
        if (isHumanResearchCertificate() && getEffectiveDate() == null) {
            // Human Research certificates need a valid effective date.
            addFieldError("effectiveDate", getText("error.effective.date.required.for.human.research.certificates"));
            return false;
        }
        try {
            getCertificate().setEffectiveDate(getParsedEffectiveDate());
        } catch (ParseException e) {
            addFieldError("effectiveDate", getText("error.date.month.year.format"));
            return false;
        }
        return true;
    }

    private boolean isHumanResearchCertificate() {
        return getCertificate().getCertificateType() == CertificateType.HUMAN_RESEARCH_CERTIFICATE;
    }

    private boolean isExpirationDateValid() {
        try {
            getCertificate().setExpirationDate(getParsedExpirationDate());
        } catch (ParseException e) {
            addFieldError("expirationDate", getText("error.date.month.year.format"));
            return false;
        }
        return true;
    }

    private boolean isExpirationAfterEffectiveDates() {
        try {
            validateExpirationAfterEffectiveDates();
        } catch (ValidationException e) {
            addFieldError("expirationDate", getText("error.expiration.date.before.effective"));
            return false;
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private void doSave() throws CredentialAlreadyExistsException, IOException, ValidationException {
        File file = getCertificateFileData();
        FileMetadata metadata = getCertificateFileMetaData();
        getProfileService().saveCertificate(getProfile(), getCertificate(), file, metadata);
    }

    private File getCertificateFileData() {
        return getCertificateFile() != null ? getCertificateFile().getData() : null;
    }

    private FileMetadata getCertificateFileMetaData() {
        return getCertificateFile() != null ? getCertificateFile().getMetadata() : null;
    }

    /**
     * Removes the credential from the profile.
     *
     * @return the struts forward.
     */
    @Action("deleteCertificate")
    public String deleteCertificate() {
        getProfileService().deleteCertificate(getProfile(), getCertificate());
        return closeDialog();
    }

    /**
     * Retrieves the CTEP ID of the selected issuing organization.
     *
     * @return CTEP ID of the selected issuing organization.
     */
    @Action(value = "retrieveCtepId",
            results = @Result(type = "json", params = { "root", "certificate.issuer.ctepId" }))
    public String retrieveCtepId() {
        return SUCCESS;
    }

    /**
     * @param cert the Certificate credential to set
     */
    public final void setCertificate(TrainingCertificate cert) {
        setCredential(cert);
    }

    /**
     * @return the credential as a Certificate
     */
    public final TrainingCertificate getCertificate() {
        if (getCredential() instanceof TrainingCertificate) {
            return (TrainingCertificate) getCredential();
        } else {
            return null;
        }
    }

    /**
     * @return uploaded certificate file data.
     */
    public Struts2UploadedFileInfo getCertificateFile() {
        return certificateFile;
    }

    /**
     * @param certificateFile uploaded certificate file data.
     */
    public void setCertificateFile(Struts2UploadedFileInfo certificateFile) {
        this.certificateFile = certificateFile;
    }

    /**
     * @return whether or not the training certificate is NIH OER issued
     */
    public boolean isNihOerIssued() {
        return nihOerIssued;
    }

    /**
     * @param nihOerIssued whether or not the training certificate is NIH OER issued
     */
    public void setNihOerIssued(boolean nihOerIssued) {
        this.nihOerIssued = nihOerIssued;
    }

    /**
     * @return the nihOerIssuer
     */
    public Organization getNihOerIssuer() {
        return nihOerIssuer;
    }

}
