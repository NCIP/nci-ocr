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
package gov.nih.nci.firebird.selenium2.scalability.tests;

import static gov.nih.nci.firebird.selenium2.scalability.tests.TimedAction.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.test.TestFileUtils;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AnnualAdditionalAttachmentsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AnnualForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AnnualRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.BrowseAnnualRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.FinancialDisclosureTab.Question;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.OverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.SignAndSubmitRegistrationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignedDocumentsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.UploadFileDialog;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSet;

import java.io.IOException;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class SubmitAnnualRegistrationTest extends AbstractScalabilityTest {

    private DataSet dataSet;
    private PrimaryOrganization originalPrimaryOrganization;
    private Organization pharmaceuticalCompany;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getBuilder().createInvestigatorWithCompleteProfile().asCtepUser();
        dataSet = getBuilder().build();
        originalPrimaryOrganization = dataSet.getInvestigatorProfile().getPrimaryOrganization();
    }

    @Test
    public void testSubmitAnnualRegistration() throws IOException {
        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin(), getCtepProvider());
        BrowseAnnualRegistrationsPage registrationsPage = openBrowseRegistrationsPage(homePage);
        AnnualRegistrationPage registrationPage = openRegistration(registrationsPage);

        completeFinancialDisclosureTab(registrationPage);
        completeForm1572Tab(registrationPage);
        completeAdditionalAttachmentsTab(registrationPage);
        submitRegistration(registrationPage);
    }

    private BrowseAnnualRegistrationsPage openBrowseRegistrationsPage(final HomePage homePage) {
        return new TimedAction<BrowseAnnualRegistrationsPage>("Click Annual Registrations Menu Item") {
            @Override
            public BrowseAnnualRegistrationsPage perform() {
                return homePage.getInvestigatorMenu().clickAnnualRegistrations();
            }
        }.time();
    }

    private AnnualRegistrationPage openRegistration(final BrowseAnnualRegistrationsPage registrationsPage) {
        return new TimedAction<AnnualRegistrationPage>("Open Registration") {
            @Override
            public AnnualRegistrationPage perform() {
                return registrationsPage.getRegistrationListings().get(0).clickEditButton().getPage();
            }
        }.time();
    }

    private void completeFinancialDisclosureTab(AnnualRegistrationPage registrationPage) throws IOException {
        FinancialDisclosureTab financialDisclosureTab = openFinancialDisclosureTab(registrationPage);
        addPharmaceuticalCompanyToFinancialDisclosure(financialDisclosureTab);
        answerAllFinancialDisclosureQuestions(financialDisclosureTab);
        attachFileToFinancialDisclosure(financialDisclosureTab);
    }

    private FinancialDisclosureTab openFinancialDisclosureTab(final AnnualRegistrationPage registrationPage) {
        return new TimedAction<FinancialDisclosureTab>("Open Financial Disclosure Tab") {
            @Override
            public FinancialDisclosureTab perform() {
                return registrationPage.clickFinancialDisclosureTab();
            }
        }.time();
    }

    private void addPharmaceuticalCompanyToFinancialDisclosure(final FinancialDisclosureTab financialDisclosureTab) {
        new TimedAction<Void>("Add Pharmaceutical Company to Financial Disclosure", MAX_NES_RESPONSE_TIME_SECONDS) {
            @Override
            public Void perform() {
                financialDisclosureTab.getHelper().selectExistingPharmaceuticalCompany(getExistingPharmaceuticalCompany());
                return null;
            }
        }.time();
        pharmaceuticalCompany = dataSet.getLastCreatedObject(Organization.class);
    }

    private void answerAllFinancialDisclosureQuestions(FinancialDisclosureTab financialDisclosureTab) {
        for (Question question : Question.values()) {
            answerFinancialDisclosureQuestion(financialDisclosureTab, question);
        }
    }

    private void answerFinancialDisclosureQuestion(final FinancialDisclosureTab financialDisclosureTab, final Question question) {
        new TimedAction<Void>("Answer Financial Disclosure Question " + question) {
            @Override
            public Void perform() {
                financialDisclosureTab.answerQuestion(question, RandomUtils.nextBoolean());
                return null;
            }
        }.time();
    }

    private void attachFileToFinancialDisclosure(final FinancialDisclosureTab financialDisclosureTab) {
        new TimedAction<Void>("Attach File to Financial Disclosure") {
            @Override
            public Void perform() throws IOException {
                financialDisclosureTab.getSupportingDocumentsTag().getHelper().uploadFile(TestFileUtils.createTemporaryFile());
                return null;
            }
        }.time();
    }

    private void completeForm1572Tab(AnnualRegistrationPage registrationPage) {
        AnnualForm1572Tab form1572Tab = openForm1572Tab(registrationPage);
        selectClinicalLab(form1572Tab);
        selectIrb(form1572Tab);
        selectPracticeSite(form1572Tab);
    }

    private AnnualForm1572Tab openForm1572Tab(final AnnualRegistrationPage registrationPage) {
        return new TimedAction<AnnualForm1572Tab>("Open Form 1572 Tab") {
            @Override
            public AnnualForm1572Tab perform() throws IOException {
                return registrationPage.clickForm1572Tab();
            }
        }.time();
    }

    private void selectClinicalLab(final AnnualForm1572Tab form1572Tab) {
        new TimedAction<Void>("Select Clinical Lab") {
            @Override
            public Void perform() throws IOException {
                form1572Tab.getClinicalLabSection().getListings().get(0).select();
                return null;
            }
        }.time();
    }

    private void selectIrb(final AnnualForm1572Tab form1572Tab) {
        new TimedAction<Void>("Select IRB") {
            @Override
            public Void perform() throws IOException {
                form1572Tab.getIrbSection().getListings().get(0).select();
                return null;
            }
        }.time();
    }

    private void selectPracticeSite(final AnnualForm1572Tab form1572Tab) {
        new TimedAction<Void>("Select Practice Site") {
            @Override
            public Void perform() throws IOException {
                form1572Tab.getPracticeSiteSection().getListings().get(0).select();
                return null;
            }
        }.time();
    }

    private void completeAdditionalAttachmentsTab(AnnualRegistrationPage registrationPage)
            throws IOException {
        AnnualAdditionalAttachmentsTab additionalAttachmentsTab = openAdditionalAttachmentsTab(registrationPage);
        UploadFileDialog uploadFileDialog = clickAddNewAttachment(additionalAttachmentsTab);
        saveAttachment(uploadFileDialog);
    }

    private AnnualAdditionalAttachmentsTab openAdditionalAttachmentsTab(final AnnualRegistrationPage registrationPage) {
        return new TimedAction<AnnualAdditionalAttachmentsTab>("Open Additional Attachments Tab") {
            @Override
            public AnnualAdditionalAttachmentsTab perform() throws IOException {
                return registrationPage.clickAdditionalAttachmentsTab();
            }
        }.time();
    }

    private UploadFileDialog clickAddNewAttachment(final AnnualAdditionalAttachmentsTab additionalAttachmentsTab) {
        return new TimedAction<UploadFileDialog>("Click Add New Attachment") {
            @Override
            public UploadFileDialog perform() throws IOException {
                return additionalAttachmentsTab.clickAddNewAttachmentButton();
            }
        }.time();
    }

    private void saveAttachment(final UploadFileDialog uploadFileDialog) throws IOException {
        uploadFileDialog.setFile(TestFileUtils.createTemporaryFile());
        uploadFileDialog.typeDescription(ValueGenerator.getUniqueString());
        new TimedAction<Void>("Save Attachment") {
            @Override
            public Void perform() throws IOException {
                uploadFileDialog.clickSaveButton();
                return null;
            }
        }.time();
    }

    private void submitRegistration(AnnualRegistrationPage registrationPage) {
        OverviewTab overviewTab = openOverviewTab(registrationPage);
        SignAndSubmitRegistrationDialog signDialog = clickSubmitRegistration(overviewTab);
        signRegistration(signDialog);
        assertEquals(RegistrationStatus.SUBMITTED.getDisplay(), overviewTab.getRegistrationStatus());
    }

    private OverviewTab openOverviewTab(final AnnualRegistrationPage registrationPage) {
        return new TimedAction<OverviewTab>("Open Overview Tab") {
            @Override
            public OverviewTab perform() throws IOException {
                return registrationPage.clickOverviewTab();
            }
        }.time();
    }

    private SignAndSubmitRegistrationDialog clickSubmitRegistration(final OverviewTab overviewTab) {
        return new TimedAction<SignAndSubmitRegistrationDialog>("Click Registration Submission Button", 
                MAX_NES_RESPONSE_TIME_SECONDS) {
            @Override
            public SignAndSubmitRegistrationDialog perform() throws IOException {
                return (SignAndSubmitRegistrationDialog) overviewTab.clickSubmitForReview();
            }
        }.time();
    }

    private void signRegistration(final SignAndSubmitRegistrationDialog signDialog) {
        SignedDocumentsDialog signedDocumentsDialog = new TimedAction<SignedDocumentsDialog>("Sign Registration", 
                MAX_AUTHENTICATION_RESPONSE_TIME_SECONDS) {
            @Override
            public SignedDocumentsDialog perform() throws IOException {
                return signDialog.getHelper().signAndSubmit(dataSet.getInvestigatorLogin());
            }
        }.time();
        signedDocumentsDialog.clickClose();
    }

    @Override
    protected void deleteTestData() {
        delete(dataSet, dataSet.getLastCreatedObject(AnnualRegistration.class));
        delete(dataSet, pharmaceuticalCompany);
        deleteInvestigator(dataSet, dataSet.getInvestigator());
        delete(dataSet, originalPrimaryOrganization);
        if (originalPrimaryOrganization != null) {
            delete(dataSet, originalPrimaryOrganization.getOrganization());
        }
    }

}
