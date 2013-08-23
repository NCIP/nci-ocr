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

import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.exception.ValidationException;

import java.text.MessageFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.NotNull;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.common.collect.Sets;

/**
 * Base class for forms filled out by users (typically registration forms).
 */
@MappedSuperclass
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.ExcessiveClassLength" })
// Mainly getters and setters
public abstract class AbstractRegistrationForm implements PersistentObject {

    private static final long serialVersionUID = 1L;

    private static final String PDF_EXTENSION = ".pdf";
    static final EnumSet<FormStatus> SUBMITTABLE_STATUSES = EnumSet.of(FormStatus.SUBMITTED, FormStatus.COMPLETED,
            FormStatus.ACCEPTED, FormStatus.REJECTED, FormStatus.REVISED);
    static final EnumSet<FormStatus> CURRENTLY_REVIEWABLE_STATUSES = EnumSet.of(FormStatus.IN_REVIEW,
            FormStatus.ACCEPTED, FormStatus.REJECTED);
    static final EnumSet<FormStatus> REVIEWED_STATUSES = EnumSet.of(FormStatus.ACCEPTED, FormStatus.REJECTED);
    static final Set<FormTypeEnum> FORMS_WITH_COLLECTION_OF_DOCUMENTS = EnumSet.of(
            FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE);

    private Long id;
    private FormStatus formStatus = FormStatus.NOT_STARTED;
    private Date formStatusDate = new Date();
    private FormType formType;
    private FirebirdFile signedPdf;
    private FirebirdFile flattenedPdf;
    private String comments;
    private final Set<Long> invalidEntityIds = Sets.newHashSet();

    AbstractRegistrationForm() {
        // no-arg constructor required by Hibernate
    }

    AbstractRegistrationForm(FormType formType) {
        setFormType(formType);
    }

    AbstractRegistrationForm(FormType formType, FormStatus formStatus) {
        setFormType(formType);
        this.formStatus = formStatus;
    }

    /**
     * @return all organizations referenced in this form.
     */
    @Transient
    public Set<Organization> getOrganizations() {
        Set<Organization> organizations = Sets.newHashSet();
        if (getRegistration().getProfile().getPrimaryOrganization() != null) {
            organizations.add(getRegistration().getProfile().getPrimaryOrganization().getOrganization());
        }
        return organizations;
    }

    /**
     * @return all organizations referenced in this form.
     */
    @Transient
    public abstract Set<Person> getPersons();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id db id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return whether or not this form has additional documents.
     */
    @Transient
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    // As a team we decided this should be here
    public boolean isAdditionalDocumentsUploaded() {
        return false;
    }

    /**
     * @return the number of additional documents added to this form.
     */
    @Transient
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    // As a team we decided this should be here
    public int getNumberOfAdditionalDocuments() {
        return 0;
    }

    /**
     * @return form status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "form_status")
    public FormStatus getFormStatus() {
        return formStatus;
    }

    /**
     * @param formStatus how the form status will show.
     */
    public void setFormStatus(FormStatus formStatus) {
        if (formStatus != this.formStatus) {
            setFormStatusDate(new Date());
            this.formStatus = formStatus;
        }
    }

    /**
     * @return the status date.
     */
    @NotNull
    @Column(name = "status_date")
    public Date getFormStatusDate() {
        return formStatusDate;
    }

    /**
     * @param formStatusDate formStatusDate to set
     */
    protected void setFormStatusDate(Date formStatusDate) {
        this.formStatusDate = formStatusDate;
    }

    /**
     * @param formType the formType to set
     */
    private void setFormType(FormType formType) {
        this.formType = formType;
    }

    /**
     * @return the formType
     */
    @ManyToOne(optional = false,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ForeignKey(name = "form_form_type_fkey")
    @JoinColumn(name = "form_type_id")
    public FormType getFormType() {
        return formType;
    }

    /**
     * @return the registration to the protocol
     */
    @Transient
    public abstract AbstractRegistration getRegistration();

    /**
     * @return the investigator associated with the form.
     */
    @Transient
    public Person getInvestigator() {
        return getRegistration().getProfile().getPerson();
    }

    /**
     * @return the signedPdf
     */
    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "signed_pdf_id")
    @ForeignKey(name = "registration_form_signed_pdf_fkey")
    public FirebirdFile getSignedPdf() {
        return signedPdf;
    }

    /**
     * @param signedPdf the signedPdf to set
     */
    public void setSignedPdf(FirebirdFile signedPdf) {
        this.signedPdf = signedPdf;
    }

    /**
     * @return the flattenedPdf
     */
    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "flattened_pdf_id")
    @ForeignKey(name = "registration_form_flattened_pdf_fkey")
    public FirebirdFile getFlattenedPdf() {
        return flattenedPdf;
    }

    /**
     * @param flattenedPdf the flattenedPdf to set
     */
    public void setFlattenedPdf(FirebirdFile flattenedPdf) {
        this.flattenedPdf = flattenedPdf;
    }

    /**
     * @return the plain text Comments.
     */
    @Transient
    public String getPlainTextComments() {
        return RichTextUtil.convertToPlainText(getComments());
    }

    /**
     * @return user entered free-form text.
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "comments")
    public String getComments() {
        return comments;
    }

    /**
     * @return the base name (i.e. no extension) to be used for files generated from this form.
     */
    @Transient
    public String getFormFileBaseName() {
        StringBuilder sb = new StringBuilder();
        sb.append(getInvestigator().getLastName());
        sb.append('_');
        sb.append(getInvestigator().getFirstName());
        sb.append('_');
        sb.append(getFormType().getFormTypeEnum().name());
        return sb.toString().toLowerCase(Locale.getDefault());
    }

    /**
     * @return the name of the file in progress.
     */
    @Transient
    public String getInProgressFileName() {
        return getFormFileBaseName() + PDF_EXTENSION;
    }

    /**
     * @param comments user entered free-form text.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the Optionality of the form in the current registration.
     */
    @Transient
    public final FormOptionality getFormOptionality() {
        FormOptionality optionality = getRegistration().getFormConfiguration().get(getFormType());
        if (optionality != null) {
            return optionality;
        } else {
            return FormOptionality.NONE;
        }
    }

    /**
     * Validates the current form content and updates its status. If Subclasses require further validation they should
     * override the validate method and call super.validate().
     *
     * @param resources resource bundle with validation messages included
     * @throws ValidationException if there are validation problems.
     */
    public final void checkSubmissionReadiness(ResourceBundle resources) throws ValidationException {
        ValidationResult result = new ValidationResult();
        validate(result, resources);
        updateFormStatus(result);
    }

    private void updateFormStatus(ValidationResult result) throws ValidationException {
        if (!getRegistration().isLockedForInvestigator()) {
            if (result.isValid()) {
                if (shouldUpdateStatus()) {
                    setFormStatus(FormStatus.COMPLETED);
                }
            } else {
                downgradeCompleted();
                throw new ValidationException(result);
            }
        }
    }

    private boolean shouldUpdateStatus() {
        return getRegistration().getLastSubmissionDate() == null
                || (!isReviewed() && getFormStatus() != FormStatus.REVISED);
    }

    private void downgradeCompleted() {
        if (getRegistration().getStatus() == RegistrationStatus.INCOMPLETE && shouldUpdateStatus()) {
            setFormStatus(FormStatus.INCOMPLETE);
        } else if (getFormStatus() == FormStatus.COMPLETED || getFormStatus() == FormStatus.SUBMITTED) {
            setFormStatus(FormStatus.IN_PROGRESS);
        }
    }

    /**
     * Performs the validation checks. Subclasses should override if necessary and call super.validate().
     *
     * @param result result to add validation failures to
     * @param resources resource bundle with validation messages included
     */
    protected void validate(ValidationResult result, ResourceBundle resources) {
        if (requiresActiveCurationStatus()) {
            validateNesCurationStatus(result, resources);
        }
    }

    /**
     * Checks whether entities and roles should be status ACTIVE in NES to be valid for signing.
     *
     * @return true if entities and roles must be ACTIVE
     */
    public boolean requiresActiveCurationStatus() {
        return Boolean.valueOf(System.getProperty("registration.validation.require.nes.status.active"));
    }

    /**
     * Create a Validation Failure object using the provided values.
     *
     * @param resources the Resource bundle to retrieve messages from.
     * @param key the Message key of the error.
     * @param arguments any optional argument replacements.
     * @return the Validation Failure object
     */
    public ValidationFailure createValidationFailure(ResourceBundle resources, String key, Object... arguments) {
        String messageTemplate = resources.getString(key);
        return new ValidationFailure(MessageFormat.format(messageTemplate, arguments));
    }

    private void validateNesCurationStatus(ValidationResult result, ResourceBundle resources) {
        for (Organization organization : getOrganizations()) {
            validateCurationStatus(organization, result, resources);
        }
        for (Person person : getPersons()) {
            validateCurationStatus(person, result, resources);
        }
    }

    private void validateCurationStatus(Organization organization, ValidationResult result, ResourceBundle resources) {
        if (CurationStatus.NULLIFIED.equals(organization.getCurationStatus())) {
            ValidationFailure failure = createValidationFailure(resources,
                    "validation.failures.organization.nullified", organization.getName(), getFormType().getName());
            addValidationFailure(result, failure, organization.getId());
        } else if (!CurationStatus.ACTIVE.equals(organization.getCurationStatus())) {
            ValidationFailure failure = createValidationFailure(resources, "validation.failure.uncurated",
                    organization.getName(), getFormType().getName(), organization.getCurationStatus().getDisplay());
            addValidationFailure(result, failure, organization.getId());
        }
    }

    /**
     * Adds the validation error to the validation result object and adds the entity id to the list of invalid entities.
     *
     * @param result validation result
     * @param failure validation failure
     * @param entityId invalid entity's ID
     */
    public void addValidationFailure(ValidationResult result, ValidationFailure failure, Long entityId) {
        result.addFailure(failure);
        getInvalidEntityIds().add(entityId);
    }

    private void validateCurationStatus(Person person, ValidationResult result, ResourceBundle resources) {
        if (CurationStatus.NULLIFIED.equals(person.getCurationStatus())) {
            result.addFailure(createValidationFailure(resources, "validation.failures.person.nullified",
                    person.getDisplayName(), getFormType().getName()));
            getInvalidEntityIds().add(person.getId());
        } else if (!CurationStatus.ACTIVE.equals(person.getCurationStatus())) {
            result.addFailure(createValidationFailure(resources, "validation.failure.uncurated",
                    person.getDisplayName(), getFormType().getName(), person.getCurationStatus().getDisplay()));
            getInvalidEntityIds().add(person.getId());
        } else if (person.isUpdatePending()) {
            result.addFailure(createValidationFailure(resources, "validation.failure.pending.nes.updates",
                    person.getDisplayName(), getFormType().getName()));
            getInvalidEntityIds().add(person.getId());
        }
    }

    /**
     * @return the file the sponsor will review.
     */
    @Transient
    public FirebirdFile getFileToReview() {
        return getFlattenedPdf();
    }

    /**
     * @return whether or not this form requires validation.
     */
    public boolean requiresValidation() {
        return !FormOptionality.NONE.equals(getFormOptionality());
    }

    /**
     * @return true if this form can be digitally signed.
     */
    @Transient
    public boolean isSignable() {
        return StringUtils.isNotEmpty(getFormType().getSigningField());
    }

    /**
     * @return true if this form has been digitally signed.
     */
    @Transient
    public boolean isSigned() {
        return getSignedPdf() != null;
    }

    /**
     * Default functionality for when this form is submitted. Forms can override functionality to provide for additional
     * actions that need to be performed when a submit occurs. Ex. Archiving data
     */
    public void submitForm() {
        if (shouldUpdateStatus()) {
            setFormStatus(FormStatus.SUBMITTED);
        }
    }

    /**
     * Default functionality for when this form is returned. Forms can override functionality to provide for additional
     * actions that need to be performed when a form is returned occurs. Ex. Removing archived data
     *
     * @return objects that should be removed from the database
     */
    Set<PersistentObject> returnForm() {
        Set<PersistentObject> objectsToDelete = Sets.newHashSet();
        FirebirdFile removedFlattenedPdf = getFlattenedPdf();
        FirebirdFile removedSignedPdf = getSignedPdf();
        setFlattenedPdf(null);
        setSignedPdf(null);
        if (shouldDeleteFile(removedFlattenedPdf)) {
            objectsToDelete.add(removedFlattenedPdf);
        }
        if (shouldDeleteFile(removedSignedPdf)) {
            objectsToDelete.add(removedSignedPdf);
        }
        return objectsToDelete;
    }

    boolean shouldDeleteFile(FirebirdFile file) {
        return file != null && getRegistration().getProfile().isOrphan(file);
    }

    /**
     * @return whether or not this form has a submittable status
     */
    @Transient
    public boolean isSubmittable() {
        return SUBMITTABLE_STATUSES.contains(getFormStatus());
    }

    /**
     * @return whether or not this form is currently reviewable by the sponsor
     */
    @Transient
    public boolean isCurrentlyReviewable() {
        return getRegistration().getStatus() == RegistrationStatus.IN_REVIEW
                && CURRENTLY_REVIEWABLE_STATUSES.contains(getFormStatus());
    }

    /**
     * @return whether or not this form is reviewed by the sponsor
     */
    @Transient
    public boolean isReviewed() {
        return REVIEWED_STATUSES.contains(getFormStatus());
    }

    /**
     * @return whether or not this form required to be reviewed by the sponsor.
     */
    @Transient
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    // This should be here
    public boolean isReviewRequired() {
        return true;
    }

    /**
     * @return All entity ids which have failed validation
     */
    @Transient
    public Set<Long> getInvalidEntityIds() {
        return invalidEntityIds;
    }

    void copyCommonData(AbstractRegistrationForm copyTo) {
        copyTo.setFormStatus(getFormStatus());
        copyTo.setFormStatusDate(getFormStatusDate());
        copyTo.setSignedPdf(getSignedPdf());
        copyTo.setFlattenedPdf(getFlattenedPdf());
        copyTo.setComments(getComments());
    }

    /**
     * Checks if the given file is referenced from this form. Subclasses should
     * override as appropriate.
     *
     * @param file check if this form references this file
     * @return true if referenced
     */
    boolean isFileReferenced(FirebirdFile file) {
        return file.equals(getFlattenedPdf()) || file.equals(getSignedPdf());
    }

    /**
     * @return whether or not to show the comments link for the investigator
     */
    @Transient
    public boolean isCommentsLinkToInvestigatorToBeDisplayed() {
        return !getPlainTextComments().isEmpty() && getFormStatus() != FormStatus.ACCEPTED
                && getFormStatus() != FormStatus.IN_REVIEW;
    }

    /**
     * @return true if this form consists of a collection of documents.
     */
    @Transient
    public boolean isCollectionOfDocuments() {
        return FORMS_WITH_COLLECTION_OF_DOCUMENTS.contains(getFormType().getFormTypeEnum());
    }

}
