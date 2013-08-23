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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

/**
 * Implementation of DigitalSigningService Interface.
 */
class DigitalSigningServiceBean implements DigitalSigningService {

    private final DigitalSigningHelper digitalSigningHelper;

    private static final int CONTENT_SIZE = 15000;
    private static final int BUFFER_LENGTH = 8192;
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String BOUNCY_CASTLE_PROVIDER = "BC";
    private String caCertFriendlyName;

    /**
     * Default constructor.
     *
     * @param inDigitalSigningHelper
     *            DigitalSigningUtils instance.
     */
    @Inject
    public DigitalSigningServiceBean(DigitalSigningHelper inDigitalSigningHelper) {
        this.digitalSigningHelper = inDigitalSigningHelper;

        // Make sure the BC provider has been loaded.
        if (Security.getProvider(BOUNCY_CASTLE_PROVIDER) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Inject
    void setCaCertFriendlyName(@Named("ca.friendly.name") String caCertFriendlyName) {
        this.caCertFriendlyName = caCertFriendlyName;
    }

    @Override
    public void createRootKeyStore(CertificateGenerationAttributes caAttributes, OutputStream caOutputStream)
            throws DigitalSigningException, IOException {
        // validate passed-in arguments.
        if (!caAttributes.isValid()) {
            throw new DigitalSigningException(
                    "Error validating CertificateGenerationAttributes object.",
                    new Exception());
        }

        // Create caKeypair and store them in a keystore file.
        KeyPair caKeyPair = digitalSigningHelper.generateKeyPair();
        Certificate caCert = digitalSigningHelper.generateRootCert(
                caKeyPair.getPublic(), caKeyPair.getPrivate(),
                caAttributes.generateUserDistinguishedName(), caAttributes.getSerialNumber(),
                caAttributes.getValidDays(), caCertFriendlyName);
        try {
            KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE, BOUNCY_CASTLE_PROVIDER);
            ks.load(null, null);
            ks.setKeyEntry(caAttributes.getAlias(),
                    caKeyPair.getPrivate(), null, new Certificate[]{caCert});
            ks.store(caOutputStream, caAttributes.getKeystorePassword().toCharArray());
        } catch (GeneralSecurityException e) {
            throw new DigitalSigningException("Error generating root keystore.", e);
        }
    }


    @Override
    public void generatePfx(CertificateGenerationAttributes attributes, OutputStream pfxOutputStream,
            InputStream caKeystore, String caKeystorePassword)
            throws DigitalSigningException, IOException {

        try {
            KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE, BOUNCY_CASTLE_PROVIDER);
            ks.load(caKeystore, caKeystorePassword.toCharArray());
            String alias = ks.aliases().nextElement();
            PrivateKey caKey = (PrivateKey) ks.getKey(alias, null);
            Certificate caCert = ks.getCertificate(alias);
            generatePfx(attributes, caKey, caCert, pfxOutputStream);
        } catch (GeneralSecurityException ex) {
            throw new DigitalSigningException("Error generating pfx file.", ex);
        }
    }

    private void generatePfx(CertificateGenerationAttributes attributes, PrivateKey issuerPrivateKey,
            Certificate issuerCert, OutputStream pfxOutputStream) throws DigitalSigningException, IOException {
        // validate passed-in arguments.
        if (!attributes.isValid()) {
            throw new DigitalSigningException(
                    "Error validating CertificateGenerationAttributes object.",
                    new Exception());
        }

        // generate key pairs and store in a pfx/p12 file
        KeyPair userKeyPair = digitalSigningHelper.generateKeyPair();
        try {
            Certificate userCert = digitalSigningHelper.generateUserCert(
                    userKeyPair.getPublic(), issuerPrivateKey, (X509Certificate) issuerCert,
                    attributes.generateUserDistinguishedName(),
                    attributes.getSerialNumber(), attributes.getValidDays());
            Certificate[] chain = {userCert, issuerCert};

            KeyStore store = KeyStore.getInstance(KEYSTORE_TYPE, BOUNCY_CASTLE_PROVIDER);
            store.load(null, null);
            store.setKeyEntry(attributes.getAlias(), userKeyPair.getPrivate(), null, chain);
            store.store(pfxOutputStream, attributes.getKeystorePassword().toCharArray());
        } catch (GeneralSecurityException e) {
            throw new DigitalSigningException("Error generating pfx file.", e);
        }
    }

    @Override
    // This is minimal parameter list for signing.
    public void signPdf(InputStream srcPdf, OutputStream destPdf,
            DigitalSigningAttributes signingAttributes)
            throws DigitalSigningException, IOException {
        try {
            KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE, BOUNCY_CASTLE_PROVIDER);
            ks.load(signingAttributes.getSigningKeyStore(),
                    signingAttributes.getSigningPassword().toCharArray());
            String alias = ks.aliases().nextElement();
            PrivateKey key = (PrivateKey) ks.getKey(alias, null);
            Certificate[] chain = ks.getCertificateChain(alias);
            // reader and stamper
            PdfReader reader = new PdfReader(srcPdf);
            PdfStamper stamper = PdfStamper.createSignature(reader, destPdf, '\0');
            PdfSignatureAppearance appearance = createPdfSigAppearance(stamper, signingAttributes, chain);
            // create signature dictionary
            PdfSignature dic = createSignatureDictionary(appearance,
                    PdfPKCS7.getSubjectFields((X509Certificate) chain[0]).getField("CN"),
                    signingAttributes.getSigningReason(),
                    signingAttributes.getSigningLocation());
            appearance.setCryptoDictionary(dic);
            // Reserve space for signature content.
            HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
            exc.put(PdfName.CONTENTS, Integer.valueOf(CONTENT_SIZE * 2 + 2));
            appearance.preClose(exc);
            // Create hash of content.
            byte[] digest = createMessageDigest(appearance);
            // Creates signed hash.
            byte[] signedHash = generateSignedHash(key, chain, digest);
            // add signature contents.
            PdfDictionary dic2 = new PdfDictionary();
            dic2.put(PdfName.CONTENTS, new PdfString(signedHash).setHexWriting(true));
            appearance.close(dic2);
        } catch (GeneralSecurityException e) {
            throw new DigitalSigningException("Failed signing pdf.", e);
        } catch (DocumentException de) {
            throw new DigitalSigningException("Failed signing pdf.", de);
        }
    }

    private PdfSignatureAppearance createPdfSigAppearance(PdfStamper stp,
            DigitalSigningAttributes signingAttributes, Certificate[] chain)
            throws IOException, BadElementException {
        PdfSignatureAppearance appearance = stp.getSignatureAppearance();
        appearance.setVisibleSignature(signingAttributes.getSigningFieldName());
        appearance.setReason(signingAttributes.getSigningReason());
        appearance.setSignDate(new GregorianCalendar());
        appearance.setCrypto(null, chain, null, null);
        appearance.setAcro6Layers(true);
        appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);

        return appearance;
    }

    private PdfSignature createSignatureDictionary(PdfSignatureAppearance appearance,
            String name, String signReason, String signLocation) {
        PdfSignature pdfSig = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
        pdfSig.setDate(new PdfDate(appearance.getSignDate()));
        pdfSig.setName(name);
        pdfSig.setReason(signReason);
        pdfSig.setLocation(signLocation);
        return pdfSig;
    }

    private byte[] createMessageDigest(PdfSignatureAppearance appearance)
        throws NoSuchAlgorithmException, IOException {
        // Create hash of content.
        InputStream data = appearance.getRangeStream();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
        byte[] buf = new byte[BUFFER_LENGTH];
        int n;
        while ((n = data.read(buf)) > 0) {
            messageDigest.update(buf, 0, n);
        }

        return messageDigest.digest();
    }

    /**
     * @param key private key
     * @param chain certificate chain.
     * @param digest hash of content.
     * @return signedHash
     */
    private byte[] generateSignedHash(PrivateKey key, Certificate[] chain, byte[] digest)
        throws NoSuchAlgorithmException, NoSuchProviderException,
        InvalidKeyException, DocumentException, SignatureException {
        // Creates signed hash.
        Calendar cal = Calendar.getInstance();
        PdfPKCS7 sgn = new PdfPKCS7(key, chain, null, "SHA256", null, false);
        byte[] sh = sgn.getAuthenticatedAttributeBytes(digest, cal, null);
        sgn.update(sh, 0, sh.length);
        byte[] encodedSig = sgn.getEncodedPKCS7(digest, cal, null, null);
        byte[] paddedSig = new byte[CONTENT_SIZE];
        System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);

        return paddedSig;
    }

}
