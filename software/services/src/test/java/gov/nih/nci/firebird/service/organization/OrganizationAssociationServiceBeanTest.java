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
package gov.nih.nci.firebird.service.organization;

import static gov.nih.nci.firebird.data.LaboratoryCertificateType.*;
import static gov.nih.nci.firebird.data.OrganizationRoleType.*;
import static gov.nih.nci.firebird.nes.NesIdTestUtil.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.PrimaryOrganizationType;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.organization.HealthCareFacilityData;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.test.GuiceTestRunnerWithMocks;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;

@SuppressWarnings("unchecked")
@RunWith(GuiceTestRunnerWithMocks.class)
public class OrganizationAssociationServiceBeanTest {

    @Inject
    private OrganizationAssociationServiceBean bean;
    @Inject
    private FileService mockFileService;
    @Inject
    private OrganizationService mockOrganizationService;
    @Inject
    private ProtocolRegistrationService mockRegistrationService;
    private Provider<Session> mockSessionProvider = mock(Provider.class);
    private Session mockSession = mock(Session.class);
    private InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
    private Organization organization = OrganizationFactory.getInstance().createWithoutExternalData();

    @Before
    public void setUp() {
        bean.setSessionProvider(mockSessionProvider);
        bean.setRegistrationService(mockRegistrationService);
        bean.setOrganizationService(mockOrganizationService);
        bean.setFileService(mockFileService);
        when(mockSessionProvider.get()).thenReturn(mockSession);
    }

    @Test
    public void testHandleNew_ExistingOrganization() throws AssociationAlreadyExistsException, ValidationException {
        HealthCareFacilityData healthCareFacilityData = new HealthCareFacilityData();
        healthCareFacilityData.setExternalId(TEST_NES_ID_STRING);
        organization.setExternalData(healthCareFacilityData);
        OrganizationAssociation association = createAssociation(CLINICAL_LABORATORY);
        bean.handleNew(association);
        verifyZeroInteractions(mockOrganizationService);
    }

    private OrganizationAssociation createAssociation(OrganizationRoleType type)
            throws AssociationAlreadyExistsException {
        return profile.addOrganizationAssociation(organization, type);
    }

    @Test
    public void testHandleNew_NewPracticeSite_CancerCenter() throws AssociationAlreadyExistsException,
            ValidationException {
        OrganizationAssociation association = createPracticeSiteAssocation(PracticeSiteType.CANCER_CENTER);
        bean.handleNew(association);
        verify(mockOrganizationService).create(organization, PRACTICE_SITE, PracticeSiteType.CANCER_CENTER);
    }

    @Test
    public void testHandleNew_NewPracticeSite_ClinicalCenter() throws AssociationAlreadyExistsException,
            ValidationException {
        OrganizationAssociation association = createPracticeSiteAssocation(PracticeSiteType.CLINICAL_CENTER);
        bean.handleNew(association);
        verify(mockOrganizationService).create(organization, PRACTICE_SITE, PracticeSiteType.CLINICAL_CENTER);
    }

    @Test
    public void testHandleNew_NewPracticeSite_HealthCareFacility() throws AssociationAlreadyExistsException,
            ValidationException {
        OrganizationAssociation association = createPracticeSiteAssocation(PracticeSiteType.HEALTH_CARE_FACILITY);
        bean.handleNew(association);
        verify(mockOrganizationService).create(organization, PRACTICE_SITE, PracticeSiteType.HEALTH_CARE_FACILITY);
    }

    @Test
    public void testHandleNew_ExistingPracticeSite() throws AssociationAlreadyExistsException,
            ValidationException {
        OrganizationAssociation association = createPracticeSiteAssocation(PracticeSiteType.HEALTH_CARE_FACILITY);
        PracticeSite practiceSite = (PracticeSite) association.getOrganizationRole();
        HealthCareFacilityData healthCareFacilityData = new HealthCareFacilityData();
        practiceSite.getOrganization().setExternalData(healthCareFacilityData);
        practiceSite.setType(null);
        when(mockOrganizationService.getPracticeSiteType(practiceSite.getOrganization())).thenReturn(PracticeSiteType.HEALTH_CARE_FACILITY);
        bean.handleNew(association);
        assertEquals(PracticeSiteType.HEALTH_CARE_FACILITY, practiceSite.getType());
    }

    private OrganizationAssociation createPracticeSiteAssocation(PracticeSiteType type)
            throws AssociationAlreadyExistsException {
        OrganizationAssociation association = createAssociation(PRACTICE_SITE);
        PracticeSite site = (PracticeSite) association.getOrganizationRole();
        site.setType(type);
        return association;
    }

    @Test
    public void testHandleCertificate() throws Exception {
        OrganizationAssociation association = createAssociation(CLINICAL_LABORATORY);
        LaboratoryCertificate certificate = new LaboratoryCertificate(CLIA);
        certificate.setEffectiveDate(new Date(System.currentTimeMillis() - 100000));
        certificate.setExpirationDate(new Date(System.currentTimeMillis() + 100000));
        Set<AbstractProtocolRegistration> returnedRegistrations = Sets.newHashSet();
        AbstractProtocolRegistration affectedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        affectedRegistration.getForm1572().getLabs().add(organization);
        returnedRegistrations.add(affectedRegistration);
        AbstractProtocolRegistration unAffectedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        unAffectedRegistration.getForm1572().getIrbs().add(OrganizationFactory.getInstance().create());
        returnedRegistrations.add(unAffectedRegistration);
        when(mockRegistrationService.getReturnedOrRevisedRegistrations(profile)).thenReturn(returnedRegistrations);
        bean.handleCertificate(association, certificate, mock(File.class), new FileMetadata(null, null));
        Set<AbstractProtocolRegistration> affectedRegistrations = Sets.newHashSet();
        affectedRegistrations.add(affectedRegistration);
        verify(mockRegistrationService).setRegistrationFormStatusesToRevisedIfReviewed(affectedRegistrations,
                FormTypeEnum.FORM_1572);
        verify(mockRegistrationService).save(affectedRegistrations);

        assertNotNull(((ClinicalLaboratory) association.getOrganizationRole()).getCertificate(CLIA));
        verify(mockFileService).createFile(any(File.class), any(FileMetadata.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleCertificateFailWrongType() throws AssociationAlreadyExistsException, IOException {
        OrganizationAssociation association = profile.addOrganizationAssociation(organization, PRACTICE_SITE);
        bean.handleCertificate(association, new LaboratoryCertificate(CLIA), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleCertificateFailNull() throws AssociationAlreadyExistsException, IOException {
        OrganizationAssociation association = createAssociation(CLINICAL_LABORATORY);
        bean.handleCertificate(association, null, null, null);
    }

    @Test(expected = IOException.class)
    public void testHandleCertificateFileFail() throws AssociationAlreadyExistsException, IOException {
        when(mockFileService.createFile(any(File.class), any(FileMetadata.class))).thenThrow(new IOException());
        OrganizationAssociation association = createAssociation(CLINICAL_LABORATORY);
        bean.handleCertificate(association, new LaboratoryCertificate(CLIA), mock(File.class), new FileMetadata(null,
                null));
    }

    @Test
    public void testSave_Irb() throws AssociationAlreadyExistsException, IOException {
        saveAndCheckRegistrationsModified(IRB);
    }

    @Test
    public void testSave_Lab() throws AssociationAlreadyExistsException, IOException {
        saveAndCheckRegistrationsModified(CLINICAL_LABORATORY);
    }

    @Test
    public void testSave_PracticeSite() throws AssociationAlreadyExistsException, IOException {
        saveAndCheckRegistrationsModified(PRACTICE_SITE);
    }

    private void saveAndCheckRegistrationsModified(OrganizationRoleType roleType)
            throws AssociationAlreadyExistsException {
        OrganizationAssociation association = profile.addOrganizationAssociation(organization, roleType);
        AbstractProtocolRegistration affectedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        selectAssociationFor1572(roleType, organization, affectedRegistration);
        Set<AbstractProtocolRegistration> returnedRegistrations = Sets.newHashSet();
        returnedRegistrations.add(affectedRegistration);
        AbstractProtocolRegistration unaffectedRegistration = RegistrationFactory.getInstance()
                .createInvestigatorRegistration();
        selectAssociationFor1572(roleType, OrganizationFactory.getInstance().create(), unaffectedRegistration);
        returnedRegistrations.add(unaffectedRegistration);
        when(mockRegistrationService.getReturnedOrRevisedRegistrations(profile)).thenReturn(returnedRegistrations);
        bean.save(association);
        Set<AbstractProtocolRegistration> affectedRegistrations = Sets.newHashSet();
        affectedRegistrations.add(affectedRegistration);
        verify(mockRegistrationService).setRegistrationFormStatusesToRevisedIfReviewed(affectedRegistrations,
                FormTypeEnum.FORM_1572);
        verify(mockRegistrationService).save(affectedRegistrations);
    }

    private void selectAssociationFor1572(OrganizationRoleType roleType, Organization organization,
            AbstractProtocolRegistration registrations) {
        registrations.getForm1572().getAssociatedOrganizations(roleType).add(organization);
    }

    @Test
    public void testCreateNewPrimaryOrganization_CancerCenter() throws Exception {
        PrimaryOrganization primaryOrganization = new PrimaryOrganization(organization,
                PrimaryOrganizationType.CANCER_CENTER);
        bean.createNewPrimaryOrganization(primaryOrganization);
        verify(mockOrganizationService).create(organization, PRIMARY_ORGANIZATION,
                PrimaryOrganizationType.CANCER_CENTER);
    }

    @Test
    public void testCreateNewPrimaryOrganization_ClinicalCenter() throws Exception {
        PrimaryOrganization primaryOrganization = new PrimaryOrganization(organization,
                PrimaryOrganizationType.CLINICAL_CENTER);
        bean.createNewPrimaryOrganization(primaryOrganization);
        verify(mockOrganizationService).create(organization, PRIMARY_ORGANIZATION,
                PrimaryOrganizationType.CLINICAL_CENTER);
    }

    @Test
    public void testCreateNewPrimaryOrganization_HealthCareFacility() throws Exception {
        PrimaryOrganization primaryOrganization = new PrimaryOrganization(organization,
                PrimaryOrganizationType.HEALTH_CARE_FACILITY);
        bean.createNewPrimaryOrganization(primaryOrganization);
        verify(mockOrganizationService).create(organization, PRIMARY_ORGANIZATION,
                PrimaryOrganizationType.HEALTH_CARE_FACILITY);
    }

}
