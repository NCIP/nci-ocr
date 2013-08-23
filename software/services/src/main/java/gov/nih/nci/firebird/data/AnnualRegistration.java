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
import gov.nih.nci.firebird.data.user.ManagedInvestigator;

import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.validator.NotNull;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

/**
 * An investigator's annual registration.
 */
@Entity
@DiscriminatorValue("ANNUAL")
@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.TooManyMethods" })
// switch statements for form handling must handle possible cases
// methods broken down for clarity
public class AnnualRegistration extends AbstractRegistration {

    private static final long serialVersionUID = 1L;

    private AnnualRegistrationConfiguration configuration;
    private AnnualRegistrationType annualRegistrationType;
    private Date dueDate;
    private Date renewalDate;
    private AnnualRegistrationForm1572 form1572;
    private CtepFinancialDisclosure financialDisclosure;
    private SupplementalInvestigatorDataForm supplementalInvestigatorDataForm;
    private SortedSet<String> notificationEmailAddresses = new TreeSet<String>();
    private boolean secondRenewalNotificationSent;
    private AnnualRegistration renewal;
    private boolean approvalAcknowledgedByInvestigator;
    private boolean approvalAcknowledgedByCoordinator;
    private AnnualRegistration parent;
    private boolean renewed;

    private static final EnumSet<RegistrationStatus> FINALIZED_STATES = EnumSet.of(RegistrationStatus.APPROVED);

    /**
     * @return the configuration
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "configuration_id")
    @ForeignKey(name = "registration_configuration_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH })
    public AnnualRegistrationConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(AnnualRegistrationConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * @return the annualRegistrationType
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "annual_registration_type", length = AnnualRegistrationType.MAX_LENGTH)
    public AnnualRegistrationType getAnnualRegistrationType() {
        return annualRegistrationType;
    }

    /**
     * @param annualRegistrationType the annualRegistrationType to set
     */
    public void setAnnualRegistrationType(AnnualRegistrationType annualRegistrationType) {
        this.annualRegistrationType = annualRegistrationType;
    }

    /**
     * @return the dueDate
     */
    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the renewal date
     */
    @Column(name = "renewal_date")
    @Temporal(TemporalType.DATE)
    public Date getRenewalDate() {
        return renewalDate;
    }

    /**
     * @param renewalDate the renewalDate
     */
    public void setRenewalDate(Date renewalDate) {
        this.renewalDate = renewalDate;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "annual_form_1572_id")
    @ForeignKey(name = "registration_annual_form_1572_fkey")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Override
    public AnnualRegistrationForm1572 getForm1572() {
        return form1572;
    }

    /**
     * @param form1572 the form1572 to set
     */
    public void setForm1572(AnnualRegistrationForm1572 form1572) {
        this.form1572 = form1572;
    }

    /**
     * @return the Financial Disclosure data object
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ctep_financial_disclosure_id")
    @ForeignKey(name = "registration_ctep_financial_disclosure_fkey")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    public CtepFinancialDisclosure getFinancialDisclosure() {
        return financialDisclosure;
    }

    /**
     * @param financialDisclosure the financial disclosure to set
     */
    public void setFinancialDisclosure(CtepFinancialDisclosure financialDisclosure) {
        this.financialDisclosure = financialDisclosure;
    }

    /**
     * @return the supplementalInvestigatorDataForm
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "supplemental_investigator_data_form_id")
    @ForeignKey(name = "registration_supplemental_investigator_data_form_fkey")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    public SupplementalInvestigatorDataForm getSupplementalInvestigatorDataForm() {
        return supplementalInvestigatorDataForm;
    }

    /**
     * @param supplementalInvestigatorDataForm the supplementalInvestigatorDataForm to set
     */
    public void setSupplementalInvestigatorDataForm(SupplementalInvestigatorDataForm supplementalInvestigatorDataForm) {
        this.supplementalInvestigatorDataForm = supplementalInvestigatorDataForm;
    }

    @Override
    @Transient
    public Set<AbstractRegistrationForm> getForms() {
        Set<AbstractRegistrationForm> forms = Sets.newLinkedHashSet();
        FirebirdCollectionUtils.addIgnoreNull(forms, getForm1572());
        FirebirdCollectionUtils.addIgnoreNull(forms, getFinancialDisclosure());
        FirebirdCollectionUtils.addIgnoreNull(forms, getSupplementalInvestigatorDataForm());
        FirebirdCollectionUtils.addIgnoreNull(forms, getAdditionalAttachmentsForm());
        return forms;
    }

    @Override
    @Transient
    public Map<FormType, FormOptionality> getFormConfiguration() {
        return getConfiguration().getFormSetConfiguration().getFormOptionalities();
    }

    /**
     * @return the notificationEmailAddresses
     */
    @CollectionOfElements
    @JoinTable(name = "annual_registration_notification_email_addresses",
               joinColumns = @JoinColumn(name = "registration_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Column(name = "email_address", nullable = false)
    @ForeignKey(name = "annual_registration_notification_email_addresses_registration_fkey")
    @Sort(type = SortType.NATURAL)
    public SortedSet<String> getNotificationEmailAddresses() {
        return notificationEmailAddresses;
    }

    @SuppressWarnings("unused")
    // setter required by Hibernate
    private void setNotificationEmailAddresses(SortedSet<String> notificationEmailAddresses) {
        this.notificationEmailAddresses = notificationEmailAddresses;
    }

    /**
     * @return the renewal
     */
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "annual_registration_renewal_id")
    @ForeignKey(name = "annual_registration_renewal_fkey")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    public AnnualRegistration getRenewal() {
        return renewal;
    }

    /**
     * @param renewal the renewal
     */
    public void setRenewal(AnnualRegistration renewal) {
        this.renewal = renewal;
        setRenewed(true);
        if (renewal != null) {
            renewal.parent = this;
        }
    }

    /**
     * @param renewed the renewed to set
     */
    private void setRenewed(boolean renewed) {
        this.renewed = renewed;
    }

    /**
     * @return the renewed
     */
    @Column(name = "renewed")
    public boolean isRenewed() {
        return renewed;
    }

    @OneToOne(mappedBy = "renewal")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public AnnualRegistration getParent() {
        return parent;
    }

    /**
     * @param parent the parent registration to set
     */
    public void setParent(AnnualRegistration parent) {
        this.parent = parent;
        if (parent != null) {
            parent.renewal = this;
        }
    }

    @Override
    void configureForm(FormType formType) {
        switch (formType.getFormTypeEnum()) {
        case CTEP_FORM_1572:
            setForm1572(new AnnualRegistrationForm1572(this, formType));
            break;
        case CTEP_FINANCIAL_DISCLOSURE_FORM:
            setFinancialDisclosure(new CtepFinancialDisclosure(this, formType));
            break;
        case SUPPLEMENTAL_INVESTIGATOR_DATA_FORM:
            setSupplementalInvestigatorDataForm(new SupplementalInvestigatorDataForm(this, formType));
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
        case CTEP_FORM_1572:
            setForm1572(null);
            break;
        case CTEP_FINANCIAL_DISCLOSURE_FORM:
            setFinancialDisclosure(null);
            break;
        case SUPPLEMENTAL_INVESTIGATOR_DATA_FORM:
            setSupplementalInvestigatorDataForm(null);
            break;
        case ADDITIONAL_ATTACHMENTS:
            setAdditionalAttachmentsForm(null);
            break;
        default:
            throw new IllegalArgumentException("Invalid FormType: " + formType.getFormTypeEnum());
        }
        return removedForm;
    }

    @Override
    @Transient
    public Organization getSponsor() {
        return getConfiguration().getSponsor();
    }

    /**
     * @return list containing the email addresses of all the investigator's approved registration coordinators and
     *         notification list email addresses
     */
    @Transient
    public Set<String> getApprovedCoordinatorAndNotificationListEmailAddresses() {
        Set<String> coordinatorAndNotificationListEmailAddresses = Sets.newHashSet();
        coordinatorAndNotificationListEmailAddresses.addAll(getApprovedRegistrationCoordinatorEmailAddresses());
        coordinatorAndNotificationListEmailAddresses.addAll(getNotificationEmailAddresses());
        return coordinatorAndNotificationListEmailAddresses;
    }

    @Transient
    private Set<String> getApprovedRegistrationCoordinatorEmailAddresses() {
        Set<String> coordinatorEmailAddresses = Sets.newHashSet();
        for (ManagedInvestigator managedInvestigator : getProfile().getRegistrationCoordinatorMappings()) {
            if (managedInvestigator.isApproved()) {
                coordinatorEmailAddresses.add(managedInvestigator.getUser().getPerson().getEmail());
            }
        }
        return coordinatorEmailAddresses;
    }

    /**
     * @return whether or not this registration is ready for renewal.
     */
    @Transient
    public boolean isPendingRenewal() {
        return getLastSubmissionDate() == null && getDueDate() != null;
    }

    /**
     *
     * @return whether or not this registration is ready for approved.
     */
    @Transient
    public boolean isApprovable() {
        return RegistrationStatus.ACCEPTED.equals(getStatus());
    }

    /**
     * @return whether or not the second notification has been emailed
     */
    @Column(name = "second_renewal_notification_sent")
    public boolean isSecondRenewalNotificationSent() {
        return secondRenewalNotificationSent;
    }

    /**
     * @param secondRenewalNotificationSent the secondRenewalNotificationSent to set
     */
    public void setSecondRenewalNotificationSent(boolean secondRenewalNotificationSent) {
        this.secondRenewalNotificationSent = secondRenewalNotificationSent;
    }

    /**
     * @return whether or not the investigator has acknowledged the sponsors approval of the registration (opened the
     *         task list item)
     */
    @Column(name = "approval_acknowledged_by_investigator")
    public boolean isApprovalAcknowledgedByInvestigator() {
        return approvalAcknowledgedByInvestigator;
    }

    /**
     * @param approvalAcknowledgedByInvestigator whether or not the investigator has acknowledged the sponsors approval
     *            of the registration (opened the task list item)
     */
    public void setApprovalAcknowledgedByInvestigator(boolean approvalAcknowledgedByInvestigator) {
        this.approvalAcknowledgedByInvestigator = approvalAcknowledgedByInvestigator;
    }

    /**
     * @return whether or not the coordinator has acknowledged the sponsors approval of the registration (opened the
     *         task list item)
     */
    @Column(name = "approval_acknowledged_by_coordinator")
    public boolean isApprovalAcknowledgedByCoordinator() {
        return approvalAcknowledgedByCoordinator;
    }

    /**
     * @param approvalAcknowledgedByCoordinator whether or not the coordinator has acknowledged the sponsors approval of
     *            the registration (opened the task list item)
     */
    public void setApprovalAcknowledgedByCoordinator(boolean approvalAcknowledgedByCoordinator) {
        this.approvalAcknowledgedByCoordinator = approvalAcknowledgedByCoordinator;
    }

    /**
     * @return whether or not this registration is finalized (accepted or suspended)
     */
    @Transient
    public boolean isFinalized() {
        return FINALIZED_STATES.contains(getStatus());
    }

    /**
     * Returns all signed PDFs as well as any uploaded supported documentation.
     * @return documents for review
     */
    @Transient
    public Set<FirebirdFile> getDocumentsForReview() {
        Set<FirebirdFile> documentsForReview = Sets.newHashSet();
        getSignedFormPdfs(documentsForReview);
        documentsForReview.addAll(getAdditionalAttachmentsForm().getAdditionalAttachments());
        documentsForReview.addAll(getClinicalLabCertificateFiles());
        documentsForReview.addAll(getFinancialDisclosure().getSupportingDocumentation());
        return documentsForReview;
    }

    @Transient
    private void getSignedFormPdfs(Set<FirebirdFile> documentsForReview) {
        for (AbstractRegistrationForm form : getFormsForSponsorReview()) {
            if (form.isSignable()) {
                documentsForReview.add(form.getFileToReview());
            }
        }
    }

    @Transient
    private Collection<FirebirdFile> getClinicalLabCertificateFiles() {
        return Collections2.transform(getForm1572().getClinicalLabCertificates(),
                new Function<LaboratoryCertificate, FirebirdFile>() {
                    @Override
                    public FirebirdFile apply(LaboratoryCertificate certificate) {
                        return certificate.getCertificateFile();
                    }
                });
    }

    @Override
    public void prepareForDeletion() {
        super.prepareForDeletion();
        if (getParent() != null) {
            getParent().setRenewal(null);
            setParent(null);
        }
    }

}
