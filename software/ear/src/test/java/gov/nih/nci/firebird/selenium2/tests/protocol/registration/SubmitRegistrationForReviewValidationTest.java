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
package gov.nih.nci.firebird.selenium2.tests.protocol.registration;

import static gov.nih.nci.firebird.data.FormStatus.*;
import static gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab.Question.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.util.WaitUtils;
import gov.nih.nci.firebird.commons.test.TestFileUtils;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.HumanResearchCertificateForm;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.LaboratoryCertificateType;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.base.OneButtonDialog;
import gov.nih.nci.firebird.selenium2.pages.base.ValidationMessageDialog;
import gov.nih.nci.firebird.selenium2.pages.coordinator.investigators.BrowseInvestigatorsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.CredentialsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.EditTrainingCertificateDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.OrganizationAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.TrainingCertificateSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ProfessionalContactInformationTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.AbstractFormTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ConfirmSubmissionToInvestigatorDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab.Question;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.Form1572OrganizationAssociationListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab.CertificateListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTabHelper;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ResubmissionCommentsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignAndSubmitRegistrationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignedDocumentsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubinvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubinvestigatorsTab.SubinvestigatorListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.ConfirmRegistrationRejectionDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.RegistrationReviewCommentDialog;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.EditFormsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInformationTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolsListPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.RegistrationFormsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewHumanResearchCertificatesDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

public class SubmitRegistrationForReviewValidationTest extends AbstractFirebirdWebDriverTest {

    private static final String MISSING_HUMAN_RESEARCH_CERT = "validation.failure.missing.document";
    private static final String MISSING_REQUIRED_DOCUMENTS = "validation.failure.missing.required.documents";
    private static final String QUESTIONS_INCOMPLETE = "validation.failure.questions.incomplete";
    private static final String MISSING_INSTITUTIONAL_REVIEW_BOARD = "validation.failure.missing.institutional.review.board";
    private static final String MISSING_PRACTICE_SITE = "validation.failure.missing.practice.site";
    private static final String MISSING_CLINICAL_LABORATORY = "validation.failure.missing.clinical.laboratory";
    private static final String SUBINVESTIGATOR_NOT_INVITED_MESSAGE = "validation.failures.subinvestigator.not.invited";

    @Inject
    private DataSetBuilder builder;

    @Test
    public void testSignRegistration() throws IOException {
        InvestigatorRegistration registration = 
                builder.createRegistration().complete().withStatus(RegistrationStatus.IN_PROGRESS).get();
        builder.createSponsor();
        DataSet dataSet = builder.build();

        RegistrationOverviewTab overviewTab = openHomePage(dataSet.getInvestigatorLogin()).getHelper()
                .openInProgressTask(registration);
        overviewTab.getHelper().assertHasStatus(RegistrationStatus.IN_PROGRESS);
        overviewTab.getHelper().assertFormsHaveStatus(FormStatus.COMPLETED);

        signRegistration(dataSet, overviewTab);
        checkForSubmittedDataSnapshots(dataSet);
        overviewTab.getHelper().assertHasStatus(RegistrationStatus.SUBMITTED);
        overviewTab.getHelper().validateFormsLocked(FormStatus.SUBMITTED, registration);
        verifyCertificateChange(registration, dataSet);
    }

    private void signRegistration(DataSet dataSet, RegistrationOverviewTab overviewTab)
            throws IOException {
        SignAndSubmitRegistrationDialog signDialog = (SignAndSubmitRegistrationDialog) overviewTab
                .clickSubmitRegistration();
        signDialog.getHelper().verifyDocumentsAgainstRegistration(dataSet.getInvestigatorRegistration());
        checkInvalidAuthorization(signDialog);
        setUppercaseUsernameCredentials(signDialog, dataSet.getInvestigatorLogin());
        SignedDocumentsDialog documentsDialog = signDialog.clickSign();
        dataSet.reload();
        documentsDialog.getHelper().checkForSignedDocuments(dataSet.getInvestigatorRegistration());
        documentsDialog.clickClose();
        WaitUtils.pause(500);
    }

    private void checkInvalidAuthorization(final SignAndSubmitRegistrationDialog signDialog) {
        signDialog.typeUsername("bad username");
        signDialog.typePassword("bad password");
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "authentication.invalid.credentials");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                signDialog.clickSign();
            }
        });
    }

    private void setUppercaseUsernameCredentials(SignAndSubmitRegistrationDialog signDialog, LoginAccount loginAccount) {
        signDialog.typeUsername(loginAccount.getUsername().toUpperCase());
        signDialog.typePassword(loginAccount.getPassword());
    }

    private void checkForSubmittedDataSnapshots(DataSet dataSet) {
        dataSet.reload();
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        assertNotNull(registration.getForm1572().getSubmittedData());
        assertNotNull(registration.getCurriculumVitaeForm().getSubmittedCredentialData());
    }

    private void verifyCertificateChange(InvestigatorRegistration registration,
            DataSet dataSet) throws IOException {
        String newContent = "this is some new content";
        String submittedFileString = getSubmittedCertificateContent(registration, dataSet.getSponsorLogin());
        ProfessionalContactInformationTab profileTab = addNewCertificate(registration, newContent,
                dataSet.getInvestigatorLogin());
        checkSubmittedFormTabForNoContentChange(profileTab, registration, submittedFileString, newContent);
        checkSubmittedFormForFileChanges(registration, submittedFileString, newContent, dataSet.getSponsorLogin());
    }

    private String getSubmittedCertificateContent(InvestigatorRegistration registration, LoginAccount sponsorLogin)
            throws IOException {
        ReviewRegistrationTab reviewPacketPage = openHomePage(sponsorLogin).getHelper()
                .openSubmittedProtocolRegistrationTask(registration);
        HumanResearchCertificateForm form = registration.getHumanResearchCertificateForm();
        ReviewHumanResearchCertificatesDialog hrcDialog = (ReviewHumanResearchCertificatesDialog) reviewPacketPage
                .getHelper().getMatchingListing(form).clickDownload();
        File submittedFile = hrcDialog.getListings().get(0).clickDownloadLink();
        return FileUtils.readFileToString(submittedFile);
    }

    private ProfessionalContactInformationTab addNewCertificate(InvestigatorRegistration registration,
            String fileContent, LoginAccount investigatorLogin) throws IOException {
        ProfessionalContactInformationTab profileTab = openHomePage(investigatorLogin).getInvestigatorMenu()
                .clickProfile();
        CredentialsTab credentialsTab = profileTab.getPage().clickCredentialsTab();
        TrainingCertificateSection certificateSection = credentialsTab.getTrainingSection();
        assertEquals(1, certificateSection.getListings().size());
        certificateSection.getListings().get(0).clickDeleteLink().clickDelete();
        assertEquals(0, certificateSection.getListings().size());
        File newFile = File.createTempFile("testCertificateChange", ".tmp");
        FileUtils.write(newFile, fileContent);
        Date effectiveDate = new Date();
        TrainingCertificate certificate = new TrainingCertificate(null, effectiveDate, DateUtils.addMonths(
                effectiveDate, 1), CertificateType.HUMAN_RESEARCH_CERTIFICATE, FirebirdFileFactory.getInstance()
                .create(newFile));
        certificate.setIssuer(getExistingExternalOrganization());
        EditTrainingCertificateDialog certificateDialog = certificateSection.clickAddCertificate();
        certificateDialog.getHelper().enterTrainingCertificateData(certificate, newFile, false);
        certificateDialog.clickSave();
        return profileTab;
    }

    private void checkSubmittedFormTabForNoContentChange(ProfessionalContactInformationTab profileTab,
            InvestigatorRegistration registration, String originalContent, String newContent) throws IOException {
        HumanResearchCertificateTab hrcTab = profileTab.getPage().getInvestigatorMenu().clickProtocolRegistrations()
                .getHelper().clickRegistrationLink(registration).getPage().clickHumanResearchCertificateTab();
        CertificateListing listing = hrcTab.getCertificates().get(0);
        File invSubmittedFile = listing.clickLink();
        String invSubmittedFileString = FileUtils.readFileToString(invSubmittedFile);
        assertEquals(originalContent, invSubmittedFileString);
        assertFalse(newContent.equals(invSubmittedFileString));
    }

    private void checkSubmittedFormForFileChanges(InvestigatorRegistration registration, String originalContent,
            String newContent, LoginAccount sponsorLogin) throws IOException {
        ProtocolsListPage protocolsPage = openHomePage(sponsorLogin).getProtocolsMenu().clickBrowse();
        ReviewRegistrationTab reviewTab = protocolsPage.getHelper().clickLink(registration.getProtocol()).getPage()
                .clickInvestigatorsTab().getHelper().getListing(registration).clickInvestigatorLink();
        HumanResearchCertificateForm form = registration.getHumanResearchCertificateForm();
        ReviewHumanResearchCertificatesDialog hrcDialog = (ReviewHumanResearchCertificatesDialog) reviewTab.getHelper()
                .getMatchingListing(form).clickDownload();
        File submittedFile2 = hrcDialog.getListings().get(0).clickDownloadLink();
        String submittedFile2String = FileUtils.readFileToString(submittedFile2);
        assertEquals(originalContent, submittedFile2String);
        assertFalse(newContent.equals(submittedFile2String));
    }

    @Test
    public void testClinicalLabCertificatesRequired() throws IOException {
        InvestigatorRegistration registration = 
                builder.createRegistration().complete().withStatus(RegistrationStatus.IN_PROGRESS).get();
        DataSet dataSet = builder.build();
        LoginAccount login = dataSet.getInvestigatorLogin();
        RegistrationOverviewTab overviewTab = openRegistration(registration, login);
        checkSubmitWhenNoLabCertificates(overviewTab, dataSet);
        checkSubmitWhenFormLabHasCertProfileLabDoesNot(overviewTab, dataSet);
        checkSubmitWhenFormLabDoesNotHaveCertProfileLabDoes(overviewTab, dataSet);
        checkSubmitWhenBothFormLabsHaveCert(overviewTab, dataSet);
    }

    private RegistrationOverviewTab openRegistration(InvestigatorRegistration registration, LoginAccount login) {
        BrowseRegistrationsPage browseRegistrationsPage = openHomePage(login).getInvestigatorMenu()
                .clickProtocolRegistrations();
        return browseRegistrationsPage.getHelper().clickRegistrationLink(registration);
    }

    private void checkSubmitWhenNoLabCertificates(final RegistrationOverviewTab overviewTab, DataSet dataSet) {
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        removeCertificateFromLab(dataSet, getRegistrationsClinicalLab(registration));
        ValidationMessageDialog messageDialog = (ValidationMessageDialog) overviewTab.clickSubmitRegistration();
        messageDialog.getHelper().verifyMessagesDisplayed("validation.failure.missing.clinical.laboratory.certificate");
        messageDialog.clickClose();
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        checkForLabValidationAsterisks(form1572Tab.getClinicalLabSection().getHelper().getSelectedListings());
    }

    private ClinicalLaboratory getRegistrationsClinicalLab(InvestigatorRegistration registration) {
        return (ClinicalLaboratory) registration.getForm1572().getLabs().iterator().next()
                .getRole(OrganizationRoleType.CLINICAL_LABORATORY);
    }

    private void removeCertificateFromLab(DataSet dataSet, ClinicalLaboratory formLab) {
        formLab.removeCertificate(LaboratoryCertificateType.CAP);
        dataSet.update(formLab);
    }

    private void checkForLabValidationAsterisks(Collection<Form1572OrganizationAssociationListing> selectedLabs) {
        for (Form1572OrganizationAssociationListing listing : selectedLabs) {
            assertTrue(listing.hasValidationError());
        }
    }

    private void checkSubmitWhenFormLabHasCertProfileLabDoesNot(RegistrationOverviewTab overviewTab,
            DataSet dataSet) {
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        ClinicalLaboratory formLab = getRegistrationsClinicalLab(registration);
        addCertificateToLab(dataSet, formLab);
        refreshoverviewTab(overviewTab);
        SignAndSubmitRegistrationDialog signDialog = (SignAndSubmitRegistrationDialog) overviewTab
                .clickSubmitRegistration();
        signDialog.closeDialog();
    }

    private void refreshoverviewTab(RegistrationOverviewTab overviewTab) {
        overviewTab.getPage().reload();
        overviewTab.waitUntilReady();
    }

    private void addCertificateToLab(DataSet dataSet, ClinicalLaboratory profileLab) {
        profileLab.addCertificate(new LaboratoryCertificate(LaboratoryCertificateType.CAP));
        dataSet.update(profileLab);
    }

    private void checkSubmitWhenFormLabDoesNotHaveCertProfileLabDoes(RegistrationOverviewTab overviewTab,
            DataSet dataSet) {
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        ClinicalLaboratory formLab = getRegistrationsClinicalLab(registration);
        removeCertificateFromLab(dataSet, formLab);
        Set<OrganizationAssociation> profileLabs = getProfilesClinicalLabs(dataSet);
        for (OrganizationAssociation organizationAssociation : profileLabs) {
            ClinicalLaboratory profileLab = (ClinicalLaboratory) organizationAssociation.getOrganizationRole();
            if (!profileLab.equals(formLab)) {
                addCertificateToLab(dataSet, profileLab);
            }
        }
        refreshoverviewTab(overviewTab);
        ValidationMessageDialog messageDialog = (ValidationMessageDialog) overviewTab.clickSubmitRegistration();
        messageDialog.getHelper().verifyMessagesDisplayed("validation.failure.missing.clinical.laboratory.certificate");
        messageDialog.clickClose();
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        checkForLabValidationAsterisks(form1572Tab.getClinicalLabSection().getHelper().getSelectedListings());
    }

    private Set<OrganizationAssociation> getProfilesClinicalLabs(DataSet dataSet) {
        return dataSet.getInvestigator().getInvestigatorRole().getProfile()
                .getOrganizationAssociations(OrganizationRoleType.CLINICAL_LABORATORY);
    }

    private void checkSubmitWhenBothFormLabsHaveCert(RegistrationOverviewTab overviewTab, DataSet dataSet) {
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        ClinicalLaboratory formLab = getRegistrationsClinicalLab(registration);
        Set<OrganizationAssociation> profileLabs = getProfilesClinicalLabs(dataSet);
        for (OrganizationAssociation organizationAssociation : profileLabs) {
            ClinicalLaboratory profileLab = (ClinicalLaboratory) organizationAssociation.getOrganizationRole();
            if (!profileLab.equals(formLab)) {
                registration.getForm1572().getLabs().add(profileLab.getOrganization());
            }
            addCertificateToLab(dataSet, profileLab);
        }
        refreshoverviewTab(overviewTab);
        SignAndSubmitRegistrationDialog signDialog = (SignAndSubmitRegistrationDialog) overviewTab
                .clickSubmitRegistration();
        signDialog.closeDialog();
    }

    @Test
    public void testIncompleteRegistration() throws IOException {
        InvestigatorRegistration primaryRegistration = builder.createRegistration().get();
        FirebirdUser subinvestigator = builder.createInvestigator().get();
        builder.createSubinvestigatorRegistration(subinvestigator, primaryRegistration)
                .withInvitationStatus(InvitationStatus.NOT_INVITED).withStatus(RegistrationStatus.NOT_STARTED);
        DataSet dataSet = builder.build();

        HomePage home = openHomePage(dataSet.getInvestigatorLogin());
        RegistrationOverviewTab overviewTab = home.getHelper()
                .openInProgressTask(dataSet.getInvestigatorRegistration());
        overviewTab.getHelper().assertHasStatus(RegistrationStatus.IN_PROGRESS);
        checkInitialFormStatuses(overviewTab, dataSet.getInvestigatorRegistration());
        ValidationMessageDialog messageDialog = (ValidationMessageDialog) overviewTab.clickSubmitRegistration();
        assertEquals(6, messageDialog.getMessages().size());
        messageDialog.getHelper().verifyMessagesDisplayed(MISSING_CLINICAL_LABORATORY, QUESTIONS_INCOMPLETE,
                MISSING_PRACTICE_SITE, MISSING_INSTITUTIONAL_REVIEW_BOARD);
        messageDialog.getHelper().verifyMessageDisplayed(MISSING_HUMAN_RESEARCH_CERT,
                dataSet.getInvestigatorRegistration().getHumanResearchCertificateForm().getFormType().getDescription());
        messageDialog.getHelper().verifyMessageDisplayed(SUBINVESTIGATOR_NOT_INVITED_MESSAGE,
                dataSet.getSubInvestigatorRegistration().getProfile().getPerson().getDisplayName());
        messageDialog.clickClose();
        WaitUtils.pause(500);
        overviewTab.getHelper().assertHasStatus(RegistrationStatus.INCOMPLETE);
        checkFormStatusesAfterIncompleSubmit(overviewTab, dataSet.getInvestigatorRegistration());
        checkFormTabMessages(overviewTab, dataSet.getInvestigatorRegistration());
    }

    private void checkInitialFormStatuses(RegistrationOverviewTab overviewTab, AbstractProtocolRegistration registration) {
        RegistrationOverviewTabHelper helper = overviewTab.getHelper();
        assertEquals(COMPLETED.getDisplay(), helper.getFormListing(registration.getCurriculumVitaeForm()).getFormStatus());
        assertEquals(NOT_STARTED.getDisplay(), helper.getFormListing(registration.getFinancialDisclosure()).getFormStatus());
        assertEquals(NOT_STARTED.getDisplay(), helper.getFormListing(registration.getHumanResearchCertificateForm())
                .getFormStatus());
        assertEquals(NOT_STARTED.getDisplay(), helper.getFormListing(registration.getForm1572()).getFormStatus());
        assertEquals(NOT_APPLICABLE.getDisplay(), helper.getFormListing(registration.getAdditionalAttachmentsForm())
                .getFormStatus());
    }

    private void checkFormStatusesAfterIncompleSubmit(RegistrationOverviewTab overviewTab,
            AbstractProtocolRegistration registration) {
        RegistrationOverviewTabHelper helper = overviewTab.getHelper();
        assertEquals(COMPLETED.getDisplay(), helper.getFormListing(registration.getCurriculumVitaeForm()).getFormStatus());
        assertEquals(INCOMPLETE.getDisplay(), helper.getFormListing(registration.getFinancialDisclosure()).getFormStatus());
        assertEquals(INCOMPLETE.getDisplay(), helper.getFormListing(registration.getHumanResearchCertificateForm())
                .getFormStatus());
        assertEquals(INCOMPLETE.getDisplay(), helper.getFormListing(registration.getForm1572()).getFormStatus());
        assertEquals(NOT_APPLICABLE.getDisplay(), helper.getFormListing(registration.getAdditionalAttachmentsForm())
                .getFormStatus());
    }

    private void checkFormTabMessages(RegistrationOverviewTab overviewTab, InvestigatorRegistration registration)
            throws IOException {
        checkForm1572TabMessages(overviewTab);
        checkFinancialDisclosureTabMessages(overviewTab);
        checkHumanResearchTabMessages(overviewTab, registration);
        checkSubInvestigatorTabMessages(overviewTab, registration);
    }

    private void checkForm1572TabMessages(RegistrationOverviewTab overviewTab) {
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        checkMessagesDisplayed(form1572Tab, null, MISSING_CLINICAL_LABORATORY, MISSING_PRACTICE_SITE,
                MISSING_INSTITUTIONAL_REVIEW_BOARD);
        checkFor1572TabAsterisks(form1572Tab, true);

        form1572Tab.getIrbSection().getListings().get(0).select();
        form1572Tab.getClinicalLabSection().getListings().get(0).select();
        form1572Tab.getPracticeSiteSection().getListings().get(0).select();
        checkMessagesNotDisplayed(form1572Tab, null, MISSING_CLINICAL_LABORATORY, MISSING_PRACTICE_SITE,
                MISSING_INSTITUTIONAL_REVIEW_BOARD);
        checkFor1572TabAsterisks(form1572Tab, false);

        form1572Tab.getIrbSection().getListings().get(0).deselect();
        form1572Tab.getClinicalLabSection().getListings().get(0).deselect();
        form1572Tab.getPracticeSiteSection().getListings().get(0).deselect();
        checkMessagesDisplayed(form1572Tab, null, MISSING_CLINICAL_LABORATORY, MISSING_PRACTICE_SITE,
                MISSING_INSTITUTIONAL_REVIEW_BOARD);
        checkFor1572TabAsterisks(form1572Tab, true);
    }

    private void checkMessagesDisplayed(AbstractFormTab<?> formTab, String replacement, String... expectedMessageKeys) {
        for (String messageKey : expectedMessageKeys) {
            assertTrue(formTab.getHelper().isMessageDisplayed(messageKey, replacement));
        }
    }

    private void checkFor1572TabAsterisks(ProtocolForm1572Tab form1572Tab, boolean visible) {
        assertEquals(visible, form1572Tab.isLabAsteriskVisible());
        assertEquals(visible, form1572Tab.isPracticeSiteAsteriskVisible());
        assertEquals(visible, form1572Tab.isIrbAsteriskVisible());
    }

    private void checkMessagesNotDisplayed(AbstractFormTab<?> formTab, String replacement,
            String... expectedMessageKeys) {
        for (String messageKey : expectedMessageKeys) {
            assertFalse(formTab.getHelper().isMessageDisplayed(messageKey, replacement));
        }
    }

    private void checkFinancialDisclosureTabMessages(RegistrationOverviewTab overviewTab) throws IOException {
        FinancialDisclosureTab financialDisclosureTab = overviewTab.getPage().clickFinancialDisclosureTab();
        checkMessagesDisplayed(financialDisclosureTab, null, QUESTIONS_INCOMPLETE); // Check all 5
        checkForAsterisks(financialDisclosureTab, false, Q1_MONETARY_GAIN, Q2_OTHER_SPONSOR_PAYMENTS,
                Q3_FINANCIAL_INTEREST, Q4_EQUITY_IN_SPONSOR);

        financialDisclosureTab.answerQuestion(Question.Q1_MONETARY_GAIN, true);
        checkMessagesDisplayed(financialDisclosureTab, null, QUESTIONS_INCOMPLETE, MISSING_REQUIRED_DOCUMENTS);
        checkForAsterisks(financialDisclosureTab, true, Q2_OTHER_SPONSOR_PAYMENTS, Q3_FINANCIAL_INTEREST,
                Q4_EQUITY_IN_SPONSOR);

        File file = TestFileUtils.createTemporaryFile();
        financialDisclosureTab.getSupportingDocumentsTag().getHelper().uploadFile(file);
        checkMessagesDisplayed(financialDisclosureTab, null, QUESTIONS_INCOMPLETE);
        checkMessagesNotDisplayed(financialDisclosureTab, null, MISSING_REQUIRED_DOCUMENTS);
        checkForAsterisks(financialDisclosureTab, false, Q2_OTHER_SPONSOR_PAYMENTS, Q3_FINANCIAL_INTEREST,
                Q4_EQUITY_IN_SPONSOR);

        financialDisclosureTab.getSupportingDocumentsTag().getHelper().getListing(file).clickDelete();
        checkMessagesDisplayed(financialDisclosureTab, null, QUESTIONS_INCOMPLETE, MISSING_REQUIRED_DOCUMENTS);
        checkForAsterisks(financialDisclosureTab, true, Q2_OTHER_SPONSOR_PAYMENTS, Q3_FINANCIAL_INTEREST,
                Q4_EQUITY_IN_SPONSOR);

        financialDisclosureTab.answerQuestion(Question.Q5_NO_FINANCIAL_INTEREST, true);
        checkMessagesNotDisplayed(financialDisclosureTab, null, QUESTIONS_INCOMPLETE, MISSING_REQUIRED_DOCUMENTS);
        checkForAsterisks(financialDisclosureTab, false);

        financialDisclosureTab.answerQuestion(Question.Q5_NO_FINANCIAL_INTEREST, false);
        checkMessagesDisplayed(financialDisclosureTab, null, QUESTIONS_INCOMPLETE);
        checkMessagesNotDisplayed(financialDisclosureTab, null, MISSING_REQUIRED_DOCUMENTS);
        checkForAsterisks(financialDisclosureTab, false, Q5_NO_FINANCIAL_INTEREST);
    }

    private void checkForAsterisks(FinancialDisclosureTab financialDisclosureTab, boolean requiredDocumentsAsterisk,
            Question... expectedQuestionAsterisks) {
        for (Question question : Question.values()) {
            boolean visible = ArrayUtils.contains(expectedQuestionAsterisks, question);
            assertEquals(visible, financialDisclosureTab.isAsteriskDisplayed(question));
        }
        assertEquals(requiredDocumentsAsterisk, financialDisclosureTab.isRequiredDocumentationAsteriskDisplayed());
    }

    private void checkHumanResearchTabMessages(RegistrationOverviewTab overviewTab,
            InvestigatorRegistration registration) {
        HumanResearchCertificateTab humanResearchTab = overviewTab.getPage().clickHumanResearchCertificateTab();
        checkMessagesDisplayed(humanResearchTab, registration.getHumanResearchCertificateForm().getFormType()
                .getDescription(), MISSING_HUMAN_RESEARCH_CERT);
        assertFalse(humanResearchTab.isNoCertificatesAsteriskDisplayed());

        humanResearchTab.getCertificates().get(0).select();
        checkMessagesNotDisplayed(humanResearchTab, registration.getHumanResearchCertificateForm().getFormType()
                .getDescription(), MISSING_HUMAN_RESEARCH_CERT);
        assertFalse(humanResearchTab.isNoCertificatesAsteriskDisplayed());

        humanResearchTab.getCertificates().get(0).deselect();
        checkMessagesDisplayed(humanResearchTab, registration.getHumanResearchCertificateForm().getFormType()
                .getDescription(), MISSING_HUMAN_RESEARCH_CERT);
        assertFalse(humanResearchTab.isNoCertificatesAsteriskDisplayed());

        humanResearchTab.getCertificates().get(0).clickDelete().clickDelete();
        assertTrue(humanResearchTab.isNoCertificatesAsteriskDisplayed());
    }

    private void checkSubInvestigatorTabMessages(RegistrationOverviewTab overviewTab,
            InvestigatorRegistration registration) {
        SubinvestigatorsTab subinvestigatorTab = overviewTab.getPage().clickSubinvestigatorsTab();
        String subinvestigatorName = Iterables.getOnlyElement(registration.getSubinvestigatorRegistrations())
                .getProfile().getPerson().getDisplayName();
        checkMessagesDisplayed(subinvestigatorTab, subinvestigatorName, SUBINVESTIGATOR_NOT_INVITED_MESSAGE);
        SubinvestigatorListing listing = Iterables.getOnlyElement(subinvestigatorTab.getSubinvestigatorListings());
        assertTrue(listing.hasValidationError());

        subinvestigatorTab.clickInvite().clickSendInvitations();
        listing = Iterables.getOnlyElement(subinvestigatorTab.getSubinvestigatorListings());
        checkMessagesNotDisplayed(subinvestigatorTab, subinvestigatorName, SUBINVESTIGATOR_NOT_INVITED_MESSAGE);
        assertFalse(listing.hasValidationError());
    }

    @Test
    public void testRemovedFormsNotValidated() {
        InvestigatorRegistration registration = 
                builder.createRegistration().complete().withStatus(RegistrationStatus.COMPLETED).get();
        builder.createSponsor();
        DataSet dataSet = builder.build();
        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin());
        RegistrationOverviewTab overviewTab = homePage.getInvestigatorMenu().clickProtocolRegistrations().getHelper()
                .clickRegistrationLink(registration);
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        Organization irb = registration.getForm1572().getIrbs().iterator().next();
        form1572Tab.getIrbSection().getHelper().getListing(irb).deselect();
        assertFalse(form1572Tab.getIrbSection().getHelper().getListing(irb).isSelected());
        overviewTab = form1572Tab.getPage().clickOverviewTab();
        assertSubmitIsInvalid(overviewTab);

        homePage = openHomePage(dataSet.getSponsorLogin());
        ProtocolInformationTab protocolTab = homePage.getProtocolsMenu().clickBrowse().getHelper()
                .clickLink(dataSet.getProtocol());
        RegistrationFormsTab formsTab = protocolTab.getPage().clickFormsTab();

        EditFormsDialog editFormsDialog = formsTab.clickEdit();
        editFormsDialog.getHelper().getListing(registration.getForm1572())
                .selectInvestigatorOptionality(FormOptionality.NONE);
        editFormsDialog.typeComments("change comments");
        editFormsDialog.clickSave();

        homePage = openHomePage(dataSet.getInvestigatorLogin());
        overviewTab = homePage.getInvestigatorMenu().clickProtocolRegistrations().getHelper()
                .clickRegistrationLink(registration);
        assertSubmitIsValid(overviewTab);
    }

    private void assertSubmitIsInvalid(RegistrationOverviewTab overviewTab) {
        ValidationMessageDialog messagesDialog = (ValidationMessageDialog) overviewTab.clickSubmitRegistration();
        messagesDialog.clickClose();
    }

    private void assertSubmitIsValid(RegistrationOverviewTab overviewTab) {
        SignAndSubmitRegistrationDialog signDialog = (SignAndSubmitRegistrationDialog) overviewTab
                .clickSubmitRegistration();
        signDialog.closeDialog();
    }

    @Test
    public void testResubmitRegistrationAsCoordinatorThenInvestigator() throws IOException {
        String coordinatorComments = "These are the comments of the coordinator.";
        String investigatorComments = "These are the comments of the investigator";

        FirebirdUser investigator = builder.createInvestigatorWithCompleteProfile().get();
        builder.createRegistration(investigator).complete();
        builder.createCoordinator().withApprovedMangedInvestigator(investigator);
        builder.createSponsor();
        DataSet dataSet = builder.build();

        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin());
        RegistrationOverviewTab overviewTab = goToAndSubmitRegistration(homePage, dataSet);
        overviewTab.getPage().clickSignOut();

        homePage = openHomePage(dataSet.getSponsorLogin());
        ReviewRegistrationTab reviewTab = homePage.getHelper().openSubmittedProtocolRegistrationTask(
                dataSet.getInvestigatorRegistration());
        reviewAndRejectForms(reviewTab, dataSet.getInvestigatorRegistration());
        completeAndReturnRegistration(reviewTab, dataSet.getInvestigatorRegistration());
        reviewTab.getPage().clickSignOut();

        homePage = openHomePage(dataSet.getCoordinatorLogin());
        overviewTab = navigateToCoordinatorRegistration(homePage, dataSet.getInvestigatorRegistration());
        ConfirmSubmissionToInvestigatorDialog submitToInvestigatorDialog = (ConfirmSubmissionToInvestigatorDialog) overviewTab
                .clickSubmitRegistration();
        ResubmissionCommentsDialog resubmitCommentsDialog = (ResubmissionCommentsDialog) submitToInvestigatorDialog
                .clickConfirm();
        resubmitCommentsDialog.typeComments(coordinatorComments);
        ((OneButtonDialog) resubmitCommentsDialog.clickSave()).clickClose();
        overviewTab.getHelper().checkCommentsForText(coordinatorComments);

        homePage = openHomePage(dataSet.getInvestigatorLogin());
        overviewTab = homePage.getHelper().openInProgressTask(dataSet.getInvestigatorRegistration());
        resubmitCommentsDialog = (ResubmissionCommentsDialog) overviewTab.clickSubmitRegistration();
        resubmitCommentsDialog.typeComments("\\n" + investigatorComments);
        SignAndSubmitRegistrationDialog signingDialog = (SignAndSubmitRegistrationDialog) resubmitCommentsDialog
                .clickSave();
        signAndSubmitRegistration(signingDialog, dataSet.getInvestigatorLogin());
        overviewTab.getHelper().checkCommentsForText(investigatorComments);

        homePage = openHomePage(dataSet.getSponsorLogin());
        reviewTab = homePage.getHelper().openSubmittedProtocolRegistrationTask(dataSet.getInvestigatorRegistration());
        reviewTab.getHelper().checkCommentsForText(investigatorComments);
    }

    private RegistrationOverviewTab goToAndSubmitRegistration(HomePage homePage, DataSet dataSet) {
        RegistrationOverviewTab overviewTab = homePage.getInvestigatorMenu().clickProtocolRegistrations().getHelper()
                .clickRegistrationLink(dataSet.getInvestigatorRegistration());
        SignAndSubmitRegistrationDialog signDialog = (SignAndSubmitRegistrationDialog) overviewTab
                .clickSubmitRegistration();
        signAndSubmitRegistration(signDialog, dataSet.getInvestigatorLogin());
        overviewTab.getHelper().assertHasStatus(RegistrationStatus.SUBMITTED);
        return overviewTab;
    }

    private void signAndSubmitRegistration(SignAndSubmitRegistrationDialog signDialog, LoginAccount login) {
        SignedDocumentsDialog documentsDialog = signDialog.getHelper().signAndSubmit(login);
        documentsDialog.clickClose();
    }

    private void reviewAndRejectForms(ReviewRegistrationTab reviewTab, AbstractProtocolRegistration reg)
            throws IOException {
        reviewTab.getHelper().reviewAllForms(reg);
        reviewTab.getHelper().rejectAllForms(reg);
    }

    private void completeAndReturnRegistration(ReviewRegistrationTab reviewTab,
            AbstractProtocolRegistration registration) {
        String reviewComments = "These are comments indicating the utter rejection of the submitted registration";
        assertTrue(reviewTab.isCompleteReviewButtonPresent());
        RegistrationReviewCommentDialog commentDialog = (RegistrationReviewCommentDialog) reviewTab
                .clickCompleteReview();

        commentDialog.typeComments(reviewComments);

        ConfirmRegistrationRejectionDialog confirmRejectionDialog = commentDialog.clickSave();
        confirmRejectionDialog.clickConfirm().clickClose();

        reviewTab.getHelper().checkForRegistrationStatus(RegistrationStatus.RETURNED);
        assertFalse(reviewTab.isCompleteReviewButtonPresent());
        reviewTab.getHelper().checkCommentsForText(reviewComments);
    }

    private RegistrationOverviewTab navigateToCoordinatorRegistration(HomePage homePage,
            AbstractProtocolRegistration registration) {
        BrowseInvestigatorsPage investigatorsPage = homePage.getInvestigatorsMenu().click();
        BrowseRegistrationsPage browseRegistrationsPage = investigatorsPage.getHelper()
                .getInvestigatorListing(registration.getProfile()).clickRegistrations();
        return browseRegistrationsPage.getHelper().clickRegistrationLink(registration);
    }

    @Test
    public void testSelectedAssociationsRemovedFromProfile() {
        InvestigatorRegistration registration = 
                builder.createRegistration().complete().withStatus(RegistrationStatus.IN_PROGRESS).get();
        DataSet dataSet = builder.build();
        Organization practiceSite = registration.getForm1572().getPracticeSites().iterator().next();
        Organization clinicalLab = registration.getForm1572().getLabs().iterator().next();
        Organization irb = registration.getForm1572().getIrbs().iterator().next();
        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin());
        RegistrationOverviewTab overviewTab = homePage.getHelper().openInProgressTask(registration);

        selectAssociations(practiceSite, clinicalLab, irb, overviewTab);
        deleteAssociationsFromProfile(practiceSite, clinicalLab, irb, overviewTab, registration);
        checkAssociationsRemovedFromRegistration(practiceSite, clinicalLab, irb, overviewTab);
        checkForWarningsOnSubmit(overviewTab);
    }

    private void selectAssociations(Organization practiceSite, Organization clinicalLab, Organization irb,
            RegistrationOverviewTab overviewTab) {
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        form1572Tab.getPracticeSiteSection().getHelper().getListing(practiceSite).select();
        form1572Tab.getClinicalLabSection().getHelper().getListing(clinicalLab).select();
        form1572Tab.getIrbSection().getHelper().getListing(irb).select();
        assertTrue(form1572Tab.getPracticeSiteSection().getHelper().getListing(practiceSite).isSelected());
        assertTrue(form1572Tab.getClinicalLabSection().getHelper().getListing(clinicalLab).isSelected());
        assertTrue(form1572Tab.getIrbSection().getHelper().getListing(irb).isSelected());
        form1572Tab.getPage().clickOverviewTab();
    }

    private void deleteAssociationsFromProfile(Organization practiceSite, Organization clinicalLab, Organization irb,
            RegistrationOverviewTab overviewTab, AbstractProtocolRegistration registration) {
        OrganizationAssociationsTab orgAssociationsTab = overviewTab.getPage().getInvestigatorMenu().clickProfile()
                .getPage().clickOrganizationAssociationsTab();
        orgAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite).clickRemoveLink()
                .clickConfirmButton();
        orgAssociationsTab.getClinicalLabSection().getHelper().getListing(clinicalLab).clickRemoveLink()
                .clickConfirmButton();
        orgAssociationsTab.getIrbSection().getHelper().getListing(irb).clickRemoveLink().clickConfirmButton();
        orgAssociationsTab.getPage().clickHome().getHelper().openInProgressTask(registration);
    }

    private void checkAssociationsRemovedFromRegistration(Organization practiceSite, Organization clinicalLab,
            Organization irb, RegistrationOverviewTab overviewTab) {
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        assertNull(form1572Tab.getPracticeSiteSection().getHelper().getListing(practiceSite));
        assertNull(form1572Tab.getClinicalLabSection().getHelper().getListing(clinicalLab));
        assertNull(form1572Tab.getIrbSection().getHelper().getListing(irb));
        form1572Tab.getPage().clickOverviewTab();
    }

    private void checkForWarningsOnSubmit(RegistrationOverviewTab overviewTab) {
        ValidationMessageDialog messagesDialog = (ValidationMessageDialog) overviewTab.clickSubmitRegistration();
        messagesDialog.getHelper().verifyMessagesDisplayed(MISSING_CLINICAL_LABORATORY, MISSING_PRACTICE_SITE,
                MISSING_INSTITUTIONAL_REVIEW_BOARD);
    }

}
