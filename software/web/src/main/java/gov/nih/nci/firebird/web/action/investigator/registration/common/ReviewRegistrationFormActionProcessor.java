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
package gov.nih.nci.firebird.web.action.investigator.registration.common;

import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AdditionalAttachmentsForm;
import gov.nih.nci.firebird.data.AnnualRegistrationForm1572;
import gov.nih.nci.firebird.data.FinancialDisclosure;
import gov.nih.nci.firebird.data.Form1572;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.HumanResearchCertificateForm;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.service.registration.review.BaseRegistrationReviewService;
import gov.nih.nci.firebird.web.action.FirebirdActionSupport;

import java.util.Collection;
import java.util.EnumSet;
import java.util.regex.Pattern;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Processor for review registration form actions.
 *
 * @param <T> registration type
 */
@SuppressWarnings("PMD.TooManyMethods")
// Methods broken down for enhanced clarity.
public class ReviewRegistrationFormActionProcessor<T extends AbstractRegistration> {

    /**
     * Maximum character count for comments.
     */
    @SuppressWarnings("ucd")
    // called from JSP pages
    public static final int MAX_CHAR_COUNT = 1000;
    private static final EnumSet<FormTypeEnum> FORM_TYPES_WITH_ADDITIONAL_DOCS = EnumSet.of(FormTypeEnum.FORM_1572,
            FormTypeEnum.CTEP_FORM_1572, FormTypeEnum.CTEP_FINANCIAL_DISCLOSURE_FORM,
            FormTypeEnum.FINANCIAL_DISCLOSURE_FORM, FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE,
            FormTypeEnum.ADDITIONAL_ATTACHMENTS);
    private static final EnumSet<FormTypeEnum> REVIEW_ONLY_ADDITIONAL_DOCS_FORM_TYPES = EnumSet
            .of(FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE);
    private AbstractRegistrationForm registrationForm = new AnnualRegistrationForm1572();
    private final BaseRegistrationReviewService<T> registrationReviewService;
    private T registration;
    private final FirebirdActionSupport action;

    /**
     * @param registrationReviewService registration review service
     * @param action action
     */
    public ReviewRegistrationFormActionProcessor(BaseRegistrationReviewService<T> registrationReviewService,
            FirebirdActionSupport action) {
        this.registrationReviewService = registrationReviewService;
        this.action = action;
    }

    /**
     * prepares the processor.
     *
     * @param registrationToSet registration
     */
    public void prepare(T registrationToSet) {
        setRegistration(registrationToSet);
        if (getRegistrationForm() != null && getRegistrationForm().getId() != null) {
            Long formId = getRegistrationForm().getId();
            setRegistrationForm(registration.findFormById(formId));
        } else {
            setRegistrationForm(null);
        }
    }

    /**
     * Navigate to the Submitted Registration page.
     *
     * @return the struts forward.
     */
    public String reviewCommentsEnter() {
        if (!registration.isCompleteable()) {
            return action.closeDialog();
        }
        return ActionSupport.SUCCESS;
    }

    /**
     * Rejects the Registration after review.
     *
     * @return the struts forward.
     */
    public String rejectRegistration() {
        if (!action.validateRichTextSize(registration.getSponsorComments(), MAX_CHAR_COUNT)) {
            return ActionSupport.INPUT;
        }
        registrationReviewService.rejectRegistration(registration,
                RichTextUtil.cleanRichText(registration.getSponsorComments()));
        return ActionSupport.SUCCESS;
    }

    /**
     * Shows the registration rejection confirmation dialog.
     *
     * @return the struts forward
     */
    public String showConfirmation() {
        if (!action.validateRichTextSize(registration.getSponsorComments(), MAX_CHAR_COUNT)) {
            return ActionSupport.INPUT;
        }
        return ActionSupport.SUCCESS;
    }

    /**
     * Sends an output stream of the requested form back to the user. If the registration or form are not currently
     * "In Review" then it will update those as well.
     *
     * @return SUCCESS
     */
    public String viewRegistrationForm() {
        promoteNecessaryStatuses();
        return ActionSupport.SUCCESS;
    }

    private void promoteNecessaryStatuses() {
        boolean registrationStatusChanged = promoteSubmittedRegistrationStatus();
        boolean formStatusChanged = promoteSubmittedFormStatus();
        if (registrationStatusChanged || formStatusChanged) {
            registrationReviewService.save(registration);
        }
    }

    private boolean promoteSubmittedRegistrationStatus() {
        if (registration.getStatus() == RegistrationStatus.SUBMITTED) {
            registration.setStatus(RegistrationStatus.IN_REVIEW);
            return true;
        }
        return false;
    }

    private boolean promoteSubmittedFormStatus() {
        if (isRegistrationInReview() && isFormReviewable()) {
            getRegistrationForm().setFormStatus(FormStatus.IN_REVIEW);
            return true;
        }
        return false;
    }

    private boolean isRegistrationInReview() {
        return registration.getStatus() == RegistrationStatus.IN_REVIEW;
    }

    private boolean isFormReviewable() {
        return getRegistrationForm().getFormStatus() == FormStatus.SUBMITTED
                || getRegistrationForm().getFormStatus() == FormStatus.REVISED;
    }

    /**
     * Accepts the Form after review.
     *
     * @return the struts forward.
     */
    public String acceptForm() {
        registrationReviewService.acceptForm(getRegistrationForm());
        return ActionSupport.NONE;
    }

    /**
     * Rejects the Form after review.
     *
     * @return the struts forward.
     */
    public String rejectForm() {
        if (!action.validateRichTextSize(getRegistrationForm().getComments(), MAX_CHAR_COUNT)
                || !action.validateRichTextNotEmpty(getRegistrationForm().getComments())) {
            return ActionSupport.INPUT;
        }

        registrationReviewService.rejectForm(getRegistrationForm(),
                RichTextUtil.cleanRichText(getRegistrationForm().getComments()));
        return action.closeDialog();
    }

    /**
     * Sets the form back to "In Review".
     *
     * @return the struts forward.
     */
    public String clearForm() {
        registrationReviewService.clearFormReviewStatus(getRegistrationForm());
        return ActionSupport.NONE;
    }

    /**
     * Navigate to the clinical labatory's registrations page.
     *
     * @return the struts forward.
     */
    public String additionalDocumentsEnter() {
        FormTypeEnum formType = getRegistrationForm().getFormType().getFormTypeEnum();
        if (registration == null || !FORM_TYPES_WITH_ADDITIONAL_DOCS.contains(formType)) {
            return ActionSupport.INPUT;
        }
        if (REVIEW_ONLY_ADDITIONAL_DOCS_FORM_TYPES.contains(formType)) {
            promoteNecessaryStatuses();
        }
        return formType.name();
    }

    /**
     * Returns the JSON list of additional documents for this form.
     *
     * @return JSON list of additional documents for this form.
     * @throws JSONException when serialization issues occur.
     */
    public String getAdditionalDocumentsJson() throws JSONException {
        Collection<?> additionalDocuments = getAdditionalDocuments();
        Collection<Pattern> excludes = Lists.newArrayList(Pattern.compile(".*\\.byteDataSource"),
                Pattern.compile(".*\\.originalCertificate"), Pattern.compile(".*\\.roles"),
                Pattern.compile(".*\\.certificates"));
        return JSONUtil.serialize(additionalDocuments, excludes, null, false, false);
    }

    private Collection<?> getAdditionalDocuments() {
        if (getRegistrationForm() instanceof Form1572) {
            return ((Form1572) getRegistrationForm()).getClinicalLabCertificates();
        } else if (getRegistrationForm() instanceof FinancialDisclosure) {
            return ((FinancialDisclosure) getRegistrationForm()).getSupportingDocumentation();
        } else if (getRegistrationForm() instanceof HumanResearchCertificateForm) {
            return ((HumanResearchCertificateForm) getRegistrationForm()).getCertificates();
        } else if (getRegistrationForm() instanceof AdditionalAttachmentsForm) {
            return ((AdditionalAttachmentsForm) getRegistrationForm()).getAdditionalAttachments();
        } else {
            throw new IllegalArgumentException("The provided form " + getRegistrationForm().getClass()
                    + " is not applicable to have additional documents!");
        }
    }

    /**
     * @return the registrationForm
     */
    public AbstractRegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    /**
     * @param registrationForm the registrationForm to set
     */
    public void setRegistrationForm(AbstractRegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    /**
     * Navigate to the Submitted Registration page.
     *
     * @return the struts forward.
     */
    public String commentsEnter() {
        return ActionSupport.SUCCESS;
    }

    /**
     * @param registration the registration to set
     */
    public void setRegistration(T registration) {
        this.registration = registration;
    }

    /**
     * @return the clean Rich Text Sponsor Comments.
     */
    public String getCleanSponsorComments() {
        return RichTextUtil.cleanRichText(registration.getSponsorComments());
    }

}
