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

import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.CreateLeadOrganizationDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.CreateProtocolPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInformationTab;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.LoginAccount.SponsorLogin;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;
import gov.nih.nci.firebird.test.util.FirebirdPropertyUtils;
import org.junit.Test;

public class CreateLeadOrganizationTest extends AbstractFirebirdWebDriverTest {

    private static final SponsorLogin SPONSOR_LOGIN = SponsorLogin.fbcisponsor1;
    private static String ERROR_IN_SECTION_PREFIX = FirebirdPropertyUtils
            .getPropertyText("accordion.form.errors.in.section") + " ";
    private final OrganizationFactory orgFactory = OrganizationFactory.getInstance();
    private final PersonFactory personFactory = PersonFactory.getInstance();
    private DataSet dataSet;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createSponsor().withLogin(SPONSOR_LOGIN).get();
        builder.createInvestigator();
        builder.createProtocol();
        dataSet = builder.build();
    }

    @Test
    public void testAddLeadOrganizationWithExistingData() {
        Protocol protocol = ProtocolFactory.getInstance().create();
        protocol.setSponsor(dataSet.getSponsor().getSponsorRepresentativeOrganizations().get(0));
        ProtocolLeadOrganization leadOrganization = getValidLeadOrganization();
        ProtocolLeadOrganization samePILeadOrganization = new ProtocolLeadOrganization(null, getLeadOrganization(),
                leadOrganization.getPrincipalInvestigator());
        protocol.getLeadOrganizations().clear();
        protocol.getLeadOrganizations().add(leadOrganization);
        protocol.getLeadOrganizations().add(samePILeadOrganization);

        CreateProtocolPage createPage = navigateToCreateProtocolPage();
        CreateLeadOrganizationDialog leadOrganizationDialog = createPage.clickSelectLeadOrganizationBtn();
        checkSaveWithoutSearching(leadOrganizationDialog);

        searchAndSelectOrganization(leadOrganizationDialog, leadOrganization.getOrganization());
        verifyNoPersonError(leadOrganizationDialog);

        searchAndSelectPrincipalInvestigator(leadOrganizationDialog, leadOrganization.getPrincipalInvestigator());
        leadOrganizationDialog.clickSave();

        createPage.getHelper().checkLeadOrganizationSelected(leadOrganization);
        verifyCanSelectSameOrganizationWithDifferentInvestigator(createPage, leadOrganization);
        leadOrganizationDialog = createPage.clickSelectLeadOrganizationBtn();
        verifyCannotSelectSameOrganization(leadOrganizationDialog, leadOrganization);
        leadOrganizationDialog.getHelper().enterDataAndSave(samePILeadOrganization);
        createPage.getHelper().checkLeadOrganizationSelected(leadOrganization);
        createPage.getHelper().checkLeadOrganizationSelected(samePILeadOrganization);
        removeAndAddLeadOrganization(createPage, leadOrganization);
        completeSaveProtcol(createPage, protocol);
    }

    private CreateProtocolPage navigateToCreateProtocolPage() {
        return openHomePage(SPONSOR_LOGIN).getProtocolsMenu().clickCreateNew();
    }

    private ProtocolLeadOrganization getValidLeadOrganization() {
        return new ProtocolLeadOrganization(null, getLeadOrganization(), getPrincipalInvestigator());
    }

    private Person getPrincipalInvestigator() {
        return getExistingExternalPerson();
    }

    private Organization getLeadOrganization() {
        return getExistingExternalOrganization();
    }

    private void checkSaveWithoutSearching(final CreateLeadOrganizationDialog leadOrganizationDialog) {
        ExpectedValidationFailure failure = new ExpectedValidationFailure();
        failure.addExpectedText(ERROR_IN_SECTION_PREFIX
                + getPropertyText("sponsor.protocol.lead.organization.select.organization.header"));
        failure.addExpectedText(ERROR_IN_SECTION_PREFIX
                + getPropertyText("sponsor.protocol.lead.organization.select.principal.investigator.header"));
        failure.addExpectedMessage("organization.search.no.selection.error");
        failure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                leadOrganizationDialog.clickSave();
            }
        });
    }

    private void searchAndSelectOrganization(CreateLeadOrganizationDialog leadOrganizationDialog,
            Organization organization) {
        leadOrganizationDialog.getHelper().searchAndSelectOrganization(organization);
        leadOrganizationDialog.clickOrganizationSearchAgain();
        leadOrganizationDialog.getHelper().searchAndSelectOrganization(organization);
    }

    private void verifyNoPersonError(final CreateLeadOrganizationDialog leadOrganizationDialog) {
        ExpectedValidationFailure failure = new ExpectedValidationFailure();

        failure.addExpectedText(ERROR_IN_SECTION_PREFIX
                + getPropertyText("sponsor.protocol.lead.organization.select.principal.investigator.header"));
        failure.addExpectedMessage("person.search.no.selection.error");
        failure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                leadOrganizationDialog.clickSave();
                leadOrganizationDialog.openPersonSection();
            }
        });
    }

    private void searchAndSelectPrincipalInvestigator(CreateLeadOrganizationDialog leadOrganizationDialog,
            Person principalInvestigator) {
        leadOrganizationDialog.getHelper().searchAndSelectPerson(principalInvestigator);
        leadOrganizationDialog.clickPrincipalInvestigatorSearchAgain();
        leadOrganizationDialog.getHelper().searchAndSelectPerson(principalInvestigator);
    }

    private void verifyCanSelectSameOrganizationWithDifferentInvestigator(final CreateProtocolPage createPage,
            final ProtocolLeadOrganization leadOrganization) {
        CreateLeadOrganizationDialog leadOrganizationDialog = createPage.clickSelectLeadOrganizationBtn();
        leadOrganizationDialog.getHelper().searchAndSelectOrganization(leadOrganization.getOrganization());
        Person newPrincipalInvestigator = getExistingExternalPerson();
        leadOrganizationDialog.getHelper().searchAndSelectPerson(newPrincipalInvestigator);
        leadOrganizationDialog.clickSave();
        ProtocolLeadOrganization newLeadOrganization = new ProtocolLeadOrganization(null,
                leadOrganization.getOrganization(), newPrincipalInvestigator);
        createPage.getHelper().getLeadOrganizationListing(newLeadOrganization).clickDeleteLeadOrganization();
    }

    private void verifyCannotSelectSameOrganization(final CreateLeadOrganizationDialog leadOrganizationDialog,
            final ProtocolLeadOrganization leadOrganization) {
        leadOrganizationDialog.getHelper().searchAndSelectOrganization(leadOrganization.getOrganization());
        leadOrganizationDialog.getHelper().searchAndSelectPerson(leadOrganization.getPrincipalInvestigator());
        ExpectedValidationFailure failure = new ExpectedValidationFailure(
                "sponsor.protocol.lead.organization.duplicate.error");
        failure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                leadOrganizationDialog.clickSave();
            }
        });
    }

    private void removeAndAddLeadOrganization(CreateProtocolPage createPage, ProtocolLeadOrganization leadOrganization) {
        createPage.getHelper().getLeadOrganizationListing(leadOrganization).clickDeleteLeadOrganization();
        createPage.getHelper().checkLeadOrganizationNotSelected(leadOrganization);
        CreateLeadOrganizationDialog leadOrganizationDialog = createPage.clickSelectLeadOrganizationBtn();
        leadOrganizationDialog.getHelper().enterDataAndSave(leadOrganization);
        createPage.getHelper().checkLeadOrganizationSelected(leadOrganization);
    }

    @Test
    public void testAddLeadOrganizationWithNewData() {
        Protocol protocol = ProtocolFactory.getInstance().create();
        protocol.setSponsor(dataSet.getSponsor().getSponsorRepresentativeOrganizations().get(0));
        ProtocolLeadOrganization leadOrganization = new ProtocolLeadOrganization(null,
                orgFactory.createWithoutExternalData(), personFactory.createWithoutExternalData());
        ProtocolLeadOrganization leadOrganizationExistingOrg = new ProtocolLeadOrganization(null,
                getLeadOrganization(), personFactory.createWithoutExternalData());
        ProtocolLeadOrganization leadOrganizationExistingPI = new ProtocolLeadOrganization(null,
                orgFactory.createWithoutExternalData(), getPrincipalInvestigator());
        protocol.getLeadOrganizations().clear();
        protocol.getLeadOrganizations().add(leadOrganization);
        protocol.getLeadOrganizations().add(leadOrganizationExistingOrg);
        protocol.getLeadOrganizations().add(leadOrganizationExistingPI);

        CreateProtocolPage createPage = navigateToCreateProtocolPage();
        CreateLeadOrganizationDialog leadOrganizationDialog = createPage.clickSelectLeadOrganizationBtn();

        enterNewOrganization(leadOrganizationDialog, leadOrganization.getOrganization());
        enterNewPrincipalInvestigator(leadOrganizationDialog, leadOrganization.getPrincipalInvestigator());
        leadOrganizationDialog.clickSave();
        createPage.getHelper().checkLeadOrganizationSelected(leadOrganization);
        removeAndRequeryNewLeadOrganizationItems(createPage, leadOrganization);
        leadOrganizationDialog = createPage.clickSelectLeadOrganizationBtn();
        leadOrganizationDialog.getHelper().enterDataAndSave(leadOrganizationExistingOrg);
        createPage.getHelper().checkLeadOrganizationSelected(leadOrganizationExistingOrg);

        leadOrganizationDialog = createPage.clickSelectLeadOrganizationBtn();
        leadOrganizationDialog.getHelper().enterDataAndSave(leadOrganizationExistingPI);
        createPage.getHelper().checkLeadOrganizationSelected(leadOrganizationExistingPI);

        completeSaveProtcol(createPage, protocol);
    }

    private void completeSaveProtcol(CreateProtocolPage createPage, Protocol protocol) {
        createPage.getHelper().enterProtocolInfo(protocol);
        ProtocolInformationTab protocolInformationTab = createPage.clickSave();
        protocolInformationTab.getHelper().checkProtocolDisplayed(protocol);
    }

    private void enterNewOrganization(CreateLeadOrganizationDialog leadOrganizationDialog, Organization organization) {
        leadOrganizationDialog.clickOrganizationCreateNew();
        checkNewOrganizationFieldErrors(leadOrganizationDialog);
        leadOrganizationDialog.clickOrganizationSearchAgain();
        leadOrganizationDialog.clickOrganizationCreateNew();
        leadOrganizationDialog.getHelper().enterNewOrganizationData(organization);
    }

    private void checkNewOrganizationFieldErrors(final CreateLeadOrganizationDialog leadOrganizationDialog) {
        ExpectedValidationFailure failure = new ExpectedValidationFailure();
        failure.addExpectedText(ERROR_IN_SECTION_PREFIX
                + getPropertyText("sponsor.protocol.lead.organization.select.organization.header"));
        failure.addExpectedText(ERROR_IN_SECTION_PREFIX
                + getPropertyText("sponsor.protocol.lead.organization.select.principal.investigator.header"));
        failure.addExpectedRequiredFields("profile.organization.name", "profile.organization.email",
                "profile.organization.postalAddress.streetAddress", "profile.organization.postalAddress.city",
                "profile.organization.postalAddress.postalCode");
        failure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                leadOrganizationDialog.clickSave();
            }
        });
    }

    private void enterNewPrincipalInvestigator(CreateLeadOrganizationDialog leadOrganizationDialog,
            Person principalInvestigator) {
        leadOrganizationDialog.openPersonSection();
        leadOrganizationDialog.clickPrincipalInvestigatorCreateNew();
        checkNewPrincipalInvestigatorFieldErrors(leadOrganizationDialog);
        leadOrganizationDialog.clickPrincipalInvestigatorSearchAgain();
        leadOrganizationDialog.clickPrincipalInvestigatorCreateNew();
        leadOrganizationDialog.getHelper().enterNewPersonData(principalInvestigator);
    }

    private void checkNewPrincipalInvestigatorFieldErrors(final CreateLeadOrganizationDialog leadOrganizationDialog) {
        ExpectedValidationFailure failure = new ExpectedValidationFailure();
        failure.addExpectedText(ERROR_IN_SECTION_PREFIX
                + getPropertyText("sponsor.protocol.lead.organization.select.principal.investigator.header"));
        failure.addExpectedText(ERROR_IN_SECTION_PREFIX
                + getPropertyText("sponsor.protocol.lead.organization.select.principal.investigator.header"));
        failure.addExpectedRequiredFields("person.firstName", "person.lastName", "person.email",
                "person.postalAddress.streetAddress", "person.postalAddress.city", "person.postalAddress.postalCode");
        failure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                leadOrganizationDialog.clickSave();
            }
        });
    }

    private void removeAndRequeryNewLeadOrganizationItems(CreateProtocolPage createPage,
            ProtocolLeadOrganization leadOrganization) {
        createPage.getHelper().getLeadOrganizationListing(leadOrganization).clickDeleteLeadOrganization();
        CreateLeadOrganizationDialog leadOrganizationDialog = createPage.clickSelectLeadOrganizationBtn();
        leadOrganizationDialog.getHelper().searchAndSelectOrganization(leadOrganization.getOrganization());
        leadOrganizationDialog.getHelper().searchAndSelectPerson(leadOrganization.getPrincipalInvestigator());
        leadOrganizationDialog.clickSave();
        createPage.getHelper().checkLeadOrganizationSelected(leadOrganization);
    }
}
