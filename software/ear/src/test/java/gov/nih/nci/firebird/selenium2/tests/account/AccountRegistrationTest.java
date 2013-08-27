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
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigatorStatus;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.nes.person.NesPersonIntegrationService;
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
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.CreateProtocolPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInformationTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.protocol.AddInvestigatorsDialog;
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
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.LoginAccount.InvestigatorLogin;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.rmi.RemoteException;
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

    private DataSetBuilder builder;

    private LoginAccount testUser;

    private Person newAccountPerson = PersonFactory.getInstance().create();

    @Inject
    private NesPersonIntegrationService nesPersonService;

    private Organization organization1;

    private Map<InvestigatorLogin, FirebirdUser> investigatorMap;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        builder = new DataSetBuilder(getDataLoader(), getGridResources());
        newAccountPerson.setNesId(null);
        testUser = helper.createNewGridUser(newAccountPerson);
        organization1 = getExistingPrimaryOrganization();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        helper.removeGridUser(testUser);
    }

    @Test
    public void testInvestigatorRegistration() throws GridInvocationException {
        builder.createInvestigator();
        DataSet dataSet = builder.build();
        Person usedPerson = dataSet.getInvestigator().getPerson();

        Person person = getExistingNesPerson();
        helper.addGroupMemberships(testUser.getUsername(), UserRoleType.INVESTIGATOR.getGroupName());

        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        List<String> roleNames = rolePage.getRoleNames();
        assertEquals(1, roleNames.size());
        assertEquals(UserRoleType.INVESTIGATOR.getDisplay(), roleNames.get(0));
        PersonSelectionPage selectPersonPage = selectPreviouslySelectedPerson(usedPerson, rolePage);
        EditPersonPage editPersonPage = selectPerson(person, selectPersonPage);
        VerificationPage verificationPage = (VerificationPage) selectOrganization(organization1, editPersonPage);
        ViewOrganizationPage viewOrgPage = (ViewOrganizationPage) verificationPage.clickPrevious();
        viewOrgPage.getHelper().verifyInformationIsDisplayed(organization1);
        verificationPage = (VerificationPage) viewOrgPage.clickNext();
        checkVerificationPage(verificationPage, person, organization1, INVESTIGATOR);
        Organization newOrganization = editOrganization(verificationPage, person, organization1);
        Person newPerson = editPerson(verificationPage, person, newOrganization);
        completeInvestigatorRegistration(verificationPage, newPerson, newOrganization);
    }

    private ViewSelectedRolesPage openSelectedRolesPage() {
        return openLoginPage().getHelper().goToSelectedRolesPage(testUser, getProvider());
    }

    private PersonSelectionPage selectPreviouslySelectedPerson(final Person person, ViewSelectedRolesPage rolePage) {
        final PersonSelectionPage selectPersonPage = checkInvalidEmailAddressCharacters(rolePage);
        selectPersonPage.getHelper().searchForPerson(person);
        selectPersonPage.getHelper().checkPersonInResults(person);
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure(USER_ALREADY_USED_ERROR_KEY);
        expectedFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                selectPersonPage.getHelper().selectPerson(person);
            }
        });
        return selectPersonPage;
    }

    private PersonSelectionPage checkInvalidEmailAddressCharacters(ViewSelectedRolesPage rolePage) {
        PersonSelectionPage selectPersonPage = (PersonSelectionPage) rolePage.clickNext();
        selectPersonPage.typeInSearchField("%@test.com");
        assertTrue(selectPersonPage.getSearchResults().size() == 0);
        return (PersonSelectionPage) selectPersonPage.clickPrevious().clickNext();
    }

    private EditPersonPage selectPerson(Person person, PersonSelectionPage selectPersonPage) {
        selectPersonPage.getHelper().searchForPerson(person);
        selectPersonPage.getHelper().checkPersonInResults(person);
        EditPersonPage editPersonPage = selectPersonPage.getHelper().selectPerson(person);
        editPersonPage.getHelper().verifyFormValues(person);
        return editPersonPage;
    }

    private AbstractAccountRegistrationPage<?> selectOrganization(Organization organization,
            EditPersonPage editPersonPage) {
        OrganizationSelectionPage selectOrgPage = (OrganizationSelectionPage) editPersonPage.clickNext();
        selectOrgPage = clickCreateNewThenSearchAgain(selectOrgPage);
        selectOrgPage.getHelper().searchForOrganization(organization);
        return selectOrgPage.getHelper().selectOrganization(organization1);
    }

    private OrganizationSelectionPage clickCreateNewThenSearchAgain(OrganizationSelectionPage selectOrgPage) {
        return selectOrgPage.clickCreateNew().clickSearchAgain();
    }

    private void checkVerificationPage(VerificationPage verificationPage, Person person, Organization organization,
            UserRoleType... roles) {
        verificationPage.getHelper().verifyPersonInformationIsDisplayed(person);
        verificationPage.getHelper().verifyPrimaryOrganizationInformationIsDisplayed(organization);
        verificationPage.getHelper().verifyRoles(roles);
    }

    private Organization editOrganization(VerificationPage verificationPage, Person person, Organization organization) {
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

    private Person editPerson(VerificationPage verificationPage, Person person, Organization organization) {
        PersonSelectionPage personSelectionPage = verificationPage.clickEditPerson().clickSearchAgain();
        checkCreateNewInvalidPersonRemovesReturnLink(personSelectionPage);
        Person newPerson = getExistingNesPerson();
        EditPersonPage editPersonPage = (EditPersonPage) personSelectionPage.getHelper().searchAndSelectPerson(
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
    public void testInvestigatorRegistrationProfileAlreadyExists() throws GridInvocationException {
        builder.createSponsor();
        DataSet dataSet = builder.build();
        Person existingPerson = getExistingNesPerson();

        createProtocolWithPerson(existingPerson, dataSet.getSponsorLogin());

        helper.addGroupMemberships(testUser.getUsername(), UserRoleType.INVESTIGATOR.getGroupName());
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        EditPersonPage editPersonPage = selectPerson(existingPerson, rolePage);
        VerificationPage verificationPage = (VerificationPage) selectOrganization(organization1, editPersonPage);
        checkVerificationPage(verificationPage, existingPerson, organization1, INVESTIGATOR);
        verificationPage.clickSave();
    }

    private EditPersonPage selectPerson(Person person, ViewSelectedRolesPage rolePage) {
        PersonSelectionPage selectPersonPage = (PersonSelectionPage) rolePage.clickNext();
        return selectPerson(person, selectPersonPage);
    }

    private void createProtocolWithPerson(Person investigator, LoginAccount loginAccount) {
        Protocol protocol = ProtocolFactory.getInstance().create();

        HomePage homePage = openHomePage(loginAccount);
        CreateProtocolPage createProtocolPage = homePage.getProtocolsMenu().clickCreateNew();
        createProtocolPage.setProtocolTitle(protocol.getProtocolTitle());
        createProtocolPage.setProtocolNumber(protocol.getProtocolNumber());
        createProtocolPage.selectPhase(protocol.getPhase());
        createProtocolPage.getHelper().selectFirstSponsor();
        ProtocolInformationTab protocolInformationTab = createProtocolPage.clickSave();
        ProtocolRegistrationPage protocolPage = protocolInformationTab.getPage();
        ProtocolInvestigatorsTab investigatorsTab = protocolPage.clickInvestigatorsTab();
        AddInvestigatorsDialog addDialog = investigatorsTab.clickAddInvestigators();
        addDialog.getHelper().searchAndAddPerson(investigator);
        addDialog.clickDone();
        protocolPage.clickSignOut();

    }

    @Test
    public void testSponsorRepresentativeRegistration() throws GridInvocationException {
        String sponsorNesId = getGridResources().getValidSponsorNesId();
        String sponsorGroup = getSponsorGroupName(sponsorNesId);
        Organization sponsor = getGridResources().getNesTestDataSource().getProtocolSponsorOrganization(sponsorNesId);
        helper.addGroupMemberships(testUser.getUsername(), sponsorGroup);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        List<String> roleNames = rolePage.getRoleNames();
        assertEquals(1, roleNames.size());
        assertEquals("Sponsor for " + sponsor.getName(), roleNames.get(0));
        EditPersonPage editPersonPage = selectPerson(getExistingNesPerson(), rolePage);
        VerificationPage verificationPage = (VerificationPage) editPersonPage.clickNext();
        assertEquals(1, verificationPage.getSponsorOrganizationSection().getSponsorRepresentativeOrganizations().size());
        assertEquals(0, verificationPage.getSponsorOrganizationSection().getSponsorDelegateOrganizations().size());
        completeRegistration(verificationPage, SPONSOR);
    }

    private String getSponsorGroupName(String sponsorNesId) {
        return UserRoleType.SPONSOR.getGroupName() + "_" + getNesIdExtension(sponsorNesId);
    }

    @Test
    public void testSponsorDelegateRegistration() throws GridInvocationException {
        String sponsorNesId = getGridResources().getValidSponsorNesId();
        Organization sponsor = getGridResources().getNesTestDataSource().getProtocolSponsorOrganization(sponsorNesId);
        String delegateGroup = getSponsorDelegateGroupName(sponsorNesId);
        helper.addGroupMemberships(testUser.getUsername(), delegateGroup);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        List<String> roleNames = rolePage.getRoleNames();
        assertEquals(1, roleNames.size());
        assertEquals("Sponsor Delegate for " + sponsor.getName(), roleNames.get(0));
        EditPersonPage editPersonPage = selectPerson(getExistingNesPerson(), rolePage);
        VerificationPage verificationPage = (VerificationPage) editPersonPage.clickNext();
        assertEquals(0, verificationPage.getSponsorOrganizationSection().getSponsorRepresentativeOrganizations().size());
        assertEquals(1, verificationPage.getSponsorOrganizationSection().getSponsorDelegateOrganizations().size());
        completeRegistration(verificationPage, SPONSOR_DELEGATE);
    }

    private String getSponsorDelegateGroupName(String sponsorNesId) {
        return UserRoleType.SPONSOR_DELEGATE.getGroupName() + "_" + getNesIdExtension(sponsorNesId);
    }

    @Test
    public void testSponsorAndDelegateRegistration() throws GridInvocationException {
        String sponsor1NesId = getGridResources().getValidProtocolSponsorNesId();
        Organization sponsor1 = getGridResources().getNesTestDataSource().getProtocolSponsorOrganization(sponsor1NesId);
        String sponsor2NesId = getGridResources().getValidProtocolSponsorNesId();
        Organization sponsor2 = getGridResources().getNesTestDataSource().getProtocolSponsorOrganization(sponsor2NesId);
        String sponsorGroup = getSponsorGroupName(sponsor1NesId);
        String delegateGroup = getSponsorDelegateGroupName(sponsor2NesId);
        helper.addGroupMemberships(testUser.getUsername(), sponsorGroup, delegateGroup);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        List<String> roleNames = rolePage.getRoleNames();
        assertEquals(2, roleNames.size());
        assertEquals("Sponsor for " + sponsor1.getName(), roleNames.get(0));
        assertEquals("Sponsor Delegate for " + sponsor2.getName(), roleNames.get(1));
        EditPersonPage editPersonPage = selectPerson(getExistingNesPerson(), rolePage);
        VerificationPage verificationPage = (VerificationPage) editPersonPage.clickNext();
        assertEquals(1, verificationPage.getSponsorOrganizationSection().getSponsorRepresentativeOrganizations().size());
        assertEquals(1, verificationPage.getSponsorOrganizationSection().getSponsorDelegateOrganizations().size());
        completeRegistration(verificationPage, SPONSOR, SPONSOR_DELEGATE);
    }

    @Test
    public void testRegisterInvestigatorWithNewEntities() throws GridInvocationException {
        Person newPerson = PersonFactory.getInstance().create();
        newPerson.setCtepId(null);
        newPerson.setNesId(null);
        newPerson.setProviderNumber(ValueGenerator.getUniqueString());
        Organization newOrganization = OrganizationFactory.getInstance().create();
        newOrganization.setNesId(null);
        helper.addGroupMemberships(testUser.getUsername(), UserRoleType.INVESTIGATOR.getGroupName());
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
        createOrganizationPage.getHelper().enterOrganizationData(organization, PracticeSiteType.CANCER_CENTER);
        return (VerificationPage) createOrganizationPage.clickNext();
    }

    @Test
    public void testRegisterRegistrationCoordinator() throws GridInvocationException {
        investigatorMap = Maps.newHashMap();
        investigatorMap.put(fbciinv2, builder.createInvestigator().withLogin(fbciinv2).get());
        investigatorMap.put(fbciinv3, builder.createInvestigator().withLogin(fbciinv3).get());
        investigatorMap.put(fbciinv4, builder.createInvestigator().withLogin(fbciinv4).get());
        builder.build();
        Person coordinatorPerson = getExistingNesPerson();
        helper.addGroupMemberships(testUser.getUsername(), UserRoleType.REGISTRATION_COORDINATOR.getGroupName());
        VerificationPage verificationPage = fillInCoordinatorRegistration(coordinatorPerson);

        verificationPage = returnAndModifySelectedInvestigators(verificationPage);
        verifySelectedInvestigatorsDisplayed(verificationPage, fbciinv3);
        HomePage homePage = completeRegistration(verificationPage, REGISTRATION_COORDINATOR);
        checkCoordinatorAwaitingVerificationTask(homePage);
        checkCoordinatorEmail(coordinatorPerson);
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

    private void checkCoordinatorEmail(Person coordinatorPerson) {
        getEmailChecker().assertEmailCount(1);
        MimeMessage[] messages = getEmailChecker().getSentEmails();
        getEmailChecker().checkTo(messages[0], getSavedProfile(fbciinv3).getPerson().getEmail());
    }

    @Test
    public void testPersonAutoSelection() throws RemoteException, GridInvocationException {
        nesPersonService.createPerson(newAccountPerson);
        helper.addGroupMemberships(testUser.getUsername(), UserRoleType.INVESTIGATOR.getGroupName());
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        EditPersonPage editPersonPage = (EditPersonPage) rolePage.clickNext();
        editPersonPage.getHelper().verifyFormValues(newAccountPerson);
        editPersonPage.clickNext();
        assertTrue(OrganizationSelectionPage.FACTORY.isDisplayed(getDriver()));
    }

    @Test
    public void testPersonAutoSelection_AlreadySelected() throws RemoteException, GridInvocationException {
        String nesId = nesPersonService.createPerson(newAccountPerson);
        newAccountPerson.setNesId(nesId);
        helper.addGroupMemberships(testUser.getUsername(), UserRoleType.INVESTIGATOR.getGroupName());
        simulatePersonRecordAlreadySelected();
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        EditPersonPage editPersonPage = (EditPersonPage) rolePage.clickNext();
        editPersonPage.getHelper().verifyFormValues(newAccountPerson);
        editPersonPage = checkForUserAlreadyAssociatedMessage(editPersonPage);
        rolePage = (ViewSelectedRolesPage) editPersonPage.clickPrevious();
        editPersonPage = (EditPersonPage) rolePage.clickNext();
        editPersonPage.getHelper().verifyFormValues(newAccountPerson);
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

    private void simulatePersonRecordAlreadySelected() {
        FirebirdUser user = FirebirdUserFactory.getInstance().create();
        user.setPerson(newAccountPerson);
        getHibernateHelper().openAndBindSession();
        getHibernateHelper().getCurrentSession().beginTransaction();
        getHibernateHelper().getCurrentSession().save(user);
        getHibernateHelper().getCurrentSession().getTransaction().commit();
        getHibernateHelper().unbindAndCleanupSession();
    }

    @Test
    public void testPersonAutoSearchByEmailAddress() throws RemoteException, GridInvocationException {
        nesPersonService.createPerson(newAccountPerson);
        Person person2 = PersonFactory.getInstance().create();
        person2.setNesId(null);
        person2.setEmail(newAccountPerson.getEmail());
        nesPersonService.createPerson(person2);
        helper.addGroupMemberships(testUser.getUsername(), UserRoleType.INVESTIGATOR.getGroupName());
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        PersonSelectionPage personSelectionPage = (PersonSelectionPage) rolePage.clickNext();
        assertEquals(newAccountPerson.getEmail(), personSelectionPage.getSearchFieldValue());
        assertEquals(2, personSelectionPage.getSearchResults().size());
    }

    @Test
    public void testPersonAutoSearchByName() throws RemoteException, GridInvocationException {
        newAccountPerson.setEmail(getUniqueEmailAddress());
        nesPersonService.createPerson(newAccountPerson);
        Person person2 = PersonFactory.getInstance().create();
        person2.setNesId(null);
        person2.setFirstName(newAccountPerson.getFirstName());
        person2.setLastName(newAccountPerson.getLastName());
        nesPersonService.createPerson(person2);
        helper.addGroupMemberships(testUser.getUsername(), UserRoleType.INVESTIGATOR.getGroupName());
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        PersonSelectionPage personSelectionPage = (PersonSelectionPage) rolePage.clickNext();
        String expectedSearchString = newAccountPerson.getLastName() + ", " + newAccountPerson.getFirstName();
        assertEquals(expectedSearchString, personSelectionPage.getSearchFieldValue());
        assertEquals(2, personSelectionPage.getSearchResults().size());
    }

    @Test
    public void testNoFunctionalityWarningIfRolesAreVerified() throws GridInvocationException {
        String sponsorNesId = getGridResources().getValidSponsorNesId();
        Organization sponsor = getGridResources().getNesTestDataSource().getProtocolSponsorOrganization(sponsorNesId);
        String sponsorGroup = getSponsorGroupName(sponsorNesId);
        String verifiedSponsorGroup = "verified_" + sponsorGroup;
        helper.addGroupMemberships(testUser.getUsername(), sponsorGroup, verifiedSponsorGroup);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        List<String> roleNames = rolePage.getRoleNames();
        assertEquals(1, roleNames.size());
        assertEquals("Sponsor for " + sponsor.getName(), roleNames.get(0));
        EditPersonPage editPersonPage = selectPerson(getExistingNesPerson(), rolePage);
        VerificationPage verificationPage = (VerificationPage) editPersonPage.clickNext();
        assertEquals(1, verificationPage.getSponsorOrganizationSection().getSponsorRepresentativeOrganizations().size());
        assertEquals(0, verificationPage.getSponsorOrganizationSection().getSponsorDelegateOrganizations().size());
        assertTrue(verificationPage.clickSave() instanceof HomePage);
    }

    @Test
    public void testOnlyUnverifiedRolesShownInFunctionalityWarning() throws GridInvocationException {
        String sponsorGroup = getSponsorGroupName(getGridResources().getValidSponsorNesId());
        String verifiedSponsorGroup = "verified_" + sponsorGroup;
        String investigatorGroup = UserRoleType.INVESTIGATOR.getGroupName();
        helper.addGroupMemberships(testUser.getUsername(), investigatorGroup, sponsorGroup, verifiedSponsorGroup);
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        EditPersonPage editPersonPage = selectPerson(getExistingNesPerson(), rolePage);
        VerificationPage verificationPage = (VerificationPage) selectOrganization(organization1, editPersonPage);
        completeRegistration(verificationPage, UserRoleType.INVESTIGATOR);
    }

    @Test
    public void testInvestigatorLeaveFlow() throws GridInvocationException {
        helper.addGroupMemberships(testUser.getUsername(), UserRoleType.INVESTIGATOR.getGroupName());
        ViewSelectedRolesPage rolePage = openSelectedRolesPage();
        ((LoginPage) rolePage.clickCancelRegistration()).waitUntilReady();
    }

}