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
package gov.nih.nci.firebird.test;

import static gov.nih.nci.firebird.test.ValueGenerator.*;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.BoardCertifiedSpecialty;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.Certification;
import gov.nih.nci.firebird.data.CertificationStatus;
import gov.nih.nci.firebird.data.CertificationType;
import gov.nih.nci.firebird.data.CertifiedSpecialtyBoard;
import gov.nih.nci.firebird.data.CertifiedSpecialtyType;
import gov.nih.nci.firebird.data.ClinicalResearchExperience;
import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.DegreeType;
import gov.nih.nci.firebird.data.Fellowship;
import gov.nih.nci.firebird.data.Internship;
import gov.nih.nci.firebird.data.MedicalLicense;
import gov.nih.nci.firebird.data.MedicalLicenseType;
import gov.nih.nci.firebird.data.MedicalSpecialty;
import gov.nih.nci.firebird.data.MedicalSpecialtyCertifyingBoard;
import gov.nih.nci.firebird.data.MedicalSubSpecialty;
import gov.nih.nci.firebird.data.MedicalSubSpecialtyCertifyingBoard;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Residency;
import gov.nih.nci.firebird.data.SpecialtyDesignation;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.WorkHistory;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.time.DateUtils;

import com.google.common.collect.Iterators;

public class CredentialFactory {

    private static final CredentialFactory INSTANCE = new CredentialFactory();

    private static final Iterator<String> DEGREE_TYPE_NAMES = Iterators.cycle(new String[] { "MD", "DO", "DVM", "DDs",
            "Pharm. D.", "Ed. D.", "MSN", "MPH", "MA", "BSN", "BN", "AA", });

    private static final Iterator<String> CERTIFICATION_TYPE_NAMES = Iterators.cycle(new String[] { "PAC", "APRN",
            "CCRA", "CCRC", "OCN" });

    private static final Iterator<String> LICENSE_TYPE_NAMES = Iterators.cycle(new String[] {
            "L.P.N. - Licensed Practical Nurse", "R.N. - Registered Nurse ", "R.N.P. - Registered Nurse Practitioner",
            "R.Ph. - Registered Pharmacist", "Sc.D. - Doctor of Science" });

    private static final Iterator<Object[]> SPECIALTIES = Iterators.cycle(new Object[][] {
            { "American Board of Allergy and Immunology", "Allergy and Immunology" },
            { "American Board of Anesthesiology", "Anesthesiology" },
            { "American Board of Anesthesiology", "Hospice and Palliative Medicine" },
            { "American Board of Emergency Medicine", "Pediatric Emergency Medicine" },
            { "American Board of Emergency Medicine", "Allergy and Immunology" },
            { "American Board of Medical Genetics", "Clinical Biochemical Genetics" },
            { "American Board of Medical Genetics", "Clinical Cytogenetics" } });
    private static final Iterator<SpecialtyDesignation> SPECIALTY_SPECIALTY_DESIGNATION = Iterators
            .cycle(SpecialtyDesignation.values());

    private static final Iterator<CertificationStatus> SPECIALTY_CERTIFICATION_STATUS = Iterators
            .cycle(CertificationStatus.values());

    private static final Iterator<CertificateType> CERTIFICATE_TYPES = Iterators.cycle(CertificateType.values());

    private static final Iterator<String> MEDICAL_SPECIALTY_NAMES = Iterators.cycle(new String[] { "Anesthesiology",
            "Dermatology", "Emergency Medicine", "Family Medicine", "Internal Medicine", "Neurological Surgery",
            "Pediatrics" });

    private static final Iterator<String> MEDICAL_SPECIALTY_CERTIFYING_BOARD_NAMES = Iterators.cycle(new String[] { "ABMS - American Board of Medical Specialties",
            "AOBOS - American Osteopathic Board of Orthopedic Surgery", "ABPS - American Board of Physician Specialties" });

    private static final Iterator<String> MEDICAL_SUBSPECIALTY_NAMES = Iterators.cycle(new String[] { "Allergy, Asthma & Immunology",
            "Cardiology", "Critical Care Medicine", "Endocrinology", "Gastroenterology", "Geriatrics",
            "Hematology" });

    private static final Iterator<String> MEDICAL_SUBSPECIALTY_CERTIFYING_BOARD_NAMES = Iterators.cycle(new String[] { "ACGME - Accreditation Council for Graduate Medical Education" });

    private CredentialFactory() {
    }

    public static CredentialFactory getInstance() {
        return INSTANCE;
    }

    public BoardCertifiedSpecialty createSpecialty() {
        CertifiedSpecialtyType specialtyType = nextCertifiedSpecialtyType();
        specialtyType.getBoard().setId(getUniqueLong());
        return createSpecialty(specialtyType);
    }

    public BoardCertifiedSpecialty createSpecialty(CertifiedSpecialtyType specialtyType) {
        BoardCertifiedSpecialty specialty = new BoardCertifiedSpecialty();
        setCredentialFields(specialty);
        specialty.setExpirationDate(DateUtils.addYears(specialty.getEffectiveDate(), 1));
        specialty.setSpecialtyType(specialtyType);
        specialty.setStatus(SPECIALTY_CERTIFICATION_STATUS.next());
        return specialty;
    }

    protected CertifiedSpecialtyType nextCertifiedSpecialtyType() {
        Object[] specialtyParams = SPECIALTIES.next();
        CertifiedSpecialtyType type = new CertifiedSpecialtyType(specialtyParams[1].toString(),
                new CertifiedSpecialtyBoard(specialtyParams[0].toString()), SPECIALTY_SPECIALTY_DESIGNATION.next());
        type.setId(getUniqueLong());
        return type;
    }

    public Degree createDegree() {
        return createDegree(DEGREE_TYPE_NAMES.next());
    }

    protected DegreeType nextDegreeType(boolean mdOrEquivalent) {
        DegreeType degreeType = new DegreeType(DEGREE_TYPE_NAMES.next());
        degreeType.setMdOrEquivalent(mdOrEquivalent);
        return degreeType;
    }

    public Degree createDegree(String degreeTypeName) {
        return createDegree(degreeTypeName, false);
    }

    public Degree createDegree(String degreeTypeName, boolean mdOrEquivalent) {
        DegreeType degreeType = nextDegreeType(mdOrEquivalent);
        return createDegree(degreeType);
    }

    public Degree createDegree(DegreeType degreeType) {
        return createDegree(degreeType, OrganizationFactory.getInstance().create());
    }

    public Degree createDegree(Organization issuer) {
        DegreeType degreeType = nextDegreeType(false);
        return createDegree(degreeType, issuer);
    }

    public Degree createDegree(DegreeType degreeType, Organization issuer) {
        Degree degree = new Degree();
        setCredentialFields(degree);
        degree.setIssuer(issuer);
        degree.setDegreeType(degreeType);
        return degree;
    }

    public MedicalLicense createLicense() {
        return createLicense(nextMedicalLicenseType());
    }

    public MedicalLicense createLicense(MedicalLicenseType licenseType) {
        MedicalLicense license = new MedicalLicense();
        setCredentialFields(license);
        license.setCountry(FirebirdConstants.US_COUNTRY_CODE);
        license.setState("MD");
        license.setLicenseId(getUniqueString());
        license.setExpirationDate(DateUtils.addYears(license.getEffectiveDate(), 1));
        license.setLicenseType(licenseType);
        return license;
    }

    protected MedicalLicenseType nextMedicalLicenseType() {
        return new MedicalLicenseType(LICENSE_TYPE_NAMES.next());
    }

    public Certification createCertification() {
        return createCertification(nextCertificationType());
    }

    public Certification createCertification(CertificationType certificationType) {
        Certification certification = new Certification();
        setCredentialFields(certification);
        certification.setExpirationDate(DateUtils.addYears(certification.getEffectiveDate(), 1));
        certification.setCertificationType(certificationType);
        return certification;
    }

    public TrainingCertificate createCertificate(CertificateType type) {
        return createCertificate(type, OrganizationFactory.getInstance().create());
    }

    public TrainingCertificate createCertificate(CertificateType type, Organization issuer) {
        TrainingCertificate certificate = new TrainingCertificate();
        setCredentialFields(certificate);
        certificate.setExpirationDate(DateUtils.addYears(certificate.getEffectiveDate(), 1));
        certificate.setCertificateType(type);
        certificate.setFile(FirebirdFileFactory.getInstance().create());
        certificate.setIssuer(issuer);
        return certificate;
    }

    public TrainingCertificate createCertificate(File file, Organization issuer) {
        TrainingCertificate certificate = createCertificate(nextCertificateType(), issuer);
        certificate.getFile().setName(file.getName());
        certificate.getFile().setUploadDate(new Date());
        return certificate;
    }

    public TrainingCertificate createCertificate(File file) {
        return createCertificate(file, OrganizationFactory.getInstance().create());
    }

    public TrainingCertificate createCertificate(Organization issuer) {
        return createCertificate(nextCertificateType(), issuer);
    }

    public TrainingCertificate createCertificate() {
        return createCertificate(nextCertificateType(), OrganizationFactory.getInstance().create());
    }

    protected CertificateType nextCertificateType() {
        return CERTIFICATE_TYPES.next();
    }

    public ClinicalResearchExperience createExperience() {
        StringBuilder exp = new StringBuilder();
        exp.append("<p>");
        for (int i = 0; i < 10; i++) {
            exp.append(getUniqueString(i)).append(' ').append(getUniqueString(i));
            if (i % 3 == 0) {
                exp.append("<br/>");
            }
        }
        exp.append("</p>");
        return new ClinicalResearchExperience(exp.toString());
    }

    protected CertificationType nextCertificationType() {
        return new CertificationType(CERTIFICATION_TYPE_NAMES.next(), Boolean.TRUE);
    }

    private void setCredentialFields(AbstractCredential<?> credential) {
        credential.setEffectiveDate(getUniqueMonth());
    }

    public Internship createInternship() {
        return createInternship(OrganizationFactory.getInstance().create());
    }

    protected MedicalSpecialty nextSpecialty() {
        MedicalSpecialty specialty = new MedicalSpecialty(MEDICAL_SPECIALTY_NAMES.next(),
                createMedicalSpecialtyCertifyingBoard());
        return specialty;
    }

    public MedicalSpecialtyCertifyingBoard createMedicalSpecialtyCertifyingBoard() {
        return new MedicalSpecialtyCertifyingBoard(MEDICAL_SPECIALTY_CERTIFYING_BOARD_NAMES.next());
    }

    public Internship createInternship(Organization issuer) {
        return createInternship(issuer, nextSpecialty());
    }

    public Internship createInternship(MedicalSpecialty specialty) {
        return createInternship(OrganizationFactory.getInstance().create(), specialty);
    }

    public Internship createInternship(Organization issuer, MedicalSpecialty specialty) {
        Internship internship = new Internship();
        setCredentialFields(internship);
        internship.setIssuer(issuer);
        internship.setSpecialty(specialty);
        return internship;
    }

    public Residency createResidency() {
        return createResidency(OrganizationFactory.getInstance().create());
    }

    public Residency createResidency(Organization issuer) {
        return createResidency(issuer, nextSpecialty());
    }

    public Residency createResidency(MedicalSpecialty specialty) {
        return createResidency(OrganizationFactory.getInstance().create(), specialty);
    }

    public Residency createResidency(Organization issuer, MedicalSpecialty specialty) {
        Residency residency = new Residency();
        setCredentialFields(residency);
        residency.setIssuer(issuer);
        residency.setSpecialty(specialty);
        return residency;
    }

    public Fellowship createFellowship() {
        return createFellowship(OrganizationFactory.getInstance().create());
    }

    public Fellowship createFellowship(Organization issuer) {
        return createFellowship(issuer, nextSubSpecialty());
    }

    public Fellowship createFellowship(MedicalSubSpecialty specialty) {
        return createFellowship(OrganizationFactory.getInstance().create(), specialty);
    }

    public Fellowship createFellowship(Organization issuer, MedicalSubSpecialty specialty) {
        Fellowship fellowship = new Fellowship();
        setCredentialFields(fellowship);
        fellowship.setIssuer(issuer);
        fellowship.setSpecialty(specialty);
        return fellowship;
    }

    private MedicalSubSpecialty nextSubSpecialty() {
        MedicalSubSpecialty specialty = new MedicalSubSpecialty(MEDICAL_SUBSPECIALTY_NAMES.next(),
                createMedicalSubSpecialtyCertifyingBoard());
        return specialty;
    }

    private MedicalSubSpecialtyCertifyingBoard createMedicalSubSpecialtyCertifyingBoard() {
        return new MedicalSubSpecialtyCertifyingBoard(MEDICAL_SUBSPECIALTY_CERTIFYING_BOARD_NAMES.next());
    }

    public WorkHistory createWorkHistory() {
        return createWorkHistory(OrganizationFactory.getInstance().create());
    }

    public WorkHistory createWorkHistory(Organization issuer) {
        WorkHistory workHistory = new WorkHistory();
        setCredentialFields(workHistory);
        workHistory.setIssuer(issuer);
        workHistory.setPosition(ValueGenerator.getUniqueString());
        return workHistory;
    }

}
