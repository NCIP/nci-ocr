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
package gov.nih.nci.firebird.selenium2.tests.coordinator;

import static gov.nih.nci.firebird.commons.test.TestFileUtils.*;
import static gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate.*;
import static gov.nih.nci.firebird.test.ValueGenerator.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.BoardCertifiedSpecialty;
import gov.nih.nci.firebird.data.Certification;
import gov.nih.nci.firebird.data.CertificationType;
import gov.nih.nci.firebird.data.CertifiedSpecialtyType;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.DegreeType;
import gov.nih.nci.firebird.data.InstitutionalReviewBoard;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.LaboratoryCertificateType;
import gov.nih.nci.firebird.data.MedicalLicense;
import gov.nih.nci.firebird.data.MedicalLicenseType;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.base.OneButtonDialog;
import gov.nih.nci.firebird.selenium2.pages.coordinator.investigators.BrowseInvestigatorsPage;
import gov.nih.nci.firebird.selenium2.pages.coordinator.investigators.BrowseInvestigatorsPage.InvestigatorListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.AddOrganizationAssociationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ClinicalExperienceDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.CredentialsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.EditTrainingCertificateDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.FilesTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.InvestigatorProfilePage;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.LabCertificatesDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageCertificationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageDegreeDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageLicenseDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageSpecialtyDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.OrganizationAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.SubInvestigatorAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ManagePersonDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.OrganizationSearchDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ProfessionalContactInformationTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ConfirmSubmissionToInvestigatorDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.CurriculumVitaeTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab.Question;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.InvestigatorRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.InviteSubinvestigatorsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProfileSubinvestigatorsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignAndSubmitRegistrationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignedDocumentsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubInvestigatorAssociationFormDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubinvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.service.messages.FirebirdStringTemplate;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;
import gov.nih.nci.firebird.test.util.FirebirdPropertyUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;

public class RegistrationCoordinatorWorkflowTest extends AbstractFirebirdWebDriverTest {

    @Inject
    private DataSetBuilder builder;
    private DataSet dataSet;
    private PracticeSite practiceSite;
    private ClinicalLaboratory laboratory;
    private InstitutionalReviewBoard irb;
    private LaboratoryCertificate labCertificate;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        FirebirdUser investigator = builder.createInvestigator().get();
        builder.createRegistration(investigator);
        builder.createCoordinator().withApprovedMangedInvestigator(investigator);
        dataSet = builder.build();

        setUpPracticeSite();
        setUpClinicalLaboratory();
        setUpLabCertificate();
        setUpIrb();
    }

    private void setUpPracticeSite() {
        practiceSite = new PracticeSite();
        Organization organization = OrganizationFactory.getInstance().createWithoutNesData();
        String ohrpNumber = getUniqueOhrpNumber();
        practiceSite.setDataField(ohrpNumber);
        practiceSite.setType(PracticeSiteType.CANCER_CENTER);
        practiceSite.setOrganization(organization);
    }

    private void setUpClinicalLaboratory() {
        laboratory = getNesTestDataSource().getClinicalLab();
    }

    private void setUpLabCertificate() {
        labCertificate = new LaboratoryCertificate(LaboratoryCertificateType.CAP);
        Date today = new Date();
        labCertificate.setEffectiveDate(DateUtils.addYears(today, -3));
        labCertificate.setExpirationDate(DateUtils.addYears(today, 3));
    }

    private void setUpIrb() {
        irb = new InstitutionalReviewBoard();
        Organization organization = OrganizationFactory.getInstance().createWithoutNesData();
        irb.setOrganization(organization);
    }

    @Test
    public void testRegistrationCompletionAndSigning() throws Exception {
        InvestigatorProfilePage profilePage = completeProfileAsCoordinator();
        completeRegistrationAsCoordinator(profilePage);
        completeAndSignRegistrationAsInvestigator();
    }

    private InvestigatorProfilePage completeProfileAsCoordinator() throws Exception {
        InvestigatorProfilePage profilePage = openProfileAsCoordinator();
        updateProfessionalContactInformation(profilePage);
        completeCredentials(profilePage);
        completeOrganizationAssociations(profilePage);
        completePersonAssociations(profilePage);
        uploadFiles(profilePage);
        return profilePage;
    }

    private InvestigatorProfilePage openProfileAsCoordinator() {
        HomePage homePage = openHomePage(dataSet.getCoordinatorLogin());
        BrowseInvestigatorsPage browseInvestigatorsPage = homePage.getInvestigatorsMenu().click();
        InvestigatorProfile profile = dataSet.getInvestigator().getInvestigatorRole().getProfile();
        InvestigatorListing listing = browseInvestigatorsPage.getHelper().getInvestigatorListing(profile);
        return listing.clickInvestigator().getPage();
    }

    private void updateProfessionalContactInformation(InvestigatorProfilePage profilePage) {
        ProfessionalContactInformationTab contactInformationTab = profilePage.clickProfessionalContactInformationTab();
        ManagePersonDialog editPersonDialog = contactInformationTab.clickEditContactInformation();
        Person person = PersonFactory.getInstance().create();
        editPersonDialog.getHelper().enterPersonData(person);
        editPersonDialog.clickSave();
        contactInformationTab.getHelper().checkForPendingUpdateMessage();
        OrganizationSearchDialog organizationSearchDialog = contactInformationTab.clickSearchOrganizationAgain();
        Organization organization = getExistingPrimaryOrganization();
        organizationSearchDialog.getHelper().search(organization);
        organizationSearchDialog.getHelper().select(organization);
        contactInformationTab.getHelper().verifyOrganization(organization);
    }

    private void completeCredentials(InvestigatorProfilePage profilePage) throws IOException {
        CredentialsTab credentialsTab = profilePage.clickCredentialsTab();
        addDegree(credentialsTab);
        addLicense(credentialsTab);
        addSpecialty(credentialsTab);
        addCertification(credentialsTab);
        addTrainingCertificate(credentialsTab);
        editBriefExperience(credentialsTab);
    }

    private void addDegree(CredentialsTab credentialsTab) {
        DegreeType degreeType = getMdEquivalentDegreeType();
        Degree degree = CredentialFactory.getInstance().createDegree(degreeType);
        degree.setIssuer(getExistingNesOrganization());
        ManageDegreeDialog dialog = credentialsTab.getDegreeSection().clickAddDegree();
        dialog.getHelper().enterDegreeData(degree);
        dialog.clickSave();
        assertNotNull(credentialsTab.getDegreeSection().getHelper().getListing(degree));
    }

    private DegreeType getMdEquivalentDegreeType() {
        List<DegreeType> degreeTypes = dataSet.getCredentialTypesData().getDegreeTypes();
        for (DegreeType degreeType : degreeTypes) {
            if (degreeType.isMdOrEquivalent()) {
                return degreeType;
            }
        }
        throw new IllegalStateException("No MD equivalent DegreeType found");
    }

    private void addLicense(CredentialsTab credentialsTab) {
        ManageLicenseDialog licenseFormDialog = credentialsTab.getLicenseSection().clickAddLicense();
        MedicalLicenseType licenseType = dataSet.getCredentialTypesData().getLicenseTypes().get(0);
        MedicalLicense license = CredentialFactory.getInstance().createLicense(licenseType);
        licenseFormDialog.getHelper().saveLicenseData(license);
        credentialsTab.getLicenseSection().getHelper().verifyLicenseListed(license);
    }

    private void addSpecialty(CredentialsTab credentialsTab) {
        ManageSpecialtyDialog specialtyDialog = credentialsTab.getSpecialtySection().clickAddSpecialty();
        CertifiedSpecialtyType specialtyType = dataSet.getCredentialTypesData().getSpecialtyTypes().get(0);
        BoardCertifiedSpecialty specialty = CredentialFactory.getInstance().createSpecialty(specialtyType);
        specialtyDialog.getHelper().saveSpecialtyData(specialty);
        credentialsTab.getSpecialtySection().getHelper().verifySpecialtyListed(specialty);
    }

    private void addCertification(CredentialsTab credentialsTab) {
        ManageCertificationDialog certificationDialog = credentialsTab.getCertificationSection()
                .clickAddCertification();
        CertificationType certificationType = dataSet.getCredentialTypesData().getCertificationTypes().get(0);
        Certification certification = CredentialFactory.getInstance().createCertification(certificationType);
        certificationDialog.getHelper().saveCertificationData(certification);
        credentialsTab.getCertificationSection().getHelper().verifyCertificationListed(certification);
    }

    private void addTrainingCertificate(CredentialsTab credentialsTab) throws IOException {
        EditTrainingCertificateDialog certificateDialog = credentialsTab.getTrainingSection().clickAddCertificate();
        File certificateFile = createTemporaryFile();
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate(certificateFile,
                getExistingNesOrganization());
        certificateDialog.getHelper().enterTrainingCertificateData(certificate, certificateFile, false);
        certificateDialog.clickSave();
        credentialsTab.getTrainingSection().getHelper().verifyCertificateListed(certificate);
    }

    private void editBriefExperience(CredentialsTab credentialsTab) {
        ClinicalExperienceDialog experienceDialog = credentialsTab.getExperienceSection().clickEditExperience();
        String experience = ValueGenerator.getUniqueString();
        experienceDialog.typeExperience(experience);
        experienceDialog.clickSave();
        credentialsTab.getExperienceSection().getHelper().verifyExperienceContent(experience);
    }

    private void completePersonAssociations(InvestigatorProfilePage profilePage) throws Exception {
        SubInvestigatorAssociationsTab associationsTab = profilePage.clickSubInvestigatorAssociationsTab();
        addAssociationToExistingPerson(associationsTab);
        addAssociationToNewPerson(associationsTab);
    }

    private void addAssociationToExistingPerson(SubInvestigatorAssociationsTab associationsTab) throws Exception {
        SubInvestigatorAssociationFormDialog associationDialog = associationsTab.clickAddNew();
        Person person = getExistingNesPerson();
        associationDialog.getHelper().searchAndSelectPerson(person);
        associationsTab.getHelper().assertListed(person);
    }

    private void addAssociationToNewPerson(SubInvestigatorAssociationsTab associationsTab) throws Exception {
        SubInvestigatorAssociationFormDialog associationDialog = associationsTab.clickAddNew();
        Person person = PersonFactory.getInstance().create();
        associationDialog.getHelper().enterNewPersonData(person);
        associationDialog.clickSave();
        associationsTab.getHelper().assertListed(person);
    }

    private void completeOrganizationAssociations(InvestigatorProfilePage profilePage) throws IOException {
        OrganizationAssociationsTab associationsTab = profilePage.clickOrganizationAssociationsTab();
        addNewPracticeSite(associationsTab);
        addExistingClinicalLab(associationsTab);
        addNewIrb(associationsTab);
    }

    private void addNewPracticeSite(OrganizationAssociationsTab associationsTab) {
        AddOrganizationAssociationDialog associationDialog = associationsTab.getPracticeSiteSection().clickCreateNew();
        associationDialog.getHelper().enterNewOrganizationData(practiceSite);
        associationDialog.clickSave();
        associationsTab.getPracticeSiteSection().getHelper().assertDisplayed(practiceSite);
    }

    private void addExistingClinicalLab(OrganizationAssociationsTab associationsTab) throws IOException {
        AddOrganizationAssociationDialog associationDialog = associationsTab.getClinicalLabSection().clickCreateNew();
        associationDialog.getHelper().searchAndSelectOrganization(laboratory);
        associationsTab.getClinicalLabSection().getHelper().assertListed(laboratory);
        addCertificateToClinicalLab(associationsTab);
    }

    private void addCertificateToClinicalLab(OrganizationAssociationsTab associationsTab) throws IOException {
        LabCertificatesDialog labCertificatesDialog = associationsTab.getClinicalLabSection().getHelper()
                .getListing(laboratory).clickCertificatesLink();
        labCertificatesDialog.getHelper().fillOutCertificate(labCertificate);
        labCertificatesDialog.setUploadFile(createTemporaryFile());
        labCertificatesDialog.clickSave();
        labCertificatesDialog.clickClose();
    }

    private void addNewIrb(OrganizationAssociationsTab associationsTab) {
        AddOrganizationAssociationDialog associationDialog = associationsTab.getIrbSection().clickCreateNew();
        associationDialog.getHelper().enterNewOrganizationData(irb);
        associationDialog.clickSave();
        associationsTab.getIrbSection().getHelper().assertDisplayed(irb);
    }

    private void uploadFiles(InvestigatorProfilePage profilePage) throws IOException {
        FilesTab filesTab = profilePage.clickFilesTab();
        String description = getUniqueString();
        File file = createTemporaryFile();
        filesTab.getHelper().uploadFile(file, description);
        filesTab.getHelper().assertListed(file, description);
    }

    private void completeRegistrationAsCoordinator(InvestigatorProfilePage profilePage) throws IOException {
        InvestigatorRegistrationPage registrationPage = openRegistration(profilePage);
        addAndInviteSubinvestigator(registrationPage);
        complete1572(registrationPage);
        completeFinancialDisclosure(registrationPage);
        completeHumanResearchCertificate(registrationPage);
        submitRegistration(registrationPage);
        registrationPage.clickSignOut();
    }

    private void submitRegistration(InvestigatorRegistrationPage registrationPage) {
        ConfirmSubmissionToInvestigatorDialog confirmDialog = (ConfirmSubmissionToInvestigatorDialog) registrationPage
                .clickOverviewTab().clickSubmitRegistration();
        OneButtonDialog successDialog = (OneButtonDialog) confirmDialog.clickConfirm();
        successDialog.clickClose();
        checkForEmailToInvestigator();
    }

    private void checkForEmailToInvestigator() {
        getEmailChecker().assertEmailCount(2);
        FirebirdStringTemplate subjectTemplate = COORDINATOR_COMPLETED_REGISTRATION_EMAIL.getSubjectTemplate();
        String expectedSubject = FirebirdPropertyUtils.evaluateAndReturnVelocityProperty(subjectTemplate, dataSet
                .getCoordinator().getPerson(), dataSet.getInvestigatorRegistration());
        MimeMessage email = getEmailChecker().getSentEmail(dataSet.getInvestigator().getPerson().getEmail(),
                expectedSubject);
        getEmailChecker().checkSubject(email, expectedSubject);
    }

    private InvestigatorRegistrationPage openRegistration(InvestigatorProfilePage profilePage) {
        BrowseInvestigatorsPage browseInvestigatorsPage = profilePage.getInvestigatorsMenu().click();
        InvestigatorProfile profile = dataSet.getInvestigator().getInvestigatorRole().getProfile();
        InvestigatorListing listing = browseInvestigatorsPage.getHelper().getInvestigatorListing(profile);
        BrowseRegistrationsPage registrationsPage = listing.clickRegistrations();
        return registrationsPage.getHelper().clickRegistrationLink(dataSet.getInvestigatorRegistration()).getPage();
    }

    private void addAndInviteSubinvestigator(InvestigatorRegistrationPage registrationPage) {
        SubinvestigatorsTab subinvestigatorsTab = registrationPage.clickSubinvestigatorsTab();
        ProfileSubinvestigatorsDialog subinvestigatorsDialog = subinvestigatorsTab.clickAddFromProfile();
        subinvestigatorsDialog.getSubinvestigatorListings().get(0).select();
        subinvestigatorsDialog.clickSave();
        InviteSubinvestigatorsDialog invitationDialog = subinvestigatorsTab.clickInvite();
        invitationDialog.clickSendInvitations();
    }

    private void complete1572(InvestigatorRegistrationPage registrationPage) {
        ProtocolForm1572Tab form1572Tab = registrationPage.clickForm1572Tab();
        form1572Tab.getPracticeSiteSection().getListings().get(0).select();
        form1572Tab.getClinicalLabSection().getListings().get(0).select();
        form1572Tab.getIrbSection().getListings().get(0).select();
    }

    private void completeFinancialDisclosure(InvestigatorRegistrationPage registrationPage) {
        FinancialDisclosureTab financialDisclosureTab = registrationPage.clickFinancialDisclosureTab();
        financialDisclosureTab.answerQuestion(Question.Q5_NO_FINANCIAL_INTEREST, true);
    }

    private void completeHumanResearchCertificate(InvestigatorRegistrationPage registrationPage) {
        HumanResearchCertificateTab hrcTab = registrationPage.clickHumanResearchCertificateTab();
        hrcTab.getCertificates().get(0).select();
    }

    private void completeAndSignRegistrationAsInvestigator() throws IOException {
        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin());
        homePage.getHelper().checkForRegistrationReadyForSigningTask(dataSet.getInvestigatorRegistration());
        BrowseRegistrationsPage registrationsPage = homePage.getInvestigatorMenu().clickProtocolRegistrations();
        InvestigatorRegistrationPage registrationPage = registrationsPage.getHelper()
                .clickRegistrationLink(dataSet.getInvestigatorRegistration()).getPage();

        check1572(registrationPage);
        checkFinancialDisclosure(registrationPage);
        checkCv(registrationPage);
        checkHumanResearchCertificate(registrationPage);
        sign(registrationPage);
    }

    private void check1572(InvestigatorRegistrationPage registrationPage) {
        ProtocolForm1572Tab form1572Tab = registrationPage.clickForm1572Tab();
        form1572Tab.getHelper().checkFormDownloads();
        assertEquals(1, form1572Tab.getPracticeSiteSection().getListings().size());
        assertEquals(1, form1572Tab.getClinicalLabSection().getListings().size());
        assertEquals(1, form1572Tab.getIrbSection().getListings().size());
    }

    private void checkFinancialDisclosure(InvestigatorRegistrationPage registrationPage) {
        FinancialDisclosureTab financialDisclosureTab = registrationPage.clickFinancialDisclosureTab();
        financialDisclosureTab.getHelper().checkFormDownloads();
        assertTrue(financialDisclosureTab.getAnswer(Question.Q5_NO_FINANCIAL_INTEREST));
    }

    private void checkCv(InvestigatorRegistrationPage registrationPage) {
        CurriculumVitaeTab cvTab = registrationPage.clickCvTab();
        cvTab.getHelper().checkFormDownloads();
    }

    private void checkHumanResearchCertificate(InvestigatorRegistrationPage registrationPage) throws IOException {
        HumanResearchCertificateTab humanResearchCertificateTab = registrationPage.clickHumanResearchCertificateTab();
        humanResearchCertificateTab.getHelper().checkFormDownloads();
    }

    private void sign(InvestigatorRegistrationPage registrationPage) {
        RegistrationOverviewTab overviewTab = registrationPage.clickOverviewTab();
        SignAndSubmitRegistrationDialog signingDialog = (SignAndSubmitRegistrationDialog) overviewTab
                .clickSubmitRegistration();
        SignedDocumentsDialog signedDocumentsDialog = signingDialog.getHelper().signAndSubmit(
                dataSet.getInvestigatorLogin());
        signedDocumentsDialog.clickClose();
    }

}
