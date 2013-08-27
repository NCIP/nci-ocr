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
package gov.nih.nci.firebird.selenium2.tests.sponsor.protocol.registration;

import static gov.nih.nci.firebird.data.RegistrationStatus.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.util.WaitUtils;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AdditionalAttachmentsForm;
import gov.nih.nci.firebird.data.Certificate;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.HumanResearchCertificateForm;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.ProtocolFinancialDisclosure;
import gov.nih.nci.firebird.data.ProtocolForm1572;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ResubmissionCommentsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignAndSubmitRegistrationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.ConfirmRegistrationRejectionDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.RegistrationReviewCommentDialog;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInformationTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolSubInvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolsListPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewCompletionDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewHumanResearchCertificatesDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab.RegistrationFormListing;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.FinancialDisclosuresSupportingDocumentsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.FormReviewCommentDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.RegistrationClinicalLabCertificatesDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.ReviewAdditionalAttachmentsDialog;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.LoginAccount.InvestigatorLogin;
import gov.nih.nci.firebird.test.LoginAccount.SponsorDelegateLogin;
import gov.nih.nci.firebird.test.LoginAccount.SponsorLogin;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import org.junit.Test;

public abstract class AbstractReviewRegistrationTest extends AbstractFirebirdWebDriverTest {
    private static final Set<FormTypeEnum> FORMS_WITH_ADDITIONAL_DOCUMENTS = EnumSet.of(FormTypeEnum.FORM_1572,
            FormTypeEnum.FINANCIAL_DISCLOSURE_FORM);
    static final LoginAccount SPONSOR_LOGIN = SponsorLogin.fbcisponsor1;
    static final LoginAccount SPONSOR_DELEGATE_LOGIN = SponsorDelegateLogin.fbcidel1;

    private DataSet dataSet;
    private InvestigatorRegistration primaryRegistration;
    private SubInvestigatorRegistration uninvitedSubinvestigatorRegistration;
    private SubInvestigatorRegistration invitedSubinvestigatorRegistration;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        InvestigatorProfile investigatorProfile = builder.createProfile().complete().get();
        FirebirdUser investigator = builder.createInvestigator(investigatorProfile).get();
        InvestigatorProfile subinvestigator1Profile = builder.createProfile().complete().get();
        FirebirdUser subinvestigator1 = builder.createInvestigator(subinvestigator1Profile)
                .withLogin(InvestigatorLogin.fbciinv2).get();
        FirebirdUser subinvestigator2 = builder.createInvestigator().withLogin(InvestigatorLogin.fbciinv3).get();
        primaryRegistration = builder.createRegistration(investigator).withStatus(RegistrationStatus.SUBMITTED)
                .complete().get();
        primaryRegistration.getFinancialDisclosure().getSupportingDocumentation()
                .add(FirebirdFileFactory.getInstance().create());
        invitedSubinvestigatorRegistration = builder
                .createSubinvestigatorRegistration(subinvestigator1, primaryRegistration).withStatus(SUBMITTED)
                .complete().get();
        uninvitedSubinvestigatorRegistration = builder
                .createSubinvestigatorRegistration(subinvestigator2, primaryRegistration)
                .withInvitationStatus(InvitationStatus.NOT_INVITED).withStatus(RegistrationStatus.NOT_STARTED).get();
        builder.createSponsor().withLogin(SPONSOR_LOGIN);
        builder.createSponsor().asDelegate().withLogin(SPONSOR_DELEGATE_LOGIN);
        removeAdditionalAttachmentFromRegistration(primaryRegistration);
        dataSet = builder.build();
    }

    private void removeAdditionalAttachmentFromRegistration(InvestigatorRegistration registration) {
        registration.getAdditionalAttachmentsForm().getAdditionalAttachments().clear();
    }

    @Test
    public void testNavigateToPrimaryRegistration() throws AssociationAlreadyExistsException {
        HomePage home = openHomePage(getLogin());
        ProtocolsListPage protocolsPage = home.getProtocolsMenu().click();
        ProtocolInformationTab protocolTab = protocolsPage.getHelper().clickLink(dataSet.getProtocol());
        protocolTab.getHelper().checkProtocolDisplayed(dataSet.getProtocol());
        ProtocolInvestigatorsTab investigatorsTab = protocolTab.getPage().clickInvestigatorsTab();
        assertNotNull(investigatorsTab.getHelper().getListing(primaryRegistration));
        WaitUtils.pause(400);

        ProtocolSubInvestigatorsTab subInvestigatorsTab = investigatorsTab.getPage().clickSubInvestigatorsTab();
        assertNull(subInvestigatorsTab.getHelper().getListing(uninvitedSubinvestigatorRegistration));
        assertNotNull(subInvestigatorsTab.getHelper().getListing(invitedSubinvestigatorRegistration));

        WaitUtils.pause(400);
        investigatorsTab = subInvestigatorsTab.getPage().clickInvestigatorsTab();
        ReviewRegistrationTab reviewTab = investigatorsTab.getHelper().clickInvestigator(primaryRegistration);
        ReviewRegistrationOverviewTab overviewTab = reviewTab.getPage().clickOverviewTab();
        assertEquals(2, overviewTab.getListings().size());
        assertNotNull(overviewTab.getHelper().getListing(primaryRegistration));
        assertNotNull(overviewTab.getHelper().getListing(invitedSubinvestigatorRegistration));
    }

    protected ReviewRegistrationTab performReviewRegistrationAcceptTest() throws IOException {
        invitedSubinvestigatorRegistration.setStatus(RegistrationStatus.ACCEPTED);
        uninvitedSubinvestigatorRegistration.setStatus(RegistrationStatus.ACCEPTED);
        dataSet.update(invitedSubinvestigatorRegistration, uninvitedSubinvestigatorRegistration);
        ReviewRegistrationTab reviewTab = openHomePage(getLogin()).getHelper().openSubmittedProtocolRegistrationTask(
                primaryRegistration);
        reviewTab.getHelper().checkFormStatuses(primaryRegistration, FormStatus.SUBMITTED);
        addAdditionalAttachment(reviewTab);
        checkAdditionalAttachments(reviewTab);
        checkAdditionalDocuments(reviewTab);
        checkHumanResearchCertificates(reviewTab, primaryRegistration.getHumanResearchCertificateForm());
        reviewTab.getHelper().reviewAllForms(primaryRegistration);
        reviewTab.getHelper().checkFormStatuses(primaryRegistration, FormStatus.IN_REVIEW);
        assertFalse(reviewTab.isCompleteReviewButtonEnabled());
        reviewTab.getHelper().rejectAllForms(primaryRegistration);
        assertTrue(reviewTab.isCompleteReviewButtonEnabled());

        checkAcceptingThenRejectingRetainsRejectionComments(reviewTab);

        reviewTab.getHelper().acceptAllForms(primaryRegistration);
        reviewTab.getHelper().clearFormDispositions(primaryRegistration);
        assertFalse(reviewTab.isCompleteReviewButtonEnabled());
        reviewTab.getHelper().acceptAllForms(primaryRegistration);

        if (getLogin() == dataSet.getSponsorLogin()) {
            ReviewCompletionDialog reviewCompletionDialog = (ReviewCompletionDialog) reviewTab.clickCompleteReview();
            reviewCompletionDialog.clickApproveRegistration();
            reviewTab.getHelper().checkForRegistrationStatus(RegistrationStatus.APPROVED);
        } else {
            reviewTab = (ReviewRegistrationTab) reviewTab.clickCompleteReview();
            reviewTab.waitForCompleteSuccessMessageDisplayed();
        }
        reviewTab.getHelper().checkAllFormDispositionsDisabled(primaryRegistration);
        return reviewTab;
    }

    private void addAdditionalAttachment(ReviewRegistrationTab reviewTab) {
        assertNull(reviewTab.getHelper().getMatchingListing(primaryRegistration.getAdditionalAttachmentsForm()));
        FirebirdFile file = FirebirdFileFactory.getInstance().create();
        primaryRegistration.getAdditionalAttachmentsForm().getAdditionalAttachments().add(file);
        dataSet.update(primaryRegistration);
        reviewTab.getPage().reload();
        assertNotNull(reviewTab.getHelper().getMatchingListing(primaryRegistration.getAdditionalAttachmentsForm()));
    }

    private void checkAdditionalAttachments(ReviewRegistrationTab reviewTab) {
        AdditionalAttachmentsForm form = primaryRegistration.getAdditionalAttachmentsForm();
        ReviewAdditionalAttachmentsDialog additionalAttachmentsDialog = (ReviewAdditionalAttachmentsDialog) reviewTab
                .getHelper().getMatchingListing(form).clickFormDownload();
        Set<FirebirdFile> attachments = form.getAdditionalAttachments();
        assertEquals(attachments.size(), additionalAttachmentsDialog.getListings().size());
        for (FirebirdFile attachment : attachments) {
            assertNotNull(additionalAttachmentsDialog.getHelper().getListing(attachment));
        }
        additionalAttachmentsDialog.clickClose();
        WaitUtils.pause(400);
    }

    private void checkAdditionalDocuments(ReviewRegistrationTab reviewTab) throws IOException {
        Set<AbstractRegistrationForm> expectedForms = primaryRegistration.getFormsForSponsorReview();
        assertEquals(expectedForms.size(), reviewTab.getFormListings().size());
        for (AbstractRegistrationForm form : expectedForms) {
            RegistrationFormListing listing = reviewTab.getHelper().getMatchingListing(form);
            boolean shouldHaveAdditionalDocuments = FORMS_WITH_ADDITIONAL_DOCUMENTS.contains(form.getFormType()
                    .getFormTypeEnum()) && form.isAdditionalDocumentsUploaded();
            assertEquals(shouldHaveAdditionalDocuments, listing.hasAdditionalDocuments());
            if (listing.hasAdditionalDocuments()) {
                if (form instanceof ProtocolForm1572) {
                    checkForClinicalLabCertificates(reviewTab, (ProtocolForm1572) form);
                } else if (form instanceof ProtocolFinancialDisclosure) {
                    checkFinancialDisclosureSupportingDocuments(reviewTab, (ProtocolFinancialDisclosure) form);
                } else {
                    fail(form.getFormType().getName() + " shouldn't have additional documents.");
                }
            }
        }
    }

    private void checkForClinicalLabCertificates(ReviewRegistrationTab reviewTab, ProtocolForm1572 form)
            throws IOException {
        RegistrationClinicalLabCertificatesDialog certificatesDialog = (RegistrationClinicalLabCertificatesDialog) reviewTab
                .getHelper().getMatchingListing(form).clickAdditionalDocuments();
        certificatesDialog.getHelper().checkForClinicalLabCertificates(form);
        certificatesDialog.clickClose();
        WaitUtils.pause(400);
    }

    private void checkFinancialDisclosureSupportingDocuments(ReviewRegistrationTab reviewTab,
            ProtocolFinancialDisclosure form) throws IOException {
        FinancialDisclosuresSupportingDocumentsDialog supportingDocumentsDialog = (FinancialDisclosuresSupportingDocumentsDialog) reviewTab
                .getHelper().getMatchingListing(form).clickAdditionalDocuments();
        supportingDocumentsDialog.getHelper().checkForSupportingDocuments(form);
        supportingDocumentsDialog.clickClose();
        WaitUtils.pause(400);
    }

    private void checkHumanResearchCertificates(ReviewRegistrationTab reviewTab, HumanResearchCertificateForm form) {
        Set<? extends Certificate> actualDocuments = form.getCertificates();
        ReviewHumanResearchCertificatesDialog certificatesDialog = (ReviewHumanResearchCertificatesDialog) reviewTab
                .getHelper().getMatchingListing(form).clickFormDownload();
        for (Certificate certificate : actualDocuments) {
            assertNotNull(certificatesDialog.getHelper().getListing(certificate));
        }
        certificatesDialog.clickClose();
    }

    private void checkAcceptingThenRejectingRetainsRejectionComments(ReviewRegistrationTab reviewTab) {
        reviewTab.getHelper().acceptForm(primaryRegistration.getForm1572());
        FormReviewCommentDialog commentsDialog = reviewTab.getHelper().getMatchingListing(primaryRegistration.getForm1572()).clickRejectRadioButton();
        assertTrue(commentsDialog.getComments().contains(primaryRegistration.getForm1572().getFormType().getName()));
        commentsDialog.clickSave();
    }

    @Test
    public void testReviewRegistrationReject() {
        uninvitedSubinvestigatorRegistration.getInvitation().setInvitationStatus(InvitationStatus.RESPONDED);
        dataSet.update(uninvitedSubinvestigatorRegistration);
        String reviewComments = "This is a comment, we are commenting";
        ReviewRegistrationTab reviewTab = openHomePage(getLogin()).getHelper().openSubmittedProtocolRegistrationTask(
                primaryRegistration);
        reviewTab.getHelper().checkFormStatuses(primaryRegistration, FormStatus.SUBMITTED);
        reviewTab.getHelper().reviewAllForms(primaryRegistration);
        assertFalse(reviewTab.isCompleteReviewButtonEnabled());
        checkFormRejectionCommentsRequired(reviewTab);
        reviewTab.getHelper().rejectAllForms(primaryRegistration);
        assertTrue(reviewTab.isCompleteReviewButtonEnabled());
        RegistrationReviewCommentDialog commentDialog = (RegistrationReviewCommentDialog) reviewTab
                .clickCompleteReview();
        commentDialog.getHelper().checkForFormComments(primaryRegistration);
        ConfirmRegistrationRejectionDialog rejectRegistrationConfirmDialog = commentDialog.clickSave();
        commentDialog = rejectRegistrationConfirmDialog.clickCancel();

        commentDialog.typeComments(reviewComments);
        checkSaveThenCancelRembersEnteredComments(commentDialog);

        rejectRegistrationConfirmDialog = commentDialog.clickSave();
        rejectRegistrationConfirmDialog.clickConfirm().clickClose();

        reviewTab.getHelper().checkAllFormDispositionsDisabled(primaryRegistration);
        reviewTab.getHelper().checkForRegistrationStatus(RegistrationStatus.RETURNED);
        assertFalse(reviewTab.isCompleteReviewButtonEnabled());
        reviewTab.getHelper().checkCommentsForText(reviewComments);

        resubmitRegistration();
        performSecondRejectionTest();
    }

    private void checkFormRejectionCommentsRequired(ReviewRegistrationTab reviewTab) {
        final FormReviewCommentDialog commentDialog = reviewTab.getHelper()
                .getMatchingListing(primaryRegistration.getForm1572()).clickRejectRadioButton();
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "registration.review.form.rejection.comments.required");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                commentDialog.clickSave();
            }
        });
        commentDialog.clickCancel();
    }

    private void checkSaveThenCancelRembersEnteredComments(RegistrationReviewCommentDialog commentDialog) {
        String reviewComments = commentDialog.getComments();
        ConfirmRegistrationRejectionDialog rejectRegistrationConfirmDialog = commentDialog.clickSave();
        rejectRegistrationConfirmDialog.clickCancel();
        commentDialog.getHelper().checkCommentsForText(reviewComments);
    }

    private void resubmitRegistration() {
        RegistrationOverviewTab registrationoverviewTab = openHomePage(dataSet.getInvestigatorLogin()).getHelper()
                .openReturnedTask(primaryRegistration);
        ResubmissionCommentsDialog commentsDialog = (ResubmissionCommentsDialog) registrationoverviewTab
                .clickSubmitRegistration();
        commentsDialog.typeComments("Fixed Issues");
        SignAndSubmitRegistrationDialog signingDialog = (SignAndSubmitRegistrationDialog) commentsDialog.clickSave();
        signingDialog.getHelper().signAndSubmit(dataSet.getInvestigatorLogin()).clickClose();
        assertEquals(RegistrationStatus.SUBMITTED.getDisplay(), registrationoverviewTab.getStatus());
        registrationoverviewTab.getPage().clickSignOut();
    }

    private void performSecondRejectionTest() {
        ReviewRegistrationTab reviewTab = openHomePage(getLogin()).getHelper().openSubmittedProtocolRegistrationTask(
                primaryRegistration);
        reviewTab.getHelper().checkFormStatuses(primaryRegistration, FormStatus.REJECTED);
        reviewTab.getHelper().reviewForm(primaryRegistration.getHumanResearchCertificateForm());
        reviewTab.getHelper().acceptForm(primaryRegistration.getCurriculumVitaeForm());
        reviewTab.getHelper().acceptForm(primaryRegistration.getFinancialDisclosure());

        String additionalComment = " 2nd Reject";
        addAdditionalRejectionComment(primaryRegistration.getForm1572(), additionalComment, reviewTab);
        addAdditionalRejectionComment(primaryRegistration.getHumanResearchCertificateForm(), additionalComment,
                reviewTab);

        RegistrationReviewCommentDialog commentDialog = (RegistrationReviewCommentDialog) reviewTab
                .clickCompleteReview();

        checkSecondRejectionFormComments(additionalComment, commentDialog);
        reviewTab.getHelper().checkForRegistrationStatus(RegistrationStatus.RETURNED);
    }

    private void addAdditionalRejectionComment(AbstractRegistrationForm form, String additionalComment,
            ReviewRegistrationTab reviewTab) {
        FormReviewCommentDialog formCommentDialog = reviewTab.getHelper().getMatchingListing(form).clickComments();
        assertTrue(formCommentDialog.getComments().contains(form.getFormType().getName()));
        formCommentDialog.typeComments(form.getFormType().getName() + additionalComment);
        formCommentDialog.clickSave();
    }

    private void checkSecondRejectionFormComments(String additionalComment,
            RegistrationReviewCommentDialog commentDialog) {
        assertTrue(commentDialog.getFormComments().contains(
                primaryRegistration.getForm1572().getFormType().getDescription()));
        assertTrue(commentDialog.getFormComments().contains(
                primaryRegistration.getForm1572().getFormType().getName() + additionalComment));
        assertTrue(commentDialog.getFormComments().contains(
                primaryRegistration.getHumanResearchCertificateForm().getFormType().getDescription()));
        assertTrue(commentDialog.getFormComments().contains(
                primaryRegistration.getHumanResearchCertificateForm().getFormType().getName() + additionalComment));
        assertFalse(commentDialog.getFormComments().contains(
                primaryRegistration.getFinancialDisclosure().getFormType().getDescription()));
        assertFalse(commentDialog.getFormComments().contains(
                primaryRegistration.getCurriculumVitaeForm().getFormType().getDescription()));
        commentDialog.clickSave().clickConfirm().clickClose();
    }

    @Test
    public void testReviewRejectThenAcceptDifferentRegistration() {
        String reviewComments = "These are my review comments, I am commenting on the rejection of this registration.";
        ReviewRegistrationTab reviewTab = openHomePage(getLogin()).getHelper().openSubmittedProtocolRegistrationTask(
                primaryRegistration);
        reviewTab.getHelper().reviewAllForms(primaryRegistration);
        reviewTab.getHelper().rejectAllForms(primaryRegistration);
        RegistrationReviewCommentDialog commentDialog = (RegistrationReviewCommentDialog) reviewTab
                .clickCompleteReview();
        commentDialog.typeComments(reviewComments);
        ConfirmRegistrationRejectionDialog rejectRegistrationConfirmDialog = commentDialog.clickSave();
        rejectRegistrationConfirmDialog.clickConfirm().clickClose();

        ReviewRegistrationOverviewTab overviewTab = reviewTab.getPage().clickOverviewTab();
        WaitUtils.pause(400); // Sometimes it tries clicking the listing before the page is fully loaded
        reviewTab = overviewTab.getHelper().getListing(invitedSubinvestigatorRegistration).clickInvestigatorLink();
        reviewTab.getHelper().reviewAllForms(invitedSubinvestigatorRegistration);
        reviewTab.getHelper().acceptAllForms(invitedSubinvestigatorRegistration);
        reviewTab.clickCompleteReview();
        reviewTab.waitForCompleteSuccessMessageDisplayed();
        dataSet.reload();
        assertEquals(RegistrationStatus.RETURNED, dataSet.getInvestigatorRegistration().getStatus());
        assertEquals(RegistrationStatus.ACCEPTED, dataSet.getSubInvestigatorRegistration().getStatus());
    }

    @Test
    public void testReviewInProgressRegistration() {
        primaryRegistration.setStatus(IN_PROGRESS);
        primaryRegistration = setFormStatuses(FormStatus.NOT_STARTED);
        HomePage homePage = openHomePage(getLogin());
        ProtocolsListPage protocolsListPage = homePage.getProtocolsMenu().clickBrowse();
        ProtocolInformationTab protocolTab = protocolsListPage.getHelper().clickLink(primaryRegistration.getProtocol());
        ProtocolInvestigatorsTab investigatorsTab = protocolTab.getPage().clickInvestigatorsTab();
        ReviewRegistrationTab reviewRegistrationTab = investigatorsTab.getHelper().clickInvestigator(
                primaryRegistration);
        reviewRegistrationTab.getHelper().clickFormDownload(primaryRegistration.getForm1572());
        dataSet.reload();
        assertEquals(IN_PROGRESS, dataSet.getInvestigatorRegistration().getStatus());
        assertEquals(FormStatus.NOT_STARTED, dataSet.getInvestigatorRegistration().getForm1572().getFormStatus());
    }

    @Test
    public void testUnModifiedReturnedRegistration() {
        primaryRegistration = setFormStatuses(FormStatus.REJECTED);
        ReviewRegistrationTab reviewTab = openHomePage(getLogin()).getHelper().openSubmittedProtocolRegistrationTask(
                primaryRegistration);
        reviewTab.getHelper().checkForRegistrationStatus(RegistrationStatus.IN_REVIEW);
        reviewTab.getHelper().checkFormStatuses(primaryRegistration, FormStatus.REJECTED);
        assertTrue(reviewTab.isCompleteReviewButtonEnabled());
    }

    private InvestigatorRegistration setFormStatuses(FormStatus status) {
        for (AbstractRegistrationForm form : primaryRegistration.getFormsForSponsorReview()) {
            form.setFormStatus(status);
            if (status == FormStatus.REJECTED) {
                form.setComments("comments");
            }
        }
        dataSet.update(primaryRegistration);
        return dataSet.getInvestigatorRegistration();
    }

    abstract LoginAccount getLogin();

    DataSet getDataSet() {
        return dataSet;
    }

}
