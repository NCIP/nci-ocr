/**
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

import static gov.nih.nci.firebird.data.FormStatus.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AbstractSupplementalForm;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Keystore;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.ProtocolFinancialDisclosure;
import gov.nih.nci.firebird.data.ProtocolPhase;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubmittedTrainingCertificate;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.ReplacedEntityException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.pdf.PdfFormValues;
import gov.nih.nci.firebird.service.pdf.PdfService;
import gov.nih.nci.firebird.service.pdf.PdfServiceBean;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.signing.DigitalSigningAttributes;
import gov.nih.nci.firebird.service.signing.DigitalSigningException;
import gov.nih.nci.firebird.service.signing.DigitalSigningService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.user.CertificateAuthorityManager;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.GuiceTestRunner;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.itextpdf.text.DocumentException;

@RunWith(GuiceTestRunner.class)
public class AbstractBaseRegistrationServiceBeanTest {

    private AbstractBaseRegistrationServiceBean<AbstractProtocolRegistration> bean = new AbstractBaseRegistrationServiceBean<AbstractProtocolRegistration>() {

        @Override
        public Set<AbstractProtocolRegistration> getRegistrations(InvestigatorProfile profile) {
            return profile.getCurrentProtocolRegistrations();
        }

        @Override
        public FirebirdMessage getCoordinatorCompletedRegistrationEmailMessage(FirebirdUser coordinator,
                AbstractProtocolRegistration registration) {
            return message;
        }

        @Override
        protected void sendSubmissionNotifications(AbstractProtocolRegistration registration) {
            mockSponsorService.notifySubmittedRegistration(registration);
        }

        @Override
        protected void submitRegistration(AbstractProtocolRegistration registration) {
            registration.setStatus(RegistrationStatus.SUBMITTED);
        }

        @Override
        protected Class<AbstractProtocolRegistration> getRegistrationClass() {
            return AbstractProtocolRegistration.class;
        }
    };

    @Mock
    private FileService mockFileService;
    @Mock
    private CertificateAuthorityManager mockCertificateAuthorityManager;
    @Mock
    private Provider<Session> mockSessionProvider;
    @Mock
    private Session mockSession = mock(Session.class);
    @Mock
    private PersonService mockPersonService;
    @Mock
    private OrganizationService mockOrganizationService;
    @Mock
    private SponsorService mockSponsorService;
    @Mock
    private PdfService mockPdfService;
    @Mock
    private DigitalSigningService mockDigitalSigningService;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private TemplateService mockTemplateService;
    @Inject
    private FileService fileService;
    private FirebirdMessage message = new FirebirdMessage("", "");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setUpMockSessionProvider();
        bean.setPersonService(mockPersonService);
        bean.setOrganizationService(mockOrganizationService);
        bean.setResources(ResourceBundle.getBundle("resources", Locale.getDefault()));
        bean.setPdfService(mockPdfService);
        bean.setDigitalSigningService(mockDigitalSigningService);
        bean.setFileService(mockFileService);
        bean.setCertificateAuthorityManager(mockCertificateAuthorityManager);
        bean.setEmailService(mockEmailService);
        bean.setTemplateService(mockTemplateService);
        System.setProperty("registration.validation.require.nes.status.active", "true");
    }

    private void setUpMockSessionProvider() {
        bean.setSessionProvider(mockSessionProvider);
        when(mockSessionProvider.get()).thenReturn(mockSession);
    }

    @Test
    public void testGeneratePdf_Cv() throws IOException {
        configureBeanForNonMockPdfGeneration();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        generateCvPdf(registration);

        InvestigatorProfileFactory.getInstance().addCredentials(registration.getProfile());
        generateCvPdf(registration);
    }

    private void configureBeanForNonMockPdfGeneration() {
        // set up with the real PDF service and file service so we can inspect the results.
        bean.setPdfService(new PdfServiceBean());
        bean.setFileService(fileService);
    }

    private byte[] generateCvPdf(InvestigatorRegistration registration) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bean.generatePdf(registration.getCurriculumVitaeForm(), baos);
        byte[] bytes = baos.toByteArray();
        assertTrue(bytes.length > 0);
        return bytes;
    }

    @Test
    public void testGeneratePdf_CvWithNullPhone() throws IOException {
        configureBeanForNonMockPdfGeneration();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getProfile().getPerson().setPhoneNumber(null);
        generateCvPdf(registration);
    }

    @Test
    public void testGeneratePdf_CvWithNullTrainingCertificateIssuer() throws IOException, CredentialAlreadyExistsException {
        configureBeanForNonMockPdfGeneration();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate();
        certificate.setIssuer(null);
        registration.getProfile().addCredential(certificate);
        generateCvPdf(registration);
    }

    @Test
    public void testGeneratePdf_CvWithNullPrimaryOrganization() throws IOException, AssociationAlreadyExistsException {
        configureBeanForNonMockPdfGeneration();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getProfile().setPrimaryOrganization(null);
        generateCvPdf(registration);
    }

    @Test
    public void testGeneratePdf_Sidf() throws IOException {
        configureBeanForNonMockPdfGeneration();
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        generateSidfPdf(registration);
        InvestigatorProfileFactory.getInstance().addCredentials(registration.getProfile());
        generateSidfPdf(registration);
    }

    private void generateSidfPdf(AnnualRegistration registration) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bean.generatePdf(registration.getSupplementalInvestigatorDataForm(), baos);
        assertTrue(baos.toByteArray().length > 0);
    }

    @Test
    public void testGeneratePdf_SidfWithNullTrainingCertificateIssuer() throws IOException, CredentialAlreadyExistsException {
        configureBeanForNonMockPdfGeneration();
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate();
        certificate.setIssuer(null);
        registration.getProfile().addCredential(certificate);
        generateSidfPdf(registration);
    }

    @Test
    public void testGeneratePdf_SidfWithNullPrimaryOrganization() throws IOException, AssociationAlreadyExistsException {
        configureBeanForNonMockPdfGeneration();
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        registration.getProfile().setPrimaryOrganization(null);
        generateSidfPdf(registration);
    }

    @Test
    public void testGeneratePdf_Empty1572() throws IOException, AssociationAlreadyExistsException {
        configureBeanForNonMockPdfGeneration();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        generate1572Pdf(registration);
    }

    private byte[] generate1572Pdf(InvestigatorRegistration registration) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bean.generatePdf(registration.getForm1572(), baos);
        byte[] bytes = baos.toByteArray();
        assertTrue(bytes.length > 0);
        return bytes;
    }

    @Test
    public void testGeneratePdf_1572() throws IOException, AssociationAlreadyExistsException {
        configureBeanForNonMockPdfGeneration();

        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProtocol().setPhase(ProtocolPhase.PHASE_2);
        generate1572Pdf(registration);

        registration.getProtocol().setPhase(ProtocolPhase.PHASE_1);
        generate1572Pdf(registration);
    }

    @Test
    public void testGeneratePdf_1572WithNullPrimaryOrganization() throws IOException, AssociationAlreadyExistsException {
        configureBeanForNonMockPdfGeneration();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProfile().setPrimaryOrganization(null);
        generate1572Pdf(registration);
    }

    @Test
    public void testGeneratePdf_FinancialDisclosure() throws IOException, DocumentException {
        configureBeanForNonMockPdfGeneration();

        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setProfile(new InvestigatorProfile());
        registration.getProfile().setPerson(PersonFactory.getInstance().create());
        generateFinancialDisclosurePdf(registration);

        registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        generateFinancialDisclosurePdf(registration);
        setKnownValues(registration);
        setAllFinancialDisclosureAnswers(registration.getFinancialDisclosure(), null);
        generateFinancialDisclosurePdf(registration);

        setAllFinancialDisclosureAnswers(registration.getFinancialDisclosure(), false);
        generateFinancialDisclosurePdf(registration);

        setAllFinancialDisclosureAnswers(registration.getFinancialDisclosure(), true);
        generateFinancialDisclosurePdf(registration);

        registration.getFinancialDisclosure().setNoFinancialInterest(true);
        generateFinancialDisclosurePdf(registration);

        FirebirdFile supportingInfoDoc = FirebirdFileFactory.getInstance().create(
                "/gov/nih/nci/firebird/service/registration/additional_information_test.doc.gz");
        registration.getFinancialDisclosure().getSupportingDocumentation().add(supportingInfoDoc);
        generateFinancialDisclosurePdf(registration);
    }

    private byte[] generateFinancialDisclosurePdf(InvestigatorRegistration registration) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bean.generatePdf(registration.getFinancialDisclosure(), baos);
        byte[] bytes = baos.toByteArray();
        assertTrue(bytes.length > 0);
        return bytes;
    }

    private void setKnownValues(InvestigatorRegistration registration) {
        registration.getProfile().getPerson().setPrefix("Dr.");
        registration.getProfile().getPerson().setFirstName("John");
        registration.getProfile().getPerson().setMiddleName("Q.");
        registration.getProfile().getPerson().setLastName("Doe");
        registration.getProfile().getPerson().setSuffix(null);
        registration.getProfile().getPrimaryOrganization().getOrganization().setName("National Cancer Institute");
        registration.getProtocol().setProtocolTitle("Protocol Title");
    }

    private void setAllFinancialDisclosureAnswers(ProtocolFinancialDisclosure financialDisclosure, Boolean value) {
        financialDisclosure.setEquityInSponsor(value);
        financialDisclosure.setFinancialInterest(value);
        financialDisclosure.setMonetaryGain(value);
        financialDisclosure.setOtherSponsorPayments(value);
    }

    @Test
    public void testGetKeystore() throws Exception {
        FirebirdUser user = new FirebirdUser();
        String password = "";
        Keystore keystore = new Keystore();
        when(mockCertificateAuthorityManager.getOrCreateUserKey(user, password)).thenReturn(keystore);
        assertSame(keystore, bean.getKeystore(user, password));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalStateException.class)
    public void testGetKeystore_IOException() throws Exception {
        FirebirdUser user = new FirebirdUser();
        String password = "";
        when(mockCertificateAuthorityManager.getOrCreateUserKey(user, password)).thenThrow(IOException.class);
        bean.getKeystore(user, password);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalStateException.class)
    public void testGetKeystore_DigitalSigningException() throws Exception {
        FirebirdUser user = new FirebirdUser();
        String password = "";
        when(mockCertificateAuthorityManager.getOrCreateUserKey(user, password)).thenThrow(
                DigitalSigningException.class);
        bean.getKeystore(user, password);
    }

    @Test
    public void testValidateFormStatuses() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        for (AbstractRegistrationForm form : registration.getForms()) {
            form.setFormStatus(FormStatus.COMPLETED);
        }
        bean.validateFormStatuses(registration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateFormStatuses_NotSubmittable() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getForm1572().setFormStatus(FormStatus.INCOMPLETE);
        bean.validateFormStatuses(registration);
    }

    @Test
    public void testSetReturnedOrRevisedRegistrationsFormStatusesToRevised() {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        setupAndCreateReturnedRegistrations(profile);
        bean.setReturnedOrRevisedRegistrationsFormStatusesToRevised(profile, FormTypeEnum.FORM_1572);
        for (AbstractProtocolRegistration registration : bean.getReturnedOrRevisedRegistrations(profile)) {
            assertEquals(FormStatus.REVISED, registration.getForm1572().getFormStatus());
        }
    }

    private Set<InvestigatorRegistration> setupAndCreateReturnedRegistrations(InvestigatorProfile profile) {
        InvestigatorRegistration submittedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration(profile);
        InvestigatorRegistration returnedRegistration1 = RegistrationFactory.getInstance()
                .createInvestigatorRegistration(profile);
        InvestigatorRegistration returnedRegistration2 = RegistrationFactory.getInstance()
                .createInvestigatorRegistration(profile);
        submittedRegistration.setStatus(RegistrationStatus.SUBMITTED);
        returnedRegistration1.setStatus(RegistrationStatus.RETURNED);
        returnedRegistration2.setStatus(RegistrationStatus.RETURNED);
        returnedRegistration1.getForm1572().setFormStatus(REJECTED);
        returnedRegistration2.getForm1572().setFormStatus(ACCEPTED);
        profile.addRegistration(submittedRegistration);
        profile.addRegistration(returnedRegistration1);
        profile.addRegistration(returnedRegistration2);
        return Sets.newHashSet(returnedRegistration1, returnedRegistration2);
    }

    @Test
    public void testSetRegistrationFormStatusesToRevisedIfReviewed_Reviewed() {
        Set<AbstractProtocolRegistration> registrations = Sets.newHashSet();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getCurriculumVitaeForm().setFormStatus(ACCEPTED);
        registrations.add(registration);
        bean.setRegistrationFormStatusesToRevisedIfReviewed(registrations, FormTypeEnum.CV);
        assertEquals(FormStatus.REVISED, registration.getCurriculumVitaeForm().getFormStatus());
    }

    @Test
    public void testSetRegistrationFormStatusesToRevisedIfReviewed_NotReviewed() {
        Set<AbstractProtocolRegistration> registrations = Sets.newHashSet();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getCurriculumVitaeForm().setFormStatus(COMPLETED);
        registrations.add(registration);
        bean.setRegistrationFormStatusesToRevisedIfReviewed(registrations, FormTypeEnum.CV);
        assertEquals(FormStatus.COMPLETED, registration.getCurriculumVitaeForm().getFormStatus());
    }

    @Test
    public void testGetReturnedOrRevisedRegistrations() {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        Set<InvestigatorRegistration> expectedRegistrations = setupAndCreateReturnedRegistrations(profile);
        Set<AbstractProtocolRegistration> actualRegistrations = bean.getReturnedOrRevisedRegistrations(profile);
        assertEquals(expectedRegistrations, actualRegistrations);
    }

    @Test
    public void testSave_Returned() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.RETURNED);
        registration.getForm1572().setFormStatus(REJECTED);
        registration.getCurriculumVitaeForm().setFormStatus(ACCEPTED);
        FormStatus financialDisclosureStatus = registration.getFinancialDisclosure().getFormStatus();
        FormStatus humanResearchStatus = registration.getHumanResearchCertificateForm().getFormStatus();
        bean.save(registration, FormTypeEnum.CV, FormTypeEnum.FORM_1572);
        verify(mockSession).saveOrUpdate(registration);
        assertEquals(FormStatus.REVISED, registration.getForm1572().getFormStatus());
        assertEquals(FormStatus.REVISED, registration.getCurriculumVitaeForm().getFormStatus());
        assertEquals(financialDisclosureStatus, registration.getFinancialDisclosure().getFormStatus());
        assertEquals(humanResearchStatus, registration.getHumanResearchCertificateForm().getFormStatus());
    }

    @Test
    public void testSave_NotReturned() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.APPROVED);
        FormStatus financialDisclosureStatus = registration.getFinancialDisclosure().getFormStatus();
        FormStatus humanResearchStatus = registration.getHumanResearchCertificateForm().getFormStatus();
        FormStatus curriculumVitaeStatus = registration.getCurriculumVitaeForm().getFormStatus();
        FormStatus form1572Status = registration.getForm1572().getFormStatus();
        bean.save(registration, FormTypeEnum.CV, FormTypeEnum.FORM_1572);
        verify(mockSession).saveOrUpdate(registration);
        assertEquals(form1572Status, registration.getForm1572().getFormStatus());
        assertEquals(curriculumVitaeStatus, registration.getCurriculumVitaeForm().getFormStatus());
        assertEquals(financialDisclosureStatus, registration.getFinancialDisclosure().getFormStatus());
        assertEquals(humanResearchStatus, registration.getHumanResearchCertificateForm().getFormStatus());
    }

    @Test
    public void testSave_RegistrationCollection() {
        Set<AbstractProtocolRegistration> registrations = Sets.newHashSet();
        AbstractProtocolRegistration registration1 = RegistrationFactory.getInstance().createInvestigatorRegistration();
        AbstractProtocolRegistration registration2 = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registrations.add(registration1);
        registrations.add(registration2);
        bean.save(registrations);
        verify(mockSession).saveOrUpdate(registration1);
        verify(mockSession).saveOrUpdate(registration2);
    }

    @Test
    public void testAddFinancialDisclosureFile() throws IOException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        bean.addFinancialDisclosureFile(registration, null, null);
        verifyZeroInteractions(mockFileService);

        bean.addFinancialDisclosureFile(registration, mock(File.class), new FileMetadata(null, null));
        verify(mockFileService).createFile(any(File.class), any(FileMetadata.class));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFinancialDisclosureFileFailNoReg() throws IOException {
        bean.addFinancialDisclosureFile(null, mock(File.class), new FileMetadata(null, null));
    }

    @Test(expected = IOException.class)
    public void testAddFinancialDisclosureFileFailSaveFile() throws IOException {
        when(mockFileService.createFile(any(File.class), any(FileMetadata.class))).thenThrow(new IOException());
        bean.addFinancialDisclosureFile(new InvestigatorRegistration(), mock(File.class), new FileMetadata(null, null));
    }

    @Test
    public void testUploadAndSelectAdditionalAttachment() throws IOException {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                profile);
        FirebirdFile testFile = FirebirdFileFactory.getInstanceWithId().create();
        File fileToUpload = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
        fileToUpload = new File(fileToUpload, getClass().getName().replace('.', '/') + ".class");
        FileMetadata metaData = new FileMetadata(null, null);

        when(mockFileService.addFileToProfile(eq(profile), any(File.class), any(FileMetadata.class))).thenReturn(
                testFile);

        bean.uploadAndSelectAdditionalAttachment(registration, fileToUpload, metaData);
        verify(mockFileService).addFileToProfile(eq(profile), any(File.class), any(FileMetadata.class));
        assertEquals(1, registration.getAdditionalAttachmentsForm().getAdditionalAttachments().size());
        verify(mockSession).saveOrUpdate(registration);
    }

    @Test
    public void testSelectAdditionalAttachment_NoFile() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        bean.selectAdditionalAttachment(registration, null);
        assertTrue(registration.getAdditionalAttachmentsForm().getAdditionalAttachments().isEmpty());
        verifyZeroInteractions(mockSession);
    }

    @Test
    public void testSelectAdditionalAttachment_ValidFile() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        FirebirdFile testFile = FirebirdFileFactory.getInstanceWithId().create();
        bean.selectAdditionalAttachment(registration, testFile);
        assertEquals(1, registration.getAdditionalAttachmentsForm().getAdditionalAttachments().size());
        verify(mockSession).saveOrUpdate(registration);
    }

    @Test(expected = IllegalStateException.class)
    public void testSelectAdditionalAttachment_LockedRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        bean.selectAdditionalAttachment(registration, null);
    }

    @Test
    public void testDeselectAdditionalAttachment_NoFile() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getAdditionalAttachmentsForm().getAdditionalAttachments().add(new FirebirdFile());
        bean.deselectAdditionalAttachment(registration, null);
        assertEquals(1, registration.getAdditionalAttachmentsForm().getAdditionalAttachments().size());
        verifyZeroInteractions(mockSession);
    }

    @Test
    public void testDeselectAdditionalAttachment_ValidFile() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        FirebirdFile testFile = FirebirdFileFactory.getInstanceWithId().create();
        registration.getAdditionalAttachmentsForm().getAdditionalAttachments().add(testFile);
        bean.deselectAdditionalAttachment(registration, testFile);
        assertTrue(registration.getAdditionalAttachmentsForm().getAdditionalAttachments().isEmpty());
        verify(mockSession).saveOrUpdate(registration);
    }

    @Test
    public void testDeselectAdditionalAttachment_NotPreviouslySelected() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getAdditionalAttachmentsForm().getAdditionalAttachments()
                .add(FirebirdFileFactory.getInstance().create());
        bean.deselectAdditionalAttachment(registration, FirebirdFileFactory.getInstance().create());
        assertEquals(1, registration.getAdditionalAttachmentsForm().getAdditionalAttachments().size());
        verify(mockSession).saveOrUpdate(registration);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeselectAdditionalAttachment_LockedRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        FirebirdFile testFile = FirebirdFileFactory.getInstanceWithId().create();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        bean.deselectAdditionalAttachment(registration, testFile);
    }

    @Test
    public void testPrepareForSubmission_Valid() throws ValidationException, AssociationAlreadyExistsException,
            CredentialAlreadyExistsException, UnavailableEntityException, ReplacedEntityException {
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate(
                CertificateType.HUMAN_RESEARCH_CERTIFICATE);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProfile().addCredential(certificate);
        registration.getHumanResearchCertificateForm().selectCertificate(certificate);
        registration.getProfile().addCredential(CredentialFactory.getInstance().createLicense());

        bean.prepareForSubmission(registration);
        assertFalse(registration.getOrganizations().isEmpty());
        assertFalse(registration.getPersons().isEmpty());
        verify(mockOrganizationService, times(registration.getOrganizations().size())).refreshFromNes(
                any(Organization.class));
        verify(mockPersonService, times(registration.getPersons().size())).refreshFromNes(any(Person.class));
        assertEquals(RegistrationStatus.COMPLETED, registration.getStatus());
        assertEquals(FormStatus.COMPLETED, registration.getForm1572().getFormStatus());
    }

    @Test
    public void testPrepareForSubmission_Returned() throws ValidationException, AssociationAlreadyExistsException,
            CredentialAlreadyExistsException, UnavailableEntityException, ReplacedEntityException {
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate(
                CertificateType.HUMAN_RESEARCH_CERTIFICATE);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProfile().addCredential(certificate);
        registration.getHumanResearchCertificateForm().selectCertificate(certificate);
        registration.getProfile().addCredential(CredentialFactory.getInstance().createLicense());
        registration.setStatus(RegistrationStatus.SUBMITTED); // populate lastSubmissionDate
        registration.setStatus(RegistrationStatus.RETURNED);
        registration.getForm1572().setFormStatus(FormStatus.ACCEPTED);

        bean.prepareForSubmission(registration);
        assertFalse(registration.getOrganizations().isEmpty());
        assertFalse(registration.getPersons().isEmpty());
        verify(mockOrganizationService, times(registration.getOrganizations().size())).refreshFromNes(
                any(Organization.class));
        verify(mockPersonService, times(registration.getPersons().size())).refreshFromNes(any(Person.class));
        assertEquals(RegistrationStatus.RETURNED, registration.getStatus());
        assertEquals(FormStatus.ACCEPTED, registration.getForm1572().getFormStatus());
    }

    @Test
    public void testPrepareForSubmission_InactiveNesPerson() throws ValidationException,
            AssociationAlreadyExistsException, CredentialAlreadyExistsException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProfile().addCredential(
                CredentialFactory.getInstance().createCertificate(CertificateType.HUMAN_RESEARCH_CERTIFICATE));
        registration.getProfile().addCredential(CredentialFactory.getInstance().createLicense());
        registration.getProfile().getPerson().setNesStatus(CurationStatus.INACTIVE);
        try {
            bean.prepareForSubmission(registration);
            fail("A Validation Exception should have been thrown.");
        } catch (ValidationException e) {
            assertEquals(RegistrationStatus.INCOMPLETE, registration.getStatus());
            assertEquals(FormStatus.INCOMPLETE, registration.getForm1572().getFormStatus());
        }
    }

    @Test
    public void testPrepareForSubmission_PreNesCurationPerson() throws ValidationException,
            AssociationAlreadyExistsException, CredentialAlreadyExistsException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProfile().addCredential(
                CredentialFactory.getInstance().createCertificate(CertificateType.HUMAN_RESEARCH_CERTIFICATE));
        registration.getProfile().addCredential(CredentialFactory.getInstance().createLicense());
        registration.getProfile().getPerson().setNesStatus(CurationStatus.PRE_NES_VALIDATION);
        try {
            bean.prepareForSubmission(registration);
            fail("A Validation Exception should have been thrown.");
        } catch (ValidationException e) {
            assertEquals(RegistrationStatus.INCOMPLETE, registration.getStatus());
            assertEquals(FormStatus.INCOMPLETE, registration.getForm1572().getFormStatus());
        }
    }

    @Test
    public void testPrepareForSubmission_CuratedPersonUncuratedOrg() throws ValidationException,
            AssociationAlreadyExistsException, CredentialAlreadyExistsException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProfile().addCredential(
                CredentialFactory.getInstance().createCertificate(CertificateType.HUMAN_RESEARCH_CERTIFICATE));
        registration.getProfile().addCredential(CredentialFactory.getInstance().createLicense());
        registration.getProfile().getPrimaryOrganization().getOrganization()
                .setNesStatus(CurationStatus.PRE_NES_VALIDATION);

        try {
            bean.prepareForSubmission(registration);
            fail("A Validation Exception should have been thrown.");
        } catch (ValidationException e) {
            assertEquals(RegistrationStatus.INCOMPLETE, registration.getStatus());
            assertEquals(FormStatus.INCOMPLETE, registration.getForm1572().getFormStatus());
        }
    }

    @Test
    public void testSignAndSubmit() throws Exception {
        FirebirdUser user = mock(FirebirdUser.class);
        String password = "password";
        Keystore keystore = mock(Keystore.class);
        when(mockCertificateAuthorityManager.getOrCreateUserKey(user, password)).thenReturn(keystore);

        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        InvestigatorProfileFactory.getInstance().addCredentials(registration.getProfile());
        registration.getHumanResearchCertificateForm().selectCertificate(
                registration.getProfile().getCredentials(TrainingCertificate.class).iterator().next());
        setRegistrationToCompleted(registration);
        bean.signAndSubmit(registration, user, password);
        assertFalse(registration.getHumanResearchCertificateForm().getCertificates().isEmpty());
        assertTrue(registration.getHumanResearchCertificateForm().getCertificates().iterator().next() instanceof SubmittedTrainingCertificate);
        assertEquals(RegistrationStatus.SUBMITTED, registration.getStatus());
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (form instanceof AbstractSupplementalForm) {
                assertEquals(NOT_APPLICABLE, form.getFormStatus());
            } else {
                assertEquals(FormStatus.SUBMITTED, form.getFormStatus());
            }
        }
        verifyServiceCalls(user, password);
    }

    @SuppressWarnings("unchecked")
    private void verifyServiceCalls(FirebirdUser user, String password) throws Exception {
        verify(mockCertificateAuthorityManager).getOrCreateUserKey(user, password);
        verify(mockPdfService, times(3)).flattenPdf(any(InputStream.class), any(OutputStream.class));
        verify(mockPdfService, times(2)).fillOutForm(any(InputStream.class), any(OutputStream.class),
                any(PdfFormValues.class));
        verify(mockDigitalSigningService).signPdf(any(InputStream.class), any(OutputStream.class),
                any(DigitalSigningAttributes.class));
        verify(mockSponsorService).notifySubmittedRegistration(any(AbstractProtocolRegistration.class));
    }

    private void setRegistrationToCompleted(AbstractProtocolRegistration registration) {
        registration.setStatus(RegistrationStatus.COMPLETED);
        for (AbstractRegistrationForm form : registration.getForms()) {
            form.setFormStatus(FormStatus.COMPLETED);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSignAndSubmitComplete() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        String password = "password";
        FirebirdUser user = mock(FirebirdUser.class);
        bean.signAndSubmit(registration, user, password);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSignAndSubmit_InvalidFormStatus() throws IOException, DigitalSigningException {
        FirebirdUser user = mock(FirebirdUser.class);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        setRegistrationToCompleted(registration);
        registration.getForm1572().setFormStatus(FormStatus.INCOMPLETE);
        bean.signAndSubmit(registration, user, "");
    }

    @Test
    public void testSignAndSubmit_Returned() throws Exception {
        FirebirdUser user = mock(FirebirdUser.class);
        String password = "password";
        Keystore keystore = mock(Keystore.class);
        when(mockCertificateAuthorityManager.getOrCreateUserKey(user, password)).thenReturn(keystore);

        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        InvestigatorProfileFactory.getInstance().addCredentials(registration.getProfile());
        registration.getHumanResearchCertificateForm().selectCertificate(
                registration.getProfile().getCredentials(TrainingCertificate.class).iterator().next());
        registration.setStatus(RegistrationStatus.SUBMITTED); // populate lastSubmissionDate
        registration.setStatus(RegistrationStatus.RETURNED);
        registration.getForm1572().setFormStatus(FormStatus.REVISED);
        registration.getCurriculumVitaeForm().setFormStatus(FormStatus.ACCEPTED);
        registration.getFinancialDisclosure().setFormStatus(FormStatus.ACCEPTED);
        registration.getHumanResearchCertificateForm().setFormStatus(FormStatus.REVISED);
        bean.signAndSubmit(registration, user, password);
        assertFalse(registration.getHumanResearchCertificateForm().getCertificates().isEmpty());
        assertTrue(registration.getHumanResearchCertificateForm().getCertificates().iterator().next() instanceof SubmittedTrainingCertificate);
        assertEquals(RegistrationStatus.SUBMITTED, registration.getStatus());
        assertEquals(FormStatus.REVISED, registration.getForm1572().getFormStatus());
        assertEquals(FormStatus.ACCEPTED, registration.getCurriculumVitaeForm().getFormStatus());
        assertEquals(FormStatus.ACCEPTED, registration.getFinancialDisclosure().getFormStatus());
        assertEquals(FormStatus.REVISED, registration.getHumanResearchCertificateForm().getFormStatus());
        verifyServiceCalls(user, password);
    }

    @Test
    public void testSendCoordinatorCompletedRegistrationEmail() {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                profile);
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        bean.sendCoordinatorCompletedRegistrationEmail(coordinator, registration);
        String investigatorEmail = profile.getPerson().getEmail();
        verify(mockEmailService).sendMessage(investigatorEmail, null, null, message);
    }

    @Test
    public void testCheckUnReviewedAndUnRevisedFormCompletionStatus() throws Exception {
        AbstractProtocolRegistration mockRegistration = mock(AbstractProtocolRegistration.class);
        bean.checkUnReviewedAndUnRevisedFormCompletionStatus(mockRegistration);
        verify(mockRegistration).validateUnReviewedAndUnRevisedForms(any(ResourceBundle.class));
        verify(mockSession).saveOrUpdate(mockRegistration);
    }

    @Test
    public void testGetByStatus() {
        Query mockQuery = mock(Query.class);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        bean.getByStatus(RegistrationStatus.IN_PROGRESS);
        verify(mockQuery).setParameter("status", RegistrationStatus.IN_PROGRESS);
        verify(mockQuery).list();
    }

}
