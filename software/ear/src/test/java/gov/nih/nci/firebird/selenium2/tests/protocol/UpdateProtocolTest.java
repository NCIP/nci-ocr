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

import static gov.nih.nci.firebird.data.FormStatus.*;
import static org.junit.Assert.*;
import com.google.common.collect.Iterables;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolInfoTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.CreateLeadOrganizationDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.EditProtocolPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.LeadOrganizationListing;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolInformationTab;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.junit.Test;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class UpdateProtocolTest extends AbstractFirebirdWebDriverTest {

    private final static Map<RegistrationStatus, FormStatusMappingPredicate> VALID_STATUS_MAPPINGS;

    static {
        VALID_STATUS_MAPPINGS = Maps.newEnumMap(RegistrationStatus.class);
        VALID_STATUS_MAPPINGS.put(RegistrationStatus.NOT_STARTED, new FormStatusMappingPredicate(NOT_STARTED));
        VALID_STATUS_MAPPINGS.put(RegistrationStatus.SUBMITTED, new FormStatusMappingPredicate(SUBMITTED));
        VALID_STATUS_MAPPINGS.put(RegistrationStatus.ACCEPTED, new FormStatusMappingPredicate(ACCEPTED));
        VALID_STATUS_MAPPINGS.put(RegistrationStatus.IN_REVIEW, new FormStatusMappingPredicate(IN_REVIEW, ACCEPTED,
                                                                                               REJECTED, SUBMITTED));
    }

    private static class FormStatusMappingPredicate implements Predicate {

        private final Set<FormStatus> validStatuses;
        private final Iterator<FormStatus> validStatusIterator;

        public FormStatusMappingPredicate(FormStatus... statuses) {
            validStatuses = Sets.newHashSet(statuses);
            validStatusIterator = Iterators.cycle(validStatuses);
        }

        @Override
        public boolean evaluate(Object object) {
            return validStatuses.contains(object);
        }

        public FormStatus getValidStatus() {
            return validStatusIterator.next();
        }

    }

    @Test
    public void testRegistrationProtocolInformationNoChanges() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        FirebirdUser investigator = builder.createInvestigator().get();
        builder.createSponsor();
        builder.createRegistration(investigator);
        DataSet dataSet = builder.build();

        checkProtocolHistoryInvestigator(dataSet);
        checkProtocolHistorySponsor(dataSet);
    }

    private void checkProtocolHistoryInvestigator(DataSet dataSet) {
        AbstractProtocolRegistration registration = dataSet.getInvestigatorRegistration();
        Protocol protocol = dataSet.getProtocol();

        HomePage homePage = openHomePage(dataSet.getInvestigatorLogin());
        RegistrationOverviewTab overviewTab = homePage.getHelper().openInProgressTask(registration);
        ProtocolInfoTab protocolInfoTab = overviewTab.getPage().clickProtocolInfoTab();
        protocolInfoTab.getPage().getHelper().checkRegistrationPageHeader(registration);

        protocolInfoTab.getHelper().checkProtocolDisplayed(protocol);
        protocolInfoTab.getHelper().checkRevisions(protocol, false);
    }

    private void checkProtocolHistorySponsor(DataSet dataSet) {
        Protocol protocol = dataSet.getProtocol();

        ProtocolInformationTab protocolInfoTab = navigateToSponsorProtocolInfo(dataSet.getSponsorLogin(), protocol);
        protocolInfoTab.getHelper().checkRevisions(protocol, true);
    }

    private ProtocolInformationTab navigateToSponsorProtocolInfo(LoginAccount login, Protocol protocol) {
        HomePage homePage = openHomePage(login);
        ProtocolInformationTab protocolInfoTab = homePage.getHelper().goToSponsorProtocol(protocol);
        protocolInfoTab.getHelper().checkProtocolDisplayed(protocol);
        return protocolInfoTab;
    }

    @Test
    public void testRegistrationProtocolInformationWithChanges() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        FirebirdUser investigator = builder.createInvestigator().get();
        builder.createSponsor();
        Protocol protocol = builder
                .createProtocol()
                .withProtocolRevision("This is a change", "1572 is no longer required",
                                      "Phase has been changed from I to II")
                .withProtocolRevision("This is another change",
                                      "Financial Disclosure is no optional for investigators",
                                      "Phase has been changed from II to III").get();
        builder.createRegistration(protocol, investigator);

        DataSet dataSet = builder.build();

        checkProtocolHistoryInvestigator(dataSet);
    }

    @Test
    public void testSponsorDelegateCantEditProtocol() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createSponsor().asDelegate().get();
        builder.createSponsor();
        Protocol protocol = builder.createProtocol().get();

        DataSet dataSet = builder.build();

        ProtocolInformationTab protocolInfoTab = navigateToSponsorProtocolInfo(dataSet.getSponsorLogin(), protocol);
        assertFalse("Sponsor Delegate should not be able to edit protocol details",
                    protocolInfoTab.isEditButtonPresent());
    }

    @Test
    public void testProtocolEdit_Sponsor() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        ProtocolLeadOrganization validLeadOrganizationInfo = new ProtocolLeadOrganization(null,
                                                                                          getExistingNesOrganization(),
                                                                                          getExistingNesPerson());
        builder.createSponsor();
        builder.createRegistration(builder.createInvestigator().get());
        Protocol protocol = builder.createProtocol().get();
        DataSet dataSet = builder.build();
        ProtocolInformationTab protocolInfoTab = navigateToSponsorProtocolInfo(dataSet.getSponsorLogin(), protocol);
        protocolInfoTab = checkProtocolEditWithNoChanges(protocolInfoTab, protocol);
        EditProtocolPage editPage = protocolInfoTab.clickEdit();
        editPage.getHelper().checkProtocolDisplayed(protocol);
        editPage.getHelper().checkSaveButtonToggling(protocol, validLeadOrganizationInfo);
        Protocol editedProtocol = editPage.getHelper().changeProtocol();
        editedProtocol.setSponsor(protocol.getSponsor());
        editedProtocol.setLeadOrganizations(protocol.getLeadOrganizations());
        checkCommentRequired(editPage);
        editPage.typeComment("some comment");
        protocolInfoTab = editPage.clickSave();
        protocolInfoTab.getHelper().checkProtocolDisplayed(editedProtocol);
        checkProtocolHistoryInvestigator(dataSet);
        checkProtocolHistorySponsor(dataSet);
    }

    private ProtocolInformationTab checkProtocolEditWithNoChanges(ProtocolInformationTab protocolInfoTab,
                                                                  Protocol protocol) {
        EditProtocolPage editPage = protocolInfoTab.clickEdit();
        editPage.getHelper().checkProtocolDisplayed(protocol);
        editPage.getHelper().checkProtocolDisplayed(protocol);
        assertFalse(editPage.isSaveButtonPresent());
        protocolInfoTab = editPage.clickCancel(false);
        protocolInfoTab.getHelper().checkProtocolDisplayed(protocol);
        return protocolInfoTab;
    }

    private void checkCommentRequired(final EditProtocolPage editPage) {
        editPage.typeComment("");
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "protocol.change.comment.required");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                editPage.clickSave();
            }
        });
    }

    @Test
    public void testProtocolEdit_DeleteAgent() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createSponsor();
        builder.createRegistration(builder.createInvestigator().get());
        Protocol protocol = builder.createProtocol().get();
        DataSet dataSet = builder.build();
        ProtocolInformationTab protocolInfoTab = navigateToSponsorProtocolInfo(dataSet.getSponsorLogin(), protocol);
        assertEquals(protocol.getAgentListForDisplay(), protocolInfoTab.getAgents());
        EditProtocolPage editPage = protocolInfoTab.clickEdit();

        for (String agent : editPage.getAgentNames()) {
            editPage.deleteAgent(agent);
        }
        editPage.typeComment("Removed Agents");
        protocolInfoTab = editPage.clickSave();
        assertTrue("Agents were not removed", protocolInfoTab.getAgents().isEmpty());
    }

    @Test
    public void testRemoveAllLeadOrgs_Sponsor() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createSponsor();
        Protocol protocol = builder.createProtocol().get();
        DataSet dataSet = builder.build();
        ProtocolInformationTab protocolInfoTab = navigateToSponsorProtocolInfo(dataSet.getSponsorLogin(), protocol);
        EditProtocolPage editPage = protocolInfoTab.clickEdit();
        for (LeadOrganizationListing listing : editPage.getSelectedLeadOrganizationListings()) {
            listing.clickDeleteLeadOrganization();
        }
        editPage.typeComment("removed lead organization");
        protocolInfoTab = editPage.clickSave();
        assertTrue(protocolInfoTab.getLeadOrganizations().isEmpty());
    }

    @Test
    public void testLeadOrgUpdates_Sponsor() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createSponsor();
        Protocol protocol = builder.createProtocol().get();
        ProtocolLeadOrganization existingLeadOrganization = Iterables.getFirst(protocol.getLeadOrganizations(), null);
        ProtocolLeadOrganization leadOrganizationToRemove = protocol.addLeadOrganization(getExistingNesOrganization(),
                                                                                         getExistingNesPerson());
        ProtocolLeadOrganization leadOrganizationSamePI =
                new ProtocolLeadOrganization(null, getExistingNesOrganization(),
                                             existingLeadOrganization.getPrincipalInvestigator());

        DataSet dataSet = builder.build();
        ProtocolInformationTab protocolInfoTab = navigateToSponsorProtocolInfo(dataSet.getSponsorLogin(), protocol);
        EditProtocolPage editPage = protocolInfoTab.clickEdit();
        CreateLeadOrganizationDialog leadOrganizationDialog = editPage.clickSelectLeadOrganizationBtn();
        verifyCannotSelectExistingLeadOrganization(leadOrganizationDialog, existingLeadOrganization);
        leadOrganizationDialog.getHelper().enterDataAndSave(leadOrganizationSamePI);
        editPage.getHelper().checkLeadOrganizationSelected(leadOrganizationSamePI);
        editPage.getHelper().getLeadOrganizationListing(leadOrganizationToRemove).clickDeleteLeadOrganization();
        editPage.getHelper().checkLeadOrganizationNotSelected(leadOrganizationToRemove);

        editPage.typeComment("Testing Lead Organization Updates.");
        protocolInfoTab = editPage.clickSave();
        dataSet.reload();
        protocol = dataSet.getProtocol();
        protocolInfoTab.getHelper().checkProtocolDisplayed(protocol);
        protocolInfoTab.getHelper().checkLeadOrganizationListed(existingLeadOrganization);
        protocolInfoTab.getHelper().checkLeadOrganizationListed(leadOrganizationSamePI);
        protocolInfoTab.getHelper().checkLeadOrganizationNotListed(leadOrganizationToRemove);
    }

    private void verifyCannotSelectExistingLeadOrganization(final CreateLeadOrganizationDialog leadOrganizationDialog,
                                                            final ProtocolLeadOrganization existingLeadOrganization) {
        leadOrganizationDialog.getHelper().searchAndSelectOrganization(
                existingLeadOrganization.getOrganization());
        leadOrganizationDialog.getHelper().searchAndSelectPerson(existingLeadOrganization.getPrincipalInvestigator());
        ExpectedValidationFailure failure = new ExpectedValidationFailure(
                "sponsor.protocol.lead.organization.duplicate.error");
        failure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                leadOrganizationDialog.clickSave();
            }
        });
    }

    @Test
    public void testProtocolEditAsDuplicate_Sponsor() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createSponsor();
        builder.createRegistration(builder.createInvestigator().get());
        Protocol protocol = builder.createProtocol().get();
        Protocol protocol2 = builder.createProtocol().get();
        DataSet dataSet = builder.build();
        ProtocolInformationTab protocolInfoTab = navigateToSponsorProtocolInfo(dataSet.getSponsorLogin(), protocol);
        EditProtocolPage editPage = protocolInfoTab.clickEdit();
        editPage.getHelper().checkProtocolDisplayed(protocol);

        editPage.getHelper().changeProtocol(protocol2, "Duplicate Protocol");
        checkForDuplicateProtocolError(editPage);
    }

    private void checkForDuplicateProtocolError(final EditProtocolPage editPage) {
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "sponsor.protocol.duplicate.number.error");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                editPage.clickSave();

            }
        });
    }

    @Test
    public void testProtocolEditCancelChange_Sponsor() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createSponsor();
        builder.createRegistration(builder.createInvestigator().get());
        Protocol protocol = builder.createProtocol().get();
        DataSet dataSet = builder.build();
        ProtocolInformationTab protocolInfoTab = navigateToSponsorProtocolInfo(dataSet.getSponsorLogin(), protocol);
        EditProtocolPage editPage = protocolInfoTab.clickEdit();
        protocolInfoTab = editPage.clickCancel(true);
        editPage = protocolInfoTab.clickEdit();
        editPage.setProtocolNumber("new number");
        editPage.clickCancel(true);
        assertEquals(protocol.getProtocolNumber(), protocolInfoTab.getSponsorProtocolId());
    }

    @Test
    public void updateProtocolWithRegistrationTest() {
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        builder.createSponsor();
        builder.createRegistration(builder.createInvestigator().get());
        builder.createProtocol();
        DataSet dataSet = builder.build();
        int expectedEmailCount = 0;
        for (RegistrationStatus registrationStatus : RegistrationStatus.values()) {
            InvestigatorRegistration registration = setRegistrationStatus(dataSet, registrationStatus);

            modifyProtocol(dataSet);

            boolean resubmitRequiredForUpdate = registration.isResubmitRequiredForUpdate();
            boolean notificationRequiredForUpdate = registration.isNotificationRequiredForUpdate();
            dataSet.reload();
            registration = dataSet.getInvestigatorRegistration();
            if (resubmitRequiredForUpdate) {
                assertEquals(
                        "Expected registration status change to In Progress when updating a protocol with a registration with status "
                        + registrationStatus.getDisplay(), RegistrationStatus.PROTOCOL_UPDATED,
                        registration.getStatus());
                checkFormsStatus(registration, FormStatus.IN_PROGRESS);
            } else {
                assertEquals(
                        "Expected registration status to not change when updating a protocol with a registration with status "
                        + registrationStatus.getDisplay(), registrationStatus, registration.getStatus());
                checkFormsStatus(registration);
            }
            if (notificationRequiredForUpdate) {
                assertEquals("Expected an email when updating a protocol with a registration with status "
                             + registrationStatus.getDisplay(), ++expectedEmailCount,
                             getEmailChecker().getSentEmailSize());
            } else {
                assertEquals("Expected no email when updating a protocol with a registration with status "
                             + registrationStatus.getDisplay(), expectedEmailCount,
                             getEmailChecker().getSentEmailSize());
            }
        }
    }

    private InvestigatorRegistration setRegistrationStatus(DataSet dataSet, RegistrationStatus registrationStatus) {
        InvestigatorRegistration registration = dataSet.getInvestigatorRegistration();
        registration.setStatus(registrationStatus);
        setFormStatuses(registration, registrationStatus);
        dataSet.update(registration);
        return registration;
    }

    private void setFormStatuses(AbstractProtocolRegistration registration, RegistrationStatus registrationStatus) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            FormStatusMappingPredicate mapping = VALID_STATUS_MAPPINGS.get(registrationStatus);
            if (mapping != null) {
                form.setFormStatus(mapping.getValidStatus());
            } else {
                form.setFormStatus(IN_PROGRESS);
            }
        }
    }

    private void modifyProtocol(DataSet dataSet) {
        ProtocolInformationTab protocolInfoTab = navigateToSponsorProtocolInfo(dataSet.getSponsorLogin(),
                                                                               dataSet.getProtocol());
        EditProtocolPage editPage = protocolInfoTab.clickEdit();
        Protocol editedProtocol = editPage.getHelper().changeProtocol();
        protocolInfoTab = editPage.clickSave();

        checkChanges(protocolInfoTab, dataSet, editedProtocol);
    }

    private void checkChanges(ProtocolInformationTab protocolInfoTab, DataSet dataSet, Protocol editedProtocol) {
        assertEquals(editedProtocol.getProtocolNumber(), protocolInfoTab.getSponsorProtocolId());
        assertEquals(editedProtocol.getProtocolTitle(), protocolInfoTab.getProtocolTitle());
        assertEquals(editedProtocol.getAgentListForDisplay(), protocolInfoTab.getAgents());
        assertEquals(editedProtocol.getPhase().getDisplay(), protocolInfoTab.getPhase());
        assertEquals(dataSet.getProtocol().getSponsor().getName(), protocolInfoTab.getSponsor());
    }

    private void checkFormsStatus(InvestigatorRegistration registration, FormStatus status) {
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            assertEquals("For Registration Status: " + form.getRegistration().getStatus() + " expected " + status
                         + " but found " + form.getFormStatus(), status, form.getFormStatus());
        }
    }

    private void checkFormsStatus(InvestigatorRegistration registration) {
        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            FormStatusMappingPredicate mapping = VALID_STATUS_MAPPINGS.get(form.getRegistration().getStatus());
            if (mapping != null) {
                assertTrue(form.getFormStatus() + " is not a valid status for Registration Status: "
                           + form.getRegistration().getStatus(), mapping.evaluate(form.getFormStatus()));
            } else {
                assertEquals("For Registration Status: " + form.getRegistration().getStatus() + " expected "
                             + IN_PROGRESS + " but found " + form.getFormStatus(), IN_PROGRESS, form.getFormStatus());
            }
        }
    }
}