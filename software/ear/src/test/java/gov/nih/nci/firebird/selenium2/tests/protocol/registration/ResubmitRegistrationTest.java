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

import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.Certificate;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.base.ValidationMessageDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab.CertificateListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTabHelper;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ResubmissionCommentsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignAndSubmitRegistrationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignedDocumentsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.RegistrationReviewCommentDialog;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolsListPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.io.IOException;

import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

public class ResubmitRegistrationTest extends AbstractFirebirdWebDriverTest {

    @Inject
    private DataSetBuilder builder;
    private static final String COMMENTS = "comments";
    private DataSet dataSet;

    @Test
    public void testResubmitInvestigatorRegistration() {
        builder.createRegistration().withStatus(RegistrationStatus.RETURNED).withAllFormsRejected().complete();
        builder.createSponsor();
        dataSet = builder.build();

        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin());
        RegistrationOverviewTab overviewTab = RegistrationOverviewTabHelper.openRegistration(homePage,
                dataSet.getInvestigatorRegistration());
        signAndSubmitResubmission(overviewTab, dataSet.getInvestigatorLogin());
        assertTrue(overviewTab.getPage().clickForm1572Tab().isReadOnly());
        checkResubmissionPostConditions(dataSet.getInvestigatorRegistration(), overviewTab);
    }

    private void signAndSubmitResubmission(RegistrationOverviewTab overviewTab, LoginAccount login) {
        ResubmissionCommentsDialog commentsDialog = (ResubmissionCommentsDialog) overviewTab.clickSubmitRegistration();
        commentsDialog.typeComments(COMMENTS);
        SignAndSubmitRegistrationDialog signingDialog = (SignAndSubmitRegistrationDialog) commentsDialog.clickSave();
        SignedDocumentsDialog documentsDialog = signingDialog.getHelper().signAndSubmit(login);
        documentsDialog.clickClose();
    }

    private void checkResubmissionPostConditions(AbstractProtocolRegistration registration,
            RegistrationOverviewTab overviewTab) {
        registration = dataSet.reloadObject(registration);
        checkSharedFormsAreLocked(overviewTab);
        assertEquals(RegistrationStatus.SUBMITTED, registration.getStatus());
        checkFormStatuses(FormStatus.REJECTED, registration);
        HomePage homePage = overviewTab.getPage().clickHome();
        homePage.getHelper().assertTaskListIsEmpty();
        homePage = openHomePage(dataSet.getSponsorLogin());
        homePage.getHelper().checkForSponsorRegistrationSubmittedTask(registration);
    }

    private void checkSharedFormsAreLocked(RegistrationOverviewTab overviewTab) {
        assertTrue(overviewTab.getPage().clickFinancialDisclosureTab().isReadOnly());
        assertTrue(overviewTab.getPage().clickCvTab().isReadOnly());
        assertTrue(overviewTab.getPage().clickHumanResearchCertificateTab().isReadOnly());
    }

    private void checkFormStatuses(FormStatus status, AbstractProtocolRegistration registration) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (form.getFormType().getFormTypeEnum() != FormTypeEnum.ADDITIONAL_ATTACHMENTS) {
                assertEquals(status, form.getFormStatus());
            }
        }
    }

    @Test
    public void testResubmitSubinvestigatorRegistration() {
        InvestigatorRegistration primaryRegistration = builder.createRegistration().get();
        FirebirdUser subinvestigator = builder.createInvestigatorWithCompleteProfile().get();
        builder.createSubinvestigatorRegistration(subinvestigator, primaryRegistration)
                .withStatus(RegistrationStatus.RETURNED).withAllFormsRejected().complete();
        builder.createSponsor();
        dataSet = builder.build();

        HomePage homePage = openHomePage(dataSet.getInvestigatorLogins().get(1));
        RegistrationOverviewTab overviewTab = RegistrationOverviewTabHelper.openRegistration(homePage,
                dataSet.getSubInvestigatorRegistration());
        signAndSubmitResubmission(overviewTab, dataSet.getInvestigatorLogins().get(1));
        checkResubmissionPostConditions(dataSet.getSubInvestigatorRegistration(), overviewTab);
    }

    @Test
    public void testRejectAndResubmitRegistration() throws IOException {
        builder.createRegistration().complete();
        builder.createSponsor();
        dataSet = builder.build();

        submitRegistration();
        returnRegistration();
        resubmitRegistration();
        checkResubmittedFormStatuses();
    }

    private void submitRegistration() {
        LoginAccount login = dataSet.getInvestigatorLogin();
        HomePage homePage = openHomePage(login);
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        RegistrationOverviewTab overviewTab = RegistrationOverviewTabHelper.openRegistration(homePage, registration);
        SignAndSubmitRegistrationDialog signingDialog = (SignAndSubmitRegistrationDialog) overviewTab
                .clickSubmitRegistration();
        signingDialog.getHelper().signAndSubmit(login).clickClose();
        overviewTab.getPage().clickSignOut();
    }

    private void returnRegistration() throws IOException {
        HomePage homePage = openHomePage(dataSet.getSponsorLogin());
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        ReviewRegistrationTab reviewRegistrationTab = navigateToRegistrationReviewTab(homePage);
        reviewRegistrationTab.getHelper().reviewAllForms(registration);
        reviewRegistrationTab.getHelper().rejectForm(registration.getForm1572(), COMMENTS);
        reviewRegistrationTab.getHelper().rejectForm(registration.getCurriculumVitaeForm(), COMMENTS);
        reviewRegistrationTab.getHelper().acceptForm(registration.getFinancialDisclosure());
        reviewRegistrationTab.getHelper().acceptForm(registration.getHumanResearchCertificateForm());
        RegistrationReviewCommentDialog dialog = (RegistrationReviewCommentDialog) reviewRegistrationTab
                .clickCompleteReview();
        dialog.clickSave().clickConfirm();
    }

    private ReviewRegistrationTab navigateToRegistrationReviewTab(HomePage homePage) {
        ProtocolsListPage browseProtocolsPage = homePage.getProtocolsMenu().clickBrowse();
        ProtocolRegistrationPage registrationPage = browseProtocolsPage.getHelper().clickLink(dataSet.getProtocol())
                .getPage();
        return registrationPage.clickInvestigatorsTab().getHelper()
                .clickInvestigator(dataSet.getInvestigatorRegistration());
    }

    private void resubmitRegistration() {
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin());
        RegistrationOverviewTab overviewTab = RegistrationOverviewTabHelper.openRegistration(homePage,
                dataSet.getInvestigatorRegistration());
        checkReturnedFormStatuses(registration, overviewTab);
        reviseHumanResearchForm(registration, overviewTab);
        removeIrb(registration, overviewTab);
        checkSubmitIncompleteRegistration(overviewTab);
        addIrb(registration, overviewTab);
        checkRevisedFormStatuses(registration, overviewTab);
        resubmitRegistration(overviewTab);
    }

    private void checkReturnedFormStatuses(InvestigatorRegistration registration, RegistrationOverviewTab overviewTab) {
        checkFormStatus(overviewTab, registration.getForm1572(), FormStatus.REJECTED);
        checkFormStatus(overviewTab, registration.getCurriculumVitaeForm(), FormStatus.REJECTED);
        checkFormStatus(overviewTab, registration.getFinancialDisclosure(), FormStatus.ACCEPTED);
        checkFormStatus(overviewTab, registration.getHumanResearchCertificateForm(), FormStatus.ACCEPTED);
    }

    private void checkFormStatus(RegistrationOverviewTab overviewTab, AbstractRegistrationForm form,
            FormStatus expectedStatus) {
        assertEquals(expectedStatus.getDisplay(), overviewTab.getHelper().getFormListing(form).getStatus());
    }

    private void reviseHumanResearchForm(InvestigatorRegistration registration, RegistrationOverviewTab overviewTab) {
        HumanResearchCertificateTab certificateTab = overviewTab.getPage().clickHumanResearchCertificateTab();
        Certificate certificate = Iterables.getOnlyElement(registration.getHumanResearchCertificateForm()
                .getCertificates());
        CertificateListing listing = certificateTab.getHelper().getListing(certificate);
        listing.deselect();
        listing.select();
    }

    private void removeIrb(InvestigatorRegistration registration, RegistrationOverviewTab overviewTab) {
        Organization irb = Iterables.getFirst(registration.getForm1572().getIrbs(), null);
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        form1572Tab.getIrbSection().getHelper().getListing(irb).deselect();
        form1572Tab.getPage().clickOverviewTab();
    }

    private void checkSubmitIncompleteRegistration(RegistrationOverviewTab overviewTab) {
        ValidationMessageDialog validationDialog = (ValidationMessageDialog) overviewTab.clickSubmitRegistration();
        validationDialog.getHelper().verifyMessageDisplayed("validation.failure.missing.institutional.review.board");
        validationDialog.clickClose();
    }

    private void resubmitRegistration(RegistrationOverviewTab overviewTab) {
        ResubmissionCommentsDialog commentsDialog = (ResubmissionCommentsDialog) overviewTab.clickSubmitRegistration();
        SignAndSubmitRegistrationDialog signingDialog = (SignAndSubmitRegistrationDialog) commentsDialog.clickSave();
        signingDialog.getHelper().signAndSubmit(dataSet.getInvestigatorLogin());
    }

    private void addIrb(InvestigatorRegistration registration, RegistrationOverviewTab overviewTab) {
        Organization irb = Iterables.getFirst(registration.getForm1572().getIrbs(), null);
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        form1572Tab.getIrbSection().getHelper().getListing(irb).select();
        form1572Tab.getPage().clickOverviewTab();
    }

    private void checkRevisedFormStatuses(InvestigatorRegistration registration, RegistrationOverviewTab overviewTab) {
        checkFormStatus(overviewTab, registration.getForm1572(), FormStatus.REVISED);
        checkFormStatus(overviewTab, registration.getCurriculumVitaeForm(), FormStatus.REJECTED);
        checkFormStatus(overviewTab, registration.getFinancialDisclosure(), FormStatus.ACCEPTED);
        checkFormStatus(overviewTab, registration.getHumanResearchCertificateForm(), FormStatus.REVISED);
    }

    private void checkResubmittedFormStatuses() {
        HomePage homePage = openHomePage(dataSet.getSponsorLogin());
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        ReviewRegistrationTab reviewRegistrationTab = navigateToRegistrationReviewTab(homePage);

        assertFalse(isDispositionSelectionEnabled(reviewRegistrationTab, registration.getForm1572()));
        assertTrue(isDispositionSelectionEnabled(reviewRegistrationTab, registration.getCurriculumVitaeForm()));
        assertTrue(isDispositionSelectionEnabled(reviewRegistrationTab, registration.getFinancialDisclosure()));
        assertFalse(isDispositionSelectionEnabled(reviewRegistrationTab, registration.getHumanResearchCertificateForm()));

        checkFormStatus(reviewRegistrationTab, registration.getForm1572(), FormStatus.REVISED);
        checkFormStatus(reviewRegistrationTab, registration.getCurriculumVitaeForm(), FormStatus.REJECTED);
        checkFormStatus(reviewRegistrationTab, registration.getFinancialDisclosure(), FormStatus.ACCEPTED);
        checkFormStatus(reviewRegistrationTab, registration.getHumanResearchCertificateForm(), FormStatus.REVISED);
    }

    private void checkFormStatus(ReviewRegistrationTab tab, AbstractRegistrationForm form, FormStatus expectedStatus) {
        assertEquals(expectedStatus.getDisplay(), tab.getHelper().getMatchingListing(form).getStatus());
    }

    private boolean isDispositionSelectionEnabled(ReviewRegistrationTab tab, AbstractRegistrationForm form) {
        return tab.getHelper().getMatchingListing(form).isDispositionSelectionEnabled();
    }

}
