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
package gov.nih.nci.firebird.service.user;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.Keystore;
import gov.nih.nci.firebird.data.RootKeystore;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.signing.DigitalSigningException;
import gov.nih.nci.firebird.test.AbstractHibernateTestCase;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.junit.Test;

import com.google.inject.Inject;

public class CertificateAuthorityManagerBeanTest extends AbstractHibernateTestCase {

    @Inject
    private CertificateAuthorityManagerBean bean;
    @Inject
    private FileService fileService;
    @Inject
    private FirebirdUserService userService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        bean.setFileService(fileService);
        bean.setUserService(userService);
    }

    @Test
    public void testCreateRootKeystore() throws IOException, DigitalSigningException, GeneralSecurityException {
        List<RootKeystore> keystores = getRootKeystores();
        assertTrue(keystores.isEmpty());
        List<FirebirdFile> files = getFiles();
        assertTrue(files.isEmpty());

        bean.createRootKeystore();
        keystores = getRootKeystores();
        assertEquals(1, keystores.size());
        files = getFiles();
        assertEquals(1, files.size());

        bean.createRootKeystore();
        keystores = getRootKeystores();
        assertEquals(2, keystores.size());
        files = getFiles();
        assertEquals(2, files.size());
    }

    @SuppressWarnings("unchecked")
    private List<RootKeystore> getRootKeystores() {
        return getCurrentSession().createCriteria(RootKeystore.class).list();
    }

    @SuppressWarnings("unchecked")
    private List<FirebirdFile> getFiles() {
        return getCurrentSession().createCriteria(FirebirdFile.class).list();
    }

    @Test
    public void testInitRootKeystore() throws IOException, DigitalSigningException, GeneralSecurityException {
        bean.initRootKeystore();
        List<RootKeystore> keystores = getRootKeystores();
        assertEquals(1, keystores.size());
        List<FirebirdFile> files = getFiles();
        assertEquals(1, files.size());

        bean.initRootKeystore();
        keystores = getRootKeystores();
        assertEquals(1, keystores.size());
        files = getFiles();
        assertEquals(1, files.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRootKeystore_NoInit() throws Exception {
        bean.getCurrentRootKeystore();
    }

    @Test
    public void testGetRootKeystore() throws IOException, DigitalSigningException, GeneralSecurityException {
        bean.initRootKeystore();
        List<RootKeystore> keystores = getRootKeystores();
        assertEquals(1, keystores.size());
        InputStream keystore = bean.getOrCreateCurrentRootKeystore();
        assertNotNull(keystore);
    }

    @Test
    public void testGenerateUserKey() throws IOException, DigitalSigningException, GeneralSecurityException {
        bean.initRootKeystore();
        FirebirdUser user = FirebirdUserFactory.getInstance().createInvestigator("foo");
        saveAndFlush(user.getInvestigatorRole().getProfile(), user);
        user = reloadObject(user);
        bean.getOrCreateUserKey(user, "password");
        flushAndClearSession();
        user = reloadObject(user);
        FirebirdFile file = user.getKeystore().getKeystoreFile();
        assertEquals("foo" + CertificateAuthorityManagerBean.KEYSTORE_FILE_NAME_SUFFIX, file.getName());
        assertEquals(CertificateAuthorityManagerBean.KEYSTORE_MIME_TYPE, file.getContentType());
        assertEquals(CertificateAuthorityManagerBean.KEYSTORE_DESCRIPTION, file.getDescription());

        Keystore keystore = user.getKeystore();

        // for branch coverage
        user = FirebirdUserFactory.getInstance().createInvestigator("bar");
        user.setKeystore(keystore);
        Keystore ks2 = bean.getOrCreateUserKey(user, "password");
        assertSame(keystore, ks2);
    }

    @Test
    public void testGenerateUserKeyWithExistingKeystore() throws IOException, DigitalSigningException,
            GeneralSecurityException {
        bean.initRootKeystore();
        FirebirdFile rootKey = (FirebirdFile) getFiles().get(0);
        FirebirdUser user = FirebirdUserFactory.getInstance().createInvestigator("foo");
        saveAndFlush(user.getInvestigatorRole().getProfile(), user);
        user = reloadObject(user);
        bean.getOrCreateUserKey(user, "password").getKeystoreFile();
        flushAndClearSession();

        user = reloadObject(user);
        FirebirdFile file2 = bean.getOrCreateUserKey(user, "password").getKeystoreFile();
        flushAndClearSession();

        List<FirebirdFile> list = getFiles();
        assertEquals(2, list.size());
        for (FirebirdFile f : list) {
            if (!f.getId().equals(rootKey.getId())) {
                assertEquals(file2.getId(), f.getId());
            }
        }
    }

    @Test
    public void testDeleteExistingUserKeystores() throws Exception {
        FirebirdUser userWithoutKeystore = FirebirdUserFactory.getInstance().create();
        FirebirdUser userWithKeystore = FirebirdUserFactory.getInstance().create();
        FirebirdFile userKeystoreFile = FirebirdFileFactory.getInstance().create();
        FirebirdFile nonKeystoreFile = FirebirdFileFactory.getInstance().create();
        Keystore userKeystore = new Keystore(userKeystoreFile);
        userWithKeystore.setKeystore(userKeystore);
        saveAndFlush(userWithKeystore, userWithoutKeystore, nonKeystoreFile);
        assertEquals(2, getFiles().size());
        bean.deleteExistingUserKeystores();
        List<FirebirdFile> files = getFiles();
        assertEquals(2, files.size()); // Keystore files should still exist, just unlink them
        assertTrue(files.get(1).getName().equals(nonKeystoreFile.getName()));
        assertNull(reloadObject(userWithKeystore).getKeystore());
    }

    @Test
    public void testIsCertificateExpired_NullExpiration() throws Exception {
        X509CertificateObject certificate = mock(X509CertificateObject.class);
        assertTrue(bean.isCertificateExpired(certificate));
    }

    @Test
    public void testIsCertificateExpired_Expired() throws Exception {
        X509CertificateObject certificate = mock(X509CertificateObject.class);
        when(certificate.getNotAfter()).thenReturn(DateUtils.addDays(new Date(), -1));
        assertTrue(bean.isCertificateExpired(certificate));
    }

    @Test
    public void testIsCertificateExpired_NotExpired() throws Exception {
        X509CertificateObject certificate = mock(X509CertificateObject.class);
        when(certificate.getNotAfter()).thenReturn(DateUtils.addDays(new Date(), 1));
        assertFalse(bean.isCertificateExpired(certificate));
    }

}
