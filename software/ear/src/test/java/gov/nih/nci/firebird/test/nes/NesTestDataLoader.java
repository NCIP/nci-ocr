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
package gov.nih.nci.firebird.test.nes;

import static org.apache.commons.lang3.StringUtils.*;
import gov.nih.nci.coppa.common.LimitOffset;
import gov.nih.nci.coppa.po.Correlation;
import gov.nih.nci.coppa.po.HealthCareFacility;
import gov.nih.nci.coppa.po.OversightCommittee;
import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.coppa.services.entities.organization.common.OrganizationI;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.coppa.services.structuralroles.healthcarefacility.common.HealthCareFacilityI;
import gov.nih.nci.coppa.services.structuralroles.oversightcommittee.common.OversightCommitteeI;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.InstitutionalReviewBoard;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.NesId;
import gov.nih.nci.firebird.nes.common.ReplacedEntityException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.nes.organization.IdentifiedOrganizationIntegrationService;
import gov.nih.nci.firebird.nes.organization.NesOrganizationIntegrationServiceFactory;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationIntegrationService;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationType;
import gov.nih.nci.firebird.nes.person.NesPersonIntegrationService;
import gov.nih.nci.firebird.nes.person.PersonTranslator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.iso._21090.CD;
import org.iso._21090.II;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class NesTestDataLoader {

    /**
     * This email address has well over 500 results.  Will slow down tests.
     */
    private static final String PROBLEMATIC_EMAIL_ADDRESS = "unknown@example.com";

    private static final int MAX_RESULT_COUNT = 500;

    private static final List<String> QUARANTINED_ORGANIZATION_NAMES = Lists.newArrayList(
            "Hospital Israelita Buenos Aires",
            "Instituto De Oncologia",
            "Hospital Italiano of Buenos Aires",
            "Colegio San Martin de Tours",
            "Hospitales Privado Guemes",
            "Deutsches Hospital",
            "Universidad Nacional Del Sur");

    private NesTestDataSource dataSource;
    private final File cacheFile;
    private final Provider<PersonI> personServiceProvider;
    private final Provider<OrganizationI> organizationServiceProvider;
    private final Provider<HealthCareFacilityI> healthCareFacilityProvider;
    private final Provider<OversightCommitteeI> oversightCommitteeProvider;
    private final int requestedOrganizationCount;
    private final int requestedPersonCount;
    private final int requestedPracticeSiteCount;
    private final int requestedClinicalLabCount;
    private final int requestedIrbCount;
    private Set<String> retrievedOrganizationIds = Sets.newHashSet();
    private final NesPersonIntegrationService nesPersonIntegrationService;
    private final Set<String> protocolSponsorNesIds;
    private final String annualRegistrationSponsorNesId;
    private final NesOrganizationIntegrationServiceFactory nesServiceFactory;
    private final IdentifiedOrganizationIntegrationService identifiedOrganizationService;

    @Inject
    NesTestDataLoader(Provider<PersonI> personServiceProvider, Provider<OrganizationI> organizationServiceProvider,
            Provider<HealthCareFacilityI> healthCareFacilityProvider,
            Provider<OversightCommitteeI> oversightCommitteeProvider,
            NesOrganizationIntegrationServiceFactory nesServiceFactory,
            NesPersonIntegrationService nesPersonIntegrationService,
            IdentifiedOrganizationIntegrationService identifiedOrganizationService,
            @Named("nes.service.hostname") String nesHostname,
            @Named("cached.nes.organization.count") int requestedOrganizationCount,
            @Named("cached.nes.person.count") int requestedPersonCount,
            @Named("cached.nes.practiceSite.count") int requestedPracticeSiteCount,
            @Named("cached.nes.clinicalLab.count") int requestedClinicalLabCount,
            @Named("cached.nes.irb.count") int requestedIrbCount,
            @Named("sponsor.organization.with.protocol.registrations.nes.ids") Set<String> protocolSponsorNesIds,
            @Named("sponsor.organization.with.annual.registrations.nes.id") String annualRegistrationSponsorNesId) {
        this.personServiceProvider = personServiceProvider;
        this.organizationServiceProvider = organizationServiceProvider;
        this.healthCareFacilityProvider = healthCareFacilityProvider;
        this.oversightCommitteeProvider = oversightCommitteeProvider;
        this.nesServiceFactory = nesServiceFactory;
        this.nesPersonIntegrationService = nesPersonIntegrationService;
        this.identifiedOrganizationService = identifiedOrganizationService;
        this.requestedOrganizationCount = requestedOrganizationCount;
        this.requestedPersonCount = requestedPersonCount;
        this.requestedPracticeSiteCount = requestedPracticeSiteCount;
        this.requestedClinicalLabCount = requestedClinicalLabCount;
        this.requestedIrbCount = requestedIrbCount;
        this.protocolSponsorNesIds = protocolSponsorNesIds;
        this.annualRegistrationSponsorNesId = annualRegistrationSponsorNesId;
        cacheFile = getCacheFile(nesHostname);
    }

    File getCacheFile(String nesHostname) {
        File targetDir = new File(NesTestDataLoader.class.getResource("/").getFile());
        return new File(targetDir, nesHostname + "_data.ser");
    }

    public NesTestDataSource getCache() {
        if (dataSource == null) {
            initializeCache();
        }
        return dataSource;
    }

    private void initializeCache() {
        if (isCacheFileAvailable() && isCachedDataFresh()) {
            deserializeCacheFile();
        } else {
            loadDataSource();
            serializeCache();
        }
    }

    private boolean isCacheFileAvailable() {
        return getCacheFile().exists();
    }

    boolean isCachedDataFresh() {
        Date lastModified = new Date(getCacheFile().lastModified());
        return lastModified.after(DateUtils.addDays(new Date(), -1));
    }

    private File getCacheFile() {
        return cacheFile;
    }

    private void deserializeCacheFile() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(getCacheFile());
            dataSource = (NesTestDataSource) SerializationUtils.deserialize(fis);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(getCacheFile() + " not found", e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    private void loadDataSource() {
        try {
            dataSource = new NesTestDataSource();
            loadPersons();
            loadProtocolSponsorOrganizations();
            loadAnnualRegistrationSponsorOrganization();
            loadIrbs();
            loadHealthCareFacilities();
            loadOrganizations();
            loadPharmaceuticalCompanies();
        } catch (RemoteException e) {
            throw new IllegalStateException("Loading cache from NES failed", e);
        } catch (UnavailableEntityException e) {
            throw new IllegalStateException("Loading cache from NES failed", e);
        }
    }

    private void loadPersons() throws RemoteException {
        Person searchPerson = PersonTranslator.buildNesPerson("%", "%");
        searchPerson.setStatusCode(getActiveStatusCode());
        int searchSize = requestedPersonCount > MAX_RESULT_COUNT ? MAX_RESULT_COUNT : requestedPersonCount;
        LimitOffset limitOffset = buildLimitOffset(searchSize);
        Person[] personResults = new Person[0];
        List<gov.nih.nci.firebird.data.Person> persons = new ArrayList<gov.nih.nci.firebird.data.Person>();
        while (personResults != null && persons.size() < requestedPersonCount) {
            personResults = personServiceProvider.get().query(searchPerson, limitOffset);
            if (personResults != null) {
                addPersons(personResults, persons);
                limitOffset.setOffset(limitOffset.getOffset() + limitOffset.getLimit());
            }
        }
        dataSource.setPersons(persons);
    }

    private void addPersons(Person[] personResults, List<gov.nih.nci.firebird.data.Person> persons) {
        for (Person nesPerson : personResults) {
            gov.nih.nci.firebird.data.Person firebirdPerson = PersonTranslator.buildFirebirdPerson(nesPerson);
            if (StringUtils.isNotBlank(firebirdPerson.getPhoneNumber())
                    && !firebirdPerson.getEmail().equalsIgnoreCase(PROBLEMATIC_EMAIL_ADDRESS)) {
                addCtepId(firebirdPerson);
                persons.add(firebirdPerson);
            }
        }
    }

    private void addCtepId(gov.nih.nci.firebird.data.Person person) {
        String ctepId = nesPersonIntegrationService.getCtepId(person.getNesId());
        person.setCtepId(ctepId);
    }

    private CD getActiveStatusCode() {
        CD activeCode = new CD();
        activeCode.setCode("active");
        return activeCode;
    }

    private LimitOffset buildLimitOffset(int requestedCount) {
        LimitOffset limitOffset = new LimitOffset();
        limitOffset.setOffset(getStartingOffset());
        limitOffset.setLimit(requestedCount);
        return limitOffset;
    }

    int getStartingOffset() {
        return 0;
    }

    private void loadProtocolSponsorOrganizations() throws UnavailableEntityException {
        Map<String, Organization> protocolSponsorOrganizationMap = new HashMap<String, Organization>();
        for (String sponsorNesId : protocolSponsorNesIds) {
            protocolSponsorOrganizationMap.put(sponsorNesId, getOrganization(sponsorNesId));
        }
        dataSource.setProtocolSponsorOrganizationMap(protocolSponsorOrganizationMap);
    }

    private void loadAnnualRegistrationSponsorOrganization() throws UnavailableEntityException {
        if (!isEmpty(annualRegistrationSponsorNesId)) {
            dataSource.setAnnualRegistrationSponsor(getOrganization(annualRegistrationSponsorNesId));
        }
    }

    private void loadIrbs() throws RemoteException, UnavailableEntityException {
        OversightCommittee searchOversightCommittee = new OversightCommittee();
        searchOversightCommittee.setStatus(getActiveStatusCode());
        List<InstitutionalReviewBoard> irbs = new ArrayList<InstitutionalReviewBoard>();
        LimitOffset limitOffset = buildLimitOffset(requestedIrbCount);
        OversightCommittee[] oversightCommittees = oversightCommitteeProvider.get().query(searchOversightCommittee,
                limitOffset);
        for (OversightCommittee committee : oversightCommittees) {
            II ii = getIi(committee, NesIIRoot.OVERSIGHT_COMMITTEE);
            Organization organization = getOrganization(ii);
            InstitutionalReviewBoard irb = new InstitutionalReviewBoard();
            organization.addRole(irb);
            irbs.add(irb);
        }
        dataSource.setIrbs(irbs);
    }

    private II getIi(Correlation correlation, NesIIRoot expectedRoot) {
        for (II ii : correlation.getIdentifier().getItem()) {
            if (expectedRoot.getRoot().equals(ii.getRoot())) {
                return ii;
            }
        }
        throw new IllegalArgumentException("No II in Correlation for root " + expectedRoot);
    }

    private Organization getOrganization(II identifier) throws RemoteException, UnavailableEntityException {
        return getOrganization(new NesId(identifier).toString());
    }

    private Organization getOrganization(String nesId) throws UnavailableEntityException {
        retrievedOrganizationIds.add(nesId);
        try {
            return nesServiceFactory.getService(nesId).getById(nesId);
        } catch (ReplacedEntityException e) {
            return getOrganization(e.getReplacmentNesId());
        }
    }

    private void loadOrganizations() throws RemoteException, UnavailableEntityException {
        List<Organization> organizations = new ArrayList<Organization>();
        gov.nih.nci.coppa.po.Organization searchOrganization = new gov.nih.nci.coppa.po.Organization();
        searchOrganization.setStatusCode(getActiveStatusCode());
        int searchSize = requestedOrganizationCount > MAX_RESULT_COUNT ? MAX_RESULT_COUNT : requestedOrganizationCount;
        LimitOffset limitOffset = buildLimitOffset(searchSize);
        gov.nih.nci.coppa.po.Organization[] nesOrganizations = new gov.nih.nci.coppa.po.Organization[0];
        while (nesOrganizations != null && organizations.size() < requestedOrganizationCount) {
            nesOrganizations = organizationServiceProvider.get().query(searchOrganization, limitOffset);
            if (nesOrganizations != null) {
                addOrganizations(nesOrganizations, organizations);
                limitOffset.setOffset(limitOffset.getOffset() + limitOffset.getLimit());
            }
        }
        dataSource.setOrganizations(organizations);
    }

    private void addOrganizations(gov.nih.nci.coppa.po.Organization[] nesOrganizations, List<Organization> organizations)
            throws UnavailableEntityException {
        for (gov.nih.nci.coppa.po.Organization nesOrganization : nesOrganizations) {
            String nesId = new NesId(nesOrganization.getIdentifier()).toString();
            if (!retrievedOrganizationIds.contains(nesId)) {
                Organization organization = getOrganization(nesId);
                if (!isNameProblematic(organization)) {
                    organizations.add(organization);
                }
            }
        }
    }

    private boolean isNameProblematic(Organization organization) {
        String name = organization.getName();
        return name.contains("  ") || name.contains("'") || name.contains(".")
                || QUARANTINED_ORGANIZATION_NAMES.contains(name);
    }

    private void loadPharmaceuticalCompanies() throws RemoteException {
        List<Organization> pharmaceuticalCompanies = getResearchOrganization().searchByName("tex",
                ResearchOrganizationType.DRUG_COMPANY);
        filterOutProblematicNames(pharmaceuticalCompanies);
        addCtepIds(pharmaceuticalCompanies);
        dataSource.setPharmaceuticalCompanies(pharmaceuticalCompanies);
    }

    private void addCtepIds(List<Organization> pharmaceuticalCompanies) {
        for (Organization organization : pharmaceuticalCompanies) {
            organization.setCtepId(identifiedOrganizationService.getCtepId(organization.getPlayerIdentifier()));
        }
    }

    private ResearchOrganizationIntegrationService getResearchOrganization() {
        return nesServiceFactory.getResearchOrganizationService();
    }

    private void filterOutProblematicNames(List<Organization> organizations) {
        for (int i = 0; i < organizations.size();) {
            if (isNameProblematic(organizations.get(i))) {
                organizations.remove(i);
            } else {
                i++;
            }
        }
    }

    private void loadHealthCareFacilities() throws RemoteException, UnavailableEntityException {
        List<HealthCareFacility> facilities = getHealthCareFacilities();
        List<HealthCareFacility> practiceSiteFacilities = facilities.subList(0, facilities.size() / 2);
        List<HealthCareFacility> clinicalLabFacilities = facilities.subList(facilities.size() / 2, facilities.size());
        loadPracticeSites(practiceSiteFacilities);
        loadClinicalLabs(clinicalLabFacilities);
    }

    private List<HealthCareFacility> getHealthCareFacilities() throws RemoteException {
        HealthCareFacility searchFacility = new HealthCareFacility();
        searchFacility.setStatus(getActiveStatusCode());
        int requestedCount = requestedClinicalLabCount + requestedPracticeSiteCount;
        int searchSize = requestedCount > MAX_RESULT_COUNT ? MAX_RESULT_COUNT : requestedCount;
        LimitOffset limitOffset = buildLimitOffset(searchSize);
        HealthCareFacility[] facilities = new HealthCareFacility[0];
        List<HealthCareFacility> healthCareFacilities = Lists.newArrayList();
        while (facilities != null && healthCareFacilities.size() < requestedCount) {
            facilities = healthCareFacilityProvider.get().query(searchFacility, limitOffset);
            if (facilities != null) {
                for (HealthCareFacility healthCareFacility : facilities) {
                    healthCareFacilities.add(healthCareFacility);
                }
                limitOffset.setOffset(limitOffset.getOffset() + limitOffset.getLimit());
            }
        }

        return healthCareFacilities;
    }

    private void loadPracticeSites(List<HealthCareFacility> facilities) throws RemoteException,
            UnavailableEntityException {
        List<PracticeSite> practiceSites = new ArrayList<PracticeSite>();
        for (HealthCareFacility facility : facilities) {
            II ii = getIi(facility, NesIIRoot.HEALTH_CARE_FACILITY);
            Organization organization = getOrganization(ii);
            PracticeSite practiceSite = new PracticeSite();
            practiceSite.setType(PracticeSiteType.HEALTH_CARE_FACILITY);
            organization.addRole(practiceSite);
            practiceSites.add(practiceSite);
        }
        dataSource.setPracticeSites(practiceSites);
    }

    private void loadClinicalLabs(List<HealthCareFacility> facilities) throws RemoteException,
            UnavailableEntityException {
        List<ClinicalLaboratory> labs = new ArrayList<ClinicalLaboratory>();
        for (HealthCareFacility facility : facilities) {
            II ii = getIi(facility, NesIIRoot.HEALTH_CARE_FACILITY);
            Organization organization = getOrganization(ii);
            ClinicalLaboratory lab = new ClinicalLaboratory();
            organization.addRole(lab);
            labs.add(lab);
        }
        dataSource.setClinicalLabs(labs);
    }

    private void serializeCache() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(getCacheFile());
            SerializationUtils.serialize(dataSource, fos);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(getCacheFile() + " not found", e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

}
