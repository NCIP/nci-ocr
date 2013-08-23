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

import static gov.nih.nci.firebird.commons.test.TestFileUtils.*;
import static gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab.Question.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab.Question;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.LoginAccount.InvestigatorLogin;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class FinancialDisclosureTabTest extends AbstractFirebirdWebDriverTest {

    private FinancialDisclosureTab financialDisclosureTab;
    private DataSet dataSet;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setUpData();
        RegistrationOverviewTab registrationPage = openRegistrationOverviewTab();
        financialDisclosureTab = registrationPage.getPage().clickFinancialDisclosureTab();
        financialDisclosureTab.getPage().getHelper().checkRegistrationPageHeader(getRegistration());
        assertTrue(financialDisclosureTab.isBottomPdfButtonPresent());
        assertFalse(financialDisclosureTab.areCommentsPresent());
    }

    void setUpData() throws AssociationAlreadyExistsException {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        InvestigatorProfile investigatorProfile = builder.createProfile().complete().get();
        FirebirdUser investigator = builder.createInvestigator(investigatorProfile).get();
        InvestigatorProfile subinvestigatorProfile = builder.createProfile().complete().get();
        FirebirdUser subinvestigator =
                builder.createInvestigator(subinvestigatorProfile).withLogin(InvestigatorLogin.fbciinv2).get();
        InvestigatorRegistration primaryRegistration = builder.createRegistration(investigator).get();
        builder.createSubinvestigatorRegistration(subinvestigator, primaryRegistration);
        dataSet = builder.build();
    }

    AbstractProtocolRegistration getRegistration() {
        return dataSet.getInvestigatorRegistration();
    }

    private RegistrationOverviewTab openRegistrationOverviewTab() {
        BrowseRegistrationsPage registrationsPage = openHomePage(getInvestigatorLogin()).getInvestigatorMenu()
                .clickProtocolRegistrations();
        return registrationsPage.getHelper().clickRegistrationLink(getRegistration());
    }

    LoginAccount getInvestigatorLogin() {
        return dataSet.getInvestigatorLogin();
    }

    @Test
    public void testFinancialDisclosurePage() throws IOException {
        checkQuestionBoxes();
        checkFileUpload();
    }

    private void checkQuestionBoxes() throws IOException {
        checkDefaultQuestionValues();
        checkTabOnlySavesSelectedValues();
        checkSelectingNoFinancialInterestSetsOthersToFalse();
        checkYesToQuestionDeselectsNoFinancialInterest();
        checkSeleectingNoToAllSelectsNoFinancialInterest();
        financialDisclosureTab.clickViewGeneratedFinancialDisclosure();
    }

    private void checkDefaultQuestionValues() {
        assertNull(financialDisclosureTab.getAnswer(Q1_MONETARY_GAIN));
        assertNull(financialDisclosureTab.getAnswer(Q2_OTHER_SPONSOR_PAYMENTS));
        assertNull(financialDisclosureTab.getAnswer(Q3_FINANCIAL_INTEREST));
        assertNull(financialDisclosureTab.getAnswer(Q4_EQUITY_IN_SPONSOR));
        assertFalse(financialDisclosureTab.getAnswer(Q5_NO_FINANCIAL_INTEREST));
    }

    private void checkTabOnlySavesSelectedValues() {
        financialDisclosureTab.answerQuestion(Q1_MONETARY_GAIN, false);
        financialDisclosureTab.answerQuestion(Q3_FINANCIAL_INTEREST, true);
        financialDisclosureTab.getPage().clickOverviewTab().getPage().clickFinancialDisclosureTab();
        assertFalse(financialDisclosureTab.getAnswer(Q1_MONETARY_GAIN));
        assertNull(financialDisclosureTab.getAnswer(Q2_OTHER_SPONSOR_PAYMENTS));
        assertTrue(financialDisclosureTab.getAnswer(Q3_FINANCIAL_INTEREST));
        assertNull(financialDisclosureTab.getAnswer(Q4_EQUITY_IN_SPONSOR));
        assertFalse(financialDisclosureTab.getAnswer(Q5_NO_FINANCIAL_INTEREST));
    }

    private void checkSelectingNoFinancialInterestSetsOthersToFalse() {
        financialDisclosureTab.answerQuestion(Q5_NO_FINANCIAL_INTEREST, true);
        checkSelectedAnswers(Q5_NO_FINANCIAL_INTEREST);
        financialDisclosureTab.getPage().clickOverviewTab().getPage().clickFinancialDisclosureTab();
        checkSelectedAnswers(Q5_NO_FINANCIAL_INTEREST);
    }

    private void checkSelectedAnswers(Question... expectedTrue) {
        for (Question question : Question.values()) {
            boolean expectedAnswer = ArrayUtils.contains(expectedTrue, question);
            assertEquals(expectedAnswer, financialDisclosureTab.getAnswer(question));
        }
    }

    private void checkYesToQuestionDeselectsNoFinancialInterest() {
        financialDisclosureTab.answerQuestion(Q1_MONETARY_GAIN, true);
        checkSelectedAnswers(Q1_MONETARY_GAIN);
    }

    private void checkSeleectingNoToAllSelectsNoFinancialInterest() {
        financialDisclosureTab.answerQuestion(Q1_MONETARY_GAIN, false);
        checkSelectedAnswers(Q5_NO_FINANCIAL_INTEREST);
    }

    private void checkFileUpload() throws IOException {
        File tempFile = createTemporaryFile();
        File tempFile2 = createTemporaryFile();

        financialDisclosureTab.getSupportingDocumentsTag().getHelper().uploadFile(tempFile);
        financialDisclosureTab.getSupportingDocumentsTag().getHelper().assertListed(tempFile);
        assertEquals("View Generated Financial Disclosure Form", financialDisclosureTab.getViewGeneratedButtonText());

        financialDisclosureTab.getSupportingDocumentsTag().getHelper().uploadFile(tempFile2);
        financialDisclosureTab.getSupportingDocumentsTag().getHelper().assertListed(tempFile2);

        financialDisclosureTab.getSupportingDocumentsTag().getHelper().getListing(tempFile).clickDelete();
        assertNull(financialDisclosureTab.getSupportingDocumentsTag().getHelper().getListing(tempFile));
        financialDisclosureTab.getSupportingDocumentsTag().getHelper().assertListed(tempFile2);
    }

    DataSet getDataSet() {
        return dataSet;
    }

}
