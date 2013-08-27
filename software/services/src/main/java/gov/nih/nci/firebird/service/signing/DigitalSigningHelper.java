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
package gov.nih.nci.firebird.service.signing;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;

/**
 * DigitalSigningService Helper.
 */
@SuppressWarnings({ "PMD.TooManyMethods" })
// Better to put all digital signing helper methods together.
class DigitalSigningHelper {

    private static final int KEYSIZE = 1024;
    private static final String BOUNCY_CASTLE_PROVIDER = "BC";

    // Error messages below.
    private static final String KEYPAIR_GENERATION_ERROR_MESSAGE = "Error generating key pair.";
    private static final String ROOT_CA_GENERATION_ERROR_MESSAGE = "Error generating root CA certificate.";
    private static final String CERT_GENERATION_ERROR_MESSAGE = "Error generating user's digital certificate.";

    /**
     * Default constructor.
     */
    DigitalSigningHelper() {

        if (Security.getProvider(BOUNCY_CASTLE_PROVIDER) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

    }

    /**
     * Generate a random 1024 bit RSA key pair.
     *
     * @throws DigitalSigningException
     *             Customized exception with error message.
     *
     * @return a random KeyPair.
     */
    KeyPair generateKeyPair() throws DigitalSigningException {

        KeyPairGenerator kpGen = null;
        try {
            kpGen = KeyPairGenerator.getInstance("RSA", BOUNCY_CASTLE_PROVIDER);
            kpGen.initialize(KEYSIZE, new SecureRandom());
            return kpGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new DigitalSigningException(KEYPAIR_GENERATION_ERROR_MESSAGE, e);
        } catch (NoSuchProviderException e) {
            throw new DigitalSigningException(KEYPAIR_GENERATION_ERROR_MESSAGE, e);
        }
    }

    /**
     * Generate the CA's certificate.
     *
     * @param publicKey
     *            Public key.
     * @param privateKey
     *            Private key.
     * @param distinguishedName
     *            Distinguished Name.
     * @param serialNumber
     *            Unique serial number.
     * @param validDays
     *            valid Days.
     * @param certFriendlyName  Certificate friendly name
     *
     * @throws DigitalSigningException
     *             Customized exception with error message.
     *
     * @return a Certificate.
     */
    @SuppressWarnings({ "PMD.AvoidCatchingGenericException", "PMD.ExcessiveParameterList" })
    // same handling for multiple exception types, parameters required for generation
    Certificate generateRootCert(PublicKey publicKey,
            PrivateKey privateKey,
            DigitalSigningDistinguishedName distinguishedName,
            long serialNumber, int validDays, String certFriendlyName) throws DigitalSigningException {

        try {
            X509V1CertificateGenerator v1CertGen = buildX509V1CertificateGenerator(
                    publicKey, distinguishedName, serialNumber, validDays);
            X509Certificate cert = v1CertGen.generate(privateKey, BOUNCY_CASTLE_PROVIDER);
            cert.checkValidity(new Date());
            cert.verify(publicKey);
            PKCS12BagAttributeCarrier bagAttr = (PKCS12BagAttributeCarrier) cert;
            bagAttr.setBagAttribute(
                    PKCSObjectIdentifiers.pkcs_9_at_friendlyName,
                    new DERBMPString(certFriendlyName));
            return cert;
        } catch (Exception e) {
            throw new DigitalSigningException(ROOT_CA_GENERATION_ERROR_MESSAGE,
                    e);
        }
    }

    private X509V1CertificateGenerator buildX509V1CertificateGenerator(
            PublicKey publicKey,
            DigitalSigningDistinguishedName distinguishedName,
            long serialNumber, int validDays) {

        X509V1CertificateGenerator v1CertGen = new X509V1CertificateGenerator();

        // Calculate Expiration Date;
        Calendar notBeforeCal = Calendar.getInstance();
        Date notBeforeDate = notBeforeCal.getTime();
        Calendar notAfterCal = Calendar.getInstance();
        notAfterCal.add(Calendar.DAY_OF_YEAR, validDays);
        Date notAfterDate = notAfterCal.getTime();

        //
        // create the certificate - version 1
        //
        v1CertGen.setSerialNumber(BigInteger.valueOf(serialNumber));
        v1CertGen.setIssuerDN(new X509Principal(getAttributeOrder(),
                buildAttributes(distinguishedName)));
        v1CertGen.setNotBefore(notBeforeDate);
        v1CertGen.setNotAfter(notAfterDate);
        // subjects name - the same as we are self signed.
        v1CertGen.setSubjectDN(new X509Principal(getAttributeOrder(),
                buildAttributes(distinguishedName)));
        v1CertGen.setPublicKey(publicKey);
        v1CertGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
        return v1CertGen;
    }

    @SuppressWarnings({ "PMD.LooseCoupling",
            "PMD.UseArrayListInsteadOfVector", "PMD.ReplaceVectorWithList" })
    // Vector required by bouncycastle X509 implementation
    private Vector<DERObjectIdentifier> getAttributeOrder() {
        Vector<DERObjectIdentifier> order = new Vector<DERObjectIdentifier>();
        order.addElement(X509Principal.C);
        order.addElement(X509Principal.ST);
        order.addElement(X509Principal.L);
        order.addElement(X509Principal.O);
        order.addElement(X509Principal.OU);
        order.addElement(X509Principal.CN);
        order.addElement(X509Principal.EmailAddress);
        return order;
    }

    @SuppressWarnings("PMD.ReplaceHashtableWithMap")
    // Map required by bouncycastle X509 implementation
    private Hashtable<DERObjectIdentifier, String> buildAttributes(
            DigitalSigningDistinguishedName distinguishedName) {
        Hashtable<DERObjectIdentifier, String> attributes = new Hashtable<DERObjectIdentifier, String>();
        attributes.put(X509Principal.C, StringUtils.defaultString(distinguishedName.getCountryName()));
        attributes.put(X509Principal.ST, StringUtils.defaultString(distinguishedName.getStateOrProvinceName()));
        attributes.put(X509Principal.L, StringUtils.defaultString(distinguishedName.getLocalityName()));
        attributes.put(X509Principal.O, StringUtils.defaultString(distinguishedName.getOrganizationName()));
        attributes.put(X509Principal.OU, StringUtils.defaultString(distinguishedName.getOrganizationalUnitName()));
        attributes.put(X509Principal.CN, StringUtils.defaultString(distinguishedName.getCommonName()));
        attributes.put(X509Principal.EmailAddress, StringUtils.defaultString(distinguishedName.getEmailAddress()));
        return attributes;
    }

    private X509V3CertificateGenerator buildX509V3CertificateGenerator(
            PublicKey publicKey, X509Certificate caCert,
            DigitalSigningDistinguishedName distinguishedName,
            long serialNumber, int validDays)
            throws CertificateEncodingException, CertificateParsingException {

        X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();

        // Calculate Expiration Date;
        Calendar notBeforeCal = Calendar.getInstance();
        Date notBeforeDate = notBeforeCal.getTime();
        Calendar notAfterCal = Calendar.getInstance();
        notAfterCal.add(Calendar.DAY_OF_YEAR, validDays);
        Date notAfterDate = notAfterCal.getTime();

        //
        // create the certificate - version 3
        //
        v3CertGen.reset();

        v3CertGen.setSerialNumber(BigInteger.valueOf(serialNumber));
        v3CertGen.setIssuerDN(PrincipalUtil.getSubjectX509Principal(caCert));
        v3CertGen.setNotBefore(notBeforeDate);
        v3CertGen.setNotAfter(notAfterDate);
        v3CertGen.setSubjectDN(new X509Principal(getAttributeOrder(),
                buildAttributes(distinguishedName)));
        v3CertGen.setPublicKey(publicKey);
        v3CertGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

        //
        // extensions
        //
        v3CertGen.addExtension(X509Extensions.SubjectKeyIdentifier, false,
                new SubjectKeyIdentifierStructure(publicKey));

        v3CertGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false,
                new AuthorityKeyIdentifierStructure(caCert));

        v3CertGen.addExtension(X509Extensions.BasicConstraints, true,
                new BasicConstraints(0));

        return v3CertGen;
    }

    /**
     * Generate intermediate certificate signed by CA.
     *
     * @param publicKey
     *            Public key.
     * @param caPrivateKey
     *            Private key.
     * @param caDistinguishedName
     *            Issuer's Distinguished Name..
     * @param distinguishedName
     *            User's Distinguished Name.
     * @param serialNumber
     *            Unique serial number.
     * @param validDays
     *            valid Days.
     * @param friendName
     *            Set a friendly name for the certificate. Can be null.
     *
     * @throws DigitalSigningException
     *             Customized exception with error message.
     *
     * @return a Certificate.
     */
    @SuppressWarnings({ "PMD.ExcessiveParameterList", "PMD.AvoidCatchingGenericException" })
    // This is minimal parameter list.
    // same handling for multiple exception types
    Certificate generateUserCert(PublicKey publicKey,
            PrivateKey caPrivateKey, X509Certificate caCert,
            DigitalSigningDistinguishedName distinguishedName,
            long serialNumber, int validDays)
            throws DigitalSigningException {
        try {
            X509V3CertificateGenerator v3CertGen = buildX509V3CertificateGenerator(publicKey, caCert,
                    distinguishedName, serialNumber, validDays);
            X509Certificate cert = v3CertGen.generate(caPrivateKey, BOUNCY_CASTLE_PROVIDER);
            cert.checkValidity(new Date());
            cert.verify(caCert.getPublicKey());
            PKCS12BagAttributeCarrier bagAttr = (PKCS12BagAttributeCarrier) cert;

            bagAttr.setBagAttribute(
                    PKCSObjectIdentifiers.pkcs_9_at_friendlyName,
                    new DERBMPString("User Certificate"));
            bagAttr.setBagAttribute(
                    PKCSObjectIdentifiers.pkcs_9_at_localKeyId,
                    new SubjectKeyIdentifierStructure(publicKey));

            return cert;
        } catch (Exception e) {
            throw new DigitalSigningException(CERT_GENERATION_ERROR_MESSAGE, e);
        }
    }

}
