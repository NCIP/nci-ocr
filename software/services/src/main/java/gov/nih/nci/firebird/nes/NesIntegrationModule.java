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
package gov.nih.nci.firebird.nes;

import static gov.nih.nci.firebird.common.FirebirdConstants.*;
import gov.nih.nci.coppa.services.business.business.client.BusinessClient;
import gov.nih.nci.coppa.services.business.business.common.BusinessI;
import gov.nih.nci.coppa.services.entities.organization.client.OrganizationClient;
import gov.nih.nci.coppa.services.entities.organization.common.OrganizationI;
import gov.nih.nci.coppa.services.entities.person.client.PersonClient;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.coppa.services.structuralroles.healthcarefacility.client.HealthCareFacilityClient;
import gov.nih.nci.coppa.services.structuralroles.healthcarefacility.common.HealthCareFacilityI;
import gov.nih.nci.coppa.services.structuralroles.healthcareprovider.client.HealthCareProviderClient;
import gov.nih.nci.coppa.services.structuralroles.healthcareprovider.common.HealthCareProviderI;
import gov.nih.nci.coppa.services.structuralroles.identifiedorganization.client.IdentifiedOrganizationClient;
import gov.nih.nci.coppa.services.structuralroles.identifiedorganization.common.IdentifiedOrganizationI;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.client.IdentifiedPersonClient;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.coppa.services.structuralroles.organizationalcontact.client.OrganizationalContactClient;
import gov.nih.nci.coppa.services.structuralroles.organizationalcontact.common.OrganizationalContactI;
import gov.nih.nci.coppa.services.structuralroles.oversightcommittee.client.OversightCommitteeClient;
import gov.nih.nci.coppa.services.structuralroles.oversightcommittee.common.OversightCommitteeI;
import gov.nih.nci.coppa.services.structuralroles.researchorganization.client.ResearchOrganizationClient;
import gov.nih.nci.coppa.services.structuralroles.researchorganization.common.ResearchOrganizationI;
import gov.nih.nci.firebird.cagrid.GridCredentialService;
import gov.nih.nci.firebird.nes.correlation.NesPersonRoleIntegrationService;
import gov.nih.nci.firebird.nes.correlation.NesPersonRoleIntegrationServiceBean;
import gov.nih.nci.firebird.nes.organization.HealthCareFacilityIntegrationService;
import gov.nih.nci.firebird.nes.organization.HealthCareFacilityIntegrationServiceBean;
import gov.nih.nci.firebird.nes.organization.IdentifiedOrganizationIntegrationService;
import gov.nih.nci.firebird.nes.organization.IdentifiedOrganizationIntegrationServiceBean;
import gov.nih.nci.firebird.nes.organization.NesOrganizationIntegrationServiceFactory;
import gov.nih.nci.firebird.nes.organization.NesOrganizationIntegrationServiceFactoryBean;
import gov.nih.nci.firebird.nes.organization.NesOrganizationServiceBean;
import gov.nih.nci.firebird.nes.organization.OrganizationEntityIntegrationService;
import gov.nih.nci.firebird.nes.organization.OrganizationEntityIntegrationServiceBean;
import gov.nih.nci.firebird.nes.organization.OversightCommitteeIntegrationService;
import gov.nih.nci.firebird.nes.organization.OversightCommitteeIntegrationServiceBean;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationIntegrationService;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationIntegrationServiceBean;
import gov.nih.nci.firebird.nes.person.NesPersonServiceBean;
import gov.nih.nci.firebird.proxy.FaultTolerantProxyFactory;
import gov.nih.nci.firebird.proxy.ProxyConfiguration;
import gov.nih.nci.firebird.service.organization.external.ExternalOrganizationService;
import gov.nih.nci.firebird.service.person.external.ExternalPersonService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Configures NES integration.
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.CouplingBetweenObjects", "PMD.AvoidDuplicateLiterals" })
// Module provides access to many NES services; annotation value specified multiple times
public class NesIntegrationModule extends AbstractModule {

    private static final Logger LOG = Logger.getLogger(NesIntegrationModule.class);

    private static final List<Class<? extends Throwable>> EXPECTED_NES_EXCEPTIONS =
            new ArrayList<Class<? extends Throwable>>(3);
    static {
        EXPECTED_NES_EXCEPTIONS.add(gov.nih.nci.coppa.common.faults.TooManyResultsFault.class);
        EXPECTED_NES_EXCEPTIONS.add(gov.nih.nci.coppa.po.faults.NullifiedEntityFault.class);
        EXPECTED_NES_EXCEPTIONS.add(gov.nih.nci.coppa.po.faults.EntityValidationFault.class);
    }

    @Override
    protected void configure() {
        bind(ExternalPersonService.class).to(NesPersonServiceBean.class).in(Scopes.SINGLETON);
        bind(ExternalOrganizationService.class).to(NesOrganizationServiceBean.class).in(Scopes.SINGLETON);
        bind(NesPersonRoleIntegrationService.class).to(NesPersonRoleIntegrationServiceBean.class)
            .in(Scopes.SINGLETON);
        bind(IdentifiedOrganizationIntegrationService.class).to(IdentifiedOrganizationIntegrationServiceBean.class)
            .in(Scopes.SINGLETON);
        bind(OrganizationEntityIntegrationService.class).to(OrganizationEntityIntegrationServiceBean.class)
            .in(Scopes.SINGLETON);
        bind(HealthCareFacilityIntegrationService.class).to(HealthCareFacilityIntegrationServiceBean.class)
            .in(Scopes.SINGLETON);
        bind(ResearchOrganizationIntegrationService.class).to(ResearchOrganizationIntegrationServiceBean.class)
            .in(Scopes.SINGLETON);
        bind(OversightCommitteeIntegrationService.class).to(OversightCommitteeIntegrationServiceBean.class)
            .in(Scopes.SINGLETON);
        bind(NesOrganizationIntegrationServiceFactory.class).to(NesOrganizationIntegrationServiceFactoryBean.class)
            .in(Scopes.SINGLETON);
    }

    @Provides
    @Named("cagrid.firebird.nes.credential")
    GlobusCredential provideNesCredential(GridCredentialService credentialService,
            @Named("cagrid.firebird.username") String username, @Named("cagrid.firebird.password") String password,
            @Named("cagrid.dorian.url") String dorianUrl) {
        GlobusCredential credential = credentialService.getCredential(username, password, dorianUrl);
        if (credential == null) {
            LOG.error("Couldn't get NES access credential");
            throw new IllegalStateException("Couldn't get NES access credential");
        }
        return credential;
    }

    @Provides
    PersonClient providePersonClient(@Named("nes.personService.url") String url,
            @Named("cagrid.firebird.nes.credential") GlobusCredential credential)
                    throws IOException {
        try {
            return new PersonClient(url, credential);
        } catch (IOException e) {
            LOG.error("Couldn't create PersonClient", e);
            throw e;
        }
    }

    @Provides
    @Singleton
    PersonI providePersonI(Provider<PersonClient> clientProvider,
            @Named("nes.proxy.configuration") ProxyConfiguration configuration) {
        return FaultTolerantProxyFactory.create(clientProvider, PersonI.class, configuration);
    }

    @Provides
    @Named("nes.proxy.configuration")
    @Singleton
    ProxyConfiguration provideNesProxyConfiguration(@Named(PROXY_POOL_MAX_IDLE_KEY) int maxIdle,
            @Named(PROXY_POOL_INITIAL_KEY) int initIdleCapacity, @Named(PROXY_MAX_ATTEMPTS_KEY) int maxAttempts) {
        return new ProxyConfiguration(maxIdle, initIdleCapacity, maxAttempts, EXPECTED_NES_EXCEPTIONS);
    }

    @Provides
    OrganizationClient provideOrganizationClient(@Named("nes.organizationService.url") String url,
            @Named("cagrid.firebird.nes.credential") GlobusCredential credential) throws IOException {
        try {
            return new OrganizationClient(url, credential);
        } catch (IOException e) {
            LOG.error("Couldn't create OrganizationClient", e);
            throw e;
        }
    }

    @Provides
    @Singleton
    OrganizationI provideOrganizationI(Provider<OrganizationClient> clientProvider,
            @Named("nes.proxy.configuration") ProxyConfiguration configuration) {
        return FaultTolerantProxyFactory.create(clientProvider, OrganizationI.class, configuration);
    }

    @Provides
    HealthCareProviderClient provideHealthCareProviderClient(@Named("nes.healthCareProvider.url") String url,
            @Named("cagrid.firebird.nes.credential") GlobusCredential credential) throws IOException {
        try {
            return new HealthCareProviderClient(url, credential);
        } catch (IOException e) {
            LOG.error("Couldn't create HealthCareProviderClient", e);
            throw e;
        }
    }

    @Provides
    @Singleton
    HealthCareProviderI provideHealthCareProviderI(Provider<HealthCareProviderClient> clientProvider,
            @Named("nes.proxy.configuration") ProxyConfiguration configuration) {
        return FaultTolerantProxyFactory.create(clientProvider, HealthCareProviderI.class, configuration);
    }

    @Provides
    OrganizationalContactClient provideOrganizationalContactClient(@Named("nes.organizationalContact.url") String url,
            @Named("cagrid.firebird.nes.credential") GlobusCredential credential) throws IOException {
        try {
            return new OrganizationalContactClient(url, credential);
        } catch (IOException e) {
            LOG.error("Couldn't create OrganizationalContactClient", e);
            throw e;
        }
    }

    @Provides
    @Singleton
    OrganizationalContactI provideOrganizationalContactI(Provider<OrganizationalContactClient> clientProvider,
            @Named("nes.proxy.configuration") ProxyConfiguration configuration) {
        return FaultTolerantProxyFactory.create(clientProvider, OrganizationalContactI.class, configuration);
    }

    @Provides
    HealthCareFacilityClient provideHealthCareFacilityClient(@Named("nes.healthCareFacility.url") String url,
            @Named("cagrid.firebird.nes.credential") GlobusCredential credential) throws IOException {
        try {
            return new HealthCareFacilityClient(url, credential);
        } catch (IOException e) {
            LOG.error("Couldn't create HealthCareFacilityClient", e);
            throw e;
        }
    }

    @Provides
    @Singleton
    HealthCareFacilityI provideHealthCareFacilityI(Provider<HealthCareFacilityClient> clientProvider,
            @Named("nes.proxy.configuration") ProxyConfiguration configuration) {
        return FaultTolerantProxyFactory.create(clientProvider, HealthCareFacilityI.class, configuration);
    }

    @Provides
    OversightCommitteeClient provideOversightCommitteeClient(@Named("nes.oversightCommittee.url") String url,
            @Named("cagrid.firebird.nes.credential") GlobusCredential credential) throws IOException {
        try {
            return new OversightCommitteeClient(url, credential);
        } catch (IOException e) {
            LOG.error("Couldn't create OversightCommitteeClient", e);
            throw e;
        }
    }

    @Provides
    @Singleton
    OversightCommitteeI provideOversightCommitteeI(Provider<OversightCommitteeClient> clientProvider,
            @Named("nes.proxy.configuration") ProxyConfiguration configuration) {
        return FaultTolerantProxyFactory.create(clientProvider, OversightCommitteeI.class, configuration);
    }

    @Provides
    IdentifiedOrganizationClient provideIdentifiedOrganizationClient(
            @Named("nes.identifiedOrganization.url") String url,
            @Named("cagrid.firebird.nes.credential") GlobusCredential credential) throws IOException {
        try {
            return new IdentifiedOrganizationClient(url, credential);
        } catch (IOException e) {
            LOG.error("Couldn't create IdentifiedOrganizationClient", e);
            throw e;
        }
    }

    @Provides
    IdentifiedPersonClient provideIdentifiedPersonClient(
            @Named("nes.identifiedPerson.url") String url,
            @Named("cagrid.firebird.nes.credential") GlobusCredential credential) throws IOException {
        try {
            return new IdentifiedPersonClient(url, credential);
        } catch (IOException e) {
            LOG.error("Couldn't create IdentifiedOrganizationClient", e);
            throw e;
        }
    }

    @Provides
    @Singleton
    IdentifiedOrganizationI provideIdentifiedOrganizationI(Provider<IdentifiedOrganizationClient> clientProvider,
            @Named("nes.proxy.configuration") ProxyConfiguration configuration) {
        return FaultTolerantProxyFactory.create(clientProvider, IdentifiedOrganizationI.class, configuration);
    }

    @Provides
    @Singleton
    IdentifiedPersonI provideIdentifiedPersonI(Provider<IdentifiedPersonClient> clientProvider,
            @Named("nes.proxy.configuration") ProxyConfiguration configuration) {
        return FaultTolerantProxyFactory.create(clientProvider, IdentifiedPersonI.class, configuration);
    }

    @Provides
    ResearchOrganizationClient provideResearchOrganizationClient(@Named("nes.researchOrganization.url") String url,
            @Named("cagrid.firebird.nes.credential") GlobusCredential credential) throws IOException {
        try {
            return new ResearchOrganizationClient(url, credential);
        } catch (IOException e) {
            LOG.error("Couldn't create ResearchOrganizationClient", e);
            throw e;
        }
    }

    @Provides
    @Singleton
    ResearchOrganizationI provideResearchOrganizationI(Provider<ResearchOrganizationClient> clientProvider,
            @Named("nes.proxy.configuration") ProxyConfiguration configuration) {
        return FaultTolerantProxyFactory.create(clientProvider, ResearchOrganizationI.class, configuration);
    }

    @Provides
    BusinessClient provideBusinessClient(@Named("nes.business.url") String url,
            @Named("cagrid.firebird.nes.credential") GlobusCredential credential) throws IOException {
        try {
            return new BusinessClient(url, credential);
        } catch (IOException e) {
            LOG.error("Couldn't create BusinessClient", e);
            throw e;
        }
    }

    @Provides
    @Singleton
    BusinessI provideBusinessI(Provider<BusinessClient> clientProvider,
            @Named("nes.proxy.configuration") ProxyConfiguration configuration) {
        return FaultTolerantProxyFactory.create(clientProvider, BusinessI.class, configuration);
    }

}
