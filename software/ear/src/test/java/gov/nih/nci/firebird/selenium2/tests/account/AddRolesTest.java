package gov.nih.nci.firebird.selenium2.tests.account;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import gov.nih.nci.firebird.cagrid.GridInvocationException;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.user.MyAccountPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.AbstractSponsorSelectionPage.SponsorListing;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.EditPersonPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.FunctionalityWarningDialog;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.InvestigatorSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.OrganizationSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.PersonSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.RoleSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.SponsorDelegateSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.SponsorRepresentativeSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.VerificationPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.ViewSelectedRolesPage;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;
import gov.nih.nci.firebird.test.data.SponsorBuilder;
import org.junit.Test;

import javax.mail.internet.MimeMessage;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import static gov.nih.nci.firebird.data.user.UserRoleType.INVESTIGATOR;
import static gov.nih.nci.firebird.data.user.UserRoleType.REGISTRATION_COORDINATOR;
import static gov.nih.nci.firebird.data.user.UserRoleType.SPONSOR;
import static gov.nih.nci.firebird.data.user.UserRoleType.SPONSOR_DELEGATE;
import static gov.nih.nci.firebird.nes.NesIdTestUtil.getNesIdExtension;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddRolesTest extends AbstractFirebirdWebDriverTest {

    @Inject
    @Named("firebird.email.support.address")
    private String supportEmailAddress;

    @Inject
    private RegistrationTestHelper registrationTestHelper;

    @Inject
    private DataSetBuilder builder;

    private LoginAccount testUserAccount;

    private DataSet dataSet;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        testUserAccount = registrationTestHelper.createNewGridUser();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        registrationTestHelper.removeGridUser(testUserAccount);
    }

    @Test
    public void testInvestigatorLeaveFlow() {
        builder.createInvestigator().get();
        dataSet = builder.build();
        MyAccountPage accountPage = openHomePage(dataSet.getInvestigatorLogin()).clickMyAccount();
        accountPage.getHelper().assertAddRolesButtonIsDisplayed();
        RoleSelectionPage rolePage = accountPage.clickAddRoles();
        rolePage.getHelper().checkOnlyRolesDisplayed(REGISTRATION_COORDINATOR, SPONSOR, SPONSOR_DELEGATE);
        rolePage.selectSponsorRole();
        SponsorRepresentativeSelectionPage sponsorPage = (SponsorRepresentativeSelectionPage) rolePage.clickNext();
        ((MyAccountPage) sponsorPage.clickCancelRegistration()).waitUntilReady();
    }

    @Test
    public void testInvestigatorAddAllRoles() throws GridInvocationException {
        InvestigatorProfile investigatorToCoordinate = getInvestigator();
        MyAccountPage accountPage = openHomePage(dataSet.getInvestigatorLogin()).clickMyAccount();
        accountPage.getHelper().assertAddRolesButtonIsDisplayed();
        UserRoleType[] rolesToRegister = new UserRoleType[] { REGISTRATION_COORDINATOR, SPONSOR, SPONSOR_DELEGATE };

        RoleSelectionPage rolePage = accountPage.clickAddRoles();
        rolePage.getHelper().checkOnlyRolesDisplayed(rolesToRegister);
        rolePage.selectRegistrationCoordinatorRole();
        rolePage.selectSponsorDelegateRole();
        rolePage.selectSponsorRole();
        InvestigatorSelectionPage investigatorsPage = (InvestigatorSelectionPage) rolePage.clickNext();
        investigatorsPage.getHelper().searchForInvestigator(investigatorToCoordinate);
        investigatorsPage.getHelper().addInvestigator(investigatorToCoordinate);
        SponsorRepresentativeSelectionPage sponsorsPage = (SponsorRepresentativeSelectionPage) investigatorsPage
                .clickNext();
        List<SponsorListing> listings = sponsorsPage.getSponsorListings();
        listings.get(0).select();
        SponsorDelegateSelectionPage delegatesPage = (SponsorDelegateSelectionPage) sponsorsPage.clickNext();
        assertFalse(delegatesPage.getHelper().getSelectedSponsors().isEmpty());
        listings = delegatesPage.getSponsorListings();
        assertFalse(listings.get(0).isSelectable());
        listings.get(listings.size() - 1).select();
        VerificationPage verificationPage = (VerificationPage) delegatesPage.clickNext();
        verificationPage.getHelper().verifyRoles(rolesToRegister);
        verificationPage.getHelper().verifySelectedInvestigatorsDisplayed(investigatorToCoordinate);
        verificationPage.getHelper().verifySponsorRepresentativesAreDisplayed(listings.subList(0, 1));
        verificationPage.getHelper().verifySponsorDelegatesAreDisplayed(listings.subList(1, 2));
        FunctionalityWarningDialog warningDialog = (FunctionalityWarningDialog) verificationPage.clickSave();
        warningDialog.getHelper().checkForRoles(REGISTRATION_COORDINATOR);
        accountPage = (MyAccountPage) warningDialog.clickConfirm();
        accountPage.getHelper().checkSelectedRolesDisplayed(rolesToRegister);
    }

    private InvestigatorProfile getInvestigator() throws GridInvocationException {
        registrationTestHelper.addGroupMemberships(testUserAccount.getUsername(), INVESTIGATOR.getGroupName());

        builder.createInvestigator().withLogin(testUserAccount);
        InvestigatorProfile investigatorToCoordinate = builder.createInvestigator().get().getInvestigatorRole()
                .getProfile();
        dataSet = builder.build();
        return investigatorToCoordinate;
    }

    @Test
    public void testSponsorAddInvestigatorRole() throws GridInvocationException {
        Organization primaryOrg = getExistingPrimaryOrganization();
        RoleSelectionPage rolePage = startSponsorTest(false);
        rolePage.getHelper().checkOnlyRolesDisplayed(INVESTIGATOR, REGISTRATION_COORDINATOR, SPONSOR_DELEGATE);
        rolePage.selectInvestigatorRole();
        OrganizationSelectionPage orgPage = (OrganizationSelectionPage) rolePage.clickNext();
        orgPage.getHelper().searchForOrganization(primaryOrg);
        VerificationPage verificationPage = (VerificationPage) orgPage.getHelper().selectOrganization(primaryOrg);
        verificationPage.getHelper().verifyRoles(INVESTIGATOR);
        verificationPage.getHelper().verifyPrimaryOrganizationInformationIsDisplayed(primaryOrg);
        completeAndVerifyRolesAddedNoWarning(verificationPage, INVESTIGATOR);
    }

    private void completeAndVerifyRolesAddedNoWarning(VerificationPage verificationPage, UserRoleType... roles) {
        MyAccountPage accountPage = (MyAccountPage) verificationPage.clickSave();
        accountPage.getHelper().checkSelectedRolesDisplayed(roles);
    }

    private RoleSelectionPage startSponsorTest(boolean performAsDelegate) throws GridInvocationException {
        addAllRolesToTestUser();
        SponsorBuilder sponsorBuilder = builder.createSponsor().withLogin(testUserAccount);
        if (performAsDelegate) {
            sponsorBuilder.asDelegate();
        }
        dataSet = builder.build();
        HomePage homePage = openHomePage(dataSet.getSponsorLogin());
        MyAccountPage accountPage = homePage.clickMyAccount();
        accountPage.getHelper().assertAddRolesButtonIsDisplayed();
        accountPage.getHelper().checkSelectedRolesDisplayed(performAsDelegate ? SPONSOR_DELEGATE : SPONSOR);
        return accountPage.clickAddRoles();
    }

    private void addAllRolesToTestUser() throws GridInvocationException {
        Set<String> groupNames = Sets.newHashSet(INVESTIGATOR.getVerifiedGroupName(),
                REGISTRATION_COORDINATOR.getGroupName());
        for (String sponsorId : getGridResources().getSponsorWithProtocolRegistrationsExternalIds()) {
            groupNames.add(SPONSOR.getVerifiedGroupName() + "_" + getNesIdExtension(sponsorId));
            groupNames.add(SPONSOR_DELEGATE.getVerifiedGroupName() + "_" + getNesIdExtension(sponsorId));
        }
        registrationTestHelper.addGroupMemberships(testUserAccount.getUsername(),
                groupNames.toArray(new String[ groupNames.size() ]));
    }

    @Test
    public void testSponsorAddDelegateRole() throws GridInvocationException {
        RoleSelectionPage rolePage = startSponsorTest(false);
        rolePage.getHelper().checkOnlyRolesDisplayed(INVESTIGATOR, REGISTRATION_COORDINATOR, SPONSOR_DELEGATE);
        rolePage.selectSponsorDelegateRole();
        SponsorDelegateSelectionPage delegatePage = (SponsorDelegateSelectionPage) rolePage.clickNext();
        List<SponsorListing> selectedSponsors = delegatePage.getHelper().getSelectedSponsors();
        assertFalse(selectedSponsors.isEmpty());
        assertEquals(1, selectedSponsors.size());
        List<SponsorListing> availableSponsors = delegatePage.getHelper().getAvailableSponsorListings();
        availableSponsors.get(0).select();

        VerificationPage verificationPage = (VerificationPage) delegatePage.clickNext();
        verificationPage.getHelper().verifyRoles(SPONSOR_DELEGATE);
        verificationPage.getHelper().verifySponsorDelegatesAreDisplayed(availableSponsors.subList(0, 1));
        completeAndVerifyRolesAddedNoWarning(verificationPage, SPONSOR_DELEGATE);
    }

    @Test
    public void testDelegateAddSponsorRole() throws GridInvocationException {
        RoleSelectionPage rolePage = startSponsorTest(true);
        rolePage.getHelper().checkOnlyRolesDisplayed(INVESTIGATOR, REGISTRATION_COORDINATOR, SPONSOR);
        rolePage.selectSponsorRole();
        SponsorRepresentativeSelectionPage sponsorsPage = (SponsorRepresentativeSelectionPage) rolePage.clickNext();
        List<SponsorListing> selectedSponsors = sponsorsPage.getHelper().getSelectedSponsors();
        assertFalse(selectedSponsors.isEmpty());
        assertEquals(1, selectedSponsors.size());
        List<SponsorListing> availableSponsors = sponsorsPage.getHelper().getAvailableSponsorListings();
        availableSponsors.get(0).select();

        VerificationPage verificationPage = (VerificationPage) sponsorsPage.clickNext();
        verificationPage.getHelper().verifyRoles(SPONSOR);
        verificationPage.getHelper().verifySponsorRepresentativesAreDisplayed(availableSponsors.subList(0, 1));
        completeAndVerifyRolesAddedNoWarning(verificationPage, SPONSOR);
    }

    @Test
    public void testAddRolesCreatesGroupMembershipAndSendsEmail() throws RemoteException, GridInvocationException {
        Person person = getExistingExternalPerson();
        HomePage homePage = setUpAsNewSponsor(testUserAccount, person);
        MyAccountPage myAccountPage = homePage.clickMyAccount();
        RoleSelectionPage roleSelectionPage = myAccountPage.clickAddRoles();
        roleSelectionPage.selectInvestigatorRole();
        roleSelectionPage.selectSponsorDelegateRole();
        OrganizationSelectionPage selectOrganizationPage = (OrganizationSelectionPage) roleSelectionPage.clickNext();
        SponsorDelegateSelectionPage delegatePage = (SponsorDelegateSelectionPage) selectOrganizationPage.getHelper()
                .searchAndSelectOrganization(getExistingPrimaryOrganization());
        SponsorListing selectedSponsorForDelegate = delegatePage.getHelper().getAvailableSponsorListings().get(0);
        selectedSponsorForDelegate.select();
        VerificationPage verificationPage = (VerificationPage) delegatePage.clickNext();
        completeAndVerifyRolesAdded(verificationPage, INVESTIGATOR, SPONSOR_DELEGATE);
        checkForAddedRolesEmail(person, selectedSponsorForDelegate);
        String investigatorGroup = INVESTIGATOR.getGroupName();
        String sponsorDelegateExternalId = getGridResources().getValidProtocolSponsorExternalId();
        String sponsorDelegateGroup = SPONSOR_DELEGATE.getGroupName() + "_" + getNesIdExtension(sponsorDelegateExternalId);
        registrationTestHelper.checkGridGrouperForGroups(testUserAccount, investigatorGroup, sponsorDelegateGroup);
    }

    private void completeAndVerifyRolesAdded(VerificationPage verificationPage, UserRoleType... roles) {
        FunctionalityWarningDialog warningDialog = (FunctionalityWarningDialog) verificationPage.clickSave();
        warningDialog.getHelper().checkForRoles(roles);
        MyAccountPage accountPage = (MyAccountPage) warningDialog.clickConfirm();
        accountPage.getHelper().checkSelectedRolesDisplayed(roles);
    }

    private HomePage setUpAsNewSponsor(LoginAccount testUser, Person person) throws GridInvocationException {
        String sponsorGroup = getSponsorGroupName();
        String verifiedSponsorGroup = "verified_" + sponsorGroup;
        registrationTestHelper.addGroupMemberships(testUser.getUsername(), sponsorGroup, verifiedSponsorGroup);
        ViewSelectedRolesPage rolesPage = openLoginPage().getHelper().goToSelectedRolesPage(testUser, getProvider());
        PersonSelectionPage selectPersonPage = (PersonSelectionPage) rolesPage.clickNext();
        selectPersonPage.getHelper().searchForPerson(person);
        EditPersonPage editPersonPage = selectPersonPage.getHelper().selectPerson(person);
        VerificationPage verificationPage = (VerificationPage) editPersonPage.clickNext();
        HomePage homePage = (HomePage) verificationPage.clickSave();
        return homePage;
    }

    private String getSponsorGroupName() {
        return UserRoleType.SPONSOR.getGroupName() + "_" + getNesIdExtension(getGridResources().getValidProtocolSponsorExternalId());
    }

    private void checkForAddedRolesEmail(Person person, SponsorListing selectedSponsorForDelegate) {
        getEmailChecker().assertEmailCount(1);
        MimeMessage message = getEmailChecker().getSentEmails()[ 0 ];
        String expectedSubject = getPropertyText("template.string.ADDED_ROLES_SUPPORT_NOTIFICATION_EMAIL_SUBJECT");
        expectedSubject = expectedSubject.replace("$FIREBIRD_USER.Person.DisplayName", person.getDisplayName());
        getEmailChecker().checkSubject(message, expectedSubject);
        getEmailChecker().checkTo(message, supportEmailAddress);
        assertTrue(getEmailChecker().getContent(message).contains(INVESTIGATOR.getDisplay()));
        assertTrue(getEmailChecker().getContent(message).contains(
                SPONSOR_DELEGATE.getDisplay() + " for " + selectedSponsorForDelegate.getName()));
    }

    @Test
    public void testEditSponsorDelegateRoleFromVerificationPage() {
        builder.createInvestigator();
        dataSet = builder.build();
        MyAccountPage accountPage = openHomePage(dataSet.getInvestigatorLogin()).clickMyAccount();
        RoleSelectionPage roleSelectionPage = accountPage.clickAddRoles();
        roleSelectionPage.selectSponsorDelegateRole();
        roleSelectionPage.selectSponsorRole();
        SponsorRepresentativeSelectionPage sponsorPage = (SponsorRepresentativeSelectionPage) roleSelectionPage
                .clickNext();
        checkCantSelectBothOrganizations(sponsorPage);
        sponsorPage.getHelper().getAvailableSponsorListings().get(0).select();
        SponsorDelegateSelectionPage delegateSelectionPage = (SponsorDelegateSelectionPage) sponsorPage.clickNext();
        delegateSelectionPage.getHelper().getAvailableSponsorListings().get(0).select();
        VerificationPage verificationPage = (VerificationPage) delegateSelectionPage.clickNext();

        delegateSelectionPage = verificationPage.getSponsorOrganizationSection()
                .clickEditSponsorDelegateOrganizations();
        delegateSelectionPage.getHelper().getAvailableSponsorListings().get(0).deselect();
        assertTrue(delegateSelectionPage.isReturnToVerificationLinkPresent());
        SponsorRepresentativeSelectionPage representativeSelectionPage = (SponsorRepresentativeSelectionPage) delegateSelectionPage
                .clickPrevious();
        assertFalse(representativeSelectionPage.isReturnToVerificationLinkPresent());
        representativeSelectionPage.clickNext();
        delegateSelectionPage.getHelper().getAvailableSponsorListings().get(0).select();
        delegateSelectionPage.clickNext();
    }

    private void checkCantSelectBothOrganizations(final SponsorRepresentativeSelectionPage sponsorPage) {
        sponsorPage.getHelper().getAvailableSponsorListings().get(0).select();
        sponsorPage.getHelper().getAvailableSponsorListings().get(1).select();
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure(
                "user.registration.sponsor.organization.selection.blocks.delegate.selection.message");
        expectedFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                sponsorPage.clickNext();
            }
        });
        checkPreviousNavigationAllowed(sponsorPage);
    }

    private void checkPreviousNavigationAllowed(SponsorRepresentativeSelectionPage sponsorPage) {
        RoleSelectionPage roleSelectionPage = (RoleSelectionPage) sponsorPage.clickPrevious();
        sponsorPage = (SponsorRepresentativeSelectionPage) roleSelectionPage.clickNext();
        assertEquals(2, sponsorPage.getHelper().getSelectedSponsors().size());
        sponsorPage.getHelper().getAvailableSponsorListings().get(0).deselect();
        sponsorPage.getHelper().getAvailableSponsorListings().get(1).deselect();
    }

}
