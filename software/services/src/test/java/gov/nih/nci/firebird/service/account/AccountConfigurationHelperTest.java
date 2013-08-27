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

import static gov.nih.nci.firebird.nes.NesIdTestUtil.getNesIdExtension;
import static gov.nih.nci.firebird.test.ValueGenerator.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.cagrid.GridGrouperService;
import gov.nih.nci.firebird.cagrid.GridInvocationException;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.user.FirebirdUserService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.PrimaryOrganizationFactory;

import java.util.EnumSet;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

public class AccountConfigurationHelperTest {

    private static final String SUPPORT_EMAIL_ADDRESS = "firebird-support@5amsolutions.com";

    private InvestigatorProfileService mockProfileService = mock(InvestigatorProfileService.class);
    private OrganizationService mockOrganizationService = mock(OrganizationService.class);
    private PersonService mockPersonService = mock(PersonService.class);
    private FirebirdUserService mockUserService = mock(FirebirdUserService.class);
    private EmailService mockEmailService = mock(EmailService.class);
    private TemplateService mockTemplateService = mock(TemplateService.class);
    private GridGrouperService mockGridGrouperService = mock(GridGrouperService.class);
    private OrganizationAssociationService mockOrganizationAssociationService = mock(OrganizationAssociationService.class);
    private static ResourceBundle resources = ResourceBundle.getBundle("resources", Locale.getDefault());
    private AccountConfigurationHelper helper = new AccountConfigurationHelper();

    @Before
    public void setUp() throws UnavailableEntityException {
        helper.setEmailService(mockEmailService);
        helper.setOrganizationService(mockOrganizationService);
        helper.setGridGrouperService(mockGridGrouperService);
        helper.setPersonService(mockPersonService);
        helper.setProfileService(mockProfileService);
        helper.setResources(resources);
        helper.setSupportEmailAddress(SUPPORT_EMAIL_ADDRESS);
        helper.setTemplateService(mockTemplateService);
        helper.setUserService(mockUserService);
        helper.setOrganizationAssociationService(mockOrganizationAssociationService);
    }

    @Test
    public void testCreateUser() throws ValidationException {
        AccountConfigurationData configurationData = createBaseConfigurationData();
        FirebirdUser user = helper.createUser(configurationData);
        checkBaseConfiguration(user, configurationData);
        verify(mockPersonService).createNesPerson(configurationData.getPerson());
        verify(mockUserService, never()).checkPersonAssociated(configurationData.getPerson());
        verify(mockUserService).save(user);
    }

    @Test
    public void testCreateUser_ExistingPerson() throws ValidationException {
        AccountConfigurationData configurationData = createBaseConfigurationData();
        configurationData.getPerson().setNesId("1234");
        FirebirdUser user = helper.createUser(configurationData);
        checkBaseConfiguration(user, configurationData);
        verify(mockPersonService, never()).createNesPerson(configurationData.getPerson());
        verify(mockUserService).checkPersonAssociated(configurationData.getPerson());
        verify(mockUserService).save(user);
    }

    @Test(expected = ValidationException.class)
    public void testCreateUser_PersonNotAvailable() throws ValidationException {
        AccountConfigurationData configurationData = createBaseConfigurationData();
        configurationData.getPerson().setNesId("1234");
        when(mockUserService.checkPersonAssociated(configurationData.getPerson())).thenReturn(true);
        helper.createUser(configurationData);
    }

    private AccountConfigurationData createBaseConfigurationData() {
        AccountConfigurationData configurationData = new AccountConfigurationData();
        configurationData.setUsername(getUniqueString());
        Person person = PersonFactory.getInstance().create();
        person.setNesId(null);
        configurationData.setPerson(person);
        return configurationData;
    }

    private void checkBaseConfiguration(FirebirdUser user, AccountConfigurationData configurationData) {
        assertNotNull(user);
        assertEquals(configurationData.getUsername(), user.getUsername());
        assertEquals(configurationData.getPerson(), user.getPerson());
        assertTrue(user.isEulaAccepted());
    }

    @Test
    public void testCreateUser_AllRoles() throws ValidationException, GridInvocationException {
        AccountConfigurationData configurationData = createBaseConfigurationData();
        addAllRoles(configurationData);
        FirebirdUser user = helper.createUser(configurationData);
        checkBaseConfiguration(user, configurationData);
        checkInvestigatorConfiguration(user, configurationData, false);
        checkRegistrationCoordinatorConfiguration(user, configurationData);
        checkSponsorConfiguration(user, configurationData, false);
        checkSponsorDelegateConfiguration(user, configurationData, false);
        verify(mockUserService).save(user);
    }

    @Test
    public void testCreateUser_InvestigatorExistingProfile() throws ValidationException, GridInvocationException, UnavailableEntityException {
        AccountConfigurationData configurationData = createBaseConfigurationData();
        addInvestigatorRole(configurationData);
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        profile.setPerson(configurationData.getPerson());
        when(mockProfileService.getByPerson(configurationData.getPerson())).thenReturn(profile);
        FirebirdUser user = helper.createUser(configurationData);
        checkBaseConfiguration(user, configurationData);
        checkInvestigatorConfiguration(user, configurationData, false);
        assertEquals(profile, user.getInvestigatorRole().getProfile());
        verify(mockUserService).save(user);
    }

    private void addAllRoles(AccountConfigurationData configurationData) {
        addInvestigatorRole(configurationData);
        configurationData.getRoles().addAll(EnumSet.allOf(UserRoleType.class));
        configurationData.getSponsorOrganizations().add(OrganizationFactory.getInstance().create());
        configurationData.getDelegateOrganizations().add(OrganizationFactory.getInstance().create());
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        profile.setId(1L);
        when(mockProfileService.getById(1L)).thenReturn(profile);
        configurationData.getSelectedInvestigators().add(profile);
    }

    private void addInvestigatorRole(AccountConfigurationData configurationData) {
        configurationData.getRoles().add(UserRoleType.INVESTIGATOR);
        configurationData.setPrimaryOrganization(PrimaryOrganizationFactory.getInstance().create());
        configurationData.getPrimaryOrganization().getOrganization().setNesId(null);
    }

    private void checkInvestigatorConfiguration(FirebirdUser user, AccountConfigurationData configurationData, boolean groupMembershipExpected) throws ValidationException, GridInvocationException {
        assertTrue(user.isInvestigator());
        InvestigatorProfile profile = user.getInvestigatorRole().getProfile();
        assertEquals(configurationData.getPerson(), profile.getPerson());
        assertEquals(configurationData.getPrimaryOrganization(), profile.getPrimaryOrganization());
        verify(mockProfileService).setPrimaryPerson(profile, configurationData.getPerson());
        verify(mockOrganizationAssociationService).createNewPrimaryOrganization(configurationData.getPrimaryOrganization());
        verify(mockProfileService).setPrimaryOrganization(profile, configurationData.getPrimaryOrganization());
        if (groupMembershipExpected) {
            verify(mockGridGrouperService).addGridUserToGroup(user.getUsername(), UserRoleType.INVESTIGATOR.getGroupName());
        }
    }

    private void checkRegistrationCoordinatorConfiguration(FirebirdUser user, AccountConfigurationData configurationData) {
        assertTrue(user.isRegistrationCoordinator());
        InvestigatorProfile managedProfile = user.getRegistrationCoordinatorRole().getManagedProfiles().get(0);
        assertEquals(configurationData.getSelectedInvestigators().iterator().next(), managedProfile);
        verify(mockEmailService).sendMessage(eq(managedProfile.getPerson().getEmail()), anyCollectionOf(String.class),
                anyString(), any(FirebirdMessage.class));
    }

    private void checkSponsorConfiguration(FirebirdUser user, AccountConfigurationData configurationData, boolean groupMembershipExpected) throws GridInvocationException {
        assertTrue(user.isSponsorRepresentative());
        assertEquals(1, user.getSponsorRepresentativeOrganizations().size());
        Organization sponsor = user.getSponsorRepresentativeOrganizations().get(0);
        assertEquals(configurationData.getSponsorOrganizations().iterator().next(), sponsor);
        if (groupMembershipExpected) {
            verify(mockGridGrouperService).addGridUserToGroup(user.getUsername(),
                    UserRoleType.SPONSOR.getGroupName() + "_" + getNesIdExtension(sponsor));
        }
    }

    private void checkSponsorDelegateConfiguration(FirebirdUser user, AccountConfigurationData configurationData, boolean groupMembershipExpected) throws GridInvocationException {
        assertTrue(user.isSponsorDelegate());
        assertEquals(1, user.getSponsorDelegateOrganizations().size());
        Organization sponsor = user.getSponsorDelegateOrganizations().get(0);
        assertEquals(configurationData.getDelegateOrganizations().iterator().next(), sponsor);
        if (groupMembershipExpected) {
            verify(mockGridGrouperService).addGridUserToGroup(user.getUsername(),
                    UserRoleType.SPONSOR_DELEGATE.getGroupName() + "_" + getNesIdExtension(sponsor));
        }
    }

    @Test
    public void testAddRoles() throws ValidationException, GridInvocationException {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        AccountConfigurationData configurationData = createBaseConfigurationData();
        addAllRoles(configurationData);
        helper.addRoles(user, configurationData);
        checkInvestigatorConfiguration(user, configurationData, true);
        checkRegistrationCoordinatorConfiguration(user, configurationData);
        checkSponsorConfiguration(user, configurationData, true);
        checkSponsorDelegateConfiguration(user, configurationData, true);
        verify(mockUserService).save(user);
        verify(mockEmailService).sendMessage(eq(SUPPORT_EMAIL_ADDRESS), anyCollectionOf(String.class),
                anyString(), any(FirebirdMessage.class));
    }

}
