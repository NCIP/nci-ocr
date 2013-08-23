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

import static gov.nih.nci.firebird.commons.test.TestFileUtils.*;
import static gov.nih.nci.firebird.data.FormTypeEnum.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.Certificate;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InstitutionalReviewBoard;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.LaboratoryCertificateType;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.AddOrganizationAssociationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ClinicalLabSection.ClinicalLabListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ConfirmDeleteCertificationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ConfirmDeleteDegreeDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ConfirmDeleteLicenseDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ConfirmDeleteSpecialtyDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ConfirmDeleteTrainingCertificateDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.CredentialsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.EditTrainingCertificateDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.IrbSection.IrbListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.LabCertificatesDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.OrganizationAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.PracticeSiteSection.PracticeSiteListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.ManagePersonDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.contact.OrganizationSearchDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab.Question;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab.CertificateListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubInvestigatorAssociationFormDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProfileSubinvestigatorsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.RegistrationOverviewTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.SubinvestigatorsTab;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

public class ReviseReturnedRegistrationFormsTest extends AbstractFirebirdWebDriverTest {

    private DataSet dataSet;
    private InvestigatorRegistration registration;
    private HomePage homePage;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        registration = builder
                .createRegistration()
                .complete()
                .withAllFormsRejected()
                .withStatus(RegistrationStatus.RETURNED).get();
        dataSet = builder.build();
        homePage = openHomePage(dataSet.getInvestigatorLogin());
    }

    @Test
    public void testSaveProfessionalContactInformation() {
        ManagePersonDialog personDialog = homePage.getInvestigatorMenu().clickProfile().clickEditContactInformation();
        registration.getProfile().getPerson().getPostalAddress().setDeliveryAddress(ValueGenerator.getUniqueString());
        personDialog.getHelper().enterPersonData(registration.getProfile().getPerson());
        personDialog.clickSave();
        checkFormsAreRevised(CV, FINANCIAL_DISCLOSURE_FORM, FORM_1572);
    }

    private void checkFormsAreRevised(FormTypeEnum... forms) {
        dataSet.reload();
        registration = dataSet.getInvestigatorRegistration();
        EnumSet<FormTypeEnum> revisedFormTypes;
        if (forms.length == 0) {
            revisedFormTypes = EnumSet.noneOf(FormTypeEnum.class);
        } else {
            revisedFormTypes = EnumSet.copyOf(Arrays.asList(forms));
        }
        checkFormsRevised(revisedFormTypes);
        EnumSet<FormTypeEnum> notRevisedFormTypes = EnumSet.complementOf(revisedFormTypes);
        checkFormsNotRevised(notRevisedFormTypes);
    }

    private void checkFormsRevised(EnumSet<FormTypeEnum> formTypes) {
        for (FormTypeEnum formType : formTypes) {
            assertEquals("Expected " + formType.name() + " to have a status of Revised", FormStatus.REVISED,
                    registration.getForm(formType).getFormStatus());
        }
    }

    private void checkFormsNotRevised(EnumSet<FormTypeEnum> formTypes) {
        for (FormTypeEnum formType : formTypes) {
            if (registration.getForm(formType) != null) {
                assertFalse("Expected " + formType.name() + " to not have a status of Revised",
                        FormStatus.REVISED == registration.getForm(formType).getFormStatus());
            }
        }
    }

    @Test
    public void testSelectPrimaryOrganization() {
        Organization organization = getExistingPrimaryOrganization();
        OrganizationSearchDialog organizationSearchDialog = homePage.getInvestigatorMenu().clickProfile().clickSearchOrganizationAgain();
        organizationSearchDialog.getHelper().searchAndSelect(organization);
        checkFormsAreRevised(CV, FINANCIAL_DISCLOSURE_FORM);
    }

    @Test
    public void testSaveDegree()  {
        CredentialsTab credentialsTab = openCredentialsTab();
        credentialsTab.getDegreeSection().getListings().get(0).clickEditLink().clickSave();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testDeleteDegree() {
        CredentialsTab credentialsTab = openCredentialsTab();
        ConfirmDeleteDegreeDialog confirmDialog = credentialsTab.getDegreeSection().getListings().get(0).clickDeleteLink();
        confirmDialog.clickDelete();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testSaveLicense() {
        CredentialsTab credentialsTab = openCredentialsTab();
        credentialsTab.getLicenseSection().getListings().get(0).clickEditLink().clickSave();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testDeleteLicense() {
        CredentialsTab credentialsTab = openCredentialsTab();
        ConfirmDeleteLicenseDialog confirmDialog = credentialsTab.getLicenseSection().getListings().get(0).clickDeleteLink();
        confirmDialog.clickDelete();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testSaveSpecialty() {
        CredentialsTab credentialsTab = openCredentialsTab();
        credentialsTab.getSpecialtySection().getListings().get(0).clickEditLink().clickSave();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testDeleteSpecialty()  {
        CredentialsTab credentialsTab = openCredentialsTab();
        ConfirmDeleteSpecialtyDialog confirmDialog = credentialsTab.getSpecialtySection().getListings().get(0).clickDeleteLink();
        confirmDialog.clickDelete();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testSaveCertification() {
        CredentialsTab credentialsTab = openCredentialsTab();
        credentialsTab.getCertificationSection().getListings().get(0).clickEditLink().clickSave();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testDeleteCertification() {
        CredentialsTab credentialsTab = openCredentialsTab();
        ConfirmDeleteCertificationDialog confirmDialog = credentialsTab.getCertificationSection().getListings().get(0).clickDeleteLink();
        confirmDialog.clickDelete();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testSaveCertificate() throws IOException {
        CredentialsTab credentialsTab = openCredentialsTab();
        addCertificate(credentialsTab);
        checkFormsAreRevised(CV);
    }

    @Test
    public void testEditCertificate_InUse() {
        CredentialsTab credentialsTab = openCredentialsTab();
        credentialsTab.getTrainingSection().getListings().get(0).clickEditLink().clickSave();
        checkFormsAreRevised(CV, HUMAN_RESEARCH_CERTIFICATE);
    }

    @Test
    public void testEditCertificate_NotInUse() throws IOException {
        CredentialsTab credentialsTab = openCredentialsTab();
        TrainingCertificate certificate = addCertificate(credentialsTab);
        checkFormsAreRevised(CV);
        setCurriculumVitaeFormToRejected();
        credentialsTab.getTrainingSection().getHelper().getListing(certificate).clickEditLink().clickSave();
        checkFormsAreRevised(CV);
    }

    private void setCurriculumVitaeFormToRejected() {
        registration.getCurriculumVitaeForm().setFormStatus(FormStatus.REJECTED);
        dataSet.update(registration);
        registration = dataSet.reloadObject(registration);
        checkFormsAreRevised();
    }

    @Test
    public void testDeleteCertificate_InUse() {
        CredentialsTab credentialsTab = openCredentialsTab();
        ConfirmDeleteTrainingCertificateDialog confirmDialog = credentialsTab.getTrainingSection().getListings().get(0).clickDeleteLink();
        confirmDialog.clickDelete();
        checkFormsAreRevised(CV, HUMAN_RESEARCH_CERTIFICATE);
    }

    @Test
    public void testDeleteCertificate_NotInUse() throws IOException {
        CredentialsTab credentialsTab = openCredentialsTab();
        TrainingCertificate certificate = addCertificate(credentialsTab);
        checkFormsAreRevised(CV);
        setCurriculumVitaeFormToRejected();
        credentialsTab.getTrainingSection().getHelper().getListing(certificate).clickDeleteLink().clickDelete();
        checkFormsAreRevised(CV);
    }

    private TrainingCertificate addCertificate(CredentialsTab credentialsTab) throws IOException {
        EditTrainingCertificateDialog addCertificateDialog = credentialsTab.getTrainingSection().clickAddCertificate();
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate(
                getExistingExternalOrganization());
        addCertificateDialog.getHelper().enterTrainingCertificateData(certificate, createTemporaryFile(), false);
        addCertificateDialog.clickSave();
        return certificate;
    }

    @Test
    public void testSaveExperience() {
        CredentialsTab credentialsTab = openCredentialsTab();
        credentialsTab.getExperienceSection().clickEditExperience().clickSave();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testSavePracticeSite() {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        AddOrganizationAssociationDialog addDialog = organizationAssociationsTab.getPracticeSiteSection().clickCreateNew();
        PracticeSite practiceSite = getTestDataSource().getPracticeSite();
        addDialog.getHelper().searchAndSelectOrganization(practiceSite);
        checkFormsAreRevised();
    }

    @Test
    public void testDeletePracticeSite_InUse() {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        Organization practiceSite = registration.getForm1572().getPracticeSites().iterator().next();
        PracticeSiteListing listing = organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite);
        listing.clickRemoveLink().clickConfirmButton();
        checkFormsAreRevised(FORM_1572);
    }

    @Test
    public void testDeletePracticeSite_NotInUse() {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        Organization practiceSite = registration.getForm1572().getPracticeSites().iterator().next();
        PracticeSiteListing listing = organizationAssociationsTab.getPracticeSiteSection().getHelper().getAnyOtherListing(practiceSite);
        listing.clickRemoveLink().clickConfirmButton();
        checkFormsAreRevised();
    }

    @Test
    public void testSaveClinicalLab() {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        AddOrganizationAssociationDialog addDialog = organizationAssociationsTab.getClinicalLabSection().clickCreateNew();
        ClinicalLaboratory lab = getTestDataSource().getClinicalLab();
        addDialog.getHelper().setOrganization(lab);
        checkFormsAreRevised();
    }

    @Test
    public void testDeleteClinicalLab_InUse() {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        Organization clinicalLab = registration.getForm1572().getLabs().iterator().next();
        ClinicalLabListing listing = organizationAssociationsTab.getClinicalLabSection().getHelper().getListing(clinicalLab);
        listing.clickRemoveLink().clickConfirmButton();
        checkFormsAreRevised(FORM_1572);
    }

    @Test
    public void testDeleteClinicalLab_NotInUse() {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        Organization clinicalLab = registration.getForm1572().getLabs().iterator().next();
        ClinicalLabListing listing = organizationAssociationsTab.getClinicalLabSection().getHelper().getAnyOtherListing(clinicalLab);
        listing.clickRemoveLink().clickConfirmButton();
        checkFormsAreRevised();
    }

    @Test
    public void testSaveIrb() {
        addIrb();
        checkFormsAreRevised();
    }

    private void addIrb() {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        AddOrganizationAssociationDialog addDialog = organizationAssociationsTab.getIrbSection().clickCreateNew();
        InstitutionalReviewBoard irb = getTestDataSource().getIrb();
        addDialog.getHelper().setOrganization(irb);
    }

    @Test
    public void testDeleteIrb_InUse() {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        Organization irb = registration.getForm1572().getIrbs().iterator().next();
        IrbListing listing = organizationAssociationsTab.getIrbSection().getHelper().getListing(irb);
        listing.clickRemoveLink().clickConfirmButton();
        checkFormsAreRevised(FORM_1572);
    }

    @Test
    public void testDeleteIrb_NotInUse() {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        addIrb();
        Organization irb = registration.getForm1572().getIrbs().iterator().next();
        IrbListing listing = organizationAssociationsTab.getIrbSection().getHelper().getAnyOtherListing(irb);
        listing.clickRemoveLink().clickConfirmButton();
        checkFormsAreRevised();
    }

    @Test
    public void testSaveClinicalLabCertificate_InUse() throws IOException {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        ClinicalLaboratory clinicalLab = getInUseClinicalLab();
        LabCertificatesDialog certificatesDialog =
                clickCertificatesLink(organizationAssociationsTab, clinicalLab);
        addLabCertificate(certificatesDialog);
        checkFormsAreRevised(FORM_1572);
    }

    private ClinicalLaboratory getInUseClinicalLab() {
        return (ClinicalLaboratory) registration.getForm1572().getLabs().iterator().next()
                .getRole(OrganizationRoleType.CLINICAL_LABORATORY);
    }

    private LaboratoryCertificate addLabCertificate(LabCertificatesDialog dialog)
            throws IOException {
        LaboratoryCertificate certificate = new LaboratoryCertificate(LaboratoryCertificateType.CLP);
        certificate.setEffectiveDate(DateUtils.addYears(new Date(), -1));
        certificate.setExpirationDate(DateUtils.addYears(new Date(), 1));
        dialog.getHelper().fillOutCertificate(certificate);
        dialog.setUploadFile(createTemporaryFile());
        dialog.clickSave();
        dialog.getHelper().checkCertificateDisplayed(certificate);
        assertTrue(dialog.getHelper().getListing(certificate).hasDownloadLink());
        return certificate;
    }

    @Test
    public void testSaveClinicalLabCertificate_NotInUse() throws IOException {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        ClinicalLaboratory clinicalLab = getNotInUseClinicalLab();
        LabCertificatesDialog certificatesDialog =
                clickCertificatesLink(organizationAssociationsTab, clinicalLab);
        addLabCertificate(certificatesDialog);
        checkFormsAreRevised();
    }

    private LabCertificatesDialog clickCertificatesLink(OrganizationAssociationsTab organizationAssociationsTab,
            ClinicalLaboratory clinicalLab) {
        return organizationAssociationsTab.getClinicalLabSection().getHelper().getListing(clinicalLab).clickCertificatesLink();
    }

    private ClinicalLaboratory getNotInUseClinicalLab() {
        ClinicalLaboratory inUseLab = getInUseClinicalLab();
        Set<OrganizationAssociation> labAssociations = registration.getProfile()
                .getOrganizationAssociations(OrganizationRoleType.CLINICAL_LABORATORY);
        for (OrganizationAssociation labAssociation : labAssociations) {
            ClinicalLaboratory lab = (ClinicalLaboratory) labAssociation.getOrganizationRole();
            if (!lab.equals(inUseLab)) {
                return lab;
            }
        }
        return null;
    }

    @Test
    public void testDeleteClinicalLabCertificate_InUse() {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        ClinicalLaboratory clinicalLab = getInUseClinicalLab();
        LabCertificatesDialog certificatesDialog =
                clickCertificatesLink(organizationAssociationsTab, clinicalLab);
        LaboratoryCertificate certificate = clinicalLab.getCertificates().values().iterator().next();
        certificatesDialog.getHelper().getListing(certificate).clickRemoveLink();
        checkFormsAreRevised(FORM_1572);
    }

    @Test
    public void testDeleteClinicalLabCertificate_NotInUse() throws IOException {
        OrganizationAssociationsTab organizationAssociationsTab = openOrganizationAssociationsTab();
        ClinicalLaboratory clinicalLab = getNotInUseClinicalLab();
        LabCertificatesDialog certificatesDialog =
                clickCertificatesLink(organizationAssociationsTab, clinicalLab);
        LaboratoryCertificate certificate = addLabCertificate(certificatesDialog);
        LabCertificatesDialog certificateDialog = clickCertificatesLink(organizationAssociationsTab, clinicalLab);
        certificateDialog.getHelper().getListing(certificate).clickRemoveLink();
        checkFormsAreRevised();
    }

    @Test
    public void testAnswerFinancialDisclosureQuestion() {
        RegistrationOverviewTab overviewTab = openRegistration();
        FinancialDisclosureTab financialDisclosureTab = overviewTab.getPage().clickFinancialDisclosureTab();
        financialDisclosureTab.answerQuestion(Question.Q1_MONETARY_GAIN, true);
        checkFormsAreRevised(FINANCIAL_DISCLOSURE_FORM);
    }

    private RegistrationOverviewTab openRegistration() {
        return homePage.getInvestigatorMenu().clickProtocolRegistrations().getHelper().clickRegistrationLink(registration);
    }

    @Test
    public void testUploadFinancialDisclosureFile() throws IOException {
        RegistrationOverviewTab overviewTab = openRegistration();
        FinancialDisclosureTab financialDisclosureTab = overviewTab.getPage().clickFinancialDisclosureTab();
        File file = createTemporaryFile();
        financialDisclosureTab.getSupportingDocumentsTag().getHelper().uploadFile(file);
        financialDisclosureTab.getSupportingDocumentsTag().getHelper().assertListed(file);
        checkFormsAreRevised(FINANCIAL_DISCLOSURE_FORM);
    }

    @Test
    public void testPracticeSiteSelection() {
        RegistrationOverviewTab overviewTab = openRegistration();
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        Organization practiceSite = registration.getForm1572().getPracticeSites().iterator().next();
        form1572Tab.getPracticeSiteSection().getHelper().getListing(practiceSite).deselect();
        checkFormsAreRevised(FORM_1572);
    }

    @Test
    public void testLabSelection() {
        RegistrationOverviewTab overviewTab = openRegistration();
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        Organization lab = registration.getForm1572().getLabs().iterator().next();
        form1572Tab.getClinicalLabSection().getHelper().getListing(lab).deselect();
        checkFormsAreRevised(FORM_1572);
    }

    @Test
    public void testIrbSelection() {
        RegistrationOverviewTab overviewTab = openRegistration();
        ProtocolForm1572Tab form1572Tab = overviewTab.getPage().clickForm1572Tab();
        Organization irb = registration.getForm1572().getIrbs().iterator().next();
        form1572Tab.getIrbSection().getHelper().getListing(irb).deselect();
        checkFormsAreRevised(FORM_1572);
    }

    @Test
    public void testHumanResearchCertificateSelection() {
        HumanResearchCertificateTab humanResearchTab = openHumanResearchCertificateTab();
        Certificate certificate = registration.getHumanResearchCertificateForm().getCertificates().iterator().next();
        humanResearchTab.getHelper().getListing(certificate).deselect();
        checkFormsAreRevised(HUMAN_RESEARCH_CERTIFICATE);
    }

    private HumanResearchCertificateTab openHumanResearchCertificateTab() {
        return openRegistration().getPage().clickHumanResearchCertificateTab();
    }

    @Test
    public void testAddHumanResearchCertificate() throws IOException {
        HumanResearchCertificateTab humanResearchTab = openHumanResearchCertificateTab();
        CertificateListing listing = addCertificate(humanResearchTab);
        checkFormsAreRevised(CV);
        listing.select();
        checkFormsAreRevised(HUMAN_RESEARCH_CERTIFICATE, CV);
    }

    private CertificateListing addCertificate(HumanResearchCertificateTab humanResearchTab) throws IOException {
        EditTrainingCertificateDialog certificateDialog = humanResearchTab.clickAddCertificate();
        File file = createTemporaryFile();
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate(file, getExistingExternalOrganization());
        certificateDialog.getHelper().enterTrainingCertificateData(certificate, file, false);
        certificateDialog.clickSave();
        return humanResearchTab.getHelper().getListing(certificate);
    }

    @Test
    public void testDeleteHumanResearchCertificate_InUse() {
        HumanResearchCertificateTab humanResearchTab = openHumanResearchCertificateTab();
        getInUseHumanResearchCertificate(humanResearchTab).clickDelete().clickDelete();
        checkFormsAreRevised(HUMAN_RESEARCH_CERTIFICATE, CV);
    }

    private CertificateListing getInUseHumanResearchCertificate(HumanResearchCertificateTab humanResearchTab) {
        Certificate certificate = registration.getHumanResearchCertificateForm().getCertificates().iterator().next();
        for (CertificateListing certificateListing : humanResearchTab.getCertificates()) {
            if (certificateListing.getId().equals(certificate.getId())) {
                return certificateListing;
            }
        }
        return null;
    }

    @Test
    public void testDeleteHumanResearchCertificate_NotInUse() throws IOException {
        HumanResearchCertificateTab humanResearchTab = openHumanResearchCertificateTab();
        addCertificate(humanResearchTab).clickDelete().clickDelete();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testEditHumanResearchCertificate_InUse() throws IOException {
        HumanResearchCertificateTab humanResearchTab = openHumanResearchCertificateTab();
        getInUseHumanResearchCertificate(humanResearchTab).clickEdit().clickSave();
        checkFormsAreRevised(HUMAN_RESEARCH_CERTIFICATE, CV);
    }

    @Test
    public void testEditHumanResearchCertificate_NotInUse() throws IOException {
        HumanResearchCertificateTab humanResearchTab = openHumanResearchCertificateTab();
        addCertificate(humanResearchTab).clickEdit().clickSave();
        checkFormsAreRevised(CV);
    }

    @Test
    public void testAddSubinvestigator_FromProfile() throws AssociationAlreadyExistsException {
        Person subinvestigator = addSubinvestigatorToProfile();
        SubinvestigatorsTab subinvestigatorsTab = openRegistration().getPage().clickSubinvestigatorsTab();
        ProfileSubinvestigatorsDialog selectFromProfileDialog = subinvestigatorsTab.clickAddFromProfile();
        selectFromProfileDialog.getHelper().selectPerson(subinvestigator);
        selectFromProfileDialog.clickSave();
        checkFormsAreRevised(FORM_1572);
    }

    private Person addSubinvestigatorToProfile() throws AssociationAlreadyExistsException {
        Person subinvestigator = getExistingExternalPerson();
        registration.getProfile().addSubInvestigator(subinvestigator);
        dataSet.update(registration.getProfile());
        return subinvestigator;
    }

    @Test
    public void testAddSubinvestigator_New() {
        SubinvestigatorsTab subinvestigatorsTab = openRegistration().getPage().clickSubinvestigatorsTab();
        SubInvestigatorAssociationFormDialog newSubinvestigatorDialog = subinvestigatorsTab.clickAddNew();
        Person subInvestigator = getExistingExternalPerson();
        newSubinvestigatorDialog.getHelper().searchAndSelectPerson(subInvestigator);
        checkFormsAreRevised(FORM_1572);
    }

    private OrganizationAssociationsTab openOrganizationAssociationsTab() {
        return homePage.getInvestigatorMenu().clickProfile().getPage().clickOrganizationAssociationsTab();
    }

    private CredentialsTab openCredentialsTab() {
        return homePage.getInvestigatorMenu().clickProfile().getPage().clickCredentialsTab();
    }
}
