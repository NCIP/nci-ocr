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
package gov.nih.nci.firebird.service.sponsor;

import static gov.nih.nci.firebird.nes.NesIdTestUtil.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.cagrid.GridGrouperService;
import gov.nih.nci.firebird.cagrid.GridInvocationException;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.SponsorRole;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.service.messages.FirebirdTemplateParameter;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.user.FirebirdUserService;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Provider;

public class SponsorServiceBeanTest {

    private static final String SPONSOR_MAILBOX_ADDRESS = "sponsorMail@example.com";
    private static final String ANNUAL_REGISTRATION_SPONSOR_MAILBOX_ADDRESS = "annualMail@example.com";

    @Mock
    private OrganizationService mockOrganizationService;
    @Mock
    private TemplateService mockTemplateService;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private GridGrouperService mockGridGrouperService;
    @Mock
    private FirebirdUserService mockFirebirdUserService;
    @Mock
    private Provider<Session> mockSessionProvider;
    @Mock
    private Session mockSession;
    private Organization annualRegistrationSponsor = OrganizationFactory.getInstance().create();
    private Organization protocolRegistrationSponsor = OrganizationFactory.getInstance().create();
    private SponsorServiceBean bean;

    @Before
    public void setUpBean() throws Exception {
        MockitoAnnotations.initMocks(this);
        bean = new SponsorServiceBean();
        bean.setEmailService(mockEmailService);
        bean.setTemplateService(mockTemplateService);
        bean.setGridGrouperService(mockGridGrouperService);
        bean.setFirebirdUserService(mockFirebirdUserService);
        bean.setSessionProvider(mockSessionProvider);
        bean.setOrganizationService(mockOrganizationService);
        when(mockSessionProvider.get()).thenReturn(mockSession);
        bean.setSponsorWithAnnualRegistrationsExternalId(annualRegistrationSponsor.getExternalId());
        when(mockOrganizationService.getByExternalId(annualRegistrationSponsor.getExternalId())).thenReturn(
                annualRegistrationSponsor);
    }

    @Test
    public void testNotifySubmittedRegistration_AbstractProtocolRegistration() {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        protocol.setSponsor(protocolRegistrationSponsor);
        setUpTestSponsorEmailMapping();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                profile, protocol);
        registration.setStatus(RegistrationStatus.SUBMITTED);

        bean.notifySubmittedRegistration(registration);
        verify(mockEmailService).sendMessage(eq(SPONSOR_MAILBOX_ADDRESS), anyCollectionOf(String.class), anyString(),
                any(FirebirdMessage.class));
        verify(mockTemplateService).generateMessage(eq(FirebirdMessageTemplate.SPONSOR_SUBMISSION_NOTIFICATION_EMAIL),
                anyMapOf(FirebirdTemplateParameter.class, Object.class));
    }

    @Test
    public void testNotifySubmittedRegistration_AnnualRegistration() {
        setUpTestSponsorEmailMapping();
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        registration.setStatus(RegistrationStatus.SUBMITTED);
        bean.notifySubmittedRegistration(registration);
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.SPONSOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL),
                anyMapOf(FirebirdTemplateParameter.class, Object.class));
        verify(mockEmailService).sendMessage(eq(ANNUAL_REGISTRATION_SPONSOR_MAILBOX_ADDRESS),
                anyCollectionOf(String.class), anyString(), any(FirebirdMessage.class));
    }

    private void setUpTestSponsorEmailMapping() {
        Map<String, String> sponsorEmails = Maps.newHashMap();
        sponsorEmails.put(protocolRegistrationSponsor.getExternalId(), SPONSOR_MAILBOX_ADDRESS);
        sponsorEmails.put(annualRegistrationSponsor.getExternalId(), ANNUAL_REGISTRATION_SPONSOR_MAILBOX_ADDRESS);
        bean.setSponsorEmailMappings(sponsorEmails);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifySubmittedRegistration_NoSponsorMailbox() {
        InvestigatorProfile inv = InvestigatorProfileFactory.getInstance().create();
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        protocol.setSponsor(protocolRegistrationSponsor);
        InvestigatorRegistration reg = RegistrationFactory.getInstance().createInvestigatorRegistration(inv, protocol);
        reg.setStatus(RegistrationStatus.SUBMITTED);
        bean.notifySubmittedRegistration(reg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifySubmittedRegistration_BadStatus() {
        InvestigatorRegistration reg = RegistrationFactory.getInstance().createInvestigatorRegistration();
        reg.setStatus(RegistrationStatus.NOT_STARTED);
        bean.notifySubmittedRegistration(reg);
    }

    @Test
    public void testRemoveSponsorDelegateRole() throws GridInvocationException {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        setUpTestSponsorEmailMapping();
        SponsorRole delegateRole = user.addSponsorDelegateRole(protocolRegistrationSponsor);
        assertTrue(user.getSponsorRoles().contains(delegateRole));
        bean.removeSponsorDelegateRole(delegateRole);
        verify(mockFirebirdUserService).save(user);
        verify(mockGridGrouperService).removeGridUserFromGroup(user.getUsername(),
                delegateRole.getSponsorOrganizationGroupName());
        verify(mockGridGrouperService).removeGridUserFromGroup(user.getUsername(),
                delegateRole.getVerifiedSponsorGroupName());
        assertFalse(user.getSponsorRoles().contains(delegateRole));
        verify(mockEmailService).sendMessage(eq(user.getPerson().getEmail()), anyCollectionOf(String.class),
                anyString(), any(FirebirdMessage.class));
        verify(mockTemplateService).generateMessage(eq(FirebirdMessageTemplate.DELEGATE_REMOVAL_NOTIFICATION_EMAIL),
                anyMapOf(FirebirdTemplateParameter.class, Object.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSponsorDelegateRole_Representative() throws GridInvocationException {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        SponsorRole delegateRole = user.addSponsorRepresentativeRole(sponsor);
        bean.removeSponsorDelegateRole(delegateRole);
    }

    @Test
    public void testGetSponsorDelegateRoles() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        SponsorRole delegateRole = user.addSponsorDelegateRole(sponsor);
        List<SponsorRole> sponsorDelegateRoles = Lists.newArrayList(delegateRole);

        Criteria mockCriteria = mock(Criteria.class);
        when(mockSession.createCriteria(SponsorRole.class)).thenReturn(mockCriteria);
        when(mockCriteria.add(any(Criterion.class))).thenReturn(mockCriteria);
        when(mockCriteria.list()).thenReturn(sponsorDelegateRoles);

        assertEquals(sponsorDelegateRoles, bean.getSponsorDelegateRoles(null));
    }

    @Test
    public void testGetSponsorRole() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        SponsorRole delegateRole = user.addSponsorDelegateRole(sponsor);
        when(mockSession.get(SponsorRole.class, 1L)).thenReturn(delegateRole);
        assertEquals(delegateRole, bean.getSponsorRole(1L));
    }

    @Test
    public void testGetSponsorEmailAddress() {
        setUpTestSponsorEmailMapping();
        assertEquals(SPONSOR_MAILBOX_ADDRESS, bean.getSponsorEmailAddress(protocolRegistrationSponsor));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSponsorEmailAddress_NoMatchingSponsor() {
        setUpTestSponsorEmailMapping();
        Organization sponsor = OrganizationFactory.getInstance().create();
        bean.getSponsorEmailAddress(sponsor);
    }

    @Test
    public void testGetSponsorOrganizations() throws Exception {
        bean.setValidSponsorExternalIds(Sets.newHashSet("sponsor1", "sponsor2"));
        Organization sponsor1 = OrganizationFactory.getInstance().create();
        Organization sponsor2 = OrganizationFactory.getInstance().create();
        when(mockOrganizationService.getByExternalId(anyString())).thenReturn(sponsor1).thenReturn(sponsor2);
        assertEquals(2, bean.getSponsorOrganizations().size());
    }

    @Test
    public void testInitializeSponsorOrganizations() throws GridInvocationException, Exception {
        bean.setSponsorWithProtocolRegistrationsExternalIds(Sets.newHashSet("existingSponsor", "newSponsor"));
        final Organization existingSponsor = OrganizationFactory.getInstance().create();
        final Organization newSponsor = OrganizationFactory.getInstance().create();
        Set<String> existingSponsorGroupNames = getSponsorGroupNames(existingSponsor);
        Set<String> newSponsorGroupNames = getSponsorGroupNames(newSponsor);
        Set<String> allSponsorGroupNames = Sets.union(existingSponsorGroupNames, newSponsorGroupNames);
        when(mockOrganizationService.getByExternalId(anyString())).thenReturn(existingSponsor).thenReturn(newSponsor);
        when(mockGridGrouperService.doesGroupExist(anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String groupName = (String) invocation.getArguments()[0];
                return groupName.endsWith("_" + getNesIdExtension(existingSponsor));
            }
        });

        bean.initializeSponsorOrganizations();
        verify(mockOrganizationService, times(2)).getByExternalId(anyString());
        checkDoesGroupExistCalled(allSponsorGroupNames);
        checkCreateGroupCalled(newSponsorGroupNames);
        checkAddGridGroupToGroupCalled(newSponsorGroupNames);
    }

    public Set<String> getSponsorGroupNames(final Organization sponsor) {
        String sponsorGroupName = "sponsor_" + getNesIdExtension(sponsor);
        String verifiedSponsorGroupName = "verified_sponsor_" + getNesIdExtension(sponsor);
        String sponsorDelegateGroupName = "sponsor_delegate_" + getNesIdExtension(sponsor);
        String verifiedSponsorDelegateGroupName = "verified_sponsor_delegate_" + getNesIdExtension(sponsor);
        Set<String> existingSponsorGroupNames = Sets.newHashSet(sponsorGroupName, sponsorDelegateGroupName,
                verifiedSponsorGroupName, verifiedSponsorDelegateGroupName);
        return existingSponsorGroupNames;
    }

    private void checkDoesGroupExistCalled(Set<String> groupNames) throws GridInvocationException {
        ArgumentCaptor<String> groupNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockGridGrouperService, times(groupNames.size())).doesGroupExist(groupNameCaptor.capture());
        assertEquals(groupNames.size(), groupNameCaptor.getAllValues().size());
        assertTrue(groupNameCaptor.getAllValues().containsAll(groupNames));
    }

    private void checkCreateGroupCalled(Set<String> groupNames) throws GridInvocationException {
        ArgumentCaptor<String> groupNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockGridGrouperService, times(groupNames.size())).createGroup(groupNameCaptor.capture(), anyString());
        assertEquals(groupNames.size(), groupNameCaptor.getAllValues().size());
        assertTrue(groupNameCaptor.getAllValues().containsAll(groupNames));
    }

    private void checkAddGridGroupToGroupCalled(Set<String> groupNames) throws GridInvocationException {
        ArgumentCaptor<String> groupNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockGridGrouperService, times(groupNames.size())).addGridGroupToGroup(groupNameCaptor.capture(),
                anyString());
        assertEquals(groupNames.size(), groupNameCaptor.getAllValues().size());
        assertTrue(groupNameCaptor.getAllValues().containsAll(groupNames));
    }

    @Test
    public void testGetSponsorOrganizationWithAnnualRegistrations() throws Exception {
        String sponsorWithAnnualRegistrationExternalId = "ctep";
        bean.setSponsorWithAnnualRegistrationsExternalId(sponsorWithAnnualRegistrationExternalId);
        OrganizationService mockOrganizationService = mock(OrganizationService.class);
        bean.setOrganizationService(mockOrganizationService);
        Organization sponsorOrganization = new Organization();
        when(mockOrganizationService.getByExternalId(sponsorWithAnnualRegistrationExternalId)).thenReturn(sponsorOrganization);
        assertSame(sponsorOrganization, bean.getSponsorOrganizationWithAnnualRegistrations());
        assertSame(sponsorOrganization, bean.getSponsorOrganizationWithAnnualRegistrations());
        verify(mockOrganizationService, times(1)).getByExternalId(sponsorWithAnnualRegistrationExternalId);
    }

    @Test
    public void testGetSponsorOrganizationWithAnnualRegistrations_NullId() {
        bean.setSponsorWithAnnualRegistrationsExternalId(null);
        assertNull(bean.getSponsorOrganizationWithAnnualRegistrations());
    }

}
