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

import static com.google.common.base.Preconditions.*;
import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.CredentialType;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.web.action.investigator.registration.common.RegistrationTabActionProcessor;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;

import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;


import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Action used to display and respond to protocol registration invitations.
 */
@Namespace("/investigator/registration/ajax")
@Results(@Result(name = RegistrationTabAction.VIEW_OVERVIEW, location = RegistrationTabAction.VIEW_OVERVIEW_PAGE))
@InterceptorRef(value = "registrationManagementStack")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
// need to suppress UCDetector warnings multiple times
public class RegistrationTabAction extends AbstractRegistrationTabAction {

    private static final long serialVersionUID = 1L;

    private static final String RETURN_1572 = "FORM_1572";
    private static final String RETURN_CV = "CV";
    private static final String RETURN_DISCLOSURE = "FINANCIAL_DISCLOSURE_FORM";
    private static final String RETURN_H_R_CERT = "HUMAN_RESEARCH_CERTIFICATE";
    private static final String RETURN_ADDITIONAL_ATTACHMENTS = "ADDITIONAL_ATTACHMENTS";

    static final String VIEW_PROTOCOL_INFORMATION = "ProtocolInformation";
    @SuppressWarnings("ucd")
    // annotations access these
    static final String VIEW_OVERVIEW_PAGE = "view_overview.jsp";
    static final String VIEW_OVERVIEW = "OVERVIEW";

    private final GenericDataRetrievalService dataService;
    private FormType formType = new FormType();
    private RegistrationTabActionProcessor<AbstractProtocolRegistration> processor;

    /**
     * Creates an action instance.
     *
     * @param dataService data service
     * @param registrationService registration service
     * @param resources FIREBIRD resource bundle
     * @param profileService profile service
     */
    @Inject
    public RegistrationTabAction(ProtocolRegistrationService registrationService,
                                 GenericDataRetrievalService dataService, ResourceBundle resources,
                                 InvestigatorProfileService profileService) {
        super(registrationService, profileService, resources);
        this.dataService = dataService;
        this.processor = new RegistrationTabActionProcessor<AbstractProtocolRegistration>(registrationService, this);
    }

    @Override
    public void prepare() {
        super.prepare();
        if (formType.getId() != null) {
            setFormType(dataService.getPersistentObject(FormType.class, formType.getId()));
        }
        processor.setRegistration(getRegistration());
    }

    /**
     * Use the ID of the form that was passed in to determine the page to forward to.
     *
     * @return the view page.
     */
    @Action(value = "viewForm", results = {
            @Result(name = RETURN_1572, location = "fda1572/form.jsp"),
            @Result(name = RETURN_CV, location = "cv/cv.jsp"),
            @Result(name = RETURN_H_R_CERT,
                    type = "chain",
                    params = { "actionName", "enter", "namespace",
                               "/investigator/registration/ajax/humanresearchcertificate" }),
            @Result(name = RETURN_DISCLOSURE,
                    type = "chain",
                    params = { "actionName", "view", "namespace",
                               "/investigator/registration/ajax/financialdisclosure" }),
            @Result(name = RETURN_ADDITIONAL_ATTACHMENTS,
                    type = "chain",
                    params = { "actionName", "enter", "namespace",
                               "/investigator/registration/ajax/additionalattachments" }) })
    public String viewForm() {
        return processor.viewForm(getForm());
    }

    /**
     * Forward to the Overview tab for the registration.
     *
     * @return the struts forward.
     */
    @Action("viewOverview")
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public String viewOverview() {
        if (isRegistrationDeleted()) {
            return FirebirdUIConstants.RETURN_ACCESS_DENIED_ENTER;
        }
        if (!getRegistration().isLockedForInvestigator()
                && getRegistration().getStatus() != RegistrationStatus.RETURNED) {
            try {
                getRegistrationService().checkUnReviewedAndUnRevisedFormCompletionStatus(getRegistration());
            } catch (ValidationException e) {
                // Ignore Validation Exception, it is expected that Validation errors could occur at this time.
            }
        }
        return VIEW_OVERVIEW;
    }

    /**
     * Forward to the Protocol Information page for the registration.
     *
     * @return the struts forward.
     */
    @Action(value = "viewProtocolInformation", results = { @Result(name = VIEW_PROTOCOL_INFORMATION,
                                                                   location = "protocol_information_tab.jsp") })
    public String viewProtocolInformation() {
        if (getId(getRegistration()) == null) {
            return FirebirdUIConstants.RETURN_ACCESS_DENIED_ENTER;
        }
        return VIEW_PROTOCOL_INFORMATION;
    }

    /**
     * Forward to the Protocol Information page for the registration.
     *
     * @return the struts forward.
     */
    @Action("initiateRegistrationRevision")
    public String initiateRegistrationRevision() {
        checkArgument(getRegistration().isInvestigatorRegistration(),
                      "Sub investigator registrations cannot initiate a registration revision");
        getRegistrationService().initiateRevision((InvestigatorRegistration) getRegistration(), getCurrentUser());
        return NONE;
    }

    /**
     * Cancels a registration revision.
     *
     * @return the struts forward.
     */
    @Action("cancelRegistrationRevision")
    public String cancelRegistrationRevision() {
        checkArgument(getRegistration().isInvestigatorRegistration(),
                "Sub investigator registrations cannot initiate a registration revision");
        getRegistrationService().cancelRevision((InvestigatorRegistration) getRegistration(), getCurrentUser());
        return NONE;
    }

    /**
     * @return the JSON string of the protocol changes.
     * @throws JSONException if a serialization exception occurs.
     */
    public String getRevisionHistoryJson() throws JSONException {
        return JSONUtil.serialize(getRegistration().getProtocol().getRevisionHistory(),
                                  Lists.newArrayList(Pattern.compile(".*\\.protocol")), null, false, false);
    }

    /**
     * @param formType the FormType to set
     */
    public void setFormType(FormType formType) {
        this.formType = formType;
    }

    /**
     * @return the FormType
     */
    public FormType getFormType() {
        return formType;
    }

    /**
     * @return the current form
     */
    @Override
    public AbstractRegistrationForm getForm() {
        if (getFormType() != null && getRegistration() != null) {
            return getRegistration().getForm(getFormType());
        } else {
            return null;
        }
    }

    /**
     * @return the protocol
     */
    public String getProtocolAgentsDisplay() {
        return getRegistration().getProtocol().getAgentListForDisplay();
    }

    /**
     * Returns the credentials to display for a given type. If there are current credentials, these are returned,
     * otherwise expired credentials are shown and flagged.
     *
     * @param type get credentials of this type
     * @return list of credentials to display
     */
    @SuppressWarnings("ucd")
    // called from JSP pages
    public SortedSet<AbstractCredential<?>> getCvCredentials(CredentialType type) {
        return getRegistration().getProfile().getCredentials(type);
    }

    /**
     * @return if there are any expired credentials in the profile.
     */
    @SuppressWarnings("ucd")
    // called from JSP pages
    public boolean profileContainsExpiredCredentials() {
        InvestigatorProfile profile = getRegistration().getProfile();
        return CollectionUtils.exists(profile.getCredentials(), new Predicate() {

            @Override
            public boolean evaluate(Object object) {
                AbstractCredential<?> credential = (AbstractCredential<?>) object;
                return credential.isExpired();
            }
        });
    }

    /**
     * @return the clean Rich Text Form Comments.
     */
    public String getCleanFormComments() {
        if (getForm() == null) {
            return null;
        } else {
            return RichTextUtil.cleanRichText(getForm().getComments());
        }
    }

    /**
     * Navigate to the Submitted Registration page.
     *
     * @return the struts forward.
     */
    @Action(value = "enterComments", results = { @Result(name = SUCCESS, location = "comments.jsp") })
    public String enterComments() {
        return SUCCESS;
    }

    /**
     * @param processor the processor to set
     */
    @SuppressWarnings("ucd")
    // used to inject mock processor from tests
    void setProcessor(RegistrationTabActionProcessor<AbstractProtocolRegistration> processor) {
        this.processor = processor;
    }

}
