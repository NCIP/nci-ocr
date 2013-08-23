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

import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.protocol.registration.review.ProtocolRegistrationReviewService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.web.action.AbstractProtocolRegistrationAction;
import gov.nih.nci.firebird.web.action.investigator.registration.common.ReviewRegistrationFormActionProcessor;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONException;

import com.google.inject.Inject;

/**
 * Action class for handling reviewal of registration forms.
 */
@Namespace("/sponsor/protocol/review/ajax")
@InterceptorRef(value = "protocolManagementStack")
@SuppressWarnings("PMD.TooManyMethods")
// Methods broken down for enhanced clarity.
public class ReviewRegistrationFormAction extends AbstractProtocolRegistrationAction {

    private static final long serialVersionUID = 1L;

    private ReviewRegistrationFormActionProcessor<AbstractProtocolRegistration> processor;

    /**
     * Constructor.
     *
     * @param registrationService the registration service
     * @param profileService the profile service
     * @param registrationReviewService registration review service
     */
    @Inject
    public ReviewRegistrationFormAction(ProtocolRegistrationService registrationService,
            InvestigatorProfileService profileService,
            ProtocolRegistrationReviewService registrationReviewService) {
        super(registrationService, profileService);
        processor = new ReviewRegistrationFormActionProcessor<AbstractProtocolRegistration>(registrationReviewService,
                this);
    }

    @Override
    public void prepare() {
        super.prepare();
        processor.prepare(getRegistration());
    }

    /**
     * Rejects the Registration after review.
     *
     * @return the struts forward.
     */
    @Action(value = "rejectRegistration", results = { @Result(name = INPUT, location = "review_comments.jsp"),
            @Result(name = SUCCESS, location = "registration_reject_success_dialog.jsp") })
    public String rejectRegistration() {
        return processor.rejectRegistration();
    }

    /**
     * Navigate to the Submitted Registration page.
     *
     * @return the struts forward.
     */
    @Action(value = "reviewCommentsEnter", results = { @Result(name = SUCCESS, location = "review_comments.jsp") })
    public String reviewCommentsEnter() {
        return processor.reviewCommentsEnter();
    }

    /**
     * Sends an output stream of the requested form back to the user. If the registration or form are not currently
     * "In Review" then it will update those as well.
     *
     * @return SUCCESS
     */
    @Action(value = "downloadForm", results = { @Result(name = SUCCESS, type = "chain", params = { "actionName",
            "downloadForm", "namespace", "/investigator/registration/ajax" }) })
    public String viewRegistrationForm() {
        return processor.viewRegistrationForm();
    }

    /**
     * Shows the registration rejection confirmation dialog.
     *
     * @return the struts forward
     */
    @Action(value = "showConfirmation", results = { @Result(name = INPUT, location = "review_comments.jsp"),
            @Result(name = SUCCESS, location = "registration_reject_confirm_dialog.jsp") })
    public String showConfirmation() {
        return processor.showConfirmation();
    }

    /**
     * Rejects the Form after review.
     *
     * @return the struts forward.
     */
    @Action(value = "rejectForm", results = { @Result(name = INPUT, location = "comments.jsp") })
    public String rejectForm() {
        return processor.rejectForm();
    }

    /**
     * Accepts the Form after review.
     *
     * @return the struts forward.
     */
    @Action("acceptForm")
    public String acceptForm() {
        return processor.acceptForm();
    }

    /**
     * Navigate to the clinical labatory's registrations page.
     *
     * @return the struts forward.
     */
    @Action(value = "additionalDocumentsEnter", results = {
            @Result(name = "FORM_1572", location = "registration_clinical_lab_certificates.jsp"),
            @Result(name = "FINANCIAL_DISCLOSURE_FORM",
                    location = "registration_finiancial_disclosure_supporting_documentation.jsp"),
            @Result(name = "HUMAN_RESEARCH_CERTIFICATE", location = "registration_human_research_certificates.jsp"),
            @Result(name = "ADDITIONAL_ATTACHMENTS", location = "registration_additional_attachments.jsp"),
            @Result(name = INPUT, location = "../browse_protocol.jsp") })
    public String additionalDocumentsEnter() {
        return processor.additionalDocumentsEnter();
    }

    /**
     * Sets the form back to "In Review".
     *
     * @return the struts forward.
     */
    @Action(value = "clearForm")
    public String clearForm() {
        return processor.clearForm();
    }

    /**
     * @return the registrationForm
     */
    public AbstractRegistrationForm getRegistrationForm() {
        return processor.getRegistrationForm();
    }

    /**
     * Returns the JSON list of additional documents for this form.
     *
     * @return JSON list of additional documents for this form.
     * @throws JSONException when serialization issues occur.
     */
    public String getAdditionalDocumentsJson() throws JSONException {
        return processor.getAdditionalDocumentsJson();
    }

    /**
     * Navigate to the Submitted Registration page.
     *
     * @return the struts forward.
     */
    @Action(value = "commentsEnter", results = { @Result(name = SUCCESS, location = "comments.jsp") })
    public String commentsEnter() {
        return processor.commentsEnter();
    }

    /**
     * @return the clean Rich Text Form Comments.
     */
    public String getCleanFormComments() {
        return RichTextUtil.cleanRichText(getRegistrationForm().getComments());
    }

    /**
     * @return the clean Rich Text Sponsor Comments.
     */
    public String getCleanSponsorComments() {
        return processor.getCleanSponsorComments();
    }

    /**
     * @param processor the processor to set
     */
    public void setProcessor(ReviewRegistrationFormActionProcessor<AbstractProtocolRegistration> processor) {
        this.processor = processor;
    }

}