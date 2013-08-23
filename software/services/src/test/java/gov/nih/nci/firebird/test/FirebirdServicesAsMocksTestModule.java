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

import static org.mockito.Mockito.*;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.firebird.cagrid.GridAuthenticationService;
import gov.nih.nci.firebird.nes.correlation.NesPersonRoleIntegrationService;
import gov.nih.nci.firebird.nes.organization.BaseOrganizationIntegrationService;
import gov.nih.nci.firebird.nes.organization.HealthCareFacilityIntegrationService;
import gov.nih.nci.firebird.nes.organization.NesOrganizationIntegrationServiceFactory;
import gov.nih.nci.firebird.nes.organization.NesOrganizationIntegrationServiceFactoryBean;
import gov.nih.nci.firebird.nes.organization.OrganizationEntityIntegrationService;
import gov.nih.nci.firebird.nes.organization.OversightCommitteeIntegrationService;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationIntegrationService;
import gov.nih.nci.firebird.security.CredentialsHandlerFactory;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.LocalServiceModule;
import gov.nih.nci.firebird.service.account.AccountManagementService;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationConfigurationService;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationService;
import gov.nih.nci.firebird.service.annual.registration.InvestigatorDisqualificationService;
import gov.nih.nci.firebird.service.annual.registration.RegistrationWithdrawalService;
import gov.nih.nci.firebird.service.annual.registration.review.AnnualRegistrationReviewService;
import gov.nih.nci.firebird.service.curation.CurationService;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.investigatorprofile.ProfileRefreshService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.messages.TemplateService;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.organization.external.ExternalOrganizationService;
import gov.nih.nci.firebird.service.organization.local.LocalOrganizationDataService;
import gov.nih.nci.firebird.service.person.PersonAssociationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.person.PersonServiceBean;
import gov.nih.nci.firebird.service.person.local.LocalPersonDataService;
import gov.nih.nci.firebird.service.protocol.FormTypeService;
import gov.nih.nci.firebird.service.protocol.ProtocolAgentService;
import gov.nih.nci.firebird.service.protocol.ProtocolImportService;
import gov.nih.nci.firebird.service.protocol.ProtocolService;
import gov.nih.nci.firebird.service.protocol.registration.review.ProtocolRegistrationReviewService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.task.TaskService;
import gov.nih.nci.firebird.service.user.CertificateAuthorityManager;
import gov.nih.nci.firebird.service.user.FirebirdUserService;

import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Provides service bindings for tests; all services are wrapped in Mockito mock objects so that method call
 * verification and stubbing (if necessary) can be done.
 */
public class FirebirdServicesAsMocksTestModule extends LocalServiceModule {

    @Override
    protected void configure() {
        // no-op, all bindings specified in provides methods
    }

    @Provides
    @Singleton
    AccountManagementService provideAccountManagementService() {
        return mock(AccountManagementService.class);
    }

    @Provides
    @Singleton
    CredentialsHandlerFactory provideCredentialsHandlerFactory() {
        return mock(CredentialsHandlerFactory.class);
    }

    @Provides
    @Singleton
    FileService provideFileService() {
        return mock(FileService.class);
    }

    @Provides
    @Singleton
    FirebirdUserService provideFirebirdUserService() {
        return mock(FirebirdUserService.class);
    }

    @Provides
    @Singleton
    FormTypeService provideFormTypeService() {
        return mock(FormTypeService.class);
    }

    @Provides
    @Singleton
    ProtocolService provideProtocolService() {
        return mock(ProtocolService.class);
    }

    @Provides
    @Singleton
    ProtocolAgentService provideProtocolAgentService() {
        return mock(ProtocolAgentService.class);
    }

    @Provides
    @Singleton
    ProtocolImportService provideProtocolImportService() {
        return mock(ProtocolImportService.class);
    }

    @Provides
    @Singleton
    ProtocolRegistrationService provideInvestigatorRegistrationService() {
        return mock(ProtocolRegistrationService.class);
    }

    @Provides
    @Singleton
    InvestigatorProfileService provideInvestigatorProfileService() {
        return mock(InvestigatorProfileService.class);
    }

    @Provides
    @Singleton
    OrganizationService provideOrganizationService() {
        return mock(OrganizationService.class);
    }

    @Provides
    @Singleton
    ExternalOrganizationService provideNesOrganizationService() {
        return mock(ExternalOrganizationService.class);
    }

    @Provides
    @Singleton
    LocalOrganizationDataService provideLocalOrganizationDataService() {
        return mock(LocalOrganizationDataService.class);
    }

    @Provides
    @Singleton
    CurationService provideCurationService() {
        return mock(CurationService.class);
    }

    @Provides
    @Singleton
    TaskService provideTaskService() {
        return mock(TaskService.class);
    }

    @Provides
    @Singleton
    NesPersonRoleIntegrationService provideNesPersonRoleIntegrationService() {
        return mock(NesPersonRoleIntegrationService.class);
    }

    @Provides
    @Singleton
    OrganizationAssociationService provideOrganizationAssociationService() {
        return mock(OrganizationAssociationService.class);
    }

    @Provides
    @Singleton
    PersonAssociationService providePersonAssociationService() {
        return mock(PersonAssociationService.class);
    }

    @Provides
    @Singleton
    CountryLookupService provideCountryLookupService() {
        return mock(CountryLookupService.class);
    }

    @Provides
    @Singleton
    CertificateAuthorityManager provideCertificateAutorityManagerLocal() {
        return mock(CertificateAuthorityManager.class);
    }

    @Provides
    @Singleton
    SponsorService provideSponsorService() {
        return mock(SponsorService.class);
    }

    @Provides
    @Singleton
    GenericDataRetrievalService provideDataRetrievalService() {
        return mock(GenericDataRetrievalService.class);
    }

    @Provides
    @Singleton
    GridAuthenticationService provideGridAuthenticationService() {
        return mock(GridAuthenticationService.class);
    }

    @Provides
    @Singleton
    StateLookupService provideStateLookupService() {
        return mock(StateLookupService.class);
    }

    @Provides
    @Singleton
    TemplateService provideTemplateService() {
        return mock(TemplateService.class);
    }

    @Provides
    @Singleton
    ProfileRefreshService provideProfileNesRefreshService() {
        return mock(ProfileRefreshService.class);
    }

    @Provides
    @Singleton
    AnnualRegistrationConfigurationService provideAnnualRegistrationConfigurationService() {
        return mock(AnnualRegistrationConfigurationService.class);
    }

    @Provides
    @Singleton
    AnnualRegistrationService provideAnnualRegistrationService() {
        return mock(AnnualRegistrationService.class);
    }

    @Provides
    @Singleton
    AnnualRegistrationReviewService provideAnnualRegistrationReviewService() {
        return mock(AnnualRegistrationReviewService.class);
    }

    @Provides
    @Singleton
    ProtocolRegistrationReviewService provideProtocolRegistrationReviewService() {
        return mock(ProtocolRegistrationReviewService.class);
    }

    @Provides
    @Singleton
    RegistrationWithdrawalService provideRegistrationWithdrawalService() {
        return mock(RegistrationWithdrawalService.class);
    }

    @Provides
    @Singleton
    InvestigatorDisqualificationService provideInvestigatorDisqualificationService() {
        return mock(InvestigatorDisqualificationService.class);
    }

    @Provides
    @Singleton
    ResearchOrganizationIntegrationService provideResearchOrganizationIntegrationService() {
        return mock(ResearchOrganizationIntegrationService.class);
    }

    @Provides
    @Singleton
    PersonService providePersonService() {
        return mock(PersonServiceBean.class);
    }

    @Provides
    @Singleton
    IdentifiedPersonI provideIdentifiedPersonI() {
        return mock(IdentifiedPersonI.class);
    }

    @Provides
    @Singleton
    LocalPersonDataService provideLocalPersonDataService() {
        return mock(LocalPersonDataService.class);
    }

    @Provides
    @Singleton
    NesOrganizationIntegrationServiceFactory provideNesOrganizationIntegrationServiceFactory(
            NesOrganizationIntegrationServiceFactoryBean bean) {
        return spy(bean);
    }

    @Provides
    @Singleton
    HealthCareFacilityIntegrationService provideHealthCareFacilityIntegrationService() {
        return mock(HealthCareFacilityIntegrationService.class);
    }

    @Provides
    @Singleton
    OversightCommitteeIntegrationService provideOversightCommitteeIntegrationService() {
        return mock(OversightCommitteeIntegrationService.class);
    }

    @Provides
    @Singleton
    OrganizationEntityIntegrationService provideOrganizationEntityIntegrationService() {
        return mock(OrganizationEntityIntegrationService.class);
    }

    @Provides
    @Singleton
    BaseOrganizationIntegrationService provideBaseOrganizationIntegrationService() {
        return mock(BaseOrganizationIntegrationService.class);
    }

}
