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
package gov.nih.nci.firebird.service.account;

import static gov.nih.nci.firebird.data.user.UserRoleType.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static gov.nih.nci.firebird.nes.NesIdTestUtil.getNesIdExtension;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.firebird.cagrid.GridAuthenticationService;
import gov.nih.nci.firebird.cagrid.GridGrouperService;
import gov.nih.nci.firebird.cagrid.GridInvocationException;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.cagrid.UserSessionInformationFactory;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus;
import gov.nih.nci.firebird.data.user.RegistrationCoordinatorRole;
import gov.nih.nci.firebird.data.user.SponsorRole;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.user.FirebirdUserService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.dorian.federation.TrustedIdentityProvider;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Provider;

@SuppressWarnings("unchecked")
public class AccountManagementServiceBeanTest {

    private static final String SUPPORT_EMAIL_ADDRESS = "firebird-support@5amsolutions.com";
    private AccountManagementServiceBean bean = new AccountManagementServiceBean();
    private Provider<Session> mockSessionProvider = mock(Provider.class);
    private Session mockSession = mock(Session.class);
    private GridGrouperService mockGridGrouperService = mock(GridGrouperService.class);
    private GridAuthenticationService mockAuthenticationService = mock(GridAuthenticationService.class);
    private EmailService mockEmailService = mock(EmailService.class);
    private TemplateService mockTemplateService = mock(TemplateService.class);
    private CountryLookupService mockCountryService = mock(CountryLookupService.class);
    private TrustedIdentityProvider identityProvider = new TrustedIdentityProvider();
    private FirebirdUserService mockUserService = mock(FirebirdUserService.class);
    private SponsorService mockSponsorService = mock(SponsorService.class);
    private Provider<AccountConfigurationHelper> mockConfigurationHelperProvider = mock(Provider.class);
    private AccountConfigurationHelper mockConfigurationHelper = mock(AccountConfigurationHelper.class);
    FirebirdMessage mockNewUserMessage = mock(FirebirdMessage.class);
    FirebirdMessage mockUserRegistrationMessage = mock(FirebirdMessage.class);
    FirebirdMessage mockRequestAccountMessage = mock(FirebirdMessage.class);
    FirebirdMessage mockRegistrationCoordinatorRequestMessage = mock(FirebirdMessage.class);

    @Before
    public void setUp() throws MalformedURIException, RemoteException, ResourcePropertyRetrievalException,
            GridInvocationException {
        setUpMockSessionProvider();
        setUpMockConfigurationHelperProvider();
        setUpMockGridAccountServices();
        setUpMockEmails();
        configureBeanWithMockServices();
    }

    private void setUpMockSessionProvider() {
        bean.setSessionProvider(mockSessionProvider);
        when(mockSessionProvider.get()).thenReturn(mockSession);
    }

    private void setUpMockConfigurationHelperProvider() {
        bean.setConfigurationHelperProvider(mockConfigurationHelperProvider);
        when(mockConfigurationHelperProvider.get()).thenReturn(mockConfigurationHelper);
    }

    private void setUpMockGridAccountServices() throws GridInvocationException {
        UserSessionInformation sessionInformation = mock(UserSessionInformation.class);
        when(mockAuthenticationService.authenticateUser("fbsponsor", "password", "idpUrl")).thenReturn(
                sessionInformation);
        identityProvider.setName("Dorian");
        identityProvider.setDisplayName("Dorian");
        identityProvider.setAuthenticationServiceURL("idpUrl");
        when(mockAuthenticationService.getIdentityProviders()).thenReturn(Collections.singletonList(identityProvider));
    }

    private void setUpMockEmails() {
        when(
                mockTemplateService.generateMessage(eq(FirebirdMessageTemplate.REGISTRATION_COORDINATOR_REQUEST_EMAIL),
                        any(Map.class))).thenReturn(mockRegistrationCoordinatorRequestMessage);
    }

    private void configureBeanWithMockServices() {
        bean.setEmailService(mockEmailService);
        bean.setTemplateService(mockTemplateService);
        bean.setGridGrouperService(mockGridGrouperService);
        Country unitedStates = new Country("United States", "840", "US", FirebirdConstants.US_COUNTRY_CODE);
        when(mockCountryService.getByThreeLetterCode(FirebirdConstants.US_COUNTRY_CODE)).thenReturn(unitedStates);
        bean.setUserService(mockUserService);
        bean.setSupportEmailAddress(SUPPORT_EMAIL_ADDRESS);
        bean.setSponsorService(mockSponsorService);
    }

    @Test
    public void testCreateConfigurationForNewUser_AllAvailableRoles() {
        Organization sponsor1 = OrganizationFactory.getInstance().create();
        Organization sponsor2 = OrganizationFactory.getInstance().create();
        when(mockSponsorService.getSponsorOrganizations()).thenReturn(Sets.newHashSet(sponsor1, sponsor2));
        Set<String> groupNames = Sets.newHashSet(INVESTIGATOR.getGroupName(), REGISTRATION_COORDINATOR.getGroupName(),
                SPONSOR.getGroupName() + "_" + getNesIdExtension(sponsor1),
                SPONSOR_DELEGATE.getGroupName() + "_" + getNesIdExtension(sponsor2));
        UserSessionInformation sessionInformation = UserSessionInformationFactory.getInstance().create("username",
                groupNames);
        AccountConfigurationData data = bean.createConfigurationForNewUser(sessionInformation);
        assertNotNull(data);
        assertEquals(sessionInformation.getFullyQualifiedUsername(), data.getUsername());
        assertNull(data.getPerson());
        assertNull(data.getPrimaryOrganization());
        assertTrue(data.getRoles().containsAll(UserRoleType.STANDARD_ROLES));
        assertEquals(1, data.getSponsorOrganizations().size());
        assertTrue(data.getSponsorOrganizations().contains(sponsor1));
        assertEquals(1, data.getDelegateOrganizations().size());
        assertTrue(data.getDelegateOrganizations().contains(sponsor2));
    }

    @Test
    public void testCreateConfigurationForNewUser_NoRoles() {
        UserSessionInformation sessionInformation = UserSessionInformationFactory.getInstance().create("username");
        AccountConfigurationData data = bean.createConfigurationForNewUser(sessionInformation);
        assertNotNull(data);
        assertTrue(data.getRoles().isEmpty());
        assertNull(data.getPerson());
        assertNull(data.getPrimaryOrganization());
        assertTrue(data.getDelegateOrganizations().isEmpty());
        assertTrue(data.getSponsorOrganizations().isEmpty());
        assertTrue(data.getSelectedInvestigators().isEmpty());
    }

    @Test
    public void testCreate() throws ValidationException {
        AccountConfigurationData configurationData = new AccountConfigurationData();
        bean.create(configurationData);
        verify(mockConfigurationHelper).createUser(configurationData);
    }

    @Test
    public void testAddRoles() throws GridInvocationException, ValidationException {
        AccountConfigurationData configurationData = new AccountConfigurationData();
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        bean.addRoles(user, configurationData);
        verify(mockConfigurationHelper).addRoles(user, configurationData);
    }

    @Test
    public void testAcceptCoordinatorRequest() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigatorProfile);
        bean.acceptCoordinatorRequest(managedInvestigator);
        assertTrue(managedInvestigator.isApproved());
        assertEquals(ManagedInvestigatorStatus.APPROVED, managedInvestigator.getStatus());
        verify(mockSession).update(managedInvestigator);
        verify(mockTemplateService).generateMessage(eq(FirebirdMessageTemplate.COORDINATOR_REQUEST_UPDATED_EMAIL),
                any(Map.class));
        verify(mockEmailService).sendMessage(eq(coordinator.getPerson().getEmail()), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testRejectCoordinatorRequest() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
        investigatorProfile.setId(1L);
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigatorProfile);
        bean.rejectCoordinatorRequest(managedInvestigator);
        assertFalse(investigatorProfile.getRegistrationCoordinatorMappings().contains(managedInvestigator));
        assertFalse(coordinator.getRegistrationCoordinatorRole().getManagedInvestigators().contains(managedInvestigator));
        assertEquals(ManagedInvestigatorStatus.AWAITING_APPROVAL, managedInvestigator.getStatus());
        verify(mockSession).delete(managedInvestigator);
        verify(mockSession).update(coordinator);
        verify(mockSession).update(investigatorProfile);
        verify(mockTemplateService).generateMessage(eq(FirebirdMessageTemplate.COORDINATOR_REQUEST_UPDATED_EMAIL),
                any(Map.class));
        verify(mockEmailService).sendMessage(eq(coordinator.getPerson().getEmail()), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testAddManagedInvestigators() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        List<InvestigatorProfile> investigatorProfiles = Lists.newArrayList();
        investigatorProfiles.add(InvestigatorProfileFactory.getInstance().create());
        investigatorProfiles.add(InvestigatorProfileFactory.getInstance().create());
        bean.addManagedInvestigators(coordinator, investigatorProfiles);
        verify(mockConfigurationHelper, times(investigatorProfiles.size())).notifyOfRegistrationCoordinatorRequest(
                any(ManagedInvestigator.class));
        verify(mockUserService).save(coordinator);
        assertEquals(2, coordinator.getRegistrationCoordinatorRole().getManagedInvestigators().size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAddManagedInvestigators_NoRole() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        List<InvestigatorProfile> investigatorProfiles = Lists.newArrayList();
        bean.addManagedInvestigators(coordinator, investigatorProfiles);
    }

    @Test
    public void testAddManagedInvestigators_EmptyList() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        List<InvestigatorProfile> investigatorProfiles = Lists.newArrayList();
        bean.addManagedInvestigators(coordinator, investigatorProfiles);
        verify(mockEmailService, never()).sendMessage(isNull(String.class), anyCollection(), isNull(String.class), any(FirebirdMessage.class));
        verify(mockUserService, never()).save(coordinator);
        assertEquals(0, coordinator.getRegistrationCoordinatorRole().getManagedInvestigators().size());
    }

    @Test
        public void testRemoveManagedInvestigator() {
            FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
            InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
            investigatorProfile.setId(1L);
            ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigatorProfile);
            bean.removeManagedInvestigator(managedInvestigator);
            assertFalse(investigatorProfile.getRegistrationCoordinatorMappings().contains(managedInvestigator));
            assertFalse(coordinator.getRegistrationCoordinatorRole().getManagedInvestigators().contains(managedInvestigator));
            verify(mockSession).delete(managedInvestigator);
            verify(mockSession).update(coordinator);
            verify(mockSession).update(investigatorProfile);
            verify(mockTemplateService).generateMessage(eq(FirebirdMessageTemplate.COORDINATOR_REMOVED_EMAIL),
                    any(Map.class));
            verify(mockEmailService).sendMessage(eq(coordinator.getPerson().getEmail()), any(Collection.class),
                    isNull(String.class), any(FirebirdMessage.class));
        }

    @Test
    public void testSuspendCoordinatorRegistrationAccess() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigatorProfile);
        managedInvestigator.setStatus(ManagedInvestigatorStatus.APPROVED);
        bean.suspendCoordinatorRegistrationAccess(managedInvestigator);
        assertTrue(managedInvestigator.isSuspendedRegistrationAccess());
        assertEquals(ManagedInvestigatorStatus.SUSPENDED_FROM_REGISTRATIONS, managedInvestigator.getStatus());
        verify(mockSession).update(managedInvestigator);
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.COORDINATOR_SUSPENDED_FROM_REGISTRATIONS_EMAIL), any(Map.class));
        verify(mockEmailService).sendMessage(eq(coordinator.getPerson().getEmail()), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testActivateCoordinatorRegistrationAccess() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigatorProfile);
        managedInvestigator.setStatus(ManagedInvestigatorStatus.SUSPENDED_FROM_REGISTRATIONS);
        bean.activateCoordinatorRegistrationAccess(managedInvestigator);
        assertEquals(ManagedInvestigatorStatus.APPROVED, managedInvestigator.getStatus());
        assertFalse(managedInvestigator.isSuspendedRegistrationAccess());
        verify(mockSession).update(managedInvestigator);
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.COORDINATOR_UNSUSPENDED_FROM_REGISTRATIONS_EMAIL), any(Map.class));
        verify(mockEmailService).sendMessage(eq(coordinator.getPerson().getEmail()), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testActivateCoordinatorRegistrationAccess_FromSuspendedStatus() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigatorProfile);
        managedInvestigator.setStatus(ManagedInvestigatorStatus.SUSPENDED);
        bean.activateCoordinatorRegistrationAccess(managedInvestigator);
        assertEquals(ManagedInvestigatorStatus.SUSPENDED_FROM_PROFILE, managedInvestigator.getStatus());
        assertFalse(managedInvestigator.isSuspendedRegistrationAccess());
        verify(mockSession).update(managedInvestigator);
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.COORDINATOR_UNSUSPENDED_FROM_REGISTRATIONS_EMAIL), any(Map.class));
        verify(mockEmailService).sendMessage(eq(coordinator.getPerson().getEmail()), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testSuspendCoordinatorProfileAccess() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigatorProfile);
        managedInvestigator.setStatus(ManagedInvestigatorStatus.APPROVED);
        bean.suspendCoordinatorProfileAccess(managedInvestigator);
        assertTrue(managedInvestigator.isSuspendedProfileAccess());
        assertEquals(ManagedInvestigatorStatus.SUSPENDED_FROM_PROFILE, managedInvestigator.getStatus());
        verify(mockSession).update(managedInvestigator);
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.COORDINATOR_SUSPENDED_FROM_PROFILE_EMAIL), any(Map.class));
        verify(mockEmailService).sendMessage(eq(coordinator.getPerson().getEmail()), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testSuspendCoordinatorProfileAndRegistrationAccess() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigatorProfile);
        managedInvestigator.setStatus(ManagedInvestigatorStatus.APPROVED);
        bean.suspendCoordinatorProfileAccess(managedInvestigator);
        bean.suspendCoordinatorRegistrationAccess(managedInvestigator);
        assertTrue(managedInvestigator.isSuspendedProfileAccess());
        assertTrue(managedInvestigator.isSuspendedRegistrationAccess());
        assertEquals(ManagedInvestigatorStatus.SUSPENDED, managedInvestigator.getStatus());
        verify(mockSession, times(2)).update(managedInvestigator);
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.COORDINATOR_SUSPENDED_FROM_PROFILE_EMAIL), any(Map.class));
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.COORDINATOR_SUSPENDED_FROM_REGISTRATIONS_EMAIL), any(Map.class));
        verify(mockEmailService, times(2)).sendMessage(eq(coordinator.getPerson().getEmail()), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testActivateCoordinatorProfileAccess() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigatorProfile);
        managedInvestigator.setStatus(ManagedInvestigatorStatus.SUSPENDED_FROM_PROFILE);
        bean.activateCoordinatorProfileAccess(managedInvestigator);
        assertEquals(ManagedInvestigatorStatus.APPROVED, managedInvestigator.getStatus());
        assertFalse(managedInvestigator.isSuspendedProfileAccess());
        verify(mockSession).update(managedInvestigator);
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.COORDINATOR_UNSUSPENDED_FROM_PROFILE_EMAIL), any(Map.class));
        verify(mockEmailService).sendMessage(eq(coordinator.getPerson().getEmail()), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testActivateCoordinatorProfileAccess_FromSuspendedStatus() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigatorProfile);
        managedInvestigator.setStatus(ManagedInvestigatorStatus.SUSPENDED);
        bean.activateCoordinatorProfileAccess(managedInvestigator);
        assertEquals(ManagedInvestigatorStatus.SUSPENDED_FROM_REGISTRATIONS, managedInvestigator.getStatus());
        assertFalse(managedInvestigator.isSuspendedProfileAccess());
        verify(mockSession).update(managedInvestigator);
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.COORDINATOR_UNSUSPENDED_FROM_PROFILE_EMAIL), any(Map.class));
        verify(mockEmailService).sendMessage(eq(coordinator.getPerson().getEmail()), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testRequestAccount() {
        AccountConfigurationData configurationData = AccountConfigurationDataFactory.getInstance().create();
        requestAccount(configurationData);
    }

    private void requestAccount(AccountConfigurationData configurationData) {
        bean.requestAccount(configurationData);
        FirebirdMessageTemplate requestToSupportTemplate;
        if (configurationData.isExistingLdapAccount()) {
            requestToSupportTemplate = FirebirdMessageTemplate.ACCOUNT_REQUEST_EXISTING_LDAP_ACCOUNT_EMAIL;
        } else {
            requestToSupportTemplate = FirebirdMessageTemplate.ACCOUNT_REQUEST_EMAIL;
        }
        verify(mockTemplateService).generateMessage(eq(requestToSupportTemplate), any(Map.class));
        verify(mockEmailService).sendMessage(eq(SUPPORT_EMAIL_ADDRESS), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
        verify(mockTemplateService).generateMessage(
                eq(FirebirdMessageTemplate.ACCOUNT_REQUEST_USER_NOTIFICATION_EMAIL), any(Map.class));
        verify(mockEmailService).sendMessage(eq(configurationData.getPerson().getEmail()), any(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test
    public void testRequestAccount_ExistingLdapAccount() {
        AccountConfigurationData configurationData = AccountConfigurationDataFactory.getInstance().create();
        configurationData.setExistingLdapAccount(true);
        requestAccount(configurationData);
    }

    @Test
    public void testRemoveRole_Sponsor() throws GridInvocationException {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        SponsorRole sponsorRole1 = addSponsorRole(user);
        SponsorRole sponsorRole2 = addSponsorRole(user);
        SponsorRole delegateRole = addSponsorDelegateRole(user);
        bean.removeRole(user, UserRoleType.SPONSOR);
        assertTrue(user.getSponsorRepresentativeOrganizations().isEmpty());
        assertEquals(1, user.getSponsorRoles().size());
        assertEquals(delegateRole, user.getSponsorRoles().iterator().next());
        checkSponsorGroupMembershipsRemoved(user, sponsorRole1);
        checkSponsorGroupMembershipsRemoved(user, sponsorRole2);
        verify(mockUserService).save(user);
    }

    private void checkSponsorGroupMembershipsRemoved(FirebirdUser user, SponsorRole role)
            throws GridInvocationException {
        verify(mockGridGrouperService).removeGridUserFromGroup(user.getUsername(), role.getVerifiedSponsorGroupName());
        verify(mockGridGrouperService).removeGridUserFromGroup(user.getUsername(),
                role.getSponsorOrganizationGroupName());
    }

    private SponsorRole addSponsorRole(FirebirdUser user) {
        return user.addSponsorRepresentativeRole(OrganizationFactory.getInstance().create());
    }

    private SponsorRole addSponsorDelegateRole(FirebirdUser user) {
        return user.addSponsorDelegateRole(OrganizationFactory.getInstance().create());
    }

    @Test
    public void testRemoveRole_SponsorDelegate() throws GridInvocationException {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        SponsorRole delegateRole = addSponsorDelegateRole(user);
        bean.removeRole(user, UserRoleType.SPONSOR_DELEGATE);
        assertTrue(user.getSponsorRepresentativeOrganizations().isEmpty());
        checkSponsorGroupMembershipsRemoved(user, delegateRole);
        verify(mockUserService).save(user);
    }

    @Test
    public void testRemoveRole_RegistrationCoordinator_Unapproved() throws GridInvocationException {
        performRemoveRegistrationCoordinatorRoleTest(false);
        verify(mockEmailService, times(2)).sendMessage(anyString(), isNull(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    private void performRemoveRegistrationCoordinatorRoleTest(boolean approved) throws GridInvocationException {
        FirebirdUser user = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile profile1 = InvestigatorProfileFactory.getInstance().create();
        InvestigatorProfile profile2 = InvestigatorProfileFactory.getInstance().create();
        RegistrationCoordinatorRole role = user.getRegistrationCoordinatorRole();
        ManagedInvestigator managedInvestigator1 = role.addManagedInvestigator(profile1);
        ManagedInvestigator managedInvestigator2 = role.addManagedInvestigator(profile2);
        if (approved) {
            managedInvestigator1.setStatus(ManagedInvestigatorStatus.APPROVED);
            managedInvestigator2.setStatus(ManagedInvestigatorStatus.APPROVED);
        }
        bean.removeRole(user, UserRoleType.REGISTRATION_COORDINATOR);
        assertNull(user.getRegistrationCoordinatorRole());
        verify(mockGridGrouperService).removeGridUserFromGroup(user.getUsername(),
                UserRoleType.REGISTRATION_COORDINATOR.getGroupName());
        verify(mockUserService).save(user);
        verify(mockSession).delete(role);
    }

    @Test
    public void testRemoveRole_RegistrationCoordinator_Approved() throws GridInvocationException {
        performRemoveRegistrationCoordinatorRoleTest(true);
        verify(mockEmailService, times(2)).sendMessage(anyString(), isNull(Collection.class),
                isNull(String.class), any(FirebirdMessage.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRole_RegistrationCoordinator_NoRole() throws GridInvocationException {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        bean.removeRole(user, UserRoleType.REGISTRATION_COORDINATOR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRole_Investigator() throws GridInvocationException {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        bean.removeRole(user, UserRoleType.INVESTIGATOR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRole_NullUser() throws GridInvocationException {
        bean.removeRole(null, UserRoleType.SPONSOR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRole_NullRole() throws GridInvocationException {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        bean.removeRole(user, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRole_NotInRole() throws GridInvocationException {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        bean.removeRole(user, UserRoleType.SPONSOR);
    }

}
