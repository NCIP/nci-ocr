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

package gov.nih.nci.firebird.data.user;

import static gov.nih.nci.firebird.data.user.UserRoleType.*;
import static gov.nih.nci.firebird.nes.NesIdTestUtil.getNesIdExtension;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

public class FirebirdUserTest {

    @Test
    public void testEquals() {
        FirebirdUser user1 = new FirebirdUser();
        user1.setUsername("username1");
        FirebirdUser duplicateUser1 = new FirebirdUser();
        duplicateUser1.setUsername("username1");
        FirebirdUser user2 = new FirebirdUser();
        user2.setUsername("username2");

        assertEquals(user1, user1);
        assertEquals(user1, duplicateUser1);
        assertEquals(user1.hashCode(), duplicateUser1.hashCode());
        assertFalse(user1.equals(user2));
        assertFalse(user1.equals(null));
        assertFalse(user1.equals(new Object()));
    }

    @Test
    public void testAddManagedInvestigator() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        coordinator.createRegistrationCoordinatorRole();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        ManagedInvestigator managedInvestigator = coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(
                profile);
        assertSame(coordinator, managedInvestigator.getUser());
        assertSame(profile, managedInvestigator.getInvestigatorProfile());
        assertTrue(coordinator.getRegistrationCoordinatorRole().getManagedInvestigators().contains(managedInvestigator));
        assertTrue(profile.getRegistrationCoordinatorMappings().contains(managedInvestigator));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddManagedInvestigatorTwice() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        coordinator.createRegistrationCoordinatorRole();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(profile);
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(profile);
    }

    @Test
    public void testGetManagedInvestigators() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        coordinator.createRegistrationCoordinatorRole();
        InvestigatorProfile investigator1 = InvestigatorProfileFactory.getInstance().create();
        InvestigatorProfile investigator2 = InvestigatorProfileFactory.getInstance().create();
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigator1);
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(investigator2);

        List<InvestigatorProfile> profiles = coordinator.getRegistrationCoordinatorRole().getManagedProfiles();
        assertEquals(2, profiles.size());
        assertTrue(profiles.contains(investigator1));
        assertTrue(profiles.contains(investigator2));
    }

    @Test
    public void testRemoveManagedInvestigator() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        coordinator.createRegistrationCoordinatorRole();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        profile.setId(12345L);
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(profile);
        coordinator.getRegistrationCoordinatorRole().removeManagedInvestigator(profile);
        assertEquals(0, coordinator.getRegistrationCoordinatorRole().getManagedInvestigators().size());
    }

    @Test
    public void testRemoveManagedInvestigator_Empty() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        coordinator.createRegistrationCoordinatorRole();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        coordinator.getRegistrationCoordinatorRole().removeManagedInvestigator(profile);
        assertEquals(0, coordinator.getRegistrationCoordinatorRole().getManagedInvestigators().size());
    }

    @Test
    public void testRemoveManagedInvestigator_NotAdded() {
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().create();
        coordinator.createRegistrationCoordinatorRole();
        InvestigatorProfile addedProfile = InvestigatorProfileFactory.getInstance().create();
        addedProfile.setId(12345L);
        InvestigatorProfile notAddedProfile = InvestigatorProfileFactory.getInstance().create();
        notAddedProfile.setId(54321L);
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(addedProfile);
        coordinator.getRegistrationCoordinatorRole().removeManagedInvestigator(notAddedProfile);
        assertEquals(1, coordinator.getRegistrationCoordinatorRole().getManagedInvestigators().size());
    }

    @Test
    public void testGetApprovedManagedProfiles() {
        FirebirdUser coordinatorUser = new FirebirdUser();
        coordinatorUser.createRegistrationCoordinatorRole();
        InvestigatorProfile profile1 = InvestigatorProfileFactory.getInstance().create();
        profile1.getPerson().setLastName("Zrumba");
        ManagedInvestigator managedInvestigator1 = coordinatorUser.getRegistrationCoordinatorRole()
                .addManagedInvestigator(profile1);
        managedInvestigator1.setStatus(ManagedInvestigatorStatus.APPROVED);
        InvestigatorProfile profile2 = InvestigatorProfileFactory.getInstance().create();
        ManagedInvestigator managedInvestigator2 = coordinatorUser.getRegistrationCoordinatorRole()
                .addManagedInvestigator(profile2);
        managedInvestigator2.setStatus(ManagedInvestigatorStatus.AWAITING_APPROVAL);
        InvestigatorProfile profile3 = InvestigatorProfileFactory.getInstance().create();
        profile3.getPerson().setLastName("Applebaum");
        ManagedInvestigator managedInvestigator3 = coordinatorUser.getRegistrationCoordinatorRole()
                .addManagedInvestigator(profile3);
        managedInvestigator3.setStatus(ManagedInvestigatorStatus.APPROVED);

        List<InvestigatorProfile> managedProfiles = coordinatorUser.getRegistrationCoordinatorRole()
                .getApprovedManagedProfiles();
        assertEquals(2, managedProfiles.size());
        assertEquals(profile3, managedProfiles.get(0));
        assertEquals(profile1, managedProfiles.get(1));
    }

    @Test
    public void testIsRegistrationCoordinator_True() {
        FirebirdUser coordinatorUser = new FirebirdUser();
        coordinatorUser.createRegistrationCoordinatorRole();
        assertTrue(coordinatorUser.isRegistrationCoordinator());
    }

    @Test
    public void testIsRegistrationCoordinator_False() {
        FirebirdUser user = new FirebirdUser();
        assertFalse(user.isRegistrationCoordinator());
    }

    @Test
    public void testIsSponsorRepresentative_True() {
        FirebirdUser user = new FirebirdUser();
        SponsorRole sponsorRole = new SponsorRole();
        Set<SponsorRole> sponsorRoles = Sets.newHashSet(sponsorRole);
        user.setSponsorRoles(sponsorRoles);
        assertTrue(user.isSponsorRepresentative());
    }

    @Test
    public void testIsSponsorRepresentative_EmptyFalse() {
        FirebirdUser user = new FirebirdUser();
        Set<SponsorRole> sponsorRoles = Sets.newHashSet();
        user.setSponsorRoles(sponsorRoles);
        assertFalse(user.isSponsorRepresentative());
    }

    @Test
    public void testIsSponsorRepresentative_False() {
        FirebirdUser user = new FirebirdUser();
        SponsorRole sponsorRole = new SponsorRole(null, null, true);
        Set<SponsorRole> sponsorRoles = Sets.newHashSet(sponsorRole);
        user.setSponsorRoles(sponsorRoles);
        assertFalse(user.isSponsorRepresentative());
    }

    @Test
    public void testIsSponsorDelegate_True() {
        FirebirdUser user = new FirebirdUser();
        SponsorRole sponsorRole = new SponsorRole(null, null, true);
        Set<SponsorRole> sponsorRoles = Sets.newHashSet(sponsorRole);
        user.setSponsorRoles(sponsorRoles);
        assertTrue(user.isSponsorDelegate());
    }

    @Test
    public void testIsSponsorDelegate_False() {
        FirebirdUser user = new FirebirdUser();
        SponsorRole sponsorRole = new SponsorRole();
        Set<SponsorRole> sponsorRoles = Sets.newHashSet(sponsorRole);
        user.setSponsorRoles(sponsorRoles);
        assertFalse(user.isSponsorDelegate());
    }

    @Test
    public void testIsSponsorDelegate_EmptyFalse() {
        FirebirdUser user = new FirebirdUser();
        Set<SponsorRole> sponsorRoles = Sets.newHashSet();
        user.setSponsorRoles(sponsorRoles);
        assertFalse(user.isSponsorDelegate());
    }

    @Test
    public void testIsInvestigator_True() {
        FirebirdUser user = FirebirdUserFactory.getInstance().createInvestigator("investigator");
        assertTrue(user.isInvestigator());
    }

    @Test
    public void testIsInvestigator_False() {
        FirebirdUser user = new FirebirdUser();
        assertFalse(user.isInvestigator());
    }

    @Test
    public void testIsSponsorRepresentative_ForOrganization_True() {
        FirebirdUser user = new FirebirdUser();
        Organization organization = OrganizationFactory.getInstance().create();
        SponsorRole sponsorRepresentativeRole = new SponsorRole(null, organization, false);
        Set<SponsorRole> sponsorRoles = Sets.newHashSet(sponsorRepresentativeRole);
        user.setSponsorRoles(sponsorRoles);
        assertTrue(user.isSponsorRepresentative(organization));
    }

    @Test
    public void testIsSponsorRepresentative_ForOrganization_False() {
        FirebirdUser user = new FirebirdUser();
        Organization organization = OrganizationFactory.getInstance().create();
        SponsorRole sponsorDelegateRole = new SponsorRole(null, organization, true);
        Set<SponsorRole> sponsorRoles = Sets.newHashSet(sponsorDelegateRole);
        user.setSponsorRoles(sponsorRoles);
        assertFalse(user.isSponsorRepresentative(organization));
    }

    @Test
    public void testIsSponsorRepresentative_ForOrganization_Empty() {
        FirebirdUser user = new FirebirdUser();
        assertFalse(user.isSponsorRepresentative(OrganizationFactory.getInstance().create()));
    }

    @Test
    public void testGetVerifiedSponsorRepresentativeOrganizations() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsorDelegateOrganization = OrganizationFactory.getInstance().create();
        Organization sponsorRepresentativeOrganization = OrganizationFactory.getInstance().create();
        user.addSponsorDelegateRole(sponsorDelegateOrganization);
        user.addSponsorRepresentativeRole(sponsorRepresentativeOrganization);
        String delegateGroupName = "verified_sponsor_delegate_" + getNesIdExtension(sponsorDelegateOrganization);
        String representativeGroupName = "verified_sponsor_" + getNesIdExtension(sponsorRepresentativeOrganization);
        Set<String> groupNames = Sets.newHashSet(delegateGroupName, representativeGroupName);
        List<Organization> sponsorRepresentativeOrgs = user.getVerifiedSponsorRepresentativeOrganizations(groupNames);
        assertTrue(sponsorRepresentativeOrgs.contains(sponsorRepresentativeOrganization));
        assertFalse(sponsorRepresentativeOrgs.contains(sponsorDelegateOrganization));
    }

    @Test
    public void testGetVerifiedSponsorOrganizations() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsorDelegateOrganization = OrganizationFactory.getInstance().create();
        Organization sponsorRepresentativeOrganization = OrganizationFactory.getInstance().create();
        user.addSponsorDelegateRole(sponsorDelegateOrganization);
        user.addSponsorRepresentativeRole(sponsorRepresentativeOrganization);
        String delegateGroupName = "verified_sponsor_delegate_" + getNesIdExtension(sponsorDelegateOrganization);
        String representativeGroupName = "verified_sponsor_" + getNesIdExtension(sponsorRepresentativeOrganization);
        Set<String> groupNames = Sets.newHashSet(delegateGroupName, representativeGroupName);
        List<Organization> sponsorRepresentativeOrgs = user.getVerifiedSponsorOrganizations(groupNames);
        assertTrue(sponsorRepresentativeOrgs.contains(sponsorRepresentativeOrganization));
        assertTrue(sponsorRepresentativeOrgs.contains(sponsorDelegateOrganization));
    }

    @Test
    public void testAddSponsorRepresentativeRole() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        SponsorRole sponsorRole = user.addSponsorRepresentativeRole(sponsor);
        assertTrue(user.getSponsorRepresentativeOrganizations().contains(sponsor));
        assertTrue(user.getSponsorRoles().contains(sponsorRole));
    }

    @Test
    public void testGetSponsorOrganizations() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsorRepresentativeOrg = OrganizationFactory.getInstance().create();
        Organization sponsorDelegateOrg = OrganizationFactory.getInstance().create();
        user.addSponsorRepresentativeRole(sponsorRepresentativeOrg);
        user.addSponsorDelegateRole(sponsorDelegateOrg);
        assertTrue(user.getSponsorOrganizations().contains(sponsorRepresentativeOrg));
        assertTrue(user.getSponsorOrganizations().contains(sponsorDelegateOrg));
    }

    @Test
    public void testAddSponsorDelegateRole() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        SponsorRole sponsorRole = user.addSponsorDelegateRole(sponsor);
        assertTrue(user.getSponsorDelegateOrganizations().contains(sponsor));
        assertTrue(user.getSponsorRoles().contains(sponsorRole));
    }

    @Test
    public void testHasVerifiedSponsorRole_True() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        user.addSponsorDelegateRole(sponsor);
        Set<String> groupNames = Sets.newHashSet(SPONSOR_DELEGATE.getVerifiedGroupName() + "_"
                + getNesIdExtension(sponsor));
        assertTrue(user.hasVerifiedSponsorRole(sponsor, groupNames));
    }

    @Test
    public void testHasVerifiedSponsorRole_False_NotVerified() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        user.addSponsorDelegateRole(sponsor);
        Set<String> groupNames = Sets.newHashSet();
        assertFalse(user.hasVerifiedSponsorRole(sponsor, groupNames));
    }

    @Test
    public void testHasVerifiedSponsorRole_False_NotASponsor() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        Set<String> groupNames = Sets.newHashSet(SPONSOR_DELEGATE.getVerifiedGroupName() + "_" + sponsor.getExternalId());
        assertFalse(user.hasVerifiedSponsorRole(sponsor, groupNames));
    }

    @Test
    public void testGetRoles() {
        FirebirdUser user = FirebirdUserFactory.getInstance().createInvestigator("");
        user.createRegistrationCoordinatorRole();
        Organization sponsor = OrganizationFactory.getInstance().create();
        user.addSponsorDelegateRole(sponsor);
        user.addSponsorRepresentativeRole(sponsor);
        user.getRegistrationCoordinatorRole().addManagedInvestigator(InvestigatorProfileFactory.getInstance().create());
        assertEquals(UserRoleType.STANDARD_ROLES, user.getRoles());
    }

    @Test
    public void testGetBaseUsername() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        user.setUsername("/O=caBIG/OU=caGrid/OU=Stage LOA1/OU=Dorian/CN=user");
        assertEquals("user", user.getBaseUsername());
    }

    @Test
    public void testCanManageRegistration_InvestigatorForRegistration() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        assertTrue(registration.getProfile().getUser().canManageRegistration(registration));
    }

    @Test
    public void testCanManageRegistration_NotInvestigatorForRegistration() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        assertFalse(FirebirdUserFactory.getInstance().createInvestigator().canManageRegistration(registration));
    }

    @Test
    public void testCanManageRegistration_ApprovedCoordinatorForInvestigator() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(registration.getProfile()).setStatus(ManagedInvestigatorStatus.APPROVED);
        assertTrue(coordinator.canManageRegistration(registration));
    }

    @Test
    public void testCanManageRegistration_UnApprovedCoordinatorForInvestigator() throws Exception {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        FirebirdUser coordinator = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        coordinator.getRegistrationCoordinatorRole().addManagedInvestigator(registration.getProfile());
        assertFalse(coordinator.canManageRegistration(registration));
    }

}
