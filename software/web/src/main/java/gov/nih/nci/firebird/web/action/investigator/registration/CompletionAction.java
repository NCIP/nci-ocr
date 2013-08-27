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
package gov.nih.nci.firebird.web.action.investigator.registration;

import static
gov.nih.nci.firebird.web.action.investigator.registration.common.CompletionActionProcessor.COMPLETE_REGISTRATION;
import static
gov.nih.nci.firebird.web.action.
investigator.registration.common.CompletionActionProcessor.COORDINATOR_COMPLETION_SUCCESS;
import static
gov.nih.nci.firebird.web.action.investigator.registration.common.CompletionActionProcessor.ENTER_COMMENTS;
import static
gov.nih.nci.firebird.web.action.
investigator.registration.common.CompletionActionProcessor.INVESTIGATOR_COMPLETION_SUCCESS;
import static
gov.nih.nci.firebird.web.action.investigator.registration.common.CompletionActionProcessor.PROTOCOL_UPDATED;
import static
gov.nih.nci.firebird.web.action.investigator.registration.common.CompletionActionProcessor.SIGNING_DISABLED;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.security.CredentialsHandlerFactory;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.web.action.AbstractProtocolRegistrationAction;
import gov.nih.nci.firebird.web.action.investigator.registration.common.CompletionActionProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Registration Completion action.
 */
@Namespace("/investigator/registration/ajax")
@InterceptorRef(value = "registrationManagementStack")
@Result(name = PROTOCOL_UPDATED, location = "protocol_updated_warning.jsp")
@SuppressWarnings("PMD.TooManyStaticImports")
// importing enums
public class CompletionAction extends AbstractProtocolRegistrationAction {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String comments;
    private CompletionActionProcessor<AbstractProtocolRegistration> processor;

    /**
     * Creates an action instance.
     *
     * @param registrationService registration service
     * @param credentialsHandlerFactory supports reauthentication of signer
     * @param profileService profile service
     * @param requireHttpsToSign is HTTPS required to digitally sign
     */
    @Inject
    public CompletionAction(ProtocolRegistrationService registrationService,
            CredentialsHandlerFactory credentialsHandlerFactory, InvestigatorProfileService profileService,
            @Named("ca.keystore.require.https")
            Boolean requireHttpsToSign) {
        super(registrationService, profileService);
        processor = new CompletionActionProcessor<AbstractProtocolRegistration>(registrationService,
                credentialsHandlerFactory, this, BooleanUtils.toBoolean(requireHttpsToSign));
    }

    @Override
    public void prepare() {
        super.prepare();
        if (getRegistration() != null && StringUtils.isNotBlank(getRegistration().getInvestigatorComments())) {
            setComments(getRegistration().getInvestigatorComments());
        }
        processor.setRegistration(getRegistration());
        /*
         * No clue why but without this here, retrieving the Additional Documents to be displayed in getDocumentsJson()
         * causes a Lazy load exception and "Connection is not associated with a managed connection" error
         */
        preLoadAdditionalAttachments();
    }

    private void preLoadAdditionalAttachments() {
        if (getRegistration() != null) {
            getRegistration().getAdditionalAttachmentsForm().isAdditionalDocumentsUploaded();
        }
    }

    /**
     * @return struts forward.
     */
    @Action(value = "submitRegistration", results = {
            @Result(name = COMPLETE_REGISTRATION, type = "chain", params = { "actionName", COMPLETE_REGISTRATION }),
            @Result(name = ENTER_COMMENTS, location = "resubmitComments.jsp"),
            @Result(name = INPUT, location = "validation.jsp") })
    public String submitRegistration() {
        if (isProtocolUpdated()) {
            return PROTOCOL_UPDATED;
        } else {
            return processor.submitRegistration();
        }
    }

    /**
     * @return the struts forward.
     */
    @Action(value = "submitComments", results = {
            @Result(name = COMPLETE_REGISTRATION, type = "chain", params = { "actionName", COMPLETE_REGISTRATION }),
            @Result(name = ENTER_COMMENTS, location = "resubmitComments.jsp") })
    public String submitComments() {
        String cleanComments = processor.validateAndCleanComments(getComments());
        if (cleanComments == null) {
            return ENTER_COMMENTS;
        } else {
            setComments(cleanComments);
            return COMPLETE_REGISTRATION;
        }
    }

    /**
     * @return if registration is valid and the investigator is completing, returns struts forward to the signing page.
     *         If coordinator is completing, then sends an email to the investigator and returns the struts forward to
     *         the success dialog.
     */
    @Action(value = COMPLETE_REGISTRATION, results = {
            @Result(name = INVESTIGATOR_COMPLETION_SUCCESS, location = "sign.jsp"),
            @Result(name = COORDINATOR_COMPLETION_SUCCESS, location = "submit_to_investigator_success.jsp"),
            @Result(name = SIGNING_DISABLED, location = "signing_disabled.jsp"),
            @Result(name = INPUT, location = "validation.jsp") })
    public String complete() {
        return processor.complete(getComments());
    }

    /**
     * @return signing page if registration is valid, or show validation error if any.
     */
    @Action(value = "confirmSubmitToInvestigatorEnter",
            results = @Result(location = "submit_to_investigator_confirm.jsp"))
    public String confirmSubmitToInvestigatorEnter() {
        if (isProtocolUpdated()) {
            return PROTOCOL_UPDATED;
        }
        return SUCCESS;
    }

    /**
     * @return show signed document list.
     */
    @Action(value = "sign", results = { @Result(name = SUCCESS, location = "documents.jsp"),
            @Result(name = INPUT, location = "sign.jsp") })
    public String sign() {
        return processor.sign(getUsername(), getPassword(), getComments());
    }

    /**
     * @return authentication password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password login authentication password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return authentication username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username authentication username (not the fully qualified username)
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the registration forms and documents as a JSON representation.
     * @throws JSONException if the serialization fails.
     */
    public String getDocumentsJson() throws JSONException {
        return processor.getDocumentsJson();
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @return the clean comments
     */
    public String getCleanComments() {
        return processor.getCleanInvestigatorComments();
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    void setProcessor(CompletionActionProcessor<AbstractProtocolRegistration> processor) {
        this.processor = processor;
    }

}
