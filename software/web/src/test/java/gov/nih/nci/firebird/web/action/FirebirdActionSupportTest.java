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
package gov.nih.nci.firebird.web.action;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.cagrid.UserSessionInformationFactory;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.SponsorRole;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.service.investigatorprofile.ProfileRefreshService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.person.external.InvalidatedPersonException;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.ValidationExceptionFactory;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;

@SuppressWarnings("unchecked")
public class FirebirdActionSupportTest extends AbstractWebTest {

    @SuppressWarnings("serial")
    FirebirdActionSupport supportAction = new FirebirdActionSupport() {
    };

    @Inject
    private SponsorService mockSponsorService;
    @Inject
    private ProfileRefreshService mockProfileRefreshService;
    @Inject
    private PersonService mockPersonService;
    @Inject
    private Provider<PersonService> mockPersonServiceProvider;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        supportAction.setSponsorService(mockSponsorService);
        supportAction.setProfileRefreshService(mockProfileRefreshService);
        supportAction.setPersonServiceProvider(mockPersonServiceProvider);
    }

    @Test
    public void testGetCurrentUser() {
        FirebirdUser user = createTestUser();
        FirebirdWebTestUtility.setCurrentUser(supportAction, user);
        assertEquals("username", supportAction.getCurrentUsername());
        assertEquals(user, supportAction.getCurrentUser());
    }

    private static FirebirdUser createTestUser() {
        FirebirdUser user = new FirebirdUser();
        user.setUsername("username");
        return user;
    }

    @Test
    public void testGetCurrentUsername() {
        assertNull(supportAction.getCurrentUsername());
        FirebirdUser user = createTestUser();
        FirebirdWebTestUtility.setCurrentUser(supportAction, user);
        assertEquals(user.getUsername(), supportAction.getCurrentUsername());
    }

    @Test
    public void testHandleValidationException_NoPrefix() {
        ValidationResult result = new ValidationResult(new ValidationFailure("action error"));
        ValidationException e = new ValidationException(result);
        supportAction.handleValidationException(e);
        assertTrue(supportAction.hasActionErrors());
        result.addFailure(new ValidationFailure("fieldKey", "action error"));
        supportAction.handleValidationException(e);
        assertTrue(supportAction.hasFieldErrors());
        assertEquals("fieldKey", supportAction.getFieldErrors().keySet().iterator().next());
    }

    @Test
    public void testHandleValidationException_PrefixNoPeriod() throws ValidationException {
        String prefix = "prefix";
        Map<String, String> errors = Maps.newHashMap();
        errors.put("field", "This is an error");
        errors.put("field2", "This is another error");
        ValidationException exception = ValidationExceptionFactory.getInstance().create(errors);

        supportAction.handleValidationException(exception, prefix);
        assertTrue(supportAction.hasFieldErrors());
        assertFalse(supportAction.hasActionErrors());
        assertEquals(2, supportAction.getFieldErrors().size());
        checkErrorsForPrefix(supportAction.getFieldErrors(), prefix + ".");
    }

    private void checkErrorsForPrefix(Map<String, List<String>> errors, String prefix) {
        for (String field : errors.keySet()) {
            assertTrue(field, field.startsWith(prefix));
            assertFalse(field, field.startsWith(prefix + "."));
        }
    }

    @Test
    public void testHandleValidationException_PrefixHasPeriod() throws ValidationException {
        String prefix = "prefix.";

        Map<String, String> errors = Maps.newHashMap();
        errors.put("field", "This is an error");
        errors.put("field2", "This is another error");
        ValidationException exception = ValidationExceptionFactory.getInstance().create(errors);

        supportAction.handleValidationException(exception, prefix);
        assertTrue(supportAction.hasFieldErrors());
        assertFalse(supportAction.hasActionErrors());
        assertEquals(2, supportAction.getFieldErrors().size());
        checkErrorsForPrefix(supportAction.getFieldErrors(), prefix);
    }

    @Test
    public void testHandleValidationException_PrefixNotAppendedToNonKeyedFailures() throws ValidationException {
        String prefix = "prefix";

        Map<String, String> errors = Maps.newHashMap();
        errors.put("field", "This is an error");
        errors.put(null, "This is another error");
        ValidationException exception = ValidationExceptionFactory.getInstance().create(errors);

        supportAction.handleValidationException(exception, prefix);
        assertTrue(supportAction.hasFieldErrors());
        assertTrue(supportAction.hasActionErrors());
        assertEquals(1, supportAction.getFieldErrors().size());
        assertEquals(1, supportAction.getActionErrors().size());
    }

    @Test
    public void testIsInvestigator_True() {
        FirebirdUser user = createInvestigatorUser();
        setUpUserInfo(user, UserRoleType.INVESTIGATOR.getGroupName());
        assertTrue(supportAction.isInvestigator());
    }

    private FirebirdUser createInvestigatorUser() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.createInvestigatorRole(profile);
        return user;
    }

    @Test
    public void testIsInvestigator_NotInGroup() {
        FirebirdUser user = createInvestigatorUser();
        setUpUserInfo(user);
        assertFalse(supportAction.isInvestigator());
    }

    @Test
    public void testIsInvestigator_NoRole() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        setUpUserInfo(user, UserRoleType.INVESTIGATOR.getGroupName());
        assertFalse(supportAction.isInvestigator());
    }

    @Test
    public void testIsCtepInvestigator() {
        FirebirdUser user = createInvestigatorUser();
        setUpUserInfo(user, UserRoleType.CTEP_INVESTIGATOR.getGroupName());
        assertTrue(supportAction.isCtepInvestigator());
    }

    @Test
    public void testIsCtepInvestigator_NotInGroup() {
        FirebirdUser user = createInvestigatorUser();
        setUpUserInfo(user);
        assertFalse(supportAction.isCtepInvestigator());
    }

    @Test
    public void testIsCtepInvestigator_NoRole() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        setUpUserInfo(user, UserRoleType.CTEP_INVESTIGATOR.getGroupName());
        assertFalse(supportAction.isCtepInvestigator());
    }

    @Test
    public void testIsVerifiedCtepInvestgiator() {
        FirebirdUser user = createInvestigatorUser();
        setUpUserInfo(user);
        assertFalse(supportAction.isVerifiedCtepInvestigator());
        setUpUserInfo(user, UserRoleType.CTEP_VERIFIED_INVESTIGATOR.getGroupName());
        assertTrue(supportAction.isVerifiedCtepInvestigator());
    }

    @Test
    public void testIsRegistrationCoordinator_True() {
        FirebirdUser user = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.getRegistrationCoordinatorRole().addManagedInvestigator(profile);
        setUpUserInfo(user, UserRoleType.REGISTRATION_COORDINATOR.getGroupName());
        assertTrue(supportAction.isRegistrationCoordinator());
    }

    @Test
    public void testIsCtepRegistrationCoordinator() {
        FirebirdUser user = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.getRegistrationCoordinatorRole().addManagedInvestigator(profile).setCtepAssociate(true);
        setUpUserInfo(user, UserRoleType.CTEP_REGISTRATION_COORDINATOR.getGroupName());
        assertTrue(supportAction.isCtepRegistrationCoordinator());
    }

    @Test
    public void testIsCtepRegistrationCoordinator_False() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        setUpUserInfo(user);
        assertFalse(supportAction.isCtepRegistrationCoordinator());
    }

    @Test
    public void testIsRegistrationCoordinatorInvestigator_NotInGroup() {
        FirebirdUser user = FirebirdUserFactory.getInstance().createRegistrationCoordinator();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        user.getRegistrationCoordinatorRole().addManagedInvestigator(profile);
        setUpUserInfo(user);
        assertFalse(supportAction.isRegistrationCoordinator());
    }

    @Test
    public void testIsRegistrationCoordinator_NoRole() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        setUpUserInfo(user, UserRoleType.REGISTRATION_COORDINATOR.getGroupName());
        assertFalse(supportAction.isRegistrationCoordinator());
    }

    @Test
    public void testIsVerifiedSponsor_True() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        addSponsorRole(user, sponsor, false);
        setUpUserInfo(user, UserRoleType.SPONSOR.getVerifiedGroupName());
        assertTrue(supportAction.isVerifiedSponsor());
    }

    @Test
    public void testIsVerifiedSponsorInvestigator_NotInGroup() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        addSponsorRole(user, sponsor, false);
        setUpUserInfo(user);
        assertFalse(supportAction.isVerifiedSponsor());
    }

    @Test
    public void testIsVerifiedSponsor_NoRole() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        setUpUserInfo(user, UserRoleType.SPONSOR.getVerifiedGroupName());
        assertFalse(supportAction.isVerifiedSponsor());
    }

    @Test
    public void testIsVerifiedSponsorDelegate_True() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        user.addSponsorDelegateRole(sponsor);
        setUpUserInfo(user, UserRoleType.SPONSOR_DELEGATE.getVerifiedGroupName());
        assertTrue(supportAction.isVerifiedSponsorDelegate());
    }

    @Test
    public void testIsVerifiedSponsorDelegateInvestigator_NotInGroup() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        Organization sponsor = OrganizationFactory.getInstance().create();
        user.addSponsorDelegateRole(sponsor);
        setUpUserInfo(user);
        assertFalse(supportAction.isVerifiedSponsorDelegate());
    }

    @Test
    public void testIsVerifiedSponsorDelegate_NoRole() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        setUpUserInfo(user, UserRoleType.SPONSOR_DELEGATE.getVerifiedGroupName());
        assertFalse(supportAction.isVerifiedSponsorDelegate());
    }

    private void setUpUserInfo(FirebirdUser user, String... groupNames) {
        UserSessionInformation userSessionInformation = UserSessionInformationFactory.getInstance().create(
                user.getUsername(), groupNames);
        FirebirdWebTestUtility.setCurrentUser(supportAction, user);
        FirebirdWebTestUtility.setUpGridSessionInformation(supportAction, userSessionInformation);
    }

    @Test
    public void testIsCtepSponsor() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        setUpUserInfo(user);
        assertFalse(supportAction.isCtepSponsor());
        setUpUserInfo(user, UserRoleType.CTEP_SPONSOR.getGroupName());
        assertTrue(supportAction.isCtepSponsor());
    }

    @Test
    public void testIsCtepSponsorDelegate() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        setUpUserInfo(user);
        assertFalse(supportAction.isCtepSponsorDelegate());
        setUpUserInfo(user, UserRoleType.CTEP_SPONSOR_DELEGATE.getGroupName());
        assertTrue(supportAction.isCtepSponsorDelegate());
    }

    @Test
    public void testGetId_NoId() {
        InvestigatorProfile profile = new InvestigatorProfile();
        assertNull(supportAction.getId(profile));
    }

    @Test
    public void testGetId() {
        InvestigatorProfile profile = new InvestigatorProfile();
        profile.setId(1L);
        assertEquals(Long.valueOf(1), supportAction.getId(profile));
    }

    @Test
    public void testGetId__Null() {
        InvestigatorProfile profile = null;
        assertNull(supportAction.getId(profile));
    }

    private SponsorRole addSponsorRole(FirebirdUser user, Organization sponsor, boolean delegate) {
        if (delegate) {
            return user.addSponsorDelegateRole(sponsor);
        } else {
            return user.addSponsorRepresentativeRole(sponsor);
        }
    }

    @Test
    public void testGetPerson() throws Exception {
        String externalId = "externalId";
        Person person = new Person();
        when(mockPersonService.getByExternalId(externalId)).thenReturn(person);
        assertEquals(person, supportAction.getPerson(externalId));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPerson_InvalidatedPersonException() throws Exception {
        String externalId = "externalId";
        when(mockPersonService.getByExternalId(externalId)).thenThrow(new InvalidatedPersonException());
        supportAction.getPerson(externalId);
    }
}
