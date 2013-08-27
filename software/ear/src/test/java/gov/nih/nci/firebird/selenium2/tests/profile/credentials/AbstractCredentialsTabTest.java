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
package gov.nih.nci.firebird.selenium2.tests.profile.credentials;

import static gov.nih.nci.firebird.test.ValueGenerator.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.test.TestFileUtils;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.BoardCertifiedSpecialty;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.Certification;
import gov.nih.nci.firebird.data.CertificationStatus;
import gov.nih.nci.firebird.data.CertificationType;
import gov.nih.nci.firebird.data.CertifiedSpecialtyBoard;
import gov.nih.nci.firebird.data.CertifiedSpecialtyType;
import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.DegreeType;
import gov.nih.nci.firebird.data.Fellowship;
import gov.nih.nci.firebird.data.Internship;
import gov.nih.nci.firebird.data.ListItem;
import gov.nih.nci.firebird.data.MedicalLicense;
import gov.nih.nci.firebird.data.MedicalLicenseType;
import gov.nih.nci.firebird.data.MedicalSpecialty;
import gov.nih.nci.firebird.data.MedicalSubSpecialty;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Residency;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.WorkHistory;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.CertificationSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.CredentialsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.DegreeSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.EditSpecialtyCredentialDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.EditTrainingCertificateDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.EditWorkHistoryDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.FellowshipSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.InternshipSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.LicenseSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageCertificationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageDegreeDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageLicenseDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ManageSpecialtyDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.ResidencySection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.SpecialtySection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.TrainingCertificateSection;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.WorkHistorySection;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.data.CredentialTypesData;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Base class for all credential tab tests.
 */
public abstract class AbstractCredentialsTabTest extends AbstractFirebirdWebDriverTest {

    private DataSet dataSet;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        configureDataSet(builder);
        dataSet = builder.build();
    }

    protected void configureDataSet(DataSetBuilder builder) {
        builder.createInvestigator();
    }

    protected CredentialsTab goToCredentialsTab() {
        HomePage homePage = openLoginPage().getHelper().goToHomePage(getDataSet().getInvestigatorLogin(), getProvider());
        return homePage.getInvestigatorMenu().clickProfile().getPage().clickCredentialsTab();
    }

    protected Organization getExistingIssuer() {
        return getNesTestDataSource().getOrganization();
    }

    protected Organization getNewIssuer() {
        return OrganizationFactory.getInstance().createWithoutNesData();
    }

    protected Degree createTestDegree(Organization issuer) {
        Degree degree = new Degree();
        degree.setIssuer(issuer);
        degree.setEffectiveDate(new Date());
        degree.setDegreeType(getDegreeTypes().get(0));
        return degree;
    }

    List<DegreeType> getDegreeTypes() {
        return getCredentialTypesData().getDegreeTypes();
    }

    private CredentialTypesData getCredentialTypesData() {
        return getDataSet().getCredentialTypesData();
    }

    protected Certification createTestCertification() {
        Certification certification = new Certification();
        certification.setCertificationType(getCertificationTypes().get(0));
        setEffectiveAndExpirationDates(certification);
        return certification;
    }

    protected List<CertificationType> getCertificationTypes() {
        return getCredentialTypesData().getCertificationTypes();
    }

    private void setEffectiveAndExpirationDates(AbstractCredential<?> credential) {
        Calendar calendar = Calendar.getInstance();
        credential.setExpirationDate(calendar.getTime());
        calendar.add(Calendar.YEAR, -1);
        credential.setEffectiveDate(calendar.getTime());
    }

    protected MedicalLicense createTestLicense() {
        MedicalLicense license = new MedicalLicense();
        license.setLicenseType(getLicenseTypes().get(0));
        license.setState("MD");
        license.setExpirationDate(new Date());
        license.setLicenseId(getUniqueString(10));
        return license;
    }

    protected List<MedicalLicenseType> getLicenseTypes() {
        return getCredentialTypesData().getLicenseTypes();
    }

    protected BoardCertifiedSpecialty createTestSpecialty() {
        BoardCertifiedSpecialty specialty = new BoardCertifiedSpecialty();
        setEffectiveAndExpirationDates(specialty);
        specialty.setSpecialtyType(getSpecialtyTypes().get(0));
        specialty.setStatus(CertificationStatus.CERTIFIED);
        return specialty;
    }

    protected List<CertifiedSpecialtyType> getSpecialtyTypes() {
        return getCredentialTypesData().getSpecialtyTypes();
    }
    
    protected List<CertifiedSpecialtyBoard> getBoards() {
        return getCredentialTypesData().getBoards();
    }

    protected TrainingCertificate createTestCertificate(Organization issuer) {
        TrainingCertificate certificate = new TrainingCertificate();
        certificate.setCertificateType(CertificateType.HUMAN_RESEARCH_CERTIFICATE);
        certificate.setFile(FirebirdFileFactory.getInstance().create());
        certificate.setIssuer(issuer);
        setEffectiveAndExpirationDates(certificate);
        return certificate;
    }

    protected WorkHistory createTestWorkHistory() {
        return createTestWorkHistory(getExistingIssuer());
    }

    protected WorkHistory createTestWorkHistory(Organization issuer) {
        WorkHistory workHistory = new WorkHistory();
        setEffectiveAndExpirationDates(workHistory);
        workHistory.setPosition(ValueGenerator.getUniqueString());
        workHistory.setIssuer(issuer);
        return workHistory;
    }

    protected Internship createTestInternship() {
        return createTestInternship(getExistingIssuer());
    }

    protected Internship createTestInternship(Organization issuer) {
        Internship internship = new Internship();
        setEffectiveAndExpirationDates(internship);
        internship.setSpecialty(getMedicalSpecialties().get(0));
        internship.setIssuer(issuer);
        return internship;
    }

    private List<MedicalSpecialty> getMedicalSpecialties() {
        return getCredentialTypesData().getMedicalSpecialties();
    }

    protected Residency createTestResidency() {
        return createTestResidency(getExistingIssuer());
    }

    protected Residency createTestResidency(Organization issuer) {
        Residency residency = new Residency();
        setEffectiveAndExpirationDates(residency);
        residency.setSpecialty(getMedicalSpecialties().get(0));
        residency.setIssuer(issuer);
        return residency;
    }

    protected Fellowship createTestFellowship(Organization issuer) {
        Fellowship fellowship = new Fellowship();
        setEffectiveAndExpirationDates(fellowship);
        fellowship.setSpecialty(getMedicalSubSpecialties().get(0));
        fellowship.setIssuer(issuer);
        return fellowship;
    }

    protected Fellowship createTestFellowship() {
        return createTestFellowship(getExistingIssuer());
    }

    private List<MedicalSubSpecialty> getMedicalSubSpecialties() {
        return getCredentialTypesData().getMedicalSubSpecialties();
    }

    protected void saveAndVerifyTrainingCertificate(TrainingCertificateSection trainingCertificateSection,
            TrainingCertificate trainingCertificate, File trainingCertificateFile, boolean isNihOerIssued)
            throws IOException {

        final EditTrainingCertificateDialog createTrainingCertificateDialog = trainingCertificateSection
                .clickAddCertificate();
        createTrainingCertificateDialog.getHelper().enterTrainingCertificateData(trainingCertificate,
                trainingCertificateFile, isNihOerIssued);

        createTrainingCertificateDialog.clickSave();
        assertNotNull(trainingCertificateSection.getHelper().getListing(trainingCertificate));
    }

    protected void saveAndVerifyTrainingCertificate(TrainingCertificateSection trainingCertificateSection,
            TrainingCertificate trainingCertificate, File trainingCertificateFile, boolean isNihOerIssued,
            ExpectedValidationFailure expectedValidationFailure) throws IOException {

        final EditTrainingCertificateDialog createTrainingCertificateDialog = trainingCertificateSection
                .clickAddCertificate();
        createTrainingCertificateDialog.getHelper().enterTrainingCertificateData(trainingCertificate,
                trainingCertificateFile, isNihOerIssued);

        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                createTrainingCertificateDialog.clickSave();
            }
        });
    }

    protected void saveAndVerifyNewDegree(DegreeSection degreeSection, Degree degree) {
        ManageDegreeDialog createDegreeDialog = degreeSection.clickAddDegree();
        createDegreeDialog.getHelper().enterDegreeData(degree);
        createDegreeDialog.clickSave();
        assertNotNull(degreeSection.getHelper().getListing(degree));
    }

    protected void saveAndVerifyNewDegree(DegreeSection degreeSection, Degree degree,
            ExpectedValidationFailure expectedValidationFailure) {
        final ManageDegreeDialog createDegreeDialog = degreeSection.clickAddDegree();
        createDegreeDialog.getHelper().enterDegreeData(degree);
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                createDegreeDialog.clickSave();
            }
        });
        createDegreeDialog.clickCancel();
    }

    protected void saveAndVerifyNewLicense(LicenseSection licenseSection, MedicalLicense license) {
        ManageLicenseDialog createLicenseDialog = licenseSection.clickAddLicense();
        createLicenseDialog.getHelper().saveLicenseData(license);
        licenseSection.getHelper().verifyLicenseListed(license);
    }

    protected void saveAndVerifyNewSpecialty(SpecialtySection specialtySection, BoardCertifiedSpecialty specialty) {
        ManageSpecialtyDialog createSpecialtyDialog = specialtySection.clickAddSpecialty();
        createSpecialtyDialog.getHelper().saveSpecialtyData(specialty);
        specialtySection.getHelper().verifySpecialtyListed(specialty);
    }

    protected void saveAndVerifyNewCertification(CertificationSection certificationSection, Certification certification) {
        ManageCertificationDialog createCertificationDialog = certificationSection.clickAddCertification();
        createCertificationDialog.getHelper().saveCertificationData(certification);
        certificationSection.getHelper().verifyCertificationListed(certification);
    }

    protected void saveAndVerifyNewCertificate(TrainingCertificateSection trainingSection,
            TrainingCertificate certificate, boolean isNihOerIssued) throws IOException {
        File certificateFile = TestFileUtils.createTemporaryFile();
        EditTrainingCertificateDialog certificateDialog = trainingSection.clickAddCertificate();
        certificateDialog.getHelper().enterTrainingCertificateData(certificate, certificateFile, isNihOerIssued);
        certificateDialog.clickSave();
        trainingSection.getHelper().verifyCertificateListed(certificate);
    }

    protected void saveAndVerifyNewWorkHistory(WorkHistorySection section, WorkHistory workHistory) {
        EditWorkHistoryDialog createDialog = section.clickAddWorkHistory();
        createDialog.getHelper().setWorkHistory(workHistory);
        createDialog.clickSave();
        section.getHelper().verifyWorkHistoryListed(workHistory);
    }

    protected void saveAndVerifyNewInternship(InternshipSection section, Internship internship) {
        EditSpecialtyCredentialDialog createDialog = section.clickAddCredential();
        createDialog.getHelper().setCredential(internship);
        createDialog.clickSave();
        section.getHelper().verifyCredentialListed(internship);
    }

    protected void saveAndVerifyNewResidency(ResidencySection section, Residency residency) {
        EditSpecialtyCredentialDialog createDialog = section.clickAddCredential();
        createDialog.getHelper().setCredential(residency);
        createDialog.clickSave();
        section.getHelper().verifyCredentialListed(residency);
    }

    protected void saveAndVerifyNewFellowship(FellowshipSection section, Fellowship fellowship) {
        EditSpecialtyCredentialDialog createDialog = section.clickAddCredential();
        createDialog.getHelper().setCredential(fellowship);
        createDialog.clickSave();
        section.getHelper().verifyCredentialListed(fellowship);
    }

    protected void changeDegreeData(Degree degree) {
        degree.setIssuer(getNewIssuer());
        changeDates(degree);
    }

    private void changeDates(AbstractCredential<?> credential) {
        if (credential.getEffectiveDate() != null) {
            credential.setEffectiveDate(subtractYearAndMonth(credential.getEffectiveDate()));
        }
        if (credential.getExpirationDate() != null) {
            credential.setExpirationDate(addYearAndMonth(credential.getExpirationDate()));
        }
    }

    private Date subtractYearAndMonth(Date effectiveDate) {
        return DateUtils.addYears(DateUtils.addMilliseconds(effectiveDate, -1), -1);
    }

    private Date addYearAndMonth(Date effectiveDate) {
        return DateUtils.addYears(DateUtils.addMilliseconds(effectiveDate, 1), 1);
    }

    protected void changeLicenseData(MedicalLicense license) {
        license.setIssuer(getNewIssuer());
        changeDates(license);
        license.setLicenseId(getUniqueString(10));
    }

    protected void changeSpecialtyData(BoardCertifiedSpecialty specialty) {
        changeSpecialtyType(specialty);
        changeDates(specialty);
        specialty.setStatus(CertificationStatus.values()[specialty.getStatus().ordinal() + 1
                % CertificationStatus.values().length]);
    }

    protected void changeSpecialtyType(BoardCertifiedSpecialty specialty) {
        CertifiedSpecialtyType currentType = specialty.getSpecialtyType();
        if (currentType != null) {
            CertifiedSpecialtyType newType = getDifferentListEntry(getSpecialtyTypes(), currentType);
            specialty.setSpecialtyType(newType);
        }
    }

    private <T extends ListItem> T getDifferentListEntry(List<T> list, T currentItem) {
        ArrayList<T> copy = Lists.newArrayList(list);
        copy.remove(currentItem);
        return Iterables.getFirst(copy, null);
    }

    protected void changeCertificationData(Certification certification) {
        changeCertificationType(certification);
        changeDates(certification);
    }

    private void changeCertificationType(Certification certification) {
        CertificationType certificationType = certification.getCertificationType();
        if (certificationType != null) {
            CertificationType newType = getDifferentListEntry(getCertificationTypes(), certificationType);
            certification.setCertificationType(newType);
        }
    }

    protected void changeCertificateData(TrainingCertificate certificate) {
        changeCertificateType(certificate);
        changeDates(certificate);
    }

    protected void changeWorkHistoryData(WorkHistory workHistory) {
        workHistory.setIssuer(getExistingIssuer());
        workHistory.setPosition(getUniqueString());
        changeDates(workHistory);
    }

    protected void changeInternshipData(Internship internship) {
        internship.setIssuer(getExistingIssuer());
        changeDates(internship);
    }

    protected void changeResidencyData(Residency residency) {
        residency.setIssuer(getExistingIssuer());
        changeDates(residency);
    }

    protected void changeFellowshipData(Fellowship fellowship) {
        fellowship.setIssuer(getExistingIssuer());
        changeDates(fellowship);
    }

    private void changeCertificateType(TrainingCertificate certificate) {
        CertificateType certificateType = certificate.getCertificateType();
        if (certificateType != null) {
            CertificateType newType = getDifferentListEntry(certificateType);
            certificate.setCertificateType(newType);
        }
    }

    private CertificateType getDifferentListEntry(CertificateType type) {
        for (CertificateType certType : CertificateType.values()) {
            if (certType != type) {
                return certType;
            }
        }
        return type;
    }

    protected void updateAndVerifyDegree(DegreeSection degreeSection, Degree degree) {
        ManageDegreeDialog dialog = degreeSection.getListings().get(0).clickEditLink();
        assertFalse(dialog.canEditOrganization());
        dialog.clickSearchAgain();
        dialog.getHelper().enterDegreeData(degree);
        dialog.clickSave();
        assertNotNull(degreeSection.getHelper().getListing(degree));
    }

    protected void updateAndVerifyLicense(LicenseSection licenseSection, MedicalLicense license) {
        ManageLicenseDialog dialog = licenseSection.getListings().get(0).clickEditLink();
        dialog.getHelper().saveLicenseData(license);
        licenseSection.getHelper().verifyLicenseListed(license);
    }

    protected void updateAndVerifySpecialty(SpecialtySection specialtySection, BoardCertifiedSpecialty specialty) {
        ManageSpecialtyDialog dialog = specialtySection.getListings().get(0).clickEditLink();
        dialog.getHelper().saveSpecialtyData(specialty);
        specialtySection.getHelper().verifySpecialtyListed(specialty);
    }

    protected void updateAndVerifyCertification(CertificationSection certificationSection, Certification certification) {
        ManageCertificationDialog editDialog = certificationSection.getListings().get(0).clickEditLink();
        editDialog.getHelper().saveCertificationData(certification);
        certificationSection.getHelper().verifyCertificationListed(certification);
    }

    protected void updateAndVerifyCertificate(TrainingCertificateSection trainingCertificateSection,
            TrainingCertificate certificate, boolean isNihOerIssued) throws IOException {
        File certificateFile = TestFileUtils.createTemporaryFile();
        EditTrainingCertificateDialog editDialog = trainingCertificateSection.getListings().get(0).clickEditLink();
        editDialog.getHelper().enterTrainingCertificateData(certificate, certificateFile, isNihOerIssued);
        editDialog.clickSave();
        trainingCertificateSection.getHelper().verifyCertificateListed(certificate);
    }

    protected void updateAndVerifyWorkHistory(WorkHistorySection section, WorkHistory workHistory) {
        EditWorkHistoryDialog editDialog = section.getListings().get(0).clickEdit();
        editDialog.getHelper().setWorkHistory(workHistory);
        editDialog.clickSave();
        section.getHelper().verifyWorkHistoryListed(workHistory);
    }

    protected void updateAndVerifyInternship(InternshipSection section, Internship internship) {
        EditSpecialtyCredentialDialog editDialog = section.getListings().get(0).clickEdit();
        editDialog.getHelper().setCredential(internship);
        editDialog.clickSave();
        section.getHelper().verifyCredentialListed(internship);
    }

    protected void updateAndVerifyResidency(ResidencySection section, Residency residency) {
        EditSpecialtyCredentialDialog editDialog = section.getListings().get(0).clickEdit();
        editDialog.getHelper().setCredential(residency);
        editDialog.clickSave();
        section.getHelper().verifyCredentialListed(residency);
    }

    protected void updateAndVerifyFellowship(FellowshipSection section, Fellowship fellowship) {
        EditSpecialtyCredentialDialog editDialog = section.getListings().get(0).clickEdit();
        editDialog.getHelper().setCredential(fellowship);
        editDialog.clickSave();
        section.getHelper().verifyCredentialListed(fellowship);
    }

    protected DataSet getDataSet() {
        return dataSet;
    }

}
