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
package gov.nih.nci.firebird.service.investigatorprofile;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.CertifiedSpecialtyBoard;
import gov.nih.nci.firebird.data.CertifiedSpecialtyType;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SpecialtyDesignation;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.correlation.NesPersonRoleIntegrationService;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonAssociationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationServiceBean;
import gov.nih.nci.firebird.test.AbstractHibernateTestCase;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.PrimaryOrganizationFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import gov.nih.nci.firebird.test.util.ComparisonUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.validator.InvalidStateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class InvestigatorProfileServiceBeanHibernateTest extends AbstractHibernateTestCase {

    @Inject
    private InvestigatorProfileServiceBean bean;
    @Inject
    private PersonService personServiceSpy;
    @Inject
    private ProtocolRegistrationService registrationServiceSpy;
    @Inject
    private PersonAssociationService personAssociationServiceSpy;
    @Mock
    private OrganizationService mockOrganizationService;
    @Mock
    private PersonService mockPersonService;
    @Mock
    private NesPersonRoleIntegrationService mockNesPersonRoleService;
    @Mock
    private OrganizationAssociationService mockOrganizationAssociationService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        bean.setPersonService(mockPersonService);
        bean.setOrganizationService(mockOrganizationService);
        bean.setNesPersonRoleService(mockNesPersonRoleService);
        bean.setRegistrationService(registrationServiceSpy);
        bean.setPersonAssociationService(personAssociationServiceSpy);
        bean.setOrganizationAssociationService(mockOrganizationAssociationService);
        reset(mockPersonService, mockOrganizationService, mockNesPersonRoleService, registrationServiceSpy);
    }

    @Test
    public void testCreateRetrieveUpdateDelete() {
        InvestigatorProfile p1 = createTestProfile();
        Long id = bean.save(p1);

        flushAndClearSession();

        InvestigatorProfile p2 = (InvestigatorProfile) getCurrentSession().get(InvestigatorProfile.class, id);
        ComparisonUtil.assertEquivalent(p1, p2);

        p2.getPerson().setNesId("2");

        bean.save(p2);

        flushAndClearSession();

        p2 = bean.getById(id);
        assertEquals("2", p2.getPerson().getNesId());

        getCurrentSession().clear();

        List<InvestigatorProfile> results = bean.getAll();
        assertEquals(1, results.size());
        assertEquals("2", results.get(0).getPerson().getNesId());

        InvestigatorProfile p3 = createTestProfile();
        p3.getPerson().setNesId("0");
        p3.getPrimaryOrganization().getOrganization().setNesId("0");
        bean.save(p3);

        flushAndClearSession();

        results = bean.getAll();
        assertEquals(2, results.size());

        p2 = bean.getById(id);
        bean.delete(p2);

        results = bean.getAll();
        assertEquals(1, results.size());

        flushAndClearSession();

        assertNull(getCurrentSession().get(InvestigatorProfile.class, id));
    }

    @Test
    public void testSetPrimaryOrganization() throws ValidationException, InterruptedException {
        InvestigatorProfile originalProfile = createTestProfile();

        // Save the profile, which adds a record each to investigator_profile and person tables
        long profileId = bean.save(originalProfile);

        flushAndClearSession();

        InvestigatorProfile retrievedProfile = bean.getById(profileId);
        ComparisonUtil.assertEquivalent(originalProfile.getPrimaryOrganization().getOrganization(),
                retrievedProfile.getPrimaryOrganization().getOrganization());

        flushAndClearSession();

        // When NES ID is valid, profile should reflect updated person values
        retrievedProfile = bean.getById(profileId);
        PrimaryOrganization primaryOrganization = PrimaryOrganizationFactory.getInstance().create();
        bean.setPrimaryOrganization(retrievedProfile, primaryOrganization);
        retrievedProfile = bean.getById(profileId);
        ComparisonUtil.assertEquivalent(originalProfile.getPrimaryOrganization().getOrganization(),
                retrievedProfile.getPrimaryOrganization().getOrganization());
    }

    @Test
    public void testCreatePrimaryOrganization() throws Exception {
        InvestigatorProfile profile = createTestProfile();
        Organization organization = profile.getPrimaryOrganization().getOrganization();
        assertNull(organization.getId());
        bean.createPrimaryOrganization(profile);
        verify(mockOrganizationAssociationService).createNewPrimaryOrganization(profile.getPrimaryOrganization());
        assertNotNull(organization.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePrimaryOrganization_ExistingOrganization() throws ValidationException {
        InvestigatorProfile profile = createTestProfile();
        Organization organization = profile.getPrimaryOrganization().getOrganization();
        organization.setId(1L);
        bean.createPrimaryOrganization(profile);
    }

    private InvestigatorProfile createTestProfile() {
        return InvestigatorProfileFactory.getInstance().create();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUploadedFiles() {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        assertTrue(profile.getUploadedFiles().isEmpty());
        byte[] data = "123".getBytes();
        FirebirdFile file = new FirebirdFile(data, 3, "name", "type", null);
        profile.getUploadedFiles().add(file);
        bean.save(profile);
        flushAndClearSession();
        profile = bean.getById(profile.getId());
        assertEquals(1, profile.getUploadedFiles().size());
        FirebirdFile f2 = profile.getUploadedFiles().iterator().next();
        assertEquals(file.getId(), f2.getId());

        profile.getUploadedFiles().clear();
        bean.save(profile);
        flushAndClearSession();
        profile = bean.getById(profile.getId());
        assertTrue(profile.getUploadedFiles().isEmpty());
        List<FirebirdFile> all = bean.getSessionProvider().get().createCriteria(FirebirdFile.class).list();
        assertEquals(1, all.size());
    }

    @Test
    public void testSaveCertificate_New() throws CredentialAlreadyExistsException, IOException, ValidationException {
        CertificateType type = CertificateType.HUMAN_RESEARCH_CERTIFICATE;
        InvestigatorProfile profile = createTestProfile();
        bean.save(profile);

        TrainingCertificate cert = new TrainingCertificate();
        cert.setCertificateType(type);
        cert.setEffectiveDate(new Date(0L));
        cert.setExpirationDate(new Date(1L));
        cert.setIssuer(OrganizationFactory.getInstance().create());
        File file = File.createTempFile(getClass().getSimpleName(), ".txt");
        FileMetadata fileInfo = new FileMetadata(file.getName(), "text/plain", "some desc");
        bean.saveCertificate(profile, cert, file, fileInfo);
        verify(registrationServiceSpy).setReturnedOrRevisedRegistrationsFormStatusesToRevised(profile, FormTypeEnum.CV);
        verify(registrationServiceSpy, never()).setRegistrationFormStatusesToRevisedIfReviewed(
                anySetOf(AbstractProtocolRegistration.class), eq(FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE));
        flushAndClearSession();

        profile = bean.getById(profile.getId());
        assertEquals(1, profile.getCredentials().size());
        TrainingCertificate added = (TrainingCertificate) profile.getCredentials().iterator().next();
        assertEquals(cert.getId(), added.getId());
        assertEquals(fileInfo.getFilename(), added.getFile().getName());
        assertEquals(fileInfo.getContentType(), added.getFile().getContentType());
        assertEquals(fileInfo.getDescription(), added.getFile().getDescription());

        cert = reloadObject(cert);
        cert.setEffectiveDate(new Date(10L));
        bean.saveCertificate(profile, cert, file, null);
        verify(registrationServiceSpy).setReturnedOrRevisedRegistrationsFormStatusesToRevised(profile, FormTypeEnum.CV);
        verify(registrationServiceSpy).setRegistrationFormStatusesToRevisedIfReviewed(
                anySetOf(AbstractProtocolRegistration.class), eq(FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE));
        flushAndClearSession();

        profile = bean.getById(profile.getId());
        assertEquals(1, profile.getCredentials().size());
        added = (TrainingCertificate) profile.getCredentials().iterator().next();
        assertEquals(cert.getId(), added.getId());
        assertEquals(fileInfo.getFilename(), added.getFile().getName());
        assertEquals(fileInfo.getContentType(), added.getFile().getContentType());
        assertEquals(fileInfo.getDescription(), added.getFile().getDescription());
    }

    @Test
    @SuppressWarnings("unchecked")
    // For Session Provider
    public void testSaveCertificate_Existing() throws IOException, CredentialAlreadyExistsException,
            ValidationException {
        Provider<Session> mockSessionProvider = mock(Provider.class);
        Session mockSession = mock(Session.class);
        when(mockSessionProvider.get()).thenReturn(mockSession);
        bean.setSessionProvider(mockSessionProvider);
        ((ProtocolRegistrationServiceBean) registrationServiceSpy).setSessionProvider(mockSessionProvider);

        InvestigatorProfile profile = createTestProfile();
        TrainingCertificate cert = new TrainingCertificate();
        cert.setId(1L);
        File file = File.createTempFile(getClass().getSimpleName(), ".txt");
        FileMetadata fileInfo = new FileMetadata(file.getName(), "text/plain", "some desc");
        profile.addCredential(cert);

        AbstractProtocolRegistration affectedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration(profile);
        affectedRegistration.setStatus(RegistrationStatus.RETURNED);
        affectedRegistration.getHumanResearchCertificateForm().selectCertificate(cert);
        profile.addRegistration(affectedRegistration);
        AbstractProtocolRegistration unaffectedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration(profile);
        unaffectedRegistration.setStatus(RegistrationStatus.RETURNED);
        profile.addRegistration(unaffectedRegistration);

        assertEquals(1, profile.getCredentials().size());
        bean.saveCertificate(profile, cert, file, fileInfo);
        assertEquals(1, profile.getCredentials().size());
        verify(registrationServiceSpy).setReturnedOrRevisedRegistrationsFormStatusesToRevised(profile, FormTypeEnum.CV);
        verify(registrationServiceSpy).setRegistrationFormStatusesToRevisedIfReviewed(Sets.newHashSet(affectedRegistration),
                FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE);
    }

    @Test
    public void testGetByPerson() {
        Person testPerson = PersonFactory.getInstance().create();
        testPerson.setId(null);
        assertNull(bean.getByPerson(testPerson));

        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        bean.save(profile);
        assertEquals(profile, bean.getByPerson(profile.getPerson()));
    }

    @Test
    public void testGetSpecialtiesByBoard() {
        CertifiedSpecialtyBoard board1 = new CertifiedSpecialtyBoard("BoardA");
        CertifiedSpecialtyBoard board2 = new CertifiedSpecialtyBoard("BoardB");
        CertifiedSpecialtyBoard board3 = new CertifiedSpecialtyBoard("BoardC");
        CertifiedSpecialtyType specialtyA1 = new CertifiedSpecialtyType("Alone Type", board1,
                SpecialtyDesignation.GENERAL);
        CertifiedSpecialtyType specialtyB1 = new CertifiedSpecialtyType("Second Type", board2,
                SpecialtyDesignation.GENERAL);
        CertifiedSpecialtyType specialtyB2 = new CertifiedSpecialtyType("First Type", board2,
                SpecialtyDesignation.SUBSPECIALTY);
        CertifiedSpecialtyType specialtyC1 = new CertifiedSpecialtyType("Sub Third Element", board3,
                SpecialtyDesignation.SUBSPECIALTY);
        CertifiedSpecialtyType specialtyC2 = new CertifiedSpecialtyType("Sub Second Element", board3,
                SpecialtyDesignation.SUBSPECIALTY);
        CertifiedSpecialtyType specialtyC3 = new CertifiedSpecialtyType("General First Element", board3,
                SpecialtyDesignation.GENERAL);

        saveAndFlush(board1, board2, board3, specialtyA1, specialtyB1, specialtyB2, specialtyC1, specialtyC2,
                specialtyC3);

        assertNotNull(bean.getSpecialtiesByBoard(board1));
        assertEquals(1, bean.getSpecialtiesByBoard(board1).size());
        assertEquals(specialtyA1, bean.getSpecialtiesByBoard(board1).get(0));

        assertNotNull(bean.getSpecialtiesByBoard(board2));
        assertEquals(2, bean.getSpecialtiesByBoard(board2).size());
        assertEquals(specialtyB1, bean.getSpecialtiesByBoard(board2).get(0));
        assertEquals(specialtyB2, bean.getSpecialtiesByBoard(board2).get(1));

        assertNotNull(bean.getSpecialtiesByBoard(board3));
        assertEquals(3, bean.getSpecialtiesByBoard(board3).size());
        assertEquals(specialtyC3, bean.getSpecialtiesByBoard(board3).get(0));
        assertEquals(specialtyC2, bean.getSpecialtiesByBoard(board3).get(1));
        assertEquals(specialtyC1, bean.getSpecialtiesByBoard(board3).get(2));
    }

    @Test
    public void testSearch() {
        bean.setPersonService(personServiceSpy);
        Person searchPerson = createSearchPerson();
        List<InvestigatorProfile> testProfiles = createAndSaveTestProfiles(searchPerson);

        List<InvestigatorProfile> retrievedProfiles = bean.search(searchPerson.getLastName() + ", "
                + searchPerson.getFirstName());
        assertEquals(3, retrievedProfiles.size());
        assertSamePersistentObjects(testProfiles.get(2), retrievedProfiles.get(0));
        assertSamePersistentObjects(testProfiles.get(0), retrievedProfiles.get(1));
        assertSamePersistentObjects(testProfiles.get(1), retrievedProfiles.get(2));
    }

    @Test
    public void testSearch_EmptyString() {
        bean.setPersonService(personServiceSpy);
        Person searchPerson = createSearchPerson();
        createAndSaveTestProfiles(searchPerson);

        List<InvestigatorProfile> retrievedProfiles = bean.search("");
        assertTrue(retrievedProfiles.isEmpty());
    }

    @Test
    public void testSearch_NoMatches() {
        bean.setPersonService(personServiceSpy);
        Person searchPerson = createSearchPerson();
        createAndSaveTestProfiles(searchPerson);

        List<InvestigatorProfile> retrievedProfiles = bean.search("sdalfj");
        assertTrue(retrievedProfiles.isEmpty());
    }

    private Person createSearchPerson() {
        Person searchPerson = new Person();
        searchPerson.setFirstName("First");
        searchPerson.setLastName("Last");
        return searchPerson;
    }

    private List<InvestigatorProfile> createAndSaveTestProfiles(Person searchPerson) {
        InvestigatorProfile profile1 = InvestigatorProfileFactory.getInstance().create();
        setNameInProfile(profile1, searchPerson.getFirstName(), searchPerson.getLastName() + "stein");
        InvestigatorProfile profile2 = InvestigatorProfileFactory.getInstance().create();
        setNameInProfile(profile2, searchPerson.getFirstName(), searchPerson.getLastName() + "steinski");
        InvestigatorProfile profile3 = InvestigatorProfileFactory.getInstance().create();
        setNameInProfile(profile3, searchPerson.getFirstName(), searchPerson.getLastName());
        saveAndFlush(profile1, profile2, profile3);
        return Lists.newArrayList(profile1, profile2, profile3);
    }

    private void setNameInProfile(InvestigatorProfile profile, String firstName, String lastName) {
        profile.getPerson().setFirstName(firstName);
        profile.getPerson().setLastName(lastName);
    }

    @Test
    public void testSearchForCtepProfiles() {
        bean.setPersonService(personServiceSpy);
        InvestigatorProfile profile1 = InvestigatorProfileFactory.getInstance().create();
        profile1.setUser(FirebirdUserFactory.getInstance().createInvestigator(profile1));
        profile1.getUser().setCtepUser(true);
        profile1.getPerson().setCtepId("1234");
        setNameInProfile(profile1, "ryan", "stein");

        InvestigatorProfile profile2 = InvestigatorProfileFactory.getInstance().create();
        profile2.setUser(FirebirdUserFactory.getInstance().createInvestigator(profile2));
        profile2.getUser().setCtepUser(false);
        profile2.getPerson().setCtepId("12345");
        setNameInProfile(profile2, "ryan", "steinski");

        InvestigatorProfile profile3 = InvestigatorProfileFactory.getInstance().create();
        profile3.setUser(FirebirdUserFactory.getInstance().createInvestigator(profile3));
        profile3.getUser().setCtepUser(true);
        profile3.getPerson().setCtepId("123456");
        setNameInProfile(profile3, "steve", "steinski");

        InvestigatorProfile profile4 = InvestigatorProfileFactory.getInstance().create();
        profile4.setUser(FirebirdUserFactory.getInstance().createInvestigator(profile4));
        profile4.getUser().setCtepUser(true);
        profile4.getPerson().setCtepId("1234567");
        setNameInProfile(profile4, "ryan", "smith");
        saveAndFlush(profile1, profile2, profile3, profile4);

        assertEquals(2, bean.searchForCtepProfiles("stei").size());
        assertEquals(1, bean.searchForCtepProfiles(profile1.getPerson().getEmail()).size());
        assertTrue(bean.searchForCtepProfiles(profile2.getPerson().getEmail()).isEmpty());
        assertEquals(1, bean.searchForCtepProfiles("1234").size());
    }

    @Test(expected = InvalidStateException.class)
    public void testAddAssociatedPracticeSite_PracticeSite_Type_Required() throws Exception {
        InvestigatorProfile profile = createTestProfile();
        Organization organization = OrganizationFactory.getInstance().create();
        assertNull(organization.getId());
        bean.addAssociatedPracticeSite(profile, organization, "12345", null);
    }

    @Test
    public void testAddAssociatedPracticeSite_OHRP_Not_Required() throws Exception {
        InvestigatorProfile profile = createTestProfile();
        Organization organization = OrganizationFactory.getInstance().create();
        assertNull(organization.getId());
        bean.addAssociatedPracticeSite(profile, organization, null, PracticeSiteType.CANCER_CENTER);
    }

    @Test
    public void testAddAssociatedPracticeSite() throws Exception {
        InvestigatorProfile profile = createTestProfile();
        Organization organization = OrganizationFactory.getInstance().create();
        assertNull(organization.getId());
        bean.addAssociatedPracticeSite(profile, organization, "12345", PracticeSiteType.CANCER_CENTER);
    }

    @Test
    public void testAddAssociatedInstitutionalReviewBoard() throws Exception {
        InvestigatorProfile profile = createTestProfile();
        Organization organization = OrganizationFactory.getInstance().create();
        assertNull(organization.getId());
        bean.addAssociatedInstitutionalReviewBoard(profile, organization);
    }

    @Test
    public void testAddAssociatedClinicalLab() throws Exception {
        InvestigatorProfile profile = createTestProfile();
        Organization organization = OrganizationFactory.getInstance().create();
        assertNull(organization.getId());
        bean.addAssociatedClinicalLab(profile, organization);
    }

}