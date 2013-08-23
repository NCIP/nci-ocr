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
package gov.nih.nci.firebird.data;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.test.AbstractHibernateTestCase;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.DiscriminatorValue;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

public class CredentialTest extends AbstractHibernateTestCase {

    private InvestigatorProfile profile;
    private Organization issuer;
    private Date effectiveDate;
    private Date expirationDate;
    private Calendar effectiveDateCalendar;
    private Calendar expirationDateCalendar;

    @Before
    public void prep() {
        profile = InvestigatorProfileFactory.getInstance().create();
        effectiveDateCalendar = Calendar.getInstance();
        effectiveDateCalendar.set(Calendar.DATE, 22);
        effectiveDateCalendar.set(Calendar.MONTH, 2);
        effectiveDateCalendar.set(Calendar.YEAR, 2020);
        effectiveDate = effectiveDateCalendar.getTime();
        expirationDateCalendar = Calendar.getInstance();
        expirationDateCalendar.set(Calendar.DATE, 11);
        expirationDateCalendar.set(Calendar.MONTH, 1);
        expirationDateCalendar.set(Calendar.YEAR, 2111);
        expirationDate = expirationDateCalendar.getTime();
        issuer = OrganizationFactory.getInstance().create();
    }

    @Test
    public void testBoardCertifiedSpecialty() throws CredentialAlreadyExistsException {
        CertifiedSpecialtyBoard board = new CertifiedSpecialtyBoard("American Board of Anesthesiology");
        CertifiedSpecialtyBoard board2 = new CertifiedSpecialtyBoard("American Board of Allergy and Immunology");
        CertifiedSpecialtyType type1 = new CertifiedSpecialtyType("Anesthesiology", board);
        CertifiedSpecialtyType type2 = new CertifiedSpecialtyType("Critical Care Medicine", board,
                SpecialtyDesignation.SUBSPECIALTY);
        CertifiedSpecialtyType type3 = new CertifiedSpecialtyType("Allergy and Immunology", board);
        type1.setName(type1.getName()); // Coverage
        assertNotNull(type1.toString()); // Coverage
        type1.setId(null); // Coverage
        assertFalse(type1.equals(board2));

        assertEquals(type1.getName() + " (" + type1.getDesignation().getDisplay() + ")", type1.getDisplay());

        board.getTypes().add(type1);
        save(board, type1, type2, type3);

        getCurrentSession().save(profile);
        Calendar effectiveDate2 = Calendar.getInstance();
        effectiveDate2.setTime(new Date());
        Calendar expirationDate2 = Calendar.getInstance();
        expirationDate2.setTime(new Date());
        expirationDate2.add(Calendar.YEAR, 1);
        BoardCertifiedSpecialty specialty1 = new BoardCertifiedSpecialty(profile, effectiveDate2.getTime(),
                expirationDate2.getTime(), CertificationStatus.CERTIFIED, type1);
        BoardCertifiedSpecialty specialty2 = new BoardCertifiedSpecialty(profile, effectiveDate, expirationDate,
                CertificationStatus.ELIGIBLE, type2);
        BoardCertifiedSpecialty specialty3 = new BoardCertifiedSpecialty(profile, effectiveDate, expirationDate,
                CertificationStatus.ELIGIBLE, type3);
        assertEquals(CredentialType.SPECIALTY, specialty1.getType());
        assertEquals(CredentialType.SPECIALTY.name(), getDiscriminator(BoardCertifiedSpecialty.class));
        assertFalse(specialty1.equals(null));

        profile.addCredential(specialty1);
        profile.addCredential(specialty2);
        profile.addCredential(specialty3);
        getCurrentSession().save(profile);

        flushAndClearSession();

        assertNotNull(specialty1.getId());
        specialty1 = loadObject(BoardCertifiedSpecialty.class, specialty1.getId());
        specialty2 = loadObject(BoardCertifiedSpecialty.class, specialty2.getId());
        specialty3 = loadObject(BoardCertifiedSpecialty.class, specialty3.getId());

        @SuppressWarnings("unchecked")
        TreeSet<BoardCertifiedSpecialty> specialtyList = new TreeSet<BoardCertifiedSpecialty>(getCurrentSession()
                .createCriteria(BoardCertifiedSpecialty.class).list());
        assertEquals(3, specialtyList.size());

        for (BoardCertifiedSpecialty specialty : specialtyList) {
            if (specialty.getSpecialtyType().equals(specialty1.getSpecialtyType())) {
                assertSame(specialty1, specialty);
            } else if (specialty.getSpecialtyType().equals(specialty2.getSpecialtyType())) {
                assertSame(specialty2, specialty);
            } else {
                assertSame(specialty3, specialty);
            }

        }

        profile = (InvestigatorProfile) getCurrentSession().get(InvestigatorProfile.class, profile.getId());
        assertEquals(3, profile.getCredentials().size());

        for (BoardCertifiedSpecialty specialty : profile.getCredentials(BoardCertifiedSpecialty.class)) {
            assertSame(profile, specialty.getProfile());
            if (specialty.getSpecialtyType().equals(specialty1.getSpecialtyType())) {
                assertDateEquals(effectiveDate2, specialty.getEffectiveDate());
                assertDateEquals(expirationDate2, specialty.getExpirationDate());
                assertEquals(CertificationStatus.CERTIFIED, specialty.getStatus());
                assertEquals(type1.getId(), specialty.getSpecialtyType().getId());
            } else if (specialty.getSpecialtyType().equals(specialty2.getSpecialtyType())) {
                assertDateEquals(effectiveDateCalendar, specialty.getEffectiveDate());
                assertDateEquals(expirationDateCalendar, specialty.getExpirationDate());
                assertEquals(CertificationStatus.ELIGIBLE, specialty.getStatus());
                assertEquals(type2.getId(), specialty.getSpecialtyType().getId());
            } else {
                assertDateEquals(effectiveDateCalendar, specialty.getEffectiveDate());
                assertDateEquals(expirationDateCalendar, specialty.getExpirationDate());
                assertEquals(CertificationStatus.ELIGIBLE, specialty.getStatus());
                assertEquals(type3.getId(), specialty.getSpecialtyType().getId());
            }

        }
    }

    @Test
    public void testCertification() {
        CertificationType certificationType = new CertificationType("A.P.R.N - Advanced Practice Registered Nurse",
                true);
        getCurrentSession().save(certificationType);

        getCurrentSession().save(profile);
        Certification certification = new Certification(profile, certificationType, effectiveDate, expirationDate);

        assertFalse(certification.equals(new Degree()));

        assertEquals(CredentialType.CERTIFICATION, certification.getType());
        assertEquals(CredentialType.CERTIFICATION.name(), getDiscriminator(Certification.class));

        profile.getCredentials().add(certification);
        getCurrentSession().save(profile);

        flushAndClearSession();

        @SuppressWarnings("unchecked")
        List<Certification> certifications = getCurrentSession().createCriteria(Certification.class).list();
        assertEquals(1, certifications.size());

        profile = (InvestigatorProfile) getCurrentSession().get(InvestigatorProfile.class, profile.getId());
        assertEquals(1, profile.getCredentials().size());
        assertEquals(certifications.get(0), profile.getCredentials().iterator().next());
        assertSame(certifications.get(0), profile.getCredentials().iterator().next());

        certification = profile.getCredentials(Certification.class).iterator().next();
        assertSame(profile, certification.getProfile());
        assertDateEquals(effectiveDateCalendar, certification.getEffectiveDate());
        assertDateEquals(expirationDateCalendar, certification.getExpirationDate());
        assertEquals(certificationType.getId(), certification.getCertificationType().getId());
    }

    @Test
    public void testCertificate_DuplicateCertificate() {
        CertificateType certificateType = CertificateType.HUMAN_RESEARCH_CERTIFICATE;
        FirebirdFile file = FirebirdFileFactory.getInstance().create();

        TrainingCertificate certificate = new TrainingCertificate(profile, effectiveDate, expirationDate,
                certificateType, file);
        TrainingCertificate duplicateCertificate = new TrainingCertificate(profile, effectiveDate, expirationDate,
                certificateType, file);
        assertTrue(certificate.equals(duplicateCertificate));
    }

    @Test
    public void testCertificate_DifferentProfile() {
        CertificateType certificateType = CertificateType.HUMAN_RESEARCH_CERTIFICATE;
        FirebirdFile file = FirebirdFileFactory.getInstance().create();

        TrainingCertificate certificate = new TrainingCertificate(profile, effectiveDate, expirationDate,
                certificateType, file);
        TrainingCertificate duplicateCertificate = new TrainingCertificate(InvestigatorProfileFactory.getInstance()
                .create(), effectiveDate, expirationDate, certificateType, file);
        assertFalse(certificate.equals(duplicateCertificate));
    }

    @Test
    public void testCertificate_DifferentCertificate() {
        CertificateType certificateType = CertificateType.HUMAN_RESEARCH_CERTIFICATE;
        FirebirdFile file = FirebirdFileFactory.getInstance().create();

        TrainingCertificate certificate = new TrainingCertificate(profile, effectiveDate, expirationDate,
                certificateType, file);
        TrainingCertificate duplicateCertificate = new TrainingCertificate(InvestigatorProfileFactory.getInstance()
                .create(), DateUtils.addDays(effectiveDate, -1), DateUtils.addDays(expirationDate, 1), certificateType,
                file);
        assertFalse(certificate.equals(duplicateCertificate));
    }

    @Test
    public void testCertificate_WrongType() {
        CertificateType certificateType = CertificateType.HUMAN_RESEARCH_CERTIFICATE;
        FirebirdFile file = FirebirdFileFactory.getInstance().create();

        TrainingCertificate certificate = new TrainingCertificate(profile, effectiveDate, expirationDate,
                certificateType, file);
        assertFalse(certificate.equals(new Degree()));
    }

    @Test
    public void testCertificate_Self() {
        CertificateType certificateType = CertificateType.HUMAN_RESEARCH_CERTIFICATE;
        FirebirdFile file = FirebirdFileFactory.getInstance().create();

        TrainingCertificate certificate = new TrainingCertificate(profile, effectiveDate, expirationDate,
                certificateType, file);
        assertTrue(certificate.equals(certificate));
        assertEquals(CredentialType.CERTIFICATE, certificate.getType());
        assertEquals(CredentialType.CERTIFICATE.name(), getDiscriminator(TrainingCertificate.class));
    }

    @Test
    public void testCertificateDelete() {
        TrainingCertificate c = makeCertificate();

        flushAndClearSession();
        TrainingCertificate c2 = loadObject(TrainingCertificate.class, c.getId());
        getCurrentSession().delete(c2);
        flushAndClearSession();
        profile = loadObject(InvestigatorProfile.class, profile.getId());
        assertTrue(profile.getCredentials().isEmpty());
    }

    @Test
    public void testCertificateComparator_LessThan() {
        FirebirdFile file1 = FirebirdFileFactory.getInstance().create();
        FirebirdFile file2 = FirebirdFileFactory.getInstance().create();
        file2.setName("_");
        TrainingCertificate certificate1 = new TrainingCertificate(profile, new Date(9999L), expirationDate,
                CertificateType.HUMAN_RESEARCH_CERTIFICATE, file1);
        TrainingCertificate certificate2 = new TrainingCertificate(profile, new Date(10000L), expirationDate,
                CertificateType.HUMAN_RESEARCH_CERTIFICATE, file2);

        assertTrue(certificate1.compareTo(certificate2) < 0);
    }

    @Test
    public void testCertificateComparator_Equal() {
        Date date = new Date();
        FirebirdFile file = FirebirdFileFactory.getInstance().create();
        TrainingCertificate certificate1 = new TrainingCertificate(profile, date, expirationDate,
                CertificateType.HUMAN_RESEARCH_CERTIFICATE, file);
        TrainingCertificate certificate2 = new TrainingCertificate(profile, date, expirationDate,
                CertificateType.HUMAN_RESEARCH_CERTIFICATE, file);

        certificate1.setEffectiveDate(certificate2.getEffectiveDate());
        assertTrue(certificate1.compareTo(certificate2) == 0);
    }

    @Test
    public void testCertificateComparator_GreaterThan() {
        Date date = new Date();
        FirebirdFile file1 = FirebirdFileFactory.getInstance().create();
        file1.setName("_");
        FirebirdFile file2 = FirebirdFileFactory.getInstance().create();

        TrainingCertificate certificate1 = new TrainingCertificate(profile, date, expirationDate,
                CertificateType.HUMAN_RESEARCH_CERTIFICATE, file1);
        TrainingCertificate certificate2 = new TrainingCertificate(profile, date, new Date(200L),
                CertificateType.HUMAN_RESEARCH_CERTIFICATE, file2);

        certificate2.setExpirationDate(new Date(200L));
        assertTrue(certificate1.compareTo(certificate2) > 0);
    }

    @Test
    public void testDegree() {
        DegreeType t = new DegreeType("Pharm. D.");
        getCurrentSession().save(t);

        getCurrentSession().save(profile);
        Degree degree = new Degree(profile, effectiveDate, issuer, t);
        Degree degree2 = new Degree(profile, new Date(System.currentTimeMillis() - 1000), issuer, t);
        assertTrue(degree2.compareTo(degree) < 1);
        assertEquals(CredentialType.DEGREE, degree.getType());
        assertEquals(CredentialType.DEGREE.name(), getDiscriminator(Degree.class));
        assertFalse(degree.equals(new TrainingCertificate()));

        profile.getCredentials().add(degree);
        getCurrentSession().save(profile);

        flushAndClearSession();

        @SuppressWarnings("unchecked")
        List<Certification> l = getCurrentSession().createCriteria(Degree.class).list();
        assertEquals(1, l.size());

        profile = (InvestigatorProfile) getCurrentSession().get(InvestigatorProfile.class, profile.getId());
        assertEquals(1, profile.getCredentials().size());
        assertSame(l.get(0), profile.getCredentials().iterator().next());

        degree = profile.getCredentials(Degree.class).iterator().next();
        assertSame(profile, degree.getProfile());
        assertDateEquals(effectiveDateCalendar, degree.getEffectiveDate());
        assertEquals(issuer.getId(), degree.getIssuer().getId());
        assertEquals(t.getId(), degree.getDegreeType().getId());
    }

    @Test
    public void testMedicalLicense() throws CredentialAlreadyExistsException {
        getCurrentSession().save(profile);
        MedicalLicense license = CredentialFactory.getInstance().createLicense();
        Set<MedicalLicense> sortLicenses = new TreeSet<MedicalLicense>();
        sortLicenses.add(license);
        sortLicenses.add(CredentialFactory.getInstance().createLicense());

        assertEquals(CredentialType.MEDICAL_LICENSE, license.getType());
        assertEquals(CredentialType.MEDICAL_LICENSE.name(), getDiscriminator(MedicalLicense.class));

        assertFalse(license.equals(CredentialFactory.getInstance().createDegree()));
        assertFalse(license.equals(CredentialFactory.getInstance().createLicense()));

        Iterator<MedicalLicense> licenseIterator = sortLicenses.iterator();
        assertTrue(licenseIterator.next().compareTo(licenseIterator.next()) < 0); // first item comes before second
                                                                                  // item.

        profile.addCredential(license);
        saveAndFlush(license.getLicenseType(), profile);

        @SuppressWarnings("unchecked")
        List<Certification> licenses = getCurrentSession().createCriteria(MedicalLicense.class).list();
        assertEquals(1, licenses.size());

        profile = (InvestigatorProfile) getCurrentSession().get(InvestigatorProfile.class, profile.getId());
        assertEquals(1, profile.getCredentials().size());
        assertSame(licenses.get(0), profile.getCredentials().iterator().next());

        MedicalLicense savedLicense = profile.getCredentials(MedicalLicense.class).iterator().next();
        assertSame(profile, savedLicense.getProfile());
        assertEquals(license.getCountry(), savedLicense.getCountry());
        assertEquals(license.getState(), savedLicense.getState());
        assertEquals(license.getLicenseId(), savedLicense.getLicenseId());
        assertEquals(license.getLicenseType(), savedLicense.getLicenseType());
        assertEquals(license.getExpirationDate(), license.getExpirationDate());
    }

    @Test(expected = CredentialAlreadyExistsException.class)
    public void testMedicalLicenseDuplicate() throws CredentialAlreadyExistsException {
        getCurrentSession().save(profile);
        MedicalLicense license = CredentialFactory.getInstance().createLicense();
        license.setId(1L);
        MedicalLicense license2 = new MedicalLicense();
        license2.setCountry(license.getCountry());
        license2.setState(license.getState());
        license2.setEffectiveDate(license.getEffectiveDate());
        license2.setExpirationDate(license.getExpirationDate());
        license2.setLicenseId("123456");
        license2.setLicenseType(license.getLicenseType());

        profile.addCredential(license);
        profile.addCredential(license2);

    }

    @Test
    public void testIsExpired() {
        Calendar cal = Calendar.getInstance();
        Certification cred = new Certification();
        assertNull(cred.getExpirationDate());
        assertFalse(cred.isExpired());

        // expired 2 years ago
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.YEAR, -2);
        cred.setExpirationDate(cal.getTime());
        assertTrue(cred.isExpired());

        // expires in 2 years
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.YEAR, 2);
        cred.setExpirationDate(cal.getTime());
        assertFalse(cred.isExpired());

        // same year, same month
        cal.setTimeInMillis(System.currentTimeMillis());
        cred.setExpirationDate(cal.getTime());
        assertFalse(cred.isExpired());

        // same year, expired last month
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.MONTH, -1);
        cred.setExpirationDate(cal.getTime());
        assertTrue(cred.isExpired());
    }

    private static String getDiscriminator(Class<? extends AbstractCredential<?>> clazz) {
        DiscriminatorValue a = clazz.getAnnotation(DiscriminatorValue.class);
        return a.value();
    }

    private void assertDateEquals(Calendar expected, Date actual) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(actual);
        assertEquals(cal.get(Calendar.YEAR), expected.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), expected.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DATE), expected.get(Calendar.DATE));
    }

    @SuppressWarnings({ "rawtypes" })
    // No need to worry about dealing with Genericising internal AbstractCredential subclass
    @Test
    public void testEquals() {
        Degree c1 = new Degree(profile, effectiveDate, issuer, null);
        Degree c2 = new Degree(profile, effectiveDate, issuer, null);
        @SuppressWarnings("serial")
        AbstractCredential abs = new AbstractCredential() {
            @Override
            public CredentialType getType() {
                return null;
            }

            @Override
            public int compareTo(Object arg0) {
                return 0;
            }

            @Override
            public AbstractCredentialSnapshot createSnapshot() {
                return null;
            }
        };
        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c2));
        c2.setEffectiveDate(new Date());
        assertFalse(c1.equals(c2));
        c2.setEffectiveDate(c1.getEffectiveDate());
        c2.setIssuer(null);
        assertFalse(abs.equals(c2));
        assertFalse(abs.equals("foo"));
    }

    private TrainingCertificate makeCertificate() {
        CertificateType t = CertificateType.HUMAN_RESEARCH_CERTIFICATE;
        FirebirdFile f = FirebirdFileFactory.getInstance().create();
        TrainingCertificate c = new TrainingCertificate(profile, effectiveDate, expirationDate, t, f);
        profile.getCredentials().add(c);
        getCurrentSession().save(profile);

        return c;
    }

}
