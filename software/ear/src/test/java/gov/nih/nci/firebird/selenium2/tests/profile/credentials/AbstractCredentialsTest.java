package gov.nih.nci.firebird.selenium2.tests.profile.credentials;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import gov.nih.nci.firebird.commons.test.TestFileUtils;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.BoardCertifiedSpecialty;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.Certification;
import gov.nih.nci.firebird.data.CertificationStatus;
import gov.nih.nci.firebird.data.CertificationType;
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
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.data.CredentialTypesData;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static gov.nih.nci.firebird.test.ValueGenerator.getUniqueString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author u1dc68 (Manav Kher)
 * @since 6/26/13
 */
public abstract class AbstractCredentialsTest extends AbstractFirebirdWebDriverTest {
    protected DataSet dataSet;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DataSetBuilder builder = new DataSetBuilder(getDataLoader(), getGridResources());
        configureDataSet(builder);
        dataSet = builder.build();
    }

    protected abstract void configureDataSet(DataSetBuilder builder);

    protected Organization getExistingIssuer() {
        return getTestDataSource().getOrganization();
    }

    protected Organization getNewIssuer() {
        return OrganizationFactory.getInstance().createWithoutExternalData();
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

    protected CredentialTypesData getCredentialTypesData() {
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

    protected void setEffectiveAndExpirationDates(AbstractCredential<?> credential) {
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
        specialty.setStatus(CertificationStatus.values()[ specialty.getStatus().ordinal() + 1
                % CertificationStatus.values().length ]);
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


    protected List<MedicalSpecialty> getMedicalSpecialties() {
        return getCredentialTypesData().getMedicalSpecialties();
    }

    protected List<MedicalSubSpecialty> getMedicalSubSpecialties() {
        return getCredentialTypesData().getMedicalSubSpecialties();
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
        expectedValidationFailure.assertFailureOccurs(new ExpectedValidationFailure.FailingAction() {
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
}
