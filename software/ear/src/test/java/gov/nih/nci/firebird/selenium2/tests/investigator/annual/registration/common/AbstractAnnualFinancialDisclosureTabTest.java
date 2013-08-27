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
package gov.nih.nci.firebird.selenium2.tests.investigator.annual.registration.common;

import static gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.FinancialDisclosureTab.Question.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.test.TestFileUtils;
import gov.nih.nci.firebird.data.AnnualRegistrationType;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.base.ConfirmDialog;
import gov.nih.nci.firebird.selenium2.pages.components.tags.SupportingDocumentsTag;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AddPharmaceuticalCompanyDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.FinancialDisclosureTab.Question;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.LoginAccount.SponsorDelegateLogin;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractAnnualFinancialDisclosureTabTest extends AbstractFirebirdWebDriverTest {

    private DataSet dataSet;
    private FinancialDisclosureTab fdfTab;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setUpData();
        fdfTab = navigateToFinancialDisclosureTab();
    }

    protected void setUpData() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        InvestigatorProfile profile = builder.createProfile().complete().get();
        FirebirdUser investigator = builder.createInvestigator(profile).asCtepUser().get();
        builder.createSponsor().asCtepUser();
        builder.createSponsor().asCtepUser().asDelegate().withLogin(SponsorDelegateLogin.fbcidel1);
        configureRegistrationDataSet(builder, investigator);
        dataSet = builder.build();
    }

    protected void configureRegistrationDataSet(DataSetBuilder builder, FirebirdUser investigator) {
        builder.createAnnualRegistration(investigator)
                .withType(AnnualRegistrationType.INITIAL)
                .get();
    }

    protected DataSet getDataSet() {
        return dataSet;
    }

    abstract protected FinancialDisclosureTab navigateToFinancialDisclosureTab();

    @Test
    public void testFinancialDisclosureTab() throws IOException {
        checkQuestionBoxes();
        checkFileUpload();
        checkPharmaceuticalCompanySelection();
        assertTrue(fdfTab.clickViewPdfButton().length() > 0);
    }

    private void checkQuestionBoxes() throws IOException {
        checkDefaultQuestionValues();
        checkTabOnlySavesSelectedValues();
        answerAllQuestions();
    }

    private void checkDefaultQuestionValues() {
        assertNull(fdfTab.getAnswer(Q1_MONETARY_GAIN));
        assertNull(fdfTab.getAnswer(Q2_OTHER_SPONSOR_PAYMENTS));
        assertNull(fdfTab.getAnswer(Q3_FINANCIAL_INTEREST));
        assertNull(fdfTab.getAnswer(Q4_EQUITY_IN_SPONSOR));
    }

    private void checkTabOnlySavesSelectedValues() {
        fdfTab.answerQuestion(Q1_MONETARY_GAIN, false);
        fdfTab.answerQuestion(Q3_FINANCIAL_INTEREST, true);
        fdfTab.getPage().clickOverviewTab();
        fdfTab.getPage().clickFinancialDisclosureTab();
        assertFalse(fdfTab.getAnswer(Q1_MONETARY_GAIN));
        assertNull(fdfTab.getAnswer(Q2_OTHER_SPONSOR_PAYMENTS));
        assertTrue(fdfTab.getAnswer(Q3_FINANCIAL_INTEREST));
        assertNull(fdfTab.getAnswer(Q4_EQUITY_IN_SPONSOR));
    }

    private void answerAllQuestions() {
        fdfTab.answerQuestion(Q1_MONETARY_GAIN, true);
        fdfTab.answerQuestion(Q2_OTHER_SPONSOR_PAYMENTS, false);
        fdfTab.answerQuestion(Q3_FINANCIAL_INTEREST, false);
        fdfTab.answerQuestion(Q4_EQUITY_IN_SPONSOR, true);
        checkSelectedAnswers(Q1_MONETARY_GAIN, Q4_EQUITY_IN_SPONSOR);
    }

    private void checkSelectedAnswers(Question... expectedTrue) {
        for (Question question : Question.values()) {
            boolean expectedAnswer = ArrayUtils.contains(expectedTrue, question);
            assertEquals(expectedAnswer, fdfTab.getAnswer(question));
        }
    }

    private void checkFileUpload() throws IOException {
        checkFileSelectionRequired();
        File tempFile = TestFileUtils.createTemporaryFile();
        File tempFile2 = TestFileUtils.createTemporaryFile();

        SupportingDocumentsTag supportingDocumentsTag = fdfTab.getSupportingDocumentsTag();
        supportingDocumentsTag.getHelper().uploadFile(tempFile);
        assertNotNull(supportingDocumentsTag.getHelper().getListing(tempFile));

        supportingDocumentsTag.getHelper().uploadFile(tempFile2);
        assertNotNull(supportingDocumentsTag.getHelper().getListing(tempFile2));

        supportingDocumentsTag.getHelper().getListing(tempFile).clickDelete();
        assertNull(supportingDocumentsTag.getHelper().getListing(tempFile));
        assertNotNull(supportingDocumentsTag.getHelper().getListing(tempFile2));
    }

    private void checkFileSelectionRequired() {
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure("investigator.profile.fileNotSelected");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                fdfTab.getSupportingDocumentsTag().clickUploadButton();
            }
        });
    }

    private void checkPharmaceuticalCompanySelection() {
        Organization newPharmaceuticalCompany = OrganizationFactory.getInstance().create();
        Organization existingPharmaceuticalCompany = getExistingPharmaceuticalCompany();
        createNewPharmaceuticalCompany(newPharmaceuticalCompany);
        selectExistingPharmaceuticalCompany(existingPharmaceuticalCompany);
        deletePharmaceuticalCompanies(newPharmaceuticalCompany, existingPharmaceuticalCompany);
    }

    private void createNewPharmaceuticalCompany(Organization pharmaceuticalCompany) {
        checkPhoneNumberRequired(pharmaceuticalCompany);
        fdfTab = fdfTab.getHelper().createNewPharmaceuticalCompany(pharmaceuticalCompany);
        assertNotNull(fdfTab.getHelper().getListing(pharmaceuticalCompany));
    }

    private void checkPhoneNumberRequired(final Organization pharmaceuticalCompany) {
        String existingPhoneNumber = pharmaceuticalCompany.getPhoneNumber();
        pharmaceuticalCompany.setPhoneNumber(null);
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure("phoneNumber.required");
        final AddPharmaceuticalCompanyDialog pharmaceuticalCompanyDialog = fdfTab.clickAddPharmaceuticalCompanyButton();
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                pharmaceuticalCompanyDialog.getHelper().createNewPharmaceuticalCompany(pharmaceuticalCompany);
            }
        });
        fdfTab = pharmaceuticalCompanyDialog.clickCancel();
        pharmaceuticalCompany.setPhoneNumber(existingPhoneNumber);
    }

    private void selectExistingPharmaceuticalCompany(Organization pharmaceuticalCompany) {
        fdfTab = fdfTab.getHelper().selectExistingPharmaceuticalCompany(pharmaceuticalCompany);
        assertNotNull(fdfTab.getHelper().getListing(pharmaceuticalCompany));
    }

    private void deletePharmaceuticalCompanies(Organization newPharmaceuticalCompany,
            Organization existingPharmaceuticalCompany) {
        ConfirmDialog confirmDialog = fdfTab.getHelper().getListing(newPharmaceuticalCompany).clickDeleteIcon();
        confirmDialog.getHelper().checkTitleAndMessage(newPharmaceuticalCompany.getName());
        confirmDialog.clickConfirmButton();
        assertNull(fdfTab.getHelper().getListing(newPharmaceuticalCompany));
        assertNotNull(fdfTab.getHelper().getListing(existingPharmaceuticalCompany));
        fdfTab.getHelper().getListing(existingPharmaceuticalCompany).clickDeleteIcon().clickConfirmButton();
        assertNull(fdfTab.getHelper().getListing(existingPharmaceuticalCompany));
    }
}
