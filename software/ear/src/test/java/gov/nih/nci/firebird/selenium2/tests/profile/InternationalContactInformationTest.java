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
package gov.nih.nci.firebird.selenium2.tests.profile;

import static gov.nih.nci.firebird.common.FirebirdConstants.*;
import static gov.nih.nci.firebird.test.ValueGenerator.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.cagrid.GridInvocationException;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.components.AddressForm;
import gov.nih.nci.firebird.selenium2.pages.components.CreateOrganizationComponent;
import gov.nih.nci.firebird.selenium2.pages.components.PersonForm;
import gov.nih.nci.firebird.selenium2.pages.components.tags.ManagePersonTag;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.AddOrganizationAssociationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.CredentialsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.InvestigatorProfilePage;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageLicenseDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.OrganizationAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.SubInvestigatorAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ManagePersonDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ProfessionalContactInformationTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubInvestigatorAssociationFormDialog;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInformationTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.protocol.AddInvestigatorsDialog;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.CreatePrimaryOrganizationPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.EditPersonPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.OrganizationSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.PersonSelectionPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.VerificationPage;
import gov.nih.nci.firebird.selenium2.pages.user.regsitration.ViewSelectedRolesPage;
import gov.nih.nci.firebird.selenium2.tests.account.RegistrationTestHelper;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;
import gov.nih.nci.firebird.test.util.FirebirdPropertyUtils;

import java.rmi.RemoteException;
import java.util.Date;

import org.junit.Test;

import com.google.inject.Inject;

public class InternationalContactInformationTest extends AbstractFirebirdWebDriverTest {

    @Inject
    private RegistrationTestHelper helper;

    @Test
    public void testInvestigatorInternationalContactInformation() throws RemoteException,
            GridInvocationException {
        Person investigator = createInternationalPerson();
        Organization primaryOrganization = createInternationalOrganization();
        HomePage homePage = createInternationalInvestigatorAccount(investigator, primaryOrganization);
        ProfessionalContactInformationTab contactInformationTab = editPrimaryPerson(homePage, investigator);
        InvestigatorProfilePage profilePage = addInternationalPersonAssociation(contactInformationTab);
        profilePage = addInternationalOrganizationAssociation(profilePage);
        addInternationalMedicalLicense(profilePage);
    }

    private Person createInternationalPerson() {
        Person person = PersonFactory.getInstance().createWithoutExternalData();
        person.setPostalAddress(getUniqueInternationalAddress());
        person.setPhoneNumber(getInternationalPhoneNumber());
        return person;
    }

    private String getInternationalPhoneNumber() {
        return "+49-89-636-480" + getUniqueInt();
    }

    private Organization createInternationalOrganization() {
        Organization organization = OrganizationFactory.getInstance().createWithoutExternalData();
        organization.setPostalAddress(getUniqueInternationalAddress());
        organization.setPhoneNumber(getInternationalPhoneNumber());
        return organization;
    }

    private HomePage createInternationalInvestigatorAccount(Person investigator, Organization primaryOrganization)
            throws RemoteException, GridInvocationException {
        LoginAccount account = helper.createNewGridUser();
        helper.addGroupMemberships(account.getUsername(), UserRoleType.INVESTIGATOR.getGroupName());
        PersonSelectionPage selectPersonPage = selectInvestigatorRole(account);
        OrganizationSelectionPage selectOrganizationPage = createPersonForNewInvestigator(investigator,
                selectPersonPage);
        VerificationPage verificationPage = createOrganizationForNewInvestigator(primaryOrganization,
                selectOrganizationPage);
        checkInformationOnVerficationPage(verificationPage, investigator, primaryOrganization);
        verificationPage.clickSave();
        return openHomePage(account);
    }

    private PersonSelectionPage selectInvestigatorRole(LoginAccount account) {
        ViewSelectedRolesPage rolePage = (ViewSelectedRolesPage) openLoginPage().getHelper()
                .login(account, getProvider()).clickAccept();
        return (PersonSelectionPage) rolePage.clickNext();
    }

    private OrganizationSelectionPage createPersonForNewInvestigator(Person investigator,
            PersonSelectionPage selectPersonPage) {
        EditPersonPage editPersonPage = selectPersonPage.clickCreateNew();
        checkForExpectedRegistrationDefaultValues(editPersonPage.getPersonForm());
        checkForUsFieldLabels(editPersonPage.getPersonForm().getAddressForm());
        editPersonPage.getHelper().enterPersonData(investigator);
        checkForInternationalFieldLabels(editPersonPage.getPersonForm().getAddressForm());
        editPersonPage.getPersonForm().getHelper().verifyFormValues(investigator);
        OrganizationSelectionPage organizationSelectionPage = (OrganizationSelectionPage) editPersonPage.clickNext();
        editPersonPage = (EditPersonPage) organizationSelectionPage.clickPrevious();
        editPersonPage.getPersonForm().getHelper().verifyFormValues(investigator);
        checkForInternationalFieldLabels(editPersonPage.getPersonForm().getAddressForm());
        OrganizationSelectionPage selectOrganizationPage = (OrganizationSelectionPage) editPersonPage.clickNext();
        return selectOrganizationPage;
    }

    private void checkForExpectedRegistrationDefaultValues(PersonForm personForm) {
        assertNotNull(personForm.getFirstName());
        assertNotNull(personForm.getLastName());
        assertNull(personForm.getPrefix());
        assertNull(personForm.getSuffix());
        assertNull(personForm.getMiddleInitial());
        assertNotNull(personForm.getEmailAddress());
        assertNull(personForm.getPhoneNumber());
        checkForExpectedDefaultValues(personForm.getAddressForm());
    }

    public void checkForExpectedDefaultValues(AddressForm addressForm) {
        assertNull(addressForm.getAddress1());
        assertNull(addressForm.getAddress2());
        assertNull(addressForm.getCity());
        assertNull(addressForm.getUsState());
        assertNull(addressForm.getZip());
        assertEquals(US_COUNTRY_CODE, addressForm.getCountry());
    }

    private void checkForUsFieldLabels(AddressForm addressForm) {
        String expectedStateLabel = FirebirdPropertyUtils.getPropertyText("dropdown.state");
        assertTrue(addressForm.getStateOrProvinceLabel().contains(expectedStateLabel));

        String expectedZipLabel = FirebirdPropertyUtils.getPropertyText("textfield.zipCode");
        assertTrue(addressForm.getZipLabel().contains(expectedZipLabel));
    }

    private void checkForInternationalFieldLabels(AddressForm addressForm) {
        String expectedStateLabel = FirebirdPropertyUtils.getPropertyText("textfield.stateOrProvince");
        assertTrue(addressForm.getStateOrProvinceLabel().contains(expectedStateLabel));

        String expectedZipLabel = FirebirdPropertyUtils.getPropertyText("textfield.postalCode");
        assertTrue(addressForm.getZipLabel().contains(expectedZipLabel));
    }

    private VerificationPage createOrganizationForNewInvestigator(Organization primaryOrganization,
            OrganizationSelectionPage organizationSelectionPage) {
        CreatePrimaryOrganizationPage createOrganizationPage = organizationSelectionPage.clickCreateNew();
        checkForExpectedDefaultValues(createOrganizationPage);
        checkForUsFieldLabels(createOrganizationPage.getAddressForm());
        createOrganizationPage.getHelper().enterOrganizationData(primaryOrganization, PracticeSiteType.CANCER_CENTER);
        createOrganizationPage.getAddressForm().getHelper().verifyFormValues(primaryOrganization.getPostalAddress());
        checkForInternationalFieldLabels(createOrganizationPage);
        VerificationPage verificationPage = (VerificationPage) createOrganizationPage.clickNext();
        createOrganizationPage = (CreatePrimaryOrganizationPage) verificationPage.clickPrevious();
        checkForInternationalFieldLabels(createOrganizationPage);
        verificationPage = (VerificationPage) createOrganizationPage.clickNext();
        return verificationPage;
    }

    private void checkInformationOnVerficationPage(VerificationPage verificationPage, Person investigator,
            Organization primaryOrganization) {
        verificationPage.getHelper().verifyPersonInformationIsDisplayed(investigator);
        verificationPage.getHelper().verifyPrimaryOrganizationInformationIsDisplayed(primaryOrganization);
    }

    private ProfessionalContactInformationTab editPrimaryPerson(HomePage homePage, Person investigator) {
        ProfessionalContactInformationTab contactInformationPage = homePage.getInvestigatorMenu().click();
        ManagePersonDialog personDialog = contactInformationPage.clickEditContactInformation();
        personDialog.clickSave();
        assertFalse(contactInformationPage.hasPendingUpdateMessage());
        personDialog = contactInformationPage.clickEditContactInformation();
        checkForInternationalFieldLabels(personDialog.getPersonForm().getAddressForm());
        personDialog.getHelper().verifyFormValues(investigator);
        investigator.setPostalAddress(getUniqueInternationalAddress());
        personDialog.getHelper().enterPersonData(investigator);
        checkForInternationalFieldLabels(personDialog.getPersonForm().getAddressForm());
        personDialog.getHelper().verifyFormValues(investigator);
        personDialog.clickSave();
        contactInformationPage.getHelper().checkForPendingUpdateMessage();
        return contactInformationPage;
    }

    private InvestigatorProfilePage addInternationalPersonAssociation(
            ProfessionalContactInformationTab contactInformationTab) {
        SubInvestigatorAssociationsTab associationTab = contactInformationTab.getPage()
                .clickSubInvestigatorAssociationsTab();
        SubInvestigatorAssociationFormDialog associationDialog = associationTab.clickAddNew();
        ManagePersonTag managePersonTag = associationDialog.clickCreateNew();
        checkForExpectedDefaultValues(managePersonTag.getAddressForm());
        checkForUsFieldLabels(managePersonTag.getAddressForm());
        Person person = createInternationalPerson();
        managePersonTag.getHelper().enterPersonData(person);
        checkForInternationalFieldLabels(managePersonTag.getAddressForm());
        managePersonTag.getHelper().verifyFormValues(person);
        associationDialog.clickSave();
        associationTab.getHelper().assertListed(person);
        return associationTab.getPage();
    }

    private InvestigatorProfilePage addInternationalOrganizationAssociation(InvestigatorProfilePage profilePage) {
        OrganizationAssociationsTab organizationAssociationsTab = profilePage.clickOrganizationAssociationsTab();
        AddOrganizationAssociationDialog labDialog = organizationAssociationsTab.getClinicalLabSection()
                .clickCreateNew();
        Organization organization = createInternationalOrganization();
        ClinicalLaboratory lab = new ClinicalLaboratory();
        lab.setOrganization(organization);
        CreateOrganizationComponent createLabComponent = (CreateOrganizationComponent) labDialog.clickCreateNew();
        checkForUsFieldLabels(createLabComponent.getAddressForm());
        createLabComponent.getHelper().enterData(lab.getOrganization());
        checkForInternationalFieldLabels(createLabComponent.getAddressForm());
        labDialog.clickSave();
        organizationAssociationsTab.getClinicalLabSection().getHelper().assertListed(lab);
        return organizationAssociationsTab.getPage();
    }

    private void addInternationalMedicalLicense(InvestigatorProfilePage profilePage) {
        CredentialsTab credentialsTab = profilePage.clickCredentialsTab();
        ManageLicenseDialog licenseDialog = credentialsTab.getLicenseSection().clickAddLicense();
        assertEquals(FirebirdConstants.US_COUNTRY_CODE, licenseDialog.getCountry());
        licenseDialog.selectCountry("DEU");
        licenseDialog.typeLicenseIdNumber("1234");
        licenseDialog.selectLicenseType("Public Health");
        licenseDialog.getHelper().setExpirationDate(new Date());
        licenseDialog.clickSave();
        assertEquals("DEU", credentialsTab.getLicenseSection().getListings().get(0).getLocation());
    }

    @Test
    public void testInternationalInvestigatorCreationBySponsor() {
        DataSet dataSet = createSponsorDataSet();
        HomePage homePage = openHomePage(dataSet.getSponsorLogin());
        ProtocolInformationTab protocolInfoTab = homePage.getProtocolsMenu().clickBrowse().getHelper().clickLink(dataSet.getProtocol());
        ProtocolInvestigatorsTab investigatorsTab = protocolInfoTab.getPage().clickInvestigatorsTab();
        AddInvestigatorsDialog addInvestigatorsDialog = investigatorsTab.clickAddInvestigators();
        ManagePersonTag createPersonTag = addInvestigatorsDialog.clickCreateNew();
        checkForUsFieldLabels(createPersonTag.getAddressForm());
        Person person = createInternationalPerson();
        checkForExpectedDefaultValues(createPersonTag.getAddressForm());
        createPersonTag.getHelper().enterPersonData(person);
        checkForInternationalFieldLabels(createPersonTag.getAddressForm());
        addInvestigatorsDialog.clickSave();
        addInvestigatorsDialog.clickDone();
    }

    private DataSet createSponsorDataSet() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createSponsor();
        builder.createProtocol();
        return builder.build();
    }

}
