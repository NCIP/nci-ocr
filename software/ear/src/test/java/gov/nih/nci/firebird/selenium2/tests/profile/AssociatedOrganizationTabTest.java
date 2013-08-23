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
package gov.nih.nci.firebird.selenium2.tests.profile;

import static gov.nih.nci.firebird.data.LaboratoryCertificateType.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.support.AbstractLoadableComponent;
import gov.nih.nci.firebird.commons.test.TestFileUtils;
import gov.nih.nci.firebird.data.AbstractOrganizationRole;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.InstitutionalReviewBoard;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.organization.AbstractNesRoleData;
import gov.nih.nci.firebird.nes.organization.IdentifiedOrganizationIntegrationService;
import gov.nih.nci.firebird.nes.organization.NesOrganizationIntegrationServiceFactory;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationType;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.components.CreateOrganizationComponent;
import gov.nih.nci.firebird.selenium2.pages.components.EditOhrpNumberComponent;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.AddOrganizationAssociationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.CreatePracticeSiteComponent;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.LabCertificatesDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.LabCertificatesDialog.LabCertificateListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.OrganizationAssociationListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.OrganizationAssociationSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.OrganizationAssociationsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.PracticeSiteSection.PracticeSiteListing;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.RemoveOrganizationAssociationConfirmDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.SetPracticeSiteFieldsDialog;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.LoginAccount;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;
import gov.nih.nci.firebird.test.util.FirebirdPropertyUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;

public class AssociatedOrganizationTabTest extends AbstractFirebirdWebDriverTest {

    private static final String PRACTICE_SITE_PREFIX_WHICH_RETURNS_ALL_TYPES = "tex";

    private static final String OHRP_NUMBER = ValueGenerator.getUniqueOhrpNumber();

    private OrganizationAssociationsTab organizationAssociationsTab;

    private Calendar effectiveDate = Calendar.getInstance();
    private Calendar expirationDate = Calendar.getInstance();
    private Calendar effectiveDate2 = Calendar.getInstance();
    private Calendar expirationDate2 = Calendar.getInstance();
    private File tmp;
    private DataSet dataSet;

    @Inject
    private NesOrganizationIntegrationServiceFactory integrationServiceFactory;
    @Inject
    private IdentifiedOrganizationIntegrationService identifiedOrganizationService;

    private LoginAccount ctepInvestigatorLogin;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        LoginAccount investigatorLogin = builder.createInvestigator().getLogin();
        ctepInvestigatorLogin = builder.createInvestigator().asCtepUser().getLogin();
        dataSet = builder.build();
        organizationAssociationsTab = openOrgAssociationsTab(investigatorLogin, false);

        effectiveDate.set(Calendar.YEAR, 2011);
        expirationDate.set(Calendar.YEAR, 2015);
        effectiveDate2.set(Calendar.YEAR, 2010);
        tmp = TestFileUtils.createTemporaryFile();
    }

    private OrganizationAssociationsTab openOrgAssociationsTab(LoginAccount login, boolean ctepUser) {
        String provider = ctepUser ? getCtepProvider() : getProvider();
        HomePage homePage = openLoginPage().getHelper().goToHomePage(login, provider);
        return homePage.getInvestigatorMenu().clickProfile().getPage().clickOrganizationAssociationsTab();
    }

    @Test
    public void testAssociatedOrganizationSelectExistingValidation() {
        PracticeSite practiceSite = getExistingPracticeSite();
        ClinicalLaboratory lab = getExistingLab();
        InstitutionalReviewBoard irb = getExistingIrb();

        addIrb(irb);
        assertNotNull(organizationAssociationsTab.getIrbSection().getHelper().getListing(irb));

        addClinicalLab(lab);
        assertNotNull(organizationAssociationsTab.getClinicalLabSection().getHelper().getListing(lab));

        addPracticeSite(practiceSite, false);
        assertNotNull(organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite));
    }

    private PracticeSite getExistingPracticeSite() {
        return getTestDataSource().getPracticeSite();
    }

    private ClinicalLaboratory getExistingLab() {
        return getTestDataSource().getClinicalLab();
    }

    private InstitutionalReviewBoard getExistingIrb() {
        return getTestDataSource().getIrb();
    }

    private void addIrb(InstitutionalReviewBoard irb) {
        AddOrganizationAssociationDialog addOrganizationAssociationDialog = organizationAssociationsTab.getIrbSection()
                .clickCreateNew();
        organizationAssociationsTab = addOrganizationAssociationDialog.clickCancel();

        if (irb.getOrganization().hasExternalRecord()) {
            selectExistingIrbValidateDuplicate(irb);
        } else {
            addOrganizationAssociationDialog = organizationAssociationsTab.getIrbSection().clickCreateNew();
            organizationAssociationsTab = (OrganizationAssociationsTab) createNewOrg(irb,
                    addOrganizationAssociationDialog);
        }
    }

    private void selectExistingIrbValidateDuplicate(InstitutionalReviewBoard irb) {
        selectExistingIrb(irb);
        assertNotNull(organizationAssociationsTab.getIrbSection().getHelper().getListing(irb));
        selectDuplicateIrb(irb);
    }

    private void selectExistingIrb(final InstitutionalReviewBoard irb) {
        AddOrganizationAssociationDialog dialog = organizationAssociationsTab.getIrbSection().clickCreateNew();
        organizationAssociationsTab = (OrganizationAssociationsTab) selectExistingOrg(irb, dialog);
    }

    private void selectDuplicateIrb(final InstitutionalReviewBoard irb) {
        final AddOrganizationAssociationDialog dialog = organizationAssociationsTab.getIrbSection().clickCreateNew();
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "organization.association.error.existing.association");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                selectExistingOrg(irb, dialog);
            }
        });
        organizationAssociationsTab = dialog.clickCancel();
    }

    private AbstractLoadableComponent<?> createNewOrg(AbstractOrganizationRole role,
            AddOrganizationAssociationDialog addOrganizationAssociationDialog) {
        CreateOrganizationComponent createOrganizationComponent = (CreateOrganizationComponent) addOrganizationAssociationDialog
                .clickCreateNew();
        saveAndCheckValidationErrors(addOrganizationAssociationDialog);
        checkInvalidEmailValidation(createOrganizationComponent, addOrganizationAssociationDialog, role);
        return saveAndValidateAssociation(createOrganizationComponent, addOrganizationAssociationDialog, role);
    }

    private AbstractLoadableComponent<?> createNewPracticeSite(final PracticeSite practiceSite,
            final AddOrganizationAssociationDialog addOrganizationAssociationDialog) {
        final CreatePracticeSiteComponent createPracticeSiteComponent = (CreatePracticeSiteComponent) addOrganizationAssociationDialog
                .clickCreateNew();
        saveAndCheckValidationErrors(addOrganizationAssociationDialog);
        checkInvalidEmailValidation(createPracticeSiteComponent.getCreateOrganizationComponent(),
                addOrganizationAssociationDialog, practiceSite);
        practiceSite.getOrganization().setEmail("this@example.com");
        createPracticeSiteComponent.getHelper().enterPracticeSiteData(practiceSite);
        verifyRequiredPracticeSiteFields(createPracticeSiteComponent);
        practiceSite.setOhrpAssuranceNumber(ValueGenerator.getUniqueOhrpNumber());
        createPracticeSiteComponent.typeOhrpNumber(practiceSite.getOhrpAssuranceNumber());
        createPracticeSiteComponent.getHelper().selectPracticeSiteType(practiceSite.getType());
        return saveAndValidateAssociation(createPracticeSiteComponent.getCreateOrganizationComponent(),
                addOrganizationAssociationDialog, practiceSite);
    }

    private void verifyRequiredPracticeSiteFields(final CreatePracticeSiteComponent createPracticeSiteComponent) {
        createPracticeSiteComponent.getHelper().selectPracticeSiteType(null);
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure();
        expectedFailure.addExpectedMessage("organization.association.error.practice.site.type.required");
        expectedFailure.assertFailureOccurs(new ExpectedValidationFailure.FailingAction() {
            @Override
            public void perform() {
                createPracticeSiteComponent.clickSaveButton();
            }
        });
    }

    private void saveAndCheckValidationErrors(final AddOrganizationAssociationDialog addOrganizationAssociationDialog) {
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure();
        expectedValidationFailure.addExpectedRequiredFields("profile.organization.name", "profile.organization.email",
                "profile.organization.postalAddress.streetAddress", "profile.organization.postalAddress.city",
                "profile.organization.postalAddress.stateOrProvince", "profile.organization.postalAddress.postalCode");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                addOrganizationAssociationDialog.clickSave();
            }
        });
    }

    private void checkInvalidEmailValidation(CreateOrganizationComponent createOrganizationComponent,
            final AddOrganizationAssociationDialog addOrganizationAssociationDialog, AbstractOrganizationRole role) {
        role.getOrganization().setEmail("this should not work");
        createOrganizationComponent.getHelper().enterData(role.getOrganization());

        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure();
        expectedValidationFailure.addExpectedText(FirebirdPropertyUtils
                .getEmailFormatError("profile.organization.email"));
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {

            @Override
            public void perform() {
                addOrganizationAssociationDialog.clickSave();
            }
        });
    }

    private AbstractLoadableComponent<?> saveAndValidateAssociation(
            CreateOrganizationComponent createOrganizationComponent,
            final AddOrganizationAssociationDialog addOrganizationAssociationDialog, AbstractOrganizationRole role) {
        role.getOrganization().setEmail("this@example.com");
        createOrganizationComponent.getHelper().enterData(role.getOrganization());
        return addOrganizationAssociationDialog.clickSave();
    }

    private AbstractLoadableComponent<?> selectExistingOrg(AbstractOrganizationRole role,
            AddOrganizationAssociationDialog addOrganizationAssociationDialog) {
        return addOrganizationAssociationDialog.getHelper().searchAndSelectOrganization(role);
    }

    private void addClinicalLab(ClinicalLaboratory lab) {
        AddOrganizationAssociationDialog addOrganizationAssociationDialog = organizationAssociationsTab
                .getClinicalLabSection().clickCreateNew();
        organizationAssociationsTab = addOrganizationAssociationDialog.clickCancel();

        if (lab.getOrganization().hasExternalRecord()) {
            selectExistingLabValidateDuplicate(lab);
        } else {
            addOrganizationAssociationDialog = organizationAssociationsTab.getClinicalLabSection().clickCreateNew();
            organizationAssociationsTab = (OrganizationAssociationsTab) createNewOrg(lab,
                    addOrganizationAssociationDialog);
        }
    }

    private void selectExistingLabValidateDuplicate(ClinicalLaboratory lab) {
        selectExistingLab(lab, false);
        assertNotNull(organizationAssociationsTab.getClinicalLabSection().getHelper().getListing(lab));
        selectExistingLab(lab, true);
    }

    private void selectExistingLab(final ClinicalLaboratory lab, boolean failureExpected) {
        final AddOrganizationAssociationDialog addOrganizationAssociationDialog = organizationAssociationsTab
                .getClinicalLabSection().clickCreateNew();
        if (failureExpected) {
            ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                    "organization.association.error.existing.association");
            expectedValidationFailure.assertFailureOccurs(new FailingAction() {

                @Override
                public void perform() {
                    selectExistingOrg(lab, addOrganizationAssociationDialog);
                }
            });
            organizationAssociationsTab = addOrganizationAssociationDialog.clickCancel();
        } else {
            selectExistingOrg(lab, addOrganizationAssociationDialog);
        }
    }

    private void addPracticeSite(PracticeSite practiceSite, boolean phoneNumberRequired) {
        if (practiceSite.getOrganization().hasExternalRecord()) {
            selectExistingPracticeSiteValidateDuplicate(practiceSite);
        } else {
            createNewPracticeSite(practiceSite, phoneNumberRequired);
        }
    }

    private void selectExistingPracticeSiteValidateDuplicate(PracticeSite practiceSite) {
        selectExistingPracticeSite(practiceSite);
        assertNotNull(organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite));
        selectDuplicatePracticeSite(practiceSite);
    }

    private void selectExistingPracticeSite(final PracticeSite practiceSite) {
        final AddOrganizationAssociationDialog dialog = organizationAssociationsTab.getPracticeSiteSection()
                .clickCreateNew();
        SetPracticeSiteFieldsDialog practiceSiteFieldsDialog = (SetPracticeSiteFieldsDialog) selectExistingOrg(
                practiceSite, dialog);
        practiceSite.setDataField(OHRP_NUMBER);
        practiceSiteFieldsDialog.typeOhrpNumber(practiceSite.getDataField());
        assertFalse(practiceSiteFieldsDialog.isSelectPracticeSiteTypePresent());
        organizationAssociationsTab = (OrganizationAssociationsTab) practiceSiteFieldsDialog.clickSaveButton();
    }

    private void selectDuplicatePracticeSite(final PracticeSite practiceSite) {
        final AddOrganizationAssociationDialog dialog = organizationAssociationsTab.getPracticeSiteSection()
                .clickCreateNew();
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "organization.association.error.existing.association");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                selectExistingOrg(practiceSite, dialog);
            }
        });
        organizationAssociationsTab = dialog.clickCancel();
    }

    private void createNewPracticeSite(PracticeSite practiceSite, boolean phoneNumberRequired) {
        AddOrganizationAssociationDialog addOrganizationAssociationDialog = organizationAssociationsTab
                .getPracticeSiteSection().clickCreateNew();
        practiceSite.getOrganization().setPhoneNumber(null);
        if (phoneNumberRequired) {
            checkPhoneNumberRequired(practiceSite, addOrganizationAssociationDialog);
            practiceSite.getOrganization().setPhoneNumber(ValueGenerator.getUniquePhoneNumber());
        }
        organizationAssociationsTab = (OrganizationAssociationsTab) createNewPracticeSite(practiceSite,
                addOrganizationAssociationDialog);
    }

    private void checkPhoneNumberRequired(PracticeSite practiceSite,
            final AddOrganizationAssociationDialog addOrganizationAssociationDialog) {
        addOrganizationAssociationDialog.getHelper().enterNewOrganizationData(practiceSite);
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure("phoneNumber.required");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {

            @Override
            public void perform() {
                addOrganizationAssociationDialog.clickSave();
            }
        });
        organizationAssociationsTab = addOrganizationAssociationDialog.clickCancel();
        organizationAssociationsTab.getPracticeSiteSection().clickCreateNew();
    }

    @Test
    public void testAddNewPracticeSiteAsCtepUser() {
        openOrgAssociationsTab(ctepInvestigatorLogin, true);
        PracticeSite practiceSite = createNewPracticeSite();
        addPracticeSite(practiceSite, true);
        assertNotNull(organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite));
    }

    private PracticeSite createNewPracticeSite() {
        Organization organization = OrganizationFactory.getInstance().createWithoutExternalData();
        PracticeSite practiceSite = new PracticeSite();
        practiceSite.setOrganization(organization);
        practiceSite.setType(PracticeSiteType.HEALTH_CARE_FACILITY);
        return practiceSite;
    }

    @Test
    public void testAssociatedOrganizationAddNew() {
        InstitutionalReviewBoard irb = createNewIrb();
        addIrb(irb);
        assertNotNull(organizationAssociationsTab.getIrbSection().getHelper().getListing(irb));
        checkAddDuplicateIrbThenCreateNew(irb);

        ClinicalLaboratory lab = createNewLab();
        addClinicalLab(lab);
        assertNotNull(organizationAssociationsTab.getClinicalLabSection().getHelper().getListing(lab));

        PracticeSite practiceSite = createNewPracticeSite();
        addPracticeSite(practiceSite, false);
        assertNotNull(organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite));
    }

    private ClinicalLaboratory createNewLab() {
        Organization organization = OrganizationFactory.getInstance().createWithoutExternalData();
        ClinicalLaboratory lab = new ClinicalLaboratory();
        lab.setOrganization(organization);
        return lab;
    }

    private InstitutionalReviewBoard createNewIrb() {
        Organization organization = OrganizationFactory.getInstance().createWithoutExternalData();
        InstitutionalReviewBoard irb = new InstitutionalReviewBoard();
        irb.setOrganization(organization);
        return irb;
    }

    private void checkAddDuplicateIrbThenCreateNew(final InstitutionalReviewBoard irb) {
        final AddOrganizationAssociationDialog addIrbDialog = organizationAssociationsTab.getIrbSection()
                .clickCreateNew();
        ExpectedValidationFailure expectedFailure = new ExpectedValidationFailure(
                "organization.association.error.existing.association");
        expectedFailure.assertFailureOccurs(new FailingAction() {

            @Override
            public void perform() {
                addIrbDialog.getHelper().searchAndSelectOrganization(irb);
            }
        });

        CreateOrganizationComponent createNewIrbDialog = (CreateOrganizationComponent) addIrbDialog.clickCreateNew();
        Organization organization = OrganizationFactory.getInstance().create();
        InstitutionalReviewBoard newIrb = new InstitutionalReviewBoard();
        newIrb.setOrganization(organization);
        saveAndValidateAssociation(createNewIrbDialog, addIrbDialog, newIrb);
    }

    @Test
    public void testUpdatePracticeSiteOhrp() {
        String newOhrp = ValueGenerator.getUniqueOhrpNumber();
        PracticeSite practiceSite = getExistingPracticeSite();
        addPracticeSite(practiceSite, false);
        PracticeSiteListing practiceSiteListing = organizationAssociationsTab.getPracticeSiteSection().getHelper()
                .getListing(practiceSite);
        assertNotNull(practiceSiteListing);
        EditOhrpNumberComponent editOhrpNumberComponent = practiceSiteListing.clickEditOhrpNumberLink();
        assertTrue(editOhrpNumberComponent.isDisplayed());
        editOhrpNumberComponent.clickCancelLink();
        assertFalse(editOhrpNumberComponent.isDisplayed());
        practiceSiteListing = organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite);
        editOhrpNumberComponent = practiceSiteListing.clickEditOhrpNumberLink();
        checkForInvalidOhrpNumberMessage("", editOhrpNumberComponent);
        checkForInvalidOhrpNumberMessage("12345678901", editOhrpNumberComponent); //First 2 aren't chars
        checkForInvalidOhrpNumberMessage("FWA1234567", editOhrpNumberComponent); //Too short
        editOhrpNumberComponent.typeOhrpNumber(newOhrp);
        editOhrpNumberComponent.clickSaveLink();
        assertNull(organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite));
        practiceSite.setOhrpAssuranceNumber(newOhrp);
        assertNotNull(organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite));
    }

    private void checkForInvalidOhrpNumberMessage(String ohrpNumber, final EditOhrpNumberComponent editOhrpNumberComponent) {
        editOhrpNumberComponent.typeOhrpNumber(ohrpNumber);
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "organization.association.error.ohrp.invalid");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {

            @Override
            public void perform() {
                editOhrpNumberComponent.clickSaveLink();
            }
        });
    }

    @Test
    public void testManageLaboratoryCertificates() {
        ClinicalLaboratory lab = getExistingLab();
        LaboratoryCertificate clia = new LaboratoryCertificate(CLIA);
        clia.setEffectiveDate(effectiveDate.getTime());
        clia.setExpirationDate(expirationDate.getTime());
        LaboratoryCertificate clp = new LaboratoryCertificate(CLP);
        clp.setEffectiveDate(effectiveDate.getTime());
        clp.setExpirationDate(expirationDate.getTime());
        LaboratoryCertificate cap = new LaboratoryCertificate(CAP);
        cap.setEffectiveDate(effectiveDate.getTime());
        cap.setExpirationDate(expirationDate.getTime());

        addClinicalLab(lab);
        LabCertificatesDialog labCertificatesDialog = organizationAssociationsTab.getClinicalLabSection().getHelper()
                .getListing(lab).clickCertificatesLink();

        addCertificateWithChecks(clia, labCertificatesDialog);
        updateCertificate(clia, labCertificatesDialog);
        addCertificateWithoutChecks(cap, labCertificatesDialog);
        assertFalse(labCertificatesDialog.getHelper().isFormDisplayed());
        labCertificatesDialog.getHelper().getListing(clia).clickRemoveLink();
        assertTrue(labCertificatesDialog.getHelper().isAddNewCertificateFormDisplayed());

        addCertificateWithoutChecks(clp, labCertificatesDialog);
        assertFalse(labCertificatesDialog.getHelper().isFormDisplayed());
    }

    private void addCertificateWithChecks(LaboratoryCertificate cert, LabCertificatesDialog labCertificatesDialog) {
        assertTrue(labCertificatesDialog.getHelper().isAddNewCertificateFormDisplayed());
        assertFalse(labCertificatesDialog.isCancelButtonPresent());
        assertFalse(labCertificatesDialog.isTableDisplayed());

        checkForRequiredFields(labCertificatesDialog);

        labCertificatesDialog.getHelper().fillOutCertificate(cert);
        labCertificatesDialog.setUploadFile(tmp);

        checkForExpirationBeforeEffectiveDateError(labCertificatesDialog);
        labCertificatesDialog.getHelper().fillOutCertificate(cert);
        labCertificatesDialog.setUploadFile(tmp);

        labCertificatesDialog.clickSave();

        assertTrue(labCertificatesDialog.isTableDisplayed());
        assertNotNull(labCertificatesDialog.getHelper().getListing(cert));
    }

    private void checkForRequiredFields(final LabCertificatesDialog labCertificatesDialog) {
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure();
        expectedValidationFailure.addExpectedMessage("error.expiration.date.required");
        expectedValidationFailure.addExpectedMessage("clinical.laboratory.certificates.error.required.type");
        expectedValidationFailure.addExpectedMessage("error.file.required");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                labCertificatesDialog.clickSave();
            }
        });
    }

    private void checkForExpirationBeforeEffectiveDateError(final LabCertificatesDialog labCertificatesDialog) {
        Date today = new Date();
        labCertificatesDialog.getHelper().setEffectiveDate(DateUtils.setMonths(today, Calendar.DECEMBER));
        labCertificatesDialog.getHelper().setExpirationDate(DateUtils.setMonths(today, Calendar.OCTOBER));
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "error.expiration.date.before.effective");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                labCertificatesDialog.clickSave();
            }
        });
    }

    private void updateCertificate(LaboratoryCertificate cert, LabCertificatesDialog labCertificatesDialog) {
        LabCertificateListing certificateListing = labCertificatesDialog.getHelper().getListing(cert);
        certificateListing.clickEditLink();
        assertTrue(labCertificatesDialog.isCancelButtonPresent());
        assertTrue(labCertificatesDialog.getHelper().isInEditMode());
        checkCantRemoveDates(labCertificatesDialog);
        cert.setEffectiveDate(effectiveDate2.getTime());
        cert.setExpirationDate(expirationDate2.getTime());
        labCertificatesDialog.getEffectiveDateTag().getHelper().setDate(cert.getEffectiveDate());
        labCertificatesDialog.getExpirationDateTag().getHelper().setDate(cert.getExpirationDate());
        labCertificatesDialog.setUploadFile(tmp);
        labCertificatesDialog.clickSave();
        assertNotNull(certificateListing);
        assertTrue(certificateListing.hasDownloadLink());
    }

    private void checkCantRemoveDates(final LabCertificatesDialog labCertificatesDialog) {
        labCertificatesDialog.getEffectiveDateTag().getHelper().clearDate();
        labCertificatesDialog.getExpirationDateTag().getHelper().clearDate();
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "error.expiration.date.required");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                labCertificatesDialog.clickSave();
            }
        });
    }

    private void addCertificateWithoutChecks(LaboratoryCertificate cert, LabCertificatesDialog labCertificatesDialog) {
        labCertificatesDialog.getHelper().fillOutCertificate(cert);
        labCertificatesDialog.setUploadFile(tmp);
        labCertificatesDialog.clickSave();
        LabCertificateListing certificateListing = labCertificatesDialog.getHelper().getListing(cert);
        assertNotNull(certificateListing);
        assertTrue(certificateListing.hasDownloadLink());
    }

    @Test
    public void testPracticeSiteOhrpNeedsToBeOnlyEnteredOnce() {
        PracticeSite practiceSite = getExistingPracticeSite();
        addPracticeSite(practiceSite, false);
        PracticeSiteListing practiceSiteListing = organizationAssociationsTab.getPracticeSiteSection().getHelper()
                .getListing(practiceSite);
        assertNotNull(practiceSiteListing);

        RemoveOrganizationAssociationConfirmDialog removeOrganizationAssociationConfirmDialog = practiceSiteListing
                .clickRemoveLink();
        removeOrganizationAssociationConfirmDialog.getOrganizationDisplayTag().getHelper()
                .verifyInformationIsDisplayed(practiceSite.getOrganization());
        removeOrganizationAssociationConfirmDialog.clickConfirmButton();
        AddOrganizationAssociationDialog addOrganizationAssociationDialog = organizationAssociationsTab
                .getPracticeSiteSection().clickCreateNew();

        SetPracticeSiteFieldsDialog practiceSiteFieldsDialog = (SetPracticeSiteFieldsDialog) addOrganizationAssociationDialog
                .getHelper().searchAndSelectOrganization(practiceSite);
        assertEquals(practiceSite.getOhrpAssuranceNumber(), practiceSiteFieldsDialog.getOhrpNumber());
        assertFalse(practiceSiteFieldsDialog.isSelectPracticeSiteTypePresent());
        practiceSiteFieldsDialog.clickSaveButton();
        assertNotNull(organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite));
    }

    @Test
    public void testAssociatedOrganizationDeleteAssociations() {
        PracticeSite practiceSite = getExistingPracticeSite();
        InstitutionalReviewBoard irb = getExistingIrb();
        ClinicalLaboratory lab = getExistingLab();

        addIrb(irb);
        assertNotNull(organizationAssociationsTab.getIrbSection().getHelper().getListing(irb));
        removeAssociation(irb);
        assertNull(organizationAssociationsTab.getIrbSection().getHelper().getListing(irb));

        addClinicalLab(lab);
        assertNotNull(organizationAssociationsTab.getClinicalLabSection().getHelper().getListing(lab));
        removeAssociation(lab);
        assertNull(organizationAssociationsTab.getClinicalLabSection().getHelper().getListing(lab));

        addPracticeSite(practiceSite, false);
        assertNotNull(organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite));
        removeAssociation(practiceSite);
        assertNull(organizationAssociationsTab.getPracticeSiteSection().getHelper().getListing(practiceSite));
    }

    private OrganizationAssociationsTab removeAssociation(AbstractOrganizationRole role) {
        OrganizationAssociationSection organizationAssociationSection = organizationAssociationsTab.getHelper()
                .getSection(role.getRoleType());
        OrganizationAssociationListing associationListing = organizationAssociationSection.getHelper().getListing(role);
        return associationListing.clickRemoveLink().clickConfirmButton();
    }

    @Test
    public void testPracticeSiteManagement() {
        Organization existingHealthCareFacility = integrationServiceFactory.getHealthCareFacilityService()
                .searchByName(PRACTICE_SITE_PREFIX_WHICH_RETURNS_ALL_TYPES).get(0);
        existingHealthCareFacility.setCtepId(getOrganizationCtepId(existingHealthCareFacility));
        Organization existingCancerCenter = integrationServiceFactory.getResearchOrganizationService()
                .searchByName(PRACTICE_SITE_PREFIX_WHICH_RETURNS_ALL_TYPES, ResearchOrganizationType.CANCER_CENTER)
                .get(0);
        existingCancerCenter.setCtepId(getOrganizationCtepId(existingCancerCenter));
        Organization existingClinicalCenter = integrationServiceFactory.getResearchOrganizationService()
                .searchByName(PRACTICE_SITE_PREFIX_WHICH_RETURNS_ALL_TYPES, ResearchOrganizationType.CLINICAL_CENTER)
                .get(0);
        existingClinicalCenter.setCtepId(getOrganizationCtepId(existingClinicalCenter));
        selectPracticeSiteAndVerifyType(existingHealthCareFacility, PracticeSiteType.HEALTH_CARE_FACILITY);
        selectPracticeSiteAndVerifyType(existingCancerCenter, PracticeSiteType.CANCER_CENTER);
        selectPracticeSiteAndVerifyType(existingClinicalCenter, PracticeSiteType.CLINICAL_CENTER);
        createPracticeSiteAndVerifyType(PracticeSiteType.HEALTH_CARE_FACILITY);
        createPracticeSiteAndVerifyType(PracticeSiteType.CANCER_CENTER);
        createPracticeSiteAndVerifyType(PracticeSiteType.CLINICAL_CENTER);
    }

    private String getOrganizationCtepId(Organization organization) {
        AbstractNesRoleData roleData = (AbstractNesRoleData) organization.getExternalData();
        return identifiedOrganizationService.getCtepId(roleData.getPlayerId());
    }

    private void selectPracticeSiteAndVerifyType(Organization practiceSiteOrganization, PracticeSiteType expectedType) {
        PracticeSite practiceSite = new PracticeSite();
        practiceSite.setOrganization(practiceSiteOrganization);
        practiceSite.setOhrpAssuranceNumber(ValueGenerator.getUniqueString());
        addPracticeSite(practiceSite, false);
        checkDatabaseForPracticeSiteData(expectedType);
    }

    private void checkDatabaseForPracticeSiteData(PracticeSiteType expectedType) {
        Organization createdOrganization = dataSet.getLastCreatedObject(Organization.class);
        if (expectedType == PracticeSiteType.HEALTH_CARE_FACILITY) {
            assertTrue(createdOrganization.getExternalId().startsWith(NesIIRoot.HEALTH_CARE_FACILITY.getRoot()));
        } else {
            assertTrue(createdOrganization.getExternalId().startsWith(NesIIRoot.RESEARCH_ORGANIZATION.getRoot()));
        }
        PracticeSite createdPracticeSite = dataSet.getLastCreatedObject(PracticeSite.class);
        assertEquals(createdPracticeSite.getType(), expectedType);
    }

    private void createPracticeSiteAndVerifyType(PracticeSiteType type) {
        PracticeSite practiceSite = createNewPracticeSite();
        practiceSite.setType(type);
        addPracticeSite(practiceSite, false);
        checkDatabaseForPracticeSiteData(type);
    }

}
