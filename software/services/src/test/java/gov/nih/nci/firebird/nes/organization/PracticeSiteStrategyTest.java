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

import static gov.nih.nci.firebird.nes.organization.ResearchOrganizationType.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.PrimaryOrganizationType;
import gov.nih.nci.firebird.test.GuiceTestRunnerWithMocks;
import gov.nih.nci.firebird.test.OrganizationFactory;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

@RunWith(GuiceTestRunnerWithMocks.class)
public class PracticeSiteStrategyTest {

    @Inject
    private PracticeSiteStrategy strategy;

    @Inject
    private ResearchOrganizationIntegrationService mockResearchOrganizationService;
    @Inject
    private HealthCareFacilityIntegrationService mockHealthCareFacilityService;

    @Test
    public void testCreate_CancerCenter() throws Exception {
        Organization organization = new Organization();
        strategy.create(organization, PracticeSiteType.CANCER_CENTER);
        verify(mockResearchOrganizationService).create(organization, ResearchOrganizationType.CANCER_CENTER);
    }

    @Test
    public void testCreate_ClinicalCenter() throws Exception {
        Organization organization = new Organization();
        strategy.create(organization, PracticeSiteType.CLINICAL_CENTER);
        verify(mockResearchOrganizationService).create(organization, ResearchOrganizationType.CLINICAL_CENTER);
    }

    @Test
    public void testCreate_HealthCareFacility() throws Exception {
        Organization organization = new Organization();
        strategy.create(organization, PracticeSiteType.HEALTH_CARE_FACILITY);
        verify(mockHealthCareFacilityService).create(organization);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_NoType() throws Exception {
        Organization organization = new Organization();
        strategy.create(organization);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_MoreThanOne() throws Exception {
        Organization organization = new Organization();
        strategy.create(organization, PracticeSiteType.CANCER_CENTER, PracticeSiteType.CLINICAL_CENTER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_InvalidType() throws Exception {
        Organization organization = new Organization();
        strategy.create(organization, PrimaryOrganizationType.CANCER_CENTER);
    }

    @Test
    public void testSearch() throws Exception {
        String term = "term";
        Organization result1 = OrganizationFactory.getInstance().create();
        Organization result2 = OrganizationFactory.getInstance().create();
        Organization result3 = OrganizationFactory.getInstance().create();
        Organization result4 = OrganizationFactory.getInstance().create();
        Organization result5 = OrganizationFactory.getInstance().create();

        when(mockHealthCareFacilityService.searchByAssignedIdentifier(term)).thenReturn(Lists.newArrayList(result1));
        when(mockHealthCareFacilityService.searchByName(term)).thenReturn(Lists.newArrayList(result2));

        when(mockResearchOrganizationService.searchByName(term, ResearchOrganizationType.CANCER_CENTER)).thenReturn(
                Lists.newArrayList(result3));

        when(mockResearchOrganizationService.searchByName(term, ResearchOrganizationType.CLINICAL_CENTER)).thenReturn(
                Lists.newArrayList(result4));

        when(mockResearchOrganizationService.searchByName(term, ResearchOrganizationType.NCI_CTEP)).thenReturn(
                Lists.newArrayList(result5));

        List<Organization> results = strategy.search(term);
        List<Organization> expectedResults = Lists.newArrayList(result1, result2, result3, result4, result5);
        assertTrue(CollectionUtils.isEqualCollection(expectedResults, results));
    }

    @Test
    public void testGetByAlternateIdentifier() throws Exception {
        String alternateIdentifier = "id";
        Organization organization = new Organization();
        when(mockHealthCareFacilityService.searchByAssignedIdentifier(alternateIdentifier)).thenReturn(
                Lists.newArrayList(organization));
        assertEquals(Lists.newArrayList(organization), strategy.getByAlternateIdentifier(alternateIdentifier));
    }

    @Test
    public void testGetByAlternateIdentifier_MoreThanOneResult() throws Exception {
        String alternateIdentifier = "id";
        Organization organization1 = OrganizationFactory.getInstance().create();
        Organization organization2 = OrganizationFactory.getInstance().create();
        when(mockHealthCareFacilityService.searchByAssignedIdentifier(alternateIdentifier)).thenReturn(
                Lists.newArrayList(organization1));
        when(mockResearchOrganizationService.searchByAssignedIdentifier(alternateIdentifier, CANCER_CENTER))
                .thenReturn(Lists.newArrayList(organization2));
        assertEquals(Lists.newArrayList(organization1, organization2),
                strategy.getByAlternateIdentifier(alternateIdentifier));
    }
    
    @Test
    public void testGetPracticeSiteType_HealthCareFacility() {
        Organization organization = OrganizationFactory.getInstance().create();
        HealthCareFacilityData healthCareFacilityData = new HealthCareFacilityData();
        organization.setExternalData(healthCareFacilityData);
        assertEquals(PracticeSiteType.HEALTH_CARE_FACILITY, strategy.getPracticeSiteType(organization));
    }
    
    @Test
    public void testGetPracticeSiteType_CancerCenter() {
        Organization organization = OrganizationFactory.getInstance().create();
        ResearchOrganizationData researchOrganizationData = new ResearchOrganizationData();
        researchOrganizationData.setResearchOrganizationType(ResearchOrganizationType.CANCER_CENTER);
        organization.setExternalData(researchOrganizationData);
        assertEquals(PracticeSiteType.CANCER_CENTER, strategy.getPracticeSiteType(organization));
    }

    @Test
    public void testGetPracticeSiteType_ClinicalCenter() {
        Organization organization = OrganizationFactory.getInstance().create();
        ResearchOrganizationData researchOrganizationData = new ResearchOrganizationData();
        researchOrganizationData.setResearchOrganizationType(ResearchOrganizationType.CLINICAL_CENTER);
        organization.setExternalData(researchOrganizationData);
        assertEquals(PracticeSiteType.CLINICAL_CENTER, strategy.getPracticeSiteType(organization));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPracticeSiteType_InvalidOrganizationType() {
        Organization organization = OrganizationFactory.getInstance().create();
        strategy.getPracticeSiteType(organization);
    }

}
