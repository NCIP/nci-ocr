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

import static gov.nih.nci.firebird.nes.NesIdTestUtil.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.rmi.RemoteException;
import java.util.List;

import gov.nih.nci.coppa.common.LimitOffset;
import gov.nih.nci.coppa.po.CorrelationNode;
import gov.nih.nci.coppa.po.OversightCommittee;
import gov.nih.nci.coppa.po.StringMap;
import gov.nih.nci.coppa.services.business.business.common.BusinessI;
import gov.nih.nci.coppa.services.entities.organization.common.OrganizationI;
import gov.nih.nci.coppa.services.structuralroles.oversightcommittee.common.OversightCommitteeI;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.NesId;
import gov.nih.nci.firebird.nes.common.ValidationErrorTranslator;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.iso21090.extensions.Id;
import gov.nih.nci.iso21090.extensions.Bl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OversightCommitteeIntegrationServiceBeanTest {

    private static final OversightCommitteeType TYPE = OversightCommitteeType.INSTITUTIONAL_REVIEW_BOARD;

    @Mock
    private OversightCommitteeTranslator mockTranslator;
    @Mock
    private Organization mockOrganization;
    @Mock
    private gov.nih.nci.coppa.po.Organization mockNesOrganization;
    @Mock
    private BusinessI mockBusinessService;
    @Mock
    private IdentifiedOrganizationIntegrationService mockIdentifiedOrganizationService;
    @Mock
    private OrganizationI mockOrganizationService;
    @Mock
    private ValidationErrorTranslator mockErrorTranslator;
    @Mock
    private OversightCommitteeI mockResearchOrganizationService;
    private OversightCommitteeIntegrationServiceBean bean;
    private NesOrganizationFactory nesObjectFactory = new NesOrganizationFactory();
    private OversightCommittee oversightCommittee = nesObjectFactory.getTestOversightCommittee(TYPE);

    @Before
    public void setUp() {
        bean = new OversightCommitteeIntegrationServiceBean(mockBusinessService, mockOrganizationService,
                mockIdentifiedOrganizationService, mockErrorTranslator, mockResearchOrganizationService, mockTranslator);
        when(mockTranslator.toOversightCommittee(any(Organization.class), any(OversightCommitteeType.class)))
                .thenReturn(oversightCommittee);
        when(mockTranslator.toNesOrganization(any(Organization.class))).thenReturn(mockNesOrganization);
        when(
                mockTranslator.toFirebirdOrganization(any(OversightCommittee.class),
                        any(gov.nih.nci.coppa.po.Organization.class))).thenReturn(mockOrganization);
    }

    @Test
    public void testCreate() throws Exception {
        Id researchOrganizationId = nesObjectFactory.getTestIdentifier(NesIIRoot.RESEARCH_ORGANIZATION);
        when(mockResearchOrganizationService.create(oversightCommittee)).thenReturn(researchOrganizationId);
        when(mockOrganizationService.create(mockNesOrganization)).thenReturn(TEST_NES_ID.toId());
        Organization organization = OrganizationFactory.getInstance().createWithoutNesData();

        bean.create(organization, TYPE);

        assertEquals(new NesId(researchOrganizationId).toString(), organization.getNesId());
        assertEquals(TEST_NES_ID.toString(), organization.getPlayerIdentifier());
        assertEquals(CurationStatus.PENDING, organization.getNesStatus());
        verify(mockOrganizationService).create(mockNesOrganization);
        verify(mockResearchOrganizationService).create(oversightCommittee);
    }

    @Test
    public void testCreate_ExistingPlayer() throws Exception {
        Id researchOrganizationId = nesObjectFactory.getTestIdentifier(NesIIRoot.RESEARCH_ORGANIZATION);
        when(mockOrganization.getPlayerIdentifier()).thenReturn(TEST_NES_ID_STRING);
        when(mockResearchOrganizationService.create(oversightCommittee)).thenReturn(researchOrganizationId);

        bean.create(mockOrganization, TYPE);
        verify(mockResearchOrganizationService).create(oversightCommittee);
        verifyZeroInteractions(mockOrganizationService);
    }

    @Test
    public void testGetOrganization() throws RemoteException {
        CorrelationNode node = nesObjectFactory.createNode(oversightCommittee, mockNesOrganization);
        when(mockBusinessService.getCorrelationByIdWithEntities(any(Id.class), any(Bl.class), any(Bl.class)))
                .thenReturn(node);
        when(mockOrganization.getPlayerIdentifier()).thenReturn(TEST_NES_ID_STRING);
        Organization organization = bean.getOrganization(TEST_NES_ID);
        assertEquals(mockOrganization, organization);
    }

    @Test
    public void testGetNesIIRoot() {
        assertEquals(NesIIRoot.OVERSIGHT_COMMITTEE, bean.getNesIIRoot());
    }

    @Test
    public void testSearchByName() throws RemoteException {
        CorrelationNode resultNode = nesObjectFactory.createNode(oversightCommittee, mockNesOrganization);
        when(
                mockBusinessService.searchCorrelationsWithEntities(any(CorrelationNode.class), any(Bl.class),
                        any(Bl.class), any(LimitOffset.class))).thenReturn(new CorrelationNode[] { resultNode });
        String searchName = "name";
        List<Organization> nameMatches = bean.searchByName(searchName, TYPE);
        verify(mockTranslator).toNesName(searchName);
        assertEquals(1, nameMatches.size());
        assertEquals(mockOrganization, nameMatches.get(0));
    }

    @Test
    public void testSearchByAssignedIdentifier() {
        bean.searchByAssignedIdentifier(TEST_EXTENSION, TYPE);
        verify(mockIdentifiedOrganizationService).getIdentifiedOrganizations(TEST_EXTENSION);
    }

    @Test
    public void testGetByIdentifiedOrganizationPlayerId() throws RemoteException {
        when(mockResearchOrganizationService.getByPlayerIds(any(Id[].class))).thenReturn(
                new OversightCommittee[] { oversightCommittee });
        List<Organization> organizations = bean.getByIdentifiedOrganizationPlayerId(TEST_NES_ID, TYPE);
        assertEquals(1, organizations.size());
        assertEquals(mockOrganization, organizations.get(0));
    }

    @Test
    public void testGetByIdentifiedOrganizationPlayerId_NoMatch() throws RemoteException {
        List<Organization> organizations = bean.getByIdentifiedOrganizationPlayerId(TEST_NES_ID, TYPE);
        assertTrue(organizations.isEmpty());
    }

    @Test
    public void testGetValidationResults() throws RemoteException {
        when(mockResearchOrganizationService.validate(oversightCommittee)).thenReturn(new StringMap());
        when(mockOrganizationService.validate(mockNesOrganization)).thenReturn(new StringMap());
        bean.getValidationResults(mockOrganization);
        verify(mockResearchOrganizationService).validate(oversightCommittee);
        verify(mockOrganizationService).validate(mockNesOrganization);
    }

}
