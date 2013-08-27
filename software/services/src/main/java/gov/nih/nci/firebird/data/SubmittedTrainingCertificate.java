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
package gov.nih.nci.firebird.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.NotNull;

import com.fiveamsolutions.nci.commons.audit.Auditable;

/**
 * Snapshot of a Certificate.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
// seriously?!? "PMD.UnusedPrivateMethod appears 4 times in this file"
@Entity(name = "submitted_certificate")
public class SubmittedTrainingCertificate implements Certificate, Auditable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private TrainingCertificate originalCertificate;
    private Date effectiveDate;
    private Date expirationDate;
    private CertificateType certificateType;
    private FirebirdFile file;
    private Organization issuer;

    @SuppressWarnings("unused")
    //for hibernate
    private SubmittedTrainingCertificate() {
        // no-op
    }

    /**
     * default ctor.
     * @param cert the certificate to snapshot.
     */
    public SubmittedTrainingCertificate(TrainingCertificate cert) {
        this.originalCertificate = cert;
        this.effectiveDate = cert.getEffectiveDate();
        this.expirationDate = cert.getExpirationDate();
        this.certificateType = cert.getCertificateType();
        this.file = cert.getFile().clone();
        this.issuer = cert.getIssuer();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id db id to set
     */
    @SuppressWarnings({ "PMD.UnusedPrivateMethod", "unused" })
    private void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the originalCertificate
     */
    @ManyToOne
    @JoinColumn(name = "original_certificate_id")
    @ForeignKey(name = "submitted_training_certificate_original_fkey")
    public TrainingCertificate getOriginalCertificate() {
        return originalCertificate;
    }

    private void setOriginalCertificate(TrainingCertificate originalCertificate) {
        this.originalCertificate = originalCertificate;
    }

    /**
     * Sets the Original Certificate reference to null. Used when a Certificate is being
     * removed from the users profile and the original certificate no longer exists in the
     * database.
     */
    public void removeLinkToOrginalCertificate() {
        setOriginalCertificate(null);
    }

    /**
     * @return certificate type
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "certificate_type")
    @Override
    public CertificateType getCertificateType() {
        return certificateType;
    }

    @SuppressWarnings({ "PMD.UnusedPrivateMethod", "unused" })
    //for hibernate
    private void setCertificateType(CertificateType certType) {
        this.certificateType = certType;
    }

    /**
     * @return certificate file.
     */
    @ManyToOne
    @Cascade({ CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    @JoinColumn(name = "certificate_file_id")
    @ForeignKey(name = "submitted_training_certificate_firebird_file_fkey")
    @NotNull
    @Override
    public FirebirdFile getFile() {
        return file;
    }

    @SuppressWarnings({ "PMD.UnusedPrivateMethod", "unused" })
    //for hibernate
    private void setFile(FirebirdFile file) {
        this.file = file;
    }

    /**
     * @return effective date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "certificate_effective_date", nullable = true)
    @Override
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    @SuppressWarnings({ "PMD.UnusedPrivateMethod", "unused" })
    //for hibernate
    private void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * @return the expiration date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "certificate_expiration_date", nullable = true)
    @Override
    public Date getExpirationDate() {
        return expirationDate;
    }

    @SuppressWarnings({ "PMD.UnusedPrivateMethod", "unused" })
    //for hibernate
    private void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return issuer organization
     */
    @ManyToOne(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE,
            javax.persistence.CascadeType.REFRESH })
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "issuer_id")
    @ForeignKey(name = "credential_organization_fkey")
    @Override
    public Organization getIssuer() {
        return issuer;
    }

    /**
     * @param issuer issuer organization
     */
    public void setIssuer(Organization issuer) {
        this.issuer = issuer;
    }

    /**
     * @return true if the expiration date is older that the next coming month.
     */
    @Transient
    @Override
    public boolean isExpired() {
        return AbstractCredential.isExpired(getExpirationDate());
    }

}
