/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a organization or an entity, and all other entities that control,
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
package gov.nih.nci.firebird.web.action.user.registration.flow;

import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.PrimaryOrganizationType;
import gov.nih.nci.firebird.data.State;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.account.AccountManagementService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.organization.InvalidatedOrganizationException;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Page action for handling the Organization Selection page of the flow.
 */
@Namespace("/user/registration/flow/primaryOrgSelection")
@Results({
        @Result(name = OrganizationSelectionPageFlowAction.ORG_SEARCH,
                location = "../organization_selection_search.jsp"),
        @Result(name = ActionSupport.INPUT, location = "../organization_selection_fields.jsp") })
public class OrganizationSelectionPageFlowAction extends AbstractPageFlowAction {
    private static final long serialVersionUID = 1L;

    static final String CREATE_NEW = "NEW";
    static final String ORG_SEARCH = "search";
    private static final String ORGANIZATION_FIELD_PREFIX = "accountConfigurationData.primaryOrganization";

    private String selectedOrganizationExternalId;
    private String navigationOption;
    private final StateLookupService stateLookup;
    private final CountryLookupService countryLookup;
    private List<Country> countries;
    private List<State> states;

    /**
     * @param stateLookup .
     * @param countryLookup .
     * @param accountService .
     */
    @Inject
    public OrganizationSelectionPageFlowAction(
            StateLookupService stateLookup,
            CountryLookupService countryLookup,
            AccountManagementService accountService) {
        super(accountService);
        this.stateLookup = stateLookup;
        this.countryLookup = countryLookup;
    }

    @Override
    public void prepare() {
        super.prepare();
        states = stateLookup.getAll();
        countries = countryLookup.getAll();
        selectedOrganizationExternalId = getExistingOrganizationId();
    }

    /**
     * Overrides the default nextStep Action to add validation for the organization being saved.
     *
     * @return the result of saving the data to the session object.
     */
    @Actions({
            @Action(value = "nextStep", results = { @Result(type = "chain", params = { "actionName", "enterNextStep",
                    "namespace", "/user/registration/nav" }) }),
            @Action(value = "gotoStep", results = { @Result(type = "chain", params = { "actionName", "enterFlowStep",
                    "namespace", "/user/registration/nav" }) }) })
    @Validations(customValidators = { @CustomValidator(type = "hibernate",
            fieldName = "accountConfigurationData.primaryOrganization.organization", parameters = {
                    @ValidationParameter(name = "resourceKeyBase", value = "organization"),
                    @ValidationParameter(name = "excludes", value = "externalId") }) },
            fieldExpressions = {
                @FieldExpressionValidator(
                fieldName = "accountConfigurationData.primaryOrganization.organization.postalAddress.stateOrProvince",
                expression = "!accountConfigurationData.primaryOrganization || "
                 + "accountConfigurationData.primaryOrganization.organization.postalAddress.stateOrProvinceValid",
                            key = "stateOrProvince.required"),
                @FieldExpressionValidator(
                fieldName = "accountConfigurationData.primaryOrganization.type",
                expression = "selectedOrganizationExternalId != null || "
                 + "accountConfigurationData.primaryOrganization.type != null",
                key = "organization.type.required")
                })
    @Override
    public String saveAndProceedNext() {
        try {
            String strutsForward = super.saveAndProceedNext();
            if (StringUtils.isEmpty(selectedOrganizationExternalId)) {
                validatePrimaryOrganization();
            }
            return strutsForward;
        } catch (ValidationException e) {
            return handleValidationException(e, ORGANIZATION_FIELD_PREFIX);
        }
    }

    private void validatePrimaryOrganization() throws ValidationException {
        getOrganizationService().validate(getAccountConfigurationData().getPrimaryOrganization().getOrganization(),
                OrganizationRoleType.PRIMARY_ORGANIZATION,
                getAccountConfigurationData().getPrimaryOrganization().getType());
    }

    /**
     * Enter action navigating between showing the fields of data or the search capabilities.
     *
     * @return the struts forward
     */
    @Override
    @Action("enter")
    public String enterAction() {
        checkAccountConfigurationData();
        if (!getAccountConfigurationData().hasPrimaryOrganization() || ORG_SEARCH.equals(navigationOption)) {
            return ORG_SEARCH;
        } else {
            return INPUT;
        }
    }

    @Override
    protected String performSave() {
        String strutsForward = SUCCESS;
        if (StringUtils.isNotEmpty(navigationOption)) {
            strutsForward = handleNavigation();
        } else if (StringUtils.isNotEmpty(selectedOrganizationExternalId)) {
            strutsForward = handleExistingOrganization();
        } else {
            removeReturnLinkIfInvalid();
        }
        return strutsForward;
    }

    private String handleNavigation() {
        if (CREATE_NEW.equals(navigationOption)) {
            getAccountConfigurationData().setPrimaryOrganization(new PrimaryOrganization(new Organization(), null));
        } else if (ORG_SEARCH.equals(navigationOption)) {
            clearPreviouslySelectedOrg();
            return ORG_SEARCH;
        }
        return SUCCESS;
    }

    private void clearPreviouslySelectedOrg() {
        if (getSelectedOrganizationExternalId() == null) {
            getAccountConfigurationData().setPrimaryOrganization(null);
        }
    }

    private String handleExistingOrganization() {
        if (!StringUtils.equals(getExistingOrganizationId(), selectedOrganizationExternalId)) {
            try {
                Organization organization = getOrganizationService().getByExternalId(
                        getSelectedOrganizationExternalId());
                PrimaryOrganizationType type = getOrganizationService().getPrimaryOrganizationType(organization);
                getAccountConfigurationData().setPrimaryOrganization(new PrimaryOrganization(organization, type));
            } catch (InvalidatedOrganizationException e) {
                addActionError(getText("organization.search.selected.organization.unavailable"));
                return INPUT;
            }
        }
        return SUCCESS;
    }

    private String getExistingOrganizationId() {
        if (getAccountConfigurationData().hasPrimaryOrganization()) {
            return getAccountConfigurationData().getPrimaryOrganization().getExternalId();
        } else {
            return null;
        }
    }

    private void removeReturnLinkIfInvalid() {
        if (getAccountConfigurationData().hasPrimaryOrganization()) {
            if (getAccountConfigurationData().getPrimaryOrganization().getType() == null) {
                getFlowController().removeVisitedStep(RegistrationFlowStep.VERIFICATION);
            } else {
                try {
                    validatePrimaryOrganization();
                } catch (ValidationException e) {
                    getFlowController().removeVisitedStep(RegistrationFlowStep.VERIFICATION);
                }
            }
        }
    }

    /**
     * @return the selectedOrganizationExternalId
     */
    public String getSelectedOrganizationExternalId() {
        return selectedOrganizationExternalId;
    }

    /**
     * @param selectedOrganizationExternalId the selectedOrganizationExternalId to set
     */
    public void setSelectedOrganizationExternalId(String selectedOrganizationExternalId) {
        this.selectedOrganizationExternalId = selectedOrganizationExternalId;
    }

    /**
     * @param navigationOption the navigationOption to set
     */
    public void setNavigationOption(String navigationOption) {
        this.navigationOption = navigationOption;
    }

    /**
     * @return a List of States.
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

}
