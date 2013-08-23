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

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import gov.nih.nci.cagrid.gridgrouper.stubs.service.GridGrouperService;
import gov.nih.nci.coppa.services.business.business.common.BusinessI;
import gov.nih.nci.coppa.services.entities.organization.common.OrganizationI;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.coppa.services.structuralroles.healthcarefacility.common.HealthCareFacilityI;
import gov.nih.nci.coppa.services.structuralroles.healthcareprovider.common.HealthCareProviderI;
import gov.nih.nci.coppa.services.structuralroles.identifiedorganization.common.IdentifiedOrganizationI;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.coppa.services.structuralroles.organizationalcontact.common.OrganizationalContactI;
import gov.nih.nci.coppa.services.structuralroles.oversightcommittee.common.OversightCommitteeI;
import gov.nih.nci.coppa.services.structuralroles.researchorganization.common.ResearchOrganizationI;
import gov.nih.nci.ctep.ces.ocr.api.CesInvestigatorService;
import gov.nih.nci.firebird.cagrid.GridAuthenticationService;
import gov.nih.nci.firebird.cagrid.GridCredentialService;
import gov.nih.nci.firebird.nes.correlation.NesPersonRoleIntegrationService;
import gov.nih.nci.firebird.nes.correlation.NesPersonRoleIntegrationServiceBean;
import gov.nih.nci.firebird.nes.organization.HealthCareFacilityIntegrationService;
import gov.nih.nci.firebird.nes.organization.HealthCareFacilityIntegrationServiceBean;
import gov.nih.nci.firebird.nes.organization.IdentifiedOrganizationIntegrationService;
import gov.nih.nci.firebird.nes.organization.IdentifiedOrganizationIntegrationServiceBean;
import gov.nih.nci.firebird.nes.organization.NesOrganizationIntegrationServiceFactory;
import gov.nih.nci.firebird.nes.organization.NesOrganizationServiceBean;
import gov.nih.nci.firebird.nes.organization.OrganizationEntityIntegrationService;
import gov.nih.nci.firebird.nes.organization.OversightCommitteeIntegrationService;
import gov.nih.nci.firebird.nes.organization.OversightCommitteeIntegrationServiceBean;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationIntegrationService;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationIntegrationServiceBean;
import gov.nih.nci.firebird.nes.person.NesPersonServiceBean;
import gov.nih.nci.firebird.service.LocalServiceModule;
import gov.nih.nci.firebird.service.account.AccountManagementService;
import gov.nih.nci.firebird.service.account.AccountManagementServiceBean;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationConfigurationService;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationConfigurationServiceBean;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationService;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationServiceBean;
import gov.nih.nci.firebird.service.annual.registration.InvestigatorDisqualificationService;
import gov.nih.nci.firebird.service.annual.registration.InvestigatorDisqualificationServiceBean;
import gov.nih.nci.firebird.service.annual.registration.RegistrationWithdrawalService;
import gov.nih.nci.firebird.service.annual.registration.RegistrationWithdrawalServiceBean;
import gov.nih.nci.firebird.service.annual.registration.review.AnnualRegistrationReviewService;
import gov.nih.nci.firebird.service.annual.registration.review.AnnualRegistrationReviewServiceBean;
import gov.nih.nci.firebird.service.ctep.esys.EsysIntegrationService;
import gov.nih.nci.firebird.service.ctep.esys.EsysIntegrationServiceBean;
import gov.nih.nci.firebird.service.curation.CurationService;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.file.FileServiceBean;
import gov.nih.nci.firebird.service.investigator.InvestigatorService;
import gov.nih.nci.firebird.service.investigator.InvestigatorServiceBean;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileServiceBean;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.CountryLookupServiceBean;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.messages.TemplateServiceImpl;
import gov.nih.nci.firebird.service.messages.email.EmailService;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationServiceBean;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.organization.OrganizationServiceBean;
import gov.nih.nci.firebird.service.organization.external.ExternalOrganizationService;
import gov.nih.nci.firebird.service.organization.local.LocalOrganizationDataService;
import gov.nih.nci.firebird.service.organization.local.LocalOrganizationDataServiceBean;
import gov.nih.nci.firebird.service.pdf.PdfService;
import gov.nih.nci.firebird.service.pdf.PdfServiceBean;
import gov.nih.nci.firebird.service.person.PersonAssociationService;
import gov.nih.nci.firebird.service.person.PersonAssociationServiceBean;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.person.PersonServiceBean;
import gov.nih.nci.firebird.service.person.external.ExternalPersonService;
import gov.nih.nci.firebird.service.person.local.LocalPersonDataService;
import gov.nih.nci.firebird.service.person.local.LocalPersonDataServiceBean;
import gov.nih.nci.firebird.service.properties.DynamicPropertiesService;
import gov.nih.nci.firebird.service.properties.DynamicPropertiesServiceBean;
import gov.nih.nci.firebird.service.protocol.FormTypeService;
import gov.nih.nci.firebird.service.protocol.FormTypeServiceBean;
import gov.nih.nci.firebird.service.protocol.ProtocolAgentService;
import gov.nih.nci.firebird.service.protocol.ProtocolAgentServiceBean;
import gov.nih.nci.firebird.service.protocol.ProtocolImportDetailService;
import gov.nih.nci.firebird.service.protocol.ProtocolImportDetailServiceBean;
import gov.nih.nci.firebird.service.protocol.ProtocolImportService;
import gov.nih.nci.firebird.service.protocol.ProtocolImportServiceBean;
import gov.nih.nci.firebird.service.protocol.ProtocolService;
import gov.nih.nci.firebird.service.protocol.ProtocolServiceBean;
import gov.nih.nci.firebird.service.protocol.ProtocolValidationService;
import gov.nih.nci.firebird.service.protocol.ProtocolValidationServiceBean;
import gov.nih.nci.firebird.service.protocol.registration.review.ProtocolRegistrationReviewService;
import gov.nih.nci.firebird.service.protocol.registration.review.ProtocolRegistrationReviewServiceBean;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationServiceBean;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.sponsor.SponsorServiceBean;
import gov.nih.nci.firebird.service.task.TaskService;
import gov.nih.nci.firebird.service.task.TaskServiceBean;
import gov.nih.nci.firebird.service.user.CertificateAuthorityManager;
import gov.nih.nci.firebird.service.user.CertificateAuthorityManagerBean;
import gov.nih.nci.firebird.service.user.FirebirdUserService;
import gov.nih.nci.firebird.service.user.FirebirdUserServiceBean;
import org.cagrid.gaards.dorian.client.GridUserClient;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Provides service bindings for tests; all services are wrapped in Mockito spy objects so that method call verification
 * and stubbing (if necessary) can be done.
 */
public class FirebirdServicesAsSpysTestModule extends LocalServiceModule {

    private EmailService mockEmailService = mock(EmailService.class);

    @Override
    protected void configure() {
        // no-op, all bindings specified in provides methods
    }

    @Provides
    EmailService provideEmailService() {
        return mockEmailService;
    }

    @Provides
    @Named("emailService")
    EmailService provideEmailServiceNamed() {
        return mockEmailService;
    }

    @Provides
    @Named("jmsEmailService")
    EmailService provideJmsEmailServiceNamed() {
        return mockEmailService;
    }

    @Provides
    TemplateService provideTemplateService(TemplateServiceImpl impl) {
        return spy(impl);
    }

    @Provides
    AccountManagementService provideAccountManagementService(AccountManagementServiceBean impl) {
        return spy(impl);
    }

    @Provides
    FileService provideFileService(FileServiceBean bean) {
        return spy(bean);
    }

    @Provides
    FirebirdUserService provideFirebirdUserService(FirebirdUserServiceBean bean) {
        return spy(bean);
    }

    @Provides
    FormTypeService provideFormTypeService(FormTypeServiceBean bean) {
        return spy(bean);
    }

    @Provides
    ProtocolImportService provideProtocolImportService(ProtocolImportServiceBean bean) {
        return spy(bean);
    }

    @Provides
    ProtocolImportDetailService provideProtocolImportDetailService(ProtocolImportDetailServiceBean bean) {
        return spy(bean);
    }

    @Provides
    ProtocolService provideProtocolService(ProtocolServiceBean bean) {
        return spy(bean);
    }

    @Provides
    ProtocolAgentService provideProtocolAgentService(ProtocolAgentServiceBean bean) {
        return spy(bean);
    }

    @Provides
    ProtocolValidationService provideProtocolValidationService(ProtocolValidationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    ProtocolRegistrationService provideProtocolRegistrationService(ProtocolRegistrationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    PdfService providePdfService(PdfServiceBean bean) {
        return spy(bean);
    }

    @Provides
    InvestigatorProfileService provideInvestigatorProfileService(InvestigatorProfileServiceBean bean) {
        return spy(bean);
    }

    @Provides
    @Singleton
    public OrganizationService provideOrganizationService(OrganizationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    @Singleton
    public ExternalOrganizationService provideNesOrganizationService(NesOrganizationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    @Singleton
    public LocalOrganizationDataService provideLocalOrganizationDataService(LocalOrganizationDataServiceBean bean) {
        return spy(bean);
    }

    @Provides
    @Singleton
    public CurationService provideCurationService() {
        return mock(CurationService.class);
    }

    @Provides
    public TaskService provideTaskService(TaskServiceBean bean) {
        return spy(bean);
    }

    @Provides
    NesPersonRoleIntegrationService provideNesPersonRoleIntegrationService(NesPersonRoleIntegrationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    OrganizationAssociationService provideOrganizationAssociationService(OrganizationAssociationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    PersonAssociationService providePersonAssociationService(PersonAssociationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    CountryLookupService provideCountryLookupService(CountryLookupServiceBean bean) {
        return spy(bean);
    }

    @Provides
    CertificateAuthorityManager provideCertificateAutorityManagerLocal(CertificateAuthorityManagerBean bean) {
        return spy(bean);
    }

    @Provides
    SponsorService provideSponsorService(SponsorServiceBean bean) {
        return spy(bean);
    }

    @Provides
    AnnualRegistrationService provideAnnualRegistrationService(AnnualRegistrationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    AnnualRegistrationReviewService provideAnnualRegistrationReviewService(AnnualRegistrationReviewServiceBean bean) {
        return spy(bean);
    }

    @Provides
    ProtocolRegistrationReviewService provideProtocolRegistrationReviewService(
            ProtocolRegistrationReviewServiceBean bean) {
        return spy(bean);
    }

    @Provides
    AnnualRegistrationConfigurationService provideAnnualRegistrationConfigurationService(
            AnnualRegistrationConfigurationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    RegistrationWithdrawalService provideRegistrationWithdrawalService(RegistrationWithdrawalServiceBean bean) {
        return spy(bean);
    }

    @Provides
    InvestigatorDisqualificationService provideInvestigatorDisqualificationService(
            InvestigatorDisqualificationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    PersonI providePersonI() {
        return mock(PersonI.class);
    }

    @Provides
    OrganizationI provideOrganizationI() {
        return mock(OrganizationI.class);
    }

    @Provides
    HealthCareFacilityI provideHealthCareFacilityI() {
        return mock(HealthCareFacilityI.class);
    }

    @Provides
    OversightCommitteeI provideOversightCommitteeI() {
        return mock(OversightCommitteeI.class);
    }

    @Provides
    ResearchOrganizationI provideResearchOrganizationI() {
        return mock(ResearchOrganizationI.class);
    }

    @Provides
    HealthCareProviderI provideHealthCareProviderI() {
        return mock(HealthCareProviderI.class);
    }

    @Provides
    OrganizationalContactI provideOrganizationalContactI() {
        return mock(OrganizationalContactI.class);
    }

    @Provides
    IdentifiedOrganizationI provideIdentifiedOrganizationI() {
        return mock(IdentifiedOrganizationI.class);
    }

    @Provides
    IdentifiedPersonI provideIdentifiedPersonI() {
        return mock(IdentifiedPersonI.class);
    }

    @Provides
    BusinessI provideBusinessI() {
        return mock(BusinessI.class);
    }

    @Provides
    GridUserClient provideGridUserClient() {
        return mock(GridUserClient.class);
    }

    @Provides
    GridAuthenticationService provideGridAuthenticationService() {
        return mock(GridAuthenticationService.class);
    }

    @Provides
    GridCredentialService provideGridCredentialService() {
        return mock(GridCredentialService.class);
    }

    @Provides
    GridGrouperService provideGridGrouperService() {
        return mock(GridGrouperService.class);
    }

    @Provides
    gov.nih.nci.firebird.cagrid.GridGrouperService provideFirebirdGridGrouperService() {
        return mock(gov.nih.nci.firebird.cagrid.GridGrouperService.class);
    }

    @Provides
    EsysIntegrationService provideEsysIntegrationService(EsysIntegrationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    DynamicPropertiesService provideDynamicPropertiesService(DynamicPropertiesServiceBean bean) {
        return spy(bean);
    }

    @Provides
    InvestigatorService provideInvestigatorService(InvestigatorServiceBean bean) {
        return spy(bean);
    }

    @Provides
    CesInvestigatorService provideCesInvestigatorService() {
        return mock(CesInvestigatorService.class);
    }

    @Provides
    OrganizationEntityIntegrationService provideOrganizationEntityIntegrationService() {
        return mock(OrganizationEntityIntegrationService.class);
    }

    @Provides
    NesOrganizationIntegrationServiceFactory provideNesOrganizationIntegrationServiceFactory() {
        return mock(NesOrganizationIntegrationServiceFactory.class);
    }

    @Provides
    LocalPersonDataService provideLocalPersonDataService(LocalPersonDataServiceBean bean) {
        return spy(bean);
    }

    @Provides
    @Singleton
    public ExternalPersonService provideExternalPersonService(NesPersonServiceBean bean) {
        return spy(bean);
    }

    @Provides
    public PersonService providePersonService(PersonServiceBean bean) {
        return spy(bean);
    }

    @Provides
    public HealthCareFacilityIntegrationService provideHealthCareFacilityIntegrationService(
            HealthCareFacilityIntegrationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    public IdentifiedOrganizationIntegrationService provideIdentifiedOrganizationIntegrationService(
            IdentifiedOrganizationIntegrationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    public OversightCommitteeIntegrationService provideOversightCommitteeIntegrationService(
            OversightCommitteeIntegrationServiceBean bean) {
        return spy(bean);
    }

    @Provides
    public ResearchOrganizationIntegrationService provideResearchOrganizationIntegrationService(
            ResearchOrganizationIntegrationServiceBean bean) {
        return spy(bean);
    }

}
