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

import static gov.nih.nci.firebird.data.OrganizationRoleType.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractOrganizationRole;
import gov.nih.nci.firebird.data.CurationDataset;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.organization.external.ExternalOrganizationService;
import gov.nih.nci.firebird.service.organization.local.LocalOrganizationDataService;
import gov.nih.nci.firebird.test.OrganizationFactory;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

public class OrganizationServiceBeanTest {

    private static final String SEARCH_TERM = "term";

    private static final String EXTERNAL_ID = "externalId";

    private OrganizationServiceBean bean = new OrganizationServiceBean();
    private Organization organization = OrganizationFactory.getInstance().create();

    @Mock
    private ExternalOrganizationService mockExternalOrganizationService;

    @Mock
    private LocalOrganizationDataService mockLocalOrganizationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bean.setExternalOrganizationService(mockExternalOrganizationService);
        bean.setLocalOrganizationDataService(mockLocalOrganizationService);
    }

    @Test
    public void testSearch_ExternalResultsOnly() {
        List<Organization> externalResults = Lists.newArrayList(organization);
        when(mockExternalOrganizationService.search(SEARCH_TERM, GENERIC_ORGANIZATION)).thenReturn(externalResults);
        List<Organization> results = bean.search(SEARCH_TERM, GENERIC_ORGANIZATION);
        assertEquals(1, results.size());
        assertEquals(organization, results.get(0));
    }

    @Test
    public void testSearch_LocalResultReplacement() {
        Organization organization1External = OrganizationFactory.getInstance().create();
        Organization organization2External = OrganizationFactory.getInstance().create();
        Organization organization2Local = OrganizationFactory.getInstance().create();
        List<Organization> externalResults = Lists.newArrayList(organization1External, organization2External);
        when(mockExternalOrganizationService.search(SEARCH_TERM, GENERIC_ORGANIZATION)).thenReturn(externalResults);
        when(mockLocalOrganizationService.getByExternalId(organization2External.getExternalId())).thenReturn(
                organization2Local);
        List<Organization> results = bean.search(SEARCH_TERM, GENERIC_ORGANIZATION);
        assertEquals(2, results.size());
        assertTrue(results.contains(organization1External));
        assertTrue(results.contains(organization2Local));
    }

    @Test
    public void testSearch_ResultsAreSorted() {
        Organization organization1 = OrganizationFactory.getInstance().create();
        organization1.setName("Aaaaa");
        Organization organization2 = OrganizationFactory.getInstance().create();
        organization2.setName("Zzzzz");
        List<Organization> externalResults = Lists.newArrayList(organization2, organization1);
        when(mockExternalOrganizationService.search(SEARCH_TERM, GENERIC_ORGANIZATION)).thenReturn(externalResults);
        List<Organization> results = bean.search(SEARCH_TERM, GENERIC_ORGANIZATION);
        assertEquals(2, results.size());
        assertEquals(organization1, results.get(0));
        assertEquals(organization2, results.get(1));
    }

    @Test
    public void testGetByAlternateIdentifier() {
        String alternateIdentifier = "id";
        when(mockExternalOrganizationService.getByAlternateIdentifier(alternateIdentifier, GENERIC_ORGANIZATION))
                .thenReturn(Lists.newArrayList(organization));
        assertEquals(Lists.newArrayList(organization),
                bean.getByAlternateIdentifier(alternateIdentifier, GENERIC_ORGANIZATION));
        verify(mockLocalOrganizationService).save(organization);
    }

    @Test
    public void testGetByAlternateIdentifier_NoResult() {
        String alternateIdentifier = "id";
        List<Organization> emptyList = Collections.emptyList();
        when(mockExternalOrganizationService.getByAlternateIdentifier(alternateIdentifier, GENERIC_ORGANIZATION))
                .thenReturn(emptyList);
        assertEquals(emptyList, bean.getByAlternateIdentifier(alternateIdentifier, GENERIC_ORGANIZATION));
        verifyZeroInteractions(mockLocalOrganizationService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearch_EmptyTerm() {
        assertNull(bean.search("  ", GENERIC_ORGANIZATION));
    }

    @Test(expected = NullPointerException.class)
    public void testSearch_NullType() {
        assertNull(bean.search(SEARCH_TERM, null));
    }

    @Test
    public void testGetByExternalId_LocalRecord() throws InvalidatedOrganizationException {
        when(mockLocalOrganizationService.getByExternalId(EXTERNAL_ID)).thenReturn(organization);
        Organization result = bean.getByExternalId(EXTERNAL_ID);
        assertEquals(organization, result);
        verify(mockLocalOrganizationService, times(0)).save(organization);
    }

    @Test
    public void testGetByExternalId_NoLocalRecord() throws InvalidatedOrganizationException {
        when(mockExternalOrganizationService.getByExternalId(EXTERNAL_ID)).thenReturn(organization);
        Organization result = bean.getByExternalId(EXTERNAL_ID);
        assertEquals(organization, result);
        verify(mockLocalOrganizationService).save(result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetByExternalId_NullExternalId() throws InvalidatedOrganizationException {
        bean.getByExternalId(null);
    }

    @Test
    public void testValidate() throws ValidationException {
        Object args = null;
        bean.validate(organization, IRB, args);
        verify(mockExternalOrganizationService).validate(organization, IRB, args);
    }

    @Test(expected = NullPointerException.class)
    public void testValidate_NullOrganization() throws ValidationException {
        bean.validate(null, IRB);
    }

    @Test(expected = NullPointerException.class)
    public void testValidate_NullType() throws ValidationException {
        bean.validate(organization, null);
    }

    @Test
    public void testCreate() throws ValidationException {
        bean.create(organization, GENERIC_ORGANIZATION);
        verify(mockExternalOrganizationService).create(organization, GENERIC_ORGANIZATION);
        verify(mockLocalOrganizationService).save(organization);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_NullArguments() throws ValidationException {
        bean.create(null, null);
    }

    @Test
    public void testRefreshNow() {
        bean.refreshNow(organization);
        verify(mockExternalOrganizationService).refreshNow(organization);
        verify(mockLocalOrganizationService).save(organization);
    }

    @Test(expected = NullPointerException.class)
    public void testRefreshNow_NullArgument() {
        bean.refreshNow(null);
    }

    @Test
    public void testRefreshIfStale() {
        bean.refreshIfStale(organization);
        verify(mockExternalOrganizationService).refreshIfStale(organization);
        verify(mockLocalOrganizationService).save(organization);
    }

    @Test
    public void testGetPracticeSiteType() {
        bean.getPracticeSiteType(organization);
        verify(mockExternalOrganizationService).getPracticeSiteType(organization);
    }

    @Test
    public void testGetPrimaryOrganizationType() {
        bean.getPrimaryOrganizationType(organization);
        verify(mockExternalOrganizationService).getPrimaryOrganizationType(organization);
    }
    
    @Test
    public void testGetOrganizationsToBeCurated() {
        List<Organization> candidates = Lists.newArrayList();
        when(mockLocalOrganizationService.getCandidateOrganizationsToBeCurated()).thenReturn(candidates);
        CurationDataset mockResult = mock(CurationDataset.class);
        when(mockExternalOrganizationService.getOrganizationsToBeCurated(candidates)).thenReturn(mockResult);
        assertEquals(mockResult, bean.getOrganizationsToBeCurated());
    }

    @Test
    public void testGetRolesToBeCurated() {
        List<AbstractOrganizationRole> candidates = Lists.newArrayList();
        when(mockLocalOrganizationService.getCandidateRolesToBeCurated()).thenReturn(candidates);
        CurationDataset mockResult = mock(CurationDataset.class);
        when(mockExternalOrganizationService.getRolesToBeCurated(candidates)).thenReturn(mockResult);
        assertEquals(mockResult, bean.getRolesToBeCurated());
    }

}
