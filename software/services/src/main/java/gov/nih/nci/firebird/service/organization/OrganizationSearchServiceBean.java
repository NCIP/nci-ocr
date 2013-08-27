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

import static gov.nih.nci.firebird.nes.organization.OversightCommitteeType.INSTITUTIONAL_REVIEW_BOARD;
import static gov.nih.nci.firebird.nes.organization.ResearchOrganizationType.CANCER_CENTER;
import static gov.nih.nci.firebird.nes.organization.ResearchOrganizationType.CLINICAL_CENTER;
import static gov.nih.nci.firebird.nes.organization.ResearchOrganizationType.NCI_CTEP;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.nes.common.ReplacedEntityException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.nes.organization.HealthCareFacilityIntegrationService;
import gov.nih.nci.firebird.nes.organization.NesOrganizationIntegrationServiceFactory;
import gov.nih.nci.firebird.nes.organization.OrganizationEntityIntegrationService;
import gov.nih.nci.firebird.nes.organization.OversightCommitteeIntegrationService;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationIntegrationService;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

/**
 * Performs a combined search of the local database and the NES Organization service and provides caching of results for
 * improved performance.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SuppressWarnings("PMD.TooManyMethods") // false positive in Eclipse problem list
public class OrganizationSearchServiceBean implements OrganizationSearchService {

    private static final String UNCHECKED = "unchecked";
    private OrganizationService organizationService;
    private NesOrganizationIntegrationServiceFactory nesServiceFactory;

    @Inject
    void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Inject
    void setNesServiceFactory(NesOrganizationIntegrationServiceFactory nesServiceFactory) {
        this.nesServiceFactory = nesServiceFactory;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    // required for varargs parameter of List<Organization>[]
    public List<OrganizationSearchResult> search(String searchTerm) {
        List<Organization> nesNameMatches = searchByName(searchTerm);
        List<Organization> assignedIdentifierMatches = searchByAssignedIdentifier(searchTerm);
        return createResults(nesNameMatches, assignedIdentifierMatches);
    }

    private List<Organization> searchByName(String searchTerm) {
        return getOrganizationEntityService().searchByName(searchTerm);
    }

    @Override
    public List<Organization> searchByAssignedIdentifier(String extension) {
        return getOrganizationEntityService().searchByAssignedIdentifier(extension);
    }

    private List<OrganizationSearchResult> createResults(List<Organization>... organizationLists) {
        Map<String, Organization> resultsMap = Maps.newHashMap();        
        for (List<Organization> organizations : organizationLists) {
            addToResultsMap(resultsMap, organizations);
        }
        return createResults(resultsMap);
    }

    private void addToResultsMap(Map<String, Organization> resultsMap, List<Organization> organizations) {
        for (Organization organization : organizations) {
            if (!CurationStatus.NULLIFIED.equals(organization.getNesStatus())) {
                resultsMap.put(organization.getNesId(), organization);
            }
        }
    }

    @SuppressWarnings("unused") // preventing false positive report in Eclipse
    private List<OrganizationSearchResult> createResults(Map<String, Organization> resultsMap) {
        substituteWithAvailableLocalOrganizations(resultsMap);
        List<Organization> organizations = Lists.newArrayList(resultsMap.values());
        Collections.sort(organizations);
        return transformToSearchResults(organizations);
    }


    private List<OrganizationSearchResult> transformToSearchResults(List<Organization> organizations) {
        return Lists.transform(organizations, new Function<Organization, OrganizationSearchResult>() {
            @Override
            public OrganizationSearchResult apply(Organization organization) {
                return new OrganizationSearchResult(getKey(organization), organization);
            }
        });
    }

    private String getKey(Organization organization) {
        String key;
        if (isNesResult(organization)) {
            key = organization.getNesId();
        } else {
            key = organization.getId().toString();
        }
        return key;
    }

    private void substituteWithAvailableLocalOrganizations(Map<String, Organization> resultsMap) {
        for (String nesIdString : resultsMap.keySet()) {
            Organization localMatch = organizationService.getByNesIdLocal(nesIdString);
            if (localMatch != null) {
                resultsMap.put(nesIdString, localMatch);
            }
        }
    }

    private boolean isNesResult(Organization organization) {
        return organization.getId() == null;
    }

    @Override
    public Organization getOrganization(String key) throws UnavailableEntityException {
        if (isNesId(key)) {
            return getByNesId(key);
        } else {
            return organizationService.getById(Long.valueOf(key));
        }
    }

    private boolean isNesId(String key) {
        return key.contains(":");
    }

    private Organization getByNesId(String nesId) throws UnavailableEntityException {
        try {
            return nesServiceFactory.getService(nesId).getById(nesId);
        } catch (ReplacedEntityException e) {
            return getByNesId(e.getReplacmentNesId());
        }
    }

    @SuppressWarnings(UNCHECKED) // required for varargs parameter of List<Organization>[]
    @Override
    public List<OrganizationSearchResult> searchForResearchOrganizations(String searchTerm,
            ResearchOrganizationType type) {
        List<Organization> matches = getResearchOrganizationService().searchByName(searchTerm, type);
        return createResults(matches);
    }

    @Override
    public List<OrganizationSearchResult> searchForPracticeSites(String searchTerm) {
        Map<String, Organization> resultsMap = Maps.newHashMap();
        addToResultsMap(resultsMap, getHealthCareFacilityService().searchByName(searchTerm));
        addToResultsMap(resultsMap, getHealthCareFacilityService().searchByAssignedIdentifier(searchTerm));

        addToResultsMap(resultsMap, getResearchOrganizationService().searchByName(searchTerm, CANCER_CENTER));
        addToResultsMap(resultsMap, 
                getResearchOrganizationService().searchByAssignedIdentifier(searchTerm, CANCER_CENTER));
        addToResultsMap(resultsMap, getResearchOrganizationService().searchByName(searchTerm, CLINICAL_CENTER));
        addToResultsMap(resultsMap, 
                getResearchOrganizationService().searchByAssignedIdentifier(searchTerm, CLINICAL_CENTER));
        addToResultsMap(resultsMap, getResearchOrganizationService().searchByName(searchTerm, NCI_CTEP));
        addToResultsMap(resultsMap, 
                getResearchOrganizationService().searchByAssignedIdentifier(searchTerm, NCI_CTEP));
        return createResults(resultsMap);
    }

    @SuppressWarnings(UNCHECKED) // required for varargs parameter of List<Organization>[]
    @Override
    public List<OrganizationSearchResult> searchForClinicalLaboratories(String searchTerm) {
        List<Organization> healthCareFacilityMatches = getHealthCareFacilityService().searchByName(searchTerm);
        List<Organization> assignedIdentifierMatches = 
                getHealthCareFacilityService().searchByAssignedIdentifier(searchTerm);
        return createResults(healthCareFacilityMatches, assignedIdentifierMatches);
    }

    @SuppressWarnings(UNCHECKED) // required for varargs parameter of List<Organization>[]
    @Override
    public List<OrganizationSearchResult> searchForInstitutionalReviewBoards(String searchTerm) {
        List<Organization> oversightCommitteeMatches = getOversightCommitteeService().searchByName(searchTerm, 
                INSTITUTIONAL_REVIEW_BOARD);
        List<Organization> assignedIdentifierMatches = getOversightCommitteeService()
                .searchByAssignedIdentifier(searchTerm, INSTITUTIONAL_REVIEW_BOARD);
        return createResults(oversightCommitteeMatches, assignedIdentifierMatches);
    }

    private OrganizationEntityIntegrationService getOrganizationEntityService() {
        return nesServiceFactory.getOrganizationEntityService();
    }

    private HealthCareFacilityIntegrationService getHealthCareFacilityService() {
        return nesServiceFactory.getHealthCareFacilityService();
    }

    private OversightCommitteeIntegrationService getOversightCommitteeService() {
        return nesServiceFactory.getOversightCommitteeService();
    }

    private ResearchOrganizationIntegrationService getResearchOrganizationService() {
        return nesServiceFactory.getResearchOrganizationService();
    }
    
}
