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
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage.RegistrationListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab.Question;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.InvestigatorRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolAdditionalAttachmentsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignAndSubmitRegistrationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignedDocumentsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.UploadFileDialog;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSet;

import java.io.IOException;
import java.util.EnumSet;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class SubmitProtocolRegistrationTest extends AbstractScalabilityTest {

    private DataSet dataSet;
    private PrimaryOrganization primaryOrganization;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        FirebirdUser investigator = getBuilder().createInvestigatorWithCompleteProfile().get();
        Protocol protocol = getBuilder().createProtocol().withoutLeadOrganization().get();
        getBuilder().createRegistration(protocol, investigator).get();
        dataSet = getBuilder().build();
        primaryOrganization = dataSet.getInvestigatorProfile().getPrimaryOrganization();
    }

    @Test
    public void testSubmitProtocolRegistration() throws IOException {
        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin());
        BrowseRegistrationsPage browseRegistrationsPage = openProtocolRegistrationsPage(homePage);
        InvestigatorRegistrationPage registrationPage = openRegistration(browseRegistrationsPage);

        completeForm1572Tab(registrationPage);
        completeFinancialDisclosureTab(registrationPage);
        completeHumanResearchCertifiateTab(registrationPage);
        completeAdditionalAttachmentsTab(registrationPage);
        submitRegistration(registrationPage);
    }

    private BrowseRegistrationsPage openProtocolRegistrationsPage(final HomePage homePage) {
        return new TimedAction<BrowseRegistrationsPage>("Click Protocol Registrations Menu Item") {
            @Override
            public BrowseRegistrationsPage perform() {
                return homePage.getInvestigatorMenu().clickProtocolRegistrations();
            }
        }.time();
    }

    private InvestigatorRegistrationPage openRegistration(BrowseRegistrationsPage browseRegistrationsPage) {
        final RegistrationListing listing = browseRegistrationsPage.getHelper().getRegistrationListing(
                dataSet.getInvestigatorRegistration());
        return new TimedAction<InvestigatorRegistrationPage>("Open Registration") {
            @Override
            public InvestigatorRegistrationPage perform() throws IOException {
                return listing.clickRegistrationLink().getPage();
            }
        }.time();
    }

    private void completeForm1572Tab(InvestigatorRegistrationPage registrationPage) {
        ProtocolForm1572Tab form1572Tab = openForm1572Tab(registrationPage);
        selectClinicalLab(form1572Tab);
        selectIrb(form1572Tab);
        selectPracticeSite(form1572Tab);
    }

    private ProtocolForm1572Tab openForm1572Tab(final InvestigatorRegistrationPage registrationPage) {
        return new TimedAction<ProtocolForm1572Tab>("Open Form 1572 Tab") {
            @Override
            public ProtocolForm1572Tab perform() throws IOException {
                return registrationPage.clickForm1572Tab();
            }
        }.time();
    }

    private void selectClinicalLab(final ProtocolForm1572Tab form1572Tab) {
        new TimedAction<Void>("Select Clinical Lab") {
            @Override
            public Void perform() throws IOException {
                form1572Tab.getClinicalLabSection().getListings().get(0).select();
                return null;
            }
        }.time();
    }

    private void selectIrb(final ProtocolForm1572Tab form1572Tab) {
        new TimedAction<Void>("Select IRB") {
            @Override
            public Void perform() throws IOException {
                form1572Tab.getIrbSection().getListings().get(0).select();
                return null;
            }
        }.time();
    }

    private void selectPracticeSite(final ProtocolForm1572Tab form1572Tab) {
        new TimedAction<Void>("Select Practice Site") {
            @Override
            public Void perform() throws IOException {
                form1572Tab.getPracticeSiteSection().getListings().get(0).select();
                return null;
            }
        }.time();
    }

    private void completeFinancialDisclosureTab(InvestigatorRegistrationPage registrationPage) {
        FinancialDisclosureTab financialDisclosureTab = openFinancialDisclosureTab(registrationPage);
        answerAllFinancialDisclosureQuestions(financialDisclosureTab);
        attachFileToFinancialDisclosure(financialDisclosureTab);
    }

    private FinancialDisclosureTab openFinancialDisclosureTab(final InvestigatorRegistrationPage registrationPage) {
        return new TimedAction<FinancialDisclosureTab>("Open Financial Disclosure Tab") {
            @Override
            public FinancialDisclosureTab perform() {
                return registrationPage.clickFinancialDisclosureTab();
            }
        }.time();
    }

    private void answerAllFinancialDisclosureQuestions(FinancialDisclosureTab financialDisclosureTab) {
        for (Question question : EnumSet.range(Question.Q1_MONETARY_GAIN, Question.Q4_EQUITY_IN_SPONSOR)) {
            answerFinancialDisclosureQuestion(financialDisclosureTab, question);
        }
    }

    private void answerFinancialDisclosureQuestion(final FinancialDisclosureTab financialDisclosureTab,
            final Question question) {
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
                financialDisclosureTab.getSupportingDocumentsTag().getHelper()
                        .uploadFile(TestFileUtils.createTemporaryFile());
                return null;
            }
        }.time();
    }

    private void completeHumanResearchCertifiateTab(InvestigatorRegistrationPage registrationPage) {
        HumanResearchCertificateTab certificateTab = openHumanResearchCertificateTab(registrationPage);
        selectCertificate(certificateTab);
    }

    private HumanResearchCertificateTab openHumanResearchCertificateTab(
            final InvestigatorRegistrationPage registrationPage) {
        return new TimedAction<HumanResearchCertificateTab>("Open Human Research Certificate Tab") {
            @Override
            public HumanResearchCertificateTab perform() throws IOException {
                return registrationPage.clickHumanResearchCertificateTab();
            }
        }.time();
    }

    private void selectCertificate(final HumanResearchCertificateTab certificateTab) {
        new TimedAction<Void>("Select Certificate") {
            @Override
            public Void perform() throws IOException {
                certificateTab.getCertificates().get(0).select();
                return null;
            }
        }.time();
    }

    private void completeAdditionalAttachmentsTab(InvestigatorRegistrationPage registrationPage) throws IOException {
        ProtocolAdditionalAttachmentsTab additionalAttachmentsTab = openAdditionalAttachmentsTab(registrationPage);
        UploadFileDialog uploadFileDialog = clickAddNewAttachment(additionalAttachmentsTab);
        saveAttachment(uploadFileDialog);
    }

    private ProtocolAdditionalAttachmentsTab openAdditionalAttachmentsTab(
            final InvestigatorRegistrationPage registrationPage) {
        return new TimedAction<ProtocolAdditionalAttachmentsTab>("Open Additional Attachments Tab") {
            @Override
            public ProtocolAdditionalAttachmentsTab perform() throws IOException {
                return registrationPage.clickAdditionalAttachmentsTab();
            }
        }.time();
    }

    private UploadFileDialog clickAddNewAttachment(final ProtocolAdditionalAttachmentsTab additionalAttachmentsTab) {
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

    private void submitRegistration(InvestigatorRegistrationPage registrationPage) {
        RegistrationOverviewTab overviewTab = openOverviewTab(registrationPage);
        SignAndSubmitRegistrationDialog signDialog = clickSubmitRegistration(overviewTab);
        signRegistration(signDialog);
        assertEquals(RegistrationStatus.SUBMITTED.getDisplay(), overviewTab.getStatus());
    }

    private RegistrationOverviewTab openOverviewTab(final InvestigatorRegistrationPage registrationPage) {
        return new TimedAction<RegistrationOverviewTab>("Open Overview Tab") {
            @Override
            public RegistrationOverviewTab perform() throws IOException {
                return registrationPage.clickOverviewTab();
            }
        }.time();
    }

    private SignAndSubmitRegistrationDialog clickSubmitRegistration(final RegistrationOverviewTab overviewTab) {
        return new TimedAction<SignAndSubmitRegistrationDialog>("Click Registration Submission Button",
                MAX_NES_RESPONSE_TIME_SECONDS) {
            @Override
            public SignAndSubmitRegistrationDialog perform() throws IOException {
                return (SignAndSubmitRegistrationDialog) overviewTab.clickSubmitRegistration();
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
        dataSet.reload();
        delete(dataSet, dataSet.getInvestigatorRegistration());
        delete(dataSet, dataSet.getInvestigatorRegistration().getProtocol());
        deleteInvestigator(dataSet, dataSet.getInvestigator());
        delete(dataSet, primaryOrganization);
        if (primaryOrganization != null) {
            delete(dataSet, primaryOrganization.getOrganization());
        }
    }

}
