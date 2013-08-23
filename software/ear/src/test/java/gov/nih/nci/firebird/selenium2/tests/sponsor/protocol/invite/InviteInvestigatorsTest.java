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
package gov.nih.nci.firebird.selenium2.tests.sponsor.protocol.invite;

import static gov.nih.nci.firebird.data.InvitationStatus.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.InviteInvestigatorsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.InviteInvestigatorsDialog.InvestigatorListing;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolsListPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.protocol.AddInvestigatorsDialog;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import org.junit.Test;

public class InviteInvestigatorsTest extends AbstractFirebirdWebDriverTest {

    private DataSet dataSet;
    private Person investigator;
    private Person investigator2;
    private Person newInvestigator;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createSponsor();
        builder.createSponsor().asDelegate();
        builder.createProtocol();
        investigator = builder.createInvestigator().get().getPerson();
        dataSet = builder.build();
        investigator2 = getTestDataSource().getPerson();
        newInvestigator = PersonFactory.getInstance().create();
    }

    @Test
    public void testSponsorDelegateCantAddOrInviteInvestigatorsToProtocol() {
        ProtocolInvestigatorsTab investigatorsTab = navigateToInvestigatorsTab(dataSet.getSponsorLogins().get(1),
                dataSet.getProtocol());
        assertFalse(investigatorsTab.isAddInvestigatorButtonPresent());
        assertFalse(investigatorsTab.isInviteInvestigatorsButtonPresent());
    }

    private ProtocolInvestigatorsTab navigateToInvestigatorsTab(LoginAccount loginAccount, Protocol protocol) {
        ProtocolsListPage protocolsPage = openHomePage(loginAccount).getProtocolsMenu().clickBrowse();
        return protocolsPage.getHelper().clickLink(protocol).getPage().clickInvestigatorsTab();
    }

    @Test
    public void testAddInvestigatorsToProtocol_Sponsor() {
        ProtocolInvestigatorsTab investigatorsTab = navigateToInvestigatorsTab(dataSet.getSponsorLogin(),
                dataSet.getProtocol());
        AddInvestigatorsDialog addInvestigatorsDialog = investigatorsTab.clickAddInvestigators();
        addInvestigator(investigator, addInvestigatorsDialog);
        addInvestigator(investigator2, addInvestigatorsDialog);
        addNewInvestigator(newInvestigator, addInvestigatorsDialog);
        checkCantAddDuplicateInvestigator(investigator2, addInvestigatorsDialog);

        assertEquals(3, investigatorsTab.getListings().size());

        investigatorsTab.getHelper().assertListed(investigator, NOT_INVITED);
        investigatorsTab.getHelper().assertListed(investigator2, NOT_INVITED);
        investigatorsTab.getHelper().assertListed(newInvestigator, NOT_INVITED);

        checkInvestigatorCantAccessRegistration(dataSet.getInvestigatorLogin());
        navigateToInvestigatorsTab(dataSet.getSponsorLogin(), dataSet.getProtocol());
        inviteInvestigators(investigator, investigator2, investigatorsTab);
        checkInvestigatorHasInvitation(dataSet.getInvestigatorLogin());
        getEmailChecker().assertEmailCount(4);
    }

    private void addInvestigator(Person person, AddInvestigatorsDialog addInvestigatorsDialog) {
        addInvestigatorsDialog.getHelper().searchAndAddPerson(person);
        String expectedConfirmationText = getPropertyText("sponsor.protocol.investigator.added",
                person.getDisplayName());
        assertEquals(expectedConfirmationText, addInvestigatorsDialog.getConfirmationText());
    }

    private void addNewInvestigator(Person newInvestigator, AddInvestigatorsDialog addInvestigatorsDialog) {
        checkForRequiredFields(addInvestigatorsDialog);
        addInvestigatorsDialog.getHelper().addNewPerson(newInvestigator);
    }

    private void checkForRequiredFields(final AddInvestigatorsDialog addInvestigatorsDialog) {
        addInvestigatorsDialog.clickCreateNew();

        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure();
        expectedValidationFailure.addExpectedRequiredFields("person.firstName", "person.lastName", "person.email",
                "person.postalAddress.streetAddress", "person.postalAddress.city",
                "person.postalAddress.stateOrProvince", "person.postalAddress.postalCode");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                addInvestigatorsDialog.getHelper().addNewPerson(new Person());
            }
        });
    }

    private void checkCantAddDuplicateInvestigator(Person duplicateInvestigator,
            AddInvestigatorsDialog addInvestigatorsDialog) {
        addInvestigatorsDialog.getHelper().searchAndAddPerson(duplicateInvestigator);
        String expectedConfirmationText = getPropertyText("sponsor.protocol.investigator.alreadyAdded",
                duplicateInvestigator.getDisplayName());
        assertEquals(expectedConfirmationText, addInvestigatorsDialog.getConfirmationText());
        addInvestigatorsDialog.clickDone();
    }

    private void checkInvestigatorCantAccessRegistration(LoginAccount investigatorLogin) {
        HomePage home = openHomePage(investigatorLogin);
        assertTrue(home.getTasks().isEmpty());
        BrowseRegistrationsPage browseRegistrationsPage = home.getInvestigatorMenu().clickProtocolRegistrations();
        assertTrue(browseRegistrationsPage.getRegistrationListings().isEmpty());
        browseRegistrationsPage.clickSignOut();
    }

    private void inviteInvestigators(Person investigator1, Person investigator2,
            ProtocolInvestigatorsTab investigatorsTab) {
        InviteInvestigatorsDialog inviteDialog = investigatorsTab.clickInviteInvestigators();
        assertEquals(3, inviteDialog.getListings().size());
        inviteDialog.getHelper().verifyInvestigatorsSelected(investigator1, investigator2, newInvestigator);
        checkInviteWithZeroSelections(inviteDialog);

        inviteInvestigator(investigator1, inviteDialog, investigatorsTab);
        inviteUnInvitedInvestigators(inviteDialog);
        investigatorsTab.getHelper().assertListed(investigator1, NO_RESPONSE);
        investigatorsTab.getHelper().assertListed(investigator2, NO_RESPONSE);
        investigatorsTab.getHelper().assertListed(newInvestigator, NO_RESPONSE);
    }

    private void checkInviteWithZeroSelections(final InviteInvestigatorsDialog inviteDialog) {
        inviteDialog.selectFilterStatus(InviteInvestigatorsDialog.SELECT_NONE);
        for (InvestigatorListing listing : inviteDialog.getListings()) {
            assertFalse(listing.isSelected());
        }
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "sponsor.protocol.investigator.invite.no.selection.warning.message");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                inviteDialog.clickSendInvitations();
            }
        });
    }

    private void inviteInvestigator(Person person, InviteInvestigatorsDialog inviteDialog,
            ProtocolInvestigatorsTab investigatorsTab) {
        inviteDialog.selectFilterStatus(InviteInvestigatorsDialog.SELECT_NONE);
        inviteDialog.getHelper().getListing(person).select();
        inviteDialog.clickSendInvitations();
        investigatorsTab.getHelper().assertListed(person, NO_RESPONSE);
        inviteDialog = investigatorsTab.clickInviteInvestigators();
        inviteDialog.getHelper().getListing(person).unselect();
        inviteDialog.getHelper().verifyInvestigatorsSelected(investigator2, newInvestigator);
    }

    private void inviteUnInvitedInvestigators(InviteInvestigatorsDialog inviteDialog) {
        inviteDialog.selectFilterStatus(InviteInvestigatorsDialog.SELECT_ANY);
        inviteDialog.clickSendInvitations();
    }

    private void checkInvestigatorHasInvitation(LoginAccount investigatorLogin) {
        HomePage home = openHomePage(investigatorLogin);
        assertEquals(1, home.getTasks().size());
        BrowseRegistrationsPage browseRegistrationsPage = home.getInvestigatorMenu().clickProtocolRegistrations();
        assertEquals(1, browseRegistrationsPage.getRegistrationListings().size());
    }

}
