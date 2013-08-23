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
package gov.nih.nci.firebird.selenium2.tests.account;

import static gov.nih.nci.firebird.data.user.UserRoleType.*;
import static gov.nih.nci.firebird.nes.NesIdTestUtil.*;
import static gov.nih.nci.firebird.selenium2.tests.account.RegistrationTestHelper.*;
import static gov.nih.nci.firebird.test.LoginAccount.InvestigatorLogin.*;
import static gov.nih.nci.firebird.test.ValueGenerator.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.cagrid.GridInvocationException;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.base.MessageHandler;
import gov.nih.nci.firebird.selenium2.pages.base.ValidationException;
import gov.nih.nci.firebird.selenium2.pages.components.AddressForm;
import gov.nih.nci.firebird.selenium2.pages.components.PersonForm;
import gov.nih.nci.firebird.selenium2.pages.coordinator.investigators.BrowseInvestigatorsPage;
import gov.nih.nci.firebird.selenium2.pages.coordinator.investigators.BrowseInvestigatorsPage.InvestigatorListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ProfessionalContactInformationTab;
import gov.nih.nci.firebird.selenium2.pages.login.LoginPage;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.AbstractAccountRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.CreatePrimaryOrganizationPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.EditPersonPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.FunctionalityWarningDialog;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.InvestigatorSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.OrganizationSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.PersonSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.VerificationPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.ViewOrganizationPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.ViewSelectedRolesPage;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.selenium2.pages.util.VerificationUtils;
import gov.nih.nci.firebird.service.person.external.ExternalPersonService;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.LoginAccount.InvestigatorLogin;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class AccountRegistrationTest extends AbstractFirebirdWebDriverTest {

    private static final String USER_ALREADY_USED_ERROR_KEY = "investigator.profile.alreadyAssociated";

    @Inject
    private RegistrationTestHelper helper;

    private LoginAccount testUser;

    private Person newAccountPerson = PersonFactory.getInstance().createWithoutExternalData();

    @Inject
    private ExternalPersonService nesPersonService;

    private Organization primaryOrganization;

    private Map<InvestigatorLogin, FirebirdUser> investigatorMap;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        testUser = helper.createNewGridUser(newAccountPerson);
        primaryOrganization = getExistingPrimaryOrganization();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        helper.removeGridUser(testUser);
    }

    @Test
    public void testInvestigatorRegistration() throws GridInvocationException {
        Person person = getExistingExternalPerson();
        addGroupMembership(UserRoleType.INVESTIGATOR);

        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        rolePage.getHelper().checkForRoles(UserRoleType.INVESTIGATOR.getDisplay());
        PersonSelectionPage selectPersonPage = (PersonSelectionPage) rolePage.clickNext();
        EditPersonPage editPersonPage = selectPerson(person, selectPersonPage);
        editPersonPage.getHelper().verifyFormValues(person);
        OrganizationSelectionPage organizationSelectionPage = (OrganizationSelectionPage) editPersonPage.clickNext();
        VerificationPage verificationPage = (VerificationPage) selectOrganization(primaryOrganization, organizationSelectionPage);
        ViewOrganizationPage viewOrganizationPage = (ViewOrganizationPage) verificationPage.clickPrevious();
        viewOrganizationPage.getHelper().verifyInformationIsDisplayed(primaryOrganization);
        verificationPage = (VerificationPage) viewOrganizationPage.clickNext();
        checkVerificationPage(verificationPage, person, primaryOrganization, INVESTIGATOR);
        Organization newOrganization = editOrganization(verificationPage, person);
        Person newPerson = editPerson(verificationPage, newOrganization);
        completeInvestigatorRegistration(verificationPage, newPerson, newOrganization);
    }

    private void addGroupMembership(UserRoleType role) throws GridInvocationException {
        addGroupMembership(role.getGroupName());
    }

    private void addGroupMembership(String groupName) throws GridInvocationException {
        addGroupMemberships(groupName);
    }

    private void addGroupMemberships(String... groupNames) throws GridInvocationException {
        helper.addGroupMemberships(testUser.getUsername(), groupNames);
    }

    private ViewSelectedRolesPage openSelectedRolesPage() {
        return openLoginPage().getHelper().goToSelectedRolesPage(testUser, getProvider());
    }

    private EditPersonPage selectPerson(Person person, PersonSelectionPage selectPersonPage) {
        return selectPersonPage.getHelper().searchAndSelectPerson(person);
    }

    private AbstractAccountRegistrationPage<?> selectOrganization(Organization organization,
            OrganizationSelectionPage organizationSelectionPage) {
        return organizationSelectionPage.getHelper().searchAndSelectOrganization(organization);
    }

    private void checkVerificationPage(VerificationPage verificationPage, Person person, Organization organization,
            UserRoleType... roles) {
        verificationPage.getHelper().verifyPersonInformationIsDisplayed(person);
        verificationPage.getHelper().verifyPrimaryOrganizationInformationIsDisplayed(organization);
        verificationPage.getHelper().verifyRoles(roles);
    }

    private Organization editOrganization(VerificationPage verificationPage, Person person) {
        OrganizationSelectionPage organizationSelectionPage = verificationPage.clickEditPrimaryOrganization()
                .clickSearchAgain();
        checkCreateNewOrganizationRemovesReturnLink(organizationSelectionPage);
        Organization newOrganization = getExistingPrimaryOrganization();
        verificationPage = (VerificationPage) organizationSelectionPage.getHelper().searchAndSelectOrganization(
                newOrganization);
        checkVerificationPage(verificationPage, person, newOrganization, INVESTIGATOR);
        return newOrganization;
    }

    protected void checkCreateNewOrganizationRemovesReturnLink(OrganizationSelectionPage organizationSelectionPage) {
        CreatePrimaryOrganizationPage createOrganizationPage = organizationSelectionPage.clickCreateNew();
        assertTrue(createOrganizationPage.isReturnToVerificationLinkPresent());
        EditPersonPage editPersonPage = (EditPersonPage) createOrganizationPage.clickPrevious();
        assertFalse(editPersonPage.isReturnToVerificationLinkPresent());
        createOrganizationPage = (CreatePrimaryOrganizationPage) editPersonPage.clickNext();
        createOrganizationPage.clickSearchAgain();
    }

    private Person editPerson(VerificationPage verificationPage, Organization organization) {
        PersonSelectionPage personSelectionPage = verificationPage.clickEditPerson().clickSearchAgain();
        checkCreateNewInvalidPersonRemovesReturnLink(personSelectionPage);
        Person newPerson = getExistingExternalPerson();
        EditPersonPage editPersonPage = personSelectionPage.getHelper().searchAndSelectPerson(
                newPerson);
        editPersonPage.getHelper().verifyFormValues(newPerson);
        ViewOrganizationPage viewOrganizationPage = (ViewOrganizationPage) editPersonPage.clickNext();
        viewOrganizationPage.getHelper().verifyInformationIsDisplayed(organization);
        verificationPage = (VerificationPage) viewOrganizationPage.clickNext();
        checkVerificationPage(verificationPage, newPerson, organization, INVESTIGATOR);
        return newPerson;
    }

    protected void checkCreateNewInvalidPersonRemovesReturnLink(PersonSelectionPage personSelectionPage) {
        EditPersonPage createPersonPage = personSelectionPage.clickCreateNew();
        assertTrue(createPersonPage.isReturnToVerificationLinkPresent());
        ViewSelectedRolesPage viewSelectedRolesPage = (ViewSelectedRolesPage) createPersonPage.clickPrevious();
        assertFalse(viewSelectedRolesPage.isReturnToVerificationLinkPresent());
        EditPersonPage editPersonPage = (EditPersonPage) viewSelectedRolesPage.clickNext();
        editPersonPage.clickSearchAgain();
    }

    private void completeInvestigatorRegistration(VerificationPage verificationPage, Person person,
            Organization organization) throws GridInvocationException {
        HomePage homePage = completeRegistration(verificationPage, INVESTIGATOR);
        homePage.getHelper().openInvestigatorAwaitingVerificationTask();
        checkNewProfile(person, organization);
    }

    private void checkNewProfile(Person person, Organization organization) throws GridInvocationException {
        HomePage homePage = openHomePage(testUser);
        ProfessionalContactInformationTab contactInformationTab = homePage.getInvestigatorMenu().clickProfile();
        contactInformationTab.getHelper().verifyProfessionalContactInformation(person);
        contactInformationTab.getHelper().verifyOrganization(organization);
        helper.checkGridGrouperForRoles(testUser, UserRoleType.INVESTIGATOR);
    }

    private HomePage completeRegistration(VerificationPage verificationPage, UserRoleType... roles)
            throws GridInvocationException {
        FunctionalityWarningDialog dialog = (FunctionalityWarningDialog) verificationPage.clickSave();
        dialog.getHelper().checkForRoles(roles);
        return (HomePage) dialog.clickConfirm();
    }
    
    @Test
    public void testPersonCantBeReusedForNewRegistration() throws GridInvocationException {
        addGroupMembership(UserRoleType.INVESTIGATOR);
        DataSetBuilder builder = createDataSetBuilder();
        Person usedPerson = builder.createInvestigator().get().getPerson();
        builder.build();
        PersonSelectionPage personSelectionPage = openPersonSelectionPage();
        checkCantSelectInUsePersonRecord(usedPerson, personSelectionPage);
        
    }

    private DataSetBuilder createDataSetBuilder() {
        return new DataSetBuilder(getDataLoader(), getGridResources());
    }

    private PersonSelectionPage openPersonSelectionPage() {
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        PersonSelectionPage personSelectionPage = (PersonSelectionPage) rolePage.clickNext();
        return personSelectionPage;
    }

    private void checkCantSelectInUsePersonRecord(final Person person, final PersonSelectionPage personSelectionPage) {
        personSelectionPage.getHelper().searchForPerson(person);
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure(USER_ALREADY_USED_ERROR_KEY);
        expectedFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                personSelectionPage.getHelper().selectPerson(person);
            }
        });
    }

    @Test
    public void testWildcardsCantBeUsedInPersonEmailSearch() throws GridInvocationException {
        addGroupMembership(UserRoleType.INVESTIGATOR);
        PersonSelectionPage selectPersonPage = openPersonSelectionPage();
        selectPersonPage.typeInSearchField("%@test.com");
        assertTrue(selectPersonPage.getSearchResults().isEmpty());
    }
    
    @Test
    public void testInvestigatorRegistrationProfileAlreadyExists() throws GridInvocationException {
        DataSetBuilder builder = createDataSetBuilder();
        Person personWithProfile = builder.createProfile().get().getPerson();
        builder.build();

        addGroupMembership(UserRoleType.INVESTIGATOR);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        EditPersonPage editPersonPage = selectPerson(personWithProfile, rolePage);
        OrganizationSelectionPage organizationSelectionPage = (OrganizationSelectionPage) editPersonPage.clickNext();
        VerificationPage verificationPage = (VerificationPage) selectOrganization(primaryOrganization, organizationSelectionPage);
        checkVerificationPage(verificationPage, personWithProfile, primaryOrganization, INVESTIGATOR);
        verificationPage.clickSave();
    }

    private EditPersonPage selectPerson(Person person, ViewSelectedRolesPage rolePage) {
        PersonSelectionPage selectPersonPage = (PersonSelectionPage) rolePage.clickNext();
        return selectPerson(person, selectPersonPage);
    }

    @Test
    public void testSponsorRepresentativeRegistration() throws GridInvocationException {
        String sponsorExternalId = getGridResources().getValidSponsorExternalId();
        String sponsorGroup = getSponsorGroupName(sponsorExternalId);
        Organization sponsor = getGridResources().getTestDataSource().getProtocolSponsorOrganization(sponsorExternalId);
        addGroupMembership(sponsorGroup);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        rolePage.getHelper().checkForRoles("Sponsor for " + sponsor.getName());
        EditPersonPage editPersonPage = selectPerson(getExistingExternalPerson(), rolePage);
        VerificationPage verificationPage = (VerificationPage) editPersonPage.clickNext();
        assertEquals(1, verificationPage.getSponsorOrganizationSection().getSponsorRepresentativeOrganizations().size());
        assertEquals(0, verificationPage.getSponsorOrganizationSection().getSponsorDelegateOrganizations().size());
        completeRegistration(verificationPage, SPONSOR);
    }

    private String getSponsorGroupName(String sponsorExternalId) {
        return UserRoleType.SPONSOR.getGroupName() + "_" + getNesIdExtension(sponsorExternalId);
    }

    @Test
    public void testSponsorDelegateRegistration() throws GridInvocationException {
        String sponsorExternalId = getGridResources().getValidSponsorExternalId();
        Organization sponsor = getGridResources().getTestDataSource().getProtocolSponsorOrganization(sponsorExternalId);
        String delegateGroup = getSponsorDelegateGroupName(sponsorExternalId);
        addGroupMembership(delegateGroup);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        rolePage.getHelper().checkForRoles("Sponsor Delegate for " + sponsor.getName());
        EditPersonPage editPersonPage = selectPerson(getExistingExternalPerson(), rolePage);
        VerificationPage verificationPage = (VerificationPage) editPersonPage.clickNext();
        assertEquals(0, verificationPage.getSponsorOrganizationSection().getSponsorRepresentativeOrganizations().size());
        assertEquals(1, verificationPage.getSponsorOrganizationSection().getSponsorDelegateOrganizations().size());
        completeRegistration(verificationPage, SPONSOR_DELEGATE);
    }

    private String getSponsorDelegateGroupName(String sponsorExternalId) {
        return UserRoleType.SPONSOR_DELEGATE.getGroupName() + "_" + getNesIdExtension(sponsorExternalId);
    }

    @Test
    public void testSponsorAndDelegateRegistration() throws GridInvocationException {
        String sponsor1ExternalId = getGridResources().getValidProtocolSponsorExternalId();
        Organization sponsor1 = getGridResources().getTestDataSource().getProtocolSponsorOrganization(sponsor1ExternalId);
        String sponsor2ExternalId = getGridResources().getValidProtocolSponsorExternalId();
        Organization sponsor2 = getGridResources().getTestDataSource().getProtocolSponsorOrganization(sponsor2ExternalId);
        String sponsorGroup = getSponsorGroupName(sponsor1ExternalId);
        String delegateGroup = getSponsorDelegateGroupName(sponsor2ExternalId);
        addGroupMemberships(sponsorGroup, delegateGroup);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        rolePage.getHelper().checkForRoles("Sponsor for " + sponsor1.getName(), "Sponsor Delegate for " + sponsor2.getName());
        EditPersonPage editPersonPage = selectPerson(getExistingExternalPerson(), rolePage);
        VerificationPage verificationPage = (VerificationPage) editPersonPage.clickNext();
        assertEquals(1, verificationPage.getSponsorOrganizationSection().getSponsorRepresentativeOrganizations().size());
        assertEquals(1, verificationPage.getSponsorOrganizationSection().getSponsorDelegateOrganizations().size());
        completeRegistration(verificationPage, SPONSOR, SPONSOR_DELEGATE);
    }

    @Test
    public void testRegisterInvestigatorWithNewEntities() throws GridInvocationException {
        Person newPerson = PersonFactory.getInstance().createWithoutExternalData();
        newPerson.setProviderNumber(ValueGenerator.getUniqueString());
        Organization newOrganization = OrganizationFactory.getInstance().createWithoutExternalData();
        addGroupMembership(UserRoleType.INVESTIGATOR);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        OrganizationSelectionPage selectOrgPage = createPerson(newPerson, rolePage);
        VerificationPage verificationPage = createOrganization(newOrganization, selectOrgPage);
        checkVerificationPage(verificationPage, newPerson, newOrganization, INVESTIGATOR);
        completeInvestigatorRegistration(verificationPage, newPerson, newOrganization);
    }

    private OrganizationSelectionPage createPerson(Person person, ViewSelectedRolesPage rolePage) {
        PersonSelectionPage selectPersonPage = (PersonSelectionPage) rolePage.clickNext();
        EditPersonPage editPersonPage = selectPersonPage.clickCreateNew();
        checkForAndClearDefaultValues(editPersonPage, newAccountPerson);
        clickNextAndValidateMissingFields(editPersonPage);
        editPersonPage.getHelper().enterPersonData(person);
        return (OrganizationSelectionPage) editPersonPage.clickNext();
    }

    private void checkForAndClearDefaultValues(EditPersonPage editPersonPage, Person person) {
        PersonForm personForm = editPersonPage.getPersonForm();
        assertEquals(person.getFirstName(), personForm.getFirstName());
        assertEquals(person.getLastName(), personForm.getLastName());
        assertEquals(person.getEmail(), personForm.getEmailAddress());
        personForm.typeFirstName("");
        personForm.typeLastName("");
        personForm.typeEmailAddress("");
    }

    private void clickNextAndValidateMissingFields(final EditPersonPage editPersonPage) {
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure();
        expectedFailure.addExpectedRequiredFields("person.firstName", "person.lastName", "person.email",
                "person.postalAddress.streetAddress", "person.postalAddress.city",
                "person.postalAddress.stateOrProvince", "person.postalAddress.postalCode");
        expectedFailure.addExpectedMessage("phone.number.required");
        expectedFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                editPersonPage.clickNext();
            }
        });
        checkStateNotSelected(editPersonPage);
    }

    private void checkStateNotSelected(final EditPersonPage editPersonPage) {
        AddressForm addressForm = editPersonPage.getPersonForm().getAddressForm();
        addressForm.selectCountry("CAN");
        checkForExpectedStateValidationBehavior(editPersonPage, false);
        addressForm.selectCountry(FirebirdConstants.US_COUNTRY_CODE);
        checkForExpectedStateValidationBehavior(editPersonPage, true);
    }

    private void checkForExpectedStateValidationBehavior(final EditPersonPage editPersonPage,
            boolean stateValidationMessageExpected) {
        try {
            new MessageHandler(getDriver(), editPersonPage).waitUntilReady().checkForValidationErrors();
            fail("Should have thrown validation error");
        } catch (ValidationException e) {
            assertEquals(
                    stateValidationMessageExpected,
                    e.getValidationMessages().contains(
                            VerificationUtils.getRequiredFieldMessage("person.postalAddress.stateOrProvince")));
        }
    }

    private VerificationPage createOrganization(Organization organization, OrganizationSelectionPage selectOrgPage) {
        CreatePrimaryOrganizationPage createOrganizationPage = selectOrgPage.clickCreateNew();
        clickNextAndValidateMissingFields(createOrganizationPage);
        createOrganizationPage.getHelper().enterOrganizationData(organization, PracticeSiteType.CANCER_CENTER);
        return (VerificationPage) createOrganizationPage.clickNext();
    }

    private void clickNextAndValidateMissingFields(final CreatePrimaryOrganizationPage createOrganizationPage) {
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure();
        expectedFailure.addExpectedRequiredFields("organization.name", "organization.email", "organization.postalAddress.streetAddress",
                "organization.postalAddress.city", "organization.postalAddress.stateOrProvince",
                "organization.postalAddress.postalCode");
        expectedFailure.addExpectedMessage("organization.type.required");
        expectedFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                createOrganizationPage.clickNext();
            }
        });
    }

    @Test
    public void testRegisterRegistrationCoordinator() throws GridInvocationException {
        DataSetBuilder builder = createDataSetBuilder();
        investigatorMap = Maps.newHashMap();
        investigatorMap.put(fbciinv2, builder.createInvestigator().withLogin(fbciinv2).get());
        investigatorMap.put(fbciinv3, builder.createInvestigator().withLogin(fbciinv3).get());
        investigatorMap.put(fbciinv4, builder.createInvestigator().withLogin(fbciinv4).get());
        builder.build();
        Person coordinatorPerson = getExistingExternalPerson();
        addGroupMembership(UserRoleType.REGISTRATION_COORDINATOR);
        VerificationPage verificationPage = fillInCoordinatorRegistration(coordinatorPerson);

        verificationPage = returnAndModifySelectedInvestigators(verificationPage);
        verifySelectedInvestigatorsDisplayed(verificationPage, fbciinv3);
        HomePage homePage = completeRegistration(verificationPage, REGISTRATION_COORDINATOR);
        checkCoordinatorAwaitingVerificationTask(homePage);
        checkCoordinatorEmail();
    }

    private VerificationPage fillInCoordinatorRegistration(Person coordinatorPerson) {
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        List<String> rolesNames = rolePage.getRoleNames();
        assertEquals(1, rolesNames.size());
        assertEquals(UserRoleType.REGISTRATION_COORDINATOR.getDisplay(), rolesNames.get(0));
        EditPersonPage editPersonPage = selectPerson(coordinatorPerson, rolePage);

        InvestigatorSelectionPage investigatorSelectPage = (InvestigatorSelectionPage) editPersonPage.clickNext();
        checkNoInvestigatorsSelected(investigatorSelectPage);
        addInvestigatorsAndCheckSave(investigatorSelectPage);
        removeAndReAddInvestigator(investigatorSelectPage);

        searchAndSelectProfile(getSavedProfile(fbciinv3), investigatorSelectPage);
        assertEquals(3, investigatorSelectPage.getHelper().getSelectedInvestigatorCount());
        VerificationPage verificationPage = verifyAddedInvestigatorsOnVerification(investigatorSelectPage,
                coordinatorPerson);
        return verificationPage;
    }

    private void checkNoInvestigatorsSelected(InvestigatorSelectionPage investigatorSelectPage) {
        verifySelectedInvestigatorsRequired(investigatorSelectPage);
        investigatorSelectPage.searchForInvestigator(INVALID_USERNAME);
        assertEquals(getPropertyText("no.matching.search.results"), investigatorSelectPage.getSearchInfoMessage());
    }

    private InvestigatorProfile getSavedProfile(InvestigatorLogin login) {
        return investigatorMap.get(login).getInvestigatorRole().getProfile();
    }

    private void verifySelectedInvestigatorsRequired(final InvestigatorSelectionPage selectionPage) {
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure(
                "user.registration.investigator.selection.no.investigators.selected");
        expectedFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                selectionPage.clickNext();
            }
        });
    }

    private void addInvestigatorsAndCheckSave(InvestigatorSelectionPage investigatorSelectPage) {
        searchAndSelectProfile(getSavedProfile(fbciinv4), investigatorSelectPage);
        searchAndSelectProfile(getSavedProfile(fbciinv2), investigatorSelectPage);

        investigatorSelectPage = (InvestigatorSelectionPage) investigatorSelectPage.clickPrevious().clickNext();
        assertEquals(2, investigatorSelectPage.getHelper().getSelectedInvestigatorCount());
    }

    private void searchAndSelectProfile(InvestigatorProfile profile, InvestigatorSelectionPage investigatorSelectPage) {
        investigatorSelectPage.getHelper().searchForInvestigator(profile);
        investigatorSelectPage.getHelper().checkProfileInResults(profile);
        investigatorSelectPage.getHelper().addInvestigator(profile);
        String expectedMessage = profile.getPerson().getDisplayName() + " "
                + getPropertyText("user.registration.investigator.selection.person.added");
        assertEquals(expectedMessage, investigatorSelectPage.getSuccessMessages().get(0));
        investigatorSelectPage.getHelper().checkProfileAdded(profile);
    }

    private void removeAndReAddInvestigator(InvestigatorSelectionPage investigatorSelectPage) {
        searchAndRemoveProfile(getSavedProfile(fbciinv4), investigatorSelectPage);
        assertEquals(1, investigatorSelectPage.getHelper().getSelectedInvestigatorCount());
        searchAndSelectProfile(getSavedProfile(fbciinv4), investigatorSelectPage);
    }

    private void searchAndRemoveProfile(InvestigatorProfile profile, InvestigatorSelectionPage investigatorSelectPage) {
        investigatorSelectPage.getHelper().searchForInvestigator(profile);
        investigatorSelectPage.getHelper().checkProfileInResults(profile);
        investigatorSelectPage.getHelper().checkForAddedCheckMark(profile);
        investigatorSelectPage.getHelper().removeInvestigator(profile);
        String expectedMessage = profile.getPerson().getDisplayName() + " "
                + getPropertyText("user.registration.investigator.selection.person.removed");
        assertEquals(expectedMessage, investigatorSelectPage.getSuccessMessages().get(0));
        investigatorSelectPage.getHelper().checkForAddButton(profile);
    }

    private VerificationPage verifyAddedInvestigatorsOnVerification(InvestigatorSelectionPage investigatorSelectPage,
            Person person) {
        VerificationPage verificationPage = (VerificationPage) investigatorSelectPage.clickNext();
        verificationPage.getHelper().verifyPersonInformationIsDisplayed(person);
        verificationPage.getHelper().verifyRoles(REGISTRATION_COORDINATOR);
        verifySelectedInvestigatorsDisplayed(verificationPage, fbciinv4, fbciinv2, fbciinv3);
        return verificationPage;
    }

    private void verifySelectedInvestigatorsDisplayed(VerificationPage verificationPage, InvestigatorLogin... logins) {
        InvestigatorProfile[] profiles = new InvestigatorProfile[logins.length];
        for (int i = 0; i < logins.length; i++) {
            profiles[i] = getSavedProfile(logins[i]);
        }
        verificationPage.getHelper().verifySelectedInvestigatorsDisplayed(profiles);
    }

    private VerificationPage returnAndModifySelectedInvestigators(VerificationPage verificationPage) {
        InvestigatorSelectionPage investigatorSelectPage = verificationPage.getHelper()
                .clickEditSelectedInvestigators();
        searchAndRemoveProfile(getSavedProfile(fbciinv4), investigatorSelectPage);
        searchAndRemoveProfile(getSavedProfile(fbciinv2), investigatorSelectPage);
        return investigatorSelectPage.clickReturnToVerification();
    }

    private void checkCoordinatorAwaitingVerificationTask(HomePage homePage) {
        BrowseInvestigatorsPage browseInvestigatorsPage = homePage.getHelper()
                .openCoordinatorAwaitingVerificationTask();
        InvestigatorListing investigatorListing = browseInvestigatorsPage.getHelper().getInvestigatorListing(
                getSavedProfile(fbciinv3));
        assertEquals(ManagedInvestigatorStatus.AWAITING_APPROVAL.getDisplay(), investigatorListing.getStatus());
    }

    private void checkCoordinatorEmail() {
        getEmailChecker().assertEmailCount(1);
        MimeMessage[] messages = getEmailChecker().getSentEmails();
        getEmailChecker().checkTo(messages[0], getSavedProfile(fbciinv3).getPerson().getEmail());
    }

    @Test
    public void testPersonAutoSelection() throws Exception {
        nesPersonService.save(newAccountPerson);
        addGroupMembership(UserRoleType.INVESTIGATOR);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        EditPersonPage editPersonPage = (EditPersonPage) rolePage.clickNext();
        editPersonPage.getHelper().verifyFormValues(newAccountPerson);
        editPersonPage.clickNext();
        assertTrue(OrganizationSelectionPage.FACTORY.isDisplayed(getDriver()));
    }

    @Test
    public void testPersonAutoSelection_AlreadySelected() throws Exception {
        simulatePersonRecordAlreadySelected();
        addGroupMembership(UserRoleType.INVESTIGATOR);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        EditPersonPage editPersonPage = (EditPersonPage) rolePage.clickNext();
        editPersonPage.getHelper().verifyFormValues(newAccountPerson);
        checkForUserAlreadyAssociatedMessage(editPersonPage);
    }

    private EditPersonPage checkForUserAlreadyAssociatedMessage(final EditPersonPage editPersonPage) {
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure(USER_ALREADY_USED_ERROR_KEY);
        expectedFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                editPersonPage.clickNext();
            }
        });
        return editPersonPage;
    }

    private void simulatePersonRecordAlreadySelected() throws Exception {
        nesPersonService.save(newAccountPerson);
        DataSetBuilder builder = createDataSetBuilder();
        InvestigatorProfile profile = builder.createProfile().withPerson(newAccountPerson).get();
        builder.createInvestigator(profile);
        builder.build();
    }

    @Test
    public void testPersonAutoSearchByEmailAddress() throws Exception {
        nesPersonService.save(newAccountPerson);
        Person person2 = PersonFactory.getInstance().createWithoutExternalData();
        person2.setEmail(newAccountPerson.getEmail());
        nesPersonService.save(person2);
        addGroupMembership(UserRoleType.INVESTIGATOR);
        PersonSelectionPage personSelectionPage = openPersonSelectionPage();
        assertEquals(newAccountPerson.getEmail(), personSelectionPage.getSearchFieldValue());
        assertEquals(2, personSelectionPage.getSearchResults().size());
    }

    @Test
    public void testPersonAutoSearchByName() throws Exception {
        newAccountPerson.setEmail(getUniqueEmailAddress());
        nesPersonService.save(newAccountPerson);
        Person person2 = PersonFactory.getInstance().createWithoutExternalData();
        person2.setFirstName(newAccountPerson.getFirstName());
        person2.setLastName(newAccountPerson.getLastName());
        nesPersonService.save(person2);
        addGroupMembership(UserRoleType.INVESTIGATOR);
        PersonSelectionPage personSelectionPage = openPersonSelectionPage();
        String expectedSearchString = newAccountPerson.getLastName() + ", " + newAccountPerson.getFirstName();
        assertEquals(expectedSearchString, personSelectionPage.getSearchFieldValue());
        assertEquals(2, personSelectionPage.getSearchResults().size());
    }

    @Test
    public void testNoFunctionalityWarningIfRolesAreVerified() throws GridInvocationException {
        String sponsorExternalId = getGridResources().getValidSponsorExternalId();
        Organization sponsor = getGridResources().getTestDataSource().getProtocolSponsorOrganization(sponsorExternalId);
        String sponsorGroup = getSponsorGroupName(sponsorExternalId);
        String verifiedSponsorGroup = "verified_" + sponsorGroup;
        addGroupMemberships(sponsorGroup, verifiedSponsorGroup);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        List<String> roleNames = rolePage.getRoleNames();
        assertEquals(1, roleNames.size());
        assertEquals("Sponsor for " + sponsor.getName(), roleNames.get(0));
        EditPersonPage editPersonPage = selectPerson(getExistingExternalPerson(), rolePage);
        VerificationPage verificationPage = (VerificationPage) editPersonPage.clickNext();
        assertEquals(1, verificationPage.getSponsorOrganizationSection().getSponsorRepresentativeOrganizations().size());
        assertEquals(0, verificationPage.getSponsorOrganizationSection().getSponsorDelegateOrganizations().size());
        assertTrue(verificationPage.clickSave() instanceof HomePage);
    }

    @Test
    public void testOnlyUnverifiedRolesShownInFunctionalityWarning() throws GridInvocationException {
        String sponsorGroup = getSponsorGroupName(getGridResources().getValidSponsorExternalId());
        String verifiedSponsorGroup = "verified_" + sponsorGroup;
        String investigatorGroup = UserRoleType.INVESTIGATOR.getGroupName();
        addGroupMemberships(investigatorGroup, sponsorGroup, verifiedSponsorGroup);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        EditPersonPage editPersonPage = selectPerson(getExistingExternalPerson(), rolePage);
        OrganizationSelectionPage organizationSelectionPage = (OrganizationSelectionPage) editPersonPage.clickNext();
        VerificationPage verificationPage = (VerificationPage) selectOrganization(primaryOrganization, organizationSelectionPage);
        completeRegistration(verificationPage, UserRoleType.INVESTIGATOR);
    }

    @Test
    public void testInvestigatorLeaveFlow() throws GridInvocationException {
        addGroupMembership(UserRoleType.INVESTIGATOR);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        assertTrue(rolePage.clickCancelRegistration() instanceof LoginPage);
    }

}