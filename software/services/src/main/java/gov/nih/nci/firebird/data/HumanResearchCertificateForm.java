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

import gov.nih.nci.firebird.common.ValidationResult;

import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.common.collect.Sets;

/**
 * Human Research Certificate for a registration.
 */
@Entity
@Table(name = "human_research_certificate_form")
public class HumanResearchCertificateForm extends AbstractProtocolRegistrationForm {

    private static final long serialVersionUID = 1L;

    private Set<SubmittedTrainingCertificate> submittedCertificates = Sets.newHashSet();
    private Set<TrainingCertificate> selectedCertificates = Sets.newTreeSet();

    /**
     * No-argument constructor for Hibernate / Struts.
     */
    public HumanResearchCertificateForm() {
        super();
    }

    HumanResearchCertificateForm(AbstractProtocolRegistration registration, FormType formType) {
        super(registration, formType);
    }

    @Override
    @Transient
    public boolean isAdditionalDocumentsUploaded() {
        return !getCertificates().isEmpty();
    }

    @Override
    @Transient
    public int getNumberOfAdditionalDocuments() {
        return getCertificates().size();
    }

    /**
     * @return the set of Immutable or mutable certificates based on status.
     */
    @Transient
    public Set<? extends Certificate> getCertificates() {
        if (getRegistration().isLockedForInvestigator() && !getSubmittedCertificates().isEmpty()) {
            return getSubmittedCertificates();
        } else {
            return getSelectedCertificates();
        }
    }

    /**
     * @return the submittedCertificates
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "formphrc_submitted_certificate", joinColumns = @JoinColumn(name = "formphrc"),
            inverseJoinColumns = @JoinColumn(name = "submitted_training_certificate"))
    @ForeignKey(name = "formphrc_fkey", inverseName = "submitted_certificate_fkey")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<SubmittedTrainingCertificate> getSubmittedCertificates() {
        return submittedCertificates;
    }

    /**
     * @param submittedCertificates the submittedCertificates to set
     */
    @SuppressWarnings({ "PMD.UnusedPrivateMethod", "unused" }) //hibernate use only
    private void setSubmittedCertificates(Set<SubmittedTrainingCertificate> submittedCertificates) {
        this.submittedCertificates = submittedCertificates;
    }

    /**
     * @return the selectedCertificates
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "formphrc_certificate", joinColumns = @JoinColumn(name = "formphrc"),
            inverseJoinColumns = @JoinColumn(name = "certificate"))
    @ForeignKey(name = "formphrc_fkey", inverseName = "certificate_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
        org.hibernate.annotations.CascadeType.REFRESH })
    private Set<TrainingCertificate> getSelectedCertificates() {
        return selectedCertificates;
    }

    /**
     * @param selectedCertificates the selectedCertificates to set
     */
    @SuppressWarnings({ "PMD.UnusedPrivateMethod", "unused" }) //hibernate use only
    private void setSelectedCertificates(Set<TrainingCertificate> selectedCertificates) {
        this.selectedCertificates = selectedCertificates;
    }

    @Override
    @OneToOne(mappedBy = "humanResearchCertificateForm", optional = false)
    public AbstractProtocolRegistration getRegistration() {
        return super.getRegistration();
    }

    /**
     * Adds a new Certificate to the Set of Selected Certificates.
     *
     * @param certificate .
     */
    public void selectCertificate(TrainingCertificate certificate) {
        getSelectedCertificates().add(certificate);
    }

    /**
     * Removes a certificate from the set of selected certificates.
     *
     * @param certificate .
     */
    public void deselectCertificate(TrainingCertificate certificate) {
        getSelectedCertificates().remove(certificate);
    }

    @Override
    @Transient
    public Set<Organization> getOrganizations() {
        return Collections.emptySet();
    }

    @Override
    @Transient
    public Set<Person> getPersons() {
        return Collections.emptySet();
    }

    @Override
    public void validate(ValidationResult result, ResourceBundle resources) {
        super.validate(result, resources);
        if (FormOptionality.REQUIRED.equals(getFormOptionality()) && getSelectedCertificates().isEmpty()) {
            result.addFailure(createValidationFailure(resources, "validation.failure.missing.document", getFormType()
                    .getDescription()));
        }
    }

    @Override
    public void submitForm() {
        super.submitForm();
        for (TrainingCertificate certificate : getSelectedCertificates()) {
            getSubmittedCertificates().add(new SubmittedTrainingCertificate(certificate));
        }
        getSelectedCertificates().clear();
    }

    @Override
    Set<PersistentObject> returnForm() {
        Set<PersistentObject> objectsToDelete = super.returnForm();
        for (SubmittedTrainingCertificate certificate : getSubmittedCertificates()) {
            if (certificate.getOriginalCertificate() != null && !certificate.isExpired()) {
                getSelectedCertificates().add(certificate.getOriginalCertificate());
            }
        }
        getSubmittedCertificates().clear();
        return objectsToDelete;
    }

    @Override
    void copyTo(AbstractProtocolRegistration registration) {
        HumanResearchCertificateForm copy = new HumanResearchCertificateForm(registration, getFormType());
        registration.setHumanResearchCertificateForm(copy);
        copyCommonData(copy);
        copy.getSubmittedCertificates().addAll(getSubmittedCertificates());
        copy.getSelectedCertificates().addAll(getSelectedCertificates());
    }

    @Override
    boolean isFileReferenced(FirebirdFile file) {
        return super.isFileReferenced(file) || isCertificateFile(file);
    }

    private boolean isCertificateFile(FirebirdFile file) {
        for (Certificate certificate : getCertificates()) {
            if (certificate.getFile().equals(file)) {
                return true;
            }
        }
        return false;
    }

}
