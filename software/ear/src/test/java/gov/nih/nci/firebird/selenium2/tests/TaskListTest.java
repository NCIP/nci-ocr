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
package gov.nih.nci.firebird.selenium2.tests;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.ManagedInvestigator;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.components.DataTable.EntriesToDisplay;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.InvestigatorRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.InvitationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.settings.CoordinatorRequestDialog;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Tests the task list on an authenticated user's home page.
 */
public class TaskListTest extends AbstractFirebirdWebDriverTest {

    private HomePage homePage;
    private InvestigatorRegistration noResponseRegistration;
    private InvestigatorRegistration inProgressRegistration;
    private InvestigatorRegistration returnedRegistration;
    private InvestigatorRegistration protocolUpdatedRegistration;
    private InvestigatorRegistration reactivatedRegistration;
    private SubInvestigatorRegistration noResponseSubInvestigatorRegistration;
    private SubInvestigatorRegistration inProgressSubInvestigatorRegistration;
    private SubInvestigatorRegistration returnedSubInvestigatorRegistration;
    private SubInvestigatorRegistration protocolUpdatedSubInvestigatorRegistration;
    private SubInvestigatorRegistration reactivatedSubInvestigatorRegistration;
    private DataSet dataSet;
    private InvestigatorRegistration completeRegistration;
    private InvestigatorRegistration submittedRegistration;
    private InvestigatorRegistration inReviewRegistration;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setUpData();
    }

    private void setUpData() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        FirebirdUser investigator = builder.createInvestigator().get();
        builder.createSponsor();
        FirebirdUser unApprovedInvestigator = builder.createInvestigator().get();
        builder.createCoordinator().withApprovedMangedInvestigator(investigator)
                .withUnapprovedMangedInvestigator(unApprovedInvestigator);
        noResponseRegistration = builder.createRegistration(investigator)
                .withInvitationStatus(InvitationStatus.NO_RESPONSE).get();
        inProgressRegistration = builder.createRegistration(investigator).withStatus(RegistrationStatus.IN_PROGRESS)
                .get();
        returnedRegistration = builder.createRegistration(investigator).withStatus(RegistrationStatus.RETURNED).get();
        protocolUpdatedRegistration = builder.createRegistration(investigator)
                .withStatus(RegistrationStatus.PROTOCOL_UPDATED).get();
        reactivatedRegistration = builder.createRegistration(investigator)
                .withInvitationStatus(InvitationStatus.REACTIVATED).get();
        completeRegistration = builder.createRegistration(investigator).withStatus(RegistrationStatus.COMPLETED).get();
        submittedRegistration = builder.createRegistration(investigator).withStatus(RegistrationStatus.SUBMITTED).get();
        inReviewRegistration = builder.createRegistration(investigator).withStatus(RegistrationStatus.IN_REVIEW).get();

        noResponseSubInvestigatorRegistration = builder
                .createSubinvestigatorRegistration(inProgressRegistration.getProfile(), noResponseRegistration)
                .withInvitationStatus(InvitationStatus.NO_RESPONSE).get();
        inProgressSubInvestigatorRegistration = builder
                .createSubinvestigatorRegistration(inProgressRegistration.getProfile(), inProgressRegistration)
                .withStatus(RegistrationStatus.IN_PROGRESS).get();
        returnedSubInvestigatorRegistration = builder
                .createSubinvestigatorRegistration(inProgressRegistration.getProfile(), returnedRegistration)
                .withStatus(RegistrationStatus.RETURNED).get();
        protocolUpdatedSubInvestigatorRegistration = builder
                .createSubinvestigatorRegistration(inProgressRegistration.getProfile(), protocolUpdatedRegistration)
                .withStatus(RegistrationStatus.PROTOCOL_UPDATED).get();
        reactivatedSubInvestigatorRegistration = builder
                .createSubinvestigatorRegistration(inProgressRegistration.getProfile(), reactivatedRegistration)
                .withInvitationStatus(InvitationStatus.REACTIVATED).get();

        dataSet = builder.build();
    }

    @Test
    public void testTaskListTaskSelection() {
        homePage = openHomePage(dataSet.getInvestigatorLogin());

        homePage.getHelper().checkForInvestigatorInvitationTask(noResponseRegistration);
        homePage.getHelper().checkForRegistrationInProgressTask(inProgressRegistration);
        homePage.getHelper().checkForReturnedTask(returnedRegistration);
        homePage.getHelper().checkForUpdatedProtocolTask(protocolUpdatedRegistration);
        homePage.selectNumberOfTasksToDisplay(EntriesToDisplay.HUNDRED);
        homePage.getHelper().checkForReactivatedRegistrationTask(reactivatedRegistration);

        homePage.getHelper().checkForSubInvestigatorInvitationTask(noResponseSubInvestigatorRegistration);
        homePage.getHelper().checkForRegistrationInProgressTask(noResponseSubInvestigatorRegistration);
        homePage.getHelper().checkForReturnedTask(returnedSubInvestigatorRegistration);
        homePage.getHelper().checkForUpdatedProtocolTask(protocolUpdatedSubInvestigatorRegistration);
        homePage.getHelper().checkForReactivatedRegistrationTask(reactivatedSubInvestigatorRegistration);

        InvitationDialog dialog = homePage.getHelper().openInvestigatorInvitationTask(noResponseRegistration);
        InvestigatorRegistrationPage page = dialog.clickBeginRegistration().getPage();
        page.getHelper().checkRegistrationPageHeader(noResponseRegistration);
        homePage = page.clickHome();
        homePage.getHelper().checkForRegistrationInProgressTask(noResponseRegistration);

        dialog = homePage.getHelper().openSubInvestigatorInvitationTask(noResponseSubInvestigatorRegistration);
        page = dialog.clickBeginRegistration().getPage();
        page.getHelper().checkRegistrationPageHeader(noResponseSubInvestigatorRegistration);
        homePage = page.clickHome();
        homePage.getHelper().checkForRegistrationInProgressTask(noResponseSubInvestigatorRegistration);

        checkRegistrationInProgressTaskLink(inProgressRegistration);
        checkRegistrationInProgressTaskLink(inProgressSubInvestigatorRegistration);
        checkRegistrationInProgressTaskLink(returnedRegistration);
        checkRegistrationInProgressTaskLink(returnedSubInvestigatorRegistration);
        checkRegistrationInProgressTaskLink(protocolUpdatedRegistration);
        checkRegistrationInProgressTaskLink(protocolUpdatedSubInvestigatorRegistration);
        homePage.selectNumberOfTasksToDisplay(EntriesToDisplay.HUNDRED);
        checkRegistrationInProgressTaskLink(reactivatedRegistration);
        homePage.selectNumberOfTasksToDisplay(EntriesToDisplay.HUNDRED);
        checkRegistrationInProgressTaskLink(reactivatedSubInvestigatorRegistration);
    }

    @Test
    public void testTaskList_InvestigatorRegistrationReadyForSigning() {
        homePage = openHomePage(dataSet.getInvestigatorLogin());
        homePage.selectNumberOfTasksToDisplay(EntriesToDisplay.HUNDRED);
        homePage.getHelper().checkForRegistrationReadyForSigningTask(completeRegistration);
        checkRegistrationInProgressTaskLink(completeRegistration);
    }

    private void checkRegistrationInProgressTaskLink(AbstractProtocolRegistration registration) {
        InvestigatorRegistrationPage page = homePage.getHelper().openInProgressTask(registration).getPage();
        page.getHelper().checkRegistrationPageHeader(registration);
        homePage = page.clickHome();
    }

    @Test
    public void testSponsorTasks() {
        homePage = openHomePage(dataSet.getSponsorLogin());
        homePage.getHelper().checkForSponsorRegistrationSubmittedTask(submittedRegistration);
        homePage.getHelper().checkForSponsorRegistrationInReviewTask(inReviewRegistration);

        ReviewRegistrationTab reviewTab = homePage.getHelper().openSubmittedProtocolRegistrationTask(
                submittedRegistration);
        reviewTab.getHelper().checkRegistrationPageHeader(submittedRegistration);
        homePage = reviewTab.getPage().clickHome();

        reviewTab = homePage.getHelper().openSubmittedProtocolRegistrationTask(inReviewRegistration);
        reviewTab.getHelper().checkRegistrationPageHeader(inReviewRegistration);
    }

    @Test
    public void testCoordinatorRequestTasks_Cancel() {
        homePage = openHomePage(dataSet.getInvestigatorLogins().get(1));
        ManagedInvestigator managedInvestigator = getManagedInvestigator(false);
        homePage.getHelper().checkForCoordinatorRequestTask(managedInvestigator);
        CoordinatorRequestDialog coordinatorRequestDialog = homePage.getHelper().openCoordinatorRequestTask(
                managedInvestigator);
        homePage = coordinatorRequestDialog.clickCancel();
        homePage.getHelper().checkForCoordinatorRequestTask(managedInvestigator);
    }

    private ManagedInvestigator getManagedInvestigator(final boolean approved) {
        return Iterables.find(dataSet.getCoordinator().getRegistrationCoordinatorRole().getManagedInvestigators(),
                new Predicate<ManagedInvestigator>() {
                    public boolean apply(ManagedInvestigator managedInvestigator) {
                        return managedInvestigator.isApproved() == approved;
                    }
                });
    }

    @Test
    public void testCoordinatorRequestTasks_Accept() {
        homePage = openHomePage(dataSet.getInvestigatorLogins().get(1));
        assertEquals(1, dataSet.getCoordinator().getRegistrationCoordinatorRole().getApprovedManagedProfiles().size());
        ManagedInvestigator managedInvestigator = getManagedInvestigator(false);
        CoordinatorRequestDialog coordinatorRequestDialog = homePage.getHelper().openCoordinatorRequestTask(
                managedInvestigator);
        homePage = coordinatorRequestDialog.clickAccept();
        homePage.getHelper().assertTaskListIsEmpty();
        dataSet.reload();
        assertEquals(2, dataSet.getCoordinator().getRegistrationCoordinatorRole().getApprovedManagedProfiles().size());
        assertTrue(getEmailChecker().getSentEmailSize() > 0);
    }

    @Test
    public void testCoordinatorRequestTasks_Reject() {
        homePage = openHomePage(dataSet.getInvestigatorLogins().get(1));
        assertEquals(1, dataSet.getCoordinator().getRegistrationCoordinatorRole().getApprovedManagedProfiles().size());
        ManagedInvestigator managedInvestigator = getManagedInvestigator(false);
        CoordinatorRequestDialog coordinatorRequestDialog = homePage.getHelper().openCoordinatorRequestTask(
                managedInvestigator);
        homePage = coordinatorRequestDialog.clickReject();
        homePage.getHelper().assertTaskListIsEmpty();
        dataSet.reload();
        assertEquals(1, dataSet.getCoordinator().getRegistrationCoordinatorRole().getApprovedManagedProfiles().size());
        assertTrue(getEmailChecker().getSentEmailSize() > 0);
    }

}
