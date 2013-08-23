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
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigator;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.base.ConfirmDialog;
import gov.nih.nci.firebird.selenium2.pages.components.tags.ManagePersonTag;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.SubInvestigatorAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.InviteSubinvestigatorsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubInvestigatorAssociationFormDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProfileSubinvestigatorsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubinvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.selenium2.pages.util.FirebirdEmailUtils;
import gov.nih.nci.firebird.service.messages.FirebirdMessageTemplate;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class SubinvestigatorsTabTest extends AbstractFirebirdWebDriverTest {

    @Inject
    private DataSetBuilder builder;
    private DataSet dataSet;
    private SubinvestigatorsTab subinvestigatorsTab;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        InvestigatorRegistration primaryRegistration = builder.createRegistration().complete().withStatus(RegistrationStatus.COMPLETED).get();
        builder.createSubinvestigatorRegistration(primaryRegistration);
        dataSet = builder.build();
        goToSubinvestigatorsTab();
    }

    private void deleteSubinvestigatorRegistrations() {
        dataSet.delete(getOnlySubinvestigatorRegistration());
    }

    private SubInvestigatorRegistration getOnlySubinvestigatorRegistration() {
        return Iterables.getOnlyElement(dataSet.getInvestigatorRegistration().getSubinvestigatorRegistrations());
    }

    private void goToSubinvestigatorsTab() {
        HomePage homePage = openLoginPage().getHelper().goToHomePage(dataSet.getInvestigatorLogin(), getProvider());
        BrowseRegistrationsPage browseRegistrationsPage = homePage.getInvestigatorMenu().clickProtocolRegistrations();
        RegistrationOverviewTab overviewTab = browseRegistrationsPage.getHelper()
                .getRegistrationListing(dataSet.getInvestigatorRegistration()).clickRegistrationLink();
        overviewTab.getPage().getHelper().checkRegistrationPageHeader(dataSet.getInvestigatorRegistration());
        subinvestigatorsTab = overviewTab.getPage().clickSubinvestigatorsTab();
    }

    @Test
    public void testRemoveSubinvestigator_SubmittedSubInvestigatorRegistration() {
        performRemoveSubinvestigatorTest(RegistrationStatus.IN_PROGRESS, RegistrationStatus.SUBMITTED,
                FormStatus.COMPLETED);
    }

    private void performRemoveSubinvestigatorTest(RegistrationStatus primaryRegistrationStatus,
            RegistrationStatus subInvestigatorRegistrationStatus, FormStatus expected1572Status) {
        dataSet.getInvestigatorRegistration().setStatus(primaryRegistrationStatus);
        SubInvestigatorRegistration subinvestigatorRegistration = getOnlySubinvestigatorRegistration();
        subinvestigatorRegistration.setStatus(subInvestigatorRegistrationStatus);
        dataSet.update(dataSet.getInvestigatorRegistration(), subinvestigatorRegistration);
        RegistrationOverviewTab overviewTab = subinvestigatorsTab.getPage().clickHome().getInvestigatorMenu()
                .clickProtocolRegistrations().getHelper().getRegistrationListing(dataSet.getInvestigatorRegistration())
                .clickRegistrationLink();
        SubinvestigatorsTab subInvestigatorsTab = overviewTab.getPage().clickSubinvestigatorsTab();
        ConfirmDialog confirmDialog = subInvestigatorsTab.getHelper()
                .getSubinvestigatorListing(subinvestigatorRegistration).clickDelete();
        confirmDialog.getHelper().checkTitleAndMessage(subinvestigatorRegistration.getProfile().getPerson().getDisplayName());
        confirmDialog.clickConfirmButton();
        checkForm1572Status(subInvestigatorsTab, expected1572Status);
        checkForEmails(subinvestigatorRegistration);
    }

    private void checkForm1572Status(SubinvestigatorsTab subInvestigatorsTab, FormStatus expectedStatus) {
        RegistrationOverviewTab overviewTab = subInvestigatorsTab.getPage().clickOverviewTab();
        String actualStatus = overviewTab.getHelper().getFormListing(dataSet.getInvestigatorRegistration().getForm1572())
                .getFormStatus();
        assertEquals(expectedStatus.getDisplay(), actualStatus);
    }

    private void checkForEmails(SubInvestigatorRegistration subinvestigatorRegistration) {
        getEmailChecker().assertEmailCount(2);
        Person investigator = subinvestigatorRegistration.getPrimaryRegistration().getProfile().getPerson();
        String expectedSubject = FirebirdEmailUtils.getExpectedSubject(
                FirebirdMessageTemplate.REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL, investigator);
        MimeMessage emailToSubinvestigator = getEmailChecker().getSentEmail(
                subinvestigatorRegistration.getProfile().getPerson().getEmail(), expectedSubject);

        Set<MimeMessage> sentEmails = Sets.newHashSet(getEmailChecker().getSentEmails());
        sentEmails.remove(emailToSubinvestigator);
        MimeMessage emailToSponsor = Iterables.getOnlyElement(sentEmails);
        String actualSubject = getEmailChecker().getMessageSubject(emailToSponsor);
        expectedSubject = FirebirdEmailUtils.getExpectedSubject(
                FirebirdMessageTemplate.REMOVE_SUBINVESTIGATOR_NOTIFICATION_EMAIL_TO_SPONSOR,
                subinvestigatorRegistration);
        assertEquals(expectedSubject, actualSubject);
    }

    @Test
    public void testAddFromProfile() {
        deleteSubinvestigatorRegistrations();
        Set<SubInvestigator> subinvestigators = addAssociatedSubinvestigators();
        ProfileSubinvestigatorsDialog dialog = subinvestigatorsTab.clickAddFromProfile();
        checkDialogContainsSubinvestigators(dialog, subinvestigators);

        Iterator<SubInvestigator> subInvestigatorIterator = subinvestigators.iterator();
        Person subinvestigator1 = subInvestigatorIterator.next().getPerson();
        addFromProfile(dialog, subinvestigator1);
        assertEquals(1, subinvestigatorsTab.getSubinvestigatorListings().size());

        dialog = subinvestigatorsTab.clickAddFromProfile();
        assertFalse(dialog.getHelper().isPersonListed(subinvestigator1));
        Person subinvestigator2 = subInvestigatorIterator.next().getPerson();
        addFromProfile(dialog, subinvestigator2);
        assertEquals(2, subinvestigatorsTab.getSubinvestigatorListings().size());

        dialog = subinvestigatorsTab.clickAddFromProfile();
        assertFalse(dialog.getHelper().isPersonListed(subinvestigator1));
        assertFalse(dialog.getHelper().isPersonListed(subinvestigator2));
    }

    private Set<SubInvestigator> addAssociatedSubinvestigators() {
        SubInvestigatorAssociationsTab personAssociationsTab = subinvestigatorsTab.getPage().getInvestigatorMenu()
                .clickProfile().getPage().clickSubInvestigatorAssociationsTab();
        addAssociatedSubinvestigator(personAssociationsTab);
        addAssociatedSubinvestigator(personAssociationsTab);
        goToSubinvestigatorsTab();
        dataSet.reload();
        return dataSet.getInvestigator().getInvestigatorRole().getProfile().getSubInvestigators();
    }

    private void addAssociatedSubinvestigator(SubInvestigatorAssociationsTab personAssociationsTab) {
        SubInvestigatorAssociationFormDialog addNewAssociationDialog = personAssociationsTab.clickAddNew();
        addNewAssociationDialog.getHelper().searchAndSelectPerson(getExistingExternalPerson());
    }

    private void checkDialogContainsSubinvestigators(ProfileSubinvestigatorsDialog dialog,
            Set<SubInvestigator> subinvestigators) {
        assertEquals(subinvestigators.size(), dialog.getSubinvestigatorListings().size());
        assertTrue(subinvestigators.size() > 1);
        for (SubInvestigator subInvestigator : subinvestigators) {
            ProfileSubinvestigatorsDialog.SubinvestigatorListing listing = dialog.getHelper().getPersonListing(
                    subInvestigator.getPerson());
            assertNotNull(listing);
            assertFalse(listing.isSelected());
        }
    }

    private void addFromProfile(ProfileSubinvestigatorsDialog dialog, Person subinvestigator) {
        dialog.getHelper().selectPerson(subinvestigator);
        dialog.clickSave();
        assertNotNull(subinvestigatorsTab.getHelper().getSubinvestigatorListing(subinvestigator));
    }

    @Test
    public void testAddNew() {
        deleteSubinvestigatorRegistrations();
        final Person person = dataSet.getInvestigatorRegistration().getProfile().getPerson();
        dataSet.reload();

        addPerson(subinvestigatorsTab, person);
        dataSet.reload();
        checkPersonAddedToRegistration(person);
        verifyPersonInTable(subinvestigatorsTab);

        // re-add same person
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "registration.subinvestigator.add.duplicate", person.getDisplayName());
        addPerson(subinvestigatorsTab, person, expectedValidationFailure);
        removeAndVerifyPerson(subinvestigatorsTab, getOnlySubinvestigatorRegistration());
        addNewSubInvestigator(subinvestigatorsTab);
    }

    private void addPerson(SubinvestigatorsTab subinvestigatorTab, Person person) {
        SubInvestigatorAssociationFormDialog dialog = subinvestigatorTab.clickAddNew();
        dialog.getHelper().searchAndSelectPerson(person);
    }

    private void addPerson(SubinvestigatorsTab subinvestigatorTab, final Person person,
            ExpectedValidationFailure expectedValidationFailure) {
        final SubInvestigatorAssociationFormDialog dialog = subinvestigatorTab.clickAddNew();
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                dialog.getHelper().searchAndSelectPerson(person);
            }
        });
        dialog.clickCancel();
    }

    private void verifyPersonInTable(SubinvestigatorsTab subInvestigatorsPage) {
        List<SubinvestigatorsTab.SubinvestigatorListing> subInvestigatorListings = subinvestigatorsTab
                .getSubinvestigatorListings();
        assertEquals(1, subInvestigatorListings.size());
        SubInvestigatorRegistration registration = getOnlySubinvestigatorRegistration();
        subInvestigatorsPage.getHelper().areEquivalent(subInvestigatorListings.get(0), registration);
    }

    private void checkPersonAddedToRegistration(Person person) {
        for (SubInvestigator subInvestigator : dataSet.getInvestigatorRegistration().getProfile().getSubInvestigators()) {
            if (subInvestigator.getPerson().getId().equals(person.getId())) {
                return;
            }
        }
        fail("not added to profile.");
    }

    private void removeAndVerifyPerson(SubinvestigatorsTab subinvestigatorTab, SubInvestigatorRegistration subReg) {
        ConfirmDialog confirmRemovalDialog = subinvestigatorTab.getHelper().getSubinvestigatorListing(subReg)
                .clickDelete();
        confirmRemovalDialog.clickConfirmButton();
        List<SubinvestigatorsTab.SubinvestigatorListing> subInvestigatorListings = subinvestigatorTab
                .getSubinvestigatorListings();
        assertTrue(subInvestigatorListings.isEmpty());
        dataSet.reload();
        assertTrue(dataSet.getInvestigatorRegistration().getSubinvestigatorRegistrations().isEmpty());
    }

    private void addNewSubInvestigator(SubinvestigatorsTab subinvestigatorTab) {
        Person person = PersonFactory.getInstance().create();
        SubInvestigatorAssociationFormDialog dialog = subinvestigatorTab.clickAddNew();
        ManagePersonTag managePersonTag = dialog.clickCreateNew();
        managePersonTag.getHelper().enterPersonData(person);
        dialog.clickSave();
        List<SubinvestigatorsTab.SubinvestigatorListing> subInvestigatorListings = subinvestigatorTab
                .getSubinvestigatorListings();
        assertEquals(1, subInvestigatorListings.size());
        dataSet.reload();
        Person subinvestigator = Iterables.getOnlyElement(dataSet.getInvestigatorRegistration().getSubinvestigators());
        assertEquals(CurationStatus.PENDING, subinvestigator.getCurationStatus());
    }

    @Test
    public void testInvitee() {
        deleteSubinvestigatorRegistrations();
        addAssociatedSubinvestigators();
        ProfileSubinvestigatorsDialog addDialog = subinvestigatorsTab.clickAddFromProfile();

        List<ProfileSubinvestigatorsDialog.SubinvestigatorListing> subinvestigatorListings = addDialog
                .getSubinvestigatorListings();
        assertEquals(3, subinvestigatorListings.size());
        subinvestigatorListings.get(0).select();
        subinvestigatorListings.get(1).select();
        addDialog.clickSave();

        InviteSubinvestigatorsDialog inviteDialog = subinvestigatorsTab.clickInvite();
        List<gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.InviteSubinvestigatorsDialog.SubinvestigatorListing> inviteListings = inviteDialog
                .getSubinvestigatorListings();
        assertEquals(2, inviteListings.size());
        assertTrue(inviteListings.get(0).isSelected());
        assertTrue(inviteListings.get(1).isSelected());
        inviteDialog.selectFilter(InviteSubinvestigatorsDialog.FILTER_NONE);
        inviteListings = inviteDialog.getSubinvestigatorListings();
        assertFalse(inviteListings.get(0).isSelected());
        assertFalse(inviteListings.get(1).isSelected());
        inviteListings.get(0).select();
        Long invitedSubinvestigatorId = inviteListings.get(0).getId();
        inviteDialog.clickSendInvitations();

        InvestigatorRegistration registration = reloadRegistration(dataSet.getInvestigatorRegistration());
        List<SubinvestigatorsTab.SubinvestigatorListing> subinvestigatorsTable = subinvestigatorsTab
                .getSubinvestigatorListings();
        for (SubinvestigatorsTab.SubinvestigatorListing row : subinvestigatorsTable) {
            SubInvestigatorRegistration subRegForRow = findSubInvRegForRow(registration, row);
            assertNotNull("expected reg id=" + row.getId(), subRegForRow);

            if (row.getId().equals(invitedSubinvestigatorId)) {
                assertEquals(InvitationStatus.NO_RESPONSE, subRegForRow.getInvitation().getInvitationStatus());
            }
        }
    }

    private SubInvestigatorRegistration findSubInvRegForRow(InvestigatorRegistration registration,
            SubinvestigatorsTab.SubinvestigatorListing row) {
        for (SubInvestigatorRegistration subReg : registration.getSubinvestigatorRegistrations()) {
            if (row.getId().equals(subReg.getId())) {
                subinvestigatorsTab.getHelper().areEquivalent(row, subReg);
                return subReg;
            }
        }
        return null;
    }

    private InvestigatorRegistration reloadRegistration(InvestigatorRegistration registration) {
        Session s = getHibernateHelper().getCurrentSession();
        Transaction t = s.beginTransaction();
        try {
            InvestigatorRegistration reg = (InvestigatorRegistration) s.get(InvestigatorRegistration.class,
                    registration.getId());
            reg.getProfile().getSubInvestigators().iterator().hasNext();
            hibernateInitialize(reg);
            Hibernate.initialize(reg.getSubinvestigatorRegistrations());
            return reg;
        } finally {
            t.rollback();
        }
    }

    protected void hibernateInitialize(InvestigatorRegistration reg) {
        hibernateIntializeBaseRegistration(reg);
        for (SubInvestigatorRegistration subInvestigatorRegistration : reg.getSubinvestigatorRegistrations()) {
            hibernateIntializeBaseRegistration(subInvestigatorRegistration);
        }
    }

    private void hibernateIntializeBaseRegistration(AbstractProtocolRegistration reg) {
        Hibernate.initialize(reg.getProfile());
        Hibernate.initialize(reg.getProfile().getPerson());
    }

}
