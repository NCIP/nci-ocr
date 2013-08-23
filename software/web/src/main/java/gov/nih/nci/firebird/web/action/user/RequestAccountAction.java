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
package gov.nih.nci.firebird.web.action.user;

import gov.nih.nci.firebird.cagrid.GridAuthenticationService;
import gov.nih.nci.firebird.cagrid.GridInvocationException;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.State;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.service.account.AccountConfigurationData;
import gov.nih.nci.firebird.service.account.AccountManagementService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.sponsor.SponsorService;
import gov.nih.nci.firebird.web.action.FirebirdActionSupport;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.cagrid.gaards.dorian.federation.TrustedIdentityProvider;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Allows new users to request a new login account.
 */
@Namespace("/user")
@InterceptorRef("firebirdNoDisclaimerStack")
public class RequestAccountAction extends FirebirdActionSupport implements Preparable {

    private static final long serialVersionUID = 1L;
    static final String INVALID_CREDENTIALS_MESSAGE_KEY = "authentication.invalid.credentials";
    static final String GRID_ERROR_MESSAGE_KEY = "authentication.error";
    private AccountConfigurationData newAccount;
    private final AccountManagementService accountService;
    private final StateLookupService stateLookup;
    private final CountryLookupService countryLookup;
    private final SponsorService sponsorService;
    private final GridAuthenticationService gridAuthService;
    private List<Country> countries;
    private List<State> states;
    private Set<Organization> sponsorOrganizations;
    private Set<String> selectedSponsorExternalIds = Sets.newHashSet();
    private Set<String> selectedSponsorDelegateExternalIds = Sets.newHashSet();
    private List<TrustedIdentityProvider> identityProviders;

    /**
     * Creates a new instance.
     *
     * @param accountService used to send the account request
     * @param stateLookup used to list states on account form
     * @param countryLookup country lookup service.
     * @param sponsorService sponsor service.
     * @param gridAuthService grid authentication service.
     */
    @Inject
    public RequestAccountAction(AccountManagementService accountService, StateLookupService stateLookup,
            CountryLookupService countryLookup, SponsorService sponsorService,
            GridAuthenticationService gridAuthService) {
        this.accountService = accountService;
        this.stateLookup = stateLookup;
        this.countryLookup = countryLookup;
        this.sponsorService = sponsorService;
        this.gridAuthService = gridAuthService;
    }

    @Override
    public void prepare() {
        states = stateLookup.getAll();
        countries = countryLookup.getAll();
        sponsorOrganizations = sponsorService.getSponsorsWithProtocolRegistrations();
        if (getNewAccount() == null) {
            initializeNewAccount();
        }
    }

    private void initializeNewAccount() {
        AccountConfigurationData account = new AccountConfigurationData();
        account.setPerson(new Person());
        account.getPerson().getPostalAddress().setCountry(FirebirdConstants.US_COUNTRY_CODE);
        setNewAccount(account);
    }

    /**
     * @return show the registration form.
     */
    @Action(value = "requestAccountEnter", results = @Result(location = "request_account.jsp"))
    public String requestAccountEnter() {
        return SUCCESS;
    }

    /**
     * @return show the registration request confirmation page.
     */
    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "newAccount.person.phoneNumber", key = "phoneNumber.required"),
            @RequiredFieldValidator(fieldName = "newAccount.primaryOrganization.organization.name",
            key = "primaryOrganization.required") },
    customValidators = {
            @CustomValidator(type = "hibernate", fieldName = "newAccount.person", parameters = {
                            @ValidationParameter(name = "resourceKeyBase", value = "account"),
                            @ValidationParameter(name = "excludes", value = "externalId") }) },
            fieldExpressions = {
                @FieldExpressionValidator(fieldName = "newAccount.person.postalAddress.stateOrProvince",
                expression = "newAccount.person.postalAddress.country != 'USA' || "
                           + "(newAccount.person.postalAddress.stateOrProvince != null &&"
                           + "newAccount.person.postalAddress.stateOrProvince.trim() != '')",
                key = "stateOrProvince.required"),
                @FieldExpressionValidator(fieldName = "newAccount.username",
                expression = "!newAccount.existingLdapAccount || "
                           + "(newAccount.username != null &&"
                           + "newAccount.username.trim() != '')",
                key = "username.required"),
                @FieldExpressionValidator(fieldName = "newAccount.password",
                expression = "!newAccount.existingLdapAccount || "
                           + "(newAccount.password != null &&"
                           + "newAccount.password.trim() != '')",
                key = "password.required") },
            regexFields = { @RegexFieldValidator(fieldName = "newAccount.username",
                key = "username.contains.invalid.characters",
                    regexExpression = "[a-zA-Z0-9_-]*") }
    )
    @Action(value = "requestAccount", results = { @Result(location = "request_account_confirmation.jsp"),
            @Result(name = ActionSupport.INPUT, location = "request_account.jsp") })
    public String requestAccount() {
        populateAccoutRequestSponsorRoles();
        accountService.requestAccount(getNewAccount());
        return SUCCESS;
    }

    /**
     * Validate method which is called before requestAccount().
     */
    @SuppressWarnings("ucd")
    // automatically called before requestAccount()
    public void validateRequestAccount() {
        validateRoleSelected();
        validateAuthenticationSuccessful();
    }

    private void validateRoleSelected() {
        if (getNewAccount().getRoles().isEmpty() && getSelectedSponsorExternalIds().isEmpty()
                && getSelectedSponsorDelegateExternalIds().isEmpty()) {
            addActionError(getText("user.registration.role.selection.error"));
        }
    }

    private void validateAuthenticationSuccessful() {
        if (getNewAccount().isExistingLdapAccount()) {
            try {
                UserSessionInformation gridAccount = getGridAccount();
                if (gridAccount == null) {
                    addActionError(getText(INVALID_CREDENTIALS_MESSAGE_KEY));
                }
            } catch (GridInvocationException e) {
                addActionError(getText(GRID_ERROR_MESSAGE_KEY));
            }
        }
    }

    private UserSessionInformation getGridAccount() throws GridInvocationException {
        return gridAuthService.authenticateUser(getNewAccount().getUsername(), getNewAccount().getPassword(),
                getNewAccount().getIdentityProviderUrl());
    }

    private void populateAccoutRequestSponsorRoles() {
        getNewAccount().getSponsorOrganizations().addAll(getSelectedSponsors(false));
        getNewAccount().getDelegateOrganizations().addAll(getSelectedSponsors(true));
    }

    private Set<Organization> getSelectedSponsors(boolean delegate) {
        Set<Organization> sponsors = Sets.newHashSet();
        Set<String> sponsorIds = getSelectedSponsorExternalIds(delegate);
        if (sponsorIds != null) {
            for (Organization sponsor : getSponsorOrganizations()) {
                if (sponsorIds.contains(sponsor.getExternalId())) {
                    sponsors.add(sponsor);
                }
            }
        }
        return sponsors;
    }

    private Set<String> getSelectedSponsorExternalIds(boolean delegate) {
        if (delegate) {
            return getSelectedSponsorDelegateExternalIds();
        } else {
            return getSelectedSponsorExternalIds();
        }
    }

    /**
     * @return the newAccount
     */
    public AccountConfigurationData getNewAccount() {
        return newAccount;
    }

    /**
     * @param newAccount the newAccount to set
     */
    public void setNewAccount(AccountConfigurationData newAccount) {
        this.newAccount = newAccount;
    }

    /**
     * @return the list of US states
     */
    public List<State> getStates() {
        return states;
    }

    /**
     * @return the list of all countries.
     */
    public List<Country> getCountries() {
        return countries;
    }

    /**
     * @return the sponsorOrganizations
     */
    public Set<Organization> getSponsorOrganizations() {
        return sponsorOrganizations;
    }

    /**
     * @return the Set of non sponsor roles that are available for the current user to select.
     */
    public EnumSet<UserRoleType> getAvailableNonSponsorRoles() {
        EnumSet<UserRoleType> roleTypes = EnumSet.of(UserRoleType.INVESTIGATOR,
                UserRoleType.REGISTRATION_COORDINATOR);
        return roleTypes;
    }

    /**
     * @return the selectedSponsorExternalIds
     */
    public Set<String> getSelectedSponsorExternalIds() {
        return selectedSponsorExternalIds;
    }

    /**
     * @param selectedSponsorExternalIds the selectedSponsorExternalIds to set
     */
    public void setSelectedSponsorExternalIds(Set<String> selectedSponsorExternalIds) {
        this.selectedSponsorExternalIds = selectedSponsorExternalIds;
    }

    /**
     * @return the selectedSponsorDelegateExternalIds
     */
    public Set<String> getSelectedSponsorDelegateExternalIds() {
        return selectedSponsorDelegateExternalIds;
    }

    /**
     * @param selectedSponsorDelegateExternalIds the selectedSponsorDelegateExternalIds to set
     */
    public void setSelectedSponsorDelegateExternalIds(Set<String> selectedSponsorDelegateExternalIds) {
        this.selectedSponsorDelegateExternalIds = selectedSponsorDelegateExternalIds;
    }

    /**
     * @return the identityProviders
     */
    public List<TrustedIdentityProvider> getIdentityProviders() {
        if (identityProviders == null) {
            identityProviders = gridAuthService.getIdentityProviders();
        }
        return identityProviders;
    }
}
