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

import gov.nih.nci.firebird.common.FirebirdDateUtils;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.LaboratoryCertificateType;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.web.common.Struts2UploadedFileInfo;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action class to handle the Modification of Organization Association data including Adding, Removing, and editing.
 */
@Namespace("/investigator/profile/associations/org/ajax")
@Results(value = {
        @Result(name = ClinicalLabCertificateAction.RETURN_VIEW_CERTIFICATES,
                location = "clinical_lab_certificates.jsp"),
        @Result(name = ActionSupport.INPUT, location = "clinical_lab_certificates.jsp") })
@InterceptorRef(value = "profileManagementStack")
public class ClinicalLabCertificateAction extends AbstractProfileAction {
    private static final String EXPIRATION_DATE_FIELD = "expirationDate";

    private static final long serialVersionUID = 1L;

    static final String RETURN_VIEW_CERTIFICATES = "viewCertificates";
    private final OrganizationAssociationService associationService;
    private LaboratoryCertificate certificate = new LaboratoryCertificate();
    private Struts2UploadedFileInfo certificateFile;
    private Long associationId;
    private String effectiveDate;
    private String expirationDate;
    private OrganizationAssociation laboratory;
    private Map<String, Collection<LaboratoryCertificate>> jsonResult;

    /**
     * @param associationService the Association service for handling the adding and saving of a certificate to a
     *            Clinical Lab Association.
     * @param profileService the investigator profile service
     */
    @Inject
    public ClinicalLabCertificateAction(OrganizationAssociationService associationService,
            InvestigatorProfileService profileService) {
        super(profileService);
        this.associationService = associationService;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (getAssociationId() != null) {
            laboratory = associationService.getById(getAssociationId());
        } else {
            laboratory = null;
        }
        resolveContentType(certificateFile);
    }

    /**
     * Validates that the user has a profile and directs traffic to the Manage Associations ajax page.
     *
     * @return the struts forward.
     */
    @Action("clinicalLabCertificateEnter")
    public String clinicalLabCertificateEnter() {
        if (laboratory == null) {
            return closeDialog();
        }
        if (certificate != null && certificate.getType() != null
                && getLab().getCertificate(certificate.getType()) != null) {
            certificate = getLab().getCertificate(certificate.getType());
            effectiveDate = formatDate(certificate.getEffectiveDate());
            expirationDate = formatDate(certificate.getExpirationDate());
        } else {
            certificate = null;
        }
        if (certificate != null) {
            effectiveDate = formatDate(certificate.getEffectiveDate());
            expirationDate = formatDate(certificate.getExpirationDate());
        }
        return RETURN_VIEW_CERTIFICATES;
    }

    /**
     * Saves the Updated Clinical Lab Certificate data.
     *
     * @return the struts forward
     * @throws ParseException the dates were improperly formatted
     */
    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "certificate.type",
                    key = "clinical.laboratory.certificates.error.required.type"),
            @RequiredFieldValidator(fieldName = EXPIRATION_DATE_FIELD, key = "error.expiration.date.required"),
            @RequiredFieldValidator(fieldName = "certificateFile.data", key = "error.file.required") },
            regexFields = {
                    @RegexFieldValidator(fieldName = "effectiveDate", key = "error.date.month.year.format",
                            regexExpression = "[01]?\\d/\\d{4}"),
                    @RegexFieldValidator(fieldName = EXPIRATION_DATE_FIELD, key = "error.date.month.year.format",
                            regexExpression = "[01]?\\d/\\d{4}") })
    @Action("saveCertificate")
    public String saveCertificateAction() throws ParseException {
        if (getLab().getCertificate(certificate.getType()) != null) {
            certificate = getLab().getCertificate(certificate.getType());
        }
        certificate.setEffectiveDate(FirebirdDateUtils.parseMonthAndYearStringOrNull(effectiveDate));
        certificate.setExpirationDate(FirebirdDateUtils.parseMonthAndYearStringOrNull(expirationDate));
        try {
            validateExpirationAndEffectiveDates();
            File file = getCertificateFile().getData();
            FileMetadata metadata = getCertificateFile().getMetadata();
            associationService.handleCertificate(getLaboratory(), getCertificate(), file, metadata);
        } catch (IOException e) {
            LOG.error("An Error occurred saving a certificate to a lab", e);
            addActionError("A Problem has occurred while trying to save this certificate.");
            return INPUT;
        } catch (ValidationException e) {
            addFieldError(EXPIRATION_DATE_FIELD, getText("error.expiration.date.before.effective"));
            return INPUT;
        }
        certificate = null;
        effectiveDate = null;
        expirationDate = null;
        return INPUT;
    }

    private void validateExpirationAndEffectiveDates() throws ValidationException {
        if (isEffectiveBeforeOrEqualToExpirationDate(certificate.getEffectiveDate(), certificate.getExpirationDate())) {
            throw new ValidationException(new ValidationFailure(EXPIRATION_DATE_FIELD,
                    getText("error.expiration.date.before.effective")));
        }
    }

    /**
     * Deletes a Certificate and saves the association.
     *
     * @return the struts forward
     */
    @Action("deleteCertificate")
    public String deleteCertificateAction() {
        getLab().removeCertificate(certificate.getType());
        associationService.save(laboratory);
        certificate = null;
        return INPUT;
    }

    /**
     * @return json representation of the Laboratory Certificates.
     * @throws JSONException If there is a problem during serialization.
     */
    public String getCertificatesJson() throws JSONException {
        List<CertificateListing> certificates = getCertificateListings();
        return JSONUtil.serialize(certificates);
    }

    private List<CertificateListing> getCertificateListings() {
        List<CertificateListing> certificates = Lists.newArrayList();
        for (LaboratoryCertificate laboratoryCertificate : getLab().getCertificates().values()) {
            certificates.add(new CertificateListing(laboratoryCertificate));
        }
        return certificates;
    }

    /**
     * Method to get a trimmed list of Certificate Types, removing any that have already been saved.
     *
     * @return the remaining certificate types.
     */
    public EnumSet<LaboratoryCertificateType> getCertificateTypes() {
        EnumSet<LaboratoryCertificateType> certificateTypes = EnumSet.allOf(LaboratoryCertificateType.class);
        for (LaboratoryCertificateType type : getLab().getCertificates().keySet()) {
            certificateTypes.remove(type);
            // A Lab can have a CLIA or a CLP, but not both.
            if (type == LaboratoryCertificateType.CLIA || type == LaboratoryCertificateType.CLP) {
                certificateTypes.remove(LaboratoryCertificateType.CLIA);
                certificateTypes.remove(LaboratoryCertificateType.CLP);
            }
        }
        return certificateTypes;
    }

    private ClinicalLaboratory getLab() {
        return (ClinicalLaboratory) laboratory.getOrganizationRole();
    }

    /**
     * @return the certificate
     */
    public LaboratoryCertificate getCertificate() {
        return certificate;
    }

    /**
     * @param certificate the certificate to set
     */
    public void setCertificate(LaboratoryCertificate certificate) {
        this.certificate = certificate;
    }

    /**
     * @return the certificateFile
     */
    public Struts2UploadedFileInfo getCertificateFile() {
        return certificateFile;
    }

    /**
     * @param certificateFile the certificateFile to set
     */
    public void setCertificateFile(Struts2UploadedFileInfo certificateFile) {
        this.certificateFile = certificateFile;
    }

    /**
     * @return the formatted effective date from the certificate.
     */
    public String getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * @param effectiveDate the effective date to parse for the certificate
     */
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * @return the formatted expiration date of the certificate.
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to parse for the certificate.
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the associationId
     */
    public Long getAssociationId() {
        return associationId;
    }

    /**
     * @param associationId the associationId to set
     */
    public void setAssociationId(Long associationId) {
        this.associationId = associationId;
    }

    /**
     * @return the laboratory
     */
    public OrganizationAssociation getLaboratory() {
        return laboratory;
    }

    /**
     * @return the jsonResult
     */
    public Map<String, Collection<LaboratoryCertificate>> getJsonResult() {
        return jsonResult;
    }

    /**
     * Table listing for a LaboratoryCertificate object.
     */
    public class CertificateListing {

        private Long id;
        private final String type;
        private final Date effectiveDate;
        private final Date expirationDate;

        /**
         * @param certificate certificate
         */
        public CertificateListing(LaboratoryCertificate certificate) {
            if (certificate.getCertificateFile() != null) {
                this.id = certificate.getCertificateFile().getId();
            }
            this.type = certificate.getType().name();
            this.effectiveDate = certificate.getEffectiveDate();
            this.expirationDate = certificate.getExpirationDate();
        }

        /**
         * @return the id
         */
        public Long getId() {
            return id;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @return the effectiveDate
         */
        public Date getEffectiveDate() {
            return effectiveDate;
        }

        /**
         * @return the expirationDate
         */
        public Date getExpirationDate() {
            return expirationDate;
        }
    }
}