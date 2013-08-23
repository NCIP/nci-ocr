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
package gov.nih.nci.firebird.web.action.sponsor.protocol;

import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.protocol.registration.review.ProtocolRegistrationReviewService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.web.action.AbstractProtocolRegistrationAction;

import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * Action class for handling the navigation and activities related to protocol registrations.
 */
@Namespace("/sponsor/protocol/review/ajax")
@InterceptorRef("protocolManagementStack")
public class ReviewRegistrationAjaxAction extends AbstractProtocolRegistrationAction {

    private static final long serialVersionUID = 1L;

    private static final Set<FormTypeEnum> DISPLAY_LINK_FOR_FORM_TYPES = EnumSet.of(
            FormTypeEnum.CV,
            FormTypeEnum.FINANCIAL_DISCLOSURE_FORM,
            FormTypeEnum.FORM_1572);

    private static final Set<FormTypeEnum> LINK_TO_ADDITIONAL_DOCUMENTS_FORM_TYPES = EnumSet.of(
            FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE,
            FormTypeEnum.ADDITIONAL_ATTACHMENTS);

    private final ProtocolRegistrationReviewService registrationReviewService;

    /**
     * @param registrationService the registration service
     * @param registrationReviewService the registration review service
     * @param profileService the profile service
     */
    @Inject
    public ReviewRegistrationAjaxAction(ProtocolRegistrationService registrationService,
            ProtocolRegistrationReviewService registrationReviewService, InvestigatorProfileService profileService) {
        super(registrationService, profileService);
        this.registrationReviewService = registrationReviewService;
    }

    @Override
    public void prepare() {
        super.prepare();
        setToInReviewIfAllFormsReviewed();
    }

    private void setToInReviewIfAllFormsReviewed() {
        if (getRegistration().getStatus() == RegistrationStatus.SUBMITTED && areAllFormsReviewed()) {
            getRegistration().setStatus(RegistrationStatus.IN_REVIEW);
            getRegistrationService().save(getRegistration());
        }
    }

    private boolean areAllFormsReviewed() {
        for (AbstractRegistrationForm form : getRegistration().getFormsForSponsorReview()) {
            if (!form.isReviewed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Accepts the Registration after review.
     *
     * @return the struts forward.
     */
    @Action(value = "acceptRegistration",
            results = @Result(location = "packet_complete.jsp"))
    public String acceptRegistration() {
        if (!getRegistration().isCompleteable()) {
            throw new IllegalArgumentException("Cannot Accept a Registration that is not ready to be completed!");
        }
        registrationReviewService.acceptRegistration(getCurrentUser(), getRegistration());
        if (isSponsorDelegateForRegistration()) {
            return null;
        }
        InvestigatorRegistration primaryRegistration = getRegistration().getPrimaryRegistration();
        if (primaryRegistration.isApprovable()) {
            return SUCCESS;
        }
        return null;
    }

    /**
     * Navigate to the Submitted Registration page.
     *
     * @return the struts forward.
     */
    @Action(value = "enter", results = { @Result(location = "review_registration.jsp"),
            @Result(name = INPUT, location = "../browse_protocol.jsp") })
    public String submittedRegistrationEnter() {
        if (getRegistration() == null) {
            return INPUT;
        }
        return SUCCESS;
    }

    /**
     * @return the serialized JSON list of forms for the registration.
     * @throws JSONException when serialization issues occur.
     */
    public String getRegistrationFormsJson() throws JSONException {
        return JSONUtil.serialize(getRegistrationFormListings(), null, null, false, true, false);
    }

    private Set<RegistrationFormListing> getRegistrationFormListings() {
        Set<RegistrationFormListing> listings = Sets.newHashSet();
        for (AbstractRegistrationForm form : getRegistration().getFormsForSponsorReview()) {
            listings.add(new RegistrationFormListing(form));
        }
        return listings;
    }

    /**
     * Table listing object for registration forms.
     */
    @SuppressWarnings("ucd")
    // needs to be protected for JSONUtil.serialize()
    protected final class RegistrationFormListing {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat(getText("date.format.timestamp"),
                Locale.getDefault());
        private final Long id;
        private final String formTypeDescription;
        private final FormStatus formStatus;
        private final boolean reviewRequired;
        private final boolean currentlyReviewable;
        private final String comments;
        private final boolean additionalDocumentsUploaded;
        private final String formStatusDateDisplay;
        private final boolean showLink;
        private final boolean linkToAddionalDocuments;

        /**
         * @param form registration form
         */
        RegistrationFormListing(AbstractRegistrationForm form) {
            this.id = form.getId();
            this.showLink = DISPLAY_LINK_FOR_FORM_TYPES.contains(form.getFormType().getFormTypeEnum());
            this.formTypeDescription = form.getFormType().getDescription();
            this.formStatus = form.getFormStatus();
            this.reviewRequired = form.isReviewRequired();
            this.currentlyReviewable = form.isCurrentlyReviewable();
            this.comments = form.getComments();
            this.additionalDocumentsUploaded = form.isAdditionalDocumentsUploaded();
            this.formStatusDateDisplay = dateFormat.format(form.getFormStatusDate());
            this.linkToAddionalDocuments = LINK_TO_ADDITIONAL_DOCUMENTS_FORM_TYPES.contains(form.getFormType()
                    .getFormTypeEnum());
        }

        /**
         * @return the id
         */
        public Long getId() {
            return id;
        }

        /**
         * @return the formTypeDescription
         */
        public String getFormTypeDescription() {
            return formTypeDescription;
        }

        /**
         * @return true if this form should provide a link to content.
         */
        @SuppressWarnings("PMD.BooleanGetMethodName")   // get reads better in this case
        public boolean getShowLink() {
            return showLink;
        }

        /**
         * @return the formStatus
         */
        public FormStatus getFormStatus() {
            return formStatus;
        }

        /**
         * @return the reviewRequired
         */
        public boolean isReviewRequired() {
            return reviewRequired;
        }

        /**
         * @return the currentlyReviewable
         */
        public boolean isCurrentlyReviewable() {
            return currentlyReviewable;
        }

        /**
         * @return the comments
         */
        public String getComments() {
            return comments;
        }

        /**
         * @return the additionalDocumentsUploaded
         */
        public boolean isAdditionalDocumentsUploaded() {
            return additionalDocumentsUploaded;
        }

        /**
         * @return the formStatusDateDisplay
         */
        public String getFormStatusDateDisplay() {
            return formStatusDateDisplay;
        }

        /**
         * @return the linkToAddionalDocuments
         */
        public boolean isLinkToAddionalDocuments() {
            return linkToAddionalDocuments;
        }
    }
}