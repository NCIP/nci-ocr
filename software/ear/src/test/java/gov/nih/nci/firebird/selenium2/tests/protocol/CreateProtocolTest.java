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

import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.BrowseRegistrationsPage.RegistrationListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.registration.common.InvestigatorRegistrationFormTablesTag.FormListing;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.CreateLeadOrganizationDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.CreateProtocolPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.InviteInvestigatorsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInformationTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolsListPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.protocol.AddInvestigatorsDialog;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.LoginAccount.SponsorDelegateLogin;
import gov.nih.nci.firebird.test.LoginAccount.SponsorLogin;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class CreateProtocolTest extends AbstractFirebirdWebDriverTest {

    private static final String AGENT = "aspirin";
    private DataSet dataSet;
    private FirebirdUser sponsor;
    private static final SponsorLogin SPONSOR_LOGIN = SponsorLogin.fbcisponsor1;
    private static final SponsorDelegateLogin DELEGATE_LOGIN = SponsorDelegateLogin.fbcidel1;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        sponsor = builder.createSponsor().withLogin(SPONSOR_LOGIN).get();
        builder.createInvestigator();
        builder.createSponsor().asDelegate().withLogin(DELEGATE_LOGIN);
        builder.createProtocol();
        dataSet = builder.build();
    }

    @Test
    public void testSponsorDelegateCantCreateProtocol() {
        HomePage homePage = openHomePage(DELEGATE_LOGIN);
        assertFalse("Sponsor Delegate should not be able to create protocols", homePage.getProtocolsMenu()
                .isCreateNewPresent());
    }

    @Test
    public void testCreateProtocol_Sponsor_ExistingLeadOrganization() {
        performCreateProtocol();
        performAgentFromAutoCompleteTest();
    }

    private void performCreateProtocol() {
        Protocol protocol = createProtocol();
        ProtocolLeadOrganization leadOrganization = getValidLeadOrganization();
        protocol.getLeadOrganizations().clear();
        protocol.addLeadOrganization(leadOrganization.getOrganization(), leadOrganization.getPrincipalInvestigator());

        CreateProtocolPage createPage = navigateToCreateProtocolPage();
        checkForRequiredFields(createPage);
        checkDuplicateProtocolNumber(createPage);
        createPage = navigateToCreateProtocolPage();
        createPage.getHelper().enterProtocolInfo(protocol);
        addInvalidAgents(createPage);
        List<String> agents = createPage.getAgentNames();
        checkProtocolAgentsDisplayed(protocol, agents);
        createPage.getHelper().selectFirstSponsor();

        addLeadOrganization(createPage, leadOrganization);
        createPage.getHelper().getLeadOrganizationListing(leadOrganization).clickDeleteLeadOrganization();
        addLeadOrganization(createPage, leadOrganization);

        ProtocolInformationTab protocolInfoTab = createPage.clickSave();
        protocolInfoTab.getHelper().checkProtocolDisplayed(protocol);

        addAndInviteInvestigator(protocolInfoTab.getPage().getProtocolsMenu().clickBrowse(), protocol);
        verifyInvestigatorRegistration();
    }

    private void checkProtocolAgentsDisplayed(Protocol protocol, List<String> agents) {
        assertEquals(protocol.getAgents().size(), agents.size());
        for (ProtocolAgent agent : protocol.getAgents()) {
            assertTrue(agents.contains(agent.getName()));
        }
    }

    private void checkForRequiredFields(final CreateProtocolPage createPage) {
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure();
        expectedValidationFailure.addExpectedRequiredFields("protocol.protocolTitle", "protocol.protocolNumber",
                                                            "protocol.phase");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                createPage.clickSave();
            }
        });
    }

    private void checkDuplicateProtocolNumber(final CreateProtocolPage createPage) {
        Protocol newProtocol = ProtocolFactory.getInstance().create();
        newProtocol.setProtocolNumber(dataSet.getProtocol().getProtocolNumber());
        createPage.getHelper().enterProtocolInfo(newProtocol);
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "sponsor.protocol.duplicate.number.error");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                createPage.clickSave();
            }
        });
    }

    private Protocol createProtocol() {
        Protocol protocol = ProtocolFactory.getInstance().createNoAgents();
        protocol.updateAgents(Sets.newHashSet(AGENT));
        protocol.setSponsor(sponsor.getSponsorRepresentativeOrganizations().get(0));
        protocol.addLeadOrganization(getLeadOrganization(), getPrincipalInvestigator());
        return protocol;
    }

    private Organization getLeadOrganization() {
        return getExistingExternalOrganization();
    }

    private Person getPrincipalInvestigator() {
        return getExistingExternalPerson();
    }

    private ProtocolLeadOrganization getValidLeadOrganization() {
        return new ProtocolLeadOrganization(null, getLeadOrganization(), getPrincipalInvestigator());
    }

    private CreateProtocolPage navigateToCreateProtocolPage() {
        return openHomePage(SPONSOR_LOGIN).getProtocolsMenu().clickCreateNew();
    }

    private void addInvalidAgents(CreateProtocolPage createPage) {
        createPage.getHelper().addAgent(StringUtils.EMPTY);
        createPage.getHelper().addAgent("   ");
    }

    private void addLeadOrganization(CreateProtocolPage createPage, ProtocolLeadOrganization leadOrganization) {
        addExistingLeadOrganization(createPage, leadOrganization);
    }

    private void addExistingLeadOrganization(CreateProtocolPage createPage, ProtocolLeadOrganization leadOrganization) {
        CreateLeadOrganizationDialog createLeadOrganizationDialog = createPage.clickSelectLeadOrganizationBtn();
        createLeadOrganizationDialog.getHelper().searchAndSelectOrganization(leadOrganization.getOrganization());
        if (leadOrganization.getPrincipalInvestigator() != null) {
            createLeadOrganizationDialog.getHelper().searchAndSelectPerson(leadOrganization.getPrincipalInvestigator());
        } else {
            createLeadOrganizationDialog.getHelper().searchAndSelectPerson(getExistingExternalPerson());
        }
        createLeadOrganizationDialog.clickSave();
        createPage.getHelper().checkLeadOrganizationSelected(leadOrganization);
    }

    private void addAndInviteInvestigator(ProtocolsListPage protocolListPage, Protocol protocol) {
        ProtocolInvestigatorsTab investigatorsTab = Iterables.getFirst(protocolListPage.getListings(), null)
                .clickLink().getPage().clickInvestigatorsTab();

        AddInvestigatorsDialog addInvistigatorDialog = investigatorsTab.clickAddInvestigators();
        addInvistigatorDialog.getHelper().searchAndAddPerson(dataSet.getInvestigator().getPerson());
        addInvistigatorDialog.clickDone();
        InviteInvestigatorsDialog inviteInvestigatorDialog = investigatorsTab.clickInviteInvestigators();
        inviteInvestigatorDialog.selectFilterStatus(InviteInvestigatorsDialog.SELECT_ANY);
        inviteInvestigatorDialog.clickSendInvitations();
    }

    private void verifyInvestigatorRegistration() {
        HomePage homePage;
        homePage = openHomePage(dataSet.getInvestigatorLogin());
        BrowseRegistrationsPage registrationsPage = homePage.getInvestigatorMenu().clickProtocolRegistrations();
        RegistrationListing registrationListing = Iterables.getFirst(registrationsPage.getRegistrationListings(), null);
        RegistrationOverviewTab overviewTab = registrationListing.clickRegistrationLink();
        checkForms(overviewTab);
    }

    private void checkForms(RegistrationOverviewTab overviewTab) {
        List<FormListing> formListings = overviewTab.getFormListings();
        assertEquals(FormTypeEnum.PROTOCOL_REGISTATION_FORM_TYPES.size(), formListings.size());
        overviewTab.getPage().clickForm1572Tab();
        overviewTab.getPage().clickFinancialDisclosureTab();
        overviewTab.getPage().clickCvTab();
        overviewTab.getPage().clickHumanResearchCertificateTab();
        overviewTab.getPage().clickAdditionalAttachmentsTab();
    }

    private void performAgentFromAutoCompleteTest() {
        CreateProtocolPage createPage = navigateToCreateProtocolPage();
        createPage.getHelper().addAgentFromAutoComplete(AGENT);
        assertTrue(createPage.getAgentNames().contains(AGENT));
        createPage.deleteAgent(AGENT);
        assertFalse(createPage.getAgentNames().contains(AGENT));
    }

    @Test
    public void testCreateProtocol_NoAgents() {
        Protocol newProtocol = ProtocolFactory.getInstance().createNoAgents();
        CreateProtocolPage createPage = navigateToCreateProtocolPage();
        createPage.getHelper().enterProtocolInfo(newProtocol);
        createPage.clickSave();
        final Protocol createdProtocol = dataSet.getLastCreatedObject(Protocol.class);
        assertTrue(createdProtocol.getAgents().isEmpty());
    }

    @Test
    public void testCreateProtocol_CheckSponsorProtocolIdLengthValidation() {
        Protocol protocol = createProtocol();
        ProtocolLeadOrganization leadOrganization = getValidLeadOrganization();
        String oversizedProtocolNumber = ValueGenerator.getUniqueString(Protocol.MAX_PROTOCOL_NUM_SIZE + 1);
        protocol.setProtocolNumber(oversizedProtocolNumber);
        CreateProtocolPage createPage = navigateToCreateProtocolPage();
        createPage.getHelper().enterProtocolInfo(protocol);
        addExistingLeadOrganization(createPage, leadOrganization);
        String expectedTruncatedProtocolNumber = oversizedProtocolNumber.substring(0, Protocol.MAX_PROTOCOL_NUM_SIZE);
        assertEquals(expectedTruncatedProtocolNumber, createPage.getProtocolNumber());
        ProtocolInformationTab protocolInfoTab = createPage.clickSave();
        assertEquals(expectedTruncatedProtocolNumber, protocolInfoTab.getSponsorProtocolId());
    }

}
