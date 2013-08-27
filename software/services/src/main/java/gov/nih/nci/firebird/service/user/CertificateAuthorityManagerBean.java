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

import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.Keystore;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.RootKeystore;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.service.AbstractGenericServiceBean;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.signing.CertificateGenerationAttributes;
import gov.nih.nci.firebird.service.signing.DigitalSigningException;
import gov.nih.nci.firebird.service.signing.DigitalSigningService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.security.auth.x500.X500Principal;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.X509CertificateObject;

import com.google.common.base.Splitter;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 *
 * Certificate Issuer Key Manager.
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CertificateAuthorityManagerBean extends AbstractGenericServiceBean<RootKeystore> implements
        CertificateAuthorityManager {

    private static final Logger LOG = Logger.getLogger(CertificateAuthorityManagerBean.class);
    private static final int CA_VALID_DAYS = 3650;
    private static final int VALID_DAYS = 365;
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String BOUNCY_CASTLE_PROVIDER = "BC";
    private static final String CURRENT_ROOT_KEYSTORE_HQL = "from " + RootKeystore.class.getName()
            + " where id = (select max(r.id) from " + RootKeystore.class.getName() + " r)";

    private static final String GET_USER_KEYSTORE_IDS_HQL = "select keystore.keystoreFile.id from "
            + FirebirdUser.class.getName() + " where keystore.keystoreFile.id is not null";
    private static final String UNLINK_ALL_USER_KEYSTORES_HQL = "update " + FirebirdUser.class.getName()
            + " set keystore = null";
    /**
     * Key-store file name.
     */
    public static final String KEYSTORE_FILE_NAME_SUFFIX = "-firebird.p12";

    /**
     * Key-store mine type.
     */
    public static final String KEYSTORE_MIME_TYPE = "application/x-pkcs12";

    /**
     * Key-store description.
     */
    public static final String KEYSTORE_DESCRIPTION = "PKCS12 key store";

    private FirebirdUserService userService;
    private String rootKeystorePassword;
    private String firebirdEmail;
    private int caCertValidDays = CA_VALID_DAYS;
    private int userCertValidDays = VALID_DAYS;
    private FileService fileService;
    private DigitalSigningService signingService;

    private String caCertFirstName;
    private String caCertLastName;
    private String caCertCountryCode;
    private String caCertCity;
    private String caCertOrganizationUnit;
    private String caCertOrganization;
    private String caCertStateProvince;
    private String caCertAlias;

    @Inject
    void setCaCertValidDays(@Named("ca.root.cert.validDays") int caCertValidDays) {
        this.caCertValidDays = caCertValidDays;
    }

    @Inject
    void setUserCertValidDays(@Named("ca.user.cert.validDays") int userCertValidDays) {
        this.userCertValidDays = userCertValidDays;
    }

    @Inject
    void setCaCertFirstName(@Named("ca.first.name") String caCertFirstName) {
        this.caCertFirstName = caCertFirstName;
    }

    @Inject
    void setCaCertLastName(@Named("ca.last.name") String caCertLastName) {
        this.caCertLastName = caCertLastName;
    }

    @Inject
    void setCaCertCountryCode(@Named("ca.country.code") String caCertCountryCode) {
        this.caCertCountryCode = caCertCountryCode;
    }

    @Inject
    void setCaCertCity(@Named("ca.city") String caCertCity) {
        this.caCertCity = caCertCity;
    }

    @Inject
    void setCaCertOrganizationUnit(@Named("ca.organization.unit") String caCertOrganizationUnit) {
        this.caCertOrganizationUnit = caCertOrganizationUnit;
    }

    @Inject
    void setCaCertOrganization(@Named("ca.organization") String caCertOrganization) {
        this.caCertOrganization = caCertOrganization;
    }

    @Inject
    void setCaCertStateProvince(@Named("ca.state.or.province") String caCertStateProvince) {
        this.caCertStateProvince = caCertStateProvince;
    }

    @Inject
    void setCaCertAlias(@Named("ca.alias") String caCertAlias) {
        this.caCertAlias = caCertAlias;
    }

    @Inject
    void setRootKeystorePassword(@Named("ca.keystore.password") String rootKeystorePassword) {
        this.rootKeystorePassword = rootKeystorePassword;
    }

    @Inject
    void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Inject
    void setDigitalSigningService(DigitalSigningService digitalSigningService) {
        this.signingService = digitalSigningService;
    }

    @Inject
    void setUserService(FirebirdUserService userService) {
        this.userService = userService;
    }

    @Inject
    void setFirebirdEmail(@Named("firebird.email.send.address") String firebirdEmail) {
        this.firebirdEmail = firebirdEmail;
    }

    @Override
    public void initRootKeystore() throws IOException, DigitalSigningException, GeneralSecurityException {
        if (isNewRootKeystoreRequired()) {
            LOG.info("Creating new Root Keystore");
            createRootKeystore();
            deleteExistingUserKeystores();
        }
    }

    private boolean isNewRootKeystoreRequired() throws IOException, GeneralSecurityException, DigitalSigningException {
        CertificateGenerationAttributes attributes = generateCaCertificateAttributes(1L, rootKeystorePassword,
                firebirdEmail);
        X509CertificateObject existingRootKeystore = getCurrentRootKeystoreCertificate();
        if (existingRootKeystore != null && !isCertificateExpired(existingRootKeystore)) {
            Collection<String> existingAttributes = getKeystoreAttributeValues(existingRootKeystore);
            return !(existingAttributes.contains(attributes.getEmailAddress())
                    && existingAttributes.contains(attributes.constructCommonName())
                    && existingAttributes.contains(attributes.getOrganizationUnit())
                    && existingAttributes.contains(attributes.getOrganization())
                    && existingAttributes.contains(attributes.getCity())
                    && existingAttributes.contains(attributes.getStateProvince()) && existingAttributes
                        .contains(attributes.getCountryCode()));
        }
        return true;
    }

    boolean isCertificateExpired(X509CertificateObject certificate) {
        if (certificate.getNotAfter() != null) {
            return new Date().after(certificate.getNotAfter());
        }
        return true;
    }

    void createRootKeystore() throws IOException, DigitalSigningException {
        RootKeystore keystore = new RootKeystore();
        save(keystore);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CertificateGenerationAttributes attributes = generateCaCertificateAttributes(keystore.getId(),
                rootKeystorePassword, firebirdEmail);
        signingService.createRootKeyStore(attributes, byteArrayOutputStream);
        FileMetadata metadata = new FileMetadata("firebird.p12", KEYSTORE_MIME_TYPE, "root key-store");
        FirebirdFile file = fileService.createFile(byteArrayOutputStream.toByteArray(), metadata);
        keystore.setKeystore(new Keystore(file));
        save(keystore);
    }

    private Collection<String> getKeystoreAttributeValues(X509CertificateObject keystore) {
        String issuerDn = keystore.getIssuerX500Principal().getName(X500Principal.RFC1779);
        return Splitter.on(',').withKeyValueSeparator(Splitter.on('=')).split(issuerDn).values();
    }

    private X509CertificateObject getCurrentRootKeystoreCertificate() throws IOException, GeneralSecurityException,
            DigitalSigningException {
        if (!getAll().isEmpty()) {
            return getKeystoreCertificate(getCurrentRootKeystore(), rootKeystorePassword);
        }
        return null;
    }

    private X509CertificateObject getKeystoreCertificate(InputStream keystoreInputStream, String password)
            throws KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException,
            CertificateException {
        KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE, BOUNCY_CASTLE_PROVIDER);
        ks.load(keystoreInputStream, password.toCharArray());
        String alias = ks.aliases().nextElement();
        return (X509CertificateObject) ks.getCertificate(alias);
    }

    void deleteExistingUserKeystores() {
        List<Long> keystoreIds = getExistingUserKeystoreIds();
        if (!keystoreIds.isEmpty()) {
            unlinkUsersFromKeystores();
        }
    }

    @SuppressWarnings("unchecked")
    // IDs will be Longs
    private List<Long> getExistingUserKeystoreIds() {
        return getSessionProvider().get().createQuery(GET_USER_KEYSTORE_IDS_HQL).list();
    }

    private void unlinkUsersFromKeystores() {
        getSessionProvider().get().createQuery(UNLINK_ALL_USER_KEYSTORES_HQL).executeUpdate();
    }

    InputStream getOrCreateCurrentRootKeystore() throws IOException, DigitalSigningException, GeneralSecurityException {
        initRootKeystore();
        return getCurrentRootKeystore();
    }

    @SuppressWarnings("unchecked")
    // Will be a list of Keystores
    InputStream getCurrentRootKeystore() throws IOException {
        List<RootKeystore> list = getSessionProvider().get().createQuery(CURRENT_ROOT_KEYSTORE_HQL).list();
        if (list.isEmpty()) {
            throw new IllegalStateException("root key-store not initialized");
        }
        return fileService.readFile(list.get(0).getKeystore().getKeystoreFile());
    }

    // here - externalize and fix
    private CertificateGenerationAttributes generateCaCertificateAttributes(long id, String caPassword, String email) {
        CertificateGenerationAttributes caAttributes = new CertificateGenerationAttributes();
        caAttributes.setFirstName(caCertFirstName);
        caAttributes.setLastName(caCertLastName);
        caAttributes.setCountryCode(caCertCountryCode);
        caAttributes.setEmailAddress(email);
        caAttributes.setCity(caCertCity);
        caAttributes.setOrganizationUnit(caCertOrganizationUnit);
        caAttributes.setOrganization(caCertOrganization);
        caAttributes.setStateProvince(caCertStateProvince);
        caAttributes.setAlias(caCertAlias);
        caAttributes.setSerialNumber(id);
        caAttributes.setValidDays(caCertValidDays);
        caAttributes.setKeystorePassword(caPassword);
        return caAttributes;
    }

    @Override
    public Keystore getOrCreateUserKey(FirebirdUser user, String password) throws IOException, DigitalSigningException,
            GeneralSecurityException {
        if (user.getKeystore() != null && !isKeystoreExpired(user.getKeystore(), password)) {
            return user.getKeystore();
        }
        Entry<String, String>[] parsedCn = UserCnUtils.parseCn(user.getUsername());
        String cn = UserCnUtils.getFirstValue("CN", parsedCn);
        String o = UserCnUtils.getLastValue("O", parsedCn);
        String ou = UserCnUtils.getLastValue("OU", parsedCn);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CertificateGenerationAttributes userDn = getAttributes(user, o, ou, password);
        signingService.generatePfx(userDn, byteArrayOutputStream, getOrCreateCurrentRootKeystore(),
                rootKeystorePassword);
        FileMetadata metadata = new FileMetadata(cn + KEYSTORE_FILE_NAME_SUFFIX, KEYSTORE_MIME_TYPE,
                KEYSTORE_DESCRIPTION);
        FirebirdFile file = fileService.createFile(byteArrayOutputStream.toByteArray(), metadata);
        user.setKeystore(new Keystore(file));
        userService.save(user);
        return user.getKeystore();
    }

    private boolean isKeystoreExpired(Keystore keystore, String password) throws IOException, GeneralSecurityException {
        InputStream keystoreInputStream = fileService.readFile(keystore.getKeystoreFile());
        X509CertificateObject certificate = getKeystoreCertificate(keystoreInputStream, password);
        return isCertificateExpired(certificate);
    }

    private CertificateGenerationAttributes getAttributes(FirebirdUser user, String o, String ou, String password) {
        CertificateGenerationAttributes userDn = new CertificateGenerationAttributes();
        Person p = user.getInvestigatorRole().getProfile().getPerson();
        userDn.setCity(p.getPostalAddress().getCity());
        userDn.setCountryCode(p.getPostalAddress().getCountry());
        userDn.setStateProvince(p.getPostalAddress().getStateOrProvince());
        userDn.setEmailAddress(p.getEmail());
        userDn.setTitle(p.getPrefix());
        userDn.setFirstName(p.getFirstName());
        userDn.setMiddleName(p.getMiddleName());
        userDn.setLastName(p.getLastName());
        userDn.setOrganization(o);
        userDn.setOrganizationUnit(ou);
        userDn.setAlias("fbsponsorinv");
        userDn.setSerialNumber(user.getId());
        userDn.setValidDays(userCertValidDays);
        userDn.setKeystorePassword(password);
        return userDn;
    }

}
