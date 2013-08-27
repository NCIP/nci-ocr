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
package gov.nih.nci.firebird.web.action;

import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.service.investigatorprofile.ProfileNesRefreshService;
import gov.nih.nci.firebird.service.organization.OrganizationSearchService;
import gov.nih.nci.firebird.service.person.PersonSearchService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.service.user.FirebirdUserService;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.common.Struts2UploadedFileInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.util.ServletContextAware;
import org.jboss.ejb3.common.proxy.plugins.async.AsyncUtils;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Provides base functionality for all FIREBIRD Struts 2 actions. Guice setter injection used
 * (as opposed to constructor) for ease of integration with subclasses.
 */
@SuppressWarnings({ "PMD.AbstractNaming", "PMD.TooManyMethods" })
// following Struts 2 base class naming convention of _Support.
// Base class exposes commonly used functionality and provides getters/setters for resources.
public abstract class FirebirdActionSupport extends ActionSupport implements ServletRequestAware, ServletContextAware {

    private static final long serialVersionUID = 1L;

    /**
     * Session attribute name for retrieving whether or not the current user's refrences have been refreshed from NES.
     */
    private static final String NES_REFERENCES_REFRESHED = "nesReferencesRefreshed";
    static final String PERSON_SEARCH_SERVICE_ATTRIBUTE_NAME = PersonSearchService.class.getName();

    private FirebirdUserService userService;
    private Provider<PersonSearchService> personSearchServiceProvider;
    private Provider<OrganizationSearchService> organizationSearchServiceProvider;
    private PersonSearchService personSearchService;
    private OrganizationSearchService organizationSearchService;
    private ProfileNesRefreshService profileRefreshService;
    private SponsorService sponsorService;
    private HttpServletRequest request;
    private ServletContext context;
    private FirebirdUser currentUser;
    private int minPaginationResults;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());

    /**
     * @param userService the FIREBIRD user service.
     */
    @Inject
    public void setUserService(FirebirdUserService userService) {
        this.userService = userService;
    }

    /**
     * @return the userService
     */
    public FirebirdUserService getUserService() {
        return userService;
    }

    /**
     * @param minPaginationResults the minPaginationResults to set
     */
    @Inject
    public void setMinPaginationResults(@Named("firebird.paginate.results.min") int minPaginationResults) {
        this.minPaginationResults = minPaginationResults;
    }

    /**
     * @return the minPaginationResults
     */
    public int getMinPaginationResults() {
        return minPaginationResults;
    }

    /**
     * @param personSearchServiceProvider the personSearchServiceProvider to set
     */
    @Inject
    public void setPersonSearchServiceProvider(Provider<PersonSearchService> personSearchServiceProvider) {
        this.personSearchServiceProvider = personSearchServiceProvider;
    }

    /**
     * Returns a <code>PersonSearchService</code> that is bound to the current web session.
     *
     * @return the search service
     */
    protected PersonSearchService getPersonSearchService() {
        if (personSearchService == null) {
            setPersonSearchService(retrievePersonSearchService());
        }
        return personSearchService;
    }

    private PersonSearchService retrievePersonSearchService() {
        PersonSearchService service = (PersonSearchService)
                getSessionAttribute(PERSON_SEARCH_SERVICE_ATTRIBUTE_NAME);
        if (service == null) {
            service = personSearchServiceProvider.get();
            getSession(true).setAttribute(PERSON_SEARCH_SERVICE_ATTRIBUTE_NAME, service);
        }
        return service;
    }

    /**
     * For rare circumstances where bean may have been invalidated, replaces the 
     * search service with a new instance.
     * 
     * @return the refreshed service
     */
    public PersonSearchService refreshPersonSearchService() {
        getSession(true).setAttribute(PERSON_SEARCH_SERVICE_ATTRIBUTE_NAME, null);
        return getPersonSearchService();
    }

    /**
     * @param personSearchService the personSearchService to set
     */
    public void setPersonSearchService(PersonSearchService personSearchService) {
        this.personSearchService = personSearchService;
    }

    /**
     * @param organizationSearchServiceProvider the personSearchServiceProvider to set
     */
    @Inject
    public void setOrganizationSearchServiceProvider(
            Provider<OrganizationSearchService> organizationSearchServiceProvider) {
        this.organizationSearchServiceProvider = organizationSearchServiceProvider;
    }

    /**
     * Returns a <code>PersonSearchService</code> that is bound to the current web session.
     *
     * @return the search service
     */
    protected OrganizationSearchService getOrganizationSearchService() {
        if (organizationSearchService == null) {
            setOrganizationSearchService(organizationSearchServiceProvider.get());
        }
        return organizationSearchService;
    }

    /**
     * @param organizationSearchService the organizationSearchService to set
     */
    public void setOrganizationSearchService(OrganizationSearchService organizationSearchService) {
        this.organizationSearchService = organizationSearchService;
    }

    @Inject
    public void setProfileRefreshService(ProfileNesRefreshService profileRefreshService) {
        this.profileRefreshService = profileRefreshService;
    }

    @Inject
    void setSponsorService(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    /**
     * @return Sponsor service
     */
    protected SponsorService getSponsorService() {
        return sponsorService;
    }

    /**
     * @return information from the grid identity provider about the current user or null if not logged in.
     */
    public UserSessionInformation getCurrentGridSessionInformation() {
        return (UserSessionInformation) getSessionAttribute(FirebirdConstants.USER_SESSION_INFORMATION);
    }

    /**
     * @return the fully-distinguished username or null if not logged in.
     */
    protected String getCurrentUsername() {
        UserSessionInformation userSessionInformation = getCurrentGridSessionInformation();
        if (userSessionInformation != null) {
            return userSessionInformation.getFullyQualifiedUsername();
        } else {
            return null;
        }
    }

    /**
     * @param attributeName the name of the session attribute
     * @return the Object from the session.
     */
    protected Object getSessionAttribute(String attributeName) {
        HttpSession session = getSession(false);
        if (session != null) {
            return session.getAttribute(attributeName);
        } else {
            return null;
        }
    }

    /**
     * @param create if true, a new session will be created if one does not exist.
     * @return the HTTPSession from the request.
     */
    protected HttpSession getSession(boolean create) {
        if (getRequest() != null) {
            return getRequest().getSession(create);
        } else {
            return null;
        }
    }

    /**
     * @return info for the current user.
     */
    public FirebirdUser getCurrentUser() {
        if (currentUser == null && getCurrentGridSessionInformation() != null) {
            currentUser = getUserService().getUserInfo(getCurrentGridSessionInformation());
        }
        if (currentUser != null && getSessionAttribute(NES_REFERENCES_REFRESHED) == null) {
            AsyncUtils.mixinAsync(profileRefreshService).refreshNesReferences(currentUser.getActiveProfiles());
            getSession(true).setAttribute(NES_REFERENCES_REFRESHED, true);
        }
        return currentUser;
    }

    @Override
    public final void setServletRequest(HttpServletRequest theRequest) {
        this.request = theRequest;
    }

    /**
     * @return the current request.
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @return the current request.
     */
    protected ServletContext getContext() {
        return context;
    }

    @Override
    public final void setServletContext(ServletContext aContext) {
        this.context = aContext;
    }

    /**
     * Reports validation failures back to the user.
     *
     * @param validationException contains information about the failed validation.
     * @return input indicator
     */
    public String handleValidationException(ValidationException validationException) {
        return handleValidationException(validationException, null);
    }

    /**
     * Reports validation failures back to the user.
     *
     * @param validationException contains information about the failed validation.
     * @param keyBase a base key to be prefixed to field error keys.
     * @return input indicator
     */
    protected String handleValidationException(ValidationException validationException, String keyBase) {
        String prefix = getKeyPrefix(keyBase);
        for (ValidationFailure failure : validationException.getResult().getFailures()) {
            handleValidationFailure(failure, prefix);
        }
        return INPUT;
    }

    @SuppressWarnings({ "PMD.UseStringBufferForStringAppends" })
    private String getKeyPrefix(String keyBase) {
        String prefix = keyBase;
        if (StringUtils.isNotEmpty(keyBase) && !StringUtils.endsWith(keyBase, ".")) {
            prefix += ".";
        }
        return prefix;
    }

    /**
     * Add messages to the action.
     * @param failure error to add.
     * @param prefix The Prefix to be appended to field keys.
     */
    private void handleValidationFailure(ValidationFailure failure, String prefix) {
        if (failure.isFieldError()) {
            String failureKey = getFailureKey(failure.getFieldKey(), prefix);
            addFieldError(failureKey, failure.getMessage());
        } else {
            addActionError(failure.getMessage());
        }
    }

    private String getFailureKey(String fieldKey, String prefix) {
        if (StringUtils.isNotBlank(prefix)) {
            return prefix + fieldKey;
        }
        return fieldKey;
    }

    /**
     * Turn the Date object into a MM/yyyy string.
     *
     * @param date the Date to format
     * @return the formatted Date string
     */
    protected String formatDate(Date date) {
        return date == null ? null : dateFormat.format(date);
    }

    /**
     * @param dialogId id of the dialog to close.
     * @return {@link FirebirdUIConstants#RETURN_CLOSE_DIALOG}
     */
    protected String closeDialog(String dialogId) {
        return closeDialog(dialogId, true);
    }

    private String closeDialog(String dialogId, boolean refresh) {
        getRequest().setAttribute(FirebirdUIConstants.NAME_DIALOG_ID, dialogId);
        getRequest().setAttribute(FirebirdUIConstants.NAME_TABSET, "tabwrapper");
        getRequest().setAttribute(FirebirdUIConstants.RELOAD, refresh);

        return FirebirdUIConstants.RETURN_CLOSE_DIALOG;
    }

    /**
     * Send the return to CLOSE_DIALOG but make sure that no reload occurs. This is useful if you are sending back
     * action errors and no updates were made.
     *
     * @return {@link FirebirdUIConstants#RETURN_CLOSE_DIALOG}
     */
    protected String closeDialogNoRefresh() {
        return closeDialog(null, false);
    }

    /**
     * @return {@link FirebirdUIConstants#RETURN_CLOSE_DIALOG}
     */
    public String closeDialog() {
        return closeDialog(null);
    }

    /**
     * Types to replace the client's content type with a more reliable one.
     * @param fileInfo uploaded file
     */
    public void resolveContentType(Struts2UploadedFileInfo fileInfo) {
        if (fileInfo != null && fileInfo.getData() != null) {
            fileInfo.resolveContentType(context);
        }
    }

    /**
     * Validates to make sure a rich text block is under the necessary character limit.
     *
     * @param text the text to validate
     * @param maxCharCount Maximum # of characters
     * @return if the text was valid or not.
     */
    public boolean validateRichTextSize(String text, int maxCharCount) {
        int textSize = RichTextUtil.getRichTextSize(text);
        if (textSize > maxCharCount) {
            String[] args = {String.valueOf(maxCharCount), Integer.toString(textSize) };
            addActionError(getText("error.rich.text.size", args));
            return false;
        }
        return true;
    }

    /**
     * Validates to make sure rich text isn't empty.
     *
     * @param text the text to validate
     * @return if the text was valid or not.
     */
    public boolean validateRichTextNotEmpty(String text) {
        int textSize = RichTextUtil.getRichTextSize(text);
        if (textSize == 0) {
            addActionError(getText("registration.review.form.rejection.comments.required"));
            return false;
        }
        return true;
    }

    /**
     *  adds error text from an error key.
     * @param errorKey property key value
     * @return ActionSupport.INPUT
     */
    public String returnActionError(String errorKey) {
        addActionError(getText(errorKey));
        return ActionSupport.INPUT;
    }

    /**
     * @return the Grid Grouper group names for the current user
     */
    public Set<String> getCurrentUserGroupNames() {
        return getCurrentGridSessionInformation().getGroupNames();
    }

    /**
     * @return true if the user is an investigator
     */
    public boolean isInvestigator() {
        return getCurrentUser().isInvestigator()
                && isInGroup(UserRoleType.INVESTIGATOR);
    }

    private boolean isInGroup(UserRoleType roleType) {
        return getCurrentUserGroupNames().contains(roleType.getGroupName());
    }

    /**
     * @return true if the user is an investigator for CTEP
     */
    public boolean isCtepInvestigator() {
        return getCurrentUser().isInvestigator()
                && isInGroup(UserRoleType.CTEP_INVESTIGATOR);
    }

    /**
     * @return true if the user is an investigator for CTEP
     */
    public boolean isVerifiedCtepInvestigator() {
        return getCurrentUser().isInvestigator()
                && isInGroup(UserRoleType.CTEP_VERIFIED_INVESTIGATOR);
    }

    /**
     * @return true if the user is a registration coordinator
     */
    public boolean isRegistrationCoordinator() {
        return getCurrentUser().isRegistrationCoordinator()
                && isInGroup(UserRoleType.REGISTRATION_COORDINATOR);
    }

    /**
     * @return true if the user is a registration coordinator for CTEP
     */
    public boolean isCtepRegistrationCoordinator() {
        return getCurrentUser().isRegistrationCoordinator()
                && isInGroup(UserRoleType.CTEP_REGISTRATION_COORDINATOR);
    }

    /**
     * @return true if the user is a sponsor for CTEP
     */
    public boolean isCtepSponsor() {
        return isInGroup(UserRoleType.CTEP_SPONSOR);
    }

    /**
     * @return true if the user is a sponsor delegate for CTEP
     */
    public boolean isCtepSponsorDelegate() {
        return isInGroup(UserRoleType.CTEP_SPONSOR_DELEGATE);
    }

    /**
     * @return true if the user is a verified sponsor
     */
    public boolean isVerifiedSponsor() {
        return getCurrentUser().isSponsorRepresentative()
                && getCurrentUserGroupNames().contains(UserRoleType.SPONSOR.getVerifiedGroupName());
    }

    /**
     * @return true if the user is a verified sponsor delegate
     */
    public boolean isVerifiedSponsorDelegate() {
        return getCurrentUser().isSponsorDelegate()
                && getCurrentUserGroupNames().contains(UserRoleType.SPONSOR_DELEGATE.getVerifiedGroupName());
    }

    /**
     * @param object object
     * @return the ID of the object or null if the object is null
     */
    protected Long getId(PersistentObject object) {
        return object == null ? null : object.getId();
    }

    void setCurrentUser(FirebirdUser user) {
        this.currentUser = user;
    }

}
