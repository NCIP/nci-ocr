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

import gov.nih.nci.firebird.common.FirebirdCollectionUtils;

import java.util.EnumSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.NotNull;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * Investigator registration to a Protocol.
 */
@Entity
@SuppressWarnings("PMD.TooManyMethods")
// mainly getters and setters
public abstract class AbstractProtocolRegistration extends AbstractRegistration {

    private static final long serialVersionUID = 1L;

    /**
     * Predicate for use in filtering Collections of Registrations to only get Invited ones.
     */
    protected static final Predicate<AbstractProtocolRegistration> IS_INVITED_PREDICATE =
            new Predicate<AbstractProtocolRegistration>() {
        @Override
        public boolean apply(AbstractProtocolRegistration input) {
            return input.getInvitation().getInvitationStatus() != InvitationStatus.NOT_INVITED;
        }
    };

    static final EnumSet<RegistrationStatus> REVISABLE_STATUSES = EnumSet.of(RegistrationStatus.ACCEPTED,
            RegistrationStatus.APPROVED, RegistrationStatus.IN_REVIEW, RegistrationStatus.SUBMITTED);

    private Protocol protocol;
    private Invitation invitation = new Invitation(this);
    private ProtocolForm1572 form1572;
    private ProtocolFinancialDisclosure financialDisclosure;
    private CurriculumVitaeForm curriculumVitaeForm;
    private HumanResearchCertificateForm humanResearchCertificateForm;
    private AbstractProtocolRegistration currentRegistration;

    private Set<AbstractProtocolRegistration> parentRegistrations = Sets.newHashSet();

    /**
     * @return the protocol the investigator is registered to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "protocol_id")
    @ForeignKey(name = "registration_protocol_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH })
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol the investigator is registered to.
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    /**
     * @return investigator invitation to this registration.
     */
    @Embedded
    @NotNull
    public Invitation getInvitation() {
        return invitation;
    }

    @SuppressWarnings("unused")
    // setter required by hibernate
    private void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "form_1572_id")
    @ForeignKey(name = "registration_form_1572_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL })
    @Override
    public ProtocolForm1572 getForm1572() {
        return form1572;
    }

    /**
     * @param form1572 the form1572 to set
     */
    public void setForm1572(ProtocolForm1572 form1572) {
        this.form1572 = form1572;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_disclosure_id")
    @ForeignKey(name = "registration_financial_disclosure_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL })
    @Override
    public ProtocolFinancialDisclosure getFinancialDisclosure() {
        return financialDisclosure;
    }

    /**
     * @param financialDisclosure the financial disclosure to set
     */
    public void setFinancialDisclosure(ProtocolFinancialDisclosure financialDisclosure) {
        this.financialDisclosure = financialDisclosure;
    }

    /**
     * @return the curriculumVitaeForm
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_vitae_id")
    @ForeignKey(name = "registration_curriculum_vitae_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL })
    public CurriculumVitaeForm getCurriculumVitaeForm() {
        return curriculumVitaeForm;
    }

    /**
     * @param curriculumVitaeForm the curriculumVitaeForm to set
     */
    public void setCurriculumVitaeForm(CurriculumVitaeForm curriculumVitaeForm) {
        this.curriculumVitaeForm = curriculumVitaeForm;
    }

    /**
     * @return the humanResearchCertificateForm
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "human_research_certificate_form_id")
    @ForeignKey(name = "registration_human_research_certificate_form_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL })
    public HumanResearchCertificateForm getHumanResearchCertificateForm() {
        return humanResearchCertificateForm;
    }

    /**
     * @param humanResearchCertificateForm the humanResearchCertificateForm to set
     */
    public void setHumanResearchCertificateForm(HumanResearchCertificateForm humanResearchCertificateForm) {
        this.humanResearchCertificateForm = humanResearchCertificateForm;
    }

    /**
     * @return the current / main investigator registration that this registration was copied from.
     */
    @ManyToOne
    @JoinColumn(name = "current_registration_id")
    @ForeignKey(name = "current_registration_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH })
    public AbstractProtocolRegistration getCurrentRegistration() {
        return currentRegistration;
    }

    /**
     * @param currentRegistration the current / main investigator registration that this registration was copied from.
     */
    public void setCurrentRegistration(AbstractProtocolRegistration currentRegistration) {
        this.currentRegistration = currentRegistration;
    }

    /**
     * @return the parentRegistrations
     */
    @OneToMany(mappedBy = "currentRegistration")
    public Set<AbstractProtocolRegistration> getParentRegistrations() {
        return parentRegistrations;
    }

    /**
     * @param parentRegistrations the parentRegistrations to set
     */
    public void setParentRegistrations(Set<AbstractProtocolRegistration> parentRegistrations) {
        this.parentRegistrations = parentRegistrations;
    }

    /**
     * @return all forms associated with this registration.
     */
    @Transient
    @Override
    public Set<AbstractRegistrationForm> getForms() {
        Set<AbstractRegistrationForm> forms = Sets.newLinkedHashSet();
        FirebirdCollectionUtils.addIgnoreNull(forms, getForm1572());
        FirebirdCollectionUtils.addIgnoreNull(forms, getFinancialDisclosure());
        FirebirdCollectionUtils.addIgnoreNull(forms, getCurriculumVitaeForm());
        FirebirdCollectionUtils.addIgnoreNull(forms, getHumanResearchCertificateForm());
        FirebirdCollectionUtils.addIgnoreNull(forms, getAdditionalAttachmentsForm());
        return forms;
    }

    @Override
    void configureForm(FormType formType) {
        switch (formType.getFormTypeEnum()) {
        case FORM_1572:
            setForm1572(new ProtocolForm1572(this, formType));
            break;
        case FINANCIAL_DISCLOSURE_FORM:
            setFinancialDisclosure(new ProtocolFinancialDisclosure(this, formType));
            break;
        case CV:
            setCurriculumVitaeForm(new CurriculumVitaeForm(this, formType));
            break;
        case HUMAN_RESEARCH_CERTIFICATE:
            setHumanResearchCertificateForm(new HumanResearchCertificateForm(this, formType));
            break;
        case ADDITIONAL_ATTACHMENTS:
            setAdditionalAttachmentsForm(new AdditionalAttachmentsForm(this, formType));
            break;
        default:
            throw new IllegalArgumentException("Invalid FormType: " + formType.getFormTypeEnum());
        }
    }

    @Override
    AbstractRegistrationForm removeForm(FormType formType) {
        AbstractRegistrationForm removedForm = getForm(formType);
        switch (formType.getFormTypeEnum()) {
        case FORM_1572:
            setForm1572(null);
            break;
        case FINANCIAL_DISCLOSURE_FORM:
            setFinancialDisclosure(null);
            break;
        case CV:
            setCurriculumVitaeForm(null);
            break;
        case HUMAN_RESEARCH_CERTIFICATE:
            setHumanResearchCertificateForm(null);
            break;
        case ADDITIONAL_ATTACHMENTS:
            setAdditionalAttachmentsForm(null);
            break;
        default:
            throw new IllegalArgumentException("Invalid FormType: " + formType.getFormTypeEnum());
        }
        return removedForm;
    }

    /**
     * Returns the <code>InvestigatorRegistration</code> for the packet this registration belongs to. This may be the
     * current object or, if a <code>SubinvestigatorRegistration</code> the parent <code>InvestigatorRegistration</code>
     * .
     *
     * @return the investigator registration.
     */
    @Transient
    public abstract InvestigatorRegistration getPrimaryRegistration();

    /**
     * @return true if the registration to display is the primary registration.
     */
    @Transient
    public boolean isInvestigatorRegistration() {
        return (this instanceof InvestigatorRegistration);
    }

    @Override
    @Transient
    public Organization getSponsor() {
        return getProtocol().getSponsor();
    }

    /**
     * @return whether or not this registration is allowed to have a revision initiated
     */
    @Transient
    public boolean isRevisable() {
        return isInvestigatorRegistration() && !isRevisedInvestigatorRegistration()
                && REVISABLE_STATUSES.contains(getStatus());
    }

    /**
     * @return true if this is a RevisedInvestigatorRegistration.
     */
    @Transient
    public boolean isRevisedInvestigatorRegistration() {
        return (this instanceof RevisedInvestigatorRegistration);
    }

    /**
     * @return whether or not this registration is a registration revision and is able to be canceled
     */
    @Transient
    public boolean isCancelable() {
        if (!isInvestigatorRegistration()) {
            return false;
        }
        RevisedInvestigatorRegistration revisedRegistration = Iterables.getFirst(
                ((InvestigatorRegistration) this).getRevisedRegistrations(), null);
        return revisedRegistration != null
                && revisedRegistration.getLastSubmissionDate().equals(getLastSubmissionDate());
    }

    @Override
    public void prepareForDeletion() {
        super.prepareForDeletion();
        getProtocol().removeRegistration(this);
    }

    /**
     * @return whether or not this registration has been approved and has been reactivated
     */
    @Transient
    public boolean isApprovedWithReactivatedRegistration() {
        if (getApprovalDate() == null || getCurrentRegistration() == null) {
            return false;
        }
        return getCurrentRegistration().getInvitation().getInvitationStatus() == InvitationStatus.REACTIVATED;
    }

    /**
     * @return whether or not this registration is a reactivation of an approved registration
     */
    @Transient
    public boolean isApprovedRegistrationReactivation() {
        if (getInvitation().getInvitationStatus() != InvitationStatus.REACTIVATED) {
            return false;
        }
        return Iterables.any(getParentRegistrations(), new Predicate<AbstractProtocolRegistration>() {
            public boolean apply(AbstractProtocolRegistration registration) {
                return registration.getApprovalDate() != null
                        && registration.getStatus() == RegistrationStatus.INACTIVE;
            }
        });
    }

    /**
     * @param registration registration to copy from
     */
    public void copyFrom(AbstractProtocolRegistration registration) {
        copyRelationships(registration);
        copyAttributes(registration);
        copyForms(registration);
    }

    private void copyRelationships(AbstractProtocolRegistration registration) {
        registration.getProfile().addRegistration(this);
        this.setProtocol(registration.getProtocol());
        this.getInvitation().setInvitationStatus(registration.getInvitation().getInvitationStatus());
        this.getInvitation().setInvitationChangeDate(registration.getInvitation().getInvitationChangeDate());
        if (this.isInvestigatorRegistration()) {
            ((InvestigatorRegistration) this).getSubinvestigatorRegistrations().addAll(
                    ((InvestigatorRegistration) registration).getSubinvestigatorRegistrations()); // clean this
        }
    }

    private void copyAttributes(AbstractProtocolRegistration registration) {
        this.setStatus(registration.getStatus());
        this.setStatusDate(registration.getStatusDate());
        this.setLastSubmissionDate(registration.getLastSubmissionDate());
        this.setCoordinatorComments(registration.getCoordinatorComments());
        this.setInvestigatorComments(registration.getInvestigatorComments());
        this.setSponsorComments(registration.getSponsorComments());
    }

    private void copyForms(AbstractProtocolRegistration registration) {
        if (registration.getCurriculumVitaeForm() != null) {
            registration.getCurriculumVitaeForm().copyTo(this);
        }
        if (registration.getFinancialDisclosure() != null) {
            registration.getFinancialDisclosure().copyTo(this);
        }
        if (registration.getHumanResearchCertificateForm() != null) {
            registration.getHumanResearchCertificateForm().copyTo(this);
        }
        if (registration.getForm1572() != null) {
            registration.getForm1572().copyTo(this);
        }
        if (registration.getAdditionalAttachmentsForm() != null) {
            registration.getAdditionalAttachmentsForm().copyTo(this);
        }
    }

}
