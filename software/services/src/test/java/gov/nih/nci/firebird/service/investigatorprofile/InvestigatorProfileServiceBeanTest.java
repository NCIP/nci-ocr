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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Provider;
import gov.nih.nci.firebird.data.*;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonAssociationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.test.*;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.*;

public class InvestigatorProfileServiceBeanTest {

    private InvestigatorProfileServiceBean bean = new InvestigatorProfileServiceBean();
    private InvestigatorProfileHelper investigatorProfileHelper = new InvestigatorProfileHelper();
    private InvestigatorRegistrationHelper investigatorRegistrationHelper = new InvestigatorRegistrationHelper();

    @Mock
    private OrganizationService mockOrganizationService;
    @Mock
    private PersonService mockPersonService;
    @Mock
    private ProtocolRegistrationService mockRegistrationService;
    @Mock
    private PersonAssociationService mockPersonAssociationService;
    @Mock
    private OrganizationAssociationService mockOrganizationAssociationService;
    @Mock
    private Provider<Session> mockSessionProvider;
    @Mock
    private Session mockSession;
    @Mock
    private FileService mockFileService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        //set the helpers
        investigatorProfileHelper.setPersonService(mockPersonService);
        investigatorRegistrationHelper.setRegistrationService(mockRegistrationService);
        bean.setInvestigatorProfileHelper(investigatorProfileHelper);
        bean.setInvestigatorRegistrationHelper(investigatorRegistrationHelper);

        bean.setOrganizationService(mockOrganizationService);
        bean.setRegistrationService(mockRegistrationService);
        bean.setPersonAssociationService(mockPersonAssociationService);
        bean.setOrganizationAssociationService(mockOrganizationAssociationService);
        bean.setSessionProvider(mockSessionProvider);
        bean.setFileService(mockFileService);
        when(mockSessionProvider.get()).thenReturn(mockSession);
    }

    @Test(expected = NullPointerException.class)
    public void testSetPrimaryOrganization_NullOrganization() throws ValidationException {
        InvestigatorProfile profile = createTestProfile();
        bean.setPrimaryOrganization(profile, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreatePrimaryOrganization_NullOrganization() throws ValidationException {
        InvestigatorProfile profile = createTestProfile();
        profile.setPrimaryOrganization(null);
        bean.createPrimaryOrganization(profile);
    }

    @Test(expected = NullPointerException.class)
    public void testCreatePrimaryOrganization_NullPrimaryOrganizationType() throws ValidationException {
        InvestigatorProfile profile = createTestProfile();
        profile.getPrimaryOrganization().setType(null);
        bean.createPrimaryOrganization(profile);
    }

    private InvestigatorProfile createTestProfile() {
        return InvestigatorProfileFactory.getInstance().create();
    }

    @Test
    public void testAddAssociatedPracticeSite() throws AssociationAlreadyExistsException, ValidationException {
        InvestigatorProfile profile = createTestProfile();
        Organization organization = OrganizationFactory.getInstance().create();
        String dataFieldValue = ValueGenerator.getUniqueString();
        OrganizationAssociation association = bean.addAssociatedPracticeSite(profile, organization, dataFieldValue,
                PracticeSiteType.CANCER_CENTER);
        verify(mockOrganizationAssociationService).handleNew(association);
        assertNotNull(association);

        assertEquals(1, profile.getOrganizationAssociations().size());
        OrganizationAssociation retrievedAssociation = profile
                .getOrganizationAssociations(OrganizationRoleType.PRACTICE_SITE).iterator().next();
        assertEquals(association.getId(), retrievedAssociation.getId());
        assertEquals(dataFieldValue, retrievedAssociation.getOrganizationRole().getDataField());
        assertEquals(PracticeSiteType.CANCER_CENTER,
                ((PracticeSite) retrievedAssociation.getOrganizationRole()).getType());

        Organization newOrganization = OrganizationFactory.getInstance().createWithoutExternalData();
        bean.addAssociatedPracticeSite(profile, newOrganization, "1234", PracticeSiteType.HEALTH_CARE_FACILITY);
        assertEquals(2, profile.getOrganizationAssociations().size());
        verifyZeroInteractions(mockRegistrationService);
    }

    @Test
    public void testAddAssociatedInsitutionalReviewBoard() throws AssociationAlreadyExistsException,
            ValidationException {
        InvestigatorProfile profile = createTestProfile();
        Organization organization = OrganizationFactory.getInstance().create();
        OrganizationAssociation association = bean.addAssociatedInstitutionalReviewBoard(profile, organization);
        verify(mockOrganizationAssociationService).handleNew(association);
        assertNotNull(association);

        assertEquals(1, profile.getOrganizationAssociations().size());
        OrganizationAssociation retrievedAssociation = profile.getOrganizationAssociations(OrganizationRoleType.IRB)
                .iterator().next();
        assertEquals(association.getId(), retrievedAssociation.getId());

        Organization newOrganization = OrganizationFactory.getInstance().createWithoutExternalData();
        bean.addAssociatedInstitutionalReviewBoard(profile, newOrganization);
        assertEquals(2, profile.getOrganizationAssociations().size());
        verifyZeroInteractions(mockRegistrationService);
    }

    @Test
    public void testAddAssociatedClinicalLab() throws AssociationAlreadyExistsException, ValidationException {
        InvestigatorProfile profile = createTestProfile();
        Organization organization = OrganizationFactory.getInstance().create();
        OrganizationAssociation association = bean.addAssociatedClinicalLab(profile, organization);
        verify(mockOrganizationAssociationService).handleNew(association);
        assertNotNull(association);

        assertEquals(1, profile.getOrganizationAssociations().size());
        OrganizationAssociation retrievedAssociation = profile
                .getOrganizationAssociations(OrganizationRoleType.CLINICAL_LABORATORY).iterator().next();
        assertEquals(association.getId(), retrievedAssociation.getId());

        Organization newOrganization = OrganizationFactory.getInstance().createWithoutExternalData();
        bean.addAssociatedClinicalLab(profile, newOrganization);
        assertEquals(2, profile.getOrganizationAssociations().size());
        verifyZeroInteractions(mockRegistrationService);
    }

    @Test
    public void testDeleteAssociatedOrganization_AffectedReturnedRegistration()
            throws AssociationAlreadyExistsException, ValidationException {
        AbstractProtocolRegistration registration = createRegistration(RegistrationStatus.IN_PROGRESS);
        InvestigatorProfile profile = registration.getProfile();
        mockRegistrationService = mock(ProtocolRegistrationService.class);
        investigatorRegistrationHelper.setRegistrationService(mockRegistrationService);
        when(mockRegistrationService.getReturnedOrRevisedRegistrations(profile)).thenReturn(
                Sets.newHashSet(registration));
        when(mockRegistrationService.save(any(AbstractProtocolRegistration.class))).thenReturn(1L);
        performDeleteAssociatedOrganization(registration, profile, OrganizationFactory.getInstance().create());
        verify(mockRegistrationService).setRegistrationFormStatusesToRevisedIfReviewed(Sets.newHashSet(registration),
                FormTypeEnum.FORM_1572);
    }

    private InvestigatorRegistration createRegistration(RegistrationStatus status) {
        InvestigatorProfile profile = createTestProfile();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                profile);
        profile.addRegistration(registration);
        registration.setStatus(status);
        return registration;
    }

    private void performDeleteAssociatedOrganization(AbstractProtocolRegistration registration,
            InvestigatorProfile profile, Organization organization) throws AssociationAlreadyExistsException,
            ValidationException {
        registration.getForm1572().getPracticeSites().add(organization);
        OrganizationAssociation association = profile.addOrganizationAssociation(organization,
                OrganizationRoleType.PRACTICE_SITE);
        assertTrue(registration.getForm1572().getOrganizations().contains(organization));
        bean.deleteAssociatedOrganization(profile, association);
        assertFalse(registration.getForm1572().getOrganizations().contains(organization));
    }

    @Test
    public void testDeleteAssociatedOrganization_UnaffectedReturnedRegistration()
            throws AssociationAlreadyExistsException, ValidationException {
        AbstractProtocolRegistration registration = createRegistration(RegistrationStatus.IN_PROGRESS);
        InvestigatorProfile profile = registration.getProfile();
        mockRegistrationService = mock(ProtocolRegistrationService.class);
        bean.setRegistrationService(mockRegistrationService);
        Set<AbstractProtocolRegistration> emptySet = Sets.newHashSet();
        when(mockRegistrationService.getReturnedOrRevisedRegistrations(profile)).thenReturn(emptySet);
        when(mockRegistrationService.save(any(AbstractProtocolRegistration.class))).thenReturn(1L);
        performDeleteAssociatedOrganization(registration, profile, OrganizationFactory.getInstance().create());
        verify(mockRegistrationService, never()).setRegistrationFormStatusesToRevisedIfReviewed(
                anySetOf(AbstractProtocolRegistration.class));
    }

    @Test
    public void testDeleteAssociatedOrganization_LockedRegistration() throws AssociationAlreadyExistsException,
            ValidationException {
        AbstractProtocolRegistration registration = createRegistration(RegistrationStatus.IN_PROGRESS);
        InvestigatorProfile profile = registration.getProfile();
        AbstractProtocolRegistration lockedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration(profile);
        lockedRegistration.setStatus(RegistrationStatus.SUBMITTED);
        profile.addRegistration(lockedRegistration);
        mockRegistrationService = mock(ProtocolRegistrationService.class);
        bean.setRegistrationService(mockRegistrationService);
        Set<AbstractProtocolRegistration> emptySet = Sets.newHashSet();
        when(mockRegistrationService.getReturnedOrRevisedRegistrations(profile)).thenReturn(emptySet);
        when(mockRegistrationService.save(any(AbstractProtocolRegistration.class))).thenReturn(1L);
        Organization organization = OrganizationFactory.getInstance().create();
        lockedRegistration.getForm1572().getPracticeSites().add(organization);
        performDeleteAssociatedOrganization(registration, profile, organization);
        verify(mockRegistrationService, never()).setRegistrationFormStatusesToRevisedIfReviewed(
                anySetOf(AbstractProtocolRegistration.class));
        assertTrue(lockedRegistration.getForm1572().getOrganizations().contains(organization));
    }

    @Test
    public void testAddSubInvestigator() throws AssociationAlreadyExistsException, ValidationException {
        InvestigatorProfile profile = createTestProfile();
        Person person = PersonFactory.getInstance().create();
        PersonService mockOS = mock(PersonService.class);
        investigatorProfileHelper.setPersonService(mockOS);

        SubInvestigator subInvestigator = bean.addSubInvestigator(profile, person);
        assertNotNull(subInvestigator);
        assertEquals(1, profile.getSubInvestigators().size());
        assertEquals(subInvestigator.getId(), profile.getSubInvestigators().iterator().next().getId());
    }

    @Test
    public void testAddCredential() throws CredentialAlreadyExistsException, ValidationException {
        DegreeType dt = new DegreeType("foo");
        InvestigatorProfile profile = createTestProfile();

        Degree degree = new Degree();
        degree.setDegreeType(dt);
        degree.setIssuer(OrganizationFactory.getInstance().create());
        degree.setEffectiveDate(new Date());
        bean.saveCredential(profile, degree, getFormTypes());
        verify(mockRegistrationService).setReturnedOrRevisedRegistrationsFormStatusesToRevised(profile, getFormTypes());

        assertEquals(1, profile.getCredentials().size());
        assertEquals(degree.getId(), profile.getCredentials().iterator().next().getId());
    }

    private FormTypeEnum[] getFormTypes() {
        FormTypeEnum[] formTypes = { FormTypeEnum.CV, FormTypeEnum.FORM_1572 };
        return formTypes;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCertificateFail() throws CredentialAlreadyExistsException, IOException, ValidationException {
        CertificateType type = CertificateType.HUMAN_RESEARCH_CERTIFICATE;
        InvestigatorProfile profile = createTestProfile();

        TrainingCertificate degree = new TrainingCertificate();
        degree.setCertificateType(type);
        degree.setEffectiveDate(new Date(0L));
        degree.setExpirationDate(new Date(1L));
        degree.setIssuer(OrganizationFactory.getInstance().create());
        bean.saveCertificate(profile, degree, null, null);
    }

    @Test
    public void testAddCredentialNewIssuer() throws CredentialAlreadyExistsException, ValidationException {
        DegreeType dt = new DegreeType("foo");
        Organization issuer = OrganizationFactory.getInstance().createWithoutExternalData();
        InvestigatorProfile profile = createTestProfile();

        Degree degree = new Degree();
        degree.setDegreeType(dt);
        degree.setIssuer(issuer);
        degree.setEffectiveDate(new Date());
        bean.saveCredential(profile, degree, getFormTypes());
        verify(mockRegistrationService).setReturnedOrRevisedRegistrationsFormStatusesToRevised(profile, getFormTypes());

        assertEquals(1, profile.getCredentials().size());
        assertEquals(degree.getId(), profile.getCredentials().iterator().next().getId());
        verify(mockOrganizationService).create(issuer, OrganizationRoleType.GENERIC_ORGANIZATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByPerson_IllegalArgument() {
        bean.getByPerson(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSpecialtiesByBoard_InvalidBoard() {
        bean.getSpecialtiesByBoard(null);
    }

    @Test
    public void testUpdateAssociationOhrp() throws AssociationAlreadyExistsException {
        String newOhrp = "54321";
        OrganizationAssociation association = getTestOrganizationAssociation(OrganizationRoleType.PRACTICE_SITE);
        bean.updateAssociationOhrp(association, newOhrp);
        assertEquals(association.getOrganizationRole().getDataField(), newOhrp);
    }

    private OrganizationAssociation getTestOrganizationAssociation(OrganizationRoleType roleType)
            throws AssociationAlreadyExistsException {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        OrganizationAssociation association = profile.addOrganizationAssociation(OrganizationFactory.getInstance()
                .create(), roleType);
        association.getOrganizationRole().setDataField("12345");
        return association;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAssociationOhrp_NullAssociation() {
        bean.updateAssociationOhrp(null, "12345");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAssociationOhrp_InvalidAssociation() throws AssociationAlreadyExistsException {
        OrganizationAssociation association = getTestOrganizationAssociation(OrganizationRoleType.CLINICAL_LABORATORY);
        bean.updateAssociationOhrp(association, "12345");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAssociationOhrp_InvalidOhrp() throws AssociationAlreadyExistsException {
        String newOhrp = "";
        OrganizationAssociation association = getTestOrganizationAssociation(OrganizationRoleType.PRACTICE_SITE);
        bean.updateAssociationOhrp(association, newOhrp);
        assertEquals(association.getOrganizationRole().getDataField(), newOhrp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAssociationOhrp_NullOhrp() throws AssociationAlreadyExistsException {
        String newOhrp = null;
        OrganizationAssociation association = getTestOrganizationAssociation(OrganizationRoleType.PRACTICE_SITE);
        bean.updateAssociationOhrp(association, newOhrp);
        assertEquals(association.getOrganizationRole().getDataField(), newOhrp);
    }

    @Test
    public void testSave() {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        bean.save(profile, getFormTypes());
        verify(mockRegistrationService).setReturnedOrRevisedRegistrationsFormStatusesToRevised(profile, getFormTypes());
    }

    @Test
    public void testDeleteCertificate_AffectedRegistration() throws CredentialAlreadyExistsException {
        performDeleteCertificate(true);
    }

    private void performDeleteCertificate(boolean returnedRegistrations) throws CredentialAlreadyExistsException {
        InvestigatorProfile profile = createTestProfile();
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate(
                CertificateType.HUMAN_RESEARCH_CERTIFICATE);
        profile.addCredential(certificate);
        AbstractProtocolRegistration affectedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration(profile);
        affectedRegistration.getHumanResearchCertificateForm().selectCertificate(certificate);
        profile.addRegistration(affectedRegistration);
        AbstractProtocolRegistration unaffectedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration(profile);
        profile.addRegistration(unaffectedRegistration);
        if (returnedRegistrations) {
            affectedRegistration.setStatus(RegistrationStatus.RETURNED);
            unaffectedRegistration.setStatus(RegistrationStatus.RETURNED);
            when(mockRegistrationService.getReturnedOrRevisedRegistrations(profile)).thenReturn(
                    Sets.newHashSet(affectedRegistration));
        }
        assertEquals(1, profile.getCredentials().size());
        bean.deleteCertificate(profile, certificate);
        verify(mockRegistrationService)
                .setReturnedOrRevisedRegistrationsFormStatusesToRevised(profile, FormTypeEnum.CV);
        Set<AbstractProtocolRegistration> affectedRegistrations = Collections.emptySet();
        if (returnedRegistrations) {
            affectedRegistrations = Sets.newHashSet(affectedRegistration);
        }
        verify(mockRegistrationService).setRegistrationFormStatusesToRevisedIfReviewed(affectedRegistrations,
                FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE);
        assertTrue(profile.getCredentials().isEmpty());
    }

    @Test
    public void testDeleteCertificate_UnaffectedRegistration() throws CredentialAlreadyExistsException {
        performDeleteCertificate(false);
    }

    @Test
    public void testDeleteCertificate_WasSubmitted() throws CredentialAlreadyExistsException {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate();
        certificate.setId(1L);
        SubmittedTrainingCertificate immutableCert = new SubmittedTrainingCertificate(certificate);
        certificate.getSubmittedCertificates().add(immutableCert);
        profile.addCredential(certificate);
        assertEquals(1, profile.getCredentials().size());
        assertEquals(certificate, immutableCert.getOriginalCertificate());

        bean.deleteCertificate(profile, certificate);
        assertTrue(profile.getCredentials().isEmpty());
        assertTrue(certificate.getSubmittedCertificates().isEmpty());
        assertNull(immutableCert.getOriginalCertificate());
        verify(mockSession).delete(certificate.getFile());
    }

    @Test
    public void testDeleteCertificate_CertificateFileReferenceExists() throws CredentialAlreadyExistsException {
        InvestigatorRegistration registration = createRegistration(RegistrationStatus.APPROVED);
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate();
        registration.getHumanResearchCertificateForm().selectCertificate(certificate);
        InvestigatorProfile profile = registration.getProfile();
        registration.createRevisedRegistration();
        bean.deleteCertificate(profile, certificate);
        assertTrue(registration.getHumanResearchCertificateForm().getCertificates().isEmpty());
        assertTrue(profile.getCredentials().isEmpty());
        assertTrue(certificate.getSubmittedCertificates().isEmpty());
        verify(mockSession, never()).delete(certificate.getFile());
    }

    @Test
    public void testDeleteCertificate_InRegistration() throws CredentialAlreadyExistsException {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration(
                profile);
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate();
        certificate.setId(1L);
        registration.getHumanResearchCertificateForm().selectCertificate(certificate);
        profile.addRegistration(registration);
        profile.addCredential(certificate);

        bean.deleteCertificate(profile, certificate);
        assertTrue(registration.getHumanResearchCertificateForm().getCertificates().isEmpty());
    }

    @Test
    public void testDeleteAssociatedSubInvestigator_InProgressRegistration() throws AssociationAlreadyExistsException,
            ValidationException {
        performDeleteAssociatedSubInvestigator(RegistrationStatus.IN_PROGRESS, InvitationStatus.NO_RESPONSE);
    }

    private SubInvestigatorRegistration performDeleteAssociatedSubInvestigator(RegistrationStatus status,
            InvitationStatus invitationStatus) throws AssociationAlreadyExistsException, ValidationException {
        InvestigatorRegistration registration = createRegistration(status);
        InvestigatorProfile profile = registration.getProfile();
        InvestigatorProfile subInvestigatorProfile = InvestigatorProfileFactory.getInstance().create();
        SubInvestigator subInvestigator = registration.getProfile().addSubInvestigator(
                subInvestigatorProfile.getPerson());
        SubInvestigatorRegistration subInvestigatorRegistration = createSubinvestigatorRegistration(registration,
                subInvestigatorProfile);
        subInvestigatorRegistration.getInvitation().setInvitationStatus(invitationStatus);
        ProtocolRegistrationService mockRegistrationService = mock(ProtocolRegistrationService.class);
        when(mockRegistrationService.getSubinvestigatorRegistrations(profile, subInvestigatorProfile.getPerson()))
                .thenReturn(Lists.newArrayList(subInvestigatorRegistration));
        investigatorRegistrationHelper.setRegistrationService(mockRegistrationService);

        bean.deleteAssociatedSubInvestigator(profile, subInvestigator);
        verify(mockPersonAssociationService).delete(subInvestigator);
        if (!registration.isLockedForInvestigator()
                && !InvestigatorRegistrationHelper.INVALID_SUBINVESTIGATOR_REMOVAL_STATES.contains(status)) {
            verify(mockRegistrationService).removeSubInvestigatorRegistrationAndNotify(
                    anyListOf(SubInvestigatorRegistration.class));
        } else {
            verify(mockRegistrationService, never()).removeSubInvestigatorRegistrationAndNotify(
                    anyListOf(SubInvestigatorRegistration.class));
        }
        return subInvestigatorRegistration;
    }

    private SubInvestigatorRegistration createSubinvestigatorRegistration(InvestigatorRegistration registration,
            InvestigatorProfile subInvestigatorProfile) {
        SubInvestigatorRegistration subinvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(registration, subInvestigatorProfile);
        return subinvestigatorRegistration;
    }

    @Test
    public void testDeleteAssociatedSubInvestigator_ReturnedRegistration() throws AssociationAlreadyExistsException,
            ValidationException {
        performDeleteAssociatedSubInvestigator(RegistrationStatus.RETURNED, InvitationStatus.NO_RESPONSE);
    }

    @Test
    public void testDeleteAssociatedSubInvestigator_SubmittedRegistration() throws AssociationAlreadyExistsException,
            ValidationException {
        performDeleteAssociatedSubInvestigator(RegistrationStatus.SUBMITTED, InvitationStatus.NO_RESPONSE);
    }

    @Test
    public void testDeleteAssociatedSubInvestigator_NotInvitedRegistration() throws AssociationAlreadyExistsException,
            ValidationException {
        performDeleteAssociatedSubInvestigator(RegistrationStatus.IN_PROGRESS, InvitationStatus.NOT_INVITED);
    }

    @Test
    public void testDeleteAssociatedShippingDesignee() throws AssociationAlreadyExistsException, ValidationException {
        InvestigatorProfile investigatorProfile = InvestigatorProfileFactory.getInstance().create();
        ShippingDesignee shippingDesignee = PersonAssociationFactory.getInstance().createShippingDesignee();
        investigatorProfile.setShippingDesignee(shippingDesignee);
        bean.deleteAssociatedShippingDesignee(investigatorProfile, shippingDesignee);
        verify(mockPersonAssociationService).delete(shippingDesignee);
    }

    @Test
    public void testAddOrderingDesignee() throws Exception {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        Person person = PersonFactory.getInstance().create();
        bean.addOrderingDesignee(profile, person);
        ArgumentCaptor<OrderingDesignee> orderingDesigneeCaptor = ArgumentCaptor.forClass(OrderingDesignee.class);
        verify(mockPersonAssociationService).handleNew(orderingDesigneeCaptor.capture());
        assertTrue(profile.getOrderingDesignees().contains(orderingDesigneeCaptor.getValue()));
    }

    @Test
    public void testDeleteAssociatedOrderingDesignee() throws Exception {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        Person person = PersonFactory.getInstance().create();
        OrderingDesignee orderingDesignee = profile.addOrderingDesignee(person);

        bean.deleteAssociatedOrderingDesignee(profile, orderingDesignee);
        verify(mockPersonAssociationService).delete(orderingDesignee);
        assertTrue(profile.getOrderingDesignees().isEmpty());

    }

    @Test
    public void testSetShippingDesignee_NoOrganizationExternalId() throws Exception {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        ShippingDesignee shippingDesignee = PersonAssociationFactory.getInstance().createShippingDesignee();
        Organization organization = shippingDesignee.getOrganization();
        organization.setExternalData(null);
        bean.setShippingDesignee(profile, shippingDesignee);
        assertEquals(profile, shippingDesignee.getProfile());
        assertEquals(shippingDesignee, profile.getShippingDesignee());
        verify(mockPersonAssociationService).handleNew(shippingDesignee);
        verify(mockOrganizationService).create(organization, OrganizationRoleType.GENERIC_ORGANIZATION);
    }

    @Test
    public void testSetShippingDesignee_WithOrganizationExternalId() throws Exception {
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        ShippingDesignee shippingDesignee = PersonAssociationFactory.getInstance().createShippingDesignee();
        bean.setShippingDesignee(profile, shippingDesignee);
        assertEquals(profile, shippingDesignee.getProfile());
        assertEquals(shippingDesignee, profile.getShippingDesignee());
        verify(mockPersonAssociationService).handleNew(shippingDesignee);
        verifyZeroInteractions(mockOrganizationService);
    }

}