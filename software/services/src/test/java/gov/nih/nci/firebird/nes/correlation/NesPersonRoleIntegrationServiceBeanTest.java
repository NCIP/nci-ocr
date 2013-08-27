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
package gov.nih.nci.firebird.nes.correlation;

import static gov.nih.nci.firebird.nes.correlation.PersonRoleType.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.coppa.po.HealthCareProvider;
import gov.nih.nci.coppa.po.OrganizationalContact;
import gov.nih.nci.coppa.po.faults.EntityValidationFault;
import gov.nih.nci.coppa.services.structuralroles.healthcareprovider.client.HealthCareProviderClient;
import gov.nih.nci.coppa.services.structuralroles.healthcareprovider.common.HealthCareProviderI;
import gov.nih.nci.coppa.services.structuralroles.organizationalcontact.common.OrganizationalContactI;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.NesId;
import gov.nih.nci.firebird.nes.NesIdTestUtil;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.iso21090.extensions.Id;

import java.rmi.RemoteException;

import org.iso._21090.DSETII;
import org.iso._21090.II;
import org.junit.Before;
import org.junit.Test;

public class NesPersonRoleIntegrationServiceBeanTest {

    private static final String TEST_EXTENSION = "12345";
    private HealthCareProviderI mockProviderClient = mock(HealthCareProviderClient.class);
    private OrganizationalContactI mockOrganizationalContactClient = mock(OrganizationalContactI.class);
    private NesPersonRoleIntegrationServiceBean service = new NesPersonRoleIntegrationServiceBean(mockProviderClient,
            mockOrganizationalContactClient);
    private Organization organization = OrganizationFactory.getInstance().create();
    private Person person = PersonFactory.getInstance().create();

    @Before
    public void setUp() {
        organization.setPlayerIdentifier(NesIdTestUtil.TEST_NES_ID_STRING);
    }

    @Test
    public void testEnsureRoleExistsNew() throws RemoteException, ValidationException {
        HealthCareProvider[] providers = new HealthCareProvider[0];
        when(mockProviderClient.getByPlayerIds(any(Id[].class))).thenReturn(providers);
        Id id = new Id();
        id.setRoot(NesIIRoot.HEALTH_CARE_PROVIDER.getRoot());
        id.setExtension(TEST_EXTENSION);
        when(mockProviderClient.create(any(HealthCareProvider.class))).thenReturn(id);
        assertEquals(id.getExtension(), service.ensureCorrelated(person, organization, HEALTH_CARE_PROVIDER));
        verify(mockProviderClient).getByPlayerIds(any(Id[].class));
        verify(mockProviderClient).create(any(HealthCareProvider.class));

        when(mockProviderClient.getByPlayerIds(any(Id[].class))).thenReturn(null);
        assertEquals(id.getExtension(), service.ensureCorrelated(person, organization, HEALTH_CARE_PROVIDER));
        verify(mockProviderClient, times(2)).getByPlayerIds(any(Id[].class));
        verify(mockProviderClient, times(2)).create(any(HealthCareProvider.class));

        when(mockOrganizationalContactClient.create(any(OrganizationalContact.class))).thenReturn(id);
        assertEquals(id.getExtension(), service.ensureCorrelated(person, organization, ORGANIZATIONAL_CONTACT));
        verify(mockOrganizationalContactClient).getByPlayerIds(any(Id[].class));
        verify(mockOrganizationalContactClient).create(any(OrganizationalContact.class));
    }

    @Test
    public void testEnsureRoleCorrelated_ExistingHealthCareProvider() throws RemoteException, ValidationException {
        String externalOrganizationRoot = NesIIRoot.CTEP.getRoot();
        HealthCareProvider provider = new HealthCareProvider();
        DSETII dsetii = new DSETII();
        II externalIi = new II();
        externalIi.setRoot(externalOrganizationRoot);
        externalIi.setExtension(TEST_EXTENSION);
        dsetii.getItem().add(externalIi);
        II ii = new II();
        ii.setRoot(NesIIRoot.HEALTH_CARE_PROVIDER.getRoot());
        ii.setExtension(TEST_EXTENSION);
        dsetii.getItem().add(ii);
        provider.setIdentifier(dsetii);
        II scoperIi = new NesId(organization.getPlayerIdentifier()).toIi();
        provider.setScoperIdentifier(scoperIi);
        HealthCareProvider wrongOrgProvider = new HealthCareProvider();
        II wrongOrgScoperIi = new II();
        wrongOrgScoperIi.setRoot(externalOrganizationRoot);
        wrongOrgScoperIi.setExtension(TEST_EXTENSION);
        wrongOrgProvider.setScoperIdentifier(wrongOrgScoperIi);
        wrongOrgProvider.setIdentifier(dsetii);
        HealthCareProvider[] providers = new HealthCareProvider[] { wrongOrgProvider, provider };
        when(mockProviderClient.getByPlayerIds(any(Id[].class))).thenReturn(providers);
        assertEquals(TEST_EXTENSION, service.ensureCorrelated(person, organization, HEALTH_CARE_PROVIDER));
        verify(mockProviderClient, never()).create(any(HealthCareProvider.class));
    }

    @Test
    public void testEnsureRoleCorrelated_ExistingOrganizationalContact() throws RemoteException, ValidationException {
        OrganizationalContact contact = new OrganizationalContact();
        DSETII dsetii = new DSETII();
        II ii = new II();
        ii.setRoot(NesIIRoot.ORGANIZATIONAL_CONTACT.getRoot());
        ii.setExtension(TEST_EXTENSION);
        dsetii.getItem().add(ii);
        contact.setIdentifier(dsetii);
        II scoperIi = new NesId(organization.getPlayerIdentifier()).toIi();
        contact.setScoperIdentifier(scoperIi);
        OrganizationalContact[] contacts = new OrganizationalContact[] { contact };
        when(mockOrganizationalContactClient.getByPlayerIds(any(Id[].class))).thenReturn(contacts);
        assertEquals(TEST_EXTENSION, service.ensureCorrelated(person, organization, ORGANIZATIONAL_CONTACT));
        verify(mockOrganizationalContactClient, never()).create(any(OrganizationalContact.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnsureRoleExistsNoNciRootFound() throws RemoteException, ValidationException {
        HealthCareProvider provider = new HealthCareProvider();
        provider.setIdentifier(new DSETII());
        II scoperIi = new NesId(organization.getPlayerIdentifier()).toIi();
        provider.setScoperIdentifier(scoperIi);
        HealthCareProvider[] providers = new HealthCareProvider[] { provider };
        when(mockProviderClient.getByPlayerIds(any(Id[].class))).thenReturn(providers);
        service.ensureCorrelated(person, organization, HEALTH_CARE_PROVIDER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnsureRoleExistsInvalidPersonRole() throws RemoteException, ValidationException {
        HealthCareProvider provider = new HealthCareProvider();
        provider.setIdentifier(new DSETII());
        II scoperIi = new II();
        scoperIi.setExtension(organization.getNesId());
        provider.setScoperIdentifier(scoperIi);
        HealthCareProvider[] providers = new HealthCareProvider[] { provider };
        when(mockProviderClient.getByPlayerIds(any(Id[].class))).thenReturn(providers);
        service.ensureCorrelated(person, organization, CLINICAL_RESEARCH_STAFF);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnsureRoleExistsUnsupportedType() throws RemoteException, ValidationException {
        service.ensureCorrelated(person, organization, PersonRoleType.PATIENT);
    }

    @Test(expected = RuntimeException.class)
    public void testEnsureRoleExistsEntityValidationFault() throws RemoteException, ValidationException {
        when(mockProviderClient.getByPlayerIds(any(Id[].class))).thenThrow(new EntityValidationFault());
        service.ensureCorrelated(person, organization, HEALTH_CARE_PROVIDER);
    }

    @Test(expected = RuntimeException.class)
    public void testEnsureRoleExistsRemoteException() throws RemoteException, ValidationException {
        when(mockProviderClient.getByPlayerIds(any(Id[].class))).thenThrow(new RemoteException());
        service.ensureCorrelated(person, organization, HEALTH_CARE_PROVIDER);
    }

}
