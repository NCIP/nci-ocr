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

import gov.nih.nci.firebird.security.Authenticator;
import gov.nih.nci.firebird.security.CredentialsHandlerFactory;
import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.Certificate;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.registration.BaseRegistrationService;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Processor for completion actions.
 *
 * @param <T> registration type
 */
@SuppressWarnings("PMD.TooManyMethods")
//Contains broken down methods as well as utility class for displaying table data
public class CompletionActionProcessor<T extends AbstractRegistration> {

    /**
     * complete struts forward.
     */
    public static final String COMPLETE_REGISTRATION = "complete";
    /**
     * enter comments struts forward.
     */
    public static final String ENTER_COMMENTS = "enterComments";
    /**
     * signing disabled struts forward.
     */
    public static final String SIGNING_DISABLED = "signing_disabled";
    /**
     * investigator completion struts forward.
     */
    public static final String INVESTIGATOR_COMPLETION_SUCCESS = "investigator_completion_success";
    /**
     * coordinator completion struts forward.
     */
    public static final String COORDINATOR_COMPLETION_SUCCESS = "coordinator_completion_success";
    /**
     * protocol updated struts forward.
     */
    public static final String PROTOCOL_UPDATED = "protocol_updated";
    /**
     * Maximum character count for comments.
     */
    @SuppressWarnings("ucd")
    // called from JSP pages
    public static final int MAX_CHAR_COUNT = 500;
    private final BaseRegistrationService<T> registrationService;
    private final boolean requireHttpsToSign;
    private final AbstractRegistrationAction<T> action;
    private T registration;
    private final CredentialsHandlerFactory credentialsHandlerFactory;

    /**
     * @param registrationService registration service
     * @param credentialsHandlerFactory supports reauthentication of signer
     * @param action action
     * @param requireHttpsToSign is HTTPS required to sign
     */
    public CompletionActionProcessor(BaseRegistrationService<T> registrationService,
            CredentialsHandlerFactory credentialsHandlerFactory, AbstractRegistrationAction<T> action,
            boolean requireHttpsToSign) {
        this.registrationService = registrationService;
        this.credentialsHandlerFactory = credentialsHandlerFactory;
        this.action = action;
        this.requireHttpsToSign = requireHttpsToSign;
    }

    /**
     * @return struts forward.
     */
    public String submitRegistration() {
        if (!isRegistrationValid()) {
            return ActionSupport.INPUT;
        } else if (registration.getLastSubmissionDate() != null) {
            return ENTER_COMMENTS;
        } else {
            return COMPLETE_REGISTRATION;
        }
    }

    private boolean isRegistrationValid() {
        try {
            registrationService.prepareForSubmission(registration);
        } catch (ValidationException e) {
            action.handleValidationException(e);
            return false;
        }
        return true;
    }

    /**
     * @param comments comments to validate and clean
     * @return clean comments if valid. Otherwise, null.
     */
    public String validateAndCleanComments(String comments) {
        if (!action.validateRichTextSize(comments, MAX_CHAR_COUNT)) {
            return null;
        }
        return RichTextUtil.cleanRichText(comments);
    }

    /**
     * @param comments comments to set
     *
     * @return if registration is valid and the investigator is completing, returns struts forward to the signing page.
     *         If coordinator is completing, then sends an email to the investigator and returns the struts forward to
     *         the success dialog.
     */
    public String complete(String comments) {
        if (action.isUserAnApprovedCoordinatorForRegistrationsProfile() || action.isCtepSponsor()
                || action.isCtepSponsorDelegate()) {
            return submitToInvestigator(comments);
        } else {
            return investigatorComplete();
        }
    }

    private String submitToInvestigator(String comments) {
        setRegistrationComments(comments);
        registrationService.save(registration);
        registrationService.sendCoordinatorCompletedRegistrationEmail(action.getCurrentUser(), registration);
        return COORDINATOR_COMPLETION_SUCCESS;
    }

    private void setRegistrationComments(String comments) {
        if (action.isUserAnApprovedCoordinatorForRegistrationsProfile()) {
            registration.setCoordinatorComments(comments);
        } else {
            registration.setSponsorComments(comments);
        }
    }

    private String investigatorComplete() {
        if (!action.isVerifiedForRegistrationSigning()) {
            return SIGNING_DISABLED;
        } else if (!checkSecureConnection()) {
            return returnActionError("login.insecure");
        }
        return INVESTIGATOR_COMPLETION_SUCCESS;
    }

    private boolean checkSecureConnection() {
        return !requireHttpsToSign || action.getRequest().isSecure();
    }

    private String returnActionError(String errorKey) {
        action.addActionError(action.getText(errorKey));
        return ActionSupport.INPUT;
    }

    /**
     * @param username username
     * @param password password
     * @param comments comments
     * @return show signed document list.
     */
    public String sign(String username, String password, String comments) {
        try {
            if (!checkSecureConnection()) {
                return returnActionError("login.insecure");
            } else if (!action.getCurrentUser().getBaseUsername().equalsIgnoreCase(username)) {
                return returnActionError("authentication.invalid.credentials");
            } else {
                reauthenticate(username, password);
            }
        } catch (FailedLoginException e) {
            return returnActionError("authentication.invalid.credentials");
        } catch (LoginException e) {
            return returnActionError("authentication.error");
        }
        registration.setInvestigatorComments(comments);
        registrationService.signAndSubmit(registration,
                action.getCurrentUser(), password);
        return ActionSupport.SUCCESS;
    }

    private void reauthenticate(String username, String password) throws LoginException {
        String providerUrl = action.getCurrentGridSessionInformation().getIdentityProviderUrl();
        Authenticator authenticator = credentialsHandlerFactory.getAuthenticator(providerUrl);
        authenticator.authenticateUser(username, password, providerUrl);
    }

    /**
     * @return the registration forms and documents as a JSON representation.
     * @throws JSONException if the serialization fails.
     */
    public String getDocumentsJson() throws JSONException {
        return JSONUtil.serialize(getJsonModelDocuments());
    }

    private SortedSet<JsonModelDocument> getJsonModelDocuments() {
        SortedSet<JsonModelDocument> jsonModelDocuments = new TreeSet<JsonModelDocument>();
        addRegistrationForms(jsonModelDocuments);
        addHumanResearchCertificates(jsonModelDocuments);
        addFinancialDisclosureSupportingDocumentation(jsonModelDocuments);
        addAdditionalAttachments(jsonModelDocuments);
        return jsonModelDocuments;
    }

    private void addRegistrationForms(SortedSet<JsonModelDocument> jsonModelDocuments) {
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (form.isSignable()) {
                jsonModelDocuments.add(new JsonModelDocument(form));
            }
        }
    }

    private void addHumanResearchCertificates(SortedSet<JsonModelDocument> jsonModelDocuments) {
        if (registration instanceof AbstractProtocolRegistration) {
            AbstractProtocolRegistration protocolRegistration = (AbstractProtocolRegistration) registration;
            for (Certificate certificate : protocolRegistration.getHumanResearchCertificateForm().getCertificates()) {
                jsonModelDocuments.add(new JsonModelDocument(certificate.getFile(),
                        FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE));
            }
        }
    }

    private void addFinancialDisclosureSupportingDocumentation(SortedSet<JsonModelDocument> jsonModelDocuments) {
        for (FirebirdFile document : registration.getFinancialDisclosure().getSupportingDocumentation()) {
            jsonModelDocuments.add(new JsonModelDocument(document, FormTypeEnum.FINANCIAL_DISCLOSURE_FORM));
        }
    }

    private void addAdditionalAttachments(SortedSet<JsonModelDocument> jsonModelDocuments) {
        for (FirebirdFile attachment : registration.getAdditionalAttachmentsForm().getAdditionalAttachments()) {
            jsonModelDocuments.add(new JsonModelDocument(attachment, FormTypeEnum.ADDITIONAL_ATTACHMENTS));
        }
    }

    /**
     * @return the clean investigator comments
     */
    public String getCleanInvestigatorComments() {
        return RichTextUtil.cleanRichText(registration.getInvestigatorComments());
    }

    /**
     * @param registration the registration to set
     */
    public void setRegistration(T registration) {
        this.registration = registration;
    }

    /**
     * Exposes only data necessary on client side for registration document listings.
     */
    @SuppressWarnings("ucd")
    // needs to be protected for JSONUtil.serialize()
    protected final class JsonModelDocument implements Comparable<JsonModelDocument> {

        private static final String GROUP_NAME_PREFIX = "form.group.name.";
        private final Long id;
        private final String name;
        private final boolean signingRequired;
        private final boolean signed;
        private final String groupName;
        private final boolean registrationForm;

        JsonModelDocument(AbstractRegistrationForm form) {
            groupName = null;
            id = form.getId();
            name = form.getFormType().getDescription();
            signingRequired = form.isSignable();
            signed = form.isSigned();
            registrationForm = true;
        }

        JsonModelDocument(FirebirdFile file, FormTypeEnum formType) {
            groupName = action.getText(GROUP_NAME_PREFIX + formType.name());
            id = file.getId();
            name = file.getName();
            signingRequired = false;
            signed = false;
            registrationForm = false;
        }

        /**
         * @return the groupName
         */
        public String getParentForm() {
            return groupName;
        }

        /**
         * @return the form id.
         */
        public Long getId() {
            return id;
        }

        /**
         * @return the form name.
         */
        public String getName() {
            return name;
        }

        /**
         * @return is signing required for form.
         */
        public boolean isSigningRequired() {
            return signingRequired;
        }

        /**
         * @return true if this form has been digitally signed.
         */
        public boolean isSigned() {
            return signed;
        }

        /**
         * @return the registrationForm
         */
        public boolean isRegistrationForm() {
            return registrationForm;
        }

        @Override
        public int compareTo(JsonModelDocument compareToForm) {
            int comparisonValue = Boolean.valueOf(compareToForm.isSigningRequired()).compareTo(isSigningRequired());
            if (comparisonValue == 0) {
                comparisonValue = getName().compareTo(compareToForm.getName());
            }
            return comparisonValue;
        }
    }

}
