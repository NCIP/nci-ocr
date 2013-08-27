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
package gov.nih.nci.firebird.service.annual.registration;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.AnnualRegistrationConfiguration;
import gov.nih.nci.firebird.data.AnnualRegistrationForm1572;
import gov.nih.nci.firebird.data.AnnualRegistrationType;
import gov.nih.nci.firebird.data.CtepFinancialDisclosure;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Keystore;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.pdf.PdfFormValues;
import gov.nih.nci.firebird.service.pdf.PdfService;
import gov.nih.nci.firebird.service.pdf.PdfServiceBean;
import gov.nih.nci.firebird.service.signing.DigitalSigningAttributes;
import gov.nih.nci.firebird.service.signing.DigitalSigningService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.user.CertificateAuthorityManager;
import gov.nih.nci.firebird.test.AnnualRegistrationConfigurationFactory;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.GuiceTestRunner;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import gov.nih.nci.firebird.test.ValueGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

@RunWith(GuiceTestRunner.class)
public class AnnualRegistrationServiceBeanTest {

    private AnnualRegistrationServiceBean bean = spy(new AnnualRegistrationServiceBean());

    @Mock
    private AnnualRegistrationConfigurationService mockConfigurationService;
    @Mock
    private Provider<Session> mockSessionProvider;
    @Mock
    private Session mockSession;
    @Mock
    private CertificateAuthorityManager mockCertificateAuthorityManager;
    @Mock
    private DigitalSigningService mockDigitalSigningService;
    @Mock
    private FileService mockFileService;
    @Mock
    private PdfService mockPdfService;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private TemplateService mockTemplateService;
    @Mock
    private SponsorService mockSponsorService;

    @Inject
    FileService fileService;
    @Inject
    @Named("annual.registration.due.first.notification.days")
    private int daysBeforeDueDateToSendFirstNotification;
    @Inject
    @Named("annual.registration.due.second.notification.days")
    private int daysBeforeDueDateToSendSecondNotification;
    @Inject
    @Named("annual.registration.renewal.based.on.due.date.days")
    private int daysAfterDueDateToUseAsBasisForRenewalDate;

    private InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();

    FirebirdFile form1572File = FirebirdFileFactory.getInstance().create("/files/ctep_form_fda_1572.pdf.gz");
    FirebirdFile financialDisclosureFile = FirebirdFileFactory.getInstance().create(
            "/files/ctep_financial_disclosure_form.pdf.gz");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bean.setConfigurationService(mockConfigurationService);
        bean.setSessionProvider(mockSessionProvider);
        bean.setResources(ResourceBundle.getBundle("resources", Locale.getDefault()));
        bean.setCertificateAuthorityManager(mockCertificateAuthorityManager);
        bean.setDigitalSigningService(mockDigitalSigningService);
        bean.setFileService(fileService);
        bean.setPdfService(new PdfServiceBean());
        bean.setSponsorService(mockSponsorService);
        bean.setTemplateService(mockTemplateService);
        bean.setEmailService(mockEmailService);
        bean.setDaysBeforeDueDateToSendFirstNotification(daysBeforeDueDateToSendFirstNotification);
        bean.setDaysBeforeDueDateToSendSecondNotification(daysBeforeDueDateToSendSecondNotification);
        bean.setDaysAfterDueDateToUseAsBasisForRenewalDate(daysAfterDueDateToUseAsBasisForRenewalDate);
        when(mockSessionProvider.get()).thenReturn(mockSession);
    }

    @Test
    public void testCreateRenewalReminderEmailToCoordinatorInitial() {
        AnnualRegistrationConfiguration configuration = AnnualRegistrationConfigurationFactory.getInstance().create();
        when(mockConfigurationService.getCurrentConfiguration()).thenReturn(configuration);

        AnnualRegistration registration = bean.createInitial(profile);
        assertEquals(AnnualRegistrationType.INITIAL, registration.getAnnualRegistrationType());
        assertEquals(RegistrationStatus.NOT_STARTED, registration.getStatus());
        assertEquals(configuration, registration.getConfiguration());
        assertEquals(profile, registration.getProfile());
        assertTrue(profile.getAnnualRegistrations().contains(registration));
        assertEquals(configuration.getFormSetConfiguration().getAssociatedFormTypes().size(), registration.getForms()
                .size());
        verify(mockSession).saveOrUpdate(registration);
    }

    @Test
    public void testCreateRenewalReminderEmailToCoordinatorRenewal() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();
        when(mockConfigurationService.getCurrentConfiguration()).thenReturn(registration.getConfiguration());
        registration.setStatus(RegistrationStatus.COMPLETED);
        profile.addRegistration(registration);
        AnnualRegistration renewalRegistration = bean.createRenewal(registration);
        assertEquals(AnnualRegistrationType.RENEWAL, renewalRegistration.getAnnualRegistrationType());
        assertEquals(RegistrationStatus.NOT_STARTED, renewalRegistration.getStatus());
        assertEquals(registration.getConfiguration(), renewalRegistration.getConfiguration());
        assertEquals(registration.getProfile(), renewalRegistration.getProfile());
        assertTrue(registration.getProfile().getAnnualRegistrations().contains(renewalRegistration));
        assertEquals(registration.getFormConfiguration(), renewalRegistration.getFormConfiguration());
        verify(mockSession).saveOrUpdate(renewalRegistration);
    }

    @Test
    public void testForm1572Pdf() throws IOException {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();

        AnnualRegistrationForm1572 form = registration.getForm1572();
        form.getIrbs().add(OrganizationFactory.getInstance().create());
        form.getLabs().add(OrganizationFactory.getInstance().create());
        form.getPracticeSites().add(OrganizationFactory.getInstance().create());
        form.setPhaseOne(true);
        form.getFormType().setTemplate(form1572File);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bean.generatePdf(form, baos);
        byte[] bytes = baos.toByteArray();
        assertTrue(bytes.length > 0);

        FileUtils.writeByteArrayToFile(new File(getTestClassesDirectory() + "Annual_Form1572.pdf"), bytes);
    }

    @Test
    public void testForm1572PdfWithInvestigators() throws IOException {
        SubInvestigatorRegistration subinvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        profile.addRegistration(subinvestigatorRegistration);

        AnnualRegistrationConfiguration configuration = AnnualRegistrationConfigurationFactory.getInstance().create();
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create(profile, configuration);

        AnnualRegistrationForm1572 form = registration.getForm1572();
        form.getIrbs().add(OrganizationFactory.getInstance().create());
        form.getLabs().add(OrganizationFactory.getInstance().create());
        form.getPracticeSites().add(OrganizationFactory.getInstance().create());

        form.getFormType().setTemplate(form1572File);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bean.generatePdf(registration.getForm1572(), baos);
        byte[] bytes = baos.toByteArray();
        assertTrue(bytes.length > 0);

        FileUtils.writeByteArrayToFile(new File(getTestClassesDirectory() + "Annual_Form1572_With_Investigators.pdf"),
                bytes);
    }

    @Test
    public void testFinancialDisclosurePdf() throws IOException {
        generateFinancialDisclosureFormPdf(AnnualRegistrationFactory.getInstanceWithId().create(),
                "ctep_financial_disclosure_form.pdf");
    }

    @Test
    public void testFinancialDisclosurePdfWithAttachments() throws IOException {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();
        AnnualRegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);

        generateFinancialDisclosureFormPdf(registration, "ctep_financial_disclosure_form_with_attachments.pdf");
    }

    @Test
    public void testFinancialDisclosurePdfMultiLinePharma() throws IOException {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();
        AnnualRegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
        do {
            registration.getFinancialDisclosure().getPharmaceuticalCompanies()
                    .add(OrganizationFactory.getInstance().create());
        } while (registration.getFinancialDisclosure().getPharmaceuticalCompanies().size() < 20);

        generateFinancialDisclosureFormPdf(registration, "ctep_financial_disclosure_form_multiline_pharma.pdf");
    }

    private void generateFinancialDisclosureFormPdf(AnnualRegistration registration, String fileName)
            throws IOException {

        CtepFinancialDisclosure financialDisclosureForm = registration.getFinancialDisclosure();
        financialDisclosureForm.getFormType().setTemplate(financialDisclosureFile);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bean.generatePdf(financialDisclosureForm, baos);
        byte[] bytes = baos.toByteArray();
        assertTrue(bytes.length > 0);

        FileUtils.writeByteArrayToFile(new File(getTestClassesDirectory() + fileName), bytes);

        AnnualRegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSignAndSubmit() throws Exception {
        bean.setFileService(mockFileService);
        bean.setPdfService(mockPdfService);
        InputStream templateFile = fileService.readFile(form1572File);

        when(mockFileService.readFile(form1572File)).thenReturn(templateFile);

        FirebirdUser user = mock(FirebirdUser.class);
        String password = "password";
        Keystore keystore = mock(Keystore.class);
        when(mockCertificateAuthorityManager.getOrCreateUserKey(user, password)).thenReturn(keystore);

        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();
        registration.getForm1572().getFormType().setSigningField("signature_field");
        registration.getForm1572().getFormType().setTemplate(form1572File);

        for (AbstractRegistrationForm form : registration.getForms()) {
            form.setFormStatus(FormStatus.ACCEPTED);
        }

        bean.signAndSubmit(registration, user, password);

        verify(mockCertificateAuthorityManager).getOrCreateUserKey(user, password);
        verify(mockDigitalSigningService).signPdf(any(InputStream.class), any(OutputStream.class),
                any(DigitalSigningAttributes.class));
        verify(mockPdfService, times(3)).flattenPdf(any(InputStream.class), any(OutputStream.class));
        verify(mockPdfService, times(2)).fillOutForm(any(InputStream.class), any(OutputStream.class), any(PdfFormValues.class));
    }

    protected String getTestClassesDirectory() {
        return AnnualRegistrationServiceBeanTest.class.getResource("/").getPath();
    }

    @Test
    public void testGetRegistrations() {
        AnnualRegistration annualRegistration = AnnualRegistrationFactory.getInstance().create();
        InvestigatorProfile profile = annualRegistration.getProfile();
        RegistrationFactory.getInstance().createInvestigatorRegistration(profile);
        assertEquals(Sets.newHashSet(annualRegistration), bean.getRegistrations(profile));
    }

    @Test
    public void testSendSubmissionNotifications() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        Person registrationCoordinator = addRegistrationCoordinator(registration);
        String notificationAddress1 = ValueGenerator.getUniqueEmailAddress();
        String notificationAddress2 = ValueGenerator.getUniqueEmailAddress();
        registration.getNotificationEmailAddresses().add(notificationAddress1);
        registration.getNotificationEmailAddresses().add(notificationAddress2);
        bean.sendSubmissionNotifications(registration);

        String investigatorEmailAddress = registration.getProfile().getPerson().getEmail();
        verify(mockSponsorService).notifySubmittedRegistration(registration);
        verify(mockEmailService).sendMessage(eq(notificationAddress1), anyCollectionOf(String.class),
                eq(investigatorEmailAddress), any(FirebirdMessage.class));
        verify(mockEmailService).sendMessage(eq(notificationAddress2), anyCollectionOf(String.class),
                eq(investigatorEmailAddress), any(FirebirdMessage.class));
        verify(mockEmailService).sendMessage(eq(registrationCoordinator.getEmail()), anyCollectionOf(String.class),
                eq(investigatorEmailAddress), any(FirebirdMessage.class));
    }

    private Person addRegistrationCoordinator(AnnualRegistration registration) {
        FirebirdUser coordinatorUser = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        ManagedInvestigator managedInvestigator = coordinatorUser.getRegistrationCoordinatorRole()
                .addManagedInvestigator(registration.getProfile());
        managedInvestigator.setStatus(ManagedInvestigatorStatus.APPROVED);
        return coordinatorUser.getPerson();
    }

    @Test
    public void testGetCoordinatorCompletedRegistrationEmailMessage() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        bean.getCoordinatorCompletedRegistrationEmailMessage(coordinator, registration);
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.ANNUAL_REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK,
                FirebirdConstants.ANNUAL_REGISTRATION_URL_PATH_WITH_ID_PARAM + registration.getId());
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION_COORDINATOR, coordinator.getPerson());
        verify(mockTemplateService).generateMessage(
                FirebirdMessageTemplate.COORDINATOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL, parameterValues);
    }

    @Test
    public void testSendRenewalReminders_InvestigatorAndCoordinator() {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(profile)
                .setStatus(ManagedInvestigatorStatus.APPROVED);
        AnnualRegistration registration1 = AnnualRegistrationFactory.getInstance().create(profile,
                AnnualRegistrationConfigurationFactory.getInstance().create());
        AnnualRegistration registration2 = AnnualRegistrationFactory.getInstance().create(profile,
                AnnualRegistrationConfigurationFactory.getInstance().create());
        setUpQueryToReturnRegistrations(registration1, registration2);
        bean.sendRenewalReminders();
        verify(mockEmailService, times(2)).sendMessage(eq(profile.getPerson().getEmail()),
                anyCollectionOf(String.class), isNull(String.class), any(FirebirdMessage.class));
        verify(mockEmailService, times(2)).sendMessage(eq(coordinator.getPerson().getEmail()),
                anyCollectionOf(String.class), isNull(String.class), any(FirebirdMessage.class));
    }

    private void setUpQueryToReturnRegistrations(AnnualRegistration... registrationsToReturn) {
        Query mockQuery = mock(Query.class);
        when(mockQuery.list()).thenReturn(Arrays.asList(registrationsToReturn));
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
    }

    @Test
    public void testSendRenewalReminders_JustInvestigator() {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        AnnualRegistration registration1 = AnnualRegistrationFactory.getInstance().create(profile,
                AnnualRegistrationConfigurationFactory.getInstance().create());
        AnnualRegistration registration2 = AnnualRegistrationFactory.getInstance().create(profile,
                AnnualRegistrationConfigurationFactory.getInstance().create());
        setUpQueryToReturnRegistrations(registration1, registration2);
        bean.sendRenewalReminders();
        verify(mockEmailService, times(2)).sendMessage(eq(profile.getPerson().getEmail()),
                anyCollectionOf(String.class), isNull(String.class), any(FirebirdMessage.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreatePendingRenewals() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        coordinator.createRegistrationCoordinatorRole();
        ManagedInvestigator coordinatorMapping = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(
                profile);
        registration.getProfile().getRegistrationCoordinatorMappings().add(coordinatorMapping);

        Query mockQuery = mock(Query.class);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Lists.newArrayList(registration));
        when(mockConfigurationService.getCurrentConfiguration()).thenReturn(registration.getConfiguration());

        bean.createPendingRenewals();
        verify(bean).createRenewal(registration);
        assertNotNull(registration.getRenewal());
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.ANNUAL_REGISTRATION_RENEWAL_SIXTY_DAY_NOTICE_EMAIL),
                anyMapOf(FirebirdTemplateParameter.class, Object.class));
        verify(mockEmailService).sendMessage(eq(coordinator.getPerson().getEmail()), isNull(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testSetRenewalDateIfNecessary_HasRenewalDate() {
        AnnualRegistration registration = new AnnualRegistration();
        Date renewalDate = new Date();
        registration.setRenewalDate(renewalDate);
        bean.setRenewalDateIfNecessary(registration);
        assertSame(renewalDate, registration.getRenewalDate());
    }

    @Test
    public void testSetRenewalDateIfNecessary_NoDueDate() {
        AnnualRegistration registration = new AnnualRegistration();
        bean.setRenewalDateIfNecessary(registration);
        Date expectedDate = DateUtils.addYears(new Date(), 1);
        assertTrue(DateUtils.isSameDay(expectedDate, registration.getRenewalDate()));
    }

    @Test
    public void testSetRenewalDateIfNecessary_WithFutureDueDate() {
        AnnualRegistration registration = new AnnualRegistration();
        Date dueDate = DateUtils.addDays(new Date(), daysBeforeDueDateToSendFirstNotification - 1);
        registration.setDueDate(dueDate);
        bean.setRenewalDateIfNecessary(registration);
        Date expectedDate = DateUtils.addYears(dueDate, 1);
        assertEquals(expectedDate, registration.getRenewalDate());
    }

    @Test
    public void testSetRenewalDateIfNecessary_WithPastDueDateWithin30Days() {
        AnnualRegistration registration = new AnnualRegistration();
        Date dueDate = DateUtils.addDays(new Date(), -5);
        registration.setDueDate(dueDate);
        bean.setRenewalDateIfNecessary(registration);
        Date expectedDate = DateUtils.addYears(dueDate, 1);
        assertTrue(DateUtils.isSameDay(expectedDate, registration.getRenewalDate()));
    }

    @Test
    public void testSetRenewalDateIfNecessary_WithPastDueDateAfter30Days() {
        AnnualRegistration registration = new AnnualRegistration();
        Date dueDate = DateUtils.addDays(new Date(), -60);
        registration.setDueDate(dueDate);
        bean.setRenewalDateIfNecessary(registration);
        Date expectedDate = DateUtils.addYears(new Date(), 1);
        assertTrue(DateUtils.isSameDay(expectedDate, registration.getRenewalDate()));
    }

    @Test
    public void testSendCoordinatorCompletedRegistrationEmail_CoordinatorCompleted() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.setCtepUser(true);
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create(profile,
                AnnualRegistrationConfigurationFactory.getInstance().create());
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(profile).setCtepAssociate(true);

        bean.sendCoordinatorCompletedRegistrationEmail(coordinator, registration);
        checkForEmailToInvestigator();
        verifyNoMoreInteractions(mockTemplateService, mockEmailService);
    }

    private void checkForEmailToInvestigator() {
        String investigatorEmail = profile.getPerson().getEmail();
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.COORDINATOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL),
                anyMapOf(FirebirdTemplateParameter.class, Object.class));
        verify(mockEmailService).sendMessage(eq(investigatorEmail), anyCollectionOf(String.class), anyString(),
                any(FirebirdMessage.class));
    }

    @Test
    public void testSendCoordinatorCompletedRegistrationEmail_SponsorCompleted_WithCoordinator() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.setCtepUser(true);
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create(profile,
                AnnualRegistrationConfigurationFactory.getInstance().create());
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(profile).setCtepAssociate(true);
        FirebirdUser sponsor = FirebirdUserFactory.getInstance().create();

        bean.sendCoordinatorCompletedRegistrationEmail(sponsor, registration);
        checkForEmailToInvestigator();
        checkForEmailToCoordinator(coordinator);
    }

    private void checkForEmailToCoordinator(FirebirdUser coordinator) {
        String coordinatorEmail = coordinator.getPerson().getEmail();
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.SPONSOR_COMPLETED_ANNUAL_REGISTRATION_EMAIL),
                anyMapOf(FirebirdTemplateParameter.class, Object.class));
        verify(mockEmailService).sendMessage(eq(coordinatorEmail), anyCollectionOf(String.class), anyString(),
                any(FirebirdMessage.class));
    }

    @Test
    public void testSendCoordinatorCompletedRegistrationEmail_SponsorCompleted_NoCoordinator() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create(profile,
                AnnualRegistrationConfigurationFactory.getInstance().create());
        FirebirdUser sponsor = FirebirdUserFactory.getInstance().create();

        bean.sendCoordinatorCompletedRegistrationEmail(sponsor, registration);
        checkForEmailToInvestigator();
        verifyNoMoreInteractions(mockTemplateService, mockEmailService);
    }

    @Test
    public void testCreateReactivatedRegistration() throws Exception {
        AnnualRegistrationConfiguration configuration = AnnualRegistrationConfigurationFactory.getInstance().create();
        when(mockConfigurationService.getCurrentConfiguration()).thenReturn(configuration);
        AnnualRegistration registration = bean.createReactivatedRegistration(profile);
        assertEquals(AnnualRegistrationType.REACTIVATED, registration.getAnnualRegistrationType());
        assertEquals(RegistrationStatus.NOT_STARTED, registration.getStatus());
        assertSame(configuration, registration.getConfiguration());
        assertSame(profile, registration.getProfile());
        assertTrue(profile.getAnnualRegistrations().contains(registration));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveUnFinalizedRegistrations_NotAnInvestigator() throws Exception {
        bean.removeUnFinalizedRegistrations(FirebirdUserFactory.getInstance().createRegistrationCoordinator());
    }

    @Test
    public void testRemoveUnFinalizedRegistrations() throws Exception {
        FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator();
        addRegistration(investigator, RegistrationStatus.APPROVED);
        AbstractRegistration submittedRegistration = addRegistration(investigator, RegistrationStatus.SUBMITTED);
        bean.removeUnFinalizedRegistrations(investigator);
        verify(mockSession).delete(submittedRegistration);
        verifyNoMoreInteractions(mockSession);
    }

    private AbstractRegistration addRegistration(FirebirdUser investigator, RegistrationStatus status) {
        AbstractRegistration registration = new AnnualRegistration();
        registration.setStatus(status);
        investigator.getInvestigatorRole().getProfile().addRegistration(registration);
        return registration;
    }

}
