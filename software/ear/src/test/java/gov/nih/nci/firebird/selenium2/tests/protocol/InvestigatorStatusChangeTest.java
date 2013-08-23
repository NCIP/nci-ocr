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
package gov.nih.nci.firebird.selenium2.tests.protocol;

import static gov.nih.nci.firebird.data.RegistrationStatus.*;
import static gov.nih.nci.firebird.selenium2.pages.util.ProtocolRegistrationUtils.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab.CertificateListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.InvestigatorDeactivateDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.InvestigatorReactivateDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInformationTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInvestigatorsTab.InvestigatorRegistrationListing;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;

public class InvestigatorStatusChangeTest extends AbstractFirebirdWebDriverTest {

    private DataSet dataSet;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testSponsorDelegateCantDeactivateInvestigator() throws IOException {
        createDataSet(IN_PROGRESS, true);
        ProtocolInvestigatorsTab investigatorsTab = navigateToInvestigatorsTab(dataSet.getSponsorLogin());
        List<InvestigatorRegistrationListing> investigators = investigatorsTab.getListings();
        assertEquals(1, investigators.size());
        String expectedRegistrationStatus = getRegistrationStatusWithTimeStamp(RegistrationStatus.IN_PROGRESS,
                dataSet.getInvestigatorRegistration());
        assertEquals(expectedRegistrationStatus, investigators.get(0).getRegistrationStatus());
        assertFalse("Sponsor Delegate should not be able to deactivate investigators", investigators.get(0)
                .hasDeactivateButton());
        assertFalse("Sponsor Delegate should not be able to reactivate investigators", investigators.get(0)
                .hasReactivateButton());
    }

    private void createDataSet(RegistrationStatus status, boolean asDelegate) {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        if (asDelegate) {
            builder.createSponsor().asDelegate();
        } else {
            builder.createSponsor();
        }
        FirebirdUser investigator = builder.createInvestigatorWithCompleteProfile().get();
        builder.createRegistration(investigator).complete().withStatus(status);
        dataSet = builder.build();
    }

    private ProtocolInvestigatorsTab navigateToInvestigatorsTab(LoginAccount loginAccount) {
        Protocol protocol = dataSet.getInvestigatorRegistration().getProtocol();
        ProtocolInformationTab informationTab = openHomePage(loginAccount).getHelper().goToSponsorProtocol(protocol);
        return informationTab.getPage().clickInvestigatorsTab();
    }

    @Test
    public void testDeactivateInvestigator_Sponsor() throws IOException {
        createDataSet(RETURNED, false);
        String rejectionComments = ValueGenerator.getUniqueString();
        dataSet.getInvestigatorRegistration().setSponsorComments(rejectionComments);
        dataSet.update(dataSet.getInvestigatorRegistration());
        final String comments = "These are comments for the deactivation";
        ProtocolInvestigatorsTab investigatorsTab = navigateToInvestigatorsTab(dataSet.getSponsorLogin());
        List<InvestigatorRegistrationListing> investigators = investigatorsTab.getListings();
        assertEquals(1, investigators.size());
        String expectedRegistrationStatus = getRegistrationStatusWithTimeStamp(RegistrationStatus.RETURNED,
                dataSet.getInvestigatorRegistration());
        assertEquals(expectedRegistrationStatus, investigators.get(0).getRegistrationStatus());
        assertTrue(investigators.get(0).hasDeactivateButton());

        final InvestigatorDeactivateDialog dialog = investigators.get(0).clickDeactivate();
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure(
                "sponsor.protocol.investigator.deactivate.comments.required");
        expectedFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                dialog.clickSave();
            }
        });

        dialog.typeComments(comments);
        dialog.clickSave();

        investigators = investigatorsTab.getListings();
        assertEquals(1, investigators.size());
        dataSet.reload();
        expectedRegistrationStatus = getRegistrationStatusWithTimeStamp(RegistrationStatus.INACTIVE,
                dataSet.getInvestigatorRegistration());
        assertEquals(expectedRegistrationStatus, investigators.get(0).getRegistrationStatus());
        assertFalse(investigators.get(0).hasDeactivateButton());

        ReviewRegistrationTab reviewTab = investigatorsTab.getHelper().clickInvestigator(
                dataSet.getInvestigatorRegistration());
        reviewTab.getHelper().clickFormDownload(dataSet.getInvestigatorRegistration().getForm1572());

        // Check the Investigator's Registration
        reviewTab.getPage().clickSignOut();
        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin());
        BrowseRegistrationsPage browsePage = homePage.getInvestigatorMenu().clickProtocolRegistrations();
        RegistrationOverviewTab overviewTab = browsePage.getHelper().clickRegistrationLink(
                dataSet.getInvestigatorRegistration());
        overviewTab.getHelper().checkCommentsForText(rejectionComments);
        assertTrue(overviewTab.getHelper().isLockedForInvestigator());
        overviewTab.getHelper().validateFormsLocked(FormStatus.INACTIVE, dataSet.getInvestigatorRegistration());
    }

    @Test
    public void testSponsorDelegateCantReactivateInvestigator() {
        createDataSet(INACTIVE, true);
        ProtocolInvestigatorsTab investigatorsTab = navigateToInvestigatorsTab(dataSet.getSponsorLogin());
        List<InvestigatorRegistrationListing> investigators = investigatorsTab.getListings();
        assertEquals(1, investigators.size());
        String expectedRegistrationStatus = getRegistrationStatusWithTimeStamp(RegistrationStatus.INACTIVE,
                dataSet.getInvestigatorRegistration());
        assertEquals(expectedRegistrationStatus, investigators.get(0).getRegistrationStatus());
        assertFalse("Sponsor Delegate should not be able to deactivate investigators", investigators.get(0)
                .hasDeactivateButton());
        assertFalse("Sponsor Delegate should not be able to reactivate investigators", investigators.get(0)
                .hasReactivateButton());
    }

    @Test
    public void testReactivateInvestigator_Sponsor() {
        createDataSet(INACTIVE, false);
        final String comments = "These are comments";
        ProtocolInvestigatorsTab investigatorsTab = navigateToInvestigatorsTab(dataSet.getSponsorLogin());
        List<InvestigatorRegistrationListing> investigators = investigatorsTab.getListings();
        assertEquals(1, investigators.size());
        String expectedRegistrationStatus = getRegistrationStatusWithTimeStamp(RegistrationStatus.INACTIVE,
                dataSet.getInvestigatorRegistration());
        assertEquals(expectedRegistrationStatus, investigators.get(0).getRegistrationStatus());
        assertFalse(investigators.get(0).hasDeactivateButton());

        InvestigatorReactivateDialog reactivateDialog = investigators.get(0).clickReactivate();
        reactivateDialog.clickSave();

        investigators = investigatorsTab.getListings();
        assertEquals(1, investigators.size());
        dataSet.reload();
        expectedRegistrationStatus = getRegistrationStatusWithTimeStamp(RegistrationStatus.IN_PROGRESS,
                dataSet.getInvestigatorRegistration());
        assertEquals(expectedRegistrationStatus, investigators.get(0).getRegistrationStatus());
        assertTrue(investigators.get(0).hasDeactivateButton());
        assertTrue(investigators.get(0).getInvitationStatus()
                .contains(getPropertyText(InvitationStatus.REACTIVATED.getKey())));

        InvestigatorDeactivateDialog deactivateDialog = investigators.get(0).clickDeactivate();
        deactivateDialog.typeComments(comments);
        deactivateDialog.clickSave();

        investigators = investigatorsTab.getListings();
        reactivateDialog = investigators.get(0).clickReactivate();
        reactivateDialog.typeComments(comments);
        reactivateDialog.clickSave();
    }

    @Test
    public void testDeactivateAndReactivateInvestigator_SubmittedRegistration() {
        ProtocolInvestigatorsTab investigatorsTab = deactivateAndReactivateInvestigator(SUBMITTED);
        InvestigatorRegistrationListing listing = Iterables.getOnlyElement(investigatorsTab.getListings());
        assertTrue(listing.getInvitationStatus().startsWith(getPropertyText(InvitationStatus.REACTIVATED.getKey())));
        assertTrue(listing.getRegistrationStatus().startsWith(IN_PROGRESS.getDisplay()));
        checkInvestigatorsSelectedCertificatesRetained();
    }

    private ProtocolInvestigatorsTab deactivateAndReactivateInvestigator(RegistrationStatus startingRegistrationStatus) {
        createDataSet(startingRegistrationStatus, false);
        ProtocolInvestigatorsTab investigatorsTab = navigateToInvestigatorsTab(dataSet.getSponsorLogin());
        List<InvestigatorRegistrationListing> investigators = investigatorsTab.getListings();
        String expectedRegistrationStatus = getRegistrationStatusWithTimeStamp(startingRegistrationStatus,
                dataSet.getInvestigatorRegistration());
        assertEquals(expectedRegistrationStatus, investigators.get(0).getRegistrationStatus());

        InvestigatorDeactivateDialog deactivateDialog = investigators.get(0).clickDeactivate();
        deactivateDialog.typeComments(ValueGenerator.getUniqueString());
        deactivateDialog.clickSave();

        investigators = investigatorsTab.getListings();
        InvestigatorReactivateDialog reactivateDialog = investigators.get(0).clickReactivate();
        reactivateDialog.clickSave();
        return investigatorsTab;
    }

    private void checkInvestigatorsSelectedCertificatesRetained() {
        RegistrationOverviewTab overviewTab = openHomePage(dataSet.getInvestigatorLogin()).getHelper()
                .openReactivatedRegistrationTask(dataSet.getInvestigatorRegistration());
        overviewTab.getHelper().assertHasStatus(IN_PROGRESS);
        HumanResearchCertificateTab humanResearchTab = overviewTab.getPage().clickHumanResearchCertificateTab();
        assertEquals(1, dataSet.getInvestigatorRegistration().getHumanResearchCertificateForm().getCertificates()
                .size());
        CertificateListing certificate = Iterables.getOnlyElement(humanResearchTab.getCertificates());
        assertTrue(certificate.isSelected());
    }

    @Test
    public void testDeactivateAndReactivateInvestigator_NoInvitationResponse() {
        ProtocolInvestigatorsTab investigatorsTab = deactivateAndReactivateInvestigator(NOT_STARTED);
        InvestigatorRegistrationListing listing = Iterables.getOnlyElement(investigatorsTab.getListings());
        assertTrue(listing.getInvitationStatus().startsWith(getPropertyText(InvitationStatus.NO_RESPONSE.getKey())));
        assertEquals(NOT_STARTED.getDisplay(), listing.getRegistrationStatus());
    }

}
