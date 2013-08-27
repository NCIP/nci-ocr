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
import static org.mockito.Mockito.*;
import gov.nih.nci.coppa.po.StringMap;
import gov.nih.nci.coppa.po.faults.NullifiedEntityFault;
import gov.nih.nci.coppa.po.faults.SimpleIIMapType;
import gov.nih.nci.coppa.po.faults.SimpleIIMapTypeEntry;
import gov.nih.nci.coppa.services.entities.organization.common.OrganizationI;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.NesId;
import gov.nih.nci.firebird.nes.common.ReplacedEntityException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.nes.common.ValidationErrorTranslator;
import gov.nih.nci.firebird.test.OrganizationFactory;

import java.rmi.RemoteException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

public class AbstractOrganizationIntegrationServiceBeanTest {
    
    @Mock
    public static OrganizationI mockOrganizationService;
    @Mock
    private ValidationErrorTranslator mockErrorTranslator;
    @Mock
    private IdentifiedOrganizationIntegrationService mockIdentifiedOrganizationService;
    private TestBean beanSpy;
    private Organization organization = OrganizationFactory.getInstance().createWithoutNesData();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        beanSpy = spy(new TestBean());
    }
    
    @Test
    public void testCreate() throws RemoteException {
        OrganizationCreator creator = mock(OrganizationCreator.class);
        when(creator.create(organization)).thenReturn(TEST_NES_ID.toId());
        beanSpy.create(organization, creator);
        assertEquals(TEST_NES_ID_STRING, organization.getNesId());
        assertNotNull(organization.getLastNesRefresh());
        assertEquals(CurationStatus.PENDING, organization.getNesStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreate_RemoteException() throws RemoteException {
        OrganizationCreator creator = mock(OrganizationCreator.class);
        when(creator.create(organization)).thenThrow(new RemoteException());
        beanSpy.create(organization, creator);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreate_ExistingOrganization() {
        organization.setNesId(TEST_NES_ID_STRING);
        beanSpy.create(organization, null);
    }

    @Test
    public void testGetById() throws UnavailableEntityException, ReplacedEntityException {
        assertEquals(organization, beanSpy.getById(TEST_NES_ID_STRING));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetById_NotFound() throws Exception {
        when(beanSpy.getOrganization(TEST_NES_ID)).thenReturn(null);
        beanSpy.getById(TEST_NES_ID_STRING);
    }

    @Test(expected = RuntimeException.class)
    public void testGetById_RemoteException() throws Exception {
        when(beanSpy.getOrganization(TEST_NES_ID)).thenThrow(new RemoteException());
        beanSpy.getById(TEST_NES_ID_STRING);
    }

    @Test
    public void testGetById_NullifiedEntityFaultForDuplicate() throws NullifiedEntityFault, RemoteException, UnavailableEntityException {
        String nullifiedIdExtension = "1";
        String nullifiedId = TEST_ROOT + ":" + nullifiedIdExtension;
        String replacementIdExtension = "2";
        String replacementId = NesIIRoot.ORGANIZATION.getRoot() + ":" + replacementIdExtension;
        final NullifiedEntityFault fault = createNullifiedEntityFault(nullifiedIdExtension, replacementIdExtension);
        organization.setNesId(nullifiedId);
        when(beanSpy.getOrganization(any(NesId.class))).thenThrow(fault);
        try {
            beanSpy.getById(nullifiedId);
            fail("Failure expected");
        } catch (ReplacedEntityException e) {
            assertEquals(nullifiedId, e.getOriginalNesId());
            assertEquals(replacementId, e.getReplacmentNesId());
        }
    }

    private NullifiedEntityFault createNullifiedEntityFault(String nullifiedIdExtension, String replacementIdExtension) {
        NullifiedEntityFault fault = new NullifiedEntityFault();
        SimpleIIMapTypeEntry[] entries = new SimpleIIMapTypeEntry[1];
        entries[0] = new SimpleIIMapTypeEntry(nullifiedIdExtension, replacementIdExtension);
        SimpleIIMapType nullifiedEntries = new SimpleIIMapType();
        nullifiedEntries.setEntry(entries);
        fault.setNullifiedEntries(nullifiedEntries);
        return fault;
    }

    @Test(expected = UnavailableEntityException.class)
    public void testGetById_NullifiedEntityFaultWithNoSubstitute() throws RemoteException, UnavailableEntityException, ReplacedEntityException {
        String nullifiedIdExtension = "1";
        String nullifiedId = TEST_ROOT + ":" + nullifiedIdExtension;
        NullifiedEntityFault fault = createNullifiedEntityFault(nullifiedIdExtension, null);
        when(beanSpy.getOrganization(any(NesId.class))).thenThrow(fault);
        beanSpy.getById(nullifiedId);
    }

    @Test(expected = UnavailableEntityException.class)
    public void testGetById_NullifiedEntityWithSameReplacementId() throws UnavailableEntityException, ReplacedEntityException, RemoteException {
        String nullifiedIdExtension = "1";
        String nullifiedId = TEST_ROOT + ":" + nullifiedIdExtension;
        String replacementIdExtension = "1";
        NullifiedEntityFault fault = createNullifiedEntityFault(nullifiedIdExtension, replacementIdExtension);
        when(beanSpy.getOrganization(any(NesId.class))).thenThrow(fault);
        beanSpy.getById(nullifiedId);
    }

    @Test
    public void testPerformSearch() throws RemoteException {
        OrganizationSearcher search = mock(OrganizationSearcher.class);
        when(search.search()).thenReturn(Lists.newArrayList(organization));
        List<Organization> organizations = beanSpy.performSearch(search);
        checkExpectedOrganizationResults(organizations);
    }

    @Test(expected = IllegalStateException.class)
    public void testSearchByName_RemoteException() throws RemoteException {
        OrganizationSearcher search = mock(OrganizationSearcher.class);
        when(search.search()).thenThrow(new RemoteException());
        beanSpy.performSearch(search);
    }

    private void checkExpectedOrganizationResults(List<Organization> organizations) {
        assertEquals(1, organizations.size());
        assertEquals(organization, organizations.get(0));
    }
    
    @Test
    public void testValidate() throws ValidationException {
        when(mockErrorTranslator.translateStringMapValidation(any(StringMap.class))).thenReturn(new ValidationResult());
        beanSpy.validate(organization);
    }

    @Test(expected = ValidationException.class)
    public void testValidate_HasErrors() throws ValidationException {
        ValidationResult failingResult = new ValidationResult();
        failingResult.addFailure(new ValidationFailure(""));
        when(mockErrorTranslator.translateStringMapValidation(any(StringMap.class))).thenReturn(failingResult);
        beanSpy.validate(organization);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidate_RemoteException() throws ValidationException, RemoteException {
        doThrow(new RemoteException()).when(beanSpy).getValidationResults(organization);
        beanSpy.validate(organization);
    }
    
    @Test
    public void testRefresh() throws UnavailableEntityException, ReplacedEntityException {
        Organization organizationToRefresh = OrganizationFactory.getInstance().create();
        Organization updatedOrganization = OrganizationFactory.getInstance().create();
        when(beanSpy.getById(organizationToRefresh.getNesId())).thenReturn(updatedOrganization);
        beanSpy.refresh(organizationToRefresh);
        assertEquals(updatedOrganization, organizationToRefresh);
    }
    
    @Test
    public void testRefresh_Nullified() throws UnavailableEntityException, ReplacedEntityException {
        Organization organizationToRefresh = OrganizationFactory.getInstance().create();
        when(beanSpy.getById(organizationToRefresh.getNesId())).thenThrow(new UnavailableEntityException(null, null));
        beanSpy.refresh(organizationToRefresh);
        assertEquals(CurationStatus.NULLIFIED, organizationToRefresh.getNesStatus());
    }

    @Test
    public void testRefresh_Replaced() throws UnavailableEntityException, ReplacedEntityException {
        final Organization organizationToRefresh = OrganizationFactory.getInstance().create();
        final Organization replacingOrganization = OrganizationFactory.getInstance().create();
        final String originalNesId = organizationToRefresh.getNesId();
        final String replacementNesId = replacingOrganization.getNesId();
        ReplacedEntityException replacedEntityException = new ReplacedEntityException(null, originalNesId, replacementNesId);
        when(beanSpy.getById(originalNesId)).thenThrow(replacedEntityException);
        when(beanSpy.getById(replacementNesId)).thenReturn(replacingOrganization);
        beanSpy.refresh(organizationToRefresh);
        assertEquals(replacingOrganization, organizationToRefresh);
    }

    private class TestBean extends AbstractOrganizationIntegrationServiceBean {

        TestBean() {
            super(mockOrganizationService, mockIdentifiedOrganizationService, mockErrorTranslator);
        }

        @Override
        Organization getOrganization(NesId nesId) throws RemoteException {
            return organization;
        }
        
        @Override
        NesIIRoot getNesIIRoot() {
            return NesIIRoot.ORGANIZATION;
        }

        @Override
        StringMap getValidationResults(Organization organization) throws RemoteException {
            return new StringMap();
        }

    }
    
}
