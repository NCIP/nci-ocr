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
package gov.nih.nci.firebird.service.protocol;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Provider;
import gov.nih.nci.firebird.common.ValidationUtility;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AbstractSupplementalForm;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolRevision;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.RegistrationType;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.SponsorRole;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.service.sponsor.SponsorNotificationService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.FormTypeFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import static gov.nih.nci.firebird.data.FormOptionality.NONE;
import static gov.nih.nci.firebird.data.FormOptionality.OPTIONAL;
import static gov.nih.nci.firebird.data.FormOptionality.REQUIRED;
import static gov.nih.nci.firebird.data.FormStatus.APPROVED;
import static gov.nih.nci.firebird.data.FormStatus.INACTIVE;
import static gov.nih.nci.firebird.data.FormStatus.IN_PROGRESS;
import static gov.nih.nci.firebird.data.FormStatus.NOT_APPLICABLE;
import static gov.nih.nci.firebird.data.FormStatus.NOT_STARTED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class ProtocolServiceBeanTest {

    @Mock
    private FormTypeService mockFormTypeService;
    @Mock
    private ProtocolRegistrationService mockRegistrationService;
    @Mock
    private ProtocolValidationService mockProtocolValidationService;
    @Mock
    private ProtocolAgentService mockProtocolAgentService;

    @Mock
    private SponsorNotificationService mockSponsorNotificationService;
    @Mock
    private Provider<Session> mockSessionProvider;
    @Mock
    private Session mockSession;
    @Mock
    private Query mockQuery;

    private ProtocolServiceBean bean = new ProtocolServiceBean();
    private Organization sponsorOrganization = OrganizationFactory.getInstance().create();
    private List<FormType> standardForms = new ArrayList<FormType>();
    private ResourceBundle resources = ResourceBundle.getBundle("resources");
    private FirebirdUser sponsorUser = FirebirdUserFactory.getInstance().create();
    private FirebirdUser delegateUser = FirebirdUserFactory.getInstance().create();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        standardForms.add(FormTypeFactory.getInstance().create(FormTypeEnum.FINANCIAL_DISCLOSURE_FORM));
        standardForms.add(FormTypeFactory.getInstance().create(FormTypeEnum.FORM_1572));
        standardForms.get(1).setInvestigatorDefault(FormOptionality.OPTIONAL);
        standardForms.get(1).setSubinvestigatorDefault(FormOptionality.NONE);
        when(mockFormTypeService.getStandardConfigureableForms(RegistrationType.PROTOCOL)).thenReturn(standardForms);
        bean.setFormTypeService(mockFormTypeService);
        when(mockSessionProvider.get()).thenReturn(mockSession);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        bean.setSessionProvider(mockSessionProvider);
        bean.setRegistrationService(mockRegistrationService);
        bean.setProtocolValidationService(mockProtocolValidationService);
        bean.setProtocolAgentService(mockProtocolAgentService);
        bean.setSponsorNotificationService(mockSponsorNotificationService);
        sponsorUser.addSponsorRepresentativeRole(sponsorOrganization);
        delegateUser.addSponsorDelegateRole(sponsorOrganization);
        bean.setResources(resources);
    }

    @Test
    public void testCreate() {
        Protocol protocol = bean.create();
        assertNotNull(protocol);
        assertEquals(REQUIRED, protocol.getRegistrationConfiguration().getInvestigatorOptionality(standardForms.get(0)));
        assertEquals(REQUIRED,
                protocol.getRegistrationConfiguration().getSubinvestigatorOptionality(standardForms.get(0)));
        assertEquals(OPTIONAL, protocol.getRegistrationConfiguration().getInvestigatorOptionality(standardForms.get(1)));
        assertEquals(NONE, protocol.getRegistrationConfiguration().getSubinvestigatorOptionality(standardForms.get(1)));
    }

    @Test
    public void testAddInvestigator() throws ValidationException {
        Protocol protocol = createTestProtocol();
        Person investigator = PersonFactory.getInstance().create();
        InvestigatorRegistration expectedRegistration = mock(InvestigatorRegistration.class);
        when(mockRegistrationService.createInvestigatorRegistration(protocol, investigator)).thenReturn(
                expectedRegistration);
        InvestigatorRegistration registration = bean.addInvestigator(protocol, investigator);
        assertEquals(expectedRegistration, registration);
    }

    private Protocol createTestProtocol() {
        Protocol protocol1 = ProtocolFactory.getInstance().createWithForms();
        protocol1.getAgents().clear();
        return protocol1;
    }

    @Test
    public void testInvite() throws InterruptedException {
        InvestigatorRegistration registration = createTestRegistrationForAcceptance();
        bean.invite(registration);
        verify(mockRegistrationService).inviteToRegistration(registration);
    }

    private InvestigatorRegistration createTestRegistrationForAcceptance() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.IN_REVIEW);
        registration.getProtocol().setSponsor(sponsorOrganization);
        return registration;
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetProtocols() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization verifiedSponsor = OrganizationFactory.getInstance().create();
        SponsorRole verifiedRole = user.addSponsorRepresentativeRole(verifiedSponsor);
        Organization unverifiedSponsor = OrganizationFactory.getInstance().create();
        SponsorRole unverifiedRole = user.addSponsorRepresentativeRole(unverifiedSponsor);
        Set<String> groupNames = Sets.newHashSet(verifiedRole.getSponsorOrganizationGroupName(),
                verifiedRole.getVerifiedSponsorGroupName(), unverifiedRole.getSponsorOrganizationGroupName());
        Protocol protocol = ProtocolFactory.getInstance().create(verifiedSponsor);
        Query mockQuery = mock(Query.class);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Lists.newArrayList(protocol));

        List<Protocol> protocols = bean.getProtocols(user, groupNames);
        ArgumentCaptor<Collection> sponsorListCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(mockQuery).setParameterList(eq("sponsors"), sponsorListCaptor.capture());
        assertEquals(1, sponsorListCaptor.getValue().size());
        assertEquals(verifiedSponsor, sponsorListCaptor.getValue().iterator().next());
        assertEquals(1, protocols.size());
    }

    @Test
    public void testGetProtocols_NoVerifiedSponsorRoles() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Set<String> emptyGroupNames = Collections.emptySet();
        List<Protocol> protocols = bean.getProtocols(user, emptyGroupNames);
        assertTrue(protocols.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testGetProtocols_NullArguments() {
        bean.getProtocols(null, null);
    }

    @Test
    public void testApproveRegistration() throws CredentialAlreadyExistsException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.ACCEPTED);
        SubInvestigatorRegistration subInvestigatorRegistration = createSubinvestigatorRegistration(registration,
                RegistrationStatus.ACCEPTED);
        bean.approvePacket(registration);
        assertEquals(RegistrationStatus.APPROVED, registration.getStatus());
        assertEquals(RegistrationStatus.APPROVED, subInvestigatorRegistration.getStatus());
        assertTrue(DateUtils.isSameDay(registration.getApprovalDate(), subInvestigatorRegistration.getApprovalDate()));
        checkFormStatuses(registration, APPROVED);
        checkFormStatuses(subInvestigatorRegistration, APPROVED);
        verify(mockSponsorNotificationService).notifyInvestigatorsOfApproval(registration);
    }

    private SubInvestigatorRegistration createSubinvestigatorRegistration(InvestigatorRegistration registration,
                                                                          RegistrationStatus status) {
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(registration);
        subInvestigatorRegistration.setStatus(status);
        return subInvestigatorRegistration;
    }

    private void checkFormStatuses(AbstractProtocolRegistration registration, FormStatus expectedStatus) {
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            assertEquals(expectedStatus, form.getFormStatus());
        }
    }

    @Test
    public void testApproveRegistration_ApprovedSubinvestigatorRegistration() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.ACCEPTED);
        SubInvestigatorRegistration subInvestigatorRegistration = createSubinvestigatorRegistration(registration,
                RegistrationStatus.APPROVED);
        Thread.sleep(100);
        bean.approvePacket(registration);
        assertEquals(RegistrationStatus.APPROVED, registration.getStatus());
        assertEquals(RegistrationStatus.APPROVED, subInvestigatorRegistration.getStatus());
        assertFalse(registration.getApprovalDate().equals(subInvestigatorRegistration.getApprovalDate()));
        checkFormStatuses(registration, APPROVED);
        verify(mockSponsorNotificationService).notifyInvestigatorsOfApproval(registration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApproveRegistrationNotApprovable() throws CredentialAlreadyExistsException {
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        InvestigatorRegistration registration = subInvestigatorRegistration.getPrimaryRegistration();
        registration.setStatus(RegistrationStatus.IN_REVIEW);
        bean.approvePacket(registration);
    }

    @Test
    public void testDeactivatePacket() {
        String comments = "You are a poopyhead and we don't like you.";
        Protocol protocol = ProtocolFactory.getInstance().createWithForms();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                InvestigatorProfileFactory.getInstance().create(), protocol);
        registration.getForm1572().setComments("comments");
        SubInvestigatorRegistration subRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(registration);

        bean.deactivatePacket(registration, comments);
        assertEquals(RegistrationStatus.INACTIVE, registration.getStatus());
        checkRegistrationFormsForStatus(registration, INACTIVE);
        assertNull(registration.getForm1572().getComments());

        assertEquals(RegistrationStatus.INACTIVE, subRegistration.getStatus());
        checkRegistrationFormsForStatus(subRegistration, INACTIVE);

        verify(mockSponsorNotificationService, times(2)).notifyOfDeactivation(any(AbstractProtocolRegistration.class), anyString());
    }

    private void checkRegistrationFormsForStatus(AbstractProtocolRegistration registration, FormStatus status) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (form instanceof AbstractSupplementalForm) {
                assertEquals(NOT_APPLICABLE, form.getFormStatus());
            } else {
                assertEquals(status, form.getFormStatus());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeactivatePacketNullComments() {
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        InvestigatorRegistration registration = subInvestigatorRegistration.getPrimaryRegistration();
        bean.deactivatePacket(registration, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeactivatePacketEmptyComments() {
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        InvestigatorRegistration registration = subInvestigatorRegistration.getPrimaryRegistration();
        bean.deactivatePacket(registration, "");
    }

    @Test
    public void testReactivatePacket() {
        Protocol protocol = peformReactivatePacketTest(InvitationStatus.RESPONDED, InvitationStatus.RESPONDED);
        InvestigatorRegistration investigatorRegistration = Iterables.getOnlyElement(protocol
                .getCurrentInvestigatorRegistrations());
        SubInvestigatorRegistration subinvestigatorRegistration = Iterables.getOnlyElement(protocol
                .getSubinvestigatorRegistrations());
        assertEquals(RegistrationStatus.IN_PROGRESS, investigatorRegistration.getStatus());
        assertEquals(InvitationStatus.REACTIVATED, investigatorRegistration.getInvitation().getInvitationStatus());
        assertEquals(DateUtils.truncate(new Date(), Calendar.DATE),
                DateUtils.truncate(investigatorRegistration.getInvitation().getInvitationChangeDate(), Calendar.DATE));
        checkRegistrationFormsForStatus(investigatorRegistration, IN_PROGRESS);
        assertEquals(RegistrationStatus.IN_PROGRESS, subinvestigatorRegistration.getStatus());
        assertEquals(InvitationStatus.REACTIVATED, subinvestigatorRegistration.getInvitation().getInvitationStatus());
        assertEquals(DateUtils.truncate(new Date(), Calendar.DATE), DateUtils.truncate(subinvestigatorRegistration
                .getInvitation().getInvitationChangeDate(), Calendar.DATE));
        checkRegistrationFormsForStatus(subinvestigatorRegistration, IN_PROGRESS);
    }

    private Protocol peformReactivatePacketTest(InvitationStatus investigatorStatus,
                                                InvitationStatus subinvestigatorStatus) {
        String comments = "These are the comments of failure";
        Protocol protocol = ProtocolFactory.getInstance().createWithForms();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                InvestigatorProfileFactory.getInstance().create(), protocol);
        registration.getInvitation().setInvitationStatus(investigatorStatus);
        registration.getInvitation().setInvitationChangeDate(DateUtils.addDays(new Date(), -10));
        SubInvestigatorRegistration subRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(registration);
        subRegistration.getInvitation().setInvitationStatus(subinvestigatorStatus);
        subRegistration.getInvitation().setInvitationChangeDate(DateUtils.addDays(new Date(), -10));
        bean.deactivatePacket(registration, comments);
        bean.reactivatePacket(registration, ValueGenerator.getUniqueString());
        verify(mockSponsorNotificationService, times(2)).notifyOfReactivation(any(AbstractProtocolRegistration.class), anyString());
        return protocol;
    }

    @Test
    public void testReactivatePacket_NoResponse() {
        Protocol protocol = peformReactivatePacketTest(InvitationStatus.NO_RESPONSE, InvitationStatus.NOT_INVITED);
        InvestigatorRegistration investigatorRegistration = Iterables.getOnlyElement(protocol
                .getCurrentInvestigatorRegistrations());
        SubInvestigatorRegistration subinvestigatorRegistration = Iterables.getOnlyElement(protocol
                .getSubinvestigatorRegistrations());
        assertEquals(RegistrationStatus.NOT_STARTED, investigatorRegistration.getStatus());
        assertEquals(InvitationStatus.NO_RESPONSE, investigatorRegistration.getInvitation().getInvitationStatus());
        assertEquals(DateUtils.truncate(DateUtils.addDays(new Date(), -10), Calendar.DATE),
                DateUtils.truncate(investigatorRegistration.getInvitation().getInvitationChangeDate(), Calendar.DATE));
        checkRegistrationFormsForStatus(investigatorRegistration, NOT_STARTED);
        assertEquals(RegistrationStatus.NOT_STARTED, subinvestigatorRegistration.getStatus());
        assertEquals(InvitationStatus.NOT_INVITED, subinvestigatorRegistration.getInvitation().getInvitationStatus());
        assertEquals(DateUtils.truncate(DateUtils.addDays(new Date(), -10), Calendar.DATE), DateUtils.truncate(
                subinvestigatorRegistration.getInvitation().getInvitationChangeDate(), Calendar.DATE));
        checkRegistrationFormsForStatus(subinvestigatorRegistration, NOT_STARTED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReactivatePacketNotInactive() {
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration();
        InvestigatorRegistration registration = subInvestigatorRegistration.getPrimaryRegistration();
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        bean.reactivatePacket(registration, "");
    }

    @Test
    public void testReactivatePacket_Approved() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                RegistrationStatus.APPROVED);
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(registration);
        subInvestigatorRegistration.setStatus(RegistrationStatus.APPROVED);
        registration.setStatus(RegistrationStatus.INACTIVE);
        subInvestigatorRegistration.setStatus(RegistrationStatus.INACTIVE);
        bean.reactivatePacket(registration, "");

        assertEquals(RegistrationStatus.INACTIVE, registration.getStatus());
        assertEquals(RegistrationStatus.INACTIVE, subInvestigatorRegistration.getStatus());

        InvestigatorRegistration newRegistration = (InvestigatorRegistration) registration.getCurrentRegistration();
        SubInvestigatorRegistration newSubInvestigatorRegistration = (SubInvestigatorRegistration) subInvestigatorRegistration
                .getCurrentRegistration();
        checkApprovedRegistrationReactivation(newRegistration);
        checkApprovedRegistrationReactivation(newSubInvestigatorRegistration);
    }

    private void checkApprovedRegistrationReactivation(AbstractProtocolRegistration registration) {
        assertEquals(RegistrationStatus.IN_PROGRESS, registration.getStatus());
        assertEquals(InvitationStatus.REACTIVATED, registration.getInvitation().getInvitationStatus());
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (form.isReviewRequired()) {
                assertEquals(FormStatus.IN_PROGRESS, form.getFormStatus());
            }
        }
    }

    @Test
    public void testUpdateProtocol_Unchanged() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        Protocol unmodifiedProtocol = protocol.createCopy();
        bean.updateProtocol(protocol, unmodifiedProtocol, "No changes");
        verify(mockSession, never()).save(unmodifiedProtocol);
    }

    @Test
    public void testUpdateProtocol_EmailRequired() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        InvestigatorRegistration registration = createTestRegistrationForAcceptance();
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        Protocol modifiedProtocol = protocol.createCopy();
        modifiedProtocol.addRegistration(registration);
        setFormsStatus(registration, FormStatus.COMPLETED);
        modifiedProtocol.setProtocolTitle("New Title");
        bean.updateProtocol(protocol, modifiedProtocol, "comments");
        verify(mockSponsorNotificationService).sendProtocolUpdateEmail(any(AbstractProtocolRegistration.class),
                any(ProtocolRevision.class));
        verifyFormStatuses(registration, FormStatus.COMPLETED);
    }

    private void setFormsStatus(InvestigatorRegistration registration, FormStatus formStatus) {
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            form.setFormStatus(formStatus);
        }
    }

    private void verifyFormStatuses(InvestigatorRegistration registration, FormStatus formStatus) {
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            assertEquals(formStatus, form.getFormStatus());
        }
    }

    @Test
    public void testUpdateProtocol_ResubmitRequired() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        InvestigatorRegistration registration = createTestRegistrationForAcceptance();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        Protocol modifiedProtocol = protocol.createCopy();
        modifiedProtocol.addRegistration(registration);
        modifiedProtocol.setProtocolTitle("New Title");
        setFormsStatus(registration, FormStatus.COMPLETED);
        bean.updateProtocol(protocol, modifiedProtocol, "comments");
        verify(mockSponsorNotificationService).sendProtocolUpdateEmail(any(AbstractProtocolRegistration.class),
                any(ProtocolRevision.class));
        verifyFormStatuses(registration, FormStatus.IN_PROGRESS);
    }

    @Test
    public void testUpdateProtocol_RemovedForms() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        InvestigatorRegistration registration = createTestRegistrationForAcceptance();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        Protocol modifiedProtocol = protocol.createCopy();
        protocol.addRegistration(registration);
        modifiedProtocol.addRegistration(registration);

        Set<AbstractRegistrationForm> forms = registration.getForms();
        FormType notRemovedFormType = modifiedProtocol.getRegistrationConfiguration().getAssociatedFormTypes().get(0);
        for (FormType formType : modifiedProtocol.getRegistrationConfiguration().getAssociatedFormTypes()) {
            modifiedProtocol.getRegistrationConfiguration().setInvestigatorOptionality(formType, FormOptionality.NONE);
        }
        modifiedProtocol.getRegistrationConfiguration().setInvestigatorOptionality(notRemovedFormType,
                FormOptionality.REQUIRED);

        bean.updateProtocol(protocol, modifiedProtocol, "comments");
        verify(mockSponsorNotificationService).sendProtocolUpdateEmail(any(AbstractProtocolRegistration.class),
                any(ProtocolRevision.class));
        verify(mockSession, times(forms.size() - 1)).delete(any(AbstractRegistrationForm.class));
        assertEquals(1, registration.getForms().size());
        assertNotNull(registration.getForm(notRemovedFormType));
    }

    @Test
    public void testUpdateProtocol_NoComment() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        Protocol modifiedProtocol = protocol.createCopy();
        modifiedProtocol.setProtocolTitle("New Title");
        bean.updateProtocol(protocol, modifiedProtocol, "comments");
        verify(mockProtocolValidationService).validateChanges(modifiedProtocol, "comments");
    }

    @Test
    public void testUpdateProtocol_InvalidInvestigatorFormOptionalities() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        Protocol modifiedProtocol = protocol.createCopy();
        modifiedProtocol.getRegistrationConfiguration().getInvestigatorConfiguration().getFormOptionalities().clear();
        modifiedProtocol.getRegistrationConfiguration().setInvestigatorOptionality(new FormType("name"),
                FormOptionality.OPTIONAL);
        bean.updateProtocol(protocol, modifiedProtocol, "comments");
        verify(mockProtocolValidationService).validateChanges(modifiedProtocol, "comments");
    }

    @Test
    public void testUpdateProtocol_InvalidSubinvestigatorFormOptionalities() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        Protocol modifiedProtocol = protocol.createCopy();
        modifiedProtocol.getRegistrationConfiguration().getSubinvestigatorConfiguration().getFormOptionalities()
                .clear();
        modifiedProtocol.getRegistrationConfiguration().setSubinvestigatorOptionality(new FormType("name"),
                FormOptionality.NONE);
        bean.updateProtocol(protocol, modifiedProtocol, "comments");
        verify(mockProtocolValidationService).validateChanges(modifiedProtocol, "comments");
    }

    @Test
    public void testUpdateProtocol_NoForms() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        Protocol modifiedProtocol = protocol.createCopy();
        modifiedProtocol.getRegistrationConfiguration().getInvestigatorConfiguration().getFormOptionalities().clear();
        modifiedProtocol.getRegistrationConfiguration().getSubinvestigatorConfiguration().getFormOptionalities()
                .clear();
        bean.updateProtocol(protocol, modifiedProtocol, "comments");
        verify(mockProtocolValidationService).validateChanges(modifiedProtocol, "comments");
    }

    @Test
    public void testRemovePacket() {
        InvestigatorRegistration registration = createTestRegistrationForAcceptance();
        registration.setStatus(RegistrationStatus.ACCEPTED);
        bean.removePacket(registration);
        verify(mockRegistrationService).delete(registration);
        verify(mockSponsorNotificationService).sendPacketRemovedEmail(registration);
    }

    @Test
    public void testRemovePacket_Approved() {
        InvestigatorRegistration registration = createTestRegistrationForAcceptance();
        registration.setStatus(RegistrationStatus.APPROVED);
        try {
            bean.removePacket(registration);
            fail("Should have thrown an exception");
        } catch (IllegalStateException e) {
            verifyZeroInteractions(mockSponsorNotificationService);
        }
    }
}
