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

import static gov.nih.nci.firebird.selenium2.pages.util.FirebirdEmailUtils.*;
import static gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubInvestigatorAssociationFormDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubinvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewCompletionDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import javax.mail.MessagingException;

import org.junit.Test;

import com.google.common.collect.Iterables;

public abstract class AbstractCancelRegistrationRevisionTest extends AbstractFirebirdWebDriverTest {

    private DataSet dataSet;
    private InvestigatorRegistration registration;
    private SubInvestigatorRegistration subInvestigatorRegistration;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setUpData();
    }

    private void setUpData() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        registration = builder.createRegistration().complete().withStatus(RegistrationStatus.SUBMITTED).get();
        FirebirdUser subinvestigator = builder.createInvestigator().get();
        subInvestigatorRegistration = builder.createSubinvestigatorRegistration(subinvestigator, registration).withStatus(RegistrationStatus.ACCEPTED).get();
        builder.createCoordinator().withApprovedMangedInvestigator(registration.getProfile().getUser());
        builder.createSponsor();
        dataSet = builder.build();
    }

    @Test
    public void testCancelRegistrationRevision() throws MessagingException {
        approveRegistration();
        reviseRegistration();
        BrowseRegistrationsPage registrationsPage = navigateToRegistrationsPage();
        assertEquals(2, registrationsPage.getRegistrationListings().size());
        RegistrationOverviewTab overviewTab = registrationsPage.getHelper().clickRegistrationLink(registration);
        SubinvestigatorsTab subinvestigatorsTab = overviewTab.getPage().clickSubinvestigatorsTab();
        SubInvestigatorRegistration invitedSubinvestigatorRegistration = addSubinvestigator(subinvestigatorsTab, true);
        SubInvestigatorRegistration uninvitedSubinvestigatorRegistration = addSubinvestigator(subinvestigatorsTab,
                false);

        overviewTab = subinvestigatorsTab.getPage().clickOverviewTab();
        cancelRegistrationRevision(overviewTab);

        InvestigatorRegistration newActiveRegistration = dataSet.getLastCreatedObject(InvestigatorRegistration.class);

        assertEquals(1, newActiveRegistration.getSubinvestigatorRegistrations().size());
        assertEquals(subInvestigatorRegistration.getId(),
                Iterables.getOnlyElement(newActiveRegistration.getSubinvestigatorRegistrations()).getId());
        assertNull(dataSet.reloadObject(invitedSubinvestigatorRegistration));
        assertNull(dataSet.reloadObject(uninvitedSubinvestigatorRegistration));
        checkForExpectedEmails(invitedSubinvestigatorRegistration);
    }

    private void approveRegistration() {
        ReviewRegistrationTab reviewTab = openHomePage(dataSet.getSponsorLogin()).getHelper().openSubmittedProtocolRegistrationTask(registration);
        reviewTab.getHelper().reviewAllForms(registration);
        reviewTab.getHelper().acceptAllForms(registration);
        ((ReviewCompletionDialog)reviewTab.clickCompleteReview()).clickApproveRegistration();
    }

    private void reviseRegistration() {
        BrowseRegistrationsPage registrationsPage = navigateToRegistrationsPage();
        assertEquals(1, registrationsPage.getRegistrationListings().size());
        RegistrationOverviewTab overviewTab = registrationsPage.getHelper().getRegistrationListing(registration).clickRegistrationLink();
        assertFalse(overviewTab.isCancelRevisionButtonPresent());
        overviewTab.clickReviseRegistration();
        assertTrue(overviewTab.isCancelRevisionButtonPresent());
        assertFalse(overviewTab.isReviseRegistrationButtonPresent());
        overviewTab.getHelper().assertHasStatus(RegistrationStatus.IN_PROGRESS);
    }

    abstract BrowseRegistrationsPage navigateToRegistrationsPage();

    private SubInvestigatorRegistration addSubinvestigator(SubinvestigatorsTab subinvestigatorsTab, boolean invite) {
        SubInvestigatorAssociationFormDialog newSubinvestigatorDialog = subinvestigatorsTab.clickAddNew();
        newSubinvestigatorDialog.getHelper().searchAndSelectPerson(getExistingExternalPerson());
        if (invite) {
            subinvestigatorsTab.clickInvite().clickSendInvitations();
        }
        return dataSet.getLastCreatedObject(SubInvestigatorRegistration.class);
    }

    private void cancelRegistrationRevision(RegistrationOverviewTab overviewTab) {
        BrowseRegistrationsPage registrationsPage = overviewTab.clickCancelRevision();
        assertEquals(1, registrationsPage.getRegistrationListings().size());
        overviewTab = registrationsPage.getRegistrationListings().get(0).clickRegistrationLink();
        overviewTab.getHelper().assertHasStatus(RegistrationStatus.APPROVED);
        overviewTab.getHelper().assertFormsHaveStatus(FormStatus.APPROVED);
        assertTrue(overviewTab.isReviseRegistrationButtonPresent());
        assertFalse(overviewTab.isCancelRevisionButtonPresent());
    }

    private void checkForExpectedEmails(SubInvestigatorRegistration subInvestigatorRegistration) {
        checkForEmailsToInvitedSubinvestigator(subInvestigatorRegistration);
        checkForCancelationEmailToSponsor();
    }

    private void checkForEmailsToInvitedSubinvestigator(SubInvestigatorRegistration subInvestigatorRegistration) {
        String subinvestigatorEmail = subInvestigatorRegistration.getProfile().getPerson().getEmail();
        getEmailChecker().getSentEmail(subinvestigatorEmail,
                getExpectedSubject(SUBINVESTIGATOR_INVITATION_EMAIL, subInvestigatorRegistration));
        getEmailChecker().getSentEmail(subinvestigatorEmail,
                getExpectedSubject(REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL, registration.getProfile().getPerson()));
    }

    private void checkForCancelationEmailToSponsor() {
        getEmailChecker().getSentEmail(
                getExpectedSubject(REGISTRATION_REVISION_CANCELED_EMAIL_TO_SPONSOR, registration));
    }

    DataSet getDataSet() {
        return dataSet;
    }

}
