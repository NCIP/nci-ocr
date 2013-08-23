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
package gov.nih.nci.firebird.selenium2.tests.investigator.annual.registration;

import static gov.nih.nci.firebird.selenium2.pages.util.FirebirdEmailUtils.*;
import static gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.AnnualRegistrationType;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.InvestigatorStatus;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.BrowseAnnualRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.WithdrawSubmissionDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ProfessionalContactInformationTab;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.ConfirmWithdrawalRequestDenialDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.RegistrationWithdrawalRequestDialog;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import javax.mail.internet.MimeMessage;

import org.junit.Test;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class WithdrawFromRegistrationsTest extends AbstractFirebirdWebDriverTest {

    private static final String WITHDRAWAL_EXPLANATION = "I am done";
    private static final String WITHDRAWAL_DENIAL_EXPLANATION = "Not yet";
    private DataSet dataSet;

    @Inject
    @Named("ctep.registration.notification.mailbox")
    private String registrationNotificationMailbox;
    private AnnualRegistration approvedRegistration;
    private AnnualRegistration submittedRegistration;
    private int totalEmails;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        InvestigatorProfile profile = builder.createProfile().complete().get();
        FirebirdUser investigator = builder.createInvestigator(profile).asCtepUser().get();
        approvedRegistration = builder.createAnnualRegistration(investigator).withStatus(RegistrationStatus.APPROVED).complete().get();
        submittedRegistration = builder.createAnnualRegistration(investigator).renewalOf(approvedRegistration).withStatus(RegistrationStatus.SUBMITTED).complete().get();
        builder.createCoordinator().asCtepUser().withApprovedMangedInvestigator(investigator);
        builder.createSponsor().asCtepUser();
        dataSet = builder.build();
    }

    @Test
    public void testWithdraw() {
        withdrawFromRegistrations();
        denyAsSponsor();
        withdrawFromRegistrations();
        acceptAsSponsor();
        checkSubmittedRegistrationRemoved();
        reactivateInvestigatorStatus();
    }

    private void withdrawFromRegistrations() {
        WithdrawSubmissionDialog withdrawSubmissionDialog = openWithdrawSubmissionDialog(dataSet);
        checkInvalidCredentials(withdrawSubmissionDialog);
        checkCommentsRequired(withdrawSubmissionDialog);
        withdrawSubmissionDialog.getHelper().submitWithdraw(dataSet.getInvestigatorLogin(), WITHDRAWAL_EXPLANATION).clickClose();
        dataSet.reload();
        String retrievedRichTextExplanation = dataSet.getInvestigator().getInvestigatorRole().getWithdrawalRequest()
                .getExplanation();
        assertEquals(WITHDRAWAL_EXPLANATION, RichTextUtil.convertToPlainText(retrievedRichTextExplanation));
        checkForWithdrawalEmailToSponsor();
    }

    private WithdrawSubmissionDialog openWithdrawSubmissionDialog(DataSet dataSet) {
        return openBrowseRegistrationsPage(dataSet).clickWithdrawSubmission();
    }

    private BrowseAnnualRegistrationsPage openBrowseRegistrationsPage(DataSet dataSet) {
        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin(), getCtepProvider());
        return homePage.getInvestigatorMenu().clickAnnualRegistrations();
    }

    private void checkInvalidCredentials(final WithdrawSubmissionDialog withdrawSubmissionDialog) {
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure("authentication.invalid.credentials");
        expectedFailure.assertFailureOccurs(new ExpectedValidationFailure.FailingAction() {
            @Override
            public void perform() {
                withdrawSubmissionDialog.getHelper().submitWithdraw("bad username", "bad password", "comments");
            }
        });
    }

    private void checkCommentsRequired(final WithdrawSubmissionDialog withdrawSubmissionDialog) {
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure(
                "investigator.withdraw.error.comments.required");
        expectedFailure.assertFailureOccurs(new ExpectedValidationFailure.FailingAction() {
            @Override
            public void perform() {
                withdrawSubmissionDialog.getHelper().submitWithdraw(dataSet.getInvestigatorLogin(), "");
            }
        });
    }

    private void checkForWithdrawalEmailToSponsor() {
        totalEmails++;
        getEmailChecker().assertEmailCount(totalEmails);
        String expectedSubject = getExpectedSubject(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_EMAIL, dataSet
                .getInvestigator().getPerson());
        MimeMessage email = getEmailChecker().getSentEmail(expectedSubject);
        assertTrue(getEmailChecker().getContent(email).contains(WITHDRAWAL_EXPLANATION));
    }

    private void denyAsSponsor() {
        HomePage homePage = openHomePage(dataSet.getSponsorLogin(), getCtepProvider());
        homePage.getHelper().checkForRegistrationWithdrawalRequestTask(dataSet.getInvestigator());
        RegistrationWithdrawalRequestDialog withdrawalRequestDialog = homePage.getHelper()
                .openRegistrationWithdrawalRequestTask(dataSet.getInvestigator());
        assertTrue(withdrawalRequestDialog.getExplanation().contains(WITHDRAWAL_EXPLANATION));
        ConfirmWithdrawalRequestDenialDialog denialDialog = withdrawalRequestDialog.clickDeny();
        checkCommentsRequired(denialDialog);
        denialDialog.typeExplanation(WITHDRAWAL_DENIAL_EXPLANATION);
        denialDialog.clickSubmitButton();
        dataSet.reload();
        assertEquals(InvestigatorStatus.ACTIVE, dataSet.getInvestigator().getInvestigatorRole().getStatus());
        assertNull(dataSet.getInvestigator().getInvestigatorRole().getWithdrawalRequest());
        checkForDenialEmails();
    }

    private void checkCommentsRequired(final ConfirmWithdrawalRequestDenialDialog denialDialog) {
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "registration.withdrawal.request.denial.explanation.required");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                denialDialog.clickSubmitButton();
            }
        });
    }

    private void checkForDenialEmails() {
        totalEmails = totalEmails + 3;
        getEmailChecker().assertEmailCount(totalEmails);
        MimeMessage email = getEmailToInvestigator(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_INVESTIGATOR);
        assertTrue(getEmailChecker().getContent(email).contains(WITHDRAWAL_DENIAL_EXPLANATION));

        email = getEmailToCoordinator(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_OTHERS);
        assertTrue(getEmailChecker().getContent(email).contains(WITHDRAWAL_DENIAL_EXPLANATION));

        email = getEmailToRegistrationNotificationMailbox(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_DENIED_EMAIL_TO_OTHERS);
        assertTrue(getEmailChecker().getContent(email).contains(WITHDRAWAL_DENIAL_EXPLANATION));
    }

    private MimeMessage getEmailToInvestigator(FirebirdMessageTemplate messageTemplate) {
        String expectedSubject = getExpectedSubject(messageTemplate);
        return getEmailChecker().getSentEmail(dataSet.getInvestigator().getPerson().getEmail(), expectedSubject);
    }

    private MimeMessage getEmailToCoordinator(FirebirdMessageTemplate messageTemplate) {
        String expectedSubject = getExpectedSubject(messageTemplate, dataSet.getInvestigator().getPerson());
        return getEmailChecker().getSentEmail(dataSet.getCoordinator().getPerson().getEmail(), expectedSubject);
    }

    private MimeMessage getEmailToRegistrationNotificationMailbox(FirebirdMessageTemplate messageTemplate) {
        String expectedSubject = getExpectedSubject(messageTemplate, dataSet.getInvestigator().getPerson());
        return getEmailChecker().getSentEmail(registrationNotificationMailbox, expectedSubject);
    }

    private void acceptAsSponsor() {
        HomePage homePage = openHomePage(dataSet.getSponsorLogin(), getCtepProvider());
        RegistrationWithdrawalRequestDialog withdrawalRequestDialog = homePage.getHelper()
                .openRegistrationWithdrawalRequestTask(dataSet.getInvestigator());
        assertTrue(withdrawalRequestDialog.getExplanation().contains(WITHDRAWAL_EXPLANATION));
        withdrawalRequestDialog.clickAccept();
        dataSet.reload();
        assertEquals(InvestigatorStatus.WITHDRAWN, dataSet.getInvestigator().getInvestigatorRole().getStatus());
        checkForAcceptanceEmails();
    }

    private void checkForAcceptanceEmails() {
        totalEmails = totalEmails + 3;
        getEmailChecker().assertEmailCount(totalEmails);
        assertNotNull(getEmailToInvestigator(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_INVESTIGATOR));
        assertNotNull(getEmailToCoordinator(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_OTHERS));
        assertNotNull(getEmailToRegistrationNotificationMailbox(CTEP_INVESTIGATOR_WITHDRAWAL_REQUEST_ACCEPTED_EMAIL_TO_OTHERS));
    }

    private void checkSubmittedRegistrationRemoved() {
        BrowseAnnualRegistrationsPage registrationsPage = openBrowseRegistrationsPage(dataSet);
        assertNotNull(registrationsPage.getHelper().getRegistrationListing(approvedRegistration));
        assertNull(registrationsPage.getHelper().getRegistrationListing(submittedRegistration));
    }

    private void reactivateInvestigatorStatus() {
        BrowseAnnualRegistrationsPage registrationsPage = openBrowseRegistrationsPage(dataSet);
        registrationsPage.clickReactivate();
        assertFalse(registrationsPage.hasReactivateButton());
        checkReactivationRegistrationCreated(registrationsPage);
        ProfessionalContactInformationTab contactInformationTab = registrationsPage.getInvestigatorMenu().clickProfile();
        assertEquals(InvestigatorStatus.ACTIVE.getDisplay(), contactInformationTab.getInvestigatorStatus());
    }

    private void checkReactivationRegistrationCreated(BrowseAnnualRegistrationsPage registrationsPage) {
        dataSet.reload();
        AnnualRegistration reactivationRegistration = dataSet.getInvestigatorProfile().getCurrentAnnualRegistration();
        assertEquals(AnnualRegistrationType.REACTIVATED, reactivationRegistration.getAnnualRegistrationType());
        assertEquals(AnnualRegistrationType.REACTIVATED, registrationsPage.getHelper().getRegistrationListing(reactivationRegistration).getType());
    }

}
