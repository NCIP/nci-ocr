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
import gov.nih.nci.firebird.data.AbstractProtocolRegistrationForm;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.AbstractFormTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab.Question;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTabHelper;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.InvestigatorRegistrationFormTablesTag.FormListing;
import gov.nih.nci.firebird.selenium2.pages.registration.common.FormRejectionCommentsDialog;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import org.junit.Before;
import org.junit.Test;

public class RegistrationTest extends AbstractFirebirdWebDriverTest {

    private static final String SPONSOR_COMMENTS = "Sponsor Comments";
    private RegistrationOverviewTab overviewTab;
    private DataSet dataSet;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testRejectionCommentsLinks() {
        setUpRejectedDataSet();
        overviewTab = openRegistration(dataSet.getInvestigatorRegistration());
        overviewTab.getHelper().checkCommentsForText(SPONSOR_COMMENTS);
        verifyFormCommentPopup(dataSet.getInvestigatorRegistration());
        verifyFormComments(dataSet.getInvestigatorRegistration());
    }

    private void setUpRejectedDataSet() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        InvestigatorRegistration registration = builder.createRegistration().withStatus(RegistrationStatus.RETURNED)
                .withAllFormsRejected().get();
        registration.setSponsorComments(SPONSOR_COMMENTS);
        dataSet = builder.build();
    }

    private RegistrationOverviewTab openRegistration(InvestigatorRegistration registration) {
        return RegistrationOverviewTabHelper.openRegistration(openHomePage(dataSet.getInvestigatorLogin()),
                registration);
    }

    private void verifyFormCommentPopup(InvestigatorRegistration registration) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (form.isReviewRequired()) {
                FormListing listing = overviewTab.getHelper().getFormListing(form);
                FormRejectionCommentsDialog dialog = listing.clickComments();
                String expectedTitle = getPropertyText("sponsor.protocol.forms.reject.comment.header", form
                        .getFormType().getDescription());
                assertEquals(expectedTitle, dialog.getTitle());
                assertEquals(form.getComments(), dialog.getComments());
                dialog.clickClose();
            }
        }
    }

    private void verifyFormComments(AbstractProtocolRegistration registration) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            AbstractFormTab<?> formTab = (AbstractFormTab<?>) overviewTab.getHelper().getFormListing(form).click();
            if (form.isReviewRequired()) {
                assertTrue(formTab.areCommentsPresent());
                assertEquals(form.getComments(), formTab.getComments());
            } else {
                assertFalse(formTab.areCommentsPresent());
            }
            formTab.getPage().clickOverviewTab();
        }
    }

    @Test
    public void testFormStatusUpdatesAutomatically() {
        setUpNotStartedDataSet();
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        overviewTab = openRegistration(registration);
        overviewTab.getHelper().assertHasStatus(RegistrationStatus.IN_PROGRESS);
        verifyFormStatus(registration.getForm1572(), FormStatus.NOT_STARTED);
        verifyFormStatus(registration.getFinancialDisclosure(), FormStatus.NOT_STARTED);
        verifyFormStatus(registration.getHumanResearchCertificateForm(), FormStatus.NOT_STARTED);
        verifyFormStatus(registration.getCurriculumVitaeForm(), FormStatus.COMPLETED);
        FinancialDisclosureTab financialDisclosureTab = overviewTab.getPage().clickFinancialDisclosureTab();
        financialDisclosureTab.answerQuestion(Question.Q5_NO_FINANCIAL_INTEREST, true);
        overviewTab = financialDisclosureTab.getPage().clickOverviewTab();
        verifyFormStatus(registration.getFinancialDisclosure(), FormStatus.COMPLETED);
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        overviewTab = form1572Tab.getPage().clickOverviewTab();
        verifyFormStatus(registration.getForm1572(), FormStatus.IN_PROGRESS);
    }

    private void setUpNotStartedDataSet() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createRegistration().withStatus(RegistrationStatus.NOT_STARTED)
                .withInvitationStatus(InvitationStatus.NO_RESPONSE);
        dataSet = builder.build();
    }

    private void verifyFormStatus(AbstractProtocolRegistrationForm form, FormStatus status) {
        assertEquals(status.getDisplay(), overviewTab.getHelper().getFormListing(form).getFormStatus());
    }
}
