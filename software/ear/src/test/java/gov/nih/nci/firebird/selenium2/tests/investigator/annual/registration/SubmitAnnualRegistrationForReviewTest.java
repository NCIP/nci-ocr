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

import static org.apache.commons.lang3.StringUtils.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.LaboratoryCertificateType;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.base.ValidationMessageDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AbstractAnnualRegistrationTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AddPharmaceuticalCompanyDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AnnualForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.BrowseAnnualRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.FinancialDisclosureTab.Question;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.SignAndSubmitRegistrationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.OverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.SupplementalInvestigatorDataFormTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignedDocumentsDialog;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.selenium2.pages.util.FirebirdEmailUtils;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.data.AnnualRegistrationTestDataSet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class SubmitAnnualRegistrationForReviewTest extends AbstractFirebirdWebDriverTest {

    private AnnualRegistrationTestDataSet dataSet;
    private String validEmail1 = "valid1@email.com";
    private String validEmail2 = "valid2@email.com";
    private String invalidEmail = "invalid@email@com";
    private String comments = "comments";

    @Test
    public void testSubmission_WithinRenewalWindow() throws IOException {
        dataSet = AnnualRegistrationTestDataSet.createWithDefaultFormOptionalitiesAndCompleteProfile(getDataLoader(),
                getGridResources());
        OverviewTab overviewTab = navigateToOverviewTab();
        assertEquals(RegistrationStatus.IN_PROGRESS.getDisplay(), overviewTab.getRegistrationStatus());
        checkFormStatuses(overviewTab, FormStatus.NOT_STARTED, getNonIdfForms());
        checkFormStatus(overviewTab, FormStatus.COMPLETED, dataSet.getRenewalRegistration().getSupplementalInvestigatorDataForm());
        overviewTab = checkFormTransitionToInProgress(overviewTab);
        checkIncompleSubmissionAttempt(overviewTab);
        overviewTab = completeForms(overviewTab);
        overviewTab = submitRegistration(overviewTab);
        checkForRenewalDate(true);
        checkForExpectedEmails();
        verifyRegistrationIsReadOnly(overviewTab);
    }

    private Iterable<AbstractRegistrationForm> getNonIdfForms() {
        return Iterables.filter(dataSet.getRenewalRegistration().getForms(), new Predicate<AbstractRegistrationForm>() {
            @Override
            public boolean apply(AbstractRegistrationForm form) {
                return form.getFormType().getFormTypeEnum() != FormTypeEnum.SUPPLEMENTAL_INVESTIGATOR_DATA_FORM;
            }
        });
    }

    private OverviewTab navigateToOverviewTab() {
        BrowseAnnualRegistrationsPage browseRegistrationsPage = openHomePage(dataSet.getInvestigatorLogin(),
                getCtepProvider()).getInvestigatorMenu().clickAnnualRegistrations();
        return browseRegistrationsPage.getHelper().getRegistrationListing(dataSet.getRenewalRegistration())
                .clickEditButton();
    }

    private void checkFormStatuses(OverviewTab overviewTab, FormStatus status) {
        checkFormStatuses(overviewTab, status, dataSet.getRenewalRegistration().getForms());
    }

    private void checkFormStatuses(OverviewTab overviewTab, FormStatus status, Iterable<AbstractRegistrationForm> forms) {
        for (AbstractRegistrationForm form : forms) {
            if (form.isReviewRequired()) {
                checkFormStatus(overviewTab, status, form);
            } else {
                assertEquals(FormStatus.NOT_APPLICABLE.getDisplay(), overviewTab.getHelper().getListing(form)
                        .getFormStatus());
            }
        }
    }

    private void checkFormStatus(OverviewTab overviewTab, FormStatus status, AbstractRegistrationForm form) {
        assertEquals(status.getDisplay(), overviewTab.getHelper().getListing(form).getFormStatus());
    }

    private OverviewTab checkFormTransitionToInProgress(OverviewTab overviewTab) {
        overviewTab = overviewTab.getPage().clickFinancialDisclosureTab().getPage().clickOverviewTab();
        overviewTab = overviewTab.getPage().clickForm1572Tab().getPage().clickOverviewTab();
        overviewTab = overviewTab.getPage().clickSupplementalInvestigatorDataFormTab().getPage().clickOverviewTab();
        overviewTab = overviewTab.getPage().clickAdditionalAttachmentsTab().getPage().clickOverviewTab();
        checkFormStatuses(overviewTab, FormStatus.IN_PROGRESS, getNonIdfForms());
        return overviewTab;
    }

    private void checkIncompleSubmissionAttempt(OverviewTab overviewTab) {
        ValidationMessageDialog validationMessageDialog = (ValidationMessageDialog) overviewTab.clickSubmitForReview();
        validationMessageDialog.getHelper().verifyMessagesDisplayed("validation.failure.missing.clinical.laboratory",
                "validation.failure.missing.institutional.review.board",
                "validation.failure.ctep.financial.disclosure.questions.incomplete",
                "validation.failure.missing.practice.site");

        validationMessageDialog.clickClose();
        checkFormStatuses(overviewTab, FormStatus.INCOMPLETE, getNonIdfForms());
    }

    private OverviewTab completeForms(OverviewTab overviewTab) {
        overviewTab.getHelper().checkProgressBar(1, 3);

        overviewTab = checkIdf(overviewTab);
        overviewTab.getHelper().checkProgressBar(1, 3);

        overviewTab = completeFinancialDisclosure(overviewTab);
        overviewTab.getHelper().checkProgressBar(2, 3);

        overviewTab = completeForm1572(overviewTab);
        overviewTab.getHelper().checkProgressBar(3, 3);

        checkFormStatuses(overviewTab, FormStatus.COMPLETED);
        return overviewTab;
    }

    private OverviewTab checkIdf(OverviewTab overviewTab) {
        SupplementalInvestigatorDataFormTab idfTab = overviewTab.getPage().clickSupplementalInvestigatorDataFormTab();
        verifyNoValidationErrorsPresent(idfTab);
        return idfTab.getPage().clickOverviewTab();
    }

    private void verifyValidationErrorsPresent(AbstractAnnualRegistrationTab<?> tab, String... errorMessageKeys) {
        List<String> validationErrors = tab.getValidationErrors();
        assertEquals(errorMessageKeys.length, validationErrors.size());
        for (String errorMessageKey : errorMessageKeys) {
            assertTrue(validationErrors.contains(getPropertyText(errorMessageKey)));
        }
    }

    private void verifyNoValidationErrorsPresent(AbstractAnnualRegistrationTab<?> tab) {
        assertTrue(tab.getValidationErrors().isEmpty());
    }

    private OverviewTab completeFinancialDisclosure(OverviewTab overviewTab) {
        FinancialDisclosureTab financialDisclosureTab = overviewTab.getPage().clickFinancialDisclosureTab();
        verifyValidationErrorsPresent(financialDisclosureTab,
                "validation.failure.ctep.financial.disclosure.questions.incomplete");

        answerAllQuestionsFalse(financialDisclosureTab);
        assertTrue(financialDisclosureTab.getValidationErrors().isEmpty());

        financialDisclosureTab.answerQuestion(Question.Q1_MONETARY_GAIN, true);
        verifyValidationErrorsPresent(financialDisclosureTab,
                "validation.failure.ctep.financial.disclosure.questions.incomplete");

        financialDisclosureTab = addPharmaceuticalCompany(financialDisclosureTab);
        verifyNoValidationErrorsPresent(financialDisclosureTab);
        return financialDisclosureTab.getPage().clickOverviewTab();
    }

    private void answerAllQuestionsFalse(FinancialDisclosureTab financialDisclosureTab) {
        for (Question question : Question.values()) {
            financialDisclosureTab.answerQuestion(question, false);
        }
    }

    private FinancialDisclosureTab addPharmaceuticalCompany(FinancialDisclosureTab financialDisclosureTab) {
        AddPharmaceuticalCompanyDialog addPharmaceuticalCompanyDialog = financialDisclosureTab
                .clickAddPharmaceuticalCompanyButton();
        return addPharmaceuticalCompanyDialog.getHelper().searchAndSelectPharmaceuticalCompany(
                getExistingPharmaceuticalCompany());
    }

    private OverviewTab completeForm1572(OverviewTab overviewTab) {
        AnnualForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        verifyValidationErrorsPresent(form1572Tab, "validation.failure.missing.clinical.laboratory",
                "validation.failure.missing.institutional.review.board", "validation.failure.missing.practice.site");

        addIrb(form1572Tab);
        verifyValidationErrorsPresent(form1572Tab, "validation.failure.missing.clinical.laboratory",
                "validation.failure.missing.practice.site");

        addPracticeSite(form1572Tab);
        verifyValidationErrorsPresent(form1572Tab, "validation.failure.missing.clinical.laboratory");

        form1572Tab = addClinicalLab(form1572Tab);

        form1572Tab.unselectPhaseOne();
        form1572Tab.unselectPhaseTwoOrThree();
        verifyValidationErrorsPresent(form1572Tab, "validation.failure.missing.phase");

        form1572Tab.selectPhaseOne();
        verifyNoValidationErrorsPresent(form1572Tab);
        return form1572Tab.getPage().clickOverviewTab();
    }

    private void addIrb(AnnualForm1572Tab form1572Tab) {
        OrganizationAssociation irb = Iterables.getFirst(dataSet.getRenewalRegistration().getProfile()
                .getOrganizationAssociations(OrganizationRoleType.IRB), null);
        form1572Tab.getIrbSection().getHelper().getListing(irb.getOrganizationRole()).select();
    }

    private void addPracticeSite(AnnualForm1572Tab form1572Tab) {
        OrganizationAssociation practiceSite = Iterables.getFirst(dataSet.getRenewalRegistration().getProfile()
                .getOrganizationAssociations(OrganizationRoleType.PRACTICE_SITE), null);
        form1572Tab.getPracticeSiteSection().getHelper().getListing(practiceSite.getOrganizationRole()).select();
    }

    private AnnualForm1572Tab addClinicalLab(AnnualForm1572Tab form1572Tab) {
        OrganizationAssociation clinicalLab = Iterables.getFirst(dataSet.getRenewalRegistration().getProfile()
                .getOrganizationAssociations(OrganizationRoleType.CLINICAL_LABORATORY), null);
        form1572Tab.getClinicalLabSection().getHelper().getListing(clinicalLab.getOrganizationRole()).select();
        form1572Tab = form1572Tab.getPage().clickOverviewTab().getPage().clickForm1572Tab();
        return addLaboratorCertificate(form1572Tab, clinicalLab);
    }

    private AnnualForm1572Tab addLaboratorCertificate(AnnualForm1572Tab form1572Tab, OrganizationAssociation clinicalLab) {
        LaboratoryCertificate laboratorCertificate = new LaboratoryCertificate(LaboratoryCertificateType.CLIA);
        laboratorCertificate.setEffectiveDate(new Date(0));
        laboratorCertificate.setExpirationDate(DateUtils.addYears(new Date(), 99));
        laboratorCertificate.setCertificateFile(FirebirdFileFactory.getInstance().create());
        ((ClinicalLaboratory) clinicalLab.getOrganizationRole()).addCertificate(laboratorCertificate);
        dataSet.update(clinicalLab);
        return form1572Tab.getPage().clickOverviewTab().getPage().clickForm1572Tab();
    }

    private OverviewTab submitRegistration(OverviewTab overviewTab) throws IOException {
        SignAndSubmitRegistrationDialog signingDialog = (SignAndSubmitRegistrationDialog) overviewTab
                .clickSubmitForReview();
        checkInvalidSubmissionRetainsCommentsAndEmailAddresses(signingDialog);
        SignedDocumentsDialog signedDocumentsDialog = signingDialog.getHelper().signAndSubmit(
                dataSet.getInvestigatorLogin());
        dataSet.reload();
        signedDocumentsDialog.getHelper().checkForSignedDocuments(dataSet.getRenewalRegistration());
        overviewTab = (OverviewTab) signedDocumentsDialog.clickClose();
        assertEquals(RegistrationStatus.SUBMITTED.getDisplay(), overviewTab.getRegistrationStatus());
        checkFormStatuses(overviewTab, FormStatus.SUBMITTED);
        assertEquals(comments, overviewTab.getInvestigatorComments());
        return overviewTab;
    }

    private void checkInvalidSubmissionRetainsCommentsAndEmailAddresses(
            final SignAndSubmitRegistrationDialog signingDialog) {
        signingDialog.typeComments(comments);
        checkAdditionalEmailAddresses(signingDialog);
        signingDialog.typeUsername("bad username");
        signingDialog.typePassword("bad password");
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure("authentication.invalid.credentials");
        expectedFailure.assertFailureOccurs(new FailingAction() {

            @Override
            public void perform() {
                signingDialog.clickSign();
            }
        });

        assertTrue(signingDialog.getComments().contains(comments));
        List<String> additionalEmailAddresses = signingDialog.getAdditionalEmailAddresses();
        assertTrue(additionalEmailAddresses.contains(validEmail1));
        assertTrue(additionalEmailAddresses.contains(validEmail2));
    }

    private void checkAdditionalEmailAddresses(final SignAndSubmitRegistrationDialog signingDialog) {
        signingDialog.addEmailAddress(validEmail1);
        assertTrue(isEmpty(signingDialog.getInvalidEmailAddressMessage()));
        signingDialog.addEmailAddress(invalidEmail);
        assertEquals(invalidEmail + " " + getPropertyText("forms.submission.invalid.email.address.suffix"),
                signingDialog.getInvalidEmailAddressMessage());
        signingDialog.addEmailAddress(validEmail2);
        assertTrue(isEmpty(signingDialog.getInvalidEmailAddressMessage()));
    }

    private void checkForRenewalDate(boolean withinRenewalWindow) {
        Date expectedRenewalDate;
        if (withinRenewalWindow) {
            expectedRenewalDate = DateUtils.addYears(dataSet.getRenewalRegistration().getDueDate(), 1);
        } else {
            expectedRenewalDate = DateUtils.addYears(new Date(), 1);
        }
        dataSet.reload();
        assertEquals(DateUtils.truncate(expectedRenewalDate, Calendar.DATE), dataSet.getRenewalRegistration()
                .getRenewalDate());
    }

    private void checkForExpectedEmails() {
        getEmailChecker().assertEmailCount(4);
        String expectedSubject = FirebirdEmailUtils.getExpectedSubject(
                FirebirdMessageTemplate.COORDINATOR_ANNUAL_REGISTRATION_SUBMISSION_NOTIFICATION_EMAIL,
                dataSet.getRenewalRegistration());
        String coordinatorEmail = dataSet.getRegistrationCoordinatorUser().getPerson().getEmail();
        getEmailChecker().getSentEmail(coordinatorEmail, expectedSubject);
        getEmailChecker().getSentEmail(validEmail1, expectedSubject);
        getEmailChecker().getSentEmail(validEmail2, expectedSubject);
    }

    private void verifyRegistrationIsReadOnly(OverviewTab overviewTab) {
        assertTrue(overviewTab.isReadOnly());
        assertTrue(overviewTab.getPage().clickForm1572Tab().isReadOnly());
        assertTrue(overviewTab.getPage().clickFinancialDisclosureTab().isReadOnly());
        assertTrue(overviewTab.getPage().clickSupplementalInvestigatorDataFormTab().isReadOnly());
        assertTrue(overviewTab.getPage().clickAdditionalAttachmentsTab().isReadOnly());
    }

    @Test
    public void testSubmission_AfterRenewalWindow() throws IOException {
        dataSet = AnnualRegistrationTestDataSet.createReadyForSubmission(getDataLoader(), getGridResources());
        dataSet.getRenewalRegistration().setDueDate(DateUtils.addDays(new Date(), -50));
        dataSet.update(dataSet.getRenewalRegistration());
        submitRegistration(navigateToOverviewTab());
        checkForRenewalDate(false);
    }

}
