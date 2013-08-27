/*
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
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

package gov.nih.nci.firebird.service.registration;

import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Keystore;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.AbstractGenericServiceBean;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.pdf.PdfService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.signing.DigitalSigningAttributes;
import gov.nih.nci.firebird.service.signing.DigitalSigningException;
import gov.nih.nci.firebird.service.signing.DigitalSigningService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.user.CertificateAuthorityManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.EnumSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.hibernate.Query;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Base class for RegistrationServiceBeans.
 *
 * @param <T> Registration type
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.CyclomaticComplexity" })
// requires many supporting methods for readability; switch must support all form types
public abstract class AbstractBaseRegistrationServiceBean<T extends AbstractRegistration> extends
        AbstractGenericServiceBean<T> implements BaseRegistrationService<T> {

    private static final EnumSet<FormTypeEnum> FORMS_WITH_PDF_OUTPUT = EnumSet.of(FormTypeEnum.CV,
            FormTypeEnum.CTEP_FORM_1572, FormTypeEnum.FORM_1572, FormTypeEnum.FINANCIAL_DISCLOSURE_FORM,
            FormTypeEnum.CTEP_FINANCIAL_DISCLOSURE_FORM, FormTypeEnum.SUPPLEMENTAL_INVESTIGATOR_DATA_FORM);

    private PersonService personService;
    private OrganizationService organizationService;
    private FileService fileService;
    private PdfService pdfService;
    private EmailService emailService;
    private TemplateService templateService;
    private DigitalSigningService digitalSigningService;
    private SponsorService sponsorService;
    private CertificateAuthorityManager certificateAuthorityManager;
    private ResourceBundle resources;

    @SuppressWarnings("unchecked")  // Hibernate list() method is unchecked
    @Override
    public List<T> getByStatus(RegistrationStatus status) {
        Query query = getSessionProvider().get().createQuery("from " + getRegistrationClass().getName()
                + " where status = :status");
        query.setParameter("status", status);
        return query.list();
    }

    /**
     * @return the type of registration handled by a child class
     */
    protected abstract Class<T> getRegistrationClass();

    @Override
    public void generatePdf(AbstractRegistrationForm form, OutputStream outputStream) {
        AbstractPdfGenerator pdfGenerator = getPdfGenerator(form);
        byte[] completedFormBytes = pdfGenerator.generatePdf();
        pdfGenerator.flattenPdf(completedFormBytes, outputStream);
    }

    private AbstractPdfGenerator getPdfGenerator(AbstractRegistrationForm form) {
        AbstractRegistration registration = form.getRegistration();
        switch (form.getFormType().getFormTypeEnum()) {
        case CV:
            return new CvPdfGenerator(registration, pdfService, fileService, resources);
        case SUPPLEMENTAL_INVESTIGATOR_DATA_FORM:
            return new SupplementalInvestigatorDataFormPdfGenerator(registration, pdfService, fileService, resources);
        case FINANCIAL_DISCLOSURE_FORM:
            return new FinancialDisclosurePdfGenerator(registration, pdfService, fileService);
        case FORM_1572:
            return new ProtocolForm1572PdfGenerator(registration, pdfService, fileService, resources);
        case CTEP_FORM_1572:
            return new AnnualRegistrationForm1572PdfGenerator(registration, pdfService, fileService, resources);
        case CTEP_FINANCIAL_DISCLOSURE_FORM:
            return new CtepFinancialDisclosurePdfGenerator(registration, pdfService, fileService);
        default:
            throw new IllegalArgumentException("Unsupported form type for PDF generation: "
                    + form.getFormType().getFormTypeEnum());
        }
    }

    /**
     *
     * @param user User
     * @param password Password
     * @return Keystore for User
     */
    protected Keystore getKeystore(FirebirdUser user, String password) {
        try {
            return certificateAuthorityManager.getOrCreateUserKey(user, password);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected error getting user keystore", e);
        } catch (DigitalSigningException e) {
            throw new IllegalStateException("Unexpected error getting user keystore", e);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unexpected error getting user keystore", e);
        }
    }

    /**
     * Validates if all forms are in Submittable status.
     *
     * @param registration Registration
     */
    protected void validateFormStatuses(T registration) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (!form.isSubmittable()) {
                throw new IllegalArgumentException("Form " + form.getFormType() + " is not submittable, status is "
                        + form.getFormStatus());
            }
        }
    }

    /**
     *
     * @param registration Registration
     * @param keystore Keystore
     * @param password Password
     */
    protected void prepareDocumentsForReview(T registration, Keystore keystore, String password) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            form.submitForm();
            if (supportsPdfOutput(form)) {
                generatePdfsForReview(form, keystore, password);
            }
        }
    }

    private boolean supportsPdfOutput(AbstractRegistrationForm form) {
        FormTypeEnum formTypeEnum = form.getFormType().getFormTypeEnum();
        return FORMS_WITH_PDF_OUTPUT.contains(formTypeEnum);
    }

    private void generatePdfsForReview(AbstractRegistrationForm form, Keystore keystore, String password) {
        AbstractPdfGenerator pdfGenerator = getPdfGenerator(form);
        byte[] completedFormBytes = pdfGenerator.generatePdfForSigning();
        if (form.isSignable()) {
            byte[] signedFormBytes = getSignedForm(completedFormBytes, form, keystore, password);
            form.setSignedPdf(createPdfFirebirdFile(form.getFormFileBaseName() + "_original.pdf", signedFormBytes));
            byte[] flattenedSignedBytes = pdfGenerator.flattenPdf(signedFormBytes);
            form.setFlattenedPdf(createPdfFirebirdFile(form.getFormFileBaseName() + ".pdf", flattenedSignedBytes));
        } else {
            byte[] flattenedPdfBytes = pdfGenerator.flattenPdf(completedFormBytes);
            form.setFlattenedPdf(createPdfFirebirdFile(form.getFormFileBaseName() + ".pdf", flattenedPdfBytes));
        }
    }

    private FirebirdFile createPdfFirebirdFile(String filename, byte[] fileBytes) {
        FileMetadata pdfMetadata = new FileMetadata(filename, "application/pdf");
        try {
            return fileService.createFile(fileBytes, pdfMetadata);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected problem creating FirebirdFile for pdf", e);
        }
    }

    @Override
    public void prepareForSubmission(T registration) throws ValidationException {
        if (registration.getStatus() != RegistrationStatus.RETURNED) {
            registration.setStatus(RegistrationStatus.INCOMPLETE);
        }
        refreshFromNes(registration);
        checkFormCompletionStatus(registration);
        // below only occurs if no validation exception was thrown.
        if (registration.getStatus() != RegistrationStatus.RETURNED) {
            registration.setStatus(RegistrationStatus.COMPLETED);
        }
        save(registration);
    }

    private void refreshFromNes(T registration) {
        for (Organization organization : registration.getOrganizations()) {
            refreshFromNes(organization);
        }
        for (Person person : registration.getPersons()) {
            personService.refreshFromNes(person);
        }
    }

    private void refreshFromNes(Organization organization) {
        organizationService.refreshFromNes(organization);
    }

    @Override
    public void checkFormCompletionStatus(T registration) throws ValidationException {
        try {
            registration.validate(getResources());
        } finally {
            save(registration);
        }
    }

    @Override
    public void checkUnReviewedAndUnRevisedFormCompletionStatus(T registration) throws ValidationException {
        try {
            registration.validateUnReviewedAndUnRevisedForms(getResources());
        } finally {
            save(registration);
        }
    }

    @Override
    public final void signAndSubmit(T registration, FirebirdUser user, String password) {
        Keystore keystore = getKeystore(user, password);
        validateFormStatuses(registration);
        prepareDocumentsForReview(registration, keystore, password);
        submitRegistration(registration);
        save(registration);
        sendSubmissionNotifications(registration);
    }

    private byte[] getSignedForm(byte[] completedFormBytes, AbstractRegistrationForm form, Keystore keystore,
            String password) {
        ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(completedFormBytes);
        ByteArrayOutputStream signedPdfOutputStream = new ByteArrayOutputStream();
        DigitalSigningAttributes signingAttributes = createSigningAttributes(form, keystore, password);
        return addSignature(pdfInputStream, signedPdfOutputStream, signingAttributes);
    }

    private byte[] addSignature(ByteArrayInputStream pdfInputStream, ByteArrayOutputStream signedPdfOutputStream,
            DigitalSigningAttributes signingAttributes) {
        try {
            digitalSigningService.signPdf(pdfInputStream, signedPdfOutputStream, signingAttributes);
            return signedPdfOutputStream.toByteArray();
        } catch (DigitalSigningException e) {
            throw new IllegalStateException("Unexpected error signing form", e);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected error signing form", e);
        } finally {
            IOUtils.closeQuietly(pdfInputStream);
            IOUtils.closeQuietly(signedPdfOutputStream);
        }
    }

    private DigitalSigningAttributes createSigningAttributes(AbstractRegistrationForm form, Keystore keystore,
            String password) {
        DigitalSigningAttributes attributes = new DigitalSigningAttributes();
        InvestigatorProfile profile = form.getRegistration().getProfile();
        attributes.setSigningFieldName(form.getFormType().getSigningField());
        attributes.setSigningKeyStore(getKeystoreInputStream(keystore));
        attributes.setSigningLocation(profile.getPrimaryOrganization().getOrganization().getPostalAddress()
                .toOneLineString());
        attributes.setSigningPassword(password);
        attributes.setSigningReason(resources.getString("signing.reason"));
        return attributes;
    }

    private InputStream getKeystoreInputStream(Keystore keystore) {
        try {
            return fileService.readFile(keystore.getKeystoreFile());
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected error reading keystore file", e);
        }
    }

    /**
     * @param registration registration to submit
     */
    protected abstract void submitRegistration(T registration);

    @Override
    public void setReturnedOrRevisedRegistrationsFormStatusesToRevised(InvestigatorProfile profile,
            FormTypeEnum... formTypes) {
        setRegistrationFormStatusesToRevisedIfReviewed(getReturnedOrRevisedRegistrations(profile), formTypes);
    }

    @Override
    public Set<T> getReturnedOrRevisedRegistrations(InvestigatorProfile profile) {
        Set<T> returnedRegistrations = Sets.newHashSet();
        for (T registration : getRegistrations(profile)) {
            if (isReturnedOrRevised(registration)) {
                returnedRegistrations.add(registration);
            }
        }
        return returnedRegistrations;
    }

    private boolean isReturnedOrRevised(T registration) {
        return registration.getStatus() == RegistrationStatus.RETURNED
                || (registration.getLastSubmissionDate() != null && !registration.isLockedForInvestigator());
    }

    @Override
    public void setRegistrationFormStatusesToRevisedIfReviewed(Set<T> registrations, FormTypeEnum... formTypes) {
        for (T registration : registrations) {
            setRegistrationFormStatusesToRevisedIfReviewed(registration, formTypes);
            save(registration);
        }
    }

    @Override
    public void save(T registration, FormTypeEnum... formTypesToSetToRevised) {
        if (isReturnedOrRevised(registration)) {
            setRegistrationFormStatusesToRevisedIfReviewed(registration, formTypesToSetToRevised);
        }
        save(registration);
    }

    @Override
    public void save(Set<T> registrations) {
        for (T registration : registrations) {
            save(registration);
        }
    }

    private void setRegistrationFormStatusesToRevisedIfReviewed(T registration, FormTypeEnum... formTypes) {
        for (FormTypeEnum formType : formTypes) {
            AbstractRegistrationForm form = registration.getForm(formType);
            if (form != null && form.isReviewed()) {
                form.setFormStatus(FormStatus.REVISED);
            }
        }
    }

    @Override
    public void addFinancialDisclosureFile(T registration, File file, FileMetadata fileMetadata) throws IOException {
        if (registration == null) {
            throw new IllegalArgumentException("Invalid Null Registration was provided!");
        }
        if (fileMetadata != null) {
            FirebirdFile firebirdFile = getFileService().createFile(file, fileMetadata);
            registration.getFinancialDisclosure().getSupportingDocumentation().add(firebirdFile);
        }
        save(registration, registration.getFinancialDisclosure().getFormType().getFormTypeEnum());
    }

    @Override
    public void uploadAndSelectAdditionalAttachment(T registration, File file, FileMetadata fileMetadata)
            throws IOException {
        FirebirdFile addedFile = fileService.addFileToProfile(registration.getProfile(), file, fileMetadata);
        selectAdditionalAttachment(registration, addedFile);
    }

    @Override
    public void selectAdditionalAttachment(T registration, FirebirdFile attachment) {
        verifyUnlocked(registration);
        if (attachment != null) {
            registration.getAdditionalAttachmentsForm().getAdditionalAttachments().add(attachment);
            save(registration);
        }
    }

    private void verifyUnlocked(T registration) {
        if (registration.isLockedForInvestigator()) {
            throw new IllegalStateException("Registration is currently locked and unmodifiable!");
        }
    }

    @Override
    public void deselectAdditionalAttachment(T registration, FirebirdFile attachment) {
        verifyUnlocked(registration);
        if (attachment != null) {
            registration.getAdditionalAttachmentsForm().getAdditionalAttachments().remove(attachment);
            save(registration);
        }
    }

    @Override
    public void sendCoordinatorCompletedRegistrationEmail(FirebirdUser coordinator, T registration) {
        String investigatorEmail = registration.getProfile().getPerson().getEmail();
        FirebirdMessage message = getCoordinatorCompletedRegistrationEmailMessage(coordinator, registration);
        emailService.sendMessage(investigatorEmail, null, null, message);
    }

    /**
     *
     * @param fileService File Service
     */
    @Inject
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     *
     * @param pdfService PDF Service
     */
    @Inject
    public void setPdfService(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     *
     * @param digitalSigningService Digital Signing Service
     */
    @Inject
    public void setDigitalSigningService(DigitalSigningService digitalSigningService) {
        this.digitalSigningService = digitalSigningService;
    }

    /**
     * @param emailService email service
     */
    @Inject
    public void setEmailService(@Named("jmsEmailService") EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * @param templateService template service
     */
    @Inject
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }

    /**
     *
     * @param certificateAuthorityManager CA Manager Service
     */
    @Inject
    public void setCertificateAuthorityManager(CertificateAuthorityManager certificateAuthorityManager) {
        this.certificateAuthorityManager = certificateAuthorityManager;
    }

    /**
     *
     * @param resources Resource Bundle
     */
    @Inject
    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }

    @Inject
    void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Inject
    void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    /**
     * @param sponsorService sponsor service
     */
    @Inject
    public void setSponsorService(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    /**
     * @return the personService
     */
    protected PersonService getPersonService() {
        return personService;
    }

    /**
     * @return the sponsorService
     */
    protected SponsorService getSponsorService() {
        return sponsorService;
    }

    /**
     * @return file service
     */
    protected FileService getFileService() {
        return fileService;
    }

    PdfService getPdfService() {
        return pdfService;
    }

    /**
     * @return the email service
     */
    protected EmailService getEmailService() {
        return emailService;
    }

    /**
     * @return the template service
     */
    protected TemplateService getTemplateService() {
        return templateService;
    }

    DigitalSigningService getDigitalSigningService() {
        return digitalSigningService;
    }

    CertificateAuthorityManager getCertificateAuthorityManager() {
        return certificateAuthorityManager;
    }

    ResourceBundle getResources() {
        return resources;
    }

    /**
     * Sends any necessary email notifications on signature and submission of a registration.
     *
     * @param registration the submitted registration.
     */
    protected abstract void sendSubmissionNotifications(T registration);

}
