/*
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

package gov.nih.nci.firebird.web.action.investigator.annual.registration.ajax;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

import gov.nih.nci.firebird.security.Authenticator;
import gov.nih.nci.firebird.security.CredentialsHandlerFactory;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationService;
import gov.nih.nci.firebird.service.annual.registration.RegistrationWithdrawalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.web.action.AbstractAnnualRegistrationAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

/**
 * Manages Withdrawing of Investigator Submissions.
 */
@Namespace("/investigator/annual/registration/ajax/withdraw")
@InterceptorRef("registrationManagementStack")
@Result(location = "withdraw_request.jsp")
public class WithdrawSubmissionAction extends AbstractAnnualRegistrationAction {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String comments;
    private final boolean requireHttpsToSign;
    private final CredentialsHandlerFactory credentialsHandlerFactory;
    private final RegistrationWithdrawalService withdrawalService;


    /**
     * @param annualRegistrationService annual registration service
     * @param profileService            profile service
     * @param withdrawalService         registration withdrawal service
     * @param credentialsHandlerFactory required for reauthentication
     * @param requireHttpsToSign        boolean value specifies if https is required
     */
    @Inject
    public WithdrawSubmissionAction(AnnualRegistrationService annualRegistrationService,
                                    InvestigatorProfileService profileService,
                                    RegistrationWithdrawalService withdrawalService,
                                    CredentialsHandlerFactory credentialsHandlerFactory,
                                    @Named("ca.keystore.require.https")
                                    Boolean requireHttpsToSign) {
        super(annualRegistrationService, profileService);
        this.withdrawalService = withdrawalService;
        this.requireHttpsToSign = requireHttpsToSign;
        this.credentialsHandlerFactory = credentialsHandlerFactory;
    }

    /**
     * Enter the Withdraw request dialog.
     *
     * @return the struts forward
     */
    @Action(value = "enterWithdraw")
    public String enterWithdraw() {
        return SUCCESS;
    }

    /**
     * @return withdraw request submission confirmation.
     */
    @Action(value = "submitWithdraw", results = { @Result(location = "withdraw_request_submitted_confirm.jsp"),
            @Result(name = INPUT, location = "withdraw_request.jsp") })
    @Validations(requiredFields =
        @RequiredFieldValidator(fieldName = "comments", key = "investigator.withdraw.error.comments.required"))
    public String submitWithdraw() {
        try {
            if (requireHttpsToSign && !checkSecureConnection()) {
                return returnActionError("login.insecure");
            } else if (!getCurrentUser().getBaseUsername().equalsIgnoreCase(getUsername())) {
                return returnActionError("authentication.invalid.credentials");
            } else {
                reauthenticate();
            }
        } catch (FailedLoginException e) {
            return returnActionError("authentication.invalid.credentials");
        } catch (LoginException e) {
            return returnActionError("authentication.error");
        }

        withdrawalService.requestWithdrawal(getCurrentUser(), getComments());
        return SUCCESS;
    }

    /**
     * Will re-authenticate user using the same IdP as the one used for the initial login.
     * @throws LoginException exception
     */
    private void reauthenticate() throws LoginException {
        String providerUrl = getCurrentGridSessionInformation().getIdentityProviderUrl();
        Authenticator authenticator = credentialsHandlerFactory.getAuthenticator(providerUrl);
        authenticator.authenticateUser(getUsername(), getPassword(), providerUrl);
    }

    /**
     * @return boolean indicating if the connection is secure
     */
    private boolean checkSecureConnection() {
        return getRequest().isSecure();
    }

    CredentialsHandlerFactory getCredentialsHandlerFactory() {
        return credentialsHandlerFactory;
    }

    /**
     * @return username value
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username Username field value
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password value
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password password field value
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return comments value
     */
    public String getComments() {
        return comments;
    }

    /**
     *
     * @param comments comments field value
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}
