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

import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.inject.GuiceInjectorHolder;

import org.apache.log4j.Logger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;

import com.fiveamsolutions.nci.commons.util.HibernateHelper;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import java.util.Set;

/**
 * Abstract Authentication Login Module provides common functionality for authenticating users.
 */
@SuppressWarnings({ "PMD.TooManyMethods" })
public class FirebirdAuthenticationLoginModule implements LoginModule {

    private static final String WEB_REQUEST_KEY = "javax.servlet.http.HttpServletRequest";
    static final String IDP_URL_PARAM = "idpUrl";

    private static final Logger LOG = Logger.getLogger(FirebirdAuthenticationLoginModule.class);

    private Subject subject;
    private CallbackHandler callbackHandler;
    private Principal principal;
    private Group rolesGroup;
    private HttpServletRequest request;
    private CredentialsHandlerFactory authenticatorFactory;
    private HibernateHelper hibernateHelper;

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void initialize(Subject sub, CallbackHandler ch, Map state, Map options) {
        getInjector().injectMembers(this);
        this.subject = sub;
        this.callbackHandler = ch;
    }

    Injector getInjector() {
        return GuiceInjectorHolder.getInjector();
    }

    private HttpServletRequest getHttpServletRequest() throws LoginException {
        if (request == null) {
            retrieveRequest();
        }
        return request;
    }

    @SuppressWarnings({ "PMD.PreserveStackTrace" }) // LoginException only provides message parameter
    private void retrieveRequest() throws LoginException {
        try {
            request = (HttpServletRequest) PolicyContext.getContext(WEB_REQUEST_KEY);
        } catch (PolicyContextException e) {
            LOG.error(e);
            throw new LoginException("Unable to get identity provider selection:  " + e.getMessage());
        }
    }

    String getIdentityProviderUrl() throws LoginException {
        return getHttpServletRequest().getParameter(IDP_URL_PARAM);
    }

    @Override
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    public boolean abort() throws LoginException {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean commit() throws LoginException {
        subject.getPrincipals().add(principal);
        subject.getPrincipals().add(rolesGroup);
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().remove(principal);
        subject.getPrincipals().remove(rolesGroup);
        return true;
    }

    void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean login() throws LoginException {
        hibernateHelper.openAndBindSession();
        try {
            NameCallback nameCallback = new NameCallback("Username: ");
            PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);
            handleCallbacks(nameCallback, passwordCallback);
            String username = nameCallback.getName();
            String password = String.valueOf(passwordCallback.getPassword());
            String providerUrl = getIdentityProviderUrl();
            UserSessionInformation sessionInformation = authenticateUser(username, password, providerUrl);
            authorizeUser(sessionInformation);
            processUserInformation(sessionInformation);
            return true;
        } finally {
            hibernateHelper.unbindAndCleanupSession();
        }
    }

    @SuppressWarnings({ "PMD.PreserveStackTrace" }) // LoginException only provides message parameter
    private void handleCallbacks(NameCallback nameCallback, PasswordCallback passwordCallback) throws LoginException {
        Callback[] callbacks = new Callback[ 2 ];
        callbacks[ 0 ] = nameCallback;
        callbacks[ 1 ] = passwordCallback;
        try {
            callbackHandler.handle(callbacks);
        } catch (IOException e) {
            throw new LoginException("Unable to handle callbacks: " + e.getMessage());
        } catch (UnsupportedCallbackException e) {
            throw new LoginException("Unable to handle callbacks: " + e.getMessage());
        }
    }

    private UserSessionInformation authenticateUser(String username, String password, String providerUrl)
            throws LoginException {
        Authenticator authenticator = getAuthenticatorFactory().getAuthenticator(providerUrl);
        return authenticator.authenticateUser(username, password, providerUrl);
    }

    private void authorizeUser(UserSessionInformation sessionInformation) throws LoginException {
        String fullyQualifiedUsername = sessionInformation.getFullyQualifiedUsername();
        RoleHandler roleHandler = getAuthenticatorFactory().getRoleHandler(fullyQualifiedUsername);
        roleHandler.handleRoles(sessionInformation);
    }

    private void processUserInformation(UserSessionInformation sessionInformation) {
        createPrincipal(sessionInformation.getFullyQualifiedUsername());
        createRolesGroup(sessionInformation.getGroupNames());
        request.getSession().setAttribute(FirebirdConstants.USER_SESSION_INFORMATION, sessionInformation);
    }

    private void createPrincipal(String fullyQualifiedUsername) {
        principal = new SimplePrincipal(fullyQualifiedUsername);
    }

    private void createRolesGroup(Set<String> groupNames) {
        rolesGroup = new SimpleGroup("Roles");
        for (String groupName : groupNames) {
            rolesGroup.addMember(new SimpleGroup(groupName));
        }
    }

    private CredentialsHandlerFactory getAuthenticatorFactory() {
        return authenticatorFactory;
    }

    @Inject
    void setAuthenticatorFactory(CredentialsHandlerFactory authenticatorFactory) {
        this.authenticatorFactory = authenticatorFactory;
    }

    @Inject
    void setHibernateHelper(HibernateHelper hibernateHelper) {
        this.hibernateHelper = hibernateHelper;
    }

}
