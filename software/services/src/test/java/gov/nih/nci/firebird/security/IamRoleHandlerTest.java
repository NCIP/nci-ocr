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
package gov.nih.nci.firebird.security;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.coppa.common.LimitOffset;
import gov.nih.nci.coppa.po.HealthCareFacility;
import gov.nih.nci.coppa.services.structuralroles.healthcarefacility.common.HealthCareFacilityI;
import gov.nih.nci.ctep.ces.ocr.api.CesInvestigatorService;
import gov.nih.nci.ctep.ces.ocr.api.InvestigatorStatus;
import gov.nih.nci.firebird.cagrid.UserSessionInformationFactory;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.organization.HealthCareFacilityIntegrationService;
import gov.nih.nci.firebird.nes.organization.NesOrganizationIntegrationServiceFactory;
import gov.nih.nci.firebird.service.ctep.iam.IamIntegrationService;
import gov.nih.nci.firebird.service.investigator.InvestigatorService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.user.FirebirdUserService;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ValueGenerator;

import java.util.Set;

import javax.security.auth.login.LoginException;

import org.iso._21090.DSETII;
import org.iso._21090.II;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class IamRoleHandlerTest {

    private static final String TEST_FULLY_QUALIFIED_USERNAME = "/O=NCI/OU=CTEP/CN=user";
    private static final String TEST_BASE_USERNAME = "user";
    private static final Set<String> TEST_ROLES = Sets.newHashSet(UserRoleType.CTEP_INVESTIGATOR.getGroupName(),
            UserRoleType.CTEP_REGISTRATION_COORDINATOR.getGroupName(), UserRoleType.CTEP_SPONSOR.getGroupName(),
            UserRoleType.CTEP_SPONSOR_DELEGATE.getGroupName());

    private IamRoleHandler roleHandler;

    @Mock
    private IamIntegrationService mockIamIntegrationService;

    @Mock
    private FirebirdUserService mockUserService;

    @Mock
    private PersonService mockPersonService;

    @Mock
    private InvestigatorService mockInvestigatorService;

    @Mock
    private CesInvestigatorService mockCesInvestigatorService;

    @Mock
    private SponsorService mockSponsorService;

    @Mock
    private HealthCareFacilityI mockHealthCareFacilityService;

    @Mock
    private HealthCareFacilityIntegrationService mockHealthCareFacilityIntegrationService;

    @Mock
    private NesOrganizationIntegrationServiceFactory mockNesServiceFactory;

    @Mock
    private OrganizationService mockOrganizationService;

    private UserSessionInformation sessionInformation = UserSessionInformationFactory.getInstance().create(
            TEST_FULLY_QUALIFIED_USERNAME);
    private Organization sponsor = OrganizationFactory.getInstance().create();
    private Organization primaryOrganization = OrganizationFactory.getInstance().create();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockSponsorService.getSponsorOrganizationWithAnnualRegistrations()).thenReturn(sponsor);
        roleHandler = new IamRoleHandler(mockUserService, mockIamIntegrationService, mockPersonService,
                mockCesInvestigatorService, mockInvestigatorService, mockSponsorService, mockHealthCareFacilityService,
                mockNesServiceFactory, mockOrganizationService);
        when(mockIamIntegrationService.getGroups(TEST_BASE_USERNAME)).thenReturn(TEST_ROLES);
        when(mockPersonService.getByCtepId(anyString())).thenReturn(PersonFactory.getInstance().create());
        when(mockCesInvestigatorService.getInvestigatorStatus(anyString())).thenReturn(InvestigatorStatus.ACTIVE);
        HealthCareFacility healthCareFacility = new HealthCareFacility();
        DSETII identifier = mock(DSETII.class);
        II ii = new II();
        ii.setRoot(NesIIRoot.HEALTH_CARE_FACILITY.getRoot());
        ii.setExtension(ValueGenerator.getUniqueString());
        when(identifier.getItem()).thenReturn(Lists.newArrayList(ii));
        healthCareFacility.setIdentifier(identifier);
        when(mockHealthCareFacilityService.query(any(HealthCareFacility.class), any(LimitOffset.class))).thenReturn(
                new HealthCareFacility[] { healthCareFacility });
        when(mockNesServiceFactory.getService(anyString())).thenReturn(mockHealthCareFacilityIntegrationService);
        when(mockHealthCareFacilityIntegrationService.getById(anyString())).thenReturn(primaryOrganization);
    }

    @Test
    public void testHandleRoles_ExistingUser() throws LoginException {
        FirebirdUser mockUser = mock(FirebirdUser.class);
        when(mockUserService.getUserInfo(sessionInformation)).thenReturn(mockUser);
        roleHandler.handleRoles(sessionInformation);
        assertTrue(sessionInformation.getGroupNames().containsAll(TEST_ROLES));
        assertTrue(sessionInformation.getGroupNames().contains(FirebirdConstants.AUTHENTICATED_USER_ROLE));
        assertTrue(sessionInformation.getGroupNames().contains(FirebirdConstants.REGISTERED_USER_ROLE));
        verify(mockUserService).getUserInfo(sessionInformation);
        verify(mockUserService).save(any(FirebirdUser.class));
    }

    @Test
    public void testHandleRoles_NewUser() {
        roleHandler.handleRoles(sessionInformation);
        ArgumentCaptor<FirebirdUser> userCaptor = ArgumentCaptor.forClass(FirebirdUser.class);
        verify(mockUserService).save(userCaptor.capture());
        verify(mockPersonService).getByCtepId(anyString());
        FirebirdUser user = userCaptor.getValue();
        assertNotNull(user.getInvestigatorRole());
        assertEquals(primaryOrganization, user.getInvestigatorRole().getProfile().getPrimaryOrganization()
                .getOrganization());
        assertNotNull(user.getInvestigatorRole().getProfile().getPerson());
        assertNotNull(user.getRegistrationCoordinatorRole());
        verify(mockInvestigatorService).handleStatus(user, gov.nih.nci.firebird.data.user.InvestigatorStatus.ACTIVE);
    }

    @Test
    public void testHandleRoles_NoRoles() {
        Set<String> emptyRoles = Sets.newHashSet();
        when(mockIamIntegrationService.getGroups(TEST_BASE_USERNAME)).thenReturn(emptyRoles);
        roleHandler.handleRoles(sessionInformation);
        ArgumentCaptor<FirebirdUser> userCaptor = ArgumentCaptor.forClass(FirebirdUser.class);
        verify(mockUserService).save(userCaptor.capture());
        verify(mockPersonService).getByCtepId(anyString());
        FirebirdUser user = userCaptor.getValue();
        assertNull(user.getInvestigatorRole());
        assertNull(user.getRegistrationCoordinatorRole());
        verify(mockInvestigatorService, never()).handleStatus(user,
                gov.nih.nci.firebird.data.user.InvestigatorStatus.ACTIVE);
    }

    @Test
    public void testHandleRoles_Sponsor() {
        Set<String> roles = Sets.newHashSet(UserRoleType.CTEP_SPONSOR.getGroupName());
        when(mockIamIntegrationService.getGroups(TEST_BASE_USERNAME)).thenReturn(roles);
        roleHandler.handleRoles(sessionInformation);
        ArgumentCaptor<FirebirdUser> userCaptor = ArgumentCaptor.forClass(FirebirdUser.class);
        verify(mockUserService).save(userCaptor.capture());
        verify(mockPersonService).getByCtepId(anyString());
        FirebirdUser user = userCaptor.getValue();
        assertNull(user.getInvestigatorRole());
        assertNull(user.getRegistrationCoordinatorRole());
        assertTrue(user.getSponsorRepresentativeOrganizations().contains(sponsor));
        assertTrue(user.getSponsorDelegateOrganizations().isEmpty());
        verify(mockInvestigatorService, never()).handleStatus(user,
                gov.nih.nci.firebird.data.user.InvestigatorStatus.ACTIVE);
    }

    @Test
    public void testHandleRoles_SponsorDelegate() {
        Set<String> roles = Sets.newHashSet(UserRoleType.CTEP_SPONSOR_DELEGATE.getGroupName());
        when(mockIamIntegrationService.getGroups(TEST_BASE_USERNAME)).thenReturn(roles);
        roleHandler.handleRoles(sessionInformation);
        ArgumentCaptor<FirebirdUser> userCaptor = ArgumentCaptor.forClass(FirebirdUser.class);
        verify(mockUserService).save(userCaptor.capture());
        verify(mockPersonService).getByCtepId(anyString());
        FirebirdUser user = userCaptor.getValue();
        assertNull(user.getInvestigatorRole());
        assertNull(user.getRegistrationCoordinatorRole());
        assertTrue(user.getSponsorRepresentativeOrganizations().isEmpty());
        assertTrue(user.getSponsorDelegateOrganizations().contains(sponsor));
        verify(mockInvestigatorService, never()).handleStatus(user,
                gov.nih.nci.firebird.data.user.InvestigatorStatus.ACTIVE);
    }

}
