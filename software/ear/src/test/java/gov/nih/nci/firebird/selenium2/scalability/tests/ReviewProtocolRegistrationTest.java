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

import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.support.AbstractLoadableComponent;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewCompletionDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewHumanResearchCertificatesDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab.RegistrationFormListing;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.FormReviewCommentDialog;
import gov.nih.nci.firebird.test.data.DataSet;

import java.io.IOException;

import org.junit.Test;

public class ReviewProtocolRegistrationTest extends AbstractScalabilityTest {

    private DataSet dataSet;
    private InvestigatorRegistration registration;
    private PrimaryOrganization primaryOrganization;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        FirebirdUser investigator = getBuilder().createInvestigatorWithCompleteProfile().get();
        Protocol protocol = getBuilder().createProtocol().withoutLeadOrganization().get();
        registration = getBuilder().createRegistration(protocol, investigator).withStatus(RegistrationStatus.SUBMITTED)
                .complete().get();
        getBuilder().createSponsor().forSponsors(registration.getProtocol().getSponsor());
        dataSet = getBuilder().build();
        primaryOrganization = dataSet.getInvestigatorProfile().getPrimaryOrganization();
    }

    @Test
    public void testReviewProtocolRegistration() throws IOException {
        final HomePage homePage = openHomePage(dataSet.getSponsorLogin());
        ReviewRegistrationTab reviewTab = openRegistration(homePage);
        downloadAllForms(reviewTab);
        rejectAllForms(reviewTab);
        acceptAllForms(reviewTab);
        ReviewCompletionDialog completionDialog = completeReview(reviewTab);
        approveRegistration(completionDialog);
        assertEquals("Status: " + RegistrationStatus.APPROVED.getDisplay(), reviewTab.getStatusHeader());
    }

    private ReviewRegistrationTab openRegistration(final HomePage homePage) {
        return new TimedAction<ReviewRegistrationTab>("Open Submitted Protocol Registration from Task List") {
            @Override
            public ReviewRegistrationTab perform() {
                return homePage.getHelper().openSubmittedProtocolRegistrationTask(registration);
            }
        }.time();
    }

    private void downloadAllForms(ReviewRegistrationTab reviewTab) throws IOException {
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            if (form.isReviewRequired()) {
                downloadForm(reviewTab, form);
            }
        }
    }

    private void downloadForm(ReviewRegistrationTab reviewTab, AbstractRegistrationForm form) throws IOException {
        final RegistrationFormListing listing = reviewTab.getHelper().getMatchingListing(form);
        new TimedAction<Void>("Download " + form.getFormType().getName()) {
            @Override
            public Void perform() throws IOException {
                AbstractLoadableComponent<?> result = listing.clickFormDownload();
                if (result instanceof ReviewHumanResearchCertificatesDialog) {
                    ReviewHumanResearchCertificatesDialog dialog = (ReviewHumanResearchCertificatesDialog) result;
                    dialog.getListings().get(0).clickDownloadLink();
                    dialog.clickClose();
                }
                return null;
            }
        }.time();
    }

    private void rejectAllForms(ReviewRegistrationTab reviewTab) {
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            if (form.isReviewRequired()) {
                rejectForm(reviewTab, form);
            }
        }
    }

    private void rejectForm(final ReviewRegistrationTab reviewTab, final AbstractRegistrationForm form) {
        final FormReviewCommentDialog commentsDialog = new TimedAction<FormReviewCommentDialog>("Reject " + form.getFormType().getName()) {
            @Override
            public FormReviewCommentDialog perform() throws IOException {
                return reviewTab.getHelper().getMatchingListing(form).clickRejectRadioButton();
            }
        }.time();
        commentsDialog.typeComments("Rejected");
        new TimedAction<Void>("Save Rejection Comments for " + form.getFormType().getName()) {
            @Override
            public Void perform() throws IOException {
                commentsDialog.clickSave();
                return null;
            }
        }.time();
    }

    private void acceptAllForms(ReviewRegistrationTab reviewTab) {
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            if (form.isReviewRequired()) {
                acceptForm(reviewTab, form);
            }
        }
    }

    private void acceptForm(final ReviewRegistrationTab reviewTab, final AbstractRegistrationForm form) {
        new TimedAction<Void>("Accept " + form.getFormType().getName()) {
            @Override
            public Void perform() throws IOException {
                reviewTab.getHelper().getMatchingListing(form).clickAcceptRadioButton();
                return null;
            }
        }.time();
    }

    private ReviewCompletionDialog completeReview(final ReviewRegistrationTab reviewTab) {
        return new TimedAction<ReviewCompletionDialog>("Complete Review") {
            public ReviewCompletionDialog perform() {
                return (ReviewCompletionDialog) reviewTab.clickCompleteReview();
            }
        }.time();
    }

    private void approveRegistration(final ReviewCompletionDialog completionDialog) {
        new TimedAction<Void>("Approve Registration") {
            @Override
            public Void perform() {
                completionDialog.clickApproveRegistration();
                return null;
            }
        }.time();
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
        delete(dataSet, dataSet.getSponsor());
        if (dataSet.getSponsor() != null) {
            delete(dataSet, dataSet.getSponsor().getPerson());
        }
    }

}
