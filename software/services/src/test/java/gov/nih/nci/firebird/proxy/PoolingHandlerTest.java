/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The nci-commons
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This nci-commons Software License (the License) is between NCI and You. You (or
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
 * its rights in the nci-commons Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the nci-commons Software; (ii) distribute and
 * have distributed to and by third parties the nci-commons Software and any
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
package gov.nih.nci.firebird.proxy;

import com.google.inject.Provider;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.StackObjectPool;
import org.junit.After;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * test PoolingHandler
 */
public class PoolingHandlerTest {

    private final static List<Thread> threads = new ArrayList<Thread>(5);

    @After
    public void resetThreads() throws InterruptedException {
        threads.clear();
        synchronized(BaseClient.class) {
            BaseClient.class.notifyAll();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    @Test
    public void testProvider() {
        @SuppressWarnings("unchecked")
        Provider<ITestClient> provider = mock(Provider.class);
        when(provider.get()).thenAnswer(new Answer<ITestClient>() {
            @Override
            public ITestClient answer(InvocationOnMock invocation) throws Throwable {
                return makeBaseMockClient();
            }
        });
        InvocationHandler handler = new PoolingHandler(provider, 3, 3);
        ITestClient proxy = (ITestClient) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] {ITestClient.class}, handler);
        proxy.doSomethingUseful(null);
        verify(provider, times(1)).get();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNormalInvocation() throws Exception {
        final ITestClient rootMock = makeBaseMockClient();
        PoolableObjectFactory<Object> factory = mock(PoolableObjectFactory.class);
        when(factory.validateObject(any())).thenReturn(Boolean.TRUE);
        when(factory.makeObject()).thenAnswer(new Answer<ITestClient>() {
            @Override
            public ITestClient answer(InvocationOnMock invocation) throws Throwable {
                return new RelayClient(rootMock);
            }
        });
        ITestClient proxy = newProxy(factory);
        assertEquals(1, proxy.doSomethingUseful("foo"));
        try {
            proxy.doSomethingBad(1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            proxy.doSomethingWorse(false);
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
        verify(rootMock).doSomethingUseful("foo");
        verify(rootMock).doSomethingBad(1);
        verify(rootMock).doSomethingWorse(false);
        verifyNoMoreInteractions(rootMock);
    }

    @Test
    public void testPoolUse() throws Exception {
        PoolableObjectFactory<Object> factory = makeMockFactory();
        ObjectPool<Object> pool = spy(new StackObjectPool<Object>(factory));
        InvocationHandler handler = new PoolingHandler(pool);
        ITestClient proxy = (ITestClient) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] {ITestClient.class}, handler);
        proxy.doSomethingUseful(null);
        verify(pool, times(1)).borrowObject();
        verify(pool, times(1)).returnObject(any());
    }

    @Test
    public void testExpectedSuperExceptionHandeling() throws Exception {
        PoolableObjectFactory<Object> factory = makeMockFactory();
        ObjectPool<Object> pool = spy(new StackObjectPool<Object>(factory));
        PoolingHandler handler = new PoolingHandler(pool);
        handler.getValidExceptions().add(RuntimeException.class);
        assertTrue(new IllegalArgumentException() instanceof RuntimeException);
        ITestClient proxy = (ITestClient) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] {ITestClient.class}, handler);
        try {
            proxy.doSomethingBad(0);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        verify(pool, times(1)).borrowObject();
        verify(pool, times(1)).returnObject(any());
        verify(factory, times(0)).destroyObject(any());
    }

    @Test
    public void testExpectedSubExceptionHandling() throws Exception {
        PoolableObjectFactory<Object> factory = makeMockFactory();
        ObjectPool<Object> pool = spy(new StackObjectPool<Object>(factory));
        PoolingHandler handler = new PoolingHandler(pool);
        class SubException extends IllegalArgumentException {
            private static final long serialVersionUID = 1L;
        }
        handler.getValidExceptions().add(SubException.class);
        ITestClient proxy = (ITestClient) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] {ITestClient.class}, handler);
        try {
            proxy.doSomethingBad(0);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        verify(pool, times(1)).borrowObject();
        verify(pool, times(0)).returnObject(any());
        verify(factory, times(1)).destroyObject(any());
    }

    @Test
    public void testPoolDepletion() throws InterruptedException {
        Provider<BaseClient> provider = new Provider<BaseClient>() {
            @Override public BaseClient get() { return new BaseClient(); }
        };
        PoolingHandler handler = new PoolingHandler(provider, 3, 0);
        final ITestClient proxy = (ITestClient) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] {ITestClient.class}, handler);

        for (int i = 0; i < 5; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    proxy.block();
                }
            };
            t.start();
        }

        synchronized(threads) {
            while (threads.size() < 5) {
                threads.wait(500L);
            }
        }
        // all threads started
        assertEquals(0, handler.getPool().getNumIdle());
        assertEquals(5, handler.getPool().getNumActive());

        // let threads resume.
        synchronized(BaseClient.class) {
            BaseClient.class.notifyAll();
        }

        for (Thread t : threads) {
            t.join();
        }
        assertEquals(3, handler.getPool().getNumIdle());
    }

    @SuppressWarnings("unchecked")
    private PoolableObjectFactory<Object> makeMockFactory() {
        PoolableObjectFactory<Object> factory = mock(PoolableObjectFactory.class);
        when(factory.validateObject(any())).thenReturn(Boolean.TRUE);
        try {
            when(factory.makeObject()).thenAnswer(new Answer<ITestClient>() {
                @Override
                public ITestClient answer(InvocationOnMock invocation) throws Throwable {
                    return makeBaseMockClient();
                }
            });
        } catch (Exception e) {
            //will never happen here.
        }
        return factory;
    }

    private ITestClient newProxy(PoolableObjectFactory<Object> factory) {
        PoolingHandler handler = new PoolingHandler(factory, 5, 1);
        handler.getValidExceptions().add(IllegalArgumentException.class);
        return (ITestClient) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] {ITestClient.class}, handler);
    }

    private ITestClient makeBaseMockClient() {
        return spy(new BaseClient());
    }

    public static interface ITestClient {
        int doSomethingUseful(String param);
        void doSomethingBad(int param) throws IllegalArgumentException;
        void doSomethingWorse(boolean param);
        void block();
    }

    private static class BaseClient implements ITestClient {

        @Override
        public int doSomethingUseful(String param) {
            return 1;
        }

        @Override
        public void doSomethingBad(int param) throws IllegalArgumentException {
            throw new IllegalArgumentException();
        }

        @Override
        public void doSomethingWorse(boolean param) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void block() {
            try {
                synchronized(BaseClient.class){
                    synchronized(threads) {
                        threads.add(Thread.currentThread());
                        threads.notify();
                    }
                    BaseClient.class.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static class RelayClient implements ITestClient {
        private final ITestClient target;

        public RelayClient(ITestClient target) {
            this.target = target;
        }

        @Override
        public int doSomethingUseful(String param) {
            return target.doSomethingUseful(param);
        }

        @Override
        public void doSomethingBad(int param) throws IllegalArgumentException {
            target.doSomethingBad(param);
        }

        @Override
        public void doSomethingWorse(boolean param) {
            target.doSomethingWorse(param);
        }

        @Override
        public void block() {
            target.block();
        }
    }
}
