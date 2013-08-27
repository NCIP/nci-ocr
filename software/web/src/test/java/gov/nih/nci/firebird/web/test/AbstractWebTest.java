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
package gov.nih.nci.firebird.web.test;

import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.test.FirebirdTestModule;
import gov.nih.nci.firebird.web.FirebirdWebModule;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.providers.XWorkConfigurationProvider;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;

/**
 * useful base class for all web tests.
 */
public abstract class AbstractWebTest {

    private ValueStack theValueStack;
    private ValueStackFactory theValueStackFactory;
    private Injector injector;

    static {
        LocalizedTextUtil.addDefaultResourceBundle("ApplicationResources");
    }

    @Before
    public void setUp() throws Exception {
        initializeMockitoMocks();
        initializeMockWebContext();
        setInjector(Guice.createInjector(getModules()));
        getInjector().injectMembers(this);
    }

    /**
     * Clean out the action context to ensure one test does not impact another.
     */
    @After
    public void cleanUpActionContext() {
        ActionContext.setContext(null);
    }

    private void setInjector(Injector injector) {
        this.injector = injector;
    }

    /**
     * @return the injector
     */
    protected Injector getInjector() {
        return injector;
    }

    private Set<Module> getModules() {
        Set<Module> modules = new HashSet<Module>();
        addModules(modules);
        return modules;
    }

    /**
     * Adds any modules needed to run this test. Override and call <code>super.addModules()</code> in
     * subclasses that need additional modules.
     *
     * @param modules add to this set of modules
     */
    protected void addModules(Set<Module> modules) {
        modules.add(new FirebirdServicesWebTestModule());
        modules.add(new FirebirdWebModule());
        modules.add(new FirebirdTestModule());
    }

    private void initializeMockitoMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private void initializeMockWebContext() throws IOException {
        Container container = setupContainer();
        setupValueStack(container);
        setupContext();
        setupRequest();
        setupResponse();
    }

    private Container setupContainer() {
        ConfigurationManager configurationManager = new ConfigurationManager();
        configurationManager.addContainerProvider(new XWorkConfigurationProvider());
        Configuration config = configurationManager.getConfiguration();
        return config.getContainer();

    }

    private void setupValueStack(Container container) {
        theValueStackFactory = container.getInstance(ValueStackFactory.class);
        theValueStack = theValueStackFactory.createValueStack();
        theValueStack.getContext().put(ActionContext.CONTAINER, container);
        ActionContext.setContext(new ActionContext(theValueStack.getContext()));
    }

    private void setupContext() {
        MockServletContext context = new MockServletContext();
        context.addInitParameter("defaultPageSize", "20");
        ServletActionContext.setServletContext(context);
    }

    private void setupRequest() {
        MockHttpServletRequest request = spy(new MockHttpServletRequest());
        request.setSession(spy(new MockHttpSession()));
        ServletActionContext.setRequest(request);
    }

    private void setupResponse() throws IOException {
        ServletActionContext.setResponse(spy(new MockHttpServletResponse()));
    }

    protected MockHttpServletRequest getMockRequest() {
        return (MockHttpServletRequest) ServletActionContext.getRequest();
    }

    protected MockHttpServletResponse getMockResponse() {
        return (MockHttpServletResponse) ServletActionContext.getResponse();
    }

    protected MockHttpSession getMockSession() {
        return (MockHttpSession) getMockRequest().getSession();
    }
}
