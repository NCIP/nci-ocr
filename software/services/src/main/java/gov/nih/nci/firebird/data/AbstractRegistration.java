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

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.exception.ValidationException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.validator.NotNull;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Investigator registration to a Protocol.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "registration")
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.ExcessiveClassLength" })
//mostly setters/getters - reasonable public interface
public abstract class AbstractRegistration implements PersistentObject {

    private static final long serialVersionUID = 1L;

    private static final EnumSet<FormStatus> REVIEWED_STATES = EnumSet.of(FormStatus.ACCEPTED, FormStatus.REJECTED);
    static final EnumSet<RegistrationStatus> NOTIFICATION_REQUIRED_ON_UPDATE_STATUSES = EnumSet.complementOf(EnumSet
            .of(RegistrationStatus.INACTIVE, RegistrationStatus.NOT_STARTED, RegistrationStatus.APPROVED));
    static final EnumSet<RegistrationStatus> RESUBMIT_REQUIRED_ON_UPDATE_STATUSES = EnumSet.of(
            RegistrationStatus.SUBMITTED, RegistrationStatus.IN_REVIEW, RegistrationStatus.ACCEPTED);
    static final ImmutableSet<RegistrationStatus> LOCKED_STATUSES = Sets.immutableEnumSet(
            RegistrationStatus.SUBMITTED, RegistrationStatus.IN_REVIEW, RegistrationStatus.ACCEPTED,
            RegistrationStatus.APPROVED, RegistrationStatus.INACTIVE, RegistrationStatus.REVIEW_ON_HOLD,
            RegistrationStatus.DISQUALIFIED);
    static final EnumSet<RegistrationStatus> SUBMITTABLE_STATUSES = Sets.complementOf(LOCKED_STATUSES);
    private static final ImmutableSet<RegistrationStatus> REVIEW_COMPLETE_STATES = Sets.immutableEnumSet(
            RegistrationStatus.ACCEPTED, RegistrationStatus.RETURNED, RegistrationStatus.APPROVED);
    static final EnumSet<RegistrationStatus> REVIEWABLE_STATUSES = EnumSet.of(RegistrationStatus.SUBMITTED,
            RegistrationStatus.IN_REVIEW);

    /**
     * compare by profile's person name.
     *
     * @see Person#NAME_COMPARATOR
     */
    public static final Comparator<AbstractRegistration> INVESTIGATOR_NAME_COMPARATOR =
            new InvestigatorNameComparator();

    private Long id;
    private InvestigatorProfile profile;
    private RegistrationStatus status;
    private Date statusDate;
    private Date lastSubmissionDate;
    private Date approvalDate;
    private AdditionalAttachmentsForm additionalAttachmentsForm;
    private String sponsorComments;
    private String investigatorComments;
    private String coordinatorComments;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the status
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = RegistrationStatus.MAX_LENGTH)
    @Index(name = "registration_status_index")
    public RegistrationStatus getStatus() {
        return status;
    }

    /**
     * @param status the status.
     */
    public void setStatus(RegistrationStatus status) {
        setStatusDate(new Date());
        updateSubmissionDateIfNecessary(status);
        updateApprovalDateIfNecessary(status);
        this.status = status;
    }

    private void updateSubmissionDateIfNecessary(RegistrationStatus newStatus) {
        if (newStatus == RegistrationStatus.SUBMITTED) {
            setLastSubmissionDate(new Date());
        }
    }

    private void updateApprovalDateIfNecessary(RegistrationStatus newStatus) {
        if (newStatus == RegistrationStatus.APPROVED) {
            setApprovalDate(new Date());
        }
    }

    /**
     * @return the status date.
     */
    @NotNull
    @Column(name = "status_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStatusDate() {
        return statusDate;
    }

    /**
     * @param statusDate the status date.
     */
    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    /**
     * @return the status date.
     */
    @Column(name = "last_submission_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastSubmissionDate() {
        return lastSubmissionDate;
    }

    void setLastSubmissionDate(Date lastSubmissionDate) {
        this.lastSubmissionDate = lastSubmissionDate;
    }

    /**
     * @return if this registration has been approved then the date this registration was approved, otherwise null
     */
    @Column(name = "approval_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * @param approvalDate if this registration has been approved then the date this registration was approved,
     *            otherwise null
     */
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    /**
     * @return the investigator's profile, if known.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investigator_profile_id")
    @ForeignKey(name = "registration_investigatorprofile_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH })
    @NotNull
    public InvestigatorProfile getProfile() {
        return profile;
    }

    /**
     * @param profile investigator's profile.
     */
    public void setProfile(InvestigatorProfile profile) {
        this.profile = profile;
    }

    /**
     * @return the Additional Documents Form
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "additional_attachments_id")
    @ForeignKey(name = "registration_additional_attachments_fkey")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    public AdditionalAttachmentsForm getAdditionalAttachmentsForm() {
        return additionalAttachmentsForm;
    }

    /**
     * @param additionalAttachmentsForm the additionalAttachmentsForm to set
     */
    public void setAdditionalAttachmentsForm(AdditionalAttachmentsForm additionalAttachmentsForm) {
        this.additionalAttachmentsForm = additionalAttachmentsForm;
    }

    /**
     * @return the plain text sponsor comments.
     */
    @Transient
    public String getPlainTextSponsorComments() {
        return RichTextUtil.convertToPlainText(getSponsorComments());
    }

    /**
     * @return the plain text coordinator comments.
     */
    @Transient
    public String getPlainTextCoordinatorComments() {
        return RichTextUtil.convertToPlainText(getCoordinatorComments());
    }

    /**
     * @return user entered free-form text.
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "sponsor_comments")
    public String getSponsorComments() {
        return sponsorComments;
    }

    /**
     * @param sponsorComments user entered free-form text.
     */
    public void setSponsorComments(String sponsorComments) {
        this.sponsorComments = sponsorComments;
    }

    /**
     * @return Combined String of all the form's comments
     */
    @Transient
    public String getFormComments() {
        StringBuilder formComments = new StringBuilder();
        for (AbstractRegistrationForm registrationForm : getForms()) {
            formComments.append(buildRegistrationFormCommentBlock(registrationForm));
        }
        return formComments.toString();
    }

    private String buildRegistrationFormCommentBlock(AbstractRegistrationForm registrationForm) {
        String commentBlock = "";
        if (!StringUtils.isEmpty(registrationForm.getComments())
                && registrationForm.getFormStatus() == FormStatus.REJECTED) {
            String formTitle = RichTextUtil.buildBoldString(registrationForm.getFormType().getDescription());
            String formComments = RichTextUtil.cleanRichText(registrationForm.getComments());
            commentBlock = formTitle + formComments;
        }
        return commentBlock;
    }

    /**
     * @return user entered free-form text.
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "investigator_comments")
    public String getInvestigatorComments() {
        return investigatorComments;
    }

    /**
     * @param investigatorComments user entered free-form text.
     */
    public void setInvestigatorComments(String investigatorComments) {
        this.investigatorComments = investigatorComments;
    }

    /**
     * @return the user entered free-form text.
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "coordinator_comments")
    public String getCoordinatorComments() {
        return coordinatorComments;
    }

    /**
     * @param coordinatorComments the coordinatorComments to set
     */
    public void setCoordinatorComments(String coordinatorComments) {
        this.coordinatorComments = coordinatorComments;
    }

    /**
     * @return all forms to be included in the sponsor review.
     */
    @Transient
    public Set<AbstractRegistrationForm> getFormsForSponsorReview() {
        Set<AbstractRegistrationForm> forms = getForms();
        if (getAdditionalAttachmentsForm() != null && !getAdditionalAttachmentsForm().isAdditionalDocumentsUploaded()) {
            forms.remove(getAdditionalAttachmentsForm());
        }
        return forms;
    }

    /**
     * @return all forms associated with this registration.
     */
    @Transient
    public abstract Set<AbstractRegistrationForm> getForms();

    void addIfNotNull(Set<AbstractRegistrationForm> forms, AbstractRegistrationForm form) {
        if (form != null) {
            forms.add(form);
        }
    }

    /**
     * Convenience method to allow the subtypes of registration to grab the correct form configuration from the protocol
     * instead of needing to determine what type you are looking at first.
     *
     * @return the FormType / Optionality mapping of forms in the protocol.
     */
    @Transient
    public abstract Map<FormType, FormOptionality> getFormConfiguration();

    /**
     * @return all organizations associated with this registration.
     */
    @Transient
    public Set<Organization> getOrganizations() {
        Set<Organization> organizations = new HashSet<Organization>();
        for (AbstractRegistrationForm form : getForms()) {
            organizations.addAll(form.getOrganizations());
        }
        return organizations;
    }

    /**
     * @return all persons associated with this registration.
     */
    @Transient
    public Set<Person> getPersons() {
        Set<Person> persons = new HashSet<Person>();
        for (AbstractRegistrationForm form : getForms()) {
            persons.addAll(form.getPersons());
        }
        return persons;
    }

    /**
     * Validates all the forms in the registration.
     *
     * @param resources resource bundle with validation messages included
     *
     * @throws ValidationException if validation failures for any forms.
     */
    public void validate(ResourceBundle resources) throws ValidationException {
        validateForms(getForms(), resources);
    }

    private void validateForms(Iterable<AbstractRegistrationForm> forms, ResourceBundle resources)
            throws ValidationException {
        ValidationResult result = new ValidationResult();
        for (AbstractRegistrationForm form : forms) {
            validateFormIfNecessary(resources, result, form);
        }
        if (!result.isValid()) {
            throw new ValidationException(result);
        }
    }

    private void validateFormIfNecessary(ResourceBundle resources, ValidationResult result,
            AbstractRegistrationForm form) {
        if (form.requiresValidation()) {
            try {
                form.checkSubmissionReadiness(resources);
            } catch (ValidationException e) {
                result.addFailures(e.getResult());
            }
        } else {
            form.setFormStatus(FormStatus.COMPLETED);
        }
    }

    /**
     * Validates all the forms in the registration that aren't already reviewed or revised.
     *
     * @param resources resource bundle with validation messages included
     *
     * @throws ValidationException if validation failures for any forms.
     */
    public void validateUnReviewedAndUnRevisedForms(ResourceBundle resources) throws ValidationException {
        validateForms(getUnReviewedAndUnRevisedForms(), resources);
    }

    @Transient
    Iterable<AbstractRegistrationForm> getUnReviewedAndUnRevisedForms() {
        return Iterables.filter(getForms(), new Predicate<AbstractRegistrationForm>() {
            @Override
            public boolean apply(AbstractRegistrationForm form) {
                return !form.isReviewed() && form.getFormStatus() != FormStatus.REVISED;
            }
        });
    }

    /**
     * comparator for a profile.investigator by last name, then first name.
     */
    private static final class InvestigatorNameComparator implements Comparator<AbstractRegistration> {
        @Override
        public int compare(AbstractRegistration r1, AbstractRegistration r2) {
            return InvestigatorProfile.INVESTIGATOR_NAME_COMPARATOR.compare(r1.getProfile(), r2.getProfile());
        }
    }

    /**
     * Ensures that all the various registrations forms used in this registration have been properly created and
     * initialized.
     *
     * @return set of removed forms
     */
    public Set<AbstractRegistrationForm> configureForms() {
        Set<AbstractRegistrationForm> removedForms = removeFormsWhichAreNoLongerNecessary();
        for (Entry<FormType, FormOptionality> form : getFormConfiguration().entrySet()) {
            if (shouldConfigure(form)) {
                configureForm(form.getKey());
            }
        }
        return removedForms;
    }

    private Set<AbstractRegistrationForm> removeFormsWhichAreNoLongerNecessary() {
        Set<AbstractRegistrationForm> removedForms = Sets.newHashSet();
        for (AbstractRegistrationForm form : getForms()) {
            FormOptionality formOptionality = getFormConfiguration().get(form.getFormType());
            if (formOptionality == null || formOptionality == FormOptionality.NONE) {
                removedForms.add(removeForm(form.getFormType()));
            }
        }
        return removedForms;
    }

    private boolean shouldConfigure(Entry<FormType, FormOptionality> form) {
        return form.getKey().isStandard() && form.getValue().shouldConfigure() && getForm(form.getKey()) == null;
    }

    abstract void configureForm(FormType formType);

    abstract AbstractRegistrationForm removeForm(FormType formType);

    /**
     * Retrieves a form by type.
     *
     * @param formType get this form type
     * @return the form or null if none found for this type
     */
    public AbstractRegistrationForm getForm(FormType formType) {
        for (AbstractRegistrationForm form : getForms()) {
            if (formType.equals(form.getFormType())) {
                return form;
            }
        }
        return null;
    }

    /**
     * Retrieves a form by type enum.
     *
     * @param formType get this form type
     * @return the form or null if none found for this type
     */
    public AbstractRegistrationForm getForm(FormTypeEnum formType) {
        for (AbstractRegistrationForm form : getForms()) {
            if (form.getFormType().getFormTypeEnum() == formType) {
                return form;
            }
        }
        return null;
    }

    /**
     * Locates a form within this registration when provided the id.
     *
     * @param formId the id of the form to be located.
     * @return the found form or null if none is found.
     */
    public AbstractRegistrationForm findFormById(Long formId) {
        for (AbstractRegistrationForm form : getForms()) {
            if (ObjectUtils.equals(form.getId(), formId)) {
                return form;
            }
        }
        return null;
    }

    /**
     * @return true if the owner is no longer allowed to modify this registration.
     */
    @Transient
    public boolean isLockedForInvestigator() {
        return LOCKED_STATUSES.contains(getStatus());
    }

    /**
     * @return whether the Registration is in a submittable state.
     */
    @Transient
    public boolean isSubmittable() {
        return SUBMITTABLE_STATUSES.contains(getStatus());
    }

    /**
     * @return whether the Registration is in a completeable state.
     */
    @Transient
    public boolean isCompleteable() {
        if (RegistrationStatus.IN_REVIEW.equals(status)) {
            for (AbstractRegistrationForm form : getForms()) {
                if (!isFormCompletable(form)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @return whether the Registration is in a reviewable state.
     */
    @Transient
    public boolean isReviewable() {
        return REVIEWABLE_STATUSES.contains(getStatus());
    }

    private boolean isFormCompletable(AbstractRegistrationForm form) {
        if (form.isReviewRequired()) {
            boolean isFormSupplementary = form.getFormOptionality() == FormOptionality.SUPPLEMENTARY;
            boolean isFormNotReviewed = !REVIEWED_STATES.contains(form.getFormStatus());
            boolean isFormRejectedWithoutComment = FormStatus.REJECTED.equals(form.getFormStatus())
                    && StringUtils.isEmpty(form.getComments());
            if (!isFormSupplementary && (isFormNotReviewed || isFormRejectedWithoutComment)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return whether the review on this registration has been completed or not.
     */
    @Transient
    public boolean isCompleted() {
        return REVIEW_COMPLETE_STATES.contains(status);
    }

    /**
     * Indicates whether a particular revision to a protocol requires notification of the
     * investigator for this registration in order to revise and resign forms.
     *
     * @return true if action is required by the investigator
     */
    @Transient
    public boolean isNotificationRequiredForUpdate() {
        return NOTIFICATION_REQUIRED_ON_UPDATE_STATUSES.contains(getStatus());
    }

    /**
     * Indicates whether a particular revision to a protocol requires that the investigator re-signs and submits the
     * registration.
     *
     * @return true if re sign and submission is required by the investigator
     */
    @Transient
    public boolean isResubmitRequiredForUpdate() {
        return RESUBMIT_REQUIRED_ON_UPDATE_STATUSES.contains(getStatus());
    }

    /**
     * Returns whether or not the review of this registration is on hold.
     *
     * @return true if the review of this registration is on hold
     */
    @Transient
    public boolean isReviewOnHold() {
        return RegistrationStatus.REVIEW_ON_HOLD == getStatus();
    }

    /**
     * @return Form FDA 1572 needed for this registration.
     */
    @Transient
    public abstract Form1572 getForm1572();

    /**
     * @return Financial Disclosure needed for this registration.
     */
    @Transient
    public abstract FinancialDisclosure getFinancialDisclosure();

    /**
     * @return Sponsor for this registration
     */
    @Transient
    public abstract Organization getSponsor();

    /**
     * Sets all form statuses to the value given.
     *
     * @param formStatus new status value for all forms
     */
    public void setFormStatuses(FormStatus formStatus) {
        for (AbstractRegistrationForm form : getForms()) {
            form.setFormStatus(formStatus);
        }
    }

    /**
     * Prepares all forms in this registration for return.
     *
     * @return obsolete objects removed from forms that should be removed from the database
     */
    public Set<PersistentObject> prepareFormsForReturn() {
        Set<PersistentObject> objectsToDelete = Sets.newHashSet();
        for (AbstractRegistrationForm form : getForms()) {
            objectsToDelete.addAll(form.returnForm());
        }
        return objectsToDelete;
    }

    boolean isFileReferenced(FirebirdFile file) {
        for (AbstractRegistrationForm form : getForms()) {
            if (form.isFileReferenced(file)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Unlinks this registration from any referencing objects so that it may be deleted cleanly. This method
     * should be overriden in subclasses to add additional unlinking functionality.
     */
    public void prepareForDeletion() {
        getProfile().removeRegistration(this);
    }

    @Transient
    public boolean isAnnualRegistration() {
        return this instanceof AnnualRegistration;
    }

    /**
     * Clears out all forms comments which aren't rejected.
     */
    public void clearNonRejectedFormsComments() {
        for (AbstractRegistrationForm form : getForms()) {
            if (form.getFormStatus() != FormStatus.REJECTED) {
                form.setComments(null);
            }
        }
    }

}
