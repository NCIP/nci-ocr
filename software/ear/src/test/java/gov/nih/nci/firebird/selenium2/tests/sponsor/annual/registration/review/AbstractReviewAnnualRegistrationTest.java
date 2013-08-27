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
package gov.nih.nci.firebird.selenium2.tests.sponsor.annual.registration.review;

import static org.junit.Assert.*;

import gov.nih.nci.firebird.commons.selenium2.util.WaitUtils;
import gov.nih.nci.firebird.data.AdditionalAttachmentsForm;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.AnnualRegistrationForm1572;
import gov.nih.nci.firebird.data.CtepFinancialDisclosure;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.components.tags.RegistrationCommentsTag.CommentType;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.OverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.RegistrationReviewCommentDialog;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.ApproveRegistrationDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.ApproveRegistrationValidationDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.ReviewAnnualRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.ReviewAnnualRegistrationPage.RegistrationListing;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.ToggleReviewOnHoldCommentsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.AdditionalAttachmentsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.FinancialDisclosuresSupportingDocumentsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.FormReviewCommentDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.RegistrationClinicalLabCertificatesDialog;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.selenium2.pages.util.FirebirdEmailUtils;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.test.data.AnnualRegistrationTestDataSet;

import java.io.IOException;

import org.junit.Test;

public abstract class AbstractReviewAnnualRegistrationTest extends AbstractFirebirdWebDriverTest {

    protected AnnualRegistrationTestDataSet dataSet;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dataSet = AnnualRegistrationTestDataSet.createWithStatus(getDataLoader(), getGridResources(),
                RegistrationStatus.SUBMITTED);
    }

    @Test
    public void testReviewAnnualRegistration_Accept() throws IOException {
        HomePage homePage = getHomePage();
        ReviewAnnualRegistrationPage reviewRegistrationPage = homePage.getHelper().openSubmittedAnnualRegistrationTask(
                getRegistration());
        reviewRegistrationPage.getHelper().verifyRegistrationDisplayed(getRegistration());

        checkOnHoldToggleSwitchFunctionality(reviewRegistrationPage);
        reviewRegistrationPage.getHelper().downloadAllForms(getRegistration());
        checkDispositionTransitions(reviewRegistrationPage);
        checkAdditionalDocumentation(reviewRegistrationPage);
        acceptRegistration(reviewRegistrationPage);
        checkForNotificationEmails(FirebirdMessageTemplate.ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_INVESTIGATOR,
                FirebirdMessageTemplate.ANNUAL_REGISTRATION_ACCEPTED_EMAIL_TO_OTHERS);
    }

    @Test
    public void testReviewAnnualRegistration_ApproveFromDialog() throws IOException {
        HomePage homePage = getHomePage();
        ReviewAnnualRegistrationPage reviewRegistrationPage = homePage.getHelper().openSubmittedAnnualRegistrationTask(
                getRegistration());
        reviewRegistrationPage.getHelper().downloadAllForms(getRegistration());
        approveRegistrationFromDialog(reviewRegistrationPage);

        // Multiplier of 2 since approve and accept emails are separate
        int expectedEmailCount = 2 * (2 + getRegistration().getNotificationEmailAddresses().size());
        getEmailChecker().assertEmailCount(expectedEmailCount);

        checkForNotificationEmailContent(FirebirdMessageTemplate.ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_INVESTIGATOR,
                FirebirdMessageTemplate.ANNUAL_REGISTRATION_APPROVED_EMAIL_TO_COORDINATOR);
    }

    @Test
    public void testReviewAnnualRegistration_ApproveFromPage() throws IOException {
        HomePage homePage = getHomePage();
        ReviewAnnualRegistrationPage reviewRegistrationPage = homePage.getHelper().openSubmittedAnnualRegistrationTask(
                getRegistration());
        reviewRegistrationPage.getHelper().downloadAllForms(getRegistration());
        approveRegistrationFromPage(reviewRegistrationPage);
    }

    /**
     * @return Homepage post login
     */
    public abstract HomePage getHomePage();

    private AnnualRegistration getRegistration() {
        return dataSet.getRenewalRegistration();
    }

    private void checkOnHoldToggleSwitchFunctionality(ReviewAnnualRegistrationPage reviewRegistrationPage) {
        reviewRegistrationPage = checkToggleToOnHold(reviewRegistrationPage);
        checkToggleToInReview(reviewRegistrationPage);
    }

    private ReviewAnnualRegistrationPage checkToggleToOnHold(ReviewAnnualRegistrationPage reviewRegistrationPage) {
        assertFalse(reviewRegistrationPage.isToggleSetToReviewOnHold());
        ToggleReviewOnHoldCommentsDialog commentsDialog = reviewRegistrationPage.toggleReviewToOnHold();
        String onHoldComments = "I am placing this review on hold until I return from vacation";
        commentsDialog.typeComments(onHoldComments);
        commentsDialog.clickSave();
        assertTrue(reviewRegistrationPage.isToggleSetToReviewOnHold());
        assertEquals(RegistrationStatus.REVIEW_ON_HOLD.getDisplay(), reviewRegistrationPage.getRegistrationStatus());
        assertEquals(onHoldComments, reviewRegistrationPage.getComments(CommentType.ADDITIONAL));
        return checkForReviewOnHoldTaskListing(reviewRegistrationPage);
    }

    private ReviewAnnualRegistrationPage checkForReviewOnHoldTaskListing(
            ReviewAnnualRegistrationPage reviewRegistrationPage) {
        HomePage homePage = reviewRegistrationPage.clickHome();
        homePage.getHelper().checkForSponsorRegistrationOnHoldTask(getRegistration());
        reviewRegistrationPage = homePage.getHelper().openSubmittedAnnualRegistrationTask(getRegistration());
        assertTrue(reviewRegistrationPage.isToggleSetToReviewOnHold());
        return reviewRegistrationPage;
    }

    private void checkToggleToInReview(ReviewAnnualRegistrationPage reviewRegistrationPage) {
        ToggleReviewOnHoldCommentsDialog commentsDialog;
        commentsDialog = reviewRegistrationPage.toggleReviewToInReview();
        String inProgressComments = "I am back from vacation";
        commentsDialog.typeComments(inProgressComments);
        commentsDialog.clickSave();
        assertFalse(reviewRegistrationPage.isToggleSetToReviewOnHold());
        assertEquals(RegistrationStatus.IN_REVIEW.getDisplay(), reviewRegistrationPage.getRegistrationStatus());
        assertEquals(inProgressComments, reviewRegistrationPage.getComments(CommentType.ADDITIONAL));
    }

    private void checkDispositionTransitions(ReviewAnnualRegistrationPage reviewRegistrationPage) {
        RegistrationListing listing = getForm1572Listing(reviewRegistrationPage);
        assertEquals(FormStatus.IN_REVIEW.getDisplay(), listing.getStatus());

        listing = performSelectAcceptTest(reviewRegistrationPage, listing);
        listing = performUnselectAcceptTest(reviewRegistrationPage, listing);
        listing = performSelectRejectTest(reviewRegistrationPage, listing);
        listing = performSelectAcceptTest(reviewRegistrationPage, listing);
        listing = performCancelRejectionTest(reviewRegistrationPage, listing);
        listing = performSelectRejectTest(reviewRegistrationPage, listing);
        checkAcceptingThenRejectingRetainsRejectionComments(reviewRegistrationPage, listing);
        performUnselectRejectTest(reviewRegistrationPage, listing);
    }

    private RegistrationListing getForm1572Listing(ReviewAnnualRegistrationPage reviewRegistrationPage) {
        return reviewRegistrationPage.getHelper().getListingById(getRegistration().getForm1572());
    }

    private RegistrationListing performSelectAcceptTest(ReviewAnnualRegistrationPage reviewRegistrationPage,
            RegistrationListing listing) {
        listing.clickAcceptRadio();
        listing = getForm1572Listing(reviewRegistrationPage);
        assertEquals(FormStatus.ACCEPTED.getDisplay(), listing.getStatus());
        return listing;
    }

    private RegistrationListing performUnselectAcceptTest(ReviewAnnualRegistrationPage reviewRegistrationPage,
            RegistrationListing listing) {
        listing.clickAcceptRadio();
        listing = getForm1572Listing(reviewRegistrationPage);
        assertEquals(FormStatus.IN_REVIEW.getDisplay(), listing.getStatus());
        return listing;
    }

    private RegistrationListing performSelectRejectTest(ReviewAnnualRegistrationPage reviewRegistrationPage,
            RegistrationListing listing) {
        FormReviewCommentDialog commentDialog = listing.clickRejectRadio();
        checkThatCommentsAreRequired(commentDialog);
        commentDialog.typeComments("comments");
        commentDialog.clickSave();
        listing = getForm1572Listing(reviewRegistrationPage);
        assertEquals(FormStatus.REJECTED.getDisplay(), listing.getStatus());
        return listing;
    }

    private RegistrationListing performCancelRejectionTest(ReviewAnnualRegistrationPage reviewRegistrationPage,
            RegistrationListing listing) {
        FormReviewCommentDialog commentDialog = listing.clickRejectRadio();
        commentDialog.clickCancel();
        listing = getForm1572Listing(reviewRegistrationPage);
        assertEquals(FormStatus.ACCEPTED.getDisplay(), listing.getStatus());
        return listing;
    }

    private void checkAcceptingThenRejectingRetainsRejectionComments(
            ReviewAnnualRegistrationPage reviewRegistrationPage, RegistrationListing listing) {
        listing.clickAcceptRadio();
        listing = getForm1572Listing(reviewRegistrationPage);
        FormReviewCommentDialog commentsDialog = listing.clickRejectRadio();
        assertTrue(commentsDialog.getComments().contains("comments"));
        commentsDialog.clickSave();
    }

    private void performUnselectRejectTest(ReviewAnnualRegistrationPage reviewRegistrationPage,
            RegistrationListing listing) {
        listing = getForm1572Listing(reviewRegistrationPage);
        listing.clickRejectRadio();
        listing = getForm1572Listing(reviewRegistrationPage);
        assertEquals(FormStatus.IN_REVIEW.getDisplay(), listing.getStatus());
    }

    private void checkThatCommentsAreRequired(final FormReviewCommentDialog commentDialog) {
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure(
                "registration.review.form.rejection.comments.required");
        commentDialog.typeComments("");
        expectedFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                commentDialog.clickSave();
            }
        });
    }

    private void checkAdditionalDocumentation(ReviewAnnualRegistrationPage reviewRegistrationPage) throws IOException {
        check1572LabCertificates(reviewRegistrationPage);
        checkFinancialDisclosureSupportingDocumentation(reviewRegistrationPage);
        checkAdditionalAttachments(reviewRegistrationPage);
    }

    private void check1572LabCertificates(ReviewAnnualRegistrationPage reviewRegistrationPage) throws IOException {
        AnnualRegistrationForm1572 form1572 = getRegistration().getForm1572();
        RegistrationClinicalLabCertificatesDialog labCertificatesDialog = (RegistrationClinicalLabCertificatesDialog) reviewRegistrationPage
                .getHelper().getListingById(form1572).clickViewAttachments();
        labCertificatesDialog.getHelper().checkForClinicalLabCertificates(form1572);
        labCertificatesDialog.clickClose();
    }

    private void checkFinancialDisclosureSupportingDocumentation(ReviewAnnualRegistrationPage reviewRegistrationPage)
            throws IOException {
        CtepFinancialDisclosure financialDisclosure = getRegistration().getFinancialDisclosure();
        FinancialDisclosuresSupportingDocumentsDialog supportingDocumentsDialog = (FinancialDisclosuresSupportingDocumentsDialog) reviewRegistrationPage
                .getHelper().getListingById(financialDisclosure).clickViewAttachments();
        supportingDocumentsDialog.getHelper().checkForSupportingDocuments(financialDisclosure);
        supportingDocumentsDialog.clickClose();
    }

    private void checkAdditionalAttachments(ReviewAnnualRegistrationPage reviewRegistrationPage) throws IOException {
        AdditionalAttachmentsForm additionalAttachmentsForm = getRegistration().getAdditionalAttachmentsForm();
        AdditionalAttachmentsDialog additionalAttachmentsDialog = (AdditionalAttachmentsDialog) reviewRegistrationPage
                .getHelper().getListingById(additionalAttachmentsForm).clickViewAttachments();
        additionalAttachmentsDialog.getHelper().checkForAdditionalAttachments(additionalAttachmentsForm);
        additionalAttachmentsDialog.clickClose();
    }

    private void approveRegistrationFromDialog(ReviewAnnualRegistrationPage reviewRegistrationPage) {
        assertFalse(reviewRegistrationPage.hasCompleteReviewButton());
        reviewRegistrationPage.getHelper().acceptAllForms(getRegistration());
        assertTrue(reviewRegistrationPage.hasCompleteReviewButton());

        ApproveRegistrationDialog reviewCompletionDialog = (ApproveRegistrationDialog) reviewRegistrationPage
                .clickCompleteReview();
        checkApproveVerificationDialog(reviewCompletionDialog.clickApproveRegistration());
    }

    private void checkApproveVerificationDialog(final ApproveRegistrationValidationDialog validationDialog) {
        ReviewAnnualRegistrationPage reviewRegistrationPage = validationDialog.clickClose();
        WaitUtils.pause(400);
        assertEquals(RegistrationStatus.APPROVED.getDisplay(), reviewRegistrationPage.getRegistrationStatus());
    }

    private void approveRegistrationFromPage(ReviewAnnualRegistrationPage reviewRegistrationPage) {
        assertFalse(reviewRegistrationPage.hasCompleteReviewButton());
        reviewRegistrationPage.getHelper().acceptAllForms(getRegistration());
        assertTrue(reviewRegistrationPage.hasCompleteReviewButton());

        ApproveRegistrationDialog reviewCompletionDialog = (ApproveRegistrationDialog) reviewRegistrationPage
                .clickCompleteReview();
        reviewCompletionDialog.clickReturnToOverview();
        reviewRegistrationPage.waitUntilReady();
        checkApproveVerificationDialog((ApproveRegistrationValidationDialog) reviewRegistrationPage
                .clickApproveRegistration());
    }

    private void acceptRegistration(ReviewAnnualRegistrationPage reviewRegistrationPage) {
        assertFalse(reviewRegistrationPage.hasCompleteReviewButton());
        reviewRegistrationPage.getHelper().acceptAllForms(getRegistration());
        assertTrue(reviewRegistrationPage.hasCompleteReviewButton());

        ApproveRegistrationDialog reviewCompletionDialog = (ApproveRegistrationDialog) reviewRegistrationPage
                .clickCompleteReview();
        reviewCompletionDialog.clickReturnToOverview();
        assertEquals(RegistrationStatus.ACCEPTED.getDisplay(), reviewRegistrationPage.getRegistrationStatus());
        assertTrue(reviewRegistrationPage.hasApproveRegistrationButton());
    }

    private void checkForNotificationEmails(FirebirdMessageTemplate investigatorSubjectTemplate,
            FirebirdMessageTemplate othersSubjectedTemplate) {
        int expectedEmailCount = 2 + getRegistration().getNotificationEmailAddresses().size();
        getEmailChecker().assertEmailCount(expectedEmailCount);

        checkForNotificationEmailContent(investigatorSubjectTemplate, othersSubjectedTemplate);
    }

    private void checkForNotificationEmailContent(FirebirdMessageTemplate investigatorSubjectTemplate,
            FirebirdMessageTemplate othersSubjectedTemplate) {

        String investigatorEmail = dataSet.getInvestigatorUser().getPerson().getEmail();
        String investigatorsExpectedSubject = FirebirdEmailUtils.getExpectedSubject(investigatorSubjectTemplate);
        getEmailChecker().getSentEmail(investigatorEmail, investigatorsExpectedSubject);

        String coordinatorEmail = dataSet.getRegistrationCoordinatorUser().getPerson().getEmail();
        String othersExpectedSubject = FirebirdEmailUtils
                .getExpectedSubject(othersSubjectedTemplate, getRegistration());
        getEmailChecker().getSentEmail(coordinatorEmail, othersExpectedSubject);

        for (String emailAddress : getRegistration().getNotificationEmailAddresses()) {
            getEmailChecker().getSentEmail(emailAddress, othersExpectedSubject);
        }
    }

    @Test
    public void testReviewAnnualRegistration_Reject() throws IOException {
        HomePage homePage = getHomePage();
        ReviewAnnualRegistrationPage reviewRegistrationPage = homePage.getHelper().openSubmittedAnnualRegistrationTask(
                getRegistration());
        reviewRegistrationPage.getHelper().verifyRegistrationDisplayed(getRegistration());

        reviewRegistrationPage.getHelper().downloadAllForms(getRegistration());
        rejectRegistration(reviewRegistrationPage);
        checkForNotificationEmails(FirebirdMessageTemplate.ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_INVESTIGATOR,
                FirebirdMessageTemplate.ANNUAL_REGISTRATION_RETURNED_EMAIL_TO_OTHERS);
        checkForTaskListItemAsInvestigator();
    }

    private void rejectRegistration(ReviewAnnualRegistrationPage reviewRegistrationPage) {
        assertFalse(reviewRegistrationPage.hasCompleteReviewButton());
        reviewRegistrationPage.getHelper().rejectAllForms(getRegistration());
        assertTrue(reviewRegistrationPage.hasCompleteReviewButton());

        RegistrationReviewCommentDialog commentsDialog = (RegistrationReviewCommentDialog) reviewRegistrationPage
                .clickCompleteReview();
        String registrationRejectionComments = "registration rejection comments";
        commentsDialog.typeComments(registrationRejectionComments);
        commentsDialog.clickSave().clickConfirm().clickClose();
        reviewRegistrationPage.waitUntilReady();
        assertEquals(RegistrationStatus.RETURNED.getDisplay(), reviewRegistrationPage.getRegistrationStatus());
        reviewRegistrationPage.getHelper().checkFormRejectionComments(getRegistration());
        reviewRegistrationPage.getHelper().checkPageForRegistrationAndFormRejectionComments(getRegistration(),
                registrationRejectionComments);
    }

    private void checkForTaskListItemAsInvestigator() {
        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin(), getCtepProvider());
        homePage.getHelper().checkForAnnualRegistrationReturnedTask();
        OverviewTab overviewTab = homePage.getHelper().openReturnedAnnualRegistrationTask(getRegistration());
        assertEquals(RegistrationStatus.RETURNED.getDisplay(), overviewTab.getRegistrationStatus());
    }

}
