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
package gov.nih.nci.firebird.cagrid;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import java.rmi.RemoteException;
import java.util.List;

import gov.nih.nci.firebird.security.UserSessionInformation;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.authentication.faults.AuthenticationProviderFault;
import org.cagrid.gaards.authentication.faults.CredentialNotSupportedFault;
import org.cagrid.gaards.authentication.faults.InsufficientAttributeFault;
import org.cagrid.gaards.authentication.faults.InvalidCredentialFault;
import org.cagrid.gaards.dorian.client.GridUserClient;
import org.cagrid.gaards.dorian.common.DorianFault;
import org.cagrid.gaards.dorian.federation.GridUserRecord;
import org.cagrid.gaards.dorian.federation.GridUserSearchCriteria;
import org.cagrid.gaards.dorian.federation.TrustedIdentityProvider;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.dorian.stubs.types.UserPolicyFault;
import org.globus.gsi.GlobusCredential;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class GridAuthenticationServiceBeanTest {

    private static final String TEST_REMOTE_PROVIDER_NAME = "remote";
    private static final String TEST_LOCAL_PROVIDER_NAME = "local";
    private static final String TEST_DISALLOWED_PROVIDER_NAME1 = "disallowed1";
    private static final String TEST_DISALLOWED_PROVIDER_NAME2 = "disallowed2";
    private static final String TEST_FULLY_QUALIFIED_USERNAME = "/fully/qualified/username";
    private static final String TEST_USERNAME = "username";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_PROVIDER_URL = "http://url";
    private static final String TEST_DISALLOWED_IDPS = TEST_DISALLOWED_PROVIDER_NAME1 + "," + TEST_DISALLOWED_PROVIDER_NAME2;
    private static final String TEST_EMAIL_ADDRESS = "address@example.com";
    private static final String TEST_FIRST_NAME = "first";
    private static final String TEST_LAST_NAME = "last";

    private GridUserClient mockDorianClient = mock(GridUserClient.class);
    private GlobusCredential mockCredential = mock(GlobusCredential.class);
    private GridCredentialService mockCredentialService = mock(GridCredentialService.class);
    private GridAuthenticationServiceBean bean =
            new GridAuthenticationServiceBean(TEST_DISALLOWED_IDPS, mockCredentialService, mockDorianClient);

    @Before
    public void setUp() throws ResourcePropertyRetrievalException, MalformedURIException, RemoteException {
        setUpTestProviders();
        setUpAuthentication();
    }

    private void setUpTestProviders() throws ResourcePropertyRetrievalException {
        List<TrustedIdentityProvider> providers = Lists.newArrayList();
        providers.add(createIdentityProvider(TEST_REMOTE_PROVIDER_NAME));
        providers.add(createIdentityProvider(TEST_LOCAL_PROVIDER_NAME));
        providers.add(createIdentityProvider(TEST_DISALLOWED_PROVIDER_NAME1));
        providers.add(createIdentityProvider(TEST_DISALLOWED_PROVIDER_NAME2));
        when(mockDorianClient.getTrustedIdentityProviders()).thenReturn(providers);
    }

    private TrustedIdentityProvider createIdentityProvider(String name) {
        TrustedIdentityProvider provider = new TrustedIdentityProvider();
        provider.setAuthenticationServiceURL(TEST_PROVIDER_URL);
        provider.setDisplayName(name);
        return provider;
    }

    private void setUpAuthentication() throws MalformedURIException, RemoteException, CredentialNotSupportedFault,
            InvalidCredentialFault, InsufficientAttributeFault, AuthenticationProviderFault, DorianFault,
            DorianInternalFault, InvalidAssertionFault, UserPolicyFault, PermissionDeniedFault {
        when(mockCredentialService.getCredential(TEST_USERNAME, TEST_PASSWORD, TEST_PROVIDER_URL)).thenReturn(mockCredential);
        when(mockCredential.getIdentity()).thenReturn(TEST_FULLY_QUALIFIED_USERNAME);
    }

    @Test
    public void testGetIdentityProviders() throws ResourcePropertyRetrievalException {
        assertEquals(2, bean.getIdentityProviders().size());
        assertEquals(TEST_REMOTE_PROVIDER_NAME, bean.getIdentityProviders().get(0).getDisplayName());
        assertEquals(TEST_LOCAL_PROVIDER_NAME, bean.getIdentityProviders().get(1).getDisplayName());
    }

    @Test
    public void testAuthenticateUser() throws GridInvocationException, RemoteException {
        GridUserRecord userRecord = new GridUserRecord(TEST_EMAIL_ADDRESS, TEST_FIRST_NAME, TEST_FULLY_QUALIFIED_USERNAME, TEST_LAST_NAME);
        when(mockDorianClient.userSearch(any(GridUserSearchCriteria.class))).thenReturn(Lists.newArrayList(userRecord));
        UserSessionInformation sessionInformation = bean.authenticateUser(TEST_USERNAME, TEST_PASSWORD, TEST_PROVIDER_URL);
        verify(mockCredentialService).getCredential(TEST_USERNAME, TEST_PASSWORD, TEST_PROVIDER_URL);
        assertNotNull(sessionInformation);
        assertEquals(TEST_PROVIDER_URL, sessionInformation.getIdentityProviderUrl());
        assertEquals(TEST_FULLY_QUALIFIED_USERNAME, sessionInformation.getFullyQualifiedUsername());
        assertEquals(TEST_FIRST_NAME, sessionInformation.getAccount().getFirstName());
        assertEquals(TEST_LAST_NAME, sessionInformation.getAccount().getLastName());
        assertEquals(TEST_EMAIL_ADDRESS, sessionInformation.getAccount().getEmailAddress());
    }

    @Test(expected = IllegalStateException.class)
    public void testAuthenticateUser_DisallowedProvider() throws GridInvocationException {
        bean.authenticateUser(TEST_USERNAME, TEST_PASSWORD, "http://disallowed");
    }

}
