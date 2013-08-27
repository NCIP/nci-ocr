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

import static gov.nih.nci.firebird.selenium2.scalability.tests.TimedAction.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.support.AbstractLoadableComponent;
import gov.nih.nci.firebird.commons.test.TestFileUtils;
import gov.nih.nci.firebird.data.AbstractOrganizationRole;
import gov.nih.nci.firebird.data.Address;
import gov.nih.nci.firebird.data.BoardCertifiedSpecialty;
import gov.nih.nci.firebird.data.Certification;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.Fellowship;
import gov.nih.nci.firebird.data.InstitutionalReviewBoard;
import gov.nih.nci.firebird.data.Internship;
import gov.nih.nci.firebird.data.MedicalLicense;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.Residency;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.WorkHistory;
import gov.nih.nci.firebird.selenium2.pages.components.tags.OrganizationDisplayTag;
import gov.nih.nci.firebird.selenium2.pages.components.tags.ViewPersonTag;
import gov.nih.nci.firebird.selenium2.pages.components.tags.search.SearchResultListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.AddOrganizationAssociationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.CertificationSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.CredentialsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.DegreeSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.DesigneeAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.EditSpecialtyCredentialDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.EditTrainingCertificateDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.EditWorkHistoryDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.FellowshipSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.FilesTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.InternshipSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.InvestigatorProfilePage;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.LicenseSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageCertificationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageDegreeDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageLicenseDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageSpecialtyDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.OrganizationAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ResidencySection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.SetPracticeSiteFieldsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.SpecialtySection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.SubInvestigatorAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.TrainingCertificateSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.WorkHistorySection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ManagePersonDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.OrganizationSearchDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ProfessionalContactInformationTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SelectOrderingDesigneeDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SelectShippingDesigneeDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubInvestigatorAssociationFormDialog;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSet;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class InvestigatorProfileTest extends AbstractScalabilityTest {

    private static final CredentialFactory CREDENTIAL_FACTORY = CredentialFactory.getInstance();
    private DataSet dataSet;
    private PrimaryOrganization originalPrimaryOrganization;
    private Organization newPrimaryOrganization;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getBuilder().createInvestigator().asCtepUser();
        dataSet = getBuilder().build();
        originalPrimaryOrganization = dataSet.getInvestigatorProfile().getPrimaryOrganization();
    }

    @Test
    public void testProfileCompletion() throws IOException {
        InvestigatorProfilePage profilePage = openHomePage(dataSet.getInvestigatorLogin(), getCtepProvider())
                .getInvestigatorMenu().click().getPage();
        updateContactInformation(profilePage.clickProfessionalContactInformationTab());
        completeCredentialsTab(profilePage.clickCredentialsTab());
        completeOrganizationAssociationsTab(profilePage.clickOrganizationAssociationsTab());
        completeSubInvestigatorAssociationsTab(profilePage.clickSubInvestigatorAssociationsTab());
        completeDesigneeAssociationsTab(profilePage.clickDesigneeAssociationsTab());
        completeFilesTab(profilePage.clickFilesTab());
    }

    private void updateContactInformation(ProfessionalContactInformationTab contactInformationTab) {
        updatePersonData(contactInformationTab);
        updatePrimaryOrganization(contactInformationTab);
    }

    private void updatePersonData(ProfessionalContactInformationTab contactInformationTab) {
        final ManagePersonDialog managePersonDialog = contactInformationTab.clickEditContactInformation();
        Person updatedPersonData = PersonFactory.getInstance().create();
        managePersonDialog.getHelper().enterPersonData(updatedPersonData);
        new TimedAction<Void>("Save Updated Person Data", MAX_NES_RESPONSE_TIME_SECONDS) {
            @Override
            public Void perform() {
                managePersonDialog.clickSave();
                return null;
            }
        }.time();
        contactInformationTab.getHelper().checkForPendingUpdateMessage();
    }

    private void updatePrimaryOrganization(ProfessionalContactInformationTab contactInformationTab) {
        final OrganizationSearchDialog organizationSearchDialog = contactInformationTab.clickSearchOrganizationAgain();
        newPrimaryOrganization = getExistingPrimaryOrganization();
        final SearchResultListing<ProfessionalContactInformationTab> result = 
                new TimedAction<SearchResultListing<ProfessionalContactInformationTab>>("Search for Primary Organization", 
                MAX_NES_RESPONSE_TIME_SECONDS) {
            @Override
            public SearchResultListing<ProfessionalContactInformationTab> perform() {
                organizationSearchDialog.getHelper().search(newPrimaryOrganization);
                return organizationSearchDialog.getHelper().getMatchingResult(newPrimaryOrganization);
            }
        }.time();
        new TimedAction<ProfessionalContactInformationTab>("Save Changed Primary Organization", 
                MAX_SELECT_PRIMARY_ORGANIZATION_RESPONSE_TIME_SECONDS) {
            @Override
            public ProfessionalContactInformationTab perform() {
                return result.clickSelect();
            }
        }.time();
        contactInformationTab.getHelper().verifyOrganization(newPrimaryOrganization);
        newPrimaryOrganization = dataSet.getLastCreatedObject(Organization.class);
    }

    private void completeCredentialsTab(CredentialsTab credentialsTab) throws IOException {
        saveAndVerifyNewDegree(credentialsTab.getDegreeSection());
        saveAndVerifyNewLicense(credentialsTab.getLicenseSection());
        saveAndVerifyNewSpecialty(credentialsTab.getSpecialtySection());
        saveAndVerifyNewCertification(credentialsTab.getCertificationSection());
        saveAndVerifyNewCertificate(credentialsTab.getTrainingSection());
        saveAndVerifyNewWorkHistory(credentialsTab.getWorkHistorySection());
        saveAndVerifyNewInternship(credentialsTab.getInternshipSection());
        saveAndVerifyNewResidency(credentialsTab.getResidencySection());
        saveAndVerifyNewFellowship(credentialsTab.getFellowshipSection());
    }

    protected void saveAndVerifyNewDegree(DegreeSection degreeSection) {
        Degree degree = CREDENTIAL_FACTORY.createDegree(dataSet.getCredentialTypesData().getDegreeType(),
                getExistingNesOrganization());
        final ManageDegreeDialog createDegreeDialog = degreeSection.clickAddDegree();
        createDegreeDialog.getHelper().enterDegreeData(degree);
        new TimedAction<Void>("Save New Degree") {
            @Override
            public Void perform() {
                createDegreeDialog.clickSave();
                return null;
            }
        }.time();
        assertNotNull(degreeSection.getHelper().getListing(degree));
    }

    protected void saveAndVerifyNewLicense(LicenseSection licenseSection) {
        MedicalLicense license = CREDENTIAL_FACTORY.createLicense(dataSet.getCredentialTypesData().getLicenseType());
        final ManageLicenseDialog createLicenseDialog = licenseSection.clickAddLicense();
        createLicenseDialog.getHelper().enterLicenseData(license);
        new TimedAction<Void>("Save New Medical License") {
            @Override
            public Void perform() {
                createLicenseDialog.clickSave();
                return null;
            }
        }.time();
        licenseSection.getHelper().verifyLicenseListed(license);
    }

    protected void saveAndVerifyNewSpecialty(SpecialtySection specialtySection) {
        BoardCertifiedSpecialty specialty = CREDENTIAL_FACTORY.createSpecialty();
        final ManageSpecialtyDialog createSpecialtyDialog = specialtySection.clickAddSpecialty();
        createSpecialtyDialog.getHelper().enterSpecialtyData(specialty);
        new TimedAction<Void>("Save New Board Certified Specialty") {
            @Override
            public Void perform() {
                createSpecialtyDialog.clickSave();
                return null;
            }
        }.time();
        specialtySection.getHelper().verifySpecialtyListed(specialty);
    }

    protected void saveAndVerifyNewCertification(CertificationSection certificationSection) {
        Certification certification = CREDENTIAL_FACTORY.createCertification(dataSet.getCredentialTypesData()
                .getCertificationType());
        final ManageCertificationDialog createCertificationDialog = certificationSection.clickAddCertification();
        createCertificationDialog.getHelper().enterCertificationData(certification);
        new TimedAction<Void>("Save New Certification") {
            @Override
            public Void perform() {
                createCertificationDialog.clickSave();
                return null;
            }
        }.time();
        certificationSection.getHelper().verifyCertificationListed(certification);
    }

    protected void saveAndVerifyNewCertificate(TrainingCertificateSection trainingSection) throws IOException {
        TrainingCertificate certificate = CREDENTIAL_FACTORY.createCertificate(getExistingNesOrganization());
        File certificateFile = TestFileUtils.createTemporaryFile();
        final EditTrainingCertificateDialog certificateDialog = trainingSection.clickAddCertificate();
        certificateDialog.getHelper().enterTrainingCertificateData(certificate, certificateFile, false);
        new TimedAction<Void>("Save New Certificate") {
            @Override
            public Void perform() {
                certificateDialog.clickSave();
                return null;
            }
        }.time();
        trainingSection.getHelper().verifyCertificateListed(certificate);
    }

    protected void saveAndVerifyNewWorkHistory(WorkHistorySection section) {
        WorkHistory workHistory = CREDENTIAL_FACTORY.createWorkHistory(getExistingNesOrganization());
        final EditWorkHistoryDialog createDialog = section.clickAddWorkHistory();
        createDialog.getHelper().setWorkHistory(workHistory);
        new TimedAction<Void>("Save New Work History") {
            @Override
            public Void perform() {
                createDialog.clickSave();
                return null;
            }
        }.time();
        section.getHelper().verifyWorkHistoryListed(workHistory);
    }

    protected void saveAndVerifyNewInternship(InternshipSection section) {
        Internship internship = CREDENTIAL_FACTORY.createInternship(getExistingNesOrganization());
        final EditSpecialtyCredentialDialog createDialog = section.clickAddCredential();
        createDialog.getHelper().setCredential(internship);
        new TimedAction<Void>("Save New Internship") {
            @Override
            public Void perform() {
                createDialog.clickSave();
                return null;
            }
        }.time();
        section.getHelper().verifyCredentialListed(internship);
    }

    protected void saveAndVerifyNewResidency(ResidencySection section) {
        Residency residency = CREDENTIAL_FACTORY.createResidency(getExistingNesOrganization());
        final EditSpecialtyCredentialDialog createDialog = section.clickAddCredential();
        createDialog.getHelper().setCredential(residency);
        new TimedAction<Void>("Save New Residency") {
            @Override
            public Void perform() {
                createDialog.clickSave();
                return null;
            }
        }.time();
        section.getHelper().verifyCredentialListed(residency);
    }

    protected void saveAndVerifyNewFellowship(FellowshipSection section) {
        Fellowship fellowship = CREDENTIAL_FACTORY.createFellowship(getExistingNesOrganization());
        final EditSpecialtyCredentialDialog createDialog = section.clickAddCredential();
        createDialog.getHelper().setCredential(fellowship);
        new TimedAction<Void>("Save New Fellowship") {
            @Override
            public Void perform() {
                createDialog.clickSave();
                return null;
            }
        }.time();
        section.getHelper().verifyCredentialListed(fellowship);
    }

    private void completeOrganizationAssociationsTab(OrganizationAssociationsTab associationsTab) {
        addPracticeSite(associationsTab);
        addPracticeSite(associationsTab);
        addIrb(associationsTab);
        addIrb(associationsTab);
        addLab(associationsTab);
        addLab(associationsTab);
    }

    private void addPracticeSite(OrganizationAssociationsTab associationsTab) {
        AddOrganizationAssociationDialog associationDialog = associationsTab.getPracticeSiteSection().clickCreateNew();
        PracticeSite practiceSite = getNesTestDataSource().getPracticeSite();
        final SearchResultListing<AbstractLoadableComponent<?>> listing = 
                searchForAssociatedOrganization(associationDialog, practiceSite, "Search for Practice Site");
        final SetPracticeSiteFieldsDialog ohrpDialog = new TimedAction<SetPracticeSiteFieldsDialog>("Select Practice Site") {
            @Override
            public SetPracticeSiteFieldsDialog perform() {
                return (SetPracticeSiteFieldsDialog) listing.clickSelect();
            }
        }.time();
        ohrpDialog.typeOhrpNumber(ValueGenerator.getUniqueOhrpNumber());
        new TimedAction<Void>("Save Practice Site") {
            @Override
            public Void perform() {
                ohrpDialog.clickSaveButton();
                return null;
            }
        }.time();
    }

    private void addIrb(OrganizationAssociationsTab associationsTab) {
        AddOrganizationAssociationDialog associationDialog = associationsTab.getIrbSection().clickCreateNew();
        InstitutionalReviewBoard irb = getNesTestDataSource().getIrb();
        final SearchResultListing<AbstractLoadableComponent<?>> listing = 
                searchForAssociatedOrganization(associationDialog, irb, "Search for IRB");
        new TimedAction<Void>("Select IRB") {
            @Override
            public Void perform() {
                listing.clickSelect();
                return null;
            }
        }.time();
    }

    private void addLab(OrganizationAssociationsTab associationsTab) {
        final AddOrganizationAssociationDialog associationDialog = associationsTab.getClinicalLabSection().clickCreateNew();
        final ClinicalLaboratory lab = getNesTestDataSource().getClinicalLab();
        final SearchResultListing<AbstractLoadableComponent<?>> listing = 
                searchForAssociatedOrganization(associationDialog, lab, "Search for Lab");
        new TimedAction<Void>("Select Lab") {
            @Override
            public Void perform() {
                listing.clickSelect();
                return null;
            }
        }.time();
    }

    private SearchResultListing<AbstractLoadableComponent<?>> searchForAssociatedOrganization(final AddOrganizationAssociationDialog associationDialog,
            final AbstractOrganizationRole role, String actionName) {
        return new TimedAction<SearchResultListing<AbstractLoadableComponent<?>>>(actionName, MAX_NES_RESPONSE_TIME_SECONDS) {
            @Override
            public SearchResultListing<AbstractLoadableComponent<?>> perform() {
                associationDialog.getHelper().search(role);
                return associationDialog.getHelper().getListing(role);
            }
        }.time();
    }

    private void completeSubInvestigatorAssociationsTab(SubInvestigatorAssociationsTab associationsTab) {
        addSubinvestigator(associationsTab);
        addSubinvestigator(associationsTab);
    }

    private void addSubinvestigator(SubInvestigatorAssociationsTab associationsTab) {
        final SubInvestigatorAssociationFormDialog associationDialog = associationsTab.clickAddNew();
        final Person newSubinvestigator = getExistingNesPerson();
        final SearchResultListing<AbstractLoadableComponent<?>> result = 
                new TimedAction<SearchResultListing<AbstractLoadableComponent<?>>>("Search for New Subinvestigator") {
            @Override
            public SearchResultListing<AbstractLoadableComponent<?>> perform() {
                associationDialog.getHelper().searchForPerson(newSubinvestigator);
                return associationDialog.getHelper().getMatchingResult(newSubinvestigator);
            }
        }.time();
        new TimedAction<Void>("Select Subinvestigator") {
            @Override
            public Void perform() {
                result.clickSelect();
                return null;
            }
        }.time();
        associationsTab.getHelper().assertListed(newSubinvestigator);
    }

    private void completeDesigneeAssociationsTab(DesigneeAssociationsTab associationsTab) {
        setShippingDesignee(associationsTab);
        addOrderingDesignee(associationsTab);
        addOrderingDesignee(associationsTab);
    }

    private void setShippingDesignee(DesigneeAssociationsTab associationsTab) {
        final SelectShippingDesigneeDialog designeeDialog = associationsTab.clickSelectShippingDesignee();
        Person shippingDesignee = getExistingNesPerson();
        final Organization organization = getExistingNesOrganization();
        Address shippingAddress = ValueGenerator.getUniqueAddress();
        selectShippingDesigneeOrganization(designeeDialog, organization);
        selectShippingDesigneePerson(designeeDialog, shippingDesignee);
        designeeDialog.getHelper().setShippingAddress(shippingAddress);
        new TimedAction<Void>("Save Shipping Designee") {
            @Override
            public Void perform() {
                designeeDialog.clickSave();
                return null;
            }
        }.time();
        associationsTab.getHelper().assertShippingDesigneeListed(shippingDesignee, organization, shippingAddress);
    }

    private void selectShippingDesigneeOrganization(final SelectShippingDesigneeDialog designeeDialog,
            final Organization organization) {
        final SearchResultListing<OrganizationDisplayTag> result = 
                new TimedAction<SearchResultListing<OrganizationDisplayTag>>("Search for Shipping Designee Organization") {
            @Override
            public SearchResultListing<OrganizationDisplayTag> perform() {
                designeeDialog.getHelper().searchForOrganization(organization);
                return designeeDialog.getHelper().getMatchingOrganizationResult(organization);
            }
        }.time();
        new TimedAction<Void>("Select Shipping Designee Organization") {
            @Override
            public Void perform() {
                result.clickSelect();
                return null;
            }
        }.time();
    }

    private void selectShippingDesigneePerson(final SelectShippingDesigneeDialog designeeDialog, final Person shippingDesignee) {
        final SearchResultListing<ViewPersonTag> result = 
                new TimedAction<SearchResultListing<ViewPersonTag>>("Search for Shipping Designee Person") {
            @Override
            public SearchResultListing<ViewPersonTag> perform() {
                designeeDialog.getHelper().searchForPerson(shippingDesignee);
                return designeeDialog.getHelper().getMatchingPersonResult(shippingDesignee);
            }
        }.time();
        new TimedAction<Void>("Select Shipping Designee Person") {
            @Override
            public Void perform() {
                result.clickSelect();
                return null;
            }
        }.time();
    }

    private void addOrderingDesignee(DesigneeAssociationsTab associationsTab) {
        final SelectOrderingDesigneeDialog designeeDialog = associationsTab.clickAddOrderingDesignee();
        final Person orderingDesignee = getExistingNesPerson();
        final SearchResultListing<AbstractLoadableComponent<?>> result = 
                new TimedAction<SearchResultListing<AbstractLoadableComponent<?>>>("Search for Ordering Designee") {
            @Override
            public SearchResultListing<AbstractLoadableComponent<?>> perform() {
                designeeDialog.getHelper().searchForPerson(orderingDesignee);
                return designeeDialog.getHelper().getMatchingResult(orderingDesignee);
            }
        }.time();
        new TimedAction<Void>("Select Ordering Designee") {
            @Override
            public Void perform() {
                result.clickSelect();
                return null;
            }
        }.time();
        associationsTab.getHelper().assertOrderingDesigneeListed(orderingDesignee);
    }

    private void completeFilesTab(FilesTab filesTab) throws IOException {
        addFile(filesTab);
        addFile(filesTab);
    }

    private void addFile(final FilesTab filesTab) throws IOException {
        final File file = TestFileUtils.createTemporaryFile();
        final String description = ValueGenerator.getUniqueString();
        new TimedAction<Void>("Upload File") {
            @Override
            public Void perform() {
                filesTab.getHelper().uploadFile(file, description);
                return null;
            }
        }.time();
        filesTab.getHelper().assertListed(file, description);
    }

    @Override
    protected void deleteTestData() {
        dataSet.reload();
        deleteInvestigator(dataSet, dataSet.getInvestigator());
        delete(dataSet, newPrimaryOrganization);
        delete(dataSet, originalPrimaryOrganization);
        if (originalPrimaryOrganization != null) {
            delete(dataSet, originalPrimaryOrganization.getOrganization());
        }
    }

}
