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

import com.google.inject.Inject;

import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.InvestigatorProfilePage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolAdditionalAttachmentsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignedDocumentsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SignAndSubmitRegistrationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.AdditionalAttachmentsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.RegistrationReviewCommentDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab;
import gov.nih.nci.firebird.selenium2.tests.investigator.registration.common.AbstractAdditionalAttachmentTest;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

public class ProtocolAdditionalAttachmentsTabTest extends AbstractAdditionalAttachmentTest {

    @Inject
    private DataSetBuilder builder;
    private DataSet dataSet;
    private InvestigatorRegistration registration;

    @Override
    protected void setUpData() {
        registration = builder.createRegistration().complete().withStatus(RegistrationStatus.IN_PROGRESS).get();
        builder.createSponsor();
        dataSet = builder.build();
    }

    @Override
    protected ProtocolAdditionalAttachmentsTab navigateToAdditionalAttachmentsTab() {
        BrowseRegistrationsPage browseRegistrationsPage = openHomePage(dataSet.getInvestigatorLogin())
                .getInvestigatorMenu().clickProtocolRegistrations();
        RegistrationOverviewTab overviewTab = browseRegistrationsPage.getHelper()
                .getRegistrationListing(registration).clickRegistrationLink();
        return overviewTab.getPage().clickAdditionalAttachmentsTab();
    }

    @Override
    protected InvestigatorProfilePage navigateToProfile(AdditionalAttachmentsTab additionalAttachmentsTab) {
        return additionalAttachmentsTab.getPage().getInvestigatorMenu().clickProfile().getPage();
    }

    @Override
    protected ProtocolAdditionalAttachmentsTab submitForReview(AdditionalAttachmentsTab additionalAttachmentsTab) {
        RegistrationOverviewTab overviewTab = ((ProtocolAdditionalAttachmentsTab) additionalAttachmentsTab).getPage()
                .clickOverviewTab();
        SignAndSubmitRegistrationDialog signingDialog = (SignAndSubmitRegistrationDialog) overviewTab
                .clickSubmitRegistration();
        SignedDocumentsDialog signedDocumentsDialog = signingDialog.getHelper().signAndSubmit(
                dataSet.getInvestigatorLogin());
        overviewTab = (RegistrationOverviewTab) signedDocumentsDialog.clickClose();
        assertEquals(RegistrationStatus.SUBMITTED.getDisplay(), overviewTab.getStatus());
        getEmailChecker().assertEmailCount(1);
        return overviewTab.getPage().clickAdditionalAttachmentsTab();
    }

    @Override
    protected ProtocolAdditionalAttachmentsTab verifyFormStatusOnOverviewTab(
            AdditionalAttachmentsTab additionalAttachmentsTab) {
        RegistrationOverviewTab overviewTab = ((ProtocolAdditionalAttachmentsTab) additionalAttachmentsTab).getPage()
                .clickOverviewTab();
        String actualStatus = overviewTab.getHelper()
                .getFormListing(registration.getAdditionalAttachmentsForm()).getFormStatus();
        assertEquals(FormStatus.NOT_APPLICABLE.getDisplay(), actualStatus);
        return overviewTab.getPage().clickAdditionalAttachmentsTab();
    }

    @Override
    protected ProtocolAdditionalAttachmentsTab returnRegistration(AdditionalAttachmentsTab additionalAttachmentsTab) {
        ReviewRegistrationTab overviewTab = openHomePage(dataSet.getSponsorLogin()).getHelper()
                .openSubmittedProtocolRegistrationTask(registration);
        overviewTab = reviewAllForms(overviewTab);
        overviewTab.getHelper().rejectAllForms(registration);
        RegistrationReviewCommentDialog reviewCommentDialog = (RegistrationReviewCommentDialog) overviewTab.clickCompleteReview();
        reviewCommentDialog.clickSave().clickConfirm().clickClose();
        getEmailChecker().assertEmailCount(2);
        overviewTab.getPage().clickSignOut();
        return navigateToAdditionalAttachmentsTab();
    }

    public ReviewRegistrationTab reviewAllForms(ReviewRegistrationTab overviewTab) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (form.isReviewRequired()) {
                overviewTab.getHelper().reviewForm(overviewTab.getHelper().getMatchingListing(form));
            }
        }
        return overviewTab;
    }

}