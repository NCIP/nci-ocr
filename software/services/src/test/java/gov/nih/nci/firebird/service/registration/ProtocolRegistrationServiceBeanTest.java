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

import static gov.nih.nci.firebird.common.MockVerificationUtils.*;
import static gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate.*;
import static gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter.*;
import static gov.nih.nci.firebird.test.ValueGenerator.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.RevisedInvestigatorRegistration;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus;
import gov.nih.nci.firebird.data.user.SponsorRole;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.person.external.InvalidatedPersonException;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.user.CertificateAuthorityManager;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.GuiceTestRunner;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import gov.nih.nci.firebird.test.ValueGenerator;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;

@SuppressWarnings("unchecked")
@RunWith(GuiceTestRunner.class)
public class ProtocolRegistrationServiceBeanTest {

    private ProtocolRegistrationServiceBean bean = new ProtocolRegistrationServiceBean();
    @Mock
    private PersonService mockPersonService;
    @Mock
    private OrganizationService mockOrganizationService;
    @Mock
    private InvestigatorProfileService mockProfileService;
    @Mock
    private TemplateService mockTemplateService;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private FileService mockFileService;
    @Mock
    private Provider<Session> mockSessionProvider;
    @Mock
    private Session mockSession;
    @Mock
    private CertificateAuthorityManager mockCertificateAuthorityManager;
    @Mock
    private SponsorService mockSponsorService;
    @Inject
    FileService fileService;
    private String sponsorEmail;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bean = spy(bean);
        bean.setEmailService(mockEmailService);
        bean.setPersonService(mockPersonService);
        bean.setOrganizationService(mockOrganizationService);
        bean.setProfileService(mockProfileService);
        bean.setTemplateService(mockTemplateService);
        bean.setFileService(mockFileService);
        when(mockSessionProvider.get()).thenReturn(mockSession);
        bean.setSessionProvider(mockSessionProvider);
        bean.setResources(ResourceBundle.getBundle("resources", Locale.getDefault()));
        bean.setCertificateAuthorityManager(mockCertificateAuthorityManager);
        bean.setSponsorService(mockSponsorService);
        System.setProperty("registration.validation.require.nes.status.active", "true");
        sponsorEmail = ValueGenerator.getUniqueEmailAddress();
        when(mockSponsorService.getSponsorEmailAddress(any(Organization.class))).thenReturn(sponsorEmail);
    }

    @Test
    public void testCreateInvestigatorRegistration() throws ValidationException {
        Protocol protocol1 = ProtocolFactory.getInstance().createWithForms();
        Person investigator1 = PersonFactory.getInstance().create();
        InvestigatorRegistration registration1 = bean.createInvestigatorRegistration(protocol1, investigator1);
        checkRegistration(protocol1, investigator1, registration1);
        verify(mockPersonService).save(investigator1);
        verify(mockProfileService).save(registration1.getProfile());

        Protocol protocol2 = ProtocolFactory.getInstance().createWithForms();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        Person investigator2 = profile.getPerson();
        when(mockProfileService.getByPerson(investigator2)).thenReturn(profile);
        InvestigatorRegistration registration2 = bean.createInvestigatorRegistration(protocol2, investigator2);
        checkRegistration(protocol2, investigator2, registration2);
    }

    private void checkRegistration(Protocol protocol, Person investigator, InvestigatorRegistration registration) {
        assertEquals(protocol, registration.getProtocol());
        assertNotNull(registration.getProfile());
        assertEquals(investigator, registration.getProfile().getPerson());
        assertTrue(registration.getProfile().getRegistrations().contains(registration));
        assertTrue(protocol.getCurrentInvestigatorRegistrations().contains(registration));
    }

    @Test
    public void testCreateSubinvestigatorRegistration() {
        Person subInvestigator = PersonFactory.getInstance().create();
        subInvestigator.setId(1L);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        SubInvestigatorRegistration subRegistration = bean.createSubinvestigatorRegistration(registration,
                subInvestigator);
        assertEquals(InvitationStatus.NOT_INVITED, subRegistration.getInvitation().getInvitationStatus());
        assertEquals(subInvestigator, subRegistration.getProfile().getPerson());
        assertEquals(registration, subRegistration.getPrimaryRegistration());
        assertEquals(FormStatus.NOT_STARTED, registration.getForm1572().getFormStatus());
    }

    @Test
    public void testCreateSubinvestigatorRegistration_Returned() {
        Person subInvestigator = PersonFactory.getInstance().create();
        subInvestigator.setId(1L);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getForm1572().setFormStatus(FormStatus.REJECTED);
        registration.setStatus(RegistrationStatus.RETURNED);
        SubInvestigatorRegistration subRegistration = bean.createSubinvestigatorRegistration(registration,
                subInvestigator);
        assertEquals(InvitationStatus.NOT_INVITED, subRegistration.getInvitation().getInvitationStatus());
        assertEquals(subInvestigator, subRegistration.getProfile().getPerson());
        assertEquals(registration, subRegistration.getPrimaryRegistration());
        assertEquals(FormStatus.REVISED, registration.getForm1572().getFormStatus());
    }

    @Test
    public void testCreateSubinvestigatorRegistrationExisting() {
        Person subInvestigator = PersonFactory.getInstance().create();
        subInvestigator.setId(1L);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        SubInvestigatorRegistration subRegistration = bean.createSubinvestigatorRegistration(registration,
                subInvestigator);
        SubInvestigatorRegistration subRegistrationBis = bean.createSubinvestigatorRegistration(registration,
                subInvestigator);
        assertSame(subRegistration, subRegistrationBis);
    }

    @Test
    public void testCreateSubInvestigatorRegistrations() throws InvalidatedPersonException {
        Person subInvestigator = PersonFactory.getInstance().create();
        subInvestigator.setId(1L);
        Person subInvestigator2 = PersonFactory.getInstance().create();
        subInvestigator2.setId(2L);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();

        when(mockPersonService.getByExternalId(subInvestigator.getExternalId())).thenReturn(subInvestigator);
        when(mockPersonService.getByExternalId(subInvestigator2.getExternalId())).thenReturn(subInvestigator2);

        List<String> ids = Lists.newArrayList(subInvestigator.getExternalId(), subInvestigator2.getExternalId());

        bean.createSubinvestigatorRegistrations(registration, ids);
        verify(bean, times(2))
                .createSubinvestigatorRegistration(any(InvestigatorRegistration.class), any(Person.class));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testInviteToRegistrationInvestigator() throws InterruptedException {
        InvestigatorRegistration investigatorRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        investigatorRegistration.getInvitation().setInvitationStatus(InvitationStatus.NOT_INVITED);
        Person investigator = investigatorRegistration.getProfile().getPerson();
        FirebirdMessage expectedInvestigatorMessage = mock(FirebirdMessage.class);
        String sponsorEmailAddress = getUniqueEmailAddress();
        when(mockSponsorService.getSponsorEmailAddress(investigatorRegistration.getProtocol().getSponsor()))
                .thenReturn(sponsorEmailAddress);
        when(mockTemplateService.generateMessage(eq(INVESTIGATOR_INVITATION_EMAIL), any(Map.class))).thenReturn(
                expectedInvestigatorMessage);
        Date now = new Date();
        Thread.sleep(1);
        bean.inviteToRegistration(investigatorRegistration);
        verify(mockEmailService).sendMessage(investigator.getEmail(), null, null, expectedInvestigatorMessage);
        assertEquals(InvitationStatus.NO_RESPONSE, investigatorRegistration.getInvitation().getInvitationStatus());
        assertTrue(now.before(investigatorRegistration.getInvitation().getInvitationChangeDate()));
        ArgumentCaptor<Map> valueMapCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockTemplateService).generateMessage(eq(INVESTIGATOR_INVITATION_EMAIL), valueMapCaptor.capture());
        Map<FirebirdTemplateParameter, Object> values = valueMapCaptor.getValue();
        assertEquals(investigatorRegistration, values.get(INVESTIGATOR_REGISTRATION));
        assertEquals(sponsorEmailAddress, values.get(SPONSOR_EMAIL_ADDRESS));
        String expectedLink = FirebirdConstants.FIREBIRD_HOME_URL_PATH;
        assertEquals(expectedLink, values.get(FIREBIRD_LINK));
    }

    @Test
    public void testInviteToRegistrationInvestigatorFromInProgress() throws InterruptedException {
        Date invitationStatusDate = new Date();
        InvestigatorRegistration investigatorRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        investigatorRegistration.setStatus(RegistrationStatus.IN_PROGRESS);
        investigatorRegistration.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        investigatorRegistration.getInvitation().setInvitationChangeDate(invitationStatusDate);
        bean.inviteToRegistration(investigatorRegistration);
        assertEquals(InvitationStatus.RESPONDED, investigatorRegistration.getInvitation().getInvitationStatus());
        assertEquals(invitationStatusDate, investigatorRegistration.getInvitation().getInvitationChangeDate());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testInviteToRegistrationSubinvestigator() throws InterruptedException {
        FirebirdMessage expectedSubinvestigatorMessage = mock(FirebirdMessage.class);
        when(mockTemplateService.generateMessage(eq(SUBINVESTIGATOR_INVITATION_EMAIL), any(Map.class))).thenReturn(
                expectedSubinvestigatorMessage);
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subInvestigatorRegistration.getInvitation().setInvitationStatus(InvitationStatus.NOT_INVITED);
        Person investigator = subInvestigatorRegistration.getProfile().getPerson();
        Date now = new Date();
        Thread.sleep(1);
        bean.inviteToRegistration(subInvestigatorRegistration);
        verify(mockEmailService).sendMessage(investigator.getEmail(), null, null, expectedSubinvestigatorMessage);
        assertEquals(InvitationStatus.NO_RESPONSE, subInvestigatorRegistration.getInvitation().getInvitationStatus());
        assertTrue(now.before(subInvestigatorRegistration.getInvitation().getInvitationChangeDate()));
        ArgumentCaptor<Map> valueMapCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockTemplateService).generateMessage(eq(SUBINVESTIGATOR_INVITATION_EMAIL), valueMapCaptor.capture());
        Map<FirebirdTemplateParameter, Object> values = valueMapCaptor.getValue();
        assertEquals(subInvestigatorRegistration, values.get(SUBINVESTIGATOR_REGISTRATION));
        assertTrue(values.containsKey(SPONSOR_EMAIL_ADDRESS));
        String expectedLink = FirebirdConstants.FIREBIRD_HOME_URL_PATH;
        assertEquals(expectedLink, values.get(FIREBIRD_LINK));
    }

    @Test
    public void testInviteToRegistrationSubInvestigatorFromInProgress() throws InterruptedException {
        /*
         * Test Investigator Registration
         */
        Date invitationStatusDate = new Date();
        SubInvestigatorRegistration subinvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        subinvestigatorRegistration.setStatus(RegistrationStatus.IN_PROGRESS);
        subinvestigatorRegistration.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        subinvestigatorRegistration.getInvitation().setInvitationChangeDate(invitationStatusDate);
        FirebirdMessage expectedInvestigatorMessage = mock(FirebirdMessage.class);
        when(mockTemplateService.generateMessage(eq(INVESTIGATOR_INVITATION_EMAIL), any(Map.class))).thenReturn(
                expectedInvestigatorMessage);
        bean.inviteToRegistration(subinvestigatorRegistration);
        assertEquals(InvitationStatus.RESPONDED, subinvestigatorRegistration.getInvitation().getInvitationStatus());
        assertEquals(invitationStatusDate, subinvestigatorRegistration.getInvitation().getInvitationChangeDate());
    }

    @Test
    public void testCheckFormCompletionStatus_Valid() throws ValidationException, AssociationAlreadyExistsException,
            CredentialAlreadyExistsException {
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate(
                CertificateType.HUMAN_RESEARCH_CERTIFICATE);
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProfile().addCredential(certificate);
        registration.getProfile().addCredential(CredentialFactory.getInstance().createLicense());
        RegistrationFactory.getInstance().setupInProgress(registration);

        bean.checkFormCompletionStatus(registration);
        assertFalse(registration.getOrganizations().isEmpty());
        assertFalse(registration.getPersons().isEmpty());
        assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
        assertEquals(FormStatus.COMPLETED, registration.getForm1572().getFormStatus());
    }

    @Test
    public void testCheckFormCompletionStatus_InactiveNesPerson() throws ValidationException,
            AssociationAlreadyExistsException, CredentialAlreadyExistsException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProfile().addCredential(
                CredentialFactory.getInstance().createCertificate(CertificateType.HUMAN_RESEARCH_CERTIFICATE));
        registration.getProfile().addCredential(CredentialFactory.getInstance().createLicense());
        registration.getProfile().getPerson().setCurationStatus(CurationStatus.INACTIVE);
        RegistrationFactory.getInstance().setupInProgress(registration);

        try {
            bean.checkFormCompletionStatus(registration);
            fail("A Validation Exception should have been thrown.");
        } catch (ValidationException e) {
            assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
            assertEquals(FormStatus.IN_PROGRESS, registration.getForm1572().getFormStatus());
        }
    }

    @Test
    public void testCheckFormCompletionStatus_PreNesCurationPerson() throws ValidationException,
            AssociationAlreadyExistsException, CredentialAlreadyExistsException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProfile().addCredential(
                CredentialFactory.getInstance().createCertificate(CertificateType.HUMAN_RESEARCH_CERTIFICATE));
        registration.getProfile().addCredential(CredentialFactory.getInstance().createLicense());
        registration.getProfile().getPerson().setCurationStatus(CurationStatus.UNSAVED);
        RegistrationFactory.getInstance().setupInProgress(registration);

        try {
            bean.checkFormCompletionStatus(registration);
            fail("A Validation Exception should have been thrown.");
        } catch (ValidationException e) {
            assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
            assertEquals(FormStatus.IN_PROGRESS, registration.getForm1572().getFormStatus());
        }
    }

    @Test
    public void testCheckFormCompletionStatus_CuratedPersonUncuratedOrg() throws ValidationException,
            AssociationAlreadyExistsException, CredentialAlreadyExistsException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        RegistrationFactory.getInstance().setupFinancialDisclosureWithFiles(registration);
        RegistrationFactory.getInstance().setup1572(registration);
        registration.getProfile().addCredential(
                CredentialFactory.getInstance().createCertificate(CertificateType.HUMAN_RESEARCH_CERTIFICATE));
        registration.getProfile().addCredential(CredentialFactory.getInstance().createLicense());
        registration.getProfile().getPrimaryOrganization().getOrganization()
                .setCurationStatus(CurationStatus.UNSAVED);
        RegistrationFactory.getInstance().setupInProgress(registration);

        try {
            bean.checkFormCompletionStatus(registration);
            fail("A Validation Exception should have been thrown.");
        } catch (ValidationException e) {
            assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
            assertEquals(FormStatus.IN_PROGRESS, registration.getForm1572().getFormStatus());
        }
    }

    @Test
    public void testRemoveSubInvestigatorRegistrationAndNotify_InProgressRegistration()
            throws AssociationAlreadyExistsException, ValidationException {
        SubInvestigatorRegistration subInvestigatorRegistration = setupSubinvestigatorRegistration(
                RegistrationStatus.IN_PROGRESS, RegistrationStatus.IN_PROGRESS, InvitationStatus.NO_RESPONSE);
        InvestigatorProfile profile = subInvestigatorRegistration.getProfile();
        bean.removeSubInvestigatorRegistrationAndNotify(subInvestigatorRegistration);
        verify(bean).delete(subInvestigatorRegistration);
        verify(mockEmailService).sendMessage(eq(profile.getPerson().getEmail()), anyCollectionOf(String.class),
                anyString(), any(FirebirdMessage.class));
        verify(mockEmailService, never()).sendMessage(eq(sponsorEmail), anyCollectionOf(String.class), anyString(),
                any(FirebirdMessage.class));
    }

    @Test
    public void testRemoveSubInvestigatorRegistrationAndNotify_SubinvestigatorReferencedInRevisedRegistration()
            throws AssociationAlreadyExistsException, ValidationException {
        SubInvestigatorRegistration subInvestigatorRegistration = setupSubinvestigatorRegistration(
                RegistrationStatus.APPROVED, RegistrationStatus.IN_PROGRESS, InvitationStatus.NO_RESPONSE);
        InvestigatorRegistration primaryRegistration = subInvestigatorRegistration.getPrimaryRegistration();
        primaryRegistration.createRevisedRegistration();
        primaryRegistration.setStatus(RegistrationStatus.IN_PROGRESS);
        bean.removeSubInvestigatorRegistrationAndNotify(subInvestigatorRegistration);
        assertNull(subInvestigatorRegistration.getPrimaryRegistration());
        verify(bean, never()).delete(subInvestigatorRegistration);
        verify(mockEmailService).sendMessage(eq(subInvestigatorRegistration.getProfile().getPerson().getEmail()),
                anyCollectionOf(String.class), anyString(), any(FirebirdMessage.class));
        verify(mockEmailService, never()).sendMessage(eq(sponsorEmail), anyCollectionOf(String.class), anyString(),
                any(FirebirdMessage.class));
    }

    private SubInvestigatorRegistration setupSubinvestigatorRegistration(RegistrationStatus primaryRegistrationStatus,
            RegistrationStatus subinvestigatorRegistrationStatus, InvitationStatus invitationStatus)
            throws AssociationAlreadyExistsException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.getForm1572().setFormStatus(FormStatus.REJECTED);
        registration.setStatus(primaryRegistrationStatus);
        InvestigatorProfile subInvestigatorProfile = InvestigatorProfileFactory.getInstance().create();
        registration.getProfile().addSubInvestigator(subInvestigatorProfile.getPerson());
        SubInvestigatorRegistration subInvestigatorRegistration = createSubinvestigatorRegistration(registration,
                subinvestigatorRegistrationStatus, subInvestigatorProfile);
        subInvestigatorRegistration.getInvitation().setInvitationStatus(invitationStatus);
        return subInvestigatorRegistration;
    }

    private SubInvestigatorRegistration createSubinvestigatorRegistration(InvestigatorRegistration registration,
            RegistrationStatus status, InvestigatorProfile subInvestigatorProfile) {
        SubInvestigatorRegistration subinvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(registration, subInvestigatorProfile);
        subinvestigatorRegistration.setStatus(status);
        return subinvestigatorRegistration;
    }

    @Test
    public void testRemoveSubInvestigatorRegistrationAndNotify_ReturnedRegistration()
            throws AssociationAlreadyExistsException, ValidationException {
        SubInvestigatorRegistration subInvestigatorRegistration = setupSubinvestigatorRegistration(
                RegistrationStatus.RETURNED, RegistrationStatus.IN_PROGRESS, InvitationStatus.NO_RESPONSE);
        InvestigatorRegistration primaryRegistration = subInvestigatorRegistration.getPrimaryRegistration();
        InvestigatorProfile profile = subInvestigatorRegistration.getProfile();
        bean.removeSubInvestigatorRegistrationAndNotify(subInvestigatorRegistration);
        verify(bean).delete(subInvestigatorRegistration);
        assertEquals(FormStatus.REVISED, primaryRegistration.getForm1572().getFormStatus());
        verify(mockEmailService).sendMessage(eq(profile.getPerson().getEmail()), anyCollectionOf(String.class),
                anyString(), any(FirebirdMessage.class));
        verify(mockEmailService, never()).sendMessage(eq(sponsorEmail), anyCollectionOf(String.class), anyString(),
                any(FirebirdMessage.class));
    }

    @Test
    public void testRemoveSubInvestigatorRegistrationAndNotify_SubmittedSubinvestigatorRegistration()
            throws AssociationAlreadyExistsException, ValidationException {
        SubInvestigatorRegistration subInvestigatorRegistration = setupSubinvestigatorRegistration(
                RegistrationStatus.IN_PROGRESS, RegistrationStatus.SUBMITTED, InvitationStatus.RESPONDED);
        InvestigatorProfile profile = subInvestigatorRegistration.getProfile();
        bean.removeSubInvestigatorRegistrationAndNotify(subInvestigatorRegistration);
        verify(bean).delete(subInvestigatorRegistration);
        verify(mockEmailService).sendMessage(eq(profile.getPerson().getEmail()), anyCollectionOf(String.class),
                anyString(), any(FirebirdMessage.class));
        verify(mockEmailService).sendMessage(eq(sponsorEmail), anyCollectionOf(String.class), anyString(),
                any(FirebirdMessage.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSubInvestigatorRegistrationAndNotify_SubmittedRegistration()
            throws AssociationAlreadyExistsException, ValidationException {
        SubInvestigatorRegistration subInvestigatorRegistration = setupSubinvestigatorRegistration(
                RegistrationStatus.SUBMITTED, RegistrationStatus.IN_PROGRESS, InvitationStatus.NO_RESPONSE);
        bean.removeSubInvestigatorRegistrationAndNotify(subInvestigatorRegistration);
    }

    @Test
    public void testRemoveSubInvestigatorRegistrationAndNotify_NotInvitedRegistration()
            throws AssociationAlreadyExistsException, ValidationException {
        SubInvestigatorRegistration subInvestigatorRegistration = setupSubinvestigatorRegistration(
                RegistrationStatus.IN_PROGRESS, RegistrationStatus.IN_PROGRESS, InvitationStatus.NOT_INVITED);
        bean.removeSubInvestigatorRegistrationAndNotify(subInvestigatorRegistration);
        verify(bean).delete(subInvestigatorRegistration);
        verifyZeroInteractions(mockEmailService);
    }

    @Test
    public void testGetRegistrations() {
        AnnualRegistration annualRegistration = AnnualRegistrationFactory.getInstance().create();
        InvestigatorProfile profile = annualRegistration.getProfile();
        InvestigatorRegistration investigatorRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration(profile);
        profile.addRegistration(investigatorRegistration);
        assertEquals(Sets.newHashSet(investigatorRegistration), bean.getRegistrations(profile));
    }

    @Test
    public void testGetCoordinatorCompletedRegistrationEmailMessage() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        bean.getCoordinatorCompletedRegistrationEmailMessage(coordinator, registration);
        Map<FirebirdTemplateParameter, Object> parameterValues = new EnumMap<FirebirdTemplateParameter, Object>(
                FirebirdTemplateParameter.class);
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION, registration);
        parameterValues.put(FirebirdTemplateParameter.FIREBIRD_LINK,
                FirebirdConstants.REGISTRATION_URL_PATH_WITH_ID_PARAM + registration.getId());
        parameterValues.put(FirebirdTemplateParameter.REGISTRATION_COORDINATOR, coordinator.getPerson());
        verify(mockTemplateService).generateMessage(FirebirdMessageTemplate.COORDINATOR_COMPLETED_REGISTRATION_EMAIL,
                parameterValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitiateRevision_NotRevisable() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        bean.initiateRevision(registration, new FirebirdUser());
    }

    @Test
    public void testInitiateRevision_AsInvestigator() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        FirebirdUser suspendedFromProfileCoordinator = createAndAddCoordinator(registration.getProfile(),
                ManagedInvestigatorStatus.SUSPENDED_FROM_PROFILE);
        FirebirdUser suspendedFromRegistrationsCoordinator = createAndAddCoordinator(registration.getProfile(),
                ManagedInvestigatorStatus.SUSPENDED_FROM_REGISTRATIONS);

        registration.getForm1572().setComments("comments");
        registration.getForm1572().setFormStatus(FormStatus.REJECTED);
        registration.getCurriculumVitaeForm().setComments("comments");

        bean.initiateRevision(registration, registration.getProfile().getUser());

        assertNotNull(registration.getForm1572().getComments());
        assertNull(registration.getCurriculumVitaeForm().getComments());

        assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
        verifyEmailMessageSent(suspendedFromProfileCoordinator.getPerson().getEmail(),
                REGISTRATION_REVISION_INITIATED_EMAIL_TO_COORDINATOR, mockEmailService, mockTemplateService);
        verifyEmailMessageSent(sponsorEmail, REGISTRATION_REVISION_INITIATED_EMAIL_TO_SPONSOR, mockEmailService,
                mockTemplateService);
        verifyEmailMessageNotSent(suspendedFromRegistrationsCoordinator.getPerson().getEmail(), mockEmailService);
    }

    private FirebirdUser createAndAddCoordinator(InvestigatorProfile profile, ManagedInvestigatorStatus status) {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(profile).setStatus(status);
        return coordinator;
    }

    @Test
    public void testInitiateRevision_AsCoordinator() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        FirebirdUser coordinator = createAndAddCoordinator(registration.getProfile(),
                ManagedInvestigatorStatus.APPROVED);
        FirebirdUser coordinator2 = createAndAddCoordinator(registration.getProfile(),
                ManagedInvestigatorStatus.APPROVED);

        bean.initiateRevision(registration, coordinator);

        assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
        verifyEmailMessageSent(registration.getProfile().getPerson().getEmail(),
                REGISTRATION_REVISION_INITIATED_EMAIL_TO_INVESTIGATOR, mockEmailService, mockTemplateService);
        verifyEmailMessageSent(sponsorEmail, REGISTRATION_REVISION_INITIATED_EMAIL_TO_SPONSOR, mockEmailService,
                mockTemplateService);
        verifyEmailMessageSent(coordinator2.getPerson().getEmail(),
                REGISTRATION_REVISION_INITIATED_EMAIL_TO_COORDINATOR, mockEmailService, mockTemplateService);
    }

    @Test
    public void testInitiateRevision_AsSuspendedProfileCoordinator() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        FirebirdUser suspendedFromProfileCoordinator = createAndAddCoordinator(registration.getProfile(),
                ManagedInvestigatorStatus.SUSPENDED_FROM_PROFILE);

        bean.initiateRevision(registration, suspendedFromProfileCoordinator);

        assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
        verifyEmailMessageSent(registration.getProfile().getPerson().getEmail(),
                REGISTRATION_REVISION_INITIATED_EMAIL_TO_INVESTIGATOR, mockEmailService, mockTemplateService);
        verifyEmailMessageSent(sponsorEmail, REGISTRATION_REVISION_INITIATED_EMAIL_TO_SPONSOR, mockEmailService,
                mockTemplateService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitiateRevision_AsSuspendedRegistrationsCoordinator() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        FirebirdUser suspendedFromProfileCoordinator = createAndAddCoordinator(registration.getProfile(),
                ManagedInvestigatorStatus.SUSPENDED_FROM_REGISTRATIONS);

        bean.initiateRevision(registration, suspendedFromProfileCoordinator);

        assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitiateRevision_AsDifferentInvestigator() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        bean.initiateRevision(registration, FirebirdUserFactory.getInstance().createInvestigator());
    }

    @Test
    public void testInitiateRevision_ApprovedRegistration() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.APPROVED);
        FirebirdUser user = registration.getProfile().getUser();
        registration.getForm1572().setFormStatus(FormStatus.APPROVED);

        bean.initiateRevision(registration, user);
        assertNull(registration.getApprovalDate());
        assertFalse(registration.getRevisedRegistrations().isEmpty());
        assertEquals(FormStatus.ACCEPTED, registration.getForm1572().getFormStatus());
    }

    @Test
    public void testCancelRevision() throws Exception {
        RevisedInvestigatorRegistration revisedRegistration = RegistrationFactory.getInstance()
                .createRevisedInvestigatorRegistration();
        InvestigatorRegistration currentRegistration = (InvestigatorRegistration) revisedRegistration
                .getCurrentRegistration();
        Protocol protocol = revisedRegistration.getProtocol();
        InvestigatorProfile profile = revisedRegistration.getProfile();
        FirebirdUser user = currentRegistration.getProfile().getUser();
        bean.cancelRevision(currentRegistration, user);
        verify(mockSession).delete(revisedRegistration);
        verify(mockSession).delete(currentRegistration);
        ArgumentCaptor<InvestigatorRegistration> captor = ArgumentCaptor.forClass(InvestigatorRegistration.class);
        verify(mockSession).saveOrUpdate(captor.capture());

        assertEquals(protocol, captor.getValue().getProtocol());
        assertEquals(profile, captor.getValue().getProfile());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCancelRevision_NoRevisedRegistrations() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        FirebirdUser user = registration.getProfile().getUser();
        bean.cancelRevision(registration, user);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetByStatusForUser() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization verifiedSponsor = OrganizationFactory.getInstance().create();
        SponsorRole verifiedRole = user.addSponsorRepresentativeRole(verifiedSponsor);
        Organization unverifiedSponsor = OrganizationFactory.getInstance().create();
        SponsorRole unverifiedRole = user.addSponsorRepresentativeRole(unverifiedSponsor);
        Set<String> groupNames = Sets.newHashSet(verifiedRole.getSponsorOrganizationGroupName(),
                verifiedRole.getVerifiedSponsorGroupName(), unverifiedRole.getSponsorOrganizationGroupName());

        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        Query mockQuery = mock(Query.class);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Lists.newArrayList(registration));

        List<AbstractProtocolRegistration> registrations = bean.getByStatusForUser(RegistrationStatus.IN_PROGRESS,
                user, groupNames);
        ArgumentCaptor<Collection> sponsorListCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(mockQuery).setParameter("status", RegistrationStatus.IN_PROGRESS);
        verify(mockQuery).setParameterList(eq("sponsors"), sponsorListCaptor.capture());
        assertEquals(1, sponsorListCaptor.getValue().size());
        assertEquals(verifiedSponsor, sponsorListCaptor.getValue().iterator().next());
        assertEquals(1, registrations.size());
    }

    @Test
    public void testGetByStatusForUser_NoVerifiedSponsorRoles() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Set<String> emptyGroupNames = Collections.emptySet();
        List<AbstractProtocolRegistration> protocols = bean.getByStatusForUser(RegistrationStatus.IN_PROGRESS, user,
                emptyGroupNames);
        assertTrue(protocols.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testGetByStatusForUser_NullArguments() {
        bean.getByStatusForUser(null, null, null);
    }

}
