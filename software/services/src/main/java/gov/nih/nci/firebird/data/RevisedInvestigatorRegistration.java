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

import static com.google.common.base.Preconditions.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.NotNull;

/**
 * A previously approved investigator registration packet that is undergoing or has been
 * revised. Provides a history of previously approved forms and subinvestigator registrations.
 */
@Entity
@DiscriminatorValue("REVISED")
public class RevisedInvestigatorRegistration extends InvestigatorRegistration
implements Comparable<RevisedInvestigatorRegistration> {

    private static final long serialVersionUID = 1L;

    private Date revisionDate = new Date();
    private Set<SubInvestigatorRegistration> subinvestigatorRegistrationsRevised =
            new HashSet<SubInvestigatorRegistration>();

    private InvestigatorRegistration currentRegistrationRevision;

    @Override
    public void copyFrom(AbstractProtocolRegistration registration) {
        checkArgument(registration.isInvestigatorRegistration(),
                "Revised registration can only be copied from an investigator registration");
        super.copyFrom(registration);
        setCurrentRegistrationRevision((InvestigatorRegistration) registration);
    }

    /**
     * @return the current / main investigator registration that this revised registration belongs to.
     */
    @ManyToOne
    @NotNull
    @JoinColumn(name = "current_registration_revision_id")
    @ForeignKey(name = "current_registration_revision_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
        org.hibernate.annotations.CascadeType.REFRESH })
    public InvestigatorRegistration getCurrentRegistrationRevision() {
        return currentRegistrationRevision;
    }

    void setCurrentRegistrationRevision(InvestigatorRegistration currentRegistrationRevision) {
        this.currentRegistrationRevision = currentRegistrationRevision;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "revised_registration_subinvestigator_registration",
            joinColumns = @JoinColumn(name = "registration_id"),
            inverseJoinColumns = @JoinColumn(name = "subinvestigator_registration_id"))
    @ForeignKey(name = "revised_registration_fkey", inverseName = "subinvestigator_registration_fkey")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<SubInvestigatorRegistration> getSubinvestigatorRegistrationsRevised() {
        return subinvestigatorRegistrationsRevised;
    }

    @SuppressWarnings("unused") // Required by hibernate
    private void setSubinvestigatorRegistrationsRevised(
            Set<SubInvestigatorRegistration> subinvestigatorRegistrationsRevised) {
        this.subinvestigatorRegistrationsRevised = subinvestigatorRegistrationsRevised;
    }

    /**
     * @return the revisionDate
     */
    @Column(name = "revision_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRevisionDate() {
        return revisionDate;
    }

    @SuppressWarnings("unused")
    // Required by hibernate
    private void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    @Override
    @Transient
    public Set<SubInvestigatorRegistration> getSubinvestigatorRegistrations() {
        return getSubinvestigatorRegistrationsRevised();
    }

    @Override
    public int compareTo(RevisedInvestigatorRegistration registration) {
        return this.getStatusDate().compareTo(registration.getStatusDate());
    }

    /**
     * Cancels a registration revision.
     * @return The registration that was being revised
     */
    public InvestigatorRegistration cancelRevision() {
        InvestigatorRegistration registration = new InvestigatorRegistration();
        registration.copyFrom(this);
        updateSubinvestigatorRegistrationsPrimaryRegistration(registration);
        removeItemsThatShouldNotBeCascaded();

        return registration;
    }

    private void updateSubinvestigatorRegistrationsPrimaryRegistration(InvestigatorRegistration registration) {
        for (SubInvestigatorRegistration subInvestigatorRegistration : registration
                .getSubinvestigatorRegistrations()) {
            subInvestigatorRegistration.setPrimaryRegistration(registration);
        }
    }

    private void removeItemsThatShouldNotBeCascaded() {
        if (getCurrentRegistration().isInvestigatorRegistration()) {
            removeSubinvestigatorRegistrations((InvestigatorRegistration) getCurrentRegistration());
        }
        removeSubinvestigatorRegistrations(this);

        removePdfsFromForms(getCurrentRegistration());
        removePdfsFromForms(this);

        removeHumanResearchCertificates(getCurrentRegistration());
        removeHumanResearchCertificates(this);

        removeExistingSupportingDocumentation();
    }

    private void removeSubinvestigatorRegistrations(InvestigatorRegistration registration) {
        registration.getSubinvestigatorRegistrations().clear();
    }

    private void removePdfsFromForms(AbstractProtocolRegistration registration) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            form.setFlattenedPdf(null);
            form.setSignedPdf(null);
        }
    }

    private void removeHumanResearchCertificates(AbstractProtocolRegistration registration) {
        registration.getHumanResearchCertificateForm().getCertificates().clear();
    }

    private void removeExistingSupportingDocumentation() {
        getCurrentRegistration().getFinancialDisclosure().getSupportingDocumentation()
                .removeAll(getFinancialDisclosure().getSupportingDocumentation());
        getFinancialDisclosure().getSupportingDocumentation().clear();
    }

}
