/*
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

package gov.nih.nci.firebird.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import gov.nih.nci.firebird.cagrid.UserSessionInformationFactory;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.security.SimplePrincipal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fiveamsolutions.nci.commons.util.HibernateHelper;
import com.google.inject.Injector;

public class FirebirdAuthenticationLoginModuleTest {

    private static final String TEST_USERNAME = "username";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_PROVIDER_URL = "http://provider";
    private static final String TEST_FULLY_QUALIFIED_USERNAME = "/fully/qualified/username";

    private FirebirdAuthenticationLoginModule loginModule = new FirebirdAuthenticationLoginModule() {
        Injector getInjector() {
            return mock(Injector.class);
        }
    };

    private CallbackHandler testCallbackHandler;
    private Subject subject = new Subject();

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpSession mockSession = mock(HttpSession.class);

    private UserSessionInformation mockSessionInformation = UserSessionInformationFactory.getInstance().create(
            TEST_FULLY_QUALIFIED_USERNAME);

    @Mock
    private CredentialsHandlerFactory mockCredentialsHandlerFactory;

    @Mock
    private Authenticator mockAuthenticator;

    @Mock
    private RoleHandler mockAuthorizer;

    @Mock
    private HibernateHelper mockHibernateHelper;

    @SuppressWarnings({ "rawtypes" })
    @Before
    public void setUp() throws LoginException {
        MockitoAnnotations.initMocks(this);
        setUpHttpServletRequest();
        setUpCallbackHandler();
        setUpAuthentication();
        loginModule.initialize(subject, testCallbackHandler, new HashMap(), new HashMap());
        loginModule.setHibernateHelper(mockHibernateHelper);
    }

    private void setUpHttpServletRequest() {
        loginModule.setRequest(mockRequest);
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getParameter(FirebirdAuthenticationLoginModule.IDP_URL_PARAM)).thenReturn(TEST_PROVIDER_URL);
    }

    private void setUpCallbackHandler() {
        testCallbackHandler = new CallbackHandler() {
            @Override
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                ((NameCallback) callbacks[0]).setName(TEST_USERNAME);
                ((PasswordCallback) callbacks[1]).setPassword(TEST_PASSWORD.toCharArray());
            }
        };
    }

    private void setUpAuthentication() throws LoginException {
        loginModule.setAuthenticatorFactory(mockCredentialsHandlerFactory);
        when(mockCredentialsHandlerFactory.getAuthenticator(anyString())).thenReturn(mockAuthenticator);
        when(mockCredentialsHandlerFactory.getRoleHandler(anyString())).thenReturn(mockAuthorizer);
        when(mockAuthenticator.authenticateUser(TEST_USERNAME, TEST_PASSWORD, TEST_PROVIDER_URL)).thenReturn(
                mockSessionInformation);
    }

    @Test
    public void testLogin() throws LoginException {
        assertTrue(loginModule.login());
        verify(mockAuthenticator).authenticateUser(TEST_USERNAME, TEST_PASSWORD, TEST_PROVIDER_URL);
        verify(mockAuthorizer).handleRoles(any(UserSessionInformation.class));
        verify(mockHibernateHelper).openAndBindSession();
        verify(mockHibernateHelper).unbindAndCleanupSession();
    }

    @Test
    public void testCommit() throws LoginException {
        loginModule.login();
        loginModule.commit();
        checkUserPrincipal();
    }

    private void checkUserPrincipal() {
        assertEquals(TEST_FULLY_QUALIFIED_USERNAME, getUserPrincipal().getName());
    }

    private Principal getUserPrincipal() {
        for (Object principal : subject.getPrincipals()) {
            if (SimplePrincipal.class.equals(principal.getClass())) {
                return (Principal) principal;
            }
        }
        return null;
    }

}
