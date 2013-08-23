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
package gov.nih.nci.firebird.nes.organization;

import static gov.nih.nci.firebird.test.ValueGenerator.getUniqueString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractOrganizationRole;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.CurationDataset;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.NesId;
import gov.nih.nci.firebird.service.organization.InvalidatedOrganizationException;
import gov.nih.nci.firebird.test.GuiceTestRunnerWithMocks;
import gov.nih.nci.firebird.test.OrganizationFactory;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

@RunWith(GuiceTestRunnerWithMocks.class)
public class NesOrganizationServiceBeanTest {

    private static final String TEST_NES_ID = "NES";

    @Mock
    private PracticeSiteStrategy mockPracticeSiteStrategy;
    @Mock
    private InstitutionReviewBoardStrategy mockIrbStrategy;
    @Mock
    private ClinicalLabStrategy mockClinicalLabStrategy;
    @Mock
    private PrimaryOrganizationStrategy mockPrimaryOrganizationStrategy;
    @Mock
    private PharmaceuticalCompanyStrategy mockPharmaceuticalCompanyStrategy;
    @Mock
    private GenericOrganizationStrategy mockGenericOrganizationStrategy;
    @Mock
    private NesOrganizationIntegrationServiceFactory mockNesServiceFactory;
    @Inject
    private NesOrganizationServiceBean bean;
    @Inject
    private BaseOrganizationIntegrationService mockOrganizationIntegrationService;

    private Organization organization;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        organization = OrganizationFactory.getInstance().create();
        bean.setClinicalLabStrategy(mockClinicalLabStrategy);
        bean.setIrbStrategy(mockIrbStrategy);
        bean.setPracticeSiteStrategy(mockPracticeSiteStrategy);
        bean.setNesServiceFactory(mockNesServiceFactory);
        bean.setPrimaryOrganizationStrategy(mockPrimaryOrganizationStrategy);
        bean.setPharmaceuticalCompanyStrategy(mockPharmaceuticalCompanyStrategy);
        bean.setGenericOrganizationStrategy(mockGenericOrganizationStrategy);
        when(mockNesServiceFactory.getService(TEST_NES_ID)).thenReturn(mockOrganizationIntegrationService);
        when(mockNesServiceFactory.getService(organization)).thenReturn(mockOrganizationIntegrationService);
    }

    @Test
    public void testSearch_ForPracticeSites() {
        String term = "term";
        when(mockPracticeSiteStrategy.search(term)).thenReturn(Lists.newArrayList(organization));

        List<Organization> results = bean.search(term, OrganizationRoleType.PRACTICE_SITE);
        List<Organization> expectedResults = Lists.newArrayList(organization);
        assertEquals(expectedResults, results);
        verify(mockPracticeSiteStrategy).search(term);
        verifyZeroInteractions(mockClinicalLabStrategy, mockIrbStrategy);
    }

    @Test
    public void testSearch_ForIrbs() {
        String term = "term";
        when(mockIrbStrategy.search(term)).thenReturn(Lists.newArrayList(organization));

        List<Organization> results = bean.search(term, OrganizationRoleType.IRB);
        List<Organization> expectedResults = Lists.newArrayList(organization);
        assertEquals(expectedResults, results);
        verify(mockIrbStrategy).search(term);
        verifyZeroInteractions(mockPracticeSiteStrategy, mockClinicalLabStrategy);
    }

    @Test
    public void testSearch_ForClinicalLabs() {
        String term = "term";
        when(mockClinicalLabStrategy.search(term)).thenReturn(Lists.newArrayList(organization));

        List<Organization> results = bean.search(term, OrganizationRoleType.CLINICAL_LABORATORY);
        List<Organization> expectedResults = Lists.newArrayList(organization);
        assertEquals(expectedResults, results);
        verify(mockClinicalLabStrategy).search(term);
        verifyZeroInteractions(mockPracticeSiteStrategy, mockIrbStrategy);
    }

    @Test
    public void testSearch_ForGenericOrganizations() {
        String term = "term";
        when(mockGenericOrganizationStrategy.search(term)).thenReturn(Lists.newArrayList(organization));

        List<Organization> results = bean.search(term, OrganizationRoleType.GENERIC_ORGANIZATION);
        List<Organization> expectedResults = Lists.newArrayList(organization);
        assertEquals(expectedResults, results);
        verify(mockGenericOrganizationStrategy).search(term);
        verifyZeroInteractions(mockPracticeSiteStrategy, mockIrbStrategy);
    }

    @Test
    public void testSearch_ForPrimaryOrganizations() {
        String term = "term";
        when(mockPrimaryOrganizationStrategy.search(term)).thenReturn(Lists.newArrayList(organization));

        List<Organization> results = bean.search(term, OrganizationRoleType.PRIMARY_ORGANIZATION);
        List<Organization> expectedResults = Lists.newArrayList(organization);
        assertEquals(expectedResults, results);
        verify(mockPrimaryOrganizationStrategy).search(term);
        verifyZeroInteractions(mockPracticeSiteStrategy, mockIrbStrategy);
    }

    @Test
    public void testSearch_ForPharmaceuticalCompanies() {
        String term = "term";
        when(mockPharmaceuticalCompanyStrategy.search(term)).thenReturn(Lists.newArrayList(organization));

        List<Organization> results = bean.search(term, OrganizationRoleType.PHARMACEUTICAL_COMPANY);
        List<Organization> expectedResults = Lists.newArrayList(organization);
        assertEquals(expectedResults, results);
        verify(mockPharmaceuticalCompanyStrategy).search(term);
        verifyZeroInteractions(mockPracticeSiteStrategy, mockIrbStrategy);
    }

    @Test
    public void testGetByExternalId() throws Exception {
        when(mockOrganizationIntegrationService.getById(TEST_NES_ID)).thenReturn(organization);
        assertEquals(organization, bean.getByExternalId(TEST_NES_ID));
    }

    @Test(expected = InvalidatedOrganizationException.class)
    public void testGetByExternalIdentifer_NoResult() throws Exception {
        when(mockOrganizationIntegrationService.getById(TEST_NES_ID)).thenThrow(new InvalidatedOrganizationException());
        bean.getByExternalId(TEST_NES_ID);
    }

    @Test
    public void testValidate() throws Exception {
        ResearchOrganizationType type = ResearchOrganizationType.CANCER_CENTER;
        bean.validate(organization, OrganizationRoleType.IRB, type);
        verify(mockIrbStrategy).validate(organization, type);
    }

    @Test
    public void testCreate() throws Exception {
        Object arg = null;
        bean.create(organization, OrganizationRoleType.IRB, arg);
        verify(mockIrbStrategy).create(organization, arg);
    }

    @Test
    public void testRefreshNow() throws Exception {
        bean.refreshNow(organization);
        verify(mockOrganizationIntegrationService).refresh(organization);
    }

    @Test
    public void testRefreshIfStale_StaleOrganization() throws Exception {
        Date staleDate = DateUtils.addDays(new Date(), -2);
        NesOrganizationData nesOrganizationData = (NesOrganizationData) organization.getExternalData();
        nesOrganizationData.setLastNesRefresh(staleDate);
        bean.refreshIfStale(organization);
        verify(mockOrganizationIntegrationService).refresh(organization);
    }

    @Test
    public void testRefreshIfStale_FreshOrganization() throws Exception {
        Date today = new Date();
        NesOrganizationData nesOrganizationData = (NesOrganizationData) organization.getExternalData();
        nesOrganizationData.setLastNesRefresh(today);
        bean.refreshIfStale(organization);
        verify(mockOrganizationIntegrationService, never()).refresh(organization);
    }

    @Test
    public void testGetByAlternateIdentifier() throws Exception {
        String alternateIdentifier = "id";
        when(mockGenericOrganizationStrategy.getByAlternateIdentifier("id")).thenReturn(
                Lists.newArrayList(organization));
        bean.getByAlternateIdentifier(alternateIdentifier, OrganizationRoleType.GENERIC_ORGANIZATION);
        assertEquals(Lists.newArrayList(organization),
                bean.getByAlternateIdentifier(alternateIdentifier, OrganizationRoleType.GENERIC_ORGANIZATION));
    }

    @Test
    public void testGetPracticeSiteType() {
        Organization organization = OrganizationFactory.getInstance().create();
        bean.getPracticeSiteType(organization);
        verify(mockPracticeSiteStrategy).getPracticeSiteType(organization);
    }

    @Test
    public void testGetPrimaryOrganizationType() {
        Organization organization = OrganizationFactory.getInstance().create();
        bean.getPrimaryOrganizationType(organization);
        verify(mockPrimaryOrganizationStrategy).getPrimaryOrganizationType(organization);
    }

    @Test
    public void testGetOrganizationsToBeCurated() {
        NesOrganizationServiceBean beanSpy = spy(bean);
        Organization activeOrganization = OrganizationFactory.getInstance().create();
        activeOrganization.setCurationStatus(CurationStatus.ACTIVE);
        Organization pendingOrganization = OrganizationFactory.getInstance().create();
        pendingOrganization.setCurationStatus(CurationStatus.PENDING);
        List<Organization> candidates = Lists.newArrayList(activeOrganization, pendingOrganization);
        CurationDataset curationDataset = beanSpy.getOrganizationsToBeCurated(candidates);
        verify(beanSpy).refreshIfStale(pendingOrganization);
        verify(beanSpy).refreshIfStale(activeOrganization);
        assertEquals(2, curationDataset.getCsvHeaders().size());
        assertEquals(2, curationDataset.getDisplayHeaderKeys().size());
        assertEquals(1, curationDataset.getRows().size());
        List<String> row = curationDataset.getRows().get(0);
        String expectedExternalId = getExtension(pendingOrganization.getExternalId());
        assertEquals(expectedExternalId, row.get(0));
        assertEquals(pendingOrganization.getName(), row.get(1));
    }

    private String getExtension(String externalId) {
        return new NesId(externalId).getExtension();
    }

    @Test
    public void testGetRolesToBeCurated() {
        AbstractOrganizationRole pendingLab = createLab(CurationStatus.PENDING);
        AbstractOrganizationRole activeLab = createLab(CurationStatus.ACTIVE);
        NesOrganizationServiceBean beanSpy = spy(bean);
        List<AbstractOrganizationRole> candidates = Lists.newArrayList(activeLab, pendingLab);
        CurationDataset curationDataset = beanSpy.getRolesToBeCurated(candidates);
        verify(beanSpy).refreshIfStale(pendingLab.getOrganization());
        verify(beanSpy).refreshIfStale(activeLab.getOrganization());
        assertEquals(4, curationDataset.getCsvHeaders().size());
        assertEquals(4, curationDataset.getDisplayHeaderKeys().size());
        assertEquals(1, curationDataset.getRows().size());
        List<String> row = curationDataset.getRows().get(0);
        String expectedExternalId = getExtension(pendingLab.getExternalId());
        HealthCareFacilityData externalData = (HealthCareFacilityData) pendingLab.getOrganization().getExternalData();
        String expectedPlayerId = getExtension(externalData.getPlayerId());
        assertEquals(expectedExternalId, row.get(0));
        assertEquals(NesIIRoot.HEALTH_CARE_FACILITY.getDisplay(), row.get(1));
        assertEquals(expectedPlayerId, row.get(2));
        assertEquals(pendingLab.getOrganization().getName(), row.get(3));
    }

    private ClinicalLaboratory createLab(CurationStatus status) {
        Organization organization = OrganizationFactory.getInstance().create();
        HealthCareFacilityData externalData = new HealthCareFacilityData();
        externalData.setExternalId(new NesId(NesIIRoot.HEALTH_CARE_FACILITY, getUniqueString()).toString());
        externalData.setPlayerId(new NesId(NesIIRoot.ORGANIZATION, getUniqueString()).toString());
        externalData.setLastNesRefresh(new Date());
        organization.setExternalData(externalData);
        organization.setCurationStatus(status);
        ClinicalLaboratory lab = new ClinicalLaboratory();
        organization.addRole(lab);
        return lab;
    }

}
